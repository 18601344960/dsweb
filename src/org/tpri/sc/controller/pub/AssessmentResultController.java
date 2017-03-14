package org.tpri.sc.controller.pub;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.pub.Assessment;
import org.tpri.sc.entity.pub.AssessmentUser;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.pub.AssessmentResultService;
import org.tpri.sc.service.pub.AssessmentUserService;
import org.tpri.sc.view.pub.AssessmentResultStatisticalView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答卷结果控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年6月30日
 */
@Controller
@RequestMapping("/pub")
public class AssessmentResultController extends BaseController {
    @Autowired
    private AssessmentResultService assessmentResultService;
    @Autowired
    private AssessmentUserService assessmentUserService;

    /**
     * 
     * <B>方法名称：</B>答卷结果保存<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("saveAssessmentResult")
    @ResponseBody
    public Map<String, Object> saveAssessmentResult(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " saveAssessmentResult begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId"); //问卷ID
        String userId = getString(request, "userId"); //用户ID
        String answers = getString(request, "answers"); //保存测试结果
        ret = assessmentResultService.saveAssessmentResult(assessmentId, answers, userId);
        logger.debug(this.getClass() + " saveAssessmentResult begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：查看某用户的问卷测评结果</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("viewAssessmentQuestion")
    @ResponseBody
    public Map<String, Object> viewAssessmentQuestion(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " viewAssessmentQuestion begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId"); //问卷ID
        String userId = getString(request, "userId"); //查询指定某人
        if (StringUtils.isEmpty(userId)) {
            UserMc user = loadUserMc(request); //登录测评人员
            userId = user.getId();
        }
        //试卷内容
        Assessment assessment = assessmentResultService.getAssessmentAndTopicInfoByAssessmentIdAndUserId(assessmentId, userId);
        AssessmentUser target = assessmentUserService.getAssessmentUserByAssessmentAndUser(assessmentId, userId);
        ret.put("item", assessment);
        ret.put("target", target);
        logger.debug(this.getClass() + " viewAssessmentQuestion end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：根据问卷测评ID获取问卷测评的结果统计</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月18日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("getAssessmentResultStatistical")
    @ResponseBody
    public Map<String, Object> getAssessmentResultStatistical(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " getAssessmentResultStatistical begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId"); //问卷ID
        AssessmentResultStatisticalView assessment = assessmentResultService.getAssessmentResultStatistical(assessmentId);
        ret.put("item", assessment);
        logger.debug(this.getClass() + " getAssessmentResultStatistical end");
        return ret;
    }
    
}