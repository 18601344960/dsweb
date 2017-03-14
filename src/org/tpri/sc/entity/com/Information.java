package org.tpri.sc.entity.com;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.UserMc;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>工作必备bean<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */
@Entity
@Table(name = "COM_INFORMATION")
public class Information extends ObjectBase {

    private static final long serialVersionUID = 1L;

    public static int TYPE_0 = 0; //公共
    public static int TYPE_1 = 1; //自有

    public static int CATEGORY_1 = 1; //工作制度
    public static int CATEGORY_2 = 2; //工作要求
    
    public static int STATUS_0 = 0; //起草
    public static int STATUS_1 = 1; //发布
    public static int STATUS_2 = 2; //取消发布

    public Information() {
        super();
        objectType = ObjectType.COM_INFORMATION;
    }

    protected String content;
    protected int type;
    protected Integer category;
    protected String ccpartyId;
    protected int hits;
    protected int status;
    protected String createUserId;
    protected Timestamp createTime;
    protected String updateUserId;
    protected Timestamp updateTime;
    protected CCParty ccparty;
    protected UserMc createUser;
    protected String publishUserId;
    

    protected UserMc updateUser;
    protected Timestamp publishTime;

    public List<ComFile> files = new ArrayList<ComFile>();
    public List<ComFile> images = new ArrayList<ComFile>();

    @Transient
    public UserMc getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserMc createUser) {
        this.createUser = createUser;
    }

    @Transient
    public UserMc getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(UserMc updateUser) {
        this.updateUser = updateUser;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "HITS")
    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "CREATE_USER_ID")
    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Column(name = "CATEGORY")
    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Column(name = "CREATE_TIME")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_USER_ID")
    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Column(name = "UPDATE_TIME")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "CCPARTY_ID")
    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }
    @Column(name = "PUBLISH_TIME")
    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }
    @Column(name = "PUBLISH_USER_ID")
    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    @Transient
    public List<ComFile> getFiles() {
        return files;
    }

    public void setFiles(List<ComFile> files) {
        this.files = files;
    }

    @Transient
    public List<ComFile> getImages() {
        return images;
    }

    public void setImages(List<ComFile> images) {
        this.images = images;
    }

    @Transient
    public CCParty getCcparty() {
        return ccparty;
    }

    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }
    
    

}
