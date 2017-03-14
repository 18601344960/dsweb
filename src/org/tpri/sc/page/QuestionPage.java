package org.tpri.sc.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tpri.sc.controller.BaseController;

/**
 * 题库页面跳转控制器
 * 
 * @author zhaozijing
 *
 */
@Controller
public class QuestionPage extends BaseController {

	private String prefix = "question/";

	/**
	 * 跳转题库选择页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("question-select")
	public String questionSelect(HttpServletRequest request) {
		logger.debug("PageController questionSelect");
		return prefix + "question-select";
	}

}
