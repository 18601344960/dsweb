package org.tpri.sc.entity.ds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 试题库分类关系
 * 
 * @author zhaozijing
 *
 */

@Entity
@Table(name = "DS_QUESTION_CATEGORY")
public class QuestionCategory extends ObjectBase {
	private static final long serialVersionUID = 1L;

	public QuestionCategory() {
		super();
		objectType = ObjectType.DS_QUESTION_CATEGORY;
	}

	private String questionId;// 试题
	private String categoryId;// 类别

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

	@Column(name = "CATEGORY_ID")
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}
