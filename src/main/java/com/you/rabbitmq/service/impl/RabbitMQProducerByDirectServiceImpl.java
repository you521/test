package com.you.rabbitmq.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.you.rabbitmq.service.RabbitMQProducerByDirectService;
import com.you.rabbitmq.util.PropertiesUtil;
import com.you.rabbitmq.util.RabbitMQUtil;

@Service("rabbitMQProducerByDirectService")
public class RabbitMQProducerByDirectServiceImpl implements RabbitMQProducerByDirectService {

    @Override
    public List<String> rabbitMQProducerByDirect(String message) {
        List<String> list = new ArrayList<>();
        Connection connection = null;
        Channel channel = null;

        Map<String, Object> map = PropertiesUtil.getInstance().getPropMap();
        // 获取队列名称
        String queueName = (String)map.get("spring.rabbitmq.queue-name");
        System.out.println("队列名称-------->" + queueName);
        // 获取direct交换器名称
        String exchangeName = (String)map.get("spring.rabbitmq.exchange-direct");
        System.out.println("direct交换器名称-------->" + exchangeName);
        // 获取路由键
        String routingkey = (String)map.get("spring.rabbitmq.direct-routingkey");
        System.out.println("路由键名称-------->" + routingkey);
        try {
            // 获取连接
            connection = RabbitMQUtil.getConnection();
            // 创建信道
            channel = connection.createChannel();
            /**
             * 申明一个队列，如果这个队列不存在，将会被创建
             * 
             * @param queue
             *            队列名称 ; 数据类型：String
             * @param durable
             *            持久性：true队列会再重启过后存在，但是其中的消息不会存在; 数据类型：boolean
             * @param exclusive
             *            是否只能由创建者使用，其他连接不能使用(排他性); 数据类型：boolean
             * @param autoDelete
             *            是否自动删除（没有连接自动删除）; 数据类型：boolean
             * @param arguments
             *            队列的其他属性(构造参数); 数据类型：Map<String, Object>
             */
            channel.queueDeclare(queueName, true, false, false, null);
            /**
             * 申明一个交换器，如果这个队列不存在，将会被创建
             * 
             * @param exchange
             *            交换机名称 ; 数据类型：String
             * @param type
             *            交换器类型; 数据类型：String
             * @param durable
             *            持久性：是否开启持久化exchange; 数据类型：boolean
             * @param autoDelete
             *            是否自动删除（没有消费者连接自动删除）; 数据类型：boolean
             * @param arguments
             *            交换器的其他属性(构造参数); 数据类型：Map<String, Object>
             */
            channel.exchangeDeclare(exchangeName, "direct", true, false, null);
            /**
             * 队列绑定到交换机并指定rouing_key
             * 
             * @param queue
             *            队列名称; 数据类型：String
             * @param exchange
             *            交换机名称 ; 数据类型：String
             * @param routingKey
             *            路由key; 数据类型：String
             * @param arguments
             *            其他属性(构造参数); 数据类型：Map<String, Object>
             */
            channel.queueBind(queueName, exchangeName, routingkey, null);
            for (int i = 0; i < 5; i++) {
                String msg = message + "-" + i;
                /**
                 * 发布一条不用持久化的消息，且设置两个监听 当exchange的值为空字符串或者是amq.direct时，此时的交换器类型默认是direct类型
                 * 可以不用单独声明Exchange，也不用单独进行Binding，系统默认将queue名称作为RoutingKey进行了绑定
                 * 
                 * @param exchange
                 *            消息交换机名称,空字符串将使用直接交换器模式，发送到默认的Exchange=amq.direct;此状态下，RoutingKey默认和Queue名称相同
                 * @param routingKey
                 *            路由关键字
                 * @param mandatory
                 *            监听是否有符合的队列；当为true时，如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，那么会调用basic.return方法将消息返还给生产者
                 *            为false时出现上述情形broker会直接将消息扔掉
                 * @param immediate
                 *            监听符合的队列上是否有至少一个Consumer；为true时，如果exchange在将消息route到queue时发现对应的queue上没有消费者，那么这条消息不会放入队列中
                 *            当与消息routeKey关联的所有queue(一个或多个)都没有消费者时，该消息会通过basic.return方法返还给生产者
                 * @param BasicProperties
                 *            设置消息持久化：MessageProperties.PERSISTENT_TEXT_PLAIN是持久化；MessageProperties.TEXT_PLAIN是非持久化
                 * @param body
                 *            消息对象转换的byte[]
                 */
                channel.basicPublish(exchangeName, routingkey, true, false, null, msg.getBytes());
                list.add(msg);
            }
        } catch (Exception e) {
            System.out.println("消息推送失败----------->" + e);
            e.printStackTrace();
        } finally {
            try {
                channel.close();
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

}
