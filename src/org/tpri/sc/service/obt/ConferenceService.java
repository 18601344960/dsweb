package org.tpri.sc.service.obt;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.sc.entity.com.ComFile;
import org.tpri.sc.entity.com.TableIndex;
import org.tpri.sc.entity.obt.Conference;
import org.tpri.sc.entity.obt.ConferenceFormat;
import org.tpri.sc.entity.obt.ConferenceLabel;
import org.tpri.sc.entity.obt.ConferenceOrgnizer;
import org.tpri.sc.entity.obt.ConferenceParticipants;
import org.tpri.sc.entity.obt.ConferencePraise;
import org.tpri.sc.entity.obt.ConferenceStep;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.com.ComFileManager;
import org.tpri.sc.manager.obt.ConferenceCategoryManager;
import org.tpri.sc.manager.obt.ConferenceCommentManager;
import org.tpri.sc.manager.obt.ConferenceFormatManager;
import org.tpri.sc.manager.obt.ConferenceLabelManager;
import org.tpri.sc.manager.obt.ConferenceManager;
import org.tpri.sc.manager.obt.ConferenceOrgnizerManager;
import org.tpri.sc.manager.obt.ConferenceParticipantsManager;
import org.tpri.sc.manager.obt.ConferencePraiseManager;
import org.tpri.sc.manager.obt.ConferenceStepManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.com.ComFileService;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.HTMLUtil;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.com.ConferenceCcpartyView;
import org.tpri.sc.view.com.QueryResultConference;
import org.tpri.sc.view.org.CcpartyCardPageView;

/**
 * 
 * <B>系统名称：</B>支部手册<BR>
 * <B>模块名称：</B>组织生活<BR>
 * <B>中文类名：</B>组织生活服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年05月02日
 */
@Service("ConferenceService")
public class ConferenceService {

    @Autowired
    private ConferenceManager conferenceManager;
    @Autowired
    private ConferenceLabelManager conferenceLabelManager;
    @Autowired
    private ConferenceStepManager conferenceStepManager;
    @Autowired
    private ConferenceFormatManager conferenceFormatManager;
    @Autowired
    private ConferenceCategoryManager categoryManager;
    @Autowired
    private ConferenceOrgnizerManager conferenceOrgnizerManager;
    @Autowired
    private ConferenceParticipantsManager conferenceParticipantsManager;
    @Autowired
    private ConferenceCommentManager commentManager;
    @Autowired
    private ConferencePraiseManager conferencePraiseManager;
    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private ComFileService comFileService;
    @Autowired
    private ComFileManager comFileManager;
    @Autowired
    private CCpartyService ccpartyService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ConferenceLabelService conferenceLabelService;
    @Autowired
    private ConferenceCategoryManager conferenceCategoryManager;

    /**
     * 
     * <B>方法名称：</B>查询组织生活列表包括标签、工作步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月22日
     * @param offset
     * @param limit
     * @param objs
     * @return
     */
    public List<Conference> getConferenceListForShare(Integer offset, Integer limit, JSONObject objs) {
        List<Object> result = conferenceManager.getConferencesForLabelOrStep(offset, limit, objs);
        List<Conference> conferences = new ArrayList<Conference>();
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Conference conference = conferenceManager.getConferenceById((String) result.get(i));
                if (conference != null) {
                    int praiseCount = conferencePraiseManager.getTotalPraiseByConferenceId(conference.getId(), ConferencePraise.TYPE_PRAISE);
                    conference.setPraiseCount(praiseCount);
                    int reply = commentManager.getTotalCommentByConferenceId(conference.getId());
                    conference.setReply(reply);
                    conference.setCreateUser(userManager.getUserFromMc(conference.getCreateUserId()));
                    conference.setCcparty(ccpartyManager.getCCPartyFromMc(conference.getCcpartyId()));
                    //所属标签
                    List<ConferenceLabel> conferenceLabels = conferenceLabelManager.getLabelsByConferenceId(conference.getId(), null);
                    for (ConferenceLabel conferenceLabel : conferenceLabels) {
                        conferenceLabel.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceLabel.getCategoryId()));
                    }
                    conference.setConferenceLabels(conferenceLabels);
                    //所属步骤
                    List<ConferenceStep> conferenceSteps = conferenceStepManager.getConferenceStepByConferenceId(conference.getId(), null);
                    for (ConferenceStep conferenceStep : conferenceSteps) {
                        conferenceStep.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceStep.getCategoryId()));
                    }
                    conference.setConferenceSteps(conferenceSteps);
                    //所属生活形式
                    List<ConferenceFormat> conferenceFormats = conferenceFormatManager.getConferenceFormatByConferenceId(conference.getId(), null);
                    for (ConferenceFormat conferenceFormat : conferenceFormats) {
                        conferenceFormat.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceFormat.getCategoryId()));
                    }
                    conference.setConferenceFormats(conferenceFormats);
                    conferences.add(conference);
                }
            }
        }
        return conferences;
    }

    /**
     * 
     * <B>方法名称：</B>查询组织生活列表记录数包括标签、工作步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月22日
     * @param paramterMap
     * @return
     */
    public Integer getConferencesTotalForShare(JSONObject objs) {
        return conferenceManager.getConferencesTotalForShare(objs);
    }

    /**
     * <B>方法名称：</B>获取本组织的工作品牌<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月30日
     * @param request
     * @return
     */
    public List<Conference> getWorkBrandsOfCcparty(Integer offset, Integer limit, Map<String, Object> param) {
        List<Object> result = conferenceManager.getWorkBrandsOfCcparty(offset, limit, param);
        List<Conference> conferences = new ArrayList<Conference>();
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Conference conference = conferenceManager.getConferenceById((String) result.get(i));
                if (conference != null) {
                    int praiseCount = conferencePraiseManager.getTotalPraiseByConferenceId(conference.getId(), ConferencePraise.TYPE_PRAISE);
                    conference.setPraiseCount(praiseCount);
                    int reply = commentManager.getTotalCommentByConferenceId(conference.getId());
                    conference.setReply(reply);
                    conference.setCreateUser(userManager.getUserFromMc(conference.getCreateUserId()));
                    conference.setCcparty(ccpartyManager.getCCPartyFromMc(conference.getCcpartyId()));
                    //所属标签
                    List<ConferenceLabel> conferenceLabels = conferenceLabelManager.getLabelsByConferenceId(conference.getId(), null);
                    for (ConferenceLabel conferenceLabel : conferenceLabels) {
                        conferenceLabel.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceLabel.getCategoryId()));
                    }
                    conference.setConferenceLabels(conferenceLabels);
                    //所属步骤
                    List<ConferenceStep> conferenceSteps = conferenceStepManager.getConferenceStepByConferenceId(conference.getId(), null);
                    for (ConferenceStep conferenceStep : conferenceSteps) {
                        conferenceStep.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceStep.getCategoryId()));
                    }
                    conference.setConferenceSteps(conferenceSteps);
                    //所属生活形式
                    List<ConferenceFormat> conferenceFormats = conferenceFormatManager.getConferenceFormatByConferenceId(conference.getId(), null);
                    for (ConferenceFormat conferenceFormat : conferenceFormats) {
                        conferenceFormat.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceFormat.getCategoryId()));
                    }
                    conference.setConferenceFormats(conferenceFormats);
                    conferences.add(conference);
                }
            }
        }
        return conferences;
    }

    /**
     * <B>方法名称：</B>获取本组织的工作品牌总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月30日
     * @param request
     * @return
     */
    public Integer getWorkBrandsOfCcpartyTotal(Map<String, Object> param) {
        return conferenceManager.getWorkBrandsOfCcpartyTotal(param);
    }

    /**
     * 
     * <B>方法名称：</B>获取组织生活详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param conferenceId
     * @return
     */
    public Conference getConferenceById(String conferenceId) {
        Conference conference = conferenceManager.getConferenceById(conferenceId);
        List<ComFile> files = comFileManager.getFileList(TableIndex.TABLE_OBT_CONFERENCE.getType(), conferenceId);
        conference.setFiles(files);
        List<ComFile> images = comFileManager.getFileList(TableIndex.TABLE_OBT_CONFERENCE.getType(), conferenceId, ComFile.FILETYPE_IMAGE);
        conference.setImages(images);
        conference.setPraiseCount(conferencePraiseManager.getTotalPraiseByConferenceId(conferenceId, ConferencePraise.TYPE_PRAISE));
        //获取组织者
        List<ConferenceOrgnizer> conferenceOrgnizers = conferenceOrgnizerManager.getOrgnizerByConferenceId(conferenceId);
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
        List<ConferenceParticipants> conferenceParticipants = conferenceParticipantsManager.getParticipantsByConferenceId(conferenceId, ConferenceParticipants.STATUS_0);
        conference.setConferenceParticipants(conferenceParticipants);
        String participants = "";
        for (ConferenceParticipants conferenceParticipant : conferenceParticipants) {
            participants = participants + conferenceParticipant.getUserName() + "；";
        }
        if (participants.length() > 0) {
            participants = participants.substring(0, participants.length() - 1);
        }
        conference.setParticipants(participants);
        return conference;
    }

    /**
     * <B>方法名称：</B>新增组织生活<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param request
     * @return
     */
    public String addConference(UserMc loginUser, Map<String, Object> param) {
        Conference conference = new Conference();
        String conferenceId = UUIDUtil.id();
        String ccpartyId = (String) param.get("ccpartyId");
        conference.setId(conferenceId);
        conference.setCcpartyId(ccpartyId);
        conference.setName((String) param.get("name"));
        conference.setAddress((String) param.get("address"));
        String occurTime = (String) param.get("occurTime");
        conference.setOccurTime(StringUtils.isEmpty(occurTime) ? null : DateUtil.str2Timestamp(occurTime, "yyyy-MM-dd"));
        conference.setAttendance((int) param.get("attendance"));
        conference.setContent((String) param.get("content"));
        Integer sourceType = (Integer) param.get("sourceType");
        if (sourceType == null) {
            conference.setSourceType(Conference.SOURCE_TYPE_1);
            conference.setSourceId(loginUser.getId());
            conference.setSourceName(loginUser.getName());
        } else {
            conference.setSourceType((int) param.get("sourceType"));
            conference.setSourceId((String) param.get("sourceId"));
            conference.setSourceName((String) param.get("sourceName"));
        }
        conference.setCreateUserId(loginUser.getId());
        conference.setStatus(Conference.STATUS_0);
        conference.setSecretLevel((int) param.get("secretLevel"));
        conference.setStatus((int)param.get("status"));
        conference.setCreateTime(new Timestamp(System.currentTimeMillis()));

        JSONArray files = JSONArray.fromObject((String) param.get("files"));
        comFileService.saveFilesFromJSONArray(conferenceId, TableIndex.TABLE_OBT_CONFERENCE, files);

        JSONArray labelIds = JSONArray.fromObject((String) param.get("labelIds"));
        if (labelIds.size() > 0) {
            conference.setIsBrand(Conference.IS_BRAND_1);
        } else {
            conference.setIsBrand(Conference.IS_BRAND_0);
        }
        conferenceManager.add(conference);

        for (int i = 0; i < labelIds.size(); i++) {
            String labelId = labelIds.getString(i);
            ConferenceLabel conferenceLabel = new ConferenceLabel();
            conferenceLabel.setId(UUIDUtil.id());
            conferenceLabel.setConferenceId(conferenceId);
            conferenceLabel.setCategoryId(labelId);
            conferenceLabelManager.add(conferenceLabel);
        }
        JSONArray stepIds = JSONArray.fromObject((String) param.get("stepIds"));
        for (int i = 0; i < stepIds.size(); i++) {
            String stepId = stepIds.getString(i);
            ConferenceStep conferenceStep = new ConferenceStep();
            conferenceStep.setId(UUIDUtil.id());
            conferenceStep.setConferenceId(conferenceId);
            conferenceStep.setCategoryId(stepId);
            conferenceStepManager.add(conferenceStep);
        }
        JSONArray formatIds = JSONArray.fromObject((String) param.get("formatIds"));
        for (int i = 0; i < formatIds.size(); i++) {
            String formatId = formatIds.getString(i);
            ConferenceFormat conferenceFormat = new ConferenceFormat();
            conferenceFormat.setId(UUIDUtil.id());
            conferenceFormat.setConferenceId(conferenceId);
            conferenceFormat.setCategoryId(formatId);
            conferenceFormatManager.add(conferenceFormat);
        }

        JSONArray orgnizerUserIds = JSONArray.fromObject((String) param.get("orgnizerUserIds"));
        for (int i = 0; i < orgnizerUserIds.size(); i++) {
            String orgnizerUserId = orgnizerUserIds.getString(i);
            ConferenceOrgnizer conferenceOrgnizer = conferenceOrgnizerManager.getOrgnizerByConferenceIdAndUserId(conferenceId, orgnizerUserId);
            if (conferenceOrgnizer == null) {
                conferenceOrgnizer = new ConferenceOrgnizer();
                conferenceOrgnizer.setId(UUIDUtil.id());
                conferenceOrgnizer.setConferenceId(conferenceId);
                conferenceOrgnizer.setUserId(orgnizerUserId);
                UserMc user = userManager.getUserFromMc(orgnizerUserId);
                if (user != null) {
                    conferenceOrgnizer.setUserName(user.getName());
                }
                conferenceOrgnizer.setType(ConferenceOrgnizer.TYPE_0);
                conferenceOrgnizerManager.add(conferenceOrgnizer);
            }
        }

        JSONArray orgnizerUserNames = JSONArray.fromObject((String) param.get("orgnizerUserNames"));
        for (int i = 0; i < orgnizerUserNames.size(); i++) {
            String orgnizerUserName = orgnizerUserNames.getString(i);
            ConferenceOrgnizer orgnizer = new ConferenceOrgnizer();
            orgnizer.setId(UUIDUtil.id());
            orgnizer.setConferenceId(conferenceId);
            orgnizer.setUserName(orgnizerUserName);
            orgnizer.setType(ConferenceOrgnizer.TYPE_1);
            conferenceOrgnizerManager.add(orgnizer);
        }

        JSONArray participantUserIds = JSONArray.fromObject((String) param.get("participantUserIds"));
        for (int i = 0; i < participantUserIds.size(); i++) {
            String participantUserId = participantUserIds.getString(i);
            ConferenceParticipants conferenceParticipants = conferenceParticipantsManager.getParticipantsByConferenceIdAndUserId(conferenceId, participantUserId);
            if (conferenceParticipants == null) {
                conferenceParticipants = new ConferenceParticipants();
                conferenceParticipants.setId(UUIDUtil.id());
                conferenceParticipants.setConferenceId(conferenceId);
                conferenceParticipants.setUserId(participantUserId);
                UserMc user = userManager.getUserFromMc(participantUserId);
                if (user != null) {
                    conferenceParticipants.setUserName(user.getName());
                }
                conferenceParticipants.setType(ConferenceParticipants.TYPE_0);
                conferenceParticipantsManager.add(conferenceParticipants);
            }
        }

        JSONArray participantUserNames = JSONArray.fromObject((String) param.get("participantUserNames"));
        for (int i = 0; i < participantUserNames.size(); i++) {
            String participantUserName = participantUserNames.getString(i);
            ConferenceParticipants conferenceParticipants = new ConferenceParticipants();
            conferenceParticipants.setId(UUIDUtil.id());
            conferenceParticipants.setConferenceId(conferenceId);
            conferenceParticipants.setUserName(participantUserName);
            conferenceParticipants.setType(ConferenceParticipants.TYPE_1);
            conferenceOrgnizerManager.add(conferenceParticipants);
        }

        return conferenceId;
    }

    /**
     * <B>方法名称：</B>修改组织生活<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param request
     * @return
     */
    public Map<String, Object> editConference(UserMc user, Map<String, Object> param) {
        Map<String, Object> ret = new HashMap<String, Object>();
        String conferenceId = (String) param.get("id");
        Conference conference = conferenceManager.getConferenceById(conferenceId);
        if (conference != null) {
            conference.setName((String) param.get("name"));
            conference.setAddress((String) param.get("address"));
            String occurTime = (String) param.get("occurTime");
            conference.setOccurTime(StringUtils.isEmpty(occurTime) ? null : DateUtil.str2Timestamp(occurTime, "yyyy-MM-dd"));
            conference.setAttendance((int) param.get("attendance"));
            conference.setContent((String) param.get("content"));
            Integer sourceType = (Integer) param.get("sourceType");
            if (sourceType != null) {
                conference.setSourceType(sourceType);
                conference.setSourceId((String) param.get("sourceId"));
                conference.setSourceName((String) param.get("sourceName"));
            }
            conference.setSecretLevel((int) param.get("secretLevel"));
            conference.setStatus((int) param.get("status"));
            JSONArray files = JSONArray.fromObject((String) param.get("files"));
            comFileService.saveFilesFromJSONArray(conferenceId, TableIndex.TABLE_OBT_CONFERENCE, files);

            // 处理对组织生活所属标签
            JSONArray labelIds = JSONArray.fromObject((String) param.get("labelIds"));
            if (labelIds.size() > 0) {
                conference.setIsBrand(Conference.IS_BRAND_1);
            } else {
                conference.setIsBrand(Conference.IS_BRAND_0);
            }
            conference.setUpdateUserId(user.getId());
            conference.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            conferenceManager.saveOrUpdate(conference);
            for (int i = 0; i < labelIds.size(); i++) {
                String labelId = labelIds.getString(i);
                // 根据labelId和组织生活id查询是否有记录存在，没有则创建
                List<ConferenceLabel> conferences = conferenceLabelManager.getConferenceLabelsByConferenceId(conferenceId, labelId);
                if (conferences == null || conferences.size() == 0) {
                    ConferenceLabel conferenceCategory = new ConferenceLabel();
                    conferenceCategory.setId(UUIDUtil.id());
                    conferenceCategory.setConferenceId(conferenceId);
                    conferenceCategory.setCategoryId(labelId);
                    conferenceLabelManager.add(conferenceCategory);
                }
            }
            // 删除没有选中的标签
            List<ConferenceLabel> conferenceLabels = conferenceLabelManager.getConferenceLabelsByConferenceId(conferenceId, null);
            for (ConferenceLabel conferenceLabel : conferenceLabels) {
                boolean isDelete = true; // 默认删除
                for (int i = 0; i < labelIds.size(); i++) {
                    if (conferenceLabel.getCategoryId().equals(labelIds.getString(i))) {
                        isDelete = false; // 存在不删除
                        break;
                    }
                }
                if (isDelete) {
                    // 执行删除
                    conferenceLabelManager.deleteConferenceLabel(conferenceLabel.getId());
                }
            }

            // 处理对组织生活所属步骤
            JSONArray stepIds = JSONArray.fromObject((String) param.get("stepIds"));
            for (int i = 0; i < stepIds.size(); i++) {
                String stepId = stepIds.getString(i);
                // 根据labelId和组织生活id查询是否有记录存在，没有则创建
                List<ConferenceStep> conferenceSteps = conferenceStepManager.getConferenceStepByConferenceId(conferenceId, stepId);
                if (conferenceSteps == null || conferenceSteps.size() == 0) {
                    ConferenceStep conferenceStep = new ConferenceStep();
                    conferenceStep.setId(UUIDUtil.id());
                    conferenceStep.setConferenceId(conferenceId);
                    conferenceStep.setCategoryId(stepId);
                    conferenceStepManager.add(conferenceStep);
                }
            }
            // 删除没有选中的步骤
            List<ConferenceStep> conferenceSteps = conferenceStepManager.getConferenceStepByConferenceId(conferenceId, null);
            for (ConferenceStep conferenceStep : conferenceSteps) {
                boolean isDelete = true; // 默认删除
                for (int i = 0; i < stepIds.size(); i++) {
                    if (conferenceStep.getCategoryId().equals(stepIds.getString(i))) {
                        isDelete = false; // 存在不删除
                        break;
                    }
                }
                if (isDelete) {
                    // 执行删除
                    conferenceStepManager.deleteConferenceStep(conferenceStep.getId());
                }
            }
            // 处理对组织生活所属生活形式
            JSONArray formatIds = JSONArray.fromObject((String) param.get("formatIds"));
            for (int i = 0; i < formatIds.size(); i++) {
                String formatId = formatIds.getString(i);
                // 根据formatId和组织生活id查询是否有记录存在，没有则创建
                List<ConferenceFormat> conferenceFormats = conferenceFormatManager.getConferenceFormatByConferenceId(conferenceId, formatId);
                if (conferenceFormats == null || conferenceFormats.size() == 0) {
                    ConferenceFormat conferenceFormat = new ConferenceFormat();
                    conferenceFormat.setId(UUIDUtil.id());
                    conferenceFormat.setConferenceId(conferenceId);
                    conferenceFormat.setCategoryId(formatId);
                    conferenceFormatManager.add(conferenceFormat);
                }
            }
            // 删除没有选中的步骤
            List<ConferenceFormat> conferenceFormats = conferenceFormatManager.getConferenceFormatByConferenceId(conferenceId, null);
            for (ConferenceFormat conferenceFormat : conferenceFormats) {
                boolean isDelete = true; // 默认删除
                for (int i = 0; i < formatIds.size(); i++) {
                    if (conferenceFormat.getCategoryId().equals(formatIds.getString(i))) {
                        isDelete = false; // 存在不删除
                        break;
                    }
                }
                if (isDelete) {
                    // 执行删除
                    conferenceFormatManager.deleteConferenceFormat(conferenceFormat.getId());
                }
            }
            ret.put("success", true);
            ret.put("msg", "保存成功。");
            return ret;
        } else {
            ret.put("success", false);
            ret.put("msg", "保存失败。");
            return ret;
        }
    }

    /**
     * 
     * <B>方法名称：</B>组织生活点击数增加<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param conferenceId
     * @return
     */
    public boolean updateHits(String conferenceId) {
        Conference conference = conferenceManager.getConferenceById(conferenceId);
        conference.setHits(conference.getHits() + 1);
        return conferenceManager.update(conference);
    }

    /**
     * 
     * <B>方法名称：</B>删除组织生活<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param user
     * @param ids
     * @return
     */
    public boolean deleteConference(UserMc user, JSONArray ids) {
        for (int i = 0; i < ids.size(); i++) {
            String conferenceId = ids.getString(i);
            conferenceManager.deleteConference(conferenceId);
            conferenceLabelManager.deleteConferenceLabelByConferenceId(conferenceId);
            conferenceStepManager.deleteConferenceStepByConferenceId(conferenceId);
            conferenceFormatManager.deleteConferenceFormatByConferenceId(conferenceId);
            commentManager.deleteCommentByConferenceId(conferenceId);
            List<ComFile> comFiles = comFileManager.getFileList(TableIndex.TABLE_OBT_CONFERENCE.getType(), conferenceId);
            for (ComFile comFile : comFiles) {
                String fileId = comFile.getId();
                comFileService.deleteFile(user, fileId);
            }
        }
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>统计与分析数据获取<BR>
     * <B>概要说明：</B>使用SQL查询<BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param queryYear
     * @param queryQuarter
     * @param ccpartyId
     * @return
     */
    public List<QueryResultConference> getConferenceStepsStatistics(String ccpartyId, String beginDate, String endDate) {
        return conferenceManager.getConferenceStepsStatistics(ccpartyId, beginDate, endDate);
    }

    public List<QueryResultConference> getConferenceFormartStatistics(String ccpartyId, String beginDate, String endDate) {
        return conferenceManager.getConferenceFormartStatistics(ccpartyId, beginDate, endDate);
    }

    public List<QueryResultConference> getConferenceLabelStatistics(String ccpartyId, String beginDate, String endDate) {
        return conferenceManager.getConferenceLabelStatistics(ccpartyId, beginDate, endDate);
    }

    /**
     * 
     * <B>方法名称：</B>组织生活推荐设置<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param loadUser
     * @param conferenceId
     * @param isRecommend
     * @return
     */
    public boolean editRecommend(UserMc loadUser, String conferenceId, int isRecommend) {
        Map<String, Object> fieldValues = new HashMap<String, Object>();
        fieldValues.put("isRecommend", isRecommend);
        conferenceManager.editConference(conferenceId, fieldValues);
        return true;
    }

    /**
     * <B>方法名称：</B>发布<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param user
     * @param id
     */
    public Map<String, Object> publishConference(UserMc user, String idsStr) {
        Map<String, Object> ret = new HashMap<String, Object>();
        String errorIds = "";
        int successCount = 0;
        String[] ids = idsStr.split(",");
        if(ids!=null && ids.length>0){
            for(int i=0;i<ids.length;i++){
                Conference conference = conferenceManager.getConferenceById(ids[i]);
                if(conference==null){
                    errorIds += " "+ids[i];
                }
                conference.setStatus(Conference.STATUS_1);
                conference.setPublishUserId(user.getId());
                conference.setPublishTime(new Timestamp(System.currentTimeMillis()));
                conferenceManager.saveOrUpdate(conference);
                successCount ++;
            }
        }
        if(successCount==0){
            ret.put("success", false);
            ret.put("msg", "发布失败。");
        }else if(successCount==ids.length){
            ret.put("success", true);
            ret.put("msg", "已成功发布"+ids.length+"条信息。");
        }else if(successCount<ids.length){
            ret.put("success", true);
            ret.put("msg", "已成功发布"+successCount+"条信息，但是"+errorIds+"发布失败。");
        }
        return ret;
    }

    /**
     * <B>方法名称：</B>取消发布<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param user
     * @param id
     */
    public Map<String, Object> unpublishConference(UserMc user, String idsStr) {
        Map<String, Object> ret = new HashMap<String, Object>();
        String errorIds = "";
        int successCount = 0;
        String[] ids = idsStr.split(",");
        if(ids!=null && ids.length>0){
            for(int i=0;i<ids.length;i++){
                Conference conference = conferenceManager.getConferenceById(ids[i]);
                if(conference==null){
                    errorIds += " "+ids[i];
                }
                conference.setStatus(Conference.STATUS_2);
                conference.setPublishUserId(null);
                conference.setPublishTime(null);
                conferenceManager.saveOrUpdate(conference);
                successCount ++;
            }
        }
        if(successCount==0){
            ret.put("success", false);
            ret.put("msg", "取消发布失败。");
        }else if(successCount==ids.length){
            ret.put("success", true);
            ret.put("msg", "已成功取消发布"+ids.length+"条信息。");
        }else if(successCount<ids.length){
            ret.put("success", true);
            ret.put("msg", "已成功取消发布"+successCount+"条信息，但是"+errorIds+"取消失败。");
        }
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取组织生活日期<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param ccpartyId
     * @param userId
     * @return
     */
    public String[] getConferencesYearsByUserOrCcparty(String ccpartyId, String userId) {
        return conferenceManager.getConferencesYearsByUserOrCcparty(ccpartyId, userId);
    }

    /**
     * 
     * <B>方法名称：</B>获取我的组织生活列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param offset
     * @param limit
     * @param userId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public List<Conference> getMyConferenceList(Integer offset, Integer limit, String userId, String beginTime, String endTime, String searchKey) {
        List<Conference> conferences = conferenceManager.getMyConferenceList(offset, limit, userId, beginTime, endTime, searchKey);
        for (Conference conference : conferences) {
            conference.setCreateUser(userManager.getUserFromMc(conference.getCreateUserId()));
            conference.setCommentCount(commentManager.getTotalCommentByConferenceId(conference.getId()));
            //所属标签
            List<ConferenceLabel> conferenceLabels = conferenceLabelManager.getLabelsByConferenceId(conference.getId(), null);
            for (ConferenceLabel conferenceLabel : conferenceLabels) {
                conferenceLabel.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceLabel.getCategoryId()));
            }
            conference.setConferenceLabels(conferenceLabels);
            //所属步骤
            List<ConferenceStep> conferenceSteps = conferenceStepManager.getConferenceStepByConferenceId(conference.getId(), null);
            for (ConferenceStep conferenceStep : conferenceSteps) {
                conferenceStep.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceStep.getCategoryId()));
            }
            conference.setConferenceSteps(conferenceSteps);
            //所属生活形式
            List<ConferenceFormat> conferenceFormats = conferenceFormatManager.getConferenceFormatByConferenceId(conference.getId(), null);
            for (ConferenceFormat conferenceFormat : conferenceFormats) {
                conferenceFormat.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceFormat.getCategoryId()));
            }
            conference.setConferenceFormats(conferenceFormats);
        }
        return conferences;
    }

    /**
     * 
     * <B>方法名称：</B>获取我的组织生活总记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param userId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public Integer getMyConferencesTotal(String userId, String beginTime, String endTime, String searchKey) {
        return conferenceManager.getMyConferencesTotal(userId, beginTime, endTime, searchKey);
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的组织生活列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param offset
     * @param limit
     * @param ccpartyId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public List<Conference> getCcpartyConferenceList(Integer offset, Integer limit, String ccpartyId, String paramter) {
        List<Conference> conferences = new ArrayList<Conference>();
        JSONObject jsonObjs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            jsonObjs = ja.getJSONObject(0);
        }
        List<Object> objs = conferenceManager.getCcpartyConferenceList(offset, limit, ccpartyId, jsonObjs);
        if (objs != null && objs.size() > 0) {
            for (Object obj : objs) {
                Conference conference = conferenceManager.getConference((String) obj);
                if (conference == null) {
                    continue;
                }
                conference.setCreateUser(userManager.getUserFromMc(conference.getCreateUserId()));
                conference.setUpdateUser(userManager.getUserFromMc(conference.getUpdateUserId()));
                conference.setCommentCount(commentManager.getTotalCommentByConferenceId(conference.getId()));
                //所属标签
                List<ConferenceLabel> conferenceLabels = conferenceLabelManager.getLabelsByConferenceId(conference.getId(), null);
                for (ConferenceLabel conferenceLabel : conferenceLabels) {
                    conferenceLabel.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceLabel.getCategoryId()));
                }
                conference.setConferenceLabels(conferenceLabels);
                //所属步骤
                List<ConferenceStep> conferenceSteps = conferenceStepManager.getConferenceStepByConferenceId(conference.getId(), null);
                for (ConferenceStep conferenceStep : conferenceSteps) {
                    conferenceStep.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceStep.getCategoryId()));
                }
                conference.setConferenceSteps(conferenceSteps);
                //所属生活形式
                List<ConferenceFormat> conferenceFormats = conferenceFormatManager.getConferenceFormatByConferenceId(conference.getId(), null);
                for (ConferenceFormat conferenceFormat : conferenceFormats) {
                    conferenceFormat.setCategory(conferenceCategoryManager.getConferenceCategoryById(conferenceFormat.getCategoryId()));
                }
                conference.setConferenceFormats(conferenceFormats);
                conferences.add(conference);
            }
        }
        return conferences;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的组织生活总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param ccpartyId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public Integer getCcpartyConferenceTotal(String ccpartyId, String paramter) {
        JSONObject jsonObjs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            jsonObjs = ja.getJSONObject(0);
        }
        return conferenceManager.getCcpartyConferenceTotal(ccpartyId, jsonObjs);
    }

    /**
     * <B>方法名称：</B>根据组织生活ID获取所属哪些生活内容<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月13日
     * @param conferenceId
     * @return
     */
    public List<ConferenceLabel> getConferenceLabelsByConferenceId(String conferenceId) {
        List<ConferenceLabel> conferenceColumns = conferenceLabelManager.getConferenceLabelsByConferenceId(conferenceId, null);
        return conferenceColumns;
    }

    /**
     * <B>方法名称：</B>根据文章ID获取所属哪些生活形式<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月12日
     * @param conferenceId
     * @return
     */
    public List<ConferenceFormat> getConferenceFormatsByConferenceId(String conferenceId) {
        List<ConferenceFormat> conferenceFormats = conferenceFormatManager.getConferenceFormatByConferenceId(conferenceId, null);
        return conferenceFormats;
    }

    /**
     * <B>方法名称：</B>根据组织生活ID获取所属哪些步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param conferenceId
     * @return
     */
    public List<ConferenceStep> getConferenceStepsByConferenceId(String conferenceId) {
        List<ConferenceStep> conferenceSteps = conferenceStepManager.getConferenceStepByConferenceId(conferenceId, null);
        return conferenceSteps;
    }

    /**
     * 
     * <B>方法名称：</B>获取支部工作的记录数 用于党组织电子活动证<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月25日
     * @param ccpartyId
     * @param page
     */
    public void getCardPagesForConference(String ccpartyId, String paramter, CcpartyCardPageView page) {
        JSONObject objs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            objs = ja.getJSONObject(0);
        }
        int total = 0; //总记录
        total = conferenceManager.getCardPagesForConference(ccpartyId, objs);
        if (total > 2) {
            //两条记录以上处理分页
            page.setBranchConferencePages((int) Math.ceil((double) total / 2)); //凑整 比如 2.1 为 3
            page.setPages(page.getPages() + page.getBranchConferencePages() - 1); //计算总页数 
            page.setBranchConferenceEndPage(page.getBranchConferenceBeginPage() + page.getBranchConferencePages());
        }
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织开展的支部工作描述<BR>
     * <B>概要说明：</B>党员电子活动证使用<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月26日
     * @param ccpartyId
     * @return
     */
    public String getConferenceDescriptionForCard(String ccpartyId, String paramter) {
        JSONObject objs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            objs = ja.getJSONObject(0);
        }
        StringBuffer totalStr = new StringBuffer();
        List<Object> conferences = conferenceManager.getConferenceListByCcparty(null, null, ccpartyId, objs);
        if (conferences != null && conferences.size() > 0) {
            //组织返回的html语言
            totalStr.append("共开展" + conferences.size() + "次组织生活。");
        } else {
            totalStr = new StringBuffer();
            totalStr.append("暂未开展任何组织生活。");
        }
        return totalStr.toString().substring(0, totalStr.length() - 1);
    }

    /**
     * 
     * <B>方法名称：</B>获取支部工作列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月26日
     * @param offset
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<Conference> getConferenceListByCcparty(Integer offset, Integer limit, String ccpartyId, String paramter) {
        List<Conference> conferences = new ArrayList<Conference>();
        JSONObject jsonObjs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            jsonObjs = ja.getJSONObject(0);
        }
        List<Object> objs = conferenceManager.getConferenceListByCcparty(offset, limit, ccpartyId, jsonObjs);
        if (objs != null && objs.size() > 0) {
            for (Object obj : objs) {
                Conference conference = conferenceManager.getConference((String) obj);
                if (conference == null) {
                    continue;
                }
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
                conferences.add(conference);
            }
        }
        return conferences;
    }

    /**
     * <B>方法名称：</B>组织工作统计<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月25日
     * @param ccpartyId
     * @return
     */
    public List<ConferenceCcpartyView> getConferenceCcpartyStatistics(String ccpartyId, String beginDate, String endDate) {
        List<ConferenceCcpartyView> views = new ArrayList<ConferenceCcpartyView>();
        CCParty ccparty = ccpartyManager.getCCPartyFromMc(ccpartyId);
        if (ccparty != null && ccparty.getParentId() != null) {
            String parentId = ccpartyId;
            if (ccparty.getType().equals(CCParty.TYPE_4)) {
                parentId = ccparty.getParentId();
            }
            List<Object> objs = conferenceManager.getConferenceCcpartyStatistics(parentId, beginDate, endDate);
            if (objs != null && objs.size() > 0) {
                for (int i = 0; i < objs.size(); i++) {
                    Object[] obj = (Object[]) objs.get(i);
                    ConferenceCcpartyView view = new ConferenceCcpartyView();
                    view.setCcpartyId(String.valueOf(obj[0]));
                    view.setCcpartyName(String.valueOf(obj[1]));
                    view.setTotalCount(Integer.parseInt(String.valueOf(obj[2])));
                    view.setIndividualCount(Integer.parseInt(String.valueOf(obj[3])));
                    view.setCcpartyCount(Integer.parseInt(String.valueOf(obj[4])));
                    view.setRecommendCount(Integer.parseInt(String.valueOf(obj[5])));
                    view.setBrandCount(Integer.parseInt(String.valueOf(obj[6])));
                    views.add(view);
                }
            }
        }
        return views;
        //            
        //            for (CCParty ccpartyTemp : ccparties) {
        //                boolean exist = false;
        //                for (ConferenceCcpartyView view : views) {
        //                    if (view.getCcpartyId().equals(ccpartyTemp.getId())) {
        //                        exist = true;
        //                        break;
        //                    }
        //                }
        //                if (!exist) {
        //                    ConferenceCcpartyView view = new ConferenceCcpartyView();
        //                    view.setCcpartyId(ccpartyTemp.getId());
        //                    view.setCcpartyName(ccpartyTemp.getName());
        //                    view.setIndividualCount(0);
        //                    view.setCcpartyCount(0);
        //                    view.setBrandCount(0);
        //                    view.setRecommendCount(0);
        //                    view.setTotalCount(0);
        //                    views.add(view);
        //                }
        //            }
        //            return views;
        //        } else {
        //            return null;
        //        }
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
    public void getConferencesByCcpartyForPartyCard(String ccpartyId, String paramter, ModelAndView ret) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT);
        List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
        JSONObject jsonObjs = null;
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            jsonObjs = ja.getJSONObject(0);
        }
        List<Object> objs = conferenceManager.getConferenceListByCcparty(null, null, ccpartyId, jsonObjs);
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

    /**
     * 
     * <B>方法名称：</B>置顶状态改变<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月13日
     * @param id
     * @param isTop
     * @return
     */
    public Map<String, Object> updateConferenceIsTop(String id, Integer isTop) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Conference conference = conferenceManager.getConference(id);
        if (conference == null) {
            ret.put("success", false);
            ret.put("msg", "操作失败，文章不存在。");
            return ret;
        }
        conference.setIsTop(isTop);
        conferenceManager.saveOrUpdate(conference);
        ret.put("success", true);
        ret.put("msg", "操作成功。");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取上一篇、下一篇<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年10月21日 	
     * @param currentId
     * @param viewSource
     * @param ccpartyId
     * @param userId
     * @return
     */
    public Map<String, Object> getUpDownConference(String currentId,String viewSource,String ccpartyId,String userId){
        Map<String, Object> ret = new HashMap<String, Object>();
        Conference conference = conferenceManager.getConference(currentId);
        if(conference==null){
            ret.put("beforeConference", null);
            ret.put("afterConference", null);
            return ret;
        }
        if("ccparty".equals(viewSource)){
            //组织列表查看的详情
        }else if("share".equals(viewSource)){
            //共享中心查看的详情
        }else if("myCard".equals(viewSource)){
            //我的党员活动证查看的详情
        }
        List<Object> beforeList = conferenceManager.getBeforeConference(conference,viewSource,ccpartyId,userId);
        if(beforeList!=null && beforeList.size()>0){
            Object obj = (Object)beforeList.get(beforeList.size()-1);
            ret.put("beforeConference",conferenceManager.getConference(obj.toString()));
        }
        List<Object> afterList = conferenceManager.getAfterConference(conference,viewSource,ccpartyId,userId);
        if(afterList!=null && afterList.size()>0){
            Object obj = (Object)afterList.get(0);
            ret.put("afterConference",conferenceManager.getConference(obj.toString()));
        }
        return ret;
    }
}
