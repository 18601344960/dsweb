package org.tpri.sc.entity.obt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * @description 标签bean
 * @author 易文俊
 * @since 2015-04-30
 */
@Entity
@Table(name = "OBT_CONFERENCE_CATEGORY")
public class ConferenceCategory extends ObjectBase {

    public static int TYPE_0 = 0; // 共用栏目
    public static int TYPE_1 = 1; // 自有栏目

    public ConferenceCategory() {
        super();
        objectType = ObjectType.OBT_CONFERENCE_CATEGORY;
    }

    private static final long serialVersionUID = 1L;
    protected int type;
    protected String parentId;
    protected String ccpartyId;
    protected String description;
    protected int orderNo;
    protected int readOnly;

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
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "CCPARTY_ID")
    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ORDER_NO")
    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "READ_ONLY")
    public int getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(int readOnly) {
        this.readOnly = readOnly;
    }

}
