package com.example.nliot.entity.thing;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class BasicInfo {
    @JSONField(name = "物名")
    private String deviceName;

    @JSONField(name = "物类型")
    private String deviceType;

    @JSONField(name = "物号")
    private String deviceId;

    @JSONField(name = "物通信地址")
    private String deviceAddress;

    @JSONField(name = "物版本号")
    private String deviceVersion;


}
