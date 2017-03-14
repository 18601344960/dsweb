package org.tpri.sc.service.org;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.Election;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.ElectionManager;
import org.tpri.sc.manager.obt.ElectionMemberManager;
import org.tpri.sc.manager.obt.PartyMemberManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.org.OrganizationManager;
import org.tpri.sc.manager.sys.CodeManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.obt.PartyMemberService;
import org.tpri.sc.service.sys.CodeService;
import org.tpri.sc.service.uam.SystemUserService;
import org.tpri.sc.view.ZTreeView;
import org.tpri.sc.view.obt.PartyMemberStatisticsView;
import org.tpri.sc.view.org.CCPartyTitleView;
import org.tpri.sc.view.org.CcpartyCardInfo;

/**
 * 
 * <B>系统名称：</B>党建系统<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党组织服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月10日
 */
@Service("CCpartyService")
public class CCpartyService {

    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private PartyMemberManager partyMemberManager;
    @Autowired
    private CodeService codeService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private PartyMemberService partyMemberService;
    @Autowired
    private ElectionManager electionManager;
    @Autowired
    private CodeManager codeManager;
    @Autowired
    private ElectionMemberManager electionMemberManager;
    @Autowired
    private SystemUserService systemUserService;

    /**
     * 
     * @Description: 根据ID获取党组织详情
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:46:32
     * @param ccpartyId
     * @return
     */
    public CCParty getCCParty(String ccpartyId) {
        CCParty ccparty = ccpartyManager.getCCPartyById(ccpartyId);
        //查询换届选举获取
        Election election = electionManager.getCurrentElection(ccpartyId);
        if (election != null) {
            ccparty.setDocumentTime(election.getStartTime());
            ccparty.setExpirationTime(election.getEndTime());
        }
        return ccparty;
    }

    /**
     * 
     * @Description: 新增或修改党组织
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:51:28
     * @param loginUser
     * @param objs
     * @param ccpartyId
     * @return
     */
    public Map<String, Object> saveOrUpdateCcparty(UserMc loginUser, JSONObject objs, String ccpartyId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        CCParty ccparty = ccpartyManager.getCCPartyById(ccpartyId);
        if (ccparty == null) {
            ccparty = new CCParty();
            ccparty.setId(ccpartyId);
            ccparty.setCreateTime(new Timestamp(System.currentTimeMillis()));
            ccparty.setCreateUserId(loginUser.getId());
        }
        ccparty.setName(objs.getString("name"));
        ccparty.setType(objs.getString("type"));
        ccparty.setParentId(objs.getString("parentCcpartyId"));
        ccparty.setPartyType(objs.getInt("partyType"));
        ccparty.setSequence(objs.getInt("sequence"));
        ccpartyManager.updateCCParty(ccparty);
        //判断该组织下是否有系统用户，如果没有则创建
        List<User> sysUsers = userManager.getSysUserByCcparty(ccparty.getId());
        if (sysUsers == null || sysUsers.size() == 0) {
            systemUserService.addSystemUser(ccparty.getId());
        }
        ret.put("success", true);
        ret.put("msg", "党组织保存成功");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>删除党组织<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月24日
     * @param id
     * @return
     */
    public Map<String, Object> deleteCCParty(String ccpartyId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (StringUtils.isEmpty(ccpartyId)) {
            ret.put("success", false);
            ret.put("msg", "删除失败，党组织为空！");
            return ret;
        }
        //step1、删除党组织
        CCParty party = ccpartyManager.getCCPartyById(ccpartyId);
        ccpartyManager.deleteCCParty(party);
        //step2、将用户群众面貌改为非党员 党员ID置为空
        List<PartyMember> partyMembers = partyMemberManager.getPartyMemberByCcpartyId(null, null, ccpartyId);
        for (PartyMember partyMember : partyMembers) {
            userManager.setUserNotPartymemberByPartymember(partyMember.getId());
        }
        //step3、删除党员
        partyMemberManager.deleteByCcparty(ccpartyId);
        ret.put("success", true);
        ret.put("msg", "删除党组成功！");
        return ret;
    }

    /**
     * 
     * @Description: 验证党组织是否占用
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:51:03
     * @param checkId 验证党组的ID
     * @return
     */
    public Map<String, Object> checkCcpartyId(String checkId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (StringUtils.isEmpty(checkId)) {
            ret.put("success", true);
            ret.put("msg", "党组织ID为空！");
        }
        CCParty ccparty = ccpartyManager.getCCPartyById(checkId);
        if (ccparty == null) {
            ret.put("success", false);
            ret.put("msg", "可以使用");
        } else {
            ret.put("success", "true");
            ret.put("msg", "该组织ID已被" + ccparty.getName() + "占用，请重新更换！");
        }
        return ret;
    }

    /**
     * <B>方法名称：</B>从缓存中获取党组织<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年12月9日
     * @param ccpartyId
     * @return
     */
    public CCParty getCCPartyFromMc(String ccpartyId) {
        return ccpartyManager.getCCPartyFromMc(ccpartyId);
    }

    /**
     * 
     * @Description: 获取所有组织树
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:45:59
     * @return
     */
    public List<ZTreeView> getTreeCCPartyAlls() {
        List<CCParty> ccpartys = ccpartyManager.getAllCCParty();
        return machineCcpartyTree(ccpartys);
    }

    /**
     * 
     * @Description: 获取当前组织和下级组织(不包含平级和孙子节点)
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:20:44
     * @param ccpartyId
     * @return
     */
    public List<ZTreeView> getTreeCCPartyOneSelfAndSon(String ccpartyId) {
        List<CCParty> ccpartys = ccpartyManager.getTreeCCPartyOneSelfAndSon(ccpartyId);
        return machineCcpartyTree(ccpartys);
    }

    /**
     * <B>方法名称：</B>获取当前组织和下级组织(不包含平级和孙子节点)分页<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 王钱俊
     * @since 2015年10月20日
     * @param start
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<ZTreeView> getTreeCCPartyOneSelfAndSonPagination(Integer start, Integer limit, String ccpartyId) {
        List<CCParty> ccpartys = ccpartyManager.getTreeCCPartyOneSelfAndSonPagination(start, limit, ccpartyId);
        return machineCcpartyTree(ccpartys);
    }

    /**
     * <B>方法名称：</B>获取当前组织和下级组织(不包含平级和孙子节点)的总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 王钱俊
     * @since 2015年11月11日
     * @param ccpartyId
     * @return
     */
    public int getTotalTreeCCPartyOneSelfAndSonPagination(String ccpartyId) {
        return ccpartyManager.getTotalTreeCCPartyOneSelfAndSonPagination(ccpartyId);
    }

    /**
     * 
     * @Description: 获取当前组织和平级和下级组织(不包含孙子节点)
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:26:51
     * @param ccpartyId
     * @return
     */
    public List<ZTreeView> getTreeCCPartyOneselfAndEqualLevelAndSon(String ccpartyId) {
        if (StringUtils.isEmpty(ccpartyId)) {
            return null;
        }
        CCParty currentCcparty = ccpartyManager.getCCPartyById(ccpartyId);
        List<CCParty> ccpartys = ccpartyManager.getTreeCCPartyOneselfAndEqualLevelAndSon(ccpartyId, currentCcparty.getParentId());
        return machineCcpartyTree(ccpartys);
    }

    /**
     * 
     * @Description: 获取当前组织的子节点树 (不包含当前组织、平级组织和孙子节点)
     * @author: 赵子靖
     * @since: 2015年9月10日 上午11:25:00
     * @param ccpartyId
     * @return
     */
    public List<ZTreeView> getTreeCCPartySon(String ccpartyId) {
        if (StringUtils.isEmpty(ccpartyId)) {
            return null;
        }
        List<CCParty> ccpartys = ccpartyManager.getTreeCCPartySon(ccpartyId);
        return machineCcpartyTree(ccpartys);
    }

    /**
     * 
     * @Description: 将组织集合组装成为组织树集合
     * @author: 赵子靖
     * @since: 2015年9月10日 上午11:26:48
     * @param ccpartys
     * @return
     */
    public List<ZTreeView> machineCcpartyTree(List<CCParty> ccpartys) {
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        for (CCParty ccparty : ccpartys) {
            ZTreeView tree = new ZTreeView();
            tree.setId(ccparty.getId());
            tree.setName(ccparty.getName());
            tree.setpId(ccparty.getParentId());
            tree.setOpen(true);
            setCcpartyIcon(tree, ccparty);
            tree.setType(ccparty.getType());
            trees.add(tree);
        }
        return trees;
    }

    /**
     * 
     * @Description: 获取组织树 查询当前组织和所有下级组织 注1、不包含平级组织 2、包含当前组织下的所有子节点，包括子节点和孙子节点
     * @author: 赵子靖
     * @since: 2015年9月9日 下午5:46:32
     * @param request
     * @return
     */
    public List<ZTreeView> getTreeCCPartyAndLowerLevel(String ccpartyId) {
        List<ZTreeView> trees = new ArrayList<ZTreeView>(); // 返回的组织tree
        List<CCParty> allCcpartys = ccpartyManager.getAllCCParty(); // 所有组织list
        CCParty ccparty = ccpartyManager.getCCPartyById(ccpartyId); // 当前党组织
        // 将当前组织放入树中
        ZTreeView node = new ZTreeView();
        node.setId(ccparty.getId());
        node.setName(ccparty.getName());
        node.setpId(ccparty.getParentId());
        node.setOpen(true);
        setCcpartyIcon(node, ccparty);
        node.setType(ccparty.getType());
        trees.add(node);

        if (ccparty != null && allCcpartys != null) {
            addChildCCParty(allCcpartys, trees, ccparty.getId());
        }
        return trees;
    }

    /**
     * 
     * <B>方法名称：</B>组装下级组织为树结构<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param cCPartylist
     * @param tree
     * @param parendId
     */
    public void addChildCCParty(List<CCParty> cCPartylist, List<ZTreeView> tree, String parendId) {
        boolean hasChild = false;
        for (CCParty ccparty : cCPartylist) {
            if (ccparty.getParentId().equals(parendId)) {
                hasChild = true;
                break;
            }
        }
        if (!hasChild) {
            return;
        }
        for (CCParty ccparty : cCPartylist) {
            if (ccparty.getParentId().equals(parendId)) {
                ZTreeView node = new ZTreeView();
                node.setId(ccparty.getId());
                node.setName(ccparty.getName());
                node.setpId(ccparty.getParentId());
                node.setOpen(false);
                setCcpartyIcon(node, ccparty);
                node.setType(ccparty.getType());
                tree.add(node);
                addChildCCParty(cCPartylist, tree, ccparty.getId());
            }
        }
    }

    /**
     * <B>方法名称：</B>设置党组织图标<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年10月29日
     * @param tree
     * @param ccparty
     */
    public void setCcpartyIcon(ZTreeView tree, CCParty ccparty) {
        String type = ccparty.getType();
        if (type.equals(CCParty.TYPE_1) || type.equals(CCParty.TYPE_2) || type.equals(CCParty.TYPE_3)) {
            tree.setIcon(ZTreeView.CCPARTY_GENERAL_TREE_ICON_FLAG);
        } else {
            tree.setIcon(ZTreeView.CCPARTY_GENERAL_TREE_ICON_EMBLEM);
        }
    }

    /**
     * 
     * @Description: 组织ID拼接字符串 只显示本级和下级
     * @author: 赵子靖
     * @since: 2015年9月10日 上午10:29:00
     * @param ccpartyId
     * @return
     */
    public String getCCPartyIdStrsAndLowerLevel(String ccpartyId) {
        String ccpartyIds = "";
        if (StringUtils.isEmpty(ccpartyId)) {
            return null;
        }
        CCParty currentCcparty = ccpartyManager.getCCPartyById(ccpartyId);
        List<CCParty> ccpartys = ccpartyManager.getTreeCCPartyOneselfAndEqualLevelAndSon(ccpartyId, currentCcparty.getParentId());
        for (int i = 0; i < ccpartys.size(); i++) {
            ccpartyIds += ccpartys.get(i).getId() + ",";
        }
        if (ccpartyIds.endsWith(",")) {
            ccpartyIds = ccpartyIds.substring(0, ccpartyIds.length() - 1);
        }
        return ccpartyIds;
    }

    /**
     * 
     * <B>方法名称：</B>获取孩子节点id<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月8日
     * @param ccpartyId
     * @return
     */
    public String getCCPartySunIds(String ccpartyId) {
        String ids = "";
        CCParty current = ccpartyManager.getCCPartyById(ccpartyId);
        if (current == null) {
            return null;
        }
        List<CCParty> sunCcpartys = ccpartyManager.getCCPartyListByParentId(current.getParentId(), CCParty.STATUS_0);
        if (sunCcpartys != null && sunCcpartys.size() > 0) {
            for (CCParty ccparty : sunCcpartys) {
                ids += ccparty.getId() + ",";
            }
        }
        if (ids.endsWith(",")) {
            ids = ids.substring(0, ids.length() - 1);
        }
        return ids;
    }

    /**
     * <B>方法名称：</B>根据父ID获取直接下级党组织列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年10月21日
     * @param parentId
     * @return
     */
    public List<CCParty> getCcpartyListByParentId(String parentId) {
        List<CCParty> ccparties = ccpartyManager.getCCPartyListByParentId(parentId, CCParty.STATUS_0);
        return ccparties;
    }

    /**
     * 
     * <B>方法名称：</B>统计组织信息 党员管理模块<BR>
     * <B>概要说明：</B>统计下属组织数、党员人数及各类型党员数<BR>
     * 
     * @author 赵子靖
     * @since 2016年1月13日
     * @param ccpartyId
     * @return
     */
    public PartyMemberStatisticsView statisticsCcpartyInfosForPartymemberManager(String ccpartyId) {
        PartyMemberStatisticsView view = new PartyMemberStatisticsView();
        List<String> statisticsCcpartyIds = new ArrayList<String>(); //所有下属组织的ID集合
        //党组数据组装
        List<ZTreeView> trees = new ArrayList<ZTreeView>(); // 返回的组织tree
        List<CCParty> allCcpartys = ccpartyManager.getAllCCParty(); // 所有组织list
        CCParty ccparty = ccpartyManager.getCCPartyById(ccpartyId); // 当前党组织
        if (ccparty != null && allCcpartys != null) {
            addChildCCParty(allCcpartys, trees, ccparty.getId());
        }
        statisticsCcpartyIds.add(ccpartyId);
        for (ZTreeView tree : trees) {
            view.setPartyNums(view.getPartyNums() + 1);
            statisticsCcpartyIds.add(tree.getId());
            if (CCParty.TYPE_1.equals(tree.getType())) {
                view.setParty01Nums(view.getParty01Nums() + 1);
            } else if (CCParty.TYPE_2.equals(tree.getType())) {
                view.setParty02Nums(view.getParty02Nums() + 1);
            } else if (CCParty.TYPE_3.equals(tree.getType())) {
                view.setParty03Nums(view.getParty03Nums() + 1);
            } else if (CCParty.TYPE_4.equals(tree.getType())) {
                view.setParty04Nums(view.getParty04Nums() + 1);
            }
        }
        //党组下的党员数据组装
        List<PartyMember> partyMembers = partyMemberManager.getPartyMembersByCcpartys((List<Object>)(List)statisticsCcpartyIds);
        for (PartyMember partyMember : partyMembers) {
            view.setPartyMemberNums(view.getPartyMemberNums() + 1);
            if (PartyMember.TYPE_0 == partyMember.getType()) {
                view.setPartyMember01Nums(view.getPartyMember01Nums() + 1);
            } else if (PartyMember.TYPE_1 == partyMember.getType()) {
                view.setPartyMember02Nums(view.getPartyMember02Nums() + 1);
            } else if (PartyMember.TYPE_2 == partyMember.getType()) {
                view.setPartyMember03Nums(view.getPartyMember03Nums() + 1);
            } else if (PartyMember.TYPE_3 == partyMember.getType()) {
                view.setPartyMember04Nums(view.getPartyMember04Nums() + 1);
            } else if (PartyMember.TYPE_4 == partyMember.getType()) {
                view.setPartyMember05Nums(view.getPartyMember05Nums() + 1);
            }
        }
        return view;
    }

    /**
     * 
     * <B>方法名称：</B>根据父ID获取组织<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年2月25日
     * @param parentId
     * @return
     */
    public List<ZTreeView> getCcpartyTreeByParentId(String ccpartyId, String parentId) {
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        List<CCParty> ccpartys = new ArrayList<CCParty>();
        ccpartys = ccpartyManager.getCCPartyListByParentId(parentId, null ,CCParty.STATUS_0);
        if (!StringUtils.isEmpty(ccpartyId) && ccpartyId.equals(parentId)) {
            CCParty ccparty = ccpartyManager.getCCPartyById(ccpartyId);
            //当前组织增加进去
            ZTreeView tree = new ZTreeView();
            String id = ccparty.getId();
            tree.setId(id);
            tree.setName(ccparty.getName() + "(" + partyMemberService.getPartymemberNums(id) + ")");
            tree.setpId(ccparty.getParentId());
            tree.setIsParent(this.hasChild(id));
            tree.setAttr1(ccparty.getFullName());
            setCcpartyIcon(tree, ccparty);
            tree.setType(ccparty.getType());
            trees.add(tree);
        }
        if (ccpartys != null && ccpartys.size() > 0) {
            for (int i = 0; i < ccpartys.size(); i++) {
                ZTreeView tree = new ZTreeView();
                String id = ccpartys.get(i).getId();
                tree.setId(id);
                tree.setName(ccpartys.get(i).getName() + "(" + partyMemberService.getPartymemberNums(id) + ")");
                tree.setpId(parentId);
                tree.setIsParent(this.hasChild(id));
                tree.setAttr1(ccpartys.get(i).getFullName());
                setCcpartyIcon(tree, ccpartys.get(i));
                tree.setType(ccpartys.get(i).getType());
                trees.add(tree);
            }
        }
        return trees;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取党组织下的党员总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param ccpartyId
     * @return
     */
    public int getPartymemberNums(String ccpartyId) {
        //获取所有子节点
        List<ZTreeView> trees = this.getTreeCCPartyAndLowerLevel(ccpartyId);
        List<String> statisticsCcpartyIds = new ArrayList<String>();
        if (trees != null && trees.size() > 0) {
            for (ZTreeView tree : trees) {
                statisticsCcpartyIds.add(tree.getId());
            }
        }
        return partyMemberManager.getPartymemberNums(ccpartyId, statisticsCcpartyIds);
    }

    /**
     * 
     * <B>方法名称：</B>是否有子节点<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年2月25日
     * @param parentId
     * @return
     */
    private boolean hasChild(String parentId) {
        List<CCParty> ccpartys = new ArrayList<CCParty>();
        ccpartys = ccpartyManager.getCCPartyListByParentId(parentId, CCParty.STATUS_0);
        if (ccpartys != null && ccpartys.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织下的所有组织ID集合<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年3月26日
     * @param ccpartyId
     * @return
     */
    public List<Object> getSunCcpartyIdsByCcparty(String ccpartyId) {
        List<Object> ids = new ArrayList<Object>();
        List<ZTreeView> trees = this.getTreeCCPartyAndLowerLevel(ccpartyId);
        if (trees != null && trees.size() > 0) {
            for (ZTreeView tree : trees) {
                ids.add(tree.getId());
            }
        }
        return ids;
    }

    /**
     * 
     * <B>方法名称：</B>获取党组织基本信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年5月25日
     * @param ccpartyId
     * @return
     */
    public CcpartyCardInfo getCcpartyInfoForCard(String ccpartyId) {
        CcpartyCardInfo info = new CcpartyCardInfo();
        CCParty ccparty = ccpartyManager.getCCPartyById(ccpartyId);
        //基本信息
        info.setCcparty(ccparty);
        //数据统计
        info.setView(statisticsCcpartyInfosForPartymemberManager(ccpartyId));
        //领导班子成员
        info.setTitleViews(createCcpartyTitles(ccparty));
        return info;
    }

    /**
     * 
     * <B>方法名称：党组织领导组装</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月19日
     * @param ccparty
     * @return
     */
    public List<CCPartyTitleView> createCcpartyTitles(CCParty ccparty) {
        List<CCPartyTitleView> titleViews = new ArrayList<CCPartyTitleView>();
        List<Code> titles = null;
        if (CCParty.TYPE_2.equals(ccparty.getType())) {
            //党组
            titles = codeManager.getPartyOneCodes();
        } else if (CCParty.TYPE_1.equals(ccparty.getType())) {
            //党委
            titles = codeManager.getPartyTwoCodes();
        } else if (CCParty.TYPE_3.equals(ccparty.getType())) {
            //党总支
            titles = codeManager.getPartyThreeCodes();
        } else if (CCParty.TYPE_4.equals(ccparty.getType())) {
            //党支部
            titles = codeManager.getPartyFourCodes();
        }
        if (titles == null) {
            return null;
        }
        Election election = electionManager.getCurrentElection(ccparty.getId()); //最近一届换届选举信息
        for (Code title : titles) {
            if (title != null) {
                CCPartyTitleView titleView = new CCPartyTitleView();
                titleView.setTitle(title);
                if (election != null) {
                    titleView.setElectionMembers(electionMemberManager.getElectionMembersByTitle(title.getCode(), election.getId()));
                }
                titleViews.add(titleView);
            }
        }
        return titleViews;
    }

    /**
     * 
     * <B>方法名称：</B>获取父组织对象<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月14日
     * @param id
     * @return
     */
    public CCParty getParentCcpartyByCcparty(String id) {
        CCParty ccparty = ccpartyManager.getCCPartyFromMc(id);
        if (ccparty == null) {
            return null;
        }
        return ccpartyManager.getCCPartyFromMc(ccparty.getParentId());
    }
}
