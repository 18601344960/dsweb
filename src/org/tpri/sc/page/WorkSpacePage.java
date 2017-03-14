package org.tpri.sc.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.uam.PrivilegeId;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>工作平台页面跳转控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月29日
 */
@Controller
public class WorkSpacePage extends BaseController {

    private String prefix = "workspace/";

    /**
     * 跳转到工作平台页面
     */
    @RequestMapping("work-space")
    public String workShare(HttpServletRequest request) {
        logger.debug("PageController work-space");
        if (hasPrivilege(request, PrivilegeId.B002)) {
            return prefix + "work-space";
        }
        return "noprivilege";
    }
    
    /**
     * 
     * <B>方法名称：</B>发展党员时间轴页面跳转<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年7月16日 	
     * @param request
     * @return
     */
    @RequestMapping("development-procedure-timer-shaft")
    public String developmentProcedureTimerShaft(HttpServletRequest request) {
        logger.debug("PageController developmentProcedureTimerShaft");
        return prefix + "development-procedure-timer-shaft";
    }
    
    /**
     * 
     * <B>方法名称：</B>答题答卷预览<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年8月11日 	
     * @param request
     * @return
     */
    @RequestMapping("assessment-detail")
    public String assessmentDetail(HttpServletRequest request) {
        return prefix + "assessment-detail";
    }
    
    /**
     * 
     * <B>方法名称：答题答卷结果统计汇总</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param request
     * @return
     */
    @RequestMapping("assessment-result-statistical")
    public String assessmentResultStatistical(HttpServletRequest request) {
        return prefix + "assessment-result-statistical";
    }
}
