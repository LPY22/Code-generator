package com.lpy.maker.meta;
//定义一个元信息异常类
public class MetaException extends RuntimeException {
    public MetaException(String message){
        super(message);//调父类RuntimeException的构造方法
    }

    public MetaException(String message, Throwable cause){
        super(message,cause);
    }
}
