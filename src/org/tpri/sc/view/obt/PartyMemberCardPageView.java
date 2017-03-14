package org.tpri.sc.view.obt;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B>党员电子活动证分页视图<BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年7月18日
 */
public class PartyMemberCardPageView {
    /**
     * 默认8页 书封面：2页(固定)、目录：1页(固定)、党员基本信息：1页(固定)、
     * 党费收缴1页、组织生活：至少2页
     * 
     */
    private int pages = 8; //总页数 (默认8)

    private int baseInfoBeginPage = 3;//基本信息开始页

    private int partyFeePages = 0; //党费收缴记录页数 (默认0)
    private int partyFeeBeginPage = 5;//党费收缴记录 从5页开始 (默认5)
    private int partyFeeEndPage = 5; //党费收缴记录 结束页  (默认5)

    private int activityPages = 1; //组织生活记录页数 (默认1)
    private int activityBeginPage = 6; //组织生活记录 从6页开始 (默认6)
    private int activityEndPage = 7; //组织生活记录 结束页  (默认7)

    /**
     * 
     * <B>方法名称：</B>带参数的构造函数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月18日
     * @param paramter
     */
    public PartyMemberCardPageView(String paramter) {
        if (!StringUtils.isEmpty(paramter)) {
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            JSONObject o = ja.getJSONObject(0);
            JSONArray cardConfigs = o.getJSONArray("cardConfig"); //计算需要哪些目录
            if (cardConfigs != null && cardConfigs.size() > 0) {
                this.pages = 2; //封正面 + 目录 = 2
                for (int i = 0; i < cardConfigs.size(); i++) {
                    switch (Integer.parseInt(String.valueOf(cardConfigs.get(i)))) {
                    case 1:
                        //基本信息篇
                        this.pages += 1;
                        break;
                    case 2:
                        //党费缴纳篇
                        this.pages += 1;
                        this.partyFeeBeginPage = this.pages;
                        this.partyFeeEndPage = this.pages;
                        break;
                    case 3:
                        //组织生活篇
                        this.pages += 2;
                        this.activityBeginPage = this.pages-1;
                        this.activityEndPage = this.pages;
                        break;
                    default:
                        break;
                    }
                }
                this.pages += 1;//封背面
            }

        }
    }

    public PartyMemberCardPageView() {

    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getActivityPages() {
        return activityPages;
    }

    public void setActivityPages(int activityPages) {
        this.activityPages = activityPages;
    }

    public int getActivityBeginPage() {
        return activityBeginPage;
    }

    public void setActivityBeginPage(int activityBeginPage) {
        this.activityBeginPage = activityBeginPage;
    }

    public int getActivityEndPage() {
        return activityEndPage;
    }

    public void setActivityEndPage(int activityEndPage) {
        this.activityEndPage = activityEndPage;
    }

    public int getPartyFeePages() {
        return partyFeePages;
    }

    public void setPartyFeePages(int partyFeePages) {
        this.partyFeePages = partyFeePages;
    }

    public int getPartyFeeBeginPage() {
        return partyFeeBeginPage;
    }

    public int getBaseInfoBeginPage() {
        return baseInfoBeginPage;
    }

    public void setBaseInfoBeginPage(int baseInfoBeginPage) {
        this.baseInfoBeginPage = baseInfoBeginPage;
    }

    public void setPartyFeeBeginPage(int partyFeeBeginPage) {
        this.partyFeeBeginPage = partyFeeBeginPage;
    }

    public int getPartyFeeEndPage() {
        return partyFeeEndPage;
    }

    public void setPartyFeeEndPage(int partyFeeEndPage) {
        this.partyFeeEndPage = partyFeeEndPage;
    }

}
