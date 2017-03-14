package org.tpri.sc.view.pub;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>我的答卷视图<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年8月11日
 */
public class MyAssessmentView {
    public String assessmentName;//答卷名称
    public String assessmentId; //答卷id
    public String assessmentCcpartyName;//答卷发起组织名称
    public int joinNum = 0; //参与人数
    public int assessmentType; //答卷状态
    public String overTip;//提示
    public boolean isJoinType = false;//参加情况 默认未参加

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentCcpartyName() {
        return assessmentCcpartyName;
    }

    public void setAssessmentCcpartyName(String assessmentCcpartyName) {
        this.assessmentCcpartyName = assessmentCcpartyName;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public int getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(int assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getOverTip() {
        return overTip;
    }

    public void setOverTip(String overTip) {
        this.overTip = overTip;
    }

    public boolean isJoinType() {
        return isJoinType;
    }

    public void setJoinType(boolean isJoinType) {
        this.isJoinType = isJoinType;
    }

    

}
