package org.tpri.sc.entity.sys;

import java.util.Comparator;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>枚举列表的排序对比类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年6月19日
 */
public class CodeComparator implements Comparator {
    //按照枚举的序号排序
    public int compare(Object ob1, Object ob2) {
        Code code1 = (Code) ob1;
        Code code2 = (Code) ob2;
        int orderNo1 = code1.getOrderNo();
        int orderNo2 = code2.getOrderNo();

        if (orderNo1 > orderNo2) {
            return 1;
        } else if (orderNo1 < orderNo2) {
            return -1;
        } else {
            return 0;
        }
    }
}