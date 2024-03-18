package com.lpy.model;

import lombok.Data;

@Data
public class MainTemplateConfig {
    //生成循环
    private boolean loop;
    //作者注释
    private String author = "unknown";
    //输出信息
    private String outputText = "sum = ";
}
