package org.tpri.sc.view.pub;

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：问卷测评</B><BR>
 * <B>中文类名：问卷测评结果试题答案结果视图类</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月18日
 */
public class AssessmentResultStatisticalAnswerView {
    private String answerId;        //答案ID
    private String answerSeq;       //答案序列
    private String answerContent;   //答案内容
    private int nums;               //答案选择人数
    
    private String userName;        //答案提供者

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswerSeq() {
        return answerSeq;
    }

    public void setAnswerSeq(String answerSeq) {
        this.answerSeq = answerSeq;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
