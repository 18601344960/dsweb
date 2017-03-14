package org.tpri.sc.manager.uam;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.uam.Privilege;

/**
 * @description 权限管理类
 * @author 易文俊
 * @since 2015-04-09
 */

@Repository("PrivilegeManager")
public class PrivilegeManager  extends ManagerBase {
	private static boolean initialized = false;
    public  void initialize() {
        if (initialized)
            return;
        initialized =  true;
        ObjectRegister.registerClass(ObjectType.UAM_PRIVILEGE, Privilege.class);
        mc.clearObject(ObjectType.UAM_PRIVILEGE);
        initializeObjects(ObjectType.UAM_PRIVILEGE);
    }
    /**
     * 根据ID获取权限
     * @return
     */
    public Privilege getPrivilege(String id)  {
    	Privilege privilege=(Privilege)loadMcCacheObject(ObjectType.UAM_PRIVILEGE,id);
        return privilege;
    }
    
    /**
     * 获取权限列表
     * @return
     */
    public List<Privilege> getPrivilegeList(Integer start, Integer limit)  {
    	DaoPara daoPara = new DaoPara();
		daoPara.setClazz(Privilege.class);
		
		if (start != null && limit != null) {
			daoPara.setStart(start);
			daoPara.setLimit(limit);
		}
		daoPara.addOrder(Order.asc("id"));
        List list = dao.loadList(daoPara);
        return list;
    }
    
    
}
