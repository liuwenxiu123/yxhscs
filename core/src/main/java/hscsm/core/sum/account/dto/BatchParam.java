package hscsm.core.sum.account.dto;

/**
 * 总张凭证推送分批依据参数
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/27.
 */
public class BatchParam {
    private String jeBatchName;
    private String accountingDate;

    public String getJeBatchName() {
        return jeBatchName;
    }

    public void setJeBatchName(String jeBatchName) {
        this.jeBatchName = jeBatchName;
    }

    public String getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(String accountingDate) {
        this.accountingDate = accountingDate;
    }
}
