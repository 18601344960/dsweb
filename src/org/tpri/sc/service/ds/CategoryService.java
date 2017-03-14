package org.tpri.sc.service.ds;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.ds.Category;
import org.tpri.sc.manager.ds.CategoryManager;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>试题分类服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（zhaozijing）
 * @since 2017年3月13日
 */
@Service("CategoryService")
public class CategoryService {
    @Autowired
    private CategoryManager categoryManager;

    /**
     * 
     * <B>方法名称：</B>获取类别<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2017年3月13日
     * @param parentId
     * @return
     */
    public List<Category> getCategoryList(String parentId) {
        List<Category> categorys = categoryManager.getCategoryList(parentId);
        return categorys;
    }
}