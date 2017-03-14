package org.tpri.sc.manager.uam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.RolePrivilege;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;

/**
 * @description 用户管理类
 * @author 易文俊
 * @since 2015-04-09
 */

@Repository("UserManager")
public class UserManager extends ManagerBase {

    private static boolean initialized = false;

    public void initialize() {
        if (initialized)
            return;
        initialized = true;
        ObjectRegister.registerClass(ObjectType.UAM_USER, User.class);
        ObjectRegister.registerClass(ObjectType.UAM_USER_MC, UserMc.class);
        List list = loadList(ObjectType.UAM_USER, null, null, null, null);
        List<UserMc> userMcs = new ArrayList<UserMc>();
        for (int i = 0; i < list.size(); i++) {
            User user = (User) list.get(i);
            getAllPrivilegeIds(user);
            UserMc userMc = UserMc.cloneUser(user);
            mc.addObject(ObjectType.UAM_USER_MC + "_" + user.getId(), userMc);
        }
    }

    public void getAllPrivilegeIds(User user) {
        Set<Role> roles = user.getRoles();
        List<String> allPrivilegeIds = new ArrayList<String>();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                List rolePrivileges = getRolePrivilegesFromMc(role.getId());
                if (rolePrivileges != null && rolePrivileges.size() > 0) {
                    for (int i = 0; i < rolePrivileges.size(); i++) {
                        RolePrivilege rolePrivilege = (RolePrivilege) rolePrivileges.get(i);
                        allPrivilegeIds.add(rolePrivilege.getPrivilegeId());
                    }
                }
            }
        }
        user.setAllPrivilegeIds(allPrivilegeIds);
    }

    /**
     * 获取角色的权限列表
     * 
     * @return
     */
    public List<RolePrivilege> getRolePrivilegesFromMc(String roleId) {
        List<RolePrivilege> list = new ArrayList<RolePrivilege>();
        List<ObjectBase> objectList = loadMcList(ObjectType.UAM_ROLE_PRIVILEGE);
        for (ObjectBase objectBase : objectList) {
            RolePrivilege rolePrivilege = (RolePrivilege) objectBase;
            if (rolePrivilege != null && rolePrivilege.getRoleId() != null && rolePrivilege.getRoleId().equals(roleId)) {
                list.add(rolePrivilege);
            }
        }
        return list;
    }

    /**
     * 添加用户
     * 
     * @return
     */
    public void addUser(User user) {
        saveOrUpdate(user);
        getAllPrivilegeIds(user);
        UserMc userMc = UserMc.cloneUser(user);
        addCache(ObjectType.UAM_USER_MC + "_" + user.getId(), userMc);
    }

    /**
     * 更新用户
     * 
     * @return
     */
    public void updateUser(User user) {
        saveOrUpdate(user);
        getAllPrivilegeIds(user);
        UserMc userMc = UserMc.cloneUser(user);
        updateCache(ObjectType.UAM_USER_MC + "_" + user.getId(), userMc);
    }

    /**
     * 删除用户
     * 
     * @return
     */
    public void deleteUser(User user) {
        UserMc userMc = getUserFromMc(user.getId());
        this.delete(user.getId(), ObjectType.UAM_USER);
        removeCache(ObjectType.UAM_USER_MC + "_" + user.getId());
    }

    /**
     * 从缓存中获取用户
     * 
     * @return
     */
    public UserMc getUserFromMc(String id) {
        UserMc userMc = (UserMc) getCache(ObjectType.UAM_USER_MC + "_" + id);
        return userMc;
    }

    /**
     * 获取用户
     * 
     * @return
     */
    public User getUser(String id) {
        Object obj = this.loadOne(ObjectType.UAM_USER, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        User user = (User) obj;
        return user;
    }

    /**
     * 
     * <B>方法名称：</B>根据用户账号和密码获取用户信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月25日
     * @param userId
     * @param password
     * @return
     */
    public User getUserByLoginNoAndPwd(String userId, String password) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("loginNo", userId));
        daoPara.addCondition(Condition.EQUAL("password", password));
        return (User) dao.loadOne(daoPara);
    }

    /**
     * 身份证号获取用户
     * 
     * @return
     */
    public User getUserByIdNumber(String idNumber) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("idNumber", idNumber));

        List list = dao.loadList(daoPara);
        if (list != null && list.size() > 0) {
            return (User) list.get(0);
        }
        return null;
    }

    /**
     * 获取用户列表
     * 
     * @return
     */
    public List<User> getUserList(String classId, Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("createTime"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 获取角色
     * 
     * @return
     */
    public Role getRole(String id) {
        return (Role) this.loadOne(ObjectType.UAM_ROLE, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 获取角色列表
     * 
     * @return
     */
    public List<Role> getRoleList(Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Role.class);

        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 根据身份证号获取符合的结果记录
     * 
     * @return
     */
    public List<User> getUsersByIdnumber(String idNumber) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("idNumber", idNumber));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取组织下的用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月3日
     * @param ccpartyId
     * @return
     */
    public List<User> getUserByCcparty(String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select new User(u.id,u.name,u.gender,u.type) from User as u left join u.partyMember as pm ");
        hql.append(" where u.status=0 and pm.ccpartyId=:ccpartyId ");       
        hql.append(" and u.userType=:userType ");
        hql.append(" and u.type in (:type) ");
        hql.append(" and pm.type in(3,4) ");
        hql.append(" order by u.sequence asc,u.nameFirstCharacter asc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("ccpartyId", ccpartyId).setInteger("userType", User.USER_TYPE_0).setParameterList("type", new String[]{User.TYPE_01,User.TYPE_02});        
        return query.list();
    }

    public void setUserNotPartymemberByPartymember(String partymemberId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" update User as u set u.type=:type,u.partyMember=null ");
        hql.append(" where u.partyMember.id=:partymemberId ");
        dao.getCurrentSession().createQuery(hql.toString()).setString("type", User.TYPE_13).setString("partymemberId", partymemberId).executeUpdate();

    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的系统用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月12日
     * @param ccpartyId
     * @return
     */
    public List<User> getSysUserByCcparty(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_2));
        daoPara.addOrder(Order.asc("sequence"));
        daoPara.addOrder(Order.asc("nameFirstCharacter"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据创建人获取党务工作者列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月29日
     * @param createUserId
     * @return
     */
    public List<User> getPartyWorkersBySysUser(String createUserId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("sysUserId", createUserId));
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_1));
        daoPara.addOrder(Order.asc("sequence"));
        daoPara.addOrder(Order.asc("nameFirstCharacter"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取系统用户记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月11日
     * @return
     */
    public Integer getSystemUsersTotal(String search, List<Object> ccpartyIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        if (ccpartyIds != null && ccpartyIds.size() > 0) {
            daoPara.addCondition(Condition.IN("ccpartyId", ccpartyIds));
        }
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_2));
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        Integer total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B>获取用户的所有党务角色<BR>
     * 
     * @author 易文俊
     * @since 2016年3月15日
     * @param userId
     * @return
     */
    public List<User> getRoleUsers(String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("parentUserId", userId));
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_1));
        daoPara.addOrder(Order.asc("sysUserId"));
        daoPara.addOrder(Order.asc("sequence"));
        daoPara.addOrder(Order.asc("nameFirstCharacter"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取系统用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月11日
     * @param start
     * @param limit
     * @return
     */
    public List<User> getSystemUsers(String search, Integer start, Integer limit, List<Object> ccpartyIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_2));
        if (ccpartyIds != null && ccpartyIds.size() > 0) {
            daoPara.addCondition(Condition.IN("ccpartyId", ccpartyIds));
        }
        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        daoPara.addOrder(Order.asc("ccpartyId"));
        daoPara.addOrder(Order.asc("sequence"));
        daoPara.addOrder(Order.asc("nameFirstCharacter"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据登录账号获取用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月20日
     * @param loginNo
     * @return
     */
    public User getUserByLoginNo(String loginNo) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("loginNo", loginNo));
        return (User) dao.loadOne(daoPara);
    }

    /**
     * 
     * <B>方法名称：</B>根据组织ID获取该组织下的所有党务工作者列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月16日
     * @param search
     * @param offset
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<Object> getPartyWorkersByCcparty(String search, Integer offset, Integer limit, List<Object> ccpartyIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select worker.id from uam_user as worker left join uam_user as parent on worker.parent_user_id=parent.id ");
        sql.append(" where worker.user_type=:userType and worker.sys_user_id in(:ccpartyIds) ");
        if(!StringUtils.isEmpty(search)){
            sql.append(" and parent.name like :search ");
        }
        sql.append(" order by worker.sys_user_id asc,parent.sequence asc,parent.NAME_FIRST_CHARACTER asc ");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setInteger("userType", User.USER_TYPE_1).setParameterList("ccpartyIds", ccpartyIds);
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        return query.list();
    }

    /**
     * <B>方法名称：</B>根据系统用户和父用户以及党内职务ID获取党务工作者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月13日
     * @param ccpartyId
     * @param parentUserId
     * @param partyTitleId
     * @return
     */
    public List<User> getPartyWorkerByCcpartyAndParentUser(String sysUserId, String parentUserId, String partyTitleId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from User as u where u.userType=:userType ");
        hql.append(" and u.parentUserId=:parentUserId and u.sysUserId=:sysUserId ");
        if(!StringUtils.isEmpty(partyTitleId)){
            hql.append(" and u.partyTitleId=:partyTitleId ");
        }
        hql.append(" order by u.sequence asc,u.nameFirstCharacter asc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setInteger("userType", User.USER_TYPE_1).setString("sysUserId", sysUserId).setString("parentUserId", parentUserId);
        if(!StringUtils.isEmpty(partyTitleId)){
            query.setString("partyTitleId", partyTitleId);
        }
        List list = query.list();
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据组织ID获取该组织下的所有党务工作者记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月16日
     * @param search
     * @param ccpartyId
     * @return
     */
    public Integer getPartyWorkersTotalByCcparty(String search, List<Object> ccpartyIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(worker.id) from uam_user as worker left join uam_user as parent on worker.parent_user_id=parent.id ");
        sql.append(" where worker.user_type=:userType and worker.sys_user_id in(:ccpartyIds) ");
        if(!StringUtils.isEmpty(search)){
            sql.append(" and parent.name like :search ");
        }
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setInteger("userType", User.USER_TYPE_1).setParameterList("ccpartyIds", ccpartyIds);
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        if (query.uniqueResult() == null) {
            return 0;
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }

    /**
     * 
     * <B>方法名称：</B>根据组织ID获取该组织下的所有党务工作者列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月16日
     * @param ccpartyId
     * @return
     */
    public List<User> getPartyWorkersByCcparty(String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from User as u where u.userType=:userType ");
        hql.append(" and u.sysUserId in( ");
        hql.append(" select u.id from User as u where u.ccpartyId=:ccpartyId ) ");
        hql.append(" order by u.sequence asc,u.nameFirstCharacter asc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setInteger("userType", User.USER_TYPE_1).setString("ccpartyId", ccpartyId);
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>根据系统编号获取普通用户条数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月15日
     * @param search
     * @param systemNo
     * @return
     */
    public Integer getGeneralUsersTotalBySysNo(String search, String systemNo, List<Object> organizationIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("systemNo", systemNo));
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_0));
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        if (organizationIds != null && organizationIds.size() > 0) {
            daoPara.addCondition(Condition.IN("organization.id", organizationIds));
        }
        return dao.getTotalCount(daoPara);
    }

    /**
     * 
     * <B>方法名称：</B>获取下级系统用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月11日
     * @param start
     * @param limit
     * @return
     */
    public List<User> getChildSystemUsers(String search, Integer start, Integer limit, List<Object> ccpartyIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_2));
        daoPara.addOrder(Order.asc("sequence"));
        daoPara.addOrder(Order.asc("nameFirstCharacter"));
        if (ccpartyIds != null && ccpartyIds.size() > 0) {
            daoPara.addCondition(Condition.IN("loginNo", ccpartyIds));
        } else {
            return null;
        }
        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        daoPara.addOrder(Order.asc("loginNo"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取下级系统用户记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月11日
     * @return
     */
    public Integer getChildSystemUsersTotal(String search, List<Object> ccpartyIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_2));
        if (ccpartyIds != null && ccpartyIds.size() > 0) {
            daoPara.addCondition(Condition.IN("loginNo", ccpartyIds));
        } else {
            return 0;
        }
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        Integer total = dao.getTotalCount(daoPara);
        return total;
    }
    
    /**
     * 
     * <B>方法名称：</B>根据党组织获取所属用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月2日
     * @param ccpartyId
     * @return
     */
    public List<User> getUsersByCcparty(String ccpartyId, String search) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from User as u ");
        hql.append(" where u.status=0 and u.partyMember.ccpartyId=:ccpartyId ");
        if (!StringUtils.isEmpty(search)) {
            hql.append(" and u.name like :search ");
        }
        hql.append(" and u.partyMember.type in(:partyMemberType) and u.partyMember.status in(:partyMemberStatus)  ");
        hql.append(" and u.userType=:userType ");
        hql.append(" and u.type in (:type) ");
        hql.append(" and u.partyMember.type in(3,4) ");
        hql.append(" order by u.partyMember.type desc, u.sequence asc,u.nameFirstCharacter asc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("ccpartyId", ccpartyId).setInteger("userType", User.USER_TYPE_0)
                .setParameterList("type", new String[] { User.TYPE_01, User.TYPE_02 }).setParameterList("partyMemberType", new Object[] { PartyMember.TYPE_3, PartyMember.TYPE_4 })
                .setParameterList("partyMemberStatus", new Object[] { PartyMember.STATUS_0 });
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        return query.list();
    }
    
}
