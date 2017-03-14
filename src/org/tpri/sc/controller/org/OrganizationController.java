package org.tpri.sc.controller.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.org.Organization;
import org.tpri.sc.entity.sys.EnvironmentId;
import org.tpri.sc.service.org.OrganizationService;
import org.tpri.sc.view.ZTreeView;

/**
 * 
 * <B>系统名称：</B>支部手册<BR>
 * <B>模块名称：</B>行政组织<BR>
 * <B>中文类名：</B>行政组织控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年11月4日
 */
@Controller
@RequestMapping("/org")
public class OrganizationController extends BaseController {

    @Resource(name = "OrganizationService")
    private OrganizationService organizationService;

    /**
     * 
     * @Description: 获取行政单位树 查询当前组织和所有下级组织 注1、不包含平级组织 2、包含当前组织下的所有子节点，包括子节点和孙子节点
     * @author: 赵子靖
     * @since: 2015年9月10日 上午10:09:20
     * @param request
     * @return
     */
    @RequestMapping("getCurrentOrganizationAndSunsToTreeView")
    @ResponseBody
    public Map<String, Object> getCurrentOrganizationAndSunsToTreeView(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCurrentOrganizationAndSunsToTreeView begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        String organizationId = getString(request, "organizationId");
        trees = organizationService.getCurrentOrganizationAndSunsToTreeView(organizationId);
        ret.put("items", trees);
        logger.debug(this.getClass() + " getCurrentOrganizationAndSunsToTreeView end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>当前系统跟行政单位和所有下级行政单位<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年9月23日
     * @param request
     * @return
     */
    @RequestMapping("getRootOrganizationAndChildren")
    @ResponseBody
    public Map<String, Object> getRootOrganizationAndChildren(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCurrentOrganizationAndSunsToTreeView begin");
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        String organizationId = (String) getEnvironmentValueById(EnvironmentId.INITIAL_ORGANIZATIONID);
        if (!StringUtils.isEmpty(organizationId)) {
            trees = organizationService.getCurrentOrganizationAndSunsToTreeView(organizationId);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", trees);
        logger.debug(this.getClass() + " getCurrentOrganizationAndSunsToTreeView end");
        return ret;
    }

    /**
     * 新增组织
     */
    @RequestMapping("addOrganization")
    @ResponseBody
    public Map<String, Object> addOrganization(HttpServletRequest request) {
        logger.debug("OrganizationController addOrganization begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject json = new JSONObject();
        json.put("name", getString(request, "name")); //单位名称
        json.put("address", getString(request, "address")); //单位地址
        json.put("perresentative", getString(request, "perresentative")); //法定代表人
        json.put("parentId", getString(request, "parentId")); //上级行政单位
        json.put("description", getString(request, "description")); //描述
        json.put("status", getString(request, "status")); //状态

        organizationService.addOrganization(loadUserMc(request), json);
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug("OrganizationController addOrganization end");
        return ret;
    }

    /**
     * 编辑用户
     */
    @RequestMapping("editOrganization")
    @ResponseBody
    public Map<String, Object> editOrganization(HttpServletRequest request) {
        logger.debug("OrganizationController editOrganization begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject json = new JSONObject();
        json.put("id", getString(request, "id")); //主键
        json.put("name", getString(request, "name")); //单位名称
        json.put("address", getString(request, "address")); //单位地址
        json.put("perresentative", getString(request, "perresentative")); //法定代表人
        json.put("parentId", getString(request, "parentId")); //上级行政单位
        json.put("description", getString(request, "description")); //描述
        json.put("status", getString(request, "status")); //状态
        organizationService.editOrganization(loadUserMc(request), json);
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug("OrganizationController editOrganization end");
        return ret;
    }

    /**
     * 删除组织
     */
    @RequestMapping("delOrganization")
    @ResponseBody
    public Map<String, Object> delOrganization(HttpServletRequest request) {
        logger.debug("OrganizationController editOrganization begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        organizationService.deleteOrganization(id);
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug("OrganizationController editOrganization end");
        return ret;
    }

    /**
     * 
     * @Description: 新增或者保存行政组织
     * @param request
     * @return
     */
    @RequestMapping("saveOrUpdateOrganization")
    @ResponseBody
    public Map<String, Object> saveOrUpdateOrganization(HttpServletRequest request) {
        logger.debug(this.getClass() + " saveOrUpdateOrganization begin!");
        Map<String, Object> ret = new HashMap<String, Object>();
        Map<String, Object> paramter = new HashMap<String, Object>();
        paramter.put("id", getString(request, "id"));
        paramter.put("sequence", getInteger(request, "sequence"));
        paramter.put("name", getString(request, "name"));
        paramter.put("parentId", getString(request, "parentId"));
        paramter.put("representative", getString(request, "representative"));
        paramter.put("address", getString(request, "address"));
        paramter.put("status", getInt(request, "status"));
        ret = organizationService.saveOrUpdateOrganization(loadUserMc(request), paramter);
        logger.debug(this.getClass() + " saveOrUpdateOrganization end!");
        return ret;
    }

    /**
     * 
     * @Description: 根据ID获取行政单位详细信息
     * @author: 赵子靖
     * @since: 2015年9月10日 上午10:05:52
     * @param request
     * @return
     */
    @RequestMapping("getOrganizationById")
    @ResponseBody
    public Map<String, Object> getOrganizationById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getOrganizationById begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String organizationId = getString(request, "organizationId");
        Organization organization = organizationService.getOrganization(organizationId);
        Organization parentOrganization = organizationService.getOrganization(organization.getParentId());
        ret.put("item", organization);
        ret.put("parent", parentOrganization);
        logger.debug(this.getClass() + " getOrganizationById begin");
        return ret;
    }

    /**
     * 
     * @Description:验证党组织ID是否被占用
     * @param request
     * @return
     */
    @RequestMapping("checkOrganizationId")
    @ResponseBody
    public Map<String, Object> checkOrganizationId(HttpServletRequest request) {
        logger.debug(this.getClass() + " checkOrganizationId begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String checkId = getString(request, "checkId");
        ret = organizationService.checkOrganizationId(checkId);
        logger.debug(this.getClass() + " checkOrganizationId begin");
        return ret;
    }

}
