package com.lpy.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

@Getter
public enum FileFilterRangeEnum {
    FILE_NAME("文件名称","fileName"),
    FILE_CONTENT("文件内容","fileContent");

    private final String text;
    private final String value;

     FileFilterRangeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    //由value值获取枚举类型
    public static FileFilterRangeEnum getEnumByValue(String value){
         if(ObjectUtil.isEmpty(value)){
             return null;
         }
         //.values()方法获得该枚举类型的所以可能的枚举值
        for (FileFilterRangeEnum anEnum: FileFilterRangeEnum.values()) {
            if(anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }
}
