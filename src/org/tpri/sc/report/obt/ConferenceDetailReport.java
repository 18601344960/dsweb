package org.tpri.sc.report.obt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.report.view.JasperReportsView;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.obt.Conference;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.obt.ConferenceService;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.HTMLUtil;

/**
 * <B>系统名称：</B>工作记录详情下载<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月27日
 */
@Controller
public class ConferenceDetailReport extends BaseController {

    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private UserManager userManager;

    @Autowired
    private ConferenceService conferenceService;

    @RequestMapping("reports/obt/conferenceDetail")
    public ModelAndView partyFee(HttpServletRequest request, HttpServletResponse response) {
        String conferenceId = getString(request, "id");
        String format = getString(request, "format");
        Conference conference = conferenceService.getConferenceById(conferenceId);
        ModelAndView ret = new ModelAndView();
        ret.addObject(JasperReportsView.KEY_USER_AGENT, getUserAgent(request));
        ret.addObject(JasperReportsView.KEY_FORMAT, format);
        ret.addObject(JasperReportsView.KEY_FILE_NAME, conference.getName());
        ret.addObject("requestObject", request);
        ret.addObject("NAME", conference.getName());
        ret.addObject("OCCUR_DATE", DateUtil.timestamp2Str(conference.getOccurTime(), DateUtil.DEFAULT_FORMAT));
        ret.addObject("ADDRESS", conference.getAddress());
        ret.addObject("CHAIR", conference.getOrgnizers());
        ret.addObject("ATTENDANCE", conference.getAttendance());
        ret.addObject("PARTICIPANTS", conference.getParticipants());
        ret.addObject("CONTENT", HTMLUtil.delHTMLTag(conference.getContent(), true));
        JREmptyDataSource emptyData = new JREmptyDataSource();
        ret.addObject("datasource", emptyData);
        return ret;
    }
}
