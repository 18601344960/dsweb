package org.tpri.sc.view.org;

import java.util.List;

import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.view.obt.PartyMemberStatisticsView;

/**
 * 
 * <B>系统名称：</B>党组织信息<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年5月25日
 */
public class CcpartyCardInfo {

    private CCParty ccparty; //组织信息
    private PartyMemberStatisticsView view;//数据统计
    private List<CCPartyTitleView> titleViews;//领导班子

    public CCParty getCcparty() {
        return ccparty;
    }

    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }

    public PartyMemberStatisticsView getView() {
        return view;
    }

    public void setView(PartyMemberStatisticsView view) {
        this.view = view;
    }

    public List<CCPartyTitleView> getTitleViews() {
        return titleViews;
    }

    public void setTitleViews(List<CCPartyTitleView> titleViews) {
        this.titleViews = titleViews;
    }

}
