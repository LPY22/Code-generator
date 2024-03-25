package com.lpy.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.lpy.maker.meta.enums.FileGenerateTypeEnum;
import com.lpy.maker.meta.enums.FileTypeEnum;

import java.nio.file.Paths;
import java.util.List;

//定义一个独立的校验类，用于校验字段
public class MetaValidator {
    public static void doValidAndFill(Meta meta){
        doValidAndFillMetaRoot(meta);

        doValidAndFillFile(meta);

        doValidAndFillModel(meta);

    }

    private static void doValidAndFillModel(Meta meta) {
        // modelConfig 校验和默认值
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        List<Meta.ModelConfig.ModelInfo> modelInfoList = modelConfig.getModels();
        if (!CollectionUtil.isNotEmpty(modelInfoList)) {
            return;
        }
        for(Meta.ModelConfig.ModelInfo modelInfo: modelInfoList){
            //输出路径默认值
            String fieldName = modelInfo.getFieldName();
            if(StrUtil.isBlank(fieldName)) throw new MetaException("未填写 fieldname");

            String modelInfoType = modelInfo.getType();
            if(StrUtil.isEmpty(modelInfoType)){
                modelInfo.setType("String");
            }
        }
    }

    private static void doValidAndFillFile(Meta meta) {
        //fileConfig 校验和默认值
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        //必填项
        String sourceRootPath = fileConfig.getSourceRootPath();
        if(StrUtil.isBlank(sourceRootPath)){
            throw new MetaException("未填写 sourceRootPath");
        }
        String inputRootPath = fileConfig.getInputRootPath();
        if(StrUtil.isEmpty(inputRootPath)){
            //path类属于java.nio(new IO处理大量并发连接或者大数据量的IO操作).file.Paths类
            //作为工具类提供操作/创建Path对象的方法 Path对象可以代表文件系统的文件或目录路径，可以通过Path对象操作文件
            //执行读取，写入，复制，移动

            //利用FileUtil 获得path对象最后一层目录的path对象
            String defaultInputRootPath = ".source/" + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
            fileConfig.setInputRootPath(defaultInputRootPath);
        }
        // outputRootPath: 默认为当前路径下的generator
        String outputRootPath = fileConfig.getOutputRootPath();
        if(StrUtil.isEmpty(outputRootPath)){
            fileConfig.setOutputRootPath("generator");
        }

        String type = fileConfig.getType();
        if(StrUtil.isEmpty(type)){
            fileConfig.setType("dir");
        }

        //fileInfo 默认值
        List<Meta.FileConfig.FileInfo> fileInfoList = fileConfig.getFiles();
        if (!CollectionUtil.isNotEmpty(fileInfoList)) {
            return;
        }
        for(Meta.FileConfig.FileInfo fileInfo: fileInfoList){
            //inputPath 必填
            String inputPath = fileInfo.getInputPath();
            if(StrUtil.isBlank(inputPath)){
                throw new MetaException("未填写 inputPath");
            }

            //todo outputPath 默认等于inputPath 这里可能有问题？
            String outputPath = fileInfo.getOutputPath();
            if(StrUtil.isEmpty(outputPath)){
                fileInfo.setOutputPath(inputPath);
            }

            //type 有.后缀未文件，否则为目录
            String filetype = fileInfo.getType();
            if(StrUtil.isBlank(filetype)){
                if(StrUtil.isBlank(FileUtil.getSuffix(inputPath))) fileInfo.setType(FileTypeEnum.DIR.getValue());
                else fileInfo.setType(FileTypeEnum.FILE.getValue());
            }

            // generateType：如果文件结尾不为 Ftl，generateType 默认为 static，否则为 dynamic
            String generateType = fileInfo.getGenerateType();
            if (StrUtil.isBlank(generateType)) {
                // 为动态模板
                if (inputPath.endsWith(".ftl")) {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
                } else {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
                }
            }
        }
    }

    private static void doValidAndFillMetaRoot(Meta meta) {
        //基础信息校验和默认值
        String name = meta.getName();
        if(StrUtil.isBlank(name)){
            name = "my-codeGenerator";
            meta.setName(name);
        }
        //isEmpty 和 isBlank的区别 isEmpty用于判断是否为空（null或者长度为0）
        // isBlank 是判断是否为空或者 只包括 空白字符
        String description = meta.getDescription();
        if(StrUtil.isEmpty(description)){
            description = "我的模板代码生成器";
            meta.setDescription(description);
        }

        String author = meta.getAuthor();
        if(StrUtil.isEmpty(author)){
            author = "lpy";
            meta.setAuthor(author);
        }

        String basePackage = meta.getBasePackage();
        if(StrUtil.isBlank(basePackage)){
            basePackage = "com.lpy";
            meta.setBasePackage(basePackage);
        }

        String version = meta.getVersion();
        if(StrUtil.isEmpty(version)){
            version = "1.0";
            meta.setVersion(version);
        }
        String createTime = meta.getCreateTime();
        if(StrUtil.isEmpty(createTime)){
            createTime = DateUtil.now();
            meta.setCreateTime(createTime);
        }
    }
}
