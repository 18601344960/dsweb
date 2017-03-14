package org.tpri.sc.controller.obt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.obt.ConferenceComment;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.obt.ConferenceCommentService;

/**
 * 
 * <B>系统名称：</B>支部手册<BR>
 * <B>模块名称：</B>文章<BR>
 * <B>中文类名：</B>评论控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年05月04日
 */
@Controller
@RequestMapping("/obt")
public class ConferenceCommentController extends BaseController {

    @Autowired
    private ConferenceCommentService commentService;

    /**
     * 
     * <B>方法名称：</B>获取某文章下的评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceCommentList")
    @ResponseBody
    public Map<String, Object> getConferenceCommentList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceCommentList begin");
        String articleId = getString(request, "articleId");
        List<ConferenceComment> list = commentService.getCommentList(articleId);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", list);
        logger.debug(this.getClass() + " getConferenceCommentList end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取文章的评论<BR>
     * <B>概要说明：</B>带分页<BR>
     * 
     * @author 赵子靖
     * @since 2015年11月2日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceComments")
    @ResponseBody
    public Map<String, Object> getConferenceComments(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceComments begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String articleId = getString(request, "articleId");
        List<ConferenceComment> comments = commentService.getConferenceComments(offset, limit, articleId);
        Integer total = commentService.getConferenceCommentsTotal(articleId);
        ret.put("rows", comments);
        ret.put("total", total);
        logger.debug(this.getClass() + " getConferenceComments end");
        return ret;
    }
    
    /**
     * 
     * <B>方法名称：</B>获取我的评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param request
     * @return
     */
    @RequestMapping("getMyCommentList")
    @ResponseBody
    public Map<String, Object> getMyCommentList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getMyCommentList begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String beginTime = getString(request, "beginTime");
        String endTime = getString(request, "endTime");
        String searchKey = getString(request, "searchKey");
        UserMc user = loadUserMc(request);
        List<ConferenceComment> comments = commentService.getMyCommentList(offset, limit, user.getId(),beginTime,endTime,searchKey);
        Integer total = commentService.getMyCommentTotal(user.getId(),beginTime,endTime,searchKey);
        ret.put("rows", comments);
        ret.put("total", total);
        logger.debug(this.getClass() + " getMyCommentList end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取评论详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param request
     * @return
     */
    @RequestMapping("getConferenceCommentById")
    @ResponseBody
    public Map<String, Object> getConferenceCommentById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getConferenceCommentById begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        String id = getString(request, "id");
        ConferenceComment comment = commentService.getCommentById(id);
        ret.put("item", comment);
        logger.debug(this.getClass() + " getConferenceCommentById end");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>新增评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param request
     * @return
     */
    @RequestMapping("addConferenceComment")
    @ResponseBody
    public Map<String, Object> addConferenceComment(HttpServletRequest request) {
        logger.debug(this.getClass() + " addConferenceComment begin");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("articleId", getString(request, "articleId"));
        map.put("content", getString(request, "content"));

        UserMc user=loadUserMc(request);
        commentService.addComment(user, map);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug(this.getClass() + " addConferenceComment begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>文章评论修改<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param request
     * @return
     */
    @RequestMapping("editConferenceComment")
    @ResponseBody
    public Map<String, Object> editConferenceComment(HttpServletRequest request) {
        logger.debug(this.getClass() + " editConferenceComment begin");

        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", getString(request, "id"));
        map.put("content", getString(request, "content"));
        UserMc user=loadUserMc(request);
        commentService.editComment(user, map);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "保存成功");
        logger.debug(this.getClass() + " editConferenceComment begin");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>删除评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param request
     * @return
     */
    @RequestMapping("deleteConferenceComment")
    @ResponseBody
    public Map<String, Object> deleteConferenceComment(HttpServletRequest request) {
        logger.debug(this.getClass() + " deleteConferenceComment begin");
        Map<String, Object> ret = new HashMap<String, Object>();

        String ids = getString(request, "ids");
        JSONArray idsArray = JSONArray.fromObject(ids);
        commentService.deleteComment(loadUserMc(request), idsArray);
        ret.put("success", true);
        ret.put("msg", "删除成功");
        logger.debug(this.getClass() + " deleteConferenceComment begin");
        return ret;
    }
}
