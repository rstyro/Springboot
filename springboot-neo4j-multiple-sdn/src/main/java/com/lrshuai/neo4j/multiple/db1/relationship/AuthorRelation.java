package com.lrshuai.neo4j.multiple.db1.relationship;

import com.lrshuai.neo4j.multiple.db1.node.ArticleNode;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

/**
 * 作者-> 文献
 */
@RelationshipProperties
public class AuthorRelation {

    @RelationshipId
    private Long id;

    // 作者到文献的索引位置
    @Property("auIndex")
    private Integer auIndex;

    @TargetNode
    private ArticleNode personNode;


}
