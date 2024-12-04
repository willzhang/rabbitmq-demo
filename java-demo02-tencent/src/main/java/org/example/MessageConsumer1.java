package org.example;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
//import com.tencent.tdmq.demo.cloud.Constant;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**

 * 消息消费者
 */
public class MessageConsumer1 {

    /**

     * 队列名称
     */
    public static final String QUEUE_NAME = "my_queue_02";

    /**

     * 交换机名称
     */
    private static final String EXCHANGE_NAME = "my_exchange_fanout";

    public static void main(String[] args) throws Exception {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务地址 (完整复制控制台接入点地址)
        factory.setUri("amqp://192.168.72.16");
        // 设置Virtual Hosts (开源 RabbitMQ 控制台中复制完整Vhost名称)
        factory.setVirtualHost("/");
        // 设置用户名 (开源 RabbitMQ 控制台中Vhost的配置权限中的user名称)
        factory.setUsername("guest");
        // 设置密码 (对应user的密钥)
        factory.setPassword("guest");
        // 获取连接
        Connection connection = factory.newConnection();
        // 建立通道
        Channel channel = connection.createChannel();
        // 绑定消息交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明队列信息
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 绑定消息交换机 (EXCHANGE_NAME必须在消息队列RabbitMQ版控制台上已存在，并且Exchange的类型与控制台上的类型一致)
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        System.out.println(" [Consumer1] Waiting for messages.");
        // 订阅消息
        channel.basicConsume(QUEUE_NAME, false, "ConsumerTag", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                //接收到的消息，进行业务逻辑处理。
                System.out.println("Received： " + new String(body, StandardCharsets.UTF_8) + ", deliveryTag: " + envelope.getDeliveryTag() + ", messageId: " + properties.getMessageId());
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
