package hscsm.core.ae.job;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.IRequestListener;
import com.hand.hap.core.impl.DefaultRequestListener;
import com.hand.hap.job.AbstractJob;
import hscs.ae.dto.AeEventBatches;
import hscs.ae.mapper.AeEventBatchesMapper;
import hscs.ae.service.IAeEventProcessService;
import hscsm.core.ae.mapper.YxAeEventProcessMapper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 目标表账务创建job，支持多线程
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/19.
 */
@DisallowConcurrentExecution
public class YxAeEventProcessJob extends AbstractJob {

    private Logger logger = LoggerFactory.getLogger(YxAeEventProcessJob.class);

    @Autowired
    private AeEventBatchesMapper aeEventBatchesMapper;

    @Autowired
    private YxAeEventProcessMapper yxAeEventProcessMapper;

    @Autowired
    private IAeEventProcessService aeEventProcessService;

    public YxAeEventProcessJob() {
    }

    public void safeExecute(JobExecutionContext context) throws Exception {
        String batch = context.getMergedJobDataMap().getString("batch");
        String batchMod = context.getMergedJobDataMap().getString("batchMod");
        String eventBatchName = context.getMergedJobDataMap().getString("eventBatchName");
        String accountingDateTo = context.getMergedJobDataMap().getString("accountingDateTo");

        // 运行计时
        long startTime = System.currentTimeMillis();

        boolean isParamsError = (batch == null || "".equals(batch.trim()) ||
                batchMod == null || "".equals(batchMod.trim()) ||
                eventBatchName == null || "".equals(eventBatchName.trim()) ||
                accountingDateTo == null || "".equals(accountingDateTo.trim())
        );

        if(isParamsError) {
            this.logger.error("账务创建job参数不齐，请检查是否正确填写所有参数");
            throw new Exception("账务创建job参数不齐，请检查是否正确填写所有参数");
        }

        this.logger.info(eventBatchName + " 账务创建开始执行:batch[" + batch + "];batchMod[" + batchMod + "];Date["+ accountingDateTo +"]");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
            Date accountingDate = sdf.parse(accountingDateTo);

            int batchInt = Integer.parseInt(batch);
            int batchModInt = Integer.parseInt(batchMod);

            // job无法获取用户信息，写死用户信息
            IRequestListener iRequestListener = new DefaultRequestListener();
            IRequest iRequest = iRequestListener.newInstance();
            iRequest.setUserId(10001L);
            iRequest.setUserName("admin");
            iRequest.setRoleId(10001L);
            iRequest.setEmployeeCode("ADMIN");
            iRequest.setLocale("zh_CN");
            iRequest.setCompanyId(-1L);
            Long[] roleIds = {-10001L};
            iRequest.setAllRoleId(roleIds);

            String programType = "NEW_RUN";

            AeEventBatches aeEventBatches = new AeEventBatches();
            aeEventBatches.setEventBatchName(eventBatchName);
            aeEventBatches = aeEventBatchesMapper.selectOne(aeEventBatches);

            String basicTable = yxAeEventProcessMapper.getBasicTableByCategory(eventBatchName);
            if (basicTable == null){
                throw new Exception("未找到日记账批名对应的目标表");
            }
            aeEventBatches.setBasicTable(basicTable);
            // 获取符合条件的单据编码集合
            List<String> referenceList = yxAeEventProcessMapper.getReferenceList(batchInt,batchModInt,accountingDateTo,
                    aeEventBatches.getBasicTable(),
                    aeEventBatches.getAccStatusField(),
                    aeEventBatches.getAccDateField(),
                    aeEventBatches.getReferenceField(),
                    aeEventBatches.getPrimaryKeyField());

            if(referenceList == null || referenceList.size() == 0){
                this.logger.info("没有符合条件的账务，job执行结束");
                return;
            }

            StringBuilder references = new StringBuilder();
            for(String reference : referenceList){
                references.append(reference).append(",");
            }

            references.deleteCharAt(references.length() - 1);
            String result = aeEventProcessService.entryToTransfer(iRequest,aeEventBatches,accountingDate,references.toString(),programType);
            this.logger.info("账务创建完成，批次号[" + result + "]");
            long endTime = System.currentTimeMillis();
            long total = endTime - startTime;
            this.logger.info("本次账务创建执行条数:" + referenceList.size());
            this.logger.info("用时:" + total + "ms");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public boolean isRefireImmediatelyWhenException() {
        return false;
    }
}
