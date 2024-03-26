package ${basePackage}.generator.file;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 */
public class FileGenerator {
    public static void doGenerate(Object model) throws TemplateException, IOException {
        //输入路径
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";

        String inputPath;
        String outputPath;
        //按类型进行文件生成
    <#list fileConfig.files as fileInfo>

        inputPath = new File(inputRootPath,"${fileInfo.inputPath}").getAbsolutePath();
        outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
        <#if fileInfo.generateType == "static">
        StaticFileGenerator.copyFilesByHutool(inputPath,outputPath);
        <#else>
        DynamicFileGenerator.doGenerate(inputPath,outputPath,model);
        </#if>
    </#list>
    }
}
