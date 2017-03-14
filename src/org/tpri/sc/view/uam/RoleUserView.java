package org.tpri.sc.view.uam;


/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B>切换用户党务角色视图<BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年3月15日
 */
public class RoleUserView {
    private String id;
    private String name;
    private String ccpartyId;
    private String ccpartyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    public String getCcpartyName() {
        return ccpartyName;
    }

    public void setCcpartyName(String ccpartyName) {
        this.ccpartyName = ccpartyName;
    }

}
