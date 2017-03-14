package org.tpri.sc.util;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>基本常量定义<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月2日
 */
public class BaseConstants {
	
	/** 跳转至各个页面时，要添加到前台Global对象中的属性map在request中的标识符，PageSupport类会将该map里的名值对复制到Global对象中 */
	public static final String PAGE_GLOBAL_VALUES_IDENTIFIER = "GLOBAL_VALUES";

	/** 未知数据库 */
    public static final int DATABASE_TYPE_UNKNOWN = -1; 
    /** sql server */
    public static final int DATABASE_TYPE_SQLSERVER = 0;
    /** oracle */
    public static final int DATABASE_TYPE_ORACLE = 1;
    /** db2 */
    public static final int DATABASE_TYPE_DB2 = 2;
    /** mysql */
    public static final int DATABASE_TYPE_MYSQL = 3;
    
    /** 判断代码：是 */
    public static final String TRUE = "1";

    /** 判断代码：否 */
    public static final String FALSE = "0";

    /** 通用字符集编码 */
    public static final String CHARSET_UTF8 = "UTF-8";

    /** 中文字符集编码 */
    public static final String CHARSET_CHINESE = "GBK";

    /** 英文字符集编码 */
    public static final String CHARSET_LATIN = "ISO-8859-1";

    /** Cookie键值：验证键值 */
    public static final String COOKIE_VALIDATE_KEY = "VALIDATE_KEY";

    /** Cookie键值：验证键值分割符 */
    public static final String COOKIE_VALIDATE_KEY_SPLIT = ";";

    /** 请求属性键值：当前用户标识 */
    public static final String REQ_CUR_USER_ID = "CUR_USER_ID";
    
    /** 请求属性键值：当前用户登录名标识 */
    public static final String REQ_CUR_USER_LOGIN_NO = "CUR_USER_LOGIN_NO";
    
    /** 请求属性键值：当前用户密码标识 */
    public static final String REQ_CUR_USER_LOGIN_PASSWORD = "CUR_USER_LOGIN_PASSWORD";

    /** 请求属性键值：当前用户名称 */
    public static final String REQ_CUR_USER_NAME = "CUR_USER_NAME";

    /** 请求属性键值：当前机构标识 */
    public static final String REQ_CUR_ORG_ID = "CUR_ORG_ID";

    /** 请求属性键值：当前角色名称 */
    public static final String REQ_CUR_ROLE_CODE = "CUR_ROLE_CODE";
    
    /** 请求属性键值：当前角色用户ID */
    public static final String REQ_CUR_ROLE_USER_ID = "CUR_ROLE_USER_ID";
    
    /** 请求属性键值：当前用户UK状态 */
    public static final String REQ_USER_UKEY_STATIC = "USER_UKEY_STATIC";
    
    /** 管理员角色 */
    public static final String SYS_ROLE_CODE = "SYS_ADMIN";

    /** 根节点ID */
    public static final String ROOT_ID = "root";

    /** NULL字符串 */
    public static final String NULL = "null";

    /** 日期格式 */
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    /** 日期时间格式 */
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /** 时间戳格式 */
    public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
    
    /** 时间链接串格式 */
    public static final String FORMAT_LINKDATE = "yyyyMMddHHmmss";
}
