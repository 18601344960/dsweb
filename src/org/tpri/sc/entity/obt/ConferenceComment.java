package org.tpri.sc.entity.obt;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.uam.UserMc;

/**
 * 
 * <B>系统名称：</B>支部手册<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>评论Bean<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月21日
 */
@Entity
@Table(name = "OBT_CONFERENCE_COMMENT")
public class ConferenceComment extends ObjectBase {

    private static final long serialVersionUID = 1L;

    public ConferenceComment() {
        super();
        objectType = ObjectType.OBT_CONFERENCE_COMMENT;
    }

    protected String title;
    protected String content;
    protected String userId;
    protected String toUserId;
    protected String toUserName;
    protected int status;
    protected Timestamp createTime;
    protected Timestamp updateTime;

    private Conference conference; //文章实体
    private UserMc user;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "CREATE_TIME")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_TIME")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "TO_USER_ID")
    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    @Column(name = "TO_USER_NAME")
    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Transient
    public UserMc getUser() {
        return user;
    }

    public void setUser(UserMc user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "CONFERENCE_ID")
    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

}
