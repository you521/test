 package com.you.rabbitmq.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pc
 * @date 2020/12/07
 */
 
@RestController
public class BaseController {
      @RequestMapping() 
      public String getIndex(){
          return "hello";
      }
}
