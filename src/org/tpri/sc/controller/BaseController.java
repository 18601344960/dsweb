package org.tpri.sc.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.tpri.sc.entity.sys.EnvironmentId;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.sys.EnvironmentService;
import org.tpri.sc.service.uam.UserService;
import org.tpri.sc.util.BaseConstants;
import org.tpri.sc.util.DateUtil;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>控制器基类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年3月28日
 */
@Controller
public class BaseController {
    public Logger logger = Logger.getLogger(BaseController.class);

    @Autowired
    UserService userService;

    @Autowired
    private EnvironmentService environmentService;

    public JSONObject getJsonPara(HttpServletRequest request) {
        String para = ServletRequestUtils.getStringParameter(request, "para", "{}");
        return JSONObject.fromObject(para);
    }

    /**
     * 获取字符串类型的参数
     * */
    public String getString(HttpServletRequest request, String name) {
        String para = ServletRequestUtils.getStringParameter(request, name, null);
        return para;
    }

    /**
     * 获取整形类型的参数
     * */
    public int getInt(HttpServletRequest request, String name, int defaultValue) {
        int para = ServletRequestUtils.getIntParameter(request, name, defaultValue);
        return para;
    }

    /**
     * 获取整形类型的参数
     * */
    public int getInt(HttpServletRequest request, String name) {
        int para = ServletRequestUtils.getIntParameter(request, name, 0);
        return para;
    }

    /**
     * 获取整形对象类型的参数
     * */
    public Integer getInteger(HttpServletRequest request, String name) {
        Integer para;
        try {
            para = ServletRequestUtils.getIntParameter(request, name);
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
            return null;
        }
        return para;
    }

    /**
     * 获取浮点类型的参数
     * */
    public Float getFloat(HttpServletRequest request, String name) {
        Float para = ServletRequestUtils.getFloatParameter(request, name, new Float(0.0));
        return para;
    }

    /**
     * 获取布尔类型的参数 默认返回false
     * */
    public boolean getBoolean(HttpServletRequest request, String name) {
        boolean para = ServletRequestUtils.getBooleanParameter(request, name, false);
        return para;
    }

    /**
     * 获取时间搓类型的参数
     * */
    public Timestamp getTimestamp(HttpServletRequest request, String name) {
        String para = ServletRequestUtils.getStringParameter(request, name, null);
        if (!StringUtils.isEmpty(para)) {
            return DateUtil.str2Timestamp(para, "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }

    /**
     * 获取日期类型的参数
     * */
    public Date getDate(HttpServletRequest request, String name) {
        String para = ServletRequestUtils.getStringParameter(request, name, null);
        if (!StringUtils.isEmpty(para)) {
            return DateUtil.str2Date(para, "yyyy-MM-dd");
        }
        return null;
    }

    /**
     * 获取登录用户ID
     */
    public String loadUserId(HttpServletRequest request) {
        String userId = (String) request.getAttribute(BaseConstants.REQ_CUR_USER_ID);
        return userId;
    }

    /**
     * 获取登录用户
     */
    public User loadUser(HttpServletRequest request) {
        String userId = loadUserId(request);
        User user = userService.getUser(userId);
        return user;
    }

    /**
     * 根据用户Id获取缓存中的登录用户
     */
    public UserMc loadUserMc(String userId) {
        UserMc user = userService.getUserFromMc(userId);
        return user;
    }

    /**
     * 获取缓存中的登录用户
     */
    public UserMc loadUserMc(HttpServletRequest request) {
        String userId = loadUserId(request);
        UserMc user = userService.getUserFromMc(userId);
        return user;
    }

    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("USER-AGENT");
    }

    /**
     * 获取环境变量值
     */
    public Object getEnvironmentValueById(EnvironmentId id) {
        return environmentService.getEnvironmentValueById(id);
    }

    /**
     * 获取上传文件
     * 
     * @param request 请求
     * @return MultipartFile 上传文件
     */
    public static MultipartFile getUploadFile(HttpServletRequest request) {
        return getUploadFile(request, "file");
    }

    /**
     * 获取上传文件
     * 
     * @param request 请求
     * @param name 文件名
     * @return MultipartFile 上传文件
     */
    public static MultipartFile getUploadFile(HttpServletRequest request, String name) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        return multipartRequest.getFile(name);
    }

    public boolean hasPrivilege(HttpServletRequest request, String privilegeId) {
        String userId = (String) request.getAttribute(BaseConstants.REQ_CUR_ROLE_USER_ID);
        UserMc user = loadUserMc(userId);
        if (user.hasPrivilege(privilegeId)) {
            return true;
        }
        return false;
    }

    public List<String> getAllPrivileges(HttpServletRequest request) {
        UserMc user = loadUserMc(request);
        List<String> allPrivilegeIds = user.getAllPrivilegeIds();
        return allPrivilegeIds;
    }

}
