package hscsm.core.sum.account.controllers;

import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.IRequestListener;
import com.hand.hap.core.impl.DefaultRequestListener;
import com.hand.hap.job.dto.JobCreateDto;
import com.hand.hap.job.dto.JobData;
import com.hand.hap.job.exception.JobException;
import com.hand.hap.job.service.IQuartzService;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import hscs.ae.dto.AeEventBatches;
import hscs.ae.dto.AeFeedbackMessage;
import hscs.ae.exception.AeEventProcessException;
import hscs.ae.mapper.AeEventBatchesMapper;
import hscs.ae.service.IAeEventProcessService;
import hscsm.core.ae.mapper.YxAeEventProcessMapper;
import hscsm.core.sum.account.dto.EbsReturnData;
import hscsm.core.sum.account.dto.ResultMessage;
import hscsm.core.sum.account.service.IRefreshMappingRedisService;
import hscsm.core.sum.account.service.ISumAccountService;
import org.quartz.SchedulerException;
import org.quartz.ee.jmx.jboss.QuartzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/8.
 */
@Controller
@RequestMapping("/api/public")
public class SumAccountController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(hscsm.core.sum.account.job.SumAccountJob.class);

    @Autowired
    private ISumAccountService sumAccountService;

    @Autowired
    private IQuartzService quartzService;

    @Autowired
    private IRefreshMappingRedisService refreshMappingRedisService;

    @Autowired
    private AeEventBatchesMapper aeEventBatchesMapper;

    @Autowired
    private YxAeEventProcessMapper yxAeEventProcessMapper;

    @Autowired
    private IAeEventProcessService aeEventProcessService;

    /**
     * TODO  测试用接口 项目上线删除
     * @param company 公司段值
     * @param eventBatchId 事件批次ID
     * @param accountingDate 入账截止日期
     * @param batchName 日记账批名
     * @return 执行结果
     */
    @RequestMapping(value = "/sumAccountPut",method = RequestMethod.GET)
    @ResponseBody
    public ResponseData sumAccountPut(@RequestParam(required = false) String company,
                                      @RequestParam(required = false) String eventBatchId,
                                      @RequestParam(required = false) String accountingDate,
                                      @RequestParam(required = false) String batchName) {
        logger.info("-----------------------------------总账接口调用-无权限接口-----------------------------------------");

        if(company != null && company.trim().isEmpty()){
            company = null;
        }
        if(eventBatchId != null && eventBatchId.trim().isEmpty()){
            eventBatchId = null;
        }
        if(accountingDate != null && accountingDate.trim().isEmpty()){
            accountingDate = null;
        }
        if(batchName != null && batchName.trim().isEmpty()){
            batchName = null;
        }
        sumAccountService.sendSumAccountToEBS(company, eventBatchId, accountingDate, batchName);
        return new ResponseData();
    }

    /**
     * 异步的总账凭证接口调用结果信息发布
     *
     * @param aeFeedbackMessages 总账验证结果信息
     * @return 请求结果
     */
    @RequestMapping(value = "/sumAccountResult",method = RequestMethod.POST)
    @ResponseBody
    public EbsReturnData sumAccountResult(@RequestBody List<AeFeedbackMessage> aeFeedbackMessages) {
        logger.info("--------------------总账结果发布-调用开始-----------------------");

        if(aeFeedbackMessages.size() == 0){
            EbsReturnData ebsReturnData = new EbsReturnData();
            ebsReturnData.setStatus("E");
            ebsReturnData.setMessage("请求参数错误");
            return ebsReturnData;
        }
        EbsReturnData ebsReturnData = new EbsReturnData();
        try {
            ebsReturnData = sumAccountService.insertByAsynResult(aeFeedbackMessages);
        } catch (Exception e) {
            ebsReturnData.setHapGroupId(aeFeedbackMessages.get(0).getAttribute1());
            List<ResultMessage> resultMessages = new ArrayList<>();
            String status = "E";

            for (AeFeedbackMessage feedbackMessage : aeFeedbackMessages) {
                ResultMessage resultMessage = new ResultMessage();
                resultMessage.setAccountingStatus(feedbackMessage.getAccountingStatus());
                resultMessage.setHapGlId(feedbackMessage.getTfrSumAccountId().toString());
                resultMessage.setxErrorMessage(e.getMessage());
                resultMessage.setxReturnStatus("E");
                resultMessages.add(resultMessage);
            }
            ebsReturnData.setMessage(e.getMessage());
            ebsReturnData.setStatus(status);
            ebsReturnData.setRows(resultMessages);

            if (this.logger.isErrorEnabled()) {
                this.logger.error(e.getMessage());
            }
        }

        return ebsReturnData;
    }

    /**
     * 刷新接口缓存
     * TODO  开发阶段临时接口 项目上线删除
     * @param interfaceName 刷新的接口名
     * @return 刷新结果
     */
    @RequestMapping(value = "/refreshRedis",method = RequestMethod.GET)
    @ResponseBody
    public ResponseData refreshRedis(@RequestParam(required = false) String interfaceName) {
        refreshMappingRedisService.refreshRedis(interfaceName);
        ResponseData responseData = new ResponseData(true);
        responseData.setMessage("缓存Job执行完毕");
        return responseData;
    }


//    /**
//     * 刷新所有缓存
//     * @return 刷新结果
//     */
//    @RequestMapping(value = "/refreshAllCache",method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseData refreshAllCache() {
//        logger.info("刷新所有缓存");
//        RefreshCache refreshKeyCache = new RefreshCache();
//        refreshKeyCache.doBatchDel();
//        ResponseData responseData = new ResponseData(true);
//        responseData.setMessage("所有缓存刷新接口执行完毕");
//        return responseData;
//    }

    /**
     * 刷新所有缓存
     */
    @RequestMapping(value = "/testAe",method = RequestMethod.GET)
    @ResponseBody
    public void test() throws AeEventProcessException, CodeRuleException {
        String name = "合同生效回填明细";
        AeEventBatches aeEventBatches = new AeEventBatches();
        aeEventBatches.setEventBatchName(name);
        aeEventBatches = aeEventBatchesMapper.selectOne(aeEventBatches);
        String basicTable = yxAeEventProcessMapper.getBasicTableByCategory(name);
        aeEventBatches.setBasicTable(basicTable);
        List<String> referenceList = yxAeEventProcessMapper.getReferenceList(5,2,"2013-01-01",
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
        Integer i = referenceList.size();
        this.logger.info(i.toString());
        for(String reference : referenceList){
            references.append(reference).append(",");
        }

        references.deleteCharAt(references.length() - 1);
        this.logger.info("拼接的字符串：" + references);

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Date accountingDate = null;
        try {
            accountingDate = sdf.parse("2013-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

        String result = aeEventProcessService.entryToTransfer(iRequest,aeEventBatches,accountingDate,references.toString(),programType);

        this.logger.info("账务创建完成，批次号[" + result + "]");
    }

    /**
     * TODO  测试用接口 项目上线删除
     * @param batch 公司段值
     * @param eventBatchName 事件批次ID
     * @param accountingDateTo 入账截止日期
     * @return 执行结果
     */
    @RequestMapping(value = "/createJobByBatch",method = RequestMethod.GET)
    @ResponseBody
    public ResponseData jobCreate(@RequestParam(required = false,defaultValue = "20") String batch,
                                  @RequestParam(required = false) String eventBatchName,
                                  @RequestParam(required = false) String accountingDateTo) throws JobException, SchedulerException, ClassNotFoundException {
        logger.info("并发JOB创建:batch["+ batch +"];eventBatchName["+ eventBatchName +"];accountingDateTo["+ accountingDateTo +"]");

        JobCreateDto jobCreateDto = new JobCreateDto();

        int totalBatch = Integer.valueOf(batch);

        for (int batchMod = 0; batchMod < totalBatch; batchMod ++){
            jobCreateDto.setJobClassName("hscsm.core.ae.job.YxAeEventProcessJob");
            jobCreateDto.setJobName(UUID.randomUUID().toString());
            jobCreateDto.setDescription(accountingDateTo + "账务创建-" + eventBatchName + "-" + batch + "-" + batchMod);

            List<JobData> jobDataList = new ArrayList<>();
            JobData jobDataBatch = new JobData();
            jobDataBatch.setName("batch");
            jobDataBatch.setValue(batch);

            JobData jobDataEventBatchName = new JobData();
            jobDataEventBatchName.setName("eventBatchName");
            jobDataEventBatchName.setValue(eventBatchName);

            JobData jobDataAccountingDateTo = new JobData();
            jobDataAccountingDateTo.setName("accountingDateTo");
            jobDataAccountingDateTo.setValue(accountingDateTo);

            JobData jobDataBatchMod = new JobData();
            String batchModStr = String.valueOf(batchMod);
            jobDataBatchMod.setName("batchMod");
            jobDataBatchMod.setValue(batchModStr);

            jobDataList.add(jobDataBatch);
            jobDataList.add(jobDataEventBatchName);
            jobDataList.add(jobDataAccountingDateTo);
            jobDataList.add(jobDataBatchMod);

            jobCreateDto.setJobDatas(jobDataList);
            jobCreateDto.setJobGroup("YxAeEventProcessJob");
            jobCreateDto.setTriggerGroup("YxAeEventProcessJob");
            jobCreateDto.setTriggerName(jobCreateDto.getJobName() + "_trigger");
            //类型为：简单的job类
            jobCreateDto.setTriggerType("SIMPLE");
            jobCreateDto.setRepeatCount("1");
            jobCreateDto.setRepeatInterval("60");
            //创建一个定时job用于刷新映射数据列
            quartzService.createJob(jobCreateDto);
        }

        return new ResponseData();
    }
}