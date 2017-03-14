package org.tpri.sc.manager.obt;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.obt.ConferenceStep;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>文章所属工作步骤管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月2日
 */

@Repository("ConferenceStepManager")
public class ConferenceStepManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_CONFERENCE_STEP, ConferenceStep.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取文章关联的某步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param id
     * @return
     */
    public ConferenceStep getConferenceStepById(String id) {
        Object obj = this.loadOne(ObjectType.OBT_CONFERENCE_STEP, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        ConferenceStep c = (ConferenceStep) obj;
        return c;
    }

    /**
     * 
     * <B>方法名称：</B>删除文章关联步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param articleId
     * @return
     */
    public boolean deleteConferenceStepByConferenceId(String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceStep.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        dao.delete(daoPara);
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>删除文章所属步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param id
     * @return
     */
    public boolean deleteConferenceStep(String id) {
        return super.delete(id, ObjectType.OBT_CONFERENCE_STEP);
    }

    /**
     * 
     * <B>方法名称：</B>根据文章ID获取所属哪些步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param articleId
     * @param categoryId
     * @param ccpartyId
     * @return
     */
    public List<ConferenceStep> getConferenceStepByConferenceId(String conferenceId, String categoryId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceStep.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        if (categoryId != null && !"".equals(categoryId)) {
            daoPara.addCondition(Condition.EQUAL("categoryId", categoryId));
        }
        List list = dao.loadList(daoPara);
        return list;
    }
}
