package hscsm.core.sum.account.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.IRequestListener;
import com.hand.hap.core.impl.DefaultRequestListener;
import com.hand.hap.system.service.IProfileService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hscs.ae.dto.AeFeedbackMessage;
import hscsm.core.sum.account.dto.BatchParam;
import hscsm.core.sum.account.dto.EbsReturnData;
import hscsm.core.sum.account.dto.ResultMessage;
import hscsm.core.sum.account.dto.SumAccount;
import hscsm.core.sum.account.mapper.SumAccountMapper;
import hscsm.core.sum.account.service.ISumAccountService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * EBS总账凭证接口调用
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/7.
 */
@Service
public class SumAccountServiceImpl extends BaseServiceImpl<SumAccount> implements ISumAccountService {

    // 推送的批次ID
    private long groupId = -1;
    // 单词job调用,推送批次计数
    private int postCount = 0;

    private Logger logger = LoggerFactory.getLogger(hscsm.core.sum.account.service.impl.SumAccountServiceImpl.class);

    @Autowired
    private SumAccountMapper sumAccountMapper;

    @Autowired
    private IProfileService profileService;

    /**
     * 根据条件，选择对应的账务汇总数据，进行总账凭证接口的调用
     * @param company 公司名
     * @param eventBatchId 批次ID
     * @param accountingDate 账务生成日期
     * @param batchName 日记账批名
     */
    @Override
    public void sendSumAccountToEBS(String company, String eventBatchId, String accountingDate, String batchName){

        // 获取EBS的接口连接信息
        IRequestListener iRequestListener = new DefaultRequestListener();
        IRequest request = iRequestListener.newInstance();
        String host = profileService.getProfileValue(request, "HSCSM.EBS.HOST");
        String username = profileService.getProfileValue(request, "HSCSM.EBS.USERNAME");
        String password = profileService.getProfileValue(request, "HSCSM.EBS.PASSWORD");

        List<BatchParam> batchParams = sumAccountMapper.selectBatchParams(company, eventBatchId, accountingDate, batchName);
        if(batchParams.size() == 0){
            this.logger.info("没有需要推送的汇总账务，Job执行结束");
            return;
        }
        this.logger.info("总账凭证接口调用Job执行--->总批次:[" + batchParams.size() + "]");
        postCount = 0;

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        // 根据日记账批名和入账日期，对需要进行推送的总账凭证进行分批次推送，单批次为一个post请求
        for (BatchParam batchParam  : batchParams){
            String jeBatchName = batchParam.getJeBatchName();
            String accountingDateParam = batchParam.getAccountingDate();
            List<SumAccount> sumAccounts = sumAccountMapper.getSumAccountsByStatus(jeBatchName, company, eventBatchId, accountingDateParam, batchName);

            // 进行数据的校验，如果数据长度不符合，就直接返回数据校验信息，这批数据将不再进行接口调用
            boolean isDataRight = true;
            List<ResultMessage> resultMessages = new ArrayList<>();
            for(SumAccount sumAccount : sumAccounts){
                ResultMessage resultMessage = new ResultMessage();
                resultMessage.setHapGlId(sumAccount.getHapGlId());
                resultMessage.setAccountingStatus("E");
                StringBuilder errorMessage = new StringBuilder();

                Set<ConstraintViolation<SumAccount>> constrainViolationSet = validator.validate(sumAccount);
                for (ConstraintViolation<SumAccount> constraintViolation : constrainViolationSet){
                    this.logger.error("检测到错误数据,账务记录ID[" + sumAccount.getHapGlId() + "]");
                    errorMessage.append(constraintViolation.getMessage()).append(" | ");
                    isDataRight = false;
                }
                resultMessage.setxErrorMessage(errorMessage.toString());
                resultMessages.add(resultMessage);
            }

            // 校验完成，没有出现错误数据，将这批数据发送到接口
            if(isDataRight){
                this.logger.info("数据校验无误，开始发送向EBS发送请求，日记账批名[" + jeBatchName + "];入账日期[" + accountingDateParam + "]");
                String bodyValTemplate = accountsToJson(sumAccounts);
                ResponseEntity<String> response = sendPost(bodyValTemplate, host, username, password);
                // 调用失败
                if(HttpStatus.INTERNAL_SERVER_ERROR.equals(response.getStatusCode())) {
                    List<ResultMessage> errorMessages = new ArrayList<>();
                    for(SumAccount sumAccount : sumAccounts){
                        ResultMessage resultMessage = new ResultMessage();
                        resultMessage.setHapGlId(sumAccount.getHapGlId());
                        resultMessage.setAccountingStatus("E");
                        resultMessage.setxErrorMessage("EBS接口调用失败，请检查一下数据和网络环境");
                        errorMessages.add(resultMessage);
                    }
                    initGroupId();
                    sumAccountMapper.updateByResult(errorMessages, "E", groupId);
                    this.logger.error("数据传送失败，批次号[" + groupId + "];日记账批名[" + jeBatchName + "];入账日期[" + accountingDateParam + "]");
                } else {
                    // 接口调用成功，将结果存入
                    savePostSuccessResult(response.getBody());
                }
            } else {
                // 将错误信息放入账务汇总表中
                initGroupId();
                sumAccountMapper.updateByResult(resultMessages, "E", groupId);
                this.logger.error("账务数据校验失败，批次号[" + groupId + "];日记账批名[" + jeBatchName + "];入账日期[" + accountingDateParam + "]");
            }
        }
    }

    /**
     * 异步回调结果插入，未走HAP标准插入和更新，不更新LAST_UPDATE_DATE 和 记录版本号
     * @param feedbackMessages 异步回调信息
     * @return 插入影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public EbsReturnData insertByAsynResult(List<AeFeedbackMessage> feedbackMessages) throws Exception {
        EbsReturnData ebsReturnData = new EbsReturnData();
        ebsReturnData.setHapGroupId(feedbackMessages.get(0).getAttribute1());
        List<ResultMessage> resultMessages = new ArrayList<>();
        String status = "S";

        for (AeFeedbackMessage feedbackMessage : feedbackMessages){
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setAccountingStatus(feedbackMessage.getAccountingStatus());
            resultMessage.setHapGlId(feedbackMessage.getTfrSumAccountId().toString());
            if(sumAccountMapper.insertByAsynResult(feedbackMessage) == 0){
                throw new Exception("EBS异步回传数据更新失败，请联系HAP管理员，之后需要将这批数据重传");
            }
            resultMessage.setxReturnStatus("S");
            resultMessages.add(resultMessage);
        }
        ebsReturnData.setStatus(status);
        ebsReturnData.setRows(resultMessages);

        return ebsReturnData;
    }

    /**
     * 接口成功调用，总账接口即时结果处理，根据返回状态，对应修改总账信息或存储发布结果
     * @param resultStr 返回的结果--JSON报文
     */
    private void savePostSuccessResult(String resultStr){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode resultJson = mapper.readTree(resultStr);
            String totalStatusCode = resultJson.get("OutputParameters").get("X_RETURN_STATUS").textValue();

            this.logger.info("汇总账务推送完成,第 " + ++ postCount +" 批--->批次号:["+ groupId +"];传送状态:[" + totalStatusCode + "]");

            String listNode = resultJson.get("OutputParameters").get("P_RETURN_DATA_TB").get("P_RETURN_DATA_TB_ITEM").toString();
            if (!("S".equals(totalStatusCode) || "E".equals(totalStatusCode))){
                throw new Exception("总账凭证接口调用--结果状态参数异常:" + totalStatusCode);
            }
            // 将信息转化为ResultMessage 放入List
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ResultMessage.class);
            // 当传送数据为单条时，返回的结果集没有用[]括起来，加上这条配置，允许这样的单条结果集转化为List
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,true);
            List<ResultMessage> resultMessages = mapper.readValue(listNode, javaType);
            if (sumAccountMapper.updateByResult(resultMessages, totalStatusCode, groupId) == 0){
                throw new Exception("总账凭证接口调用--实时结果数据更新异常");
            }
        } catch (Exception e) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error(e.getMessage());
            }
        }
    }

    /**
     * 生成唯一的批次ID,放入groupId中
     */
    private void initGroupId(){
        //获取当前时间戳
        SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
        String temp = sf.format(new Date());
        //获取4位随机数
        int random = (int)((Math.random() + 1) * 1000);
        groupId = Long.parseLong(temp + random);
    }

    /**
     * 根据查出的数据，拼接需要发送到EBS报文
     * @param sumAccounts 查询出的总账数据
     * @return 拼接好的报文
     */
    private String accountsToJson(List<SumAccount> sumAccounts){
        // 生成groupId
        initGroupId();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ObjectNode importDataInput = mapper.createObjectNode();

        // 拼接固定的报文头
        importDataInput.put("@xmlns","http://xmlns.oracle.com/apps/cux/rest/CUXJNALIMP/import_data/");

        ObjectNode RESTHeaderNode = mapper.createObjectNode();
        RESTHeaderNode.put("@xmlns","http://xmlns.oracle.com/apps/fnd/rest/header")
                .put("Responsibility","")
                .put("RespApplication","")
                .put("SecurityGroup","")
                .put("NLSLanguage","SIMPLIFIED CHINESE")
                .put("Org_Id",0);
        importDataInput.set("RESTHeader",RESTHeaderNode);

        ObjectNode inputParametersNode = mapper.createObjectNode();
        ObjectNode pJournalDataTb = mapper.createObjectNode();

        //将需要推送的总账数据拼接成JSON
        ArrayNode pJournalDataTbItem = mapper.createArrayNode();
        for (SumAccount sumAccount : sumAccounts){
            try {
                sumAccount.setHapGroupId(groupId);
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
                String date = sumAccount.getCunverDt();
                sumAccount.setPrdNm(sf.format(sf.parse(date)));
                pJournalDataTbItem.add(mapper.readTree(mapper.writeValueAsString(sumAccount)));
            } catch (IOException | ParseException e) {
                if (this.logger.isErrorEnabled()) {
                    this.logger.error(e.getMessage(), e);
                }
            }
        }

        pJournalDataTb.set("P_JOURNAL_DATA_TB_ITEM",pJournalDataTbItem);
        inputParametersNode.set("P_JOURNAL_DATA_TB",pJournalDataTb);
        importDataInput.set("InputParameters",inputParametersNode);
        root.set("IMPORT_DATA_Input",importDataInput);

        return root.toString();
    }

    /**
     * 往EBS发送post请求
     * @param bodyValTemplate 请求的JSON格式报文
     * @return 请求结果
     */
    private ResponseEntity<String> sendPost(String bodyValTemplate, String host, String username, String password) {
        //拼接EBS接口请求地址,必须带上协议头
        final String PROTOCOL_HEADER = "http://";
        final String POST_URI = "/webservices/rest/CUXJNALIMP/IMPORT_DATA/";
        String url = PROTOCOL_HEADER + host + POST_URI ;

        RestTemplate restTemplate = new RestTemplate();
        StringHttpMessageConverter stringHttpMessageConverter=new StringHttpMessageConverter(Charset.forName("UTF-8"));
        List<HttpMessageConverter<?>> list= new ArrayList<>();
        list.add(stringHttpMessageConverter);
        restTemplate.setMessageConverters(list);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getToken(username, password));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(bodyValTemplate, headers);

        ResponseEntity<String> resultMessage;

        try {
            resultMessage = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return resultMessage;
        } catch (Exception e) {
            if (this.logger.isErrorEnabled()) {
                this.logger.error(e.getMessage(), e);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据用户名密码获取token,验证方式：Basic Auth
     * @param username EBS 用户名
     * @param password EBS 密码
     * @return EBS接口token
     */
    private String getToken(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
        return  "Basic " + new String(encodedAuth);
    }
}
