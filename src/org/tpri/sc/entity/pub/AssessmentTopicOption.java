package org.tpri.sc.entity.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答题答卷选项<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年8月10日
 */
@Entity
@Table(name = "PUB_ASSESSMENT_TOPIC_OPTION")
public class AssessmentTopicOption extends ObjectBase {
    private static final long serialVersionUID = 1L;
    protected int objectType = ObjectType.PUB_ASSESSMENT_TOPIC_OPTION;

    protected String topicId; //试题ID
    protected String content; //选项内容
    protected String seq; //选项序号 	ABCD

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "TOPIC_ID")
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "SEQ")
    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

}
