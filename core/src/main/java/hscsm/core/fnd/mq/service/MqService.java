package hscsm.core.fnd.mq.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by wangxu on 2018/3/7.
 */
public interface MqService {

    public void sendMsg (String sysName, String msg) throws IOException, TimeoutException;

}
