package top.lrshuai.neo4j.relationship;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import top.lrshuai.neo4j.node.PersonNode;

import java.util.List;

@RelationshipProperties
@Data
@Accessors(chain = true)
public class Role {
    @RelationshipId
    private Long id;

    @Property("roles")
    private List<String> roles;

    // @TargetNode 代表开始节点
    @TargetNode
    private PersonNode person;

}
