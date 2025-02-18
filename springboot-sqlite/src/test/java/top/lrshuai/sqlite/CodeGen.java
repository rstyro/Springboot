package top.lrshuai.sqlite;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.SqliteTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.SqliteQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGen {
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String outDir = projectPath +"/src/main/java";
        String outXmlDir = projectPath +"/src/main/resources/mapper/";
        DataSourceConfig.Builder build = new DataSourceConfig.Builder("jdbc:sqlite:D:/home/sqlite3/test.db", "", "")
                .dbQuery(new SqliteQuery())
                .schema("main")
                .typeConvert(new SqliteTypeConvert());

        FastAutoGenerator.create(build)
                .globalConfig(builder -> {
                    builder.author("rstyro") // 设置作者
                            .dateType(DateType.ONLY_DATE)
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("top.lrshuai.sqlite") // 设置父包名
//                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outXmlDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("demo") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
