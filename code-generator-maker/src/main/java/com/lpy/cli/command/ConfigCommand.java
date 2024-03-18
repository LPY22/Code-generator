package com.lpy.cli.command;

import com.lpy.model.MainTemplateConfig;
import picocli.CommandLine;

import java.lang.reflect.Field;

@CommandLine.Command(name = "config",description = "查看参数信息",mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable{

    @Override
    public void run() {
        System.out.println("查看参数信息");
        //s使用JDK 原生反射语法实现
//        Class<?> myClass = MainTemplateConfig.class;
        Class<?> myclass = MainTemplateConfig.class;//?是出于泛型考虑，可以填MainTemplateConfig
        Field[] fields = myclass.getDeclaredFields();
        for (Field field :
                fields) {
            System.out.println("字段名称："+field.getName());
            System.out.println("字段类型："+field.getType());
            System.out.println("Modifier:" + java.lang.reflect.Modifier.toString(field.getModifiers()));
            System.out.println("---");
        }

    }
}
