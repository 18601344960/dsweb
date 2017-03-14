package org.tpri.sc.service.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.sys.Enumeration;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.sys.EnumerationManager;

/**
 * @description 枚举服务类
 * @author 易文俊
 * @since 2015-06-30
 */

@Service("EnumerationService")
public class EnumerationService {

	@Autowired
	EnumerationManager enumerationManager;

	/**
	 * 添加枚举
	 */
	public boolean addEnumeration(UserMc user, Map<String, Object> param) {
		Enumeration enumeration =new Enumeration();
		String id = (String) param.get("id");
		enumeration.setId(id);
		enumeration.setName((String) param.get("name"));
		enumeration.setStatus((int) param.get("status"));
		enumerationManager.addEnumeration(enumeration);
		return true;
	}
	public boolean updateEnumeration(UserMc user, Map<String, Object> param) {
		String id = (String) param.get("id");
		Enumeration enumeration = enumerationManager.getEnumeration(id);
		enumeration.setId(id);
		enumeration.setName((String) param.get("name"));
		enumeration.setStatus((int) param.get("status"));
		enumerationManager.saveEnumeration(enumeration);
		return true;
	}

	/**
	 * 删除枚举
	 */
	public boolean deleteEnumeration(UserMc user, String id) {
		Enumeration enumeration = enumerationManager.getEnumeration(id);
		enumerationManager.deleteEnumeration(enumeration);
		return true;
	}

	/**
	 * 根据ID获取枚举
	 */
	public Enumeration getEnumerationById(String id) {
		Enumeration enumeration = enumerationManager.getEnumeration(id);
		return enumeration;
	}

	/**
	 * 获取所有枚举
	 */
	public List<Enumeration> getEnumerationList() {
		List<Enumeration> list = new ArrayList<Enumeration>();
		list = enumerationManager.getEnumerationList();
		return list;
	}

	/**
	 * 获取所有枚举
	 */
	public List<Enumeration> getEnumerationList(Integer start, Integer limit) {
		List<Enumeration> list = new ArrayList<Enumeration>();
		list = enumerationManager.getEnumerationList(start, limit);
		return list;
	}

	public Integer getEnumerationTotal() {
		return enumerationManager.getEnumerationTotal();
	}

	
}
