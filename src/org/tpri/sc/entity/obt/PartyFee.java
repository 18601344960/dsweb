package org.tpri.sc.entity.obt;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tpri.sc.core.ObjectBase;
import org.tpri.sc.core.ObjectType;

/**
 * 
 * @Description: 党费管理实体
 * @author guojing
 * @date 2015-07-06
 */
@Entity
@Table(name = "OBT_PARTY_FEE")
public class PartyFee extends ObjectBase {
    protected int objectType = ObjectType.OBT_PARTY_FEE;

    private static final long serialVersionUID = 1L;

    public static final double PARTY_FEE_FREE = -1; //表示免缴
    public static final double PARTY_CANCEL_FREE = 0; //表示撤销免缴

    protected String id;
    protected String userId; //交款人ID
    protected int year; //年份
    protected int month;
    protected double baseSalary;
    protected double rate; //缴纳比例
    protected double payAble;
    protected double payIn;
    protected String receiver;
    protected Date receiveDate;
    protected String remark;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "YEAR")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Column(name = "MONTH")
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Column(name = "BASE_SALARY")
    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    @Column(name = "RATE")
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Column(name = "PAYABLE")
    public double getPayAble() {
        return payAble;
    }

    public void setPayAble(double payAble) {
        this.payAble = payAble;
    }

    @Column(name = "PAY_IN")
    public double getPayIn() {
        return payIn;
    }

    public void setPayIn(double payIn) {
        this.payIn = payIn;
    }

    @Column(name = "RECEIVER")
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Column(name = "RECEIVE_DATE")
    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PartyFee [objectType=" + objectType + ", id=" + id + ", userId=" + userId + ", year=" + year + ", month=" + month + ", baseSalary=" + baseSalary + ", rate=" + rate + ", payAble="
                + payAble + ", payIn=" + payIn + "]";
    }

}
