package org.tpri.sc.entity.obt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.sys.Code;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举领导班子成员党内职务bean<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */
@Entity
@Table(name = "OBT_ELECTION_MEMBER_TITLE")
public class ElectionMemberTitle extends ObjectBase {
    private static final long serialVersionUID = -820143208534988235L;
    protected int objectType = ObjectType.OBT_ELECTION_MEMBER_TITLE;

    protected String memberId; //换届选举班子成员Id
    protected String partyTitleId;//党内职务
    protected int sequence;//排序

    private Code code;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "PARTY_TITLE_ID")
    public String getPartyTitleId() {
        return partyTitleId;
    }

    public void setPartyTitleId(String partyTitleId) {
        this.partyTitleId = partyTitleId;
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

    @Column(name = "MEMBER_ID")
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

}
