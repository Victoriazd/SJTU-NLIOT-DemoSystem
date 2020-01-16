package com.example.nliot.entity.thing;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ServiceInfo {
    @JSONField(name = "服务名称")
    private String serviceName;

    @JSONField(name = "数据类型")
    private String dataType;

    @JSONField(name = "接口地址")
    private String serviceAddress;

    @JSONField(name = "协议类型")
    private String protocolType;

    @JSONField(name = "协议参数")
    private String protocolParameter;

    @JSONField(name = "备注")
    private String remark;
}
