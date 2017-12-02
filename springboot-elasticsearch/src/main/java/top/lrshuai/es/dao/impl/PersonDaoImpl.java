package top.lrshuai.es.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import top.lrshuai.es.dao.PersonDao;
import top.lrshuai.es.entity.Person;

@Component
public class PersonDaoImpl implements PersonDao{

	@Autowired
    private TransportClient transportClient;
	
	private Logger log = Logger.getLogger(getClass());
	//索引名称（数据库名）
	private String index = "test";
	
	//类型名称（表名）
	private String type = "person";
	
	/**
	 * 保存
	 */
	@Override
	public String save(Person person) {
		 try {
			 XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
			builder.field("name", person.getName());
			builder.field("age", person.getAge());
			builder.field("sex", person.getSex());
			builder.field("birthday", person.getBirthday());
			builder.field("introduce", person.getIntroduce());
			builder.endObject();
			IndexResponse response = this.transportClient.prepareIndex(index, type)
					.setSource(builder).get();
			return response.getId();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 更新
	 */
	@Override
	public String update(Person person) {
		UpdateRequest request = new UpdateRequest(index, type, person.getId());
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
            if (person.getName() != null) {
                builder.field("name", person.getName());
            }
            if (person.getSex() != null) {
            	builder.field("sex", person.getSex());
            }
            if (person.getIntroduce() != null) {
            	builder.field("introduce", person.getIntroduce());
            }
            if (person.getBirthday() != null) {
            	builder.field("birthday", person.getBirthday());
            }
            if (person.getAge() > 0) {
            	builder.field("age", person.getAge());
            }
            builder.endObject();
            request.doc(builder);
            UpdateResponse response = transportClient.update(request).get();
            return response.getId();
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
        return null;
	}
	
	@Override
	public String deltele(String id) {
		DeleteResponse response = transportClient.prepareDelete(index, type, id).get();
		return response.getId();
	}
	
	@Override
	public Object find(String id) {
		GetResponse response =transportClient.prepareGet(index, type, id).get();
		System.out.println("response="+response);
		Map<String, Object> result = response.getSource();
		if(result != null) {
			result.put("_id", response.getId());
		}
		return result;
	}
	
	@Override
	public Object query(Person person) {
		List<Map<String,Object>> result =  new ArrayList<>();;
		try {
			 BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
	        if (person.getName() != null) {
//	            boolBuilder.must(QueryBuilders.matchQuery("name", person.getName()));
	            boolBuilder.should(QueryBuilders.matchQuery("name", person.getName()));
	        }
	        if (person.getIntroduce() != null) {
//	            boolBuilder.must(QueryBuilders.matchQuery("introduce", person.getIntroduce()));
	            boolBuilder.should(QueryBuilders.matchQuery("introduce", person.getIntroduce()));
	        }
	        
	        //大于age,小于age+10
	        if(person.getAge() > 0) {
	        	RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
	        	rangeQuery.from(person.getAge());
	        	rangeQuery.to(person.getAge()+10);
	        	boolBuilder.filter(rangeQuery);
	        }
	        SearchRequestBuilder builder = transportClient.prepareSearch(index)
	                    .setTypes(type)
	                    .setSearchType(SearchType.QUERY_THEN_FETCH)
	                    .setQuery(boolBuilder)
	                    .setFrom(0)
	                    .setSize(10);
	        log.info(String.valueOf(builder));
	        SearchResponse response = builder.get();
	        response.getHits().forEach((s)->result.add(s.getSource()));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return result;
	}
	
}
