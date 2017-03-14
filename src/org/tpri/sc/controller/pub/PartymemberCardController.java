package org.tpri.sc.controller.pub;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.service.pub.PartymemberCardService;
import org.tpri.sc.view.obt.PartyMemberCardPageView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员电子活动证控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月21日
 */
@Controller
@RequestMapping("/card")
public class PartymemberCardController extends BaseController {
    @Autowired
    private PartymemberCardService partymemberCardService;

    /**
     * 
     * <B>方法名称：</B>计算获得党员电子活动证的总页数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月21日
     * @param request
     * @return
     */
    @RequestMapping("getCardPages")
    @ResponseBody
    public Map<String, Object> getCardPages(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCardPages begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String userId = getString(request, "userId");
        String paramter = getString(request, "paramter");
        PartyMemberCardPageView page = partymemberCardService.getPartymemberCards(userId, paramter);
        ret.put("page", page);
        logger.debug(this.getClass() + " getCardPages end");
        return ret;
    }
}
