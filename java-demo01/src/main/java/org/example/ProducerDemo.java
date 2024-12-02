package org.example;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.HashMap;


public class ProducerDemo {
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

        channel.exchangeDeclare("my_exchange", BuiltinExchangeType.DIRECT, true);
        channel.queueDeclare("my_queue_01", true, false, false, null);
        channel.queueBind("my_queue_01", "my_exchange", "my_routing_key");
        // 开始发送消息。
        String message = "Hello World";
        channel.basicPublish("my_exchange","my_routing_key", null, message.getBytes("UTF-8"));
        System.out.println("produce message: " + message);
        connection.close();
    }
}
