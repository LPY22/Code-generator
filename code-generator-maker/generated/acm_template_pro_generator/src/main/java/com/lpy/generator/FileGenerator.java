package com.lpy.generator.file;

import com.lpy.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 */
public class FileGenerator {
    public static void doGenerate(Object model) throws TemplateException, IOException {
        //输入路径
        String inputRootPath = ".source/acm-template-pro";
        String outputRootPath = "generated";

        String inputPath;
        String outputPath;
        //按类型进行文件生成

        inputPath = new File(inputRootPath,"src/com/lpy/acm/MainTemplate.java.ftl").getAbsolutePath();
        outputPath = new File(outputRootPath, "src/com/lpy/acm/MainTemplate.java").getAbsolutePath();
        DynamicFileGenerator.doGenerate(inputPath,outputPath,model);

        inputPath = new File(inputRootPath,".gitignore").getAbsolutePath();
        outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
        StaticFileGenerator.copyFilesByHutool(inputPath,outputPath);

        inputPath = new File(inputRootPath,"README.md").getAbsolutePath();
        outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
        StaticFileGenerator.copyFilesByHutool(inputPath,outputPath);
    }
}
