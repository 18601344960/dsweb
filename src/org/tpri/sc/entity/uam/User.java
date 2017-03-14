package org.tpri.sc.entity.uam;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.org.CCParty;

/**
 * @description 用户bean
 * @author 易文俊
 * @since 2015-04-09
 */

@Entity
@Table(name = "UAM_USER")
public class User extends ObjectBase implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int STATUS_0 = 0; //正常
    public static final int STATUS_1 = 1; //停用

    public static final String PASSWORD_DEFAULT = "123456";//默认初始密码

    public static final int USER_TYPE_0 = 0; //普通用户
    public static final int USER_TYPE_1 = 1; //党务用户
    public static final int USER_TYPE_2 = 2; //系统用户
    public static final int USER_TYPE_3 = 3; //超级管理员

    public static final String TYPE_01 = "A476201"; //中国共产党党员
    public static final String TYPE_02 = "A476202"; //中国共产党预备党员
    public static final String TYPE_03 = "A476203"; //中国共产主义青年团团员
    public static final String TYPE_13 = "A476213"; //群众
    public static final int DB_TABLE_STATUS_0 = 0; //status字段  0 正常
    public static final int DB_TABLE_STATUS_1 = 1; //status字段  1 挂起
    public static final String GENDER_MAN = "A010701";
    public static final String GENDER_WOMAN = "A010702";

    protected String systemNo; //系统编号
    protected String loginNo; //登录账号
    protected String namePhoneticize;//姓名全拼
    protected String nameFirstCharacter;//姓名首字母
    protected String workNo; //工号
    protected String password; //密码
    protected Timestamp lastLoginTime; //最后一次登陆时间
    protected Timestamp thisLoginTime; //本次登陆时间
    protected String lastLoginIp; //最后一次登陆IP
    protected int loginCount; //登陆次数
    protected int gender; //性别
    protected int status; //状态：0正常1挂起
    protected String description; //描述
    protected String createUserId; //创建人
    protected Timestamp createTime; //创建日期
    protected String updateUserId; //更新人
    protected Timestamp updateTime; //更新日期
    protected String idNumber; //身份号
    protected String type;
    protected String nation; //民族
    protected String occupation; //职业
    protected String education; //教育程度
    protected String degree;//学位
    protected Date birthDay; //出生日期
    protected String birthPlace; //籍贯 
    protected String jobTitle; //行政职务
    protected String officePhone; //办公电话
    protected String mobile; //手机
    protected String email; //电邮
    protected String address; //家庭住址
    protected byte[] icon; //头像
    protected Set<Role> roles;
    protected PartyMember partyMember; //党员
    protected String ccpartyId;//管辖党组织
    protected String parentUserId;//所指向的父用户ID（字段党务用户有效）
    protected String sysUserId;//系统用户ID（字段党务用户有效）
    protected int userType; //人员类型：0普通用户1党务用户2系统用户
    protected String partyTitleId; //党内职务（user_type=1使用）
    protected int sequence = 10000;//序号

    public UserMc parentUser;//党务工作者关联用户
    public CCParty ccparty; //组织视图

    protected List<String> allPrivilegeIds;

    public User() {
        super();
        objectType = ObjectType.UAM_USER;
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String id, String name, int gender, String type) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.type = type;
    }

    @Column(name = "SYSTEM_NO")
    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    @Column(name = "LOGIN_NO")
    public String getLoginNo() {
        return loginNo;
    }

    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }

    @Column(name = "PARENT_USER_ID")
    public String getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
    }

    @Column(name = "SYS_USER_ID")
    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    @Column(name = "USER_TYPE")
    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    @Column(name = "BIRTHDAY")
    public Date getBirthDay() {
        return birthDay;
    }

    @Column(name = "BIRTHPLACE")
    public String getBirthPlace() {
        return birthPlace;
    }

    @Column(name = "CREATE_TIME")
    public Timestamp getCreateTime() {
        return createTime;
    }

    @Column(name = "CREATE_USER_ID")
    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    @Column(name = "EDUCATION")
    public String getEducation() {
        return education;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    @Column(name = "GENDER")
    public int getGender() {
        return gender;
    }

    @Column(name = "ICON")
    public byte[] getIcon() {
        return icon;
    }

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    @Column(name = "ID_NUMBER")
    public String getIdNumber() {
        return idNumber;
    }

    @Column(name = "JOB_TITLE")
    public String getJobTitle() {
        return jobTitle;
    }

    @Column(name = "LAST_LOGIN_IP")
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    @Column(name = "LAST_LOGIN_TIME")
    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    @Column(name = "LOGIN_COUNT")
    public int getLoginCount() {
        return loginCount;
    }

    @Column(name = "MOBILE")
    public String getMobile() {
        return mobile;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    @Column(name = "NATION")
    public String getNation() {
        return nation;
    }

    @Column(name = "OCCUPATION")
    public String getOccupation() {
        return occupation;
    }

    @Column(name = "OFFICE_PHONE")
    public String getOfficePhone() {
        return officePhone;
    }

    @OneToOne(cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = PartyMember.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "PARTY_MEMBER_ID")
    public PartyMember getPartyMember() {
        return partyMember;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    @ManyToMany(cascade = CascadeType.MERGE, targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "UAM_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
    public Set<Role> getRoles() {
        return roles;
    }

    @Column(name = "PARTY_TITLE_ID")
    public String getPartyTitleId() {
        return partyTitleId;
    }

    public void setPartyTitleId(String partyTitleId) {
        this.partyTitleId = partyTitleId;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    @Column(name = "UPDATE_TIME")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    @Column(name = "UPDATE_USER_ID")
    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Column(name = "WORK_NO")
    public String getWorkNo() {
        return workNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public void setPartyMember(PartyMember partyMember) {
        this.partyMember = partyMember;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    @Transient
    public CCParty getCcparty() {
        return ccparty;
    }

    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }

    @Column(name = "CCPARTY_ID")
    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    @Transient
    public UserMc getParentUser() {
        return parentUser;
    }

    public void setParentUser(UserMc parentUser) {
        this.parentUser = parentUser;
    }

    @Column(name = "DEGREE")
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Transient
    public List<String> getAllPrivilegeIds() {
        return allPrivilegeIds;
    }

    public void setAllPrivilegeIds(List<String> allPrivilegeIds) {
        this.allPrivilegeIds = allPrivilegeIds;
    }

    @Column(name = "NAME_PHONETICIZE")
    public String getNamePhoneticize() {
        return namePhoneticize;
    }

    public void setNamePhoneticize(String namePhoneticize) {
        this.namePhoneticize = namePhoneticize;
    }

    @Column(name = "NAME_FIRST_CHARACTER")
    public String getNameFirstCharacter() {
        return nameFirstCharacter;
    }

    public void setNameFirstCharacter(String nameFirstCharacter) {
        this.nameFirstCharacter = nameFirstCharacter;
    }

    @Column(name = "THIS_LOGIN_TIME")
    public Timestamp getThisLoginTime() {
        return thisLoginTime;
    }

    public void setThisLoginTime(Timestamp thisLoginTime) {
        this.thisLoginTime = thisLoginTime;
    }

    @Column(name = "SEQUENCE")
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

}
