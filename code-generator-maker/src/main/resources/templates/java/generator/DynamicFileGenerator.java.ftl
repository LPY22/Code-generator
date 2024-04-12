package ${basePackage}.generator;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DynamicFileGenerator {
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
        //如果文件不存在则创建文件和父目录
        if(!FileUtil.exist(outputPath)){
            System.out.println(outputPath);
            FileUtil.touch(outputPath);
        }
        //用模板载入数据模型生成文件
        FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
        Writer out = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        template.process(model,out);
        //记得关闭
        out.close();
    }

}
