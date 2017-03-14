package org.tpri.sc.view.obt;

import java.util.ArrayList;
import java.util.List;

import org.tpri.sc.entity.org.CCParty;
/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织收缴党费视图<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年7月5日
 */
public class PartyFeeDetailView {

    protected CCParty ccparty;//党组织
    protected int year;//年份
    protected double shoulePartyFeeTotal = 0.00; //合计应缴纳党费金额
    protected double actuallyPartyFeeTotal = 0.00;//合计实际缴纳党费金额
    protected double bigPartyFeeTotal = 0.00;//合计大额党费缴纳金额
    protected List<PartyFeesDetailView> feesDeatilViews = new ArrayList<PartyFeesDetailView>(); //个月统计党费缴纳集合

    public CCParty getCcparty() {
        return ccparty;
    }

    public void setCcparty(CCParty ccparty) {
        this.ccparty = ccparty;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getShoulePartyFeeTotal() {
        return shoulePartyFeeTotal;
    }

    public void setShoulePartyFeeTotal(double shoulePartyFeeTotal) {
        this.shoulePartyFeeTotal = shoulePartyFeeTotal;
    }

    public double getActuallyPartyFeeTotal() {
        return actuallyPartyFeeTotal;
    }

    public void setActuallyPartyFeeTotal(double actuallyPartyFeeTotal) {
        this.actuallyPartyFeeTotal = actuallyPartyFeeTotal;
    }

    public double getBigPartyFeeTotal() {
        return bigPartyFeeTotal;
    }

    public void setBigPartyFeeTotal(double bigPartyFeeTotal) {
        this.bigPartyFeeTotal = bigPartyFeeTotal;
    }

    public List<PartyFeesDetailView> getFeesDeatilViews() {
        return feesDeatilViews;
    }

    public void setFeesDeatilViews(List<PartyFeesDetailView> feesDeatilViews) {
        this.feesDeatilViews = feesDeatilViews;
    }

}
