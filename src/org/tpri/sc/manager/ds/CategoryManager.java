package org.tpri.sc.manager.ds;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.ds.Category;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>试题库类别<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（zhaozijing）
 * @since 2017年3月13日
 */
@Repository("CategoryManager")
public class CategoryManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.DS_CATEGORY, Category.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2017年3月13日
     * @param parentId
     * @return
     */
    public List<Category> getCategoryList(String parentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Category.class);
        daoPara.addCondition(Condition.EQUAL("parentId", parentId));
        daoPara.addCondition(Condition.EQUAL("status", Category.STATUS_0));
        daoPara.addOrder(Order.desc("sequence"));
        return (List) dao.loadList(daoPara);
    }

}
