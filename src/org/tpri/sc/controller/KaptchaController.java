/**
 * Copyright 2016 TPRI. All Rights Reserved.
 */
package org.tpri.sc.controller;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>验证码控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年8月3日
 */
@Controller
@RequestMapping("/kaptcha")
public class KaptchaController extends BaseController {

    @Autowired
    private Producer captchaProducer = null;

    /**
     * <B>方法名称：</B>生成验证码<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月3日
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getKaptchaImage")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    /**
     * <B>方法名称：</B>判断验证码是否正确<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("validateKaptchaCode")
    @ResponseBody
    public Map<String, Object> validateKaptchaCode(HttpServletRequest request) {
        logger.debug("KaptchaController validateKaptchaCode begin");
        String code = getString(request, "code");
        HttpSession session = request.getSession();
        String captchaCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        Map<String, Object> ret = new HashMap<String, Object>();
        if (code == null) {
            ret.put("success", false);
            ret.put("msg", "");
        } else if (code.equalsIgnoreCase(captchaCode)) {
            ret.put("success", true);
        } else {
            ret.put("success", false);
            ret.put("msg", "请输入正确的验证码");
        }
        logger.debug("KaptchaController validateKaptchaCode end");
        return ret;
    }

}