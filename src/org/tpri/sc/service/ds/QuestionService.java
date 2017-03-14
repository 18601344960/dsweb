package org.tpri.sc.service.ds;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.ds.Question;
import org.tpri.sc.manager.ds.QuestionManager;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>试题库服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（zhaozijing）
 * @since 2017年3月13日
 */
@Service("QuestionService")
public class QuestionService {
    @Autowired
    private QuestionManager questionManager;

    /**
     * 
     * <B>方法名称：</B>获取试题库集合列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2017年3月13日
     * @param offset
     * @param limit
     * @param search
     * @return
     */
    public List<Question> getQuestionList(Integer offset, Integer limit, String search) {
        List<Question> questions = questionManager.getQuestionList(offset, limit, search);
        return questions;
    }

    /**
     * 
     * <B>方法名称：</B>获取试题库记录条数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2017年3月13日
     * @param search
     * @return
     */
    public Integer getQuestionTotal(String search) {
        return questionManager.getQuestionTotal(search);
    }
}