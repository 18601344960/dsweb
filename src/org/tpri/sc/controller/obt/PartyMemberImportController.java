package org.tpri.sc.controller.obt;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.service.obt.PartyMemberImportService;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党统数据导入控制器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年1月26日
 */
@Controller
@RequestMapping("/obt")
public class PartyMemberImportController extends BaseController {

    @Autowired
    private PartyMemberImportService partyMemberImportService;
    
    /**
     * 
     * <B>方法名称：</B>excel文件党统数据导入<BR>
     * <B>概要说明：</B><BR>
     * @author 赵子靖
     * @since 2016年2月22日 	
     * @param request
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping("beginExcelImportPartymemberInfo")
    @ResponseBody
    public Map<String, Object> beginExcelImportPartymemberInfo(HttpServletRequest request) throws IOException, ParseException {
        logger.debug(this.getClass() + " beginExcelImportPartymemberInfo begin");
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject objs = new JSONObject();
        objs.put("files", getString(request, "files"));
        String ccpartyId = getString(request, "ccpartyId");
        ret = partyMemberImportService.beginExcelImportPartymemberInfo(objs,ccpartyId);
        logger.debug(this.getClass() + " beginExcelImportPartymemberInfo begin");
        return ret;
    }
}
