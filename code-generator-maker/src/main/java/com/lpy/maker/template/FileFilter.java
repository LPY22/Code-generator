package com.lpy.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.lpy.maker.template.enums.FileFilterRangeEnum;
import com.lpy.maker.template.enums.FileFilterRuleEnum;
import com.lpy.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class FileFilter {
    //对某个文件或目录进行过滤，返回文件列表

    public static List<File> doFilter(String filePath, List<FileFilterConfig> fileFilterConfigsList){
        List<File> fileList = FileUtil.loopFiles(filePath,null);
        return  fileList.stream()
                .filter(file -> doSingleFileFilter(fileFilterConfigsList,file))
                .collect(Collectors.toList());//获取所有过滤通过的文件的列表
    }

    /**
     * 单个文件过滤
     * @param fileFilterConfigList 过滤设置列表
     * @param file 单个文件
     * @return
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file){
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

        //所有过滤器校验结束的结果
        boolean result = true;
        if(CollUtil.isEmpty(fileFilterConfigList)){
            return true;
        }

        for (FileFilterConfig fileFilterConfig :
                fileFilterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if(fileFilterRangeEnum == null) continue;

            //要过滤的原内容 对于枚举类型，switch语句可以直接case 枚举值
            String content = fileName;
//            System.out.print(content);
            switch(fileFilterRangeEnum){
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }

            FileFilterRuleEnum filterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if(filterRuleEnum == null){
                continue;
            }
            switch (filterRuleEnum){
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                default:
            }

            //有一个不满足，直接返回
            if(!result){
//                System.out.println("\tfalse");
                return false;
            }
        }
        return true;
    }
}
