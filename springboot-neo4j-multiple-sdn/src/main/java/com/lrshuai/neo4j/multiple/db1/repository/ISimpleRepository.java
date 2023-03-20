package com.lrshuai.neo4j.multiple.db1.repository;

import com.lrshuai.neo4j.multiple.db1.node.BaseNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISimpleRepository extends Neo4jRepository<BaseNode,Long> {

}
