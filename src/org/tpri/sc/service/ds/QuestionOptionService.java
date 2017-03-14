package org.tpri.sc.service.ds;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.ds.QuestionOption;
import org.tpri.sc.manager.ds.QuestionOptionManager;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>试题库选项服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（zhaozijing）
 * @since 2017年3月13日
 */
@Service("QuestionOptionService")
public class QuestionOptionService {
    @Autowired
    private QuestionOptionManager questionOptionManager;

    /**
     * 
     * <B>方法名称：</B>获取某试题下的选项<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2017年3月13日
     * @param questionId
     * @return
     */
    public List<QuestionOption> getQuestionOptionByQuestion(String questionId) {
        List<QuestionOption> options = questionOptionManager.getQuestionOptionByQuestion(questionId);
        return options;
    }

    /**
     * 
     * <B>方法名称：</B>根据ID获取详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2017年3月13日
     * @param id
     * @return
     */
    public QuestionOption getQuestionOptionById(String id) {
        return questionOptionManager.getQuestionOptionById(id);
    }
}