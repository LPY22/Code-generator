package com.lpy.maker.generator.file;

import com.lpy.maker.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 */
public class FileGenerator {
    public static void doGenerate(Object model) throws TemplateException, IOException {
        //得到整个项目的根路径
        String projectPath= System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        //输入路径
        String inputPath = new File(parentFile,"code-demo-projects/acm-template").getAbsolutePath();
        String outputPath = projectPath;
        //生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath,outputPath);
        //生成动态文件
        String dynamicInputPath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String dynamicOutputPath = outputPath + File.separator + "acm-template/src/com/lpy/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(dynamicInputPath,dynamicOutputPath,model);
    }

    public static void main(String[] args) throws TemplateException, IOException {
        DataModel dataModel = new DataModel();
        dataModel.setAuthor("你你你");
        dataModel.setLoop(false);
        dataModel.setOutputText("求和结果： ");
        doGenerate(dataModel);
    }

}
