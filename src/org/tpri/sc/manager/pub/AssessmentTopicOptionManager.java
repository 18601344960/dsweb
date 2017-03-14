package org.tpri.sc.manager.pub;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.pub.AssessmentTopicOption;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答题答卷选项管理类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年8月10日
 */
@Repository("AssessmentTopicOptionManager")
public class AssessmentTopicOptionManager extends ManagerBase {
	static {
        ObjectRegister.registerClass(ObjectType.PUB_ASSESSMENT_TOPIC_OPTION, AssessmentTopicOption.class);
    }

    /**
     * 根据试题ID获取选项集合
     * @param topicId
     * @return
     */
    public List<AssessmentTopicOption> getOptionByTopicId(String topicId){
    	DaoPara daoPara = new DaoPara();
		daoPara.setClazz(AssessmentTopicOption.class);
		daoPara.addCondition(Condition.EQUAL("topicId", topicId));
		daoPara.addOrder(Order.asc("seq"));
        List list = dao.loadList(daoPara);
        return list;
    }
    
    /**
     * 根据ID获取选项
     * @return
     */
    public AssessmentTopicOption getOptionById(String id)  {
    	AssessmentTopicOption option=(AssessmentTopicOption)super.load(id, ObjectType.PUB_ASSESSMENT_TOPIC_OPTION);
    	return option;
    }
    
}
