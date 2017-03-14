package org.tpri.sc.service.obt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.obt.DevelopmentProcedureCommonContent;
import org.tpri.sc.manager.obt.DevelopmentProcedureCommonContentManager;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.UUIDUtil;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展阶段公共内容服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年12月18日
 */
@Service("DevelopmentProcedureCommonContentService")
public class DevelopmentProcedureCommonContentService {

    @Autowired
    DevelopmentProcedureCommonContentManager developmentProcedureCommonContentManager;

    /**
     * 
     * <B>方法名称：</B>获取某党员的公共数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param partymemberId
     * @param type
     * @return
     */
    public List<DevelopmentProcedureCommonContent> getProceduresCommonContentByType(String partymemberId, int type) {
        return developmentProcedureCommonContentManager.getProceduresCommonContentByType(partymemberId, type);
    }

    /**
     * 
     * <B>方法名称：</B>保存党员发展阶段公共内容<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param paramters
     * @return
     */
    public Map<String, Object> saveOrUpdateDevelopmentProcedureCommonContent(String paramters) {
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject jodata = JSONObject.fromObject(paramters);
        JSONArray ja = JSONArray.fromObject(jodata.get("data"));
        JSONObject objs = ja.getJSONObject(0);
        DevelopmentProcedureCommonContent content = developmentProcedureCommonContentManager.getProcedureCommonContentById(objs.getString("id"));
        if (content == null) {
            content = new DevelopmentProcedureCommonContent();
            content.setId(UUIDUtil.id());
        }
        content.setType(objs.getInt("type"));
        content.setContentDate(StringUtils.isEmpty(objs.getString("contentDate")) ? null : DateUtil.str2Date(objs.getString("contentDate"), "yyyy-MM-dd"));
        content.setName(objs.getString("name"));
        content.setPartymemberId(objs.getString("partymemberId"));
        content.setProcedureId(objs.getString("procedureId"));
        content.setContent(objs.getString("content"));
        developmentProcedureCommonContentManager.saveOrUpdate(content);
        ret.put("success", true);
        ret.put("msg", "已成功保存。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>删除党员发展阶段公共内容<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param id
     * @return
     */
    public Map<String, Object> deleteDevelopmentProcedureCommonContent(String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        DevelopmentProcedureCommonContent content = developmentProcedureCommonContentManager.getProcedureCommonContentById(id);
        if (content == null) {
            ret.put("success", false);
            ret.put("msg", "删除失败，对象为空。");
            return ret;
        }
        developmentProcedureCommonContentManager.delete(content);
        ret.put("success", true);
        ret.put("msg", "已成功删除。");
        return ret;
    }

}
