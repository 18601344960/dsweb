package org.tpri.sc.controller.obt;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.obt.DevelopmentProcedureConfigService;

/**
 * 
 * <B>系统名称：</B>党员发展流程配置控制器<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年11月30日
 */
@Controller
@RequestMapping("/obt")
public class DevelopmentProcedureConfigController extends BaseController {

    @Autowired
    private DevelopmentProcedureConfigService developmentProcedureConfigService;

    /**
     * <B>方法名称：</B>更新党员发展流程配置<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月30日
     * @param request
     * @return
     */
    @RequestMapping("updateDevelopmentProcedureConfig")
    @ResponseBody
    public Map<String, Object> updateDevelopmentProcedureConfig(HttpServletRequest request) {
        logger.debug(this.getClass() + " updateDevelopmentProcedureConfig begin");

        Map<String, Object> param = new HashMap<String, Object>();

        String ccpartyId = getString(request, "ccpartyId");
        String developtmentId = getString(request, "developtmentId");
        Integer status = getInt(request, "status");

        UserMc user = loadUserMc(request);
        Map<String, Object> ret = new HashMap<String, Object>();
        boolean result = developmentProcedureConfigService.updateDevelopmentProcedureConfig(user, ccpartyId, developtmentId, status);
        ret.put("success", result);
        logger.debug(this.getClass() + " updateDevelopmentProcedureConfig end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取党员发展流程配置<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月30日
     * @param request
     * @return
     */
    @RequestMapping("getDevelopmentProcedureList")
    @ResponseBody
    public Map<String, Object> getDevelopmentProcedureList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getDevelopmentProcedureList begin");
        String ccpartyId = getString(request, "ccpartyId");
        Integer phaseCode = getInteger(request, "phaseCode");
        Map<String, Integer> config = developmentProcedureConfigService.getDevelopmentProcedureList(ccpartyId, phaseCode);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("config", config);
        logger.debug(this.getClass() + " getDevelopmentProcedureList end");
        return ret;
    }

}
