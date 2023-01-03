package top.rstyro.poetry.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.rstyro.poetry.commons.ApiException;
import top.rstyro.poetry.commons.ApiExceptionCode;
import top.rstyro.poetry.commons.Const;
import top.rstyro.poetry.dto.SearchAggsDto;
import top.rstyro.poetry.dto.SearchDto;
import top.rstyro.poetry.dto.SearchFilterDto;
import top.rstyro.poetry.es.base.EsResult;
import top.rstyro.poetry.es.index.PoetryIndex;
import top.rstyro.poetry.es.service.impl.PoetryEsService;
import top.rstyro.poetry.es.vo.AggregationVo;
import top.rstyro.poetry.es.vo.EsSearchResultVo;
import top.rstyro.poetry.es.vo.TermAggregationVo;
import top.rstyro.poetry.service.IPoetryService;
import top.rstyro.poetry.util.ContextUtil;
import top.rstyro.poetry.util.LambdaUtil;
import top.rstyro.poetry.vo.FlyFlowerVo;
import top.rstyro.poetry.vo.SearchDetailVo;
import top.rstyro.poetry.vo.SearchVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PoetryServiceImpl implements IPoetryService {

    @Value("${spring.profiles.active}")
    private String env;


    private PoetryEsService poetryEsService;

    @Autowired
    public void setPoetryEsService(PoetryEsService poetryEsService) {
        this.poetryEsService = poetryEsService;
    }

    @Override
    public EsSearchResultVo<SearchVo> getList(SearchDto dto) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String kw = dto.getKw();
        if (StringUtils.hasLength(kw)) {
            BoolQueryBuilder keywordBool = QueryBuilders.boolQuery();
            keywordBool.should(QueryBuilders.matchQuery(LambdaUtil.getFieldName(PoetryIndex::getTitle), kw));
            keywordBool.should(QueryBuilders.matchQuery(LambdaUtil.getFieldName(PoetryIndex::getContent), kw));
            keywordBool.should(QueryBuilders.matchQuery(LambdaUtil.getFieldName(PoetryIndex::getAuthor), kw));
            boolQuery.must(keywordBool);
        }
        // 过滤项
        if (!ObjectUtils.isEmpty(dto.getFilters())) {
            SearchFilterDto filters = dto.getFilters();
            if (!ObjectUtils.isEmpty(filters.getTags())) {
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
//                filterBool.must(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getTags), filters.getTags()));
                filters.getTags().stream().forEach(i->{
                    filterBool.must(QueryBuilders.termQuery(LambdaUtil.getFieldName(PoetryIndex::getTags), i));
                });
                boolQuery.must(filterBool);
            }
            if (!ObjectUtils.isEmpty(filters.getDynastyList())) {
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getDynasty), filters.getDynastyList()));
                boolQuery.must(filterBool);
            }
            if (!ObjectUtils.isEmpty(filters.getTypeList())) {
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getType), filters.getTypeList()));
                boolQuery.must(filterBool);
            }

            if (!ObjectUtils.isEmpty(filters.getAuthorList())) {
                BoolQueryBuilder filterBool = QueryBuilders.boolQuery();
                filterBool.should(QueryBuilders.termsQuery(LambdaUtil.getFieldName(PoetryIndex::getAuthor) + ".keyword", filters.getAuthorList()));
                boolQuery.must(filterBool);
            }
        }
        searchSourceBuilder.query(boolQuery);
        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // * 全部字段
        highlightBuilder.field(LambdaUtil.getFieldName(PoetryIndex::getTitle));
        highlightBuilder.field(LambdaUtil.getFieldName(PoetryIndex::getAuthor));
        highlightBuilder.field(LambdaUtil.getFieldName(PoetryIndex::getContent));
        highlightBuilder.fragmentSize(200);
        searchSourceBuilder.highlighter(highlightBuilder);
        // 聚类
        if (!ObjectUtils.isEmpty(dto.getAggsList())) {
            List<SearchAggsDto> aggsList = dto.getAggsList();
            aggsList.stream().forEach(aggs -> {
                searchSourceBuilder.aggregation(AggregationBuilders.terms(aggs.getKey()).field(aggs.getKey()).size(aggs.getSize()).shardSize(1000));
            });
        }
        // 是否需要结果集
        if (dto.getNeedRecords()) {
            setPageParams(searchSourceBuilder);

        } else {
            searchSourceBuilder.size(0);
        }
        if ("dev".equals(env)) {
            log.info("ES-SQL={}", searchSourceBuilder.toString());
        }
        EsResult<PoetryIndex> response = poetryEsService.search(searchSourceBuilder);
        EsSearchResultVo<SearchVo> resultVo = new EsSearchResultVo<>();
        resultVo.setTook(response.getTook());
        resultVo.setTotal(response.getTotal());
        List<SearchVo> list = new ArrayList<>();
        List<PoetryIndex> records = response.getRecords();
        if (!ObjectUtils.isEmpty(records)) {
            records.stream().forEach(i -> {
                SearchVo vo = new SearchVo();
                BeanUtil.copyProperties(i, vo);
                List<String> contentHighs = i.getHighlight().get(LambdaUtil.getFieldName(PoetryIndex::getContent));
                if(!ObjectUtils.isEmpty(contentHighs)){
                    contentHighs.stream().forEach(c->{
                        String sourceC = c.replace("<em>", "").replace("</em>", "");
                        // 把高亮的句子 替换原文
                        List<String> newContents = vo.getContent().stream().map(cc -> cc.replace(sourceC, c)).collect(Collectors.toList());
                        vo.setContent(newContents);
                    });
                    // 内容排序，高亮在前，截取前三个即可
                    List<String> content = vo.getContent();
                    Collections.sort(content, (c1, c2) -> c1.contains("<em>")?-1:(c2.contains("<em>")?1:0));
                    vo.setContent(content.subList(0,Math.min(content.size(),3)));
                }
                List<String> titleHighs = i.getHighlight().get(LambdaUtil.getFieldName(PoetryIndex::getTitle));
                if(!ObjectUtils.isEmpty(titleHighs)){
                    vo.setTitle(titleHighs.get(0));
                }
                List<String> auHighs = i.getHighlight().get(LambdaUtil.getFieldName(PoetryIndex::getAuthor));
                if(!ObjectUtils.isEmpty(auHighs)){
                    vo.setAuthor(auHighs.get(0));
                }
                list.add(vo);
            });
        }
        resultVo.setRecords(list);
        // 聚类解析
        Aggregations aggregation = response.getAggregation();
        if (!ObjectUtils.isEmpty(aggregation)) {
            dto.getAggsList().stream().forEach(a -> {
                ParsedTerms parsedTerms = aggregation.get(a.getKey());
                if (!ObjectUtils.isEmpty(parsedTerms)) {
                    List<TermAggregationVo> aggregationVoList = new ArrayList<>();
                    AtomicLong sum = new AtomicLong(0);
                    parsedTerms.getBuckets().forEach(bucket -> {
                        TermAggregationVo termAggregationVo = new TermAggregationVo();
                        termAggregationVo.setKey(bucket.getKeyAsString());
                        termAggregationVo.setDocCount(bucket.getDocCount());
                        sum.addAndGet(bucket.getDocCount());
                        aggregationVoList.add(termAggregationVo);
                    });
                    resultVo.addAggregation(new AggregationVo<TermAggregationVo>().setKey(a.getKey()).setList(aggregationVoList).setSumDoc(sum.get()));
                }

            });
        }
        return resultVo;
    }

    // 设置分页
    public void setPageParams(SearchSourceBuilder searchSourceBuilder) {
        int from = (ContextUtil.getPageNo() - 1) * ContextUtil.getPageSize();
        if (from > Const.MAX_RESULT || (from + ContextUtil.getPageSize()) > Const.MAX_RESULT) {
            throw new ApiException(ApiExceptionCode.ES_OVER_MAX_RESULT);
        }
        searchSourceBuilder.from(from).size(ContextUtil.getPageSize());
        // 显示总条数
        searchSourceBuilder.trackTotalHits(true);
    }

    @SneakyThrows
    @Override
    public SearchDetailVo getDetail(String id) {
        SearchDetailVo vo = new SearchDetailVo();
        PoetryIndex docById = poetryEsService.getDocById(id);
        BeanUtil.copyProperties(docById, vo);
        return vo;
    }

    @Override
    public EsSearchResultVo<FlyFlowerVo> getFlyFlower(String text) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchPhraseQuery(LambdaUtil.getFieldName(PoetryIndex::getContent), text));
        searchSourceBuilder.query(boolQuery);
        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        String fieldName = LambdaUtil.getFieldName(PoetryIndex::getContent);
        highlightBuilder.field(fieldName);
        highlightBuilder.fragmentSize(500);
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.fetchSource(new String[]{"title", "author"}, null);
        // 分页
        setPageParams(searchSourceBuilder);
        EsResult<PoetryIndex> response = poetryEsService.search(searchSourceBuilder);
        List<FlyFlowerVo> resultList = new ArrayList<>();
        List<PoetryIndex> records = response.getRecords();
        EsSearchResultVo<FlyFlowerVo> resultVo = new EsSearchResultVo<>();
        resultVo.setTook(response.getTook()).setTotal(response.getTotal());
        if (!ObjectUtils.isEmpty(records)) {
            records.stream().forEach(i -> {
                FlyFlowerVo flyFlowerVo = new FlyFlowerVo();
                flyFlowerVo.setId(i.get_id()).setAuthor(i.getAuthor()).setTitle(i.getTitle()).set_id(i.get_id());
                List<String> list = i.getHighlight().get(fieldName);
                if(!ObjectUtils.isEmpty(list)){
                    flyFlowerVo.setContent(list.get(0));
                    resultList.add(flyFlowerVo);
                }else {
                    log.info("有毒，检索到但是没高亮，id={},text={}",i.get_id(),text);
                }
            });
        }
        resultVo.setRecords(resultList);
        return resultVo;
    }

}
