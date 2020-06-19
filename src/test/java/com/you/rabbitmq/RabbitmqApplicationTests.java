package com.you.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.you.rabbitmq.service.RabbitMQConsumerService;
import com.you.rabbitmq.service.RabbitMQProducerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {

    @Autowired
    private RabbitMQConsumerService rabbitMQConsumerService;
    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;
    
    @Test
    public void testInsertUser1(){
        try 
        {
            rabbitMQProducerService.rabbitMQProducer("guilfFirstRabbitMQ2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertUser(){
        try 
        {
            rabbitMQConsumerService.rabbitMQConsumer("shaoyou");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
