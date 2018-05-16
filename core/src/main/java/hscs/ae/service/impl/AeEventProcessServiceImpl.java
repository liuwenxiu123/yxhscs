package hscs.ae.service.impl;

import com.hand.hap.code.rule.exception.CodeRuleException;
import com.hand.hap.code.rule.service.ISysCodeRuleProcessService;
import com.hand.hap.core.IRequest;
import hscs.ae.cache.*;
import hscs.ae.dto.*;
import hscs.ae.exception.AeEventProcessException;
import hscs.ae.mapper.*;
import hscs.ae.service.*;
import hscs.ae.util.AeConstant;
import hscs.ae.util.AeUtils;
import hscs.ae.util.DateUtils;
import hscs.utils.controllers.HscsUtil;
import hscs.utils.exception.CalculateException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static hscs.ae.exception.AeEventProcessException.EXCEPTION_CODE;
import static hscs.ae.exception.AeEventProcessException.THE_EVENT_BATCH_NOT_FROZEN;
import static hscs.ae.util.AeConstant.*;
import static hscs.utils.HscsItfUtils.removeDuplicate;
import static hscs.utils.controllers.HscsConstants.Public.Y;

/**
 * @author xin.tian01@hand-china.com
 * @version 1.0
 * @name AeEventProcessServiceImpl
 * @description 会计引擎生成转换实现类
 * @date 2018/1/25
 */

@Service
public class AeEventProcessServiceImpl implements IAeEventProcessService {

    @Autowired
    private AeEventHeadersMapper aeEventHeadersMapper;

    @Autowired
    private AeEventCategoryMapper aeEventCategoryMapper;

    @Autowired
    private AeUtils aeUtils;

    @Autowired
    private AeEventCategoryCache aeEventCategoryCache;

    @Autowired
    private AeDataConvertProcess aeDataConvertProcess;

    //期间缓存
    @Autowired
    private DefinationDateValueCache definationDateValueCache;

    @Autowired
    private AeEventSourcesMapper aeEventSourcesMapper;

    @Autowired
    private AeSourceSqlCache aeSourceSqlCache;

    @Autowired
    private AeSourceSqlHeadersMapper aeSourceSqlHeadersMapper;

    @Autowired
    private AeEventLineLovCache aeEventLineLovCache;

    @Autowired
    private AeAcctCategoryMapper aeAcctCategoryMapper;

    @Autowired
    private AeAcctCategoryCache aeAcctCategoryCache;

    @Autowired
    private AeTfrBatchEventsMapper aeTfrBatchEventsMapper;

    @Autowired
    private AeTfrEventsMapper aeTfrEventsMapper;

    @Autowired
    private ISysCodeRuleProcessService iSysCodeRuleProcessService;

    @Autowired
    private IAeFormulaLinesService iAeFormulaLinesService;

    @Autowired
    private AeComparisonDataCache aeComparisonDataCache;

    @Autowired
    private AeMappingDataCache aeMappingDataCache;

    @Autowired
    private AeRuleProcessUtil aeRuleProcessUtil;

    @Autowired
    private AeFilterDataCache aeFilterDataCache;

    @Autowired
    private AeTfrDtlAccountsMapper aeTfrDtlAccountsMapper;

    @Autowired
    private AeEventLinesMapper aeEventLinesMapper;

    @Autowired
    private IAeComparisonLinesService iAeComparisonLinesService;

    @Autowired
    private IAeTfrSumAccountsService iAeTfrSumAccountsService;

    @Autowired
    private AeAccountLinesMapper aeAccountLinesMapper;

    @Autowired
    private AeRuleHeadersMapper arhMapper;

    @Autowired
    private IAeFilterHeadersService aeFilterHeadersService;

    private final Logger logger = LoggerFactory.getLogger(AeEventProcessServiceImpl.class);

    /**
     * 界面账务生成
     *
     * @param iRequest
     * @param aeEventBatches
     * @param accountingDateTo
     * @param references       关联单据集合
     * @return
     * @throws AeEventProcessException
     * @throws CodeRuleException
     */
    @Override
    public String entryToTransfer(IRequest iRequest, AeEventBatches aeEventBatches, Date accountingDateTo, String references,String programType) throws AeEventProcessException, CodeRuleException {
        if (aeEventBatches == null) {
            throw new AeEventProcessException(EXCEPTION_CODE, "必须指定对应的事件定义才能创建账务", null);
        }

        if (AE_FROZEN_FLAG_N.equals(aeEventBatches.getFrozenFlag())) {
            throw new AeEventProcessException(EXCEPTION_CODE, THE_EVENT_BATCH_NOT_FROZEN, null);
        }

        //如果是E-HR会计接口数据新增特殊校验必须输入单据号
        if ("E-HR_SALARY_INFORMATION_V1.0".equals(aeEventBatches.getEventBatchName())) {
            if (references == null || "".equals(references)) {
                throw new AeEventProcessException(EXCEPTION_CODE, "E-HR会计引擎必须输入单号进行处理", null);
            }
        }
        //获取批次
        String batchNo = aeUtils.getAeBatchNum(iRequest);

        //获取事件头信息
        AeEventHeaders aeEventHeaders = new AeEventHeaders();
        aeEventHeaders.setEventBatchId(aeEventBatches.getEventBatchId());
        List<AeEventHeaders> aeEventHeadersList = aeEventHeadersMapper.select(aeEventHeaders);

        //校验会计分类和会计规则
        AeAcctCategory aeAcctCategory = new AeAcctCategory();
        List<AeAcctCategory> aeAcctCategoryList;
        for(AeEventHeaders eventHeaders:aeEventHeadersList){
            aeAcctCategory.setEventHeaderId(eventHeaders.getEventHeaderId());
            aeAcctCategoryList = aeAcctCategoryMapper.selectValidate(aeAcctCategory);
            if(aeAcctCategoryList == null || aeAcctCategoryList.size() <= 0){
                throw new AeEventProcessException(EXCEPTION_CODE, "子事件" + eventHeaders.getEventName() + "下不存在有效会计分类", null);
            }
            int i = 0;
            StringBuilder acctCategoryB = new StringBuilder();
            for(AeAcctCategory acctCategory: aeAcctCategoryList){
                AeRuleHeaders arhTemp = new AeRuleHeaders();
                arhTemp.setAcctCategoryId(acctCategory.getAcctCategoryId());
                List<AeRuleHeaders> arhList = arhMapper.selectValidate(arhTemp);
                if(arhList !=null && arhList.size()>0){
                    i++;
                }else{
                    acctCategoryB.append("、").append(acctCategory.getAcctCategoryName());
                }
            }
            if( i == 0){
                throw new AeEventProcessException(EXCEPTION_CODE, "子事件:" + eventHeaders.getEventName() + "," + "会计分类：" + acctCategoryB.toString().substring(1) + "下不存在有效会计规则", null);
            }
        }

        //获取事件来源
        AeEventCategory aeEventCategory = aeEventCategoryCache.getValue(aeEventBatches.getEventCategoryCode());

        if (aeEventCategory == null) {
            //数据库查询
            aeEventCategory = new AeEventCategory();
            aeEventCategory.setEventCategoryCode(aeEventBatches.getEventCategoryCode());
            aeEventCategory = aeEventCategoryMapper.selectOne(aeEventCategory);
            AeEventSources aeEventSources = new AeEventSources();
            aeEventSources.setEventCategoryId(aeEventCategory.getEventCategoryId());
            List<AeEventSources> aeEventSourcesList = aeEventSourcesMapper.select(aeEventSources);
            aeEventCategory.setAeEventSourcesList(aeEventSourcesList);
        }

        //获取入账基准表
        //获取入账基准表事件来源对象
        AeSourceSqlHeaders assq = aeSourceSqlCache.getValue(aeEventCategory.getBasicTable());
        if (assq == null) {
            //数据库查询
            AeSourceSqlHeaders sourceSqlHeaders = new AeSourceSqlHeaders();
            sourceSqlHeaders.setSqlCode(aeEventCategory.getBasicTable());
            assq = aeSourceSqlHeadersMapper.selectOne(sourceSqlHeaders);
        }


        List<Long> accountPrimaryValue = new ArrayList<>();
        //入账源数据
        List<Map> accountMap;

        //冲销
        if(AeConstant.REV_RUN.equals(programType)){
            accountMap = aeDataConvertProcess.getRevSource(aeEventBatches, assq, accountingDateTo, references);
            if (accountMap == null) {
                throw new AeEventProcessException(EXCEPTION_CODE, "获取不到满足条件的冲销源数据", null);
            }
        }else{
            accountMap = aeDataConvertProcess.getSource(aeEventBatches, assq, accountingDateTo, references);
            if (accountMap == null) {
                throw new AeEventProcessException(EXCEPTION_CODE, "获取不到满足条件的正向源数据", null);
            }
        }

        for (int i = 0; i < accountMap.size(); i++) {
            accountPrimaryValue.add(Long.valueOf(accountMap.get(i).get(aeEventBatches.getPrimaryKeyField()).toString()));
        }

        //获取事件来源数据
        HashMap<String, List<Map>> sourceMap = new HashMap<>();
        for (AeEventHeaders aeEventHeaders1 : aeEventHeadersList) {
            List<Map> sourceDataMap = aeDataConvertProcess.getEventSourceData(aeEventHeaders1, accountPrimaryValue,programType);
            if(sourceDataMap!=null && !sourceDataMap.isEmpty()) {
                sourceMap.put(aeEventHeaders1.getEventHeaderId().toString(), sourceDataMap);
            }else{
                throw new AeEventProcessException(EXCEPTION_CODE, "子事件["+aeEventHeaders1.getEventName()+"]获取不到满足条件的来源数据", null);
            }

        }

        if (sourceMap.isEmpty()) {
            throw new AeEventProcessException(EXCEPTION_CODE, "获取不到满足条件的来源数据", null);
        }

        //生成事件与子事件
        generateTwoTfr(iRequest, aeEventBatches, assq.getTableName(), aeEventHeadersList, accountMap, sourceMap, batchNo, references,programType);

        //验证子事件
        return batchNo;
    }

    /**
     * 生成事件与子事件数据
     *
     * @param aeb
     * @param map
     * @param batchNo 批次号
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void generateTwoTfr(IRequest iRequest, AeEventBatches aeb, String baiscTable, List<AeEventHeaders> aeEventHeadersList, List<Map> accountMap, HashMap<String, List<Map>> map, String batchNo, String references,String programType) throws CodeRuleException, AeEventProcessException {
       /* asoMapper.autocommit();
        asoMapper.last_txc_xid();*/
        Map<String, Map<Long, List<Map>>> mapHashMap = new HashMap<>();
        Map<String, Map<Long, Map>> mapSourceHashMap = new HashMap<>();
        for (int n = 0; n< aeEventHeadersList.size();n++) {
            AeEventHeaders aeEventHeaders = aeEventHeadersList.get(n);
            Map<Long, Map> sourceMap1 = new HashMap<>();
            Map<Long, List<Map>> sourceMap = new HashMap<>();
            List<Map> listMap = map.get(aeEventHeaders.getEventHeaderId().toString());
            if(listMap != null && !listMap.isEmpty()) {
                for (int j = 0; j < accountMap.size(); j++) {
                    List<Map> sourceListMap = new ArrayList<>();
                    for (int i = 0; i < listMap.size(); i++) {
                        if (listMap.get(i).get(aeEventHeaders.getAccountPrimaryField()).equals(accountMap.get(j).get(aeEventHeaders.getAccountPrimaryField()))) {
                            sourceListMap.add(listMap.get(i));
                        }
                    }
                    sourceMap.put(Long.valueOf(accountMap.get(j).get(aeEventHeaders.getAccountPrimaryField()).toString()), sourceListMap);
                }
                for (int k = 0; k < listMap.size(); k++) {
                    sourceMap1.put(Long.valueOf(listMap.get(k).get(aeEventHeaders.getEventSourcePrimaryField()).toString()), listMap.get(k));
                }
                mapSourceHashMap.put(aeEventHeaders.getEventHeaderId().toString(), sourceMap1);
                mapHashMap.put(aeEventHeaders.getEventHeaderId().toString(), sourceMap);
                AeEventLines aeEventLines = new AeEventLines();
                aeEventLines.setEventHeaderId(aeEventHeaders.getEventHeaderId());
                List<AeEventLines> aelList = aeEventLinesMapper.select(aeEventLines);

                aeEventHeaders.setLinesList(aelList);
            }else{
                aeEventHeadersList.remove(n);
            }
        }
        List<AeTfrBatchEvents> tfrBatchEventsList = new ArrayList<>();
        List<AeTfrEvents> aeTfrEventsList = new ArrayList<>();

        int count_batch = 0;
        int count = 0;
        List<List<AeTfrBatchEvents>> listBatchArrayList = new ArrayList<>();
        List<List<AeTfrEvents>> listArrayList = new ArrayList<>();
        List<Long> accKeyIdValue = new ArrayList<>();
        StringBuilder documentNos = new StringBuilder();
        //数据验证
        errorPoint:
        for (int j = 0; j < accountMap.size(); j++) {
            AeTfrBatchEvents atbe = new AeTfrBatchEvents();
            Long ifrEventBatchId = aeUtils.getAeBatchId();
            Boolean flag = true;
            Boolean period = true;
            List<AeTfrEvents> aeTfrEventss = new ArrayList<>();
            //子事件验证
            for (AeEventHeaders aeEventHeaders : aeEventHeadersList) {
                //获取对应ID的来源数据
                List<Map> sourceMap = mapHashMap.get(aeEventHeaders.getEventHeaderId().toString()).get(Long.valueOf(accountMap.get(j).get(aeEventHeaders.getAccountPrimaryField()).toString()));
                if (sourceMap != null && sourceMap.size()>0) {
                    //校验入账基准表主键字段
                    if (aeEventHeaders.getAccountPrimaryField() == null || "".equals(aeEventHeaders.getAccountPrimaryField())) {
                        throw new AeEventProcessException(EXCEPTION_CODE, "请先为事件来源维护入账基准表主键字段", null);
                    }
                    for (int i = 0; i < sourceMap.size(); i++) {
                        Boolean periodFlag;
                        //校验是否有公司字段
                        if (aeEventHeaders.getCompanyField() == null || "".equals(aeEventHeaders.getCompanyField())) {
                            periodFlag = true;
                        } else {
                            periodFlag = this.periodVerify(aeb, baiscTable, aeEventHeaders, sourceMap.get(i),programType);
                        }
                        if (periodFlag) {
                            AeTfrEvents ate = new AeTfrEvents();
                            //验证数据
                            String mes = verifySource(iRequest, aeEventHeaders.getLinesList(), sourceMap.get(i));
                            if (!"".equals(mes)) {
                                flag = false;
                                ate.setAccountingStatus("VERIFIED_ERROR"); //验证失败
                                ate.setAccountingRemarks(mes);
                            } else {
                                ate.setAccountingStatus("VERIFIED"); //验证成功
                            }
                            // 当为行表时， 需要用行表的表去代替.
                            ate.setEventBatchId(aeb.getEventBatchId());
                            ate.setEventHeaderId(aeEventHeaders.getEventHeaderId());
                            ate.setSourceTable(aeEventHeaders.getSourceTable());  // 动态的
                            ate.setSourcePrimaryKeyId(Long.parseLong(sourceMap.get(i).get(aeEventHeaders.getEventSourcePrimaryField()).toString())); // 动态的

                            ate.setTfrEventBatchId(ifrEventBatchId);
                            if(AeConstant.REV_RUN.equals(programType)){
                                ate.setAccountingDate((Date)map.get(aeb.getRevAccDateField()));
                                ate.setReversalFlag("Y");
                                ate.setAccountingStatus("GENERATED");
                            }else {
                                ate.setAccountingDate((Date) accountMap.get(j).get(aeb.getAccDateField()));
                                ate.setReversalFlag("N");
                            }
                            ate.setBatchNo(batchNo);
                            ate.setCreatedBy(iRequest.getUserId());
                            ate.setLastUpdatedBy(iRequest.getUserId());
                            ate.setLastUpdateLogin(iRequest.getUserId());
                            aeTfrEventss.add(ate);
                        }else{
                            period = false;
                        }
                    }
                }else{
                    continue errorPoint;
                }
            }
            if (!period) {
                aeTfrEventss = null;
            }

            if (period) {
                if (aeTfrEventss != null && aeTfrEventss.size() > 0) {
                    for (AeTfrEvents aeTfrEvents : aeTfrEventss) {
                        if (count % 1000 == 0) {
                            listArrayList.add(new ArrayList<>());
                        }
                        listArrayList.get(count++ / 1000).add(aeTfrEvents);
                        aeTfrEventsList.add(aeTfrEvents);
                    }
                }
                atbe.setTfrEventBatchId(ifrEventBatchId);
                atbe.setEventBatchId(aeb.getEventBatchId());
                atbe.setBasicTable(baiscTable);
                atbe.setPrimaryKeyField(aeb.getPrimaryKeyField());
                atbe.setPrimaryKeyId(Long.parseLong(accountMap.get(j).get(aeb.getPrimaryKeyField()).toString()));
                atbe.setReference(String.valueOf(accountMap.get(j).get(aeb.getReferenceField()).toString()));
                atbe.setReversalFlag(AE_REVERSAL_FLAG_N);
                atbe.setAccountingDate((Date) accountMap.get(j).get(aeb.getAccDateField()));
                atbe.setAccountingStatus(AE_ACCOUNTING_STATUS_NEW);
                atbe.setAccountingDate((Date) accountMap.get(j).get(aeb.getAccDateField()));
                atbe.setBatchNo(batchNo);
                atbe.setCreatedBy(Long.valueOf(iRequest.getUserId().toString()));
                atbe.setLastUpdatedBy(Long.valueOf(iRequest.getUserId().toString()));
                atbe.setLastUpdateLogin(Long.valueOf(iRequest.getUserId().toString()));

                documentNos.append(",").append(String.valueOf(accountMap.get(j).get(aeb.getReferenceField()).toString()));
                if (!flag) {
                    //更新事件批次
                    atbe.setAccountingStatus("VERIFIED_ERROR");
                    atbe.setAccountingRemarks("存在某行子事件验证失败的数据");
                    accKeyIdValue.add(atbe.getPrimaryKeyId());
                    //剔除数据组对应的入账源数据
                    /* accountMap.remove(j);*/
                    for (AeEventHeaders aeEventHeaders : aeEventHeadersList) {
                        mapHashMap.get(aeEventHeaders.getEventHeaderId().toString()).remove(accountMap.get(j).get(aeb.getPrimaryKeyField()));
                    }
                } else {
                    atbe.setAccountingStatus("VERIFIED");
                }
                if (count_batch % 1000 == 0) {
                    listBatchArrayList.add(new ArrayList<>());
                }
                listBatchArrayList.get(count_batch++ / 1000).add(atbe);
                tfrBatchEventsList.add(atbe);
            }
        }

        if (listBatchArrayList == null) {
            throw new AeEventProcessException(EXCEPTION_CODE, "没有存在期间校验通过的数据", null);
        }

        if (listBatchArrayList.size() > 0) {
            listBatchArrayList.forEach(oneList -> aeTfrBatchEventsMapper.insertAll(oneList));
        }

        if (listArrayList.size() > 0) {
            listArrayList.forEach(aList -> aeTfrEventsMapper.insertAll(aList));
        }

        //更新验证失败的原始单据
        if (accKeyIdValue != null && accKeyIdValue.size() > 0) {
            aeDataConvertProcess.updateOriginalDocument(aeb, baiscTable, accKeyIdValue, "VERIFIED_ERROR");
        }

        if(references == null && documentNos.toString().length() > 1){
            references = documentNos.toString().substring(1);
        }
        for (AeEventHeaders aeEventHeaders : aeEventHeadersList) {
            if (references != null) {
                references = references.replaceAll("，", ",");  // 中文换英文逗号
                String[] documentNo = references.split(",", -1);
                List<AeTfrDtlAccounts> list;
                List<AeTfrDtlAccounts> aeTfrDtlAccountsList1 = new ArrayList<>();
                Set<String> documentNoSet = new HashSet<>();
                for (int i = 0; i < documentNo.length; i++) {
                    documentNoSet.add(documentNo[i].toString());
                }
                for(String document:documentNoSet){
                    //事件转换
                    list = entryToTransferDtl(iRequest, aeb, baiscTable, aeEventHeaders, mapSourceHashMap, batchNo, document);
                    aeTfrDtlAccountsList1.addAll(list);
                }
                if(aeTfrDtlAccountsList1 !=null && !aeTfrDtlAccountsList1.isEmpty()) {
                    iAeTfrSumAccountsService.sumTfrByAccHeaderParr(iRequest, aeTfrDtlAccountsList1, aeb.getEventBatchId());
                }
            }
        }
    }

    /**
     * 期间校验
     *
     * @param aeEventBatches
     * @param basicTable
     * @param aeEventHeaders
     * @param sourceMap
     * @return Boolean
     * @throws AeEventProcessException
     */
    @Override
    public Boolean periodVerify(AeEventBatches aeEventBatches, String basicTable, AeEventHeaders aeEventHeaders, Map sourceMap,String programType) throws AeEventProcessException {
        //校验期间字段
        String periodValue;
        String primaryKeyField;
        Boolean validateFlag = true;
        //获取事件分类
        periodValue = aeEventHeaders.getCompanyField();
        primaryKeyField = aeEventHeaders.getAccountPrimaryField();
        String companyCodeValue = sourceMap.get(periodValue).toString();
        Long primaryKeyValue = (Long) sourceMap.get(aeEventHeaders.getAccountPrimaryField());
        if (companyCodeValue == null) {
            validateFlag = false;
            String message = "该笔数据公司字段值为空或不存在所在公司";
            aeDataConvertProcess.updateSourceData(aeEventBatches, basicTable, primaryKeyField, primaryKeyValue, message,programType);
        } else {
            Date accDate;
            if(REV_RUN.equals(programType)){
                accDate = (Date) sourceMap.get(aeEventBatches.getRevAccDateField());
            }else{
                accDate = (Date) sourceMap.get(aeEventBatches.getAccDateField());
            }
            Boolean flag = definationDateValueCache.cuxVal(accDate, companyCodeValue);
            if (!flag) {
                validateFlag = false;
                //期间校验失败
                String message;
                if(AeConstant.REV_RUN.equals(programType)){
                    message = "该笔数据冲销入账日期" + accDate + "所在公司" + companyCodeValue + "对应的期间未打开或者已关闭";
                }else{
                    message = "该笔数据入账日期" + accDate + "所在公司" + companyCodeValue + "对应的期间未打开或者已关闭";
                }

                aeDataConvertProcess.updateSourceData(aeEventBatches, basicTable, primaryKeyField, primaryKeyValue, message,programType);
            }
        }
        return validateFlag;

    }

    /**
     * 子事件校验
     *
     * @param request
     * @param aeEventLinesList
     * @param sourceMap
     * @return
     */
    private String verifySource(IRequest request, List<AeEventLines> aeEventLinesList, Map sourceMap) {
        StringBuilder mesStr = new StringBuilder();
        for (AeEventLines ael : aeEventLinesList) {
            if (Y.equals(ael.getRequiredFlag())) {  // 必输
                if (sourceMap.get(ael.getFieldName()) == null) {
                    mesStr.append(ael.getTitleText()).append("不允许为空！");

                }
            }
            if (sourceMap.get(ael.getFieldName()) != null) {   // null 不判断)
                switch (ael.getValueType()) {
                    case "DATE":
                        if (!(sourceMap.get(ael.getFieldName()) instanceof Date)) {
                            mesStr.append(ael.getTitleText()).append("值类型与子事件定义不符!");
                        }
                        break;
                    case "VARCHAR":
                        if ((sourceMap.get(ael.getFieldName()) instanceof Date)) { // 字符串这里就判断日期了，数字其实属于字符串！
                            mesStr.append(ael.getTitleText()).append("值类型与子事件定义不符!");

                        }
                        if (sourceMap.get(ael.getFieldName()).toString().length() > ael.getColumnLen()) {
                            mesStr.append(ael.getTitleText()).append("字符长度超出最大限度!");

                        }
                        break;
                    case "NUMBER":
                        try {  // 这个这样稳妥
                            Double.valueOf(sourceMap.get(ael.getFieldName()).toString());
                        } catch (NumberFormatException e) {
                            mesStr.append(ael.getTitleText()).append("值类型与子事件定义不符!");

                        }
                        break;
                    default:
                        break;

                }
            }
            String flexSetName = ael.getFlexSetName();
            String flexSetColumn = ael.getFlexSetColumn();
            String fieldName = ael.getFieldName();
            if (flexSetName != null && flexSetColumn != null && sourceMap.get(fieldName) != null) {
                boolean isPassFlex = isPassFlex(request, sourceMap.get(ael.getFieldName()).toString(), ael.getFlexSetColumn(), ael.getFlexSetName());
                if (!isPassFlex) {
                    mesStr.append(ael.getTitleText()).append("不在验证值集段值中！");
                }
            }

        }
        return mesStr.toString();
    }

    /**
     * 事件行字段值集校验
     *
     * @param iRequest
     * @param columnValue
     * @param fexColumn
     * @param lovCode
     * @return
     */
    private Boolean isPassFlex(IRequest iRequest, String columnValue, String fexColumn, String lovCode) {
        //查询redis中是否有为空的，为空则去数据库中查询一次，将查询结果放入redis中
        List<String> list = aeEventLineLovCache.getValue(fexColumn + ',' + lovCode);
        if (list == null || list.size() == 0) {
            //从数据中再找一次同步到缓存中
            list = aeEventLineLovCache.syncAeEventLineCacheByCode(iRequest, fexColumn, lovCode);
        }
        //验证
        return list.contains(columnValue);
    }

    /**
     * 事件转换方法
     *
     * @param iRequest
     * @param batchNo
     */
    public List<AeTfrDtlAccounts> entryToTransferDtl(IRequest iRequest, AeEventBatches aeb, String basicTable, AeEventHeaders aeEventHeaders, Map<String, Map<Long, Map>> map, String batchNo, String documentNo) throws AeEventProcessException, CodeRuleException {
        //获取满足条件的子事件数据
        List<AeTfrEvents> aeTfrEventsList = aeTfrEventsMapper.selectVerifiedTfrEvents(batchNo, aeEventHeaders.getEventHeaderId(), documentNo);
        List<AeTfrDtlAccounts> aeTfrDtlAccountsList1 = new ArrayList<>();
        //正确的事件批次ID集合
        List<Long> aeTfrEventBatchIdList = new ArrayList<>();
        //错误的事件批次ID集合
        List<Long> aeTfrEventBatchErrorList = new ArrayList<>();
        String globalflag = "true";
        Date currentDate = new Date();
        if (aeTfrEventsList != null) {
            //以单据为最小单位进行控制
            //初始化会计引擎配置数据
            Map FieldNotNull = new HashMap<>();
            List<AeAcctCategory> acctCategories = new ArrayList<>();
            AeAcctCategory category = new AeAcctCategory();
            category.setEventHeaderId(aeEventHeaders.getEventHeaderId());
            List<AeAcctCategory> aeAcctCategoryList = aeAcctCategoryMapper.selectValidate(category);
            for (AeAcctCategory acctCategory : aeAcctCategoryList) {
                acctCategory = aeAcctCategoryCache.getValue(acctCategory.getAcctCategoryId().toString());
                acctCategories.add(acctCategory);
            }
            Map<Long, String> aeEventLineMap = new HashMap<>();
            Map<Long, String> columnMap = new HashMap<>();
            for (AeEventLines aeEventLines : aeEventHeaders.getLinesList()) {
                if (AE_COLUMN_TYPE_SOURCE.equals(aeEventLines.getColumnType())) {
                    aeEventLineMap.put(aeEventLines.getEventLineId(), aeEventLines.getFieldName());
                }
                columnMap.put(aeEventLines.getEventLineId(), aeEventLines.getColumnName());
                String columnName = aeEventLines.getColumnName().toLowerCase();
                FieldNotNull.put(columnName, true);
            }
            //获取账务表
            List<AeAccountLines> aeAccountLinesList = aeAccountLinesMapper.selectAccountLineByEHeaderId(aeEventHeaders.getEventHeaderId());
            //目前写死账务表 后期改成从子事件里面取
             /*AeAccountLines aeAccountLines = new AeAccountLines();
            aeAccountLines.setAccountHeaderId(10021L);*/
            for (AeAccountLines accountLines : aeAccountLinesList) {
                String columnName = accountLines.getColumnName().toLowerCase();
                FieldNotNull.put(columnName, true);
            }
            //原始单据错误ID集合
            List<Long> aesourceIdErrorList = new ArrayList<>();
            List<Long> aesourceIdList = new ArrayList<>();
            //遍历所有需要转换的子事件表
            Map<Map<String, String>, String> filterMap = new HashMap<>();
            for (AeTfrEvents aeTfrEvents : aeTfrEventsList) {
                Long startTime = System.currentTimeMillis();
                Map<String, String> columnValueMap = new HashMap<>();
                Map<Long, String> filterFlag = new HashMap<>();
                List<AeTfrDtlAccounts> aeTfrDtlAccountsList = new ArrayList<>();
                Boolean validateFlag = true;
                //获取源数据
                Map sourceMap = map.get(aeTfrEvents.getEventHeaderId().toString()).get(aeTfrEvents.getSourcePrimaryKeyId());
                //公司字段
                String primaryField = aeEventHeaders.getAccountPrimaryField();
                Boolean flag = false;
                Test:
                for (AeAcctCategory aeAcctCategory : acctCategories) {
                    //筛选组判定
                    Long accFilterId = aeAcctCategory.getFilterHeaderId();
                    if (accFilterId != null && judgeFilterDate(iRequest,accFilterId)) {
                        String judgeFlag = filterFlag.get(aeAcctCategory.getFilterHeaderId());
                        if (judgeFlag == null) {
                            AeFilterHeaders aeFilterHeaders = aeFilterDataCache.getValue(aeAcctCategory.getFilterHeaderId().toString());
                            Map<String, String> filterValueMap = getFilterColumnValue(aeFilterHeaders, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                            //获取筛选组的值
                            judgeFlag = filterMap.get(filterValueMap);
                            if (judgeFlag == null) {
                                judgeFlag = judgeFilter(aeFilterHeaders, columnValueMap, columnMap, aeTfrEvents.getTfrEventId(), filterMap);
                            }
                            filterFlag.put(aeFilterHeaders.getFilterHeaderId(), judgeFlag);
                        }
                        filterFlag.put(aeAcctCategory.getFilterHeaderId(), judgeFlag);
                        if ("error".equals(judgeFlag)) {
                            validateFlag = false;
                            flag = true;
                            break Test;
                        } else if ("true".equals(judgeFlag)) {
                            flag = true;
                            //筛选组通用
                            for (AeAcctCategoryValue aeAcctCategoryValue : aeAcctCategory.getAeAcctCategoryValues()) {
                                for (AeRuleHeaders aeRuleHeaders : aeAcctCategoryValue.getAeRuleHeadersList()) {
                                    /*long ruleStarttTime=System.currentTimeMillis(); //获取开始时间*/
                                    if(aeRuleHeaders.getStartDate() != null) {
                                        boolean dateFlag = (aeRuleHeaders.getEndDate() != null) && (currentDate.after(aeRuleHeaders.getEndDate())
                                                || currentDate.before(aeRuleHeaders.getStartDate()));
                                        if (dateFlag) {
                                            continue;
                                        } else if (aeRuleHeaders.getEndDate() == null && currentDate.before(aeRuleHeaders.getStartDate())) {
                                            continue;
                                        }
                                    }
                                    Long filterHeaderId = aeRuleHeaders.getFilterHeaderId();
                                    if (filterHeaderId != null && judgeFilterDate(iRequest,filterHeaderId)) {
                                        String judgeFlagRH = filterFlag.get(aeRuleHeaders.getFilterHeaderId());
                                        if (judgeFlagRH == null) {
                                            AeFilterHeaders filterHeaders = aeFilterDataCache.getValue(aeRuleHeaders.getFilterHeaderId().toString());
                                            //获取筛选组的值
                                            Map<String, String> filterValueMap = getFilterColumnValue(filterHeaders, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                                            //获取筛选组的值
                                            judgeFlagRH = filterMap.get(filterValueMap);
                                            if (judgeFlagRH == null) {
                                                judgeFlagRH = judgeFilter(filterHeaders, columnValueMap, columnMap, aeTfrEvents.getTfrEventId(), filterMap);
                                            }
                                            filterFlag.put(aeRuleHeaders.getFilterHeaderId(), judgeFlagRH);
                                        }

                                        if ("error".equals(judgeFlagRH)) {
                                            validateFlag = false;
                                            break Test;
                                        } else if ("true".equals(judgeFlagRH)) {
                                            AeTfrDtlAccounts aeTfrDtlAccounts = entryConvertAccountDtl(iRequest, aeTfrEvents, aeRuleHeaders, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                                            if (aeTfrDtlAccounts == null) {
                                                validateFlag = false;
                                                break Test;
                                            } else {
                                                aeTfrDtlAccountsList.add(aeTfrDtlAccounts);
                                            }
                                        }

                                    } else {
                                        AeTfrDtlAccounts aeTfrDtlAccounts = entryConvertAccountDtl(iRequest, aeTfrEvents, aeRuleHeaders, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                                        if (aeTfrDtlAccounts == null) {
                                            validateFlag = false;
                                            break Test;
                                        } else {
                                            aeTfrDtlAccountsList.add(aeTfrDtlAccounts);
                                        }
                                    }
                                }
                                /*long acctEndTime=System.currentTimeMillis(); //获取结束时间
                                logger.debug("子事件数据"+aeTfrEvents.getTfrEventId()+"子会计分类"+aeAcctCategoryValue.getAcctCategoryMeaning()+"处理时间"+(acctEndTime-acctStartTime)+"ms");*/
                            }
                        }
                    } else {
                        flag = true;
                        for (AeAcctCategoryValue aeAcctCategoryValue : aeAcctCategory.getAeAcctCategoryValues()) {
                            for (AeRuleHeaders aeRuleHeaders : aeAcctCategoryValue.getAeRuleHeadersList()) {
                                //通过时间过滤无效的会计规则
                                if(aeRuleHeaders.getStartDate() != null) {
                                    boolean dateFlag = (aeRuleHeaders.getEndDate() != null) && (currentDate.after(aeRuleHeaders.getEndDate())
                                            || currentDate.before(aeRuleHeaders.getStartDate()));
                                    if (dateFlag){
                                        continue;
                                    } else if (aeRuleHeaders.getEndDate() == null && currentDate.before(aeRuleHeaders.getStartDate())) {
                                        continue;
                                    }
                                }
                                Long filterHeaderId = aeRuleHeaders.getFilterHeaderId();
                                if (filterHeaderId != null && judgeFilterDate(iRequest,filterHeaderId)) {
                                    String judgeFlagRH = filterFlag.get(aeRuleHeaders.getFilterHeaderId());
                                    if (judgeFlagRH == null) {
                                        AeFilterHeaders filterHeaders = aeFilterDataCache.getValue(aeRuleHeaders.getFilterHeaderId().toString());
                                        //获取筛选组的值
                                        Map<String, String> filterValueMap = getFilterColumnValue(filterHeaders, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                                        //获取筛选组的值
                                        judgeFlagRH = filterMap.get(filterValueMap);
                                        if (judgeFlagRH == null) {
                                            judgeFlagRH = judgeFilter(filterHeaders, columnValueMap, columnMap, aeTfrEvents.getTfrEventId(), filterMap);
                                        }
                                        filterFlag.put(aeRuleHeaders.getFilterHeaderId(), judgeFlagRH);
                                    }

                                    if ("error".equals(judgeFlagRH)) {
                                        validateFlag = false;
                                        break Test;
                                    } else if ("true".equals(judgeFlagRH)) {
                                        AeTfrDtlAccounts aeTfrDtlAccounts = entryConvertAccountDtl(iRequest, aeTfrEvents, aeRuleHeaders, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                                        if (aeTfrDtlAccounts == null) {
                                            validateFlag = false;
                                            break Test;
                                        } else {
                                            aeTfrDtlAccountsList.add(aeTfrDtlAccounts);
                                        }
                                    }

                                } else {
                                    AeTfrDtlAccounts aeTfrDtlAccounts = entryConvertAccountDtl(iRequest, aeTfrEvents, aeRuleHeaders, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                                    if (aeTfrDtlAccounts == null) {
                                        validateFlag = false;
                                        break Test;
                                    } else {
                                        aeTfrDtlAccountsList.add(aeTfrDtlAccounts);
                                    }
                                }
                            }
                        }
                    }
                }
                long endTime = System.currentTimeMillis(); //获取结束时间
                logger.debug("子事件数据" + aeTfrEvents.getTfrEventId() + "处理时间" + (endTime - startTime) + "ms");
                if (!flag || (aeTfrDtlAccountsList.size() <= 0 && validateFlag)) {
                    globalflag = "false";
                    validateFlag = false;
                    String errorMes = "该笔子事件不满足任何会计分类";
                    aeDataConvertProcess.updateTfrData(aeTfrEvents.getTfrEventId(), errorMes, "GENERATED_ERROR");

                }
                if (validateFlag) {
                    aeTfrEventBatchIdList.add(aeTfrEvents.getTfrEventBatchId());
                    aesourceIdList.add(Long.valueOf(sourceMap.get(primaryField).toString()));
                    for (int i = 0; i < aeTfrDtlAccountsList.size(); i++) {
                        aeTfrDtlAccountsList1.add(aeTfrDtlAccountsList.get(i));
                    }
                } else {
                    globalflag = "false";
                    aeTfrEventBatchErrorList.add(aeTfrEvents.getTfrEventBatchId());
                    aesourceIdErrorList.add(Long.valueOf(sourceMap.get(primaryField).toString()));
                }
            }
            //批量更新验证失败的原始单据状态
            if (aesourceIdErrorList != null && aesourceIdErrorList.size() > 0) {
                aesourceIdErrorList = removeDuplicate(aesourceIdErrorList);
                //排除错误的事件头
                for (int k = 0; k < aesourceIdErrorList.size(); k++) {
                    removeDuplicate(aesourceIdList);
                    aesourceIdList.remove(aesourceIdErrorList.get(k));
                }
                aeDataConvertProcess.updateOriginalDocument(aeb, basicTable, aesourceIdErrorList, "GENERATED_ERROR");
            }

            //批量更新错误事件数据
            if (aeTfrEventBatchErrorList != null && aeTfrEventBatchErrorList.size() > 0) {
                removeDuplicate(aeTfrEventBatchErrorList);
                aeDataConvertProcess.batchUpdateTfrBatchData(aeTfrEventBatchErrorList, "该笔子事件存在一行或多行账务明细行转换失败", "GENERATED_ERROR");
            }

            //排除错误的事件头
            for (int k = 0; k < aeTfrEventBatchErrorList.size(); k++) {
                removeDuplicate(aeTfrEventBatchIdList);
                aeTfrEventBatchIdList.remove(aeTfrEventBatchErrorList.get(k));
            }

            if ("true".equals(globalflag)) {
                //批量更新正确事件数据
                if (aeTfrEventBatchIdList != null && aeTfrEventBatchIdList.size() > 0) {
                    removeDuplicate(aeTfrEventBatchIdList);
                    aeDataConvertProcess.batchUpdateTfrBatchData(aeTfrEventBatchIdList, null, "GENERATED");
                }

                //批量更新成功原始单据
                if (aesourceIdList != null && aesourceIdList.size() > 0) {
                    removeDuplicate(aesourceIdList);
                    aeDataConvertProcess.updateOriginalDocument(aeb, basicTable, aesourceIdList, "GENERATED");
                }
                List<Long> aeTfrEventList = new ArrayList<>();
                //批量插入
                if (aeTfrDtlAccountsList1 != null && aeTfrDtlAccountsList1.size() > 0) {

                    int count = 0;
                    List<List<AeTfrDtlAccounts>> aeTfrDtlAccountss = new ArrayList<>();
                    for (int j = 0; j < aeTfrDtlAccountsList1.size(); j++) {
                        AeTfrDtlAccounts tfrDtlAccounts = aeTfrDtlAccountsList1.get(j);
                        aeTfrEventList.add(tfrDtlAccounts.getTfrEventId());

                        if (count % 1000 == 0) {
                            aeTfrDtlAccountss.add(new ArrayList<>());
                        }
                        aeTfrDtlAccountss.get(count++ / 1000).add(tfrDtlAccounts);
                    }
                    //批量更新子事件
                    if (aeTfrEventList != null && aeTfrEventList.size() > 0) {
                        removeDuplicate(aeTfrEventList);
                        aeDataConvertProcess.batchUpdateTfrData(aeTfrEventList, null, "GENERATED");
                    }
                    if (aeTfrDtlAccountss.size() > 0) {
                        aeTfrDtlAccountss.forEach(oneList -> aeTfrDtlAccountsMapper.insertAll(oneList, FieldNotNull));
                    }
                    //汇总
                    // iAeTfrSumAccountsService.sumTfrByAccHeaderParr(iRequest, aeTfrDtlAccountsList1, aeb.getEventBatchId());
                }
            }
            return aeTfrDtlAccountsList1;
        } else {
            return null;
        }
    }

    /**
     * 子事件转换成入账明细
     *
     * @param iRequest
     * @param aeTfrEvents
     * @param aeRuleHeaders
     * @param sourceMap
     * @param aeEventLineMap
     * @param columnMap
     * @return
     * @author xin.tian01@hand-china.com
     */
    private AeTfrDtlAccounts entryConvertAccountDtl(IRequest iRequest, AeTfrEvents aeTfrEvents, AeRuleHeaders aeRuleHeaders, Map sourceMap, Map<Long, String> aeEventLineMap, Map<Long, String> columnMap, Map<String, String> columnValueMap) {
        AeTfrDtlAccounts accounts = new AeTfrDtlAccounts();
        accounts.setEventBatchId(aeTfrEvents.getEventBatchId());
        accounts.setEventHeaderId(aeTfrEvents.getEventHeaderId());
        accounts.setTfrEventId(aeTfrEvents.getTfrEventId());
        accounts.setTfrEventBatchId(aeTfrEvents.getTfrEventBatchId());
        accounts.setCreatedBy(iRequest.getUserId());
        accounts.setLastUpdatedBy(iRequest.getUserId());
        accounts.setAccountingStatus("GENERATED");
        accounts.setAccountHeaderId(aeRuleHeaders.getAccountHeaderId());
        accounts.setSummaryFlag("N");
        accounts.setRuleHeaderId(aeRuleHeaders.getRuleHeaderId());
        accounts.setAccountingDate(aeTfrEvents.getAccountingDate());
        accounts = aeSourceDataMap(iRequest, aeRuleHeaders, sourceMap, aeEventLineMap, aeTfrEvents.getTfrEventId(), columnMap, accounts, columnValueMap);
        return accounts;
    }


    /**
     * 获取筛选组来源数据值
     *
     * @param aeFilterHeaders
     * @param sourceMap
     * @param aeEventLineMap
     * @param columnMap
     * @param columnValueMap
     * @return
     */
    private Map<String, String> getFilterColumnValue(AeFilterHeaders aeFilterHeaders, Map sourceMap, Map<Long, String> aeEventLineMap, Map<Long, String> columnMap, Map<String, String> columnValueMap) {
        Map<String, String> filterValueMap = new HashMap<>();
        Date currentDate = new Date();
        filterValueMap.put(aeFilterHeaders.getFilterHeaderId().toString(), aeFilterHeaders.getFilterName());
        for (AeFilterLines aeFilterLines : aeFilterHeaders.getAeFilterLines()) {
            String columnName = columnMap.get(aeFilterLines.getEventLineId());
            String columnValue = columnValueMap.get(columnName);
            filterValueMap.put(columnName, columnValue);
            if (columnValue == null) {
                if ("SOURCE".equals(aeFilterLines.getFieldType())) {
                    Object columnValue1 = sourceMap.get(aeEventLineMap.get(aeFilterLines.getEventLineId()));
                    if (columnValue1 != null) {
                        columnValueMap.put(columnName, columnValue1.toString());
                        filterValueMap.put(columnName, columnValue1.toString());
                    }else{
                        columnValueMap.put(columnName, null);
                        filterValueMap.put(columnName, null);
                    }
                } else if ("TARGET".equals(aeFilterLines.getFieldType())) {
                    AeMappingHeaders aeMappingHeaders = aeMappingDataCache.getValue(aeFilterLines.getEventLineId().toString());
                    // 增加映射日期判断 add by xizhi.ding bug:23205
                    boolean dateFlag = aeMappingHeaders.getEndDate() != null && (currentDate.after(aeMappingHeaders.getEndDate())
                            || currentDate.before(aeMappingHeaders.getStartDate()));
                    if(dateFlag){
                        columnValueMap.put(columnName, null);
                        filterValueMap.put(columnName, null);
                    }else if(aeMappingHeaders.getEndDate() == null && currentDate.before(aeMappingHeaders.getStartDate())){
                        columnValueMap.put(columnName, null);
                        filterValueMap.put(columnName, null);
                    }else{
                        columnValueMap = aeRuleProcessUtil.getTargetValueList(aeMappingHeaders, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                        columnValue = columnValueMap.get(columnName);
                        filterValueMap.put(columnName, columnValue);
                    }
                } else if ("COMPARISON".equals(aeFilterLines.getFieldType())) {
                    AeComparisonHeaders aeComparisonHeaders = aeComparisonDataCache.getValue(aeFilterLines.getEventLineId().toString());
                    boolean dateFlag = aeComparisonHeaders.getEndDate() != null && (currentDate.after(aeComparisonHeaders.getEndDate())
                            || currentDate.before(aeComparisonHeaders.getStartDate()));
                    if (dateFlag) {
                        columnValue = null;
                    } else if (aeComparisonHeaders.getEndDate() == null && currentDate.before(aeComparisonHeaders.getStartDate())) {
                        columnValue = null;
                    }else {
                        for (AeComparisonLines aeComparisonLines : aeComparisonHeaders.getAeComparisonLines()) {
                            String aFieldValue = aeRuleProcessUtil.getComparisonAFieldValue(aeComparisonLines, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                            String bFieldValue = aeRuleProcessUtil.getComparisonBFieldValue(aeComparisonLines, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                            if (aFieldValue == null || "".equals(aFieldValue)) {
                                columnValue = "";
                                break;
                            }
                            if (bFieldValue == null || "".equals(bFieldValue)) {
                                columnValue = "";
                                break;
                            }
                            if (iAeComparisonLinesService.doCompare(aeComparisonLines, aFieldValue, bFieldValue)) {
                                columnValue = aeRuleProcessUtil.getComparisonResultValue(aeComparisonLines, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                                break;
                            }
                        }
                    }
                    columnValueMap.put(columnName, columnValue);
                    filterValueMap.put(columnName, columnValue);
                }
            }
        }
        return filterValueMap;
    }

    /**
     * 筛选组判定
     *
     * @param aeFilterHeaders 筛选组对象
     * @param columnValueMap  通用字段跟真实值对应关系
     * @return 筛选组判定结果
     * @author xin.tian01@hand-china.com
     */
    public String judgeFilter(AeFilterHeaders aeFilterHeaders, Map<String, String> columnValueMap, Map<Long, String> columnMap, Long aeTfrEventId, Map<Map<String, String>, String> filterMap) {

        List<String> markCodeList = aeFilterHeaders.getMarkCodeList();
        List<AeFilterLines> aeFilterLinesList = aeFilterHeaders.getAeFilterLines();
        StringBuffer errorMessage = new StringBuffer();
        //定义这个并集对应的筛选组行返回的总状态
        List<String> headerFlagList = new ArrayList<>();
        Map<String, String> columnFilterMap = new HashMap<>();
        columnFilterMap.put(aeFilterHeaders.getFilterHeaderId().toString(), aeFilterHeaders.getFilterName());
        for (int i = 0, len1 = markCodeList.size(); i < len1; i++) {

            //定义这个并集对应的筛选组行返回的状态
            List<String> lineFlagList = new ArrayList<>();
            for (int m = 0; m < aeFilterLinesList.size(); m++) {
                //根据筛选组中的接口行id找到对应的column
                //根据columnName在map中找到对应的value
                //如果并集标志相等，则进行判断
                if (markCodeList.get(i).equals(aeFilterLinesList.get(m).getMarkCode())) {
                    String columnName = columnMap.get(aeFilterLinesList.get(m).getEventLineId());
                    String columnValue = columnValueMap.get(columnName);
                    columnFilterMap.put(columnName, columnValue);
                    //如果当前的"筛选方式"为包含，则判断是否等于范围自
                    switch (aeFilterLinesList.get(m).getFilterMethod()) {
                        case AeConstant.AE_FILTER_WAY_INCLUDE:
                            if (columnValue == null || "".equals(columnValue)) {
//                                errorMessage.append(aeFilterHeaders.getFilterName() + "]筛选组中[" + aeFilterLinesList.get(m).getTitleText() + "]字段不能为空！");
//                                aeDataConvertProcess.updateTfrData(aeTfrEventId, errorMessage.toString(), "GENERATED_ERROR");
//                                return "error";
                                lineFlagList.add(AeConstant.STRING_N);
                            } else {
                                if (AeConstant.AE_DEFINATION_VARCHAR.equals(aeFilterLinesList.get(m).getValueType())) {
                                    if (columnValue.compareTo(aeFilterLinesList.get(m).getValueFrom()) >= 0 && columnValue.compareTo(aeFilterLinesList.get(m).getValueFrom()) <= 0) {
                                        lineFlagList.add(AeConstant.STRING_Y);
                                    } else {
                                        lineFlagList.add(AeConstant.STRING_N);
                                    }
                                } else if (AeConstant.AE_DEFINATION_NUMBER.equals(aeFilterLinesList.get(m).getValueType())) {
                                    //Integer valueNumber = Integer.parseInt(columnValue);
                                    Double valueNumber = Double.parseDouble(columnValue);
                                    if (valueNumber >= Double.parseDouble(aeFilterLinesList.get(m).getValueFrom()) && valueNumber <= Double.parseDouble(aeFilterLinesList.get(m).getValueTo())) {
                                        lineFlagList.add(AeConstant.STRING_Y);
                                    } else {
                                        lineFlagList.add(AeConstant.STRING_N);
                                    }
                                } else if (AeConstant.AE_DEFINATION_DATE.equals(aeFilterLinesList.get(m).getValueType())) {
                                    Date date = HscsUtil.str2Date(columnValue, "yyyy-MM-dd HH:mm:ss");
                                    Date valueFromDate = HscsUtil.str2Date(aeFilterLinesList.get(m).getValueFrom(), "yyyy-MM-dd HH:mm:ss");
                                    Date valueToDate = HscsUtil.str2Date(aeFilterLinesList.get(m).getValueTo(), "yyyy-MM-dd HH:mm:ss");
                                    int result = DateUtils.rangeDate(date, valueFromDate, valueToDate);
                                    if (result == 1) {
                                        lineFlagList.add(AeConstant.STRING_Y);
                                    } else {
                                        lineFlagList.add(AeConstant.STRING_N);
                                    }
                                }
                            }
                            continue;
                        case AeConstant.AE_FILTER_WAY_EXCEPT:
                            if (columnValue == null || "".equals(columnValue)) {
//                                errorMessage.append(aeFilterHeaders.getFilterName() + "]筛选组中[" + aeFilterLinesList.get(m).getTitleText() + "]字段不能为空！");
//                                aeDataConvertProcess.updateTfrData(aeTfrEventId, errorMessage.toString(), "GENERATED_ERROR");
//                                return "error";
                                lineFlagList.add(AeConstant.STRING_Y);
                            } else {
                                if (AeConstant.AE_DEFINATION_VARCHAR.equals(aeFilterLinesList.get(m).getValueType())) {
                                    if (columnValue.compareTo(aeFilterLinesList.get(m).getValueFrom()) >= 0 && columnValue.compareTo(aeFilterLinesList.get(m).getValueFrom()) <= 0) {
                                        lineFlagList.add(AeConstant.STRING_N);
                                    } else {
                                        lineFlagList.add(AeConstant.STRING_Y);
                                    }
                                } else if (AeConstant.AE_DEFINATION_NUMBER.equals(aeFilterLinesList.get(m).getValueType())) {
//                                    Integer valueNumber = Integer.parseInt(columnValue);
                                    Double valueNumber = Double.parseDouble(columnValue);
                                    if (valueNumber >= Double.parseDouble(aeFilterLinesList.get(m).getValueFrom()) && valueNumber <= Double.parseDouble(aeFilterLinesList.get(m).getValueTo())) {
                                        lineFlagList.add(AeConstant.STRING_N);
                                    } else {
                                        lineFlagList.add(AeConstant.STRING_Y);
                                    }
                                } else if (AeConstant.AE_DEFINATION_DATE.equals(aeFilterLinesList.get(m).getValueType())) {
                                    Date date = HscsUtil.str2Date(columnValue, "yyyy-MM-dd HH:mm:ss");
                                    Date valueFromDate = HscsUtil.str2Date(aeFilterLinesList.get(m).getValueFrom(), "yyyy-MM-dd HH:mm:ss");
                                    Date valueToDate = HscsUtil.str2Date(aeFilterLinesList.get(m).getValueTo(), "yyyy-MM-dd HH:mm:ss");
                                    int result = DateUtils.rangeDate(date, valueFromDate, valueToDate);
                                    if (result == 0) {
                                        lineFlagList.add(AeConstant.STRING_Y);
                                    } else {
                                        lineFlagList.add(AeConstant.STRING_N);
                                    }
                                }
                            }
                            continue;
                        case AeConstant.AE_FILTER_WAY_ISNOTNULL:
                            if (StringUtils.isNotBlank(columnValue)) {
                                lineFlagList.add(AeConstant.STRING_Y);
                            } else {
                                lineFlagList.add(AeConstant.STRING_N);
                            }
                            continue;
                        case AeConstant.AE_FILTER_WAY_ISNULL:
                            if (StringUtils.isBlank(columnValue)) {
                                lineFlagList.add(AeConstant.STRING_Y);
                            } else {
                                lineFlagList.add(AeConstant.STRING_N);
                            }
                            continue;
                        default:
                            break;
                    }
                }
                //剔除已经判定的
                // aeFilterLinesList.remove(m);
            }
            //只要这个并集标志下的筛选组行数据不包含成功的，跳出外层循环
            if (!lineFlagList.contains("Y") && lineFlagList != null && !lineFlagList.isEmpty()) {
                headerFlagList.add(AeConstant.STRING_N);
                break;//跳出所有的循环不在执行
            } else {
                headerFlagList.add(AeConstant.STRING_Y);
            }
        }

        //如果所有的并集标志都通过了筛选组，则此时数据通过了筛选组，返回Y，为通过返回“”
        if (!headerFlagList.contains(AeConstant.STRING_N) && headerFlagList != null && !headerFlagList.isEmpty()) {
            filterMap.put(columnFilterMap, "true");
            return "true";

        } else {
            filterMap.put(columnFilterMap, "false");
            return "false";
        }
    }

    /**
     * 来源数据进行映射转换
     *
     * @param iRequest
     * @param aeRuleHeaders
     * @param sourceMap
     * @param aeEventLineMap
     * @return
     * @author xin.tian01@hand-china.com
     */
    private AeTfrDtlAccounts aeSourceDataMap(IRequest iRequest, AeRuleHeaders aeRuleHeaders, Map sourceMap, Map<Long, String> aeEventLineMap, Long aeTfrEventId, Map<Long, String> columnMap, AeTfrDtlAccounts aeTfrDtlAccounts, Map<String, String> columnValueMap) {
        StringBuffer errorMessage = new StringBuffer();
        List<String> fieldNameList = new ArrayList<>();
        List<String> fieldValueList = new ArrayList<>();
        String columnName = "";
        StringBuffer columnValuestr = new StringBuffer();
        Boolean flag = true;
        Date currentDate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        boolean attributeFlag;
        Map<String,String> attributeMap = new HashMap<>();
        for (AeRuleLines ruleLines : aeRuleHeaders.getAeRuleLinesList()) {
            /*long startTime=System.currentTimeMillis(); //获取结束时间*/
            String fieldValue;
            String column = columnMap.get(ruleLines.getEventLineId());
            if(column!=null) {
                fieldValue = columnValueMap.get(column);
            }else{
                fieldValue=null;
            }
            if (fieldValue == null) {
                //取值方式是序列时,对应的值是序列名称
                if ("SEQUENCE".equals(ruleLines.getValueFrom())) {
                    //通过编码代码找到对应的流水号
                    try {
                        fieldValue = iSysCodeRuleProcessService.getRuleCode(ruleLines.getSequenceCode());
                    } catch (CodeRuleException e) {
                        flag = false;
                        errorMessage.append("会计生成规则:[")
                                .append(aeRuleHeaders.getRuleName())
                                .append("] 下的[")
                                .append(ruleLines.getFieldName())
                                .append("]编码规则有异常！")
                                .append(e.getMessage());
                    }
                    if (fieldValue == null) {
                        flag = false;
                        errorMessage.append("会计生成规则:[")
                                .append(aeRuleHeaders.getRuleName())
                                .append("] 下的[")
                                .append(ruleLines.getFieldName())
                                .append("]编码规则不存在！");
                    }
                }
                //取值方式是常数时，对应的值是常数值
                if ("CONSTANT".equals(ruleLines.getValueFrom())) {
                    //如果字段为数值类型则进行判定符号更新
                    fieldValue = ruleLines.getDefaultValue();
                }
                //取值方式是源数据时，对应的值是来源列对应的value在接口拆分行表中的值
                if ("SOURCE".equals(ruleLines.getValueFrom())) {
                    Object sourceValue=sourceMap.get(aeEventLineMap.get(ruleLines.getEventLineId()));
                    if(sourceValue!=null) {
                        fieldValue=sourceValue.toString();
                    }
                    columnValueMap.put(column, fieldValue);
                }
                //取值方式是映射数据时，对应的值是映射转换中对应的数据
                if ("TARGET".equals(ruleLines.getValueFrom())) {
                    //获取映射对象
                    AeMappingHeaders aeMappingHeaders = aeMappingDataCache.getValue(ruleLines.getEventLineId().toString());

                    if (aeMappingHeaders == null) {
                        flag = false;
                        errorMessage.append("会计生成规则:[")
                                .append(aeRuleHeaders.getRuleName())
                                .append("] 下的[")
                                .append(ruleLines.getFieldName())
                                .append("]字段的映射定义不存在！");
                    } else {
                        boolean dateFlag = aeMappingHeaders.getEndDate() != null && (currentDate.after(aeMappingHeaders.getEndDate())
                                || currentDate.before(aeMappingHeaders.getStartDate()));
                        if (dateFlag) {
                            flag = false;
                            errorMessage.append("会计生成规则:[")
                                    .append(aeRuleHeaders.getRuleName())
                                    .append("] 下的[")
                                    .append(ruleLines.getFieldName())
                                    .append("]字段的映射定义[")
                                    .append(aeMappingHeaders.getMappingName())
                                    .append("]不在有效日期内！");
                        } else if (aeMappingHeaders.getEndDate() == null && currentDate.before(aeMappingHeaders.getStartDate())) {
                            flag = false;
                            errorMessage.append("会计生成规则:[")
                                    .append(aeRuleHeaders.getRuleName())
                                    .append("] 下的[")
                                    .append(ruleLines.getFieldName())
                                    .append("]字段的映射定义[")
                                    .append(aeMappingHeaders.getMappingName())
                                    .append("]不在有效日期内！");
                        }else {
                            if (aeMappingHeaders.getAeMappingLinesList() == null) {
                                flag = false;
                                errorMessage.append("会计生成规则:[")
                                        .append(aeRuleHeaders.getRuleName())
                                        .append("] 下的[")
                                        .append(ruleLines.getColumnComment())
                                        .append("]字段的映射定义的映射信息不存在！");
                            } else {
                                columnValueMap = aeRuleProcessUtil.getTargetValueList(aeMappingHeaders, sourceMap, aeEventLineMap, columnMap, columnValueMap);

                            }
                        }
                    }
                    fieldValue = columnValueMap.get(column);
                }
                //取值方式是为公式定义，对应的值通过公式进行转换
                if ("FORMULA".equals(ruleLines.getValueFrom())) {
                    try {
                        Double resultValue = iAeFormulaLinesService.programByFormula(iRequest, sourceMap, aeRuleHeaders.getRuleName(), ruleLines, aeEventLineMap, columnMap, columnValueMap);
                        fieldValue = resultValue == null?null:resultValue.toString();
                    } catch (CalculateException c) {
                        flag = false;
                        errorMessage.append(c.getMessage());
                    } catch (Exception c) {
                        flag = false;
                        errorMessage.append(c.getMessage());
                    }
//                    if("Y".equals(ruleLines.getRequiredFlag()) && StringUtils.isBlank(fieldValue)){
//                        flag = false;
//                        errorMessage.append("[").append(ruleLines.getColumnComment()).append("]公式计算的结果不能为空");
//                    }
                }
                //取值方式是为比较判定，对应的值通过比较判定进行转换
                if ("COMPARISON".equals(ruleLines.getValueFrom())) {
                    AeComparisonHeaders aeComparisonHeaders = aeComparisonDataCache.getValue(ruleLines.getEventLineId().toString());
                    if(aeComparisonHeaders.getStartDate() != null) {
                        boolean dateFlag = aeComparisonHeaders.getEndDate() != null && (currentDate.after(aeComparisonHeaders.getEndDate())
                                || currentDate.before(aeComparisonHeaders.getStartDate()));
                        if (dateFlag) {
                            flag = false;
                            errorMessage.append("账务表字段:[")
                                    .append(ruleLines.getFieldName())
                                    .append("]取值出错,比较结果列")
                                    .append(aeComparisonHeaders.getColumnName())
                                    .append("找不到有效的判定定义");
                        } else if (aeComparisonHeaders.getEndDate() == null && currentDate.before(aeComparisonHeaders.getStartDate())) {
                            flag = false;
                            errorMessage.append("账务表字段:[")
                                    .append(ruleLines.getFieldName())
                                    .append("]取值出错,比较结果列")
                                    .append(aeComparisonHeaders.getColumnName())
                                    .append("找不到有效的判定定义");
                        }
                    }
                    for (AeComparisonLines aeComparisonLines : aeComparisonHeaders.getAeComparisonLines()) {
                        String aFieldValue = aeRuleProcessUtil.getComparisonAFieldValue(aeComparisonLines, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                        String bFieldValue = aeRuleProcessUtil.getComparisonBFieldValue(aeComparisonLines, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                        if (aFieldValue == null || "".equals(aFieldValue)) {
                            flag = false;
                            errorMessage.append("会计生成规则:[")
                                    .append(aeRuleHeaders.getRuleName())
                                    .append("] 下的[")
                                    .append(ruleLines.getFieldName())
                                    .append("]字段的比较判定列的通用字段A找不到值！");
                            break;
                        }
                        if (bFieldValue == null || "".equals(bFieldValue)) {
                            flag = false;
                            errorMessage.append("会计生成规则:[")
                                    .append(aeRuleHeaders.getRuleName())
                                    .append("] 下的[")
                                    .append(ruleLines.getFieldName())
                                    .append("]字段的比较判定列的通用字段B找不到值！");
                            break;
                        }
                        if (aeRuleProcessUtil.verityComparison(aeComparisonLines, aFieldValue, bFieldValue)) {
                            fieldValue = aeRuleProcessUtil.getComparisonResultValue(aeComparisonLines, sourceMap, aeEventLineMap, columnMap, columnValueMap);
                            break;
                        }
                        columnValueMap.put(column, fieldValue);
                    }
                }
            }
            if ("NUMBER".equals(ruleLines.getDataType())) {

                if(fieldValue != null ){
                    //拼接符号（MINUS--负号，POSITIVE--正号）
                    if ("MINUS".equals(ruleLines.getNumberType())) {
                        BigDecimal b1 = new BigDecimal("0");
                        BigDecimal b2 = new BigDecimal("0");
                        try {
                            b2 = new BigDecimal(fieldValue);
                        }catch (Exception e){
                            flag = false;
                            errorMessage.append("账务表字段:[")
                                    .append(ruleLines.getColumnComment())
                                    .append("]取值出错,该字段值必须是数值格式！\n");
                        }
                        double doubleValue = b1.subtract(b2).doubleValue();
                        fieldValue = String.valueOf(doubleValue);
                    }

                    if(ruleLines.getColumnDecimalLen()!=null && fieldValue != null){
                        fieldValue = VerifyType.formatDecimalWithZero(Double.parseDouble(fieldValue),ruleLines.getColumnDecimalLen().intValue());
                    }
                }
                fieldNameList.add(ruleLines.getFieldName());
                fieldValueList.add(fieldValue);
            } else if ("VARCHAR".equals(ruleLines.getDataType())) {
                //判定是否需要进行拼接处理
                if (columnName.equals(ruleLines.getFieldName())) {
                    if (fieldValue != null) {
                        columnValuestr.append(fieldValue);
                    }
                } else {
                    fieldNameList.add(ruleLines.getFieldName());
                    columnValuestr = new StringBuffer();
                    if (fieldValue != null) {
                        columnValuestr.append(fieldValue);
                    }
                }
                columnName = ruleLines.getFieldName();
                fieldValue = columnValuestr.toString();
                if(ruleLines.getColumnLen() !=null && fieldValue != null) {
                    int acctLength = ruleLines.getColumnLen().intValue();
                    if (fieldValue.length() > acctLength) {
                        flag = false;
                        errorMessage.append("账务表字段:[")
                                .append(ruleLines.getFieldName())
                                .append("] 取值错误：该字段超出最大长度：[")
                                .append(acctLength).append("]!");
                    }
                }
            }

            if ("Y".equals(ruleLines.getRequiredFlag())) {
                if (fieldValue == null || "".equals(fieldValue)) {
                    flag = false;
                    errorMessage.append("会计生成规则:[")
                            .append(aeRuleHeaders.getRuleName())
                            .append("] 下的[")
                            .append(ruleLines.getColumnComment())
                            .append("]字段为必输字段不能为空！");
                }
            }

            if ("DATE".equals(ruleLines.getDataType())) {
                if (fieldValue != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat(ruleLines.getFormatString());
                    try {
                        Date valueDate = sdf1.parse(fieldValue);
                        fieldValue = sdf.format(valueDate);
                    } catch (Exception e) {
                        flag = false;
                        errorMessage.append("会计生成规则:[")
                                .append(aeRuleHeaders.getRuleName())
                                .append("] 下的[")
                                .append(ruleLines.getFieldName())
                                .append("]字段的日期格式不正确！");
                        break;
                    }
                }
            }

            attributeFlag = AeConstant.AE_DR_AMOUNT.equals(ruleLines.getFieldAttribute())||AeConstant.AE_CR_AMOUNT.equals(ruleLines.getFieldAttribute())
                    ||AeConstant.AE_DR_BASE_AMOUNT.equals(ruleLines.getFieldAttribute())||AeConstant.AE_CR_BASE_AMOUNT.equals(ruleLines.getFieldAttribute());
            if(attributeFlag && !HscsUtil.strIsNull(fieldValue)){
                attributeMap.put(ruleLines.getFieldAttribute(),fieldValue);
            }

            AeUtils.setValue(aeTfrDtlAccounts, ruleLines.getFieldName(), fieldValue);

           /* Long endTime=System.currentTimeMillis();
            logger.debug("会计规则"+aeRuleHeaders.getRuleName()+"下的"+ruleLines.getColumnComment()+"字段的处理时间"+(endTime-startTime)+"ms");*/
        }
        if(!HscsUtil.strIsNull(attributeMap.get(AeConstant.AE_DR_AMOUNT)) && !HscsUtil.strIsNull(attributeMap.get(AeConstant.AE_CR_AMOUNT))){
            flag = false;
            errorMessage.append("同一笔明细账务行不能“借方原币金额”及“贷方原币金额”同时有值！");
        }

        if(!HscsUtil.strIsNull(attributeMap.get(AeConstant.AE_DR_BASE_AMOUNT)) && !HscsUtil.strIsNull(attributeMap.get(AeConstant.AE_CR_BASE_AMOUNT))){
            flag = false;
            errorMessage.append("同一笔明细账务行不能“借方本币金额”及“贷方本币金额”同时有值！");
        }

        if (!flag) {
            aeTfrDtlAccounts = null;
        }
        if (!"".equals(errorMessage.toString())) {
            aeDataConvertProcess.updateTfrData(aeTfrEventId, errorMessage.toString(), "GENERATED_ERROR");

        }
        return aeTfrDtlAccounts;
    }

    @Override
    public String revToTransfer(IRequest iRequest, AeEventBatches aeEventBatches, Date accountingDateTo,String references) throws AeEventProcessException, CodeRuleException{
        if (aeEventBatches == null) {
            throw new AeEventProcessException(EXCEPTION_CODE, "必须指定对应的事件定义才能创建账务", null);
        }

        if (AE_FROZEN_FLAG_N.equals(aeEventBatches.getFrozenFlag())) {
            throw new AeEventProcessException(EXCEPTION_CODE, THE_EVENT_BATCH_NOT_FROZEN, null);
        }

        // 获取批次
        /*try {
            // ateService.reverseTransfer(IRequest iRequest, AeEventBatches aeEventBatches, Date accountingDateTo,String references);
        }catch (Exception e){
            throw new AeEventProcessException(EXCEPTION_CODE,"冲销的原始单据数据验证失败，请检查！ 校验批次为:" + batchNo,null);
        }*/
        return aeUtils.getAeBatchNum(iRequest);
    }

    private boolean judgeFilterDate(IRequest iRequest,Long filterId){
        Date currentDate  = new Date();
        AeFilterHeaders aeFilterHeaders = aeFilterDataCache.getValue(filterId.toString());
        if(aeFilterHeaders == null ){
            AeFilterHeaders headers = new AeFilterHeaders();
            headers.setFilterHeaderId(filterId);
            aeFilterHeaders = aeFilterHeadersService.selectByPrimaryKey(iRequest,headers);
        }
        if(aeFilterHeaders != null) {
            if (aeFilterHeaders.getStartDate() != null) {
                boolean dateFlag = (aeFilterHeaders.getEndDate() != null) && (currentDate.after(aeFilterHeaders.getEndDate())
                        || currentDate.before(aeFilterHeaders.getStartDate()));
                if (dateFlag) {
                    return false;
                } else {
                    return aeFilterHeaders.getEndDate() != null || !currentDate.before(aeFilterHeaders.getStartDate());
                }
            } else {
                return true;
            }
        }
        return false;
    }
}