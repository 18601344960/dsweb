package org.tpri.sc.view.obt;
/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>组织收缴党费明细视图<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年7月5日
 */
public class PartyFeesDetailView {

    protected int month;//月 01月、02月等
    protected double shoulePartyFee = 0.00; //应缴纳党费金额
    protected double actuallyPartyFee = 0.00;//实际缴纳党费金额
    protected double bigPartyFee = 0.00;//大额党费缴纳金额

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getShoulePartyFee() {
        return shoulePartyFee;
    }

    public void setShoulePartyFee(double shoulePartyFee) {
        this.shoulePartyFee = shoulePartyFee;
    }

    public double getActuallyPartyFee() {
        return actuallyPartyFee;
    }

    public void setActuallyPartyFee(double actuallyPartyFee) {
        this.actuallyPartyFee = actuallyPartyFee;
    }

    public double getBigPartyFee() {
        return bigPartyFee;
    }

    public void setBigPartyFee(double bigPartyFee) {
        this.bigPartyFee = bigPartyFee;
    }

}
