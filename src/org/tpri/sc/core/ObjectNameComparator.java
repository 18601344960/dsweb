package org.tpri.sc.core;

import java.text.Collator;
import java.util.Comparator;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>实现对比接口类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年6月19日
 */
public class ObjectNameComparator implements Comparator {
	//按照对象的名称拼音排序
	private static Collator collator = Collator.getInstance(java.util.Locale.CHINA);
    public int compare(Object o1, Object o2) {
        ObjectBase ob1 = (ObjectBase)o1;
        ObjectBase ob2 = (ObjectBase)o2;
        String name1 = ob1.getName();
		String name2 = ob2.getName();
		
		if (name1 == null && name2 == null)
        	return 0;

		if (name1 != null && name2 == null) {
			return 1;
		}
		if (name1 == null && name2 != null) {
			return -1;
		}
		
		if (name1.equals(name2)) {
			return 0;
		}
		
        return collator.compare(name1, name2);
    }
}