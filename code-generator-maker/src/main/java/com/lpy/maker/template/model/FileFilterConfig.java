package com.lpy.maker.template.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder //可以通过链式调用设置各个属性Car car = Car.builder().brand("Tesla").numberOfSeats(5).model("Model S").build() **以.builder开始 .build结束哦
public class FileFilterConfig {
    //过滤范围
    private String range;
    //过滤方式
    private String rule;
    //过滤值
    private String value;

}
