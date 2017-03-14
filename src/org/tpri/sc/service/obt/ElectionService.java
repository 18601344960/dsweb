package org.tpri.sc.service.obt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.obt.Election;
import org.tpri.sc.entity.obt.ElectionMember;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.ElectionManager;
import org.tpri.sc.manager.obt.ElectionMemberManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.service.uam.PartyWorkerService;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.UUIDUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */
@Service("ElectionService")
public class ElectionService {

    public Logger logger = Logger.getLogger(ElectionService.class);

    @Autowired
    ElectionManager electionManager;
    @Autowired
    ElectionMemberManager electionMemberManager;
    @Autowired
    CCPartyManager ccpartyManager;
    @Autowired
    private PartyWorkerService partyWorkerService;

    /**
     * 
     * <B>方法名称：</B>添加换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年10月23日
     * @param user
     * @param param
     * @return
     */
    public boolean addElection(UserMc user, Map<String, Object> param) {
        Election election = new Election();
        election.setId(UUIDUtil.id());
        election.setCcpartyId((String) param.get("ccpartyId"));
        election.setSequence((int) param.get("sequence"));
        election.setAgeLimit((int) param.get("ageLimit"));
        election.setSelectMode((int) param.get("selectMode"));
        election.setStartTime((Date) param.get("startTime"));
        election.setEndTime((Date) param.get("endTime"));
        election.setParticipants((int) param.get("participants"));
        election.setAttendance((int) param.get("attendance"));
        election.setCreateUserId(user.getId());
        election.setCreateTime(new Timestamp(System.currentTimeMillis()));
        electionManager.add(election);
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>更新换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年10月23日
     * @param user
     * @param param
     * @return
     */
    public boolean updateElection(UserMc user, Map<String, Object> param) {
        String id = (String) param.get("id");
        Election election = electionManager.getElectionById(id);
        if (election != null) {
            election.setSequence((int) param.get("sequence"));
            election.setAgeLimit((int) param.get("ageLimit"));
            election.setSelectMode((int) param.get("selectMode"));
            election.setParticipants((int) param.get("participants"));
            election.setAttendance((int) param.get("attendance"));
            election.setStartTime((Date) param.get("startTime"));
            election.setEndTime((Date) param.get("endTime"));
            electionManager.saveOrUpdate(election);
            return true;
        }
        return false;
    }

    /**
     * 
     * <B>方法名称：</B>删除换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年10月23日
     * @param user
     * @param id
     * @return
     */
    public boolean deleteElection(UserMc user, String id) {
        //删除换届选举领导班子
        List<ElectionMember> members = electionMemberManager.getElectionMemberByElectionId(id);
        if (members != null && members.size() > 0) {
            for (ElectionMember member : members) {
                electionMemberManager.delete(member.getId(), ObjectType.OBT_ELECTION_MEMBER);
            }
        }
        //删除换届选举
        electionManager.deleteElection(id);
        return true;
    }

    /**
     * <B>方法名称：</B>获取当前的换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月25日
     * @param direction
     * @param ccpartyId
     * @param sequence
     * @return
     */
    public Election getCurrentElection(String ccpartyId) {
        Election election = electionManager.getCurrentElection(ccpartyId);
        return election;
    }

    /**
     * <B>方法名称：</B>获取上一届换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月25日
     * @param ccpartyId
     * @param sequence
     * @return
     */
    public Election getLastElection(String ccpartyId, Integer sequence) {
        Election election = electionManager.getLastElection(ccpartyId, sequence);
        return election;
    }

    /**
     * <B>方法名称：</B>获取下一届换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月25日
     * @param ccpartyId
     * @param sequence
     * @return
     */
    public Election getNextElection(String ccpartyId, Integer sequence) {
        Election election = electionManager.getNextElection(ccpartyId, sequence);
        return election;
    }

    /**
     * 
     * <B>方法名称：</B>获取换届选举最大届次<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年10月23日
     * @param ccpartyId
     * @return
     */
    public Integer getElectionMaxSequence(String ccpartyId) {
        return electionManager.getElectionMaxSequence(ccpartyId);
    }

    public List<Election> getElectionList(String ccpartyId, Integer limit, Integer offset) {
        List<Election> list = electionManager.getElectionList(ccpartyId, limit, offset);
        return list;
    }

    public Integer getElectionTotal(String ccpartyId) {
        return electionManager.getElectionTotal(ccpartyId);
    }

    public Election getElectionById(String id) {
        return electionManager.getElectionById(id);
    }

    /**
     * <B>方法名称：</B>获取换届选举提醒内容<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月4日
     * @param ccpartyId
     * @return
     */
    public String getElectionReminder(String ccpartyId) {
        CCParty ccparty = ccpartyManager.getCCPartyFromMc(ccpartyId);
        Election election = electionManager.getCurrentElection(ccpartyId);
        String tipContent = "提示：";
        if (election != null) {
            Date currentDate = DateUtil.getNow();
            Date endDate = election.getEndTime();
            int day = DateUtil.daysBetween(currentDate, endDate);
            if (day <= 0) {
                tipContent = tipContent + "【" + ccparty.getName() + "】换届选举已到期，到期时间为：【" + DateUtil.date2Str(endDate, DateUtil.DEFAULT_FORMAT + "】，请及时更新换届选举信息！");
            } else if (day > 0 && day < 90) {
                tipContent = tipContent + "【" + ccparty.getName() + "】换届选举还有" + day + "天到期，到期时间为：【" + DateUtil.date2Str(endDate, DateUtil.DEFAULT_FORMAT + "】，请及时换届！");
            } else if (day > 0 && currentDate.getYear() == endDate.getYear()) {
                tipContent = tipContent + "【" + ccparty.getName() + "】换届选举本年度到期，到期时间为：【" + DateUtil.date2Str(endDate, DateUtil.DEFAULT_FORMAT + "】，请及时换届！");
            } else {
                tipContent = "";
            }
        } else {
            tipContent = tipContent + "【" + ccparty.getName() + "】暂无换届选举信息，请及时维护！";
        }
        return tipContent;
    }

    /**
     * <B>方法名称：</B>获取下级组织的换届选举总览<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月5日
     * @param ccpartyId
     * @return
     */
    public List<Election> getElectionSummary(String ccpartyId) {
        List<Election> elections = new ArrayList<Election>();
        if (ccpartyId != null && !ccpartyId.equals("")) {
            List<CCParty> ccparties = ccpartyManager.getCCPartyListByParentId(ccpartyId, CCParty.STATUS_0);
            if (ccparties != null && ccparties.size() > 0) {
                for (CCParty ccparty : ccparties) {
                    Election election = electionManager.getCurrentElection(ccparty.getId());
                    if (election == null) {
                        election = new Election();
                        election.setRemark("暂无");
                        election.setStatus(-1);
                    } else {
                        Date currentDate = DateUtil.getNow();
                        Date endDate = election.getEndTime();
                        int day = DateUtil.daysBetween(currentDate, endDate);
                        if (day <= 0) {
                            election.setRemark("已到期");
                            election.setStatus(1);
                        } else if (day > 0 && day <= 90) {
                            election.setRemark(day + "天后到期");
                            election.setStatus(2);
                        } else if (day > 0 && currentDate.getYear() == endDate.getYear()) {
                            election.setRemark("本年度到期");
                            election.setStatus(3);
                        } else {
                            election.setRemark("正常");
                            election.setStatus(0);
                        }
                    }
                    election.setCcparty(ccparty);
                    elections.add(election);
                }
            }
        }
        return elections;
    }
}
