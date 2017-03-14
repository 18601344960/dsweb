package org.tpri.sc.manager.obt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.ConferenceComment;

/**
 * 
 * <B>系统名称：</B>支部手册<BR>
 * <B>模块名称：</B>文章评论<BR>
 * <B>中文类名：</B>文章评论管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月04日
 */
@Repository("ConferenceCommentManager")
public class ConferenceCommentManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.OBT_CONFERENCE_COMMENT, ConferenceComment.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取文章的评论列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param articleId
     * @return
     */
    public List<ConferenceComment> getCommentByConferenceId(String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceComment.class);
        daoPara.addCondition(Condition.EQUAL("conference.id", conferenceId));
        daoPara.addOrder(Order.asc("createTime"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取文章评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年11月3日
     * @param offset
     * @param limit
     * @param articleId
     * @return
     */
    public List<ConferenceComment> getConferenceComments(Integer offset, Integer limit, String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceComment.class);
        daoPara.addCondition(Condition.EQUAL("conference.id", conferenceId));
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("createTime"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取文章的评论总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param articleId
     * @return
     */
    public int getTotalCommentByConferenceId(String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceComment.class);
        daoPara.addCondition(Condition.EQUAL("conference.id", conferenceId));
        int total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * 
     * <B>方法名称：</B>根据ID获取评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param id
     * @return
     */
    public ConferenceComment getCommentById(String id) {
        ConferenceComment comment = (ConferenceComment) super.load(id, ObjectType.OBT_CONFERENCE_COMMENT);
        return comment;
    }

    /**
     * 
     * <B>方法名称：</B>修改评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param id
     * @param fieldValues
     * @return
     */
    public boolean editComment(String id, Map<String, Object> fieldValues) {
        return super.update(id, ObjectType.OBT_CONFERENCE_COMMENT, fieldValues);
    }

    /**
     * 
     * <B>方法名称：</B>删除评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月04日
     * @param id
     * @return
     */
    public boolean deleteComment(String id) {
        return super.delete(id, ObjectType.OBT_CONFERENCE_COMMENT);
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
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceComment.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        if (!StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime)) {
            List<Object> values = new ArrayList<Object>();
            values.add(beginTime);
            values.add(endTime);
            daoPara.addCondition(Condition.BETWEEN("createTime", values));
        }
        if (!StringUtils.isEmpty(searchKey)) {
            daoPara.addCondition(Condition.LIKE("content", searchKey));
        }
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("createTime"));
        List list = dao.loadList(daoPara);
        return list;
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
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceComment.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        if (!StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime)) {
            List<Object> values = new ArrayList<Object>();
            values.add(beginTime);
            values.add(endTime);
            daoPara.addCondition(Condition.BETWEEN("createTime", values));
        }
        if (!StringUtils.isEmpty(searchKey)) {
            daoPara.addCondition(Condition.LIKE("content", searchKey));
        }
        int total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * 
     * <B>方法名称：</B>删除文章评论<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月3日
     * @param articleId
     */
    public boolean deleteCommentByConferenceId(String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceComment.class);
        daoPara.addCondition(Condition.EQUAL("conference.id", conferenceId));
        dao.delete(daoPara);
        return true;

    }
}
