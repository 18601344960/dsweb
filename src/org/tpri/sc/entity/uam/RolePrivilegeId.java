package org.tpri.sc.entity.uam;


/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>角色权限ID<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月4日
 */
public class RolePrivilegeId implements java.io.Serializable {
    /** */
    private static final long serialVersionUID = 1L;

    protected String roleId;
    protected String privilegeId;

    public RolePrivilegeId() {

    }

    public RolePrivilegeId(String roleId, String privilegeId) {
        this.roleId = roleId;
        this.privilegeId = privilegeId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(String privilegeId) {
        this.privilegeId = privilegeId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof RolePrivilegeId))
            return false;
        RolePrivilegeId castOther = (RolePrivilegeId) other;

        return ((this.getRoleId() == castOther.getRoleId()) || (this.getRoleId() != null && castOther.getRoleId() != null && this.getRoleId().equals(castOther.getRoleId())))
                && ((this.getPrivilegeId() == castOther.getPrivilegeId()) || (this.getPrivilegeId() != null && castOther.getPrivilegeId() != null && this.getPrivilegeId().equals(
                        castOther.getPrivilegeId())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getRoleId() == null ? 0 : this.getRoleId().hashCode());
        result = 37 * result + (getPrivilegeId() == null ? 0 : this.getPrivilegeId().hashCode());
        return result;
    }
}
