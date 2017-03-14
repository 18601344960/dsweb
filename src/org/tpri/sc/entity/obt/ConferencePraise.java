package org.tpri.sc.entity.obt;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * @description 点赞bean
 * @author 易文俊
 * @since 2015-05-12
 */
@Entity
@Table(name = "OBT_CONFERENCE_PRAISE")
public class ConferencePraise extends ObjectBase {
    private static final long serialVersionUID = 1L;

    public ConferencePraise() {
        super();
        objectType = ObjectType.OBT_CONFERENCE_PRAISE;
    }

    public static int TYPE_PRAISE = 0;
    public static int TYPE_FAVORITE = 1;

    protected int type;
    protected String userId;
    protected Timestamp createTime;
    protected Conference article;
    
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "CREATE_TIME")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @ManyToOne
    @JoinColumn(name = "CONFERENCE_ID")
    public Conference getArticle() {
        return article;
    }

    public void setArticle(Conference article) {
        this.article = article;
    }
    
    
}
