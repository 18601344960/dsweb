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
import org.tpri.sc.entity.obt.Election;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.obt.ElectionService;

/**
 * 
 * <B>系统名称：</B>换届选举控制器<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年7月1日
 */
@Controller
@RequestMapping("/obt")
public class ElectionController extends BaseController {

    @Autowired
    private ElectionService electionService;

    /**
     * <B>方法名称：</B>添加换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年9月17日
     * @param request
     * @return
     */
    @RequestMapping("addElection")
    @ResponseBody
    public Map<String, Object> addElection(HttpServletRequest request) {
        logger.debug(this.getClass() + " addElection begin");

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("sequence", getInt(request, "sequence"));
        param.put("ageLimit", getInt(request, "ageLimit"));
        param.put("selectMode", getInt(request, "selectMode"));
        param.put("startTime", getDate(request, "startTime"));
        param.put("endTime", getDate(request, "endTime"));
        param.put("participants", getInt(request, "participants"));
        param.put("attendance", getInt(request, "attendance"));

        UserMc user = loadUserMc(request);

        Map<String, Object> ret = new HashMap<String, Object>();
        boolean result = electionService.addElection(user, param);
        ret.put("success", result);
        ret.put("description", "添加成功");
        logger.debug(this.getClass() + " addElection end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>更新换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年9月17日
     * @param request
     * @return
     */
    @RequestMapping("updateElection")
    @ResponseBody
    public Map<String, Object> updateElection(HttpServletRequest request) {
        logger.debug(this.getClass() + " updateElection begin");

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("id", getString(request, "id"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("sequence", getInt(request, "sequence"));
        param.put("ageLimit", getInt(request, "ageLimit"));
        param.put("selectMode", getInt(request, "selectMode"));
        param.put("startTime", getDate(request, "startTime"));
        param.put("endTime", getDate(request, "endTime"));
        param.put("participants", getInt(request, "participants"));
        param.put("attendance", getInt(request, "attendance"));
        UserMc user = loadUserMc(request);
        boolean result = electionService.updateElection(user, param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        logger.debug(this.getClass() + " updateElection end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>删除换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年9月17日
     * @param request
     * @return
     */

    @RequestMapping("deleteElection")
    @ResponseBody
    public Map<String, Object> deleteElection(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteElection begin");
        String id = getString(request, "id");
        UserMc user = loadUserMc(request);

        Map<String, Object> ret = new HashMap<String, Object>();
        boolean result = electionService.deleteElection(user, id);
        ret.put("success", result);
        logger.debug(this.getClass() + " deleteElection end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取换届选举列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年9月17日
     * @param request
     * @return
     */
    @RequestMapping("getElectionList")
    @ResponseBody
    public Map<String, Object> getElectionList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getElectionList begin");
        String ccpartyId = getString(request, "ccpartyId");
        Integer limit = getInt(request, "limit");
        Integer offset = getInt(request, "offset");
        List<Election> list = electionService.getElectionList(ccpartyId, limit, offset);
        Integer total = electionService.getElectionTotal(ccpartyId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        ret.put("total", total);
        logger.debug(this.getClass() + " getElectionList end");
        return ret;
    }

    /**
     * 获取当前换届选举
     */
    @RequestMapping("getCurrentElection")
    @ResponseBody
    public Map<String, Object> getCurrentElection(HttpServletRequest request) {
        String ccpartyId = getString(request, "ccpartyId");
        String id = getString(request, "id");
        Election election = null;
        if (id != null && !id.equals("")) {
            election = electionService.getElectionById(id);
        } else {
            election = electionService.getCurrentElection(ccpartyId);
        }
        Integer total = electionService.getElectionTotal(ccpartyId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", election);
        ret.put("rowNum", total);
        ret.put("total", total);
        return ret;
    }

    /**
     * 获取上一届换届选举
     */
    @RequestMapping("getLastElection")
    @ResponseBody
    public Map<String, Object> getLastElection(HttpServletRequest request) {
        String ccpartyId = getString(request, "ccpartyId");
        Integer sequence = getInteger(request, "sequence");
        Election election = electionService.getLastElection(ccpartyId, sequence);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", election);
        return ret;
    }

    /**
     * 获取下一届换届选举
     */
    @RequestMapping("getNextElection")
    @ResponseBody
    public Map<String, Object> getNextElection(HttpServletRequest request) throws Exception {
        String ccpartyId = getString(request, "ccpartyId");
        Integer sequence = getInteger(request, "sequence");
        Election election = electionService.getNextElection(ccpartyId, sequence);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", election);
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织换届选举最大届次<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年10月22日
     * @param request
     * @return
     */

    @RequestMapping("getElectionMaxSequence")
    @ResponseBody
    public Map<String, Object> getElectionMaxSequence(HttpServletRequest request) {
        logger.debug(this.getClass() + " getElectionMaxSerial begin");
        String ccpartyId = getString(request, "ccpartyId");
        Integer maxSequence = electionService.getElectionMaxSequence(ccpartyId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("maxSequence", maxSequence);
        logger.debug(this.getClass() + " getElectionMaxSequence end");
        return ret;
    }

    /**
     * <B>方法名称：根据id获取换届选举</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年9月17日
     * @param request
     * @return
     */
    @RequestMapping("getElectionById")
    @ResponseBody
    public Map<String, Object> getElectionById(HttpServletRequest request) {
        String id = getString(request, "id");
        Election election = electionService.getElectionById(id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", election);
        return ret;
    }

    /**
     * <B>方法名称：</B>获取换届选举提醒内容<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月4日
     * @param request
     * @return
     */
    @RequestMapping("getElectionReminder")
    @ResponseBody
    public Map<String, Object> getElectionReminder(HttpServletRequest request) {
        String ccpartyId = getString(request, "ccpartyId");
        String tipContent = electionService.getElectionReminder(ccpartyId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("tipContent", tipContent);
        return ret;
    }
    /**
     * <B>方法名称：</B>获取下级组织的换届选举总览<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月5日
     * @param request
     * @return
     */
    @RequestMapping("getElectionSummary")
    @ResponseBody
    public Map<String, Object> getElectionSummary(HttpServletRequest request) {
        logger.debug(this.getClass() + " getElectionSummary begin");
        String ccpartyId = getString(request, "ccpartyId");
        List<Election> elections = electionService.getElectionSummary(ccpartyId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", elections);
        logger.debug(this.getClass() + " getElectionSummary end");
        return ret;
    }
}
