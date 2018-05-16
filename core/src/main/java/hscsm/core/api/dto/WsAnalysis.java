package hscsm.core.api.dto;

import com.hand.hap.system.dto.BaseDTO;

/**
 * Created by shuai.xie 2018/03/01
 */
public class WsAnalysis extends BaseDTO {
    /**
	 * 
	 */
	private static final long serialVersionUID = -9126184533632708362L;

	private String fieldName;

    private String fieldType;

    private String fieldValue;
    
    private String tableName;
    
    private String dynamicSQL;

    public String getDynamicSQL() {
		return dynamicSQL;
	}

	public void setDynamicSQL(String dynamicSQL) {
		this.dynamicSQL = dynamicSQL;
	}

	public String getFieldName() {
        return fieldName;
    }

    public WsAnalysis setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public String getFieldType() {
        return fieldType;
    }

    public WsAnalysis setFieldType(String fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public WsAnalysis setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
        return this;
    }
    public String getTableName() {
        return tableName;
    }

    public WsAnalysis setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
}
