package org.tpri.sc.controller.uam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.uam.UserService;
import org.tpri.sc.util.BaseConstants;
import org.tpri.sc.util.MD5Util;
import org.tpri.sc.util.RequestUtils;
import org.tpri.sc.util.ResponseUtils;
import org.tpri.sc.view.uam.RoleUserView;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>登录控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月30日
 */
@Controller
@RequestMapping("/uam")
public class LoginController extends BaseController {

    @Resource(name = "UserService")
    private UserService userService;

    /**
     * <B>方法名称：</B>登录验证<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年4月30日
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("loginValidate")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) {
        logger.debug(this.getClass() + " loginValidate begin");
        Map<String, Object> ret = new HashMap<String, Object>();

        String loginNo = getString(request, "userId"); //账号
        String password = getString(request, "password");//密码
        if (StringUtils.isBlank(loginNo)) {
            ret.put("success", false);
            ret.put("msg", "请输入用户名！");
            return ret;
        }

        if (StringUtils.isBlank(password)) {
            ret.put("success", false);
            ret.put("msg", "请输入密码！");
            return ret;
        }
        //将密码进行md5加密
        password = MD5Util.md5(password);
        UserMc user = userService.getUserByLoginNoAndPwd(request, loginNo, password);
        if (user == null) {
            ret.put("success", false);
            ret.put("msg", "用户名或者密码不正确！");
            return ret;
        } else {
            this.initLoginInfo(request, response, user);
            ret.put("success", true);
            ret.put("useType", user.getUserType());
            ret.put("msg", "登录成功！");
        }
        logger.debug(this.getClass() + " login end");
        return ret;
    }

    /**
     * <B>方法名称：</B>初始化登录信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月19日
     * @param request
     * @param response
     * @param user
     * @return
     */
    private boolean initLoginInfo(HttpServletRequest request, HttpServletResponse response, UserMc user) {
        // 设定验证键值
        StringBuffer validateKey = new StringBuffer();
        validateKey.append(user.getId());
        validateKey.append(BaseConstants.COOKIE_VALIDATE_KEY_SPLIT);
        validateKey.append(user.getName());
        validateKey.append(BaseConstants.COOKIE_VALIDATE_KEY_SPLIT);
        String roleUserId = user.getId();
        List<RoleUserView> views = userService.getRoleUsers(user.getId());
        if (views != null && views.size() == 1) {
            RoleUserView view = views.get(0);
            roleUserId = view.getId();
        } else if (views != null && views.size() > 1) {
            for (int i = views.size() - 1; i >= 0; i--) {
                RoleUserView view = views.get(i);
                if (view.getCcpartyId() != null && view.getCcpartyId().equals(user.getCcpartyId())) {
                    roleUserId = view.getId();
                    break;
                }
                if (i == views.size() - 1) {
                    roleUserId = view.getId();
                }
            }
        }
        validateKey.append(roleUserId);
        ResponseUtils.setValidateKey(response, validateKey.toString());
        //将登陆标识保存session
        request.getSession().setAttribute(BaseConstants.REQ_CUR_USER_ID, user.getId());

        String loginNo = getString(request, "userId");
        String password = getString(request, "password");
        boolean rememberPassword = getBoolean(request, "rememberPassword");
        String loginKeyValues = "";
        if (rememberPassword) {
            loginKeyValues = loginNo + ":" + password + ";";
        } else {
            loginKeyValues = loginNo + ":;";
        }

        String loginNos = RequestUtils.getLoginNo(request);
        if (loginNos != null && !loginNos.equals("")) {
            String keyValues[] = loginNos.split(";");
            int num = 0;
            for (String keyValue : keyValues) {
                if (keyValue != null && !keyValue.equals("")) {
                    String key = keyValue.split(":")[0];
                    if (!key.equals(loginNo)) {
                        loginKeyValues += keyValue + ";";
                    }
                    num++;
                }
                if (num >= 5) {
                    break;
                }
            }
        }
        //将登录名和密码对放入cookie中
        ResponseUtils.setLoginNo(response, loginKeyValues);
        return true;
    }

    /**
     * <B>方法名称：</B>登出<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月19日
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("logout")
    @ResponseBody
    public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) {
        logger.debug(this.getClass() + " logout begin");

        ResponseUtils.setCookieValue(response, BaseConstants.COOKIE_VALIDATE_KEY, null, 0);
       /* //将cookie中的登录密码置空
        String loginKeyValues = "";
        String loginNos = RequestUtils.getLoginNo(request);
        if (loginNos != null && !loginNos.equals("")) {
            String keyValues[] = loginNos.split(";");
            int num = 0;
            for (String keyValue : keyValues) {
                if (keyValue != null && !keyValue.equals("")) {
                    String key = keyValue.split(":")[0];
                    if (num == 0) {
                        loginKeyValues += key + ":;";
                    } else {
                        loginKeyValues += keyValue + ";";
                    }
                    num++;
                }
            }
        }
        //将登录名和密码对放入cookie中
        ResponseUtils.setLoginNo(response, loginKeyValues);
*/
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "登出成功！");
        logger.debug(this.getClass() + " logout end");
        return ret;
    }
    
    /**
     * <B>方法名称：</B>删除登录历史记录<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年11月4日    
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("deleteLogonNoHistory")
    @ResponseBody
    public Map<String, Object> deleteLogonNoHistory(HttpServletRequest request, HttpServletResponse response) {
        logger.debug(this.getClass() + " deleteLogonNoHistory begin");
        String userId = getString(request, "userId");

        String loginKeyValues = "";
        String loginNos = RequestUtils.getLoginNo(request);
        if (loginNos != null && !loginNos.equals("")) {
            String keyValues[] = loginNos.split(";");
            for (String keyValue : keyValues) {
                if (keyValue != null && !keyValue.equals("")) {
                    String key = keyValue.split(":")[0];
                    if (key != null && !key.equals("") && !key.equals(userId)) {
                        loginKeyValues += keyValue + ";";
                    }
                }
            }
        }
        //将登录名和密码对放入cookie中
        ResponseUtils.setLoginNo(response, loginKeyValues);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        logger.debug(this.getClass() + " deleteLogonNoHistory end");
        return ret;
    }
}
