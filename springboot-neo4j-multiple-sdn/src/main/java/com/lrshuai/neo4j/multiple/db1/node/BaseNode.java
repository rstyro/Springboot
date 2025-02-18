package com.lrshuai.neo4j.multiple.db1.node;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

public class BaseNode {
    @Id
    @GeneratedValue  //加这个注解id对应着neo4j的id，不加这个注解会把id当成属性，也能通过这个属性进行CURD
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
