package org.tpri.sc.controller.uam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.uam.PrivilegeService;
import org.tpri.sc.service.uam.RoleService;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.ZTreeView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>角色控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月9日
 */
@Controller
@RequestMapping("/uam")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    /**
     * 加载角色列表
     */
    @RequestMapping("loadRole")
    @ResponseBody
    public Map<String, Object> loadRole(HttpServletRequest request) {
        logger.debug("RoleController loadRole begin");

        String searchName = getString(request, "search");
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        UserMc user = loadUserMc(request);
        List list = roleService.loadRole(searchName, start, limit);
        Integer total = roleService.loadRoleTotal(searchName);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        ret.put("total", total);

        logger.debug("RoleController loadRole end");
        return ret;
    }

    /**
     * 
     * @Description: 获取角色详情
     * @param request
     * @return
     */
    @RequestMapping("getRoleById")
    @ResponseBody
    public Map<String, Object> getRoleById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getRoleById begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        Role role = roleService.getRoleById(id);
        ret.put("item", role);
        logger.debug(this.getClass() + " getRoleById end");
        return ret;
    }

    /**
     * 新增角色
     */
    @RequestMapping("addRole")
    @ResponseBody
    public Map<String, Object> addRole(HttpServletRequest request) {
        logger.debug("RoleController addRole begin");
        String id = getString(request, "id");
        String name = getString(request, "name");
        String description = getString(request, "description");
        String status = getString(request, "status");

        roleService.addRole(id, name, description, status);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug("RoleController addRole end");
        return ret;
    }

    /**
     * 编辑角色
     */
    @RequestMapping("editRole")
    @ResponseBody
    public Map<String, Object> editRole(HttpServletRequest request) {
        logger.debug("RoleController editRole begin");
        String id = getString(request, "id");
        String name = getString(request, "name");
        String status = getString(request, "status");

        String description = getString(request, "description");

        roleService.editRole(id, name, description, status);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug("RoleController editRole end");
        return ret;
    }

    /**
     * 新增or编辑角色
     */
    @RequestMapping("addOrEditRole")
    @ResponseBody
    public Map<String, Object> addOrEditRole(HttpServletRequest request) {
        logger.debug("RoleController addOrEditRole begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        String id = getString(request, "id");
        if (id == null || "".equals(id)) {
            objs.put("id", UUIDUtil.id());
        } else {
            objs.put("id", getString(request, "id"));
        }
        objs.put("name", getString(request, "name"));
        objs.put("status", getString(request, "status"));
        objs.put("desc", getString(request, "desc"));
        if (roleService.addOrEditRole(objs)) {
            ret.put("success", true);
            ret.put("msg", "保存成功");
        } else {
            ret.put("success", false);
            ret.put("msg", "保存失败");
        }
        logger.debug("RoleController addOrEditRole end");
        return ret;
    }

    /**
     * 删除角色
     */
    @RequestMapping("delRole")
    @ResponseBody
    public Map<String, Object> delRole(HttpServletRequest request) {
        logger.debug("RoleController delRole begin");
        String id = getString(request, "userId");
        Map<String, Object> ret = new HashMap<String, Object>();
        try {
            roleService.deleteRole(id);
            ret.put("success", true);
            ret.put("msg", "保存成功");
            logger.debug("RoleController delRole end");
            return ret;

        } catch (Exception e) {
            ret.put("success", false);
            ret.put("msg", "删除失败！");
            logger.debug("RoleController delRole exception", e);
            return ret;

        }
    }

    /**
     * 
     * @Description: 角色分配权限
     * @param request
     * @return
     */
    @RequestMapping("saveRolePrivileges")
    @ResponseBody
    public Map<String, Object> saveRolePrivileges(HttpServletRequest request) {
        logger.debug(this.getClass() + " saveRolePrivileges begin");
        String roleId = getString(request, "roleId");
        String privileges = getString(request, "privileges");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = roleService.saveRolePrivileges(roleId, privileges);
        logger.debug(this.getClass() + "saveRolePrivileges end");
        return ret;
    }

    /**
     * 
     * @Description: 获得角色树
     * @param request
     * @return
     */
    @RequestMapping("getRolesTree")
    @ResponseBody
    public Map<String, Object> getRolesTree(HttpServletRequest request) {
        logger.debug(this.getClass() + " getRolesTree begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ZTreeView> rolesTree = roleService.getRolesTree();
        ret.put("items", rolesTree);
        logger.debug(this.getClass() + " getRolesTree end");
        return ret;
    }
}
