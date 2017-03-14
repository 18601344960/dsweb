package org.tpri.sc.controller.pub;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.pub.Assessment;
import org.tpri.sc.entity.pub.AssessmentTopic;
import org.tpri.sc.service.pub.AssessmentTopicService;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>试题设置<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年6月29日
 */
@Controller
@RequestMapping("/pub")
public class AssessmentTopicController extends BaseController {
    @Autowired
    private AssessmentTopicService assessmentTopicService;

    /**
     * 
     * <B>方法名称：</B>获取试题<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年6月29日 	
     * @param request
     * @return
     */
    @RequestMapping("getAssessmentTopicByAssessment")
    @ResponseBody
    public Map<String, Object> getAssessmentTopicByAssessment(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAssessmentTopicByAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId"); //问卷ID
        Assessment assessment = assessmentTopicService.getAssessmentTopicByAssessment(assessmentId);
        ret.put("item", assessment);
        logger.debug(this.getClass() + " getAssessmentTopicByAssessment end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取试题详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月16日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("getAssessmentTopicById")
    @ResponseBody
    public Map<String, Object> getAssessmentTopicById(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " getAssessmentTopicById begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String topicId = getString(request, "topicId"); //试题ID
        AssessmentTopic topic = assessmentTopicService.getAssessmentTopicById(topicId);
        ret.put("row", topic);
        logger.debug(this.getClass() + " getAssessmentTopicById end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取问卷测评最大题号<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月16日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("getMaxSeqAssessmentTopicByAssessment")
    @ResponseBody
    public Map<String, Object> getMaxSeqAssessmentTopicByAssessment(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " getMaxSeqAssessmentTopicByAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId"); //问卷ID
        ret = assessmentTopicService.getMaxSeqAssessmentTopicByAssessment(assessmentId);
        logger.debug(this.getClass() + " getMaxSeqAssessmentTopicByAssessment end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>删除试题<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月16日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("deleteAssessmentTopic")
    @ResponseBody
    public Map<String, Object> deleteAssessmentTopic(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " deleteAssessmentTopic begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String topicId = getString(request, "topicId"); //试题ID
        ret = assessmentTopicService.deleteAssessmentTopicByTopic(topicId);
        logger.debug(this.getClass() + " deleteAssessmentTopic  ");
        return ret;
    }
}