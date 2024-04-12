package com.lpy.maker.template;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.lpy.maker.meta.Meta;
import com.lpy.maker.template.model.TemplateMakerConfig;
import com.lpy.maker.template.model.TemplateMakerFileConfig;
import com.lpy.maker.template.model.TemplateMakerModelFileConfig;
import com.lpy.maker.template.model.TemplateMakerOutputConfig;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TemplateMakerTest extends TestCase {
    @Test
    public void testMakeTemplateBug1() {
        Meta meta = new Meta();
        meta.setName("spingboot-template-generator");
        meta.setDescription("Spring Boot示例模板生成器");

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent()+File.separator+"code-demo-projects/springboot-init";

        //配置文件参数：TemplateMakerFileConfig
        String inputFilePath1 = "/src/main/resources/application.yml";
        TemplateMakerFileConfig  templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath1);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1));

        //配置模型参数 TemplateMakerModelFileConfig
        TemplateMakerModelFileConfig templateMakerModelFileConfig = new TemplateMakerModelFileConfig();
        TemplateMakerModelFileConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelFileConfig.ModelInfoConfig();
        TemplateMakerOutputConfig templateMakerOutputConfig = new TemplateMakerOutputConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");
        templateMakerModelFileConfig.setModels(Arrays.asList(modelInfoConfig1));

        long id = TemplateMaker.makeTemplate(1776208424862777344L,meta,originProjectPath,templateMakerFileConfig,templateMakerModelFileConfig,templateMakerOutputConfig);
        System.out.println(id);

    }
    @Test
    /**
     * 如果多次制作时指定了相同的目录（如common包），那么后续制作时会扫描到之前生成的FTL模板文件，并尝试基于FTL文件再次制作模板
     * 导致生成了错误的配置
     */
    public void testTemplateMakerBug2(){
        Meta meta = new Meta();
        meta.setName("spingboot-template-generator");
        meta.setDescription("Spring Boot示例模板生成器");

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent()+File.separator+"code-demo-projects/springboot-init";
        // 文件参数配置，扫描目录
        String inputPath1 = "/src/main/java/com/yupi/springbootinit/common";
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputPath1);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1));
        // 模型参数配置
        TemplateMakerModelFileConfig templateMakerModelFileConfig = new TemplateMakerModelFileConfig();
        TemplateMakerModelFileConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelFileConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setReplaceText("BaseResponse");
        List<TemplateMakerModelFileConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelFileConfig.setModels(modelInfoConfigList);

        TemplateMakerOutputConfig templateMakerOutputConfig = new TemplateMakerOutputConfig();
        long id = TemplateMaker.makeTemplate(1777366705211826176l, meta, originProjectPath, templateMakerFileConfig, templateMakerModelFileConfig,templateMakerOutputConfig);
        System.out.println(id);

    }
    @Test
    public void testMakerTemplateWithJSON(){
        String rootPath = "examples/springboot-init/";
        String configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker.json");
//        String configStr = ResourceUtil.readUtf8Str("templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        System.out.println(templateMakerConfig);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
    }
    @Test
    public void testMakeSpringBootTemplate(){ //方法名必须要带test??!!
        String rootPath = "examples/springboot-init/";
        String configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        System.out.println(templateMakerConfig);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker1.json");
        templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        System.out.println(templateMakerConfig);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath +"templateMaker2.json");
        templateMakerConfig = JSONUtil.toBean(configStr,templateMakerConfig.getClass());
        System.out.println(templateMakerConfig);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath +"templateMaker3.json");
        templateMakerConfig = JSONUtil.toBean(configStr,templateMakerConfig.getClass());
        System.out.println(templateMakerConfig);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath +"templateMaker4.json");
        templateMakerConfig = JSONUtil.toBean(configStr,templateMakerConfig.getClass());
        System.out.println(templateMakerConfig);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath +"templateMaker5.json");
        templateMakerConfig = JSONUtil.toBean(configStr,templateMakerConfig.getClass());
        System.out.println(templateMakerConfig);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath +"templateMaker6.json");
        templateMakerConfig = JSONUtil.toBean(configStr,templateMakerConfig.getClass());
        System.out.println(templateMakerConfig);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath +"templateMaker7.json");
        templateMakerConfig = JSONUtil.toBean(configStr,templateMakerConfig.getClass());
        System.out.println(templateMakerConfig);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath +"templateMaker8.json");
        templateMakerConfig = JSONUtil.toBean(configStr,templateMakerConfig.getClass());
        System.out.println(templateMakerConfig);
        TemplateMaker.makeTemplate(templateMakerConfig);



        System.out.println(id);
    }
}