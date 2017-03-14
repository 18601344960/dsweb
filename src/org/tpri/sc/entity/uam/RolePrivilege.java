package org.tpri.sc.entity.uam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>角色权限bean<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月9日
 */

@Entity
@Table(name = "UAM_ROLE_PRIVILEGE")
@IdClass(value = RolePrivilegeId.class)
public class RolePrivilege extends ObjectBase {
    /** */
    private static final long serialVersionUID = 1L;
    
    protected String roleId;
    protected String privilegeId;

    public RolePrivilege() {
        super();
        objectType = ObjectType.UAM_ROLE_PRIVILEGE;
    }

    @Id
    @Column(name = "ROLE_ID")
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Id
    @Column(name = "PRIVILEGE_ID")
    public String getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(String privilegeId) {
        this.privilegeId = privilegeId;
    }

}
