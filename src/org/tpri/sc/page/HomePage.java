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
 * <B>中文类名：</B>首页页面跳转控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年3月28日
 */
@Controller
public class HomePage extends BaseController {

    private String prefix = "home/";

    /**
     * 跳转到首页
     */
    @RequestMapping("home")
    public String home(HttpServletRequest request) {
        logger.debug("PageController home");
        if (hasPrivilege(request, PrivilegeId.B004)) {
            return prefix + "home";
        }
        return "noprivilege";
    }

    /**
     * 跳转到通知详情页面
     */
    @RequestMapping("announcement-view")
    public String announcementContent(HttpServletRequest request) {
        logger.debug("PageController announcement-view");
        return prefix + "announcement-view";
    }

    /**
     * 跳转到机关支部工作法介绍
     */
    @RequestMapping("work-method")
    public String workMethod(HttpServletRequest request) {
        logger.debug("PageController workMethod");
        return prefix + "work-method";
    }

    /**
     * 跳转到机关支部工作法工作步骤
     */
    @RequestMapping("work-step")
    public String workStep(HttpServletRequest request) {
        logger.debug("PageController workStep");
        return prefix + "work-step";
    }
    
    /**
     * baobiao
     */
    @RequestMapping("report")
    public String report(HttpServletRequest request) {
        logger.debug("PageController report");
        return "report";
    }
}
