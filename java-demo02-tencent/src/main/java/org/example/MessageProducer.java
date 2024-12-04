package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
//import com.tencent.tdmq.demo.cloud.Constant;

/**

 * 消息生产者
 */
public class MessageProducer {

    /**

     * 交换机名称
     */
    private static final String EXCHANGE_NAME = "my_exchange_fanout";

    public static void main(String[] args) throws Exception {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务地址 (完整复制控制台接入点地址)
        factory.setUri("amqp://192.168.72.16");
        // 设置Virtual Hosts (开源 RabbitMQ 控制台复制完整Vhost名称)
        factory.setVirtualHost("/");
        // 设置用户名 (开源 RabbitMQ 控制台中Vhost的配置权限中的user名称)
        factory.setUsername("guest");
        // 设置密码 (对应user的密钥)
        factory.setPassword("guest");
        // 获取连接、建立通道
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            // 绑定消息交换机 (EXCHANGE_NAME必须在消息队列RabbitMQ版控制台上已存在，并且Exchange的类型与控制台上的类型一致)
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            for (int i = 0; i < 10; i++) {
                String message = "this is rabbitmq message " + i;
                // 发布消息到交换机，交换机自动将消息投递到相应队列
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
                System.out.println(" [producer] Sent '" + message + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
