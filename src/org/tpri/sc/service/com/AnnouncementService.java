package org.tpri.sc.service.com;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.com.Announcement;
import org.tpri.sc.entity.com.ComFile;
import org.tpri.sc.entity.com.TableIndex;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.com.AnnouncementManager;
import org.tpri.sc.manager.com.ComFileManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.FileService;
import org.tpri.sc.util.UUIDUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>通知通告服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */

@Service("AnnouncementService")
public class AnnouncementService {

    @Autowired
    private AnnouncementManager announcementManager;
    @Autowired
    private FileService fileService;
    @Autowired
    private ComFileService comFileService;
    @Autowired
    private ComFileManager comFileManager;
    @Autowired
    private UserManager userManager;

    /**
     * <B>方法名称：</B>获取通知通告列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param status
     * @param ccpartyIds
     * @param search
     * @param offset
     * @param limit
     * @return
     */
    public List<Announcement> getAnnouncementList(Integer status, List<String> ccpartyIdList, String search, Integer offset, Integer limit) {
        List<Announcement> list = new ArrayList<Announcement>();
        list = announcementManager.getAnnouncementList(status, ccpartyIdList, search, offset, limit);
        for (Announcement announcement : list) {
            announcement.setCreateUser(userManager.getUserFromMc(announcement.getCreateUserId()));
            announcement.setUpdateUser(userManager.getUserFromMc(announcement.getUpdateUserId()));
        }
        return list;
    }

    /**
     * <B>方法名称：</B>获取通知通告总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param status
     * @param ccpartyIds
     * @param search
     * @return
     */
    public int getAnnouncementTotal(Integer status, List<String> ccpartyIdList, String search) {
        return announcementManager.getAnnouncementTotal(status, ccpartyIdList, search);
    }

    /**
     * <B>方法名称：</B>获取通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param id
     * @return
     */
    public Announcement getAnnouncementById(String id) {
        Announcement announcement = announcementManager.getAnnouncementById(id);
        List<ComFile> files = comFileManager.getFileList(TableIndex.TABLE_COM_ANNOUNCEMENT.getType(), id);
        announcement.setFiles(files);
        List<ComFile> images = comFileManager.getFileList(TableIndex.TABLE_COM_ANNOUNCEMENT.getType(), id, ComFile.FILETYPE_IMAGE);
        announcement.setImages(images);
        announcement.setCreateUser(userManager.getUserFromMc(announcement.getCreateUserId()));
        announcement.setUpdateUser(userManager.getUserFromMc(announcement.getUpdateUserId()));
        return announcement;
    }
    
    /**
     * 获取最新一条通知通告
     * @param ccpartyId
     * @return
     */
    public Announcement getFirstAnnouncement(String ccpartyId){
    	Announcement announcement = announcementManager.getFirstAnnouncement(ccpartyId);
    	return announcement;
    }

    /**
     * <B>方法名称：</B>添加通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param user
     * @param param
     * @return
     */
    public boolean addAnnouncement(UserMc user, Map<String, Object> param) {
        Announcement announcement = new Announcement();
        String id = UUIDUtil.id();
        announcement.setId(id);
        announcement.setName((String) param.get("name"));
        announcement.setContent((String) param.get("content"));
        announcement.setStatus(Announcement.STATUS_0);
        announcement.setCcpartyId((String) param.get("ccpartyId"));
        announcement.setCreateUserId(user.getId());
        announcement.setCreateTime(new Timestamp(System.currentTimeMillis()));
        announcementManager.add(announcement);

        JSONArray files = JSONArray.fromObject((String) param.get("files"));
        comFileService.saveFilesFromJSONArray(id, TableIndex.TABLE_COM_ANNOUNCEMENT, files);
        return true;
    }

    /**
     * <B>方法名称：</B>更新通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param user
     * @param param
     * @return
     */
    public boolean updateAnnouncement(UserMc user, Map<String, Object> param) {
        String id = (String) param.get("id");
        Announcement announcement = announcementManager.getAnnouncementById(id);
        announcement.setName((String) param.get("name"));
        announcement.setContent((String) param.get("content"));
        announcement.setUpdateUserId(user.getId());
        announcement.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        announcementManager.saveOrUpdate(announcement);

        JSONArray files = JSONArray.fromObject((String) param.get("files"));
        comFileService.saveFilesFromJSONArray(id, TableIndex.TABLE_COM_ANNOUNCEMENT, files);
        return true;
    }

    /**
     * <B>方法名称：</B>删除通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param user
     * @param id
     * @return
     */
    public boolean deleteAnnouncement(UserMc user, String id) {
        announcementManager.deleteAnnouncement(id);
        List<ComFile> comFiles = comFileManager.getFileList(TableIndex.TABLE_COM_ANNOUNCEMENT.getType(), id);
        for (ComFile comFile : comFiles) {
            String fileId = comFile.getId();
            comFileService.deleteFile(user, fileId);
        }
        return true;
    }

    /**
     * <B>方法名称：</B>修改点击数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param id
     * @return
     */
    public boolean updateHits(String id) {
        Announcement announcement = announcementManager.getAnnouncementById(id);
        announcement.setHits(announcement.getHits() + 1);
        return announcementManager.update(announcement);
    }

    /**
     * <B>方法名称：</B>发布通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param user
     * @param id
     * @return
     */
    public boolean publishAnnouncement(UserMc user, String id) {
        Announcement announcement = announcementManager.getAnnouncementById(id);
        announcement.setStatus(Announcement.STATUS_1);
        announcement.setPublishTime(new Timestamp(System.currentTimeMillis()));
        announcementManager.saveOrUpdate(announcement);
        return true;
    }

    /**
     * <B>方法名称：</B>取消发布通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param user
     * @param id
     * @return
     */
    public boolean unpublishAnnouncement(UserMc user, String id) {
        Announcement announcement = announcementManager.getAnnouncementById(id);
        announcement.setStatus(Announcement.STATUS_2);
        announcement.setPublishTime(null);
        announcementManager.saveOrUpdate(announcement);
        return true;
    }

}
