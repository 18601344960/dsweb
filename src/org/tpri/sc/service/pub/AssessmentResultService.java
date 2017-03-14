package org.tpri.sc.service.pub;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.pub.Assessment;
import org.tpri.sc.entity.pub.AssessmentResult;
import org.tpri.sc.entity.pub.AssessmentTopic;
import org.tpri.sc.entity.pub.AssessmentTopicOption;
import org.tpri.sc.entity.pub.AssessmentUser;
import org.tpri.sc.manager.obt.PartyMemberManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.pub.AssessmentManager;
import org.tpri.sc.manager.pub.AssessmentResultManager;
import org.tpri.sc.manager.pub.AssessmentTopicManager;
import org.tpri.sc.manager.pub.AssessmentTopicOptionManager;
import org.tpri.sc.manager.pub.AssessmentUserManager;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.pub.AssessmentResultStatisticalAnswerView;
import org.tpri.sc.view.pub.AssessmentResultStatisticalTopicView;
import org.tpri.sc.view.pub.AssessmentResultStatisticalView;

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：问卷测评</B><BR>
 * <B>中文类名：答卷结果服务类</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月15日
 */
@Service("AssessmentResultService")
public class AssessmentResultService {
    
    public Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private AssessmentResultManager assessmentResultManager;
    @Autowired
    private AssessmentUserManager assessmentUserManager;
    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private AssessmentTopicManager assessmentTopicManager;
    @Autowired
    private AssessmentTopicOptionManager assessmentTopicOptionManager;
    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private PartyMemberManager partyMemberManager;

    /**
     * 
     * <B>方法名称：</B>保存用户答卷结果<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param user
     * @param assessmentId
     * @param answersJson
     * @param reminderId
     * @return
     */
    public Map<String, Object> saveAssessmentResult(String assessmentId, String answersJson, String userId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if(assessment==null){
            ret.put("success", false);
            ret.put("msg", "保存失败，答卷不存在。");
            return ret;
        }
        //测评用户状态更改
        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setId(UUIDUtil.id());
        PartyMember partyMember = partyMemberManager.getPartyMember(userId);
        if (partyMember != null) {
            assessmentUser.setCcpartyId(partyMember.getCcpartyId());
        }
        assessmentUser.setUserId(userId);
        assessmentUser.setAssessmentId(assessment.getId());
        assessmentUser.setSubmitTime(new Timestamp(System.currentTimeMillis()));
        assessmentUserManager.saveOrUpdate(assessmentUser);
        JSONObject jodata = JSONObject.fromObject(answersJson);
        JSONArray ja = JSONArray.fromObject(jodata.get("data"));
        for (int i = 0; i < ja.size(); i++) {
            AssessmentResult result = new AssessmentResult();
            result.setId(UUIDUtil.id());
            result.setAssessmentUserId(assessmentUser.getId());
            JSONObject o = ja.getJSONObject(i);
            if (o.get("topicId") != null && !"".equals(o.get("topicId"))) {
                result.setTopicId(o.getString("topicId"));
            }
            if (o.get("answer") != null && !"".equals(o.get("answer"))) {
                result.setResult(o.getString("answer"));
            }
            assessmentResultManager.saveOrUpdate(result);
        }
        ret.put("success", true);
        ret.put("msg", "已完成答卷,谢谢参与。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：根据问卷ID获取统计结果集</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月18日
     * @param assessmentId
     * @return
     */
    public AssessmentResultStatisticalView getAssessmentResultStatistical(String assessmentId) {
        AssessmentResultStatisticalView view = new AssessmentResultStatisticalView();
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment == null) {
            return null;
        }
        view.setAssessment(assessment);
        this.getAssessmentResultStatisticalForAppraisal(view, assessment.getId());
        return view;
    }

    /**
     * 
     * <B>方法名称：</B>答卷结果统计<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param view
     * @param assessmentId
     * @return
     */
    public AssessmentResultStatisticalView getAssessmentResultStatisticalForAppraisal(AssessmentResultStatisticalView view, String assessmentId) {
        List<AssessmentResultStatisticalTopicView> topicViewss = new ArrayList<AssessmentResultStatisticalTopicView>();
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment==null) {
            return null;
        }
        List<AssessmentTopic> topics = assessmentTopicManager.getTopicByAssessmentId(assessment.getId()); //获取试题集合
        for (AssessmentTopic topic : topics) {
            AssessmentResultStatisticalTopicView topicView = new AssessmentResultStatisticalTopicView();
            topicView.setTopicId(topic.getId());
            topicView.setTopicName(topic.getTitle());
            topicView.setTopicType(topic.getType());
            topicView.setTopicSeq(topic.getSequence());
            List<AssessmentResultStatisticalAnswerView> answerViews = null;
            if (topic.getType()==AssessmentTopic.TOPIC_TYPE_1 || topic.getType()==AssessmentTopic.TOPIC_TYPE_2) {
                answerViews = assessmentManager.getOptionAnswerResults(topic.getId(),assessmentId);
            } else if (topic.getType()==AssessmentTopic.TOPIC_TYPE_3) {
                //简单
                answerViews = assessmentManager.getOptionContentResults(topic.getId(),assessmentId);
            }
            topicView.setAnswers(answerViews);
            topicViewss.add(topicView);
        }
        view.setTopics(topicViewss);
        return view;
    }

    /**
     * 
     * <B>方法名称：获取用户的测试内容及答案</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param assessmentId
     * @param userId
     * @return
     */
    public Assessment getAssessmentAndTopicInfoByAssessmentIdAndUserId(String assessmentId, String userId) {
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment==null || StringUtils.isEmpty(userId)) {
            return null;
        }
        //获取问卷测评
        assessment = this.getAssessmentAndTopicResult(assessment, userId);
        return assessment;
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户的测评结果<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月15日
     * @param assessment
     * @param userId
     * @return
     */
    public Assessment getAssessmentAndTopicResult(Assessment assessment, String userId) {
        AssessmentUser assessmentUser = assessmentUserManager.getAssessmentUserByAssessmentAndUser(assessment.getId(), userId);
        List<AssessmentTopic> topics = assessmentTopicManager.getTopicByAssessmentId(assessment.getId());
        for (AssessmentTopic topic : topics) {
            //step2、根据试题获取选项
            if (AssessmentTopic.TOPIC_TYPE_3!=topic.getType()) {
                //不是简答题有选项需要设置
                List<AssessmentTopicOption> options = assessmentTopicOptionManager.getOptionByTopicId(topic.getId());
                topic.setOptions(options); //设置选项
            }
            //step4、获取测试人员答案
            if (assessmentUser != null) {
                List<AssessmentResult> myassessresult = assessmentResultManager.getAssessresultResultByUser(assessmentUser.getId(), topic.getId());
                for (AssessmentResult assessresult : myassessresult) {
                    if (AssessmentTopic.TOPIC_TYPE_1==topic.getType() || AssessmentTopic.TOPIC_TYPE_2==topic.getType()) {
                        AssessmentTopicOption option = assessmentTopicOptionManager.getOptionById(assessresult.getResult());
                        assessresult.setResultStr(option.getSeq() + "、" + option.getContent());
                    } else {
                        assessresult.setResultStr(assessresult.getResult());
                    }
                    topic.setMyAssessresult(myassessresult);
                }
            }
        }
        //将试题集合放入试卷中
        assessment.setTopics(topics);
        return assessment;
    }
    
}