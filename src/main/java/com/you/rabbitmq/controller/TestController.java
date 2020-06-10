package com.you.rabbitmq.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.you.rabbitmq.test.bean.User;
import com.you.rabbitmq.test.bean.Word;

@RestController
@RequestMapping(value="/api/v1")
public class TestController
{
     @GetMapping(value = "/get")
     public Word getJSONString() {
         Word word = new Word();
         word.setA("A");
         word.setB(null);
         word.setC(null);
         word.setDate(new Date());
         List<User> list = new ArrayList<User>();
         User user = new  User();
         user.setId(1);
         user.setAge(27);
         user.setName(null);
         list.add(user);
         word.setList(list);
         System.out.println(word.toString());
         return word;
     }
}
