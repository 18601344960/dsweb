package org.tpri.sc.controller.obt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.obt.PartyGroupMember;
import org.tpri.sc.service.obt.PartyGroupMemberService;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党小组成员控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年8月1日
 */
@Controller
@RequestMapping("/obt")
public class PartyGroupMemberController extends BaseController {

    @Autowired
    private PartyGroupMemberService partyGroupMemberService;

    /**
     * <B>方法名称：</B>获取党小组下的成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     */
    @RequestMapping("getPartyGroupMemberList")
    @ResponseBody
    public Map<String, Object> getPartyGroupMemberList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getSelectedPartyGroupMember begin");
        String groupId = getString(request, "groupId");
        List<PartyGroupMember> partyGroupMembers = partyGroupMemberService.getPartyGroupMemberList(groupId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", partyGroupMembers);
        logger.debug(this.getClass() + " getPartyGroupMemberList end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取党小组成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     */
    @RequestMapping("getPartyGroupMemberById")
    @ResponseBody
    public Map<String, Object> getPartyGroupMemberById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getPartyGroupMemberById begin");
        String memberId = getString(request, "memberId");
        PartyGroupMember partyGroupMember = partyGroupMemberService.getPartyGroupMemberById(memberId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", partyGroupMember);
        logger.debug(this.getClass() + " getPartyGroupMemberById end");
        return ret;
    }

    /**
     * <B>方法名称：</B>新增党小组成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addPartyGroupMember")
    @ResponseBody
    public Map<String, Object> addPartyGroupMember(HttpServletRequest request) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        String groupId = getString(request, "groupId");
        param.put("groupId", groupId);
        param.put("userIds", getString(request, "userIds"));
        param.put("type", getInt(request, "type", 0));
        param.put("sequence", getInt(request, "sequence", 100));
        partyGroupMemberService.addPartyGroupMember(param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("memberCount", partyGroupMemberService.getPartyGroupMemberTotalByGroupId(groupId));
        return ret;
    }

    /**
     * <B>方法名称：</B>移除党小组成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月1日
     * @param request
     * @return
     */
    @RequestMapping("removePartyGroupMember")
    @ResponseBody
    public Map<String, Object> removePartyGroupMember(HttpServletRequest request) {
        String memberId = getString(request, "memberId");
        PartyGroupMember partyGroupMember = partyGroupMemberService.getPartyGroupMemberById(memberId);
        Map<String, Object> ret = new HashMap<String, Object>();
        if (partyGroupMember != null) {
            boolean result = partyGroupMemberService.removePartyGroupMember(partyGroupMember);
            ret.put("success", result);
        } else {
            ret.put("success", false);
        }
        ret.put("memberCount", partyGroupMemberService.getPartyGroupMemberTotalByGroupId(partyGroupMember.getGroupId()));
        return ret;
    }

    /**
     * <B>方法名称：</B>设为组长<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("setGroupLeader")
    @ResponseBody
    public Map<String, Object> setGroupLeader(HttpServletRequest request) {
        String memberId = getString(request, "memberId");
        boolean result = partyGroupMemberService.setGroupLeader(memberId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        return ret;
    }

    /**
     * <B>方法名称：</B>设为副组长<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("setGroupDeputyLeader")
    @ResponseBody
    public Map<String, Object> setGroupDeputyLeader(HttpServletRequest request) {
        String memberId = getString(request, "memberId");
        boolean result = partyGroupMemberService.setGroupDeputyLeader(memberId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        return ret;
    }

    /**
     * <B>方法名称：</B>设为成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("setGroupMember")
    @ResponseBody
    public Map<String, Object> setGroupMember(HttpServletRequest request) {
        String memberId = getString(request, "memberId");
        boolean result = partyGroupMemberService.setGroupMember(memberId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        return ret;
    }
}
