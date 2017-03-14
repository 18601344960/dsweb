package org.tpri.sc.controller.obt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.obt.PartyMemberService;
import org.tpri.sc.view.ZTreeView;

/**
 * 
 * <B>系统名称：</B>党建系统<BR>
 * <B>模块名称：</B>党员管理<BR>
 * <B>中文类名：</B>党员管理控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月21日
 */
@Controller
@RequestMapping("/org")
public class PartyMemberController extends BaseController {
    @Autowired
    private PartyMemberService partyMemberService;

    /**
     * <B>方法名称：</B>获取所有党员（预备党员和正式党员），如果ccpartyId有值则获取某组织的党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月12日
     * @param request
     * @return
     */
    @RequestMapping("getPartyMemberList")
    @ResponseBody
    public Map<String, Object> getPartyMemberList(HttpServletRequest request) {
        logger.debug("PartyMemberController getPartyMemberList begin");

        String ccpartyId = getString(request, "ccpartyId");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");

        List<PartyMember> list = partyMemberService.getPartyMemberList(ccpartyId, offset, limit);
        Integer total = partyMemberService.getPartyMemberTotal(ccpartyId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        ret.put("total", total);

        logger.debug("PartyMemberController getPartyMemberList end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>加载党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月21日
     * @param request
     * @return
     */
    @RequestMapping("loadPartyMember")
    @ResponseBody
    public Map<String, Object> loadPartyMember(HttpServletRequest request) {
        logger.debug("PartyMemberController loadPartyMember begin");

        Integer start = getInteger(request, "start");
        Integer limit = getInteger(request, "limit");

        List<PartyMember> list = partyMemberService.loadPartyMember(start, limit);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", list);
        ret.put("totalCount", list.size());

        logger.debug("PartyMemberController loadPartyMember end");
        return ret;
    }

    /**
     * 加载某党组织的党员列表
     */
    @RequestMapping("loadPartyMemberByCcpartyId")
    @ResponseBody
    public Map<String, Object> loadPartyMemberByCcpartyId(HttpServletRequest request) {
        logger.debug("PartyMemberController loadPartyMember begin");
        String ccpartyId = getString(request, "ccpartyId");

        Integer start = getInteger(request, "start");
        Integer limit = getInteger(request, "limit");

        List<PartyMember> list = partyMemberService.loadPartyMemberByCcpartyId(start, limit, ccpartyId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", list);
        ret.put("totalCount", list.size());

        logger.debug("PartyMemberController loadPartyMember end");
        return ret;
    }

    @RequestMapping("loadTreePartyMemberByCcpartyId")
    @ResponseBody
    public Map<String, Object> loadTreePartyMemberByCcpartyId(HttpServletRequest request) {
        logger.debug("PartyMemberController loadPartyMember begin");
        String ccpartyId = getString(request, "ccpartyId");
        List<ZTreeView> tree = new ArrayList<ZTreeView>();
        Integer start = getInteger(request, "start");
        Integer limit = getInteger(request, "limit");
        if (!StringUtils.isEmpty(ccpartyId)) {
            tree = partyMemberService.loadTreePartyMemberByCcpartyId(start, limit, ccpartyId);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", tree);
        logger.debug("PartyMemberController loadPartyMember end");
        return ret;
    }

    /**
     * 编辑党员用户
     */
    @RequestMapping("editPartyMember")
    @ResponseBody
    public Map<String, Object> editPartyMember(HttpServletRequest request) {
        logger.debug("PartyMemberController editPartyMember begin");
        String id = getString(request, "id");
        String name = getString(request, "name");
        String partyNo = getString(request, "partyNo");
        int gender = getInt(request, "gender");
        String idNumber = getString(request, "idNumber");
        String nation = getString(request, "nation");
        String occupation = getString(request, "occupation");
        String education = getString(request, "education");
        String jobTitle = getString(request, "jobTitle");
        String birthPlace = getString(request, "birthPlace");
        String officePhone = getString(request, "officePhone");
        String mobile = getString(request, "mobile");
        String email = getString(request, "email");
        String address = getString(request, "address");
        String ccpartyId = getString(request, "ccpartyId");
        String joinTime = getString(request, "joinTime");
        String type = getString(request, "type");
        String introducer = getString(request, "introducer");
        String picture = getString(request, "picture");
        String status = getString(request, "status");
        String description = getString(request, "description");
        String joinType = getString(request, "joinType");
        String joinCurrentTime = getString(request, "joinCurrentTime");
        String joinActivity = getString(request, "joinActivity");
        String fee = getString(request, "fee");
        String isDelegate = getString(request, "isDelegate");
        String isDestitution = getString(request, "isDestitution");

        try {
            partyMemberService.editPartyMember(loadUserMc(request), id, name, partyNo, gender, idNumber, nation, occupation, education, jobTitle, birthPlace, officePhone, mobile, email, address,
                    ccpartyId, joinTime, type, introducer, picture, status, description, joinType, joinCurrentTime, joinActivity, fee, isDelegate, isDestitution);
        } catch (ParseException e) {
            logger.error("editPartyMember 保存异常" + e.getMessage());
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug("PartyMemberController editPartyMember end");
        return ret;
    }

    /**
     * 
     * @Description: 党建项目的 党员保存
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping("editPartyMemberForDjweb")
    @ResponseBody
    public Map<String, Object> editPartyMemberForDjweb(HttpServletRequest request) throws ParseException {
        logger.debug(this.getClass() + "editPartyMemberForDjweb begin");
        JSONObject objs = new JSONObject();
        objs.put("id", getString(request, "id"));
        objs.put("type", getInt(request, "type"));
        objs.put("partyNo", getString(request, "partyNo"));
        objs.put("isDelegate", getInt(request, "repre"));
        objs.put("isManager", getInt(request, "worker"));
        objs.put("isDestitution", getInt(request, "harder"));
        objs.put("joinTime", getString(request, "joinTime"));
        objs.put("joinCurrentTime", getString(request, "joinCurrentTime"));
        objs.put("joinType", getInt(request, "joinType"));
        objs.put("ccpartyId", getString(request, "ccpartyId"));
        objs.put("introducer1", getString(request, "introducer1"));
        objs.put("introducer2", getString(request, "introducer2"));
        objs.put("passTime", getString(request, "passTime"));
        objs.put("auditTime", getString(request, "auditTime"));
        objs.put("fee", getString(request, "fee"));
        objs.put("joinActivity", getInt(request, "live"));
        objs.put("applyTime", getString(request, "applyTime"));
        objs.put("activistTime", getString(request, "activistTime"));
        objs.put("targetTime", getString(request, "targetTime"));
        objs.put("trainer", getString(request, "trainer"));

        Map<String, Object> ret = new HashMap<String, Object>();
        if (partyMemberService.saveOrUpdatePartyMemberForDjweb(loadUserMc(request), objs)) {
            ret.put("success", true);
            ret.put("msg", "保存成功");
        } else {
            ret.put("success", false);
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + "editPartyMemberForDjweb end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>假删除党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月23日
     * @param request
     * @return
     */
    @RequestMapping("fakeDeletePartymember")
    @ResponseBody
    public Map<String, Object> fakeDeletePartymember(HttpServletRequest request) {
        logger.debug(this.getClass() + " fakeDeletePartymember begin");
        String id = getString(request, "id");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = partyMemberService.fakeDeletePartymember(loadUserMc(request), id);
        logger.debug(this.getClass() + " fakeDeletePartymember end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>恢复党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月23日
     * @param request
     * @return
     */
    @RequestMapping("recoverPartyMember")
    @ResponseBody
    public Map<String, Object> recoverPartyMember(HttpServletRequest request) {
        logger.debug(this.getClass() + " recoverPartyMember begin");
        String id = getString(request, "id");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = partyMemberService.recoverPartyMember(loadUserMc(request), id);
        logger.debug(this.getClass() + " recoverPartyMember end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>永久删除党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月23日
     * @param request
     * @return
     */
    @RequestMapping("realDeletePartymember")
    @ResponseBody
    public Map<String, Object> realDeletePartymember(HttpServletRequest request) {
        logger.debug(this.getClass() + " realDeletePartymember begin");
        String id = getString(request, "id");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = partyMemberService.realDeletePartymember(loadUserMc(request), id);
        logger.debug(this.getClass() + " realDeletePartymember end");
        return ret;
    }

    /**
     * 
     * @Description: 根据用户ID加载党员信息
     * @param request
     * @return
     */
    @RequestMapping("loadPartyMemberInfoByUserId")
    @ResponseBody
    public Map<String, Object> loadPartyMemberInfoByUserId(HttpServletRequest request) {
        logger.debug(this.getClass() + " loadPartyMemberInfoByUserId begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String userId = getString(request, "userId");
        if (StringUtils.isEmpty(userId)) {
            ret.put("success", false);
            ret.put("msg", "会员ID为空！");
        } else {
            PartyMember member = partyMemberService.getPartyMemberByUserId(userId);
            ret.put("success", true);
            ret.put("item", member);
        }
        logger.debug(this.getClass() + " loadPartyMemberInfoByUserId end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：新增或保存党员和用户统一处理</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月21日
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping("saveOrUpdatePartymemberAndUser")
    @ResponseBody
    public Map<String, Object> saveOrUpdatePartymemberAndUser(HttpServletRequest request) throws ParseException {
        Map<String, Object> ret = new HashMap<String, Object>();
        logger.debug(this.getClass() + " saveOrUpdatePartymemberAndUser begin");
        String ccpartyId = getString(request, "ccpartyId");
        String paramters = getString(request, "paramters");
        UserMc user = loadUserMc(request);
        ret = partyMemberService.saveOrUpdatePartymemberAndUser(user, paramters, ccpartyId);
        logger.debug(this.getClass() + " saveOrUpdatePartymemberAndUser end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取党组织下的党员数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月24日
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping("getPartymemberTotalByCcparty")
    @ResponseBody
    public Map<String, Object> getPartymemberTotalByCcparty(HttpServletRequest request) throws ParseException {
        Map<String, Object> ret = new HashMap<String, Object>();
        logger.debug(this.getClass() + " getPartymemberTotalByCcparty begin");
        String ccpartyId = getString(request, "ccpartyId");
        Integer total = partyMemberService.getPartymemberTotalByCcparty(ccpartyId);
        ret.put("total", total);
        logger.debug(this.getClass() + " getPartymemberTotalByCcparty end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>保存党员信息<BR>
     * <B>概要说明：</B>个人资料<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月30日
     * @param request
     * @return
     */
    @RequestMapping("updatePartymemberInfoForMyInfo")
    @ResponseBody
    public Map<String, Object> updatePartymemberInfoForMyInfo(HttpServletRequest request) {
        logger.debug(this.getClass() + " updatePartymemberInfoForMyInfo begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("userId", getString(request, "userId"));
        objs.put("joinCurrentTime", getString(request, "joinCurrentTime"));
        objs.put("joinType", getInt(request, "joinType"));
        objs.put("activistTime", getString(request, "activistTime"));
        objs.put("applyTime", getString(request, "applyTime"));
        objs.put("targetTime", getString(request, "targetTime"));
        objs.put("trainer", getString(request, "trainer"));
        objs.put("passTime", getString(request, "passTime"));
        objs.put("auditTime", getString(request, "auditTime"));
        objs.put("joinTime", getString(request, "joinTime"));
        objs.put("fee", getString(request, "fee"));
        objs.put("introducer", getString(request, "introducer"));
        objs.put("joinActivity", getInt(request, "joinActivity"));
        ret = partyMemberService.updatePartymemberInfoForMyInfo(objs);
        logger.debug(this.getClass() + " updatePartymemberInfoForMyInfo end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取某组织及其下属组织的所有成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月13日
     * @param request
     * @return
     */
    @RequestMapping("getAllPartyMembersByCcpartyId")
    @ResponseBody
    public Map<String, Object> getAllPartyMembersByCcpartyId(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAllPartyMembersByCcpartyId begin");
        String ccpartyId = getString(request, "ccpartyId");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String search = getString(request, "search");
        String searchCcpartyId = getString(request, "searchCcpartyId");
        Map<String, Object> ret = new HashMap<String, Object>();

        List<User> users = partyMemberService.getAllPartyMembersByCcpartyId(search, offset, limit, ccpartyId, searchCcpartyId);
        Integer total = partyMemberService.getAllPartyMembersByCcpartyIdTotal(search, ccpartyId, searchCcpartyId);
        ret.put("rows", users);
        ret.put("total", total);
        logger.debug(this.getClass() + " getAllPartyMembersByCcpartyId end");
        return ret;
    }

    /**
     * <B>方法名称：</B>加载某个阶段的发展党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月30日
     * @param request
     * @return
     */
    @RequestMapping("getPartyMemberListByType")
    @ResponseBody
    public Map<String, Object> getPartyMemberListByType(HttpServletRequest request) {
        logger.debug("PartyMemberController getPartyMemberListByType begin");

        String ccpartyId = getString(request, "ccpartyId");
        String developmentId = getString(request, "developmentId");
        Integer type = getInteger(request, "type");
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");

        List<PartyMember> list = partyMemberService.getPartyMemberListByType(ccpartyId, type, developmentId, start, limit);
        Integer total = partyMemberService.getPartyMemberTotalByType(ccpartyId, type, developmentId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        ret.put("total", total);

        logger.debug("PartyMemberController getPartyMemberListByType end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>处理从党员发展计划保存党员和用户<BR>
     * <B>概要说明：</B>保存申请人、积极分子、发展对象、预备党员、正式党员统一入口<BR>
     * 
     * @author 赵子靖
     * @since 2015年12月4日
     * @param request
     * @return
     */
    @RequestMapping("saveOrUpdatePartymemberForDevelopmentPlan")
    @ResponseBody
    public Map<String, Object> saveOrUpdatePartymemberForDevelopmentPlan(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();
        logger.debug(this.getClass() + " saveOrUpdatePartymemberForDevelopmentPlan begin");
        String paramters = getString(request, "paramters");
        UserMc user = loadUserMc(request);
        ret = partyMemberService.saveOrUpdatePartymemberForDevelopmentPlan(user, paramters);
        logger.debug(this.getClass() + " saveOrUpdatePartymemberForDevelopmentPlan end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某发展阶段下的党员信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param request
     * @return
     */
    @RequestMapping("getPartymemberDevelopmentProcedureInfo")
    @ResponseBody
    public Map<String, Object> getPartymemberDevelopmentProcedureInfo(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();
        logger.debug(this.getClass() + " getPartymemberDevelopmentProcedureInfo begin");
        String partymemberId = getString(request, "partymemberId");
        String processId = getString(request, "processId");
        ret = partyMemberService.getPartymemberDevelopmentProcedureInfo(partymemberId, processId);
        logger.debug(this.getClass() + " getPartymemberDevelopmentProcedureInfo end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>党员发展详细流程保存<BR>
     * <B>概要说明：</B>每个阶段的详细步骤保存入口<BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param request
     * @return
     */
    @RequestMapping("saveOrUpdatePartymemberDevelopmentStep")
    @ResponseBody
    public Map<String, Object> saveOrUpdatePartymemberDevelopmentStep(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();
        logger.debug(this.getClass() + " saveOrUpdatePartymemberDevelopmentStep begin");
        String paramters = getString(request, "paramters");
        UserMc user = loadUserMc(request);
        ret = partyMemberService.saveOrUpdatePartymemberDevelopmentStep(user, paramters);
        logger.debug(this.getClass() + " saveOrUpdatePartymemberDevelopmentStep end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某党员的发展阶段信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月2日
     * @param request
     * @return
     */
    @RequestMapping("getPartymemberDevelopmentProcedureByUser")
    @ResponseBody
    public Map<String, Object> getPartymemberDevelopmentProcedureByUser(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();
        logger.debug(this.getClass() + " getPartymemberDevelopmentProcedureByUser begin");
        String userId = getString(request, "userId");
        ret = partyMemberService.getPartymemberDevelopmentProcedureByUser(userId);
        logger.debug(this.getClass() + " getPartymemberDevelopmentProcedureByUser end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取抓出或者退档党员列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月23日
     * @param request
     * @return
     */
    @RequestMapping("getExportPartyMembers")
    @ResponseBody
    public Map<String, Object> getExportPartyMembers(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();
        logger.debug(this.getClass() + " getExportPartyMembers begin");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String search = getString(request, "search");
        String ccpartyId = getString(request, "ccpartyId");
        List<User> exportUsers = partyMemberService.getExportPartyMembers(offset, limit, search, ccpartyId);
        Integer total = partyMemberService.getExportPartyMembersTotal(search, ccpartyId);
        ret.put("rows", exportUsers);
        ret.put("total", total);
        logger.debug(this.getClass() + " getExportPartyMembers end");
        return ret;
    }
}
