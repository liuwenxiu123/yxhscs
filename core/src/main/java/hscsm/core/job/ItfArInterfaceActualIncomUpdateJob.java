package hscsm.core.job;

import com.hand.hap.job.AbstractJob;
import hscsm.core.itf.service.IItfArInterfaceService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 月末计算实收等字段
 * @author xuwang01@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/03/20
 */
@DisallowConcurrentExecution
public class ItfArInterfaceActualIncomUpdateJob extends AbstractJob {

    @Autowired
    IItfArInterfaceService iItfArInterfaceService;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {

        String dateMonth = context.getMergedJobDataMap().getString("dateMonth");
        iItfArInterfaceService.updateArActualIncom(dateMonth);
    }

    @Override
    public boolean isRefireImmediatelyWhenException() {
        return false;
    }

}
