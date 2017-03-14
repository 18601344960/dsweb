package org.tpri.sc.entity.obt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * <B>系统名称：</B>文章所属生活形式bean<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月17日
 */
@Entity
@Table(name = "OBT_CONFERENCE_FORMAT")
public class ConferenceFormat extends ObjectBase {

    public ConferenceFormat() {
        super();
        objectType = ObjectType.OBT_CONFERENCE_FORMAT;
    }

    private static final long serialVersionUID = 1L;
    protected String conferenceId;
    protected String categoryId;
    protected ConferenceCategory category;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "CONFERENCE_ID")
    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    @Column(name = "CATEGORY_ID")
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Transient
    public ConferenceCategory getCategory() {
        return category;
    }

    public void setCategory(ConferenceCategory category) {
        this.category = category;
    }
}
