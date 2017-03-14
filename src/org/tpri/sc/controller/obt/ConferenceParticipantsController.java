package org.tpri.sc.controller.obt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.obt.Conference;
import org.tpri.sc.entity.obt.ConferenceParticipants;
import org.tpri.sc.service.obt.ConferenceParticipantsService;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织生活参会人员控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月24日
 */
@Controller
@RequestMapping("/obt")
public class ConferenceParticipantsController extends BaseController {
    @Autowired
    private ConferenceParticipantsService conferenceParticipantsService;

    /**
     * <B>方法名称：</B>增加组织生活本组织参与人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param request
     * @return
     */
    @RequestMapping("addConferenceInParticipants")
    @ResponseBody
    public Map<String, Object> addConferenceInParticipants(HttpServletRequest request) {
        logger.debug(this.getClass() + " addConferenceInParticipants begin");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userIds", getString(request, "userIds"));
        param.put("conferenceId", getString(request, "conferenceId"));
        boolean result = conferenceParticipantsService.addConferenceInParticipants(param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        if (result) {
            ret.put("msg", "保存成功");
        } else {
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + " addConferenceInParticipants end");
        return ret;
    }

    /**
     * <B>方法名称：</B>增加组织生活组织外参与人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月3日
     * @param request
     * @return
     */
    @RequestMapping("addConferenceOutParticipants")
    @ResponseBody
    public Map<String, Object> addConferenceOutParticipants(HttpServletRequest request) {
        logger.debug(this.getClass() + " addConferenceOutParticipants begin");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userNames", getString(request, "userNames"));
        param.put("conferenceId", getString(request, "conferenceId"));
        boolean result = conferenceParticipantsService.addConferenceOutParticipants(param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        if (result) {
            ret.put("msg", "保存成功");
        } else {
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + " addConferenceOutParticipants end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取某组织生活的参与人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceParticipants")
    @ResponseBody
    public Map<String, Object> getConferenceParticipants(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceParticipants begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInt(request, "offset");
        Integer limit = getInt(request, "limit");
        String conferenceId = getString(request, "conferenceId");
        List<ConferenceParticipants> conferenceParticipants = conferenceParticipantsService.getConferenceParticipants(offset, limit, conferenceId);
        Integer total = conferenceParticipantsService.getConferenceParticipantsTotal(conferenceId);
        ret.put("rows", conferenceParticipants);
        ret.put("total", total);
        logger.debug(this.getClass() + " getConferenceParticipants end");
        return ret;
    }

    /**
     * <B>方法名称：</B>删除组织生活参与人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param request
     * @return
     */
    @RequestMapping("deleteConferenceParticipants")
    @ResponseBody
    public Map<String, Object> deleteConferenceParticipants(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteConferenceParticipants begin");
        String id = getString(request, "id");
        boolean result = conferenceParticipantsService.deleteConferenceParticipants(id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        if (result) {
            ret.put("msg", "保存成功");
        } else {
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + " deleteConferenceParticipants end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>移除通知人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年2月29日
     * @param request
     * @return
     */

    /**
     * 
     * <B>方法名称：</B>获取某用户参加的支部工作分类数详情<BR>
     * <B>概要说明：</B>党员电子活动证使用<BR>
     * 
     * @author 赵子靖
     * @since 2016年3月22日
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("getStatisticsDetailBranchConference")
    @ResponseBody
    public Map<String, Object> getStatisticsDetailBranchConference(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug(this.getClass() + " getStatisticsDetailBranchConference begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String userId = getString(request, "userId");
        String paramter = getString(request, "paramter");
        String resultStr = conferenceParticipantsService.getStatisticsDetailBranchConference(userId, paramter);
        ret.put("item", resultStr);
        logger.debug(this.getClass() + " getStatisticsDetailBranchConference end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户参加的支部工作列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月22日
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("getBranchConferenceListByUser")
    @ResponseBody
    public Map<String, Object> getBranchConferenceListByUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug(this.getClass() + " getBranchConferenceListByUser begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String userId = getString(request, "userId");
        Integer start = getInteger(request, "start");
        Integer limit = getInteger(request, "limit");
        String paramter = getString(request, "paramter");
        List<Conference> branchConferences = conferenceParticipantsService.getBranchConferenceListByUser(start, limit, userId, paramter);
        ret.put("items", branchConferences);
        logger.debug(this.getClass() + " getBranchConferenceListByUser end");
        return ret;
    }
}
