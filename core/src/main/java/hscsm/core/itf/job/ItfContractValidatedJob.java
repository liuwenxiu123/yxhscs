package hscsm.core.itf.job;

import com.hand.hap.job.AbstractJob;
import hscsm.core.itf.service.IItfContractValidatedService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 合同取消红冲凭证生成Job
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/3.
 */
@DisallowConcurrentExecution
public class ItfContractValidatedJob extends AbstractJob {

    private Logger logger = LoggerFactory.getLogger(hscsm.core.itf.job.ItfContractValidatedJob.class);

    @Autowired
    private IItfContractValidatedService itfContractValidatedService;


    public void safeExecute(JobExecutionContext context){
        try {
            this.logger.info("合同取消红冲凭证生成Job执行");

            itfContractValidatedService.updateByStatus();
        } catch (Exception var4) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error(var4.getMessage(), var4);
            }
        }
    }

    public boolean isRefireImmediatelyWhenException() {
        return false;
    }

}
