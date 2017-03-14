package org.tpri.sc.entity.pub;

import java.sql.Timestamp;
import java.util.ArrayList;
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

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：问卷测评</B><BR>
 * <B>中文类名：问卷测评bean</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年6月16日
 */
@Entity
@Table(name = "PUB_ASSESSMENT")
public class Assessment extends ObjectBase {

    private static final long serialVersionUID = 9018054539434507341L;

    protected int objectType = ObjectType.PUB_ASSESSMENT;

    public static final int STATUS_0 = 0; //未开启
    public static final int STATUS_1 = 1; //已开启
    public static final int STATUS_2 = 2; //已关闭
    public static final int ISEXPIRY_0 = 0; //是否有截止日期 无
    public static final int ISEXPIRY_1 = 1; //是否有截止日期 有

    protected String ccpartyId;//党组织
    protected String description; //描述
    protected Date endDate; //截止日期
    protected int status; //状态：0未开启1已开启2已关闭
    protected String createUserId; //创建人
    protected Timestamp createTime; //创建时间
    private int isExpiry = 0; //是否有截止日期：0无1有  

    public int joinNum = 0;//参与人数
    public List<AssessmentTarget> targets = new ArrayList<AssessmentTarget>();//参与单位集合
    public List<AssessmentTopic> topics = new ArrayList<AssessmentTopic>(); //试题集合
    private CCParty ccparty;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Column(name = "CCPARTY_ID")
    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Transient
    public List<AssessmentTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<AssessmentTarget> targets) {
        this.targets = targets;
    }

    @Transient
    public List<AssessmentTopic> getTopics() {
        return topics;
    }

    public void setTopics(List<AssessmentTopic> topics) {
        this.topics = topics;
    }

    @Transient
    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    @Column(name = "IS_EXPIRY")
    public int getIsExpiry() {
        return isExpiry;
    }

    public void setIsExpiry(int isExpiry) {
        this.isExpiry = isExpiry;
    }

    @Transient
    public CCParty getCcparty() {
        return ccparty;
    }

    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }

}
