package org.tpri.sc.util;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>生成UUID的工具类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月9日
 */
public class UUIDUtil {

	/**
	 * 获取一个随机的GUID
	 * @return
	 */
    public static String id(){
        return java.util.UUID.randomUUID().toString().toUpperCase();
    }
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(UUIDUtil.id().length());
		}
	}
}
