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
import org.tpri.sc.entity.obt.ElectionMember;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.obt.ElectionMemberService;

/**
 * 
 * <B>系统名称：</B>换届选举领导班子成员控制器<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年10月23日
 */
@Controller
@RequestMapping("/obt")
public class ElectionMemberController extends BaseController {

    @Autowired
    private ElectionMemberService electionMemberService;

    /**
     * <B>方法名称：获取换届选举领导班子成员</B><BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年9月17日
     * @param request
     * @return
     */
    @RequestMapping("getElectionMemberList")
    @ResponseBody
    public Map<String, Object> getElectionMemberList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getSelectedElectionMember begin");
        String electionId = getString(request, "electionId");
        List<ElectionMember> electionMembers = electionMemberService.getElectionMemberList(electionId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", electionMembers);
        logger.debug(this.getClass() + " getElectionMemberList end");
        return ret;
    }

    /**
     * 获取领导班子成员
     */
    @RequestMapping("getElectionMemberById")
    @ResponseBody
    public Map<String, Object> getElectionMemberById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getElectionMemberById begin");
        String memberId = getString(request, "memberId");
        ElectionMember electionMember = electionMemberService.getElectionMemberById(memberId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", electionMember);
        logger.debug(this.getClass() + " getElectionMemberById end");
        return ret;
    }

    /**
     * <B>方法名称：</B>新增领导班子成员<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月2日 	
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addElectionMember")
    @ResponseBody
    public Map<String, Object> addElectionMember(HttpServletRequest request) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("electionId", getString(request, "electionId"));
        param.put("userId", getString(request, "userId"));
        param.put("userName", getString(request, "userName"));
        param.put("gender", getInt(request, "gender"));
        param.put("birthDay", getDate(request, "birthDay"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        
        electionMemberService.addElectionMember(param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        return ret;
    }

    /**
     * <B>方法名称：</B>修改领导班子成员<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月2日 	
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("updateElectionMember")
    @ResponseBody
    public Map<String, Object> updateElectionMember(HttpServletRequest request) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", getString(request, "memberId"));
        param.put("startTime", getDate(request, "startTime"));
        param.put("endTime", getDate(request, "endTime"));
        param.put("remark", getString(request, "remark"));
        param.put("sequence", getInt(request, "sequence", 10000));
        UserMc user = loadUserMc(request);
        electionMemberService.updateElectionMember(user, param);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        return ret;
    }

    /**
     * <B>方法名称：</B>移除领导班子成员<BR>
     * <B>概要说明：</B><BR>
     * @author 易文俊
     * @since 2016年7月2日 	
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("removeElectionMember")
    @ResponseBody
    public Map<String, Object> removeElectionMember(HttpServletRequest request) throws Exception {
        String memberId = getString(request, "memberId");
        electionMemberService.removeElectionMember(memberId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织最终换届选举<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月13日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("getLastElectionMemberListByCcparty")
    @ResponseBody
    public Map<String, Object> getLastElectionMemberListByCcparty(HttpServletRequest request) throws Exception {
        String ccpartyId = getString(request, "ccpartyId");
        Map<String, Object> ret = new HashMap<String, Object>();
        List<ElectionMember> members = electionMemberService.getElectionMembersByCcparty(ccpartyId);
        ret.put("rows", members);
        return ret;
    }

}
