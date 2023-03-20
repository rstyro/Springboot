package com.lrshuai.neo4j.multiple.db2.node;

import com.lrshuai.neo4j.multiple.db1.node.BaseNode;
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
public class Article2Node extends BaseNode {

    @Property("aid")
    private String aid;

    @Property("ti")
    private String ti;

    @Property("dp")
    private Long dp;

}
