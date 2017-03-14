package org.tpri.sc.entity.obt;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.UserMc;

/**
 * 
 * @Description: 党员信息bean
 * @author zhaozijing
 * @date 2015年7月6日 下午5:05:24
 */
@Entity
@Table(name = "OBT_PARTY_MEMBER")
public class PartyMember extends ObjectBase implements Serializable {

    public static final int DB_TABLE_STATUS_0 = 0; //status字段  0 正常
    public static final int DB_TABLE_STATUS_1 = 1; //status字段  1 挂起

    public static final int STATUS_0 = 0; //status字段  0 正常
    public static final int STATUS_1 = 1; //status字段  1 转出中
    public static final int STATUS_2 = 2; //status字段  2 转出
    public static final int STATUS_3 = 3; //status字段  3 退党

    public static final int TYPE_0 = 0; //申请人
    public static final int TYPE_1 = 1; //积极分子
    public static final int TYPE_2 = 2; //发展对象
    public static final int TYPE_3 = 3; //预备党员
    public static final int TYPE_4 = 4; //正式党员

    private static final long serialVersionUID = 1L;
    protected String partyNo; //党员代号
    public String ccpartyId; //所属党组织
    protected Date joinTime; //入党时间
    protected Date joinCurrentTime; //加入当前组织时间
    protected int joinType; //进入当前支部类型：0新入党1恢复党籍2组织关系转入
    protected int type; //党员类型:0入党申请1积极分子2发展对象3预备党员4流动党员5正式党员
    protected String introducer; //入党介绍人
    protected Date passTime; //支部大会通过时间
    protected Date auditTime; //上级组织批准时间
    protected double fee; //月缴纳党费（元）
    protected int joinActivity; //参加组织生活情况：0正常参加1间断参加2不参加
    protected Date applyTime; //申请入党日期
    protected Date activistTime; //列为积极分子日期
    protected Date targetTime; //列为发展对象日期
    protected String trainer; //培养联系人
    protected int status; //状态：0有效1挂起
    protected String description; //描述
    protected String createUserId; //创建人
    protected Timestamp createTime; //创建日期
    protected String updateUserId; //更新人
    protected Timestamp updateTime; //更新日期
    protected int isManager; //是否党务工作人员0否1是
    protected int isDelegate; //是否党代表：0否1是
    protected int isDestitution; //是否困难党员:0否1是
    protected String developmentId;//发展阶段

    protected UserMc user; //User实体
    protected CCParty ccparty; //组织实体
    protected DevelopmentProcedure process;//发展阶段实体
    protected PartyMemberDevelopmentInfo developmentInfo;//党员发展详情
    protected List<ElectionMember> electionMembers;//党内职务
    protected String electionMemberTitle;//该党员所担任的党内职务

    public PartyMember() {
        super();
        objectType = ObjectType.OBT_PARTY_MEMBER;
    }

    @Transient
    public UserMc getUser() {
        return user;
    }

    public void setUser(UserMc user) {
        this.user = user;
    }

    @Transient
    public CCParty getCcparty() {
        return ccparty;
    }

    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "CREATE_USER_ID")
    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Column(name = "CREATE_TIME")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_USER_ID")
    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Column(name = "UPDATE_TIME")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "PARTY_NO")
    public String getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(String partyNo) {
        this.partyNo = partyNo;
    }

    @Column(name = "CCPARTY_ID")
    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    @Column(name = "JOIN_TIME")
    @Temporal(TemporalType.DATE)
    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    @Column(name = "TYPE")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "INTRODUCER")
    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }

    @Column(name = "JOIN_CURRENT_TIME")
    public Date getJoinCurrentTime() {
        return joinCurrentTime;
    }

    public void setJoinCurrentTime(Date joinCurrentTime) {
        this.joinCurrentTime = joinCurrentTime;
    }

    @Column(name = "JOIN_TYPE")
    public int getJoinType() {
        return joinType;
    }

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    @Column(name = "PASS_TIME")
    public Date getPassTime() {
        return passTime;
    }

    public void setPassTime(Date passTime) {
        this.passTime = passTime;
    }

    @Column(name = "AUDIT_TIME")
    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    @Column(name = "FEE")
    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @Column(name = "JOIN_ACTIVITY")
    public int getJoinActivity() {
        return joinActivity;
    }

    public void setJoinActivity(int joinActivity) {
        this.joinActivity = joinActivity;
    }

    @Column(name = "APPLY_TIME")
    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    @Column(name = "ACTIVIST_TIME")
    public Date getActivistTime() {
        return activistTime;
    }

    public void setActivistTime(Date activistTime) {
        this.activistTime = activistTime;
    }

    @Column(name = "TARGET_TIME")
    public Date getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(Date targetTime) {
        this.targetTime = targetTime;
    }

    @Column(name = "TRAINER")
    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    @Column(name = "IS_MANAGER")
    public int getIsManager() {
        return isManager;
    }

    public void setIsManager(int isManager) {
        this.isManager = isManager;
    }

    @Column(name = "IS_DELEGATE")
    public int getIsDelegate() {
        return isDelegate;
    }

    public void setIsDelegate(int isDelegate) {
        this.isDelegate = isDelegate;
    }

    @Column(name = "IS_DESTITUTION")
    public int getIsDestitution() {
        return isDestitution;
    }

    public void setIsDestitution(int isDestitution) {
        this.isDestitution = isDestitution;
    }

    @Column(name = "DEVELOPMENT_ID")
    public String getDevelopmentId() {
        return developmentId;
    }

    public void setDevelopmentId(String developmentId) {
        this.developmentId = developmentId;
    }

    @Transient
    public DevelopmentProcedure getProcess() {
        return process;
    }

    public void setProcess(DevelopmentProcedure process) {
        this.process = process;
    }

    @Transient
    public PartyMemberDevelopmentInfo getDevelopmentInfo() {
        return developmentInfo;
    }

    public void setDevelopmentInfo(PartyMemberDevelopmentInfo developmentInfo) {
        this.developmentInfo = developmentInfo;
    }

    @Transient
    public String getElectionMemberTitle() {
        return electionMemberTitle;
    }

    public void setElectionMemberTitle(String electionMemberTitle) {
        this.electionMemberTitle = electionMemberTitle;
    }

    @Transient
    public List<ElectionMember> getElectionMembers() {
        return electionMembers;
    }

    public void setElectionMembers(List<ElectionMember> electionMembers) {
        this.electionMembers = electionMembers;
    }

    @Override
    public String toString() {
        return "PartyMember [objectType=" + objectType + ", partyNo=" + partyNo + ", ccpartyId=" + ccpartyId + ", joinTime=" + joinTime + ", joinCurrentTime=" + joinCurrentTime + ", joinType="
                + joinType + ", type=" + type + ", introducer=" + introducer + ", passTime=" + passTime + ", auditTime=" + auditTime + ", fee=" + fee
                + ", joinActivity=" + joinActivity + ", applyTime=" + applyTime + ", activistTime=" + activistTime + ", targetTime=" + targetTime + ", trainer=" + trainer + ", status=" + status
                + ", description=" + description + ", createUser=" + createUserId + ", createTime=" + createTime + ", updateUser=" + updateUserId + ", updateTime=" + updateTime + ", isManager="
                + isManager + ", isDelegate=" + isDelegate + ", isDestitution=" + isDestitution + ", user=" + user + ", ccparty=" + ccparty + "]";
    }

}
