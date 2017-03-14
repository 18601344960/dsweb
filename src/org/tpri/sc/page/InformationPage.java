package org.tpri.sc.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tpri.sc.controller.BaseController;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>工作共享页面跳转控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年3月28日
 */
@Controller
public class InformationPage extends BaseController {

    private String prefix = "information/";

    /**
     * 
     * <B>方法名称：</B>跳转到工作必备页面<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年3月28日
     * @param request
     * @return
     */
    @RequestMapping("information")
    public String information(HttpServletRequest request) {
        logger.debug("PageController information");
        return prefix + "information";
    }

    /**
     * 
     * <B>方法名称：</B>跳转到工作必备详情页面<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年3月28日
     * @param request
     * @return
     */
    @RequestMapping("information-view")
    public String informationView(HttpServletRequest request) {
        logger.debug("PageController information-view");
        return prefix + "information-view";
    }
}
