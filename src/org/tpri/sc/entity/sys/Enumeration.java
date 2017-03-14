package org.tpri.sc.entity.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * @description 枚举bean
 * @author 易文俊
 * @since 2015-06-30
 */

@Entity
@Table(name="SYS_ENUMERATION")
public class Enumeration  extends ObjectBase{
	
	private static final long serialVersionUID = buildSerialVersionUID(ObjectType.SYS_ENUMERATION);

	protected int status;
	
	public Enumeration(){
		super();
		objectType = ObjectType.SYS_ENUMERATION;
	}
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name="STATUS")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
