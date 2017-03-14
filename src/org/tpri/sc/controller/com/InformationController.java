package org.tpri.sc.controller.com;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.com.Information;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.FileService;
import org.tpri.sc.service.com.InformationService;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.view.com.HomeStatisticsNumView;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>工作必备控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */
@Controller
@RequestMapping("/com")
public class InformationController extends BaseController {

    @Autowired
    private InformationService informationService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CCpartyService ccpartyService;

    /**
     * <B>方法名称：</B>获取发布的工作必备列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("getShowInformationList")
    @ResponseBody
    public Map<String, Object> getShowInformationList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getShowInformationList begin");
        Integer category = getInteger(request, "category");
        String ccpartyId = getString(request, "ccpartyId");
        String search = getString(request, "search");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        List<Information> list = informationService.getInformationList(category, Information.STATUS_1, ccpartyId, search, offset, limit, true);
        int total = informationService.getInformationTotal(category, Information.STATUS_1, ccpartyId, search, true);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        ret.put("total", total);
        logger.debug(this.getClass() + " getInformationList end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取本组织的工作必备列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月23日
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyInformationList")
    @ResponseBody
    public Map<String, Object> getCcpartyInformationList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyInformationList begin");
        Integer category = getInteger(request, "category");
        String ccpartyId = getString(request, "ccpartyId");
        String search = getString(request, "search");
        Integer status = getInteger(request, "status");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        List<Information> list = informationService.getInformationList(category, status, ccpartyId, search, offset, limit, false);
        int total = informationService.getInformationTotal(category, status, ccpartyId, search, false);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        ret.put("total", total);
        logger.debug(this.getClass() + " getCcpartyInformationList end");
        return ret;
    }

    /**
     * <B>方法名称：</B>根据ID获取工作必备<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("getInformationById")
    @ResponseBody
    public Map<String, Object> getInformationById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getInformationById begin");
        String id = getString(request, "id");
        boolean isView = getBoolean(request, "isView");
        Information information = informationService.getInformationById(id);
        if (isView) {
            informationService.updateHits(id);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", information);
        logger.debug(this.getClass() + " getInformationById end");
        return ret;
    }

    /**
     * <B>方法名称：</B>新增工作必备<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("addInformation")
    @ResponseBody
    public Map<String, Object> addInformation(HttpServletRequest request) {
        logger.debug(this.getClass() + " addInformation begin");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("category", getInteger(request, "category"));
        param.put("type", getInteger(request, "type") == null ? Information.TYPE_1 : getInteger(request, "type"));
        param.put("name", getString(request, "name"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("content", getString(request, "content"));
        param.put("files", getString(request, "files"));

        UserMc user = loadUserMc(request);
        boolean result = informationService.addInformation(user, param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        ret.put("msg", "保存成功");
        logger.debug(this.getClass() + " addInformation end");
        return ret;
    }

    /**
     * <B>方法名称：</B>编辑工作必备<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("updateInformation")
    @ResponseBody
    public Map<String, Object> updateInformation(HttpServletRequest request) {
        logger.debug(this.getClass() + " updateInformation begin");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", getString(request, "id"));
        param.put("type", getInteger(request, "type") == null ? Information.TYPE_1 : getInteger(request, "type"));
        param.put("category", getInteger(request, "category"));
        param.put("name", getString(request, "name"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("content", getString(request, "content"));
        param.put("files", getString(request, "files"));

        UserMc user = loadUserMc(request);
        boolean result = informationService.updateInformation(user, param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        ret.put("msg", "保存成功");
        logger.debug(this.getClass() + " updateInformation end");
        return ret;
    }

    /**
     * <B>方法名称：</B>删除工作必备<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("deleteInformation")
    @ResponseBody
    public Map<String, Object> deleteInformation(HttpServletRequest request) {
        logger.debug("InformationController deleteInformation begin");
        String id = getString(request, "id");
        UserMc user = loadUserMc(request);
        informationService.deleteInformation(user, id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "删除成功");
        logger.debug("InformationController deleteInformation end");
        return ret;
    }

    /**
     * <B>方法名称：</B>发布工作必备<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 张波
     * @since 2016年7月25日
     * @param request
     * @return
     */
    @RequestMapping("publishInformation")
    @ResponseBody
    public Map<String, Object> publishInformation(HttpServletRequest request) {
        logger.debug("InformationController publishInformation begin");
        String id = getString(request, "id");
        UserMc user = loadUserMc(request);
        informationService.publishInformation(user, id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "发布成功");
        logger.debug("InformationController publishInformation end");
        return ret;
    }

    /**
     * <B>方法名称：</B>取消发布工作必备<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 张波
     * @since 2016年7月25日
     * @param request
     * @return
     */
    @RequestMapping("unpublishInformation")
    @ResponseBody
    public Map<String, Object> unpublishInformation(HttpServletRequest request) {
        logger.debug("InformationController unpublishInformation begin");
        String id = getString(request, "id");
        UserMc user = loadUserMc(request);
        informationService.unpublishInformation(user, id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "取消发布成功");
        logger.debug("InformationController unpublishInformation end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取首页统计数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月4日
     * @param request
     * @return
     */
    @RequestMapping("getHomeStatisticsNums")
    @ResponseBody
    public Map<String, Object> getHomeStatisticsNums(HttpServletRequest request) {
        logger.debug("getHomeStatisticsNums deleteInformation begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        HomeStatisticsNumView view = informationService.getHomeStatisticsNums(ccpartyId);
        ret.put("item", view);
        logger.debug("getHomeStatisticsNums deleteInformation end");
        return ret;
    }
}
