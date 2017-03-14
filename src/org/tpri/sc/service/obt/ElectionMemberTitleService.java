package org.tpri.sc.service.obt;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.Election;
import org.tpri.sc.entity.obt.ElectionMember;
import org.tpri.sc.entity.obt.ElectionMemberTitle;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.manager.obt.ElectionManager;
import org.tpri.sc.manager.obt.ElectionMemberManager;
import org.tpri.sc.manager.obt.ElectionMemberTitleManager;
import org.tpri.sc.manager.sys.CodeManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.uam.PartyWorkerService;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.UUIDUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举领导班子成员党内职务服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月2日
 */
@Service("ElectionMemberTitleService")
public class ElectionMemberTitleService {

    public Logger logger = Logger.getLogger(ElectionMemberTitleService.class);

    @Autowired
    private ElectionManager electionManager;
    @Autowired
    private ElectionMemberManager electionMemberManager;
    @Autowired
    private ElectionMemberTitleManager electionMemberTitleManager;
    @Autowired
    private CodeManager codeManager;
    @Autowired
    private PartyWorkerService partyWorkerService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;

    /**
     * <B>方法名称：</B>添加领导班子成员党内职务<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param objs
     */
    public void addElectionMemberTitle(Map<String, Object> param) {
        String memberId = (String) param.get("memberId");
        ElectionMember electionMember = electionMemberManager.getElectionMemberById(memberId);
        Election election = electionManager.getElectionById(electionMember.getElectionId());

        ElectionMemberTitle memberTitle = new ElectionMemberTitle();
        memberTitle.setId(UUIDUtil.id());
        memberTitle.setMemberId(memberId);
        String partyTitleId = (String) param.get("partyTitleId");
        Code partyTitle = codeManager.getCode("A070101." + partyTitleId);
        memberTitle.setPartyTitleId(partyTitleId);
        int sequence = (int) param.get("sequence");
        if (sequence == 10000) {
            if (partyTitle != null) {
                sequence = partyTitle.getOrderNo() * 10;
            }
        }
        memberTitle.setSequence(sequence);
        electionMemberTitleManager.add(memberTitle);
        String userId = electionMember.getUserId();
        User user = userManager.getUser(userId);
        if (user != null && partyTitle != null) {
            User sysUser = userManager.getUserByLoginNo(election.getCcpartyId());
            if (sysUser != null) {
                //判断该用户是否已是党务干部，如果不是新增
                List<User> workers = userManager.getPartyWorkerByCcpartyAndParentUser(sysUser.getId(), userId, null);
                if (workers != null && workers.size() >0){
                    for (User worker : workers) {
                        //查询该用户的领导头衔
                        List<ElectionMemberTitle> titles = electionMemberTitleManager.getMemberTitlesByMemberId(electionMember.getId());
                        if (titles != null && titles.size() > 0) {
                            String names = "";
                            for (ElectionMemberTitle title : titles) {
                                Code code = codeManager.getCode("A070101." + title.getPartyTitleId());
                                if (code != null) {
                                    if (!StringUtils.isEmpty(names)) {
                                        names += "、" + code.getName();
                                    } else {
                                        names += code.getName();
                                    }
                                }
                            }
                            worker.setName(names);
                        } else {
                            worker.setName("党务干部");
                        }
                        userManager.updateUser(worker);
                    }
                }
                
            }
        }
    }

    /**
     * <B>方法名称：</B>删除领导班子成员党内职务<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param memberId
     * @return
     */
    public boolean deleteElectionMemberTitle(String memberTitleId) {
        ElectionMemberTitle electionMemberTitle = electionMemberTitleManager.getMemberTitleById(memberTitleId);
        if (electionMemberTitle == null) {
            return false;
        }
        electionMemberTitleManager.deleteElectionMemberTitle(memberTitleId);
        List<ElectionMemberTitle> titles = electionMemberTitleManager.getMemberTitlesByMemberId(electionMemberTitle.getMemberId());
        ElectionMember member = electionMemberManager.getElectionMemberById(electionMemberTitle.getMemberId());
        if(member!=null){
            User worker = userManager.getUser(member.getWorkerId());
            if(worker==null){
                Election election = electionManager.getElectionById(member.getElectionId());
                if(election!=null){
                    List<User> workers = userManager.getPartyWorkerByCcpartyAndParentUser(election.getCcpartyId(), member.getUserId(), null);
                    if(workers!=null && workers.size()>0){
                        worker = workers.get(0);
                    }
                    
                }
            }
            if(worker!=null){
                if (titles != null && titles.size() >0) {
                    String names = "";
                    for (ElectionMemberTitle title : titles) {
                        Code code = codeManager.getCode("A070101." + title.getPartyTitleId());
                        if (code != null) {
                            if (!StringUtils.isEmpty(names)) {
                                names += "、" + code.getName();
                            } else {
                                names += code.getName();
                            }
                        }
                    }
                    worker.setName(names);
                }else{
                    worker.setName("党务干部");
                }
                userManager.updateUser(worker);
            }
        }
        return true;
    }

    /**
     * <B>方法名称：</B>获取某班子成员的党内职务列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param memberId
     * @return
     */
    public List<ElectionMemberTitle> getElectionMemberTitleList(String memberId) {
        return electionMemberTitleManager.getMemberTitlesByMemberId(memberId);
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织的党内职务<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月4日
     * @param userId
     * @param ccpartyId
     * @return
     */
    public String getCcpartyElectionTitle(String userId, String ccpartyId) {
        String title = "";
        List<Object> userTitles = electionMemberTitleManager.getUserElectionTitles(userId, ccpartyId);
        if (userTitles != null && userTitles.size() > 0) {
            TreeMap<String, String> titleMap = new TreeMap<String, String>();
            for (int i = 0; i < userTitles.size(); i++) {
                Object[] objs = (Object[]) userTitles.get(i);
                if (StringUtils.isEmpty(titleMap.get(objs[0]))) {
                    titleMap.put((String) objs[0], (String) objs[3]);
                } else {
                    titleMap.put((String) objs[0], "、" + objs[3]);
                }
            }
            Iterator it = titleMap.keySet().iterator();
            while (it.hasNext()) {
                title += titleMap.get(it.next());
            }
        }
        return title;
    }

}
