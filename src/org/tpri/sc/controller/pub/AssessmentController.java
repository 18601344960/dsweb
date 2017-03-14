package org.tpri.sc.controller.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.pub.Assessment;
import org.tpri.sc.service.pub.AssessmentService;
import org.tpri.sc.view.pub.MyAssessmentView;

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：问卷测评</B><BR>
 * <B>中文类名：问卷测评控制器</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年6月16日
 */
@Controller
@RequestMapping("/pub")
public class AssessmentController extends BaseController {
    @Autowired
    private AssessmentService assessmentService;

    /**
     * 
     * <B>方法名称：获取问卷测评列表</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param request
     * @return
     */
    @RequestMapping("getAssessmentList")
    @ResponseBody
    public Map<String, Object> getAssessmentList(HttpServletRequest request) {
        logger.debug(this.getClass() + " loadAssessments begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        String search = getString(request, "search");
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        List<Assessment> list = assessmentService.getAssessmentList(start, limit, search, ccpartyId);
        Integer total = assessmentService.getAssessmentTotal(search, ccpartyId);
        ret.put("rows", list);
        ret.put("total", total);
        logger.debug(this.getClass() + " loadAssessments end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取我的答卷列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月11日
     * @param request
     * @return
     */
    @RequestMapping("getMyAssessments")
    @ResponseBody
    public Map<String, Object> getMyAssessments(HttpServletRequest request) {
        logger.debug(this.getClass() + " getMyAssessments begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String userId = getString(request, "userId");
        String ccpartyId = getString(request, "ccpartyId");
        String search = getString(request, "search");
        int joinType = getInt(request, "joinType");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        List<MyAssessmentView> list = assessmentService.getMyAssessments(offset, limit, search, ccpartyId, userId,joinType);
        Integer total = assessmentService.getMyAssessmentsTotal(search, ccpartyId, userId,joinType);
        ret.put("rows", list);
        ret.put("total", total);
        logger.debug(this.getClass() + " getMyAssessments end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：保存问卷测评</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("saveAssessment")
    @ResponseBody
    public Map<String, Object> saveAssessment(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " saveAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("name", getString(request, "name"));
        objs.put("isExpiry", getInt(request, "isExpiry"));
        objs.put("endDate", getString(request, "endDate"));
        objs.put("description", getString(request, "description"));
        objs.put("ccpartyId", getString(request, "ccpartyId"));
        objs.put("ccpartyIds", getString(request, "ccpartyIds"));
        ret = assessmentService.saveAssessment(loadUserMc(request), objs);
        logger.debug(this.getClass() + " saveAssessment end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：根据ID获取文件测评信息</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("getAssessmentById")
    @ResponseBody
    public Map<String, Object> getAssessmentById(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " getAssessmentById begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        Assessment assessment = assessmentService.getAssessmentById(id);
        ret.put("item", assessment);
        logger.debug(this.getClass() + " getAssessmentById end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：修改问卷测评</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateAssessment")
    @ResponseBody
    public Map<String, Object> updateAssessment(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " updateAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("id", getString(request, "id"));
        objs.put("name", getString(request, "name"));
        objs.put("isExpiry", getInt(request, "isExpiry"));
        objs.put("endDate", getString(request, "endDate"));
        objs.put("description", getString(request, "description"));
        objs.put("ccpartyIds", getString(request, "ccpartyIds"));
        ret = assessmentService.updateAssessment(objs);
        logger.debug(this.getClass() + " updateAssessment end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>状态更改<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年10月11日   
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateAssessmentStatus")
    @ResponseBody
    public Map<String, Object> updateAssessmentStatus(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " updateAssessmentStatus begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        Integer status = getInteger(request, "status");
        ret = assessmentService.updateAssessmentStatus(id,status);
        logger.debug(this.getClass() + " updateAssessmentStatus end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：根据ID删除问卷测评</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("deleteAssessment")
    @ResponseBody
    public Map<String, Object> deleteAssessment(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " deleteAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        ret = assessmentService.deleteAssessment(id);
        logger.debug(this.getClass() + " deleteAssessment end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>文件发布<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param request
     * @return
     */
    @RequestMapping("publishAssessment")
    @ResponseBody
    public Map<String, Object> publishAssessment(HttpServletRequest request) {
        logger.debug(this.getClass() + " publishAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        ret = assessmentService.publishAssessment(id);
        logger.debug(this.getClass() + " publishAssessment end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：结束问卷测评</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param request
     * @return
     */
    @RequestMapping("overAssessment")
    @ResponseBody
    public Map<String, Object> overAssessment(HttpServletRequest request) {
        logger.debug(this.getClass() + " overAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        ret = assessmentService.overAssessment(id);
        logger.debug(this.getClass() + " overAssessment end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>保存试题<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月6日
     * @param request
     * @return
     */
    @RequestMapping("saveAssessmentTopic")
    @ResponseBody
    public Map<String, Object> saveAssessmentTopic(HttpServletRequest request) {
        logger.debug(this.getClass() + " saveAssessmentTopic begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId");
        String questions = getString(request, "questions"); //试题json
        ret = assessmentService.saveAssessmentTopic(assessmentId, questions);
        logger.debug(this.getClass() + " saveAssessmentTopic end");
        return ret;
    }

    /**
     * 删除试卷下的所有试题和选项、答案
     * 
     * @param request
     * @return
     */
    @RequestMapping("deleteAssessmentTopicByAssessmentId")
    @ResponseBody
    public Map<String, Object> deleteAssessmentTopicByAssessmentId(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteAssessmentTopicByAssessmentId begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId");
        if (!StringUtils.isEmpty(assessmentId)) {
            if (assessmentService.deleteAssessmentTopic(assessmentId)) {
                ret.put("success", true);
                ret.put("msg", "删除试题成功");
            } else {
                ret.put("success", false);
                ret.put("msg", "删除试题失败");
            }
        }
        logger.debug(this.getClass() + " deleteAssessmentTopicByAssessmentId end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：根据测评问卷ID获取内容及试题信息</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param request
     * @return
     */
    @RequestMapping("getAssessment")
    @ResponseBody
    public Map<String, Object> getAssessment(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId");
        Assessment assessment = assessmentService.getAssessmentInfoById(assessmentId);
        ret.put("item", assessment);
        logger.debug(this.getClass() + " getAssessment end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>我的答卷详情<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年8月12日 	
     * @param request
     * @return
     */
    @RequestMapping("getMyAssessmentTopicByAssessmentAndUser")
    @ResponseBody
    public Map<String, Object> getMyAssessmentTopicByAssessmentAndUser(HttpServletRequest request) {
        logger.debug(this.getClass() + " getMyAssessmentTopicByAssessmentAndUser begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId");
        String userId = getString(request, "userId");
        ret = assessmentService.getMyAssessmentTopicByAssessmentAndUser(assessmentId,userId);
        logger.debug(this.getClass() + " getMyAssessmentTopicByAssessmentAndUser end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>查看答卷结果详情<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年9月14日    
     * @param request
     * @return
     */
    @RequestMapping("getAssessmentResultInfos")
    @ResponseBody
    public Map<String, Object> getAssessmentResultInfos(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAssessmentResultInfos begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = assessmentService.getAssessmentResultInfos(getString(request,"id"));
        logger.debug(this.getClass() + " getAssessmentResultInfos end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取上级答卷监控<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年10月11日   
     * @param request
     * @return
     */
    @RequestMapping("getLeaderAssessmentList")
    @ResponseBody
    public Map<String, Object> getLeaderAssessmentList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getLeaderAssessmentList begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        String search = getString(request, "search");
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        List<Assessment> list = assessmentService.getLeaderAssessmentList(start, limit, search, ccpartyId);
        Integer total = assessmentService.getLeaderAssessmentTotal(search, ccpartyId);
        ret.put("rows", list);
        ret.put("total", total);
        logger.debug(this.getClass() + " getLeaderAssessmentList end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取我的待答卷数目<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年9月24日    
     * @param request
     * @return
     */
    @RequestMapping("getMyAssessmentNum")
    @ResponseBody
    public Map<String, Object> getMyAssessmentNum(HttpServletRequest request) {
        logger.debug(this.getClass() + " getMyAssessmentNum begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        String userId = getString(request, "userId");
        Integer total = assessmentService.getMyAssessmentNum(ccpartyId, userId);
        ret.put("total", total);
        logger.debug(this.getClass() + " getMyAssessmentNum end");
        return ret;
    }

}