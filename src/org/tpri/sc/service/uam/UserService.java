package org.tpri.sc.service.uam;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tpri.sc.entity.obt.Election;
import org.tpri.sc.entity.obt.ElectionMember;
import org.tpri.sc.entity.obt.ElectionMemberTitle;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.ElectionManager;
import org.tpri.sc.manager.obt.ElectionMemberManager;
import org.tpri.sc.manager.obt.ElectionMemberTitleManager;
import org.tpri.sc.manager.obt.PartyMemberManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.org.OrganizationManager;
import org.tpri.sc.manager.sys.CodeManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.service.org.OrganizationService;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.ClientHostInfo;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.MD5Util;
import org.tpri.sc.view.ZTreeView;
import org.tpri.sc.view.uam.RoleUserView;
import org.tpri.sc.view.uam.UserRoleView;

/**
 * @description 用户服务类
 * @author 易文俊
 * @since 2015-04-09
 */

@Service("UserService")
public class UserService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private PartyMemberManager partyMemberManager;
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private CCpartyService ccpartyService;
    @Autowired
    private ElectionMemberManager electionMemberManager;
    @Autowired
    private ElectionMemberTitleManager electionMemberTitleManager;
    @Autowired
    private CodeManager codeManager;
    @Autowired
    private ElectionManager electionManager;

    /**
     * 获取用户
     * 
     * @return
     */
    public User getUser(String id) {
        return userManager.getUser(id);
    }

    /**
     * 从缓存获取用户
     * 
     * @return
     */
    public UserMc getUserFromMc(String id) {
        return userManager.getUserFromMc(id);
    }

    /**
     * 
     * <B>方法名称：</B>根据用户登录账号和密码获取信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月25日
     * @param loginNo
     * @param password
     * @return
     */
    public UserMc getUserByLoginNoAndPwd(HttpServletRequest request, String loginNo, String password) {
        User user = userManager.getUserByLoginNoAndPwd(loginNo, password);
        if (user != null) {
            user.setLastLoginIp(ClientHostInfo.getIPAddress(request));
            user.setLastLoginTime(user.getThisLoginTime());
            user.setThisLoginTime(new Timestamp(System.currentTimeMillis()));
            user.setLoginCount(user.getLoginCount() + 1);
            userManager.updateUser(user);
            UserMc userMc = userManager.getUserFromMc(user.getId());
            return userMc;
        }
        return null;
    }

    /**
     * 
     * @Description: 根据身份证获取用户
     * @param idNumber
     * @return
     */
    public List<User> loadUserByIdnumber(String idNumber) {
        return userManager.getUsersByIdnumber(idNumber);
    }

    /**
     * 
     * <B>方法名称：</B>删除用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月3日
     * @param id
     * @return
     */
    public Map<String, Object> deleteUser(String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(id);
        if (user == null) {
            ret.put("success", false);
            ret.put("msg", "删除失败，用户对象为空。");
            return ret;
        }
        userManager.delete(user);
        // 删除党员
        PartyMember member = partyMemberManager.getPartyMember(id);
        if (member != null) {
            partyMemberManager.delete(member);
        }
        ret.put("success", true);
        ret.put("msg", "用户删除成功。");
        return ret;
    }

    public boolean updatePwd(User loginUser, String newPwd) {
        User user = userManager.getUser(loginUser.getId());
        user.setPassword(newPwd);
        userManager.updateUser(user);
        return true;

    }

    public boolean editUserRole(String userId, String roleIds) {
        User user = userManager.getUser(userId);
        List<Role> list = roleManager.getRoleListByIds(roleIds);
        user.setRoles(new HashSet<>(list));
        userManager.updateUser(user);
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的党员用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月3日
     * @param ccpartyId
     * @return
     */
    public List<ZTreeView> getUsersByCcparty(String ccpartyId) {
        List<User> users = userManager.getUserByCcparty(ccpartyId);
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        for (User user : users) {
            ZTreeView tree = new ZTreeView();
            tree.setId(user.getId());
            tree.setName(user.getName());
            tree.setAttr3(user.getGender());
            tree.setpId(ccpartyId);
            tree.setOpen(true);
            tree.setIcon("images/ztree/user.png");
            trees.add(tree);
        }
        return trees;
    }

    /**
     * 
     * @Description: 保存头像
     * @param userId
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public boolean saveHeadImg(String userId, MultipartFile multipartFile) throws IOException {
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        User user = userManager.getUser(userId);
        if (user == null) {
            return false;
        }
        user.setIcon(multipartFile.getBytes());
        userManager.saveOrUpdate(user);
        return true;
    }

    /**
     * 
     * @Description: 下载头像
     * @param userId
     * @param outputStream
     * @return
     * @throws IOException
     */
    public boolean getHeadImg(String userId, ServletOutputStream outputStream) throws IOException {
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        User user = userManager.getUser(userId);
        if (user == null) {
            return false;
        }
        if (user.getIcon() == null || user.getIcon().length == 0) {
            return false;
        }
        outputStream.write(user.getIcon());
        return true;
    }

    /**
     * @Description: 重置密码
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public Map<String, Object> resetUserPassword(String id, String oldPassword, String newPassword) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(id);
        String oldPwd = MD5Util.md5(oldPassword);
        if (user != null) {
            if (oldPwd.equals(user.getPassword())) {
                // 将密码进行md5加密
                user.setPassword(MD5Util.md5(newPassword));
                userManager.updateUser(user);
                ret.put("success", true);
                ret.put("msg", "密码已重置");
            } else {
                ret.put("success", false);
                ret.put("msg", "原始密码不正确");
            }
        } else {
            ret.put("success", false);
            ret.put("msg", "用户不存在");
        }
        return ret;
    }

    /**
     * @Description: 管理员重置密码
     * @param id
     * @param newPassword 新密码
     * @return
     */
    public Map<String, Object> resetUserPasswordByManager(String id, String newPassword) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(id);
        if (user != null) {
            // 将密码进行md5加密
            user.setPassword(MD5Util.md5(newPassword));
            userManager.updateUser(user);
            ret.put("success", true);
            ret.put("msg", "密码已重置");
        } else {
            ret.put("success", false);
            ret.put("msg", "用户不存在");
        }
        return ret;
    }

    /**
     * 
     * @Description: 保存用户角色设置
     * @param userId
     * @param roles
     * @return
     */
    public Map<String, Object> saveUserRoles(String userId, String roleIdsStr) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(userId);
        if (user == null) {
            ret.put("success", false);
            ret.put("msg", "用户不存在");
            return ret;
        }
        Set<Role> roles = new HashSet<Role>();
        String[] roleIds = roleIdsStr.split(",");
        // 使用hibernate关联对象自动处理
        for (String roleId : roleIds) {
            Role role = roleManager.getRoleById(roleId);
            if (role != null) {
                roles.add(role);
            }
        }
        user.setRoles(roles);
        userManager.updateUser(user);
        ret.put("success", true);
        ret.put("msg", "用户角色保存成功");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>验证身份证号码是否占用<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月17日
     * @param idNumber
     * @return
     */
    public Map<String, Object> checkUserIdNumber(String idNumber) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUserByIdNumber(idNumber);
        if (user == null) {
            ret.put("success", true);
        } else {
            ret.put("success", false);
            ret.put("msg", "身份证号已被" + user.getName() + "占用，请更换。");
        }
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>验证登录账号是否存在<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月24日
     * @param userId
     * @return
     */
    public Map<String, Object> checkUserId(String userId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(userId);
        if (user == null) {
            ret.put("success", true);
        } else {
            ret.put("success", false);
            ret.put("msg", "登录号已被" + user.getName() + "占用，请更换。");
        }
        return ret;
    }

    /**
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B>获取用户的所有党务角色<BR>
     * 
     * @author 易文俊
     * @since 2016年3月15日
     * @param userId
     * @return
     */
    public List<RoleUserView> getRoleUsers(String userId) {
        List<RoleUserView> views = new ArrayList<RoleUserView>();
        List<User> users = userManager.getRoleUsers(userId);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                RoleUserView view = new RoleUserView();
                view.setId(user.getId());
                view.setName(user.getName());
                UserMc sysUser = userManager.getUserFromMc(user.getSysUserId());
                if (sysUser != null) {
                    CCParty ccparty = ccpartyManager.getCCPartyFromMc(sysUser.getCcpartyId());
                    if (ccparty != null) {
                        view.setCcpartyId(ccparty.getId());
                        view.setCcpartyName(ccparty.getName());
                    }
                }
                views.add(view);
            }
        }
        return views;
    }

    /**
     * 
     * <B>方法名称：</B>验证登录账号是否占用<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月20日
     * @param loginNo
     * @return
     */
    public Map<String, Object> checkUserLoginNo(String loginNo) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUserByLoginNo(loginNo);
        if (user == null) {
            ret.put("success", true);
        } else {
            ret.put("success", false);
            ret.put("msg", "登录账号已被" + user.getName() + "占用，请更换。");
        }
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>根据系统编号获取普通用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月15日
     * @param search
     * @param offset
     * @param limit
     * @return
     */
    public List<User> getUserPartymembersByCcpartys(String search, Integer offset, Integer limit, String ccpartyIds, String ccpartyId) {
        List<Object> ccpartyIdLists = new ArrayList<Object>();
        if (!StringUtils.isEmpty(ccpartyIds)) {
            String[] ids = ccpartyIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                ccpartyIdLists.add(ids[i]);
            }
        } else {
            //获取组织下的所有子节点组织
            List<ZTreeView> trees = ccpartyService.getTreeCCPartyOneSelfAndSon(ccpartyId);
            if (trees != null && trees.size() > 0) {
                for (ZTreeView tree : trees) {
                    ccpartyIdLists.add(tree.getId());
                }
            }
        }
        List<User> users = partyMemberManager.getUserPartymembersByCcpartys(search, offset, limit, ccpartyIdLists);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user.getPartyMember() != null) {
                    user.setCcparty(ccpartyManager.getCCPartyFromMc(user.getPartyMember().getCcpartyId()));
                }
            }
        }
        return users;
    }

    /**
     * 
     * <B>方法名称：</B>根据系统编号获取普通用户条数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月15日
     * @param search
     * @return
     */
    public Integer getUserPartymembersByCcpartysTotal(String search, String ccpartyIds, String ccpartyId) {
        List<Object> ccpartyIdLists = new ArrayList<Object>();
        if (!StringUtils.isEmpty(ccpartyIds)) {
            String[] ids = ccpartyIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                ccpartyIdLists.add(ids[i]);
            }
        } else {
            //获取组织下的所有子节点组织
            List<ZTreeView> trees = ccpartyService.getTreeCCPartyOneSelfAndSon(ccpartyId);
            if (trees != null && trees.size() > 0) {
                for (ZTreeView tree : trees) {
                    ccpartyIdLists.add(tree.getId());
                }
            }
        }
        return partyMemberManager.getUserPartymembersByCcpartysTotal(search, ccpartyIdLists);
    }

    /**
     * 
     * <B>方法名称：</B>获取用户权限<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月11日
     * @param user
     * @return
     */
    public List<UserRoleView> getUserRolesHas(String id, Boolean showAll) {
        List<UserRoleView> views = new ArrayList<UserRoleView>();
        User user = userManager.getUser(id);
        Set<Role> roles = user.getRoles();//用户拥有的权限
        Map<String, Role> hasRoles = new HashMap<String, Role>();
        List<Role> allRoles = roleManager.getRoleList(null, null, null);
        Iterator<Role> it = roles.iterator();
        while (it.hasNext()) {
            Role role = (Role) it.next();
            hasRoles.put(role.getId(), role);
        }
        for (Role role : allRoles) {
            UserRoleView view = new UserRoleView();
            view.setRole(role);
            if (hasRoles.get(role.getId()) != null) {
                view.setIsHave(true);
            } else {
                view.setIsHave(false);
            }
            views.add(view);
        }
        return views;
    }

    /**
     * 
     * <B>方法名称：</B>保存用户信息 并且返回用户对象<BR>
     * <B>概要说明：</B>党员管理模块中的保存 <BR>
     * 
     * @author 赵子靖
     * @since 2015年12月7日
     * @param loginUser
     * @param objs
     * @param partyMember
     * @return
     */
    public User saveUserInfos(UserMc loginUser, JSONObject objs, PartyMember partyMember, String id) {
        User user = userManager.getUser(id);
        boolean isUpdateOperator = false;
        if (user == null) {
            user = new User();
            user.setId(id);
            user.setSystemNo(BaseConfig.SYSTEM_NO);
            user.setPassword(MD5Util.md5(User.PASSWORD_DEFAULT));
            user.setCreateTime(new Timestamp(System.currentTimeMillis()));
            user.setCreateUserId(loginUser.getId());
        } else {
            isUpdateOperator = true;
            user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            user.setUpdateUserId(loginUser.getId());
        }
        user.setLoginNo(objs.getString("loginNo").trim());
        user.setIdNumber(objs.getString("idNumber"));
        user.setName(objs.getString("name"));
        user.setGender(objs.getInt("gender"));
        user.setNation(objs.getString("nation"));
        user.setBirthPlace(objs.getString("birthPlace"));
        user.setOccupation(objs.getString("occupation"));
        user.setEducation(objs.getString("education"));
        user.setDegree(objs.getString("degree"));
        user.setJobTitle(objs.getString("jobTitle"));
        user.setEmail(objs.getString("email"));
        user.setOfficePhone(objs.getString("officePhone"));
        user.setMobile(objs.getString("mobile"));
        user.setBirthDay(StringUtils.isEmpty(objs.getString("birthday")) ? null : DateUtil.str2Date(objs.getString("birthday"), DateUtil.DEFAULT_FORMAT));
        user.setAddress(objs.getString("address"));
        user.setType(objs.getString("type"));
        user.setSequence(objs.getInt("sequence"));
        user.setPartyMember(partyMember);
        //增加党员、群众权限
        if (user.getRoles() == null || user.getRoles().size() == 0) {
            user.setRoles(new HashSet<Role>());
        }
        user.getRoles().add(roleManager.getRoleById(Role.ROLE_PARTYMEMBER_DEFAULT));
        if (isUpdateOperator) {
            userManager.updateUser(user);
        } else {
            userManager.addUser(user);
        }
        return user;
    }

    /**
     * 
     * <B>方法名称：</B>获取用户信息<BR>
     * <B>概要说明：</B>我的信息模块<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月26日
     * @param userId
     * @return
     */
    public User getUserInfosForMyInfo(String userId) {
        User user = userManager.getUser(userId);
        if (user == null) {
            return null;
        }
        if (user.getPartyMember() != null) {
            user.getPartyMember().setCcparty(ccpartyManager.getCCPartyFromMc(user.getPartyMember().getCcpartyId()));
            //获取该党员的党内职务
            List<ElectionMember> members = electionMemberManager.getElectionMembersByUser(user.getId());
            if (members != null && members.size() > 0) {
                for (ElectionMember electionMember : members) {
                    //查询该届的职务
                    List<ElectionMemberTitle> titles = electionMemberTitleManager.getMemberTitlesByMemberId(electionMember.getId());
                    if(titles!=null && titles.size()>0){
                        for (ElectionMemberTitle title : titles) {
                            Code code = codeManager.getCode("A070101."+title.getPartyTitleId());
                            if(code!=null){
                                String electionMemberTitle = user.getPartyMember().getElectionMemberTitle();
                                user.getPartyMember().setElectionMemberTitle(StringUtils.isEmpty(electionMemberTitle)?code.getName():electionMemberTitle+"、"+code.getName());
                            }
                        }
                    }
                    Election election = electionManager.getElectionById(electionMember.getElectionId());
                    if (election != null) {
                        electionMember.setCcparty(ccpartyManager.getCCPartyFromMc(election.getCcpartyId()));
                    }
                }
                user.getPartyMember().setElectionMembers(members);
            }
        }
        return user;
    }

    /**
     * 
     * <B>方法名称：</B>修改新用户信息<BR>
     * <B>概要说明：</B>个人资料<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月30日
     * @param objs
     * @return
     */
    public Map<String, Object> updateUserLittleInfoForMyInfo(JSONObject objs) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(objs.getString("userId"));
        if (user == null) {
            ret.put("success", false);
            ret.put("msg", "用户信息修改失败。");
            return ret;
        }
        user.setName(objs.getString("name"));
        user.setNamePhoneticize(objs.getString("namePhoneticize"));
        userManager.updateUser(user);
        ret.put("success", true);
        ret.put("msg", "修改成功。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>修改用户基本信息<BR>
     * <B>概要说明：</B>个人资料<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月30日
     * @param objs
     * @return
     */
    public Map<String, Object> updateUserBaseInfoForMyInfo(JSONObject objs) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(objs.getString("userId"));
        if (user == null) {
            ret.put("success", false);
            ret.put("msg", "用户信息修改失败。");
            return ret;
        }
        user.setGender(objs.getInt("gender"));
        user.setBirthDay(StringUtils.isEmpty(objs.getString("birthday")) ? null : DateUtil.str2Date(objs.getString("birthday"), DateUtil.DEFAULT_FORMAT));
        user.setNation(objs.getString("nation"));
        user.setBirthPlace(objs.getString("birthPlace"));
        user.setEducation(objs.getString("education"));
        user.setDegree(objs.getString("degree"));
        user.setMobile(objs.getString("mobile"));
        user.setOfficePhone(objs.getString("officePhone"));
        user.setEmail(objs.getString("email"));
        user.setAddress(objs.getString("address"));
        userManager.updateUser(user);
        ret.put("success", true);
        ret.put("msg", "修改成功。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>删除用户头像<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月30日
     * @param userId
     * @return
     */
    public Map<String, Object> deleteUserHeadImg(String userId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(userId);
        if (user == null) {
            ret.put("success", false);
            ret.put("msg", "头像删除失败。");
            return ret;
        }
        user.setIcon(null);
        userManager.updateUser(user);
        ret.put("success", true);
        ret.put("msg", "头像已成功移除。");
        return ret;
    }
    
    /**
     * 
     * @Description: 根据党组织获取所属用户列表
     * @param ccpartyId
     * @return
     */
    public List<User> getUserByCcparty(String ccpartyId, String search) {
        return userManager.getUsersByCcparty(ccpartyId, search);
    }
}
