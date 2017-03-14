package org.tpri.sc.manager.sys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.sys.Enumeration;

/**
 * @description 枚举管理类
 * @author 易文俊
 * @since 2015-06-30
 */

@Repository("EnumerationManager")
public class EnumerationManager extends ManagerBase {
	private static boolean initialized = false;

	public void initialize() {
		if (initialized)
			return;
		initialized = true;
		ObjectRegister.registerClass(ObjectType.SYS_ENUMERATION, Enumeration.class);
		mc.clearObject(ObjectType.SYS_ENUMERATION);
		initializeObjects(ObjectType.SYS_ENUMERATION);
	}

	/**
	 * 添加枚举
	 * 
	 * @return
	 */
	public void addEnumeration(Enumeration enumeration) {
		add(enumeration);
		addCache(enumeration);
	}

	/**
	 * 保存枚举
	 * 
	 * @return
	 */
	public void saveEnumeration(Enumeration enumeration) {
		update(enumeration);
		updateCache(enumeration);
	}

	/**
	 * 删除枚举
	 * 
	 * @return
	 */
	public void deleteEnumeration(Enumeration enumeration) {
		this.delete(enumeration);
		removeCache(enumeration);
	}

	/**
	 * 根据ID获取枚举
	 * 
	 * @return
	 */
	public Enumeration getEnumeration(String id) {
		Enumeration enumeration = (Enumeration) loadMcCacheObject(ObjectType.SYS_ENUMERATION, id);
		return enumeration;
	}

	/**
	 * 获取所有枚举
	 * 
	 * @return
	 */
	public List<Enumeration> getEnumerationList() {
		List<Enumeration> list = new ArrayList<Enumeration>();
		List<ObjectBase> objectList = loadMcList(ObjectType.SYS_ENUMERATION);
		for (ObjectBase objectBase : objectList) {
			list.add((Enumeration) objectBase);
		}
		return list;
	}

	/**
	 * 从数据库获取所有枚举
	 * 
	 * @return
	 */
	public List<Enumeration> getEnumerationList(Integer start, Integer limit) {
		DaoPara daoPara = new DaoPara();
		daoPara.setClazz(Enumeration.class);
		if (start != null && limit != null) {
			daoPara.setStart(start);
			daoPara.setLimit(limit);
		}
		daoPara.addOrder(Order.asc("id"));
		List list = dao.loadList(daoPara);
		return list;
	}
	/**
	 * 从数据库获取枚举数目
	 * 
	 * @return
	 */
	public Integer getEnumerationTotal() {
		DaoPara daoPara = new DaoPara();
		daoPara.setClazz(Enumeration.class);
		Integer tatol = dao.getTotalCount(daoPara);
		return tatol;
	}

}
