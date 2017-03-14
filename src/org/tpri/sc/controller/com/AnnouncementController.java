package org.tpri.sc.controller.com;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.com.Announcement;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.FileService;
import org.tpri.sc.service.com.AnnouncementService;
import org.tpri.sc.service.org.CCpartyService;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>通知通告控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */
@Controller
@RequestMapping("/com")
public class AnnouncementController extends BaseController {

    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CCpartyService ccpartyService;

    /**
     * <B>方法名称：</B>获取通知通告列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("getAnnouncementList")
    @ResponseBody
    public Map<String, Object> getAnnouncementList(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAnnouncementList begin");
        String ccpartyId = getString(request, "ccpartyId");
        String search = getString(request, "search");
        Integer status = getInteger(request, "status");
        Integer offset = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        String ccpartyIds = "";
        List<Announcement> announcements = new ArrayList<Announcement>();
        int total = 0;
        if (ccpartyId != null && !ccpartyId.equals("")) {
            ccpartyIds = ccpartyService.getCCPartyIdStrsAndLowerLevel(ccpartyId);
            List<String> ccpartyIdList = new ArrayList<String>();
            if (ccpartyIds.length() > 0) {
                String[] ccpartyIdStr = ccpartyIds.split(",");
                for (int i = 0; i < ccpartyIdStr.length; i++) {
                    ccpartyIdList.add(ccpartyIdStr[i]);
                }
            }
            announcements = announcementService.getAnnouncementList(status, ccpartyIdList, search, offset, limit);
            total = announcementService.getAnnouncementTotal(status, ccpartyIdList, search);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("rows", announcements);
        ret.put("total", total);
        logger.debug(this.getClass() + " getAnnouncementList end");
        return ret;
    }

    /**
     * <B>方法名称：</B>根据ID获取通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("getAnnouncementById")
    @ResponseBody
    public Map<String, Object> getAnnouncementById(HttpServletRequest request) {
        logger.debug(this.getClass() + " getAnnouncementById begin");
        String id = getString(request, "id");
        boolean isView = getBoolean(request, "isView");
        Announcement announcement = announcementService.getAnnouncementById(id);
        if (isView) {
            announcementService.updateHits(id);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", announcement);
        logger.debug(this.getClass() + " getAnnouncementById end");
        return ret;
    }

    /**
     * 获取最新一条通知通告
     * @param request
     * @return
     */
    @RequestMapping("getFirstAnnouncement")
    @ResponseBody
    public Map<String, Object> getFirstAnnouncement(HttpServletRequest request) {
        logger.debug(this.getClass() + " getFirstAnnouncement begin");
        Announcement announcement = announcementService.getFirstAnnouncement(getString(request, "ccpartyId"));
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("item", announcement);
        logger.debug(this.getClass() + " getFirstAnnouncement end");
        return ret;
    }
    
    /**
     * <B>方法名称：</B>添加通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("addAnnouncement")
    @ResponseBody
    public Map<String, Object> addAnnouncement(HttpServletRequest request) throws IOException {
        logger.debug(this.getClass() + " addAnnouncement begin");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("category", getInteger(request, "category"));
        param.put("type", getInteger(request, "type") == null ? Announcement.TYPE_1 : getInteger(request, "type"));
        param.put("name", getString(request, "name"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("content", getString(request, "content"));
        param.put("files", getString(request, "files"));

        UserMc user = loadUserMc(request);
        boolean result = announcementService.addAnnouncement(user, param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        ret.put("msg", "保存成功");
        logger.debug(this.getClass() + " addAnnouncement end");
        return ret;
    }

    /**
     * <B>方法名称：</B>更新通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     * @throws ParseException
     * @throws IOException
     */
    @RequestMapping("updateAnnouncement")
    @ResponseBody
    public Map<String, Object> updateAnnouncement(HttpServletRequest request) throws ParseException, IOException {
        logger.debug(this.getClass() + " updateAnnouncement begin");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", getString(request, "id"));
        param.put("type", getInteger(request, "type") == null ? Announcement.TYPE_1 : getInteger(request, "type"));
        param.put("category", getInteger(request, "category"));
        param.put("name", getString(request, "name"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        param.put("content", getString(request, "content"));
        param.put("files", getString(request, "files"));

        UserMc user = loadUserMc(request);
        boolean result = announcementService.updateAnnouncement(user, param);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", result);
        ret.put("msg", "保存成功");
        logger.debug(this.getClass() + " updateAnnouncement end");
        return ret;
    }

    /**
     * <B>方法名称：</B>删除通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("deleteAnnouncement")
    @ResponseBody
    public Map<String, Object> deleteAnnouncement(HttpServletRequest request) {
        logger.debug("AnnouncementController deleteAnnouncement begin");
        String id = getString(request, "id");
        UserMc user = loadUserMc(request);
        announcementService.deleteAnnouncement(user, id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "删除成功");
        logger.debug("AnnouncementController deleteAnnouncement end");
        return ret;
    }

    /**
     * <B>方法名称：</B>发布通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("publishAnnouncement")
    @ResponseBody
    public Map<String, Object> publishAnnouncement(HttpServletRequest request) {
        logger.debug("AnnouncementController publishAnnouncement begin");
        String id = getString(request, "id");
        UserMc user = loadUserMc(request);
        announcementService.publishAnnouncement(user, id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "发布成功");
        logger.debug("AnnouncementController publishAnnouncement end");
        return ret;
    }

    /**
     * <B>方法名称：</B>取消发布通知通告<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月1日
     * @param request
     * @return
     */
    @RequestMapping("unpublishAnnouncement")
    @ResponseBody
    public Map<String, Object> unpublishAnnouncement(HttpServletRequest request) {
        logger.debug("AnnouncementController unpublishAnnouncement begin");
        String id = getString(request, "id");
        UserMc user = loadUserMc(request);
        announcementService.unpublishAnnouncement(user, id);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("msg", "取消发布成功");
        logger.debug("AnnouncementController unpublishAnnouncement end");
        return ret;
    }

}
