package com.lpy.maker.generator.file;

import cn.hutool.core.io.FileUtil;

public class StaticFileGenerator {
    /*
    * 拷贝文件（ Hutool 实现 ，将输入目录完整拷贝到输出目录下）
    * */
    public static void copyFilesByHutool(String inputPath,String outputPath) {
        FileUtil.copy(inputPath,outputPath,false);
    }


}
