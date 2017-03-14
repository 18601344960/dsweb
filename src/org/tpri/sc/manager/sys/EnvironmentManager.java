package org.tpri.sc.manager.sys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.sys.Environment;
import org.tpri.sc.entity.sys.EnvironmentId;

/**
 * @description 环境变量管理类
 * @author 易文俊
 * @since 2015-05-11
 */

@Repository("EnvironmentManager")
public class EnvironmentManager extends ManagerBase {
	private static boolean initialized = false;

	public void initialize() {
		if (initialized)
			return;
		initialized = true;
		ObjectRegister.registerClass(ObjectType.SYS_ENVIRONMENT, Environment.class);
		mc.clearObject(ObjectType.SYS_ENVIRONMENT);
		initializeObjects(ObjectType.SYS_ENVIRONMENT);
	}

	/**
	 * 添加环境变量
	 * 
	 * @return
	 */
	public void addEnvironment(Environment environment) {
		add(environment);
		addCache(environment);
	}

	/**
	 * 保存环境变量
	 * 
	 * @return
	 */
	public void saveEnvironment(Environment environment) {
		update(environment);
		updateCache(environment);
	}

	/**
	 * 删除环境变量
	 * 
	 * @return
	 */
	public void deleteEnvironment(Environment environment) {
		this.delete(environment);
		removeCache(environment);
	}

	/**
	 * 根据ID获取环境变量
	 * 
	 * @return
	 */
	public Environment getEnvironmentById(EnvironmentId id) {
		Environment environment = (Environment) loadMcCacheObject(ObjectType.SYS_ENVIRONMENT, id.toString());
		return environment;
	}

	/**
	 * 根据ID获取环境变量
	 * 
	 * @return
	 */
	public Environment getEnvironmentById(String id) {
		Environment environment = (Environment) loadMcCacheObject(ObjectType.SYS_ENVIRONMENT, id);
		return environment;
	}

	/**
	 * 获取所有环境变量
	 * 
	 * @return
	 */
	public List<Environment> getEnvironmentList() {
		List<Environment> list = new ArrayList<Environment>();
		List<ObjectBase> objectList = loadMcList(ObjectType.SYS_ENVIRONMENT);
		for (ObjectBase objectBase : objectList) {
			list.add((Environment) objectBase);
		}
		return list;
	}

}
