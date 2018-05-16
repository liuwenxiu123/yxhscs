package hscsm.core.sum.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * EBS返回报文接收类
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultMessage {
    @JsonProperty("HAP_GL_ID")
    private String hapGlId;

    @JsonProperty("X_RETURN_STATUS")
    private String xReturnStatus;

    @JsonProperty("ACCOUNT_STATUS")
    private String accountingStatus;

    @JsonProperty("X_ERROR_MESSAGE")
    private String xErrorMessage;



    public String getHapGlId() {
        return hapGlId;
    }

    public void setHapGlId(String hapGlId) {
        this.hapGlId = hapGlId;
    }

    public String getxReturnStatus() {
        return xReturnStatus;
    }

    public void setxReturnStatus(String xReturnStatus) {
        this.xReturnStatus = xReturnStatus;
    }

    public String getAccountingStatus() {
        return accountingStatus;
    }

    public void setAccountingStatus(String accountingStatus) {
        this.accountingStatus = accountingStatus;
    }

    public String getxErrorMessage() {
        return xErrorMessage;
    }

    public void setxErrorMessage(String xErrorMessage) {
        this.xErrorMessage = xErrorMessage;
    }
}
