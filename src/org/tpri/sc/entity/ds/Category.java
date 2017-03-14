package org.tpri.sc.entity.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 试题分类
 * 
 * @author zhaozijing
 *
 */

@Entity
@Table(name = "DS_CATEGORY")
public class Category extends ObjectBase {
    private static final long serialVersionUID = 1L;

    public Category() {
        super();
        objectType = ObjectType.DS_CATEGORY;
    }

    public static final int STATUS_0 = 0;//正常
    public static final int STATUS_1 = 1;//禁用

    private String parentId;// 父ID
    private int status;// 状态 0正常，1禁用
    private int sequence;// 序号

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

    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "SEQUENCE")
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

}
