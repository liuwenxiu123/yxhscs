package hscsm.core.sum.account.job;

import com.hand.hap.job.AbstractJob;
import hscsm.core.sum.account.service.ISumAccountService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * EBS总账接口调用Job
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/7.
 */
@DisallowConcurrentExecution
public class SumAccountJob extends AbstractJob {

    private Logger logger = LoggerFactory.getLogger(hscsm.core.sum.account.job.SumAccountJob.class);

    @Autowired
    private ISumAccountService ISumAccountService;

    public void safeExecute(JobExecutionContext context){
        try {
            this.logger.info("总账凭证接口调用job开始执行");
            String company = context.getMergedJobDataMap().getString("company");
            String eventBatchId = context.getMergedJobDataMap().getString("eventBatchId");
            String accountingDate = context.getMergedJobDataMap().getString("accountingDate");
            String batchName = context.getMergedJobDataMap().getString("batchName");

            ISumAccountService.sendSumAccountToEBS(company, eventBatchId, accountingDate, batchName);
            this.logger.info("总账凭证接口调用job执行结束");
        } catch (Exception e) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error(e.getMessage(), e);
            }
        }
    }

    public boolean isRefireImmediatelyWhenException() {
        return false;
    }
}