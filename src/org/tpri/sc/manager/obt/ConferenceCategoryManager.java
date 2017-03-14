package org.tpri.sc.manager.obt;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.ConferenceCategory;

/**
 * @description 文章类别管理类
 * @author 易文俊
 * @since 2015-04-30
 */

@Repository("ConferenceCategoryManager")
public class ConferenceCategoryManager extends ManagerBase {

    private static boolean initialized = false;

    public void initialize() {
        if (initialized)
            return;
        initialized = true;
        ObjectRegister.registerClass(ObjectType.OBT_CONFERENCE_CATEGORY, ConferenceCategory.class);
        initializeObjects(ObjectType.OBT_CONFERENCE_CATEGORY);
    }

    /**
     * <B>方法名称：</B>添加类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月14日
     * @param conferenceCategory
     */
    public void addConferenceCategory(ConferenceCategory conferenceCategory) {
        add(conferenceCategory);
        addCache(conferenceCategory);
    }

    /**
     * <B>方法名称：</B>保存类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月14日
     * @param conferenceCategory
     */
    public void saveConferenceCategory(ConferenceCategory conferenceCategory) {
        update(conferenceCategory);
        updateCache(conferenceCategory);
    }

    /**
     * <B>方法名称：</B>删除类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月14日
     * @param conferenceCategory
     */
    public void deleteConferenceCategory(ConferenceCategory conferenceCategory) {
        this.delete(conferenceCategory);
        removeCache(conferenceCategory);
    }

    /**
     * <B>方法名称：</B>根据ID获取类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月14日
     * @param id
     * @return
     */
    public ConferenceCategory getConferenceCategoryById(String id) {
        ConferenceCategory conferenceCategory = (ConferenceCategory) loadMcCacheObject(ObjectType.OBT_CONFERENCE_CATEGORY, id);
        return conferenceCategory;
    }

    /**
     * <B>方法名称：</B>获取支部工作步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param ccpartyId
     * @param showRoot
     * @param start
     * @param limit
     * @return
     */
    public List<ConferenceCategory> getStepList() {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceCategory.class);
        daoPara.addCondition(Condition.EQUAL("parentId", "B"));
        daoPara.addOrder(Order.asc("orderNo"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取支部组织生活形式<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月12日
     * @return
     */
    public List<ConferenceCategory> getFormatList() {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceCategory.class);
        daoPara.addCondition(Condition.EQUAL("parentId", "A"));
        daoPara.addOrder(Order.asc("orderNo"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取某组织标签列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param ccpartyId
     * @param showRoot
     * @param start
     * @param limit
     * @return
     */
    public List<ConferenceCategory> getLabelList(String ccpartyId, boolean showRoot, Integer start, Integer limit) {
        String hql = " from ConferenceCategory where parentId=? and (type=0 or ccpartyId=?) order by orderNo asc";
        if (showRoot) {
            hql = "from ConferenceCategory where (parentId=? and (type=0 or ccpartyId=?)) or id='C' order by orderNo asc";
        }
        Object[] params = new Object[] { "C", ccpartyId };
        List list = dao.loadList(hql, start, limit, params);
        return list;
    }

    /**
     * <B>方法名称：</B>获取某组织标签总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param ccpartyId
     * @param showRoot
     * @return
     */
    public Integer getLabelTotal(String ccpartyId, boolean showRoot) {
        String hql = "select count(*) from ConferenceCategory where parentId=? and ccpartyId=?";
        if (showRoot) {
            hql = "select count(*) from ConferenceCategory where (parentId=? and ccpartyId=?) or id='C')";
        }
        Object[] params = new Object[] { "C", ccpartyId };
        Integer total = dao.getTotalCount(hql, params);
        return total;
    }

    /**
     * 
     * <B>方法名称：</B>获取C类标签当前最大的序号<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 刘佳丽
     * @since 2016年7月14日
     * @param parentId
     * @return
     */
    public Integer getMaxOrderNo(String parentId, String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select COALESCE(max(cfc.orderNo),0) from ConferenceCategory as cfc where cfc.parentId=:parentId and cfc.ccpartyId=:ccpartyId and cfc.orderNo !='10000' ");
        Query query = dao.getCurrentSession().createQuery(hql.toString()).setString("parentId", parentId).setString("ccpartyId", ccpartyId);
        int orderNo = (new Integer(query.uniqueResult().toString())).intValue();
        return orderNo++;
    }

}
