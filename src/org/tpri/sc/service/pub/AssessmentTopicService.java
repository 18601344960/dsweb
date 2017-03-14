package org.tpri.sc.service.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.pub.Assessment;
import org.tpri.sc.entity.pub.AssessmentTopic;
import org.tpri.sc.entity.pub.AssessmentTopicOption;
import org.tpri.sc.manager.pub.AssessmentManager;
import org.tpri.sc.manager.pub.AssessmentTopicManager;
import org.tpri.sc.manager.pub.AssessmentTopicOptionManager;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答卷服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年6月29日
 */
@Service("AssessmentTopicService")
public class AssessmentTopicService {
    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private AssessmentTopicManager assessmentTopicManager;
    @Autowired
    private AssessmentTopicOptionManager assessmentTopicOptionManager;

    /**
     * 
     * <B>方法名称：</B>获取答题答卷<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月29日
     * @param assessmentId
     * @return
     */
    public Assessment getAssessmentTopicByAssessment(String assessmentId) {
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment != null) {
            //step1、获取试题
            List<AssessmentTopic> topics = assessmentTopicManager.getTopicByAssessmentId(assessment.getId());
            if (topics != null && topics.size() > 0) {
                for (AssessmentTopic topic : topics) {
                    //step2、根据试题获取选项
                    if (AssessmentTopic.TOPIC_TYPE_3!=topic.getType()) {
                        //不是简答题有选项需要设置
                        List<AssessmentTopicOption> options = assessmentTopicOptionManager.getOptionByTopicId(topic.getId());
                        topic.setOptions(options); //设置选项
                    }
                }
                //将试题集合放入试卷中
                assessment.setTopics(topics);
            }
        }
        return assessment;
    }

    /**
     * 
     * <B>方法名称：</B>获取试题详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月16日
     * @param topicId
     * @return
     */
    public AssessmentTopic getAssessmentTopicById(String topicId) {
        AssessmentTopic topic = assessmentTopicManager.getAssessmentTopicById(topicId);
        topic.setOptions(assessmentTopicOptionManager.getOptionByTopicId(topicId));
        return topic;
    }

    /**
     * 
     * <B>方法名称：</B>获取试题最大题号<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月16日
     * @param assessmentId
     * @return
     */
    public Map<String, Object> getMaxSeqAssessmentTopicByAssessment(String assessmentId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        int maxSeq = assessmentTopicManager.getMaxSeqAssessmentTopicByAssessment(assessmentId);
        ret.put("sequence", maxSeq + 1);
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>删除试题<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月16日
     * @param topicId
     * @return
     */
    public Map<String, Object> deleteAssessmentTopicByTopic(String topicId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        AssessmentTopic topic = assessmentTopicManager.getAssessmentTopicById(topicId);
        if (topic == null) {
            ret.put("success", false);
            ret.put("msg", "删除失败，试题为空。");
            return ret;
        }
        //删除选项
        List<AssessmentTopicOption> options = assessmentTopicOptionManager.getOptionByTopicId(topicId);
        for (AssessmentTopicOption option : options) {
            assessmentTopicOptionManager.delete(option.getId(), ObjectType.PUB_ASSESSMENT_TOPIC_OPTION);
        }
        //删除试题
        assessmentTopicManager.delete(topic);
        ret.put("success", true);
        ret.put("msg", "已成功删除。");
        return ret;
    }
}