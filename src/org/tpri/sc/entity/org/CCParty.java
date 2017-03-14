package org.tpri.sc.entity.org;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * @description 党组织bean
 * @author 易文俊
 * @since 2015-04-24
 */
@Entity
@Table(name = "ORG_CCPARTY")
public class CCParty extends ObjectBase {

    public static final int STATUS_0 = 0; // 有效
    public static final int STATUS_1 = 1; // 挂起

    public static final String TYPE_1 = "Z000102"; //党委
    public static final String TYPE_2 = "Z000101"; //党组
    public static final String TYPE_3 = "Z000103"; //总支
    public static final String TYPE_4 = "Z000104"; //党支部

    public static final int LEAF_0 = 0; //不是
    public static final int FEAF_1 = 1; //是

    private static final long serialVersionUID = 1L;
    protected String type; //党组织类型 
    protected String orgId;
    protected String parentId;
    protected String description;
    protected String documentNo;
    protected Date documentTime;
    protected Date expirationTime;
    protected int isTip;
    protected int status;
    protected String createUserId;
    protected Timestamp createTime;
    protected String updateUserId;
    protected Timestamp updateTime;
    protected int isLeaf; //是否叶节点：0不是1是
    protected Integer partyType; //组织类型:0无1部机关2在京直属单位3京外直属单位
    protected Integer sequence = 10000; //序号
    protected String fullName;//全称

    public CCParty() {
        super();
        objectType = ObjectType.ORG_CCPARTY;
    }

    @Id
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

    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "DOCUMENT_NO")
    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    @Column(name = "DOCUMENT_TIME")
    public Date getDocumentTime() {
        return documentTime;
    }

    public void setDocumentTime(Date documentTime) {
        this.documentTime = documentTime;
    }

    @Column(name = "EXPIRATION_TIME")
    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Column(name = "IS_TIP")
    public int getIsTip() {
        return isTip;
    }

    public void setIsTip(int isTip) {
        this.isTip = isTip;
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

    @Column(name = "IS_LEAF")
    public int getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(int isLeaf) {
        this.isLeaf = isLeaf;
    }

    @Column(name = "PARTY_TYPE")
    public Integer getPartyType() {
        return partyType;
    }

    public void setPartyType(Integer partyType) {
        this.partyType = partyType;
    }

    @Column(name = "SEQUENCE")
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Column(name = "FULL_NAME")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
