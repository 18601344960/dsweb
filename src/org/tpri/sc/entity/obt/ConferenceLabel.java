package org.tpri.sc.entity.obt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * @description 文章所属标签bean
 * @author 易文俊
 * @since 2015-04-30
 */
@Entity
@Table(name = "OBT_CONFERENCE_LABEL")
public class ConferenceLabel extends ObjectBase {

    public ConferenceLabel() {
        super();
        objectType = ObjectType.OBT_CONFERENCE_LABEL;
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
