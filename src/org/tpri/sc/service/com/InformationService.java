package org.tpri.sc.service.com;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.com.Announcement;
import org.tpri.sc.entity.com.ComFile;
import org.tpri.sc.entity.com.Information;
import org.tpri.sc.entity.com.TableIndex;
import org.tpri.sc.entity.obt.Conference;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.com.ComFileManager;
import org.tpri.sc.manager.com.InformationManager;
import org.tpri.sc.manager.obt.ConferenceManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.FileService;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.com.HomeStatisticsNumView;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>工作必备服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */

@Service("InformationService")
public class InformationService {

    @Autowired
    InformationManager informationManager;
    @Autowired
    private FileService fileService;
    @Autowired
    ComFileService comFileService;
    @Autowired
    ComFileManager comFileManager;
    @Autowired
    UserManager userManager;
    @Autowired
    CCPartyManager ccpartyManager;
    @Autowired
    private ConferenceManager conferenceManager;

    public List<Information> getInformationList(Integer category, Integer status, String ccpartyId, String search, Integer offset, Integer limit, boolean isShow) {
        List<Information> list = new ArrayList<Information>();
        list = informationManager.getInformationList(category, status, ccpartyId, search, offset, limit, isShow);
        for (Information information : list) {
            information.setCreateUser(userManager.getUserFromMc(information.getCreateUserId()));
            information.setUpdateUser(userManager.getUserFromMc(information.getUpdateUserId()));
            information.setCcparty(ccpartyManager.getCCPartyFromMc(information.getCcpartyId()));
        }
        return list;
    }

    public int getInformationTotal(Integer category, Integer status, String ccpartyId, String search, boolean isShow) {
        return informationManager.getInformationTotal(category, status, ccpartyId, search, isShow);
    }

    public Information getInformationById(String id) {
        Information information = informationManager.getInformationById(id);
        List<ComFile> files = comFileManager.getFileList(TableIndex.TABLE_COM_INFORMATION.getType(), id);
        information.setFiles(files);
        List<ComFile> images = comFileManager.getFileList(TableIndex.TABLE_COM_INFORMATION.getType(), id, ComFile.FILETYPE_IMAGE);
        information.setImages(images);
        information.setCreateUser(userManager.getUserFromMc(information.getCreateUserId()));
        information.setUpdateUser(userManager.getUserFromMc(information.getUpdateUserId()));
        return information;
    }

    public boolean addInformation(UserMc user, Map<String, Object> param) {
        Information information = new Information();
        String id = UUIDUtil.id();
        information.setId(id);
        information.setCategory((Integer) param.get("category"));
        information.setType((Integer) param.get("type"));
        information.setName((String) param.get("name"));
        information.setContent((String) param.get("content"));
        information.setCcpartyId((String) param.get("ccpartyId"));
        information.setCreateUserId(user.getId());
        information.setCreateTime(new Timestamp(System.currentTimeMillis()));
        informationManager.add(information);

        JSONArray files = JSONArray.fromObject((String) param.get("files"));
        comFileService.saveFilesFromJSONArray(id, TableIndex.TABLE_COM_INFORMATION, files);
        return true;
    }

    public boolean updateInformation(UserMc user, Map<String, Object> param) {
        String id = (String) param.get("id");
        Information information = informationManager.getInformationById(id);
        information.setName((String) param.get("name"));
        information.setType((Integer) param.get("type"));
        information.setContent((String) param.get("content"));
        information.setCreateUserId(user.getId());
        information.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        informationManager.saveOrUpdate(information);

        JSONArray files = JSONArray.fromObject((String) param.get("files"));
        comFileService.saveFilesFromJSONArray(id, TableIndex.TABLE_COM_INFORMATION, files);
        return true;
    }

    public boolean deleteInformation(UserMc user, String id) {
        informationManager.deleteInformation(id);
        List<ComFile> comFiles = comFileManager.getFileList(TableIndex.TABLE_COM_INFORMATION.getType(), id);
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
        Information information = informationManager.getInformationById(id);
        information.setHits(information.getHits() + 1);
        return informationManager.update(information);
    }

    /**
     * 
     * <B>方法名称：</B>获取首页数据统计<BR>
     * <B>概要说明：</B>统计所有数据和统计最近一个月数据<BR>
     * 
     * @author 赵子靖
     * @since 2016年8月4日
     * @param ccpartyId
     * @return
     */
    public HomeStatisticsNumView getHomeStatisticsNums(String ccpartyId) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(date); //现在日期
        String afterMonthDate = String.valueOf(DateUtil.getBeforeAfterDate(nowDate, 30)); //前一个月日期

        HomeStatisticsNumView view = new HomeStatisticsNumView();
        //工作共享
        List<Conference> allShares = conferenceManager.getConferences(null, null, ccpartyId, null);
        List<Conference> monthShares = conferenceManager.getConferences(nowDate, afterMonthDate, ccpartyId, null);
        int shareBrandNum = 0;
        int shareRecomment = 0;
        int shareSumNum = 0;
        int monthShareNum = 0;
        if (allShares != null && allShares.size() > 0) {
            for (int i = 0; i < allShares.size(); i++) {
                if (allShares.get(i).getIsBrand() == Conference.IS_BRAND_1) {
                    shareBrandNum += 1;
                }
                if (allShares.get(i).getIsRecommend() == Conference.RECOMMEND_YES) {
                    shareRecomment += 1;
                }
                shareSumNum += 1;
            }
        }
        if (monthShares != null && monthShares.size() > 0) {
            monthShareNum = monthShares.size();
        }
        view.setShareStr("共发布 " + shareSumNum + "篇，其中品牌" + shareBrandNum + "篇、推荐" + shareRecomment + "篇，近一月发布" + monthShareNum + "篇。");
        //工作必备
        int infoBaseNum = 0;
        int infoRequireNum = 0;
        int infoSumNum = 0;
        List<Information> informations = informationManager.getInforamations(ccpartyId);
        if (informations != null && informations.size() > 0) {
            for (int i = 0; i < informations.size(); i++) {
                if (informations.get(i).getCategory() == Information.CATEGORY_1) {
                    //工作制度
                    infoBaseNum += 1;
                } else if (informations.get(i).getCategory() == Information.CATEGORY_2) {
                    //工作要求
                    infoRequireNum += 1;
                }
                infoSumNum += 1;
            }
        }
        view.setWorkStr("共发布" + infoSumNum + "篇，其中基本制度" + infoBaseNum + "篇、工作要求" + infoRequireNum + "篇。");
        //工作品牌
        List<Conference> allBrands = conferenceManager.getConferences(null, null, ccpartyId, Conference.IS_BRAND_1);
        List<Conference> monthBrands = conferenceManager.getConferences(nowDate, afterMonthDate, ccpartyId, Conference.IS_BRAND_1);
        int brandSumNum = 0;
        int monthBrandNum = 0;
        if (allBrands != null && allBrands.size() > 0) {
            brandSumNum = allBrands.size();
        }
        if (monthBrands != null && monthBrands.size() > 0) {
            monthBrandNum = monthBrands.size();
        }
        view.setBrandStr("共发布" + brandSumNum + "篇，近一月发布" + monthBrandNum + "篇。");
        return view;
    }

    /**
     * <B>方法名称：</B>发布工作必备<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 张波
     * @since 2016年7月25日
     * @param id
     * @return
     */
    public void publishInformation(UserMc user, String id) {
        Information information = informationManager.getInformationById(id);
        information.setStatus(Information.STATUS_1);
        information.setPublishUserId(user.getId());
        informationManager.saveOrUpdate(information);
        //return true;
    }

    /**
     * <B>方法名称：</B>取消发布工作必备<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 张波
     * @since 2016年7月25日
     * @param id
     * @return
     */
    public boolean unpublishInformation(UserMc user, String id) {
        Information information = informationManager.getInformationById(id);
        information.setStatus(Information.STATUS_2);
        informationManager.saveOrUpdate(information);
        return true;
    }

}
