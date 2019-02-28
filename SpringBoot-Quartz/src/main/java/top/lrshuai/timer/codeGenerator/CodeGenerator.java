package top.lrshuai.timer.codeGenerator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Mybatis-plus 的代码生成类
 */
public class CodeGenerator {

    //页面生成根目录
    public static String projectPageRelativePath="/src/main/resources/templates/page/";
    //自定义 页面模板路径
    public static String pageTemplatesPath="templates/pageTemplates/list.ftl";

    //自定义Controller模板路径
    public static String controlTemplatesPath="templates/codeTemplates/Controller.java.ftl";
    //自定义ServiceImpl模板路径
    public static String serviceImplTemplatesPath="templates/codeTemplates/ServiceImpl.java.ftl";
    //自定义Service模板路径
    public static String serviceTemplatesPath="templates/codeTemplates/Service.java.ftl";



    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("rstyro");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/quartz?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Hongkong");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        String modelName=scanner("模块名");
        pc.setModuleName(modelName);
        pc.setParent("top.lrshuai.timer");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.lrs.admin");
        strategy.setEntityLombokModel(true);
        strategy.setInclude(scanner("表名"));
//        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setRestControllerStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");

        String isGeneratorPage = scanner("是否生成页面：1 -- 生成，0 -- 不生成，这个模板是我以前用的，现在不适合，要用自己修改模板");
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            public void initMap() {}
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()+ "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        //项目相对路径
        String projectRelative ="/src/main/java/top/lrshuai/timer/"+modelName;
        if("1".equals(isGeneratorPage)){
            strategy.setRestControllerStyle(false);
            focList.add(new FileOutConfig(pageTemplatesPath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return projectPath + projectPageRelativePath +modelName+"/"+tableInfo.getEntityPath()+"_list.html";
                }
            });

            focList.add(new FileOutConfig(controlTemplatesPath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return projectPath + projectRelative+"/controller/"+tableInfo.getControllerName()+StringPool.DOT_JAVA;
                }
            });
            focList.add(new FileOutConfig(serviceImplTemplatesPath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return projectPath +projectRelative+"/service/impl/"+tableInfo.getServiceImplName()+StringPool.DOT_JAVA;
                }
            });
            focList.add(new FileOutConfig(serviceTemplatesPath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return projectPath +projectRelative+"/service/"+tableInfo.getServiceName()+StringPool.DOT_JAVA;
                }
            });

        }
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.setStrategy(strategy);
        mpg.execute();
    }

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

}
