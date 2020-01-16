package com.example.nliot.entity.thing;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PropertyInfo {
    @JSONField(name = "物生产商")
    private String deviceProducer;

    @JSONField(name = "物用户名")
    private String deviceUsername;

    @JSONField(name = "颜色")
    private String deviceColor;

    @JSONField(name = "重量")
    private String deviceWeight;
}
