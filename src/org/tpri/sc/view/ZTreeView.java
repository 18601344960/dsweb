package org.tpri.sc.view;

/**
 * @description ztree树节点视图对象
 * @author 易文俊
 * @since 2015-04-24
 */
public class ZTreeView {

    public static final String DEPARTMENT_TREE_ICON = "images/ztree/department.png"; //部门Icon
    public static final String ORGANIZATION_TREE_ICON = "images/ztree/national_emblem_16px.png"; //行政组织树Icon
    public static final String CCPARTY_GENERAL_TREE_ICON_EMBLEM = "images/ztree/emblem_16px.png"; //支部Icon
    public static final String CCPARTY_GENERAL_TREE_ICON_FLAG = "images/ztree/Flag_Red_16px.png"; //支部Icon
    public static final String CCPARTY_USER_TREE_ICON = "images/ztree/user.png"; //支部Icon
    public static final String UNION_ICON = "images/ztree/union.png"; //工会Icon
    public static final String YOUTH_ICON = "images/ztree/youth.png"; //团委Icon
    public static final String WOMAN_ICON = "images/ztree/woman.png"; //妇工委Icon

    private String id;
    private String name;
    private String pId;
    private String icon;
    private boolean open = false;
    private boolean isParent;
    private String type;//类型
    private String attr1;//自定义属性1
    private String attr2;//自定义属性2
    private int attr3;//自定义属性3
    private int attr4;//自定义属性4

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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public int getAttr3() {
        return attr3;
    }

    public void setAttr3(int attr3) {
        this.attr3 = attr3;
    }

    public int getAttr4() {
        return attr4;
    }

    public void setAttr4(int attr4) {
        this.attr4 = attr4;
    }
    
    

}
