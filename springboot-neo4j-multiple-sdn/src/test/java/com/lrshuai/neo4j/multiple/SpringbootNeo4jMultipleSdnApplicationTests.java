package com.lrshuai.neo4j.multiple;

import com.lrshuai.neo4j.multiple.db1.node.ArticleNode;
import com.lrshuai.neo4j.multiple.db1.repository.ISimpleRepository;
import com.lrshuai.neo4j.multiple.db2.node.Article2Node;
import com.lrshuai.neo4j.multiple.db2.repository.IDb2SimpleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import javax.annotation.Resource;

@SpringBootTest
class SpringbootNeo4jMultipleSdnApplicationTests {

    @Resource
    private ISimpleRepository simpleRepository;


    @Resource
    private IDb2SimpleRepository db2SimpleRepository;

    @Test
    void contextLoads() {
        ArticleNode articleNode = new ArticleNode();
        articleNode.setUid("5418687b1eb9f93dba31fcd537b9796a");
        ArticleNode articleNode1 = simpleRepository.findOne(Example.of(articleNode)).orElse(null);
        System.out.println(articleNode1);

        Article2Node articleNode2 = new Article2Node();
        articleNode2.setAid("55d3ab1cef04063852d5cb639e99d4ea");
        articleNode2 = db2SimpleRepository.findOne(Example.of(articleNode2)).orElse(null);
        System.out.println(articleNode2);
    }

}
