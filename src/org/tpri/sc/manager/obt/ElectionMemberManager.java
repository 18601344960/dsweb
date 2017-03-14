package org.tpri.sc.manager.obt;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.tpri.sc.entity.obt.ElectionMember;
import org.tpri.sc.util.DateUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举领导班子成员管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */

@Repository("ElectionMemberManager")
public class ElectionMemberManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_ELECTION_MEMBER, ElectionMember.class);
    }

    public ElectionMember getElectionMemberById(String id) {
        Object obj = this.loadOne(ObjectType.OBT_ELECTION_MEMBER, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        ElectionMember electionMember = (ElectionMember) obj;
        return electionMember;
    }

    /**
     * <B>方法名称：</B>获取某换届选举下的领导班子成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param electionId
     * @return
     */
    public List<ElectionMember> getElectionMemberByElectionId(String electionId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ElectionMember.class);
        daoPara.addCondition(Condition.EQUAL("electionId", electionId));
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>删除某换届选举下的所有领导班子成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param electionId
     * @return
     */
    public boolean deleteElectionMemberByElectionId(String electionId) {
        String hql = "delete from ElectionMember where electionId=:electionId";
        Object[] params = new Object[] { electionId };
        dao.delete(hql, params);
        return true;
    }

    /**
     * <B>方法名称：</B>根据ID删除领导班子成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param id
     * @return
     */
    public boolean deleteElectionMember(String id) {
        return super.delete(id, ObjectType.OBT_ELECTION_MEMBER);
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户的党内职务列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月30日
     * @param ccpartyId
     * @return
     */
    public List<ElectionMember> getElectionMembersByUser(String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ElectionMember.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT);
        daoPara.addCondition(Condition.GREATER_EQUAL("endTime", sdf.format(new Date())));
        daoPara.addOrder(Order.asc("sequence"));
        daoPara.addOrder(Order.desc("startTime"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取某换届选举下的某职务的成员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月3日
     * @param titleId
     * @param electionId
     * @return
     */
    public List<ElectionMember> getElectionMembersByTitle(String titleId, String electionId) {
        StringBuffer hql = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT);
        hql.append(" from ElectionMember as em where em.electionId=:electionId ");
        hql.append(" and em.id in( ");
        hql.append(" select distinct emt.memberId from ElectionMemberTitle as emt ");
        hql.append(" where emt.partyTitleId=:titleId ");
        hql.append(" ) ");
        hql.append(" and DATE_FORMAT(em.endTime,'%Y-%m-%d')>=:nowDate ");
        hql.append(" order by em.sequence asc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("electionId", electionId).setString("titleId", titleId).setString("nowDate", sdf.format(new Date()));
        return query.list();

    }
}
