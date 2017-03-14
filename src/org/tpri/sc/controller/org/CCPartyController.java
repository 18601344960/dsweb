package org.tpri.sc.controller.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.service.obt.PartyMemberService;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.service.uam.UserService;
import org.tpri.sc.view.ZTreeView;
import org.tpri.sc.view.obt.PartyMemberStatisticsView;
import org.tpri.sc.view.org.CcpartyCardInfo;

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：党组织</B><BR>
 * <B>中文类名：党组织控制器</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月18日
 */
@Controller
@RequestMapping("/org")
public class CCPartyController extends BaseController {

    @Autowired
    private CCpartyService cCpartyService;
    @Autowired
    private PartyMemberService partyMemberService;
    @Autowired
    private UserService userService;

    /**
     * 
     * @Description: 获取所有党组织树
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:44:08
     * @param request
     * @return
     */
    @RequestMapping("getTreeCCPartyAlls")
    @ResponseBody
    public Map<String, Object> getTreeCCPartyAlls(HttpServletRequest request) {
        logger.debug(this.getClass() + " getTreeCCPartyAlls begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        trees = cCpartyService.getTreeCCPartyAlls();
        ret.put("items", trees);
        logger.debug(this.getClass() + " getTreeCCPartyAlls end");
        return ret;
    }

    /**
     * 
     * @Description: 获取组织树 查询当前组织和所有下级组织 注1、不包含平级组织 2、包含当前组织下的所有子节点，包括子节点和孙子节点
     *               3、如果传入组织的节点为空 将返回null
     * @author: 赵子靖
     * @since: 2015年9月9日 下午5:46:32
     * @param request
     * @return
     */
    @RequestMapping("getTreeCCPartyAndLowerLevel")
    @ResponseBody
    public Map<String, Object> getTreeCCPartyAndLowerLevel(HttpServletRequest request) {
        logger.debug(this.getClass() + " getTreeCCPartyAndLowerLevel begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ZTreeView> tree = new ArrayList<ZTreeView>();
        String ccpartyId = getString(request, "ccpartyId");
        if (!StringUtils.isEmpty(ccpartyId)) {
            tree = cCpartyService.getTreeCCPartyAndLowerLevel(ccpartyId);
        }
        ret.put("items", tree);
        logger.debug(this.getClass() + " getTreeCCPartyAndLowerLevel end");
        return ret;
    }

    /**
     * 
     * @Description: 获取当前组织和下级组织树(不包含平级和孙子节点)
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:15:12
     * @param request
     * @return
     */
    @RequestMapping("getTreeCCPartyOneSelfAndSon")
    @ResponseBody
    public Map<String, Object> getTreeCCPartyOneSelfAndSon(HttpServletRequest request) {
        logger.debug(this.getClass() + " getTreeCCPartyOneSelfAndSon begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        String ccpartyId = getString(request, "ccpartyId");
        trees = cCpartyService.getTreeCCPartyOneSelfAndSon(ccpartyId);
        ret.put("items", trees);
        logger.debug(this.getClass() + " getTreeCCPartyOneSelfAndSon end");
        return ret;
    }

    /**
     * 
     * @Description: 获取当前组织、平级组织和子节点树(不含孙子节点)
     * @author: 赵子靖
     * @since: 2015年9月10日 上午9:23:06
     * @param request
     * @return
     */
    @RequestMapping("getTreeCCPartyOneselfAndEqualLevelAndSon")
    @ResponseBody
    public Map<String, Object> getTreeCCPartyOneselfAndEqualLevelAndSon(HttpServletRequest request) {
        logger.debug(this.getClass() + " getTreeCCPartyOneselfAndEqualLevelAndSon begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        String ccpartyId = getString(request, "ccpartyId");
        if (!StringUtils.isEmpty(ccpartyId)) {
            trees = cCpartyService.getTreeCCPartyOneselfAndEqualLevelAndSon(ccpartyId);
        }
        ret.put("items", trees);
        logger.debug(this.getClass() + " getTreeCCPartyOneselfAndEqualLevelAndSon end");
        return ret;
    }

    /**
     * 
     * @Description: 获取当前组织的子节点树 (不包含当前组织、平级组织和孙子节点)
     * @author: 赵子靖
     * @since: 2015年9月10日 上午11:23:02
     * @param request
     * @return
     */
    @RequestMapping("getTreeCCPartySon")
    @ResponseBody
    public Map<String, Object> getTreeCCPartySon(HttpServletRequest request) {
        logger.debug(this.getClass() + " getTreeCCPartySon begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        String ccpartyId = getString(request, "parentId");
        if (!StringUtils.isEmpty(ccpartyId)) {
            trees = cCpartyService.getTreeCCPartySon(ccpartyId);
        }
        ret.put("items", trees);
        logger.debug(this.getClass() + " getTreeCCPartySon end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>根据父ID获取组织树<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年2月25日    
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyTreeByParentId")
    @ResponseBody
    public List<ZTreeView> getCcpartyTreeByParentId(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyTreeByParentId begin");
        String ccpartyId = getString(request, "ccpartyId");
        String parentId = getString(request, "parentId");
        List<ZTreeView> list = cCpartyService.getCcpartyTreeByParentId(ccpartyId,parentId);
        logger.debug(this.getClass() + " getCcpartyTreeByParentId end");
        return list;
    }

    /**
     * 
     * @Description: 删除党组织
     * @param request
     * @return
     */
    @RequestMapping("delCCParty")
    @ResponseBody
    public Map<String, Object> delCCParty(HttpServletRequest request) {
        logger.debug(this.getClass() + " delCCParty begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        ret = cCpartyService.deleteCCParty(ccpartyId);
        logger.debug(this.getClass() + " delCCParty end");
        return ret;
    }

    /**
     * 获取党组织
     */
    @RequestMapping("getCCParty")
    @ResponseBody
    public Map<String, Object> getCCParty(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCCParty begin");
        Map<String, Object> ret = new HashMap<String, Object>();

        String parentId = getString(request, "parentId");
        CCParty ccparty = cCpartyService.getCCParty(parentId);
        CCParty parentCcparty = cCpartyService.getCCParty(ccparty.getParentId());
        ret.put("item", ccparty);
        ret.put("parent", parentCcparty);
        logger.debug(this.getClass() + " getCCParty begin");
        return ret;
    }

    /**
     * 
     * @Description: 根据ID获取党组织详细信息
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyById")
    @ResponseBody
    public Map<String, Object> getCcpartyById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyById begin");
        Map<String, Object> ret = new HashMap<String, Object>();

        String ccpartyId = getString(request, "ccpartyId");
        CCParty ccparty = cCpartyService.getCCParty(ccpartyId);
        CCParty parentCcparty = cCpartyService.getCCParty(ccparty.getParentId());
        ret.put("item", ccparty);
        ret.put("parent", parentCcparty);
        logger.debug(this.getClass() + " getCcpartyById begin");
        return ret;
    }

    /**
     * 
     * @Description: 新增或修改党组织保存
     * @param request
     * @return
     */
    @RequestMapping("saveOrUpdateCcparty")
    @ResponseBody
    public Map<String, Object> saveOrUpdateCcparty(HttpServletRequest request) {
        logger.debug(this.getClass() + " saveOrUpdateCcparty begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        JSONObject objs = new JSONObject();
        objs.put("myselfId", getString(request, "myselfId"));
        objs.put("name", getString(request, "name"));
        objs.put("fullName", getString(request, "fullName"));
        objs.put("type", getString(request, "type"));
        objs.put("documentNo", getString(request, "documentNo"));
        objs.put("parentCcpartyId", getString(request, "parentCcpartyId"));
        objs.put("description", getString(request, "description"));
        objs.put("partyType", getInt(request, "partyType"));
        objs.put("sequence", getInt(request, "sequence"));

        ret = cCpartyService.saveOrUpdateCcparty(loadUserMc(request), objs, id);
        logger.debug(this.getClass() + " saveOrUpdateCcparty begin");
        return ret;
    }

    /**
     * 
     * @Description:验证党组织ID是否被占用
     * @param request
     * @return
     */
    @RequestMapping("checkCcpartyId")
    @ResponseBody
    public Map<String, Object> checkCcpartyId(HttpServletRequest request) {
        logger.debug(this.getClass() + " checkCcpartyId begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String checkId = getString(request, "checkId");
        ret = cCpartyService.checkCcpartyId(checkId);
        logger.debug(this.getClass() + " checkCcpartyId begin");
        return ret;
    }


    /**
     * 
     * <B>方法名称：</B>根据父ID获取直接下级党组织列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年10月21日
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyListByParentId")
    @ResponseBody
    public Map<String, Object> getCcpartyListByParentId(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyListByParentId begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String parentId = getString(request, "parentId");
        List<CCParty> ccparties = cCpartyService.getCcpartyListByParentId(parentId);
        ret.put("rows", ccparties);
        logger.debug(this.getClass() + " getCcpartyListByParentId begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>统计组织信息 党员管理模块<BR>
     * <B>概要说明：</B>统计下属组织数、党员人数及各类型党员数<BR>
     * 
     * @author 赵子靖
     * @since 2016年1月13日
     * @param request
     * @return
     */
    @RequestMapping("statisticsCcpartyInfosForPartymemberManager")
    @ResponseBody
    public Map<String, Object> statisticsCcpartyInfosForPartymemberManager(HttpServletRequest request) {
        logger.debug(this.getClass() + " statisticsCcpartyInfosForPartymemberManager begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        PartyMemberStatisticsView view = cCpartyService.statisticsCcpartyInfosForPartymemberManager(ccpartyId);
        ret.put("item", view);
        logger.debug(this.getClass() + " statisticsCcpartyInfosForPartymemberManager begin");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取党组织基本信息<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年5月25日    
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyInfoForCard")
    @ResponseBody
    public Map<String, Object> getCcpartyInfoForCard(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyInfoForCard begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        CcpartyCardInfo info = cCpartyService.getCcpartyInfoForCard(ccpartyId);
        ret.put("item", info);
        logger.debug(this.getClass() + " getCcpartyInfoForCard end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取下级组织<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年6月14日    
     * @param request
     * @return
     */
    @RequestMapping("getTreeCCPartySonForCard")
    @ResponseBody
    public Map<String, Object> getTreeCCPartySonForCard(HttpServletRequest request) {
        logger.debug(this.getClass() + " getTreeCCPartySonForCard begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        String ccpartyId = getString(request, "parentId");
        trees = cCpartyService.getTreeCCPartySon(ccpartyId);
        ret.put("items", trees);
        ret.put("parentCcparty", cCpartyService.getParentCcpartyByCcparty(ccpartyId));
        logger.debug(this.getClass() + " getTreeCCPartySonForCard end");
        return ret;
    }
}
