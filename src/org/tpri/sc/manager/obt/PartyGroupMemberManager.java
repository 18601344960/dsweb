package org.tpri.sc.manager.obt;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.PartyGroupMember;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党小组成员管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年8月1日
 */

@Repository("PartyGroupMemberManager")
public class PartyGroupMemberManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_PARTY_GROUP_MEMBER, PartyGroupMember.class);
    }

    /**
     * <B>方法名称：</B>根据ID获取党小组成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param id
     * @return
     */
    public PartyGroupMember getPartyGroupMemberById(String id) {
        Object obj = this.loadOne(ObjectType.OBT_PARTY_GROUP_MEMBER, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        PartyGroupMember partyGroupMember = (PartyGroupMember) obj;
        return partyGroupMember;
    }

    /**
     * <B>方法名称：</B>获取某党小组下的成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param groupId
     * @return
     */
    public List<PartyGroupMember> getPartyGroupMemberByGroupId(String groupId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyGroupMember.class);
        daoPara.addCondition(Condition.EQUAL("groupId", groupId));
        daoPara.addOrder(Order.desc("type"));
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取某党小组下的成员总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param groupId
     * @return
     */
    public Integer getPartyGroupMemberTotalByGroupId(String groupId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyGroupMember.class);
        daoPara.addCondition(Condition.EQUAL("groupId", groupId));
        Integer total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * <B>方法名称：</B>根据党小组ID和用户Id获取某党小组下的成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param groupId
     * @return
     */
    public List<PartyGroupMember> getPartyGroupMember(String groupId, String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyGroupMember.class);
        daoPara.addCondition(Condition.EQUAL("groupId", groupId));
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>删除某党小组下的成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param groupId
     * @return
     */
    public boolean deletePartyGroupMemberByGroupId(String groupId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyGroupMember.class);
        daoPara.addCondition(Condition.EQUAL("groupId", groupId));
        dao.delete(daoPara);
        return true;
    }

    /**
     * <B>方法名称：</B>根据ID删除党小组下的成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param id
     * @return
     */
    public boolean deletePartyGroupMember(String id) {
        return super.delete(id, ObjectType.OBT_PARTY_GROUP_MEMBER);
    }
}
