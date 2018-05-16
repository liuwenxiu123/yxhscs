package hscsm.core.fnd.mq.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by wangxu on 2018/3/2.
 */
public interface ImportDataService {

    public void sendAllFailImportDatas() throws Exception;

}
