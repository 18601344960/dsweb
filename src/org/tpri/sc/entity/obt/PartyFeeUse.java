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


/**
 * 
 * <B>系统名称：</B>党费使用实体类<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（王钱俊）
 * @since 2015年9月19日
 */
@Entity
@Table(name = "OBT_PARTY_FEE_USE")
public class PartyFeeUse extends ObjectBase {
    public static final int TYPE_0=0;  //0/1 本身/下级
    public static final int TYPE_1=1;
    
    public static final int STATUS_0=0; //0未提交1党组织负责人审核中2党组织负责人打回3组织部审核中4组织部打回5上级审核中6上级打回7审批通过8上报出错
    public static final int STATUS_1=1;
    public static final int STATUS_2=2;
    public static final int STATUS_3=3;
    public static final int STATUS_4=4;
    public static final int STATUS_5=5;
    public static final int STATUS_6=6;
    public static final int STATUS_7=7;
    public static final int STATUS_8=8;
    
    public static final int AUDIT_STATUS_0=0;  //0新申请1审核不通过2审核通过
    public static final int AUDIT_STATUS_1=1;
    public static final int AUDIT_STATUS_2=2;
    
    /**  */
    private static final long serialVersionUID = 2521276609095396941L;

    protected int objectType = ObjectType.OBT_PARTY_FEE_USE;
    
    private String id;
    private int type;
    private String ccpartyId;
    private String fromCcpartyId;
    private String fromId;
    private String activityLocation;
    private Date activityTime;
    private String participants;
    private int population;
    private String content;
    private String description;
    private int amount;
    private int status;
    private int auditStatus;
    private String createUserId;
    private Date createTime;
    private String remark;
    private String reportUserId;
    private Date reportTime;
    private String beforeAuditId;
    private Date beforeAuditTime;
    
    private CCParty ccparty;
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
    @Column(name = "ACTIVITY_LOCATION")
    public String getActivityLocation() {
        return activityLocation;
    }
    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }
    @Column(name = "ACTIVITY_TIME")
    public Date getActivityTime() {
        return activityTime;
    }
    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }
    @Column(name = "PARTICIPANTS")
    public String getParticipants() {
        return participants;
    }
    public void setParticipants(String participants) {
        this.participants = participants;
    }
    @Column(name = "POPULATION")
    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = population;
    }
    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name = "AMOUNT")
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    @Column(name = "AUDIT_STATUS")
    public int getAuditStatus() {
        return auditStatus;
    }
    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
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
    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
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
    @Transient
    public CCParty getCcparty() {
        return ccparty;
    }
    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }
    @Override
    public String toString() {
        return "PartyFeeUse [objectType=" + objectType + ", id=" + id + ", type=" + type + ", ccpartyId=" + ccpartyId
                + ", fromCcpartyId=" + fromCcpartyId + ", fromId=" + fromId + ", activityLocation=" + activityLocation
                + ", activityTime=" + activityTime + ", participants=" + participants + ", population=" + population
                + ", content=" + content + ", description=" + description + ", amount=" + amount + ", status=" + status
                + ", auditStatus=" + auditStatus + ", createUserId=" + createUserId + ", createTime=" + createTime
                + ", remark=" + remark + ", reportUserId=" + reportUserId + ", reportTime=" + reportTime
                + ", beforeAuditId=" + beforeAuditId + ", beforeAuditTime=" + beforeAuditTime + ", ccparty=" + ccparty
                + "]";
    }
   
  
   
}
