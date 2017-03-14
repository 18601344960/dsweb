package org.tpri.sc.manager.obt;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.obt.DevelopmentProcedureConfig;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展流程配置管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年11月30日
 */

@Repository("DevelopmentProcedureConfigManager")
public class DevelopmentProcedureConfigManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_DEVELOPMENT_PROCEDURE_CONFIG, DevelopmentProcedureConfig.class);
    }

    /**
     * 根据ID获取党员发展流程配置
     * 
     * @return
     */
    public DevelopmentProcedureConfig getDevelopmentProcedureConfigById(String id) {
        Object obj = this.loadOne(ObjectType.OBT_DEVELOPMENT_PROCEDURE_CONFIG, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        DevelopmentProcedureConfig o = (DevelopmentProcedureConfig) obj;
        return o;
    }

    /**
     * 删除党员发展流程配置
     * 
     * @return
     */
    public boolean deleteDevelopmentProcedureConfig(String id) {
        return super.delete(id, ObjectType.OBT_DEVELOPMENT_PROCEDURE_CONFIG);
    }

    /**
     * <B>方法名称：</B>获取某组织某阶段党员发展流程配置<BR>
     * 
     * @return
     */
    public DevelopmentProcedureConfig getDevelopmentProcedureConfigByProcedureId(String ccpartyId, String developtmentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(DevelopmentProcedureConfig.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.EQUAL("developtmentId", developtmentId));
        List list = dao.loadList(daoPara);
        if (list != null && list.size() > 0) {
            return (DevelopmentProcedureConfig) list.get(0);
        }
        return null;
    }

    /**
     * <B>方法名称：</B>获取某组织党员发展流程配置<BR>
     * 
     * @return
     */
    public List<DevelopmentProcedureConfig> getDevelopmentProcedureConfigList(String ccpartyId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(DevelopmentProcedureConfig.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>删除党员发展流程配置<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月30日
     * @param ccpartyId
     * @param procedureId
     */
    public boolean deleteDevelopmentProcedureConfig(String ccpartyId, String developtmentId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(DevelopmentProcedureConfig.class);
        daoPara.addCondition(Condition.EQUAL("ccpartyId", ccpartyId));
        daoPara.addCondition(Condition.EQUAL("developtmentId", developtmentId));
        dao.delete(daoPara);
        return true;
    }

}
