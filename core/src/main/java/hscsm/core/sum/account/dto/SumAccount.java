package hscsm.core.sum.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hscsm.core.sum.account.utils.ByteLength;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 总账表，变量名为EBS目标表字段名的驼峰写法
 * 该DTO加入了自定义的字节长度的校验，专用于EBS的请求
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/7.
 */
@Table(name = "HSCS_AE_TFR_SUM_ACCOUNTS")
public class SumAccount {
    //账务汇总ID
    @Column(name = "TFR_SUM_ACCOUNT_ID")
    @JsonProperty("HAP_GL_ID")
    private String hapGlId;

    //传输批次ID
    @Column(name = "ATTRIBUTE1")
    @JsonProperty("HAP_GROUP_ID")
    private Long hapGroupId;

    @Column(name = "TFR_SUM_ACCOUNT_NUM")
    @JsonProperty("SUM_ACC_NUM")
    private String sumAccNum;

    //分类账名称
    @Column(name = "VALUE1")
    @JsonProperty("LDG_NM")
    @ByteLength(max = 30, message = "分类账名称")
    private String ldgNm;

    //日记账批名
    @Column(name = "VALUE2")
    @JsonProperty("BT_NM")
    @ByteLength(max = 100, message = "日记账批名")
    private String btNm;

    //批说明
    @Column(name = "VALUE3")
    @JsonProperty("BT_DESC")
    @ByteLength(max = 240, message = "批说明")
    private String btDesc;

    //日记账名称
    @Column(name = "VALUE4")
    @JsonProperty("JE_NM")
    @ByteLength(max = 100, message = "日记账名称")
    private String jeNm;

    //日记账头说明
    @Column(name = "VALUE5")
    @JsonProperty("JE_DESC")
    @ByteLength(max = 240 , message = "日记账头说明")
    private String jeDesc;

    //期间
    @Transient
    @JsonProperty("PRD_NM")
    @ByteLength(max = 15, message = "期间")
    private String prdNm;

    //入账日期
    @Column(name = "ACCOUNTING_DATE")
    @JsonProperty("ACCT_DT")
    @ByteLength(max = 30, message = "入账日期")
    private String acctDt;

    //日记账类别
    @Column(name = "VALUE6")
    @JsonProperty("CTG_NM")
    @ByteLength(max = 25, message = "日记账类别")
    private String ctgNm;

    //币种
    @Column(name = "VALUE7")
    @JsonProperty("CURY_CD")
    @ByteLength(max = 15 , message = "币种")
    private String curyCd;

    //汇率类型
    @Column(name = "VALUE8")
    @JsonProperty("CONVER_TP")
    @ByteLength(max = 30, message = "汇率类型")
    private String converTp;

    //汇率日期
    @Column(name = "ACCOUNTING_DATE")
    @JsonProperty("CUNVER_DT")
    @ByteLength(max = 30, message = "汇率日期")
    private String cunverDt;

    //汇率值
    @Column(name = "VALUE9")
    @JsonProperty("CUR_CONVER_RT")
    private String curConverRt;

    //贷方原币金额
    @Column(name = "VALUE27")
    @JsonProperty("ETR_CR")
    private String etrCr;

    //借方原币金额
    @Column(name = "VALUE25")
    @JsonProperty("ETR_DR")
    private String etrDr;

    //贷方本币金额
    @Column(name = "VALUE28")
    @JsonProperty("ACCT_CR")
    private String acctCr;

    //借方本币金额
    @Column(name = "VALUE26")
    @JsonProperty("ACCT_DR")
    private String acctDr;

    //日记账分录行说明
    @Column(name = "VALUE11")
    @JsonProperty("LINE_DESC")
    @ByteLength(max = 240, message = "日记账分录行说明")
    private String lineDesc;

    //COA段1-公司
    @Column(name = "VALUE12")
    @JsonProperty("SEG1")
    @ByteLength(max = 25, message = "COA段1-公司")
    private String seg1;

    //COA段2-成本中心
    @Column(name = "VALUE13")
    @JsonProperty("SEG2")
    @ByteLength(max = 25, message = "COA段2-成本中心")
    private String seg2;

    //COA段3-会计科目
    @Column(name = "VALUE14")
    @JsonProperty("SEG3")
    @ByteLength(max = 25, message = "COA段3-会计科目")
    private String seg3;

    //COA段4-明细段
    @Column(name = "VALUE15")
    @JsonProperty("SEG4")
    @ByteLength(max = 25, message = "COA段4-明细段")
    private String seg4;

    //COA段5-业务线
    @Column(name = "VALUE16")
    @JsonProperty("SEG5")
    @ByteLength(max = 25, message = "COA段5-业务线")
    private String seg5;

    //COA段6-区域
    @Column(name = "VALUE17")
    @JsonProperty("SEG6")
    @ByteLength(max = 25, message = "COA段6-区域")
    private String seg6;

    //COA段7-项目
    @Column(name = "VALUE18")
    @JsonProperty("SEG7")
    @ByteLength(max = 25, message = "COA段7-项目")
    private String seg7;

    //COA段8-产品
    @Column(name = "VALUE19")
    @JsonProperty("SEG8")
    @ByteLength(max = 25, message = "COA段8-产品")
    private String seg8;

    //COA段9-渠道
    @Column(name = "VALUE20")
    @JsonProperty("SEG9")
    @ByteLength(max = 25, message = "COA段9-渠道")
    private String seg9;

    //COA段10-关联公司
    @Column(name = "VALUE21")
    @JsonProperty("SEG10")
    @ByteLength(max = 25, message = "COA段10-关联公司")
    private String seg10;

    //COA段11-备用1
    @Column(name = "VALUE22")
    @JsonProperty("SEG11")
    @ByteLength(max = 25, message = "COA段11-备用1")
    private String seg11;

    //COA段12-备用2
    @Column(name = "VALUE23")
    @JsonProperty("SEG12")
    @ByteLength(max = 25, message = "COA段12-备用2")
    private String seg12;

    //COA段13-备用3
    @Column(name = "VALUE24")
    @JsonProperty("SEG13")
    @ByteLength(max = 25, message = "COA段13-备用3")
    private String seg13;

    public String getHapGlId() {
        return hapGlId;
    }

    public void setHapGlId(String hapGlId) {
        this.hapGlId = hapGlId;
    }

    public Long getHapGroupId() {
        return hapGroupId;
    }

    public void setHapGroupId(Long hapGroupId) {
        this.hapGroupId = hapGroupId;
    }

    public String getSumAccNum() {
        return sumAccNum;
    }

    public void setSumAccNum(String sumAccNum) {
        this.sumAccNum = sumAccNum;
    }

    public String getLdgNm() {
        return ldgNm;
    }

    public void setLdgNm(String ldgNm) {
        this.ldgNm = ldgNm;
    }

    public String getBtNm() {
        return btNm;
    }

    public void setBtNm(String btNm) {
        this.btNm = btNm;
    }

    public String getBtDesc() {
        return btDesc;
    }

    public void setBtDesc(String btDesc) {
        this.btDesc = btDesc;
    }

    public String getJeNm() {
        return jeNm;
    }

    public void setJeNm(String jeNm) {
        this.jeNm = jeNm;
    }

    public String getJeDesc() {
        return jeDesc;
    }

    public void setJeDesc(String jeDesc) {
        this.jeDesc = jeDesc;
    }

    public String getPrdNm() {
        return prdNm;
    }

    public void setPrdNm(String prdNm) {
        this.prdNm = prdNm;
    }


    public String getCtgNm() {
        return ctgNm;
    }

    public void setCtgNm(String ctgNm) {
        this.ctgNm = ctgNm;
    }

    public String getCuryCd() {
        return curyCd;
    }

    public void setCuryCd(String curyCd) {
        this.curyCd = curyCd;
    }

    public String getConverTp() {
        return converTp;
    }

    public void setConverTp(String converTp) {
        this.converTp = converTp;
    }

    public String getCunverDt() {
        return cunverDt;
    }

    public void setCunverDt(String cunverDt) {
        this.cunverDt = cunverDt;
    }

    public String getCurConverRt() {
        return curConverRt;
    }

    public void setCurConverRt(String curConverRt) {
        this.curConverRt = curConverRt;
    }

    public String getEtrCr() {
        return etrCr;
    }

    public void setEtrCr(String etrCr) {
        this.etrCr = etrCr;
    }

    public String getEtrDr() {
        return etrDr;
    }

    public void setEtrDr(String etrDr) {
        this.etrDr = etrDr;
    }

    public String getAcctCr() {
        return acctCr;
    }

    public void setAcctCr(String acctCr) {
        this.acctCr = acctCr;
    }

    public String getAcctDr() {
        return acctDr;
    }

    public void setAcctDr(String acctDr) {
        this.acctDr = acctDr;
    }

    public String getLineDesc() {
        return lineDesc;
    }

    public void setLineDesc(String lineDesc) {
        this.lineDesc = lineDesc;
    }

    public String getSeg1() {
        return seg1;
    }

    public void setSeg1(String seg1) {
        this.seg1 = seg1;
    }

    public String getSeg2() {
        return seg2;
    }

    public void setSeg2(String seg2) {
        this.seg2 = seg2;
    }

    public String getSeg3() {
        return seg3;
    }

    public void setSeg3(String seg3) {
        this.seg3 = seg3;
    }

    public String getSeg4() {
        return seg4;
    }

    public void setSeg4(String seg4) {
        this.seg4 = seg4;
    }

    public String getSeg5() {
        return seg5;
    }

    public void setSeg5(String seg5) {
        this.seg5 = seg5;
    }

    public String getSeg6() {
        return seg6;
    }

    public void setSeg6(String seg6) {
        this.seg6 = seg6;
    }

    public String getSeg7() {
        return seg7;
    }

    public void setSeg7(String seg7) {
        this.seg7 = seg7;
    }

    public String getSeg8() {
        return seg8;
    }

    public void setSeg8(String seg8) {
        this.seg8 = seg8;
    }

    public String getSeg9() {
        return seg9;
    }

    public void setSeg9(String seg9) {
        this.seg9 = seg9;
    }

    public String getSeg10() {
        return seg10;
    }

    public void setSeg10(String seg10) {
        this.seg10 = seg10;
    }

    public String getSeg11() {
        return seg11;
    }

    public void setSeg11(String seg11) {
        this.seg11 = seg11;
    }

    public String getSeg12() {
        return seg12;
    }

    public void setSeg12(String seg12) {
        this.seg12 = seg12;
    }

    public String getSeg13() {
        return seg13;
    }

    public void setSeg13(String seg13) {
        this.seg13 = seg13;
    }

    public String getAcctDt() {
        return acctDt;
    }

    public void setAcctDt(String acctDt) {
        this.acctDt = acctDt;
    }
}