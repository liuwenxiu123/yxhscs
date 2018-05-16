package hscsm.core.fnd.mq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Created by xuhuanjun on 2018/3/1.
 */
public class ReceiveLogsTopic {

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost(Constants.SQUENCE_HOST);
//		factory.setHost(Constants.PROT);
//		factory.setUsername(Constants.USER_NAME);
//		factory.setPassword(Constants.PASSWORD);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

//		channel.exchangeDeclare(
//				Constants.EXCHANGE_NAME,
//				Constants.EXCHANGE_TOPIC_TYPE
//		);// 声明topic类型的Exchange

		String queueName = "RENTCAR_FOR_ERP";// 定义队列名为“queue_topic_logs1”的Queue
		channel.queueDeclare(queueName, false, false, false, null);
		// String routingKeyOne = "*.error.two";// "error"路由规则
		// channel.queueBind(queueName, EXCHANGE_NAME, routingKeyOne);// 把Queue、Exchange及路由绑定
		String routingKeyTwo = "logs.*.one";// 通配所有logs下第三词（最后一个）词为one的消息
		channel.queueBind(queueName, Constants.ALIX_EXCHANGE_NAME, routingKeyTwo);

		System.out.println(" [*] Waiting for messages.");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(
				queueName,  //队列名字，即要从哪个队列中接收消息
				true, //是否自动确认，默认true
				consumer); //消费者，即谁接收消息

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
//			String routingKey = delivery.getEnvelope().getRoutingKey();

			System.out.println(Base64Utils.decryptBASE64(message));
		}
	}
}
