package org.tpri.sc.entity.obt;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展阶段含义<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年12月18日
 */
public enum DevelopmentProcedureEnum {

    //发展阶段ID
    PROCEDURE_0001("0001", "申请人确定为积极分子"),
    PROCEDURE_0101("0101", "对入党积极分子进行培养教育和考察"),
    PROCEDURE_0102("0102", "确定发展对象-听取党内外有关群众及团组织推优意见"),
    PROCEDURE_0103("0103", "确定发展对象-党小组讨论"),
    PROCEDURE_0104("0104", "确定发展对象-支委会（支部大会）讨论"),
    PROCEDURE_0201("0201", "为发展对象确定入党介绍人"),
    PROCEDURE_0202("0202", "为发展对象进行政治审查"),
    PROCEDURE_0203("0203", "为发展对象进行集中培训"),
    PROCEDURE_0204("0204", "报上级党组织预审"),
    PROCEDURE_0205("0205", "公示情况"),
    PROCEDURE_0206("0206", "召开支部大会讨论"),
    PROCEDURE_0207("0207", "上级党组织派人谈话"),
    PROCEDURE_0208("0208", "上级党组织审批"),
    PROCEDURE_0301("0301", "对预备党员进行教育和考察"),
    PROCEDURE_0302("0302", "预备党员转正-党小组讨论"),
    PROCEDURE_0303("0303", "预备党员转正-征求党内外群众意见"),
    PROCEDURE_0304("0304", "预备党员转正-支委会审查"),
    PROCEDURE_0305("0305", "预备党员转正-支部大会讨论"),
    PROCEDURE_0306("0306", "预备党员转正-上级党委审批"),
    PROCEDURE_0401("0401", "成为正式党员");

    private String type;
    private String desc;

    DevelopmentProcedureEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}
