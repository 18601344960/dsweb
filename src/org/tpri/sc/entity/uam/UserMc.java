package org.tpri.sc.entity.uam;

import java.util.Date;
import java.util.List;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 
 * <B>系统名称：</B>放入缓存中的用户<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年9月24日
 */
public class UserMc extends ObjectBase {
    private static final long serialVersionUID = 1L;

    public UserMc() {
        super();
        objectType = ObjectType.UAM_USER_MC;
    }

    protected int gender; //性别
    protected List<String> allPrivilegeIds;
    protected String ccpartyId = ""; //党组织ID
    protected String type; //人员类型
    protected int userType; //人员类型：0普通用户1党务用户2系统用户
    protected String parentUserId;//所指向的父用户ID（字段党务用户有效）
    protected String sysUserId;//系统用户ID（字段党务用户有效）
    protected Date birthDay; //出生日期

    /**
     * 
     * <B>方法名称：</B>克隆用户数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年9月24日
     * @param user
     * @return
     */
    public static UserMc cloneUser(User user) {
        UserMc userMC = new UserMc();
        userMC.setId(user.getId());
        userMC.setName(user.getName());
        userMC.setParentUserId(user.getParentUserId());
        userMC.setSysUserId(user.getSysUserId());
        userMC.setBirthDay(user.getBirthDay());
        userMC.setGender(user.getGender());
        userMC.setUserType(user.getUserType());
        userMC.setType(user.getType());
        if (user.getUserType() == User.USER_TYPE_1 || user.getUserType() == User.USER_TYPE_2 || user.getUserType() == User.USER_TYPE_3) {
            userMC.setCcpartyId(user.getCcpartyId());
        } else {
            if (user.getPartyMember() != null) {
                String ccpartyId = user.getPartyMember().getCcpartyId();
                userMC.setCcpartyId(ccpartyId);
            }
        }
        userMC.setAllPrivilegeIds(user.getAllPrivilegeIds());

        return userMC;
    }

    /**
     * 
     * <B>方法名称：</B>判断用户是否有权限<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年9月24日
     * @param privilegeId
     * @return
     */
    public boolean hasPrivilege(String privilegeId) {
        return allPrivilegeIds.contains(privilegeId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    public List<String> getAllPrivilegeIds() {
        return allPrivilegeIds;
    }

    public void setAllPrivilegeIds(List<String> allPrivilegeIds) {
        this.allPrivilegeIds = allPrivilegeIds;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
