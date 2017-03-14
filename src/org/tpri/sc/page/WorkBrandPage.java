package org.tpri.sc.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tpri.sc.controller.BaseController;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>工作品牌页面跳转控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年3月28日
 */
@Controller
public class WorkBrandPage extends BaseController {

    private String prefix = "workbrand/";

    /**
     * 跳转到工作品牌
     */
    @RequestMapping("work-brand")
    public String workBrand(HttpServletRequest request) {
        logger.debug("PageController workBrand");
        return prefix + "work-brand";
    }

    /**
     * 跳转到工作品牌详情
     */
    @RequestMapping("work-brand-view")
    public String workBrandView(HttpServletRequest request) {
        logger.debug("PageController workBrandView");
        return prefix + "work-brand-view";
    }
}
