package com.healthsystem.util;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public class RabbitMQConnectionManager {

	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private String queueName;

	public RabbitMQConnectionManager(String queueName)
			throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {

		this.queueName = queueName;
		String uri = "amqp://aovdehgg:qTbF0k3KF2O2GRn9WSOwcwlvaevLhkFJ@buffalo.rmq.cloudamqp.com/aovdehgg";

		factory = new ConnectionFactory();
		factory.setUri(uri);

		// Recommended settings
		factory.setRequestedHeartbeat(30);
		factory.setConnectionTimeout(30000);

		connection = factory.newConnection();
		channel = connection.createChannel();

		String queue = queueName; // queue name
		boolean durable = true; // durable - RabbitMQ will never lose the
								// queue if a crash occurs
		boolean exclusive = false; // exclusive - if queue only will be used
									// by one connection
		boolean autoDelete = false; // autodelete - queue is deleted when
	      							// last consumer unsubscribes
		
		channel.queueDeclare(queue, durable, exclusive, autoDelete, null);

	}

	public void publish(String key, String message) throws IOException, TimeoutException {

		
		channel.basicPublish("", queueName, false, false, null, message.getBytes());
		closeAll();

	}

	private void closeAll() throws IOException, TimeoutException {
		channel.close();
		connection.close();
	}

	public void consume() throws IOException, TimeoutException {
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		
		channel.basicConsume(queueName, false, consumer);
		
		closeAll();

	}
}
