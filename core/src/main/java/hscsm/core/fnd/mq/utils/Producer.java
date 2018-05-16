package hscsm.core.fnd.mq.utils;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import hscsm.core.fnd.mq.dto.FeedMessageDto;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by wangxu on 2018/3/6.
 */
public class Producer {
    private final static String QUEUE_NAME = "ALIX_SYSTEM";// alix_erp
//    private final static String QUEUE_NAME = "RENTCAR_FOR_ERP";// 进销存
//    private final static String QUEUE_NAME = "FINALOAN_ERP";// 贷后
//    private final static String QUEUE_NAME = "OPL_SYSTEM";// 经租

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {

        /* 使用工厂类建立Connection和Channel，并且设置参数 */
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("192.168.144.24");// MQ的IP
        factory.setHost("192.168.145.161");// MQ的IP
        factory.setPort(5672);// MQ端口


        factory.setUsername("guest");// MQ用户名
        factory.setPassword("guest");// MQ密码
        factory.setVirtualHost("alix_erp");


//        factory.setUsername("rentcar_admin");// MQ用户名
//        factory.setPassword("rentcar");// MQ密码
//        factory.setVirtualHost("rentcar");


//        factory.setUsername("rentcar_admin");// MQ用户名
//        factory.setPassword("rentcar");// MQ密码
//        factory.setVirtualHost("rentcar");



//        factory.setUsername("guest");// MQ用户名
//        factory.setPassword("guest");// MQ密码
//        factory.setVirtualHost("alix_erp");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
//        channel.exchangeDeclare(Constants.OPL_SYSTEM , "topic");

        /* 创建消息队列，并且发送消息 */
        channel.queueDeclare(QUEUE_NAME,
                false,
                false,
                false,
                null);
        String a ="W3sic291cmNlU3lzdGVtIjoiQUxJWF9TWVNURU0iLCJpbnRlcmZhY2VOYW1lIjoiQUxJWF9BUl9J\n" +
                "TlRFUkZBQ0UiLCJiYXRjaE51bSI6IkFMSVgxODAzMTUwMjg2OTQiLCJ1bmlxdWVDb2RlIjoiNjc2\n" +
                "QUU2QzdCNTc2N0M3MUUwNTMwQTlBQThDMEM2OEQiLCJzeW5jU3RhdHVzIjoiRSIsImVycm9yQ29k\n" +
                "ZSI6bnVsbCwiZXJyb3JNZXNzYWdlIjoi19a2zqG+vMa7rru5v+7I1cbaob/U2r3Tv9q2qNLltKbO\n" +
                "qrHYyuTX1rbOo6yyu8Tczqq/1SJ9LHsic291cmNlU3lzdGVtIjoiQUxJWF9TWVNURU0iLCJpbnRl\n" +
                "cmZhY2VOYW1lIjoiQUxJWF9BUl9JTlRFUkZBQ0UiLCJiYXRjaE51bSI6IkFMSVgxODAzMTUwMjg2\n" +
                "OTUiLCJ1bmlxdWVDb2RlIjoiNjc2QjBFRDg3NEJDN0QwNkUwNTMwQTlBQThDMDNGNEIiLCJzeW5j\n" +
                "U3RhdHVzIjoiRSIsImVycm9yQ29kZSI6bnVsbCwiZXJyb3JNZXNzYWdlIjoi19a2zqG+vMa7rru5\n" +
                "v+7I1cbaob/U2r3Tv9q2qNLltKbOqrHYyuTX1rbOo6yyu8Tczqq/1SJ9XQ==\n";
        channel.basicPublish("", QUEUE_NAME, null, a.getBytes());
        System.out.println("生产了个'" + Base64Utils.decryptBASE64(a) + "'");
//        /* 关闭连接 */
        channel.close();
        connection.close();
    }
}
