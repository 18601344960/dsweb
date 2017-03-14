package org.tpri.sc.entity.obt;

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
import org.tpri.sc.entity.com.ComFile;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.UserMc;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>文章内容bean<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月2日
 */
@Entity
@Table(name = "OBT_CONFERENCE")
public class Conference extends ObjectBase {

    public Conference() {
        super();
        objectType = ObjectType.OBT_CONFERENCE;
    }

    public static int RECOMMEND_NO = 0;
    public static int RECOMMEND_YES = 1;

    public static int IS_BRAND_0 = 0;//不是工作品牌
    public static int IS_BRAND_1 = 1;//是工作品牌

    public static int STATUS_0 = 0;//未发布
    public static int STATUS_1 = 1;//发布
    public static int STATUS_2 = 2;//取消发布

    public static final int SOURCE_TYPE_0 = 0;//党组织
    public static final int SOURCE_TYPE_1 = 1;//党员个人

    public static int SECRET_LEVEL_0 = 0;//组织内部
    public static int SECRET_LEVEL_1 = 1;//本组织和上级
    public static int SECRET_LEVEL_2 = 2;//公开

    private static final long serialVersionUID = 1L;
    protected String ccpartyId; //文章所属组织ID
    protected Timestamp occurTime;//活动日期
    protected String address;//活动地点
    protected String chair;//主持人
    protected int attendance;//参会人数
    protected String content;//内容
    protected int hits;//点击数
    protected int isRecommend;//是否推荐：0否1是
    protected int isBrand;//是否工作品牌：0否1是
    protected int status;//状态：0未发布1发布2取消发布
    protected int sourceType;//SOURCE_TYPE
    protected String sourceId;//来源用户ID
    protected String sourceName;//来源用户名称
    protected String createUserId;//创建用户ID
    protected Timestamp createTime;//创建时间
    protected String updateUserId;//最后更新用户ID
    protected Timestamp updateTime;//最后修改时间
    protected String publishUserId;//发布人
    protected Timestamp publishTime;//发布时间
    protected int secretLevel = 0;//密级：0组织内部；1本组织和上级；2公开
    private Integer isTop = 0;

    protected int reply;//
    public int praiseCount;//点赞数
    public List<ComFile> files = new ArrayList<ComFile>(); //附件
    public List<ComFile> images = new ArrayList<ComFile>(); //图片附件
    public List<ConferenceLabel> conferenceLabels = new ArrayList<ConferenceLabel>(); //标签
    public List<ConferenceStep> conferenceSteps = new ArrayList<ConferenceStep>(); //步骤
    public List<ConferenceFormat> conferenceFormats = new ArrayList<ConferenceFormat>(); //生活形式
    public CCParty ccparty; //所属组织
    public int commentCount = 0;//评论数
    public UserMc createUser;
    public UserMc updateUser;
    public List<ConferenceOrgnizer> conferenceOrgnizers;//组织者
    public String orgnizers;//组织者
    public List<ConferenceParticipants> conferenceParticipants;//参加人员
    protected String participants;//参与人员

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

    @Column(name = "SOURCE_TYPE")
    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    @Column(name = "SOURCE_ID")
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Column(name = "SOURCE_NAME")
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
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

    @Transient
    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    @Transient
    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    @Column(name = "IS_RECOMMEND")
    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
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
    public List<ConferenceLabel> getConferenceLabels() {
        return conferenceLabels;
    }

    public void setConferenceLabels(List<ConferenceLabel> conferenceLabels) {
        this.conferenceLabels = conferenceLabels;
    }

    @Transient
    public List<ConferenceStep> getConferenceSteps() {
        return conferenceSteps;
    }

    public void setConferenceSteps(List<ConferenceStep> conferenceSteps) {
        this.conferenceSteps = conferenceSteps;
    }

    @Column(name = "CREATE_USER_ID")
    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Transient
    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    @Transient
    public List<ConferenceParticipants> getConferenceParticipants() {
        return conferenceParticipants;
    }

    public void setConferenceParticipants(List<ConferenceParticipants> conferenceParticipants) {
        this.conferenceParticipants = conferenceParticipants;
    }

    @Column(name = "OCCUR_TIME")
    public Timestamp getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Timestamp occurTime) {
        this.occurTime = occurTime;
    }

    @Column(name = "CHAIR")
    public String getChair() {
        return chair;
    }

    public void setChair(String chair) {
        this.chair = chair;
    }

    @Column(name = "ATTENDANCE")
    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    @Column(name = "CCPARTY_ID")
    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

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

    @Transient
    public CCParty getCcparty() {
        return ccparty;
    }

    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }

    @Transient
    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Column(name = "PUBLISH_USER_ID")
    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    @Column(name = "PUBLISH_TIME")
    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    @Column(name = "SECRET_LEVEL")
    public int getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(int secretLevel) {
        this.secretLevel = secretLevel;
    }

    @Column(name = "IS_BRAND")
    public int getIsBrand() {
        return isBrand;
    }

    public void setIsBrand(int isBrand) {
        this.isBrand = isBrand;
    }

    @Transient
    public List<ConferenceFormat> getConferenceFormats() {
        return conferenceFormats;
    }

    public void setConferenceFormats(List<ConferenceFormat> conferenceFormats) {
        this.conferenceFormats = conferenceFormats;
    }

    @Transient
    public List<ConferenceOrgnizer> getConferenceOrgnizers() {
        return conferenceOrgnizers;
    }

    public void setConferenceOrgnizers(List<ConferenceOrgnizer> conferenceOrgnizers) {
        this.conferenceOrgnizers = conferenceOrgnizers;
    }

    @Transient
    public String getOrgnizers() {
        return orgnizers;
    }

    public void setOrgnizers(String orgnizers) {
        this.orgnizers = orgnizers;
    }

    @Column(name = "IS_TOP")
    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

}
