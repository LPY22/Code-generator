package com.lpy.maker.meta.enums;

/**
 * 文件生成类型枚举
 */
public enum FileGenerateTypeEnum {
    DYNAMIC("动态","dynamic"),
    STATIC("静态","static");

    private final String text;
    private final String value;

    FileGenerateTypeEnum(String text, String value) {
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
