package org.tpri.sc.manager.ds;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.ds.Category;
import org.tpri.sc.entity.ds.QuestionCategory;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>试题库类别关联管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（zhaozijing）
 * @since 2017年3月13日
 */
@Repository("QuestionCategoryManager")
public class QuestionCategoryManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.DS_QUESTION_CATEGORY, QuestionCategory.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取某各类别下的试题列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2017年3月13日
     * @param categoryId
     * @return
     */
    public List<QuestionCategory> getQuestionsByCategory(String categoryId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(Category.class);
        daoPara.addCondition(Condition.EQUAL("categoryId", categoryId));
        return (List) dao.loadList(daoPara);
    }

}
