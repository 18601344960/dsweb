package org.tpri.sc.util;

import org.tpri.sc.entity.sys.Environment;
import org.tpri.sc.entity.sys.EnvironmentId;
import org.tpri.sc.manager.sys.EnvironmentManager;

/**
 * @description 基本配置
 * @author 易文俊
 * @since 2015-05-18
 */
public class BaseConfig {
    //初始化根党组织
    public static String INITIAL_CCPARTYID;
    //系统编号，每部署一套系统都有一个唯一编号，这个编号有部署人员统一安排
    public static String SYSTEM_NO;

    public static void setConfig(EnvironmentManager environmentManager) {
        Environment systemNo = environmentManager.getEnvironmentById(EnvironmentId.SYSTEM_NO);
        if (systemNo != null) {
            SYSTEM_NO = systemNo.getValue();
        }
        Environment ccpartyId = environmentManager.getEnvironmentById(EnvironmentId.INITIAL_CCPARTYID);
        if (ccpartyId != null) {
            INITIAL_CCPARTYID = ccpartyId.getValue();
        }
    }
}
