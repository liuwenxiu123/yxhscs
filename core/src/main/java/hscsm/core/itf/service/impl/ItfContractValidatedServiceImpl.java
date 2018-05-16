package hscsm.core.itf.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hscsm.core.itf.dto.ContractData;
import hscsm.core.itf.dto.ItfContractValidated;
import hscsm.core.itf.mapper.ItfContractValidatedMapper;
import hscsm.core.itf.service.IItfContractValidatedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/2.
 */
@Service
public class ItfContractValidatedServiceImpl extends BaseServiceImpl<ItfContractValidated> implements IItfContractValidatedService {

    @Autowired
    private ItfContractValidatedMapper itfContractValidatedMapper;

    private Logger logger = LoggerFactory.getLogger(hscsm.core.itf.service.impl.ItfContractValidatedServiceImpl.class);

    /**
     * 根据接口头行表的数据状态，将数据同步到合同成交明细目标表
     */
    @Override
    public void updateByStatus(){
        this.logger.info("合同取消红冲凭证生成Job开始执行");

        List<ContractData> contractDataList = itfContractValidatedMapper.getContractDataByStatus();
        if(contractDataList.size() == 0){
            this.logger.info("没有需要更新的数据，Job执行结束");
            return;
        }
        this.logger.debug("更新记录条数:" + contractDataList.size());

        // 根据不同的头ID，分批更新数据
        for (ContractData contractData : contractDataList){
            try {
                updateContractData(contractData);
            } catch (Exception e) {
                if (this.logger.isErrorEnabled()) {
                    this.logger.error(e.getMessage());
                }
            }
        }

        this.logger.info("合同取消红冲凭证生成Job执行结束");
    }

    /**
     * 更新合同成交明细表信息
     * @param contractData 合同成交明细表的更新数据
     */
    @Transactional(rollbackFor = Exception.class)
    protected void updateContractData(ContractData contractData) throws Exception {

        //进行数据校验，合同结束时间与合同状态不能为空
        boolean dataError = contractData.getContractEndDate().isEmpty()
                || contractData.getContractStatus().isEmpty()
                || "".equals(contractData.getContractStatus().trim())
                || "".equals(contractData.getContractEndDate().trim());
        if(dataError){
            this.logger.info("错误数据----->ApplyNum:[" + contractData.getApplyNum() + "]");
            if(itfContractValidatedMapper.updateItfImpLineStatus(contractData.getApplyNum(),"E") == 0){
                try {
                    throw new Exception("接口头表数据状态更新异常");
                } catch (Exception e) {
                    if (this.logger.isErrorEnabled()) {
                        this.logger.error(e.getMessage());
                    }
                }
            }
            return;
        }

        // 数据未导入目标表，不进行更新操作
        if(itfContractValidatedMapper.updateContractValidated(contractData) == 0){
            this.logger.info("目标表不存在对应数据,ApplyNum:" + contractData.getApplyNum());
            return;
        }

        // 更新接口头表的数据状态
        if(itfContractValidatedMapper.updateItfImpLineStatus(contractData.getApplyNum(),"Y") == 0){
            throw new Exception("接口头表数据状态更新异常");
        }
    }
}
