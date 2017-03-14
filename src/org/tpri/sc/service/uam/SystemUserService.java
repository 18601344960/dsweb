package org.tpri.sc.service.uam;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.MD5Util;
import org.tpri.sc.view.ZTreeView;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>系统用户服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年1月11日
 */

@Service("SystemUserService")
public class SystemUserService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private CCPartyManager ccPartyManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private CCpartyService ccpartyService;

    /**
     * <B>方法名称：</B>获取系统用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月11日
     * @param start
     * @param limit
     * @return
     */
    public List<User> getSystemUsers(String search, Integer start, Integer limit, String ccpartyId) {
        List<ZTreeView> trees = ccpartyService.getTreeCCPartyOneSelfAndSon(ccpartyId);
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (trees != null && trees.size() > 0) {
            for (ZTreeView tree : trees) {
                ccpartyIds.add(tree.getId());
            }
        }
        List<User> users = userManager.getSystemUsers(search, start, limit, ccpartyIds);
        for (User user : users) {
            user.setCcparty(ccPartyManager.getCCPartyFromMc(user.getCcpartyId()));
        }
        return users;
    }

    /**
     * <B>方法名称：</B>获取系统用户记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月11日
     * @return
     */
    public Integer getSystemUsersTotal(String search, String ccpartyId) {
        List<ZTreeView> trees = ccpartyService.getTreeCCPartyOneSelfAndSon(ccpartyId);
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (trees != null && trees.size() > 0) {
            for (ZTreeView tree : trees) {
                ccpartyIds.add(tree.getId());
            }
        }
        return userManager.getSystemUsersTotal(search, ccpartyIds);
    }

    /**
     * 
     * <B>方法名称：</B>新增系统用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月12日
     * @param loginUser
     * @param paramters
     * @return
     */
    public Map<String, Object> addSystemUser(UserMc loginUser, JSONObject objs) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = new User();
        user.setId(objs.getString("ccpartyId"));
        user.setSystemNo(BaseConfig.SYSTEM_NO);
        user.setLoginNo(objs.getString("loginNo"));
        user.setPassword(MD5Util.md5(User.PASSWORD_DEFAULT));
        user.setName(objs.getString("name"));
        user.setGender(objs.getInt("gender"));
        user.setCreateUserId(loginUser.getId());
        user.setUserType(User.USER_TYPE_2);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));

        Set<Role> roles = new HashSet<Role>();
        if (!StringUtils.isEmpty(objs.getString("ccpartyId"))) {
            user.setCcpartyId(objs.getString("ccpartyId"));
        }
        //分配系统角色
        roles.add(roleManager.getRoleById(Role.ROLE_ADMINISTRATOR));
        user.setRoles(roles);
        userManager.addUser(user);
        ret.put("success", true);
        ret.put("msg", "已成功保存。");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>根据组织新增系统用户<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年8月23日 	
     * @param ccpartyId
     * @return
     */
    public User addSystemUser(String ccpartyId){
        if(StringUtils.isEmpty(ccpartyId)){
            return null;
        }
        CCParty ccparty = ccPartyManager.getCCPartyById(ccpartyId);
        if(ccparty==null){
            return null;
        }
        User user = new User();
        user.setId(ccparty.getId());
        user.setSystemNo(BaseConfig.SYSTEM_NO);
        user.setLoginNo(ccparty.getId());
        user.setPassword(MD5Util.md5(User.PASSWORD_DEFAULT));
        user.setCcpartyId(ccparty.getId());
        user.setUserType(User.USER_TYPE_2);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setName(ccparty.getName());
        //分配系统角色
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleManager.getRoleById(Role.ROLE_ADMINISTRATOR));
        user.setRoles(roles);
        userManager.addUser(user);
        return user;
    }

    /**
     * 
     * <B>方法名称：</B>修改系统用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月12日
     * @param loginUser
     * @param paramters
     * @return
     */
    public Map<String, Object> updateSystemUser(UserMc loginUser, JSONObject objs) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(objs.getString("id"));
        if (user == null) {
            ret.put("success", false);
            ret.put("msg", "保存失败，对象为空。");
            return ret;
        }
        user.setGender(objs.getInt("gender"));
        user.setName(objs.getString("name"));

        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<Role>();
        }
        user.setCcpartyId(StringUtils.isEmpty(objs.getString("ccpartyId")) ? "" : objs.getString("ccpartyId"));
        //分配系统角色
        roles.add(roleManager.getRoleById(Role.ROLE_ADMINISTRATOR));
        user.setRoles(roles);
        userManager.updateUser(user);

        //查询由系统用户创建的党务人员 将各组织保持一致
        List<User> users = userManager.getPartyWorkersBySysUser(user.getId());
        if (users != null && users.size() > 0) {
            for (User worker : users) {
                worker.setCcpartyId(user.getCcpartyId());
                userManager.updateUser(worker);
            }
        }

        ret.put("success", true);
        ret.put("msg", "已成功保存。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取系统用户详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月12日
     * @param id
     * @return
     */
    public User getSystemUserById(String id) {
        User user = userManager.getUser(id);
        if (user != null) {
            user.setCcparty(ccPartyManager.getCCPartyFromMc(user.getCcpartyId()));
        }
        return user;
    }

    /**
     * 
     * <B>方法名称：</B>重置系统角色<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月12日
     * @param id
     * @return
     */
    public Map<String, Object> resetSystemUserRole(String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(id);
        if (user == null) {
            ret.put("success", false);
            ret.put("msg", "重置角色失败，为获取用户。");
            return ret;
        }
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleManager.getRoleById(Role.ROLE_ADMINISTRATOR));
        user.setRoles(roles);
        userManager.updateUser(user);
        ret.put("success", true);
        ret.put("msg", "角色重置成功。");
        return ret;
    }
    /**
     * <B>方法名称：</B>获取下级系统用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年3月29日
     * @param start
     * @param limit
     * @return
     */
    public List<User> getChildSystemUsers(String search, Integer start, Integer limit, String ccpartyId) {
        List<CCParty> ccparties = ccPartyManager.getTreeCCPartySon(ccpartyId);
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (ccparties != null && ccparties.size() > 0) {
            for (CCParty ccparty : ccparties) {
                ccpartyIds.add(ccparty.getId());
            }
        }
        List<User> users = userManager.getChildSystemUsers(search, start, limit, ccpartyIds);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user.getCcpartyId() != null) {
                    user.setCcparty(ccPartyManager.getCCPartyFromMc(user.getCcpartyId()));
                }
            }
        }
        return users;
    }

    /**
     * <B>方法名称：</B>获取下级系统用户记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年3月29日
     * @return
     */
    public Integer getChildSystemUsersTotal(String search, String ccpartyId) {
        List<CCParty> ccparties = ccPartyManager.getTreeCCPartySon(ccpartyId);
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (ccparties != null && ccparties.size() > 0) {
            for (CCParty ccparty : ccparties) {
                ccpartyIds.add(ccparty.getId());
            }
        }
        return userManager.getChildSystemUsersTotal(search, ccpartyIds);
    }
}
