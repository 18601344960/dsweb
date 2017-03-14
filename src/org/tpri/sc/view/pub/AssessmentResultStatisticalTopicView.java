package org.tpri.sc.view.pub;

import java.util.List;

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：问卷测评</B><BR>
 * <B>中文类名：问卷测评结果统计分析试题结果视图类</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月18日
 */
public class AssessmentResultStatisticalTopicView {
    private String topicId;     //试题ID
    private String topicSeq;    //试题序列
    private String topicName;   //试题名称
    private int topicType;      //试题类型 单选、多选、简单
    private List<AssessmentResultStatisticalAnswerView> answers; //试题答案结果集

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicSeq() {
        return topicSeq;
    }

    public void setTopicSeq(String topicSeq) {
        this.topicSeq = topicSeq;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<AssessmentResultStatisticalAnswerView> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AssessmentResultStatisticalAnswerView> answers) {
        this.answers = answers;
    }

    public int getTopicType() {
        return topicType;
    }

    public void setTopicType(int topicType) {
        this.topicType = topicType;
    }
    

}
