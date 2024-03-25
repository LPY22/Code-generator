package com.lpy.maker.meta;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Meta {

    /**
     * name : acm_template_pro_generator
     * description : ACM示例模板生成器
     * basePackage : com.lpy
     * version : 1.0
     * author : lpy
     * createTime : 2024-03-18
     * fileConfig : {"inputRootPath":"D:/桌面文件/ava_exercise/Code-generator/code-demo-projects/acm-template-pro","outputRootPath":"generated","type":"dir","files":[{"inputPath":"src/com/lpy/acm/MainTemplate.java.ftl","outputPath":"src/com/lpy/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]}
     * modelConfig : {"models":[{"fieldName":"loop","type":"boolean","description":"是否生成循环","defaultValue":false,"abbr":"l"},{"fieldName":"author","type":"String","description":"作者注释","defaultValue":"lpy","abbr":"a"},{"fieldName":"outputText","type":"String","description":"输出信息","defaultValue":"sum = ","abbr":"o"}]}
     */

    private String name;
    private String description;
    private String basePackage;
    private String version;
    private String author;
    private String createTime;
    private FileConfig fileConfig;
    private ModelConfig modelConfig;

    @NoArgsConstructor
    @Data
    public static class FileConfig {
        /**加了个sourceRootPath
         * inputRootPath : D:/桌面文件/ava_exercise/Code-generator/code-demo-projects/acm-template-pro
         * outputRootPath : generated
         * type : dir
         * files : [{"inputPath":"src/com/lpy/acm/MainTemplate.java.ftl","outputPath":"src/com/lpy/acm/MainTemplate.java","type":"file","generateType":"dynamic"},{"inputPath":".gitignore","outputPath":".gitignore","type":"file","generateType":"static"},{"inputPath":"README.md","outputPath":"README.md","type":"file","generateType":"static"}]
         */

        private String inputRootPath;
        private String outputRootPath;
        private String sourceRootPath;
        private String type;
        private List<FileInfo> files;

        @NoArgsConstructor
        @Data
        public static class FileInfo {
            /**
             * inputPath : src/com/lpy/acm/MainTemplate.java.ftl
             * outputPath : src/com/lpy/acm/MainTemplate.java
             * type : file
             * generateType : dynamic
             */

            private String inputPath;
            private String outputPath;
            private String type;
            private String generateType;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ModelConfig {
        private List<ModelInfo> models;

        @NoArgsConstructor
        @Data
        public static class ModelInfo {
            /**
             * fieldName : loop
             * type : boolean
             * description : 是否生成循环
             * defaultValue : false
             * abbr : l
             */

            private String fieldName;
            private String type;
            private String description;
            private Object defaultValue;
            private String abbr;
        }
    }
}
