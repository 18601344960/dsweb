package org.tpri.sc.controller.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.org.CcpartyGroup;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.service.org.CcpartyGroupService;
import org.tpri.sc.view.ZTreeView;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党小组控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年8月1日
 */
@Controller
@RequestMapping("/org")
public class CcpartyGroupController extends BaseController {

    @Autowired
    private CcpartyGroupService ccpartyGroupService;
    @Autowired
    private CCpartyService ccpartyService;

    /**
     * <B>方法名称：</B>根据组织ID加载党小组列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyGroupList")
    @ResponseBody
    public Map<String, Object> getCcpartyGroupList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyGroupList begin");

        String searchName = getString(request, "search");
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String ccpartyId = getString(request, "ccpartyId");
        List<CcpartyGroup> groups = new ArrayList<CcpartyGroup>();
        if (ccpartyId != null) {
            groups = ccpartyGroupService.getCcpartyGroupList(start, limit, ccpartyId, searchName);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", groups);
        logger.debug(this.getClass() + " getCcpartyGroupList end");
        return ret;
    }

    /**
     * <B>方法名称：</B>根据ID获取党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyGroupById")
    @ResponseBody
    public Map<String, Object> getCcpartyGroupById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyGroupById begin");
        String id = getString(request, "id");
        Map<String, Object> ret = new HashMap<String, Object>();
        CcpartyGroup ccpartyGroup = ccpartyGroupService.getCcpartyGroupById(id);
        ret.put("item", ccpartyGroup);
        logger.debug(this.getClass() + " getCcpartyGroupById end");
        return ret;
    }

    /**
     * <B>方法名称：</B>根据党组织Id获取所属党小组树<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyGroupsTree")
    @ResponseBody
    public Map<String, Object> getCcpartyGroupsTree(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyGroupsTree begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        List<ZTreeView> tree = new ArrayList<ZTreeView>();
        if (!StringUtils.isEmpty(ccpartyId)) {
            tree = ccpartyGroupService.getGroupsTreeByCcparty(ccpartyId);
        }
        ret.put("items", tree);
        logger.debug(this.getClass() + " getCcpartyGroupsTree end");
        return ret;
    }

    /**
     * <B>方法名称：</B>新增党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     */
    @RequestMapping("addCcpartyGroup")
    @ResponseBody
    public Map<String, Object> addCcpartyGroup(HttpServletRequest request) {
        logger.debug(this.getClass() + " addCcpartyGroup begin");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("name", getString(request, "name"));
        param.put("description", getString(request, "description"));
        param.put("sequence", getInt(request, "sequence", 100));
        Map<String, Object> ret = new HashMap<String, Object>();
        boolean result = ccpartyGroupService.addCcpartyGroup(loadUserMc(request), param);
        if (result) {
            ret.put("success", true);
            ret.put("msg", "保存成功！");
        } else {
            ret.put("success", false);
            ret.put("msg", "抱歉，保存失败，请稍后再试！");
        }
        logger.debug(this.getClass() + " addCcpartyGroup end");
        return ret;
    }

    /**
     * <B>方法名称：</B>新增党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     */
    @RequestMapping("updateCcpartyGroup")
    @ResponseBody
    public Map<String, Object> updateCcpartyGroup(HttpServletRequest request) {
        logger.debug(this.getClass() + " updateCcpartyGroup begin");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", getString(request, "id"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("name", getString(request, "name"));
        param.put("description", getString(request, "description"));
        param.put("sequence", getInt(request, "sequence", 100));
        Map<String, Object> ret = new HashMap<String, Object>();
        boolean result = ccpartyGroupService.updateCcpartyGroup(loadUserMc(request), param);
        if (result) {
            ret.put("success", true);
            ret.put("msg", "保存成功！");
        } else {
            ret.put("success", false);
            ret.put("msg", "抱歉，保存失败，请稍后再试！");
        }
        logger.debug(this.getClass() + " updateCcpartyGroup end");
        return ret;
    }

    /**
     * <B>方法名称：</B>删除党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     */
    @RequestMapping("deleteCcpartyGroup")
    @ResponseBody
    public Map<String, Object> deleteCcpartyGroup(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteCcpartyGroup begin");
        String id = getString(request, "id");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = ccpartyGroupService.deleteCcpartyGroup(id);
        logger.debug(this.getClass() + " deleteCcpartyGroup end");
        return ret;
    }
}
