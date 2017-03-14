package org.tpri.sc.view.com;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B>统计分析<BR>
 * <B>中文类名：</B>组织工作统计视图<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月25日
 */
public class ConferenceCcpartyView {

    private String ccpartyId;
    private String ccpartyName;
    private int individualCount;
    private int ccpartyCount;
    private int brandCount;
    private int recommendCount;
    private int totalCount;

    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    public String getCcpartyName() {
        return ccpartyName;
    }

    public void setCcpartyName(String ccpartyName) {
        this.ccpartyName = ccpartyName;
    }

    public int getIndividualCount() {
        return individualCount;
    }

    public void setIndividualCount(int individualCount) {
        this.individualCount = individualCount;
    }

    public int getCcpartyCount() {
        return ccpartyCount;
    }

    public void setCcpartyCount(int ccpartyCount) {
        this.ccpartyCount = ccpartyCount;
    }

    public int getBrandCount() {
        return brandCount;
    }

    public void setBrandCount(int brandCount) {
        this.brandCount = brandCount;
    }

    public int getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
