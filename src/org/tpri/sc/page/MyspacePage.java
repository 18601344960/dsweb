package org.tpri.sc.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.uam.PrivilegeId;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>个人中心页面跳转控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月30日
 */
@Controller
public class MyspacePage extends BaseController {

    private static String prefix = "myspace/";

    /**
     * <B>方法名称：</B>跳转到个人中心页面<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月30日
     * @param request
     * @return
     */
    @RequestMapping("myspace")
    public String mySpace(HttpServletRequest request) {
        logger.debug("PageController mySpace");
        if (hasPrivilege(request, PrivilegeId.B003)) {
            return prefix + "myspace";
        }
        return "noprivilege";
    }

    /**
     * <B>方法名称：</B>跳转到添加我的工作<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月30日
     * @param request
     * @return
     */
    @RequestMapping("my-article-add")
    public String addMyArticle(HttpServletRequest request) {
        logger.debug("PageController addMyArticle");
        return prefix + "my-article-add";
    }

    /**
     * <B>方法名称：</B>跳转到编辑我的工作<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月30日
     * @param request
     * @return
     */
    @RequestMapping("my-article-edit")
    public String editMyArticle(HttpServletRequest request) {
        logger.debug("PageController editMyArticle");
        return prefix + "my-article-edit";
    }
    
    /**
     * 
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年8月12日 	
     * @param request
     * @return
     */
    @RequestMapping("my-assessment-detail")
    public String MyAssessmentDetail(HttpServletRequest request) {
        logger.debug("PageController MyAssessmentDetail");
        return prefix + "my-assessment-detail";
    }
}
