package org.tpri.sc.service.obt;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.ConferenceOrgnizer;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.ConferenceOrgnizerManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.util.UUIDUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织生活组织者服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年6月24日
 */
@Service("ConferenceOrgnizerService")
public class ConferenceOrgnizerService {

    public Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ConferenceOrgnizerManager conferenceOrgnizerManager;
    @Autowired
    private UserManager userManager;

    /**
     * <B>方法名称：</B>增加组织生活本组织组织者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param param
     * @return
     */
    public boolean addConferenceInOrgnizer(Map<String, Object> param) {
        JSONArray userIds = JSONArray.fromObject((String) param.get("userIds"));
        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.getString(i);
            String conferenceId = (String) param.get("conferenceId");
            ConferenceOrgnizer conferenceOrgnizer = conferenceOrgnizerManager.getOrgnizerByConferenceIdAndUserId(conferenceId, userId);
            if (conferenceOrgnizer == null) {
                conferenceOrgnizer = new ConferenceOrgnizer();
                conferenceOrgnizer.setId(UUIDUtil.id());
                conferenceOrgnizer.setConferenceId(conferenceId);
                conferenceOrgnizer.setUserId(userId);
                UserMc user = userManager.getUserFromMc(userId);
                if (user != null) {
                    conferenceOrgnizer.setUserName(user.getName());
                }
                conferenceOrgnizer.setType(ConferenceOrgnizer.TYPE_0);
                conferenceOrgnizerManager.add(conferenceOrgnizer);
            }

        }
        return true;
    }

    /**
     * <B>方法名称：</B>增加组织生活组织外组织者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param param
     * @return
     */
    public boolean addConferenceOutOrgnizer(Map<String, Object> param) {
        JSONArray userNames = JSONArray.fromObject((String) param.get("userNames"));
        for (int i = 0; i < userNames.size(); i++) {
            String userName = userNames.getString(i);
            ConferenceOrgnizer participant = new ConferenceOrgnizer();
            participant.setId(UUIDUtil.id());
            participant.setConferenceId((String) param.get("conferenceId"));
            participant.setUserName(userName);
            participant.setType(ConferenceOrgnizer.TYPE_1);
            conferenceOrgnizerManager.add(participant);
        }
        return true;
    }

    /**
     * <B>方法名称：</B>获取某组织生活的组织者列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param offset
     * @param limit
     * @param conferenceId
     * @param status
     * @return
     */
    public List<ConferenceOrgnizer> getConferenceOrgnizer(Integer offset, Integer limit, String conferenceId) {
        List<ConferenceOrgnizer> orgnizer = conferenceOrgnizerManager.getConferenceOrgnizer(offset, limit, conferenceId);
        if (orgnizer != null && orgnizer.size() > 0) {
            for (ConferenceOrgnizer conferenceOrgnizer : orgnizer) {
                conferenceOrgnizer.setUser(userManager.getUserFromMc(conferenceOrgnizer.getUserId()));
            }
        }
        return orgnizer;
    }

    /**
     * <B>方法名称：</B>获取某组织生活的组织者总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param conferenceId
     * @return
     */
    public Integer getConferenceOrgnizerTotal(String conferenceId) {
        return conferenceOrgnizerManager.getConferenceOrgnizerTotal(conferenceId);
    }

    /**
     * <B>方法名称：</B>删除组织生活组织者<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月24日
     * @param request
     * @return
     */
    public boolean deleteConferenceOrgnizer(String id) {
        ConferenceOrgnizer conferenceOrgnizer = conferenceOrgnizerManager.getConferenceOrgnizer(id);
        if (conferenceOrgnizer == null) {
            return false;
        }
        conferenceOrgnizerManager.delete(conferenceOrgnizer);
        return true;
    }

}
