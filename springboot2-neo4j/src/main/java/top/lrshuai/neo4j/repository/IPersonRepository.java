package top.lrshuai.neo4j.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import top.lrshuai.neo4j.node.PersonNode;

@Repository
public interface IPersonRepository extends Neo4jRepository<PersonNode,Long> {

}
