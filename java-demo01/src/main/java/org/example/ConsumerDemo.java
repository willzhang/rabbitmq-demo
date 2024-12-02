package org.example;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ConsumerDemo{
    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        // 设置接入点ip和端口，参照获取接入点说明。
        factory.setHost("192.168.72.16");
        factory.setPort(5672);
        // 设置用户名密码
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 开始消费消息。
        channel.queueDeclare("my_queue_01", true, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                System.out.println("consume message: "  + new String(body, "UTF-8"));
            }
        };
        channel.basicConsume("my_queue_01", true, consumer);
    }
}
