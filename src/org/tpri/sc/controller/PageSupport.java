package org.tpri.sc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.sys.Enumeration;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.sys.CodeManager;
import org.tpri.sc.manager.sys.EnumerationManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.BaseConstants;
import org.tpri.sc.util.RequestUtils;
import org.tpri.sc.util.ResponseUtils;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>页面帮助类<BR>
 * <B>概要说明：</B> 。 用于将一些参数在页面加载时传递给前台页面。
 * 1、将所有参数放在一个JSONObject中，可以放字符串、数字、数组、List、Map等
 * 2、前台的Global会将JSONObject中的属性转换成Global对象自己的属性。
 * 3、跳转至某个页面前，可以将个性化的信息通过request.setAttribute
 * (BaseConstants.PAGE_GLOBAL_VALUES_IDENTIFIER, Map xxx)
 * 方法放入request中，本类会自动将其转换成Global的属性 <BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月5日
 */
public class PageSupport extends BaseController {

    /**
     * 构建要传给前台页面的属性
     */
    public static JSONObject buildAttributes(HttpServletRequest request, HttpServletResponse response) {

        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());

        JSONObject jo = new JSONObject();
        // 将各页面自定义的信息传递给Global
        Map globalValues = (Map) request.getAttribute(BaseConstants.PAGE_GLOBAL_VALUES_IDENTIFIER);
        if (globalValues != null && globalValues instanceof Map) {
            jo.putAll(globalValues);
        }
        String validateKey = RequestUtils.getValidateKey(request);
        if (!StringUtils.isBlank(validateKey)) {
            setSession(request, response, validateKey);
        }
        jo.put("loginKeyValues", RequestUtils.getLoginNo(request));
        // 用户信息
        String userId = (String) request.getAttribute(BaseConstants.REQ_CUR_USER_ID);
        String roleUserId = (String) request.getAttribute(BaseConstants.REQ_CUR_ROLE_USER_ID);
        if (userId != null && !userId.equals("")) {
            UserManager userManager = (UserManager) applicationContext.getBean("UserManager");
            CCPartyManager ccpartyManager = (CCPartyManager) applicationContext.getBean("CCPartyManager");
            UserMc user = userManager.getUserFromMc(userId);
            if (user != null) {
                jo.put("userId", user.getId());
                jo.put("userName", user.getName());

                UserMc currentUser = null;
                UserMc roleUser = userManager.getUserFromMc(roleUserId);
                if (roleUserId != null && !roleUserId.equals("") && !userId.equals(roleUserId) && roleUser != null) {
                    jo.put("userType", roleUser.getUserType());
                    jo.put("userTitle", roleUser.getName());
                    List<String> allPrivilegeIds = roleUser.getAllPrivilegeIds();
                    JSONArray ja = new JSONArray();
                    for (String privilegeId : allPrivilegeIds) {
                        ja.add(privilegeId);
                    }
                    jo.put("allPrivilegeIds", ja);
                    currentUser = userManager.getUserFromMc(roleUser.getSysUserId());
                } else {
                    if (user.getUserType() == User.USER_TYPE_2) {
                        jo.put("userTitle", "系统用户");
                    }
                    jo.put("userType", user.getUserType());
                    List<String> allPrivilegeIds = user.getAllPrivilegeIds();
                    JSONArray ja = new JSONArray();
                    for (String privilegeId : allPrivilegeIds) {
                        ja.add(privilegeId);
                    }
                    jo.put("allPrivilegeIds", ja);

                    currentUser = user;
                }
                if (currentUser != null) {
                    String ccpartyId = currentUser.getCcpartyId();
                    jo.put("ccpartyId", currentUser.getCcpartyId());
                    CCParty ccparty = ccpartyManager.getCCPartyFromMc(currentUser.getCcpartyId());
                    if (ccparty != null) {
                        jo.put("ccpartyName", ccparty.getName());
                        jo.put("ccpartyType", ccparty.getType());
                        jo.put("partyType", ccparty.getPartyType());
                    } else {
                        jo.put("ccpartyName", "");
                    }
                    if (ccpartyId.equals(BaseConfig.INITIAL_CCPARTYID)) {
                        jo.put("rootCcparty", true);
                    } else {
                        jo.put("rootCcparty", false);
                    }
                }
            }
        } else {
            jo.put("userId", "");
            jo.put("userName", "未登录用户");
        }

        Map<String, String> enumerationsMap = getEnumerations(applicationContext);
        jo.put("enums", enumerationsMap);

        Map<String, String> codeMap = getCodes(applicationContext);
        jo.put("codes", codeMap);
        return jo;
    }

    private static void setSession(HttpServletRequest request, HttpServletResponse response, String validateKey) {
        String roleUserId = ServletRequestUtils.getStringParameter(request, "roleUserId", null);
        if (roleUserId != null && !roleUserId.equals("")) {
            String[] values = validateKey.split(BaseConstants.COOKIE_VALIDATE_KEY_SPLIT);
            // 设定验证键值
            StringBuffer newValidateKey = new StringBuffer();
            newValidateKey.append(values[0]);
            newValidateKey.append(BaseConstants.COOKIE_VALIDATE_KEY_SPLIT);
            newValidateKey.append(values[1]);
            newValidateKey.append(BaseConstants.COOKIE_VALIDATE_KEY_SPLIT);
            newValidateKey.append(roleUserId);
            ResponseUtils.setValidateKey(response, newValidateKey.toString());
            validateKey = newValidateKey.toString();
        }
        // 分解验证键值
        String[] values = validateKey.split(BaseConstants.COOKIE_VALIDATE_KEY_SPLIT);
        request.setAttribute(BaseConstants.REQ_CUR_USER_ID, values[0]);
        request.setAttribute(BaseConstants.REQ_CUR_USER_NAME, values[1]);
        request.setAttribute(BaseConstants.REQ_CUR_ROLE_USER_ID, values[2]);
    }

    // 获取枚举
    private static Map<String, String> getEnumerations(ApplicationContext applicationContext) {
        EnumerationManager enumerationManager = (EnumerationManager) applicationContext.getBean("EnumerationManager");
        List<Enumeration> enumerations = enumerationManager.getEnumerationList();
        Map<String, String> enumerationsMap = new HashMap<String, String>();
        for (Enumeration enumeration : enumerations) {
            enumerationsMap.put(enumeration.getId(), enumeration.getName());
        }
        return enumerationsMap;
    }

    // 获取代码表数据
    private static Map<String, String> getCodes(ApplicationContext applicationContext) {
        CodeManager codeManager = (CodeManager) applicationContext.getBean("CodeManager");
        List<Code> codes = codeManager.getCodeList();
        Map<String, String> codeMap = new HashMap<String, String>();
        for (Code code : codes) {
            codeMap.put(code.getId(), code.getName());
        }
        return codeMap;
    }

}
