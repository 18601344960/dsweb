/**
 * Copyright 2016 TPRI. All Rights Reserved.
 */
package org.tpri.sc.report.mgr;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.report.view.JasperReportsView;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.obt.ConferenceParticipantsService;
import org.tpri.sc.service.obt.PartyFeeService;
import org.tpri.sc.service.obt.PartyMemberService;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.util.FileUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员手册下载<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（李辉）
 * @since 2016年7月15日
 */
@Controller
public class MemberCardReport extends BaseController {

    @Autowired
    private CCpartyService ccpartyService;
    @Autowired
    private PartyMemberService partyMemberService;
    @Autowired
    private PartyFeeService partyFeeService;
    @Autowired
    private ConferenceParticipantsService conferenceParticipantsService;
    @Autowired
    private UserManager userManager;
    

    /**
     * <B>方法名称：</B>党员手册<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 李辉
     * @since 2016年7月21日
     * @param request 请求参数
     * @param response 相应参数
     * @param format 格式
     * @return 视图模型
     */
    @RequestMapping("reports/mgr/memberCardReport")
    public ModelAndView getMemberCardReport(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView ret = new ModelAndView();
        String format = getString(request, "format");
        String userId = getString(request, "userId");
        UserMc user = userManager.getUserFromMc(userId);
        String paramter = getString(request, "paramter");
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        if (!StringUtils.isEmpty(paramter)) {
            //查询参数
            JSONObject jodata = JSONObject.fromObject(paramter);
            JSONArray ja = JSONArray.fromObject(jodata.get("data"));
            JSONObject jsonObjs = ja.getJSONObject(0);
            String beginTime = jsonObjs.getString("beginTime");
            if(!StringUtils.isEmpty(beginTime)){
                year = beginTime.substring(0,4);
            }
        }
        
        //dysc_ml(首页)
        partyMemberService.setUserByUserIdForMemberCard(userId,ret);

        //dysc_base（基本信息）
        partyMemberService.setBaseInfoForMemberCard(userId,ret,request);

        //dysc_money(党费缴纳记录)
        partyFeeService.setPartyFeeForMemberCard(userId, Integer.parseInt(year), ret);
        

        //(组织生活记录)
        conferenceParticipantsService.getConferencesByUserForMemberCard(userId, paramter, ret);
        
        //设定报表路径
        String exportPathPre = FileUtil.getContextPath(request);
        String subreportUrl = exportPathPre + "/reports/mgr/member-card/";
        ret.addObject("SUBREPORT_DIR", subreportUrl);
        //ret.addObject("images_paths", imageUrl+"/");
        ret.addObject("year", year);
        ret.addObject("datasource", new JREmptyDataSource());
        ret.addObject(JasperReportsView.KEY_USER_AGENT, getUserAgent(request)); // 用户客户端
        ret.addObject(JasperReportsView.KEY_FORMAT, format);
        ret.addObject(JasperReportsView.KEY_FILE_NAME, "中国共产党党员手册-"+user.getName());
        ret.addObject(JasperReportsView.KEY_PRINT_SESSION, false); // 仅当需要输出曲线图表时设定为true
        return ret;
    }
}
