package com.lpy.maker.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

//编写脚本生成逻辑，先将生成逻辑封装成一个方法，传入jar包路径（jarPath）(不同的元信息构建出的 jar包名称和路径是不同的)
//因为脚本的内容很短就不编写ftl模板，直接用StringBuilder拼接字符串，然后写入文件。
//ps:非Windows系统要再生成文件后使用 PostixFilePermissions 类给文件默认添加可执行权限
public class ScriptGenerator {
    public static void doGenerate(String outputPath,String jarPath){
        StringBuilder sb = new StringBuilder();

        //linux
        sb.append("#!/bin/bash").append("/n");
        sb.append(String.format("java -jar %s \"$@\"",jarPath)).append("\n");
        FileUtil.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8),outputPath);
        //添加可执行权限
        try{
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(Paths.get(outputPath),permissions);
        }catch (Exception e){
        }
        //windows
        sb = new StringBuilder();
        sb.append("@echo off").append("\n");
        sb.append(String.format("java -jar %s %%*",jarPath)).append("\n");
        FileUtil.writeBytes((sb.toString().getBytes(StandardCharsets.UTF_8)),outputPath+".bat");
    }

    public static void main(String[] args) {
        String outputPath = System.getProperty("user.dir") + File.separator + "generator";
        doGenerate(outputPath,"");
    }
}
