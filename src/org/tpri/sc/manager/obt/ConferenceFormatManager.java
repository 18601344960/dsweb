package org.tpri.sc.manager.obt;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.obt.ConferenceFormat;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>文章所属生活形式管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月2日
 */

@Repository("ConferenceFormatManager")
public class ConferenceFormatManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_CONFERENCE_FORMAT, ConferenceFormat.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取文章关联的某生活形式<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param id
     * @return
     */
    public ConferenceFormat getConferenceFormatById(String id) {
        Object obj = this.loadOne(ObjectType.OBT_CONFERENCE_FORMAT, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        ConferenceFormat c = (ConferenceFormat) obj;
        return c;
    }

    /**
     * 
     * <B>方法名称：</B>删除文章关联生活形式<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param articleId
     * @return
     */
    public boolean deleteConferenceFormatByConferenceId(String conferenceId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceFormat.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        dao.delete(daoPara);
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>删除文章所属生活形式<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param id
     * @return
     */
    public boolean deleteConferenceFormat(String id) {
        return super.delete(id, ObjectType.OBT_CONFERENCE_FORMAT);
    }

    /**
     * 
     * <B>方法名称：</B>根据文章ID获取所属哪些生活形式<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年5月2日
     * @param articleId
     * @param categoryId
     * @param ccpartyId
     * @return
     */
    public List<ConferenceFormat> getConferenceFormatByConferenceId(String conferenceId, String categoryId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ConferenceFormat.class);
        daoPara.addCondition(Condition.EQUAL("conferenceId", conferenceId));
        if (categoryId != null && !"".equals(categoryId)) {
            daoPara.addCondition(Condition.EQUAL("categoryId", categoryId));
        }
        List list = dao.loadList(daoPara);
        return list;
    }
}
