package org.tpri.sc.core;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>对象注册值表<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年4月2日
 */
public class ObjectType {

    public static final int UNKNOWN = -1;
    public static final int OBJECTREGISTER = 0;

    //通用资源管理表
    public static final int COM_FILE = 100;
    public static final int COM_INFORMATION = 101;
    public static final int COM_ANNOUNCEMENT = 102;
    public static final int COM_WORK_BRAND = 103;
    public static final int COM_PARTICIPANTS = 104;

    //用户权限管理表
    public static final int UAM_USER = 200;
    public static final int UAM_ROLE = 201;
    public static final int UAM_PRIVILEGE = 202;
    public static final int UAM_USERROLE = 203;
    public static final int UAM_ROLE_PRIVILEGE = 204;

    //系统表
    public static final int SYS_NAVIGATION = 300;
    public static final int SYS_ENVIRONMENT = 301;
    public static final int SYS_ENUMERATION = 302;
    public static final int SYS_CODE = 303;
    public static final int SYS_SERVICES_CONFIG = 304;

    //党政管理表
    public static final int ORG_CCPARTY = 400;
    public static final int ORG_ORGANIZATION = 401;
    public static final int ORG_PARTY_LEADER = 403;
    public static final int ORG_DEPARTMENT = 404;
    public static final int ORG_CCPARTY_LEADER = 405;
    public static final int ORG_CCPARTY_GROUP = 406;

    //组织建设
    public static final int OBT_PARTY_MEMBER = 500;
    public static final int OBT_USER_EDUCATION = 501;
    public static final int OBT_PARTYMEMBER_RESUME = 502;
    public static final int OBT_USER_RESUME = 503;
    public static final int OBT_USER_REWARDS_PUNISHMENTS = 504;
    public static final int OBT_ELECTION = 505;
    public static final int OBT_ELECTION_MEMBER = 506;
    public static final int OBT_ELECTION_MEMBER_TITLE = 507;
    public static final int OBT_CONFERENCE = 508;
    public static final int OBT_CONFERENCE_COMMENT = 509;
    public static final int OBT_CONFERENCE_CATEGORY = 510;
    public static final int OBT_CONFERENCE_LABEL = 511;
    public static final int OBT_CONFERENCE_PRAISE = 512;
    public static final int OBT_CONFERENCE_STEP = 513;
    public static final int OBT_CONFERENCE_ORGNIZER = 514;
    public static final int OBT_CONFERENCE_FORMAT = 515;
    public static final int OBT_DEVELOPMENT_PROCEDURE = 516;
    public static final int OBT_DEVELOPMENT_PROCEDURE_CONFIG = 517;
    public static final int OBT_DEVELOPMENT_MEMBER = 518;
    public static final int OBT_DEVELOPMENT_PROCEDURE_COMMON_CONTENT = 519;
    public static final int OBT_PARTYMEMBER_DEVELOPMENT_INFO = 520;
    public static final int OBT_PARTY_GROUP_MEMBER = 521;

    public static final int OBT_PARTY_FEE = 600; //党费收缴
    public static final int OBT_PARTY_FEE_USE = 601;
    public static final int OBT_PARTY_FEE_SPECIAL = 602;
    public static final int OBT_PARTY_FEE_REPORT = 603;
    
    public static final int PUB_ASSESSMENT = 700;
    public static final int PUB_ASSESSMENT_RESULT = 701;
    public static final int PUB_ASSESSMENT_TARGET = 702;
    public static final int PUB_ASSESSMENT_TOPIC = 703;
    public static final int PUB_ASSESSMENT_TOPIC_OPTION = 704;
    public static final int PUB_ASSESSMENT_USER = 705;

    public static final int OBT_CONFERENCE_PARTICIPANTS = 800;
    

    //缓存对象
    public static final int UAM_USER_MC = 900;

    //查询结果集表
    public static final int CountResult = 1000;
    
    public static final int DS_QUESTION = 10000;
    public static final int DS_QUESTION_OPTION = 10001;
    public static final int DS_QUESTION_ANSWER = 10002;
    public static final int DS_QUESTION_CATEGORY = 10003;
    public static final int DS_CATEGORY = 10004;
}
