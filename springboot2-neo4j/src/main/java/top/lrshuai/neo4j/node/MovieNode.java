package top.lrshuai.neo4j.node;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import top.lrshuai.neo4j.relationship.Director;
import top.lrshuai.neo4j.relationship.Role;

import java.util.ArrayList;
import java.util.List;

@Node("Movie")
@Data
@Accessors(chain = true)
public class MovieNode {
    @Id
//    @GeneratedValue  加这个注解id对应着neo4j的id，不加这个注解会把id当成属性，也能通过这个属性进行CURD
    private Long id;

    @Property("title")
    private String title;

    @Property("tagline")
    private String description;

//    @CreatedBy
//    private User user;

    @Relationship(type = "ACTED_IN", direction = Direction.INCOMING)
    private List<Role> roles;

    @Relationship(type = "DIRECTED", direction = Direction.INCOMING)
    private List<Director> directors = new ArrayList<>();

}
