package org.tpri.sc.controller.obt;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.service.obt.CcpartyCardService;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.view.org.CcpartyCardPageView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党组织电子活动证控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年5月25日
 */
@Controller
@RequestMapping("/card")
public class CcpartyCardController extends BaseController {
    @Autowired
    private CcpartyCardService ccpartyCardService;
    @Autowired
    private CCpartyService ccpartyService;

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
    @RequestMapping("getCcpartyCardPages")
    @ResponseBody
    public Map<String, Object> getCardPages(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCcpartyCardPages begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        String paramter = getString(request, "paramter");
        CcpartyCardPageView page = ccpartyCardService.getCcpartyCardPage(ccpartyId, paramter);
        CCParty ccparty = ccpartyService.getCCPartyFromMc(ccpartyId);
        ret.put("page", page);
        ret.put("ccparty", ccparty);
        logger.debug(this.getClass() + " getCcpartyCardPages end");
        return ret;
    }
}
