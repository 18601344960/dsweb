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
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.service.uam.SystemUserService;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>系统用户控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年1月11日
 */
@Controller
@RequestMapping("/uam")
public class SystemUserController extends BaseController {

    @Autowired
    private SystemUserService systemUserService;

    /**
     * <B>方法名称：</B>获取系统用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月11日
     * @param request
     * @return
     */
    @RequestMapping("getSystemUsers")
    @ResponseBody
    public Map<String, Object> getSystemUsers(HttpServletRequest request) {
        logger.debug(this.getClass() + " getSystemUsers begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String search = getString(request, "search");
        String ccpartyId = getString(request, "ccpartyId");
        List<User> partyWorkers = systemUserService.getSystemUsers(search, offset, limit,ccpartyId);
        Integer total = systemUserService.getSystemUsersTotal(search,ccpartyId);
        ret.put("rows", partyWorkers);
        ret.put("total", total);
        logger.debug(this.getClass() + " getSystemUsers end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>新增系统用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月12日
     * @param request
     * @return
     */
    @RequestMapping("addSystemUser")
    @ResponseBody
    public Map<String, Object> addSystemUser(HttpServletRequest request) {
        logger.debug(this.getClass() + " addSystemUser begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("loginNo", getString(request, "loginNo"));
        objs.put("name", getString(request, "name"));
        objs.put("gender", getInteger(request, "gender"));
        objs.put("ccpartyId", getString(request, "ccpartyId"));
        ret = systemUserService.addSystemUser(loadUserMc(request),objs);
        logger.debug(this.getClass() + " addSystemUser end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>修改系统用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月12日
     * @param request
     * @return
     */
    @RequestMapping("updateSystemUser")
    @ResponseBody
    public Map<String, Object> updateSystemUser(HttpServletRequest request) {
        logger.debug(this.getClass() + " updateSystemUser begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("id", getString(request, "id"));
        objs.put("name", getString(request, "name"));
        objs.put("gender", getInteger(request, "gender"));
        objs.put("ccpartyId", getString(request, "ccpartyId"));
        ret = systemUserService.updateSystemUser(loadUserMc(request), objs);
        logger.debug(this.getClass() + " updateSystemUser end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取系统用户详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月12日
     * @param request
     * @return
     */
    @RequestMapping("getSystemUserById")
    @ResponseBody
    public Map<String, Object> getSystemUserById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getSystemUserById begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = systemUserService.getSystemUserById(getString(request, "id"));
        ret.put("item", user);
        logger.debug(this.getClass() + " getSystemUserById end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>重置系统用户角色<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年1月12日 	
     * @param request
     * @return
     */
    @RequestMapping("resetSystemUserRole")
    @ResponseBody
    public Map<String, Object> resetSystemUserRole(HttpServletRequest request) {
        logger.debug(this.getClass() + " resetSystemUserRole begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = systemUserService.resetSystemUserRole(getString(request, "id"));
        logger.debug(this.getClass() + " resetSystemUserRole end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B>获取下级系统用户列表<BR>
     * @author 易文俊
     * @since 2016年3月29日    
     * @param request
     * @return
     */
    @RequestMapping("getChildSystemUsers")
    @ResponseBody
    public Map<String, Object> getChildSystemUsers(HttpServletRequest request) {
        logger.debug(this.getClass() + " getChildSystemUsers begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String search = getString(request, "search");
        String ccpartyId = getString(request, "ccpartyId");
        List<User> partyWorkers = systemUserService.getChildSystemUsers(search, offset, limit,ccpartyId);
        Integer total = systemUserService.getChildSystemUsersTotal(search,ccpartyId);
        ret.put("rows", partyWorkers);
        ret.put("total", total);
        logger.debug(this.getClass() + " getChildSystemUsers end");
        return ret;
    }
}
