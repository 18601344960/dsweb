package org.tpri.sc.controller.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.sys.Environment;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.sys.EnvironmentService;

/**
 * @description 环境变量控制器
 * @author 易文俊
 * @since 2015-05-11
 */
@Controller
@RequestMapping("/sys")
public class EnviromentController extends BaseController {

	@Resource(name = "EnvironmentService")
	private EnvironmentService environmentService;

	/**
	 * 获取所有环境变量
	 */
	@RequestMapping("getEnvironmentList")
	@ResponseBody
	public Map<String, Object> getEnvironmentList(HttpServletRequest request) {
		logger.debug(this.getClass() + " getEnvironmentList begin");
		List list = environmentService.getEnvironmentList();
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("rows", list);
		logger.debug(this.getClass() + " getEnvironmentList end");
		return ret;
	}

	/**
	 * 根据ID获取环境变量
	 */
	@RequestMapping("getEnvironmentById")
	@ResponseBody
	public Map<String, Object> getEnvironmentById(HttpServletRequest request) {
		logger.debug(this.getClass() + " getEnvironmentById begin");
		String id = getString(request, "id");
		Environment environment = environmentService.getEnvironmentById(id);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("item", environment);
		logger.debug(this.getClass() + " getEnvironmentById end");
		return ret;
	}

	/**
	 * 新增环境变量
	 */
	@RequestMapping("addEnvironment")
	@ResponseBody
	public Map<String, Object> addEnvironment(HttpServletRequest request) {
		logger.debug(this.getClass() + " addEnvironment begin");

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("id", getString(request, "id"));
		param.put("type", getInt(request, "type"));
		param.put("value", getString(request, "value"));
		param.put("description", getString(request, "description"));
		UserMc user = loadUserMc(request);
		environmentService.addEnvironment(user, param);

		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("success", true);
		ret.put("msg", "保存成功");
		logger.debug(this.getClass() + " addEnvironment begin");
		return ret;
	}

	/**
	 * 修改环境变量
	 */
	@RequestMapping("updateEnvironment")
	@ResponseBody
	public Map<String, Object> updateEnvironment(HttpServletRequest request) {
		logger.debug(this.getClass() + " updateEnvironment begin");

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("id", getString(request, "id"));
		param.put("type", getInt(request, "type"));
		param.put("value", getString(request, "value"));
		param.put("description", getString(request, "description"));
		UserMc user = loadUserMc(request);
		environmentService.updateEnvironment(user, param);

		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("success", true);
		logger.debug(this.getClass() + " updateEnvironment begin");
		return ret;
	}

	/**
	 * 刪除环境变量
	 */
	@RequestMapping("deleteEnvironment")
	@ResponseBody
	public Map<String, Object> deleteEnvironment(HttpServletRequest request) {
		logger.debug(this.getClass() + " deleteEnvironment begin");
		String id = getString(request, "id");
		UserMc user = loadUserMc(request);
		boolean result = environmentService.deleteEnvironment(user, id);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("success", result);
		logger.debug(this.getClass() + " deleteEnvironment begin");
		return ret;
	}
}
