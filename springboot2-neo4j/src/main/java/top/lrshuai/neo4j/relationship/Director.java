package top.lrshuai.neo4j.relationship;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import top.lrshuai.neo4j.node.PersonNode;

import java.util.List;

/**
 * 导演
 */
@RelationshipProperties
@Data
@Accessors(chain = true)
public class Director {
    @RelationshipId
    private Long id;

    @Property("roles")
    private List<String> roles;

    @TargetNode
    private PersonNode person;

}
