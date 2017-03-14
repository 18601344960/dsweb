package org.tpri.sc.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.tpri.sc.util.BaseConstants;
import org.tpri.sc.util.RequestUtils;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>验证登录过滤器<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月25日
 */
public class AuthValidateFilter implements Filter {

    /** 默认：登陆页面地址 */
    public static final String DEFAULT_LOGIN_URL = "/login";

    /** 参数：基础路径地址 */
    public static final String PARAM_BASE_PATH = "basePath";

    /** 参数：登陆页面地址 */
    public static final String PARAM_LOGIN_URL = "loginUrl";

    /** 参数：验证忽略地址 */
    public static final String PARAM_IGNORE_URL = "ignoreUrl";

    /** 切分符 */
    public static final String SPLIT_FLAG = ",";

    /** 基础地址 */
    private String basePath = null;

    /** 登陆页面地址 */
    private String loginUrl = null;

    /** 验证忽略地址 */
    private String[] ignoreUrls = null;

    /**
     * 初始化过滤器
     */
    public void init(FilterConfig fConfig) throws ServletException {

        String pValue = null;
        pValue = fConfig.getInitParameter(PARAM_BASE_PATH);
        if (StringUtils.isBlank(pValue)) {
            this.basePath = "/" + fConfig.getServletContext().getServletContextName();
        }
        else {
            this.basePath = pValue;
        }

        // 验证失败的转向地址参数初始化
        pValue = fConfig.getInitParameter(PARAM_LOGIN_URL);
        if (StringUtils.isBlank(pValue)) {
            this.loginUrl = this.basePath + DEFAULT_LOGIN_URL;
        }
        else {
            this.loginUrl = this.basePath + pValue;
        }

        // 验证忽略的地址参数初始化
        pValue = fConfig.getInitParameter(PARAM_IGNORE_URL);
        if (!StringUtils.isBlank(pValue)) {
            this.ignoreUrls = pValue.split(SPLIT_FLAG);
            for (int i = 0; i < this.ignoreUrls.length; i++) {
            	String ignoreUrl=ignoreUrls[i].replaceAll("[\\n\\t\\r]", "");
                ignoreUrls[i] = this.basePath + ignoreUrl;
            }
        }

    }

    /**
     * 验证访问的合法有效性。
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 当前链接地址
        String uri = req.getRequestURI();

        // 为静态文件时，跳过验证处理
        if (isStaticFile(uri)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 验证键值存在检查
        String validateKey = RequestUtils.getValidateKey(req);
        
        // 为忽略地址时，跳过验证处理
        if (isIgnoreUrl(uri)) {
        	//忽略的地址，但是已经登陆过了
        	if(!StringUtils.isBlank(validateKey)){
        		setSession(req,validateKey);
        	}
            chain.doFilter(request, response);
            return;
        }

        
        //拼接url参数
        Enumeration<String> paramNames=req.getParameterNames();
        while(paramNames.hasMoreElements()){
        	String paramName=paramNames.nextElement();
        	String paramValue=req.getParameter(paramName);
        	if(uri.contains("?")){
        		uri=uri+"&"+paramName+"="+paramValue;
        	}else{
        		uri=uri+"?"+paramName+"="+paramValue;
        	}
        }
        //拼接登录成功后回调的地址
        if (StringUtils.isBlank(validateKey)) {
            res.sendRedirect(this.loginUrl+"?returnUrl="+uri);
            return;
        }
        setSession(req,validateKey);
        
        chain.doFilter(request, response);
    }
    private void setSession(HttpServletRequest req,String validateKey){
    	// 分解验证键值
        String[] values = validateKey.split(BaseConstants.COOKIE_VALIDATE_KEY_SPLIT);
        req.setAttribute(BaseConstants.REQ_CUR_USER_ID, values[0]);
        req.setAttribute(BaseConstants.REQ_CUR_USER_NAME, values[1]);
        req.setAttribute(BaseConstants.REQ_CUR_ROLE_USER_ID, values[2]);
    }

    /**
     * 释放过滤器资源。
     */
    public void destroy() {
    }

    /**
     *判断是否为忽略地址。
     */
    protected boolean isIgnoreUrl(String url) {

        // 待判断URL为空时
        if (StringUtils.isBlank(url)) {
            return false;
        }

        // 待判断URL为登陆页面地址时
        if (url.equals(this.loginUrl)) {
            return true;
        }
        
        // 验证忽略地址不存在时
        if (this.ignoreUrls == null || this.ignoreUrls.length < 1) {
            return false;
        }

        // 待判断URL为验证忽略地址时
        for (String ignoreUrl : this.ignoreUrls) {
            if (ignoreUrl.equals(url)) {
                return true;
            }
        }

        return false;
    }
    /**
     *判断是否为静态文件。
     */
    protected boolean isStaticFile(String url) {
    	String[] postfixs=new String[]{".js",".html",".jpg",".css",".png",".gif",".zip"};
        for(String postfix:postfixs) {
        	if(url.toLowerCase().endsWith(postfix.toLowerCase())){
        		return true;
        	}
        }
        return false;
    }
}
