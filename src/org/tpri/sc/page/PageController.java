package org.tpri.sc.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tpri.sc.controller.BaseController;
/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>页面跳转控制器<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年3月28日
 */
@Controller
public class PageController extends BaseController {
	
	/**
	 * 跳转到相应栏目介绍页面
	 * 由参数action决定调整到哪个页面
	 */
	@RequestMapping("columnaction")
	public String columnAction(HttpServletRequest request) {
		logger.debug("PageController columnaction");
		String action=getString(request, "action");
		return action;
	}
	
	/**
	 * 跳转到登录页面
	 */
	@RequestMapping("login")
	public String login(HttpServletRequest request) {
		logger.debug("PageController login");
		return "login";
	}
}
