package org.tpri.sc.manager.obt;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.obt.DevelopmentProcedure;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展流程管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年11月30日
 */

@Repository("DevelopmentProcedureManager")
public class DevelopmentProcedureManager extends ManagerBase {
    private static boolean initialized = false;

    public void initialize() {
        if (initialized)
            return;
        initialized = true;
        ObjectRegister.registerClass(ObjectType.OBT_DEVELOPMENT_PROCEDURE, DevelopmentProcedure.class);
        //mc.clearObject(ObjectType.OBT_DEVELOPMENT_PROCEDURE);
        initializeObjects(ObjectType.OBT_DEVELOPMENT_PROCEDURE);
    }
    
    /**
     * 根据ID获取党员发展流程
     * 
     * @return
     */
    public DevelopmentProcedure getDevelopmentProcedureById(String id) {
        DevelopmentProcedure developmentProcedure = (DevelopmentProcedure) loadMcCacheObject(ObjectType.OBT_DEVELOPMENT_PROCEDURE, id);
        if(developmentProcedure==null){
            //尝试从数据库获取
            developmentProcedure = this.getDevelopmentProcedureFromDB(id);
            if(developmentProcedure!=null){
                addCache(developmentProcedure);
            }
        }
        return developmentProcedure;
    }
    
    public DevelopmentProcedure getDevelopmentProcedureFromDB(String id){
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(DevelopmentProcedure.class);
        daoPara.addCondition(Condition.EQUAL("id", id));
        return (DevelopmentProcedure)dao.loadOne(daoPara);
    }

    /**
     * 获取所有党员发展流程
     * 
     * @return
     */
    public List<DevelopmentProcedure> getDevelopmentProcedureList(Integer phaseCode) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(DevelopmentProcedure.class);
        if(phaseCode!=null){
            daoPara.addCondition(Condition.EQUAL("phaseCode", phaseCode));
        }
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * 
     * <B>方法名称：</B>获取某个发展阶段不可跳过的计划<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月5日
     * @return
     */
    public List<Object> getDevelopmentProceduresByPhaseCode(String phaseCode, String ccpartyId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ODP.ID FROM OBT_DEVELOPMENT_PROCEDURE AS ODP WHERE ODP.ID IN(\n");
        sql.append("    SELECT ODP.ID FROM OBT_DEVELOPMENT_PROCEDURE AS ODP WHERE ODP.STATUS=0\n");
        sql.append("    UNION ALL\n");
        sql.append("    (\n");
        sql.append("        SELECT ODPC.DEVELOPMENT_ID FROM OBT_DEVELOPMENT_PROCEDURE_CONFIG AS ODPC WHERE ODPC.CCPARTY_ID=:ccpartyId AND ODPC.STATUS=0\n");
        sql.append("    )\n");
        sql.append(")\n");
        sql.append("AND ODP.PHASE_CODE=:phaseCode\n");
        sql.append("ORDER BY ODP.ID ASC\n");
        return dao.getCurrentSession().createSQLQuery(sql.toString()).setString("ccpartyId", ccpartyId).setString("phaseCode", phaseCode).list();
    }

    /**
     * 
     * <B>方法名称：</B>获取下一个不可跳过的阶段<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param id
     * @return
     */
    public Object getNextDevelopmentProcedures(String id, String ccpartyId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT MIN(P.ID) FROM (\n");
        sql.append("    SELECT ODP.ID FROM OBT_DEVELOPMENT_PROCEDURE AS ODP WHERE ODP.STATUS=0\n");
        sql.append("    UNION ALL \n");
        sql.append("    (\n");
        sql.append("        SELECT ODPC.DEVELOPMENT_ID FROM OBT_DEVELOPMENT_PROCEDURE_CONFIG AS ODPC WHERE ODPC.CCPARTY_ID=:ccpartyId AND ODPC.STATUS=0\n");
        sql.append("    )\n");
        sql.append(") AS P\n");
        sql.append("WHERE P.ID>:id\n");
        sql.append("ORDER BY P.ID ASC\n");
        return dao.getCurrentSession().createSQLQuery(sql.toString()).setString("ccpartyId", ccpartyId).setString("id", id).uniqueResult();
    }
}
