package top.lrshuai.generator;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.*;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath);
        gc.setAuthor("rstyro");
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        //useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC
        dsc.setUrl("jdbc:mysql://localhost:3306/mqtest?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent("top.lrshuai.amqp");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
                Map<String, Object> map = new HashMap<>();
                map.put("commons", "top.lrshuai.amqp.commons." + pc.getModuleName());
                map.put("provider", "top.lrshuai.amqp.provider." + pc.getModuleName());
                this.setMap(map);
            }
        };

        // 包配置变量
        String commonsModuleName = "/amqp-commons";
        String providerModuleName = "/amqp-provider";
        String srcPath = "/src/main/java/top/lrshuai/amqp/";
        String providerSrcPath = srcPath + "/provider/";
        String commonsSrcPath = srcPath + "/commons/";

        // 自定义entity模板路径
        String entityTemplatesPath = "templates/entity.java.ftl";
        //自定义Controller模板路径
        String controlTemplatesPath = "templates/controller.java.ftl";
        //自定义ServiceImpl模板路径
        String serviceImplTemplatesPath = "templates/serviceImpl.java.ftl";
        //自定义Service模板路径
        String serviceTemplatesPath = "templates/service.java.ftl";
        //自定义xml模板路径
        String mapperXMLTemplatesPath = "templates/mapper.xml.ftl";
        //自定义mapper java模板路径
        String mapperJavaTemplatesPath = "templates/mapper.java.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出

        focList.add(new FileOutConfig(controlTemplatesPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                System.out.println("tableInfo=" + JSON.toJSONString(tableInfo));
                return projectPath + providerModuleName + providerSrcPath + pc.getModuleName() + "/controller/" + tableInfo.getControllerName() + StringPool.DOT_JAVA;
            }
        });


        // commons模块的基本前缀路径
        String baseOutFilepath = projectPath + commonsModuleName + commonsSrcPath + pc.getModuleName();

        focList.add(new FileOutConfig(mapperXMLTemplatesPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + commonsModuleName + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }

        });
        focList.add(new FileOutConfig(mapperJavaTemplatesPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseOutFilepath + "/mapper/" + tableInfo.getMapperName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(serviceImplTemplatesPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseOutFilepath + "/service/impl/" + tableInfo.getServiceImplName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(serviceTemplatesPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseOutFilepath + "/service/" + tableInfo.getServiceName() + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig(entityTemplatesPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return baseOutFilepath + "/entity/" + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig
                .setXml(null)
                .setController(null)
                .setEntity(null)
                .setMapper(null)
                .setService(null)
                .setServiceImpl(null);

        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        strategy.setSuperControllerClass("top.lrshuai.amqp.commons.base.BaseController");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
