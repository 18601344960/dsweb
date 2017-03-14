package org.tpri.sc.controller.obt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.obt.ConferenceCategory;
import org.tpri.sc.entity.obt.ConferenceLabel;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.obt.ConferenceCategoryService;
import org.tpri.sc.service.obt.ConferenceLabelService;
import org.tpri.sc.view.ZTreeView;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>文章类别控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月24日
 */
@Controller
@RequestMapping("/obt")
public class ConferenceCategoryController extends BaseController {

    @Autowired
    private ConferenceCategoryService conferenceCategoryService;
    @Autowired
    private ConferenceLabelService conferenceLabelService;

    /**
     * <B>方法名称：</B>新增标签<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param request
     * @return
     */
    @RequestMapping("addConferenceLabel")
    @ResponseBody
    public Map<String, Object> addConferenceLabel(HttpServletRequest request) {
        logger.debug(this.getClass() + " addConferenceLabel begin");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name", getString(request, "name"));
        param.put("parentId", getString(request, "parentId"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("description", getString(request, "description"));
        param.put("type", getInteger(request, "type"));

        UserMc user = loadUserMc(request);
        boolean result = conferenceCategoryService.addLabel(user, param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        if (result) {
            ret.put("msg", "保存成功");
        } else {
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + " addConferenceLabel begin");
        return ret;
    }

    /**
     * <B>方法名称：</B>修改标签<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param request
     * @return
     */
    @RequestMapping("updateConferenceLabel")
    @ResponseBody
    public Map<String, Object> updateConferenceLabel(HttpServletRequest request) {
        logger.debug(this.getClass() + " updateConferenceLabel begin");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", getString(request, "id"));
        param.put("name", getString(request, "name"));
        param.put("orderNo", getInt(request, "orderNo"));
        param.put("description", getString(request, "description"));
        UserMc user = loadUserMc(request);
        boolean result = conferenceCategoryService.updateLabel(user, param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        if (result) {
            ret.put("msg", "保存成功");
        } else {
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + " updateConferenceLabel begin");
        return ret;
    }

    /**
     * <B>方法名称：</B>根据ID获取类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceCategoryById")
    @ResponseBody
    public Map<String, Object> getConferenceCategoryById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceCategoryById begin");
        String id = getString(request, "id");
        ConferenceCategory category = conferenceCategoryService.getConferenceCategoryById(id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", category);
        logger.debug(this.getClass() + " getConferenceCategoryById end");
        return ret;
    }

    /**
     * <B>方法名称：</B>根据ID删除类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param request
     * @return
     */
    @RequestMapping("deleteConferenceCategory")
    @ResponseBody
    public Map<String, Object> deleteConferenceCategory(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteConferenceCategory begin");
        String id = getString(request, "id");
        UserMc user = loadUserMc(request);
        boolean result = conferenceCategoryService.deleteConferenceCategory(user, id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        logger.debug(this.getClass() + " deleteConferenceCategory begin");
        return ret;
    }
    /**
     * 
     * <B>方法名称：</B>删除含有关联的标签<BR>
     * <B>概要说明：</B><BR>
     * @author 刘佳丽
     * @since 2016年7月14日 	
     * @param request
     * @return
     */
    @RequestMapping("deleteConferenceCategoryAndArtical")
    @ResponseBody
    public Map<String,Object>deleteConferenceCategoryAndArtical(HttpServletRequest request){
    	logger.debug(this.getClass() + " deleteConferenceCategoryAndArtical begin");
    	String id = getString(request, "id");
    	boolean result = conferenceCategoryService.deleteConferenceCategoryAndArtical(id);
    	Map<String,Object> ret = new HashMap<String, Object>();
    	ret.put("success", result);
    	logger.debug(this.getClass() + " deleteConferenceCategoryAndArtical end");
    	return ret;
    }
    /**
     * 
     * <B>方法名称：</B>根据标签ID查询是否存在关联文章<BR>
     * <B>概要说明：</B><BR>
     * @author 刘佳丽
     * @since 2016年7月14日 	
     * @param request
     * @return
     */
    @RequestMapping("getConferenceLabelByCategoryId")
    @ResponseBody
    public Map<String, Object> getConferenceLabelByCategoryId(HttpServletRequest request){
    	logger.debug(this.getClass() + " getConferenceLabelByCategoryId begin");
    	String categoryId = getString(request, "id");
    	List<ConferenceLabel> list=conferenceLabelService.getConferenceLabelByCategoryId(categoryId);
    	Map<String,Object> ret = new HashMap<String, Object>();
    	if(list.size()==0){
    		ret.put("success", true);
    	}
    	logger.debug(this.getClass() + " getConferenceLabelByCategoryId end");
    	return ret;
    }

    /**
     * <B>方法名称：</B>获取支部工作步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceStepList")
    @ResponseBody
    public Map<String, Object> getConferenceStepList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceStepList begin");
        List<ConferenceCategory> list = conferenceCategoryService.getStepList();
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        logger.debug(this.getClass() + " getConferenceStepList end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取支部组织生活形式<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月12日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceFormatList")
    @ResponseBody
    public Map<String, Object> getConferenceFormatList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceFormatList begin");
        List<ConferenceCategory> list = conferenceCategoryService.getFormatList();
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        logger.debug(this.getClass() + " getConferenceFormatList end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取本组织的所有标签的标签树<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月13日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceLabelZTree")
    @ResponseBody
    public Map<String, Object> getConferenceLabelZTree(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceLabelZTree begin");
        String ccpartyId = getString(request, "ccpartyId");
        List<ZTreeView> tree = conferenceCategoryService.getLabelZTree(ccpartyId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", tree);
        logger.debug(this.getClass() + " getConferenceLabelZTree end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取某组织标签列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceLabelList")
    @ResponseBody
    public Map<String, Object> getConferenceLabelList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceLabelList begin");

        boolean showRoot = getBoolean(request, "showRoot");
        String ccpartyId = getString(request, "ccpartyId");
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        List<ConferenceCategory> list = conferenceCategoryService.getLabelList(ccpartyId, showRoot, start, limit);
        Integer total = conferenceCategoryService.getLabelTotal(ccpartyId, showRoot);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        ret.put("total", total);
        logger.debug(this.getClass() + " getConferenceLabelList end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的步骤、形式、内容分类<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月21日
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyConferenceCategorys")
    @ResponseBody
    public Map<String, Object> getCcpartyConferenceCategorys(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyConferenceCategorys begin");
        String ccpartyId = getString(request, "ccpartyId");
        List<ConferenceCategory> steps = conferenceCategoryService.getStepList();
        List<ConferenceCategory> formats = conferenceCategoryService.getFormatList();
        List<ConferenceCategory> labels = conferenceCategoryService.getLabelList(ccpartyId, false, null, null);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("steps", steps);
        ret.put("formats", formats);
        ret.put("labels", labels);
        logger.debug(this.getClass() + " getCcpartyConferenceCategorys end");
        return ret;
    }
    
}
