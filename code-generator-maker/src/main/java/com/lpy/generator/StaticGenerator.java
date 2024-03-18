package com.lpy.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class StaticGenerator {
    /*
    * 拷贝文件（ Hutool 实现 ，将输入目录完整拷贝到输出目录下）
    * */
    public static void copyFilesByHutool(String inputPath,String outputPath) {
        FileUtil.copy(inputPath,outputPath,false);
    }

    public static void main(String[] args) {

        //直接调用工具完成整个目录的复制生成
        //获取整个项目的根路径
        String projectPath = System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        //inputpath ACM示例模版目录
        String inputPath = new File(parentFile,"code-demo-projects/acm-template").getAbsolutePath();
        //outputpath 直接输出到项目模版的根目录
        String outputPath = projectPath;
        copyFilesByHutool(inputPath,outputPath);
    }
    /*
    * 递归拷贝文件（递归实现）
    * */
    public static void copyFilesByRecursive(String inputPath, String outputPath){
        File inputFile= new File(inputPath);
        File outputFile = new File(outputPath);
        try{
            copyFileByRecursive(inputFile,outputFile);
        }catch (Exception e){
            System.err.println("文件复制失败");
            e.printStackTrace();
        }

    }
    public static void copyFileByRecursive(File inputFile,File outputFile) throws IOException {
        //看看是文件还是目录
        if(inputFile.isDirectory()){
            System.out.println(inputFile.getName());
            File destOutputFile = new File(outputFile,inputFile.getName());
            //如果是目录，首先创建目标目录
            if(!destOutputFile.exists()){
                destOutputFile.mkdirs();
            }
            //获取目录下的所有文件和子目录
            File[] files = inputFile.listFiles();
            //没有子文件时
            if(ArrayUtil.isEmpty(files)){
                return;
            }
            for(File file:files){
                //递归拷贝下一层文件
                copyFileByRecursive(file,destOutputFile);
            }
        }else {
            //是文件，直接复制到目标目录下
            Path destPath = outputFile.toPath().resolve(inputFile.getName());
            Files.copy(inputFile.toPath(),destPath, StandardCopyOption.REPLACE_EXISTING);
        }

    }

}
