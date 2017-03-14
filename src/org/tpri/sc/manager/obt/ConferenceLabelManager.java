package org.tpri.sc.manager.obt;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.obt.ConferenceLabel;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>标签管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月2日
 */

@Repository("ConferenceLabelManager")
public class ConferenceLabelManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_CONFERENCE_LABEL, ConferenceLabel.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取文章关联的某标签<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param id
     * @return
     */
    public ConferenceLabel getConferenceLabelById(String id) {
        Object obj = this.loadOne(ObjectType.OBT_CONFERENCE_LABEL, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        ConferenceLabel c = (ConferenceLabel) obj;
        return c;
    }

    /**
     * <B>方法名称：</B>获取组织工作所属标签<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月23日
     * @param conferenceId
     * @param categoryId
     * @return
     */
    public List<ConferenceLabel> getLabelsByConferenceId(String conferenceId, String categoryId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceLabel.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        if (categoryId != null && !"".equals(categoryId)) {
            daoPara.addCondition(Condition.EQUAL("categoryId", categoryId));
        }
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>删除文章关联<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param articleId
     * @return
     */
    public boolean deleteConferenceLabelByConferenceId(String articleId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceLabel.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", articleId));
        dao.delete(daoPara);
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>删除文章所属标签<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param id
     * @return
     */
    public boolean deleteConferenceLabel(String id) {
        return super.delete(id, ObjectType.OBT_CONFERENCE_LABEL);
    }

    /**
     * 
     * <B>方法名称：</B>根据文章ID获取所属哪些标签<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param articleId
     * @param categoryId
     * @param ccpartyId
     * @return
     */
    public List<ConferenceLabel> getConferenceLabelsByConferenceId(String conferenceId, String categoryId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceLabel.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        if (categoryId != null && !"".equals(categoryId)) {
            daoPara.addCondition(Condition.EQUAL("categoryId", categoryId));
        }
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>获取某批组织的标签关联<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月3日
     * @param articleIdList
     * @return
     */
    public List<ConferenceLabel> getConferenceLabelByConferenceIds(List<Object> articleIdList) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceLabel.class);
        if (articleIdList != null && articleIdList.size() > 0) {
            daoPara.addCondition(Condition.IN("conferenceId", articleIdList));
        } else {
            return null;
        }
        List list = dao.loadList(daoPara);
        return list;
    }
	/**
	 * 
	 * <B>方法名称：</B>根据标签ID查看是否存在关联<BR>
	 * <B>概要说明：</B><BR>
	 * @author 刘佳丽
	 * @since 2016年7月14日 	
	 * @param categoryId
	 * @return
	 */
	public List<ConferenceLabel> getConferenceLabelByCategoryId(String categoryId) {
		DaoPara daoPara = new DaoPara();
		daoPara.setClazz(ConferenceLabel.class);
		daoPara.addCondition(Condition.EQUAL("categoryId", categoryId));
		List list = dao.loadList(daoPara);
		return list;
	}

}
