package org.tpri.sc.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tpri.sc.controller.BaseController;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>工作记录页面跳转控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年3月28日
 */
@Controller
public class WorkRecordPage extends BaseController {

    private String prefix = "workrecord/";

    /**
     * 
     * <B>方法名称：</B>跳转到工作记录页面<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年3月28日
     * @param request
     * @return
     */
    @RequestMapping("article-add")
    public String articleAdd(HttpServletRequest request) {
        logger.debug("PageController article-add");
        return prefix + "article-add";
    }

    /**
     * 
     * <B>方法名称：</B>跳转到文章浏览页面<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年3月28日
     * @param request
     * @return
     */
    @RequestMapping("article-view")
    public String articleView(HttpServletRequest request) {
        logger.debug("PageController article-view");
        return prefix + "article-view";
    }

    /**
     * 
     * <B>方法名称：</B>跳转到文章编辑页面<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年3月28日
     * @param request
     * @return
     */
    @RequestMapping("article-edit")
    public String articleEdit(HttpServletRequest request) {
        logger.debug("PageController article-edit");
        return prefix + "article-edit";
    }

    /**
     * 
     * <B>方法名称：</B>跳转到文章预览页面<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年3月28日
     * @param request
     * @return
     */
    @RequestMapping("article-preview")
    public String articlePreView(HttpServletRequest request) {
        logger.debug("PageController article-preview");
        return prefix + "article-preview";
    }
}
