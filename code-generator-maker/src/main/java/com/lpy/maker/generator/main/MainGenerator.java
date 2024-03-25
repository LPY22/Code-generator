package com.lpy.maker.generator.main;

public class MainGenerator extends GenerateTemplate {
    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        System.out.println("不再输出dist了！");
        return "";
    }
}
