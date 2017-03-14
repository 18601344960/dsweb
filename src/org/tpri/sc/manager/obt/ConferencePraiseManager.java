package org.tpri.sc.manager.obt;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.ConferencePraise;

/**
 * @description 点赞管理类
 * @author 易文俊
 * @since 2015-05-04
 */

@Repository("ConferencePraiseManager")
public class ConferencePraiseManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.OBT_CONFERENCE_PRAISE, ConferencePraise.class);
    }

    /**
     * 获取用户对某文章的点赞或收藏
     * 
     * @return
     */
    public ConferencePraise getPraise(String userId, String articleId, int type) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferencePraise.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addCondition(Condition.EQUAL("article.id", articleId));
        daoPara.addCondition(Condition.EQUAL("type", type));
        List list = dao.loadList(daoPara);
        if (list != null && list.size() > 0) {
            return (ConferencePraise) list.get(0);
        }
        return null;
    }

    /**
     * 获取文章的点赞或收藏总数
     * 
     * @return
     */
    public int getTotalPraiseByConferenceId(String articleId, int type) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferencePraise.class);
        daoPara.addCondition(Condition.EQUAL("article.id", articleId));
        daoPara.addCondition(Condition.EQUAL("type", type));
        int total = dao.getTotalCount(daoPara);
        return total;
    }

    /**
     * 删除点赞或收藏总数
     * 
     * @return
     */
    public boolean deletePraise(String id) {
        return super.delete(id, ObjectType.OBT_CONFERENCE_PRAISE);
    }

    /**
     * 
     * <B>方法名称：</B>获取我的点赞列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月17日
     * @param search
     * @param offset
     * @param limit
     * @param userId
     * @return
     */
    public List<ConferencePraise> getMyPraiseList(String search, Integer offset, Integer limit, String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferencePraise.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addCondition(Condition.EQUAL("type", ConferencePraise.TYPE_PRAISE));
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("article.name", search));
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
     * <B>方法名称：</B>获取我的点赞记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月17日
     * @param search
     * @param userId
     * @return
     */
    public Integer getMyPraiseListTotal(String search, String userId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferencePraise.class);
        daoPara.addCondition(Condition.EQUAL("userId", userId));
        daoPara.addCondition(Condition.EQUAL("type", ConferencePraise.TYPE_PRAISE));
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("article.name", search));
        }
        return dao.getTotalCount(daoPara);
    }
}
