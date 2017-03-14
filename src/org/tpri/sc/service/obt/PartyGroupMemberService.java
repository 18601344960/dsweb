package org.tpri.sc.service.obt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.obt.PartyGroupMember;
import org.tpri.sc.entity.org.CcpartyGroup;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.PartyGroupMemberManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.org.CcpartyGroupManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.uam.PartyWorkerService;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.UUIDUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党小组成员服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年8月1日
 */
@Service("PartyGroupMemberService")
public class PartyGroupMemberService {

    public Logger logger = Logger.getLogger(PartyGroupMemberService.class);

    @Autowired
    private PartyGroupMemberManager partyGroupMemberManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private PartyWorkerService partyWorkerService;
    @Autowired
    private CcpartyGroupManager ccpartyGroupManager; 
    @Autowired
    private RoleManager roleManager;

    /**
     * <B>方法名称：</B>获取党小组下的成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param groupId
     * @return
     */
    public List<PartyGroupMember> getPartyGroupMemberList(String groupId) {
        List<PartyGroupMember> groupMembers = partyGroupMemberManager.getPartyGroupMemberByGroupId(groupId);
        for (PartyGroupMember groupMember : groupMembers) {
            UserMc user = userManager.getUserFromMc(groupMember.getUserId());
            groupMember.setUser(user);
        }
        return groupMembers;
    }

    /**
     * <B>方法名称：</B>获取党小组下的成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param groupId
     * @return
     */
    public int getPartyGroupMemberTotalByGroupId(String groupId) {
        return partyGroupMemberManager.getPartyGroupMemberTotalByGroupId(groupId);
    }

    /**
     * <B>方法名称：</B>添加党小组成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param param
     */
    public boolean addPartyGroupMember(Map<String, Object> param) {
        String groupId = (String) param.get("groupId");
        JSONArray userIds = JSONArray.fromObject((String) param.get("userIds"));
        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.getString(i);
            List<PartyGroupMember> members = partyGroupMemberManager.getPartyGroupMember(groupId, userId);
            if (members != null && members.size() > 0) {
                continue;
            }
            PartyGroupMember member = new PartyGroupMember();
            member.setId(UUIDUtil.id());
            member.setGroupId(groupId);
            member.setUserId(userId);
            member.setType((int) param.get("type"));
            member.setSequence((int) param.get("sequence"));
            partyGroupMemberManager.add(member);
        }
        return true;
    }

    /**
     * <B>方法名称：</B>移除党小组成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param memberId
     * @return
     */
    public boolean removePartyGroupMember(PartyGroupMember partyGroupMember) {
        //判断在该组织下是否存在党务工作者
        CcpartyGroup group = ccpartyGroupManager.getCcpartyGroupById(partyGroupMember.getGroupId());
        if(group!=null){
            List<User> workers = userManager.getPartyWorkerByCcpartyAndParentUser(group.getCcpartyId(),partyGroupMember.getUserId(), null);
            if(workers!=null && workers.size()>=0){
                for (User worker : workers) {
                    //删除
                    userManager.deleteUser(worker);
                }
            }
        }
        return partyGroupMemberManager.delete(partyGroupMember.getId(),ObjectType.OBT_PARTY_GROUP_MEMBER);
    }

    /**
     * <B>方法名称：</B>获取党小组成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月28日
     * @param memberId
     * @return
     */
    public PartyGroupMember getPartyGroupMemberById(String memberId) {
        PartyGroupMember partyGroupMember = partyGroupMemberManager.getPartyGroupMemberById(memberId);
        String userId = partyGroupMember.getUserId();
        partyGroupMember.setUser(userManager.getUserFromMc(userId));
        return partyGroupMember;
    }

    /**
     * <B>方法名称：</B>设为组长<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月3日
     * @param memberId
     * @return
     */
    public boolean setGroupLeader(String memberId) {
        PartyGroupMember partyGroupMember = partyGroupMemberManager.getPartyGroupMemberById(memberId);
        partyGroupMember.setType(PartyGroupMember.TYPE_2);
        partyGroupMemberManager.saveOrUpdate(partyGroupMember);
        //判断在该组织下是否存在党务工作者
        CcpartyGroup group = ccpartyGroupManager.getCcpartyGroupById(partyGroupMember.getGroupId());
        if(group!=null){
            List<User> workers = userManager.getPartyWorkerByCcpartyAndParentUser(group.getCcpartyId(),partyGroupMember.getUserId(), null);
            if(workers==null || workers.size()==0){
                //创建党务工作者
                User partyWorker = new User();//党务工作者
                partyWorker.setId(UUIDUtil.id());
                partyWorker.setParentUserId(partyGroupMember.getUserId());//所指向的父用户ID
                partyWorker.setSysUserId(group.getCcpartyId());//系统用户ID
                partyWorker.setSystemNo(BaseConfig.SYSTEM_NO);
                partyWorker.setName("党务干部");
                partyWorker.setUserType(User.USER_TYPE_1);
                partyWorker.setCreateTime(new Timestamp(System.currentTimeMillis()));
                //党务工作者默认角色授予
                Role role = roleManager.getRoleById(Role.ROLE_PARTYWORKER);
                Set<Role> roles = new HashSet<Role>();
                roles.add(role);
                partyWorker.setRoles(roles);
                userManager.addUser(partyWorker);
            }
        }
        return true;
    }

    /**
     * <B>方法名称：</B>设为副组长<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月3日
     * @param memberId
     * @return
     */
    public boolean setGroupDeputyLeader(String memberId) {
        PartyGroupMember partyGroupMember = partyGroupMemberManager.getPartyGroupMemberById(memberId);
        partyGroupMember.setType(PartyGroupMember.TYPE_1);
        partyGroupMemberManager.saveOrUpdate(partyGroupMember);
        //判断在该组织下是否存在党务工作者
        CcpartyGroup group = ccpartyGroupManager.getCcpartyGroupById(partyGroupMember.getGroupId());
        if(group!=null){
            List<User> workers = userManager.getPartyWorkerByCcpartyAndParentUser(group.getCcpartyId(),partyGroupMember.getUserId(), null);
            if(workers==null || workers.size()==0){
                //创建党务工作者
                User partyWorker = new User();//党务工作者
                partyWorker.setId(UUIDUtil.id());
                partyWorker.setParentUserId(partyGroupMember.getUserId());//所指向的父用户ID
                partyWorker.setSysUserId(group.getCcpartyId());//系统用户ID
                partyWorker.setSystemNo(BaseConfig.SYSTEM_NO);
                partyWorker.setName("党务干部");
                partyWorker.setUserType(User.USER_TYPE_1);
                partyWorker.setCreateTime(new Timestamp(System.currentTimeMillis()));
                //党务工作者默认角色授予
                Role role = roleManager.getRoleById(Role.ROLE_PARTYWORKER);
                Set<Role> roles = new HashSet<Role>();
                roles.add(role);
                partyWorker.setRoles(roles);
                userManager.addUser(partyWorker);
            }
        }
        return true;
    }

    /**
     * <B>方法名称：</B>设为成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月3日
     * @param memberId
     * @return
     */
    public boolean setGroupMember(String memberId) {
        PartyGroupMember partyGroupMember = partyGroupMemberManager.getPartyGroupMemberById(memberId);
        partyGroupMember.setType(PartyGroupMember.TYPE_0);
        partyGroupMemberManager.saveOrUpdate(partyGroupMember);
        return true;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取党小组信息<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年8月8日 	
     * @param ccpartyId
     * @param ret
     */
    public void setCcpartyGroupInfoToPartyCard(String ccpartyId,ModelAndView ret){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<CcpartyGroup> groups = ccpartyGroupManager.getCcpartyGroupList(null, null, ccpartyId, null);
        if(groups!=null && groups.size()>0){
            for (CcpartyGroup group : groups) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("GROUP_NAME", group.getName());
                List<PartyGroupMember> members = partyGroupMemberManager.getPartyGroupMemberByGroupId(group.getId());
                if(members!=null && members.size()>0){
                    int num = 0;
                    String leaderName = "";
                    String assistantLeaderName = "";
                    for (PartyGroupMember member : members) {
                        if(member.getType()==PartyGroupMember.TYPE_2){
                            //组长
                            UserMc user = userManager.getUserFromMc(member.getUserId());
                            if(user!=null){
                                if(StringUtils.isEmpty(leaderName)){
                                    leaderName = user.getName();
                                }else{
                                    leaderName += "、"+user.getName();
                                }
                            }
                        }else if(member.getType()==PartyGroupMember.TYPE_1){
                            //副组长
                            UserMc user = userManager.getUserFromMc(member.getUserId());
                            if(user!=null){
                                if(StringUtils.isEmpty(assistantLeaderName)){
                                    assistantLeaderName = user.getName();
                                }else{
                                    assistantLeaderName += "、"+user.getName();
                                }
                            }
                        }
                        num += 1;
                    }
                    map.put("LEADER_NAME", leaderName);
                    map.put("ASSISTANT_LEADER_NAME", assistantLeaderName);
                    map.put("PARTYMEMBER_NUM", num);
                }else{
                    map.put("LEADER_NAME", "");
                    map.put("ASSISTANT_LEADER_NAME", "");
                    map.put("PARTYMEMBER_NUM", 0);
                }
                list.add(map);
            }
        }else{
            for (int i = 0; i < 8; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("GROUP_NAME", "");
                map.put("LEADER_NAME", "");
                map.put("ASSISTANT_LEADER_NAME", "");
                map.put("PARTYMEMBER_NUM", 0);
                list.add(map);
            }
        }
        
        ret.addObject("detailData", list);
    }
}
