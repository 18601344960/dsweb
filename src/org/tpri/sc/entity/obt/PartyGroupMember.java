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
 * <B>中文类名：</B>党小组成员bean<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年8月1日
 */
@Entity
@Table(name = "OBT_PARTY_GROUP_MEMBER")
public class PartyGroupMember extends ObjectBase {
    private static final long serialVersionUID = -820143208534988235L;
    protected int objectType = ObjectType.OBT_PARTY_GROUP_MEMBER;

    public static final int TYPE_0 = 0; // 普通成员
    public static final int TYPE_1 = 1; // 副组长
    public static final int TYPE_2 = 2; // 组长

    protected String groupId; //党小组ID
    protected int type; //成员类型：0普通成员；1副组长；2组长
    protected String userId;//成员ID
    protected int sequence;//排序
    private UserMc user;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "SEQUENCE")
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Column(name = "GROUP_ID")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Column(name = "TYPE")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Transient
    public UserMc getUser() {
        return user;
    }

    public void setUser(UserMc user) {
        this.user = user;
    }

}
