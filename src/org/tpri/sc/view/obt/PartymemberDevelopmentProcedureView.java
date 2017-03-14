package org.tpri.sc.view.obt;

import java.util.ArrayList;
import java.util.List;

import org.tpri.sc.entity.obt.DevelopmentProcedureCommonContent;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.obt.PartyMemberDevelopmentInfo;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B>党员发展流程信息<BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年8月2日
 */
public class PartymemberDevelopmentProcedureView {
    public String userName;
    public String ccpartyName;
    public PartyMember partyMember;
    public PartyMemberDevelopmentInfo info;
    public List<DevelopmentProcedureCommonContent> activityContents = new ArrayList<DevelopmentProcedureCommonContent>();//组织生活情况
    public List<DevelopmentProcedureCommonContent> reportContents = new ArrayList<DevelopmentProcedureCommonContent>();//汇报情况
    public List<DevelopmentProcedureCommonContent> inspectContents = new ArrayList<DevelopmentProcedureCommonContent>();//考察情况

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCcpartyName() {
        return ccpartyName;
    }

    public void setCcpartyName(String ccpartyName) {
        this.ccpartyName = ccpartyName;
    }

    public PartyMemberDevelopmentInfo getInfo() {
        return info;
    }

    public void setInfo(PartyMemberDevelopmentInfo info) {
        this.info = info;
    }

    public List<DevelopmentProcedureCommonContent> getActivityContents() {
        return activityContents;
    }

    public void setActivityContents(List<DevelopmentProcedureCommonContent> activityContents) {
        this.activityContents = activityContents;
    }

    public List<DevelopmentProcedureCommonContent> getReportContents() {
        return reportContents;
    }

    public void setReportContents(List<DevelopmentProcedureCommonContent> reportContents) {
        this.reportContents = reportContents;
    }

    public List<DevelopmentProcedureCommonContent> getInspectContents() {
        return inspectContents;
    }

    public void setInspectContents(List<DevelopmentProcedureCommonContent> inspectContents) {
        this.inspectContents = inspectContents;
    }

    public PartyMember getPartyMember() {
        return partyMember;
    }

    public void setPartyMember(PartyMember partyMember) {
        this.partyMember = partyMember;
    }

}
