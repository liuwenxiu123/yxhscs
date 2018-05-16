package hscsm.core.api.service.lmpl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hscsm.core.api.dto.WsAnalysis;
import hscsm.core.api.dto.WsResponse;
import hscsm.core.api.mapper.WsAnalysisMapper;
import hscsm.core.api.service.IItfImpDataService;
import hscsm.core.api.utils.FomatUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xieshuai on 2018/2/28.
 */
@Service
//@Transactional
@Transactional(rollbackFor={Exception.class})
public class ItfImpDataServiceImpl extends BaseServiceImpl<WsAnalysis> implements IItfImpDataService {
    @Autowired
    private WsAnalysisMapper wsTableDemoMapper;

    @Override
    public List<WsResponse> dealImpInterfaceMap(List<Map<String, Object>> impItfMapList) throws Exception {
        FomatUtils formatUtils = new FomatUtils();//格式校验类
        StringBuffer sbErrorMsg = new StringBuffer("");//错误消息
        String validateResult = "S"; //验证结果标记
        String errorCode=null;
        Date wsDate = null;//ws日期
        boolean breakFlag = false;
        List<WsResponse> list=new ArrayList<>();
        /*************************************************************************
         * 1.0 数据校验
         ************************************************************************ */
        String sourceSystem="";
        String interfaceName="";
        String batchNum="";
        String lineCount="";
        try {
            if(impItfMapList==null && impItfMapList.size()==0){
                sbErrorMsg.append("传输数据为空;");
                errorCode="E10002";
            }else {
                sourceSystem= String.valueOf(impItfMapList.get(0).get("sourceSystem"));
                interfaceName=String.valueOf(impItfMapList.get(0).get("interfaceName"));
                batchNum=String.valueOf(impItfMapList.get(0).get("batchNum"));
                lineCount=String.valueOf(impItfMapList.get(0).get("lineCount"));
            }
        }catch (Exception e){
            sbErrorMsg.append("报文解析异常;");
            errorCode="E10001";
        }

        /*************************************************************************
         * 3.0 ws数据写入ws各接口表
         ************************************************************************ */
        StringBuffer SQL=new StringBuffer("");
        int icount=0;
        int it=0;
        List<WsAnalysis> wsTableList = new ArrayList<>();
        WsAnalysis wsTable = new WsAnalysis();
         if (sbErrorMsg == null || "".equals(sbErrorMsg.toString())) {
             for (Map<String, Object> map : impItfMapList) {
                 icount++;it++;

                 WsResponse wsResponse = new WsResponse();
                 sbErrorMsg = new StringBuffer("");
                 errorCode = null;
                 validateResult = "S";
                 /*************************************************************************
                  * 1.0 数据校验
                  ************************************************************************ */
                 if (interfaceName == null || "".equals(interfaceName)) {
                     validateResult = "E";
                     sbErrorMsg.append("参数错误：interfaceName" + ",接口名称不能为空;");
                     errorCode = "E10000";

                 }
                 if (sourceSystem == null || "".equals(sourceSystem)) {
                     validateResult = "E";
                     sbErrorMsg.append("参数错误：sourceSystem" + ",来源系统不能为空;");
                     errorCode = "E10000";
                 }
                 if (batchNum == null || "".equals(batchNum)) {
                     validateResult = "E";
                     sbErrorMsg.append("参数错误：batchNum" + ",批次号不能为空;");
                     errorCode = "E10000";
                 }
                 if (lineCount == null || "".equals(lineCount)) {
                     validateResult = "E";
                     sbErrorMsg.append("参数错误：lineCount" + ",传输数量不能为空;");
                 }
                 if (lineCount != null) {
                     try {
                         int count = impItfMapList.size();
                         int lineCount1 = Integer.valueOf(String.valueOf(map.get("lineCount")).trim());
                         if (count != lineCount1) {
                             validateResult = "E";
                             sbErrorMsg.append("传输数量与实际数量不一致;");
                         }
                     } catch (Exception e) {
                         validateResult = "E";
                         sbErrorMsg.append("参数错误：lineCount" + ",格式应为数字;");
                     }
                 }
                 if (map.get("value1") == null || "".equals(map.get("value1"))) {
                     validateResult = "E";
                     sbErrorMsg.append("参数错误：value1" + ",唯一标识value1不能为空;");
                     errorCode = "E10000";
                 }
                 if (!(sourceSystem.equals(String.valueOf(map.get("sourceSystem"))) &&
                         interfaceName.equals(String.valueOf(map.get("interfaceName"))) &&
                         batchNum.equals(String.valueOf(map.get("batchNum"))) &&
                         lineCount.equals(String.valueOf(map.get("lineCount"))))) {
                     String sourceSystem1 = String.valueOf(map.get("sourceSystem"));
                     String interfaceName1 = String.valueOf(map.get("interfaceName"));
                     String batchNum1 = String.valueOf(map.get("batchNum"));
                     String lineCount1 = String.valueOf(map.get("lineCount"));
                     validateResult = "E";
                     sbErrorMsg.append("传入批次中sourceSystem:" + sourceSystem1 +
                             ",interfaceName:" + interfaceName1 +
                             ",batchNum:" + batchNum1 +
                             ",lineCount:" + lineCount1 +
                             "的值不一致;");
                 }

                 /*************************************************************************/
                 if (sbErrorMsg == null || "".equals(sbErrorMsg.toString())) {
                     breakFlag = false;
                     //拼接动态sql
                     StringBuffer sb = new StringBuffer("insert into ");
                     StringBuffer sbf1;
                     StringBuffer sbf;
                     sb.append("hscs_itf_imp_interfaces");
                     wsTable.setTableName("hscs_itf_imp_interfaces");
                     //数据库为mysql，无需插入id
                     sb.append("(");
                     sbf1 = new StringBuffer(")values");
                     sbf = new StringBuffer("(\'");

                     //匹配字段名称
                     sb.append("SOURCE_SYSTEM");
                     sb.append(",");
                     //转义单引号
                     sbf.append(sourceSystem);
                     sbf.append("','");
                     //匹配字段名称
                     sb.append("BATCH_NUM");
                     sb.append(",");
                     //转义单引号
                     sbf.append(batchNum);
                     sbf.append("','");
                     //匹配字段名称
                     sb.append("INTERFACE_NAME");
                     sb.append(",");
                     //转义单引号
                     sbf.append(interfaceName);
                     sbf.append("','");
                     //匹配字段名称
                     sb.append("LINE_COUNT");
                     sb.append(",");
                     //转义单引号
                     sbf.append(lineCount);
                     sbf.append("','");
                     //匹配字段名称
                     sb.append("MODULE_CODE");
                     sb.append(",");
                     //转义单引号
                     sbf.append("ITF");
                     sbf.append("','");
                     //匹配字段名称
                     sb.append("VALUE200");
                     sb.append(",");
                     //转义单引号
                     sbf.append(batchNum);
                     sbf.append("','");
                     //匹配字段名称
                     sb.append("STATUS_CODE");
                     sb.append(",");
                     //转义单引号
                     sbf.append("N");
                     sbf.append("','");

                     //循环150个value
                     String comumn = "value";
                     for (int i = 1; i <= 150; i++) {
                         //过滤为null的数据
                             //匹配字段名称
                             sb.append(comumn + i);
                             sb.append(",");
                             //转义单引号
                             Object o = map.get(comumn + i);
                             if (o != null) {
                                 String str = o.toString();
                                 str.replace("'", "''");
                                 sbf.append(str);
                                 sbf.append("','");
                             } else {
                                 sbf.append("");
                                 sbf.append("','");
                             }
                     }//end for (WsTableField field : fields)
                     if (breakFlag) {
                         continue;
                     }
                     String newSB = sb.substring(0, sb.length() - 1);
                     String newSBF = sbf.substring(0, sbf.length() - 2);
//                     String dynamicSQL = newSB + newSBF + ")";
//                     wsTable.setDynamicSQL(dynamicSQL);
                     //拼接SQL
                     if (it == 1) {
                         SQL.append(newSB).append(sbf1).append(newSBF);
                     } else {
                         SQL.append("),").append(newSBF);
                     }
                     //2000条插入一次
                     if(it==2000){
                         //清0，重新计数
                         it=0;
                         SQL.append(")");
                         wsTable.setDynamicSQL(SQL.toString());
                         wsTableList.add(wsTable);
                         //重新开始拼接
                          wsTable = new WsAnalysis();
                         SQL=new StringBuffer("");
                     }else if (icount == impItfMapList.size()) {
                         //最后一次插入
                         SQL.append(")");
                         wsTable.setDynamicSQL(SQL.toString());
                         wsTableList.add(wsTable);
                     }


                 }
                 if ("E".equals(validateResult)) {
                     wsResponse.setSourceSystem(String.valueOf(sourceSystem));
                     wsResponse.setInterfaceName(String.valueOf(interfaceName));
                     wsResponse.setBatchNum(String.valueOf(batchNum));
                     wsResponse.setUniqueCode(String.valueOf(map.get("value1")));
                     wsResponse.setSyncStatus(validateResult);
                     wsResponse.setErrorMessage(sbErrorMsg.toString());
                     wsResponse.setErrorCode(errorCode);
                     list.add(wsResponse);
                 }

             }
         }else {
             //报文解析错误
                 WsResponse wsResponse=new WsResponse();
                 wsResponse.setSourceSystem(sourceSystem);
                 wsResponse.setInterfaceName(interfaceName);
                 wsResponse.setBatchNum(batchNum);
                 wsResponse.setSyncStatus("E");
                 wsResponse.setErrorCode(errorCode);
                 wsResponse.setErrorMessage("报文解析异常!");
                 wsResponse.setUniqueCode("");
                 list.add(wsResponse);
         }
         if(list.size()==0){
             //事物处理回滚
             Boolean transactional=true;
             for(WsAnalysis wsTable1:wsTableList){
                 try {
                     wsTableDemoMapper.insertWsTable(wsTable1);
                 } catch (Exception e) {
                     transactional=false;
                 }//end try catch
             }
             if(transactional){
                 WsResponse wsResponse=new WsResponse();
                 wsResponse.setSourceSystem(sourceSystem);
                 wsResponse.setInterfaceName(interfaceName);
                 wsResponse.setSyncStatus("S");
                 wsResponse.setErrorMessage("");
                 wsResponse.setErrorCode(errorCode);
                 wsResponse.setUniqueCode("");
                 wsResponse.setBatchNum(batchNum);
                 list.add(wsResponse);
             }else {
                 throw new Exception("数据插入失败！");
             }

         }
        return list;
    }
}
