package com.lpy.maker.model;

import lombok.Data;

/**
 * 数据模型
 */
@Data
public class DataModel {
    //生成循环
    private boolean loop;
    //作者注释
    private String author = "unknown";
    //输出信息
    private String outputText = "sum = ";
}
