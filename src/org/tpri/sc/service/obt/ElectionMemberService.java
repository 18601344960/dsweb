package org.tpri.sc.service.obt;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.sc.entity.obt.Election;
import org.tpri.sc.entity.obt.ElectionMember;
import org.tpri.sc.entity.obt.ElectionMemberTitle;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.ElectionManager;
import org.tpri.sc.manager.obt.ElectionMemberManager;
import org.tpri.sc.manager.obt.ElectionMemberTitleManager;
import org.tpri.sc.manager.sys.CodeManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.uam.PartyWorkerService;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.UUIDUtil;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举领导班子成员服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年10月23日
 */
@Service("ElectionMemberService")
public class ElectionMemberService {

    public Logger logger = Logger.getLogger(ElectionMemberService.class);

    @Autowired
    private ElectionMemberManager electionMemberManager;
    @Autowired
    private ElectionMemberTitleManager electionMemberTitleManager;
    @Autowired
    private ElectionMemberTitleService electionMemberTitleService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ElectionManager electionManager;
    @Autowired
    private CodeManager codeManager;
    @Autowired
    private RoleManager roleManager;

    public List<ElectionMember> getElectionMemberList(String electionId) {
        List<ElectionMember> electionMembers = electionMemberManager.getElectionMemberByElectionId(electionId);
        for (ElectionMember electionMember : electionMembers) {
            UserMc user = userManager.getUserFromMc(electionMember.getUserId());
            if (user != null && electionMember.getUserType() == ElectionMember.USERTYPE_0) {
                electionMember.setName(user.getName());
                electionMember.setGender(user.getGender());
                electionMember.setBirthDay(user.getBirthDay());
            } else {
                user = new UserMc();
                user.setName(electionMember.getUserName());
                user.setGender(electionMember.getGender());
                user.setBirthDay(electionMember.getBirthDay());
            }
            electionMember.setUser(user);
            List<ElectionMemberTitle> electionMemberTitles = electionMemberTitleManager.getMemberTitlesByMemberId(electionMember.getId());
            electionMember.setMemberTitles(electionMemberTitles);
        }
        return electionMembers;
    }

    /**
     * <B>方法名称：</B>添加领导班子成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param objs
     */
    public void addElectionMember(Map<String, Object> param) {
        ElectionMember member = new ElectionMember();
        member.setId(UUIDUtil.id());
        member.setElectionId((String) param.get("electionId"));
        String userId = (String) param.get("userId");
        User user = userManager.getUser(userId);
        if (user == null) {
            //系统外用户
            member.setUserType(ElectionMember.USERTYPE_1);
            member.setUserName((String) param.get("userName"));
            member.setGender((int) param.get("gender"));
            member.setBirthDay(param.get("birthDay") != null ? (Date) param.get("birthDay") : null);
        } else {
            //系统内用户
            member.setUserType(ElectionMember.USERTYPE_0);
            member.setUserId(user.getId());
            member.setUserName(user.getName());
            member.setGender(user.getGender());
            member.setBirthDay(user.getBirthDay());
            //查询是否存在党务干部，如果不存在则创建
            Election election = electionManager.getElectionById((String) param.get("electionId"));
            List<User> workers = userManager.getPartyWorkerByCcpartyAndParentUser(election.getCcpartyId(), userId, null);
            if(workers==null || workers.size()==0){
                User partyWorker = new User();//党务工作者
                String workerId = UUIDUtil.id();
                partyWorker.setId(workerId);
                partyWorker.setParentUserId(user.getId());//所指向的父用户ID
                partyWorker.setSysUserId(election.getCcpartyId());//系统用户ID
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
                member.setWorkerId(workerId);
            }
        }
        electionMemberManager.add(member);
    }

    /**
     * <B>方法名称：</B>更新班子成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param loginUser
     * @param param
     * @return
     */
    public boolean updateElectionMember(UserMc loginUser, Map<String, Object> param) {
        String memberId = (String) param.get("memberId");
        ElectionMember electionMember = electionMemberManager.getElectionMemberById(memberId);
        if (electionMember == null) {
            return false;
        }
        electionMember.setStartTime((Date) param.get("startTime"));
        electionMember.setEndTime((Date) param.get("endTime"));
        electionMember.setRemark((String) param.get("remark"));
        electionMember.setSequence((Integer)param.get("sequence"));
        return electionMemberManager.saveOrUpdate(electionMember);
    }

    /**
     * <B>方法名称：</B>删除班子成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param memberId
     * @return
     */
    public boolean removeElectionMember(String memberId) {
        ElectionMember member = electionMemberManager.getElectionMemberById(memberId);
        if (member == null) {
            return false;
        }
        List<ElectionMemberTitle> memberTitles = electionMemberTitleManager.getMemberTitlesByMemberId(memberId);
        for (ElectionMemberTitle memberTitle : memberTitles) {
            electionMemberTitleService.deleteElectionMemberTitle(memberTitle.getId());
        }
        User worker = userManager.getUser(member.getWorkerId());
        if(worker!=null){
            userManager.deleteUser(worker);
        }
        return electionMemberManager.deleteElectionMember(memberId);
    }

    /**
     * <B>方法名称：</B>获取领导班子成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月28日
     * @param memberId
     * @return
     */
    public ElectionMember getElectionMemberById(String memberId) {
        ElectionMember electionMember = electionMemberManager.getElectionMemberById(memberId);
        String userId = electionMember.getUserId();
        electionMember.setUser(userManager.getUserFromMc(userId));
        List<ElectionMemberTitle> electionMemberTitles = electionMemberTitleManager.getMemberTitlesByMemberId(memberId);
        electionMember.setMemberTitles(electionMemberTitles);
        return electionMember;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织的最终委员班子集合<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月30日
     * @param ccpartyId
     * @return
     */
    public List<ElectionMember> getElectionMembersByCcparty(String ccpartyId) {
        Election election = electionManager.getCurrentElection(ccpartyId);
        if (election == null) {
            return null;
        }
        List<ElectionMember> electionMembers = electionMemberManager.getElectionMemberByElectionId(election.getId());
        for (ElectionMember electionMember : electionMembers) {
            UserMc user = userManager.getUserFromMc(electionMember.getUserId());
            if (user == null) {
                user = new UserMc();
                user.setName(electionMember.getUserName());
                user.setGender(electionMember.getGender());
            }
            electionMember.setUser(user);
            List<ElectionMemberTitle> memberTitles = electionMemberTitleManager.getMemberTitlesByMemberId(electionMember.getId());
            if(memberTitles!=null && memberTitles.size()>0){
                for (ElectionMemberTitle electionMemberTitle : memberTitles) {
                    electionMemberTitle.setCode(codeManager.getCode("A070101."+electionMemberTitle.getPartyTitleId()));
                }
            }
            electionMember.setMemberTitles(memberTitles);
        }
        return electionMembers;
    }

    /**
     * 
     * <B>方法名称：</B>获取某人在某组织下的党内职务<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月25日
     * @return
     */
    public ElectionMember getElectionMemberByCcpartyAndUser(String ccpartyId, String userId) {
        List<ElectionMember> electionMembers = getElectionMembersByCcparty(ccpartyId);
        if (electionMembers != null && electionMembers.size() > 0) {
            for (ElectionMember member : electionMembers) {
                if (member.getUserId().equals(userId)) {
                    return member;
                }
            }
        }
        return null;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取该组织下的换届选举信息<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年7月28日 	
     * @param ccpartyId
     * @param ret
     */
    public void getElectionMembersByCcpartyForPartyCard(String ccpartyId,ModelAndView ret) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT);
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Election election = electionManager.getCurrentElection(ccpartyId);
        if (election == null) {
            return;
        }
        List<ElectionMember> electionMembers = electionMemberManager.getElectionMemberByElectionId(election.getId());
        if(electionMembers!=null && electionMembers.size()>0){
            for (ElectionMember electionMember : electionMembers) {
                List<ElectionMemberTitle> titles = electionMemberTitleManager.getMemberTitlesByMemberId(electionMember.getId());
                if(titles!=null && titles.size()>0){
                    for (ElectionMemberTitle title : titles) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        Code code = codeManager.getCode("A070101."+title.getPartyTitleId());
                        if(code!=null){
                            map.put("PARTY_TITLE_ID", code.getName());
                        }else{
                            map.put("PARTY_TITLE_ID", "无");
                        }
                        User user = userManager.getUser(electionMember.getUserId());
                        if(user!=null){
                            map.put("NAME", user.getName());
                            map.put("GENDER", user.getGender()==1?"男":"女");
                            map.put("BIRTHDAY", user.getBirthDay()!=null?sdf.format(user.getBirthDay()):"");
                            map.put("JOIN_TIME", user.getPartyMember().getJoinTime()!=null?sdf.format(user.getPartyMember().getJoinTime()):"");
                            map.put("DESCRIPTION", "");
                        }else{
                            map.put("NAME", electionMember.getUserName());
                            map.put("GENDER", electionMember.getGender()==1?"男":"女");
                            map.put("BIRTHDAY", electionMember.getBirthDay()!=null?sdf.format(electionMember.getBirthDay()):"");
                            map.put("JOIN_TIME", "");
                            map.put("DESCRIPTION", "");
                        }
                        list.add(map);
                    }
                }
            }
            if(list.size()<7){
                for(int i=0;i<=7-list.size();i++){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("PARTY_TITLE_ID", "");
                    map.put("NAME", "");
                    map.put("GENDER", "");
                    map.put("BIRTHDAY", "");
                    map.put("JOIN_TIME", "");
                    map.put("DESCRIPTION", "");
                    list.add(map);
                }
            }
        }
        if(list==null || list.size()==0){
            for(int i=0;i<7;i++){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("PARTY_TITLE_ID", "");
                map.put("NAME", "");
                map.put("GENDER", "");
                map.put("BIRTHDAY", "");
                map.put("JOIN_TIME", "");
                map.put("DESCRIPTION", "");
                list.add(map);
            }
        }
        ret.addObject("ELECTION_START_DATE", election.getStartTime()!=null?sdf.format(election.getStartTime()):"暂无");
        ret.addObject("mainData", list);
    }

}
