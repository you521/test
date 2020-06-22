package com.you.rabbitmq.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 
 * @author Administrator rabbitmq工具类
 */

public class RabbitMQUtil {
    
    
    
    // ==========================测试环境================================
//    private final static String MQ_IP = "49.232.136.165";
//    private final static Integer MQ_PORT = 5672;
//    private final static String MQ_USERNAME = "admin";
//    private final static String MQ_PASSWORD = "admin";
    private final static String QUEUE_NAME = "mq_test";

    /**
     * rabbitmq获取连接
     * 
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        Map<String, Object> map = PropertiesUtil.getInstance().getPropMap();
        // 创建工厂连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置rabbitmq服务器的IP地址
        connectionFactory.setHost((String)map.get("spring.rabbitmq.host"));
        // 设置服务器端口号
        connectionFactory.setPort(Integer.parseInt(map.get("spring.rabbitmq.port").toString()));
        // 设置服务器用户名
        connectionFactory.setUsername((String)map.get("spring.rabbitmq.username"));
        // 设置服务器密码
        connectionFactory.setPassword((String)map.get("spring.rabbitmq.password"));
        return connectionFactory.newConnection();
    }

    /**
     * 创建生产者
     * 
     * @param connection
     * @param message
     * @throws TimeoutException
     * @throws IOException
     */
    public static void getProducerRabbitMQ(Connection connection, String message) throws Exception {
        System.out.println("发送消息为-------->" + message);
        // 创建一个通道
        Channel channel = null;
        try {
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
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            /**
             * 发布一条不用持久化的消息，且设置两个监听
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

            // 当exchange的值为空字符串或者是amq.direct时，此时的交换器类型默认是direct类型
            // 可以不用单独声明Exchange，也不用单独进行Binding，系统默认将queue名称作为RoutingKey进行了绑定
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭信道
            channel.close();
            // 关闭连接
            connection.close();
        }
    }

    /**
     * 创建消费者
     * 
     * @param connection
     */
    public static void getConsumerRabbitMQ(Connection connection) {
        // 创建一个通道
        Channel channel;
        try {
            channel = connection.createChannel();
            // 申明一个队列，如果这个队列不存在，将会被创建
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            // 设置客户端最多接收未被确认(ack)的消息的个数
            channel.basicQos(1);
            // DefaultConsumer类实现了Consumer接口，通过传入一个频道，告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                    // 将字节数据转成string字符串
                    String message = new String(body, "UTF-8");
                    System.out.println("消费的消息ID为-------->" + envelope.getDeliveryTag());
                    System.out.println("消费的消息为-------->" + message);
                    /**
                     * channel.basicAck() 参数： deliveryTag：该消息的index multiple：是否批量处理.true:将一次性ack所有小于deliveryTag的消息
                     * 
                     * consumer处理成功后，通知broker删除队列中的消息，如果设置multiple=true，表示支持批量确认机制以减少网络流量 例如：有值为5,6,7,8 deliveryTag的投递
                     * 如果此时channel.basicAck(8, true);则表示前面未确认的5,6,7投递也一起确认处理完毕 如果此时channel.basicAck(8,
                     * false);则仅表示deliveryTag=8的消息已经成功处理
                     */
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    try {
                        // 模拟消费等待
                        TimeUnit.SECONDS.sleep(5L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            /**
             * channel.basicConsume() RabbitMQ中的消费者的消息确认机制, 注意：执行这个方法会回调handleConsumeOk方法,在此方法中处理消息。
             * 
             * @param queue
             *            队列名称
             * @param autoAck
             *            是否自动应答;false表示consumer在成功消费过后必须要手动回复一下服务器，如果不回复，服务器就将认为此条消息消费失败，继续分发给其他consumer
             *            如果不自动ack，需要使用channel.ack、channel.nack、channel.basicReject进行消息应答
             * @param callback
             *            回调方法类
             * @return 由服务器生成的consumertag
             */
            channel.basicConsume(QUEUE_NAME, false, consumer);
            // 等待回调函数执行完毕后，关闭资源
            TimeUnit.SECONDS.sleep(5L);
            channel.close();
            connection.close();
            // 创建消费者对象，用于读取消息
            // QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
            // channel.basicConsume(QUEUE_NAME, true, queueingConsumer);
            // 读取队列，并且阻塞，在读到消息之前在这里阻塞，知道等到消息，完成消息的阅读后，继续阻塞循环
            // while(true)
            // {
            // QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            // String message = new String(delivery.getBody());
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
