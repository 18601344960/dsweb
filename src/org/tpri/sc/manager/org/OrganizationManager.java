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
import org.tpri.sc.entity.org.Organization;

/**
 * 
 * <B>系统名称：</B>党建系统<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>行政单位管理<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年10月20日
 */
@Repository("OrganizationManager")
public class OrganizationManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.ORG_ORGANIZATION, Organization.class);
    }

    /**
     * 
     * <B>方法名称：</B>根据ID获取详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月20日
     * @param id
     * @return
     */
    public Organization getOrganization(String id) {
        return (Organization) this.loadOne(ObjectType.ORG_ORGANIZATION, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 
     * <B>方法名称：</B>获取行政组织列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月20日
     * @param classId
     * @param start
     * @param limit
     * @return
     */
    public List<Organization> getOrganizationList(String classId, Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Organization.class);

        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取所有下级单位<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月20日
     * @param parentId
     * @return
     */
    public List<Organization> getOrganizationListById(String parentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Organization.class);
        daoPara.addCondition(Condition.EQUAL("parentId", parentId));
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取所有行政单位<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月20日
     * @return
     */
    public List<Organization> getAllOrganization() {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Organization.class);
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据名称获取符合条件的行政单位列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月20日
     * @param searchName
     * @return
     */
    public List<Organization> getOrganizationsByName(String searchName) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Organization.class);
        if (!StringUtils.isEmpty(searchName)) {
            daoPara.addCondition(Condition.LIKE("name", searchName));
        }
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

}
