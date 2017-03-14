package org.tpri.sc.manager.ds;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.ds.Question;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>试题库管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（zhaozijing）
 * @since 2017年3月13日
 */
@Repository("QuestionManager")
public class QuestionManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.DS_QUESTION, Question.class);
    }

    /**
     * 获取试题库集合列表
     * 
     * @param offset
     * @param limit
     * @param search
     * @return
     */
    public List<Question> getQuestionList(Integer offset, Integer limit, String search) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Question.class);
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        if (offset != null && limit != null) {
            daoPara.setStart(offset);
            daoPara.setLimit(limit);
        }
        daoPara.addOrder(Order.desc("sequence"));
        return (List) dao.loadList(daoPara);
    }

    /**
     * 获取试题库记录条数
     * 
     * @param search
     * @return
     */
    public Integer getQuestionTotal(String search) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Question.class);
        if (!StringUtils.isEmpty(search)) {
            daoPara.addCondition(Condition.LIKE("name", search));
        }
        return dao.getTotalCount(daoPara);
    }

    /**
     * 根据id获取详情
     * 
     * @param id
     * @return
     */
    public Question getQuestionById(String id) {
        return (Question) this.loadOne(ObjectType.DS_QUESTION, new String[] { "id" }, new Object[] { id });
    }

}
