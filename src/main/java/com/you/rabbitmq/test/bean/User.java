package com.you.rabbitmq.test.bean;

import java.io.Serializable;

import lombok.Data;



@Data
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private String name;
    
    private Integer age;
}
