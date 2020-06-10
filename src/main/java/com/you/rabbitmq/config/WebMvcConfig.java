package com.you.rabbitmq.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * 
    * @ClassName: WebMvcConfig  
    * @Description: spring mvc的拓展配置文件
    * @author you  
    * @date 2020年5月27日  
    *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
      /**           
       *   追加默认的消息转换器，删除掉Jackson消息转换器， 并在最后添加fastjson转换器
       *   
       *   @param converters 消息转换器列表
       */
      @Override
      public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    /*
            先把JackSon的消息转换器删除.
    (1)源码分析可知，返回json的过程为:
    Controller调用结束后返回一个数据对象，for循环遍历conventers，找到支持application/json的HttpMessageConverter，然后将返回的数据序列化成json。
             具体参考org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor的writeWithMessageConverters方法
    (2)由于是list结构，我们添加的fastjson在最后。因此必须要将jackson的转换器删除，不然会先匹配上jackson，导致没使用fastjson
    */
          // for循环遍历删除jackson转换器
          for(int i = converters.size() - 1; i >= 0; i--) {
              if (converters.get(i) instanceof MappingJackson2HttpMessageConverter){
                converters.remove(i);
              }
          }
          // 创建fastjson消息转换器
          FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
          // 创建fastjson全局配置类
          FastJsonConfig fastJsonConfig = new FastJsonConfig();
          fastJsonConfig.setSerializerFeatures(
                  //结果格是否式化,默认为false
                  SerializerFeature.PrettyFormat,
                  //List字段如果为null,输出为 [ ],而非null
                  SerializerFeature.WriteNullListAsEmpty,
                  //字符类型字段如果为null,输出为 " " ,而非null
                  SerializerFeature.WriteNullStringAsEmpty,
                  //Boolean字段如果为null,输出为false,而非null
                  SerializerFeature.WriteNullBooleanAsFalse,
                  //消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
                  SerializerFeature.DisableCircularReferenceDetect,
                  //将数值类型字段的空值输出为0
                  SerializerFeature.WriteNullNumberAsZero,
                  //是否输出值为null的字段,默认为false
                  SerializerFeature.WriteMapNullValue
                  );
          // fastjson 编码配置
          fastJsonConfig.setCharset(Charset.forName("UTF-8"));
          //在convert中添加配置信息
          fastJsonConverter.setFastJsonConfig(fastJsonConfig);
          //处理中文乱码问题
          List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
          //添加支持的MediaTypes;不添加时默认为*/*,也就是默认支持全部
          //但是MappingJackson2HttpMessageConverter里面支持的MediaTypes为application/json
          //参考它的做法, fastjson也只添加application/json的MediaType
          fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
          fastJsonConverter.setSupportedMediaTypes(fastMediaTypes);
          //将fastjson添加到转换器列表内
          converters.add(fastJsonConverter);
      }
      
      @Override
      public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
          for (HttpMessageConverter<?> messageConverter : converters) {
              System.out.println("消息转换器列表："+messageConverter);
          }
      }
}
