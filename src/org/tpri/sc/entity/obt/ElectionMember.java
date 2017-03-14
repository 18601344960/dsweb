package org.tpri.sc.entity.obt;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.uam.UserMc;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举领导班子成员bean<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */
@Entity
@Table(name = "OBT_ELECTION_MEMBER")
public class ElectionMember extends ObjectBase {
    private static final long serialVersionUID = -820143208534988235L;
    protected int objectType = ObjectType.OBT_ELECTION_MEMBER;

    public static final int USERTYPE_0 = 0; // 组织内成员
    public static final int USERTYPE_1 = 1; // 组织外成员

    protected String electionId; //换届选举
    protected int userType; //成员类型：0系统内成员1系统外成员
    protected String userId;//成员ID
    protected String userName;//成员名称
    protected int gender;//性别
    protected Date birthDay;//出生年月
    protected Date startTime;//批准任职日期
    protected Date endTime;//批准免职日期
    protected String remark;//职务说明
    protected int sequence;//排序
    protected String workerId;//党务干部ID

    private List<ElectionMemberTitle> memberTitles;
    private CCParty ccparty;
    private Code code;
    private UserMc user;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "ELECTION_ID")
    public String getElectionId() {
        return electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "USER_NAME")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "USER_TYPE")
    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Column(name = "START_DATE")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "END_DATE")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Transient
    public UserMc getUser() {
        return user;
    }

    public void setUser(UserMc user) {
        this.user = user;
    }

    @Column(name = "SEQUENCE")
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Transient
    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    @Transient
    public CCParty getCcparty() {
        return ccparty;
    }

    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }

    @Column(name = "GENDER")
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Column(name = "BIRTHDAY")
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Transient
    public List<ElectionMemberTitle> getMemberTitles() {
        return memberTitles;
    }

    public void setMemberTitles(List<ElectionMemberTitle> memberTitles) {
        this.memberTitles = memberTitles;
    }

    @Column(name = "WORKER_ID")
    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

}
