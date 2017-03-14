package org.tpri.sc.manager.org;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.org.CcpartyGroup;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党小组管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年8月1日
 */
@Repository("CcpartyGroupManager")
public class CcpartyGroupManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.ORG_CCPARTY_GROUP, CcpartyGroup.class);
    }

    /**
     * <B>方法名称：</B>添加党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param group
     */
    public void addCcpartyGroup(CcpartyGroup group) {
        this.add(group);
    }

    /**
     * <B>方法名称：</B>更新党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param group
     */
    public void updateCcpartyGroup(CcpartyGroup group) {
        this.saveOrUpdate(group);
    }

    /**
     * <B>方法名称：</B>删除党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param group
     */
    public void deleteCcpartyGroup(CcpartyGroup group) {
        this.delete(group.getId(), ObjectType.ORG_CCPARTY_GROUP);
    }

    /**
     * <B>方法名称：</B>获取党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param id
     * @return
     */
    public CcpartyGroup getCcpartyGroupById(String id) {
        return (CcpartyGroup) this.loadOne(ObjectType.ORG_CCPARTY_GROUP, new String[] { "id" }, new Object[] { id });
    }

    /**
     * <B>方法名称：</B>根据党组织ID获取党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param start
     * @param limit
     * @param ccpartyId
     * @param searchName
     * @return
     */
    public List<CcpartyGroup> getCcpartyGroupList(Integer start, Integer limit, String ccpartyId, String searchName) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(CcpartyGroup.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        if (!StringUtils.isEmpty(searchName)) {
            daoPara.addCondition(Condition.LIKE("name", searchName));
        }
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>根据党组织ID删除党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param ccpartyId
     * @return
     */
    public boolean deleteCcpartyGroupByCcpartyId(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(CcpartyGroup.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        dao.delete(daoPara);
        return true;

    }
}
