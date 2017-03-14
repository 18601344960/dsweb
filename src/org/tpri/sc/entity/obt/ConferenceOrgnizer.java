package org.tpri.sc.entity.obt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.uam.UserMc;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织生活组织者表<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月24日
 */
@Entity
@Table(name = "OBT_CONFERENCE_ORGNIZER")
public class ConferenceOrgnizer extends ObjectBase {
    private static final long serialVersionUID = -820143208534988235L;
    protected int objectType = ObjectType.OBT_CONFERENCE_ORGNIZER;

    public static final int TYPE_0 = 0;//系统内
    public static final int TYPE_1 = 1;//系统外

    protected String conferenceId;//支部工作ID
    protected int type;//状态:0系统内;1系统外
    protected String userId;//人员ID
    protected String userName;//人员姓名
    protected int orderNo;//顺序号

    public UserMc user;

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

    @Transient
    public UserMc getUser() {
        return user;
    }

    public void setUser(UserMc user) {
        this.user = user;
    }

}
