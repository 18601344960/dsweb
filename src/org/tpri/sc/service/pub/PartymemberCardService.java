package org.tpri.sc.service.pub;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.service.obt.ConferenceParticipantsService;
import org.tpri.sc.view.obt.PartyMemberCardPageView;

/**
 * 
 * <B>系统名称：</B>党员电子活动证服务类<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年7月18日
 */
@Service("PartymemberCardService")
public class PartymemberCardService {
    @Autowired
    private ConferenceParticipantsService conferenceParticipantsService;

    /**
     * 
     * <B>方法名称：</B>获取党员电子活动证分页信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月18日
     * @param userId
     * @param paramter
     * @param pages
     * @return
     */
    public PartyMemberCardPageView getPartymemberCards(String userId, String paramter) {
        //分页构造函数，动态设置栏目
        PartyMemberCardPageView page = new PartyMemberCardPageView(paramter);
        if (StringUtils.isEmpty(userId)) {
            return null;
        }

        if (!StringUtils.isEmpty(paramter)) {
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            JSONObject o = ja.getJSONObject(0);
            JSONArray cardConfigs = o.getJSONArray("cardConfig"); //计算需要哪些目录
            if (cardConfigs != null && cardConfigs.size() > 0) {
                for (int i = 0; i < cardConfigs.size(); i++) {
                    switch (Integer.parseInt(String.valueOf(cardConfigs.get(i)))) {
                    case 3:
                        //组织生活记录数获取
                        conferenceParticipantsService.getCardPagesForBranchConference(userId, paramter, page);
                        break;
                    default:
                        break;
                    }
                }
            }
        }
        return page;
    }
}
