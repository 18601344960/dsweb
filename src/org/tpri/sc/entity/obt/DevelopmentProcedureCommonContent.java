package org.tpri.sc.entity.obt;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展阶段公用内容Bean<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年12月18日
 */
@Entity
@Table(name = "OBT_DEVELOPMENT_PROCEDURE_COMMON_CONTENT")
public class DevelopmentProcedureCommonContent extends ObjectBase {

    /**  */
    private static final long serialVersionUID = -6444299696218348160L;

    protected int objectType = ObjectType.OBT_DEVELOPMENT_PROCEDURE_COMMON_CONTENT;

    public static final int TYPE_1 = 1; // 组织生活情况
    public static final int TYPE_2 = 2; // 向党组织汇报
    public static final int TYPE_3 = 3; // 考察情况

    protected String partymemberId;//党员ID
    protected String procedureId;//所属阶段
    protected int type;//类别 1组织生活，2向党组织汇报，3考察情况
    protected Date contentDate;//日期
    protected String content;//内容

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "PARTY_MEMBER_ID")
    public String getPartymemberId() {
        return partymemberId;
    }

    public void setPartymemberId(String partymemberId) {
        this.partymemberId = partymemberId;
    }

    @Column(name = "PROCEDURE_ID")
    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    @Column(name = "TYPE")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "CONTENT_DATE")
    public Date getContentDate() {
        return contentDate;
    }

    public void setContentDate(Date contentDate) {
        this.contentDate = contentDate;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
