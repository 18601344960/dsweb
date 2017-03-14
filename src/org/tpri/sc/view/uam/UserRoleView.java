package org.tpri.sc.view.uam;

import org.tpri.sc.entity.uam.Role;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>用户权限拥有状态<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年1月11日
 */
public class UserRoleView {
    private Role role; //权限
    private boolean isHave; //是否拥有 true拥有 false没有该权限

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean getIsHave() {
        return isHave;
    }

    public void setIsHave(boolean isHave) {
        this.isHave = isHave;
    }

}
