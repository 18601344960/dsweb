package org.tpri.sc.controller.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.sys.CodeService;
import org.tpri.sc.view.ZTreeView;

/**
 * @description 代码表控制器
 * @author 易文俊
 * @since 2015-06-30
 */
@Controller
@RequestMapping("/sys")
public class CodeController extends BaseController {

    @Autowired
    private CodeService codeService;

    /**
     * <B>方法名称：</B>根据ID获取分类的代码<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月22日
     * @param request
     * @return
     */
    @RequestMapping("getCodeById")
    @ResponseBody
    public Map<String, Object> getCodeById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCodeById begin");
        String id = getString(request, "id");
        Code code = codeService.getCodeById(id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", code);
        logger.debug(this.getClass() + " getCodeById end");
        return ret;
    }

    /**
     * 获取某个分类的代码表
     */
    @RequestMapping("getCodeListByParentId")
    @ResponseBody
    public Map<String, Object> getCodesByParentId(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCodeListByParentId begin");
        String parentId = getString(request, "parentId");
        List<Code> list = codeService.getCodesByParentId(parentId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        logger.debug(this.getClass() + " getCodeListByParentId end");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取某个分类的代码表并封装成树<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月28日
     * @param request
     * @return
     */
    @RequestMapping("getCodeTreeByParentId")
    @ResponseBody
    public List<ZTreeView> getCodeTreeByParentId(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCodeTreeByParentId begin");
        String parentId = getString(request, "parentId");
        List<ZTreeView> list = codeService.getCodeTreeByParentId(parentId);
        logger.debug(this.getClass() + " getCodeListByParentId end");
        return list;
    }

    /**
     * 获取代码表
     */
    @RequestMapping("getCodeList")
    @ResponseBody
    public Map<String, Object> getCodeList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCodeList begin");
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        List<Code> list = codeService.getCodeList(start, limit);
        Integer total = codeService.getCodeTotal();
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        ret.put("total", total);
        logger.debug(this.getClass() + " getCodeList end");
        return ret;
    }

    /**
     * 获取所有的代码表
     */
    @RequestMapping("getCodeAllList")
    @ResponseBody
    public Map<String, Object> getCodeAllList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getCodeAllList begin");
        List<Code> list = codeService.getCodeList();
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", list);
        logger.debug(this.getClass() + " getCodeAllList end");
        return ret;
    }

    /**
     * 新增代码表
     */
    @RequestMapping("addCode")
    @ResponseBody
    public Map<String, Object> addCode(HttpServletRequest request) {
        logger.debug(this.getClass() + " addCode begin");

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("id", getString(request, "id"));
        param.put("name", getString(request, "name"));
        param.put("code", getString(request, "code"));
        param.put("parentCode", getString(request, "parentCode"));
        param.put("description", getString(request, "description"));
        param.put("status", getInt(request, "status"));

        UserMc user = loadUserMc(request);
        codeService.addCode(user, param);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug(this.getClass() + " addCode begin");
        return ret;
    }

    /**
     * 修改代码表
     */
    @RequestMapping("updateCode")
    @ResponseBody
    public Map<String, Object> updateCode(HttpServletRequest request) {
        logger.debug(this.getClass() + " updateCode begin");

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("id", getString(request, "id"));
        param.put("name", getString(request, "name"));
        param.put("code", getString(request, "code"));
        param.put("parentCode", getString(request, "parentCode"));
        param.put("description", getString(request, "description"));
        param.put("status", getInt(request, "status"));

        UserMc user = loadUserMc(request);
        codeService.updateCode(user, param);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug(this.getClass() + " updateCode begin");
        return ret;
    }

    /**
     * 刪除代码表
     */
    @RequestMapping("deleteCode")
    @ResponseBody
    public Map<String, Object> deleteCode(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteCode begin");
        String id = getString(request, "id");
        UserMc user = loadUserMc(request);
        codeService.deleteCode(user, id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "删除成功");
        logger.debug(this.getClass() + " deleteCode begin");
        return ret;
    }
}
