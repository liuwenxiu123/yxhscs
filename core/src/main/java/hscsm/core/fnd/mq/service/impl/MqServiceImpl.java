package hscsm.core.fnd.mq.service.impl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import hscsm.core.fnd.mq.service.MqService;
import hscsm.core.fnd.mq.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by wangxu on 2018/3/7.
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class MqServiceImpl implements MqService {

    private static ConnectionFactory factory = new ConnectionFactory();

    private static String OTHER_SYS = ""; // 待定


    @Value("${rabbitmq.addresses}")
    private String commonAddress;

    @Value("${rabbitmq.port}")
    private int port;

    // ================ALIX=====================
    @Value("${alix.rabbitmq.userName}")
    private String alxiName;

    @Value("${alix.rabbitmq.password}")
    private String alixPwd;

    @Value("${alix.rabbitmq.virtualHost}")
    private String alixVhost;

    @Value("${alix.rabbitmq.queueName}")
    private String alixQueueName;

    // ================进销存=====================
    @Value("${rentcar.rabbitmq.userName}")
    private String rencatName;

    @Value("${rentcar.rabbitmq.password}")
    private String rentcarPwd;

    @Value("${rentcar.rabbitmq.virtualHost}")
    private String rentcarVhost;

    @Value("${rentcar.rabbitmq.queueName}")
    private String rentcarQueueName;

    // ================贷后=====================
    @Value("${mal.rabbitmq.userName}")
    private String malName;

    @Value("${mal.rabbitmq.password}")
    private String malPwd;

    @Value("${mal.rabbitmq.virtualHost}")
    private String malVhost;

    @Value("${mal.rabbitmq.queueName}")
    private String malQueueName;

    // ================贷后=====================
    @Value("${opl.rabbitmq.addresses}")
    private String oplHost;

    @Value("${opl.rabbitmq.userName}")
    private String oplName;

    @Value("${opl.rabbitmq.password}")
    private String oplPwd;

    @Value("${opl.rabbitmq.virtualHost}")
    private String oplVhost;

    @Value("${opl.rabbitmq.queueName}")
    private String oplQueueName;


    @Override
    public void sendMsg(String sysName, String msg) throws IOException, TimeoutException {

        MqServiceImpl.factory.setHost(commonAddress);
        MqServiceImpl.factory.setPort(port);
        MqServiceImpl.factory.setAutomaticRecoveryEnabled(true); //设置网络异常重连
        MqServiceImpl.factory.setTopologyRecoveryEnabled(true);//设置重新声明交换器，队列等信息。
        if (Constants.ALIX_SYSTEM.equals(sysName)) {
            MqServiceImpl.factory.setUsername(alxiName);
            MqServiceImpl.factory.setPassword(alixPwd);
            MqServiceImpl.factory.setVirtualHost(alixVhost);
            this.sendAction(alixQueueName, msg);
        } else if (Constants.PSM_SYSTEM.equals(sysName)) {
            MqServiceImpl.factory.setUsername(rencatName);
            MqServiceImpl.factory.setPassword(rentcarPwd);
            MqServiceImpl.factory.setVirtualHost(rentcarVhost);
            this.sendAction(rentcarQueueName, msg);
        } else if (Constants.MAL_SYSTEM.equals(sysName)) {
            MqServiceImpl.factory.setUsername(malName);
            MqServiceImpl.factory.setPassword(malPwd);
            MqServiceImpl.factory.setVirtualHost(malVhost);
            this.sendAction(malQueueName, msg);
        } else if (Constants.OPL_SYSTEM.equals(sysName)) {
            MqServiceImpl.factory.setHost(oplHost);
            MqServiceImpl.factory.setUsername(oplName);
            MqServiceImpl.factory.setPassword(oplPwd);
            MqServiceImpl.factory.setVirtualHost(oplVhost);
            this.sendAction(oplQueueName, msg);
        }
    }

    private void sendAction(String quenceName, String msg) throws IOException, TimeoutException {

        Connection connection = null;
        Channel channel = null;
        String exchangeName = quenceName;
        connection = factory.newConnection();
        channel = connection.createChannel();

        // queueDeclare的参数：queue 队列名；
        // durable true为持久化；
        // exclusive 是否排外，true为队列只可以在本次的连接中被访问，
        // autoDelete true为connection断开队列自动删除；
        // arguments 用于拓展参数
        channel.queueDeclare(quenceName,
                true,
                false,
                false,
                null);
        msg = msg.replaceAll("[\\s*\t\n\r]", "");
        channel.basicPublish("", quenceName, null, msg.getBytes("utf-8"));
        if (channel != null) {
            channel.close();
            if (connection != null)
                connection.close();
        }

    }
}
