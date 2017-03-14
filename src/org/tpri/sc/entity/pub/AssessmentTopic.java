package org.tpri.sc.entity.pub;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答题答卷试题<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年8月10日
 */
@Entity
@Table(name = "PUB_ASSESSMENT_TOPIC")
public class AssessmentTopic extends ObjectBase {
    private static final long serialVersionUID = 1L;

    protected int objectType = ObjectType.PUB_ASSESSMENT_TOPIC;

    public static final int TOPIC_TYPE_1 = 1; //试卷类型 单选
    public static final int TOPIC_TYPE_2 = 2; //试卷类型 多选
    public static final int TOPIC_TYPE_3 = 3; //试卷类型 简答
    public static final int TOPIC_ISMUST_0 = 0; //必做
    public static final int TOPIC_ISMUST_1 = 1; //不必做


    protected String assessmentId; //试卷ID
    protected String title; //试题标题
    protected int type; //试题类型 1单选、2多选、3简答
    protected int isMust; //是否必做 0必做、1不必做
    protected String sequence; //试题序号

    protected List<AssessmentTopicOption> options; //该试题的选项
    protected List<AssessmentResult> myAssessresult; //参与调查问卷者给出的答案

    @Transient
    public List<AssessmentTopicOption> getOptions() {
        return options;
    }

    public void setOptions(List<AssessmentTopicOption> options) {
        this.options = options;
    }

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "TYPE")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "IS_MUST")
    public int getIsMust() {
        return isMust;
    }

    public void setIsMust(int isMust) {
        this.isMust = isMust;
    }

    @Column(name = "ASSESSMENT_ID")
    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    @Transient
    public List<AssessmentResult> getMyAssessresult() {
        return myAssessresult;
    }

    public String getSequence() {
        return sequence;
    }

    @Column(name = "SEQUENCE")
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void setMyAssessresult(List<AssessmentResult> myAssessresult) {
        this.myAssessresult = myAssessresult;
    }

}
