package org.tpri.sc.manager.org;

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
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.org.CCParty;

/**
 * @description 党组织管理类
 * @author 易文俊
 * @since 2015-04-24
 */

@Repository("CCPartyManager")
public class CCPartyManager extends ManagerBase {
    private static boolean initialized = false;

    public void initialize() {
        if (initialized)
            return;
        initialized = true;
        ObjectRegister.registerClass(ObjectType.ORG_CCPARTY, CCParty.class);
        initializeObjects(ObjectType.ORG_CCPARTY);
    }

    /**
     * 添加党组织
     * 
     * @return
     */
    public void addCCParty(CCParty ccparty) {
        add(ccparty);
        addCache(ccparty);
    }

    /**
     * 更新党组织
     * 
     * @return
     */
    public void updateCCParty(CCParty ccparty) {
        saveOrUpdate(ccparty);
        updateCache(ccparty);
    }

    /**
     * 删除党组织
     * 
     * @return
     */
    public void deleteCCParty(CCParty ccparty) {
        this.delete(ccparty);
        removeCache(ccparty);
    }

    /**
     * 从缓存中获取党组织
     * 
     * @return
     */
    public CCParty getCCPartyFromMc(String id) {
        CCParty ccparty = (CCParty) loadMcCacheObject(ObjectType.ORG_CCPARTY, id);
        return ccparty;
    }

    /**
     * 获取所有党组织
     * 
     * @return
     */
    public List<CCParty> getAllCCParty() {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(CCParty.class);
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 根据党组织ID获取下级党组织列表
     * 
     * @return
     */
    public List<CCParty> getCCPartyListByParentId(String parentId, Integer status) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(CCParty.class);
        if (StringUtils.isNotEmpty(parentId)) {
            daoPara.addCondition(Condition.EQUAL("parentId", parentId));
        }
        if (status != null) {
            daoPara.addCondition(Condition.EQUAL("status", status));
        }
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    public CCParty getCCPartyById(String id) {
        return (CCParty) this.loadOne(ObjectType.ORG_CCPARTY, new String[] { "id" }, new Object[] { id });
    }
    
    /**
     * <B>方法名称：</B>根据党组织ID和单位类型获取下级党组织列表<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年9月18日    
     * @param parentId
     * @param partyType
     * @param status
     * @return
     */
    public List<CCParty> getCCPartyListByParentId(String parentId, Integer partyType, Integer status) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(CCParty.class);
        if (StringUtils.isNotEmpty(parentId)) {
            daoPara.addCondition(Condition.EQUAL("parentId", parentId));
        }
        if (partyType != null) {
            daoPara.addCondition(Condition.EQUAL("partyType", partyType));
        }
        if (status != null) {
            daoPara.addCondition(Condition.EQUAL("status", status));
        }
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 根据组织id获取所有集合
     * 
     * @param orgId
     * @return
     */
    public List<CCParty> getCcpartyListById(String id) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(CCParty.class);
        daoPara.addCondition(Condition.EQUAL("id", id));
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * @Description: 获取当前组织和下级组织(不包含平级和孙子节点)
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:33:07
     * @param ccpartyId 当前组织ID
     * @return
     */
    public List<CCParty> getTreeCCPartyOneSelfAndSon(String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" FROM CCParty c WHERE c.id=:ccpartyId or c.parentId=:ccpartyId ");
        hql.append(" order by c.sequence asc ");
        return dao.getCurrentSession().createQuery(hql.toString()).setString("ccpartyId", ccpartyId).list();
    }

    /**
     * <B>方法名称：</B>获取当前组织和下级组织(不包含平级和孙子节点)并且分页<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 王钱俊
     * @since 2015年10月20日
     * @param start
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<CCParty> getTreeCCPartyOneSelfAndSonPagination(Integer start, Integer limit, String ccpartyId) {
        StringBuffer hql = new StringBuffer("FROM CCParty c WHERE c.id='" + ccpartyId + "' or c.parentId='" + ccpartyId + "'  ORDER BY c.id ");
        Query q = dao.getCurrentSession().createQuery(hql.toString());
        if (start != null && limit != null) {
            q.setFirstResult(start);
            q.setMaxResults(limit);
        }
        return q.list();
    }

    /**
     * 
     * @Description: 获取当前组织和下级组织(不包含平级和孙子节点)
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:32:44
     * @param currentCcpartyId 当前组织ID
     * @param currentCcpartyParentId 当前组织父ID
     * @return
     */
    public List<CCParty> getTreeCCPartyOneselfAndEqualLevelAndSon(String currentCcpartyId, String currentCcpartyParentId) {
        StringBuffer hql = new StringBuffer();
        hql.append("FROM CCParty c WHERE (c.parentId=:currentCcpartyId or c.parentId=:currentCcpartyParentId) ");
        hql.append(" order by c.sequence asc ");
        return dao.getCurrentSession().createQuery(hql.toString()).setString("currentCcpartyId", currentCcpartyId).setString("currentCcpartyParentId", currentCcpartyParentId).list();
    }

    /**
     * 
     * @Description: 获取当前组织的子节点树 (不包含当前组织、平级组织和孙子节点)
     * @author: 赵子靖
     * @since: 2015年9月10日 上午11:24:41
     * @param currentCcpartyId
     * @return
     */
    public List<CCParty> getTreeCCPartySon(String currentCcpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append("FROM CCParty c WHERE c.parentId=:currentCcpartyId");
        hql.append(" order by c.sequence asc ");
        return dao.getCurrentSession().createQuery(hql.toString()).setString("currentCcpartyId", currentCcpartyId).list();
    }

    /**
     * 
     * <B>方法名称：</B>获取满足条件的党组织集合<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月19日
     * @param ccpartyIds
     * @return
     */
    public List<CCParty> getCcpartysByCcpartyIds(List<Object> ccpartyIds) {
        StringBuffer hql = new StringBuffer();
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(CCParty.class);
        daoPara.addCondition(Condition.IN("id", ccpartyIds));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获得直接下级党组织数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年10月27日
     * @param ccpartyId
     * @return
     */
    public int getChildNumsOfParty(String ccpartyId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select count(ccparty.id) ");
        hql.append(" from CCParty as ccparty ");
        hql.append(" where ccparty.parentId=:ccpartyId");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("ccpartyId", ccpartyId);
        return (new Integer(query.uniqueResult().toString())).intValue();
    }
    
    /**
     * <B>方法名称：</B>获取当前组织和下级组织(不包含平级和孙子节点)总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 王钱俊
     * @since 2015年11月11日
     * @param ccpartyId
     * @return
     */
    public int getTotalTreeCCPartyOneSelfAndSonPagination(String ccpartyId) {
        StringBuffer hql = new StringBuffer("SELECT COUNT(*) FROM CCParty c WHERE c.id='" + ccpartyId + "' or c.parentId='" + ccpartyId + "'  ORDER BY c.id ");
        Query q = dao.getCurrentSession().createQuery(hql.toString());
        return Integer.parseInt(q.list().get(0).toString());
    }
}
