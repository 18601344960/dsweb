package org.tpri.sc.manager.org;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.org.OrgPartyLeader;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.util.UUIDUtil;

/**
 * @description 领导班子管理类
 * @author zhaozijing
 * @since 2015-06-22
 */

@Repository("OrgPartyLeaderManager")
public class OrgPartyLeaderManager extends ManagerBase {
	
	static {
        ObjectRegister.registerClass(ObjectType.ORG_PARTY_LEADER, OrgPartyLeader.class);
    }
    
    /**
     * 查询组织的领导班子
     * @param partyId
     * @return
     */
    public List<OrgPartyLeader> loadPartyLeaderByPartyId(String partyId){
    	List<OrgPartyLeader> leaders = new ArrayList<OrgPartyLeader>();
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT opm.id,ocl.title,opm.name\n");
    	sql.append("FROM org_ccparty_leader AS ocl LEFT JOIN org_party_member AS opm ON ocl.partymemberid=opm.id\n");
    	if(!StringUtils.isEmpty(partyId)){
    		sql.append("where ocl.partyid='").append(partyId).append("'\n");
    	}else{
    		return null;
    	}
    	List list = new ArrayList();
    	try{
    		if(!StringUtils.isEmpty(partyId)){
    			Session session=dao.getCurrentSession();
        		Query query=session.createSQLQuery(sql.toString());
        		list=query.list();
        		for(int i=0;i<list.size();i++){
        			Object[] objs = (Object[])list.get(i);
        			OrgPartyLeader leader = new OrgPartyLeader();
        			leader.setId(String.valueOf(objs[0]));
        			leader.setTitle(String.valueOf(objs[1]));
        			leader.setUserName(String.valueOf(objs[2]));
        			leaders.add(leader);
        		}
    		}else{
    			return null;
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("查询领导班子异常："+e.getMessage());
    	}
    	return leaders;
    }
    
    /**
     * 根据组织ID删除领导班子
     * @param ccpartyId
     * @return
     */
    public boolean deletePartyLeaderByCcpartyId(String ccpartyId)  {
    	DaoPara daoPara = new DaoPara();
    	Class clazz = ObjectRegister.getClassByClassType(ObjectType.ORG_PARTY_LEADER);
    	daoPara.setClazz(clazz);
    	daoPara.addCondition(Condition.EQUAL("partyid", ccpartyId));
    	dao.delete(daoPara);
        return true;
    	
    }
    
    
    public boolean updatePartyLeaderByRoleIds(String orgId, String leaderids,User loginUser) {
		boolean execute = false;
		//删除该用户所有权限
		if(this.deletePartyLeaderByCcpartyId(orgId)){
			if(leaderids!=null && !"".equals(leaderids)){
				JSONArray roleIdsArray=JSONArray.fromObject(leaderids);
				for(int i=0;i<roleIdsArray.size();i++){
					//activitiService.deleteDeployment(roleIdsArray.getString(i));
					String[] strs = roleIdsArray.getString(i).split("\\|");
					OrgPartyLeader leader = new OrgPartyLeader();
					leader.setId(UUIDUtil.id());
					if(strs.length==2){
						leader.setTitle(strs[1]);
					}else{
						leader.setTitle("");
					}
					//TODO
//					leader.setPartyMember(partym);
//					leader.setPartyMemberId(strs[0]);
					leader.setCcpartyId(orgId);
					leader.setStatus(0);
					leader.setCreateUser(loginUser.getName());
					leader.setCreateTime(new Timestamp(System.currentTimeMillis()));
					
					if(this.saveOrUpdate(leader)){
						execute = true;
					}else{
						execute = false;
					}
				}
			}
		}else{
			execute = false;
		}
		return execute;
	}
    
}
