package org.tpri.sc.service.obt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.DevelopmentProcedure;
import org.tpri.sc.entity.obt.DevelopmentProcedureConfig;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.DevelopmentProcedureConfigManager;
import org.tpri.sc.manager.obt.DevelopmentProcedureManager;
import org.tpri.sc.util.UUIDUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展流程配置服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年11月30日
 */

@Service("DevelopmentProcedureConfigService")
public class DevelopmentProcedureConfigService {

    public Logger logger = Logger.getLogger(DevelopmentProcedureConfigService.class);

    @Autowired
    DevelopmentProcedureConfigManager developmentProcedureConfigManager;

    @Autowired
    DevelopmentProcedureManager developmentProcedureManager;

    /**
     * <B>方法名称：</B>更新党员发展流程配置<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年12月1日
     * @param user
     * @param ccpartyId
     * @param developtmentId
     * @param status
     * @return
     */
    public boolean updateDevelopmentProcedureConfig(UserMc user, String ccpartyId, String developtmentId, Integer status) {
        if (status != null && status == DevelopmentProcedureConfig.STATUS_0) {
            DevelopmentProcedureConfig developmentProcedureConfig = new DevelopmentProcedureConfig();
            developmentProcedureConfig.setId(UUIDUtil.id());
            developmentProcedureConfig.setCcpartyId(ccpartyId);
            developmentProcedureConfig.setDeveloptmentId(developtmentId);
            developmentProcedureConfig.setStatus(DevelopmentProcedureConfig.STATUS_0);
            developmentProcedureConfigManager.saveOrUpdate(developmentProcedureConfig);
        } else {
            developmentProcedureConfigManager.deleteDevelopmentProcedureConfig(ccpartyId, developtmentId);
        }
        return true;
    }

    /**
     * <B>方法名称：</B>获取党员发展流程配置<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月30日
     * @param ccpartyId
     * @return
     */
    public Map<String, Integer> getDevelopmentProcedureList(String ccpartyId, Integer phaseCode) {
        List<DevelopmentProcedure> developmentProcedureList = developmentProcedureManager.getDevelopmentProcedureList(phaseCode);
        List<DevelopmentProcedureConfig> developmentProcedureConfigList = developmentProcedureConfigManager.getDevelopmentProcedureConfigList(ccpartyId);
        Map<String, Integer> config = new HashMap<String, Integer>();
        for (DevelopmentProcedure developmentProcedure : developmentProcedureList) {
            config.put(developmentProcedure.getId(), developmentProcedure.getStatus());
            if (developmentProcedureConfigList != null && developmentProcedureConfigList.size() > 0) {
                for (DevelopmentProcedureConfig developmentProcedureConfig : developmentProcedureConfigList) {
                    if (developmentProcedure.getId().equals(developmentProcedureConfig.getDeveloptmentId())) {
                        config.put(developmentProcedure.getId(), developmentProcedureConfig.getStatus());
                    }
                }
            }
        }
        return config;
    }

}
