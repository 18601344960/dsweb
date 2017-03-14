package org.tpri.sc.view.com;
/**
 * 
 * @Description: 上报统计视图
 * @author 易文俊
 * @date 2015-08-14
 */
public class ReportStatisticView {
	private String ccpartyId;
	private String ccpartyName;
	private int num;
	private int num1;
	
	public ReportStatisticView(String ccpartyId, String ccpartyName, int num) {
		super();
		this.ccpartyId = ccpartyId;
		this.ccpartyName = ccpartyName;
		this.num = num;
	}
	
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }
	
	
}
