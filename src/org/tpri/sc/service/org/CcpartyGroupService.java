package org.tpri.sc.service.org;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.org.CcpartyGroup;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.PartyGroupMemberManager;
import org.tpri.sc.manager.org.CcpartyGroupManager;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.ZTreeView;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党小组服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年8月1日
 */
@Service("CcpartyGroupService")
public class CcpartyGroupService {

    public Logger logger = Logger.getLogger(CcpartyGroupService.class);

    @Autowired
    private CcpartyGroupManager ccpartyGroupManager;
    @Autowired
    private PartyGroupMemberManager partyGroupMemberManager;

    /**
     * <B>方法名称：</B>根据党组织ID获取党小组列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param start
     * @param limit
     * @param ccpartyId
     * @param searchName
     * @return
     */
    public List<CcpartyGroup> getCcpartyGroupList(Integer start, Integer limit, String ccpartyId, String searchName) {
        List<CcpartyGroup> groups = ccpartyGroupManager.getCcpartyGroupList(start, limit, ccpartyId, searchName);
        for (CcpartyGroup group : groups) {
            int memberCount = partyGroupMemberManager.getPartyGroupMemberTotalByGroupId(group.getId());
            group.setMemberCount(memberCount);
        }
        return groups;
    }

    /**
     * <B>方法名称：</B>添加党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param loginUser
     * @param param
     * @return
     */
    public boolean addCcpartyGroup(UserMc loginUser, Map<String, Object> param) {
        CcpartyGroup group = new CcpartyGroup();
        group.setId(UUIDUtil.id());
        group.setCcpartyId((String) param.get("ccpartyId"));
        group.setName((String) param.get("name"));
        group.setDescription((String) param.get("description"));
        group.setSequence((int) param.get("sequence"));
        group.setCreateTime(new Timestamp(System.currentTimeMillis()));
        group.setCreateUserId(loginUser.getId());
        ccpartyGroupManager.addCcpartyGroup(group);
        return true;
    }

    /**
     * <B>方法名称：</B>修改党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param loginUser
     * @param param
     * @return
     */
    public boolean updateCcpartyGroup(UserMc loginUser, Map<String, Object> param) {
        String id = (String) param.get("id");
        CcpartyGroup group = ccpartyGroupManager.getCcpartyGroupById(id);
        if (group != null) {
            group.setName((String) param.get("name"));
            group.setDescription((String) param.get("description"));
            group.setSequence((int) param.get("sequence"));
            group.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            group.setUpdateUserId(loginUser.getId());
            ccpartyGroupManager.updateCcpartyGroup(group);
            return true;
        } else {
            return false;
        }
    }

    /**
     * <B>方法名称：</B>删除党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param id
     * @return
     */
    public Map<String, Object> deleteCcpartyGroup(String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        CcpartyGroup group = ccpartyGroupManager.getCcpartyGroupById(id);
        if (group == null) {
            ret.put("success", false);
            ret.put("msg", "删除失败，对象为空。");
            return ret;
        }
        ccpartyGroupManager.deleteCcpartyGroup(group);
        ret.put("success", true);
        ret.put("msg", "已成功删除党小组。");
        return ret;
    }

    /**
     * <B>方法名称：</B>根据党组织获取所属党小组树<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param ccpartyId
     * @return
     */
    public List<ZTreeView> getGroupsTreeByCcparty(String ccpartyId) {
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        List<CcpartyGroup> groups = ccpartyGroupManager.getCcpartyGroupList(null, null, ccpartyId, null);
        for (CcpartyGroup group : groups) {
            ZTreeView tree = new ZTreeView();
            tree.setId(group.getId());
            tree.setName(group.getName());
            tree.setOpen(true);
            tree.setpId(ccpartyId);
            tree.setIcon(ZTreeView.DEPARTMENT_TREE_ICON);
            trees.add(tree);
        }
        return trees;
    }

    /**
     * <B>方法名称：</B>根据ID获取党小组<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param id
     * @return
     */
    public CcpartyGroup getCcpartyGroupById(String id) {
        CcpartyGroup group = ccpartyGroupManager.getCcpartyGroupById(id);
        return group;
    }

}
