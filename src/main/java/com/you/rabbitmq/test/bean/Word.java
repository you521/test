package com.you.rabbitmq.test.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class Word implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String a;
    private Integer b;
    private Boolean c;
    // FastJson时间格式化注解
    @JSONField(format = "yyyy-MM-dd HH:mm:ss") 
    private Date date;
    private Map<String , Object> map;
    private List<User> list;
}
