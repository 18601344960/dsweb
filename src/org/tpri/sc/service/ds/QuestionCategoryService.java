package org.tpri.sc.service.ds;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.ds.QuestionCategory;
import org.tpri.sc.manager.ds.QuestionCategoryManager;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>试题分类关联服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（zhaozijing）
 * @since 2017年3月13日
 */
@Service("QuestionCategoryService")
public class QuestionCategoryService {
    @Autowired
    private QuestionCategoryManager questionCategoryManager;
    

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
        List<QuestionCategory> questionCategorys = questionCategoryManager.getQuestionsByCategory(categoryId);
        return questionCategorys;
    }
}