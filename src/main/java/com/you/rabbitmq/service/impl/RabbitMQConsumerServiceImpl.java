package com.you.rabbitmq.service.impl;

import org.springframework.stereotype.Service;

import com.rabbitmq.client.Connection;
import com.you.rabbitmq.service.RabbitMQConsumerService;
import com.you.rabbitmq.util.RabbitMQUtil;

@Service("rabbitMQConsumerService")
public class RabbitMQConsumerServiceImpl implements RabbitMQConsumerService {

    @Override
    public String rabbitMQConsumer(String message) {
        // 获取连接
        Connection connection;
        try {
            System.out.println("-------------------rabbitmq消费者接受消息开始------------------");
            connection = RabbitMQUtil.getConnection();
            RabbitMQUtil.getConsumerRabbitMQ(connection);
            System.out.println("-------------------rabbitmq消费者接受消息结束------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

}
