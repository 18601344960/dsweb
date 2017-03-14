package org.tpri.sc.service.obt;

import java.sql.Timestamp;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.ConferencePraise;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.ConferenceManager;
import org.tpri.sc.manager.obt.ConferencePraiseManager;
import org.tpri.sc.util.UUIDUtil;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>点赞服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月9日
 */

@Service("ConferencePraiseService")
public class ConferencePraiseService {

    @Autowired
    ConferencePraiseManager praiseManager;
    @Autowired
    private ConferenceManager articleManager;

    /**
     * 
     * <B>方法名称：</B>获取用户对某文章的点赞或收藏<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年4月9日
     * @param userId
     * @param articleId
     * @param type
     * @return
     */
    public ConferencePraise getPraise(String userId, String articleId, int type) {
        ConferencePraise praise = praiseManager.getPraise(userId, articleId, type);
        return praise;
    }

    /**
     * 
     * <B>方法名称：</B>添加点赞或收藏<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年4月9日
     * @param user
     * @param articleId
     * @param type
     * @return
     */
    public boolean addPraise(UserMc user, String articleId, int type) {
        ConferencePraise praise = new ConferencePraise();
        praise.setId(UUIDUtil.id());
        praise.setArticle(articleManager.getConferenceById(articleId));
        praise.setType(type);
        praise.setUserId(user.getId());
        praise.setCreateTime(new Timestamp(System.currentTimeMillis()));
        praiseManager.add(praise);
        return true;
    }

    /**
     * 
     * <B>方法名称：</B> 删除点赞或收藏<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年4月9日
     * @param user
     * @param ids
     * @return
     */
    public boolean deletePraise(UserMc user, JSONArray ids) {
        for (int i = 0; i < ids.size(); i++) {
            String praiseId = ids.getString(i);
            praiseManager.deletePraise(praiseId);
        }
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>获取我的点赞列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月17日
     * @param search
     * @param offset
     * @param limit
     * @param userId
     * @return
     */
    public List<ConferencePraise> getMyPraiseList(String search, Integer offset, Integer limit, String userId) {
        List<ConferencePraise> praises = praiseManager.getMyPraiseList(search, offset, limit, userId);
        return praises;
    }

    /**
     * 
     * <B>方法名称：</B>获取我的点赞列表记录数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月17日
     * @param search
     * @param userId
     * @return
     */
    public Integer getMyPraiseListTotal(String search, String userId) {
        return praiseManager.getMyPraiseListTotal(search, userId);
    }
}
