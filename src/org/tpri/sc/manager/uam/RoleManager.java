package org.tpri.sc.manager.uam;

import java.util.ArrayList;
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
import org.tpri.sc.dao.condition.Logic;
import org.tpri.sc.dao.condition.Operator;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.uam.Privilege;
import org.tpri.sc.entity.uam.Role;

/**
 * @description 授权管理类
 * @author 易文俊
 * @since 2015-04-09
 */

@Repository("RoleManager")
public class RoleManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.UAM_ROLE, Role.class);
    }

    /**
     * 获取角色
     * 
     * @return
     */
    public Role getRoleById(String id) {
        Object obj = this.loadOne(ObjectType.UAM_ROLE, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        Role role = (Role) obj;
        return role;
    }

    /**
     * 获取角色列表
     * 
     * @return
     */
    public List<Role> getRoleList(String searchName, Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Role.class);
        if (!StringUtils.isEmpty(searchName)) {
            daoPara.addCondition(Condition.EQUAL("name", searchName));
        }
        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }
    
    /**
     * 获取角色列表
     * 
     * @return
     */
    public List<Role> getRoleList(String searchName, Integer start, Integer limit, Boolean showAll) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Role.class);
        if (showAll == null || showAll == false) {
            daoPara.addCondition(Condition.EQUAL("isShow", Role.ISSHOW_0));
        }
        if (!StringUtils.isEmpty(searchName)) {
            daoPara.addCondition(Condition.EQUAL("name", searchName));
        }
        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * @Description:获取角色条数
     * @param searchName
     * @return
     */
    public Integer loadRoleTotal(String searchName) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Role.class);
        if (!StringUtils.isEmpty(searchName)) {
            daoPara.addCondition(Condition.LIKE("name", searchName));
        }
        Integer total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * 获取角色列表
     * 
     * @return
     */
    public List<Role> getRoleListByIds(String rolesid) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Role.class);
        if (StringUtils.isNotEmpty(rolesid)) {
            List<Condition> listc = new ArrayList<Condition>();
            String[] ids = rolesid.split(",");
            List list = new ArrayList();
            for (int i = 0; i < ids.length; i++) {
                list.add(ids[i]);
            }
            Condition c = new Condition();
            c.setValues(list);
            c.setFieldName("id");
            c.setOperator(Operator.IN);
            c.setLogic(Logic.AND);
            listc.add(c);

            daoPara.setConditions(listc);
        }

        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }

    public List<Privilege> getPrivilegeListByIds(String privilegeid) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Privilege.class);
        if (StringUtils.isNotEmpty(privilegeid)) {
            List<Condition> listc = new ArrayList<Condition>();
            String[] ids = privilegeid.split(",");
            List list = new ArrayList();
            for (int i = 0; i < ids.length; i++) {
                list.add(ids[i]);
            }
            Condition c = new Condition();
            c.setValues(list);
            c.setFieldName("id");
            c.setOperator(Operator.IN);
            c.setLogic(Logic.AND);
            listc.add(c);

            daoPara.setConditions(listc);
        }

        daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取某角色的所有用户<BR>
     * <B>概要说明：</B><BR>
     * @author zhaozijing
     * @since 2016年8月25日    
     * @param roleId
     * @return
     */
    public List<Object> getUsersByRole(String roleId){
        StringBuffer sql = new StringBuffer();
        sql.append(" select uur.user_id from uam_user_role as uur where uur.role_id=:roleId ");
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("roleId", roleId);
        return query.list();
    }

}
