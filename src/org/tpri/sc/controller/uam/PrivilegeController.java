package org.tpri.sc.controller.uam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.uam.Privilege;
import org.tpri.sc.service.uam.PrivilegeService;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.ZTreeView;

/**
 * @description  角色控制器
 * @author 常华荣
 * @since 2015-04-25
 */
@Controller
@RequestMapping("/uam")
public class PrivilegeController  extends BaseController{

	@Resource(name = "PrivilegeService")
	private PrivilegeService privilegeService;
	
	/**
	 * 加载角色列表
	 */
	@RequestMapping("loadPrivilege")
	@ResponseBody
	public Map loadPrivilege(HttpServletRequest request) {
		logger.debug("PrivilegeController loadPrivilege begin");
		
		JSONObject jo = getJsonPara(request);
		Integer start=jo.optString("start",null)==null?null:jo.getInt("start");
		Integer limit=jo.optString("limit",null)==null?null:jo.getInt("limit");
		
		List list=privilegeService.loadPrivilege(start,limit);
		Map ret = new HashMap();
		ret.put("items", list);
		ret.put("totalCount", list.size());
		
		logger.debug("PrivilegeController loadPrivilege end");
		return ret;
	}
	/**
	 * 新增用户
	 */
	@RequestMapping("addPrivilege")
	@ResponseBody
	public Map addPrivilege(HttpServletRequest request) {
		logger.debug("PrivilegeController addPrivilege begin");
		String id=getString(request,"id");
		String name=getString(request,"name");
		String description=getString(request,"description");
		String parentId=getString(request,"parentId");		
		
		privilegeService.addPrivilege(id,name,description,parentId);
		Map ret = new HashMap();
		ret.put("success", true);
		ret.put("msg", "保存成功");
		logger.debug("PrivilegeController addPrivilege end");
		return ret;
	}
	
	/**
	 * 编辑用户
	 */
	@RequestMapping("editPrivilege")
	@ResponseBody
	public Map editPrivilege(HttpServletRequest request) {
		logger.debug("PrivilegeController editPrivilege begin");
		String id=getString(request,"id");
		String name=getString(request,"name");
		String description=getString(request,"description");
		String parentId=getString(request,"parentId");
		
		privilegeService.editPrivilege(id, name, description, parentId);
		Map ret = new HashMap();
		ret.put("success", true);
		ret.put("msg", "保存成功");
		logger.debug("PrivilegeController editPrivilege end");
		return ret;
	}
	/**
	 * 编辑用户
	 */
	@RequestMapping("delPrivilege")
	@ResponseBody
	public Map delPrivilege(HttpServletRequest request) {
		logger.debug("PrivilegeController editPrivilege begin");
		String id=getString(request,"id");	
		Map ret = new HashMap();
		try {
			privilegeService.deletePrivilege(id);
			ret.put("success", true);
			ret.put("msg", "保存成功");
			logger.debug("PrivilegeController editPrivilege end");
			return ret;
			
		} catch (Exception e) {
			ret.put("success", false);
			ret.put("msg", "删除失败！");
			logger.debug("PrivilegeController editPrivilege exception",e);
			return ret;
			
		}
	}
	
	/**
	 * 新增or保存权限
	 */
	@RequestMapping("addOrEditPrivilege")
	@ResponseBody
	public Map addOrEditPrivilege(HttpServletRequest request) {
		logger.debug("PrivilegeController addOrEditPrivilege begin");
		Map ret = new HashMap();
		JSONObject objs = new JSONObject();
		String id=getString(request,"id");	
		if(id!=null && !"".equals(id)){
			objs.put("id", id);
		}else{
			objs.put("id", UUIDUtil.id());
		}
		objs.put("name", getString(request,"name"));
		objs.put("desc", getString(request,"desc"));
		if(privilegeService.saveOrUpdatePrivilege(objs)){
			ret.put("success", true);
			ret.put("msg", "保存成功");
		}else{
			ret.put("success", false);
			ret.put("msg", "保存失败");
		}
		logger.debug("PrivilegeController addOrEditPrivilege end");
		return ret;
	}
	
	/**
	 * 删除权限
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deletePrivilege")
    @ResponseBody
    public Map deletePrivilege(HttpServletRequest request) throws Exception {
    	logger.debug(this.getClass()+" deletePrivilege begin");
        String deletePrivilegeIds=getString(request,"deletePrivilegeIds");
		JSONArray deletePrivilegeIdsArray=JSONArray.fromObject(deletePrivilegeIds);
		for(int i=0;i<deletePrivilegeIdsArray.size();i++){
			privilegeService.deletePrivilege(deletePrivilegeIdsArray.getString(i));
		}
		Map ret = new HashMap();
		ret.put("success",true);
		logger.debug(this.getClass()+" deletePrivilege end");
		return ret;
    }
	
	/**
	 * 
	 * @Description: 加载权限 使用bootstrap形式展示树结构
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getPrivilegesTableTree")
    @ResponseBody
    public Map getPrivilegesTree(HttpServletRequest request) throws Exception {
    	logger.debug(this.getClass()+" getPrivilegesTableTree begin");
    	Map ret = new HashMap();
		List<Privilege> privileges =privilegeService.getPrivilegesTableTree();
		ret.put("root", Privilege.ROOTID);
		ret.put("rows",privileges);
		logger.debug(this.getClass()+" getPrivilegesTableTree end");
		return ret;
    }
	
	/**
	 * 
	 * @Description: 获取所有权限树型展示
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAllPrivilegesTree")
    @ResponseBody
    public Map getAllPrivilegesTree(HttpServletRequest request) throws Exception {
    	logger.debug(this.getClass()+" getAllPrivilegesTree begin");
    	Map ret = new HashMap();
    	List<ZTreeView> privilegesTree = privilegeService.loadPrivilegeTree();
    	ret.put("items", privilegesTree);
		logger.debug(this.getClass()+" getAllPrivilegesTree end");
		return ret;
    }
	
	
}
