package hscsm.core.job;

import com.hand.hap.job.AbstractJob;
import hscsm.core.itf.service.IItfArInterfaceService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuwang01@hand-china.com
 * @version 1.0
 * @name
 * @description 月末更新还款状态
 * @date 2018/03/19
 */
@DisallowConcurrentExecution
public class ItfArInterfaceMonthUpdateJob extends AbstractJob {

    @Autowired
    IItfArInterfaceService iItfArInterfaceService;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        // 获取参数日期
        String deadDate = context.getMergedJobDataMap().getString("deadDate");
        iItfArInterfaceService.updateArInterfaceMonthEnd(deadDate);

    }

    @Override
    public boolean isRefireImmediatelyWhenException() {
        return false;
    }

}
