package com.example.nliot.entity.thing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Thing {
    @JSONField(name = "物接口协议版本")
    private String interfaceVersion;

    @JSONField(name = "encoding standard")
    private String encodingStandard;

    @JSONField(name = "基本信息")
    private BasicInfo basicInfo;

    @JSONField(name = "属性信息")
    private PropertyInfo propertyInfo;

    @JSONField(name = "服务信息")
    private List<ServiceInfo> serviceInfos = new ArrayList<>();

    @JSONField(name = "扩展信息")
    private List<ExtendInfo> extendInfos = new ArrayList<>();

    public String toJson(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append(String.format("\"encoding standard\":\"%s\",", encodingStandard));
        stringBuilder.append(String.format("\"物接口协议版本\":\"%s\",", interfaceVersion));

        stringBuilder.append("\"基本信息\":");
        String basic = JSON.toJSONString(basicInfo);
        stringBuilder.append(basic);
        stringBuilder.append(",");

        stringBuilder.append("\"属性信息\":");
        String property = JSON.toJSONString(propertyInfo);
        stringBuilder.append(property);
        stringBuilder.append(",");

        stringBuilder.append("\"服务信息\":");
        String service = JSON.toJSONString(serviceInfos);
        stringBuilder.append(service);
        stringBuilder.append(",");

        stringBuilder.append("\"扩展信息\":");
        String extend = JSON.toJSONString(extendInfos);
        stringBuilder.append(extend);
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
