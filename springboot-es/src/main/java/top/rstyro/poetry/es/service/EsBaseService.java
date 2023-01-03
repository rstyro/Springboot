package top.rstyro.poetry.es.service;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import top.rstyro.poetry.es.base.EsResult;
import top.rstyro.poetry.es.index.EsBaseIndex;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Slf4j
public class EsBaseService<T extends EsBaseIndex> {
    private Class<T> indexClass;
    T t;

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public void setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @SneakyThrows
    @PostConstruct
    private void initialize(){
        indexClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        t= indexClass.getDeclaredConstructor().newInstance();
    }


    /**
     * 添加文档
     * @param doc 文档
     * @return boolean
     * @throws IOException
     */
    @SneakyThrows
    public boolean saveDoc(T doc){
        IndexRequest indexRequest = getSaveRequest(doc);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return true;
    }

    /**
     * 获取文档
     *
     * @param id
     * @return
     */
    public Map<String, Object> getDocMapById(String id) throws IOException {
        GetRequest request = new GetRequest(t.getIndexName(), id);
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        Map<String, Object> docMap = response.getSourceAsMap();
        docMap.put("_id",response.getId());
        return docMap;
    }

    /**
     * 获取文档
     *
     * @param id
     * @return
     */
    public T getDocById(String id) throws IOException {
        GetRequest request = new GetRequest(t.getIndexName(), id);
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        T result = JSON.parseObject(response.getSourceAsString(), indexClass);
        if(null!=result){
            result.set_id(response.getId());
        }
        return result;
    }

    /**
     * 批量新增
     *
     * @param list
     * @return
     * @throws IOException
     */
    public boolean batchSaveDoc(List<T> list) throws IOException {
        if (list == null || list.size() < 1) return false;
        BulkRequest request = new BulkRequest();
        for (T item : list) {
            IndexRequest saveRequest = getSaveRequest(item);
            request.add(saveRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        if(bulk.hasFailures()){
            log.error("batchSave error msg={}",bulk.buildFailureMessage());
        }
        return !bulk.hasFailures();
    }

    private IndexRequest getSaveRequest(T item){
        IndexRequest indexRequest = new IndexRequest(item.getIndexName());
        if(!StringUtils.isEmpty(item.get_id())){
            indexRequest.id(item.get_id());
            indexRequest.routing(item.get_id());
            indexRequest.opType(DocWriteRequest.OpType.INDEX);
        }
        indexRequest.source(JSON.toJSONString(item.set_id(null)), XContentType.JSON);
        return indexRequest;
    }

    /**
     * 更新文档
     *
     * @param id
     * @param data
     * @return
     */
    public boolean updateDocById(String id, T data) throws IOException {
        UpdateRequest request = new UpdateRequest(data.getIndexName(), id).doc(JSON.toJSONString(data), XContentType.JSON);
        UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        return true;
    }

    /**
     * Script更新数字字段
     * @param id
     * @param fieldName
     * @param oprateNum
     * @return
     * @throws IOException
     */
    public boolean updateFieldNum(String id,String fieldName,int oprateNum) throws IOException {
        UpdateRequest request = new UpdateRequest(t.getIndexName(),id).script(new Script("ctx._source."+fieldName+" += "+oprateNum));
        UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        return update.getShardInfo().getFailed()==0;
    }

    /**
     * 批量更新
     *
     * @param list
     * @return
     */
    public boolean batchUpdateDoc(List<T> list) throws IOException {
        if (list == null || list.size() < 1) return false;
        BulkRequest request = new BulkRequest();
        for (T item : list) {
            UpdateRequest updateRequest = new UpdateRequest(item.getIndexName(), item.get_id()).doc(JSON.toJSONString(item), XContentType.JSON);
            request.add(updateRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    /**
     * 获取Id 列表
     */
    public EsResult<T> searchIds(String... ids) throws IOException {
        EsResult<T> result = new EsResult<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(ids)).size(10000);
        SearchRequest searchRequest = new SearchRequest().indices(t.getIndexName()).source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        parseResult(result,response);
        return result;
    }

    @SneakyThrows
    public EsResult<T> search(SearchSourceBuilder sourceBuilder) {
        EsResult<T> result = new EsResult<>();
        SearchRequest request = new SearchRequest();
        request.source(sourceBuilder);
        request.indices(t.getIndexName());
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        parseResult(result,response);
        return result;
    }

    public void parseResult(EsResult<T> result,SearchResponse response){
        List<T> records = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            if (hit.getSourceAsMap() == null) {
                continue;
            }
            T index = Convert.convert(indexClass, hit.getSourceAsMap());
            index.set_id(hit.getId());
            if (hit.getHighlightFields() != null) {
                Map<String, List<String>> tempHighLightMap = new HashMap<>();
                for (HighlightField oneResult : hit.getHighlightFields().values()) {
                    List<String> tempStrList = new LinkedList<>();
                    for (Text fragment : oneResult.getFragments()) {
                        tempStrList.add(fragment.toString());
                    }
                    tempHighLightMap.put(oneResult.getName(), tempStrList);
                }
                index.setHighlight(tempHighLightMap);
            }
            records.add(index);
        }
        if (response.getHits() != null) {
            result.setTotal(response.getHits().getTotalHits().value);
        } else {
            result.setTotal(0);
        }
        if (response.getAggregations() != null && response.getAggregations().get("_count") != null) {
            Terms terms = response.getAggregations().get("_count");
            result.setTotal(terms.getSumOfOtherDocCounts());
        }
        result.setTook(response.getTook().getMillis());
        result.setRecords(records);
        result.setAggregation(response.getAggregations());
        result.setSuggest(response.getSuggest());
    }


}
