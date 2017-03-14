package org.tpri.sc.controller.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.pub.AssessmentTarget;
import org.tpri.sc.service.pub.AssessmentTargetService;
import org.tpri.sc.view.ZTreeView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答卷测评对象控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年6月30日
 */
@Controller
@RequestMapping("/pub")
public class AssessmentTargetController extends BaseController {
    @Autowired
    private AssessmentTargetService assessmentTargetService;

    /**
     * 
     * <B>方法名称：</B>获取答卷测评通知人员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param request
     * @return
     */
    @RequestMapping("getAssessmentTargetsByAssessment")
    @ResponseBody
    public Map<String, Object> getAssessmentTargetsByAssessment(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAssessmentTargetsByAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId");
        List<AssessmentTarget> list = assessmentTargetService.getAssessmentTargetsByAssessment(assessmentId);
        ret.put("rows", list);
        logger.debug(this.getClass() + " getAssessmentTargetsByAssessment end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织的完成情况<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月11日
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyAssessmentTargetsByAssessment")
    @ResponseBody
    public Map<String, Object> getCcpartyAssessmentTargetsByAssessment(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAssessmentTargetsByAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId");
        String ccpartyId = getString(request, "ccpartyId");
        List<AssessmentTarget> list = assessmentTargetService.getCcpartyAssessmentTargetsByAssessment(assessmentId, ccpartyId);
        ret.put("rows", list);
        logger.debug(this.getClass() + " getAssessmentTargetsByAssessment end");
        return ret;
    }


    /**
     * 
     * <B>方法名称：</B>删除测评人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param request
     * @return
     */
    @RequestMapping("deleteAssessmentTarget")
    @ResponseBody
    public Map<String, Object> deleteAssessmentTarget(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteAssessmentTarget begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        ret = assessmentTargetService.deleteAssessmentTarget(id);
        logger.debug(this.getClass() + " deleteAssessmentTarget end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取参与答卷组织数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月10日
     * @param request
     * @return
     */
    @RequestMapping("getAssessmentTargetTrees")
    @ResponseBody
    public Map<String, Object> getAssessmentTargetTrees(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAssessmentTargetTrees begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId");
        List<ZTreeView> trees = assessmentTargetService.getAssessmentTargetTrees(assessmentId);
        ret.put("items", trees);
        logger.debug(this.getClass() + " getAssessmentTargetTrees end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取参与答卷组织数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月10日
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyAssessmentTargetTrees")
    @ResponseBody
    public Map<String, Object> getCcpartyAssessmentTargetTrees(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAssessmentTargetTrees begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId");
        String ccpartyId = getString(request, "ccpartyId");
        List<ZTreeView> trees = assessmentTargetService.getCcpartyAssessmentTargetTrees(assessmentId, ccpartyId);
        ret.put("items", trees);
        logger.debug(this.getClass() + " getCcpartyAssessmentTargetTrees end");
        return ret;
    }

}