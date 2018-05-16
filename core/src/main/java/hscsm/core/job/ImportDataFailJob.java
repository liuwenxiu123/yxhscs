package hscsm.core.job;

import com.hand.hap.job.AbstractJob;
import hscsm.core.fnd.mq.service.ImportDataService;
import hscsm.core.fnd.mq.service.impl.ImportDataServiceImpl;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author xuwang02@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/03/02
 */
@DisallowConcurrentExecution
public class ImportDataFailJob extends AbstractJob {

    Logger logger = LoggerFactory.getLogger(ImportDataFailJob.class);

    @Autowired
    ImportDataService importDataService;

    @Override
    public void safeExecute(JobExecutionContext context) {
        try {
            importDataService.sendAllFailImportDatas();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public boolean isRefireImmediatelyWhenException() {
        return false;
    }

}
