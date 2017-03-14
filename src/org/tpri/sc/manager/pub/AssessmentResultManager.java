package org.tpri.sc.manager.pub;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.pub.AssessmentResult;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答题答卷结果<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年6月30日
 */
@Repository("AssessmentResultManager")
public class AssessmentResultManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.PUB_ASSESSMENT_RESULT, AssessmentResult.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取某参与人员某试题的结果<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param userId
     * @param assessmentUserId
     * @return
     */
    public List<AssessmentResult> getAssessresultResultByUser(String assessmentUserId, String topicId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentResult.class);
        daoPara.addCondition(Condition.EQUAL("assessmentUserId", assessmentUserId));
        daoPara.addCondition(Condition.EQUAL("topicId", topicId));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取某参与人员结果集合<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月1日
     * @param assessmentUserId
     * @return
     */
    public List<AssessmentResult> getAssessresultResultByUser(String assessmentUserId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(AssessmentResult.class);
        daoPara.addCondition(Condition.EQUAL("assessmentUserId", assessmentUserId));
        List list = dao.loadList(daoPara);
        return list;
    }

}
