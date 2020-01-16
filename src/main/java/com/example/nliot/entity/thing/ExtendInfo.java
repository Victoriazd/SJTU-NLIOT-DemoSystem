package com.example.nliot.entity.thing;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ExtendInfo {
    @JSONField(name = "联系方式")
    private String contactMethod;

    @JSONField(name = "联系地址")
    private String contactAddress;
}
