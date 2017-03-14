package org.tpri.sc.manager.com;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.com.Information;
import org.tpri.sc.entity.obt.ConferencePraise;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>通告与知识管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月9日
 */

@Repository("InformationManager")
public class InformationManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.COM_INFORMATION, Information.class);
    }

    public Information getInformationById(String id) {
        Object obj = this.loadOne(ObjectType.COM_INFORMATION, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        Information information = (Information) obj;
        return information;
    }

    public boolean deleteInformation(String id) {
        return super.delete(id, ObjectType.COM_INFORMATION);
    }

    /**
     * 
     * <B>方法名称：</B>获取通告与知识<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年4月9日
     * @param id
     * @return
     */
    public Information getInformation(String id) {
        Object obj = this.loadOne(ObjectType.COM_INFORMATION, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        Information Information = (Information) obj;
        return Information;
    }

    /**
     * 
     * <B>方法名称：</B>获取通告与知识列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年4月9日
     * @param category
     * @param status
     * @param ccpartyIds
     * @param search
     * @param offset
     * @param limit
     * @return
     */
    public List<Information> getInformationList(Integer category, Integer status, String ccpartyId, String search, Integer offset, Integer limit, boolean isShow) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from Information as t where t.category=:category ");
        if (status != null) {
            hql.append(" and t.status=:status ");
        }
        if (isShow) {
            hql.append(" and (t.type=0 or t.ccpartyId =:ccpartyId) ");
        } else {
            hql.append(" and t.ccpartyId =:ccpartyId ");
        }
        if (!StringUtils.isEmpty(search)) {
            hql.append(" and t.name like :search ");
        }
        hql.append(" order by type asc, createTime desc ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setInteger("category", category);
        if (status != null) {
            query.setInteger("status", status);
        }
        query.setString("ccpartyId", ccpartyId);
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        return query.list();
    }

    public int getInformationTotal(Integer category, Integer status, String ccpartyId, String search, boolean isShow) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(t.id) from Information as t where t.category=:category ");
        if (status != null) {
            hql.append(" and t.status=:status ");
        }
        if (isShow) {
            hql.append(" and (t.type=0 or t.ccpartyId =:ccpartyId) ");
        } else {
            hql.append(" and t.ccpartyId =:ccpartyId ");
        }
        if (!StringUtils.isEmpty(search)) {
            hql.append(" and t.name like :search ");
        }
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setInteger("category", category);
        if (status != null) {
            query.setInteger("status", status);
        }
        query.setString("ccpartyId", ccpartyId);
        if (!StringUtils.isEmpty(search)) {
            query.setString("search", "%" + search + "%");
        }
        if (query.uniqueResult() == null) {
            return 0;
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }

    /**
     * 
     * <B>方法名称：</B>工作必备列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月4日
     * @param ccpartyId
     * @return
     */
    public List<Information> getInforamations(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Information.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        List list = dao.loadList(daoPara);
        return list;
    }
}
