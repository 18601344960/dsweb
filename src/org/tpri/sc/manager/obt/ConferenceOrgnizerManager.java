package org.tpri.sc.manager.obt;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.ConferenceOrgnizer;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织生活组织者管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月24日
 */
@Repository("ConferenceOrgnizerManager")
public class ConferenceOrgnizerManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_CONFERENCE_ORGNIZER, ConferenceOrgnizer.class);
    }

    /**
     * <B>方法名称：</B>根据ID获取组织者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param id
     * @return
     */
    public ConferenceOrgnizer getConferenceOrgnizer(String id) {
        return (ConferenceOrgnizer) this.loadOne(ObjectType.OBT_CONFERENCE_ORGNIZER, new String[] { "id" }, new Object[] { id });
    }

    /**
     * <B>方法名称：</B>获取组织生活的组织者列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param conferenceId
     * @param status
     * @return
     */
    public List<ConferenceOrgnizer> getOrgnizerByConferenceId(String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceOrgnizer.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        daoPara.addOrder(Order.asc("orderNo"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取某组织生活的组织者列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param offset
     * @param limit
     * @param conferenceId
     * @return
     */
    public List<ConferenceOrgnizer> getConferenceOrgnizer(Integer offset, Integer limit, String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceOrgnizer.class);
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
     * <B>方法名称：</B>获取某组织生活的组织者总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param conferenceId
     * @return
     */
    public Integer getConferenceOrgnizerTotal(String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceOrgnizer.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        return dao.getTotalCount(daoPara);
    }

    /**
     * <B>方法名称：</B>获取某个组织生活的某个组织者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年10月9日
     * @param conferenceId
     * @param userId
     * @return
     */
    public ConferenceOrgnizer getOrgnizerByConferenceIdAndUserId(String conferenceId, String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceOrgnizer.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addOrder(Order.asc("orderNo"));
        ConferenceOrgnizer conferenceOrgnizer = (ConferenceOrgnizer) dao.loadOne(daoPara);
        return conferenceOrgnizer;
    }

}
