package org.tpri.sc.manager.obt;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.Election;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举管理类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */

@Repository("ElectionManager")
public class ElectionManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_ELECTION, Election.class);
    }

    /**
     * <B>方法名称：</B>根据ID获取换届选举<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月1日 	
     * @param id
     * @return
     */
    public Election getElectionById(String id) {
        Object obj = this.loadOne(ObjectType.OBT_ELECTION, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        Election o = (Election) obj;
        return o;
    }

    /**
     * <B>方法名称：</B>获取届次最大的换届选举<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月1日 	
     * @param ccpartyId
     * @return
     */
    public Election getCurrentElection(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Election.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.setStart(0);
        daoPara.setLimit(1);
        daoPara.addOrder(Order.desc("sequence"));
        Election election = (Election) dao.loadOne(daoPara);
        return election;
    }

    /**
     * <B>方法名称：</B>获取上一届换届选举<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月1日 	
     * @param ccpartyId
     * @param sequence
     * @return
     */
    public Election getLastElection(String ccpartyId, Integer sequence) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Election.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.LESS("sequence", sequence));
        daoPara.setStart(0);
        daoPara.setLimit(1);
        daoPara.addOrder(Order.desc("sequence"));
        Election election = (Election) dao.loadOne(daoPara);
        return election;
    }

    /**
     * <B>方法名称：</B>获取下一届换届选举<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月1日 	
     * @param ccpartyId
     * @param sequence
     * @return
     */
    public Election getNextElection(String ccpartyId, Integer sequence) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Election.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.GREATER("sequence", sequence));
        daoPara.setStart(0);
        daoPara.setLimit(1);
        daoPara.addOrder(Order.asc("sequence"));
        Election election = (Election) dao.loadOne(daoPara);
        return election;
    }

    /**
     * <B>方法名称：</B>删除换届选举<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月1日 	
     * @param id
     * @return
     */
    public boolean deleteElection(String id) {
        return super.delete(id, ObjectType.OBT_ELECTION);
    }

    /**
     * <B>方法名称：</B>获取最大届次<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月1日 	
     * @param ccpartyId
     * @return
     */
    public Integer getElectionMaxSequence(String ccpartyId) {
        String hql = "select max(sequence) from Election where ccpartyId='" + ccpartyId + "'";
        Object[] params = new Object[] {};
        Integer maxSerial = dao.queryForInt(hql, params);
        if (maxSerial == null) {
            maxSerial = 0;
        }
        return maxSerial;
    }

    /**
     * <B>方法名称：</B>获取某组织换届选举<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月1日 	
     * @param ccpartyId
     * @param limit
     * @param offset
     * @return
     */
    public List<Election> getElectionList(String ccpartyId, Integer limit, Integer offset) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Election.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        if (limit != null && offset != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取某组织换届选举总数<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月1日 	
     * @param ccpartyId
     * @return
     */
    public Integer getElectionTotal(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Election.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        Integer total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * <B>方法名称：</B>更新换届选举<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月1日 	
     * @param id
     * @param fieldValues
     * @return
     */
    public boolean updateElection(String id, Map<String, Object> fieldValues) {
        super.update(id, ObjectType.OBT_ELECTION, fieldValues);
        return true;
    }

}
