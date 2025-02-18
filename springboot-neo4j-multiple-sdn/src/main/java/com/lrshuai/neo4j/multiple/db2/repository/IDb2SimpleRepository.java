package com.lrshuai.neo4j.multiple.db2.repository;

import com.lrshuai.neo4j.multiple.db1.node.BaseNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * 第二个数据源的
 */
@Repository
public interface IDb2SimpleRepository extends Neo4jRepository<BaseNode,Long> {

}
