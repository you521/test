package com.you.rabbitmq.service.impl;

import org.springframework.stereotype.Service;

import com.rabbitmq.client.Connection;
import com.you.rabbitmq.service.RabbitMQProducerService;
import com.you.rabbitmq.util.RabbitMQUtil;

@Service("rabbitMQProducerService")
public class RabbitMQProducerServiceImpl implements RabbitMQProducerService
{

    @Override
    public String rabbitMQProducer(String message)
    {
        try
        {
            // 获取rabbitmq连接
            Connection connection = RabbitMQUtil.getConnection();
            System.out.println("-------------------rabbitmq生产者发送消息开始------------------");
            RabbitMQUtil.getProducerRabbitMQ(connection, message);
            System.out.println("-------------------rabbitmq生产者发送消息结束------------------");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return message;
    }

}
