package org.tpri.sc.service.pub;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.pub.Assessment;
import org.tpri.sc.entity.pub.AssessmentTarget;
import org.tpri.sc.entity.pub.AssessmentTopic;
import org.tpri.sc.entity.pub.AssessmentTopicOption;
import org.tpri.sc.entity.pub.AssessmentUser;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.com.ComFileManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.pub.AssessmentManager;
import org.tpri.sc.manager.pub.AssessmentTargetManager;
import org.tpri.sc.manager.pub.AssessmentTopicManager;
import org.tpri.sc.manager.pub.AssessmentTopicOptionManager;
import org.tpri.sc.manager.pub.AssessmentUserManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.sys.EnvironmentService;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.StringUtil;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.pub.MyAssessmentView;

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：问卷测评</B><BR>
 * <B>中文类名：问卷测评服务类</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月15日
 */
@Service("AssessmentService")
public class AssessmentService {
    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private AssessmentTopicManager assessmentTopicManager;
    @Autowired
    private AssessmentTopicOptionManager assessmentTopicOptionManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EnvironmentService environmentService;
    @Autowired
    private ComFileManager comFileManager;
    @Autowired
    private AssessmentUserManager assessmentUserManager;
    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private AssessmentUserService assessmentUserService;
    @Autowired
    private AssessmentTargetService assessmentTargetService;
    @Autowired
    private AssessmentTargetManager assessmentTargetManager;
    @Autowired
    private AssessmentResultService assessmentResultService;
    @Autowired
    private AssessmentTopicService assessmentTopicService;

    /**
     * 
     * <B>方法名称：获取问卷测评列表</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param start
     * @param limit
     * @param search
     * @param ccpartyId
     * @return
     */
    public List<Assessment> getAssessmentList(Integer start, Integer limit, String search, String ccpartyId) {
        List<Assessment> assessments = assessmentManager.getAssessmentList(start, limit, search, ccpartyId);
        if (assessments != null && assessments.size() > 0) {
            for (Assessment assessment : assessments) {
                List<AssessmentUser> assessmentUsers = assessmentUserManager.getAssessmentUsersByAssessment(null, null, assessment.getId());
                if (assessmentUsers != null && assessmentUsers.size() > 0) {
                    assessment.setJoinNum(assessmentUsers.size());
                }
            }
        }
        return assessments;
    }

    /**
     * 
     * <B>方法名称：获取问卷测评总记录条数</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param search
     * @param ccpartyId
     * @return
     */
    public Integer getAssessmentTotal(String search, String ccpartyId) {
        return assessmentManager.getAssessmentTotal(search, ccpartyId);
    }

    /**
     * 
     * <B>方法名称：</B>获取我的答卷<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月11日
     * @param offset
     * @param limit
     * @param search
     * @param ccpartyId
     * @param userId
     * @param joinType
     * @return
     */
    public List<MyAssessmentView> getMyAssessments(Integer offset, Integer limit, String search, String ccpartyId, String userId, Integer joinType) {
        List<MyAssessmentView> views = new ArrayList<MyAssessmentView>();
        List<Assessment> assessments = assessmentManager.getMyAssessments(offset, limit, search, ccpartyId, userId, joinType);
        if (assessments != null && assessments.size() > 0) {
            Map<String, String> joinMaps = new HashMap<String, String>();
            List<AssessmentUser> assessmentUsers = assessmentUserManager.getAssessmentUserByUser(userId);
            if (assessmentUsers != null && assessmentUsers.size() > 0) {
                for (AssessmentUser assessmentUser : assessmentUsers) {
                    joinMaps.put(assessmentUser.getAssessmentId(), assessmentUser.getAssessmentId());
                }
            }
            for (Assessment assessment : assessments) {
                MyAssessmentView view = new MyAssessmentView();
                view.setAssessmentName(assessment.getName());
                view.setAssessmentId(assessment.getId());
                CCParty ccparty = ccpartyManager.getCCPartyById(assessment.getCcpartyId());
                view.setAssessmentCcpartyName(ccparty != null ? ccparty.getName() : "");
                if (joinMaps.get(assessment.getId()) != null) {
                    view.setJoinType(true);
                }
                view.setAssessmentType(assessment.getStatus());
                String overTip = "";
                if (Assessment.ISEXPIRY_0 == assessment.getIsExpiry()) {
                    overTip = "无截止日期";
                } else {
                    if (assessment.getEndDate() != null) {
                        int days = DateUtil.daysBetween(new Date(), assessment.getEndDate());
                        if (days > 0) {
                            overTip = "还有" + days + "天结束";
                        } else if (days == 0) {
                            overTip = "今天结束";
                        } else {
                            overTip = "已过期";
                        }
                    }
                }
                if (assessment.getStatus() == Assessment.STATUS_2) {
                    overTip = "已关闭";
                }
                view.setOverTip(overTip);
                List<AssessmentUser> joinUsers = assessmentUserManager.getAssessmentUsersByAssessment(null, null, assessment.getId());
                if (joinUsers != null && joinUsers.size() > 0) {
                    view.setJoinNum(joinUsers.size());
                }
                views.add(view);
            }
        }
        return views;
    }

    /**
     * 
     * <B>方法名称：</B>获取我的答卷记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月11日
     * @param search
     * @param ccpartyId
     * @param userId
     * @param joinType
     * @return
     */
    public Integer getMyAssessmentsTotal(String search, String ccpartyId, String userId, Integer joinType) {
        return assessmentManager.getMyAssessmentsTotal(search, ccpartyId, userId, joinType);
    }

    /**
     * 
     * <B>方法名称：保存文件测评</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param loginUser
     * @param objs
     * @return
     */
    public Map<String, Object> saveAssessment(UserMc loginUser, JSONObject objs) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Assessment assessment = new Assessment();
        assessment.setId(UUIDUtil.id());
        assessment.setName(objs.getString("name"));
        assessment.setIsExpiry(objs.getInt("isExpiry"));
        if (Assessment.ISEXPIRY_1 == objs.getInt("isExpiry")) {
            assessment.setEndDate(StringUtil.isEmpty(objs.getString("endDate")) ? null : DateUtil.str2Date(objs.getString("endDate"), DateUtil.DEFAULT_FORMAT));
        } else {
            assessment.setEndDate(null);
        }
        assessment.setDescription(objs.getString("description"));
        assessment.setCcpartyId(objs.getString("ccpartyId"));
        assessment.setCreateUserId(loginUser.getId());
        assessment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        assessmentManager.saveOrUpdate(assessment);
        //保存参与单位信息
        if (!StringUtils.isEmpty(objs.getString("ccpartyIds"))) {
            String[] ids = objs.getString("ccpartyIds").split(",");
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    if (ids[i].equals(objs.getString("ccpartyId"))) {
                        AssessmentTarget target = new AssessmentTarget();
                        target.setId(UUIDUtil.id());
                        target.setCcpartyId(ids[i]);
                        target.setAssessmentId(assessment.getId());
                        assessmentTargetManager.saveOrUpdate(target);
                    } else {
                        CCParty ccparty = ccpartyManager.getCCPartyById(ids[i]);
                        assessmentTargetService.SaveAssessmentTargetByCcpartyRecursion(ccparty, assessment.getId());
                    }

                }
            }
        }
        ret.put("success", true);
        ret.put("msg", "保存成功。");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>状态更改<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月11日
     * @param id
     * @param status
     * @return
     */
    public Map<String, Object> updateAssessmentStatus(String id, Integer status) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Assessment assessment = assessmentManager.getAssessmentById(id);
        if (assessment == null) {
            ret.put("success", false);
            ret.put("msg", "操作失败，数据不存在。");
            return ret;
        }
        assessment.setStatus(status);
        assessmentManager.saveOrUpdate(assessment);
        ret.put("success", true);
        ret.put("msg", "操作成功。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：修改文件测评</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param loginUser
     * @param objs
     * @return
     */
    public Map<String, Object> updateAssessment(JSONObject objs) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Assessment assessment = assessmentManager.getAssessmentById(objs.getString("id"));
        if (assessment == null) {
            ret.put("success", false);
            ret.put("msg", "保存失败，答卷不存在。");
            return ret;
        }
        assessment.setName(objs.getString("name"));
        assessment.setIsExpiry(objs.getInt("isExpiry"));
        if (Assessment.ISEXPIRY_1 == objs.getInt("isExpiry")) {
            assessment.setEndDate(StringUtil.isEmpty(objs.getString("endDate")) ? null : DateUtil.str2Date(objs.getString("endDate"), DateUtil.DEFAULT_FORMAT));
        } else {
            assessment.setEndDate(null);
        }
        assessment.setDescription(objs.getString("description"));
        assessmentManager.saveOrUpdate(assessment);
        //删除之前的参与单位信息
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessment(null, null, assessment.getId());
        if (targets != null && targets.size() > 0) {
            for (AssessmentTarget target : targets) {
                assessmentTargetManager.delete(target.getId(), ObjectType.PUB_ASSESSMENT_TARGET);
            }
        }
        //保存参与单位信息
        if (!StringUtils.isEmpty(objs.getString("ccpartyIds"))) {
            String[] ids = objs.getString("ccpartyIds").split(",");
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    if (ids[i].equals(assessment.getCcpartyId())) {
                        AssessmentTarget target = new AssessmentTarget();
                        target.setId(UUIDUtil.id());
                        target.setCcpartyId(ids[i]);
                        target.setAssessmentId(assessment.getId());
                        assessmentTargetManager.saveOrUpdate(target);
                    } else {
                        CCParty ccparty = ccpartyManager.getCCPartyById(ids[i]);
                        assessmentTargetService.SaveAssessmentTargetByCcpartyRecursion(ccparty, assessment.getId());
                    }

                }
            }
        }
        ret.put("success", true);
        ret.put("msg", "保存成功！");
        return ret;
    }

    /**
     * 
     * <B>方法名称：根据ID删除问卷测评</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param id
     * @return
     */
    public Map<String, Object> deleteAssessment(String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Assessment assessment = assessmentManager.getAssessmentById(id);
        if (assessment == null) {
            ret.put("success", false);
            ret.put("msg", "删除失败，答卷不存在！");
            return ret;
        }
        //step1、删除测评人员
        List<AssessmentUser> assessmentUsers = assessmentUserManager.getAssessmentUsersByAssessment(null, null, assessment.getId());
        if (assessmentUsers != null && assessmentUsers.size() > 0) {
            for (AssessmentUser user : assessmentUsers) {
                assessmentUserManager.delete(user.getId(), ObjectType.PUB_ASSESSMENT_USER);
            }
        }
        //step2、删除测评组织
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessment(null, null, assessment.getId());
        if (targets != null && targets.size() > 0) {
            for (AssessmentTarget target : targets) {
                assessmentTargetManager.delete(target.getId(), ObjectType.PUB_ASSESSMENT_TARGET);
            }
        }
        //step3、删除问卷测评试题、答案
        deleteAssessmentTopic(assessment.getId());
        //step4、删除答题答卷
        assessmentManager.delete(assessment.getId(), ObjectType.PUB_ASSESSMENT);
        ret.put("success", true);
        ret.put("msg", "已成功删除。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：根据ID获取问卷信息</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param id
     * @return
     */
    public Assessment getAssessmentById(String id) {
        Assessment assessment = assessmentManager.getAssessmentById(id);
        if (assessment != null) {
            assessment.setTargets(assessmentTargetManager.getAssessmentTargetsByAssessment(null, null, assessment.getId()));
        }
        return assessment;
    }

    /**
     * 
     * <B>方法名称：启动问卷测评</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param id
     * @return
     */
    public Map<String, Object> publishAssessment(String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Assessment assessment = assessmentManager.getAssessmentById(id);
        CCParty ccparty = ccpartyManager.getCCPartyFromMc(assessment.getCcpartyId());
        if (assessment == null) {
            ret.put("success", false);
            ret.put("msg", "开启失败，未找到问卷！");
            return ret;
        }
        assessment.setStatus(Assessment.STATUS_1);
        assessmentManager.saveOrUpdate(assessment);
        ret.put("success", true);
        ret.put("msg", "已开启答卷。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：结束问卷测评</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param id
     * @return
     */
    public Map<String, Object> overAssessment(String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Assessment assessment = assessmentManager.getAssessmentById(id);
        CCParty ccparty = ccpartyManager.getCCPartyFromMc(assessment.getCcpartyId());
        if (assessment == null || ccparty == null) {
            ret.put("success", false);
            ret.put("msg", "结束失败。");
            return ret;
        }
        assessment.setStatus(Assessment.STATUS_2);
        assessmentManager.saveOrUpdate(assessment);
        ret.put("success", true);
        ret.put("msg", "已成功结束！");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>试卷保存试题及选项和答案<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月6日
     * @param assessmentId
     * @param questions
     * @return
     */
    public Map<String, Object> saveAssessmentTopic(String assessmentId, String questions) {
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject jodata = JSONObject.fromObject(questions);
        JSONArray ja = JSONArray.fromObject(jodata.get("data"));
        for (int iQuestion = 0; iQuestion < ja.size(); iQuestion++) {
            JSONObject o = ja.getJSONObject(iQuestion);
            String id = o.getString("id");//题目ID
            String topicTitle = o.getString("topicTitle");
            String isMust = o.getString("isMust");
            int topicType = o.getInt("topicType");
            String options = o.getString("options");
            String seq = o.getString("seq");

            AssessmentTopic topic = assessmentTopicManager.getAssessmentTopicById(id);
            if (topic == null) {
                topic = new AssessmentTopic();
                topic.setId(UUIDUtil.id());
            }
            topic.setAssessmentId(assessmentId);
            topic.setTitle(topicTitle);
            topic.setSequence(seq);
            if ("true".equals(isMust)) {
                topic.setIsMust(0);
            } else {
                topic.setIsMust(1);
            }
            topic.setType(topicType);
            assessmentTopicManager.saveOrUpdate(topic);
            if (AssessmentTopic.TOPIC_TYPE_1 == topicType) {
                List<AssessmentTopicOption> deleteOptions = assessmentTopicOptionManager.getOptionByTopicId(topic.getId());
                if (deleteOptions != null && deleteOptions.size() > 0) {
                    for (AssessmentTopicOption option : deleteOptions) {
                        assessmentTopicOptionManager.delete(option.getId(), ObjectType.PUB_ASSESSMENT_TOPIC_OPTION);
                    }
                }
                //对试题的选项处理
                JSONArray optionsJa = JSONArray.fromObject(options);
                for (int i = 0; i < optionsJa.size(); i++) {
                    int index = i + 1;
                    JSONObject objs = optionsJa.getJSONObject(i);
                    AssessmentTopicOption option = new AssessmentTopicOption();
                    option.setId(UUIDUtil.id());
                    option.setTopicId(topic.getId());
                    option.setContent(objs.getString("title"));
                    option.setSeq(StringUtil.numberToLetter(index));
                    assessmentTopicOptionManager.saveOrUpdate(option);
                }
            } else if (AssessmentTopic.TOPIC_TYPE_2 == topicType) {
                List<AssessmentTopicOption> deleteOptions = assessmentTopicOptionManager.getOptionByTopicId(topic.getId());
                if (deleteOptions != null && deleteOptions.size() > 0) {
                    for (AssessmentTopicOption option : deleteOptions) {
                        assessmentTopicOptionManager.delete(option.getId(), ObjectType.PUB_ASSESSMENT_TOPIC_OPTION);
                    }
                }
                //对多选题处理
                JSONArray optionsJa = JSONArray.fromObject(options);
                for (int i = 0; i < optionsJa.size(); i++) {
                    int index = i + 1;
                    JSONObject objs = optionsJa.getJSONObject(i);
                    AssessmentTopicOption option = new AssessmentTopicOption();
                    option.setId(UUIDUtil.id());
                    option.setTopicId(topic.getId());
                    option.setContent(objs.getString("title"));
                    option.setSeq(StringUtil.numberToLetter(index));
                    assessmentTopicOptionManager.saveOrUpdate(option);
                }
            }
        }
        ret.put("success", true);
        ret.put("msg", "答卷题目保存成功。");
        return ret;
    }

    /**
     * 根据试卷ID删除该试卷的试题和选项、答案
     * 
     * @param assessmentId
     * @return
     */
    public boolean deleteAssessmentTopic(String assessmentId) {
        List<AssessmentTopic> deleteTopics = assessmentTopicManager.getTopicByAssessmentId(assessmentId);
        for (AssessmentTopic deleteTopic : deleteTopics) {
            //删除选项
            List<AssessmentTopicOption> options = assessmentTopicOptionManager.getOptionByTopicId(deleteTopic.getId());
            if (options != null && options.size() > 0) {
                for (AssessmentTopicOption option : options) {
                    assessmentTopicManager.delete(option.getId(), ObjectType.PUB_ASSESSMENT_TOPIC_OPTION);
                }
            }
            //删除题目
            assessmentTopicManager.delete(deleteTopic.getId(), ObjectType.PUB_ASSESSMENT_TOPIC);
        }
        return true;
    }

    /**
     * 
     * <B>方法名称：根据问卷测评ID获取内容及试题</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param assessmentId
     * @return
     */
    public Assessment getAssessmentInfoById(String assessmentId) {
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment == null) {
            return null;
        }
        //测评 
        return this.getAssessmentEvaluation(assessment);
    }

    /**
     * 
     * <B>方法名称：</B>获取测评信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月14日
     * @param assessment
     * @return
     */
    public Assessment getAssessmentEvaluation(Assessment assessment) {
        //step1、获取试题
        List<AssessmentTopic> topics = assessmentTopicManager.getTopicByAssessmentId(assessment.getId());
        for (AssessmentTopic topic : topics) {
            //step2、根据试题获取选项
            if (AssessmentTopic.TOPIC_TYPE_3 != topic.getType()) {
                //不是简答题有选项需要设置
                List<AssessmentTopicOption> options = assessmentTopicOptionManager.getOptionByTopicId(topic.getId());
                topic.setOptions(options); //设置选项
            }
        }
        //将试题集合放入试卷中
        assessment.setTopics(topics);
        return assessment;
    }

    public Map<String, Object> getMyAssessmentTopicByAssessmentAndUser(String assessmentId, String userId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        boolean isAnswer = true;//答题状态  默认答题 
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment == null) {
            return null;
        }
        if (Assessment.ISEXPIRY_1 == assessment.getIsExpiry()) {
            int days = DateUtil.daysBetween(new Date(), assessment.getEndDate());
            if (days < 0 || assessment.getStatus() == Assessment.STATUS_2) {
                //截止日期结束
                isAnswer = false;
            }
        }
        AssessmentUser assessmentUser = assessmentUserManager.getAssessmentUserByAssessmentAndUser(assessmentId, userId);
        if (assessmentUser != null) {
            //已经答过
            isAnswer = false;
        }
        if (isAnswer) {
            //答题
            ret.put("isAnswer", isAnswer);
            ret.put("item", assessmentTopicService.getAssessmentTopicByAssessment(assessmentId));
        } else {
            //结果统计
            ret.put("isAnswer", isAnswer);
            ret.put("item", assessmentResultService.getAssessmentAndTopicInfoByAssessmentIdAndUserId(assessmentId, userId));
        }
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取答卷情况<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年9月29日
     * @param id
     * @return
     */
    public Map<String, Object> getAssessmentResultInfos(String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Assessment assessment = assessmentManager.getAssessmentById(id);
        if (assessment == null) {
            ret.put("success", false);
            ret.put("msg", "获取失败。");
            return ret;
        }
        int submitNum = 0;
        int memberNum = 0;
        double ratio = 0.00;
        List<AssessmentTarget> targets = assessmentTargetService.getAssessmentTargetsByAssessment(id);
        if (targets != null && targets.size() > 0) {
            for (AssessmentTarget target : targets) {
                submitNum += target.getSubmitNum();
                memberNum += target.getMemberNum();
            }
        }
        ret.put("item", assessment);
        ret.put("ccparty", ccpartyManager.getCCPartyById(assessment.getCcpartyId()));
        ret.put("submitNum", submitNum);
        DecimalFormat df = new DecimalFormat("######0.00");
        ratio = ((double) submitNum / memberNum) * 100;
        ret.put("ratio", df.format(ratio));
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取某组织需要参与的答卷列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月11日
     * @param offset
     * @param limit
     * @param search
     * @param ccpartyId
     * @return
     */
    public List<Assessment> getLeaderAssessmentList(Integer offset, Integer limit, String search, String ccpartyId) {
        List<Assessment> assessments = new ArrayList<Assessment>();
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByCcparty(ccpartyId);
        List<Object> ids = new ArrayList<Object>();
        if (targets != null && targets.size() > 0) {
            for (AssessmentTarget target : targets) {
                ids.add(target.getAssessmentId());
            }
        }
        assessments = assessmentManager.getAssessmentsByIds(offset, limit, search, ids, ccpartyId);
        if (assessments != null && assessments.size() > 0) {
            for (Assessment assessment : assessments) {
                assessment.setCcparty(ccpartyManager.getCCPartyFromMc(assessment.getCcpartyId()));
            }
        }
        return assessments;
    }

    /**
     * 
     * <B>方法名称：</B>获取需要参与的答卷数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月11日
     * @param search
     * @param ccpartyId
     * @return
     */
    public Integer getLeaderAssessmentTotal(String search, String ccpartyId) {
        List<Assessment> assessments = new ArrayList<Assessment>();
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByCcparty(ccpartyId);
        List<Object> ids = new ArrayList<Object>();
        if (targets != null && targets.size() > 0) {
            for (AssessmentTarget target : targets) {
                ids.add(target.getAssessmentId());
            }
        }
        return assessmentManager.getLeaderAssessmentTotal(search, ids, ccpartyId);
    }
    
    /**
     * 
     * <B>方法名称：</B>获取我未参与的答卷<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年9月29日
     * @param search
     * @param ccpartyId
     * @param userId
     * @param joinType
     * @return
     */
    public Integer getMyAssessmentNum(String ccpartyId, String userId) {
        User user = userManager.getUser(userId);
        if (user == null || user.getPartyMember() == null) {
            return null;
        }
        return assessmentManager.getMyAssessmentNum(userId, ccpartyId);
    }

}