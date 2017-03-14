package org.tpri.sc.controller.uam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.service.uam.PartyWorkerService;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党务人员控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年1月8日
 */
@Controller
@RequestMapping("/uam")
public class PartyWorkerController extends BaseController {

    @Autowired
    private PartyWorkerService partyWorkerService;

    /**
     * 
     * @Description: 获取党务人员列表
     * @param request
     * @return
     */
    @RequestMapping("getPartyWorkers")
    @ResponseBody
    public Map<String, Object> getPartyWorkers(HttpServletRequest request) {
        logger.debug(this.getClass() + " getPartyWorkers begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        String searchCcpartyIds = getString(request, "searchCcpartyIds"); 
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String search = getString(request, "search");
        List<User> partyWorkers = partyWorkerService.getPartyWorkersByCcparty(search, offset, limit, ccpartyId,searchCcpartyIds);
        Integer total = partyWorkerService.getPartyWorkersTotalByCcparty(search, ccpartyId,searchCcpartyIds);
        ret.put("rows", partyWorkers);
        ret.put("total", total);
        logger.debug(this.getClass() + " getPartyWorkers end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>报送根据组织ID获取党务工作者<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年3月24日 	
     * @param request
     * @return
     */
    @RequestMapping("getPartyWorkersByCcparty")
    @ResponseBody
    public Map<String, Object> getPartyWorkersByCcparty(HttpServletRequest request) {
        logger.debug(this.getClass() + " getPartyWorkersByCcparty begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        List<User> partyWorkers = partyWorkerService.getPartyWorkersByCcparty(ccpartyId);
        ret.put("rows", partyWorkers);
        logger.debug(this.getClass() + " getPartyWorkersByCcparty end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>新增党务人员<BR>
     * <B>概要说明：</B>增加党务人员<BR>
     * 
     * @author 赵子靖
     * @since 2016年1月11日
     * @param request
     * @return
     */
    @RequestMapping("addPartyWorker")
    @ResponseBody
    public Map<String, Object> addPartyWorker(HttpServletRequest request) {
        logger.debug(this.getClass() + " addPartyWorker begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("userId", getString(request, "userId"));
        objs.put("partyWorkerName", getString(request, "partyWorkerName"));
        objs.put("sysUserId", getString(request, "sysUserId"));
        ret = partyWorkerService.addPartyWorker(objs);
        logger.debug(this.getClass() + " addPartyWorker end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>修改党务人员<BR>
     * <B>概要说明：</B>修改党务人员<BR>
     * 
     * @author 赵子靖
     * @since 2016年1月11日
     * @param request
     * @return
     */
    @RequestMapping("updatePartyWorker")
    @ResponseBody
    public Map<String, Object> updatePartyWorker(HttpServletRequest request) {
        logger.debug(this.getClass() + " updatePartyWorker begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret = partyWorkerService.updatePartyWorker(loadUserMc(request), getString(request, "paramters"));
        logger.debug(this.getClass() + " updatePartyWorker end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取党务工作<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年1月11日
     * @param request
     * @return
     */
    @RequestMapping("getPartyWorkerById")
    @ResponseBody
    public Map<String, Object> getPartyWorkerById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getPartyWorkerById begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = partyWorkerService.getPartyWorkerById(getString(request, "id"));
        ret.put("item", user);
        logger.debug(this.getClass() + " getPartyWorkerById end");
        return ret;
    }

//    /**
//     * 
//     * <B>方法名称：</B>权限修改<BR>
//     * <B>概要说明：</B><BR>
//     * 
//     * @author 赵子靖
//     * @since 2016年1月11日
//     * @param request
//     * @return
//     */
//    @RequestMapping("updatePartyWorkerRole")
//    @ResponseBody
//    public Map<String, Object> updatePartyWorkerRole(HttpServletRequest request) {
//        logger.debug(this.getClass() + " updatePartyWorkerRole begin");
//        Map<String, Object> ret = new HashMap<String, Object>();
//        String userId = getString(request, "userId");
//        String roleId = getString(request, "roleId");
//        String isChecked = getString(request, "isChecked");
//        ret = partyWorkerService.updatePartyWorkerRole(userId, roleId, isChecked);
//        logger.debug(this.getClass() + " updatePartyWorkerRole end");
//        return ret;
//    }

}
