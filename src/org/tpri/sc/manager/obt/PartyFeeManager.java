package org.tpri.sc.manager.obt;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.PartyFee;
import org.tpri.sc.entity.obt.PartyFeeReport;
import org.tpri.sc.entity.obt.PartyFeeSpecial;
import org.tpri.sc.entity.obt.PartyFeeUse;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.uam.User;

/**
 * @description 党费管理类
 * @author 王钱俊
 * @since 2015-10-05
 */

@Repository("PartyFeeManager")
public class PartyFeeManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.OBT_PARTY_FEE, PartyFee.class);
        ObjectRegister.registerClass(ObjectType.OBT_PARTY_FEE_SPECIAL, PartyFeeSpecial.class);
    }

    /**
     * <B>方法名称：</B>根据ccpartyId获取党费收缴列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月23日
     * @param start
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<PartyMember> getPartyMemberByCcpartyIdAndOrderBySequence(Integer start, Integer limit, String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select pm from User as u left join u.partyMember as pm  ");
        hql.append(" where u.status=0 and pm.ccpartyId=:ccpartyId ");
        hql.append(" and u.userType=:userType ");
        hql.append(" and u.type in (:type) ");
        hql.append(" and pm.type in(3,4) ");
        hql.append(" order by u.name desc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("ccpartyId", ccpartyId).setInteger("userType", User.USER_TYPE_0)
                .setParameterList("type", new String[] { User.TYPE_01, User.TYPE_02 });
        Boolean isForPage = (limit != null);
        if (isForPage) {
            query.setFirstResult(start);
            query.setMaxResults(limit);
        }
        return query.list();
    }

    /**
     * <B>方法名称：</B>根据ccpartyId统计党费收缴列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月23日
     * @param ccpartyId
     * @return
     */
    public int countPartyMemberByCcpartyId(String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(u.id) from User as u left join u.partyMember as pm  ");
        hql.append(" where u.status=0 and pm.ccpartyId=:ccpartyId ");
        hql.append(" and u.userType=:userType ");
        hql.append(" and u.type in (:type) ");
        hql.append(" and pm.type in(3,4) ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("ccpartyId", ccpartyId).setInteger("userType", User.USER_TYPE_0)
                .setParameterList("type", new String[] { User.TYPE_01, User.TYPE_02 });
        return ((Number) query.uniqueResult()).intValue();
    }

    //通过id来获取特殊党费
    public PartyFeeSpecial getPartyFeeSpecialById(String id) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyFeeSpecial.class);
        daoPara.addCondition(Condition.EQUAL("id", id));
        Object obj = dao.loadOne(daoPara);
        if (obj == null) {
            return null;
        }
        PartyFeeSpecial partyFeeSpecial = (PartyFeeSpecial) obj;
        return partyFeeSpecial;
    }

    //通过年份获取特殊党费列表
    public List<PartyFeeSpecial> getPartyFeeSpecialByYear(int year, String ccpartyId, Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyFeeSpecial.class);
        daoPara.addCondition(Condition.EQUAL("year", year));
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("createTime"));
        List list = dao.loadList(daoPara);
        if (list == null) {
            return null;
        }
        return list;
    }

    /**
     * <B>方法名称：</B>获取特殊党费记录<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月23日
     * @param year
     * @param ccpartyId
     * @param userId
     * @param start
     * @param limit
     * @return
     */
    public List<PartyFeeSpecial> getPartyFeeSpecialDetailByYear(int year, String ccpartyId, String userId, Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyFeeSpecial.class);
        daoPara.addCondition(Condition.EQUAL("year", year));
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("createTime"));
        List list = dao.loadList(daoPara);
        if (list == null) {
            return null;
        }
        return list;
    }

    //获取特殊党费的总数
    public int getTotalPartyFeeSpecialDetailByYear(int year, String ccpartyId, String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyFeeSpecial.class);
        daoPara.addCondition(Condition.EQUAL("year", year));
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addOrder(Order.asc("id"));
        int total = dao.getTotalCount(daoPara);
        return total;
    }

    //添加特殊党费
    public boolean addPartyFeeSpecial(PartyFeeSpecial partyFeeSpecial) {
        return super.add(partyFeeSpecial);
    }

    //删除特殊党费记录
    public boolean deletePartySpecial(String id) {
        return super.delete(id, ObjectType.OBT_PARTY_FEE_SPECIAL);
    }

    public boolean updatePartySpecial(PartyFeeSpecial partyFeeSpecial) {
        return super.update(partyFeeSpecial);
    }

    //通过userId和年份查询缴费记录
    public PartyFee getPartyFeesByUserIdAndYear(String userId, Integer year, Integer month) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyFee.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addCondition(Condition.EQUAL("year", year));
        daoPara.addCondition(Condition.EQUAL("month", month));
        PartyFee partyFee = (PartyFee) dao.loadOne(daoPara);
        return partyFee;
    }

    //通过UserId获取缴费集合
    public List<PartyFee> getPartyFeesByUser(String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyFee.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addOrder(Order.desc("year"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取党费统计列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param userId
     * @param year
     * @return
     */
    public List<PartyFee> countPartyFeeByYear(String userId, Integer year) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyFee.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addCondition(Condition.EQUAL("year", year));
        List partyFeeList = dao.loadList(daoPara);
        return partyFeeList;
    }

    /** 获取党组织下每个用户一年总共特殊党费 */
    public List<PartyFeeSpecial> getEveryUserPartyFeeSpecialByYear(int year, String ccpartyId, String userId) {
        Query query = null;
        StringBuffer hql = new StringBuffer("SELECT ccpartyId,userId,year,sum(amount) from " + PartyFeeSpecial.class.getName()
                + " WHERE year=? and ccpartyId=? and userId=? GROUP BY ccpartyId,userId,year ORDER BY year");
        Session session = dao.getCurrentSession();
        query = session.createQuery(hql.toString());
        query.setInteger(0, year);
        query.setString(1, ccpartyId);
        query.setString(2, userId);
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>大额党费按年月统计<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖\
     * @since 2016年7月5日
     * @param ccpartyId
     * @param year
     * @return
     */
    public List<Object> getPartyFeeSpecialForMonth(String ccpartyId, int year) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select opfs.ccparty_id,DATE_FORMAT(opfs.create_time,'%m'),sum(opfs.amount) \n");
        sql.append(" from obt_party_fee_special as opfs where opfs.ccparty_id=:ccpartyId \n");
        sql.append(" and DATE_FORMAT(opfs.create_time,'%Y')=:year \n");
        sql.append(" group by DATE_FORMAT(opfs.create_time,'%Y-%m') \n");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("ccpartyId", ccpartyId).setInteger("year", year);
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户某年月的特殊党费缴纳数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月6日
     * @param ccpartyId
     * @param year
     * @return
     */
    public List<Object> getPartyFeeSpecialByUserForMonth(String userId, int year) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select opfs.user_id,DATE_FORMAT(opfs.create_time,'%m'),sum(opfs.amount) \n");
        sql.append(" from obt_party_fee_special as opfs where opfs.user_id=:userId \n");
        sql.append(" and DATE_FORMAT(opfs.create_time,'%Y')=:year \n");
        sql.append(" group by DATE_FORMAT(opfs.create_time,'%Y-%m') \n");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("userId", userId).setInteger("year", year);
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织某年份党费缴纳数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月3日
     * @param ccpartyId
     * @param year
     * @return
     */
    public List<PartyFee> getPartyFeesByCcparty(String ccpartyId, Integer year) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from PartyFee as pf where pf.userId in( ");
        hql.append(" select id from PartyMember as pm where pm.ccpartyId=:ccpartyId) ");
        hql.append(" and pf.year=:year ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("ccpartyId", ccpartyId).setInteger("year", year);
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>获取某年份某党员的缴纳数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月3日
     * @param userId
     * @param year
     * @return
     */
    public List<PartyFee> getPartyFeesByUser(String userId, Integer year) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyFee.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addCondition(Condition.EQUAL("year", year));
        List list = dao.loadList(daoPara);
        return list;
    }
}
