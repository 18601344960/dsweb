package org.tpri.sc.manager.sys;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.sys.Code;

/**
 * @description 代码表管理类
 * @author 易文俊
 * @since 2015-06-30
 */

@Repository("CodeManager")
public class CodeManager extends ManagerBase {
    private static boolean initialized = false;

    public void initialize() {
        if (initialized)
            return;
        initialized = true;
        ObjectRegister.registerClass(ObjectType.SYS_CODE, Code.class);
        //mc.clearObject(ObjectType.SYS_CODE);
        initializeObjects(ObjectType.SYS_CODE);
    }

    /**
     * 添加代码
     * 
     * @return
     */
    public void addCode(Code code) {
        add(code);
        addCache(code);
    }

    /**
     * 保存代码
     * 
     * @return
     */
    public void saveCode(Code code) {
        update(code);
        updateCache(code);
    }

    /**
     * 删除代码
     * 
     * @return
     */
    public void deleteCode(Code code) {
        this.delete(code);
        removeCache(code);
    }

    /**
     * 根据ID获取代码
     * 
     * @return
     */
    public Code getCode(String id) {
        Code code = (Code) loadMcCacheObject(ObjectType.SYS_CODE, id);
        return code;
    }

    /**
     * 获取所有分类的代码
     * 
     * @return
     */
    public List<Code> getCodeList() {
        List<Code> list = new ArrayList<Code>();
        List<ObjectBase> objectList = loadMcList(ObjectType.SYS_CODE);
        for (ObjectBase objectBase : objectList) {
            list.add((Code) objectBase);
        }
        return list;
    }

    public List<Code> getCodeList(Integer start, Integer limit) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Code.class);
        if (start != null && limit != null) {
            daoPara.setStart(start);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.asc("id"));
        List code = dao.loadList(daoPara);
        return code;
    }

    public int getCodeTotal() {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Code.class);
        int total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * 获取某个分类的代码
     * 
     * @return
     */
    public List<Code> getCodeListByParentId(String parentId) {
        if (parentId == null || parentId.equals("")) {
            return null;
        }
        List<Code> list = new ArrayList<Code>();
        List<ObjectBase> objectList = loadMcList(ObjectType.SYS_CODE);
        for (ObjectBase objectBase : objectList) {
            Code code = (Code) objectBase;
            if (code.getParentId().equals(parentId)) {
                list.add((Code) objectBase);
            }
        }
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>根据名称获取Code<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年2月24日
     * @param codeName
     * @return
     */
    public List<Code> getCodeByName(String codeName, String parentId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from Code as c where c.parentId=:parentId ");
        hql.append(" and (c.name=:codeName or c.name1=:codeName or c.name2=:codeName) ");
        Session session = dao.getCurrentSession();
        Query query = session.createQuery(hql.toString()).setString("parentId", parentId).setString("codeName", codeName);
        return query.list();
    }

    /**
     * 
     * <B>方法名称：</B>党组职务列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月3日
     * @return
     */
    public List<Code> getPartyOneCodes() {
        List<Code> partyOneCodes = new ArrayList<Code>();
        partyOneCodes.add(this.getCode("A070101.01"));
        partyOneCodes.add(this.getCode("A070101.02"));
        partyOneCodes.add(this.getCode("A070101.03"));
        return partyOneCodes;
    }

    /**
     * 
     * <B>方法名称：</B>党委职务列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月3日
     * @return
     */
    public List<Code> getPartyTwoCodes() {
        List<Code> partyTwoCodes = new ArrayList<Code>();
        partyTwoCodes.add(this.getCode("A070101.04"));
        partyTwoCodes.add(this.getCode("A070101.22"));
        partyTwoCodes.add(this.getCode("A070101.05"));
        partyTwoCodes.add(this.getCode("A070101.06"));
        partyTwoCodes.add(this.getCode("A070101.07"));
        partyTwoCodes.add(this.getCode("A070101.15"));
        partyTwoCodes.add(this.getCode("A070101.16"));
        return partyTwoCodes;
    }

    /**
     * 
     * <B>方法名称：</B>党总支职务列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月3日
     * @return
     */
    public List<Code> getPartyThreeCodes() {
        List<Code> getPartyThreeCodes = new ArrayList<Code>();
        getPartyThreeCodes.add(this.getCode("A070101.09"));
        getPartyThreeCodes.add(this.getCode("A070101.10"));
        getPartyThreeCodes.add(this.getCode("A070101.11"));
        return getPartyThreeCodes;
    }

    /**
     * 
     * <B>方法名称：</B>党支部职位列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月3日
     * @return
     */
    public List<Code> getPartyFourCodes() {
        List<Code> getPartyFourCodes = new ArrayList<Code>();
        getPartyFourCodes.add(this.getCode("A070101.12"));
        getPartyFourCodes.add(this.getCode("A070101.13"));
        getPartyFourCodes.add(this.getCode("A070101.14"));
        return getPartyFourCodes;
    }
}
