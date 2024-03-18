package com.lpy.generator;

import com.lpy.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DynamicGenerator {
    //选择数据模型并set,然后调用doGenerate生成
    public static void main(String[] args) throws IOException, TemplateException {
        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath+File.separator+"src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath = projectPath+File.separator+"MainTemplate.java";//这两行是将来命令行改造的部分
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("lpy");
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setOutputText("求和结果： ");
        doGenerate(inputPath,outputPath,mainTemplateConfig);
    }
    /**
     * 生成文件
     *
     * @param inputPath 模板文件输入路径
     * @param outputPath 输出路径
     * @param model 数据模型  templateconfig类
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerate(String inputPath,String outputPath, Object model) throws IOException,TemplateException{
        //new 出配置类
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        //设置相关配置(模板存放目录，编码格式等）
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);
        configuration.setDefaultEncoding("utf-8");
        //载入指定模板
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);
        //new出数据模型类 并set
//        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
//        model.setAuthor("lpy");
//        model.setLoop(false);
//        model.setOutputText("求和结果： ");
        //用模板载入数据模型生成文件
        FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
        Writer out = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        template.process(model,out);
        out.close();
    }

}
