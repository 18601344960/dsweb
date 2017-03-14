package org.tpri.sc.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tpri.sc.controller.BaseController;
/**
 * @description 通用页面跳转控制器
 * @author 易文俊
 * @since 2015-08-10
 */
@Controller
public class CommonPage extends BaseController {
	private String prefix="common/";
	/**
     * 跳转到下载浏览器页面
     */
    @RequestMapping("download-browser")
    public String downloadBrowser(HttpServletRequest request) {
        return prefix+"download-browser";
    }
}
