package org.tpri.sc.controller.uam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.org.Organization;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.org.OrganizationManager;
import org.tpri.sc.service.obt.ElectionMemberTitleService;
import org.tpri.sc.service.uam.RoleService;
import org.tpri.sc.service.uam.UserService;
import org.tpri.sc.view.ZTreeView;
import org.tpri.sc.view.uam.RoleUserView;
import org.tpri.sc.view.uam.UserRoleView;


/**
 * 
 * <B>系统名称：</B>党建系统<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>用户控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年10月11日
 */
@Controller
@RequestMapping("/uam")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private OrganizationManager organizatiomManager;
    @Autowired
    private ElectionMemberTitleService electionMemberTitleService;

    /**
     * 
     * <B>方法名称：</B>删除用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月3日
     * @param request
     * @return
     */
    @RequestMapping("deleteUser")
    @ResponseBody
    public Map<String, Object> delUser(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteUser begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        ret = userService.deleteUser(id);
        logger.debug(this.getClass() + " deleteUser end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取组织下的党员用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月3日
     * @param request
     * @return
     */
    @RequestMapping("getUsersByCcparty")
    @ResponseBody
    public Map<String, Object> getUsersByCcparty(HttpServletRequest request) {
        logger.debug(this.getClass() + " getUsersByCcparty begin");
        String ccpartyId = getString(request, "ccpartyId");
        String search = getString(request, "search");
        if (StringUtils.isEmpty(ccpartyId)) {
            UserMc user = loadUserMc(request);
            ccpartyId = user.getCcpartyId();
        }
        // 获取党组织信息
        List<User> users = userService.getUserByCcparty(ccpartyId, search);
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        for (User user : users) {
            ZTreeView tree = new ZTreeView();
            tree.setId(user.getId());
            tree.setName(user.getName());
            tree.setpId(ccpartyId);
            tree.setOpen(true);
            tree.setIcon("images/ztree/user.png");
            tree.setAttr3(user.getGender());
            trees.add(tree);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", trees);
        logger.debug(this.getClass() + " getUsersByCcparty end");
        return ret;
    }

    /**
     * 
     * @Description: 获取用户信息
     * @param request
     * @return
     */
    @RequestMapping("loadUserInfoByUserId")
    @ResponseBody
    public Map<String, Object> loadUserInfoByUserId(HttpServletRequest request) {
        logger.debug(this.getClass() + " loadUserInfoByUserId begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String userId = getString(request, "userId");
        User user = userService.getUser(userId);
        String partyTitleName = "无";
        if (User.TYPE_01.equals(user.getType()) || User.TYPE_02.equals(user.getType())) {
            if (user.getPartyMember() != null) {
                CCParty ccparty = ccpartyManager.getCCPartyFromMc(user.getPartyMember().getCcpartyId());
                ret.put("names", ccparty.getName());
                ret.put("ids", ccparty.getId());
                ret.put("partyMember", user.getPartyMember());
                //获取党内职务
                partyTitleName = electionMemberTitleService.getCcpartyElectionTitle(user.getId(), user.getPartyMember().getCcpartyId());
            }
        } else if (User.TYPE_13.equals(user.getType())) {
            //群众也可能在党员表中会有信息 比如 申请人、积极分子、发展对象
            if (user.getPartyMember() != null) {
                CCParty ccparty = ccpartyManager.getCCPartyFromMc(user.getPartyMember().getCcpartyId());
                ret.put("names", ccparty.getName());
                ret.put("ids", ccparty.getId());
                ret.put("partyMember", user.getPartyMember());
            }
        }
        ret.put("partyTitleName", partyTitleName);
        ret.put("item", user);
        List<UserRoleView> views = userService.getUserRolesHas(userId, true);
        ret.put("rows", views);
        logger.debug(this.getClass() + " loadUserInfoByUserId end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>更换头像<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月8日
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("replaceHeadImg")
    @ResponseBody
    public Map<String, Object> replaceHeadImg(HttpServletRequest request) throws IOException {
        logger.debug(this.getClass() + " replaceHeadImg begin");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("headImg");
        Map<String, Object> ret = new HashMap<String, Object>();
        String userId = getString(request, "userId");
        if (userService.saveHeadImg(userId, multipartFile)) {
            ret.put("success", true);
            ret.put("msg", "头像上传成功！");
        } else {
            ret.put("success", false);
            ret.put("msg", "头像上传失败！");
        }
        logger.debug(this.getClass() + " replaceHeadImg end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>头像展示<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月8日
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("getHeadImg")
    @ResponseBody
    public Map<String, Object> getHeadImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug(this.getClass() + " getHeadImg begin");
        String userId = getString(request, "userId");
        Map<String, Object> ret = new HashMap<String, Object>();
        request.setCharacterEncoding("UTF-8");
        String filename = "head.jpg";
        String userAgent = request.getHeader("User-Agent");
        byte[] bytes = userAgent.contains("MSIE") ? filename.getBytes() : filename.getBytes("UTF-8"); // name.getBytes("UTF-8")处理safari的乱码问题
        filename = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", filename));
        userService.getHeadImg(userId, response.getOutputStream());
        logger.debug(this.getClass() + " getHeadImg end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>密码重置 用户修改密码<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月8日
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("resetUserPassword")
    @ResponseBody
    public Map<String, Object> resetUserPassword(HttpServletRequest request) throws IOException {
        logger.debug(this.getClass() + " resetUserPassword begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        String oldPassword = getString(request, "oldPassword");
        String newPassword = getString(request, "newPassword");
        ret = userService.resetUserPassword(id, oldPassword, newPassword);
        logger.debug(this.getClass() + " resetUserPassword end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>管理员或党务工作人员强制重置用户密码<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月8日
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("resetUserPasswordByManager")
    @ResponseBody
    public Map<String, Object> resetUserPasswordByManager(HttpServletRequest request) throws IOException {
        logger.debug(this.getClass() + " resetUserPassword begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        String newPassword = getString(request, "newPassword");
        ret = userService.resetUserPasswordByManager(id, newPassword);
        logger.debug(this.getClass() + " resetUserPassword end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>保存用户角色设置<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月8日
     * @param request
     * @return
     */
    @RequestMapping("saveUserRoles")
    @ResponseBody
    public Map<String, Object> saveUserRoles(HttpServletRequest request) {
        logger.debug(this.getClass() + " saveUserRoles begin");
        String userId = getString(request, "userId");
        String roles = getString(request, "roles");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = userService.saveUserRoles(userId, roles);
        logger.debug(this.getClass() + "saveUserRoles end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>验证身份证号码是否存在<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月8日
     * @param request
     * @return
     */
    @RequestMapping("checkUserIdNumber")
    @ResponseBody
    public Map<String, Object> checkUserIdNumber(HttpServletRequest request) {
        logger.debug(this.getClass() + " checkUserIdNumber begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = userService.checkUserIdNumber(getString(request, "idNumber"));
        logger.debug(this.getClass() + " checkUserIdNumber begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>验证用户ID是否存在<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月24日
     * @param request
     * @return
     */
    @RequestMapping("checkUserId")
    @ResponseBody
    public Map<String, Object> checkUserId(HttpServletRequest request) {
        logger.debug(this.getClass() + " checkUserId begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = userService.checkUserId(getString(request, "userId"));
        logger.debug(this.getClass() + " checkUserId begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：获取登录用户信息</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月14日
     * @param request
     * @return
     */
    @RequestMapping("getLoginUserInfos")
    @ResponseBody
    public Map<String, Object> getLoginUserInfos(HttpServletRequest request) {
        logger.debug(this.getClass() + " getLoginUserInfos begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        User loginUser = loadUser(request);
        ret.put("item", loginUser);
        logger.debug(this.getClass() + " getLoginUserInfos begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>验证登录账号是否存在<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月20日
     * @param request
     * @return
     */
    @RequestMapping("checkUserLoginNo")
    @ResponseBody
    public Map<String, Object> checkUserLoginNo(HttpServletRequest request) {
        logger.debug(this.getClass() + " checkUserLoginNo begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String loginNo = getString(request, "loginNo");
        ret = userService.checkUserLoginNo(loginNo);
        logger.debug(this.getClass() + " checkUserLoginNo begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的党员用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月16日
     * @param request
     * @return
     */
    @RequestMapping("getUserPartymembersByCcpartys")
    @ResponseBody
    public Map<String, Object> getUserPartymembersByCcpartys(HttpServletRequest request) {
        logger.debug(this.getClass() + " getUserPartymembersByCcpartys begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String search = getString(request, "search");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String ccpartyId = getString(request, "ccpartyId");
        String ccpartyIds = getString(request, "ccpartyIds");//下拉框选择查询的单位
        List<User> users = userService.getUserPartymembersByCcpartys(search, offset, limit, ccpartyIds, ccpartyId);
        Integer total = userService.getUserPartymembersByCcpartysTotal(search, ccpartyIds, ccpartyId);
        ret.put("rows", users);
        ret.put("total", total);
        logger.debug(this.getClass() + " getUserPartymembersByCcpartys end");
        return ret;
    }

    /**
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B>获取用户的所有党务角色<BR>
     * 
     * @author 易文俊
     * @since 2016年3月15日
     * @param request
     * @return
     */
    @RequestMapping("getRoleUsers")
    @ResponseBody
    public Map<String, Object> getRoleUsers(HttpServletRequest request) {
        logger.debug(this.getClass() + " getRoleUsers begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String userId = getString(request, "userId");
        UserMc user = loadUserMc(request);
        List<RoleUserView> views = userService.getRoleUsers(userId);
        boolean myccparty = false;
        if (views != null && views.size() > 0) {
            for (RoleUserView view : views) {
                if (view.getCcpartyId().equals(user.getCcpartyId())) {
                    myccparty = true;
                }
            }
            if (!myccparty) {
                RoleUserView view = new RoleUserView();
                view.setId(user.getId());
                view.setName("支部成员");
                view.setCcpartyId(user.getCcpartyId());
                CCParty ccparty = ccpartyManager.getCCPartyFromMc(user.getCcpartyId());
                view.setCcpartyName(ccparty.getName());
                views.add(view);
            }
        }
        ret.put("rows", views);
        logger.debug(this.getClass() + " getRoleUsers end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取用户权限<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月12日
     * @param request
     * @return
     */
    @RequestMapping("getUserRoles")
    @ResponseBody
    public Map<String, Object> getUserRoles(HttpServletRequest request) {
        logger.debug(this.getClass() + " getUserRoles begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        List<UserRoleView> views = userService.getUserRolesHas(id, false);
        ret.put("rows", views);
        logger.debug(this.getClass() + " getUserRoles end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取用户信息<BR>
     * <B>概要说明：</B>我的信息模块<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月26日
     * @param request
     * @return
     */
    @RequestMapping("getUserInfosForMyInfo")
    @ResponseBody
    public Map<String, Object> getUserInfosForMyInfo(HttpServletRequest request) {
        logger.debug(this.getClass() + " getUserInfosForMyInfo begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", userService.getUserInfosForMyInfo(getString(request, "userId")));
        logger.debug(this.getClass() + " getUserInfosForMyInfo end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>保存用户信息<BR>
     * <B>概要说明：</B>个人资料<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月30日
     * @param request
     * @return
     */
    @RequestMapping("updateUserLittleInfoForMyInfo")
    @ResponseBody
    public Map<String, Object> updateUserLittleInfoForMyInfo(HttpServletRequest request) {
        logger.debug(this.getClass() + " updateUserLittleInfoForMyInfo begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("userId", getString(request, "userId"));
        objs.put("name", getString(request, "name"));
        objs.put("namePhoneticize", getString(request, "namePhoneticize"));
        ret = userService.updateUserLittleInfoForMyInfo(objs);
        logger.debug(this.getClass() + " updateUserLittleInfoForMyInfo end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>保存用户基本信息<BR>
     * <B>概要说明：</B>个人资料<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月30日
     * @param request
     * @return
     */
    @RequestMapping("updateUserBaseInfoForMyInfo")
    @ResponseBody
    public Map<String, Object> updateUserBaseInfoForMyInfo(HttpServletRequest request) {
        logger.debug(this.getClass() + " updateUserBaseInfoForMyInfo begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("userId", getString(request, "userId"));
        objs.put("gender", getString(request, "gender"));
        objs.put("birthday", getString(request, "birthday"));
        objs.put("nation", getString(request, "nation"));
        objs.put("birthPlace", getString(request, "birthPlace"));
        objs.put("education", getString(request, "education"));
        objs.put("degree", getString(request, "degree"));
        objs.put("mobile", getString(request, "mobile"));
        objs.put("officePhone", getString(request, "officePhone"));
        objs.put("email", getString(request, "email"));
        objs.put("address", getString(request, "address"));
        ret = userService.updateUserBaseInfoForMyInfo(objs);
        logger.debug(this.getClass() + " updateUserBaseInfoForMyInfo end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>删除用户头像<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月30日
     * @param request
     * @return
     */
    @RequestMapping("deleteUserHeadImg")
    @ResponseBody
    public Map<String, Object> deleteUserHeadImg(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteUserHeadImg begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = userService.deleteUserHeadImg(getString(request, "userId"));
        logger.debug(this.getClass() + " deleteUserHeadImg end");
        return ret;
    }
}
