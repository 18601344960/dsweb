package org.tpri.sc.entity.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * @description 系统环境变量bean
 * @author 易文俊
 * @since 2015-04-30
 */

@Entity
@Table(name="SYS_ENVIRONMENT")
public class Environment  extends ObjectBase{
	
	private static final long serialVersionUID = buildSerialVersionUID(ObjectType.SYS_ENVIRONMENT);

	public static final int TYPE_STRING = 0; 	//字符串
	public static final int TYPE_INT = 1; 		//整数
	public static final int TYPE_FLOAT = 2; 		//浮点数
	public static final int TYPE_BOOLEAN = 3; 	//布尔值
	
	public static final int APPLICATION_0 = 0; 	//公用
	public static final int APPLICATION_1 = 1; 	//党建系统
	public static final int APPLICATION_2 = 2; 	//支部工作手册
	
	protected int type;
	protected String value;
	protected String description;
	
	public Environment(){
		super();
		objectType = ObjectType.SYS_ENVIRONMENT;
	}
	

	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="TYPE")
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Column(name="VALUE")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
