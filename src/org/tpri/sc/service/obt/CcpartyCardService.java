package org.tpri.sc.service.obt;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.view.org.CcpartyCardPageView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B>组织支部手册分页服务类<BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年7月19日
 */
@Service("CcpartyCardService")
public class CcpartyCardService {
    @Autowired
    private ConferenceService conferenceService;
    
    /**
     * 
     * <B>方法名称：</B>获取支部工作分页信息<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年7月19日 	
     * @param ccpartyId
     * @param paramter
     * @return
     */
    public CcpartyCardPageView getCcpartyCardPage(String ccpartyId,String paramter){
      //分页构造函数，动态设置栏目
        CcpartyCardPageView page = new CcpartyCardPageView(paramter);
        if (StringUtils.isEmpty(ccpartyId)) {
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
                        conferenceService.getCardPagesForConference(ccpartyId, paramter, page);
                        break;
                    default:
                        break;
                    }
                }
            }
        }
        return page;
        
        
//        if (!StringUtils.isEmpty(ccpartyId)) {
//            //支部工作记录数获取
//            branchConferenceService.getCardPagesForConference(ccpartyId, year, page);
//            //党费收缴页数
//            page.setPartyFeeBeginPage(page.getBranchConferenceEndPage() + 1);
//            page.setPartyFeePages(0);
//            page.setPages(page.getPages() + page.getPartyFeePages()); //计算总页数 
//            page.setPartyFeeEndPage(page.getPartyFeeBeginPage());
//        }
    }
    
}
