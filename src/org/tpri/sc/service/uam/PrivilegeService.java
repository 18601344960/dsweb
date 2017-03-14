package org.tpri.sc.service.uam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.tpri.sc.entity.uam.Privilege;
import org.tpri.sc.manager.uam.PrivilegeManager;
import org.tpri.sc.view.ZTreeView;

/**
 * @description 授权服务类
 * @author 易文俊
 * @since 2015-04-09
 */

@Service("PrivilegeService")
public class PrivilegeService {

	@Resource(name = "PrivilegeManager")
	PrivilegeManager privilegeManager;	
	/**
	 * 获取角色
	 * 
	 * @return
	 */
	public Privilege getPrivilege(String id) {
		Privilege Privilege = privilegeManager.getPrivilege(id);
		return Privilege;
	}

	public List loadPrivilege(Integer start, Integer limit) {
		List list = privilegeManager.getPrivilegeList(start, limit);
		return list;
	}

	public boolean addPrivilege(String id, String name, String description,
			String parentId) {

		Privilege privilege = new Privilege();
		privilege.setId(id);
		privilege.setName(name);
		privilege.setDescription(description);
		privilege.setParentId(parentId);
		privilegeManager.add(privilege);
		return true;
	}

	public boolean editPrivilege(String id, String name, String description,
			String parentId) {

		Privilege privilege=privilegeManager.getPrivilege(id);
		privilege.setName(name);
		privilege.setDescription(description);
		privilegeManager.update(privilege);
		return true;

	}
	public boolean deletePrivilege(String id) {

		Privilege Privilege = privilegeManager.getPrivilege(id);
		privilegeManager.delete(Privilege);
		return true;

	}
	
	/**
	 * 保存or修改权限
	 * @param objs
	 * @return
	 */
	public boolean saveOrUpdatePrivilege(JSONObject objs) {
		Privilege privilege = new Privilege();
		privilege.setId(objs.getString("id"));
		privilege.setName(objs.getString("name"));
		privilege.setDescription(objs.getString("desc"));
		if(privilegeManager.saveOrUpdate(privilege)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 加载权限 使用bootstrap形式展示树结构
	 * @return
	 */
	public List<Privilege> getPrivilegesTableTree(){
		List<Privilege> returnPrivileges = new ArrayList<Privilege>();
		List<Privilege> privileges = privilegeManager.getPrivilegeList(null, null);	//获取所有的权限
		this.machinePrivilege(privileges, returnPrivileges, Privilege.ROOTID);
		return returnPrivileges;
	}
	
	/**
	 * 
	 * @Description: 组装
	 * @param sourcelist
	 * @param privileges
	 * @param parendId
	 */
	public void machinePrivilege(List<Privilege> sourcelist,List<Privilege>privileges,String parendId){
		boolean hasChild=false;
		for(Privilege privilege: sourcelist){
			if(privilege.getParentId().equals(parendId)){
				hasChild=true;
				break;
			}
		}
		if(!hasChild){
			return;
		}
		for(Privilege privilege: sourcelist){
			if(privilege.getParentId().equals(parendId)){
				privileges.add(privilege);
				machinePrivilege(sourcelist,privileges,privilege.getId());
			}
		}
	}
	
	/**
	 * 
	 * @Description:获取权限树展示
	 * @return
	 */
	public List<ZTreeView> loadPrivilegeTree(){
		List<ZTreeView> trees = new ArrayList<ZTreeView>();
		List<Privilege> privileges = privilegeManager.getPrivilegeList(null, null);	//获取所有的权限
		this.machinePrivilegeTree(privileges, trees, Privilege.ROOTID);
		return trees;
	}
	
	/**
	 * 
	 * @Description: 组装成树
	 * @param sourcelist
	 * @param trees
	 * @param parendId
	 */
	public void machinePrivilegeTree(List<Privilege> sourcelist,List<ZTreeView> trees,String parendId){
		boolean hasChild=false;
		for(Privilege privilege: sourcelist){
			if(privilege.getParentId().equals(parendId)){
				hasChild=true;
				break;
			}
		}
		if(!hasChild){
			return;
		}
		for(Privilege privilege: sourcelist){
			if(privilege.getParentId().equals(parendId)){
				ZTreeView tree = new ZTreeView();
				tree.setId(privilege.getId());
				tree.setName(privilege.getName());
				tree.setpId(privilege.getParentId());
				tree.setOpen(true);
				trees.add(tree);
				machinePrivilegeTree(sourcelist,trees,privilege.getId());
			}
		}
	}
}
