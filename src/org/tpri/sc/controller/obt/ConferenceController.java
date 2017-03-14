package org.tpri.sc.controller.obt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.obt.Conference;
import org.tpri.sc.entity.obt.ConferenceFormat;
import org.tpri.sc.entity.obt.ConferenceLabel;
import org.tpri.sc.entity.obt.ConferenceStep;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.com.ExportStatistics;
import org.tpri.sc.service.obt.ConferenceService;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.view.com.ConferenceCcpartyView;
import org.tpri.sc.view.com.QueryResultConference;

/**
 * 
 * <B>系统名称：</B>支部手册<BR>
 * <B>模块名称：</B>组织生活<BR>
 * <B>中文类名：</B>组织生活控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月2日
 */

@Controller
@RequestMapping("/obt")
public class ConferenceController extends BaseController {

    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private ExportStatistics exportStatistics;
    @Autowired
    private CCpartyService cCpartyService;

    /**
     * <B>方法名称：</B>工作共享列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月13日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceListForShare")
    @ResponseBody
    public Map<String, Object> getConferenceListForShare(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceListForShare begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("label", getString(request, "label")); // 组织生活内容
        objs.put("step", getString(request, "step"));//组织生活所在工作步骤
        objs.put("format", getString(request, "format"));//组织生活形式
        objs.put("currentCcpartyId", getString(request, "currentCcpartyId"));
        objs.put("ccpartyId", getString(request, "ccpartyId")); // 所选组织
        objs.put("beginTime", getString(request, "beginTime"));
        objs.put("endTime", getString(request, "endTime"));
        objs.put("name", getString(request, "name"));
        objs.put("sourceType", getString(request, "sourceType"));
        objs.put("brandType", getString(request, "brandType"));
        objs.put("isRecommend", getString(request, "isRecommend"));
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");

        List<Conference> articles = conferenceService.getConferenceListForShare(offset, limit, objs);
        Integer total = conferenceService.getConferencesTotalForShare(objs);
        ret.put("rows", articles);
        ret.put("total", total);
        logger.debug(this.getClass() + " getConferenceListForShare end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取本组织的工作品牌<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月30日
     * @param request
     * @return
     */
    @RequestMapping("getWorkBrandsOfCcparty")
    @ResponseBody
    public Map<String, Object> getWorkBrandsOfCcparty(HttpServletRequest request) {
        logger.debug(this.getClass() + " getWorkBrandsOfCcparty begin");
        Map<String, Object> ret = new HashMap<String, Object>();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name", getString(request, "name"));
        param.put("label", getString(request, "label"));
        param.put("step", getString(request, "step"));
        param.put("format", getString(request, "format"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("beginTime", getString(request, "beginTime"));
        param.put("endTime", getString(request, "endTime"));
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");

        List<Conference> articles = conferenceService.getWorkBrandsOfCcparty(offset, limit, param);
        Integer total = conferenceService.getWorkBrandsOfCcpartyTotal(param);
        ret.put("rows", articles);
        ret.put("total", total);
        logger.debug(this.getClass() + " getWorkBrandsOfCcparty end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取我的组织生活列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param offset
     * @param limit
     * @param userId
     * @return
     */
    @RequestMapping("getMyConferenceList")
    @ResponseBody
    public Map<String, Object> getMyConferenceList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getMyConferenceList begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String beginTime = getString(request, "beginTime");
        String endTime = getString(request, "endTime");
        String searchKey = getString(request, "searchKey");
        UserMc user = loadUserMc(request);
        List<Conference> articles = conferenceService.getMyConferenceList(offset, limit, user.getId(), beginTime, endTime, searchKey);
        Integer total = conferenceService.getMyConferencesTotal(user.getId(), beginTime, endTime, searchKey);
        ret.put("rows", articles);
        ret.put("total", total);
        logger.debug(this.getClass() + " getMyConferenceList end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的组织生活列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param userId
     * @return
     */
    @RequestMapping("getCcpartyConferenceList")
    @ResponseBody
    public Map<String, Object> getCcpartyConferenceList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyConferenceList begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String ccpartyId = getString(request, "ccpartyId");
        String paramter = getString(request, "paramter");
        List<Conference> articles = conferenceService.getCcpartyConferenceList(offset, limit, ccpartyId, paramter);
        Integer total = conferenceService.getCcpartyConferenceTotal(ccpartyId, paramter);
        ret.put("rows", articles);
        ret.put("total", total);
        logger.debug(this.getClass() + " getCcpartyConferenceList end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>根据ID获取组织生活详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceById")
    @ResponseBody
    public Map<String, Object> getConferenceById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceById begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        boolean isView = getBoolean(request, "isView");
        Conference article = conferenceService.getConferenceById(id);
        if (isView) {
            conferenceService.updateHits(id);
        }
        ret.put("item", article);
        logger.debug(this.getClass() + " getConferenceById end");
        return ret;
    }

    /**
     * <B>方法名称：</B>新增组织生活<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param request
     * @return
     */
    @RequestMapping("addConference")
    @ResponseBody
    public Map<String, Object> addConference(HttpServletRequest request) {
        logger.debug(this.getClass() + " addConference begin");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name", getString(request, "name"));
        param.put("occurTime", getString(request, "occurTime"));
        param.put("address", getString(request, "address"));
        param.put("attendance", getInt(request, "attendance"));
        param.put("content", getString(request, "content"));
        param.put("stepIds", getString(request, "stepIds"));
        param.put("formatIds", getString(request, "formatIds"));
        param.put("labelIds", getString(request, "labelIds"));
        param.put("files", getString(request, "files"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("sourceType", getInt(request, "sourceType"));
        param.put("sourceId", getString(request, "sourceId"));
        param.put("sourceName", getString(request, "sourceName"));
        param.put("secretLevel", getInt(request, "secretLevel", Conference.SECRET_LEVEL_1));
        param.put("orgnizerUserIds", getString(request, "orgnizerUserIds"));
        param.put("orgnizerUserNames", getString(request, "orgnizerUserNames"));
        param.put("participantUserIds", getString(request, "participantUserIds"));
        param.put("participantUserNames", getString(request, "participantUserNames"));
        param.put("status", getInt(request, "status"));

        UserMc user = loadUserMc(request);
        String conferenceId = conferenceService.addConference(user, param);

        Map<String, Object> ret = new HashMap<String, Object>();
        if (conferenceId != null && !conferenceId.equals("")) {
            ret.put("success", true);
            ret.put("msg", "保存成功。");
            ret.put("conferenceId", conferenceId);
        } else {
            ret.put("success", false);
            ret.put("success", "保存失败。");
        }
        logger.debug(this.getClass() + " addConference begin");
        return ret;
    }

    /**
     * <B>方法名称：</B>修改组织生活<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param request
     * @return
     */
    @RequestMapping("editConference")
    @ResponseBody
    public Map<String, Object> editConference(HttpServletRequest request) {
        logger.debug(this.getClass() + " editConference begin");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", getString(request, "id"));
        param.put("name", getString(request, "name"));
        param.put("occurTime", getString(request, "occurTime"));
        param.put("address", getString(request, "address"));
        param.put("attendance", getInt(request, "attendance"));
        param.put("content", getString(request, "content"));
        param.put("stepIds", getString(request, "stepIds"));
        param.put("formatIds", getString(request, "formatIds"));
        param.put("labelIds", getString(request, "labelIds"));
        param.put("files", getString(request, "files"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("sourceType", getInt(request, "sourceType"));
        param.put("sourceId", getString(request, "sourceId"));
        param.put("sourceName", getString(request, "sourceName"));
        param.put("secretLevel", getInt(request, "secretLevel", Conference.SECRET_LEVEL_1));
        param.put("status", getInt(request, "status"));

        UserMc user = loadUserMc(request);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = conferenceService.editConference(user, param);
        logger.debug(this.getClass() + " editConference begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>刪除组织生活<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param request
     * @return
     */
    @RequestMapping("deleteConference")
    @ResponseBody
    public Map<String, Object> deleteConference(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteConference begin");

        String ids = getString(request, "ids");
        JSONArray idsArray = JSONArray.fromObject(ids);
        conferenceService.deleteConference(loadUserMc(request), idsArray);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "删除成功");
        logger.debug(this.getClass() + " deleteConference begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>将组织生活置成推荐<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param request
     * @return
     */
    @RequestMapping("recommendConference")
    @ResponseBody
    public Map<String, Object> recommendConference(HttpServletRequest request) {
        logger.debug(this.getClass() + " recommendConference begin");

        String articleId = getString(request, "articleId");
        conferenceService.editRecommend(loadUserMc(request), articleId, Conference.RECOMMEND_YES);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "推荐成功");
        logger.debug(this.getClass() + " recommendConference begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>取消组织生活推荐<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param request
     * @return
     */
    @RequestMapping("cancelRecommendConference")
    @ResponseBody
    public Map<String, Object> cancelRecommendConference(HttpServletRequest request) {
        logger.debug(this.getClass() + " cancelRecommendConference begin");

        String articleId = getString(request, "articleId");
        conferenceService.editRecommend(loadUserMc(request), articleId, Conference.RECOMMEND_NO);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "推荐成功");
        logger.debug(this.getClass() + " cancelRecommendConference begin");
        return ret;
    }

    /**
     * <B>方法名称：</B>发布<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("publishConference")
    @ResponseBody
    public Map<String, Object> publishConference(HttpServletRequest request) {
        logger.debug(this.getClass() + " publishConference begin");
        String ids = getString(request, "ids");
        UserMc user = loadUserMc(request);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = conferenceService.publishConference(user, ids);
        logger.debug(this.getClass() + " publishConference begin");
        return ret;
    }

    /**
     * <B>方法名称：</B>取消发布<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("unpublishConference")
    @ResponseBody
    public Map<String, Object> unpublishConference(HttpServletRequest request) {
        logger.debug(this.getClass() + " unpublishConference begin");
        String ids = getString(request, "ids");
        UserMc user = loadUserMc(request);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = conferenceService.unpublishConference(user, ids);
        logger.debug(this.getClass() + " unpublishConference begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>统计与分析数据获取<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceStepsStatistics")
    @ResponseBody
    public Map<String, Object> getConferenceStepsStatistics(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceStepsStatistics begin");
        String ccpartyId = getString(request, "ccpartyId"); // 组织
        String beginDate = getString(request, "beginDate");
        String endDate = getString(request, "endDate");
        List<QueryResultConference> list = conferenceService.getConferenceStepsStatistics(ccpartyId, beginDate, endDate);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        logger.debug(this.getClass() + " getConferenceStepsStatistics end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>统计与分析数据获取<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceFormartStatistics")
    @ResponseBody
    public Map<String, Object> getConferenceFormartStatistics(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceFormartStatistics begin");
        String ccpartyId = getString(request, "ccpartyId"); // 组织
        String beginDate = getString(request, "beginDate");
        String endDate = getString(request, "endDate");
        List<QueryResultConference> list = conferenceService.getConferenceFormartStatistics(ccpartyId, beginDate, endDate);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        logger.debug(this.getClass() + " getConferenceFormartStatistics end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>统计与分析数据获取<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceLabelStatistics")
    @ResponseBody
    public Map<String, Object> getConferenceLabelStatistics(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceLabelStatistics begin");
        String ccpartyId = getString(request, "ccpartyId"); // 组织
        String beginDate = getString(request, "beginDate");
        String endDate = getString(request, "endDate");
        List<QueryResultConference> list = conferenceService.getConferenceLabelStatistics(ccpartyId, beginDate, endDate);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        logger.debug(this.getClass() + " getConferenceLabelStatistics end");
        return ret;
    }

    /**
     * <B>方法名称：</B>统计分析-组织工作统计<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月25日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceCcpartyStatistics")
    @ResponseBody
    public Map<String, Object> getConferenceCcpartyStatistics(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceCcpartyStatistics begin");
        String ccpartyId = getString(request, "ccpartyId"); // 组织
        String beginDate = getString(request, "beginDate");
        String endDate = getString(request, "endDate");
        List<ConferenceCcpartyView> list = conferenceService.getConferenceCcpartyStatistics(ccpartyId, beginDate, endDate);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        logger.debug(this.getClass() + " getConferenceCcpartyStatistics end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>根据组织生活发表获取查询统计的日期<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param request
     * @return
     */
    @RequestMapping("getConferencesYearsByUserOrCcparty")
    @ResponseBody
    public Map<String, Object> getConferencesYearsByUserOrCcparty(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferencesYearsByUserOrCcparty begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        String userId = getString(request, "userId");
        String[] years = conferenceService.getConferencesYearsByUserOrCcparty(ccpartyId, userId);
        ret.put("rows", years);
        logger.debug(this.getClass() + " getConferencesYearsByUserOrCcparty end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>导出统计分析数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("exportStatistics")
    @ResponseBody
    public ModelAndView exportStatistics(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug(this.getClass() + " exportStatistics begin");
        String beginTime = getString(request, "beginTime");
        String endTime = getString(request, "endTime");
        String ccpartyId = getString(request, "ccpartyId"); // 组织
        UserMc currentUser = loadUserMc(request);
        model.put("beginTime", beginTime);
        model.put("endTime", endTime);
        model.put("ccpartyId", ccpartyId);
        model.put("currentUser", currentUser);
        logger.debug(this.getClass() + " exportStatistics end");
        return new ModelAndView(exportStatistics, model);
    }

    /**
     * <B>方法名称：</B>根据组织生活ID获取所属哪些步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceStepsByConferenceId")
    @ResponseBody
    public Map<String, Object> getConferenceStepsByConferenceId(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceStepsByConferenceId begin");
        String conferenceId = getString(request, "conferenceId");
        List<ConferenceStep> conferenceSteps = conferenceService.getConferenceStepsByConferenceId(conferenceId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", conferenceSteps);
        logger.debug(this.getClass() + " getConferenceStepsByConferenceId end");
        return ret;
    }

    /**
     * <B>方法名称：</B>根据组织生活ID获取所属哪些生活形式<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月12日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceFormatsByConferenceId")
    @ResponseBody
    public Map<String, Object> getConferenceFormatsByConferenceId(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceFormatsByConferenceId begin");
        String conferenceId = getString(request, "conferenceId");
        List<ConferenceFormat> conferenceFormats = conferenceService.getConferenceFormatsByConferenceId(conferenceId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", conferenceFormats);
        logger.debug(this.getClass() + " getConferenceFormatsByConferenceId end");
        return ret;
    }

    /**
     * <B>方法名称：</B>根据组织生活ID获取所属哪些生活内容<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月13日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceLabelsByConferenceId")
    @ResponseBody
    public Map<String, Object> getConferenceLabelsByConferenceId(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceLabelsByConferenceId begin");
        String conferenceId = getString(request, "conferenceId");
        List<ConferenceLabel> conferenceLabels = conferenceService.getConferenceLabelsByConferenceId(conferenceId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", conferenceLabels);
        logger.debug(this.getClass() + " getConferenceLabelsByConferenceId end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织开展的支部工作描述<BR>
     * <B>概要说明：</B>党员电子活动证使用<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月26日
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("getConferenceDescriptionForCard")
    @ResponseBody
    public Map<String, Object> getConferenceDescriptionForCard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug(this.getClass() + " getConferenceDescriptionForCard begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        String paramter = getString(request, "paramter");
        String resultStr = conferenceService.getConferenceDescriptionForCard(ccpartyId, paramter);
        CCParty ccparty = cCpartyService.getCCParty(ccpartyId);
        ret.put("item", resultStr);
        ret.put("ccparty", ccparty);
        logger.debug(this.getClass() + " getConferenceDescriptionForCard end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取支部工作列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月26日
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getConferenceListByCcparty")
    @ResponseBody
    public Map<String, Object> getConferenceListByCcparty(HttpServletRequest request, HttpServletResponse response) {
        logger.debug(this.getClass() + " getConferenceListByCcparty begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String paramter = getString(request, "paramter");
        List<Conference> conferences = conferenceService.getConferenceListByCcparty(offset, limit, ccpartyId, paramter);
        ret.put("items", conferences);
        logger.debug(this.getClass() + " getConferenceListByCcparty end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>置顶状态修改<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月13日
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("updateConferenceIsTop")
    @ResponseBody
    public Map<String, Object> updateConferenceIsTop(HttpServletRequest request, HttpServletResponse response) {
        logger.debug(this.getClass() + " updateConferenceIsTop begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        Integer isTop = getInteger(request, "isTop");
        ret = conferenceService.updateConferenceIsTop(id, isTop);
        logger.debug(this.getClass() + " updateConferenceIsTop end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取前一篇、后一篇<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年10月21日 	
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getUpDownConference")
    @ResponseBody
    public Map<String, Object> getUpDownConference(HttpServletRequest request, HttpServletResponse response) {
        logger.debug(this.getClass() + " getUpDownConference begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        String viewSource = getString(request, "viewSource");
        String ccpartyId = getString(request, "ccpartyId");
        String userId = getString(request, "userId");
        ret = conferenceService.getUpDownConference(id, viewSource,ccpartyId,userId);
        logger.debug(this.getClass() + " getUpDownConference end");
        return ret;
    }

}
