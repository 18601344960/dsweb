package org.tpri.sc.entity.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 试题库
 * 
 * @author zhaozijing
 *
 */

@Entity
@Table(name = "DS_QUESTION")
public class Question extends ObjectBase {
	private static final long serialVersionUID = 1L;

	public Question() {
		super();
		objectType = ObjectType.DS_QUESTION;
	}

	public static int TYPE_1 = 1;// 单选题
	public static int TYPE_2 = 2;// 多选题
	public static int TYPE_3 = 3;// 判断题

	public static int STATUS_0 = 0;// 正常
	public static int STATUS_1 = 1;// 停用

	private String title;// 标题
	private int type;// 试题类型 1单选题2多选题3判断题
	private int status;// 状态：0正常1停用
	private int sequence;// 题号

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "TYPE")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "STATUS")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "SEQUENCE")
	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
