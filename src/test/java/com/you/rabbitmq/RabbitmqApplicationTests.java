package com.you.rabbitmq;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.you.rabbitmq.service.RabbitMQConsumerService;
import com.you.rabbitmq.service.RabbitMQProducerByDirectService;
import com.you.rabbitmq.service.RabbitMQProducerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {

    @Autowired
    private RabbitMQConsumerService rabbitMQConsumerService;
    @Autowired
    private RabbitMQProducerService rabbitMQProducerService;
    @Autowired
    private RabbitMQProducerByDirectService rabbitMQProducerByDirectService;

    // @Test
    // public void testInsertUser1() {
    // try {
    // rabbitMQProducerService.rabbitMQProducer("guilfFirstRabbitMQ2");
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    // @Test
    // public void testInsertUser() {
    // try {
    // String name = rabbitMQConsumerService.rabbitMQConsumer("shaoyou");
    // System.out.println("name-------->" + name);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    @Test
    public void testInsertUser1() {
        try {
            List<String> list = rabbitMQProducerByDirectService.rabbitMQProducerByDirect("jiashaoyou");
            System.out.println("返回list为---------->" + list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
