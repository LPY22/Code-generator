package com.lpy.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

public class MetaManager {
    //volatile关键子
    // 1.用于保证多线程环境下变量的可见性。线程在访问这个变量时会直接从主内存中读取而不是从线程的本地缓存读取
    //这样可以确保所以线程能够看到最新的值，不会出现数据不一致的情况。
    //2.禁止指令重排序：为提高程序的执行效率，编译器和处理器会对指令进行优化和重排序。使用volatile关键字可以禁止重排序。
    //ps : volatile只作用于读取和赋值
    private static volatile Meta meta;

    private MetaManager(){
//        私有构造函数防止外部实例化（单例模式必备）
    }
    //常用的单例模式构造：1.volatile关键字 2.私有构造器 3.双检锁 在锁的前后都检查单例是否创建，锁后检查因为
    //多线程锁竞争，一个现场加锁后创建完毕其他线程获得锁后，此时实例已经创建，所以需要第二道检验
    public static Meta getMetaObject(){
        if(meta == null){
            synchronized (MetaManager.class){
                if(meta == null){
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta(){
//        从JSON文件中把对象属性读入到meta对象中进行初始化
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        Meta newMeta = JSONUtil.toBean(metaJson,Meta.class);
        Meta.FileConfig fileConfig = newMeta.getFileConfig();
        //校验和处理默认值
        MetaValidator.doValidAndFill(newMeta);
        return newMeta;
    }

}
