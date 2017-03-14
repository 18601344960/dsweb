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
import org.tpri.sc.entity.obt.ConferenceOrgnizer;
import org.tpri.sc.service.obt.ConferenceOrgnizerService;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织生活组织者控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月24日
 */
@Controller
@RequestMapping("/obt")
public class ConferenceOrgnizerController extends BaseController {
    @Autowired
    private ConferenceOrgnizerService conferenceOrgnizerService;

    /**
     * <B>方法名称：</B>增加组织生活本组织组织者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param request
     * @return
     */
    @RequestMapping("addConferenceInOrgnizer")
    @ResponseBody
    public Map<String, Object> addConferenceInOrgnizer(HttpServletRequest request) {
        logger.debug(this.getClass() + " addConferenceInOrgnizer begin");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userIds", getString(request, "userIds"));
        param.put("conferenceId", getString(request, "conferenceId"));
        boolean result = conferenceOrgnizerService.addConferenceInOrgnizer(param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        if (result) {
            ret.put("msg", "保存成功");
        } else {
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + " addConferenceInOrgnizer end");
        return ret;
    }

    /**
     * <B>方法名称：</B>增加组织生活组织外组织者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月3日
     * @param request
     * @return
     */
    @RequestMapping("addConferenceOutOrgnizer")
    @ResponseBody
    public Map<String, Object> addConferenceOutOrgnizer(HttpServletRequest request) {
        logger.debug(this.getClass() + " addConferenceOutOrgnizer begin");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userNames", getString(request, "userNames"));
        param.put("conferenceId", getString(request, "conferenceId"));
        boolean result = conferenceOrgnizerService.addConferenceOutOrgnizer(param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        if (result) {
            ret.put("msg", "保存成功");
        } else {
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + " addConferenceOutOrgnizer end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取某组织生活的组织者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceOrgnizer")
    @ResponseBody
    public Map<String, Object> getConferenceOrgnizer(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceOrgnizer begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInt(request, "offset");
        Integer limit = getInt(request, "limit");
        String conferenceId = getString(request, "conferenceId");
        List<ConferenceOrgnizer> conferenceOrgnizer = conferenceOrgnizerService.getConferenceOrgnizer(offset, limit, conferenceId);
        Integer total = conferenceOrgnizerService.getConferenceOrgnizerTotal(conferenceId);
        ret.put("rows", conferenceOrgnizer);
        ret.put("total", total);
        logger.debug(this.getClass() + " getConferenceOrgnizer end");
        return ret;
    }

    /**
     * <B>方法名称：</B>删除组织生活组织者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param request
     * @return
     */
    @RequestMapping("deleteConferenceOrgnizer")
    @ResponseBody
    public Map<String, Object> deleteConferenceOrgnizer(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteConferenceOrgnizer begin");
        String id = getString(request, "id");
        boolean result = conferenceOrgnizerService.deleteConferenceOrgnizer(id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        if (result) {
            ret.put("msg", "保存成功");
        } else {
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + " deleteConferenceOrgnizer end");
        return ret;
    }
}
