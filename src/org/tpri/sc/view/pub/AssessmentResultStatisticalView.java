package org.tpri.sc.view.pub;

import java.util.List;

import org.tpri.sc.entity.pub.Assessment;

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：问卷测评</B><BR>
 * <B>中文类名：问卷测评统计分析展现视图类</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月18日
 */
public class AssessmentResultStatisticalView {
    private Assessment assessment; //问卷
    private List<AssessmentResultStatisticalTopicView> topics; //试题集合

    public List<AssessmentResultStatisticalTopicView> getTopics() {
        return topics;
    }

    public void setTopics(List<AssessmentResultStatisticalTopicView> topics) {
        this.topics = topics;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }
}
