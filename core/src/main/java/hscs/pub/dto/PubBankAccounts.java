package hscs.pub.dto;

import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 覆盖银行账务DTO
 * 新增字段 LEDGER_NAME 分类账名称
 *
 * @author junlin.zhu@hand-china.com
 * @Time 2018/3/19.
 */
@ExtensionAttribute(disable = false)
@Table(name = "hscs_pub_bank_accounts")
public class PubBankAccounts extends BaseDTO {
    @Id
    @GeneratedValue
    private Long accountId; //表ID，主键，供其他表做外键, 银行账号ID

    @NotEmpty
    private String companyCode; //科目结构公司段值
    @Transient
    private String bankName;
    @Transient
    private String branchName;
    @Transient
    private String currencyName;
    @Transient
    private String description1;
    @Transient
    private String description2;
    @Transient
    private String description3;
    @Transient
    private String description4;
    @Transient
    private String currencyCode;
    @Transient
    private String companyFullName;

    /**
     * Alex
     * 2017-12-26
     * 记录所属公司名称
     */
    @Transient
    private String companyName;

    @Transient
    private String description5;

    @NotEmpty
    @Condition(operator = LIKE,autoWrap = false)
    private String accountNumber; //账号

    @NotEmpty
    @Condition(operator = LIKE,autoWrap = false)
    private String accountName; //户名

    @Condition(operator = LIKE,autoWrap = false)
    private String accountAlias; //账号别名

    @NotNull
    private Long bankId; //所属银行ID

    @NotNull
    private Long bankBranchId; //银行分行ID

    private String accountProperty; //账号性质

    @NotNull
    private Long currencyId; //币种

    @NotEmpty
    private String soaComCode; //公司段

    @NotEmpty
    private String soaAccCode; //现金科目段

    @NotEmpty
    private String soaSubaccCode; //子目段

    private String soaCostaccCode; //手续费科目段

    private String soaDeptCode; //部门段

    private String accountType; //账户类型

    @NotEmpty
    private String statusCode; //状态

    private String description; //说明

    private Long programId;

    private Long requestId;

    @Column(name = "LEDGER_NAME")
    private String ledgerName; //新增字段，分类账名

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCompanyFullName() {
        return companyFullName;
    }

    public void setCompanyFullName(String companyFullName) {
        this.companyFullName = companyFullName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription3() {
        return description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }

    public String getDescription4() {
        return description4;
    }

    public void setDescription4(String description4) {
        this.description4 = description4;
    }

    public String getDescription5() {
        return description5;
    }

    public void setDescription5(String description5) {
        this.description5 = description5;
    }

    public void setAccountId(Long accountId){
        this.accountId = accountId;
    }

    public Long getAccountId(){
        return accountId;
    }

    public void setCompanyCode(String companyCode){
        this.companyCode = companyCode;
    }

    public String getCompanyCode(){
        return companyCode;
    }

    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    public String getAccountName(){
        return accountName;
    }

    public void setAccountAlias(String accountAlias){
        this.accountAlias = accountAlias;
    }

    public String getAccountAlias(){
        return accountAlias;
    }

    public void setBankId(Long bankId){
        this.bankId = bankId;
    }

    public Long getBankId(){
        return bankId;
    }

    public void setBankBranchId(Long bankBranchId){
        this.bankBranchId = bankBranchId;
    }

    public Long getBankBranchId(){
        return bankBranchId;
    }

    public void setAccountProperty(String accountProperty){
        this.accountProperty = accountProperty;
    }

    public String getAccountProperty(){
        return accountProperty;
    }

    public void setCurrencyId(Long currencyId){
        this.currencyId = currencyId;
    }

    public Long getCurrencyId(){
        return currencyId;
    }

    public void setSoaComCode(String soaComCode){
        this.soaComCode = soaComCode;
    }

    public String getSoaComCode(){
        return soaComCode;
    }

    public void setSoaAccCode(String soaAccCode){
        this.soaAccCode = soaAccCode;
    }

    public String getSoaAccCode(){
        return soaAccCode;
    }

    public void setSoaSubaccCode(String soaSubaccCode){
        this.soaSubaccCode = soaSubaccCode;
    }

    public String getSoaSubaccCode(){
        return soaSubaccCode;
    }

    public void setSoaCostaccCode(String soaCostaccCode){
        this.soaCostaccCode = soaCostaccCode;
    }

    public String getSoaCostaccCode(){
        return soaCostaccCode;
    }

    public void setSoaDeptCode(String soaDeptCode){
        this.soaDeptCode = soaDeptCode;
    }

    public String getSoaDeptCode(){
        return soaDeptCode;
    }

    public void setAccountType(String accountType){
        this.accountType = accountType;
    }

    public String getAccountType(){
        return accountType;
    }

    public void setStatusCode(String statusCode){
        this.statusCode = statusCode;
    }

    public String getStatusCode(){
        return statusCode;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    @Override
    public void setProgramId(Long programId){
        this.programId = programId;
    }

    @Override
    public Long getProgramId(){
        return programId;
    }

    @Override
    public void setRequestId(Long requestId){
        this.requestId = requestId;
    }

    @Override
    public Long getRequestId(){
        return requestId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
