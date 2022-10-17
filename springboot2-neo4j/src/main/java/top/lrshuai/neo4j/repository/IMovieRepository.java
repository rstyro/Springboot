package top.lrshuai.neo4j.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import top.lrshuai.neo4j.node.MovieNode;

import java.util.List;

@Repository
public interface IMovieRepository extends Neo4jRepository<MovieNode,Long> {

    /**
     * 使用 apoc.coll.contains 需要安装APOC插件
     * @param role 参数
     * @param title 参数
     * @return list
     */
    @Query("match (p:Person)-[r]->(n:Movie) where n.title=~$title or apoc.coll.contains(r.roles,$role) return DISTINCT n")
    List<MovieNode> getMovieNodeByRolesOrTitleIsLike(String role,String title);

}
