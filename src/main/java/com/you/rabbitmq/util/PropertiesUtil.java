package com.you.rabbitmq.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * java 单列模式读取配置文件
 * @author Administrator
 *
 */

public class PropertiesUtil
{
      private static Properties properties = null;
      
      private static InputStream in = null;
    
      // 保存rabbitmq连接信息
      private Map<String, Object> proMap = new HashMap<>();
      
      // 懒汉式(用的时候再创建),延迟加载
      private static PropertiesUtil instance = null;
      
      /*
       * 静态方法访问时，直接访问不需要实例化
       */
      // synchronized表示同时只能一个线程进行实例化
      public static synchronized PropertiesUtil getInstance(){
          if(instance == null){
              // 如果两个进程同时进入时，同时创建很多实例，不符合单例
              instance = new PropertiesUtil();
          }
          return instance;
      }

  
      /**
        * 私有构造函数
       */
      private PropertiesUtil() {
          properties = new Properties();
          try
          {
              in = PropertiesUtil.class.getClassLoader().getResourceAsStream("rabbitmq.properties");
              properties.load(in);
              Enumeration<?> en = properties.propertyNames();
              while (en.hasMoreElements()) {
                  String key = (String) en.nextElement();
                  String value = properties.getProperty(key);
                  proMap.put(key, value);
               }
          } catch (Exception e)
          {
              System.out.println("Could not load properties from path:" + e.getMessage());
          } finally {
              try
              {
                in.close();
              } catch (IOException e)
              {
                e.printStackTrace();
              }
          }
      }
      
      /**
       * 获取map值
       * @return
       */
      public Map<String, Object> getPropMap() {
          return proMap;
      }
}
