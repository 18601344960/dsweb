package org.tpri.sc.service.obt;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.ConferenceComment;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.ConferenceCommentManager;
import org.tpri.sc.manager.obt.ConferenceManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.util.UUIDUtil;

/**
 * 
 * <B>系统名称：</B>支部手册<BR>
 * <B>模块名称：</B>文章评论<BR>
 * <B>中文类名：</B>评论服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月04日
 */
@Service("ConferenceCommentService")
public class ConferenceCommentService {

    @Autowired
    private ConferenceCommentManager commentManager;

    @Autowired
    private ConferenceManager articleManager;

    @Autowired
    private UserManager userManager;

    /**
     * 
     * <B>方法名称：</B>获取某文章下的评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param articleId
     * @return
     */
    public List<ConferenceComment> getCommentList(String articleId) {
        List<ConferenceComment> comments = commentManager.getCommentByConferenceId(articleId);
        for (ConferenceComment comment : comments) {
            comment.setUser(userManager.getUserFromMc(comment.getUserId()));
        }
        return comments;
    }

    /**
     * 
     * <B>方法名称：</B>获取某文章的评论列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月2日
     * @param offset
     * @param limit
     * @param articleId
     * @return
     */
    public List<ConferenceComment> getConferenceComments(Integer offset, Integer limit, String articleId) {
        List<ConferenceComment> comments = commentManager.getConferenceComments(offset, limit, articleId);
        for (ConferenceComment comment : comments) {
            comment.setUser(userManager.getUserFromMc(comment.getUserId()));
        }
        return comments;
    }

    /**
     * 
     * <B>方法名称：</B>获取某文章评论记录条数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月2日
     * @param articleId
     * @return
     */
    public Integer getConferenceCommentsTotal(String articleId) {
        return commentManager.getTotalCommentByConferenceId(articleId);
    }

    /**
     * 
     * <B>方法名称：</B>根据ID获取文章评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param commentId
     * @return
     */
    public ConferenceComment getCommentById(String commentId) {
        return commentManager.getCommentById(commentId);
    }

    /**
     * 
     * <B>方法名称：</B>添加文章评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param user
     * @param json
     * @return
     */
    public boolean addComment(UserMc user, Map<String, Object> map) {
        ConferenceComment comment = new ConferenceComment();
        comment.setId(UUIDUtil.id());
        comment.setConference(articleManager.getConferenceById((String) map.get("articleId")));
        comment.setContent((String) map.get("content"));
        comment.setUserId(user.getId());
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        commentManager.add(comment);
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>修改文章评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param user
     * @param json
     * @return
     */
    public boolean editComment(UserMc user, Map<String, Object> map) {
        String id = (String) map.get("articleId");
        ConferenceComment comment = commentManager.getCommentById(id);
        if (comment != null) {
            comment.setContent((String) map.get("content"));
            return commentManager.saveOrUpdate(comment);
        }
        return false;
    }

    /**
     * 
     * <B>方法名称：</B>删除评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param user
     * @param ids
     * @return
     */
    public boolean deleteComment(UserMc user, JSONArray ids) {
        for (int i = 0; i < ids.size(); i++) {
            String commentId = ids.getString(i);
            commentManager.deleteComment(commentId);
        }
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>获取我的评论列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param offset
     * @param limit
     * @param userId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public List<ConferenceComment> getMyCommentList(Integer offset, Integer limit, String userId, String beginTime, String endTime, String searchKey) {
        return commentManager.getMyCommentList(offset, limit, userId, beginTime, endTime, searchKey);
    }

    /**
     * 
     * <B>方法名称：</B>获取我的评论总记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月4日
     * @param userId
     * @param beginTime
     * @param endTime
     * @param searchKey
     * @return
     */
    public Integer getMyCommentTotal(String userId, String beginTime, String endTime, String searchKey) {
        return commentManager.getMyCommentTotal(userId, beginTime, endTime, searchKey);
    }
}
