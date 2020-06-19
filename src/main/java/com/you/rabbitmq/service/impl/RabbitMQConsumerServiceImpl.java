package com.you.rabbitmq.service.impl;

import org.springframework.stereotype.Service;

import com.rabbitmq.client.Connection;
import com.you.rabbitmq.service.RabbitMQConsumerService;
import com.you.rabbitmq.util.RabbitMQUtil;

@Service("rabbitMQConsumerService")
public class RabbitMQConsumerServiceImpl implements RabbitMQConsumerService
{

    @Override
    public String rabbitMQConsumer(String message)
    {
        // 获取连接
        Connection connection;
        try
        {
            connection = RabbitMQUtil.getConnection();
            RabbitMQUtil.getConsumerRabbitMQ(connection);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return message;
    }

}
