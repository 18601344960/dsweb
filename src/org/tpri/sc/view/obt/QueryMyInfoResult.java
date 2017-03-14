package org.tpri.sc.view.obt;

import org.tpri.sc.entity.obt.PartyMember;

/**
 * 个人信息实体
 * @author zhaozijing
 *
 */
public class QueryMyInfoResult extends PartyMember{
	/**  */
    private static final long serialVersionUID = 1L;
    protected String id = "";			//登陆账号
	protected String workno = "";		//工号
	protected String password = "";		//登陆密码
	protected String createuser = "";	//创建人
	protected String createtime = "";	//创建时间
	protected int articlecount = 0;		//帖子数
	protected int commentcount = 0;		//回复数
	protected String joindate = "";		//入党时间
	protected String partytype = "";	//党员类型
	protected String ccpartyname = "";	//所属组织名称
	protected String isparty = "no";		//是否党员  no否，其余是
	protected String gender = "未知";	
	protected String nation = "";
	protected String birthPlace = "";
	protected String idNumber = "";
	protected String education = "";
	protected String occupation = "";
	protected String jobTitle = "";
	protected String officePhone = "";
	protected String mobile = "";
	protected String email = "";
	protected String politicsStatus = "";	//政治面貌
	
	
	public String getIsparty() {
		return isparty;
	}
	public void setIsparty(String isparty) {
		this.isparty = isparty;
	}
	public String getCcpartyname() {
		return ccpartyname;
	}
	public void setCcpartyname(String ccpartyname) {
		this.ccpartyname = ccpartyname;
	}
	public String getPartytype() {
		return partytype;
	}
	public void setPartytype(String partytype) {
		this.partytype = partytype;
	}
	public String getJoindate() {
		return joindate;
	}
	public void setJoindate(String joindate) {
		this.joindate = joindate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWorkno() {
		return workno;
	}
	public void setWorkno(String workno) {
		this.workno = workno;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getArticlecount() {
		return articlecount;
	}
	public void setArticlecount(int articlecount) {
		this.articlecount = articlecount;
	}
	public int getCommentcount() {
		return commentcount;
	}
	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthPlace() {
		return birthPlace;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	//政治面貌
	public String getPoliticsStatus() {
		
		return politicsStatus;
	}
	public void setPoliticsStatus(String politicsStatus) {
		this.politicsStatus = politicsStatus;
	}
	
}
