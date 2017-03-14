package org.tpri.sc.util;

import org.springframework.context.ApplicationContext;
/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>Bean工厂类<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月2日
 */
public class BeanFactory {
	private  static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		BeanFactory.applicationContext = applicationContext;
	}
	
	public static Object getBean(String beanName){
		return BeanFactory.applicationContext.getBean(beanName);
	}
	
}
