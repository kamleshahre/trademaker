package org.lifeform.mq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConnectionParameters;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

public class Main {

	public static void main (String[] args) {
		
		ConnectionParameters params = new ConnectionParameters();
		String userName = "guest";
		String password = "guest";
		params.setUsername(userName);
		params.setPassword(password);
		params.setVirtualHost("/");
		params.setRequestedHeartbeat(0);
		ConnectionFactory factory = new ConnectionFactory(params);
		
		String hostName = "dev.rabbitmq.com";
		
		Connection conn = null;
		Channel channel =null;
		try {
			conn = factory.newConnection(hostName, 5672);

		channel = conn.createChannel();
		
		String exchangeName = "MyExchange";
		String queueName = "MyQueue";
		
		channel.exchangeDeclare(exchangeName, "direct");
		channel.queueDeclare(queueName);
		String routingKey = "key";
		channel.queueBind(queueName, exchangeName, routingKey);
		
		byte[] messageBodyBytes = "Hello, world!".getBytes();
		channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);
		
		
		
		boolean noAck = false;
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, noAck, consumer);
		boolean inComplete =true;
		while (inComplete) {
		    QueueingConsumer.Delivery delivery;
		    try {
		        delivery = consumer.nextDelivery();
		        System.out.println(delivery.toString());
		    } catch (InterruptedException ie) {
		        continue;
		    }
		    // (process the message components ...)
		    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(null != channel)
				try {
					channel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (null != conn)
				try {
					conn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		
	}
}
