package hscsm.core.sum.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 接口调用返回结果--EBS报文格式
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/9.
 */
public class EbsReturnData {
    @JsonProperty("HAP_GROUP_ID")
    private String hapGroupId;

    @JsonProperty("X_RETURN_STATUS")
    private String status;

    @JsonProperty("X_ERROR_MESSAGE")
    private String message;

    @JsonProperty("P_RETURN_DATA")
    private List<?> rows;

    public String getHapGroupId() {
        return hapGroupId;
    }

    public void setHapGroupId(String hapGroupId) {
        this.hapGroupId = hapGroupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
