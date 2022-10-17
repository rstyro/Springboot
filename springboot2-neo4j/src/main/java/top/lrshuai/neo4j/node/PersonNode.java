package top.lrshuai.neo4j.node;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Person")
@Data
@Accessors(chain = true)
public class PersonNode {
//    @Id
//    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    @Id
    private Long id;

    @Property("name")
    private final String name;

    @Property("born")
    private final Integer born;

    public PersonNode(Integer born, String name) {
        this.born = born;
        this.name = name;
    }
}
