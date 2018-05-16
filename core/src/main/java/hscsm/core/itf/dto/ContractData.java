package hscsm.core.itf.dto;

/**
 * 合同取消红冲凭证生成专用DTO，用于更新ItfContractValidated表的数据
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/2.
 */
public class ContractData {

    // 目标表标识
    private String applyNum;

    // 合同状态
    private String contractStatus;

    // 合同结束时间
    private String contractEndDate;

    public String getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(String applyNum) {
        this.applyNum = applyNum;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }
}
