package org.tpri.sc.entity.uam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * @description 角色bean
 * @author 常华荣
 * @since 2015-04-09
 */

@Entity
@Table(name = "UAM_PRIVILEGE")
public class Privilege extends ObjectBase {

    /**  */
    private static final long serialVersionUID = 1L;

    public static final String ROOTID = "root";

    protected String description;
    protected String parentId;

    public Privilege() {
        super();
        objectType = ObjectType.UAM_PRIVILEGE;
    }

    @Id
    @Column(name = "ID")
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

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
