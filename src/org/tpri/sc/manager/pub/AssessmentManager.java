package org.tpri.sc.manager.pub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.pub.Assessment;
import org.tpri.sc.view.pub.AssessmentResultStatisticalAnswerView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：答题答卷</B><BR>
 * <B>中文类名：答题答卷管理类</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月15日
 */
@Repository("AssessmentManager")
public class AssessmentManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.PUB_ASSESSMENT, Assessment.class);
    }

    /**
     * 
     * <B>方法名称：获取答题答卷列表</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param start
     * @param limit
     * @param search
     * @param organizationId
     * @return
     */
    public List<Assessment> getAssessmentList(Integer offset, Integer limit, String search, String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Assessment.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("createTime"));
        return (List) dao.loadList(daoPara);
    }

    /**
     * 
     * <B>方法名称：获取答题答卷总记录条数</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月15日
     * @param search
     * @param organizationId
     * @return
     */
    public Integer getAssessmentTotal(String search, String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Assessment.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        return dao.getTotalCount(daoPara);
    }

    /**
     * 
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月11日
     * @param offset
     * @param limit
     * @param search
     * @param ccpartyId
     * @param userId
     * @return
     */
    public List<Assessment> getMyAssessments(Integer offset, Integer limit, String search, String ccpartyId,String userId,Integer joinType) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from Assessment as a where a.status in(:status) and a.id in( ");
        hql.append(" select distinct at.assessmentId from AssessmentTarget as at where at.ccpartyId=:ccpartyId ");
        hql.append(" ) ");
        if(joinType==1){
            //已参加
            hql.append(" and a.id in( ");
            hql.append(" select au.assessmentId from AssessmentUser as au where au.userId=:userId ");
            hql.append(" ) ");
        }else if(joinType==0){
            //未参与
            hql.append(" and a.id not in( ");
            hql.append(" select au.assessmentId from AssessmentUser as au where au.userId=:userId ");
            hql.append(" ) ");
        }
        if (!StringUtils.isEmpty(search)) {
            hql.append(" and a.name like :search ");
        }
        hql.append(" order by a.createTime desc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setParameterList("status", new Integer[] { Assessment.STATUS_1, Assessment.STATUS_2 }).setString("ccpartyId", ccpartyId);
        if(joinType!=-1){
            query.setString("userId", userId);
        }
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        return query.list();
    }
    
    public Integer getMyAssessmentsTotal(String search, String ccpartyId,String userId,Integer joinType) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(a.id) from Assessment as a where a.status in(:status) and a.id in( ");
        hql.append(" select distinct at.assessmentId from AssessmentTarget as at where at.ccpartyId=:ccpartyId ");
        hql.append(" ) ");
        if(joinType==1){
            //已参加
            hql.append(" and a.id in( ");
            hql.append(" select au.assessmentId from AssessmentUser as au where au.userId=:userId ");
            hql.append(" ) ");
        }else if(joinType==0){
            //未参与
            hql.append(" and a.id not in( ");
            hql.append(" select au.assessmentId from AssessmentUser as au where au.userId=:userId ");
            hql.append(" ) ");
        }
        if (!StringUtils.isEmpty(search)) {
            hql.append(" and a.name like :search ");
        }
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setParameterList("status", new Integer[] { Assessment.STATUS_1, Assessment.STATUS_2 }).setString("ccpartyId", ccpartyId);
        if(joinType!=-1){
            query.setString("userId", userId);
        }
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }

    /**
     * 
     * <B>方法名称：</B>根据ID获取详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月14日
     * @param id
     * @return
     */
    public Assessment getAssessmentById(String id) {
        return (Assessment) this.loadOne(ObjectType.PUB_ASSESSMENT, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 
     * <B>方法名称：根据试题获取单选、多选结果集合</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月18日
     * @param assessmentId
     * @return
     */
    public List<AssessmentResultStatisticalAnswerView> getOptionAnswerResults(String topicId, String assessmentId) {
        List<AssessmentResultStatisticalAnswerView> views = new ArrayList<AssessmentResultStatisticalAnswerView>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT pato.id,pato.seq,pato.content,COUNT(par.result) ");
        sql.append(" FROM pub_assessment_topic AS pat ");
        sql.append(" LEFT JOIN pub_assessment_topic_option AS pato ON pat.id=pato.topic_id ");
        sql.append(" LEFT JOIN pub_assessment_result AS par ON ");
        sql.append(" (pato.id=par.result and  ");
        sql.append(" par.assessment_user_id in( ");
        sql.append(" select pau.id from pub_assessment_user as pau where pau.assessment_id=:assessmentId ");
        sql.append(" ) ");
        sql.append(" ) ");
        sql.append(" WHERE pat.id=:topicId ");
        sql.append(" GROUP BY pato.id ");
        sql.append(" ORDER BY pat.sequence,pato.seq ");
        SQLQuery query = dao.getCurrentSession().createSQLQuery(sql.toString());
        query.setString("topicId", topicId).setString("assessmentId", assessmentId);
        List list = query.list();
        for (int i = 0; i < list.size(); i++) {
            Object[] objs = (Object[]) list.get(i);
            AssessmentResultStatisticalAnswerView view = new AssessmentResultStatisticalAnswerView();
            view.setAnswerId(String.valueOf(objs[0]));
            view.setAnswerSeq(String.valueOf(objs[1]));
            view.setAnswerContent(String.valueOf(objs[2]));
            view.setNums(Integer.parseInt(String.valueOf(objs[3])));

            views.add(view);
        }
        return views;
    }

    /**
     * 
     * <B>方法名称：根据试题获取简答结果集合</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月18日
     * @param assessmentId
     * @return
     */
    public List<AssessmentResultStatisticalAnswerView> getOptionContentResults(String topicId, String assessmentId) {
        List<AssessmentResultStatisticalAnswerView> views = new ArrayList<AssessmentResultStatisticalAnswerView>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT par.id,'***',par.result ");
        sql.append(" FROM pub_assessment_topic AS pat ");
        sql.append(" LEFT JOIN pub_assessment_result AS par ON pat.id=par.topic_id ");
        sql.append(" LEFT JOIN pub_assessment_user AS pau ON (par.assessment_user_id=pau.id ");
        sql.append(" ) ");
        sql.append(" WHERE pat.id=:topicId ");
        sql.append(" AND pau.assessment_id=:assessmentId ");
        sql.append(" ORDER BY pat.sequence ");
        Session session = dao.getCurrentSession();
        SQLQuery query = session.createSQLQuery(sql.toString());
        query.setString("topicId", topicId).setString("assessmentId", assessmentId);
        List list = query.list();
        for (int i = 0; i < list.size(); i++) {
            Object[] objs = (Object[]) list.get(i);
            AssessmentResultStatisticalAnswerView view = new AssessmentResultStatisticalAnswerView();
            view.setAnswerId(String.valueOf(objs[0]));
            view.setUserName(String.valueOf(objs[1]));
            view.setAnswerContent(String.valueOf(objs[2]));

            views.add(view);
        }
        return views;
    }

    /**
     * 
     * <B>方法名称：</B>根据ids获取集合<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年10月11日   
     * @param offset
     * @param limit
     * @param search
     * @param ids
     * @return
     */
    public List<Assessment> getAssessmentsByIds(Integer offset,Integer limit,String search,List<Object> ids,String ccpartyId){
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Assessment.class);
        daoPara.addCondition(Condition.IN("id", ids));
        daoPara.addCondition(Condition.NOTEQUAL("ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.NOTEQUAL("status", Assessment.STATUS_0));
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("createTime"));
        return (List) dao.loadList(daoPara);
    }
    
    /**
     * 
     * <B>方法名称：</B>获取参与答卷的数<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年10月11日   
     * @param search
     * @param ids
     * @return
     */
    public Integer getLeaderAssessmentTotal(String search,List<Object> ids,String ccpartyId){
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Assessment.class);
        daoPara.addCondition(Condition.IN("id", ids));
        daoPara.addCondition(Condition.NOTEQUAL("status", Assessment.STATUS_0));
        daoPara.addCondition(Condition.NOTEQUAL("ccpartyId", ccpartyId));
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        return dao.getTotalCount(daoPara);
    }

    /**
     * 
     * <B>方法名称：</B>获取我需要答卷的数目<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年9月29日    
     * @param ccpartyIds
     * @param userId
     * @param scopes
     * @return
     */
    public Integer getMyAssessmentNum(String userId,String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(a.id) from Assessment as a where a.status in(:status) and a.id in( ");
        hql.append(" select distinct at.assessmentId from AssessmentTarget as at ");
        hql.append(" where (at.ccpartyId=:ccpartyId) ");
        hql.append(" ) ");
        //未参与
        hql.append(" and a.id not in( ");
        hql.append(" select au.assessmentId from AssessmentUser as au where au.userId=:userId ");
        hql.append(" ) ");
        hql.append(" and ( a.isExpiry=0 or (a.isExpiry=1 and DATE_FORMAT(a.endDate,'%Y-%m-%d')>=:nowDate ) ) ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setParameterList("status", new Integer[] { Assessment.STATUS_1, Assessment.STATUS_2 }).setString("ccpartyId", ccpartyId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Calendar calendar = Calendar.getInstance();
        query.setString("userId", userId).setString("nowDate", sdf.format(calendar.getTime()));
        return (new Integer(query.uniqueResult().toString())).intValue();
    }
}
