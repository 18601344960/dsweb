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
import org.tpri.sc.entity.pub.AssessmentUser;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.service.pub.AssessmentUserService;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答卷测评用户控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年6月30日
 */
@Controller
@RequestMapping("/pub")
public class AssessmentUserController extends BaseController {
    @Autowired
    private AssessmentUserService assessmentUserService;

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
    @RequestMapping("getAssessmentUsersByAssessment")
    @ResponseBody
    public Map<String, Object> getAssessmentUsersByAssessment(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAssessmentUsersByAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String assessmentId = getString(request, "assessmentId");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        List<AssessmentUser> list = assessmentUserService.getAssessmentUsersByAssessment(offset, limit, assessmentId);
        Integer total = assessmentUserService.getAssessmentUsersTotalByAssessment(assessmentId);
        ret.put("rows", list);
        ret.put("total", total);
        logger.debug(this.getClass() + " getAssessmentUsersByAssessment end");
        return ret;
    }

    
    /**
     * 
     * <B>方法名称：</B>获取答卷人员完成情况<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年10月10日 	
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("getAllAssessmentUsersByAssessment")
    @ResponseBody
    public Map<String, Object> getAllAssessmentUsersByAssessment(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " getAllAssessmentUsersByAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String search = getString(request, "search");
        Integer status = getInteger(request, "status");
        String assessmentId = getString(request, "assessmentId");
        String ccpartyIds = getString(request, "ccpartyIds");
        List<AssessmentUser> assessmentUsers = assessmentUserService.getAllAssessmentUsersByAssessment(offset,limit,search,status,assessmentId, ccpartyIds);
        Integer total = assessmentUserService.getAllAssessmentUserTotalByAssessment(search,status,assessmentId,ccpartyIds);
        ret.put("rows", assessmentUsers);
        ret.put("total", total);
        logger.debug(this.getClass() + " getAllAssessmentUsersByAssessment end");
        return ret;
    }
    
    
    /**
     * 
     * <B>方法名称：</B>根据组织获取人员完成情况<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年10月10日   
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("getCcpartyAllAssessmentUsersByAssessment")
    @ResponseBody
    public Map<String, Object> getCcpartyAllAssessmentUsersByAssessment(HttpServletRequest request) throws Exception {
        logger.debug(this.getClass() + " getAllAssessmentUsersByAssessment begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String search = getString(request, "search");
        Integer status = getInteger(request, "status");
        String currentCcpartyId = getString(request, "currentCcpartyId");
        String assessmentId = getString(request, "assessmentId");
        String ccpartyIds = getString(request, "ccpartyIds");
        List<AssessmentUser> assessmentUsers = assessmentUserService.getCcpartyAllAssessmentUsersByAssessment(offset,limit,search,status,currentCcpartyId,assessmentId, ccpartyIds);
        Integer total = assessmentUserService.getCcpartyAllAssessmentUserTotalByAssessment(search,status,currentCcpartyId,assessmentId,ccpartyIds);
        ret.put("rows", assessmentUsers);
        ret.put("total", total);
        logger.debug(this.getClass() + " getCcpartyAllAssessmentUsersByAssessment end");
        return ret;
    }
    
}