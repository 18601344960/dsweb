package org.tpri.sc.service.ds;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.ds.QuestionAnswer;
import org.tpri.sc.manager.ds.QuestionAnswerManager;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>试题库答案服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（zhaozijing）
 * @since 2017年3月13日
 */
@Service("QuestionAnswerService")
public class QuestionAnswerService {
    @Autowired
    private QuestionAnswerManager questionAnswerManager;

    /**
     * 
     * <B>方法名称：</B>获取某试题下的答案<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2017年3月13日
     * @param questionId
     * @return
     */
    public List<QuestionAnswer> getQuestionAnswerByQuestion(String questionId) {
        List<QuestionAnswer> answers = questionAnswerManager.getQuestionAnswerByQuestion(questionId);
        return answers;
    }

}