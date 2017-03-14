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
import org.tpri.sc.entity.obt.ElectionMemberTitle;
import org.tpri.sc.service.obt.ElectionMemberTitleService;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举领导班子成员党内职务控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月2日
 */
@Controller
@RequestMapping("/obt")
public class ElectionMemberTitleController extends BaseController {

    @Autowired
    private ElectionMemberTitleService memberTitleService;

    /**
     * <B>方法名称：</B>获取某班子成员的党内职务列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param request
     * @return
     */
    @RequestMapping("getElectionMemberTitleList")
    @ResponseBody
    public Map<String, Object> getElectionMemberTitleList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getElectionMemberTitleList begin");
        String memberId = getString(request, "memberId");
        List<ElectionMemberTitle> memberTitles = memberTitleService.getElectionMemberTitleList(memberId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", memberTitles);
        logger.debug(this.getClass() + " getElectionMemberTitleList end");
        return ret;
    }

    /**
     * <B>方法名称：</B>添加领导班子成员党内职务<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addElectionMemberTitle")
    @ResponseBody
    public Map<String, Object> addElectionMemberTitle(HttpServletRequest request) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", getString(request, "memberId"));
        param.put("partyTitleId", getString(request, "partyTitleId"));
        param.put("sequence", getInt(request, "sequence", 10000));
        memberTitleService.addElectionMemberTitle(param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        return ret;
    }

    /**
     * <B>方法名称：</B>删除领导班子成员党内职务<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("deleteElectionMemberTitle")
    @ResponseBody
    public Map<String, Object> deleteElectionMemberTitle(HttpServletRequest request) throws Exception {
        String id = getString(request, "id");
        boolean result = memberTitleService.deleteElectionMemberTitle(id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        return ret;
    }

}
