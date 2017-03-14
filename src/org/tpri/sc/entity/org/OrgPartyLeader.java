package org.tpri.sc.entity.org;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.obt.PartyMember;

/**
 * @description 组织领导班子bean
 * @author zhaozijing
 * @since 2015-06-22
 */
@Entity
@Table(name="ORG_CCPARTY_LEADER")
public class OrgPartyLeader extends ObjectBase {
	
	private static final long serialVersionUID = 1L;
	
	protected String title;
	protected String ccpartyId;
//	protected String partyMemberId;
	protected int status;
	protected String remark;
	protected String createUser;
	protected Timestamp createTime;
	protected String updateUser;
	protected Timestamp updateTime;
	
	protected PartyMember partyMember;
	protected String userName;
	
	public OrgPartyLeader(){
		super();
		objectType = ObjectType.ORG_PARTY_LEADER;
	}
	
	@Id  
	@Column(name="ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="TITLE")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name="PARTYID")
	public String getCcpartyId() {
		return ccpartyId;
	}
	public void setCcpartyId(String partyId) {
		this.ccpartyId = partyId;
	}
	
//	@Column(name="PARTYMEMBERID")
//	public String getPartyMemberId() {
//		return partyMemberId;
//	}
//	public void setPartyMemberId(String partyMemberId) {
//		this.partyMemberId = partyMemberId;
//	}
	
	@Column(name="STATUS")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="CREATEUSER")
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@Column(name="CREATETIME")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="UPDATEUSER")
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	@Column(name="UPDATETIME")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	@Transient
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE},targetEntity=PartyMember.class,fetch=FetchType.EAGER)
	@JoinColumn(name="PARTYMEMBERID")
	public PartyMember getPartyMember() {
		return partyMember;
	}
	public void setPartyMember(PartyMember partyMember) {
		this.partyMember = partyMember;
	}
	
	
	
	

}
