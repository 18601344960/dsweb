package org.tpri.sc.manager.obt;

import java.util.List;

import net.sf.json.JSONObject;

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
import org.tpri.sc.entity.obt.Conference;
import org.tpri.sc.entity.obt.ConferenceParticipants;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织生活参会人员管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月24日
 */
@Repository("ConferenceParticipantsManager")
public class ConferenceParticipantsManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_CONFERENCE_PARTICIPANTS, ConferenceParticipants.class);
    }

    /**
     * <B>方法名称：</B>根据ID获取参会人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param id
     * @return
     */
    public ConferenceParticipants getConferenceParticipants(String id) {
        return (ConferenceParticipants) this.loadOne(ObjectType.OBT_CONFERENCE_PARTICIPANTS, new String[] { "id" }, new Object[] { id });
    }

    /**
     * <B>方法名称：</B>获取组织生活的参与人员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param conferenceId
     * @param status
     * @return
     */
    public List<ConferenceParticipants> getParticipantsByConferenceId(String conferenceId, Integer status) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceParticipants.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        if (status != null) {
            daoPara.addCondition(Condition.EQUAL("status", status));
        }
        daoPara.addOrder(Order.asc("orderNo"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取某组织生活的参与人员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param offset
     * @param limit
     * @param conferenceId
     * @return
     */
    public List<ConferenceParticipants> getConferenceParticipants(Integer offset, Integer limit, String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceParticipants.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("orderNo"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取某组织生活的参与人员总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param conferenceId
     * @return
     */
    public Integer getConferenceParticipantsTotal(String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceParticipants.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        return dao.getTotalCount(daoPara);
    }

    /**
     * 
     * <B>方法名称：</B>根据支部工作和用户获取唯一记录<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月7日
     * @param conferenceId
     * @param userId
     * @return
     */
    public ConferenceParticipants getConferenceParticipantsByConferenceAndUser(String conferenceId, String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceParticipants.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        return (ConferenceParticipants) dao.loadOne(daoPara);
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户参加的支部工作条数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月22日
     * @param userId
     * @return
     */
    public Integer getBranchConferenceTotalByUser(String userId, JSONObject objs) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(DISTINCT (OC.ID)) \n");
        sql.append(" FROM OBT_CONFERENCE_PARTICIPANTS AS OCP \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE AS OC ON OCP.CONFERENCE_ID=OC.ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON OC.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON OC.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON OC.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE OC.STATUS=:status \n");
        sql.append(" AND OCP.USER_ID=:userId \n");
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            sql.append(" AND OCS.CATEGORY_ID=:step \n");
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            sql.append(" AND OCF.CATEGORY_ID=:format \n");
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            sql.append(" AND OCL.CATEGORY_ID=:label \n");
        }
        if (!StringUtils.isEmpty(objs.getString("beginTime"))) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') >=:beginTime \n");
        }
        if (!StringUtils.isEmpty(objs.getString("endTime"))) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') <=:endTime \n");
        }
        int brandType = objs.getInt("brandType");
        if (brandType != -1) {
            sql.append(" AND OC.IS_BRAND =:brandType \n");
        }
        int isRecommend = objs.getInt("isRecommend");
        if (isRecommend != -1) {
            sql.append(" AND OC.IS_RECOMMEND=:isRecommend \n");
        }
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("userId", userId).setInteger("status", Conference.STATUS_1);
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            query.setString("step", objs.getString("step"));
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            query.setString("format", objs.getString("format"));
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            query.setString("label", objs.getString("label"));
        }
        if (!StringUtils.isEmpty(objs.getString("beginTime"))) {
            query.setString("beginTime", objs.getString("beginTime"));
        }
        if (!StringUtils.isEmpty(objs.getString("endTime"))) {
            query.setString("endTime", objs.getString("endTime"));
        }
        if (objs.getInt("brandType") != -1) {
            query.setInteger("brandType", objs.getInt("brandType"));
        }
        if (isRecommend != -1) {
            query.setInteger("isRecommend", objs.getInt("isRecommend"));
        }
        if (query.uniqueResult() == null) {
            return 0;
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户参加支部工作的列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月22日
     * @param conferenceId
     * @return
     */
    public List<Object> getBranchConferenceListByUser(Integer offset, Integer limit, String userId, JSONObject objs) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT (OC.ID) \n");
        sql.append(" FROM OBT_CONFERENCE_PARTICIPANTS AS OCP \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE AS OC ON OCP.CONFERENCE_ID=OC.ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON OC.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON OC.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON OC.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE OC.STATUS=:status \n");
        sql.append(" AND OCP.USER_ID=:userId \n");
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            sql.append(" AND OCS.CATEGORY_ID=:step \n");
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            sql.append(" AND OCF.CATEGORY_ID=:format \n");
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            sql.append(" AND OCL.CATEGORY_ID=:label \n");
        }
        if (!StringUtils.isEmpty(objs.getString("beginTime"))) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') >=:beginTime \n");
        }
        if (!StringUtils.isEmpty(objs.getString("endTime"))) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') <=:endTime \n");
        }
        int brandType = objs.getInt("brandType");
        if (brandType != -1) {
            sql.append(" AND OC.IS_BRAND =:brandType \n");
        }
        int isRecommend = objs.getInt("isRecommend");
        if (isRecommend != -1) {
            sql.append(" AND OC.IS_RECOMMEND=:isRecommend \n");
        }
        sql.append(" ORDER BY OC.OCCUR_TIME DESC,OC.CREATE_TIME DESC \n");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("userId", userId).setInteger("status", Conference.STATUS_1);
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            query.setString("step", objs.getString("step"));
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            query.setString("format", objs.getString("format"));
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            query.setString("label", objs.getString("label"));
        }
        if (!StringUtils.isEmpty(objs.getString("beginTime"))) {
            query.setString("beginTime", objs.getString("beginTime"));
        }
        if (!StringUtils.isEmpty(objs.getString("endTime"))) {
            query.setString("endTime", objs.getString("endTime"));
        }
        if (objs.getInt("brandType") != -1) {
            query.setInteger("brandType", objs.getInt("brandType"));
        }
        if (isRecommend != -1) {
            query.setInteger("isRecommend", objs.getInt("isRecommend"));
        }
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        return query.list();
    }

    /**
     * <B>方法名称：</B>获取某个组织生活的某个参与人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年10月9日
     * @param conferenceId
     * @param userId
     * @return
     */
    public ConferenceParticipants getParticipantsByConferenceIdAndUserId(String conferenceId, String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceParticipants.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addOrder(Order.asc("orderNo"));
        ConferenceParticipants conferenceParticipants = (ConferenceParticipants) dao.loadOne(daoPara);
        return conferenceParticipants;
    }
}
