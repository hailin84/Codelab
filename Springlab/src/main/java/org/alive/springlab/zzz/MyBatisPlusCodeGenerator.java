package org.alive.springlab.zzz;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Arrays;
import java.util.Collections;

/**
 * MyBatisPlus Code Generator工具
 */
public class MyBatisPlusCodeGenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testdb?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8";
        FastAutoGenerator.create(url, "root", "AaZz1234")
                .globalConfig(builder -> {
                    builder.author("hailin84").outputDir("D://temp");
                })
                .packageConfig(builder -> {
                    builder.parent("org.alive.springlab"); // 可通过pathinfo配置各输出文件位置
                })
                .strategyConfig(builder -> {
                    String[] tableNames = {"account", "student", "user"};
                    tableNames = new String[] {"product"};
                    builder.addInclude(Arrays.asList(tableNames));
                }).templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
