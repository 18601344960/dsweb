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
 * <B>中文类名：</B>党员发展流程<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年11月30日
 */
@Entity
@Table(name = "OBT_DEVELOPMENT_PROCEDURE")
public class DevelopmentProcedure extends ObjectBase {

    /**  */
    private static final long serialVersionUID = -6444299696218348160L;

    protected int objectType = ObjectType.OBT_DEVELOPMENT_PROCEDURE;
    
    public static final int PUASE_CODE_0=0;//申请人
    public static final int PUASE_CODE_1=1;//积极分子
    public static final int PUASE_CODE_2=2;//发展对象
    public static final int PUASE_CODE_3=3;//预备党员
    public static final int PUASE_CODE_4=4;//正式党员
    
    public static final String OFFICIAL_PARTYMEMBER = "0401"; 
    
    public static final int STATUS_0 = 0; // 不可以跳过
    public static final int STATUS_1 = 1; // 可以跳过

    protected int phaseCode;
    protected String phaseName;
    protected String nodeCode;
    protected String nodeName;
    protected String nextNodeCode;
    protected int status;
    protected String description;
    protected int sequence;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "PHASE_CODE")
    public int getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(int phaseCode) {
        this.phaseCode = phaseCode;
    }

    @Column(name = "PHASE_NAME")
    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    @Column(name = "NODE_CODE")
    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    @Column(name = "NODE_NAME")
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Column(name = "NEXT_NODE_CODE")
    public String getNextNodeCode() {
        return nextNodeCode;
    }

    public void setNextNodeCode(String nextNodeCode) {
        this.nextNodeCode = nextNodeCode;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "SEQUENCE")
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

}
