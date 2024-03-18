package com.lpy.generator;

import com.lpy.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 */
public class MainGenerator {
    public static void doGenerate(Object model) throws TemplateException, IOException {
        //得到整个项目的根路径
        String projectPath= System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        //输入路径
        String inputPath = new File(parentFile,"code-demo-projects/acm-template").getAbsolutePath();
        String outputPath = projectPath;
        //生成静态文件
        StaticGenerator.copyFilesByRecursive(inputPath,outputPath);
        //生成动态文件
        String dynamicInputPath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String dynamicOutputPath = outputPath + File.separator + "acm-template/src/com/lpy/acm/MainTemplate.java";
        DynamicGenerator.doGenerate(dynamicInputPath,dynamicOutputPath,model);
    }

    public static void main(String[] args) throws TemplateException, IOException {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("你你你");
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setOutputText("求和结果： ");
        doGenerate(mainTemplateConfig);
    }

}
