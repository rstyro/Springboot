# Springboot 与 ES整合Demo
- 1、先创建ES 索引，索引文件为：`poetry_index.json`在src的script目录下
- 2、执行test目录下的`PoetryApiApplicationTests.testAddES()` 方法把`唐诗三百首.json`文件到入到ES中，就可以请求接口了
- 3、可以浏览器访问：`http://localhost:8008/search/detail/2d0385e7-0c72-423f-a538-f66acc9d4c46`
- 4、飞花令接口：`http://localhost:8008/search/flyFlower?text=花`