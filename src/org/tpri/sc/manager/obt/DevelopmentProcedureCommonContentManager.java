package org.tpri.sc.manager.obt;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.DevelopmentProcedureCommonContent;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展阶段公共内容管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年12月18日
 */

@Repository("DevelopmentProcedureCommonContentManager")
public class DevelopmentProcedureCommonContentManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_DEVELOPMENT_PROCEDURE_COMMON_CONTENT, DevelopmentProcedureCommonContent.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param id
     * @return
     */
    public DevelopmentProcedureCommonContent getProcedureCommonContentById(String id) {
        return (DevelopmentProcedureCommonContent) this.loadOne(ObjectType.OBT_DEVELOPMENT_PROCEDURE_COMMON_CONTENT, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 
     * <B>方法名称：</B>获取某党员的公共数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param partymemberId
     * @param type
     * @return
     */
    public List<DevelopmentProcedureCommonContent> getProceduresCommonContentByType(String partymemberId, int type) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(DevelopmentProcedureCommonContent.class);
        daoPara.addCondition(Condition.EQUAL("partymemberId", partymemberId));
        daoPara.addCondition(Condition.EQUAL("type", type));
        daoPara.addOrder(Order.desc("contentDate"));
        List list = dao.loadList(daoPara);
        return list;
    }

}
