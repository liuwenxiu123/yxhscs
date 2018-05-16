package hscsm.core.itf.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hscs.pub.dto.PubSoaSegmentValue;
import hscs.pub.mapper.PubSoaSegmentValueMapper;
import hscsm.core.fnd.mq.dto.HeadLineDto;
import hscsm.core.fnd.mq.dto.ItfImpLines;
import hscsm.core.fnd.mq.mapper.ItfImplLineMapper;
import hscsm.core.fnd.mq.utils.ArithUtil;
import hscsm.core.itf.dto.*;
import hscsm.core.itf.mapper.ItfArInterfaceMapper;
import hscsm.core.itf.mapper.PubAlixSubcomMapper;
import hscsm.core.itf.mapper.PubInvoiceEntityMapper;
import hscsm.core.itf.mapper.QueryUtilMapper;
import org.opensaml.xmlsec.algorithm.MACAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hscsm.core.itf.service.IItfArInterfaceService;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItfArInterfaceServiceImpl extends BaseServiceImpl<ItfArInterface> implements IItfArInterfaceService {


    Logger logger = LoggerFactory.getLogger(ItfArInterfaceServiceImpl.class);

    private static String SERIERS_ONE = "0";
    // 时间格式
    private static String DAY_FORMAT = "yyyy-MM-dd";
    // 还款状态
    private static String REPAY_STATUS_ACTIVE = "生效";
    private static String REPAY_STATUS_CLOSED = "结束";
    private static String REPAY_STATUS_OVER_DATE = "逾期";
    // 合同状态
    private static String CONTRACT_STATUS_CREATED = "创建";
    private static String CONTRACT_STATUS_ACTIVED = "生效";
    private static String CONTRACT_STATUS_CLEARED = "结清";
    private static String CONTRACT_STATUS_CANCEL = "取消";
    private static String CONTRACT_STATUS_REJECTED = "拒绝";
    private static String CONTRACT_STATUS_CLOSED = "关闭";
    private static String NORMAL_TYPE = "正租";
    private static String BACK_TYPE = "回租";
    private static String RUNNING_TYPE = "经营性租赁";

    @Autowired
    ItfImplLineMapper itfImplLineMapper;

    @Autowired
    ItfArInterfaceMapper itfArInterfaceMapper;

    @Autowired
    QueryUtilMapper queryUtilMapper;

    @Autowired
    PubAlixSubcomMapper pubAlixSubcomMapper;

    @Autowired
    PubSoaSegmentValueMapper pubSoaSegmentValueMapper;

    @Autowired
    PubInvoiceEntityMapper pubInvoiceEntityMapper;

    // 月末定时跑 定时更新还款状态
    @Override
    public void updateArInterfaceMonthEnd(String deadDate) {

        String[] strList = deadDate.split("-");
        String strDate = strList[0] + "-" + strList[1];

        // 获取目标表的新记录
        List<ItfArInterface> itfArInterList = itfArInterfaceMapper.queryMonthRaw(strDate);

        List<QueryUtilDto> contractList = new ArrayList<>();
        List<QueryUtilDto> applyList = new ArrayList<>();
        List<QueryUtilDto> collectList = new ArrayList<>();
        List<ItfArInterface> flagArList = new ArrayList<>();
        // 根据申请编号分组
        Map<String, List<ItfArInterface>> map = new HashMap<>();
        for (int i = 0; i < itfArInterList.size(); i++) {
            ItfArInterface itfArInterface = itfArInterList.get(i);
            String applyNum = itfArInterface.getApplyNum();
            if (map.get(applyNum) == null) {
                List<ItfArInterface> list = new ArrayList<>();
                list.add(itfArInterface);
                map.put(applyNum, list);
            } else {
                List<ItfArInterface> list1 = map.get(applyNum);
                list1.add(itfArInterface);
                map.put(applyNum, list1);
            }
        }

        List<QueryUtilDto> repayList1 = new ArrayList<>();
        Iterator iterator = map.entrySet().iterator();

        // 遍历各个合同
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String applyNum = (String) entry.getKey();
            List<ItfArInterface> arlist = (List<ItfArInterface>) entry.getValue();
            int countSeries = arlist.size();
            // 获取最后一期
            ItfArInterface itfArInterfaceEnd = arlist.get(countSeries - 1);
            if ((arlist.size() - 1) != Integer.valueOf(itfArInterfaceEnd.getSerialNumber())) {
                continue;
            }
            QueryUtilDto qut = new QueryUtilDto();
            qut.setAttribute1(applyNum);
            String currentContractStatus = null;
            String currentPaymentStatus = null;
            QueryUtilDto queryUtilDto1 = queryUtilMapper.getApplicateStatus(applyNum); // 获取每个合同下的申请状态
            if (queryUtilDto1 != null) {
                String applyStatus = queryUtilDto1.getAttribute3();
                if (applyStatus != null) {
                    currentPaymentStatus = applyStatus;
                    qut.setAttribute2(applyStatus);
                    applyList.add(qut);
                }
            }
            // 收车日期
            QueryUtilDto qDto = new QueryUtilDto();
            qDto.setAttribute1("MAL_WITHDRAW_INTERFACE");
            qDto.setAttribute2(applyNum);
            QueryUtilDto queryUtilDto11 = queryUtilMapper.queryCollectDate(qDto);
            if (queryUtilDto11 != null) {
                QueryUtilDto queryUtilDto2 = new QueryUtilDto();
                queryUtilDto2.setAttribute1(applyNum);
                queryUtilDto2.setAttribute2(queryUtilDto11.getAttribute1());
                collectList.add(queryUtilDto2);
            }
            // 收车日期 end=======
            QueryUtilDto queryUtilDto = queryUtilMapper.getContractStatus(applyNum); // 获取每个合同下面行的状态
            List<ItfArInterface> itfArInterfaceList = map.get(applyNum);
            for (int i = 0; i < itfArInterfaceList.size(); i++) {
                ItfArInterface itfArInterface = itfArInterfaceList.get(i);
                if (queryUtilDto != null) {
                    String contactStatus = queryUtilDto.getAttribute1();
                    if (contactStatus == null) {
                        contactStatus = queryUtilDto.getAttribute2();
                    }
                    currentContractStatus = contactStatus;
                    if (contactStatus != null) {
                        QueryUtilDto qut22 = new QueryUtilDto();
                        qut22.setAttribute1(itfArInterface.getArInterfaceId() + "");
                        qut22.setAttribute2(contactStatus);
                        qut22.setAttribute3(itfArInterface.getAccountingStatus());
                        // 添加到合同更新集合中
                        contractList.add(qut22);
                    }
                }
            }

            // 只有合同状态有值的时候才去跟新还款状态
            // 比较日期参数和归属期间
            for (int i = 0; i < arlist.size(); i++) {
                ItfArInterface itfArInterface = arlist.get(i);
                flagArList.add(itfArInterface);
                QueryUtilDto qq1 = new QueryUtilDto();
                if (strDate.equals(itfArInterface.getIncomePeriod())) {
                    qq1.setAttribute1(itfArInterface.getArInterfaceId() + "");
                    qq1.setAttribute3(itfArInterface.getApplyNum());
                    // 设置计划还款日期
                    qq1.setAttribute5(itfArInterface.getScheduledRepaymentDate());
                    // 设置时间
                    qq1.setAttribute6(deadDate);
                    if ((ItfArInterfaceServiceImpl.CONTRACT_STATUS_CLEARED.equals(currentContractStatus) || ItfArInterfaceServiceImpl.CONTRACT_STATUS_CLEARED.equals(itfArInterface.getContractStatus()))
                            || (ItfArInterfaceServiceImpl.CONTRACT_STATUS_CANCEL.equals(currentContractStatus) || ItfArInterfaceServiceImpl.CONTRACT_STATUS_CANCEL.equals(itfArInterface.getContractStatus()))) {

                        qq1.setAttribute2(currentContractStatus);
                        repayList1.add(qq1);
                        break;
                    } else if ((ItfArInterfaceServiceImpl.REPAY_STATUS_ACTIVE.equals(itfArInterface.getPaymentStatus()))) {
                        qq1.setAttribute2(ItfArInterfaceServiceImpl.REPAY_STATUS_OVER_DATE);
                        repayList1.add(qq1);
                        break;
                    }
                }
            }
        }

        // 更新合同状态// 申请状态
        if (contractList.size() > 0) {
            queryUtilMapper.updateContractStatus(contractList);
        }
        if (applyList.size() > 0) {
            queryUtilMapper.updateApplicationStatus(applyList);
        }

        // 更新收车日期
        if (collectList.size() > 0) {
            queryUtilMapper.updateCollectDate(collectList);
        }

        // 更新还款状态
        if (repayList1.size() > 0) {
            queryUtilMapper.updateReapplyStatus(repayList1);
        }

        // 打上该月初一号(精确到天)
        if (flagArList.size() > 0) {
            itfArInterfaceMapper.updateMonthFlag(flagArList);
        }

    }

    // 每个月定时更新实际收入 顺序在没有更新job之后
    @Override
    public void updateArActualIncom(String date) {

        List<ItfArInterface> updateItfArInterfaces = new ArrayList<>();

        List<ItfArInterface> itfArInterList = itfArInterfaceMapper.queryActualIncome(date);

        for (int i = 0; i < itfArInterList.size(); i++) {
            ItfArInterface itfArInterface = itfArInterList.get(i);
            String incomePeriod = itfArInterface.getIncomePeriod();
            String payStatus = itfArInterface.getPaymentStatus();
            String collectDate = itfArInterface.getCollectDate();
            String overDays = itfArInterface.getOverdueDays();
            String leaseType11 = itfArInterface.getLeaseType();
            String over90Day = itfArInterface.getOverdue90Date();
            String rentelIncom1 = itfArInterface.getRentalIncomeI1();
            String prepaidRentPerperiod = itfArInterface.getPrepaidRentPerPeriod();
            String receInest1 = itfArInterface.getReceInterestI1(); // A
            String receInest2 = itfArInterface.getReceInterestI2(); // B
            String posFree = itfArInterface.getPostFee();
            String shallCarryInest = itfArInterface.getShallCarryInterest();
            String serviceChargeType = itfArInterface.getServiceChargeFunction();
            String serviceCharge1 = itfArInterface.getServiceChargeI1();
            String serviceCharge2 = itfArInterface.getServiceChargeI2();

            if (payStatus.equals("结束")) {
                itfArInterface.setActualIncome(itfArInterface.getShouldIncome());
                itfArInterface = this.calcActulDetail(itfArInterface, leaseType11, receInest1, shallCarryInest, receInest2, posFree, serviceCharge1, serviceCharge2);
            } else {
                if (payStatus.equals("逾期")) {
                    String[] str2 = over90Day.split("-");

                    if (collectDate == null) {
                        if (Integer.valueOf(overDays) <= 90) {
                            itfArInterface.setActualIncome(itfArInterface.getShouldIncome());
                            itfArInterface = this.calcActulDetail(itfArInterface, leaseType11, receInest1, shallCarryInest, receInest2, posFree, serviceCharge1, serviceCharge2);
                        } else {
                            if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(leaseType11)) {
                                if (incomePeriod.equals(str2[0] + "-" + str2[1])) {
                                    itfArInterface.setActualIncome(ArithUtil.add(Double.valueOf(rentelIncom1 != null ? rentelIncom1 : "0"), Double.valueOf(prepaidRentPerperiod != null ? prepaidRentPerperiod : "0")) + "");

                                    itfArInterface.setActualMonthlyIncome(ArithUtil.add(Double.valueOf(rentelIncom1 != null ? rentelIncom1 : "0"), Double.valueOf(prepaidRentPerperiod != null ? prepaidRentPerperiod : "0")) + "");
                                    itfArInterface.setActualCarryInterest("0");
                                    itfArInterface.setActualServiceCharge("0");

                                } else {
                                    itfArInterface.setActualIncome(prepaidRentPerperiod); // 9
                                    itfArInterface.setActualMonthlyIncome(prepaidRentPerperiod);
                                    itfArInterface.setActualCarryInterest("0");
                                    itfArInterface.setActualServiceCharge("0");
                                }
                            } else {
                                if (ItfArInterfaceServiceImpl.NORMAL_TYPE.equals(leaseType11) || ItfArInterfaceServiceImpl.BACK_TYPE.equals(leaseType11)) {
                                    if ("一次性".equals(serviceChargeType)) {
                                        if (incomePeriod.equals(str2[0] + "-" + str2[1])) { // 7
                                            double dd1 = ArithUtil.add(Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0"), Double.valueOf(serviceCharge2 != null ? serviceCharge2 : "0"));
                                            double dd2 = ArithUtil.add(ArithUtil.add(Double.valueOf(receInest1 != null ? receInest1 : "0"), Double.valueOf(shallCarryInest != null ? shallCarryInest : "0")), Double.valueOf(posFree != null ? posFree : "0"));
                                            itfArInterface.setActualIncome(ArithUtil.add(dd1, dd2) + "");

                                            itfArInterface.setActualMonthlyIncome(receInest1);
                                            itfArInterface.setActualCarryInterest(shallCarryInest);
                                            double aa = ArithUtil.add(Double.valueOf(posFree != null ? posFree : "0"), Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0"));
                                            itfArInterface.setActualServiceCharge(ArithUtil.add(aa, Double.valueOf(serviceCharge2 != null ? serviceCharge2 : "0")) + "");

                                        } else { // 10上半部分
                                            double dd1 = ArithUtil.add(Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0"), Double.valueOf(serviceCharge2 != null ? serviceCharge2 : "0"));
                                            double dd2 = ArithUtil.add(Double.valueOf(shallCarryInest != null ? shallCarryInest : "0"), Double.valueOf(posFree != null ? posFree : "0"));
                                            itfArInterface.setActualIncome(ArithUtil.add(dd1, dd2) + "");

                                            itfArInterface.setActualMonthlyIncome("0");
                                            itfArInterface.setActualCarryInterest(shallCarryInest);
                                            itfArInterface.setActualServiceCharge(ArithUtil.add(ArithUtil.add(Double.valueOf(posFree != null ? posFree : "0.0"), Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0.0")), Double.valueOf(serviceCharge2 != null ? serviceCharge2 : "0.0")) + "");
                                        }

                                    } else {
                                        if (incomePeriod.equals(str2[0] + "-" + str2[1])) { // 8
                                            double dd1 = ArithUtil.add(ArithUtil.add(Double.valueOf(receInest1 != null ? receInest1 : "0"), Double.valueOf(shallCarryInest != null ? shallCarryInest : "0")), Double.valueOf(posFree != null ? posFree : "0"));
                                            itfArInterface.setActualIncome(ArithUtil.add(dd1, Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0")) + "");

                                            itfArInterface.setActualMonthlyIncome(receInest1);
                                            itfArInterface.setActualCarryInterest(shallCarryInest);
                                            itfArInterface.setActualServiceCharge(ArithUtil.add(Double.valueOf(posFree != null ? posFree : "0"), Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0")) + "");

                                        } else { // 10下部分
                                            itfArInterface.setActualIncome(ArithUtil.add(Double.valueOf(shallCarryInest != null ? shallCarryInest : "0"), Double.valueOf(posFree != null ? posFree : "0")) + "");

                                            itfArInterface.setActualMonthlyIncome("0");
                                            itfArInterface.setActualCarryInterest(shallCarryInest);
                                            itfArInterface.setActualServiceCharge(posFree);
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        collectDate = collectDate.replace("/", "-");
                        String[] str3 = collectDate.split("-");
                        if (Integer.valueOf(overDays) <= 90) {
                            if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(leaseType11)) {
                                if (incomePeriod.equals(str3[0] + "-" + str3[1])) { // 11 上半部分
                                    itfArInterface.setActualIncome(ArithUtil.add(Double.valueOf(rentelIncom1 != null ? rentelIncom1 : "0"), Double.valueOf(prepaidRentPerperiod != null ? prepaidRentPerperiod : "0")) + "");
                                } else { // 12 上半部分
                                    itfArInterface.setActualIncome(prepaidRentPerperiod);
                                }

                                itfArInterface.setActualMonthlyIncome(itfArInterface.getActualIncome());
                                itfArInterface.setActualCarryInterest("0");
                                itfArInterface.setActualServiceCharge("0");

                            } else {
                                if ("一次性".equals(serviceChargeType)) { // 11 中部分
                                    double dd1 = ArithUtil.add(Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0"), Double.valueOf(serviceCharge2 != null ? serviceCharge2 : "0"));
                                    double dd2 = ArithUtil.add(Double.valueOf(posFree != null ? posFree : "0"), Double.valueOf(shallCarryInest != null ? shallCarryInest : "0"));
                                    double dd3 = ArithUtil.add(dd1, dd2);
                                    if (incomePeriod.equals(str3[0] + "-" + str3[1])) {
                                        itfArInterface.setActualIncome(ArithUtil.add(dd3, Double.valueOf(receInest1 != null ? receInest1 : "0")) + "");

                                        itfArInterface.setActualMonthlyIncome(receInest1);
                                    } else {  // 12 中部分
                                        itfArInterface.setActualIncome(dd3 + "");

                                        itfArInterface.setActualMonthlyIncome("0");
                                    }
                                    itfArInterface.setActualCarryInterest(shallCarryInest);
                                    itfArInterface.setActualServiceCharge(ArithUtil.add(dd1, Double.valueOf(posFree != null ? posFree : "0")) + "");


                                } else {
                                    double dd2 = ArithUtil.add(Double.valueOf(posFree != null ? posFree : "0"), Double.valueOf(shallCarryInest != null ? shallCarryInest : "0"));
                                    if (incomePeriod.equals(str3[0] + "-" + str3[1])) { //    11 后部分
                                        double dd1 = ArithUtil.add(Double.valueOf(receInest1 != null ? receInest1 : "0"), Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0"));
                                        itfArInterface.setActualIncome(ArithUtil.add(dd1, dd2) + "");

                                        itfArInterface.setActualMonthlyIncome(receInest1);
                                        itfArInterface.setActualServiceCharge(ArithUtil.add(Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0"), Double.valueOf(posFree != null ? posFree : "0")) + "");

                                    } else { // 12 后部分
                                        itfArInterface.setActualIncome(dd2 + "");

                                        itfArInterface.setActualMonthlyIncome("0");
                                        itfArInterface.setActualServiceCharge(posFree);
                                    }
                                    itfArInterface.setActualCarryInterest(shallCarryInest);
                                }
                            }
                        } else {
                            if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(leaseType11)) {
                                itfArInterface.setActualIncome(prepaidRentPerperiod); // 13 上半

                                itfArInterface.setActualMonthlyIncome(prepaidRentPerperiod);
                                itfArInterface.setActualCarryInterest("0");
                                itfArInterface.setActualServiceCharge("0");

                            } else {
                                if (ItfArInterfaceServiceImpl.NORMAL_TYPE.equals(leaseType11) || ItfArInterfaceServiceImpl.BACK_TYPE.equals(leaseType11)) {
                                    double dd1 = ArithUtil.add(Double.valueOf(posFree != null ? posFree : "0"), Double.valueOf(shallCarryInest != null ? shallCarryInest : "0"));
                                    if ("一次性".equals(serviceChargeType)) { // 13中
                                        double dd2 = ArithUtil.add(Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0"), Double.valueOf(serviceCharge2 != null ? serviceCharge2 : "0"));
                                        itfArInterface.setActualIncome(ArithUtil.add(dd1, dd2) + "");

                                        itfArInterface.setActualServiceCharge(ArithUtil.add(dd2, Double.valueOf(posFree != null ? posFree : "0")) + "");

                                    } else { // 13下
                                        itfArInterface.setActualIncome(dd1 + "");

                                        itfArInterface.setActualServiceCharge(posFree);
                                    }
                                    itfArInterface.setActualMonthlyIncome("0");
                                    itfArInterface.setActualCarryInterest(shallCarryInest);
                                }
                            }
                        }

                    }

                }

            }

            // 本期实提收入 end
            updateItfArInterfaces.add(itfArInterface); //把更新的数据添加到更新集合中
        }
        if (updateItfArInterfaces.size() > 0) {
            itfArInterfaceMapper.updateArInterfaceData(updateItfArInterfaces); // 更新目标表数据
        }
    }

    private ItfArInterface calcActulDetail(ItfArInterface itfArInterface, String leaseType11, String receInest1, String shallCarryInest, String receInest2, String posFree, String serviceCharge1, String serviceCharge2) {

        /*********新添加的三个字段***************/
        // 实提月供收入
        if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(leaseType11)) {
            itfArInterface.setActualMonthlyIncome(itfArInterface.getActualIncome());
            // 实提贴息
            itfArInterface.setActualCarryInterest("0");
            // 实提手续费
            itfArInterface.setActualServiceCharge("0");

        } else if (ItfArInterfaceServiceImpl.NORMAL_TYPE.equals(leaseType11) || ItfArInterfaceServiceImpl.BACK_TYPE.equals(leaseType11)) {
            itfArInterface.setActualMonthlyIncome(ArithUtil.add(Double.valueOf(receInest1 != null ? receInest1 : "0.0"), Double.valueOf(receInest2 != null ? receInest2 : "0.0")) + "");
            // 实提贴息
            itfArInterface.setActualCarryInterest(shallCarryInest);
            // 实提手续费
            itfArInterface.setActualServiceCharge(ArithUtil.add(ArithUtil.add(Double.valueOf(posFree != null ? posFree : "0.0"), Double.valueOf(serviceCharge1 != null ? serviceCharge1 : "0.0")), Double.valueOf(serviceCharge2 != null ? serviceCharge2 : "0.0")) + "");

        }
        /*********新添加的三个字段 end***************/
        return itfArInterface;
    }


    @Override
    public List<ItfArInterface> queryNeedUpdateArInterface() {
        return itfArInterfaceMapper.queryNeedUpdateArInterface();
    }

    /**
     * 每天需要更新
     */
    @Override
    public void updateArInterface() {
        // 查询MAL_MONPAY_INTERFACE增量数据
        List<ItfImpLines> linesList = itfImplLineMapper.getAdd_MAL();

        List<HeadLineDto> headLineDtos = new ArrayList<>(); // 打标志的list
        List<QueryUtilDto> queryUtilDtos = new ArrayList<>(); // 查找目标表条件数据集合

        for (int i = 0; i < linesList.size(); i++) {
            ItfImpLines impLines1 = linesList.get(i);
            String tempApplyNum = impLines1.getValue2();
            String tempSeriesNum = impLines1.getValue6();
            String tempAmount = impLines1.getValue3();

            QueryUtilDto queryUtilDto = new QueryUtilDto();
            queryUtilDto.setAttribute1(tempApplyNum);
            queryUtilDto.setAttribute2(tempSeriesNum);
            double amount = 0;
            if (tempAmount != null) {
                amount = Double.valueOf(tempAmount);
            }
            queryUtilDto.setAmount(amount);
            queryUtilDtos.add(queryUtilDto);

            HeadLineDto headLineDto = new HeadLineDto();
            headLineDto.setLineId(impLines1.getLineId());
            headLineDtos.add(headLineDto);

        }
        if (queryUtilDtos.size() > 0) {
            itfArInterfaceMapper.updateArPaymentStatus(queryUtilDtos);
            itfImplLineMapper.updateTimeFlag(headLineDtos); // 增量数据的标志
        }
    }


    /**
     * 行表插入到行表中
     */
    @Override
    public void addCalcArInterface(int pageSize, String scheme, String applyNum) {

        List<ItfArInterface> itfArInterfaces = new ArrayList<ItfArInterface>(); // 最后插入到目标表的集合
        List<BatchDto> batchDtos = new ArrayList<BatchDto>();
        // 根据申请号关联合同成交明细表接口ALIX_CONTRACT_INTERFACE，OPL_CONTRACT_INTERFACE
        // 查询出符合条件的数据
        List<ItfImpLines> itfImpLines = null;
        if (null == applyNum) { // 这个判断主要是多种情况
            itfImpLines = itfImplLineMapper.queryCalcedItfs(pageSize);
        } else {
            String[] times = applyNum.split("@");
            if (times.length > 1) { // 如果是时间范围
                QueryUtilDto qUtil = new QueryUtilDto();
                qUtil.setAttribute1(times[0]);
                qUtil.setAttribute2(times[1]);
                itfImpLines = itfImplLineMapper.queryCalcedItfsByTimeRange(qUtil);
            } else { // 指定合同查询
                itfImpLines = itfImplLineMapper.queryCalcedItfsByApplyNum(applyNum);
            }
        }

        Map allMap = this.sortGroupByApplNum(itfImpLines); // 存放所有的申请编号对应的期数(包括重复的情况)

        // 合同分期序号去重----------------------
        itfImpLines = this.distinctListBySeries(itfImpLines);
        // 合同分期序号去重----------end---------
        Map groupDataMap = this.sortGroupByApplNum(itfImpLines);

        List<QueryUtilDto> applyDataList = new ArrayList<>();
        List<QueryUtilDto> servicefunctionDataList = new ArrayList<>();

        List<QueryUtilDto> applyAllList = new ArrayList<>();
        Set<String> keys = groupDataMap.keySet();// 得到全部的key
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            QueryUtilDto qq1 = new QueryUtilDto();
            qq1.setAttribute1(iter.next());
            applyAllList.add(qq1);
        }

        if (itfImpLines.size() > 0) {
            applyDataList = queryUtilMapper.getAttrByTwoInterfaceByList(applyAllList);
            servicefunctionDataList = queryUtilMapper.getSeriveFunctionList(applyAllList);
        }

        double sumPost = 0.0;
        double sumInterest = 0.0;
        double sumPrepaid = 0.0;

        // 按照申请编号计算
        Iterator iterator = groupDataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            List<ItfImpLines> itfImpList = (List<ItfImpLines>) entry.getValue(); // 获取到每个合同下的分期明细
            // =====添加没有的其他期的数据***********************************====
            ItfImpLines impLines = itfImpList.get(0); // 获取第一条数据获取判断期数
            String _interfaceName = impLines.getInterfaceName();
            // 获取分组后的有序的合同 如果该合同最后一行的期数不能 该合同数量 则舍弃这个合同

            ItfImpLines impLinesEnd = itfImpList.get(itfImpList.size() - 1);
            // 最后一期的期号是多少
            String lastSeries = impLinesEnd.getValue21();
            String _seriesNum1 = impLinesEnd.getValue12().split("期")[0];

            if (!"A".equals(scheme)) { // 后期导入数据
                int _listSize = itfImpList.size();
                int _seriesNum = Integer.valueOf(impLines.getValue12().split("期")[0]);

                if ((("ALIX_AR_INTERFACE".equals(_interfaceName) && _listSize != (_seriesNum + 1)) || ("OPL_AR_INTERFACE".equals(_interfaceName) && _listSize != _seriesNum) && !lastSeries.equals(_seriesNum1))) { // 如果不相等 则表示该合同不完整 跳过当前合同
                    continue;
                }
            } else { // 初始化导入数据
                if (!(("ALIX_AR_INTERFACE".equals(_interfaceName) || "OPL_AR_INTERFACE".equals(_interfaceName)) && lastSeries.equals(_seriesNum1))) { // 如果不相等 则表示该合同不完整 跳过当前合同
                    continue;
                }
            }

            String series = impLines.getValue21();

            if (!"0".equals(series)) { // 如果缺少前面期数
                Calendar calendar = Calendar.getInstance();
                for (int i = 0; i < Integer.valueOf(series); i++) {
                    ItfImpLines impLinesAdditon = new ItfImpLines();
                    impLinesAdditon.setSourceIterfaceId(impLines.getSourceIterfaceId()); // 来源系统代码
                    impLinesAdditon.setInterfaceName(impLines.getInterfaceName()); // 接口名称
                    impLinesAdditon.setValue3(impLines.getValue3()); // *合同号
                    impLinesAdditon.setValue4(impLines.getValue4()); // *合同状态
                    impLinesAdditon.setValue5(impLines.getValue5()); // *合同生效日期
                    impLinesAdditon.setValue6(impLines.getValue6()); // *租赁公司
                    impLinesAdditon.setValue7(impLines.getValue7()); // *分公司
                    impLinesAdditon.setValue8(impLines.getValue8()); // *客户名称
                    impLinesAdditon.setValue9(impLines.getValue9()); // *租赁属性
                    impLinesAdditon.setValue10(impLines.getValue10()); // *业务类型
                    impLinesAdditon.setValue11(impLines.getValue11()); // *产品方案名称
                    impLinesAdditon.setValue12(impLines.getValue12()); // *融资期限
                    impLinesAdditon.setValue17("0"); // *融资额
                    impLinesAdditon.setValue18("0"); // *应收本金（含税）
                    impLinesAdditon.setValue19("0"); // 应收利息（含税）
                    impLinesAdditon.setValue20("0"); // 应收手续费（含税）
                    impLinesAdditon.setValue21("0"); // 应收贴息（含税）
                    impLinesAdditon.setValue22("0"); // 应收贴手续费（含税）
                    impLinesAdditon.setValue23("0"); // 本金税金
                    impLinesAdditon.setValue24("0"); // 收入税金
                    impLinesAdditon.setValue26(""); // 起始日期
                    impLinesAdditon.setValue27(""); // 终止日期

                    calendar.setTime(this.stringToDate(impLines.getValue24(), ItfArInterfaceServiceImpl.DAY_FORMAT));
                    impLinesAdditon.setValue21(i + ""); // *分期序号
                    impLinesAdditon.setValue2(impLines.getValue2()); // *申请编号
                    impLinesAdditon.setValue1(impLines.getValue2() + "-" + impLinesAdditon.getValue21()); // *唯一标识 = 申请编号+ "-" + 期数
                    calendar.add(calendar.MONTH, -(Integer.valueOf(series) - i));
                    impLinesAdditon.setValue24(this.calendarToStr(calendar.getTime(), ItfArInterfaceServiceImpl.DAY_FORMAT)); // *计划还款日期
                    impLinesAdditon.setValue25("0"); // *每期客户租金
                    impLinesAdditon.setValue26("0"); // *每期月供利息
                    impLinesAdditon.setValue27("0"); // *每期月供本金
                    impLinesAdditon.setValue28("0"); // *每期本金税金
                    impLinesAdditon.setValue29("0"); // *每期收入税金
                    impLinesAdditon.setValue30("0"); // *本金余额
                    impLinesAdditon.setValue31("0"); // *预付租金金额
                    impLinesAdditon.setValue32("0"); // *预付租金每期摊销金额
                    impLinesAdditon.setValue33("0"); // *每期应收贴息
                    impLinesAdditon.setValue34("0"); // *每期应收贴手续费
                    impLinesAdditon.setValue35("0"); // *每期应收手续费
                    itfImpList.add(i, impLinesAdditon);
                }
            }
            // =====添加没有的其他期的数据 end***********************************====
            for (int j = 0; j < itfImpList.size(); j++) {
                ItfImpLines itfImpLines1 = itfImpList.get(j);
                sumPost += Double.valueOf(itfImpLines1.getValue33() != null && !"".equals(itfImpLines1.getValue33()) ? itfImpLines1.getValue33() : "0");
                sumInterest += Double.valueOf(itfImpLines1.getValue34() != null && !"".equals(itfImpLines1.getValue34()) ? itfImpLines1.getValue34() : "0");
                sumPrepaid += Double.valueOf(itfImpLines1.getValue32() != null && !"".equals(itfImpLines1.getValue32()) ? itfImpLines1.getValue32() : "0");
            }
            // 每个合同下 手续费收取方式
            //====================优化部分===================
//            QueryUtilDto queryDto = queryUtilMapper.getSeriveFunction(itfImpList.get(0).getValue2());
            QueryUtilDto queryDto = this.getServiceFunction(servicefunctionDataList, itfImpList.get(0).getValue2());
            //====================优化部分===================
            String serviceChargeFunction = "一次性";
            if (queryDto != null) {
                serviceChargeFunction = queryDto.getAttribute6();
            }
            PubAlixSubcom pubAlixSubcom1 = null;
            for (int i = 0; i < itfImpList.size(); i++) {    // 遍历所有的分期明细 完成每个申请号
                ItfImpLines impImp = itfImpList.get(i);  // 遍历合同编号对应的行 对每个字段进行设置
                if (i == 0) { // 只进行一次查询
                    PubAlixSubcom pubAlixSubcom = new PubAlixSubcom();
                    pubAlixSubcom.setBelongcomName(impImp.getValue6());
                    pubAlixSubcom.setSubcomName(impImp.getValue7());
                    pubAlixSubcom1 = pubAlixSubcomMapper.selectOne(pubAlixSubcom);
                }
                ItfArInterface calcItfImpInter = null;
                if (ItfArInterfaceServiceImpl.SERIERS_ONE.equals(impImp.getValue21())) { // 判断当前是不不是第0期
                    calcItfImpInter = this.getCalcItfArInterface(itfImpList, impImp, true, serviceChargeFunction, itfArInterfaces, sumPost, sumInterest, sumPrepaid, pubAlixSubcom1, applyDataList);
                } else {
                    calcItfImpInter = this.getCalcItfArInterface(itfImpList, impImp, false, serviceChargeFunction, itfArInterfaces, sumPost, sumInterest, sumPrepaid, pubAlixSubcom1, applyDataList);
                }
                itfArInterfaces.add(calcItfImpInter);
            }
            itfArInterfaces = this.setOverStartDate(itfArInterfaces);
        }

        if (itfArInterfaces.size() > 0) {
            Map<String, String> mm = new HashMap();
            // 更新去重后的标志 start
            for (int i = 0; i < itfArInterfaces.size(); i++) {
                String _appNum1 = itfArInterfaces.get(i).getApplyNum();
                mm.put(_appNum1, _appNum1);
            }

            Iterator ii = mm.entrySet().iterator();
            while (ii.hasNext()) {
                Map.Entry entry1 = (Map.Entry) ii.next();
                String _applyNum1 = (String) entry1.getValue();
                List<ItfImpLines> list11 = (List<ItfImpLines>) allMap.get(_applyNum1);
                for (int j = 0; j < list11.size(); j++) {
                    BatchDto batchDto1 = new BatchDto();
                    batchDto1.setId(list11.get(j).getLineId());
                    batchDtos.add(batchDto1);
                }
            }


            // 更新去重后的标志 end=========================

            // 插入目标表
            System.out.println("*******************insert begin*************************");
            itfArInterfaceMapper.batchInsertArInterface(itfArInterfaces);
            System.out.println("********************insert end************************");

            // 更新标志
            itfImplLineMapper.batchUpdateLineFlag(batchDtos);
        }
    }


    /**
     * 有序的list 申请编号相同根据分期序号去重
     */

    private List<ItfImpLines> distinctListBySeries(List<ItfImpLines> lists) {
        List<ItfImpLines> list = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            ItfImpLines lines = lists.get(i);
            if (i == 0) {
                list.add(lines);
            } else {
                ItfImpLines impLines = lists.get(i - 1); // 上一条数据
                if (impLines.getValue21().equals(lines.getValue21())) { // 重复
                    if (!"0.0".equals(lines.getValue25()) || !"0".equals(lines.getValue25())) { // 取value25不为'0.0' 如果是不做添加
                        // 替换上个位置
                        list.remove(list.size() - 1);
                        list.add(lines);
                    }
                } else {
                    list.add(lines);
                }
            }
        }
        return list;
    }


    /**
     * 去除相同申请编号 不同的headId 选择最大的一个headId 对应的申请编号
     *
     * @param itfImpLines
     * @return
     */

    private List<ItfImpLines> repeatLineByHeadId(List<ItfImpLines> itfImpLines) {

        List<ItfImpLines> linesList = new ArrayList<>();
        Map map1 = this.sortGroupByApplNum(itfImpLines);
        Iterator iterator1 = map1.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator1.next();
            List<ItfImpLines> list = (List<ItfImpLines>) entry.getValue();
            Map<String, List<ItfImpLines>> map2 = new HashMap();
            for (int i = 0; i < list.size(); i++) {
                ItfImpLines itfImpLines1 = list.get(i);
                String headId = itfImpLines1.getHeaderId() + "";
                List<ItfImpLines> tempList1 = map2.get(headId);
                if (tempList1 == null) {
                    List<ItfImpLines> linesList1 = new ArrayList<>();
                    linesList1.add(itfImpLines1);
                    map2.put(headId, linesList1);
                } else {
                    tempList1.add(itfImpLines1);
                    map2.put(headId, tempList1);
                }
            }

            Iterator iterator2 = map2.entrySet().iterator();
            long temp = 0L;
            while (iterator2.hasNext()) {
                Map.Entry entry1 = (Map.Entry) iterator2.next();
                long headerId = Long.parseLong((String) entry1.getKey());
                if (temp < headerId) {
                    temp = headerId;
                }
            }

            List<ItfImpLines> tList = (List<ItfImpLines>) map2.get(temp + "");
            for (int i = 0; i < tList.size(); i++)
                linesList.add(tList.get(i));
        }
        return linesList;
    }


    /**
     * 把查出的数据集合 根据合同编号分组
     *
     * @param itfImpLines
     * @return
     */

    private Map sortGroupByApplNum(List<ItfImpLines> itfImpLines) {
        // 对数据根绝申请编号进行分组
        Map<String, List<ItfImpLines>> groupDataMap = new HashMap<>();
        // 数据申请编号分组部分
        for (int i = 0; i < itfImpLines.size(); i++) {
            ItfImpLines itfImpInterfaces1 = itfImpLines.get(i);
            String applNum = itfImpInterfaces1.getValue2();
            List<ItfImpLines> implList = groupDataMap.get(applNum); // 根据编号获取对应分组的数据
            if (implList == null) {
                List<ItfImpLines> itfImpList = new ArrayList<>();
                itfImpList.add(itfImpInterfaces1);
                groupDataMap.put(applNum, itfImpList);
            } else {
                implList.add(itfImpInterfaces1);
            }
        }
        return groupDataMap;
    }


    /**
     * 根据申请编号查找合同信息和其他信息
     */
    private QueryUtilDto getApplyContrListData(List applyDataList, String appNum) {
        QueryUtilDto queryUtilDto = null;
        for (int i = 0; i < applyDataList.size(); i++) {
            QueryUtilDto queryUtilDto1 = (QueryUtilDto) applyDataList.get(i);
            if (appNum.equals(queryUtilDto1.getAttribute7())) {
                queryUtilDto = queryUtilDto1;
                break;
            }
        }
        return queryUtilDto;
    }

    /**
     * 根据申请编号查找合同手续费收取方式
     */
    private QueryUtilDto getServiceFunction(List serviceFunctionList, String appNum) {
        QueryUtilDto queryUtilDto = null;
        for (int i = 0; i < serviceFunctionList.size(); i++) {
            QueryUtilDto queryUtilDto1 = (QueryUtilDto) serviceFunctionList.get(i);
            if (appNum.equals(queryUtilDto1.getAttribute7())) {
                queryUtilDto = queryUtilDto1;
                break;
            }
        }
        return queryUtilDto;
    }


    /**
     * 计算每一期字段的数据
     *
     * @param itfImpData
     * @param isZero
     * @return
     */
    private ItfArInterface getCalcItfArInterface(List<ItfImpLines> itfImpList, ItfImpLines itfImpData, Boolean isZero, String serviceChargeFunction, List<ItfArInterface> itfArInterfaces, Double sumInterest, Double sumPost, Double sumPrepaid, PubAlixSubcom pubAlixSubcom, List applyDataList) {

        ItfArInterface itfArInterface = new ItfArInterface();

        itfArInterface.setSourceIterfaceId(itfImpData.getLineId());

        /***为其他业务加的四个字段***/
        itfArInterface.setAttribute6(itfImpData.getValue25());
        itfArInterface.setAttribute7(itfImpData.getValue26());
        itfArInterface.setAttribute8(itfImpData.getValue27());
        itfArInterface.setAttribute9(itfImpData.getValue35());

        /***为其他业务加的四个字段  end***/
        itfArInterface.setApplyNum(itfImpData.getValue2()); // 申请编号
        itfArInterface.setContractStartDate(itfImpData.getValue5()); // 合同生效日期
        itfArInterface.setCustomerName(itfImpData.getValue8()); // 客户名称
        itfArInterface.setProdSchemeName(itfImpData.getValue11()); // 产品名称
        itfArInterface.setUniqueCode(itfImpData.getValue1()); // 唯一标识

        // ==============优化更改===================
//        QueryUtilDto queryUtilDto3 = queryUtilMapper.getAttrByTwoInterface(itfArInterface.getApplyNum());
        QueryUtilDto queryUtilDto3 = this.getApplyContrListData(applyDataList, itfArInterface.getApplyNum());
        // ==============优化更改===================
        String _leaseType = queryUtilDto3.getAttribute4(); // 租赁属性
        String _serviceType = queryUtilDto3.getAttribute5(); // 业务类型
        if (ItfArInterfaceServiceImpl.NORMAL_TYPE.equals(_leaseType)) {
            if ("库存融资".equals(_serviceType)
                    || "车抵贷".equals(_serviceType)
                    || "消费贷".equals(_serviceType)
                    || "押车贷".equals(_serviceType)) {
                itfArInterface.setLeaseType(ItfArInterfaceServiceImpl.NORMAL_TYPE);
            } else if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(_serviceType)) {
                itfArInterface.setLeaseType(ItfArInterfaceServiceImpl.RUNNING_TYPE);
            }

        } else if (ItfArInterfaceServiceImpl.BACK_TYPE.equals(_leaseType)) {
            if ("消费贷".equals(_serviceType) ||
                    "库存融资".equals(_serviceType) ||
                    "车抵贷".equals(_serviceType) ||
                    "押车贷".equals(_serviceType)) {
                itfArInterface.setLeaseType(ItfArInterfaceServiceImpl.BACK_TYPE);
            }

        } else if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(_leaseType)) {
            if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(_serviceType)) {
                itfArInterface.setLeaseType(ItfArInterfaceServiceImpl.RUNNING_TYPE);
            }
        }

        itfArInterface.setLeaseCompany(itfImpData.getValue6()); // 租赁公司
        itfArInterface.setSubCompany(itfImpData.getValue7()); // 分公司

        if (pubAlixSubcom != null) {
            String subComVal = pubAlixSubcom.getSubcomValue();
            if (itfImpData.getInterfaceName().equals("ALIX_AR_INTERFACE")) {
                itfArInterface.setSubComValue(subComVal); // 分公司段
            } else {
                if (itfImpData.getValue7() != null) {
                    PubSoaSegmentValue soaSegmentValue = new PubSoaSegmentValue();
                    soaSegmentValue.setDescription(itfImpData.getValue7());
                    List<PubSoaSegmentValue> soaSegmentValue1 = pubSoaSegmentValueMapper.select(soaSegmentValue);
                    if (soaSegmentValue1.size() == 1) {
                        itfArInterface.setSubComValue(soaSegmentValue1.get(0).getSegmentValue());
                    }

                }
            }
        }

        if (itfImpData.getValue6() != null) {
            if (itfImpData.getValue6().equals("上海易鑫")) { // 所属公司段
                itfArInterface.setBelongComValue("03001");
            } else if (itfImpData.getValue6().equals("天津易鑫")) {
                itfArInterface.setBelongComValue("05001");
            } else if (itfImpData.getValue6().equals("北京易鑫")) {
                itfArInterface.setBelongComValue("07001");
            } else if (itfImpData.getValue6().equals("广州荣车")) {
                itfArInterface.setBelongComValue("14001");
            }
        }

        itfArInterface.setSerialNumber(itfImpData.getValue21()); // 分期序号

        if (itfImpData.getValue27() == null || "".equals(itfImpData.getValue27())) {
            itfImpData.setValue27("0");
        }
        itfArInterface.setRecePrincipal(itfImpData.getValue27()); // 每期月供本金
        if (itfImpData.getValue28() == null || "".equals(itfImpData.getValue28())) {
            itfImpData.setValue28("0");
        }
        itfArInterface.setPrincipalTax(itfImpData.getValue28()); // 本期本金税金
        if (itfImpData.getValue32() == null || "".equals(itfImpData.getValue32())) {
            itfImpData.setValue32("0");
        }
        if (itfImpData.getValue30() == null || "".equals(itfImpData.getValue30())) {
            itfImpData.setValue3("0");
        }
        itfArInterface.setPrincipalBalance(itfImpData.getValue30()); // 本金余额
        itfArInterface.setContractStatus(ItfArInterfaceServiceImpl.CONTRACT_STATUS_ACTIVED); // 合同状态
        if (serviceChargeFunction == null || "".equals(serviceChargeFunction)) {
            serviceChargeFunction = "一次性";
        }
        itfArInterface.setServiceChargeFunction(serviceChargeFunction); //  手续费收取方式

        if (isZero) { // 计划还款日期 如果他是第0期
            for (int i = 0; i < itfImpList.size(); i++) {
                ItfImpLines impLines = itfImpList.get(i);
                if ("1".equals(impLines.getValue21())) { // 只取第一期 第零期的值来源于第一期
                    String monthStr = this.getSpanMonthDateStr(impLines.getValue24(), -1); // 第一期计划还款日期，月份-1
                    itfArInterface.setScheduledRepaymentDate(monthStr); // 计划还款日期
                    String _monthStr = itfArInterface.getScheduledRepaymentDate();
                    itfArInterface.setIncomePeriod(_monthStr.substring(0, _monthStr.lastIndexOf("-"))); // 收入归属期间
                    itfArInterface.setAccountingDate(this.getLastDayTheMonth(itfArInterface.getIncomePeriod()));
                    itfArInterface.setShallCarryInterest(this.getNumDivi(Double.valueOf(impLines.getValue33() != null && !"".equals(impLines.getValue33()) ? impLines.getValue33() : "0"), itfArInterface, impLines) + ""); // 本期应提贴息
                    itfArInterface.setPostFee(this.getNumDivi(Double.valueOf(impLines.getValue34() != null && !"".equals(impLines.getValue34()) ? impLines.getValue34() : "0"), itfArInterface, impLines) + ""); // 本期应提贴手续费
                    itfArInterface.setPrepaidRentPerPeriod(this.getNumDivi(Double.valueOf(impLines.getValue32() != null && !"".equals(impLines.getValue32()) ? impLines.getValue32() : "0"), itfArInterface, impLines) + ""); // 预付租金每期摊销金额
                    break;
                }
            }
            itfArInterface.setRentalIncomeI1("0"); // 经租本期应提租金收入I1
            itfArInterface.setReceInterestI1("0");// 本期应提利息收入I1
            itfArInterface.setServiceChargeI1("0");// 本期应提手续费I1
            itfArInterface.setPaymentStatus(ItfArInterfaceServiceImpl.REPAY_STATUS_CLOSED); // 还款状态（生效、结束、逾期）
            itfArInterface.setOverdueDays("0");// 逾期天数

        } else {
            itfArInterface.setScheduledRepaymentDate(itfImpData.getValue24()); // 计划还款日期
            String _monthStr = itfArInterface.getScheduledRepaymentDate();
            _monthStr = _monthStr.replace("/", "-");
            itfArInterface.setIncomePeriod(_monthStr.substring(0, _monthStr.lastIndexOf("-"))); // 收入归属期间
            itfArInterface.setAccountingDate(this.getLastDayTheMonth(itfArInterface.getIncomePeriod()));
            String preNum = Integer.valueOf(itfImpData.getValue21()) - 1 + ""; // 上一期的的期号
            for (int i = 0; i < itfImpList.size(); i++) {
                ItfImpLines impLines = itfImpList.get(i);
                if (preNum.equals(impLines.getValue21())) { // 找出上期的
                    if (itfImpList.size() == (i + 1)) {
                        itfArInterface.setRentalIncomeI1(ArithUtil.sub(Double.valueOf(itfImpData.getValue25() != null ? itfImpData.getValue25() : "0"), Double.valueOf("0")) + ""); // 经租本期应提租金收入I1
                        itfArInterface.setReceInterestI1(ArithUtil.sub(Double.valueOf(itfImpData.getValue26() != null ? itfImpData.getValue26() : "0"), Double.valueOf("0")) + ""); // 本期应提利息收入I1
                        itfArInterface.setServiceChargeI1(ArithUtil.sub(Double.valueOf(itfImpData.getValue35() != null ? itfImpData.getValue35() : "0"), Double.valueOf("0")) + ""); // 本期应提利息收入I1
                    } else {
                        String a1 = itfArInterfaces.get(itfArInterfaces.size() - 1).getRentalIncomeI2();
                        String a2 = itfArInterfaces.get(itfArInterfaces.size() - 1).getReceInterestI2();
                        String a3 = itfArInterfaces.get(itfArInterfaces.size() - 1).getServiceChargeI2();
                        itfArInterface.setRentalIncomeI1(ArithUtil.sub(Double.valueOf(itfImpData.getValue25() != null && !"".equals(itfImpData.getValue25()) ? itfImpData.getValue25() : "0"), Double.valueOf(a1 != null && !"".equals(a1) ? a1 : "0")) + ""); // 经租本期应提租金收入I1
                        itfArInterface.setReceInterestI1(ArithUtil.sub(Double.valueOf(itfImpData.getValue26() != null && !"".equals(itfImpData.getValue26()) ? itfImpData.getValue26() : "0"), Double.valueOf(a2 != null && !"".equals(a2) ? a2 : "0")) + ""); // 本期应提利息收入I1
                        itfArInterface.setServiceChargeI1(ArithUtil.sub(Double.valueOf(itfImpData.getValue35() != null && !"".equals(itfImpData.getValue35()) ? itfImpData.getValue35() : "0"), Double.valueOf(a3 != null && !"".equals(a3) ? a3 : "0")) + ""); // 本期应提利息收入I1
                    }
                    break;
                }
            }

            // =================本期应提贴息&& 本期应提贴手续费==================
            int series = Integer.valueOf(itfImpData.getValue21());// 当前期号
            if (itfImpList.size() == (series + 1)) { // 是最后一期

                ItfImpLines preLine = itfImpList.get(series-1); // 先找上一期
                ItfImpLines curLine = itfImpList.get(series); // 先找当前期

                String dateStr1 = preLine.getValue24();
                Calendar calendar = Calendar.getInstance();
                int day1 = 0;
                int maxDay = 0;
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(itfImpData.getValue24().replace("/", "-"))); // 本期还款日期
                    day1 = calendar.get(Calendar.DAY_OF_MONTH);
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr1.replace("/", "-"))); // 上一计划还款日
                    maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    logger.error("时间格式转换错误!");
                    e.printStackTrace();
                }
                int spanDay = maxDay - day1 +1;
                String value32 = (curLine.getValue32() == null || "".equals(curLine.getValue32()))? "0.0": curLine.getValue32();
                String value33 = (curLine.getValue33() == null || "".equals(curLine.getValue33()))? "0.0": curLine.getValue33();
                String value34 = (curLine.getValue34() == null || "".equals(curLine.getValue34()))? "0.0": curLine.getValue34();
                itfArInterface.setShallCarryInterest(ArithUtil.sub(Double.valueOf(value33), ArithUtil.div(ArithUtil.mul(Double.valueOf(value33), spanDay), maxDay)) + ""); // 本期应提贴息
                itfArInterface.setPostFee(ArithUtil.sub(Double.valueOf(value34), ArithUtil.div(ArithUtil.mul(Double.valueOf(value34), spanDay), maxDay)) + ""); // 本期应提贴手续费
                itfArInterface.setPrepaidRentPerPeriod(ArithUtil.sub(Double.valueOf(value32), ArithUtil.div(ArithUtil.mul(Double.valueOf(value32), spanDay), maxDay)) + ""); // 预付租金每期摊销金额

            } else {
                String _scheduledRepaymentDate = itfArInterfaces.get(series - 1).getScheduledRepaymentDate(); // 上一计划还款日
                // 获取当期天数
                Calendar calendar1 = Calendar.getInstance();
                int day1 = 0;
                int maxDay = 0;
                try {
                    calendar1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(itfImpData.getValue24().replace("/", "-"))); // 本期还款日期
                    day1 = calendar1.get(Calendar.DAY_OF_MONTH);
                    calendar1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(_scheduledRepaymentDate.replace("/", "-"))); // 上一计划还款日
                    maxDay = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    logger.error("时间格式转换错误!");
                    e.printStackTrace();
                }
                double interest1 = ArithUtil.mul(ArithUtil.div(Double.valueOf(itfImpData.getValue33() != null && !"".equals(itfImpData.getValue33()) ? itfImpData.getValue33() : "0"), maxDay), (day1 - 1));
                double pos1 = ArithUtil.mul(ArithUtil.div(Double.valueOf(itfImpData.getValue34() != null && !"".equals(itfImpData.getValue34()) ? itfImpData.getValue34() : "0"), maxDay), (day1 - 1));
                double pre1 = ArithUtil.mul(ArithUtil.div(Double.valueOf(itfImpData.getValue32() != null && !"".equals(itfImpData.getValue32()) ? itfImpData.getValue32() : "0"), maxDay), (day1 - 1));

                // 计划还款日期到月末应收贴息金额=第(N+1)期应收贴息value33/本月天数*（月末最后一天-计划还款日+1）
                // 第N期应收贴息=（月初-计划还款日期前1天的应收贴息金额)+(计划还款日期-月末应收贴息金额)
                double interest2 = this.getLine2Value(itfImpList, "value33", itfArInterface.getScheduledRepaymentDate(), itfImpData);
                double pos2 = this.getLine2Value(itfImpList, "value34", itfArInterface.getScheduledRepaymentDate(), itfImpData);
                double pre2 = this.getLine2Value(itfImpList, "value32", itfArInterface.getScheduledRepaymentDate(), itfImpData);
                itfArInterface.setShallCarryInterest(ArithUtil.add(interest1, interest2) + ""); // 本期应提贴息
                itfArInterface.setPostFee(ArithUtil.add(pos1, pos2) + ""); // 本期应提贴手续费
                itfArInterface.setPrepaidRentPerPeriod(ArithUtil.add(pre1, pre2) + ""); // 预付租金每期摊销金额
            }

            // ==============本期应提贴息 END==================
            if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(itfArInterface.getLeaseType())) { // 还款状态（生效、结束、逾期）
                if (itfImpData.getValue21().equals("1")) {
                    itfArInterface.setPaymentStatus(ItfArInterfaceServiceImpl.REPAY_STATUS_CLOSED);
                }
            }
            //  正租 或者是回租 从第一期开始
            if (!ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(itfArInterface.getLeaseType()) || (!itfImpData.getValue21().equals("1") && ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(itfArInterface.getLeaseType()))) {
                itfArInterface.setPaymentStatus(ItfArInterfaceServiceImpl.REPAY_STATUS_ACTIVE);
            }

            // --------------逾期天数 --------
            if (ItfArInterfaceServiceImpl.REPAY_STATUS_OVER_DATE.equals(itfArInterface.getPaymentStatus())) {
                Calendar calendar = Calendar.getInstance();
                long millis1 = calendar.getTimeInMillis();
                calendar.setTime(new Date()); // 当期时间
                long millis2 = calendar.getTimeInMillis();
                String day = Integer.parseInt(String.valueOf((millis1 - millis2) / 1000 / 24 / 60)) + 1 + "";
                itfArInterface.setOverdueDays(day);// 逾期天数
            } else {
                itfArInterface.setOverdueDays("0");// 逾期天数
            }
            // --------------逾期天数 END--------
        }

        //***********************为了补全的前面期数的 添加还款状态逻辑**********************
        if ("0".equals(itfImpData.getValue25())) {
            itfArInterface.setPaymentStatus(ItfArInterfaceServiceImpl.REPAY_STATUS_CLOSED);
        }
        //***********************为了补全的前面期数的 添加还款状态逻辑  END**********************

        String scheduleStr = itfArInterface.getScheduledRepaymentDate().replace("/", "-");
        // ============== 经租本期应提租金收入I2 && 本期应提利息收入I2 && 本期应提手续费I2=========

        itfArInterface.setRentalIncomeI2(this.getLine2Value(itfImpList, "value25", scheduleStr, itfImpData) + "");
        itfArInterface.setReceInterestI2(this.getLine2Value(itfImpList, "value26", scheduleStr, itfImpData) + "");
        itfArInterface.setServiceChargeI2(this.getLine2Value(itfImpList, "value35", scheduleStr, itfImpData) + "");
        // ============== 经租本期应提租金收入I2 && 本期应提利息收入I2 && 本期应提手续费I2 END ========

        itfArInterface.setIncomePeriod(scheduleStr.substring(0, scheduleStr.lastIndexOf("-"))); // 收入归属期间
        itfArInterface.setAccountingDate(this.getLastDayTheMonth(itfArInterface.getIncomePeriod()));

        // 逾期第90天日期
        if (ItfArInterfaceServiceImpl.REPAY_STATUS_OVER_DATE.equals(itfArInterface.getPaymentStatus())) {

            Date date = this.stringToDate(itfArInterface.getScheduledRepaymentDate(), ItfArInterfaceServiceImpl.DAY_FORMAT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 90);
            itfArInterface.setOverdue90Date(this.calendarToStr(calendar.getTime(), ItfArInterfaceServiceImpl.DAY_FORMAT));
        }
        // 逾期第90天日期 end

        //本期收入税金
        String leaseType = itfArInterface.getLeaseType();

        double receInterestI1 = Double.valueOf(itfArInterface.getReceInterestI1() != null ? itfArInterface.getReceInterestI1() : "0");
        double receInterestI2 = Double.valueOf(itfArInterface.getReceInterestI2() != null ? itfArInterface.getReceInterestI2() : "0");
        double prepaidRentPerPeriod = Double.valueOf(itfArInterface.getPrepaidRentPerPeriod() != null && !"".equals(itfArInterface.getPrepaidRentPerPeriod()) ? itfArInterface.getPrepaidRentPerPeriod() : "0");
        double shallCarryInterest = Double.valueOf(itfArInterface.getShallCarryInterest() != null ? itfArInterface.getShallCarryInterest() : "0");
        double postFree = Double.valueOf(itfArInterface.getPostFee() != null ? itfArInterface.getPostFee() : "0");
        double charge1 = Double.valueOf(itfArInterface.getServiceChargeI1() != null ? itfArInterface.getServiceChargeI1() : "0");
        double charge2 = Double.valueOf(itfArInterface.getServiceChargeI2() != null ? itfArInterface.getServiceChargeI2() : "0");
        double interest = ArithUtil.add(receInterestI1, receInterestI2);
        double total = ArithUtil.add(ArithUtil.add(ArithUtil.add(ArithUtil.add(charge1, charge2), shallCarryInterest), interest), postFree);

        if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(leaseType)) {
            double incomTax = ArithUtil.mul(ArithUtil.div(ArithUtil.add(interest, prepaidRentPerPeriod), 1.17), 0.17);
            itfArInterface.setIncomeTax(incomTax + "");
        } else if (ItfArInterfaceServiceImpl.NORMAL_TYPE.equals(leaseType)) {
            double incomTax = ArithUtil.mul(ArithUtil.div(total, 1.17), 0.17);
            itfArInterface.setIncomeTax(incomTax + "");

        } else if (ItfArInterfaceServiceImpl.BACK_TYPE.equals(leaseType)) {
            double incomTax = ArithUtil.mul(ArithUtil.div(total, 1.06), 0.06);
            itfArInterface.setIncomeTax(incomTax + "");
        }
        //本期收入税金 end

        //本期应提收入 ====
        if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(itfArInterface.getLeaseType())) {
            itfArInterface.setShouldIncome(ArithUtil.add(ArithUtil.add(Double.valueOf(itfArInterface.getRentalIncomeI1() != null ? itfArInterface.getRentalIncomeI1() : "0"), Double.valueOf(itfArInterface.getRentalIncomeI2() != null ? itfArInterface.getRentalIncomeI2() : "0")), Double.valueOf(itfArInterface.getPrepaidRentPerPeriod() != null ? itfArInterface.getPrepaidRentPerPeriod() : "0")) + "");
        } else if (ItfArInterfaceServiceImpl.NORMAL_TYPE.equals(itfArInterface.getLeaseType()) || ItfArInterfaceServiceImpl.BACK_TYPE.equals(itfArInterface.getLeaseType())) {
            itfArInterface.setShouldIncome(ArithUtil.add(ArithUtil.add(ArithUtil.add(ArithUtil.add(ArithUtil.add(Double.valueOf(itfArInterface.getReceInterestI1() != null ? itfArInterface.getReceInterestI1() : "0"), Double.valueOf(itfArInterface.getReceInterestI2() != null ? itfArInterface.getReceInterestI2() : "0")), Double.valueOf(itfArInterface.getShallCarryInterest() != null ? itfArInterface.getShallCarryInterest() : "0")), Double.valueOf(itfArInterface.getPostFee() != null ? itfArInterface.getPostFee() : "0")), Double.valueOf(itfArInterface.getServiceChargeI1() != null ? itfArInterface.getServiceChargeI1() : "0")), Double.valueOf(itfArInterface.getServiceChargeI2() != null ? itfArInterface.getServiceChargeI2() : "0")) + "");
        }
        //本期应提收入 == end

        // 是否资源车
//        QueryUtilDto queryUtilDto2 = queryUtilMapper.getAttrByTwoInterface(itfArInterface.getApplyNum());
        itfArInterface.setSourceVehicle(queryUtilDto3.getAttribute1());
        // 是否资源车 end
        // 申请状态
        itfArInterface.setApplyStatus(queryUtilDto3.getAttribute2());
        //申请状态 end

        //发票主体  发票主体
        PubInvoiceEntity pe = new PubInvoiceEntity();
        pe.setInvoiceEntityName(queryUtilDto3.getAttribute3());
        PubInvoiceEntity pe2 = pubInvoiceEntityMapper.selectOne(pe);
        if (pe2 != null) {
            itfArInterface.setInvoiceEntity(pe2.getInvoiceEntityValue());
            itfArInterface.setInvoiceComValue(pe2.getInvoiceEntityValue());
        }

//        String invoiceEntityStr = itfArInterface.getInvoiceEntity();
//        if (invoiceEntityStr != null) {
//            PubSoaSegmentValue soaSegmentValue = new PubSoaSegmentValue();
//            soaSegmentValue.setDescription(invoiceEntityStr);
//            soaSegmentValue.setTypeCode("COM");
//            PubSoaSegmentValue soaSegmentValue1 =  pubSoaSegmentValueMapper.selectOne(soaSegmentValue);
//            if (soaSegmentValue1 != null) {
//                itfArInterface.setInvoiceComValue(soaSegmentValue1.getSegmentValue());
//            }
//        }

        //发票主体 end

        // 记账主体
        String sourceType = itfArInterface.getSourceVehicle();
        String leaseType1 = itfArInterface.getLeaseType();

        if (ItfArInterfaceServiceImpl.NORMAL_TYPE.equals(leaseType1) || ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(leaseType1)) {
//            if ("是".equals(sourceType)) {
//                itfArInterface.setAccEntity(itfArInterface.getInvoiceComValue());
//            } else if ("否".equals(sourceType)) {
            itfArInterface.setAccEntity(itfArInterface.getInvoiceComValue());
//                if (ItfArInterfaceServiceImpl.RUNNING_TYPE.equals(leaseType1)) {
//                    itfArInterface.setAccEntity(itfArInterface.getInvoiceComValue());
//                } else {
//                    itfArInterface.setAccEntity(itfArInterface.getInvoiceComValue());
//                }
//            }
        } else if (ItfArInterfaceServiceImpl.BACK_TYPE.equals(leaseType1)) {
            itfArInterface.setAccEntity(itfArInterface.getBelongComValue());
        }
        // 记账主体 end
        return itfArInterface;
    }

    /**
     * 获取距指定相差的月份的时间字符串 year-month-day
     * 负数代表前几个月时间
     * 整数代表后几个月
     *
     * @param num
     * @param dataStr
     * @return
     */
    private String getSpanMonthDateStr(String dataStr, int num) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(this.stringToDate(dataStr, ItfArInterfaceServiceImpl.DAY_FORMAT));

        calendar.add(Calendar.MONTH, num);

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(ItfArInterfaceServiceImpl.DAY_FORMAT);


        return sdf.format(date);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    private int daysBetween(String smdate, String bdate) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.stringToDate(smdate, ItfArInterfaceServiceImpl.DAY_FORMAT));
        int day1 = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(this.stringToDate(bdate, ItfArInterfaceServiceImpl.DAY_FORMAT));
        int day2 = cal.get(Calendar.DAY_OF_MONTH);
        return day2 - day1;
    }

    private String calendarToStr(Date date, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(date);
    }

    private Date stringToDate(String dataStr, String farmat) {

        SimpleDateFormat sdf = new SimpleDateFormat(farmat);
        Date date = null;
        try {
            dataStr = dataStr.replace("/", "-");
            date = sdf.parse(dataStr);
        } catch (ParseException e) {
            logger.error("时间格式转换错误!");
            e.printStackTrace();
        }

        return date;
    }

    /**
     * num /本月天数*（月末最后一天-计划还款日期+1）的value
     *
     * @param num
     * @param itfArInterface
     * @return
     */
    private double getNumDivi(double num, ItfArInterface itfArInterface, ItfImpLines itfImpData) {

        Calendar calendar = Calendar.getInstance();
        String _month = itfArInterface.getScheduledRepaymentDate(); // 计划还款日
        calendar.setTime(this.stringToDate(_month, ItfArInterfaceServiceImpl.DAY_FORMAT));
        int day = calendar.get(Calendar.DATE);
        int _activeDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 本月天数

        return ArithUtil.mul(ArithUtil.div(num, Double.valueOf(_activeDay)), Double.valueOf(_activeDay + 1 - day));
    }


    // 行1计算逻辑
    private Double getLine2Value(List<ItfImpLines> itfImpList, String valueNum, String scheduledRepaymentDate, ItfImpLines itfImpData) {

        double value = 0.0;
        int maxDay = 0;
        int _tempDay = 0;
        int _seriesNum = Integer.parseInt(itfImpData.getValue21());
        if (itfImpList.size() != Integer.valueOf(_seriesNum + 1)) { // 判断当前是不是最后一期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.stringToDate(scheduledRepaymentDate, ItfArInterfaceServiceImpl.DAY_FORMAT));
            maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, maxDay);
            _tempDay = this.daysBetween(scheduledRepaymentDate, this.calendarToStr(calendar.getTime(), ItfArInterfaceServiceImpl.DAY_FORMAT)) + 1;  // 月末最后一天-计划还款日期 +1
            value = ArithUtil.mul(Double.valueOf(maxDay), Double.valueOf(_tempDay));
        } else { //  如果是最后一期直接赋值0
            return value;
        }

        // 经租本期应提租金收入I2 && 本期应提利息收入I2 && 本期应提手续费I2

        ItfImpLines impLines = itfImpList.get(_seriesNum + 1);
        double d1 = 0;
        if ("value25".equals(valueNum)) {
            d1 = Double.valueOf(impLines.getValue25() != null && !"".equals(impLines.getValue25()) ? impLines.getValue25() : "0"); // 第N+1期客户租金value25
        } else if ("value26".equals(valueNum)) {
            d1 = Double.valueOf(impLines.getValue26() != null && !"".equals(impLines.getValue26()) ? impLines.getValue26() : "0"); // 第N+1期客户租金value26
        } else if ("value35".equals(valueNum)) {
            d1 = Double.valueOf(impLines.getValue35() != null && !"".equals(impLines.getValue35()) ? impLines.getValue35() : "0"); // 第N+1期应收手续费value35
        } else if ("value34".equals(valueNum)) {
            d1 = Double.valueOf(impLines.getValue34() != null && !"".equals(impLines.getValue34()) ? impLines.getValue34() : "0");
        } else if ("value33".equals(valueNum)) {
            d1 = Double.valueOf(impLines.getValue33() != null && !"".equals(impLines.getValue33()) ? impLines.getValue33() : "0");
        } else if ("value32".equals(valueNum)) {
            d1 = Double.valueOf(impLines.getValue32() != null && !"".equals(impLines.getValue32()) ? impLines.getValue32() : "0");
        }
        return ArithUtil.mul(ArithUtil.div(d1, maxDay), _tempDay);
    }

    // 获取逾期起始日期
    private List<ItfArInterface> setOverStartDate(List<ItfArInterface> itfArInterfaces) {

        String _date = "";
        for (int i = 0; i < itfArInterfaces.size(); i++) {
            ItfArInterface itfArInterface = itfArInterfaces.get(i);
            String _payStatus = itfArInterface.getPaymentStatus();
            if (ItfArInterfaceServiceImpl.REPAY_STATUS_OVER_DATE.equals(_payStatus)) {
                String scheduleRepayDate = itfArInterface.getScheduledRepaymentDate();
                itfArInterface.setOverdueStartDate(scheduleRepayDate);
                break;
            }
        }
        return itfArInterfaces;
    }

    private Date getLastDayTheMonth(String dateMonth) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            calendar.setTime(sdf.parse(dateMonth));
            calendar.set(Calendar.DATE, calendar.getActualMaximum(calendar.DATE));

        } catch (ParseException e) {
            logger.error("时间格式转换错误!");
            e.printStackTrace();
            return null;
        }

        return calendar.getTime();
    }














    //=================================================================================================














    @Override
    public void updatePer(List<QueryUtilDto> queryUtilDtos) {

        List<ItfArInterface> itfArInterfaces = new ArrayList<ItfArInterface>(); // 最后更新
        List<BatchDto> batchDtos = new ArrayList<BatchDto>();
        List<ItfImpLines> itfImpLines = itfImplLineMapper.queryUpdatePer(queryUtilDtos);

        Map allMap = this.sortGroupByApplNum(itfImpLines); // 存放所有的申请编号对应的期数(包括重复的情况)

        // 合同分期序号去重----------------------
        itfImpLines = this.distinctListBySeries(itfImpLines);
        // 合同分期序号去重----------end---------
        Map groupDataMap = this.sortGroupByApplNum(itfImpLines);

        List<QueryUtilDto> applyDataList = new ArrayList<>();
        List<QueryUtilDto> servicefunctionDataList = new ArrayList<>();

        List<QueryUtilDto> applyAllList = new ArrayList<>();
        Set<String> keys = groupDataMap.keySet();// 得到全部的key
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            QueryUtilDto qq1 = new QueryUtilDto();
            qq1.setAttribute1(iter.next());
            applyAllList.add(qq1);
        }

        if (itfImpLines.size() > 0) {
            applyDataList = queryUtilMapper.getAttrByTwoInterfaceByList(applyAllList);
            servicefunctionDataList = queryUtilMapper.getSeriveFunctionList(applyAllList);
        }

        double sumPost = 0.0;
        double sumInterest = 0.0;
        double sumPrepaid = 0.0;

        // 按照申请编号计算
        Iterator iterator = groupDataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            List<ItfImpLines> itfImpList = (List<ItfImpLines>) entry.getValue(); // 获取到每个合同下的分期明细
            // =====添加没有的其他期的数据***********************************====
            ItfImpLines impLines = itfImpList.get(0); // 获取第一条数据获取判断期数
            String _interfaceName = impLines.getInterfaceName();
            // 获取分组后的有序的合同 如果该合同最后一行的期数不能 该合同数量 则舍弃这个合同

            ItfImpLines impLinesEnd = itfImpList.get(itfImpList.size() - 1);
            // 最后一期的期号是多少
            String lastSeries = impLinesEnd.getValue21();
            String _seriesNum1 = impLinesEnd.getValue12().split("期")[0];

            // 初始化导入数据
            if (!(("ALIX_AR_INTERFACE".equals(_interfaceName) || "OPL_AR_INTERFACE".equals(_interfaceName)) && lastSeries.equals(_seriesNum1))) { // 如果不相等 则表示该合同不完整 跳过当前合同
                continue;
            }


            String series = impLines.getValue21();
            if (!"0".equals(series)) { // 如果缺少前面期数
                Calendar calendar = Calendar.getInstance();
                for (int i = 0; i < Integer.valueOf(series); i++) {
                    ItfImpLines impLinesAdditon = new ItfImpLines();
                    impLinesAdditon.setSourceIterfaceId(impLines.getSourceIterfaceId()); // 来源系统代码
                    impLinesAdditon.setInterfaceName(impLines.getInterfaceName()); // 接口名称
                    impLinesAdditon.setValue3(impLines.getValue3()); // *合同号
                    impLinesAdditon.setValue4(impLines.getValue4()); // *合同状态
                    impLinesAdditon.setValue5(impLines.getValue5()); // *合同生效日期
                    impLinesAdditon.setValue6(impLines.getValue6()); // *租赁公司
                    impLinesAdditon.setValue7(impLines.getValue7()); // *分公司
                    impLinesAdditon.setValue8(impLines.getValue8()); // *客户名称
                    impLinesAdditon.setValue9(impLines.getValue9()); // *租赁属性
                    impLinesAdditon.setValue10(impLines.getValue10()); // *业务类型
                    impLinesAdditon.setValue11(impLines.getValue11()); // *产品方案名称
                    impLinesAdditon.setValue12(impLines.getValue12()); // *融资期限
                    impLinesAdditon.setValue17("0"); // *融资额
                    impLinesAdditon.setValue18("0"); // *应收本金（含税）
                    impLinesAdditon.setValue19("0"); // 应收利息（含税）
                    impLinesAdditon.setValue20("0"); // 应收手续费（含税）
                    impLinesAdditon.setValue21("0"); // 应收贴息（含税）
                    impLinesAdditon.setValue22("0"); // 应收贴手续费（含税）
                    impLinesAdditon.setValue23("0"); // 本金税金
                    impLinesAdditon.setValue24("0"); // 收入税金
                    impLinesAdditon.setValue26(""); // 起始日期
                    impLinesAdditon.setValue27(""); // 终止日期

                    calendar.setTime(this.stringToDate(impLines.getValue24(), ItfArInterfaceServiceImpl.DAY_FORMAT));
                    impLinesAdditon.setValue21(i + ""); // *分期序号
                    impLinesAdditon.setValue2(impLines.getValue2()); // *申请编号
                    impLinesAdditon.setValue1(impLines.getValue2() + "-" + impLinesAdditon.getValue21()); // *唯一标识 = 申请编号+ "-" + 期数
                    calendar.add(calendar.MONTH, -(Integer.valueOf(series) - i));
                    impLinesAdditon.setValue24(this.calendarToStr(calendar.getTime(), ItfArInterfaceServiceImpl.DAY_FORMAT)); // *计划还款日期
                    impLinesAdditon.setValue25("0"); // *每期客户租金
                    impLinesAdditon.setValue26("0"); // *每期月供利息
                    impLinesAdditon.setValue27("0"); // *每期月供本金
                    impLinesAdditon.setValue28("0"); // *每期本金税金
                    impLinesAdditon.setValue29("0"); // *每期收入税金
                    impLinesAdditon.setValue30("0"); // *本金余额
                    impLinesAdditon.setValue31("0"); // *预付租金金额
                    impLinesAdditon.setValue32("0"); // *预付租金每期摊销金额
                    impLinesAdditon.setValue33("0"); // *每期应收贴息
                    impLinesAdditon.setValue34("0"); // *每期应收贴手续费
                    impLinesAdditon.setValue35("0"); // *每期应收手续费
                    itfImpList.add(i, impLinesAdditon);
                }
            }
            // =====添加没有的其他期的数据 end***********************************====

            // 每个合同下 手续费收取方式
            //====================优化部分===================
//            QueryUtilDto queryDto = queryUtilMapper.getSeriveFunction(itfImpList.get(0).getValue2());
            QueryUtilDto queryDto = this.getServiceFunction(servicefunctionDataList, itfImpList.get(0).getValue2());
            //====================优化部分===================
            String serviceChargeFunction = "一次性";
            if (queryDto != null) {
                serviceChargeFunction = queryDto.getAttribute6();
            }
            PubAlixSubcom pubAlixSubcom1 = null;
            for (int i = 0; i < itfImpList.size(); i++) {    // 遍历所有的分期明细 完成每个申请号
                ItfImpLines impImp = itfImpList.get(i);  // 遍历合同编号对应的行 对每个字段进行设置
                if (i == 0) { // 只进行一次查询
                    PubAlixSubcom pubAlixSubcom = new PubAlixSubcom();
                    pubAlixSubcom.setBelongcomName(impImp.getValue6());
                    pubAlixSubcom.setSubcomName(impImp.getValue7());
                    pubAlixSubcom1 = pubAlixSubcomMapper.selectOne(pubAlixSubcom);
                }
                ItfArInterface calcItfImpInter = null;
                if (ItfArInterfaceServiceImpl.SERIERS_ONE.equals(impImp.getValue21())) { // 判断当前是不不是第0期
                    calcItfImpInter = this.getCalcItfArInterface(itfImpList, impImp, true, serviceChargeFunction, itfArInterfaces, sumPost, sumInterest, sumPrepaid, pubAlixSubcom1, applyDataList);
                } else {
                    calcItfImpInter = this.getCalcItfArInterface(itfImpList, impImp, false, serviceChargeFunction, itfArInterfaces, sumPost, sumInterest, sumPrepaid, pubAlixSubcom1, applyDataList);
                }
                itfArInterfaces.add(calcItfImpInter);
            }
            itfArInterfaces = this.setOverStartDate(itfArInterfaces);
        }

        itfArInterfaceMapper.updatePreValue(itfArInterfaces);


    }


















}