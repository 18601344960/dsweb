package org.tpri.sc.manager.obt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.tpri.sc.view.com.QueryResultConference;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>文章管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */
@Repository("ConferenceManager")
public class ConferenceManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.OBT_CONFERENCE, Conference.class);
    }

    /**
     * 
     * <B>方法名称：</B>查询文章列表包括标签、工作步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月22日
     * @param offset
     * @param limit
     * @param objs
     * @return
     */
    public List<Object> getConferencesForLabelOrStep(Integer offset, Integer limit, JSONObject objs) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT(CA.ID) \n");
        sql.append(" FROM obt_conference AS CA \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON CA.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON CA.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON CA.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE CA.STATUS=:status \n");
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            sql.append(" AND OCS.CATEGORY_ID IN(:step) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            sql.append(" AND OCF.CATEGORY_ID IN(:format) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            sql.append(" AND OCL.CATEGORY_ID IN(:label) \n");
        }
        sql.append(" ##权限 \n");
        sql.append(" AND \n");
        sql.append(" ( \n");
        sql.append("         (CA.SECRET_LEVEL=2) ##公开 \n");
        sql.append("    OR (CA.SECRET_LEVEL IN(0,1) AND CA.CCPARTY_ID=:currentCcpartyId)   ##本组织 \n");
        sql.append("   OR (CA.SECRET_LEVEL=1 AND CA.CCPARTY_ID IN(SELECT OC.ID FROM ORG_CCPARTY AS OC WHERE OC.PARENT_ID=:currentCcpartyId AND OC.STATUS=0)) ##本级及上级可见 \n");
        sql.append(" ) \n");
        sql.append(" ##组织 \n");
        if (!StringUtils.isEmpty(objs.getString("ccpartyId"))) {
            sql.append(" AND CA.CCPARTY_ID=:ccpartyId \n");
        }
        if (!StringUtils.isEmpty(objs.getString("beginTime"))) {
            sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d') >=:beginTime \n");
        }
        if (!StringUtils.isEmpty(objs.getString("endTime"))) {
            sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d') <=:endTime \n");
        }
        if (!StringUtils.isEmpty(objs.getString("name"))) {
            sql.append(" AND CA.NAME LIKE :name \n");
        }
        int sourceType = objs.getInt("sourceType");
        if (sourceType != -1) {
            sql.append(" AND CA.SOURCE_TYPE =:sourceType \n");
        }
        int brandType = objs.getInt("brandType");
        if (brandType != -1) {
            sql.append(" AND CA.IS_BRAND =:brandType \n");
        }
        int isRecommend = objs.getInt("isRecommend");
        if (isRecommend != -1) {
            sql.append(" AND CA.IS_RECOMMEND=:isRecommend \n");
        }
        sql.append(" ORDER BY CA.IS_TOP DESC,CA.OCCUR_TIME DESC \n");

        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setInteger("status", Conference.STATUS_1).setString("currentCcpartyId", objs.getString("currentCcpartyId"));
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            String[] strs = objs.getString("step").split(",");
            query.setParameterList("step", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            String[] strs = objs.getString("format").split(",");
            query.setParameterList("format", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            String[] strs = objs.getString("label").split(",");
            query.setParameterList("label", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("beginTime"))) {
            query.setString("beginTime", objs.getString("beginTime"));
        }
        if (!StringUtils.isEmpty(objs.getString("endTime"))) {
            query.setString("endTime", objs.getString("endTime"));
        }
        if (!StringUtils.isEmpty(objs.getString("ccpartyId"))) {
            query.setString("ccpartyId", objs.getString("ccpartyId"));
        }
        if (!StringUtils.isEmpty(objs.getString("name"))) {
            query.setString("name", "%" + objs.getString("name") + "%");
        }
        if (objs.getInt("sourceType") != -1) {
            query.setInteger("sourceType", objs.getInt("sourceType"));
        }
        if (objs.getInt("brandType") != -1) {
            query.setInteger("brandType", objs.getInt("brandType"));
        }
        if (isRecommend != -1) {
            query.setInteger("isRecommend", objs.getInt("isRecommend"));
        }
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>查询文章列表记录数包括标签、工作步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月22日
     * @param objs
     * @return
     */
    public Integer getConferencesTotalForShare(JSONObject objs) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(DISTINCT(CA.ID)) \n");
        sql.append(" FROM obt_conference AS CA \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON CA.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON CA.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON CA.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE CA.STATUS=:status \n");
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            sql.append(" AND OCS.CATEGORY_ID IN(:step) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            sql.append(" AND OCF.CATEGORY_ID IN(:format) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            sql.append(" AND OCL.CATEGORY_ID IN(:label) \n");
        }
        sql.append(" ##权限 \n");
        sql.append(" AND \n");
        sql.append(" ( \n");
        sql.append("         (CA.SECRET_LEVEL=2) ##公开 \n");
        sql.append("    OR (CA.SECRET_LEVEL IN(0,1) AND CA.CCPARTY_ID=:currentCcpartyId)   ##本组织 \n");
        sql.append("   OR (CA.SECRET_LEVEL=1 AND CA.CCPARTY_ID IN(SELECT OC.ID FROM ORG_CCPARTY AS OC WHERE OC.PARENT_ID=:currentCcpartyId AND OC.STATUS=0)) ##本级及上级可见 \n");
        sql.append(" ) \n");
        sql.append(" ##组织 \n");
        if (!StringUtils.isEmpty(objs.getString("ccpartyId"))) {
            sql.append(" AND CA.CCPARTY_ID=:ccpartyId \n");
        }
        if (!StringUtils.isEmpty(objs.getString("beginTime"))) {
            sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d') >=:beginTime \n");
        }
        if (!StringUtils.isEmpty(objs.getString("endTime"))) {
            sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d') <=:endTime \n");
        }
        if (!StringUtils.isEmpty(objs.getString("name"))) {
            sql.append(" AND CA.NAME LIKE :name \n");
        }
        int sourceType = objs.getInt("sourceType");
        if (sourceType != -1) {
            sql.append(" AND CA.SOURCE_TYPE =:sourceType \n");
        }
        int brandType = objs.getInt("brandType");
        if (brandType != -1) {
            sql.append(" AND CA.IS_BRAND =:brandType \n");
        }
        int isRecommend = objs.getInt("isRecommend");
        if (isRecommend != -1) {
            sql.append(" AND CA.IS_RECOMMEND=:isRecommend \n");
        }

        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setInteger("status", Conference.STATUS_1).setString("currentCcpartyId", objs.getString("currentCcpartyId"));
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            String[] strs = objs.getString("step").split(",");
            query.setParameterList("step", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            String[] strs = objs.getString("format").split(",");
            query.setParameterList("format", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            String[] strs = objs.getString("label").split(",");
            query.setParameterList("label", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("beginTime"))) {
            query.setString("beginTime", objs.getString("beginTime"));
        }
        if (!StringUtils.isEmpty(objs.getString("endTime"))) {
            query.setString("endTime", objs.getString("endTime"));
        }
        if (!StringUtils.isEmpty(objs.getString("ccpartyId"))) {
            query.setString("ccpartyId", objs.getString("ccpartyId"));
        }
        if (!StringUtils.isEmpty(objs.getString("name"))) {
            query.setString("name", "%" + objs.getString("name") + "%");
        }
        if (objs.getInt("sourceType") != -1) {
            query.setInteger("sourceType", objs.getInt("sourceType"));
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
     * <B>方法名称：</B>获取本组织的工作品牌<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月30日
     * @param request
     * @return
     */
    public List<Object> getWorkBrandsOfCcparty(Integer offset, Integer limit, Map<String, Object> param) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT(CA.ID) \n");
        sql.append(" FROM obt_conference AS CA \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON CA.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON CA.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON CA.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE CA.STATUS=:status \n");
        if (!StringUtils.isEmpty((String) param.get("step"))) {
            sql.append(" AND OCS.CATEGORY_ID IN(:step) \n");
        }
        if (!StringUtils.isEmpty((String) param.get("format"))) {
            sql.append(" AND OCF.CATEGORY_ID IN(:format) \n");
        }
        if (!StringUtils.isEmpty((String) param.get("label"))) {
            sql.append(" AND OCL.CATEGORY_ID IN(:label) \n");
        }
        sql.append(" ##权限 \n");
        sql.append(" AND \n");
        sql.append(" ( \n");
        sql.append("         (CA.SECRET_LEVEL=2) ##公开 \n");
        sql.append("    OR (CA.SECRET_LEVEL IN(0,1) AND CA.CCPARTY_ID=:ccpartyId)   ##本组织 \n");
        sql.append("   OR (CA.SECRET_LEVEL=1 AND CA.CCPARTY_ID IN(SELECT OC.ID FROM ORG_CCPARTY AS OC WHERE OC.PARENT_ID=:ccpartyId AND OC.STATUS=0)) ##本级及上级可见 \n");
        sql.append(" ) \n");
        sql.append(" ##组织 \n");
        if (!StringUtils.isEmpty((String) param.get("ccpartyId"))) {
            sql.append(" AND CA.CCPARTY_ID=:ccpartyId \n");
        }
        if (!StringUtils.isEmpty((String) param.get("beginTime"))) {
            sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d') >=:beginTime \n");
        }
        if (!StringUtils.isEmpty((String) param.get("endTime"))) {
            sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d') <=:endTime \n");
        }
        if (!StringUtils.isEmpty((String) param.get("name"))) {
            sql.append(" AND CA.NAME LIKE :name \n");
        }
        sql.append(" AND CA.IS_BRAND =:isBrand \n");
        sql.append(" ORDER BY CA.IS_TOP DESC ,CA.OCCUR_TIME DESC \n");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setInteger("status", Conference.STATUS_1).setString("ccpartyId", (String) param.get("ccpartyId"))
                .setInteger("isBrand", Conference.IS_BRAND_1);
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        if (!StringUtils.isEmpty((String) param.get("step"))) {
            String[] strs = ((String) param.get("step")).split(",");
            query.setParameterList("step", strs);
        }
        if (!StringUtils.isEmpty((String) param.get("format"))) {
            String[] strs = ((String) param.get("format")).split(",");
            query.setParameterList("format", strs);
        }
        if (!StringUtils.isEmpty((String) param.get("label"))) {
            String[] strs = ((String) param.get("label")).split(",");
            query.setParameterList("label", strs);
        }
        if (!StringUtils.isEmpty((String) param.get("beginTime"))) {
            query.setString("beginTime", (String) param.get("beginTime"));
        }
        if (!StringUtils.isEmpty((String) param.get("endTime"))) {
            query.setString("endTime", (String) param.get("endTime"));
        }
        if (!StringUtils.isEmpty((String) param.get("name"))) {
            query.setString("name", "%" + (String) param.get("name") + "%");
        }
        return query.list();
    }

    /**
     * <B>方法名称：</B>获取本组织的工作品牌总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月30日
     * @param request
     * @return
     */
    public Integer getWorkBrandsOfCcpartyTotal(Map<String, Object> param) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(DISTINCT(CA.ID)) \n");
        sql.append(" FROM obt_conference AS CA \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON CA.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON CA.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON CA.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE CA.STATUS=:status \n");
        if (!StringUtils.isEmpty((String) param.get("step"))) {
            sql.append(" AND OCS.CATEGORY_ID IN(:step) \n");
        }
        if (!StringUtils.isEmpty((String) param.get("format"))) {
            sql.append(" AND OCF.CATEGORY_ID IN(:format) \n");
        }
        if (!StringUtils.isEmpty((String) param.get("label"))) {
            sql.append(" AND OCL.CATEGORY_ID IN(:label) \n");
        }
        sql.append(" ##权限 \n");
        sql.append(" AND \n");
        sql.append(" ( \n");
        sql.append("         (CA.SECRET_LEVEL=2) ##公开 \n");
        sql.append("    OR (CA.SECRET_LEVEL IN(0,1) AND CA.CCPARTY_ID=:ccpartyId)   ##本组织 \n");
        sql.append("   OR (CA.SECRET_LEVEL=1 AND CA.CCPARTY_ID IN(SELECT OC.ID FROM ORG_CCPARTY AS OC WHERE OC.PARENT_ID=:ccpartyId AND OC.STATUS=0)) ##本级及上级可见 \n");
        sql.append(" ) \n");
        sql.append(" ##组织 \n");
        if (!StringUtils.isEmpty((String) param.get("ccpartyId"))) {
            sql.append(" AND CA.CCPARTY_ID=:ccpartyId \n");
        }
        if (!StringUtils.isEmpty((String) param.get("beginTime"))) {
            sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d') >=:beginTime \n");
        }
        if (!StringUtils.isEmpty((String) param.get("endTime"))) {
            sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d') <=:endTime \n");
        }
        if (!StringUtils.isEmpty((String) param.get("name"))) {
            sql.append(" AND CA.NAME LIKE :name \n");
        }
        sql.append(" AND CA.IS_BRAND =:isBrand \n");
        sql.append(" ORDER BY CA.OCCUR_TIME DESC \n");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setInteger("status", Conference.STATUS_1).setString("ccpartyId", (String) param.get("ccpartyId"))
                .setInteger("isBrand", Conference.IS_BRAND_1);
        if (!StringUtils.isEmpty((String) param.get("step"))) {
            String[] strs = ((String) param.get("step")).split(",");
            query.setParameterList("step", strs);
        }
        if (!StringUtils.isEmpty((String) param.get("format"))) {
            String[] strs = ((String) param.get("format")).split(",");
            query.setParameterList("format", strs);
        }
        if (!StringUtils.isEmpty((String) param.get("label"))) {
            String[] strs = ((String) param.get("label")).split(",");
            query.setParameterList("label", strs);
        }
        if (!StringUtils.isEmpty((String) param.get("beginTime"))) {
            query.setString("beginTime", (String) param.get("beginTime"));
        }
        if (!StringUtils.isEmpty((String) param.get("endTime"))) {
            query.setString("endTime", (String) param.get("endTime"));
        }
        if (!StringUtils.isEmpty((String) param.get("name"))) {
            query.setString("name", "%" + (String) param.get("name") + "%");
        }
        if (query.uniqueResult() == null) {
            return 0;
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织某些栏目文章列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param offset
     * @param limit
     * @param paramterMap
     * @return
     */
    public List<Conference> getConferenceList(Integer offset, Integer limit, Map<String, Object> paramterMap) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from Conference as a where a.ccpartyId in(:ccpartyIds) ");
        String categoryId = (String) paramterMap.get("categoryId");
        if (!StringUtils.isEmpty(categoryId)) {
            hql.append(" and a.id in( select distinct ac.articleId from ConferenceCategory as ac where ac.category.id=:categoryId )");
        }
        String searchBeginTime = (String) paramterMap.get("searchBeginTime");
        String searchEndTime = (String) paramterMap.get("searchEndTime");
        String searchKey = (String) paramterMap.get("searchKey");
        if (!StringUtils.isEmpty(searchBeginTime) && !StringUtils.isEmpty(searchEndTime)) {
            hql.append(" and a.createTime between :beginTime and :endTime ");
        }
        if (!StringUtils.isEmpty(searchKey)) {
            hql.append(" and a.name like :name ");
        }
        hql.append(" order by a.isRecommend desc, a.createTime desc ");

        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setParameterList("ccpartyIds", (List<Object>) paramterMap.get("ccpartyIds"));
        if (!StringUtils.isEmpty(categoryId)) {
            query.setString("categoryId", categoryId);
        }
        if (!StringUtils.isEmpty(searchBeginTime) && !StringUtils.isEmpty(searchEndTime)) {
            query.setString("beginTime", searchBeginTime).setString("endTime", searchEndTime);
        }
        if (!StringUtils.isEmpty(searchKey)) {
            query.setString("name", "%" + searchKey + "%");
        }
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        List list = query.list();
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织某些栏目文章总记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param paramterMap
     * @return
     */
    public Integer getConferenceTotal(Map<String, Object> paramterMap) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(a.id) from Conference as a where a.ccpartyId in(:ccpartyIds) ");
        String categoryId = (String) paramterMap.get("categoryId");
        if (!StringUtils.isEmpty(categoryId)) {
            hql.append(" and a.id in( select distinct ac.article.id from ConferenceCategory as ac where ac.category.id=:categoryId )");
        }
        String searchBeginTime = (String) paramterMap.get("searchBeginTime");
        String searchEndTime = (String) paramterMap.get("searchEndTime");
        String searchKey = (String) paramterMap.get("searchKey");
        if (!StringUtils.isEmpty(searchBeginTime) && !StringUtils.isEmpty(searchEndTime)) {
            hql.append(" and a.createTime between :beginTime and :endTime ");
        }
        if (!StringUtils.isEmpty(searchKey)) {
            hql.append(" and a.name like :name ");
        }

        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setParameterList("ccpartyIds", (List<Object>) paramterMap.get("ccpartyIds"));
        if (!StringUtils.isEmpty(categoryId)) {
            query.setString("categoryId", categoryId);
        }
        if (!StringUtils.isEmpty(searchBeginTime) && !StringUtils.isEmpty(searchEndTime)) {
            query.setString("beginTime", searchBeginTime).setString("endTime", searchEndTime);
        }
        if (!StringUtils.isEmpty(searchKey)) {
            query.setString("name", "%" + searchKey + "%");
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }

    /**
     * 
     * <B>方法名称：</B>获取我的文章列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param offset
     * @param limit
     * @param userId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public List<Conference> getMyConferenceList(Integer offset, Integer limit, String userId, String beginTime, String endTime, String searchKey) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Conference.class);
        daoPara.addCondition(Condition.EQUAL("createUserId", userId));
        /*
         * if (!StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime))
         * { List<Object> values = new ArrayList<Object>();
         * values.add(beginTime); values.add(endTime);
         * daoPara.addCondition(Condition.BETWEEN("occurTime", values)); }
         */
        if (!StringUtils.isEmpty(beginTime)) {
            daoPara.addCondition(Condition.GREATER_EQUAL("occurTime", beginTime));
        }
        if (!StringUtils.isEmpty(endTime)) {
            daoPara.addCondition(Condition.LESS_EQUAL("occurTime", endTime));
        }
        if (!StringUtils.isEmpty(searchKey)) {
            daoPara.addCondition(Condition.LIKE("name", searchKey));
        }
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("isTop"));
        daoPara.addOrder(Order.desc("createTime"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取我的文章总记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param userId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public Integer getMyConferencesTotal(String userId, String beginTime, String endTime, String searchKey) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Conference.class);
        daoPara.addCondition(Condition.EQUAL("createUserId", userId));
        /*
         * if (!StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime))
         * { List<Object> values = new ArrayList<Object>();
         * values.add(beginTime); values.add(endTime);
         * daoPara.addCondition(Condition.BETWEEN("occurTime", values)); }
         */
        if (!StringUtils.isEmpty(beginTime)) {
            daoPara.addCondition(Condition.GREATER_EQUAL("occurTime", beginTime));
        }
        if (!StringUtils.isEmpty(endTime)) {
            daoPara.addCondition(Condition.LESS_EQUAL("occurTime", endTime));
        }
        if (!StringUtils.isEmpty(searchKey)) {
            daoPara.addCondition(Condition.LIKE("name", searchKey));
        }
        return dao.getTotalCount(daoPara);
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的文章列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param offset
     * @param limit
     * @param ccpartyId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public List<Object> getCcpartyConferenceList(Integer offset, Integer limit, String ccpartyId, JSONObject objs) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT(OC.ID) \n");
        sql.append(" FROM obt_conference AS OC \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON OC.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON OC.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON OC.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE OC.CCPARTY_ID=:ccpartyId \n");
        if (!StringUtils.isEmpty(objs.getString("name"))) {
            sql.append(" AND OC.NAME LIKE :name \n");
        }
        int sourceType = objs.getInt("sourceType");
        if (sourceType != -1) {
            sql.append(" AND OC.SOURCE_TYPE =:sourceType \n");
        }
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            sql.append(" AND OCS.CATEGORY_ID IN(:step) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            sql.append(" AND OCF.CATEGORY_ID IN(:format) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            sql.append(" AND OCL.CATEGORY_ID IN(:label) \n");
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
        sql.append(" ORDER BY OC.IS_TOP DESC,OC.OCCUR_TIME DESC,OC.CREATE_TIME DESC \n");

        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("ccpartyId", ccpartyId);
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        if (!StringUtils.isEmpty(objs.getString("name"))) {
            query.setString("name", "%" + objs.getString("name") + "%");
        }
        if (sourceType != -1) {
            query.setInteger("sourceType", objs.getInt("sourceType"));
        }
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            String[] strs = objs.getString("step").split(",");
            query.setParameterList("step", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            String[] strs = objs.getString("format").split(",");
            query.setParameterList("format", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            String[] strs = objs.getString("label").split(",");
            query.setParameterList("label", strs);
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
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的文章总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param ccpartyId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public Integer getCcpartyConferenceTotal(String ccpartyId, JSONObject objs) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(DISTINCT(OC.ID)) \n");
        sql.append(" FROM obt_conference AS OC \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON OC.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON OC.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON OC.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE OC.CCPARTY_ID=:ccpartyId \n");
        if (!StringUtils.isEmpty(objs.getString("name"))) {
            sql.append(" AND OC.NAME LIKE :name \n");
        }
        int sourceType = objs.getInt("sourceType");
        if (sourceType != -1) {
            sql.append(" AND OC.SOURCE_TYPE =:sourceType \n");
        }
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            sql.append(" AND OCS.CATEGORY_ID IN(:step) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            sql.append(" AND OCF.CATEGORY_ID IN(:format) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            sql.append(" AND OCL.CATEGORY_ID IN(:label) \n");
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
        Query query = session.createSQLQuery(sql.toString()).setString("ccpartyId", ccpartyId);
        if (!StringUtils.isEmpty(objs.getString("name"))) {
            query.setString("name", "%" + objs.getString("name") + "%");
        }
        if (sourceType != -1) {
            query.setInteger("sourceType", objs.getInt("sourceType"));
        }
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            String[] strs = objs.getString("step").split(",");
            query.setParameterList("step", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            String[] strs = objs.getString("format").split(",");
            query.setParameterList("format", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            String[] strs = objs.getString("label").split(",");
            query.setParameterList("label", strs);
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
     * <B>方法名称：</B>根据ID获取文章<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param id
     * @return
     */
    public Conference getConferenceById(String id) {
        Conference article = (Conference) super.load(id, ObjectType.OBT_CONFERENCE);
        return article;
    }

    /**
     * 
     * <B>方法名称：</B>更新文章<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param id
     * @param fieldValues
     * @return
     */
    public boolean editConference(String id, Map<String, Object> fieldValues) {
        return super.update(id, ObjectType.OBT_CONFERENCE, fieldValues);
    }

    /**
     * 
     * <B>方法名称：</B>根据ID删除文章<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param id
     * @return
     */
    public boolean deleteConference(String id) {
        return super.delete(id, ObjectType.OBT_CONFERENCE);
    }

    /**
     * 
     * <B>方法名称：</B>文章统计分析<BR>
     * <B>概要说明：</B>使用原始SQL语句查询，适合MySql数据库，如需切换其他数据库需要重写此方法。<BR>
     * 
     * @author 赵子靖
     * @since 2015年9月23日
     * @param queryYear 查询年份
     * @param queryQuarter 查询时间类型
     * @param ccpartyId 查询组织ID
     * @param ccpartyName 查询组织名称
     * @return 集合
     */
    public List<QueryResultConference> getConferenceStepsStatistics(String ccpartyId, String beginDate, String endDate) {
        List<QueryResultConference> articleCounts = new ArrayList<QueryResultConference>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select occ.id,occ.name,count(conference.id),IFNULL(sum(conference.hits),0) hits,IFNULL(sum(conference.reply),0) reply,IFNULL(sum(conference.file),0) file \n");
        sql.append(" from obt_conference_category as occ left join  \n");
        sql.append(" ( \n");
        sql.append(" SELECT DISTINCT OCC.ID AS CATEGORY_ID,OCC.NAME AS CATEGORY_NAME,OC.ID,IFNULL(OC.HITS,0) AS HITS, \n");
        sql.append(" IFNULL((SELECT COUNT(CONFERENCE_ID) FROM OBT_CONFERENCE_COMMENT WHERE CONFERENCE_ID=OC.ID GROUP BY CONFERENCE_ID),0) AS REPLY, \n");
        sql.append(" IFNULL((SELECT COUNT(OBJECT_ID) FROM COM_FILE  WHERE OBJECT_ID=OC.ID GROUP BY OBJECT_ID),0) AS FILE \n");
        sql.append(" FROM OBT_CONFERENCE_CATEGORY AS OCC  \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON OCC.ID=OCS.CATEGORY_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE AS OC ON OCS.CONFERENCE_ID=OC.ID \n");
        sql.append(" WHERE OCC.PARENT_ID='B' AND OCC.TYPE=0  \n");
        sql.append(" AND OC.CCPARTY_ID=:ccpartyId \n");
        if (!StringUtils.isEmpty(beginDate)) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') >=:beginDate \n");
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') <=:endDate \n");
        }
        sql.append(" ORDER BY OCC.ORDER_NO \n");
        sql.append(" )AS CONFERENCE on occ.id=conference.category_id\n");
        sql.append(" where occ.parent_id='B' \n");
        sql.append(" group by occ.id \n");
        List list = new ArrayList();
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("ccpartyId", ccpartyId);
        if (!StringUtils.isEmpty(beginDate)) {
            query.setString("beginDate", beginDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setString("endDate", endDate);
        }
        list = query.list();
        for (int i = 0; i < list.size(); i++) {
            Object[] objs = (Object[]) list.get(i);
            QueryResultConference queryConference = new QueryResultConference();
            queryConference.setCategoryName(String.valueOf(objs[1]));
            queryConference.setNum(Integer.parseInt(String.valueOf(objs[2])));
            queryConference.setHits(Integer.parseInt(String.valueOf(objs[3])));
            queryConference.setReply(Integer.parseInt(String.valueOf(objs[4])));
            queryConference.setFiles(Integer.parseInt(String.valueOf(objs[5])));

            articleCounts.add(queryConference);
        }
        return articleCounts;
    }

    public List<QueryResultConference> getConferenceFormartStatistics(String ccpartyId, String beginDate, String endDate) {
        List<QueryResultConference> articleCounts = new ArrayList<QueryResultConference>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select occ.id,occ.name,count(conference.id),IFNULL(sum(conference.hits),0) hits,IFNULL(sum(conference.reply),0) reply,IFNULL(sum(conference.file),0) file \n");
        sql.append(" from obt_conference_category as occ left join  \n");
        sql.append(" ( \n");
        sql.append(" SELECT DISTINCT OCC.ID AS CATEGORY_ID,OCC.NAME AS CATEGORY_NAME,OC.ID,IFNULL(OC.HITS,0) AS HITS, \n");
        sql.append(" IFNULL((SELECT COUNT(CONFERENCE_ID) FROM OBT_CONFERENCE_COMMENT WHERE CONFERENCE_ID=OC.ID GROUP BY CONFERENCE_ID),0) AS REPLY, \n");
        sql.append(" IFNULL((SELECT COUNT(OBJECT_ID) FROM COM_FILE  WHERE OBJECT_ID=OC.ID GROUP BY OBJECT_ID),0) AS FILE \n");
        sql.append(" FROM OBT_CONFERENCE_CATEGORY AS OCC  \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCS ON OCC.ID=OCS.CATEGORY_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE AS OC ON OCS.CONFERENCE_ID=OC.ID \n");
        sql.append(" WHERE OCC.PARENT_ID='A' AND OCC.TYPE=0  \n");
        sql.append(" AND OC.CCPARTY_ID=:ccpartyId \n");
        if (!StringUtils.isEmpty(beginDate)) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') >=:beginDate \n");
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') <=:endDate \n");
        }
        sql.append(" ORDER BY OCC.ORDER_NO \n");
        sql.append(" )AS CONFERENCE on occ.id=conference.category_id\n");
        sql.append(" where occ.parent_id='A' \n");
        sql.append(" group by occ.id \n");
        List list = new ArrayList();
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("ccpartyId", ccpartyId);
        if (!StringUtils.isEmpty(beginDate)) {
            query.setString("beginDate", beginDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setString("endDate", endDate);
        }
        list = query.list();
        for (int i = 0; i < list.size(); i++) {
            Object[] objs = (Object[]) list.get(i);
            QueryResultConference queryConference = new QueryResultConference();
            queryConference.setCategoryName(String.valueOf(objs[1]));
            queryConference.setNum(Integer.parseInt(String.valueOf(objs[2])));
            queryConference.setHits(Integer.parseInt(String.valueOf(objs[3])));
            queryConference.setReply(Integer.parseInt(String.valueOf(objs[4])));
            queryConference.setFiles(Integer.parseInt(String.valueOf(objs[5])));

            articleCounts.add(queryConference);
        }
        return articleCounts;
    }

    public List<QueryResultConference> getConferenceLabelStatistics(String ccpartyId, String beginDate, String endDate) {
        List<QueryResultConference> articleCounts = new ArrayList<QueryResultConference>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select occ.id,occ.name,count(conference.id),IFNULL(sum(conference.hits),0) hits,IFNULL(sum(conference.reply),0) reply,IFNULL(sum(conference.file),0) file \n");
        sql.append(" from obt_conference_category as occ left join  \n");
        sql.append(" ( \n");
        sql.append(" SELECT DISTINCT OCC.ID AS CATEGORY_ID,OCC.NAME AS CATEGORY_NAME,OC.ID,IFNULL(OC.HITS,0) AS HITS, \n");
        sql.append(" IFNULL((SELECT COUNT(CONFERENCE_ID) FROM OBT_CONFERENCE_COMMENT WHERE CONFERENCE_ID=OC.ID GROUP BY CONFERENCE_ID),0) AS REPLY, \n");
        sql.append(" IFNULL((SELECT COUNT(OBJECT_ID) FROM COM_FILE  WHERE OBJECT_ID=OC.ID GROUP BY OBJECT_ID),0) AS FILE \n");
        sql.append(" FROM OBT_CONFERENCE_CATEGORY AS OCC  \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCS ON OCC.ID=OCS.CATEGORY_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE AS OC ON OCS.CONFERENCE_ID=OC.ID \n");
        sql.append(" WHERE OCC.PARENT_ID='C' \n");
        sql.append(" AND OC.CCPARTY_ID=:ccpartyId \n");
        if (!StringUtils.isEmpty(beginDate)) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') >=:beginDate \n");
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') <=:endDate \n");
        }
        sql.append(" ORDER BY OCC.ORDER_NO \n");
        sql.append(" )AS CONFERENCE on occ.id=conference.category_id\n");
        sql.append(" where occ.parent_id='C' AND ( OCC.TYPE=0 OR (OCC.TYPE=1 AND OCC.CCPARTY_ID=:ccpartyId)) \n");
        sql.append(" group by occ.id \n");
        List list = new ArrayList();
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("ccpartyId", ccpartyId);
        if (!StringUtils.isEmpty(beginDate)) {
            query.setString("beginDate", beginDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setString("endDate", endDate);
        }
        list = query.list();
        for (int i = 0; i < list.size(); i++) {
            Object[] objs = (Object[]) list.get(i);
            QueryResultConference queryConference = new QueryResultConference();
            queryConference.setCategoryName(String.valueOf(objs[1]));
            queryConference.setNum(Integer.parseInt(String.valueOf(objs[2])));
            queryConference.setHits(Integer.parseInt(String.valueOf(objs[3])));
            queryConference.setReply(Integer.parseInt(String.valueOf(objs[4])));
            queryConference.setFiles(Integer.parseInt(String.valueOf(objs[5])));

            articleCounts.add(queryConference);
        }
        return articleCounts;
    }

    /**
     * 
     * <B>方法名称：</B>根据文章发表获取查询统计的日期<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月22日
     * @param ccpartyId
     * @param userId
     * @return
     */
    public String[] getConferencesYearsByUserOrCcparty(String ccpartyId, String userId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select YEAR(a.createTime) ");
        hql.append(" from Conference as a where a.sourceId=:userId or a.sourceId=:ccpartyId ");
        hql.append(" group by YEAR(a.createTime) ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("userId", userId).setString("ccpartyId", ccpartyId);
        List list = query.list();
        String[] years = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            years[i] = list.get(i).toString();
        }
        return years;
    }

    /**
     * <B>方法名称：</B>获取某组织某个时间段内某些栏目下的文章<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月3日
     * @param categoryIdList
     * @param ccpartyId
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<Conference> getConferenceList(String categoryIds, String ccpartyId, Timestamp beginTime, Timestamp endTime) {
        StringBuffer hql = new StringBuffer();
        Object[] params = null;
        if (categoryIds != null && categoryIds.length() > 0) {
            hql.append(" select a from Conference as a, ConferenceCategory as b where a.ccpartyId=?");
            hql.append(" and a.createTime between ? and ?");
            hql.append(" and a.id=b.articleId and b.category.id in (" + categoryIds + ")");
            params = new Object[] { ccpartyId, beginTime, endTime };
        } else {
            hql.append("from Conference as a where a.ccpartyId=?");
            hql.append(" and a.createTime between ? and ?");
            params = new Object[] { ccpartyId, beginTime, endTime };
        }
        List list = dao.loadList(hql.toString(), null, null, params);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>删除不需要转发的文章<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月8日
     * @param articleId
     * @param ccpartyIds
     */
    public void deleteConferenceNotIn(String articleId, List<Object> ccpartyIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Conference.class);
        daoPara.addCondition(Condition.EQUAL("fromId", articleId));
        daoPara.addCondition(Condition.NOTIN("ccpartyId", ccpartyIds));
        dao.delete(daoPara);
    }

    /**
     * 
     * <B>方法名称：</B>根据ID获取详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年2月25日
     * @param id
     * @return
     */
    public Conference getConference(String id) {
        return (Conference) this.loadOne(ObjectType.OBT_CONFERENCE, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 
     * <B>方法名称：</B>获取支部工作记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月25日
     * @param ccpartyId
     * @return
     */
    public Integer getCardPagesForConference(String ccpartyId, JSONObject objs) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(DISTINCT (OC.ID)) \n");
        sql.append(" FROM OBT_CONFERENCE AS OC \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON OC.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON OC.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON OC.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE OC.STATUS=:status \n");
        sql.append(" AND OC.CCPARTY_ID=:ccpartyId AND OC.SOURCE_TYPE=:sourceType \n");
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            sql.append(" AND OCS.CATEGORY_ID IN(:step) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            sql.append(" AND OCF.CATEGORY_ID IN(:format) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            sql.append(" AND OCL.CATEGORY_ID IN(:label) \n");
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
        Query query = session.createSQLQuery(sql.toString()).setString("ccpartyId", ccpartyId).setInteger("status", Conference.STATUS_1).setInteger("sourceType", Conference.SOURCE_TYPE_0);
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            String[] strs = objs.getString("step").split(",");
            query.setParameterList("step", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            String[] strs = objs.getString("format").split(",");
            query.setParameterList("format", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            String[] strs = objs.getString("label").split(",");
            query.setParameterList("label", strs);
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
     * <B>方法名称：</B>获取支部工作列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月26日
     * @param offset
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<Object> getConferenceListByCcparty(Integer offset, Integer limit, String ccpartyId, JSONObject objs) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT(OC.ID) \n");
        sql.append(" FROM obt_conference AS OC \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_STEP AS OCS ON OC.ID=OCS.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_FORMAT AS OCF ON OC.ID=OCF.CONFERENCE_ID \n");
        sql.append(" LEFT JOIN OBT_CONFERENCE_LABEL AS OCL ON OC.ID=OCL.CONFERENCE_ID \n");
        sql.append(" WHERE OC.STATUS=:status \n");
        sql.append(" AND OC.SOURCE_TYPE =:sourceType \n");
        sql.append(" AND OC.CCPARTY_ID=:ccpartyId \n");
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            sql.append(" AND OCS.CATEGORY_ID IN(:step) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            sql.append(" AND OCF.CATEGORY_ID IN(:format) \n");
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            sql.append(" AND OCL.CATEGORY_ID IN(:label) \n");
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
        sql.append(" ORDER BY OC.IS_TOP DESC,OC.OCCUR_TIME DESC,OC.CREATE_TIME DESC \n");

        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setInteger("status", Conference.STATUS_1).setString("ccpartyId", ccpartyId).setInteger("sourceType", Conference.SOURCE_TYPE_0);
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        if (!StringUtils.isEmpty(objs.getString("step"))) {
            String[] strs = objs.getString("step").split(",");
            query.setParameterList("step", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("format"))) {
            String[] strs = objs.getString("format").split(",");
            query.setParameterList("format", strs);
        }
        if (!StringUtils.isEmpty(objs.getString("label"))) {
            String[] strs = objs.getString("label").split(",");
            query.setParameterList("label", strs);
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
        return query.list();
    }

    /**
     * <B>方法名称：</B>组织工作统计<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月25日
     * @param ccpartyId
     * @return
     */
    public List<Object> getConferenceCcpartyStatistics(String parentId, String beginDate, String endDate) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT C.ID,C.NAME,IFNULL(OC.SUM,0) SUM,IFNULL(OC.MEMBER,0) MEMBER,IFNULL(OC.PARTY,0) PARTY,IFNULL(OC.RECOMMEND,0) RECOMMEND,IFNULL(OC.BRAND,0) BRAND \n");
        sql.append(" FROM ORG_CCPARTY AS C \n");
        sql.append(" LEFT JOIN \n");
        sql.append(" ( \n");
        sql.append(" SELECT OC.CCPARTY_ID,COUNT(OC.ID) SUM, \n");
        sql.append(" (SELECT COUNT(ID) FROM OBT_CONFERENCE WHERE CCPARTY_ID=OC.CCPARTY_ID AND SOURCE_TYPE=1 \n");
        if (!StringUtils.isEmpty(beginDate)) {
            sql.append(" AND DATE_FORMAT(OCCUR_TIME,'%Y-%m-%d') >=:beginDate \n");
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql.append(" AND DATE_FORMAT(OCCUR_TIME,'%Y-%m-%d') <=:endDate \n");
        }
        sql.append(" ) MEMBER, \n");
        sql.append(" (SELECT COUNT(ID) FROM OBT_CONFERENCE WHERE CCPARTY_ID=OC.CCPARTY_ID AND SOURCE_TYPE=0 \n");
        if (!StringUtils.isEmpty(beginDate)) {
            sql.append(" AND DATE_FORMAT(OCCUR_TIME,'%Y-%m-%d') >=:beginDate \n");
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql.append(" AND DATE_FORMAT(OCCUR_TIME,'%Y-%m-%d') <=:endDate \n");
        }
        sql.append(" ) PARTY, \n");
        sql.append(" (SELECT COUNT(ID) FROM OBT_CONFERENCE WHERE CCPARTY_ID=OC.CCPARTY_ID AND IS_RECOMMEND=1 \n");
        if (!StringUtils.isEmpty(beginDate)) {
            sql.append(" AND DATE_FORMAT(OCCUR_TIME,'%Y-%m-%d') >=:beginDate \n");
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql.append(" AND DATE_FORMAT(OCCUR_TIME,'%Y-%m-%d') <=:endDate \n");
        }
        sql.append(" ) RECOMMEND, \n");
        sql.append(" (SELECT COUNT(ID) FROM OBT_CONFERENCE WHERE CCPARTY_ID=OC.CCPARTY_ID AND IS_BRAND=1 \n");
        if (!StringUtils.isEmpty(beginDate)) {
            sql.append(" AND DATE_FORMAT(OCCUR_TIME,'%Y-%m-%d') >=:beginDate \n");
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql.append(" AND DATE_FORMAT(OCCUR_TIME,'%Y-%m-%d') <=:endDate \n");
        }
        sql.append(" ) BRAND \n");
        sql.append(" FROM OBT_CONFERENCE AS OC WHERE 1=1 \n");
        if (!StringUtils.isEmpty(beginDate)) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') >=:beginDate \n");
        }
        if (!StringUtils.isEmpty(endDate)) {
            sql.append(" AND DATE_FORMAT(OC.OCCUR_TIME,'%Y-%m-%d') <=:endDate \n");
        }
        sql.append(" GROUP BY OC.CCPARTY_ID \n");
        sql.append(" ) AS OC ON C.ID=OC.CCPARTY_ID \n");
        sql.append(" WHERE C.PARENT_ID =:parentId \n");
        sql.append(" ORDER BY C.SEQUENCE ASC \n");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("parentId", parentId);
        if (!StringUtils.isEmpty(beginDate)) {
            query.setString("beginDate", beginDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            query.setString("endDate", endDate);
        }
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>获取列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月4日
     * @param yearMonth
     * @param ccpartyId
     * @param brand
     * @return
     */
    public List<Conference> getConferences(String nowDate, String afterMonthDate, String ccpartyId, Integer brand) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from Conference as c ");
        hql.append(" where c.ccpartyId=:ccpartyId ");
        if (!StringUtils.isEmpty(nowDate) && !StringUtils.isEmpty(afterMonthDate)) {
            hql.append(" and DATE_FORMAT(c.createTime,'%Y-%m-%d') between :afterMonthDate and :nowDate ");
        }
        if (brand != null) {
            hql.append(" and c.isBrand=:brand ");
        }
        hql.append(" and c.status=:status ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("ccpartyId", ccpartyId).setInteger("status", Conference.STATUS_1);
        if (!StringUtils.isEmpty(nowDate) && !StringUtils.isEmpty(afterMonthDate)) {
            query.setString("afterMonthDate", afterMonthDate);
            query.setString("nowDate", nowDate);
        }
        if (brand != null) {
            query.setInteger("brand", brand);
        }
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>获取支部工作记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月25日
     * @param ccpartyId
     * @return
     */
    public Integer getCardPagesForConference(String ccpartyId, String year) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(bc.id) from Conference as bc where bc.ccpartyId=:ccpartyId ");
        if (!StringUtils.isEmpty(year)) {
            hql.append(" and DATE_FORMAT(bc.occurTime, '%Y')=:year ");
        }
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("ccpartyId", ccpartyId);
        if (!StringUtils.isEmpty(year)) {
            query.setString("year", year);
        }
        if (query.uniqueResult() == null) {
            return 0;
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }

    /**
     * 
     * <B>方法名称：</B>获取上一篇<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月21日
     * @param currentId
     * @param viewSource
     * @param ccpartyId
     * @param userId
     * @return
     */
    public List<Object> getBeforeConference(Conference conference, String viewSource, String ccpartyId, String userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT(CA.ID) \n");
        sql.append(" FROM obt_conference AS CA \n");
        sql.append(" WHERE CA.STATUS=:status \n");
        if ("ccparty".equals(viewSource)) {
            //组织列表查看的详情
            sql.append(" ##组织 \n");
            if (!StringUtils.isEmpty(ccpartyId)) {
                sql.append(" AND CA.CCPARTY_ID=:ccpartyId \n");
            }
        } else if ("share".equals(viewSource)) {
            //共享中心查看的详情
            sql.append(" ##权限 \n");
            sql.append(" AND \n");
            sql.append(" ( \n");
            sql.append("         (CA.SECRET_LEVEL=2) ##公开 \n");
            sql.append("    OR (CA.SECRET_LEVEL IN(0,1) AND CA.CCPARTY_ID=:ccpartyId)   ##本组织 \n");
            sql.append("   OR (CA.SECRET_LEVEL=1 AND CA.CCPARTY_ID IN(SELECT OC.ID FROM ORG_CCPARTY AS OC WHERE OC.PARENT_ID=:ccpartyId AND OC.STATUS=0)) ##本级及上级可见 \n");
            sql.append(" ) \n");
        } else if ("myCard".equals(viewSource)) {
            //我的党员活动证查看的详情
            sql.append(" AND CA.ID IN( ");
            sql.append(" select conference_id from obt_conference_participants where user_id=:userId ");
            sql.append(" ) ");
        }
        sql.append(" AND CA.ID !=:id ");
        sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d %H:%i:%s') >=DATE_FORMAT(:occurTime,'%Y-%m-%d %H:%i:%s') \n");
        sql.append(" ORDER BY CA.IS_TOP DESC,CA.OCCUR_TIME DESC,CA.CREATE_TIME DESC \n");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setInteger("status", Conference.STATUS_1);
        if ("ccparty".equals(viewSource)) {
            //组织列表查看的详情
            query.setString("ccpartyId", ccpartyId);
        } else if ("share".equals(viewSource)) {
            //共享中心查看的详情
            query.setString("ccpartyId", ccpartyId);
        } else if ("myCard".equals(viewSource)) {
            //我的党员活动证查看的详情
            query.setString("userId", userId);
        }
        query.setString("id", conference.getId());
        query.setString("occurTime", conference.getOccurTime().toString());
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>获取上一篇<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月21日
     * @param currentId
     * @param viewSource
     * @param ccpartyId
     * @param userId
     * @return
     */
    public List<Object> getAfterConference(Conference conference, String viewSource, String ccpartyId, String userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT(CA.ID) \n");
        sql.append(" FROM obt_conference AS CA \n");
        sql.append(" WHERE CA.STATUS=:status \n");
        if ("ccparty".equals(viewSource)) {
            //组织列表查看的详情
            sql.append(" ##组织 \n");
            if (!StringUtils.isEmpty(ccpartyId)) {
                sql.append(" AND CA.CCPARTY_ID=:ccpartyId \n");
            }
        } else if ("share".equals(viewSource)) {
            //共享中心查看的详情
            sql.append(" ##权限 \n");
            sql.append(" AND \n");
            sql.append(" ( \n");
            sql.append("         (CA.SECRET_LEVEL=2) ##公开 \n");
            sql.append("    OR (CA.SECRET_LEVEL IN(0,1) AND CA.CCPARTY_ID=:ccpartyId)   ##本组织 \n");
            sql.append("   OR (CA.SECRET_LEVEL=1 AND CA.CCPARTY_ID IN(SELECT OC.ID FROM ORG_CCPARTY AS OC WHERE OC.PARENT_ID=:ccpartyId AND OC.STATUS=0)) ##本级及上级可见 \n");
            sql.append(" ) \n");
        } else if ("myCard".equals(viewSource)) {
            //我的党员活动证查看的详情
            sql.append(" AND CA.ID IN( ");
            sql.append(" select conference_id from obt_conference_participants where user_id=:userId ");
            sql.append(" ) ");
        }
        sql.append(" AND CA.ID !=:id ");
        sql.append(" AND DATE_FORMAT(CA.OCCUR_TIME,'%Y-%m-%d %H:%i:%s') <=DATE_FORMAT(:occurTime,'%Y-%m-%d %H:%i:%s') \n");
        sql.append(" ORDER BY CA.IS_TOP DESC,CA.OCCUR_TIME DESC,CA.CREATE_TIME DESC \n");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setInteger("status", Conference.STATUS_1);
        if ("ccparty".equals(viewSource)) {
            //组织列表查看的详情
            query.setString("ccpartyId", ccpartyId);
        } else if ("share".equals(viewSource)) {
            //共享中心查看的详情
            query.setString("ccpartyId", ccpartyId);
        } else if ("myCard".equals(viewSource)) {
            //我的党员活动证查看的详情
            query.setString("userId", userId);
        }
        query.setString("id", conference.getId());
        query.setString("occurTime", conference.getOccurTime().toString());
        return query.list();
    }
}
