/**
 * Copyright 2016 TPRI. All Rights Reserved.
 */
package org.tpri.sc.view.obt;

/**
 * <B>系统名称：</B>支部工作手册<BR>
 * <B>模块名称：</B>党费收缴<BR>
 * <B>中文类名：</B>统计党费列表<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年8月4日
 */
public class PartyFeeCountView {
    private String id;
    private String ccpartyId;
    private String userId;
    private String name;
    private int year;
    private double january = 0.00;
    private double february = 0.00;
    private double march = 0.00;
    private double april = 0.00;
    private double may = 0.00;
    private double june = 0.00;
    private double july = 0.00;
    private double august = 0.00;
    private double september = 0.00;
    private double octorber = 0.00;
    private double november = 0.00;
    private double december = 0.00;
    private double specialFee = 0.00;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCcpartyId() {
        return ccpartyId;
    }

    public void setCcpartyId(String ccpartyId) {
        this.ccpartyId = ccpartyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getJanuary() {
        return january;
    }

    public void setJanuary(double january) {
        this.january = january;
    }

    public double getFebruary() {
        return february;
    }

    public void setFebruary(double february) {
        this.february = february;
    }

    public double getMarch() {
        return march;
    }

    public void setMarch(double march) {
        this.march = march;
    }

    public double getApril() {
        return april;
    }

    public void setApril(double april) {
        this.april = april;
    }

    public double getMay() {
        return may;
    }

    public void setMay(double may) {
        this.may = may;
    }

    public double getJune() {
        return june;
    }

    public void setJune(double june) {
        this.june = june;
    }

    public double getJuly() {
        return july;
    }

    public void setJuly(double july) {
        this.july = july;
    }

    public double getAugust() {
        return august;
    }

    public void setAugust(double august) {
        this.august = august;
    }

    public double getSeptember() {
        return september;
    }

    public void setSeptember(double september) {
        this.september = september;
    }

    public double getOctorber() {
        return octorber;
    }

    public void setOctorber(double octorber) {
        this.octorber = octorber;
    }

    public double getNovember() {
        return november;
    }

    public void setNovember(double november) {
        this.november = november;
    }

    public double getDecember() {
        return december;
    }

    public void setDecember(double december) {
        this.december = december;
    }

    public double getSpecialFee() {
        return specialFee;
    }

    public void setSpecialFee(double specialFee) {
        this.specialFee = specialFee;
    }
}
