package hscsm.core.api.controllers;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import hscsm.core.fnd.mq.utils.Base64Utils;
import hscsm.core.fnd.mq.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by cujintao on 2018/4/2.
 */
@Controller
@RequestMapping("/testMq")
public class MqProductor extends BaseController{
    Logger logger =LoggerFactory.getLogger(MqProductor.class);
    private static ConnectionFactory factory=new ConnectionFactory();

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

    // ================经租=====================
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

    @GetMapping({"/product"})
    @ResponseBody
    public ResponseData getParameter1(HttpServletRequest request)  throws IOException, TimeoutException {
        String sysName=request.getParameter("sysName");
        MqProductor.factory.setHost(commonAddress);
        MqProductor.factory.setPort(port);
        MqProductor.factory.setAutomaticRecoveryEnabled(true); //设置网络异常重连
        MqProductor.factory.setTopologyRecoveryEnabled(true);//设置重新声明交换器，队列等信息。
        String message=null;
        String message1=null;
        String quenceName=null;
        if (Constants.ALIX_SYSTEM.equals(sysName)) {
            MqProductor.factory.setUsername(alxiName);
            MqProductor.factory.setPassword(alixPwd);
            MqProductor.factory.setVirtualHost(alixVhost);
            message ="[{'sourceSystem':'ALIX_SYSTEM','interfaceName':'xx22','batchNum':'2','uniqueCode':null,'syncStatus':'E','errorCode':null,'errorMessage':'test'}]";
            quenceName=alixQueueName;
            try {
                message1= Base64Utils.encryptBASE64(message);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("mq消息base64转换失败!");
            }
        } else if (Constants.PSM_SYSTEM.equals(sysName)) {
            logger.error("mq消息进入进销存."+"sysName:"+sysName);
            MqProductor.factory.setUsername(rencatName);
            MqProductor.factory.setPassword(rentcarPwd);
            MqProductor.factory.setVirtualHost(rentcarVhost);
            quenceName=rentcarQueueName;
            message ="[{'sourceSystem':'PSM_SYSTEM','interfaceName':'xx22','batchNum':'2','uniqueCode':null,'syncStatus':'E','errorCode':null,'errorMessage':'test'}]";
            try {
                message1= Base64Utils.encryptBASE64(message);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("mq消息base64转换失败!");
            }
        } else if (Constants.MAL_SYSTEM.equals(sysName)) {
            MqProductor.factory.setUsername(malName);
            MqProductor.factory.setPassword(malPwd);
            MqProductor.factory.setVirtualHost(malVhost);
            quenceName=malQueueName;
            message ="[{'sourceSystem':'MAL_SYSTEM','interfaceName':'xx22','batchNum':'2','uniqueCode':null,'syncStatus':'E','errorCode':null,'errorMessage':'test'}]";
            try {
                message1= Base64Utils.encryptBASE64(message);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("mq消息base64转换失败!");
            }
        } else if (Constants.OPL_SYSTEM.equals(sysName)) {
            MqProductor.factory.setHost(oplHost);
            MqProductor.factory.setUsername(oplName);
            MqProductor.factory.setPassword(oplPwd);
            MqProductor.factory.setVirtualHost(oplVhost);
            quenceName=oplQueueName;
            message ="[{'sourceSystem':'OPL_SYSTEM','interfaceName':'xx22','batchNum':'2','uniqueCode':null,'syncStatus':'E','errorCode':null,'errorMessage':'test'}]";
            try {
                message1= Base64Utils.encryptBASE64(message);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("mq消息base64转换失败!");
            }
        }else {
            ResponseData responseData=new ResponseData();
            responseData.setMessage("无此系统类型");
            return responseData;
        }
        try{
            //创建连接工厂
        /*ConnectionFactory factory = new ConnectionFactory();*/
            //设置RabbitMQ相关信息
      /*  factory.setHost(host);
        factory.setUsername(userName);
        factory.setPassword(passWord);*/
        /*factory.setPort(port);*/
            //创建一个新的连接
            Connection connection = factory.newConnection();
            logger.debug("===================创建连接====================");
            //创建一个通道
            Channel channel = connection.createChannel();
            logger.debug("===================创建通道====================");
            //  声明一个队列        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
/*
        String message ="[{'sourceSystem':'MAL_SYSTEM','interfaceName':'xx22','batchNum':'2','uniqueCode':null,'syncStatus':'S','errorCode':null,'errorMessage':null}]";
*/
            //发送消息到队列中
            logger.debug("开始推送:"+"message"+message1+",quenceName:"+quenceName);
            channel.basicPublish("", quenceName, null, message1.getBytes("UTF-8"));
            logger.debug("推送完成");
        /*System.out.println("Producer Send +'" + message + "'");*/
            //关闭通道和连接
            channel.close();
            connection.close();
            logger.debug("关闭通道和连接");
        }catch (Exception e){
            logger.error("连接失败");
        }
        ResponseData responseData=new ResponseData();
        responseData.setMessage(message);
        return responseData;
    }
}
