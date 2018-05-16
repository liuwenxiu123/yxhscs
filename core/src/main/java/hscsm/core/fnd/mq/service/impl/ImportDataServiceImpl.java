package hscsm.core.fnd.mq.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hscsm.core.fnd.mq.dto.FeedMessageDto;
import hscsm.core.fnd.mq.dto.HeadLineDto;
import hscsm.core.fnd.mq.dto.ItfImpLines;
import hscsm.core.fnd.mq.mapper.ItfImplLineMapper;
import hscsm.core.fnd.mq.service.ImportDataService;
import hscsm.core.fnd.mq.service.MqService;
import hscsm.core.fnd.mq.utils.Base64Utils;
import hscsm.core.fnd.mq.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ldap.HasControls;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by wangxu on 2018/3/2.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ImportDataServiceImpl implements ImportDataService {

    @Autowired
    ItfImplLineMapper itfImplLineMapper;

    @Autowired
    MqService mqService;

    @Override
    public void sendAllFailImportDatas() throws Exception {

        // 查询每次的更新失败的行
        List<ItfImpLines> itfImpLines = itfImplLineMapper.getFailLineData();

        if (itfImpLines.size() > 0) { //只发送失败的数据行到mq
            List<FeedMessageDto> feedMessageDtoList = new ArrayList<FeedMessageDto>();
            List<HeadLineDto> headLineDtos = new ArrayList<HeadLineDto>();
            for (int i = 0; i < itfImpLines.size(); i++) {
                ItfImpLines itfImpLines1 = itfImpLines.get(i);
                FeedMessageDto feedMessageDto = new FeedMessageDto();
                feedMessageDto.setSourceSystem(itfImpLines1.getSourceSystemCode());
                feedMessageDto.setInterfaceName(itfImpLines1.getInterfaceName());
                feedMessageDto.setBatchNum(itfImpLines1.getBatchNum());
                feedMessageDto.setErrorCode(null);
                feedMessageDto.setErrorMessage(itfImpLines1.getProcessMessage());
                feedMessageDto.setUniqueCode(itfImpLines1.getValue1());
                feedMessageDto.setSyncStatus(Constants.FAILED_FLAG);
                feedMessageDtoList.add(feedMessageDto);

                HeadLineDto headLineDto = new HeadLineDto();
                headLineDto.setLineId(itfImpLines1.getLineId());
                headLineDtos.add(headLineDto);
            }

            try {
                this.distributeSysData(headLineDtos, feedMessageDtoList, Constants.LINE_DATA);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("数据分发失败!");
            }

        }
        List<ItfImpLines> itfImpSuccessHeader = itfImplLineMapper.getSuccessHeadersData();
        if (itfImpSuccessHeader.size() > 0) { // 只发送成功的头到mq
            List<FeedMessageDto> feedMessageDtoList = new ArrayList<FeedMessageDto>();
            List<HeadLineDto> headLineDtos = new ArrayList<HeadLineDto>();
            for (int i = 0; i < itfImpSuccessHeader.size(); i++) {
                ItfImpLines itfImpLines1 = itfImpSuccessHeader.get(i);
                FeedMessageDto feedMessageDto = new FeedMessageDto();

                feedMessageDto.setSourceSystem(itfImpLines1.getSourceSystemCode());
                feedMessageDto.setInterfaceName(itfImpLines1.getInterfaceName());
                feedMessageDto.setBatchNum(itfImpLines1.getBatchNum());
                feedMessageDto.setErrorCode(null);
                feedMessageDto.setErrorMessage(null);
                feedMessageDto.setUniqueCode(null);
                feedMessageDto.setSyncStatus(Constants.SUCEESS_FLAG);
                feedMessageDtoList.add(feedMessageDto);

                HeadLineDto headLineDto = new HeadLineDto();
                headLineDto.setHeaderId(itfImpLines1.getHeaderId());
                headLineDtos.add(headLineDto);
            }
            try {
                this.distributeSysData(headLineDtos, feedMessageDtoList, Constants.HEADER_DATA);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("数据分发失败!");
            }
        }
    }

    /**
     * 发送不同数据到不同系统mq
     *
     * @param feedMessageDtos 数据集合
     * @param dataType        数据来源(line / header)
     */

    private void distributeSysData(List<HeadLineDto> headLineDtos, List<FeedMessageDto> feedMessageDtos, String dataType) throws Exception {

        List<FeedMessageDto> alixList = new ArrayList<FeedMessageDto>();
        List<FeedMessageDto> pmsList = new ArrayList<FeedMessageDto>();
        List<FeedMessageDto> malList = new ArrayList<FeedMessageDto>();
        List<FeedMessageDto> oplList = new ArrayList<FeedMessageDto>();

        for (int i = 0; i < feedMessageDtos.size(); i++) {
            FeedMessageDto feedMessageDto = feedMessageDtos.get(i);
            if (Constants.ALIX_SYSTEM.equals(feedMessageDto.getSourceSystem())) {
                alixList.add(feedMessageDto);
            } else if (Constants.PSM_SYSTEM.equals(feedMessageDto.getSourceSystem())) {
                pmsList.add(feedMessageDto);
            } else if (Constants.MAL_SYSTEM.equals(feedMessageDto.getSourceSystem())) {
                malList.add(feedMessageDto);
            } else if (Constants.OPL_SYSTEM.equals(feedMessageDto.getSourceSystem())) {
                oplList.add(feedMessageDto);
            }
        }

        if (alixList.size() > 0) {
            this.sendMsgByApplyNum(headLineDtos, alixList, Constants.ALIX_SYSTEM, dataType);
        }

        if (pmsList.size() > 0) {
            this.sendMsgByApplyNum(headLineDtos, pmsList, Constants.PSM_SYSTEM, dataType);
        }

        if (malList.size() > 0) {
            this.sendMsgByApplyNum(headLineDtos, malList, Constants.MAL_SYSTEM, dataType);
        }

        if (oplList.size() > 0) {
            this.sendMsgByApplyNum(headLineDtos, oplList, Constants.OPL_SYSTEM, dataType);
        }

    }

    /**
     * 推送信息到不同系统
     *
     * @param list          数据集合
     * @param sysSourceType // 指定推送的系统
     * @param dataType      // 数据来源类型(header or line)
     */

    private void sortMsgAction(List<HeadLineDto> headLineDtos, List<FeedMessageDto> list, String sysSourceType, String dataType) throws Exception {

        String feedMsg = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            String aa = mapper.writeValueAsString(list);
//            logger.debug(aa);
            feedMsg = Base64Utils.encryptBASE64(mapper.writeValueAsString(list));
        } catch (JsonProcessingException e) {
            throw new Exception("mq消息base64转换失败!");
        }

        try {
            mqService.sendMsg(sysSourceType, feedMsg);
            if (Constants.LINE_DATA.equals(dataType)) {
                itfImplLineMapper.batchUpdateFailDataFlags(headLineDtos); // 更新行
            } else if (Constants.HEADER_DATA.equals(dataType)) {
                itfImplLineMapper.updateSuccessHeaderFlags(headLineDtos); // 更新头
            }

        }catch (Exception e){
             e.printStackTrace();
           throw new Exception("mq消息未发送成功!");
        }


    }


    /**
     * 根据申请编号分批推到mq
     */

    private void sendMsgByApplyNum(List<HeadLineDto> headLineDtos, List<FeedMessageDto> list, String sysSourceType, String dataType) throws Exception {

        Map<String, List<FeedMessageDto>> batchNumMap = new HashMap<String, List<FeedMessageDto>>();
        for (int i = 0; i < list.size(); i++) {
            FeedMessageDto fmd = list.get(i);
            String batchNum1 = fmd.getBatchNum();
            List<FeedMessageDto> fmdList = batchNumMap.get(batchNum1);
            if (fmdList == null) {
                List<FeedMessageDto> tempFmdList = new ArrayList<>();
                tempFmdList.add(fmd);
                batchNumMap.put(batchNum1, tempFmdList);
            } else {
                fmdList.add(fmd);
            }
        }

        Iterator iterator = batchNumMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            List<FeedMessageDto> feedMessageDtoList = (List<FeedMessageDto>) entry.getValue();
            this.sortMsgAction(headLineDtos, feedMessageDtoList, sysSourceType, dataType);
        }
    }

}
