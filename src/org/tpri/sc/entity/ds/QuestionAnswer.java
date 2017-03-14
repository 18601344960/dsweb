package org.tpri.sc.entity.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 试题库答案
 * 
 * @author zhaozijing
 *
 */

@Entity
@Table(name = "DS_QUESTION_ANSWER")
public class QuestionAnswer extends ObjectBase {
	private static final long serialVersionUID = 1L;

	public QuestionAnswer() {
		super();
		objectType = ObjectType.DS_QUESTION_ANSWER;
	}

	private String questionId;// 试题
	private String answer;// 答案

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

	@Column(name = "ANSWER")
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
