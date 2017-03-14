package org.tpri.sc.manager.uam;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.uam.RolePrivilege;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>角色权限管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月4日
 */

@Repository("RolePrivilegeManager")
public class RolePrivilegeManager extends ManagerBase {
    private static boolean initialized = false;

    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        ObjectRegister.registerClass(ObjectType.UAM_ROLE_PRIVILEGE, RolePrivilege.class);
        List list = loadList(ObjectType.UAM_ROLE_PRIVILEGE, null, null, null, null);
        List<RolePrivilege> rolePrivileges = new ArrayList<RolePrivilege>();
        for (int i = 0; i < list.size(); i++) {
            RolePrivilege rolePrivilege = (RolePrivilege) list.get(i);
            rolePrivilege.setId(rolePrivilege.getRoleId() + "_" + rolePrivilege.getPrivilegeId());
            rolePrivileges.add(rolePrivilege);
        }
        initializeObjects(ObjectType.UAM_ROLE_PRIVILEGE, rolePrivileges);
    }

    /**
     * 添加角色权限
     * 
     * @return
     */
    public void addRolePrivilege(RolePrivilege rolePrivilege) {
        saveOrUpdate(rolePrivilege);
        rolePrivilege.setId(rolePrivilege.getRoleId() + "_" + rolePrivilege.getPrivilegeId());
        addCache(rolePrivilege);
    }

    /**
     * 更新角色权限
     * 
     * @return
     */
    public void updateRolePrivilege(RolePrivilege rolePrivilege) {
        saveOrUpdate(rolePrivilege);
        rolePrivilege.setId(rolePrivilege.getRoleId() + "_" + rolePrivilege.getPrivilegeId());
        updateCache(rolePrivilege);
    }

    /**
     * 删除角色权限
     * 
     * @return
     */
    public void deleteRolePrivilege(RolePrivilege rolePrivilege) {
        this.deleteRolePrivilegeForHQL(rolePrivilege);
        rolePrivilege.setId(rolePrivilege.getRoleId() + "_" + rolePrivilege.getPrivilegeId());
        removeCache(rolePrivilege);
    }
    
    public void deleteRolePrivilegeForHQL(RolePrivilege rolePrivilege){
        StringBuffer hql = new StringBuffer();
        hql.append(" delete from RolePrivilege as rp where rp.roleId=:roleId and rp.privilegeId=:privilegeId ");
        dao.getCurrentSession().createQuery(hql.toString()).setString("roleId", rolePrivilege.getRoleId()).setString("privilegeId", rolePrivilege.getPrivilegeId()).executeUpdate();
    }

    /**
     * 从缓存中获取角色权限
     * 
     * @return
     */
    public RolePrivilege getRolePrivilegeFromMc(String roleId, String privilegeId) {
        RolePrivilege rolePrivilege = (RolePrivilege) loadMcCacheObject(ObjectType.UAM_ROLE_PRIVILEGE, roleId + "_" + privilegeId);
        return rolePrivilege;
    }

    /**
     * 删除某角色下的权限列表
     * 
     * @return
     */
    public void deleteByRoleId(String roleId) {
        List<RolePrivilege> rolePrivileges = getRolePrivilegeList(roleId);
        if (rolePrivileges != null && rolePrivileges.size() > 0) {
            for (RolePrivilege rolePrivilege : rolePrivileges) {
                deleteRolePrivilege(rolePrivilege);
            }
        }
    }

    /**
     * 获取角色的权限列表
     * 
     * @return
     */
    public List<RolePrivilege> getRolePrivilegesFromMc(String roleId) {
        List<RolePrivilege> list = new ArrayList<RolePrivilege>();
        List<ObjectBase> objectList = loadMcList(ObjectType.UAM_ROLE_PRIVILEGE);
        for (ObjectBase objectBase : objectList) {
            RolePrivilege rolePrivilege = (RolePrivilege) objectBase;
            if (rolePrivilege != null && rolePrivilege.getRoleId() != null && rolePrivilege.getRoleId().equals(roleId)) {
                list.add(rolePrivilege);
            }
        }
        return list;
    }

    /**
     * 获取角色的权限列表
     * 
     * @return
     */
    public List<RolePrivilege> getRolePrivilegeList(String roleId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(RolePrivilege.class);
        daoPara.addCondition(Condition.EQUAL("roleId", roleId));
        List list = dao.loadList(daoPara);
        return list;
    }
}
