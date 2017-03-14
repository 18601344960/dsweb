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
import org.tpri.sc.entity.obt.DevelopmentProcedureCommonContent;
import org.tpri.sc.service.obt.DevelopmentProcedureCommonContentService;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展阶段公共内容控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年12月18日
 */
@Controller
@RequestMapping("/obt")
public class DevelopmentProcedureCommonContentController extends BaseController {

    @Autowired
    private DevelopmentProcedureCommonContentService commonContentService;

    /**
     * 
     * <B>方法名称：</B>获取某党员的公用内容<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param request
     * @return
     */
    @RequestMapping("getDevelopmentProcedureCommonContents")
    @ResponseBody
    public Map<String, Object> getDevelopmentProcedureCommonContents(HttpServletRequest request) {
        logger.debug(this.getClass() + " getDevelopmentProcedureCommonContents begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String partymemberId = getString(request, "partymemberId");
        Integer type = getInt(request, "type");
        List<DevelopmentProcedureCommonContent> contents = commonContentService.getProceduresCommonContentByType(partymemberId, type);
        ret.put("rows", contents);
        logger.debug(this.getClass() + " getDevelopmentProcedureCommonContents end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>保存或修改党员发展阶段公共内容<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param request
     * @return
     */
    @RequestMapping("saveOrUpdateDevelopmentProcedureCommonContent")
    @ResponseBody
    public Map<String, Object> saveOrUpdateDevelopmentProcedureCommonContent(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();
        logger.debug(this.getClass() + " saveOrUpdateDevelopmentProcedureCommonContent begin");
        String paramters = getString(request, "paramters");
        ret = commonContentService.saveOrUpdateDevelopmentProcedureCommonContent(paramters);
        logger.debug(this.getClass() + " saveOrUpdateDevelopmentProcedureCommonContent end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>删除<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param request
     * @return
     */
    @RequestMapping("deleteDevelopmentProcedureCommonContent")
    @ResponseBody
    public Map<String, Object> deleteDevelopmentProcedureCommonContent(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();
        logger.debug(this.getClass() + " deleteDevelopmentProcedureCommonContent begin");
        String id = getString(request, "id");
        ret = commonContentService.deleteDevelopmentProcedureCommonContent(id);
        logger.debug(this.getClass() + " deleteDevelopmentProcedureCommonContent end");
        return ret;
    }

}
