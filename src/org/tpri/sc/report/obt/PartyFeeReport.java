package org.tpri.sc.report.obt;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.report.view.JasperReportsView;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.obt.PartyFeeService;
import org.tpri.sc.util.StringUtil;
import org.tpri.sc.view.obt.PartyFeeCountView;
import org.tpri.sc.view.obt.PartyFeeView;

/**
 * <B>系统名称：</B>党费统计报表<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（王钱俊）
 * @since 2015年10月13日
 */
@Controller
public class PartyFeeReport extends BaseController {

    @Autowired
    private PartyFeeService partyFeeService;
    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private UserManager userManager;

    /**
     * <B>方法名称：</B>下载收缴表报表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("reports/obt/PartyFee")
    public ModelAndView partyFee(HttpServletRequest request, HttpServletResponse response) {
        Integer year = getInteger(request, "year");
        String ccpartyId = getString(request, "ccpartyId");
        Integer month = getInteger(request, "month");
        String userIdList = getString(request, "userIdList");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (!StringUtil.isEmpty(userIdList)) {
            String[] userId = userIdList.split(",");
            List<String> user = Arrays.asList(userId);
            List<PartyFeeView> partyFee = partyFeeService.getPartyFeesListByCcparty(null, null, ccpartyId, year, month);
            if (partyFee != null && partyFee.size() > 0) {
                int j = 1;
                for (int i = 0; i < partyFee.size(); i++) {
                    if (user.contains(partyFee.get(i).getPartyFee().getUserId())) {
                        Map<String, Object> member = new HashMap<String, Object>();
                        member.put("SEQUENCE", j);
                        String name = "";
                        if (!StringUtils.isEmpty(partyFee.get(i).getName())) {
                            name = partyFee.get(i).getName();
                        }
                        member.put("NAME", name);
                        member.put("TIME", year + "年" + month + "月");                    
                        member.put("PAY_IN", "");
                        member.put("MARK", "");
                        list.add(member);
                        j++;
                    } else {
                        continue;
                    }
                }
            }
        }
        CCParty cCPartyMc = ccpartyManager.getCCPartyFromMc(ccpartyId);
        String format = getString(request, "format");
        ModelAndView ret = new ModelAndView();
        ret.addObject("requestObject", request);
        ret.addObject("CCPARTY_NAME", cCPartyMc.getName());
        ret.addObject(JasperReportsView.KEY_USER_AGENT, getUserAgent(request)); // 用户客户端
        ret.addObject(JasperReportsView.KEY_FORMAT, format);
        ret.addObject(JasperReportsView.KEY_FILE_NAME, "党费收缴表");
        JREmptyDataSource emptyData = new JREmptyDataSource();
        if (list.size() == 0) {
            ret.addObject("datasource", emptyData);
        } else {
            ret.addObject("datasource", list);
        }
        return ret;
    }

    /**
     * <B>方法名称：</B>统计党费表报表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("reports/obt/PartyFeeData")
    public ModelAndView PartyFeeData(HttpServletRequest request, HttpServletResponse response) {
        Integer year = getInteger(request, "year");
        String ccpartyId = getString(request, "ccpartyId");
        CCParty cCPartyMc = ccpartyManager.getCCPartyFromMc(ccpartyId);
        String format = getString(request, "format");
        ModelAndView ret = new ModelAndView();
        ret.addObject("requestObject", request);
        ret.addObject("CCPARTY_NAME", cCPartyMc.getName());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String january = "", february = "", march = "", april = "", may = "", june = "", july = "", august = "", september = "", october = "", november = "", december = "", specialFee = "";
        double januaryTotal = 0.00, februaryTotal = 0.00, marchTotal = 0.00, aprilTotal = 0.00, mayTotal = 0.00, juneTotal = 0.00, julyTotal = 0.00, augustTotal = 0.00, septemberTotal = 0.00, octoberTotal = 0.00, novemberTotal = 0.00, decemberTotal = 0.00, specialFeeTotal = 0.00;
        List<PartyFeeCountView> partyFeeCountViews = partyFeeService.countPartyFeeByYear(null, null, ccpartyId, year);
        if (partyFeeCountViews != null && partyFeeCountViews.size() > 0) {
            for (int i = 0; i < partyFeeCountViews.size()-1; i++) {
                Map<String, Object> member = new HashMap<String, Object>();
                member.put("SEQUENCE", i + 1);
                String name = "";
                if (!StringUtils.isEmpty(partyFeeCountViews.get(i).getName())) {
                    name = partyFeeCountViews.get(i).getName();
                }
                member.put("NAME", name);
                member.put("YEAR", year);
                if (partyFeeCountViews.get(i).getJanuary() == -1) {
                    january = "免";
                } else {
                    january = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getJanuary());
                    double tempJanuary = Double.parseDouble(january);
                    januaryTotal += tempJanuary;
                }
                if (partyFeeCountViews.get(i).getFebruary() == -1) {
                    february = "免";
                } else {
                    february = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getFebruary());
                    double tempFebruary = Double.parseDouble(february);
                    februaryTotal += tempFebruary;
                }
                if (partyFeeCountViews.get(i).getMarch() == -1) {
                    march = "免";
                } else {
                    march = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getMarch());
                    double tempMarch = Double.parseDouble(march);
                    marchTotal += tempMarch;
                }
                if (partyFeeCountViews.get(i).getApril() == -1) {
                    april = "免";
                } else {
                    april = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getApril());
                    Double tempApril = Double.parseDouble(april);
                    aprilTotal += tempApril;
                }
                if (partyFeeCountViews.get(i).getMay() == -1) {
                    may = "免";
                } else {
                    may = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getMay());
                    Double tempMay = Double.parseDouble(may);
                    mayTotal += tempMay;
                }
                if (partyFeeCountViews.get(i).getJune() == -1) {
                    june = "免";
                } else {
                    june = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getJune());
                    Double tempJune = Double.parseDouble(june);
                    juneTotal += tempJune;
                }
                if (partyFeeCountViews.get(i).getJuly() == -1) {
                    july = "免";
                } else {
                    july = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getJuly());
                    Double tempJuly = Double.parseDouble(july);
                    julyTotal += tempJuly;
                }
                if (partyFeeCountViews.get(i).getAugust() == -1) {
                    august = "免";
                } else {
                    august = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getAugust());
                    Double tempAugust = Double.parseDouble(august);
                    augustTotal += tempAugust;
                }
                if (partyFeeCountViews.get(i).getSeptember() == -1) {
                    september = "免";
                } else {
                    september = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getSeptember());
                    Double tempSeptember = Double.parseDouble(september);
                    septemberTotal += tempSeptember;
                }
                if (partyFeeCountViews.get(i).getOctorber() == -1) {
                    october = "免";
                } else {
                    october = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getOctorber());
                    Double tempOctober = Double.parseDouble(october);
                    octoberTotal += tempOctober;
                }
                if (partyFeeCountViews.get(i).getNovember() == -1) {
                    november = "免";
                } else {
                    november = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getNovember());
                    Double tempNovember = Double.parseDouble(november);
                    novemberTotal += tempNovember;
                }
                if (partyFeeCountViews.get(i).getDecember() == -1) {
                    december = "免";
                } else {
                    december = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getDecember());
                    Double tempDecember = Double.parseDouble(december);
                    decemberTotal += tempDecember;
                }
                specialFee = new DecimalFormat("#0.00").format(partyFeeCountViews.get(i).getSpecialFee());
                Double tempSpecialFee = Double.parseDouble(specialFee);
                specialFeeTotal += tempSpecialFee;
                member.put("JANUARY", january);
                member.put("FEBRUARY", february);
                member.put("MARCH", march);
                member.put("APRIL", april);
                member.put("MAY", may);
                member.put("JUNE", june);
                member.put("JULY", july);
                member.put("AUGUST", august);
                member.put("SEPTEMBER", september);
                member.put("OCTOBER", october);
                member.put("NOVEMBER", november);
                member.put("DECEMBER", december);
                member.put("SPECIALFEE", specialFee);
                list.add(member);
            }
        }
        ret.addObject("TOTAL1", new DecimalFormat("#0.00").format(januaryTotal));
        ret.addObject("TOTAL2", new DecimalFormat("#0.00").format(februaryTotal));
        ret.addObject("TOTAL3", new DecimalFormat("#0.00").format(marchTotal));
        ret.addObject("TOTAL4", new DecimalFormat("#0.00").format(aprilTotal));
        ret.addObject("TOTAL5", new DecimalFormat("#0.00").format(mayTotal));
        ret.addObject("TOTAL6", new DecimalFormat("#0.00").format(juneTotal));
        ret.addObject("TOTAL7", new DecimalFormat("#0.00").format(julyTotal));
        ret.addObject("TOTAL8", new DecimalFormat("#0.00").format(augustTotal));
        ret.addObject("TOTAL9", new DecimalFormat("#0.00").format(septemberTotal));
        ret.addObject("TOTAL10", new DecimalFormat("#0.00").format(octoberTotal));
        ret.addObject("TOTAL11", new DecimalFormat("#0.00").format(novemberTotal));
        ret.addObject("TOTAL12", new DecimalFormat("#0.00").format(decemberTotal));
        ret.addObject("TOTAL13", new DecimalFormat("#0.00").format(specialFeeTotal));
        double total = januaryTotal + februaryTotal + marchTotal + aprilTotal + mayTotal + juneTotal + julyTotal + augustTotal + septemberTotal + octoberTotal + novemberTotal + decemberTotal
                + specialFeeTotal;
        ret.addObject("TOTAL", new DecimalFormat("#0.00").format(total));
        ret.addObject(JasperReportsView.KEY_USER_AGENT, getUserAgent(request)); // 用户客户端
        ret.addObject(JasperReportsView.KEY_FORMAT, format);
        ret.addObject(JasperReportsView.KEY_FILE_NAME, "党费收缴数据表");
        JREmptyDataSource emptyData = new JREmptyDataSource();
        if (list.size() == 0) {
            ret.addObject("datasource", emptyData);
        } else {
            ret.addObject("datasource", list);
        }
        return ret;
    }

    /**
     * <B>方法名称：</B>下载特殊党费收缴表报表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("reports/obt/LargePartyFee")
    public ModelAndView LargePartyFee(HttpServletRequest request, HttpServletResponse response) {
        String ccpartyId = getString(request, "ccpartyId");
        CCParty cCPartyMc = ccpartyManager.getCCPartyFromMc(ccpartyId);
        String format = getString(request, "format");

        ModelAndView ret = new ModelAndView();
        ret.addObject("requestObject", request);
        ret.addObject("CCPARTY_NAME", cCPartyMc.getName());

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 12; i++) {
            Map<String, Object> member = new HashMap<String, Object>();
            member.put("SEQUENCE", i + 1);
            member.put("NAME", "");
            member.put("YEAR", "");
            member.put("MOUNT", "");
            member.put("REMARK", "");
            list.add(member);
        }

        ret.addObject(JasperReportsView.KEY_USER_AGENT, getUserAgent(request)); // 用户客户端
        ret.addObject(JasperReportsView.KEY_FORMAT, format);
        ret.addObject(JasperReportsView.KEY_FILE_NAME, "特殊党费统计表");
        JREmptyDataSource emptyData = new JREmptyDataSource();
        if (list.size() == 0) {
            ret.addObject("datasource", emptyData);
        } else {
            ret.addObject("datasource", list);
        }
        return ret;
    }
}
