package org.tpri.sc.service.uam;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.org.OrganizationManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.ZTreeView;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党务人员服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年1月8日
 */

@Service("PartyWorkerService")
public class PartyWorkerService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private CCPartyManager ccPartyManager;
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private CCpartyService ccpartyService;
    @Autowired
    private SystemUserService systemUserService;

    /**
     * 
     * <B>方法名称：</B>根据组织ID获取该组织下的所有党务工作者列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月16日
     * @param search
     * @param offset
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<User> getPartyWorkersByCcparty(String search, Integer offset, Integer limit, String ccpartyId, String searchCcpartyIds) {
        List<ZTreeView> trees = ccpartyService.getTreeCCPartyAndLowerLevel(ccpartyId);
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (StringUtils.isEmpty(searchCcpartyIds)) {
            if (trees != null && trees.size() > 0) {
                for (ZTreeView tree : trees) {
                    ccpartyIds.add(tree.getId());
                }
            }
        } else {
            String[] strs = searchCcpartyIds.split(",");
            for (int i = 0; i < strs.length; i++) {
                ccpartyIds.add(strs[i]);
            }
        }
        List<User> workers = new ArrayList<User>();
        List<Object> objs = userManager.getPartyWorkersByCcparty(search, offset, limit, ccpartyIds);
        if(objs!=null && objs.size()>0){
            for(int i=0;i<objs.size();i++){
                User worker = userManager.getUser(String.valueOf(objs.get(i)));
                if(worker!=null){
                    worker.setParentUser(userManager.getUserFromMc(worker.getParentUserId()));
                    UserMc sysUser = userManager.getUserFromMc(worker.getSysUserId());
                    if (!StringUtils.isEmpty(sysUser.getCcpartyId())) {
                        worker.setCcparty(ccPartyManager.getCCPartyFromMc(sysUser.getCcpartyId()));
                    }
                    workers.add(worker);
                }
            }
        }
        return workers;
    }

    /**
     * 
     * <B>方法名称：</B>根据组织ID获取该组织下的所有党务工作者记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月16日
     * @param search
     * @param ccpartyId
     * @return
     */
    public Integer getPartyWorkersTotalByCcparty(String search, String ccpartyId,String searchCcpartyIds) {
        List<ZTreeView> trees = ccpartyService.getTreeCCPartyAndLowerLevel(ccpartyId);
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (StringUtils.isEmpty(searchCcpartyIds)) {
            if (trees != null && trees.size() > 0) {
                for (ZTreeView tree : trees) {
                    ccpartyIds.add(tree.getId());
                }
            }
        } else {
            String[] strs = searchCcpartyIds.split(",");
            for (int i = 0; i < strs.length; i++) {
                ccpartyIds.add(strs[i]);
            }
        }
        return userManager.getPartyWorkersTotalByCcparty(search, ccpartyIds);
    }

    /**
     * 
     * <B>方法名称：</B>根据组织ID获取该组织下的所有党务工作者列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月16日
     * @param ccpartyId
     * @return
     */
    public List<User> getPartyWorkersByCcparty(String ccpartyId) {
        List<User> users = userManager.getPartyWorkersByCcparty(ccpartyId);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                user.setParentUser(userManager.getUserFromMc(user.getParentUserId()));
            }
        }
        return users;
    }

    /**
     * 
     * <B>方法名称：</B>新增党务人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月11日
     * @param loginUser
     * @param paramters
     * @return
     */
    public Map<String, Object> addPartyWorker(JSONObject objs) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(objs.getString("userId"));//被授予党务工作者的用户
        if(user==null){
            ret.put("success", false);
            ret.put("msg", "保存失败，被授予用户为空。");
            return ret;
        }
        User sysUser = userManager.getUser(objs.getString("sysUserId"));//系统用户
        if ( sysUser == null) {
            //该组织没有系统用户需要创建系统用户
            sysUser = systemUserService.addSystemUser(objs.getString("sysUserId"));
        }
        //判断该用户在该组织下是否已经存在党务工作干部身份
        List<User> workers = userManager.getPartyWorkerByCcpartyAndParentUser(sysUser.getId(), user.getId(), null);
        if(workers!=null && workers.size()>0){
            ret.put("success", false);
            ret.put("msg", "该组织下该用户已经添加过党务干部。");
            return ret;
        }
        User partyWorker = new User();//党务工作者
        partyWorker.setId(UUIDUtil.id());
        partyWorker.setParentUserId(user.getId());//所指向的父用户ID
        partyWorker.setSysUserId(sysUser.getId());//系统用户ID
        partyWorker.setSystemNo(BaseConfig.SYSTEM_NO);
        partyWorker.setName(objs.getString("partyWorkerName"));
        partyWorker.setUserType(User.USER_TYPE_1);
        partyWorker.setCreateTime(new Timestamp(System.currentTimeMillis()));
        //党务工作者默认角色授予
        Role role = roleManager.getRoleById(Role.ROLE_PARTYWORKER);
        Set<Role> roles = new HashSet<Role>();
        roles.add(role);
        partyWorker.setRoles(roles);
        userManager.addUser(partyWorker);
        ret.put("success", true);
        ret.put("msg", "已成功保存。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>修改党务人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月11日
     * @param loginUser
     * @param paramters
     * @return
     */
    public Map<String, Object> updatePartyWorker(UserMc loginUser, String paramters) {
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject jodata = JSONObject.fromObject(paramters);
        JSONArray ja = JSONArray.fromObject(jodata.get("data"));
        JSONObject objs = ja.getJSONObject(0);
        User user = userManager.getUser(objs.getString("id"));
        if (user == null) {
            ret.put("success", false);
            ret.put("msg", "保存失败，对象为空。");
            return ret;
        }
        user.setName(objs.getString("name"));
        user.setUpdateUserId(loginUser.getId());
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userManager.updateUser(user);
        ret.put("success", true);
        ret.put("msg", "已成功保存。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取党务人员详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月11日
     * @param id
     * @return
     */
    public User getPartyWorkerById(String id) {
        User user = userManager.getUser(id);
        if (user != null) {
            user.setCcparty(ccPartyManager.getCCPartyFromMc(user.getCcpartyId()));
        }
        return user;
    }

}
