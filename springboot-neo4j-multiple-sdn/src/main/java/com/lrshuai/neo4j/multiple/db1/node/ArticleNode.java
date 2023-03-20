package com.lrshuai.neo4j.multiple.db1.node;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * 文献节点
 */
@Node("Article")
@Data
@Accessors(chain = true)
public class ArticleNode extends BaseNode{

    @Property("uid")
    private String uid;

    @Property("doi")
    private String doi;

}
