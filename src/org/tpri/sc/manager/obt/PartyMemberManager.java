package org.tpri.sc.manager.obt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.uam.User;

/**
 * 
 * <B>系统名称：</B>党建系统<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年10月13日
 */
@Repository("PartyMemberManager")
public class PartyMemberManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.OBT_PARTY_MEMBER, PartyMember.class);
    }

    /**
     * <B>方法名称：</B>获取所有党员（预备党员和正式党员），如果ccpartyId有值则获取某组织的党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月12日
     * @param start
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<PartyMember> getFormalPartyMemberList(String ccpartyId, Integer offset, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMember.class);
        if (ccpartyId != null) {
            daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        }
        List<Object> types = new ArrayList<Object>();
        types.add(PartyMember.TYPE_3);
        types.add(PartyMember.TYPE_4);
        daoPara.addCondition(Condition.IN("type", types));
        List<Object> statuses = new ArrayList<Object>();
        statuses.add(PartyMember.STATUS_0);
        statuses.add(PartyMember.STATUS_1);
        daoPara.addCondition(Condition.IN("status", statuses));
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取所有党员数目（预备党员和正式党员），如果ccpartyId有值则获取某组织的党员数目<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月12日
     * @param ccpartyId
     * @return
     */
    public Integer getFormalPartyMemberTotal(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMember.class);
        if (ccpartyId != null) {
            daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        }
        List<Object> types = new ArrayList<Object>();
        types.add(PartyMember.TYPE_3);
        types.add(PartyMember.TYPE_4);
        daoPara.addCondition(Condition.IN("type", types));
        List<Object> statuses = new ArrayList<Object>();
        statuses.add(PartyMember.STATUS_0);
        statuses.add(PartyMember.STATUS_1);
        daoPara.addCondition(Condition.IN("status", statuses));
        Integer total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * 
     * <B>方法名称：</B>获取党员详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param id
     * @return
     */
    public PartyMember getPartyMember(String id) {
        return (PartyMember) this.loadOne(ObjectType.OBT_PARTY_MEMBER, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 
     * <B>方法名称：</B>获取党员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param start
     * @param limit
     * @return
     */
    public List<PartyMember> getPartyMemberList(Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMember.class);

        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取所有党员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @return
     */
    public List<PartyMember> getAllPartyMember() {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMember.class);
        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取党员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param start
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<PartyMember> getPartyMemberByCcpartyId(Integer start, Integer limit, String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMember.class);

        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取党员列表数目<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param ccpartyId
     * @return
     */
    public Integer getPartyMemberTotalByCcpartyId(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMember.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        Integer total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * 
     * <B>方法名称：</B>获取某党组织下的党员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param ccpartyId
     * @return
     */
    public List<PartyMember> loadPartyMemberByPartyId(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMember.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.addOrder(Order.asc("ccpartyId"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据身份证号码获取党User<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param idNumber
     * @return
     */
    public List<User> loadPartyMemberByIdNumber(String idNumber) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("idNumber", idNumber));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据会员ID获取党员信息<BR>
     * <B>概要说明：</B>党员ID和用户ID为同一个（身份证号码）<BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param userId
     * @return
     */
    public PartyMember getPartyMemberByUserId(String userId) {
        return (PartyMember) this.loadOne(ObjectType.OBT_PARTY_MEMBER, new String[] { "id" }, new Object[] { userId });
    }

    /**
     * 
     * <B>方法名称：</B>统计党员学历构成<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param ccpartyId
     * @return
     */
    public List<Object> partymemberEducationStatistics(String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select u.partyMember.ccpartyId,u.education,count(u.education) ");
        hql.append(" from User as u ");
        hql.append(" where u.partyMember.ccpartyId in (");
        hql.append(" select c.id from CCParty as c where c.id=:ccpartyId or c.parentId=:ccpartyId ) ");
        hql.append(" and u.education != '' and u.education!=null and u.userType=0 ");
        hql.append(" group by u.education ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("ccpartyId", ccpartyId);
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>删除党组织下的所有党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月24日
     * @param ccpartyId
     */
    public void deleteByCcparty(String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" delete from PartyMember as pm where pm.ccpartyId=:ccpartyId ");
        dao.getCurrentSession().createQuery(hql.toString()).setString("ccpartyId", ccpartyId).executeUpdate();
    }

    /**
     * 
     * <B>方法名称：</B>获取党组织下的党员总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param ccpartyId
     * @return
     */
    public int getPartymemberNums(List<Object> ccpartyIds) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(pm.id) ");
        hql.append(" from PartyMember as pm where pm.ccpartyId in(:ccpartyIds) ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setParameterList("ccpartyIds", ccpartyIds);
        return (new Integer(query.uniqueResult().toString())).intValue();
    }

    /**
     * 
     * <B>方法名称：</B>获取组织集合的党员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月13日
     * @param statisticsCcpartyIds
     * @return
     */
    public List<PartyMember> getPartyMembersByCcpartys(List<Object> statisticsCcpartyIds) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select u.partyMember ");
        hql.append(" from User as u where u.userType=:userType and u.type in(:type) and u.partyMember.status in(:partyMemberStatus)");
        hql.append(" and u.partyMember.ccpartyId in (:ids) ");
        hql.append(" and u.status=:status ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setInteger("status", User.DB_TABLE_STATUS_0).setParameterList("partyMemberStatus", new Object[] { PartyMember.STATUS_0 }).setInteger("userType", User.USER_TYPE_0)
                .setParameterList("type", new String[] { User.TYPE_01, User.TYPE_02 });
        query.setParameterList("ids", statisticsCcpartyIds);
        List list = query.list();
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据组织获取是党员的用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月16日
     * @return
     */
    public List<User> getUserPartymembersByCcpartys(String search, Integer offset, Integer limit, List<Object> ccpartyIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_0));
        daoPara.addCondition(Condition.IN("partyMember.ccpartyId", ccpartyIds));
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        daoPara.addOrder(Order.asc("partyMember.ccpartyId"));
        daoPara.addOrder(Order.asc("nameFirstCharacter"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据组织获取是党员的用户列表条数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月16日
     * @param search
     * @param ccpartyIds
     * @return
     */
    public Integer getUserPartymembersByCcpartysTotal(String search, List<Object> ccpartyIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("userType", User.USER_TYPE_0));
        daoPara.addCondition(Condition.IN("partyMember.ccpartyId", ccpartyIds));
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        daoPara.addOrder(Order.asc("partyMember.ccpartyId"));
        daoPara.addOrder(Order.asc("nameFirstCharacter"));
        return (Integer) dao.getTotalCount(daoPara);
    }

    /**
     * <B>方法名称：</B>获取某组织及其下属组织的所有成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月13日
     * @param search
     * @param offset
     * @param limit
     * @param ccpartyIds
     * @return
     */
    public List<User> getAllPartyMembersByCcpartyId(String search, Integer offset, Integer limit, List<Object> ccpartyIds) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from User as u where u.type=:type and u.userType=:userType ");
        if (!StringUtils.isEmpty(search)) {
            hql.append(" and u.name like :search ");
        }
        hql.append(" and u.partyMember!=null and u.partyMember.type in(:partyMemberType) and u.partyMember.status in(:partyMemberStatus) ");
        hql.append(" and u.partyMember.ccpartyId in(:ccpartyIds) ");
        hql.append(" order by u.partyMember.ccpartyId asc,u.sequence asc,u.nameFirstCharacter asc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("type", User.TYPE_01).setInteger("userType", User.USER_TYPE_0)
                .setParameterList("partyMemberType", new Object[] { PartyMember.TYPE_3, PartyMember.TYPE_4 })
                .setParameterList("partyMemberStatus", new Object[] { PartyMember.STATUS_0, PartyMember.STATUS_1 }).setParameterList("ccpartyIds", ccpartyIds);
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        return query.list();
    }

    /**
     * <B>方法名称：</B>获取某组织及其下属组织的所有成员总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月13日
     * @param search
     * @param ccpartyIds
     * @return
     */
    public Integer getAllPartyMembersByCcpartyIdTotal(String search, List<Object> ccpartyIds) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(u.id) from User as u where u.type in(:type) and u.userType=:userType ");
        if (!StringUtils.isEmpty(search)) {
            hql.append(" and u.name like :search ");
        }
        hql.append(" and u.partyMember!=null and u.partyMember.type in(:partyMemberType) and u.partyMember.status in(:partyMemberStatus) ");
        hql.append(" and u.partyMember.ccpartyId in(:ccpartyIds) ");
        hql.append(" order by u.partyMember.ccpartyId asc,u.nameFirstCharacter asc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setParameterList("type",  new String[] { User.TYPE_01, User.TYPE_02 }).setInteger("userType", User.USER_TYPE_0)
                .setParameterList("partyMemberType", new Object[] { PartyMember.TYPE_3, PartyMember.TYPE_4 })
                .setParameterList("partyMemberStatus", new Object[] { PartyMember.STATUS_0 }).setParameterList("ccpartyIds", ccpartyIds);
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        if (query.uniqueResult() == null) {
            return 0;
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }

    /**
     * <B>方法名称：</B>加载某个阶段的发展党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月30日
     * @param ccpartyId
     * @param type
     * @param developmentId
     * @param start
     * @param limit
     * @return
     */
    public List<PartyMember> getPartyMemberListByType(String ccpartyId, Integer type, String developmentId, Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMember.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        if (type != null && !type.equals("")) {
            daoPara.addCondition(Condition.EQUAL("type", type));
        }
        if (developmentId != null && !developmentId.equals("")) {
            daoPara.addCondition(Condition.EQUAL("developmentId", developmentId));
        }
        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取党组织下的党员总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月11日
     * @param ccpartyId
     * @param statisticsCcpartyIds
     * @return
     */
    public int getPartymemberNums(String ccpartyId, List<String> statisticsCcpartyIds) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(u.id) ");
        hql.append(" from User as u ");
        if (statisticsCcpartyIds != null) {
            hql.append(" where u.partyMember.ccpartyId in (:ids) ");
        } else {
            hql.append(" where u.partyMember.ccpartyId in(");
            hql.append(" select c.id from CCParty as c where (c.id=:ccpartyId or c.parentId=:ccpartyId) ) ");
        }
        hql.append(" and u.partyMember.type in(:partyMemberType) and u.partyMember.status in(:partyMemberStatus)  ");
        hql.append(" and u.userType=:userType and u.type in(:type) ");
        hql.append(" and u.status=:status ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setInteger("status", User.DB_TABLE_STATUS_0).setInteger("userType", User.USER_TYPE_0)
                .setParameterList("type", new String[] { User.TYPE_01, User.TYPE_02 }).setParameterList("partyMemberType", new Object[] { PartyMember.TYPE_3, PartyMember.TYPE_4 })
                .setParameterList("partyMemberStatus", new Object[] { PartyMember.STATUS_0 });
        if (statisticsCcpartyIds != null) {
            query.setParameterList("ids", statisticsCcpartyIds);
        } else {
            query.setString("ccpartyId", ccpartyId);
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }
    
    /**
     * <B>方法名称：</B>加载某个阶段的发展党员数量<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月30日
     * @param ccpartyId
     * @param type
     * @param developmentId
     * @return
     */
    public Integer getPartyMemberTotalByType(String ccpartyId, Integer type, String developmentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMember.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        if (type != null && !type.equals("")) {
            daoPara.addCondition(Condition.EQUAL("type", type));
        }
        if (developmentId != null && !developmentId.equals("")) {
            daoPara.addCondition(Condition.EQUAL("developmentId", developmentId));
        }
        Integer total = dao.getTotalCount(daoPara);
        return total;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取党员转出和退党党员列表<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年8月23日 	
     * @param offset
     * @param limit
     * @param search
     * @param ccpartyId
     * @return
     */
    public List<User> getExportPartyMembers(Integer offset,Integer limit,String search,String ccpartyId){
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("partyMember.ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.NOTEQUAL("status", User.STATUS_0));
        daoPara.addCondition(Condition.NOTEQUAL("partyMember.status", PartyMember.STATUS_0));
        if(!StringUtils.isEmpty(search)){
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("sequence"));
        daoPara.addOrder(Order.asc("nameFirstCharacter"));
        List list = dao.loadList(daoPara);
        return list;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取转出和退党党员记录数<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年8月23日 	
     * @param search
     * @param ccpartyId
     * @return
     */
    public Integer getExportPartyMembersTotal(String search,String ccpartyId){
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(User.class);
        daoPara.addCondition(Condition.EQUAL("partyMember.ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.NOTEQUAL("status", User.STATUS_0));
        daoPara.addCondition(Condition.NOTEQUAL("partyMember.status", PartyMember.STATUS_0));
        if(!StringUtils.isEmpty(search)){
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        return dao.getTotalCount(daoPara);
    }
    
}
