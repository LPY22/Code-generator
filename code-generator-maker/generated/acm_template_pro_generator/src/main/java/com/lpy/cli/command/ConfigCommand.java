package com.lpy.cli.command;

import com.lpy.model.DataModel;
import picocli.CommandLine;
import cn.hutool.core.util.ReflectUtil;
import java.lang.reflect.Field;

@CommandLine.Command(name = "config",description = "查看参数信息",mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable{

    @Override
    public void run() {
        System.out.println("查看参数信息");
        Field[] fields = ReflectUtil.getFields(DataModel.class);
        for (Field field :
                fields) {
            System.out.println("字段名称："+field.getName());
            System.out.println("字段类型："+field.getType());
            System.out.println("Modifier:" + java.lang.reflect.Modifier.toString(field.getModifiers()));
            System.out.println("---");
        }

    }
}
