package hscs.ae.service.impl;

import com.hand.hap.core.IRequest;
import hscs.ae.dto.*;
import hscs.ae.mapper.*;
import hscs.ae.service.IAeAccountLinesService;
import hscs.ae.service.IAeTfrSumAccountsService;
import hscs.ae.service.impl.rollback.SourceTableUpdate;
import hscs.ae.service.impl.rollback.TfrTableRollBack;
import hscs.utils.controllers.Arith;
import jodd.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AeRollBackOnEventApply {
    @Autowired
    TfrTableRollBack tfrTableRollBack;
    @Autowired
    SourceTableUpdate sourceTableUpdate;
    @Autowired
    private AeTfrBatchEventsBakMapper atbebMapper;
    @Autowired
    private AeTfrDtlAccountsMapper atdaMapper;
    @Autowired
    private AeEventBatchesMapper aebMapper;
    @Autowired
    private IAeAccountLinesService aalService;
    @Autowired
    private IAeTfrSumAccountsService atsaService;
    @Autowired
    private AeTfrSumAccountsMapper atsaMapper;

    private AeEventBatches getAeb(Long eventBatchId,Map<Long,AeEventBatches> map){
        if(map.containsKey(eventBatchId)){
            return map.get(eventBatchId);
        }
        AeEventBatches aeb =  aebMapper.getAebById(eventBatchId) ;
        map.put(eventBatchId,aeb);
        return aeb;
    }
    public  List<AeAccountLines> getAaList(Long accountHeaderId , Map<Long,List<AeAccountLines>> aalsMap){
        List<AeAccountLines> aals = null;
        if(aalsMap.containsKey(accountHeaderId)){
            aals = aalsMap.get(accountHeaderId) ;
        }else{
            aals = aalService.getSumFieldByHeaderId(accountHeaderId);
            aalsMap.put(accountHeaderId,aals);
        }
        return aals;
    }

    @Transactional(rollbackFor = Exception.class,timeout = 500)
    public void reRunRollBack(IRequest r, List<AeTfrBatchEvents> atbeList){
        long time = System.currentTimeMillis();
        // tfr eventbatchid
        StringBuilder sb = new StringBuilder();
        Map<Long,AeEventBatches> aebMap = new HashMap(8);
        for(AeTfrBatchEvents atbe : atbeList){
            sb.append(atbe.getTfrEventBatchId().toString() + ",");
            AeEventBatches aeb =  getAeb(atbe.getEventBatchId(),aebMap);
            if("N".equals(atbe.getReversalFlag()) ) {
                sourceTableUpdate.updateSourceRev(aeb,atbe.getPrimaryKeyId(),"NEW");
            }else{
                //将原始单据的“冲销入账状态”改为“入账成功”，清空账务消息字段
                sourceTableUpdate.updateSourceRev(aeb,atbe.getPrimaryKeyId(),"ACCOUNTED");
            }
        }
        // 根据勾选事件id 查找汇总 和明细信息 , 按照Y N 顺序排序查出来的
        List<AeTfrDtlAccounts> atbeList2 = atdaMapper.getSumByBatchEvent(sb.substring(0,sb.length()-1));
        if(atbeList2.isEmpty()){
            // throw new RuntimeException("根据勾选数据未找到汇总数据，数据出错！");
            //
        }
        // 汇总id
        Set<Long> tfrSumIdSet = new HashSet();
        List<AeTfrSumAccounts> atsaUpdateList = new ArrayList<>();
        Map<Long,List<AeAccountLines>> aalsMap = new HashMap<>(8);

        Set<Long> uniSumIdSet = new HashSet(1024);
        for(AeTfrDtlAccounts atda : atbeList2){
            uniSumIdSet.add(atda.getTfrSumAccountId()) ;
        }
        String strId = Arrays.toString(uniSumIdSet.toArray());
        String allSumIds = strId.substring(1,strId.length()-1);
        // 一次性查询所有汇总 ， 遍历汇总做。
        if (StringUtils.isNotEmpty(allSumIds)) {
            List<AeTfrSumAccounts> atsaList = atsaMapper.getBySumIds(allSumIds);
            for(AeTfrSumAccounts atsa : atsaList){
                if(atsa.getLineCounts().equals(1L)){
                    tfrSumIdSet.add(atsa.getTfrSumAccountId());
                }else{
                    List<AeAccountLines> aals = getAaList(atsa.getAccountHeaderId(),aalsMap);
                    Map<String,Double> subMap = new HashMap();
                    // 行数相等，直接删除
                    if(atsa.getLineCounts().equals(atbeList2.parallelStream().filter((atda)->atda.getTfrSumAccountId().equals(atsa.getTfrSumAccountId())).count())) {
                        tfrSumIdSet.add(atsa.getTfrSumAccountId());
                    }else{
                        Long lineCounts = 0L ;
                        for(AeTfrDtlAccounts atda : atbeList2){
                            if(atda.getTfrSumAccountId().equals(atsa.getTfrSumAccountId())){
                                lineCounts ++ ;
                                for (AeAccountLines aal : aals) {
                                    if ("ACCUMULATE".equals(aal.getSummaryMethod())) {
                                        String colName = aal.getColumnName().toLowerCase();
                                        double subVal;
                                        if(ReflectUitls.getValue(atda, colName)==null ){
                                            subVal = 0D;
                                        }else{
                                            subVal = Double.parseDouble(String.valueOf(Optional.ofNullable(ReflectUitls.getValue(atda, colName)).orElse(0) ));
                                        }

                                        if(subMap.containsKey(colName)){
                                            subMap.put(colName,Arith.add(subMap.get(colName),subVal) );
                                        }else{
                                            subMap.put(colName,subVal);
                                        }
                                    }
                                }
                            }
                        }
                        for (Map.Entry<String, Double> entry : subMap.entrySet()) {
                            Double oriVal = Double.parseDouble(String.valueOf(Optional.ofNullable(ReflectUitls.getValue(atsa, entry.getKey())).orElse(0) ) );
                            double val = Arith.sub(oriVal, entry.getValue());
                            ReflectUitls.setValue(atsa, entry.getKey(), String.valueOf(val));
                        }
                        atsa.setAccountingDate(null);
                        atsa.setLineCounts(atsa.getLineCounts() - lineCounts);
                        // atsaService.updateByPrimaryKeySelective(r, atsa);
                        atsaUpdateList.add(atsa);
                    }
                }
            }
        }
        // 批量删除备份汇总
        deleteSumAccAndBak(tfrSumIdSet);
        if(!atsaUpdateList.isEmpty()){
            atsaMapper.updateListSelective(atsaUpdateList);
        }
        tfrTableRollBack.delete3Table(sb.substring(0,sb.length()-1)) ;
        //
        System.out.println(atbeList2.size() + "条" + ( System.currentTimeMillis() - time )+ "ms");
    }


    @Autowired
    private AeTfrSumAccountsBakMapper atsabMapper;
    public int deleteSumAccAndBak(Set idSet){
        if(idSet.isEmpty()){
            return 0;
        }
        String ids = Arrays.toString(idSet.toArray()).substring(1,Arrays.toString(idSet.toArray()).length()-1);

        String deleteSql = " delete from hscs_ae_tfr_sum_accounts where TFR_SUM_ACCOUNT_ID in( " + ids + ")" ;
        List<AeTfrSumAccountsBak> atsabList = atsabMapper.getSumByids(ids);
        UpdateSource.inserDiv(atsabList,atsabMapper::insertList);
        return atbebMapper.deleteSelect(deleteSql);
    }


    public boolean preValidate(List<AeTfrBatchEvents> atbeList , boolean run2Vali){
        //  tfr 放入hashset
        HashSet<AeTfrBatchEvents> hsAe = new HashSet();
        HashSet<String> hsLong = new HashSet();
        for(AeTfrBatchEvents atbe : atbeList){
            hsAe.add(atbe);
            // 放入勾选的id
            hsLong.add(atbe.getTfrEventBatchId()+"") ;
        }
        // 检查已勾选数据是否为正向事件，且存在冲销事件，并且冲销事件不在此次勾选的数据范围内，若是，不允许回滚，报错
        StringBuffer ids = new StringBuffer();
        for(AeTfrBatchEvents atbe : hsAe){
            // N 的为正向数据 , 是否冲销有了
            if("N".equals(atbe.getReversalFlag()) && !StringUtil.isEmpty(atbe.getReversalObjects()) ){
                if(!hsLong.contains( atbe.getReversalObjects() )){
                    throw new RuntimeException("该笔单据："+atbe.getReference() + " 没有勾选冲销事件,不允许回滚！" );
                }
            }
            String id = atbe.getTfrEventBatchId().toString();
            ids.append(id + ",");
        }
        if(run2Vali){
            validateExistS(ids.substring(0,ids.length()-1));
        }
        return true;
    }
    protected  boolean validateExistS(String ids){
         /*
            select * from hscs_ae_tfr_dtl_accounts i ,  hscs_ae_tfr_sum_accounts j
                where i.TFR_SUM_ACCOUNT_ID = j.TFR_SUM_ACCOUNT_ID
                and j.DELIVERY_STATUS = 'S'
                and i.TFR_EVENT_BATCH_ID in ()
                选数据所有的汇总账务是否存在传送状态为S的数据，若存在，则不允许回滚，并报错：
             */
        List<AeTfrDtlAccounts> list = atdaMapper.isSumDeliverS(ids);
        if(list.isEmpty()){
            return true;
        }else{
            // 单据XX，YY存在汇总账务已推送，不允许回滚！
            Set<String> s = new HashSet<>();
            for(AeTfrDtlAccounts a : list){
                s.add(a.getReference());
            }
            throw new RuntimeException("单据 " + String.join(",",s) +"存在汇总账务已推送，不允许回滚！");
        }

    }
}
