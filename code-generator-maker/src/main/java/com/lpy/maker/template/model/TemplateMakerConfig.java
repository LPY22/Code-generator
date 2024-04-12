package com.lpy.maker.template.model;

import com.lpy.maker.meta.Meta;
import lombok.Data;

//用于封装所有模板制作工具需要的参数
@Data
public class TemplateMakerConfig {

    private Long id;

    private Meta meta = new Meta(); //赋默认值，防止NPE（nullPointerException）

    private String originProjectPath;

    TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();

    TemplateMakerModelFileConfig modelConfig = new TemplateMakerModelFileConfig();

    //输出配置
    TemplateMakerOutputConfig templateMakerOutputConfig = new TemplateMakerOutputConfig();

}
