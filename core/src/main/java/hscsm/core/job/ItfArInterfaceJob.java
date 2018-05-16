package hscsm.core.job;

import com.hand.hap.job.AbstractJob;
import hscsm.core.fnd.mq.service.ImportDataService;
import hscsm.core.itf.service.IItfArInterfaceService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuwang02@hand-china.com
 * @version 1.0
 * @name
 * @description
 * @date 2018/03/09
 */

@DisallowConcurrentExecution
public class ItfArInterfaceJob extends AbstractJob {

    @Autowired
    IItfArInterfaceService iItfArInterfaceService;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        // pageSize一次 最大导入的数据
        // scheme (B:代表正常推送方式,A:表示期初数据导入)
        String pageSize = context.getMergedJobDataMap().getString("pageSize");
        String scheme = context.getMergedJobDataMap().getString("scheme");
        iItfArInterfaceService.addCalcArInterface(Integer.valueOf(pageSize),scheme, null);
    }

    @Override
    public boolean isRefireImmediatelyWhenException() {
        return false;
    }

}
