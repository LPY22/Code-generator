package com.lpy.maker.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> files;

    private FileGroupConfig fileGroupConfig;


    @Data
    @NoArgsConstructor
    public static class FileInfoConfig {

        private String Path;

        private String condition;
        //给单个文件设置生成条件

        private List<FileFilterConfig> fileFilterConfigs;
    }
    @Data //加入了分组相关的参数，与meta实体类中的属性一一对应，按我们之前的设计，分组是按TemplateMakerFileConfig来分组
    //即同一个templateMakerFileConfig里面的文件是一组，使用相同的组配置
    public static class FileGroupConfig {

        private String condition;

        private String groupKey;

        private String groupName;

    }
}
