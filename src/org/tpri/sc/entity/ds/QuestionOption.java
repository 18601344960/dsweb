package org.tpri.sc.entity.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 试题库选项
 * 
 * @author zhaozijing
 *
 */

@Entity
@Table(name = "DS_QUESTION_OPTION")
public class QuestionOption extends ObjectBase {
	private static final long serialVersionUID = 1L;

	public QuestionOption() {
		super();
		objectType = ObjectType.DS_QUESTION_OPTION;
	}

	private String questionId;// 试题
	private String content;// 选项内容
	private String sequence;// 选项索引 A、B、C、D等

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "QUESTION_ID")
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "SEQUENCE")
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

}
