package org.tpri.sc.listener;

import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.tpri.sc.core.MemoryCacheFactory;
import org.tpri.sc.manager.obt.ConferenceCategoryManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.sys.CodeManager;
import org.tpri.sc.manager.sys.EnumerationManager;
import org.tpri.sc.manager.sys.EnvironmentManager;
import org.tpri.sc.manager.uam.PrivilegeManager;
import org.tpri.sc.manager.uam.RolePrivilegeManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.BeanFactory;
/**
 * @description 应用监听器
 * @author 易文俊
 * @since 2015-04-02
 */
public class DataPretreater extends ContextLoaderListener{
	public static Logger logger = Logger.getLogger(DataPretreater.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.debug("**********djcom begin start**********");
		MemoryCacheFactory.initialize();
		super.contextInitialized(event);
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		BeanFactory.setApplicationContext(applicationContext);
		initManager(applicationContext);
		initConfig(applicationContext);
		initOsName();
        logger.debug("**********djcom started**********");
	}
	private void initManager(ApplicationContext applicationContext){
		//使用缓存的Manager需要在此初始化
	    ((RolePrivilegeManager) applicationContext.getBean("RolePrivilegeManager")).initialize();
		((CCPartyManager) applicationContext.getBean("CCPartyManager")).initialize();
        ((PrivilegeManager) applicationContext.getBean("PrivilegeManager")).initialize();
        ((ConferenceCategoryManager) applicationContext.getBean("ConferenceCategoryManager")).initialize();
        ((CodeManager) applicationContext.getBean("CodeManager")).initialize();
        ((EnumerationManager) applicationContext.getBean("EnumerationManager")).initialize();
        ((EnvironmentManager) applicationContext.getBean("EnvironmentManager")).initialize();
        ((UserManager) applicationContext.getBean("UserManager")).initialize();
	}
	private void initOsName() {
		Properties properties = new Properties(System.getProperties());
		String osName = properties.getProperty("os.name");
		logger.debug("current os name:" + osName);
	}
	private void initConfig(ApplicationContext applicationContext) {
        logger.debug("initConfig begin");
        EnvironmentManager environmentManager = ((EnvironmentManager) applicationContext.getBean("EnvironmentManager"));
        BaseConfig.setConfig(environmentManager);
        logger.debug("initConfig end");
    }
}
