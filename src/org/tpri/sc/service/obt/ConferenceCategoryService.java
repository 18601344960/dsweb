package org.tpri.sc.service.obt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.ConferenceCategory;
import org.tpri.sc.entity.obt.ConferenceLabel;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.ConferenceCategoryManager;
import org.tpri.sc.manager.obt.ConferenceLabelManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.ZTreeView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>文章类别服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月9日
 */

@Service("ConferenceCategoryService")
public class ConferenceCategoryService {

    @Autowired
    ConferenceCategoryManager conferenceCategoryManager;
    @Autowired
    private CCPartyManager cCPartyManager;
    @Autowired
    private ConferenceLabelManager conferenceLabelManager;

    /**
     * <B>方法名称：</B>添加标签<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param user
     * @param param
     * @return
     */
    public boolean addLabel(UserMc user, Map<String, Object> param) {
        String ccpartyId = (String) param.get("ccpartyId");
        String parentId = (String) param.get("parentId");
        ConferenceCategory category = new ConferenceCategory();
        String id = UUIDUtil.id();
        category.setId(id);
        category.setName((String) param.get("name"));
        category.setParentId(parentId);
        category.setType(ConferenceCategory.TYPE_1);
        category.setCcpartyId(ccpartyId);
        category.setDescription((String) param.get("description"));
        Integer orderNo = conferenceCategoryManager.getMaxOrderNo(parentId, ccpartyId);
        category.setOrderNo(orderNo);
        conferenceCategoryManager.addConferenceCategory(category);
        return true;
    }

    /**
     * <B>方法名称：</B>修改标签<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param user
     * @param param
     * @return
     */
    public boolean updateLabel(UserMc user, Map<String, Object> param) {
        String id = (String) param.get("id");
        ConferenceCategory category = conferenceCategoryManager.getConferenceCategoryById(id);
        category.setName((String) param.get("name"));
        category.setDescription((String) param.get("description"));
        category.setOrderNo((Integer) param.get("orderNo"));
        conferenceCategoryManager.saveConferenceCategory(category);
        return true;
    }

    /**
     * <B>方法名称：</B>根据ID获取类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param id
     * @return
     */
    public ConferenceCategory getConferenceCategoryById(String id) {
        ConferenceCategory category = conferenceCategoryManager.getConferenceCategoryById(id);
        return category;
    }

    /**
     * <B>方法名称：</B>根据ID删除类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param user
     * @param id
     * @return
     */
    public boolean deleteConferenceCategory(UserMc user, String id) {
        ConferenceCategory conferenceCategory = conferenceCategoryManager.getConferenceCategoryById(id);
        conferenceCategoryManager.deleteConferenceCategory(conferenceCategory);
        return true;
    }

    /**
     * <B>方法名称：</B>根据ID获取类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param id
     * @return
     */
    public ConferenceCategory getCategoryById(String id) {
        ConferenceCategory category = conferenceCategoryManager.getConferenceCategoryById(id);
        return category;
    }

    public boolean deleteConferenceCategoryAndArtical(String id) {
        List<ConferenceLabel> list = conferenceLabelManager.getConferenceLabelByCategoryId(id);
        for (ConferenceLabel conferenceLabel : list) {
            conferenceLabelManager.deleteConferenceLabelByConferenceId(conferenceLabel.getConferenceId());
        }
        ConferenceCategory conferenceCategory = conferenceCategoryManager.getConferenceCategoryById(id);
        conferenceCategoryManager.deleteConferenceCategory(conferenceCategory);
        return true;
    }

    /**
     * <B>方法名称：</B>获取本支部的标签树<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月13日
     * @param ccpartyId
     * @return
     */
    public List<ZTreeView> getLabelZTree(String ccpartyId) {
        List<ConferenceCategory> categories = conferenceCategoryManager.getLabelList(ccpartyId, true, null, null);
        List<ZTreeView> tree = new ArrayList<ZTreeView>();
        if (categories != null) {
            tree = getZTree(categories);
        }
        return tree;
    }

    private List<ZTreeView> getZTree(List<ConferenceCategory> categories) {
        List<ZTreeView> tree = new ArrayList<ZTreeView>();
        for (ConferenceCategory category : categories) {
            ZTreeView treeNode = new ZTreeView();
            treeNode.setId(category.getId());
            treeNode.setName(category.getName());
            treeNode.setpId(category.getParentId());
            boolean hasChild = hasChild(categories, category.getId());
            if (hasChild) {
                treeNode.setOpen(true);
                treeNode.setIsParent(true);
            }
            tree.add(treeNode);
        }
        return tree;
    }

    private boolean hasChild(List<ConferenceCategory> categories, String parendId) {
        boolean hasChild = false;
        for (ConferenceCategory category : categories) {
            if (category.getParentId() != null && category.getParentId().equals(parendId)) {
                hasChild = true;
                break;
            }
        }
        return hasChild;
    }

    /**
     * <B>方法名称：</B>获取支部工作步骤<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param ccpartyId
     * @param showRoot
     * @param start
     * @param limit
     * @return
     */
    public List<ConferenceCategory> getStepList() {
        List<ConferenceCategory> list = conferenceCategoryManager.getStepList();
        return list;
    }

    /**
     * <B>方法名称：</B>获取支部组织生活形式<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月12日
     * @return
     */
    public List<ConferenceCategory> getFormatList() {
        List<ConferenceCategory> list = conferenceCategoryManager.getFormatList();
        return list;
    }

    /**
     * <B>方法名称：</B>获取某组织标签列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param ccpartyId
     * @param showRoot
     * @param start
     * @param limit
     * @return
     */
    public List<ConferenceCategory> getLabelList(String ccpartyId, boolean showRoot, Integer start, Integer limit) {
        List<ConferenceCategory> list = conferenceCategoryManager.getLabelList(ccpartyId, showRoot, start, limit);
        return list;
    }

    /**
     * <B>方法名称：</B>获取某组织标签总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月17日
     * @param ccpartyId
     * @param showRoot
     * @return
     */
    public Integer getLabelTotal(String ccpartyId, boolean showRoot) {
        return conferenceCategoryManager.getLabelTotal(ccpartyId, showRoot);
    }

}
