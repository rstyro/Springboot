# Neo4j 多数据源使用
- 参考官方例子：[https://github.com/michael-simons/neo4j-examples-and-tips/tree/master/examples/sdn6-multidb-multi-connections](https://github.com/michael-simons/neo4j-examples-and-tips/tree/master/examples/sdn6-multidb-multi-connections)
- 如果不需要健康检查，直接把POM的`spring-boot-starter-actuator`依赖去掉
- 把`Neo4jHealthConfig` 和`DatabaseSelectionAwareNeo4jHealthIndicator` 删除
- 和Neo4jConfig1与Neo4jConfig2中的 `DatabaseSelectionAwareNeo4jHealthIndicator` Bean注入方法删掉
- 