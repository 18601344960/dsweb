package org.tpri.sc.entity.obt;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.UserMc;



@Entity
@Table(name = "OBT_PARTY_FEE_REPORT")
public class PartyFeeReport extends ObjectBase{

    /**  */
    private static final long serialVersionUID = 1L;
    protected int objectType = ObjectType.OBT_PARTY_FEE_REPORT;
    
    public static final int TYPE_0=0;     //0自有1报送
    public static final int TYPE_1=1;     
    
    public static final int FINISHED_STATUS_0=0;  //0已完成1未完成
    public static final int FINISHED_STATUS_1=1;
    
    public static final int STATUS_0=0;   //0拟稿1审核中2审核不通过3上报中4报送出错5完成6打回
    public static final int STATUS_1=1;   
    public static final int STATUS_2=2;   
    public static final int STATUS_3=3;   
    public static final int STATUS_4=4;   
    public static final int STATUS_5=5;   
    public static final int STATUS_6=6; 
    
    public static final int REPORT_STATUS_0 = 0; //0新上报1不通过2通过
    public static final int REPORT_STATUS_1 = 1;
    public static final int REPORT_STATUS_2 = 2;
    
    
    
    private String id;
    private int type;
    private String ccpartyId;
    private String fromCcpartyId;
    private String fromId;
    private int year;
    private double payAbleTotal;
    private double realFeeTotal;
    private int finishedState;
    private double specialFeeTotal;
    private double useFeeTotal;
    private String remark;
    private String createUserId;
    private Date createTime;
    private int status;
    private int reportsStatus;
    private String reportUserId;
    private Date reportTime;
    private String beforeAuditId;
    private Date beforeAuditTime;
    
    protected CCParty ccparty;
    protected CCParty fromCcparty;
    protected UserMc user;
    
    @Transient
    public CCParty getCcparty() {
        return ccparty;
    }
    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }
    @Transient
    public CCParty getFromCcparty() {
        return fromCcparty;
    }
    public void setFromCcparty(CCParty fromCcparty) {
        this.fromCcparty = fromCcparty;
    }
    @Transient
    public UserMc getUser() {
        return user;
    }
    public void setUser(UserMc user) {
        this.user = user;
    }
    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name = "TYPE")
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    
    @Column(name = "CCPARTY_ID")
    public String getCcpartyId() {
        return ccpartyId;
    }
    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }
    
    @Column(name = "FROM_CCPARTY_ID")
    public String getFromCcpartyId() {
        return fromCcpartyId;
    }
    public void setFromCcpartyId(String fromCcpartyId) {
        this.fromCcpartyId = fromCcpartyId;
    }
    
    @Column(name = "FROM_ID")
    public String getFromId() {
        return fromId;
    }
    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
    
    @Column(name = "YEAR")
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    
    @Column(name = "PAYABLE_TOTAL")
    public double getPayAbleTotal() {
        return payAbleTotal;
    }
    public void setPayAbleTotal(double payAbleTotal) {
        this.payAbleTotal = payAbleTotal;
    }
    
    @Column(name = "REAL_FEE_TOTAL")
    public double getRealFeeTotal() {
        return realFeeTotal;
    }
    public void setRealFeeTotal(double realFeeTotal) {
        this.realFeeTotal = realFeeTotal;
    }
    
    @Column(name = "FINISHED_STATE")
    public int getFinishedState() {
        return finishedState;
    }
    public void setFinishedState(int finishedState) {
        this.finishedState = finishedState;
    }
    
    @Column(name = "SPECIAL_FEE_TOTAL")
    public double getSpecialFeeTotal() {
        return specialFeeTotal;
    }
    public void setSpecialFeeTotal(double specialFeeTotal) {
        this.specialFeeTotal = specialFeeTotal;
    }
    
    @Column(name = "USE_FEE_TOTAL")
    public double getUseFeeTotal() {
        return useFeeTotal;
    }
    public void setUseFeeTotal(double useFeeTotal) {
        this.useFeeTotal = useFeeTotal;
    }
    
    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Column(name = "CREATE_USER_ID")
    public String getCreateUserId() {
        return createUserId;
    }
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
    
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
    @Column(name = "REPORT_STATUS")
    public int getReportsStatus() {
        return reportsStatus;
    }
    public void setReportsStatus(int reportsStatus) {
        this.reportsStatus = reportsStatus;
    }
    
    @Column(name = "REPORT_USER_ID")
    public String getReportUserId() {
        return reportUserId;
    }
    public void setReportUserId(String reportUserId) {
        this.reportUserId = reportUserId;
    }
    
    @Column(name = "REPORT_TIME")
    public Date getReportTime() {
        return reportTime;
    }
    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }
    
    @Column(name = "BEFORE_AUDIT_ID")
    public String getBeforeAuditId() {
        return beforeAuditId;
    }
    public void setBeforeAuditId(String beforeAuditId) {
        this.beforeAuditId = beforeAuditId;
    }
    
    @Column(name = "BEFORE_AUDIT_TIME")
    public Date getBeforeAuditTime() {
        return beforeAuditTime;
    }
    public void setBeforeAuditTime(Date beforeAuditTime) {
        this.beforeAuditTime = beforeAuditTime;
    }
    @Override
    public String toString() {
        return "PartyFeeReport [objectType=" + objectType + ", id=" + id + ", type=" + type + ", ccpartyId="
                + ccpartyId + ", fromCcpartyId=" + fromCcpartyId + ", fromId=" + fromId + ", year=" + year
                + ", payAbleTotal=" + payAbleTotal + ", realFeeTotal=" + realFeeTotal + ", finishedState="
                + finishedState + ", specialFeeTotal=" + specialFeeTotal + ", useFeeTotal=" + useFeeTotal + ", remark="
                + remark + ", createUserId=" + createUserId + ", createTime=" + createTime + ", status=" + status
                + ", reportsStatus=" + reportsStatus + ", reportUserId=" + reportUserId + ", reportTime=" + reportTime
                + ", beforeAuditId=" + beforeAuditId + ", beforeAuditTime=" + beforeAuditTime + ", ccparty=" + ccparty
                + ", fromCcparty=" + fromCcparty + ", user=" + user + "]";
    }
    
   
    
}
