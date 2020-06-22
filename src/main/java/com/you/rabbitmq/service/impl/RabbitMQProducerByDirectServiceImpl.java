package com.you.rabbitmq.service.impl;

import org.springframework.stereotype.Service;

import com.you.rabbitmq.service.RabbitMQProducerByDirectService;

@Service("rabbitMQProducerByDirectService")
public class RabbitMQProducerByDirectServiceImpl implements RabbitMQProducerByDirectService
{

    @Override
    public String rabbitMQProducerByDirect(String message)
    {
        return null;
    }

}
