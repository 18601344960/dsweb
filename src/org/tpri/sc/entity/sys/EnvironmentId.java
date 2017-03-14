package org.tpri.sc.entity.sys;
/**
 * @description 系统参数ID,添加环境变量后都需要在这里注册
 * @author 易文俊
 * @since 2015-05-11
 */
public enum EnvironmentId {
	INITIAL_CCPARTYID,			//初始化根党组织
	INITIAL_ORGANIZATIONID,			//初始化根行政单位
	URL_MEMCACHE,				//memcached的服务器地址
	FILES_ROOT_PATH,			//文件存储根路径
	SYSTEM_NO //系统编号，每部署一套系统都有一个唯一编号，这个编号有部署人员统一安排
}
