package org.tpri.sc.entity.uam;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * @description 角色bean
 * @author 常华荣
 * @since 2015-04-09
 */

@Entity
@Table(name = "UAM_ROLE")
public class Role extends ObjectBase {
	/** */
	private static final long serialVersionUID = 1L;

	public static int ISSHOW_0 = 0;		//显示
	public static int ISSHOW_1 = 1;		//隐藏

	public static String ROLE_SUPER_ADMINISTRATOR = "ADMINISTRATOR";	    //超级管理员角色
	public static String ROLE_ADMINISTRATOR = "ROLE_1001"; //系统管理员（各组织）
	public static String ROLE_PARTYWORKER = "ROLE_1002";//党务干部
	public static String ROLE_PARTYMEMBER_DEFAULT = "ROLE_1003";	//党员默认角色

	protected String id;
	protected String name;
	protected String description;
	protected int status;
	protected int isShow;

	private List<Privilege> privileges; //角色下的权限
	
	public Role() {
		super();
		objectType = ObjectType.UAM_ROLE;
	}

	@Id
	@Column(name = "ID")
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

	 @Column(name = "IS_SHOW")
    public int getIsShow() {
        return isShow;
    }
   
    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
    
    @Transient
    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
	
	
}
