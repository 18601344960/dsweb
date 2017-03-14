package org.tpri.sc.entity.obt;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.uam.UserMc;



/**
 * <B>系统名称：</B>特殊党费实体类<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（王钱俊）
 * @since 2015年11月12日
 */
@Entity
@Table(name = "OBT_PARTY_FEE_SPECIAL")
public class PartyFeeSpecial extends ObjectBase{
    /**  */
    private static final long serialVersionUID = 1L;
    protected int objectType = ObjectType.OBT_PARTY_FEE_SPECIAL;
    
    private String id;
    private String ccpartyId;
    private String userId;
    private int year;
    private double amount;
    private String remark;
    private String createUserId;
    private Date createTime;
    
    protected UserMc userMc;
    
    @Transient
    public UserMc getUserMc() {
        return userMc;
    }
    public void setUserMc(UserMc userMc) {
        this.userMc = userMc;
    }
    
    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name = "CCPARTY_ID")
    public String getCcpartyId() {
        return ccpartyId;
    }
    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Column(name = "YEAR")
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    
    @Column(name = "AMOUNT")
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
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
    
    @Override
    public String toString() {
        return "PartyFeeSpecial [objectType=" + objectType + ", id=" + id + ", userId=" + userId + ", year=" + year+", ccpartyId="+ccpartyId
                + ", amount=" + amount + ", remark=" + remark + ",userMc="+ userMc+ ", createUserId=" + createUserId + ", createTime="
                + createTime + "]";
    }
    
    
    
}
