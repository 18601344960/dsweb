package org.tpri.sc.service.org;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.org.Organization;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.org.OrganizationManager;
import org.tpri.sc.service.sys.EnvironmentService;
import org.tpri.sc.util.TpriStringUtils;
import org.tpri.sc.view.ZTreeView;

/**
 * 
 * <B>系统名称：</B>行政组织服务类<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年11月4日
 */
@Service("OrganizationService")
public class OrganizationService {

    @Resource(name = "OrganizationManager")
    OrganizationManager organizationManager;
    @Resource(name = "EnvironmentService")
    private EnvironmentService environmentService;
    @Resource(name = "CCPartyManager")
    private CCPartyManager ccpartyManager;

    /**
     * 
     * @Description: 根据ID获取行政单位
     * @author: 赵子靖
     * @since: 2015年9月10日 上午10:22:52
     * @param id
     * @return
     */
    public Organization getOrganization(String id) {
        Organization organization = organizationManager.getOrganization(id);
        return organization;
    }

    /**
     * 
     * @Description: 新增行政单位
     * @param loadUser
     * @param json
     * @return
     */
    public boolean addOrganization(UserMc loadUser, JSONObject json) {
        Organization org = new Organization();
        org.setId(TpriStringUtils.randomForNumbers(32)); // 生成32位随机数
        org.setName(json.getString("name"));
        org.setAddress(json.getString("address"));
        org.setRepresentative(json.getString("perresentative"));
        org.setParentId(json.getString("parentId"));
        org.setDescription(json.getString("description"));
        org.setStatus(json.getInt("status"));
        org.setCreateUserId(loadUser.getId());
        org.setCreateTime(new Timestamp(System.currentTimeMillis()));
        organizationManager.add(org);
        return true;

    }

    /**
     * 
     * @Description: 删除行政单位
     * @param id
     * @return
     */
    public boolean deleteOrganization(String id) {
        Organization Organization = organizationManager.getOrganization(id);
        return organizationManager.delete(Organization);
    }

    /**
     * 
     * @Description: 编辑行政单位
     * @param loadUser
     * @param json
     * @return
     */
    public boolean editOrganization(UserMc loadUser, JSONObject json) {
        String id = json.getString("id");
        if (id != null && !"".equals(id)) {
            Organization org = organizationManager.getOrganization(id);
            org.setName(json.getString("name"));
            org.setAddress(json.getString("address"));
            org.setRepresentative(json.getString("perresentative"));
            org.setParentId(json.getString("parentId"));
            org.setDescription(json.getString("description"));
            org.setStatus(json.getInt("status"));
            org.setUpdateUserId(loadUser.getId());
            org.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            organizationManager.update(org);
            return true;
        } else {
            return false;
        }

    }

    /**
     * 
     * @Description: 获取行政单位树 查询当前组织和所有下级组织 注1、不包含平级组织 2、包含当前组织下的所有子节点，包括子节点和孙子节点
     * @author: 赵子靖
     * @since: 2015年9月10日 上午10:18:21
     * @param organizationId
     * @return
     */
    public List<ZTreeView> getCurrentOrganizationAndSunsToTreeView(String organizationId) {
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        List<Organization> allOrganizations = organizationManager.getAllOrganization(); // 获取所有组织信息
        Organization organization = organizationManager.getOrganization(organizationId); // 当前组织
        // 将当前组织放入树中
        ZTreeView node = new ZTreeView();
        node.setId(organization.getId());
        node.setName(organization.getName());
        node.setpId(organization.getParentId());
        node.setOpen(true);
        node.setIcon(ZTreeView.ORGANIZATION_TREE_ICON);
        trees.add(node);

        if (organization != null && allOrganizations != null) {
            addChildOrganization(allOrganizations, trees, organization.getId());
        }
        return trees;
    }

    /**
     * 
     * <B>方法名称：</B>组装下级行政组织为树结构<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param organizationList
     * @param trees
     * @param parendId
     */
    private void addChildOrganization(List<Organization> organizationList, List<ZTreeView> trees, String parendId) {
        boolean hasChild = false;
        for (Organization organization : organizationList) {
            if (organization.getParentId().equals(parendId)) {
                hasChild = true;
                break;
            }
        }
        if (!hasChild) {
            return;
        }
        for (Organization organization : organizationList) {
            if (organization.getParentId().equals(parendId)) {
                ZTreeView node = new ZTreeView();
                node.setId(organization.getId());
                node.setName(organization.getName());
                node.setpId(organization.getParentId());
                node.setOpen(false);
                node.setIcon(ZTreeView.ORGANIZATION_TREE_ICON);
                trees.add(node);
                addChildOrganization(organizationList, trees, organization.getId());
            }
        }
    }

    /**
     * 
     * @Description: 保存或修改行政单位
     * @param loginUser
     * @param paramter
     * @return
     */
    public Map<String, Object> saveOrUpdateOrganization(UserMc loginUser, Map<String, Object> paramter) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Organization organization = organizationManager.getOrganization((String) paramter.get("id"));
        if (organization == null) {
            organization = new Organization();
            organization.setId((String) paramter.get("id"));
            organization.setCreateTime(new Timestamp(System.currentTimeMillis()));
            organization.setCreateUserId(loginUser.getId());
        } else {
            organization.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            organization.setUpdateUserId(loginUser.getId());
        }
        organization.setName((String) paramter.get("name"));
        organization.setParentId((String) paramter.get("parentId"));
        organization.setRepresentative((String) paramter.get("representative"));
        organization.setAddress((String) paramter.get("address"));
        organization.setStatus((int) paramter.get("status"));
        organization.setSequence((int) paramter.get("sequence"));
        organizationManager.saveOrUpdate(organization);
        ret.put("success", true);
        ret.put("msg", "保存成功");
        return ret;
    }

    /**
     * 
     * @Description: 验证行政单位是否占用
     * @param checkId
     * @return
     */
    public Map<String, Object> checkOrganizationId(String checkId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (StringUtils.isEmpty(checkId)) {
            ret.put("success", true);
            ret.put("msg", "行政单位ID为空！");
        }
        Organization organization = organizationManager.getOrganization(checkId);
        if (organization == null) {
            ret.put("success", false);
            ret.put("msg", "可以使用");
        } else {
            ret.put("success", "true");
            ret.put("msg", "该单位ID已被" + organization.getName() + "占用，请重新更换！");
        }
        return ret;
    }


}
