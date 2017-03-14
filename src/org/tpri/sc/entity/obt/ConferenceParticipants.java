package org.tpri.sc.entity.obt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织生活参会人员表<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月24日
 */
@Entity
@Table(name = "OBT_CONFERENCE_PARTICIPANTS")
public class ConferenceParticipants extends ObjectBase {
    private static final long serialVersionUID = -820143208534988235L;
    protected int objectType = ObjectType.OBT_CONFERENCE_PARTICIPANTS;

    public static final int STATUS_0 = 0;//参会
    public static final int STATUS_1 = 1;//缺席

    public static final int TYPE_0 = 0;//组织内
    public static final int TYPE_1 = 1;//组织外

    protected String conferenceId;//支部工作ID
    protected int type;//状态:0系统内;1系统外
    protected String userId;//人员ID
    protected String userName;//人员姓名
    protected int status;//状态:0参会;1缺席
    protected int orderNo;//顺序号

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "CONFERENCE_ID")
    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "USER_NAME")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "TYPE")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    @Column(name = "ORDER_NO")
    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

}
