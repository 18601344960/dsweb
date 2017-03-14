package org.tpri.sc.service.obt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.obt.Conference;
import org.tpri.sc.entity.obt.ConferenceFormat;
import org.tpri.sc.entity.obt.ConferenceLabel;
import org.tpri.sc.entity.obt.ConferenceOrgnizer;
import org.tpri.sc.entity.obt.ConferenceParticipants;
import org.tpri.sc.entity.obt.ConferenceStep;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.ConferenceCategoryManager;
import org.tpri.sc.manager.obt.ConferenceFormatManager;
import org.tpri.sc.manager.obt.ConferenceLabelManager;
import org.tpri.sc.manager.obt.ConferenceManager;
import org.tpri.sc.manager.obt.ConferenceOrgnizerManager;
import org.tpri.sc.manager.obt.ConferenceParticipantsManager;
import org.tpri.sc.manager.obt.ConferenceStepManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.HTMLUtil;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.obt.PartyMemberCardPageView;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织生活参会人员服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月24日
 */
@Service("ConferenceParticipantsService")
public class ConferenceParticipantsService {

    public Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ConferenceParticipantsManager conferenceParticipantsManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ConferenceCategoryManager conferenceCategoryManager;
    @Autowired
    private ConferenceLabelManager branchConferenceCategoryManager;
    @Autowired
    private ConferenceLabelManager conferenceLabelManager;
    @Autowired
    private ConferenceStepManager conferenceStepManager;
    @Autowired
    private ConferenceManager conferenceManager;
    @Autowired
    private ConferenceOrgnizerManager conferenceOrgnizerManager;
    @Autowired
    private ConferenceFormatManager conferenceFormatManager;

    /**
     * <B>方法名称：</B>增加组织生活本组织参与人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param param
     * @return
     */
    public boolean addConferenceInParticipants(Map<String, Object> param) {
        JSONArray userIds = JSONArray.fromObject((String) param.get("userIds"));
        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.getString(i);
            String conferenceId = (String) param.get("conferenceId");
            ConferenceParticipants participant = conferenceParticipantsManager.getConferenceParticipantsByConferenceAndUser(conferenceId, userId);
            if (participant == null) {
                participant = new ConferenceParticipants();
                participant.setId(UUIDUtil.id());
                participant.setConferenceId((String) param.get("conferenceId"));
                participant.setUserId(userId);
                UserMc user = userManager.getUserFromMc(userId);
                if (user != null) {
                    participant.setUserName(user.getName());
                }
                participant.setStatus(ConferenceParticipants.STATUS_0);
                participant.setType(ConferenceParticipants.TYPE_0);
                conferenceParticipantsManager.add(participant);

                Conference conference = conferenceManager.getConference(conferenceId);
                conference.setAttendance(conference.getAttendance() + 1);
                conferenceManager.saveOrUpdate(conference);
            }
        }
        return true;
    }

    /**
     * <B>方法名称：</B>增加组织生活组织外参与人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param param
     * @return
     */
    public boolean addConferenceOutParticipants(Map<String, Object> param) {
        JSONArray userNames = JSONArray.fromObject((String) param.get("userNames"));
        for (int i = 0; i < userNames.size(); i++) {
            String userName = userNames.getString(i);
            ConferenceParticipants participant = new ConferenceParticipants();
            participant.setId(UUIDUtil.id());
            String conferenceId = (String) param.get("conferenceId");
            participant.setConferenceId(conferenceId);
            participant.setUserName(userName);
            participant.setStatus(ConferenceParticipants.STATUS_0);
            participant.setType(ConferenceParticipants.TYPE_1);
            conferenceParticipantsManager.add(participant);

            Conference conference = conferenceManager.getConference(conferenceId);
            conference.setAttendance(conference.getAttendance() + 1);
            conferenceManager.saveOrUpdate(conference);
        }
        return true;
    }

    /**
     * <B>方法名称：</B>获取某组织生活的参与人员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param offset
     * @param limit
     * @param conferenceId
     * @param status
     * @return
     */
    public List<ConferenceParticipants> getConferenceParticipants(Integer offset, Integer limit, String conferenceId) {
        List<ConferenceParticipants> participants = conferenceParticipantsManager.getConferenceParticipants(offset, limit, conferenceId);
        return participants;
    }

    /**
     * <B>方法名称：</B>获取某组织生活的参与人员总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param conferenceId
     * @return
     */
    public Integer getConferenceParticipantsTotal(String conferenceId) {
        return conferenceParticipantsManager.getConferenceParticipantsTotal(conferenceId);
    }

    /**
     * <B>方法名称：</B>删除组织生活参与人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param request
     * @return
     */
    public boolean deleteConferenceParticipants(String id) {
        ConferenceParticipants conferenceParticipants = conferenceParticipantsManager.getConferenceParticipants(id);
        if (conferenceParticipants == null) {
            return false;
        }
        String conferenceId = conferenceParticipants.getConferenceId();
        conferenceParticipantsManager.delete(conferenceParticipants.getId(),ObjectType.OBT_CONFERENCE_PARTICIPANTS);

        Conference conference = conferenceManager.getConference(conferenceId);
        if (conference.getAttendance() > 0) {
            conference.setAttendance(conference.getAttendance() - 1);
            conferenceManager.saveOrUpdate(conference);
        }
        return true;
    }

    /**
     * 
     * @Description: 获取党员的电子活动证分页数
     * @param userId
     * @param page 分页实体
     * @return void
     */

    /**
     * 
     * <B>方法名称：</B>获取党员的电子活动证中 组织生活的页数处理<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月22日
     * @param userId
     * @param page
     */
    public void getCardPagesForBranchConference(String userId, String paramter, PartyMemberCardPageView page) {
        JSONObject objs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            objs = ja.getJSONObject(0);
        }
        int total = 0; //总记录
        total = conferenceParticipantsManager.getBranchConferenceTotalByUser(userId, objs);
        if (total > 2) {
            //两条记录以上处理分页
            page.setActivityPages((int) Math.ceil((double) total / 2)); //凑整 比如 2.1 为 3
            page.setPages(page.getPages() + page.getActivityPages() - 1); //计算总页数 
            page.setActivityEndPage(page.getActivityBeginPage() + page.getActivityPages());
        }
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户参加的支部工作分类数详情<BR>
     * <B>概要说明：</B>党员电子活动证使用<BR>
     * 
     * @author 赵子靖
     * @since 2016年3月22日
     * @param userId
     * @return
     */
    public String getStatisticsDetailBranchConference(String userId, String paramter) {
        JSONObject objs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            objs = ja.getJSONObject(0);
        }
        StringBuffer totalStr = new StringBuffer();
        Map<String, Integer> resultMap = new HashMap<String, Integer>();//记录map
        Integer total = conferenceParticipantsManager.getBranchConferenceTotalByUser(userId, objs);
        totalStr.append("共参加 " + total + " 次组织生活活动。");
        return totalStr.toString();
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户参加的支部工作列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月22日
     * @param userId
     * @param start
     * @param limit
     * @return
     */
    public List<Conference> getBranchConferenceListByUser(Integer offset, Integer limit, String userId, String paramter) {
        JSONObject objs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            objs = ja.getJSONObject(0);
        }
        List<Conference> branchConferences = new ArrayList<Conference>();
        List<Object> conferenceIds = conferenceParticipantsManager.getBranchConferenceListByUser(offset, limit, userId, objs);
        if (conferenceIds != null && conferenceIds.size() > 0) {
            for (int i = 0; i < conferenceIds.size(); i++) {
                Object obj = conferenceIds.get(i);
                Conference conference = conferenceManager.getConference(String.valueOf(conferenceIds.get(i)));
                if (conference != null) {
                    //获取组织者
                    List<ConferenceOrgnizer> conferenceOrgnizers = conferenceOrgnizerManager.getOrgnizerByConferenceId(conference.getId());
                    conference.setConferenceOrgnizers(conferenceOrgnizers);
                    String orgnizers = "";
                    for (ConferenceOrgnizer conferenceOrgnizer : conferenceOrgnizers) {
                        orgnizers = orgnizers + conferenceOrgnizer.getUserName() + "；";
                    }
                    if (orgnizers.length() > 0) {
                        orgnizers = orgnizers.substring(0, orgnizers.length() - 1);
                    }
                    conference.setOrgnizers(orgnizers);
                    //获取参会信息
                    List<ConferenceParticipants> conferenceParticipants = conferenceParticipantsManager.getParticipantsByConferenceId(conference.getId(), ConferenceParticipants.STATUS_0);
                    conference.setConferenceParticipants(conferenceParticipants);
                    String participants = "";
                    for (ConferenceParticipants conferenceParticipant : conferenceParticipants) {
                        participants = participants + conferenceParticipant.getUserName() + "；";
                    }
                    if (participants.length() > 0) {
                        participants = participants.substring(0, participants.length() - 1);
                    }
                    conference.setParticipants(participants);
                    //所属标签
                    List<ConferenceLabel> labels = conferenceLabelManager.getLabelsByConferenceId(conference.getId(), null);
                    if (labels != null && labels.size() > 0) {
                        for (ConferenceLabel label : labels) {
                            label.setCategory(conferenceCategoryManager.getConferenceCategoryById(label.getCategoryId()));
                        }
                    }
                    conference.setConferenceLabels(labels);
                    //工作步骤
                    List<ConferenceStep> steps = conferenceStepManager.getConferenceStepByConferenceId(conference.getId(), null);
                    if (steps != null && steps.size() > 0) {
                        for (ConferenceStep step : steps) {
                            step.setCategory(conferenceCategoryManager.getConferenceCategoryById(step.getCategoryId()));
                        }
                    }
                    conference.setConferenceSteps(steps);
                    //所属生活形式
                    List<ConferenceFormat> conferenceFormats = conferenceFormatManager.getConferenceFormatByConferenceId(conference.getId(), null);
                    for (ConferenceFormat conferenceFormat : conferenceFormats) {
                        conferenceFormat.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceFormat.getCategoryId()));
                    }
                    conference.setConferenceFormats(conferenceFormats);
                    branchConferences.add(conference);
                }
            }
        }
        return branchConferences;
    }

    /**
     * 
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月28日
     * @param ccpartyId
     * @param paramter
     */
    public void getConferencesByUserForMemberCard(String userId, String paramter, ModelAndView ret) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT);
        List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
        JSONObject jsonObjs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            jsonObjs = ja.getJSONObject(0);
        }
        List<Object> objs = conferenceParticipantsManager.getBranchConferenceListByUser(null, null, userId, jsonObjs);
        if (objs != null && objs.size() > 0) {
            for (Object obj : objs) {
                Map<String, Object> map = new HashMap<String, Object>();
                Conference conference = conferenceManager.getConference((String) obj);
                if (conference == null) {
                    continue;
                }
                map.put("TITLE", conference.getName());
                map.put("DATE", conference.getOccurTime() != null ? sdf.format(conference.getOccurTime()) : "");
                map.put("ADDRESS", conference.getAddress());
                //获取组织者
                List<ConferenceOrgnizer> conferenceOrgnizers = conferenceOrgnizerManager.getOrgnizerByConferenceId(conference.getId());
                conference.setConferenceOrgnizers(conferenceOrgnizers);
                String orgnizers = "";
                for (ConferenceOrgnizer conferenceOrgnizer : conferenceOrgnizers) {
                    orgnizers = orgnizers + conferenceOrgnizer.getUserName() + "；";
                }
                if (orgnizers.length() > 0) {
                    orgnizers = orgnizers.substring(0, orgnizers.length() - 1);
                }
                map.put("CHAIR", orgnizers);
                //获取参会信息
                List<ConferenceParticipants> conferenceParticipants = conferenceParticipantsManager.getParticipantsByConferenceId(conference.getId(), ConferenceParticipants.STATUS_0);
                String participants = "";
                for (ConferenceParticipants conferenceParticipant : conferenceParticipants) {
                    participants = participants + conferenceParticipant.getUserName() + "；";
                }
                if (participants.length() > 0) {
                    participants = participants.substring(0, participants.length() - 1);
                }
                map.put("PARTICIPANTS", participants);
                if (conferenceParticipants != null && conferenceParticipants.size() > 0) {
                    map.put("PARTICIPANTNUM", conferenceParticipants.size());
                } else {
                    map.put("PARTICIPANTNUM", 0);
                }
                map.put("CONTENT", HTMLUtil.delHTMLTag(conference.getContent(), true));
                subList.add(map);
            }
        }
        ret.addObject("conferences", subList);
    }
}
