package org.tpri.sc.service.sys;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.sys.Environment;
import org.tpri.sc.entity.sys.EnvironmentId;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.sys.EnvironmentManager;
import org.tpri.sc.util.BaseConfig;

/**
 * @description 环境变量服务类
 * @author 易文俊
 * @since 2015-05-11
 */

@Service("EnvironmentService")
public class EnvironmentService {
	public Logger logger = Logger.getLogger(EnvironmentService.class);

	@Resource(name = "EnvironmentManager")
	EnvironmentManager environmentManager;

	/**
	 * 添加环境变量
	 */
	public boolean addEnvironment(UserMc user, Map<String, Object> param) {
		Environment environment = new Environment();
		String id = (String) param.get("id");
		environment.setId(id);
		environment.setType((int) param.get("type"));
		environment.setValue((String) param.get("value"));
		environment.setDescription((String) param.get("description"));
		environmentManager.addEnvironment(environment);
		return true;
	}

	/**
	 * 修改环境变量
	 */
	public boolean updateEnvironment(UserMc user, Map<String, Object> param) {
		String id = (String) param.get("id");
		Environment environment = environmentManager.getEnvironmentById(id);
		environment.setType((int) param.get("type"));
		environment.setValue((String) param.get("value"));
		environment.setDescription((String) param.get("description"));
		environmentManager.saveEnvironment(environment);
		BaseConfig.setConfig(environmentManager);
		return true;
	}

	/**
	 * 删除环境变量
	 */
	public boolean deleteEnvironment(UserMc user, String id) {
		Environment environment = environmentManager.getEnvironmentById(id);
		environmentManager.deleteEnvironment(environment);
		return true;
	}

	/**
	 * 根据ID获取环境变量
	 */
	public Environment getEnvironmentById(String id) {
		Environment environment = environmentManager.getEnvironmentById(id);
		return environment;
	}

	/**
	 * 获取所有环境变量
	 */
	public List<Environment> getEnvironmentList() {
		List<Environment> environments = environmentManager.getEnvironmentList();
		Collections.sort(environments, new Comparator<Environment>() {
			@Override
			public int compare(Environment o1, Environment o2) {
				return o1.getId().compareToIgnoreCase(o2.getId());
			}
		});
		return environments;
	}

	/**
	 * 根据ID获取环境变量的值
	 */
	public Object getEnvironmentValueById(EnvironmentId id) {
		Environment environment = environmentManager.getEnvironmentById(id);
		if (environment == null) {
			return null;
		}
		int type = environment.getType();
		String value = environment.getValue();
		if (value == null || value.equals("")) {
			return null;
		}
		if (type == Environment.TYPE_INT) {
			try {
				Integer realValue = Integer.valueOf(value);
				return realValue;
			} catch (Exception e) {
				logger.error("环境变量" + environment.getId() + "的值是非法的整数");
				return null;
			}
		} else if (type == Environment.TYPE_FLOAT) {
			try {
				Float realValue = Float.valueOf(value);
				return realValue;
			} catch (Exception e) {
				logger.error("环境变量" + environment.getId() + "的值是非法的浮点数");
				return null;
			}
		} else if (type == Environment.TYPE_BOOLEAN) {
			try {
				Boolean realValue = Boolean.valueOf(value);
				return realValue;
			} catch (Exception e) {
				logger.error("环境变量" + environment.getId() + "的值是非法的布尔值");
				return null;
			}
		}
		return value;
	}

}
