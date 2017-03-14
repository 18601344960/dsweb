package org.tpri.sc.manager.pub;

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
import org.tpri.sc.entity.pub.AssessmentUser;
import org.tpri.sc.entity.uam.User;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：答题答卷</B><BR>
 * <B>中文类名：答卷测评用户管理类</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月17日
 */
@Repository("AssessmentUserManager")
public class AssessmentUserManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.PUB_ASSESSMENT_USER, AssessmentUser.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取某答卷的测评用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param offset
     * @param limit
     * @param assessmentId
     * @return
     */
    public List<AssessmentUser> getAssessmentUsersByAssessment(Integer offset, Integer limit, String assessmentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentUser.class);
        daoPara.addCondition(Condition.EQUAL("assessmentId", assessmentId));
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("submitTime"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户的答卷测评用户记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param assessmentId
     * @return
     */
    public Integer getAssessmentUsersTotalByAssessment(String assessmentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentUser.class);
        daoPara.addCondition(Condition.EQUAL("assessmentId", assessmentId));
        return dao.getTotalCount(daoPara);
    }

    /**
     * 
     * <B>方法名称：根据ID获取通知对象</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月17日
     * @param id
     * @return
     */
    public AssessmentUser getAssessmentUserById(String id) {
        return (AssessmentUser) this.loadOne(ObjectType.PUB_ASSESSMENT_USER, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 
     * <B>方法名称：</B>根据答卷和用户获取测评人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param assessmentId
     * @param userId
     * @return
     */
    public AssessmentUser getAssessmentUserByAssessmentAndUser(String assessmentId, String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentUser.class);
        daoPara.addCondition(Condition.EQUAL("assessmentId", assessmentId));
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        return (AssessmentUser) dao.loadOne(daoPara);
    }
    
    /**
     * 
     * <B>方法名称：</B>获取某用户参与的答卷列表<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年8月11日 	
     * @param userId
     * @return
     */
    public List<AssessmentUser> getAssessmentUserByUser(String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentUser.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取组织下完成列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年9月24日
     * @param assessmentId
     * @param ccpartyIds
     * @return
     */
    public List<AssessmentUser> getAssessmentUsersByAssessmentAndCcpartys(String assessmentId, List<Object> ccpartyIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentUser.class);
        daoPara.addCondition(Condition.EQUAL("assessmentId", assessmentId));
        daoPara.addCondition(Condition.IN("ccpartyId", ccpartyIds));
        List list = dao.loadList(daoPara);
        return list;
    }
    
    /**
     * 
     * <B>方法名称：</B>查询人员完成情况<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月10日
     * @param assessmentId
     * @param ccpartyIds
     * @return
     */
    public List<Object> getAllAssessmentUsersByAssessment(Integer offset, Integer limit, String search, Integer status, String assessmentId, List<Object> ccpartyIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select uu.id userid,uu.name username,oc.id ccpartyid,oc.name ccpartyname,oc.full_name ccpartyfullname,pau.id assessmentuserid,pau.submit_time ");
        sql.append(" from uam_user as uu left join obt_party_member as opm on (uu.id=opm.id and opm.type in(:partyMemberType)) ");
        sql.append(" left join org_ccparty as oc on opm.ccparty_id=oc.id ");
        sql.append(" left join pub_assessment_user as pau on (uu.id=pau.user_id and pau.assessment_id=:assessmentId) ");
        sql.append(" where uu.user_type=0 and opm.ccparty_id in(:ccpartyIds) and uu.type in(:type) ");
        sql.append(" and opm.status in(:partyMemberStatus) ");
        if (!StringUtils.isEmpty(search)) {
            sql.append(" and uu.name like :search ");
        }
        if (status != null) {
            if (0 == status) {
                sql.append(" and pau.id is null ");
            } else if (1 == status) {
                sql.append(" and pau.id is not null ");
            }
        }
        sql.append(" order by opm.ccparty_id asc,uu.name_first_character asc ");
        Session session = dao.getCurrentSession();
        List<Object> partyMemberTypes = new ArrayList<Object>();
        partyMemberTypes.add(PartyMember.TYPE_3);
        partyMemberTypes.add(PartyMember.TYPE_4);
        Query query = session.createSQLQuery(sql.toString()).setParameterList("type", new String[] { User.TYPE_01, User.TYPE_02 }).setString("assessmentId", assessmentId)
                .setParameterList("ccpartyIds", ccpartyIds).setParameterList("partyMemberType", partyMemberTypes)
                .setParameterList("partyMemberStatus", new Object[] { PartyMember.STATUS_0 })
                .setFirstResult(offset).setMaxResults(limit);
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>查询人员完成情况记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月10日
     * @param assessmentId
     * @param ccpartyIds
     * @return
     */
    public Integer getAllAssessmentUserTotalByAssessment(String search, Integer status, String assessmentId, List<Object> ccpartyIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(uu.id) ");
        sql.append(" from uam_user as uu left join obt_party_member as opm on (uu.id=opm.id and opm.type in(:partyMemberType)) ");
        sql.append(" left join org_ccparty as oc on opm.ccparty_id=oc.id ");
        sql.append(" left join pub_assessment_user as pau on (uu.id=pau.user_id and pau.assessment_id=:assessmentId) ");
        sql.append(" where uu.user_type=0 and opm.ccparty_id in(:ccpartyIds) and uu.type in(:type) ");
        sql.append(" and opm.status in(:partyMemberStatus) ");
        if (!StringUtils.isEmpty(search)) {
            sql.append(" and uu.name like :search ");
        }
        if (status != null) {
            if (0 == status) {
                sql.append(" and pau.id is null ");
            } else if (1 == status) {
                sql.append(" and pau.id is not null ");
            }
        }
        Session session = dao.getCurrentSession();
        List<Object> partyMemberTypes = new ArrayList<Object>();
        partyMemberTypes.add(PartyMember.TYPE_3);
        partyMemberTypes.add(PartyMember.TYPE_4);
        Query query = session.createSQLQuery(sql.toString()).setParameterList("type", new String[] { User.TYPE_01, User.TYPE_02 }).setString("assessmentId", assessmentId)
                .setParameterList("ccpartyIds", ccpartyIds).setParameterList("partyMemberType", partyMemberTypes).setParameterList("partyMemberStatus", new Object[] { PartyMember.STATUS_0 });
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        if (query.uniqueResult() == null) {
            return 0;
        }
        return (new Integer(query.uniqueResult().toString())).intValue();

    }
}
