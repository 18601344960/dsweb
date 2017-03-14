package org.tpri.sc.entity.obt;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展阶段信息实体<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年12月21日
 */
@Entity
@Table(name = "OBT_PARTY_MEMBER_DEVELOPMENT_INFO")
public class PartyMemberDevelopmentInfo extends ObjectBase {

    private static final long serialVersionUID = -6444299696218348160L;

    protected int objectType = ObjectType.OBT_PARTYMEMBER_DEVELOPMENT_INFO;

    protected String partymemberId;//党员ID
    protected String orgEducationInfo02;//受组织教育情况(积极分子阶段、第2阶)
    protected String workInfo02;//社会工作情况(积极分子阶段、第2阶)
    protected String massesOpinion03;//群众意见(积极分子阶段、第3阶)
    protected String youthOpinion03;//团组织推优意见(积极分子阶段、第3阶)
    protected Integer groupOpinion04=0;//党小组意见0：同意，1不同意，2暂缓。 (积极分子阶段、第4阶)
    protected Date discussDate04;//讨论日期(积极分子阶段、第4阶)
    protected String groupLeader04;//党小组组长(积极分子阶段、第4阶)
    protected Integer branchOpinion05=0;//支委会意见0：同意，1不同意，2暂缓。 (积极分子阶段、第5阶)
    protected Date discussDate05;//讨论日期(积极分子阶段、第5阶)
    protected String secretary05;//党支部书记(积极分子阶段、第5阶)
    protected Integer politicalaAudit07=0;//政审结果0：同意，1不同意，2暂缓。 (发展对象阶段、第7阶)
    protected Date cultivateDate08;//集中培训日期(发展对象阶段、第8阶)
    protected Integer cultivateResult08=0;//培训结果0通过，1不通过(发展对象阶段、第8阶)
    protected String superiorAuditOrg09;//上级预审组织(发展对象阶段、第9阶)
    protected Date auditDate09;//审核日期(发展对象阶段、第9阶)
    protected Integer auditResult09=0;//预审结果0：同意，1不同意，2暂缓。 (发展对象阶段、第9阶)
    protected String noticeInfo10;//公示情况(发展对象阶段、第10阶)
    protected Date branchDiscussDate11;//支部大会讨论日期(发展对象阶段、第11阶)
    protected Integer branchDiscussResult11=0;//支部大会讨论结果0：同意，1不同意，2暂缓。(发展对象阶段、第11阶)
    protected Integer joinCcpartyType11=0;//加入中共组织类型0新吸收为中共预备党员1重新吸收为中共预备党员2直接批准为中共正式党员3直接批准重新入党者为中共正式党员4恢复党籍5追认为中共正式党员
    protected Date talkDate12;//上级谈话日期(发展对象阶段、第12阶)
    protected String talker12;//谈话人(发展对象阶段、第12阶)
    protected String superiorAuditOrg13;//上级审批组织(发展对象阶段、第13阶)
    protected Date auditDate13;//上级审查时间(发展对象阶段、第13阶)
    protected Integer auditResult13=0;//上级审查结果0：同意，1不同意，2暂缓。(发展对象阶段、第13阶)
    protected Integer isSpecialPartymember13=0;//是否为一线特殊发展党员 0否 1是
    protected Integer isSpecialMaxPrivilege13=0;//是否特殊情况下提高审批权限发展 0否 1是
    protected String workInfo14;//社会工作情况(预备党员阶段、第14阶)
    protected Integer groupOpinion15=0;//党小组意见0：同意，1不同意，2暂缓。 (预备党员阶段、第15阶)
    protected Date discussDate15;//讨论日期(预备党员阶段、第15阶)
    protected String groupLeader15;//小组组长(预备党员阶段、第15阶)
    protected String massesOpinion16;//党内外群众意见(预备党员阶段、第16阶)
    protected Integer branchOpinion17=0;//支委会意见0：同意，1不同意，2暂缓。(预备党员阶段、第17阶)
    protected Date discussDate17;//讨论日期(预备党员阶段、第17阶)
    protected String secretary17;//党支部书记(预备党员阶段、第17阶)
    protected Integer branchMeetingResult18=0;//支部大会讨论结果0：同意，1不同意，2暂缓。(预备党员阶段、第18阶)
    protected Date officialDate18;//转正日期(预备党员阶段、第18阶)
    protected Integer officialInfo18=0;//转正情况(预备党员阶段、第18阶) 0按期转正1延长预备期满转正2预备期满尚未讨论或审批3延长预备期4预备期满被取消预备党员资格
    protected String superiorAuditOrg19;//上级审批组织(预备党员阶段、第19阶)
    protected Integer auditResult19=0;//审批结果(预备党员阶段、第19阶)

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "PARTY_MEMBER_ID")
    public String getPartymemberId() {
        return partymemberId;
    }

    public void setPartymemberId(String partymemberId) {
        this.partymemberId = partymemberId;
    }

    @Column(name = "ORG_EDUCATION_INFO_02")
    public String getOrgEducationInfo02() {
        return orgEducationInfo02;
    }

    public void setOrgEducationInfo02(String orgEducationInfo02) {
        this.orgEducationInfo02 = orgEducationInfo02;
    }

    @Column(name = "WORK_INFO_02")
    public String getWorkInfo02() {
        return workInfo02;
    }

    public void setWorkInfo02(String workInfo02) {
        this.workInfo02 = workInfo02;
    }

    @Column(name = "MASSES_OPINION_03")
    public String getMassesOpinion03() {
        return massesOpinion03;
    }

    public void setMassesOpinion03(String massesOpinion03) {
        this.massesOpinion03 = massesOpinion03;
    }

    @Column(name = "YOUTH_OPINION_03")
    public String getYouthOpinion03() {
        return youthOpinion03;
    }

    public void setYouthOpinion03(String youthOpinion03) {
        this.youthOpinion03 = youthOpinion03;
    }

    @Column(name = "GROUP_OPINION_04")
    public int getGroupOpinion04() {
        return groupOpinion04;
    }

    public void setGroupOpinion04(int groupOpinion04) {
        this.groupOpinion04 = groupOpinion04;
    }

    @Column(name = "DISCUSS_DATE_04")
    public Date getDiscussDate04() {
        return discussDate04;
    }

    public void setDiscussDate04(Date discussDate04) {
        this.discussDate04 = discussDate04;
    }

    @Column(name = "GROUP_LEADER_04")
    public String getGroupLeader04() {
        return groupLeader04;
    }

    public void setGroupLeader04(String groupLeader04) {
        this.groupLeader04 = groupLeader04;
    }

    @Column(name = "BRANCH_OPINION_05")
    public int getBranchOpinion05() {
        return branchOpinion05;
    }

    public void setBranchOpinion05(int branchOpinion05) {
        this.branchOpinion05 = branchOpinion05;
    }

    @Column(name = "DISCUSS_DATE_05")
    public Date getDiscussDate05() {
        return discussDate05;
    }

    public void setDiscussDate05(Date discussDate05) {
        this.discussDate05 = discussDate05;
    }

    @Column(name = "SECRETARY_05")
    public String getSecretary05() {
        return secretary05;
    }

    public void setSecretary05(String secretary05) {
        this.secretary05 = secretary05;
    }

    @Column(name = "POLITICAL_AUDIT_07")
    public int getPoliticalaAudit07() {
        return politicalaAudit07;
    }

    public void setPoliticalaAudit07(int politicalaAudit07) {
        this.politicalaAudit07 = politicalaAudit07;
    }

    @Column(name = "CULTIVATE_DATE_08")
    public Date getCultivateDate08() {
        return cultivateDate08;
    }

    public void setCultivateDate08(Date cultivateDate08) {
        this.cultivateDate08 = cultivateDate08;
    }

    @Column(name = "CULTIVATE_RESULT_08")
    public int getCultivateResult08() {
        return cultivateResult08;
    }

    public void setCultivateResult08(int cultivateResult08) {
        this.cultivateResult08 = cultivateResult08;
    }

    @Column(name = "SUPERIOR_AUDIT_ORG_09")
    public String getSuperiorAuditOrg09() {
        return superiorAuditOrg09;
    }

    public void setSuperiorAuditOrg09(String superiorAuditOrg09) {
        this.superiorAuditOrg09 = superiorAuditOrg09;
    }

    @Column(name = "AUDIT_DATE_09")
    public Date getAuditDate09() {
        return auditDate09;
    }

    public void setAuditDate09(Date auditDate09) {
        this.auditDate09 = auditDate09;
    }

    @Column(name = "AUDIT_RESULT_09")
    public int getAuditResult09() {
        return auditResult09;
    }

    public void setAuditResult09(int auditResult09) {
        this.auditResult09 = auditResult09;
    }

    @Column(name = "NOTICE_INFO_10")
    public String getNoticeInfo10() {
        return noticeInfo10;
    }

    public void setNoticeInfo10(String noticeInfo10) {
        this.noticeInfo10 = noticeInfo10;
    }

    @Column(name = "BRANCH_DISCUSS_DATE_11")
    public Date getBranchDiscussDate11() {
        return branchDiscussDate11;
    }

    public void setBranchDiscussDate11(Date branchDiscussDate11) {
        this.branchDiscussDate11 = branchDiscussDate11;
    }

    @Column(name = "BRANCH_DISCUSS_RESULT_11")
    public int getBranchDiscussResult11() {
        return branchDiscussResult11;
    }

    public void setBranchDiscussResult11(int branchDiscussResult11) {
        this.branchDiscussResult11 = branchDiscussResult11;
    }

    @Column(name = "TALK_DATE_12")
    public Date getTalkDate12() {
        return talkDate12;
    }

    public void setTalkDate12(Date talkDate12) {
        this.talkDate12 = talkDate12;
    }

    @Column(name = "TALKER_12")
    public String getTalker12() {
        return talker12;
    }

    public void setTalker12(String talker12) {
        this.talker12 = talker12;
    }

    @Column(name = "SUPERIOR_AUDIT_ORG_13")
    public String getSuperiorAuditOrg13() {
        return superiorAuditOrg13;
    }

    public void setSuperiorAuditOrg13(String superiorAuditOrg13) {
        this.superiorAuditOrg13 = superiorAuditOrg13;
    }

    @Column(name = "AUDIT_DATE_13")
    public Date getAuditDate13() {
        return auditDate13;
    }

    public void setAuditDate13(Date auditDate13) {
        this.auditDate13 = auditDate13;
    }

    @Column(name = "AUDIT_RESULT_13")
    public int getAuditResult13() {
        return auditResult13;
    }

    public void setAuditResult13(int auditResult13) {
        this.auditResult13 = auditResult13;
    }

    @Column(name = "WORK_INFO_14")
    public String getWorkInfo14() {
        return workInfo14;
    }

    public void setWorkInfo14(String workInfo14) {
        this.workInfo14 = workInfo14;
    }

    @Column(name = "GROUP_OPINION_15")
    public int getGroupOpinion15() {
        return groupOpinion15;
    }

    public void setGroupOpinion15(int groupOpinion15) {
        this.groupOpinion15 = groupOpinion15;
    }

    @Column(name = "DISCUSS_DATE_15")
    public Date getDiscussDate15() {
        return discussDate15;
    }

    public void setDiscussDate15(Date discussDate15) {
        this.discussDate15 = discussDate15;
    }

    @Column(name = "GROUP_LEADER_15")
    public String getGroupLeader15() {
        return groupLeader15;
    }

    public void setGroupLeader15(String groupLeader15) {
        this.groupLeader15 = groupLeader15;
    }

    @Column(name = "MASSES_OPINION_16")
    public String getMassesOpinion16() {
        return massesOpinion16;
    }

    public void setMassesOpinion16(String massesOpinion16) {
        this.massesOpinion16 = massesOpinion16;
    }

    @Column(name = "BRANCH_OPINION_17")
    public int getBranchOpinion17() {
        return branchOpinion17;
    }

    public void setBranchOpinion17(int branchOpinion17) {
        this.branchOpinion17 = branchOpinion17;
    }

    @Column(name = "DISCUSS_DATE_17")
    public Date getDiscussDate17() {
        return discussDate17;
    }

    public void setDiscussDate17(Date discussDate17) {
        this.discussDate17 = discussDate17;
    }

    @Column(name = "SECRETARY_17")
    public String getSecretary17() {
        return secretary17;
    }

    public void setSecretary17(String secretary17) {
        this.secretary17 = secretary17;
    }

    @Column(name = "BRANCH_MEETING_RESULT_18")
    public int getBranchMeetingResult18() {
        return branchMeetingResult18;
    }

    public void setBranchMeetingResult18(int branchMeetingResult18) {
        this.branchMeetingResult18 = branchMeetingResult18;
    }

    @Column(name = "OFFICIAL_DATE_18")
    public Date getOfficialDate18() {
        return officialDate18;
    }

    public void setOfficialDate18(Date officialDate18) {
        this.officialDate18 = officialDate18;
    }

    @Column(name = "OFFICIAL_INFO_18")
    public int getOfficialInfo18() {
        return officialInfo18;
    }

    public void setOfficialInfo18(int officialInfo18) {
        this.officialInfo18 = officialInfo18;
    }

    @Column(name = "SUPERIOR_AUDIT_ORG_19")
    public String getSuperiorAuditOrg19() {
        return superiorAuditOrg19;
    }

    public void setSuperiorAuditOrg19(String superiorAuditOrg19) {
        this.superiorAuditOrg19 = superiorAuditOrg19;
    }

    @Column(name = "AUDIT_RESULT_19")
    public int getAuditResult19() {
        return auditResult19;
    }

    public void setAuditResult19(int auditResult19) {
        this.auditResult19 = auditResult19;
    }

    @Column(name = "JOIN_CCPARTY_TYPE_11")
    public int getJoinCcpartyType11() {
        return joinCcpartyType11;
    }

    public void setJoinCcpartyType11(int joinCcpartyType11) {
        this.joinCcpartyType11 = joinCcpartyType11;
    }

    @Column(name = "IS_SPECIAL_PARTY_MEMBER_13")
    public int getIsSpecialPartymember13() {
        return isSpecialPartymember13;
    }

    public void setIsSpecialPartymember13(int isSpecialPartymember13) {
        this.isSpecialPartymember13 = isSpecialPartymember13;
    }

    @Column(name = "IS_SPECIAL_MAX_PRIVILEGE_13")
    public int getIsSpecialMaxPrivilege13() {
        return isSpecialMaxPrivilege13;
    }

    public void setIsSpecialMaxPrivilege13(int isSpecialMaxPrivilege13) {
        this.isSpecialMaxPrivilege13 = isSpecialMaxPrivilege13;
    }

}
