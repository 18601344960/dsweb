package org.tpri.sc.view.org;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党组织电子活动证分页视图<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年5月25日
 */
public class CcpartyCardPageView {
    /**
     * 默认7页 书封面：2页(固定)、目录：1页(固定)、党组织基本信息：1页(固定) 、党费缴纳：至少1页、组织生活：至少2页
     * 
     */
    private int pages = 7; //总页数 (默认7)

    private int baseInfoBeginPage = 3;//组织信息开始页

    private int partyFeePages = 0; //党费收缴记录页数 (默认1)
    private int partyFeeBeginPage = 4;//党费收缴记录 从4页开始 (默认4)
    private int partyFeeEndPage = 4; //党费收缴记录 结束页  (默认4)

    private int branchConferencePages = 1; //支部工作记录页数 (默认1)
    private int branchConferenceBeginPage = 5; //支部工作记录 从5页开始 (默认5)
    private int branchConferenceEndPage = 6; //支部工作记录 结束页  (默认6)

    /**
     * 
     * <B>方法名称：</B>带参数的构造函数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月19日
     * @param paramter
     */
    public CcpartyCardPageView(String paramter) {
        if (!StringUtils.isEmpty(paramter)) {
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            JSONObject o = ja.getJSONObject(0);
            JSONArray cardConfigs = o.getJSONArray("cardConfig"); //计算需要哪些目录
            if (cardConfigs != null && cardConfigs.size() > 0) {
                this.pages = 2; //封正面 + 目录 = 2
                for (int i = 0; i < cardConfigs.size(); i++) {
                    switch (Integer.parseInt(String.valueOf(cardConfigs.get(i)))) {
                    case 1: {
                        //基本信息篇
                        this.pages += 1;
                        break;
                    }
                    case 2: {
                        //党费缴纳篇
                        this.pages += 1;
                        this.partyFeeBeginPage = this.pages;
                        this.partyFeeEndPage = this.pages;
                        break;
                    }
                    case 3: {
                        //组织生活篇
                        this.pages += 2;
                        this.branchConferenceBeginPage = this.pages - 1;
                        this.branchConferenceEndPage = this.pages;
                        break;
                    }
                    default:
                        break;
                    }
                }
                this.pages += 1;//封背面
            }

        }
    }

    public CcpartyCardPageView() {

    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getBranchConferencePages() {
        return branchConferencePages;
    }

    public void setBranchConferencePages(int branchConferencePages) {
        this.branchConferencePages = branchConferencePages;
    }

    public int getBranchConferenceBeginPage() {
        return branchConferenceBeginPage;
    }

    public void setBranchConferenceBeginPage(int branchConferenceBeginPage) {
        this.branchConferenceBeginPage = branchConferenceBeginPage;
    }

    public int getBranchConferenceEndPage() {
        return branchConferenceEndPage;
    }

    public void setBranchConferenceEndPage(int branchConferenceEndPage) {
        this.branchConferenceEndPage = branchConferenceEndPage;
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

    public void setPartyFeeBeginPage(int partyFeeBeginPage) {
        this.partyFeeBeginPage = partyFeeBeginPage;
    }

    public int getPartyFeeEndPage() {
        return partyFeeEndPage;
    }

    public void setPartyFeeEndPage(int partyFeeEndPage) {
        this.partyFeeEndPage = partyFeeEndPage;
    }

    public int getBaseInfoBeginPage() {
        return baseInfoBeginPage;
    }

    public void setBaseInfoBeginPage(int baseInfoBeginPage) {
        this.baseInfoBeginPage = baseInfoBeginPage;
    }

}
