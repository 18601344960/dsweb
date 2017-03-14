/**
 * Copyright 2016 TPRI. All Rights Reserved.
 */
package org.tpri.sc.report.mgr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.service.obt.ConferenceService;
import org.tpri.sc.service.obt.ElectionMemberService;
import org.tpri.sc.service.obt.PartyGroupMemberService;
import org.tpri.sc.service.obt.PartyMemberService;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.util.FileUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>支部手册下载<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（李辉）
 * @since 2016年7月15日
 */
@Controller
public class PartyCardReport extends BaseController {

    @Autowired
    private CCpartyService ccpartyService;
    @Autowired
    private ElectionMemberService electionMemberService;
    @Autowired
    private PartyMemberService partyMemberService;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private PartyGroupMemberService partyGroupMemberService;

    /**
     * <B>方法名称：</B>支部工作手册<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 李辉
     * @since 2016年7月15日
     * @param request 请求参数
     * @param response 相应参数
     * @return 视图 模型
     */
    @RequestMapping("reports/mgr/partyCardReport")
    public ModelAndView getPartyCardReport(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView ret = new ModelAndView();
        String format = getString(request, "format");
        String ccpartyId = getString(request, "ccpartyId");
        String paramter = getString(request, "paramter");
        CCParty ccparty = ccpartyService.getCCPartyFromMc(ccpartyId);
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
        
        //sub1(首页)
        ret.addObject("year", year);
        ret.addObject("ZBMC", ccparty.getName());
        ret.addObject("sub1Data", new JREmptyDataSource());

        //sub2(说明)
        ret.addObject("sub2Data", new JREmptyDataSource());

        //sub3(党支部基本情况)
        electionMemberService.getElectionMembersByCcpartyForPartyCard(ccpartyId,ret); 
        
        //党小组概况 
        partyGroupMemberService.setCcpartyGroupInfoToPartyCard(ccpartyId, ret);

        //sub4(上交的党费情况)
        List<Map<String, Object>> sub4List = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("MONTH", i+"月");
            map.put("SUBMIT_DATE", "");
            map.put("SUBMIT_MOENY", "");
            map.put("SUBMIT_PEOPLE", "");
            map.put("COLLECT_PEOPLE", "");
            sub4List.add(map);
        }
        ret.addObject("sub4Data", sub4List);

        //sub5(党员名册)
        partyMemberService.getPartyMembersByCcpartyForPartyCard(ccpartyId,ret); 

        //sub6(年度工作计划)
        StringBuffer sb6 = new StringBuffer();
        for (int i = 0; i < 9; i++) {
            sb6.append("祝愿祖国早日实现中国梦！");
        }
        ret.addObject("sub6_p1", sb6.toString());
        ret.addObject("sub6Data", new JREmptyDataSource());

        //会议记录
        StringBuffer sb7 = new StringBuffer();
        for (int i = 0; i < 9; i++) {
            sb7.append("打到日本帝国主义!!");
        }
        ret.addObject("sub7_p1", sb7.toString());
        ret.addObject("sub7Data", new JREmptyDataSource());
        
        conferenceService.getConferencesByCcpartyForPartyCard(ccpartyId, paramter, ret);

        //设定报表路径
        String exportPathPre = FileUtil.getContextPath(request);
        String subreportUrl = exportPathPre + "/reports/mgr/ccparty-card/";
        ret.addObject("SUBREPORT_DIR", subreportUrl);
        ret.addObject("datasource", new JREmptyDataSource());
        ret.addObject(JasperReportsView.KEY_USER_AGENT, getUserAgent(request)); // 用户客户端
        ret.addObject(JasperReportsView.KEY_FORMAT, format);
        ret.addObject(JasperReportsView.KEY_FILE_NAME, "党支部工作手册-"+ccparty.getName());
        ret.addObject(JasperReportsView.KEY_PRINT_SESSION, false); // 仅当需要输出曲线图表时设定为true
        return ret;
    }
}
