package hscsm.core.api.dto;

/**
 * shuai.xie 2018/02/27
 * webservice数据处理后返回状态
 */
public class WsResponse {
    //系统
    private String sourceSystem;
    //接口名称
    private String interfaceName;
    //数据批次
    private String batchNum;
    //唯一编码
    private String uniqueCode;
    //同步状态  E:错误 U:警告  S:成功
    private String syncStatus;
    //错误代码
    private String errorCode;
    //错误消息
    private String errorMessage;

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}
