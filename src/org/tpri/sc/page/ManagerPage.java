package org.tpri.sc.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.uam.PrivilegeId;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>管理中心页面跳转控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年3月28日
 */
@Controller
public class ManagerPage extends BaseController {

    private String prefix = "manager/";

    /**
     * 
     * <B>方法名称：</B>跳转到管理中心<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年3月28日
     * @param request
     * @return
     */
    @RequestMapping("manager")
    public String manager(HttpServletRequest request) {
        logger.debug("PageController manager");
        if (hasPrivilege(request, PrivilegeId.B001)) {
            return prefix + "manager";
        }
        return "noprivilege";
    }

    /**
     * 
     * <B>方法名称：</B>跳转到统计分析页面<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年3月28日
     * @param request
     * @return
     */
    @RequestMapping("statistics")
    public String statistics(HttpServletRequest request) {
        logger.debug("PageController statistics");
        return prefix + "statistics";
    }

}
