package org.alive.springlab.zzz;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Arrays;
import java.util.Collections;

/**
 * MyBatisPlus Code Generator工具
 */
public class MyBatisPlusCodeGenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://172.16.8.30:3306/dst_db_security?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8";
        FastAutoGenerator.create(url, "dev", "ZV4wf:grl5tngntd")
                .globalConfig(builder -> {
                    builder.author("xuhailin")
                            .outputDir("D://temp")
                            .commentDate("yyyy-MM-dd")
                            .dateType(DateType.ONLY_DATE);
                })
                .packageConfig(builder -> {
                    builder.parent("com.dst.security.core.modules.business.sensitive"); // 可通过pathinfo配置各输出文件位置
                })
                .strategyConfig(builder -> {
                    String[] tableNames = {"sensitive_view_log"};
                    builder.addInclude(Arrays.asList(tableNames))
                            .controllerBuilder()
                            .enableRestStyle()
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImp")
                            .entityBuilder()
                            .enableLombok()
                            .mapperBuilder()
                            .enableMapperAnnotation();
                }).templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
