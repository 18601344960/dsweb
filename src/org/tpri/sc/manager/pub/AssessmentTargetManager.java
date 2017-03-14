package org.tpri.sc.manager.pub;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.pub.AssessmentTarget;

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：文件测评</B><BR>
 * <B>中文类名：问卷测评通知对象管理类</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月17日
 */
@Repository("AssessmentTargetManager")
public class AssessmentTargetManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.PUB_ASSESSMENT_TARGET, AssessmentTarget.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取某答卷的测评对象列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param offset
     * @param limit
     * @param assessmentId
     * @return
     */
    public List<AssessmentTarget> getAssessmentTargetsByAssessment(Integer offset, Integer limit, String assessmentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentTarget.class);
        daoPara.addCondition(Condition.EQUAL("assessmentId", assessmentId));
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("ccpartyId"));
        List list = dao.loadList(daoPara);
        return list;
    }


    /**
     * 
     * <B>方法名称：</B>获取某对象的答卷测评对象记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param assessmentId
     * @return
     */
    public Integer getAssessmentTargetsTotalByAssessment(String assessmentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentTarget.class);
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
    public AssessmentTarget getAssessmentTargetById(String id) {
        return (AssessmentTarget) this.loadOne(ObjectType.PUB_ASSESSMENT_TARGET, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 
     * <B>方法名称：</B>根据答卷和对象获取测评人员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param assessmentId
     * @param userId
     * @return
     */
    public AssessmentTarget getAssessmentTargetByAssessmentAndTarget(String assessmentId, String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentTarget.class);
        daoPara.addCondition(Condition.EQUAL("assessmentId", assessmentId));
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        return (AssessmentTarget) dao.loadOne(daoPara);
    }
    
    public List<AssessmentTarget> getAssessmentTargetsByAssessmentAndCcpartys(String assessmentId, List<Object> ccpartyIds) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentTarget.class);
        daoPara.addCondition(Condition.EQUAL("assessmentId", assessmentId));
        if(ccpartyIds!=null && ccpartyIds.size()>0){
            daoPara.addCondition(Condition.IN("ccpartyId", ccpartyIds));
        }
        daoPara.addOrder(Order.asc("ccpartyId"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据组织获取需要参与的列表<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年10月11日   
     * @param ccpartyId
     * @return
     */
    public List<AssessmentTarget> getAssessmentTargetsByCcparty(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentTarget.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        List list = dao.loadList(daoPara);
        return list;
    }
}
