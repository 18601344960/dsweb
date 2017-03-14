package org.tpri.sc.manager.com;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.com.Announcement;
import org.tpri.sc.entity.uam.User;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>通知通告管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */

@Repository("AnnouncementManager")
public class AnnouncementManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.COM_ANNOUNCEMENT, Announcement.class);
    }

    public Announcement getAnnouncementById(String id) {
        Object obj = this.loadOne(ObjectType.COM_ANNOUNCEMENT, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        Announcement announcement = (Announcement) obj;
        return announcement;
    }

    public boolean deleteAnnouncement(String id) {
        return super.delete(id, ObjectType.COM_ANNOUNCEMENT);
    }

    /**
     * <B>方法名称：</B>获取通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param id
     * @return
     */
    public Announcement getAnnouncement(String id) {
        Object obj = this.loadOne(ObjectType.COM_INFORMATION, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        Announcement Announcement = (Announcement) obj;
        return Announcement;
    }
    
    /**
     * 获取最新一条通知
     * @param ccpartyId
     * @return
     */
    public Announcement getFirstAnnouncement(String ccpartyId) {
    	 DaoPara daoPara = new DaoPara();
         daoPara.setClazz(Announcement.class);
         daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
         daoPara.setStart(0);
         daoPara.setLimit(1);
         daoPara.addOrder(Order.desc("createTime"));
         return (Announcement) dao.loadOne(daoPara);
    }

    /**
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param status
     * @param ccpartyIds
     * @param search
     * @param offset
     * @param limit
     * @return
     */
    public List<Announcement> getAnnouncementList(Integer status, List<String> ccpartyIdList, String search, Integer offset, Integer limit) {
        String hql = "from Announcement as t where 1=1 ";
        if (status != null) {
            hql += " and status=:status ";
        }
        if (search != null && !search.equals("")) {
            hql += " and name like '%:search%' ";
        }
        if (ccpartyIdList != null && ccpartyIdList.size() > 0) {
            hql += " and ccpartyId in(:ccpartyIds) ";
        }
        hql += " order by createTime desc";

        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql);
        if (status != null) {
            query.setInteger("status", status);
        }

        if (search != null && !search.equals("")) {
            query.setString("search", search);
        }
        if (ccpartyIdList != null && ccpartyIdList.size() > 0) {
            query.setParameterList("ccpartyIds", ccpartyIdList);
        }
        if (offset != null && limit != null) {
            query.setFirstResult(offset).setMaxResults(limit);
        }
        return query.list();
    }

    public Integer getAnnouncementTotal(Integer status, List<String> ccpartyIdList, String search) {
        String hql = "select count(*) from Announcement as t where 1=1 ";
        if (status != null) {
            hql += " and status=:status ";
        }
        if (search != null && !search.equals("")) {
            hql += " and name like '%:search%' ";
        }
        if (ccpartyIdList != null && ccpartyIdList.size() > 0) {
            hql += " and ccpartyId in(:ccpartyIds)";
        }
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql);
        if (status != null) {
            query.setInteger("status", status);
        }

        if (search != null && !search.equals("")) {
            query.setString("search", search);
        }
        if (ccpartyIdList != null && ccpartyIdList.size() > 0) {
            query.setParameterList("ccpartyIds", ccpartyIdList);
        }
        if (query.uniqueResult() == null) {
            return 0;
        }
        return (new Integer(query.uniqueResult().toString())).intValue();
    }
}
