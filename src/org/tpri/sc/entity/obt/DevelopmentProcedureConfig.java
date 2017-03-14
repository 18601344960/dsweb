package org.tpri.sc.entity.obt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展流程配置<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年11月30日
 */
@Entity
@Table(name = "OBT_DEVELOPMENT_PROCEDURE_CONFIG")
public class DevelopmentProcedureConfig extends ObjectBase {

    /**  */
    private static final long serialVersionUID = -6444299696218348160L;

    protected int objectType = ObjectType.OBT_DEVELOPMENT_PROCEDURE_CONFIG;

    public static final int STATUS_0 = 0; // 不可以跳过
    public static final int STATUS_1 = 1; // 可以跳过

    protected String ccpartyId;
    protected String developtmentId;
    protected int status;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "CCPARTY_ID")
    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    @Column(name = "DEVELOPMENT_ID")
    public String getDeveloptmentId() {
        return developtmentId;
    }

    public void setDeveloptmentId(String developtmentId) {
        this.developtmentId = developtmentId;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
