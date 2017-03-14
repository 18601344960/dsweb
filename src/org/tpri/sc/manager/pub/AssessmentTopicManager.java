package org.tpri.sc.manager.pub;

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
import org.tpri.sc.entity.pub.AssessmentTopic;
/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答题答卷试题管理类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年8月10日
 */
@Repository("AssessmentTopicManager")
public class AssessmentTopicManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.PUB_ASSESSMENT_TOPIC, AssessmentTopic.class);
    }

    /**
     * 根据试卷ID获取试题集合
     * 
     * @param assessmentId
     * @return
     */
    public List<AssessmentTopic> getTopicByAssessmentId(String assessmentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentTopic.class);
        daoPara.addCondition(Condition.EQUAL("assessmentId", assessmentId));
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据ID获取详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月16日
     * @param id
     * @return
     */
    public AssessmentTopic getAssessmentTopicById(String id) {
        return (AssessmentTopic) this.loadOne(ObjectType.PUB_ASSESSMENT_TOPIC, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 
     * <B>方法名称：</B>获取试题最大题号<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2015年12月16日 	
     * @param assessmentId
     * @return
     */
    public int getMaxSeqAssessmentTopicByAssessment(String assessmentId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select max(at.sequence) from AssessmentTopic as at ");
        hql.append(" where at.assessmentId=:assessmentId ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("assessmentId", assessmentId);
        if(query.uniqueResult()!=null){
            return (new Integer(query.uniqueResult().toString())).intValue();
        }else {
            return 0;
        }
    }
}
