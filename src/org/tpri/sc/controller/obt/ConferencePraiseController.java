package org.tpri.sc.controller.obt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.obt.ConferencePraise;
import org.tpri.sc.service.obt.ConferencePraiseService;

/**
 * 
 * <B>系统名称：</B>点赞控制器<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月12日
 */
@Controller
@RequestMapping("/obt")
public class ConferencePraiseController extends BaseController {

    @Autowired
    private ConferencePraiseService praiseService;

    /**
     * 新增点赞
     */
    @RequestMapping("addConferencePraise")
    @ResponseBody
    public Map addConferencePraise(HttpServletRequest request) {
        logger.debug(this.getClass() + " addConferencePraise begin");

        JSONObject json = new JSONObject();
        String articleId = getString(request, "articleId");
        int type = getInt(request, "type");

        ConferencePraise praise = praiseService.getPraise(loadUserId(request), articleId, type);
        if (praise != null) {
            Map ret = new HashMap();
            ret.put("success", true);
            ret.put("repeat", true);
            return ret;
        }

        boolean result = praiseService.addPraise(loadUserMc(request), articleId, type);

        Map ret = new HashMap();
        ret.put("success", result);
        if (result) {
            ret.put("msg", "保存成功");
        } else {
            ret.put("msg", "保存失败");
        }
        logger.debug(this.getClass() + " addConferencePraise begin");
        return ret;
    }

    /**
     * 刪除点赞
     */
    @RequestMapping("deleteConferencePraise")
    @ResponseBody
    public Map deleteConferencePraise(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteConferencePraise begin");

        String ids = getString(request, "ids");
        JSONArray idsArray = JSONArray.fromObject(ids);
        praiseService.deletePraise(loadUserMc(request), idsArray);
        Map ret = new HashMap();
        ret.put("success", true);
        ret.put("msg", "删除成功");
        logger.debug(this.getClass() + " deleteConferencePraise begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取我的点赞列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月17日
     * @param request
     * @return
     */
    @RequestMapping("getMyConferencePraiseList")
    @ResponseBody
    public Map<String, Object> getMyConferencePraiseList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getMyConferencePraiseList begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String beginTime = getString(request, "beginTime");
        String endTime = getString(request, "endTime");
        String search = getString(request, "searchKey");
        String userId = getString(request, "userId");
        List<ConferencePraise> praises = praiseService.getMyPraiseList(search, offset, limit, userId);
        Integer total = praiseService.getMyPraiseListTotal(search, userId);
        ret.put("rows", praises);
        ret.put("total", total);
        logger.debug(this.getClass() + " getMyConferencePraiseList end");
        return ret;
    }

}
