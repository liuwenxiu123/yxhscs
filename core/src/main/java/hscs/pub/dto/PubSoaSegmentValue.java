package hscs.pub.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 目标表结构更改覆盖DTO
 * 将Description属性的 @NotEmpty 校验去掉
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/4/11.
 */
@ExtensionAttribute(
        disable = false
)
@Table(
        name = "hscs_pub_soa_segment_value"
)
public class PubSoaSegmentValue extends BaseDTO {
    @Id
    @GeneratedValue
    private Long soaValueId;
    @NotEmpty
    private String typeCode;
    @NotEmpty
    private String valuesetName;
    @NotEmpty
    private String segmentValue;

    private String description;
    @NotEmpty
    private String enableFlag;
    private Date startActiveDate;
    private Date endActiveDate;
    @Transient
    private Long parentCompanyId;
    @NotEmpty
    private String fatherFlag;
    @NotEmpty
    private String levelCode;
    private String fatherValue;
    private String segment1;
    private String segment2;
    private String segment3;
    private String segment4;
    private String segment5;
    private String segment6;
    private String segment7;
    private String segment8;
    private String segment9;
    private String segment10;
    private String segment11;
    private String segment12;
    private String segment13;
    private String segment14;
    private String segment15;
    private String segment16;
    private String segment17;
    private String segment18;
    private String segment19;
    private String segment20;
    private String segment21;
    private String segment22;
    private String segment23;
    private String segment24;
    private String segment25;
    private String segment26;
    private String segment27;
    private String segment28;
    private String segment29;
    private String segment30;
    private Long programId;
    private Long requestId;

    public PubSoaSegmentValue() {
    }

    public Long getParentCompanyId() {
        return this.parentCompanyId;
    }

    public void setParentCompanyId(Long parentCompanyId) {
        this.parentCompanyId = parentCompanyId;
    }

    public void setSoaValueId(Long soaValueId) {
        this.soaValueId = soaValueId;
    }

    public Long getSoaValueId() {
        return this.soaValueId;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeCode() {
        return this.typeCode;
    }

    public void setValuesetName(String valuesetName) {
        this.valuesetName = valuesetName;
    }

    public String getValuesetName() {
        return this.valuesetName;
    }

    public void setSegmentValue(String segmentValue) {
        this.segmentValue = segmentValue;
    }

    public String getSegmentValue() {
        return this.segmentValue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getEnableFlag() {
        return this.enableFlag;
    }

    public void setStartActiveDate(Date startActiveDate) {
        this.startActiveDate = startActiveDate;
    }

    public Date getStartActiveDate() {
        return this.startActiveDate;
    }

    public void setEndActiveDate(Date endActiveDate) {
        this.endActiveDate = endActiveDate;
    }

    public Date getEndActiveDate() {
        return this.endActiveDate;
    }

    public void setFatherFlag(String fatherFlag) {
        this.fatherFlag = fatherFlag;
    }

    public String getFatherFlag() {
        return this.fatherFlag;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getLevelCode() {
        return this.levelCode;
    }

    public void setFatherValue(String fatherValue) {
        this.fatherValue = fatherValue;
    }

    public String getFatherValue() {
        return this.fatherValue;
    }

    public void setSegment1(String segment1) {
        this.segment1 = segment1;
    }

    public String getSegment1() {
        return this.segment1;
    }

    public void setSegment2(String segment2) {
        this.segment2 = segment2;
    }

    public String getSegment2() {
        return this.segment2;
    }

    public void setSegment3(String segment3) {
        this.segment3 = segment3;
    }

    public String getSegment3() {
        return this.segment3;
    }

    public void setSegment4(String segment4) {
        this.segment4 = segment4;
    }

    public String getSegment4() {
        return this.segment4;
    }

    public void setSegment5(String segment5) {
        this.segment5 = segment5;
    }

    public String getSegment5() {
        return this.segment5;
    }

    public void setSegment6(String segment6) {
        this.segment6 = segment6;
    }

    public String getSegment6() {
        return this.segment6;
    }

    public void setSegment7(String segment7) {
        this.segment7 = segment7;
    }

    public String getSegment7() {
        return this.segment7;
    }

    public void setSegment8(String segment8) {
        this.segment8 = segment8;
    }

    public String getSegment8() {
        return this.segment8;
    }

    public void setSegment9(String segment9) {
        this.segment9 = segment9;
    }

    public String getSegment9() {
        return this.segment9;
    }

    public void setSegment10(String segment10) {
        this.segment10 = segment10;
    }

    public String getSegment10() {
        return this.segment10;
    }

    public void setSegment11(String segment11) {
        this.segment11 = segment11;
    }

    public String getSegment11() {
        return this.segment11;
    }

    public void setSegment12(String segment12) {
        this.segment12 = segment12;
    }

    public String getSegment12() {
        return this.segment12;
    }

    public void setSegment13(String segment13) {
        this.segment13 = segment13;
    }

    public String getSegment13() {
        return this.segment13;
    }

    public void setSegment14(String segment14) {
        this.segment14 = segment14;
    }

    public String getSegment14() {
        return this.segment14;
    }

    public void setSegment15(String segment15) {
        this.segment15 = segment15;
    }

    public String getSegment15() {
        return this.segment15;
    }

    public void setSegment16(String segment16) {
        this.segment16 = segment16;
    }

    public String getSegment16() {
        return this.segment16;
    }

    public void setSegment17(String segment17) {
        this.segment17 = segment17;
    }

    public String getSegment17() {
        return this.segment17;
    }

    public void setSegment18(String segment18) {
        this.segment18 = segment18;
    }

    public String getSegment18() {
        return this.segment18;
    }

    public void setSegment19(String segment19) {
        this.segment19 = segment19;
    }

    public String getSegment19() {
        return this.segment19;
    }

    public void setSegment20(String segment20) {
        this.segment20 = segment20;
    }

    public String getSegment20() {
        return this.segment20;
    }

    public void setSegment21(String segment21) {
        this.segment21 = segment21;
    }

    public String getSegment21() {
        return this.segment21;
    }

    public void setSegment22(String segment22) {
        this.segment22 = segment22;
    }

    public String getSegment22() {
        return this.segment22;
    }

    public void setSegment23(String segment23) {
        this.segment23 = segment23;
    }

    public String getSegment23() {
        return this.segment23;
    }

    public void setSegment24(String segment24) {
        this.segment24 = segment24;
    }

    public String getSegment24() {
        return this.segment24;
    }

    public void setSegment25(String segment25) {
        this.segment25 = segment25;
    }

    public String getSegment25() {
        return this.segment25;
    }

    public void setSegment26(String segment26) {
        this.segment26 = segment26;
    }

    public String getSegment26() {
        return this.segment26;
    }

    public void setSegment27(String segment27) {
        this.segment27 = segment27;
    }

    public String getSegment27() {
        return this.segment27;
    }

    public void setSegment28(String segment28) {
        this.segment28 = segment28;
    }

    public String getSegment28() {
        return this.segment28;
    }

    public void setSegment29(String segment29) {
        this.segment29 = segment29;
    }

    public String getSegment29() {
        return this.segment29;
    }

    public void setSegment30(String segment30) {
        this.segment30 = segment30;
    }

    public String getSegment30() {
        return this.segment30;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Long getProgramId() {
        return this.programId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRequestId() {
        return this.requestId;
    }
}
