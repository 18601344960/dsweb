package org.tpri.sc.service.obt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.ConferenceLabel;
import org.tpri.sc.manager.obt.ConferenceLabelManager;

/**
 * 
 * <B>系统名称：</B>支部手册<BR>
 * <B>模块名称：</B>文章<BR>
 * <B>中文类名：</B>文章服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年05月02日
 */
@Service("ConferenceLabelService")
public class ConferenceLabelService {

    @Autowired
    private ConferenceLabelManager categoryManager;
    @Autowired
    private ConferenceLabelManager conferenceLabelManager;

	public List<ConferenceLabel> getConferenceLabelByCategoryId( String categoryId) {
		return conferenceLabelManager.getConferenceLabelByCategoryId(categoryId);
	}


    

}
