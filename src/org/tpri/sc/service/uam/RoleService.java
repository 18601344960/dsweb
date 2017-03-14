package org.tpri.sc.service.uam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.uam.Privilege;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.RolePrivilege;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.manager.uam.PrivilegeManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.RolePrivilegeManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.view.ZTreeView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>角色服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月9日
 */

@Service("RoleService")
public class RoleService {

    @Autowired
    private RoleManager roleManager;
    @Autowired
    private PrivilegeManager privilegeManager;
    @Autowired
    private RolePrivilegeManager rolePrivilegeManager;
    @Autowired
    private UserManager userManager;

    /**
     * 获取角色
     * 
     * @return
     */
    public Role getRoleById(String id) {
        Role role = roleManager.getRoleById(id);
        if (role != null) {
            List<RolePrivilege> rolePrivileges = rolePrivilegeManager.getRolePrivilegesFromMc(id);
            if (rolePrivileges != null && rolePrivileges.size() > 0) {
                List<Privilege> privileges = new ArrayList<Privilege>();
                for (RolePrivilege rolePrivilege : rolePrivileges) {
                    Privilege privilege = privilegeManager.getPrivilege(rolePrivilege.getPrivilegeId());
                    privileges.add(privilege);
                }
                role.setPrivileges(privileges);
            }
        }
        return role;
    }

    /**
     * 
     * @Description: 获取角色列表
     * @param searchName
     * @param start
     * @param limit
     * @return
     */
    public List loadRole(String searchName, Integer start, Integer limit) {
        List list = roleManager.getRoleList(searchName, start, limit);
        return list;
    }

    /**
     * 
     * @Description: 获取角色列表条数
     * @param searchName
     * @return
     */
    public Integer loadRoleTotal(String searchName) {
        return roleManager.loadRoleTotal(searchName);
    }

    public boolean addRole(String id, String name, String description, String status) {

        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        role.setStatus(Integer.valueOf(status));
        roleManager.add(role);
        return true;
    }

    public boolean editRole(String id, String name, String description, String status) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        role.setStatus(Integer.valueOf(status));
        roleManager.update(role);
        return true;
    }

    /**
     * 保存或者修改角色
     * 
     * @param objs
     * @return
     */
    public boolean addOrEditRole(JSONObject objs) {
        Role role = new Role();
        String id = objs.getString("id");
        role.setName(objs.getString("name"));
        role.setDescription(objs.getString("desc"));
        role.setStatus(objs.getInt("status"));
        role.setId(id);
        if (roleManager.saveOrUpdate(role)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteRole(String id) {

        Role role = roleManager.getRoleById(id);
        roleManager.delete(role);
        return true;

    }

    /**
     * 
     * @Description: 为角色授权限
     * @param roleId
     * @param privileges
     * @return
     */
    public Map<String, Object> saveRolePrivileges(String roleId, String privilegeIdsStr) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Role role = roleManager.getRoleById(roleId);
        if (role == null) {
            ret.put("success", false);
            ret.put("msg", "角色不存在");
            return ret;
        }
        List<Privilege> privileges = new ArrayList<Privilege>();
        String[] privilegeIds = privilegeIdsStr.split(",");

        //        rolePrivilegeManager.deleteByRoleId(roleId);
        //获取之前的角色权限
        List<RolePrivilege> rolePrivileges = rolePrivilegeManager.getRolePrivilegesFromMc(roleId);
        if (rolePrivileges != null && rolePrivileges.size() > 0) {
            for (RolePrivilege rolePrivilege : rolePrivileges) {
                rolePrivilegeManager.deleteRolePrivilege(rolePrivilege);
            }
        }
        for (String privilegeId : privilegeIds) {
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setRoleId(roleId);
            rolePrivilege.setPrivilegeId(privilegeId);
            rolePrivilegeManager.addRolePrivilege(rolePrivilege);
        }
        roleManager.saveOrUpdate(role);
        //更新缓存中该角色的用户
        List<Object> list = roleManager.getUsersByRole(role.getId());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                User user = userManager.getUser(String.valueOf(list.get(i)));
                userManager.getAllPrivilegeIds(user);
                userManager.updateUser(user);
            }
        }
        ret.put("success", true);
        ret.put("msg", "权限分配成功");
        return ret;
    }

    /**
     * 
     * @Description: 获取所有角色并且封装成树
     * @return
     */
    public List<ZTreeView> getRolesTree() {
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        List<Role> roles = roleManager.getRoleList(null, null, null);
        for (Role role : roles) {
            ZTreeView tree = new ZTreeView();
            tree.setId(role.getId());
            tree.setName(role.getName());
            tree.setOpen(true);
            tree.setpId("root");
            trees.add(tree);
        }
        return trees;
    }
}
