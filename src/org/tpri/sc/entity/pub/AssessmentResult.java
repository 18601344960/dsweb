package org.tpri.sc.entity.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 
 * <B>系统名称：支部工作</B><BR>
 * <B>模块名称：答题答卷</B><BR>
 * <B>中文类名：答题答卷结果bean</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月17日
 */
@Entity
@Table(name = "PUB_ASSESSMENT_RESULT")
public class AssessmentResult extends ObjectBase {
    private static final long serialVersionUID = 9018054539434507341L;

    protected int objectType = ObjectType.PUB_ASSESSMENT_RESULT;

    protected String id;
    protected String assessmentUserId;//答卷测评人员
    protected String topicId; //试题ID
    protected String result; //测评结果

    protected String resultStr; //转换后的测评结果

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "TOPIC_ID")
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    @Column(name = "RESULT")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Transient
    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    @Column(name = "ASSESSMENT_USER_ID")
    public String getAssessmentUserId() {
        return assessmentUserId;
    }

    public void setAssessmentUserId(String assessmentUserId) {
        this.assessmentUserId = assessmentUserId;
    }

}
