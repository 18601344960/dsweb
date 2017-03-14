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
import org.tpri.sc.entity.sys.Enumeration;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.sys.EnumerationService;

/**
 * @description 枚举控制器
 * @author 易文俊
 * @since 2015-06-30
 */
@Controller
@RequestMapping("/sys")
public class EnumerationController extends BaseController {

	@Resource(name = "EnumerationService")
	private EnumerationService enumerationService;

	/**
	 * 获取所有枚举
	 */
	@RequestMapping("getEnumerationList")
	@ResponseBody
	public Map<String, Object> getEnumerationList(HttpServletRequest request) {
		logger.debug(this.getClass() + " getEnumerationList begin");
		Integer start = getInteger(request, "offset");
		Integer limit = getInteger(request, "limit");
		List list = enumerationService.getEnumerationList(start, limit);
		Integer total = enumerationService.getEnumerationTotal();
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("rows", list);
		ret.put("total", total);
		logger.debug(this.getClass() + " getEnumerationList end");
		return ret;
	}
	
	/**
	 * 根据ID获取枚举
	 */
	@RequestMapping("getEnumerationById")
	@ResponseBody
	public Map<String, Object> getEnumerationById(HttpServletRequest request) {
		logger.debug(this.getClass() + " getEnumerationById begin");
		String id = getString(request, "id");
		Enumeration enumeration = enumerationService.getEnumerationById(id);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("item", enumeration);
		logger.debug(this.getClass() + " getEnumerationById end");
		return ret;
	}

	/**
	 * 新增枚举
	 */
	@RequestMapping("addEnumeration")
	@ResponseBody
	public Map<String, Object> addEnumeration(HttpServletRequest request) {
		logger.debug(this.getClass() + " addEnumeration begin");

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", getString(request, "id"));
		param.put("name", getString(request, "name"));
		param.put("status", getInt(request, "status"));
		UserMc user = loadUserMc(request);
		boolean reuslt = enumerationService.addEnumeration(user, param);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("success", reuslt);
		logger.debug(this.getClass() + " addEnumeration begin");
		return ret;
	}
	/**
	 * 更新枚举
	 */
	@RequestMapping("updateEnumeration")
	@ResponseBody
	public Map<String, Object> updateEnumeration(HttpServletRequest request) {
		logger.debug(this.getClass() + " updateEnumeration begin");

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", getString(request, "id"));
		param.put("name", getString(request, "name"));
		param.put("status", getInt(request, "status"));
		UserMc user = loadUserMc(request);
		boolean reuslt = enumerationService.updateEnumeration(user, param);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("success", reuslt);
		logger.debug(this.getClass() + " updateEnumeration begin");
		return ret;
	}

	/**
	 * 刪除枚举
	 */
	@RequestMapping("deleteEnumeration")
	@ResponseBody
	public Map<String, Object> deleteEnumeration(HttpServletRequest request) {
		logger.debug(this.getClass() + " deleteEnumeration begin");
		String id = getString(request, "id");
		UserMc user = loadUserMc(request);
		enumerationService.deleteEnumeration(user, id);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("success", true);
		logger.debug(this.getClass() + " deleteEnumeration begin");
		return ret;
	}
}
