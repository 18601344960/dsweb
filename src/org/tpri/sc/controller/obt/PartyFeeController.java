/**
 * @description 党费管理控制器
 * @author 王钱俊
 * @since 2015-12-08
 */
package org.tpri.sc.controller.obt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tpri.sc.controller.BaseController;
import org.tpri.sc.entity.obt.PartyFee;
import org.tpri.sc.entity.obt.PartyFeeSpecial;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.service.obt.PartyFeeService;
import org.tpri.sc.service.obt.PartyMemberService;
import org.tpri.sc.service.uam.UserService;
import org.tpri.sc.view.ZTreeView;
import org.tpri.sc.view.obt.PartyFeeCountView;
import org.tpri.sc.view.obt.PartyFeeDetailView;
import org.tpri.sc.view.obt.PartyFeeView;

@Controller
@RequestMapping("/obt")
public class PartyFeeController extends BaseController {

    public static final int PAYFEEBYMONTH = 1;
    public static final int PAYFEEBYSEASON = 3;
    public static final int PAYFEEBYHALFYEAR = 6;
    public static final int PAYFEEBYYEAR = 12;

    @Autowired
    private PartyFeeService partyFeeService;

    @Autowired
    private PartyMemberService partyMemberService;

    @Autowired
    private UserService userService;

    /**
     * <B>方法名称：</B>查询普通党费<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("getPartyFeesList")
    @ResponseBody
    public Map<String, Object> getPartyFeesList(HttpServletRequest request) {
        logger.debug(this.getClass() + "getPartyFeesList begin");
        String ccpartyId = getString(request, "ccpartyId");
        Integer year = getInteger(request, "year");
        Integer month = getInteger(request, "month");
        if (year == null) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            Calendar calendar = Calendar.getInstance();
            month = calendar.get(Calendar.MONTH) + 1;
        }
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");

        List<PartyFeeView> list = partyFeeService.getPartyFeesListByCcparty(start, limit, ccpartyId, year, month);
        Integer total = partyFeeService.countPartyMemberByCcpartyId(ccpartyId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", list);
        map.put("total", total);
        logger.debug(this.getClass() + "getPartyFeesList end");
        return map;
    }

    /**
     * <B>方法名称：</B>获取所有收缴人<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("getUsersByCcparty")
    @ResponseBody
    public Map<String, Object> getUsersByCcparty(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String ccpartyId = getString(request, "ccpartyId");
        List<User> items = partyFeeService.getUsersByCcparty(ccpartyId);
        map.put("items", items);
        return map;
    }

    /**
     * <B>方法名称：</B>通过userID获取党费记录<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("getUsersByUserId")
    @ResponseBody
    public Map<String, Object> getPartyFeesByUserIdAndYear(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = getString(request, "userId");
        Integer year = getInteger(request, "year");
        Integer month = getInteger(request, "month");
        if (year == null) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            Calendar calendar = Calendar.getInstance();
            month = calendar.get(Calendar.MONTH) + 1;
        }
        PartyFee partyFee = partyFeeService.getPartyFeesByUserIdAndYear(userId, year, month);
        map.put("items", partyFee);
        return map;
    }

    /**
     * <B>方法名称：</B>编辑并保存每月实缴费用<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("savePayIn")
    @ResponseBody
    public Map<String, Object> savePayIn(HttpServletRequest request) {
        logger.debug(this.getClass() + " payFee begin");
        String userId = getString(request, "userId");
        double payActually = Double.parseDouble(getString(request, "payActually"));
        Calendar calendar = Calendar.getInstance();
        Integer year = getInteger(request, "year");
        Integer month = getInteger(request, "month");
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH) + 1;
        }
        boolean m = partyFeeService.savePayIn(userId, payActually, year, month);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", m);
        logger.debug(this.getClass() + "payFee end");
        return map;
    }

    /**
     * <B>方法名称：</B>保存签收人<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("saveReceiver")
    @ResponseBody
    public Map<String, Object> saveReceiver(HttpServletRequest request) {
        logger.debug(this.getClass() + " saveReceiver begin");
        String userId = getString(request, "userId");
        String receiver = getString(request, "receiver");
        Calendar calendar = Calendar.getInstance();
        Integer year = getInteger(request, "year");
        Integer month = getInteger(request, "month");
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH) + 1;
        }
        boolean m = partyFeeService.saveReceiver(userId, year, month, receiver);
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("success", m);
        logger.debug(this.getClass() + "saveReceiver end");
        return map;
    }

    /**
     * <B>方法名称：</B>保存签收日期<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月3日
     * @param request
     * @return
     */
    @RequestMapping("saveReceivDate")
    @ResponseBody
    public Map<String, Object> saveReceivDate(HttpServletRequest request) {
        logger.debug(this.getClass() + " saveReceivDate begin");
        String userId = getString(request, "userId");
        Date receiveDate = getDate(request, "receiveDate");
        Calendar calendar = Calendar.getInstance();
        Integer year = getInteger(request, "year");
        Integer month = getInteger(request, "month");
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH) + 1;
        }
        boolean m = partyFeeService.saveReceiveDate(userId, year, month, receiveDate);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", m);
        logger.debug(this.getClass() + "saveReceivDate end");
        return map;
    }

    /**
     * <B>方法名称：</B>保存备注<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年9月27日
     * @param request
     * @return
     */
    @RequestMapping("savePartyFeeRemark")
    @ResponseBody
    public Map<String, Object> savePartyFeeRemark(HttpServletRequest request) {
        logger.debug(this.getClass() + " savePartyFeeRemark begin");
        String userId = getString(request, "userId");
        String remark = getString(request, "remark");
        Calendar calendar = Calendar.getInstance();
        Integer year = getInteger(request, "year");
        Integer month = getInteger(request, "month");
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH) + 1;
        }
        boolean m = partyFeeService.savePartyFeeRemark(userId, year, month, remark);
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("success", m);
        logger.debug(this.getClass() + "savePartyFeeRemark end");
        return map;
    }

    /**
     * <B>方法名称：</B>通过年份获取特殊党费记录<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 王钱俊
     * @since 2015年11月13日
     * @param request
     * @return
     */
    @RequestMapping("getPartyFeeSpecialByYear")
    @ResponseBody
    public Map<String, Object> getPartyFeeSpecialByYear(HttpServletRequest request) {
        logger.debug(this.getClass() + "getPartyFeeSpecialByYear begin");
        Map<String, Object> map = new HashMap<String, Object>();
        Integer year = getInteger(request, "year");
        String ccpartyId = getString(request, "ccpartyId");
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        if (year == null) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        List<PartyFeeSpecial> partyFeeSpecial = partyFeeService.getPartyFeeSpecialByYear(year, ccpartyId, start, limit);
        Integer total = partyFeeService.countPartyMemberByCcpartyId(ccpartyId);
        logger.debug(this.getClass() + " getPartyFeeSpecialByYear end");
        map.put("rows", partyFeeSpecial);
        map.put("total", total);
        return map;
    }

    /**
     * @since 2016年8月23日
     * @param request
     * @return
     */
    @RequestMapping("getPartyFeeSpecialDetailByYear")
    @ResponseBody
    public Map<String, Object> getPartyFeeSpecialDetailByYear(HttpServletRequest request) {
        logger.debug(this.getClass() + "getPartyFeeSpecialDetailByYear begin");
        Map<String, Object> map = new HashMap<String, Object>();
        Integer year = getInteger(request, "year");
        String ccpartyId = getString(request, "ccpartyId");
        String userId = getString(request, "userId");
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        if (year == null) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        List<PartyFeeSpecial> partyFeeSpecial = partyFeeService.getPartyFeeSpecialDetailByYear(year, ccpartyId, userId, start, limit);
        int total = partyFeeService.getTotalPartyFeeSpecialDetailByYear(year, ccpartyId, userId);
        logger.debug(this.getClass() + " getPartyFeeSpecialDetailByYear end");
        map.put("rows", partyFeeSpecial);
        map.put("total", total);
        return map;

    }

    /**
     * <B>方法名称：</B>添加特殊党费<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 王钱俊
     * @since 2015年11月13日
     * @param request
     * @return
     */
    @RequestMapping("addPartyFeeSpecial")
    @ResponseBody
    public Map<String, Object> addPartyFeeSpecial(HttpServletRequest request) {
        logger.debug(this.getClass() + "addPartyFeeSpecial begin");
        Map<String, String> param = new HashMap<String, String>();
        UserMc user = loadUserMc(request);
        param.put("userId", getString(request, "userId"));
        param.put("ccpartyId", getString(request, "ccpartyId"));
        Integer year = getInteger(request, "year");
        if (year == null) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        param.put("year", year + "");
        param.put("amount", getString(request, "amount"));
        param.put("remark", getString(request, "remark"));
        param.put("createUserId", user.getId());
        boolean m = partyFeeService.addPartyFeeSpecial(param);
        Map<String, Object> map = new HashMap<String, Object>();
        logger.debug(this.getClass() + " addPartyFeeSpecial end");
        map.put("result", m);
        return map;
    }

    /**
     * <B>方法名称：</B>删除特殊党费<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 王钱俊
     * @since 2015年11月13日
     * @param request
     * @return
     */
    @RequestMapping("deletePartyFeeSpecial")
    @ResponseBody
    public Map<String, Object> deletePartyFeeSpecial(HttpServletRequest request) {
        logger.debug(this.getClass() + "addPartyFeeSpecial begin");
        Map<String, Object> map = new HashMap<String, Object>();
        String id = getString(request, "id");
        boolean m = partyFeeService.deletePartyFeeSpecial(id);
        logger.debug(this.getClass() + " addPartyFeeSpecial end");
        map.put("result", m);
        return map;
    }

    /**
     * <B>方法名称：</B>编辑特殊党费<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 王钱俊
     * @since 2015年11月13日
     * @param request
     * @return
     */
    @RequestMapping("updatePartyFeeSpecial")
    @ResponseBody
    public Map<String, Object> updatePartyFeeSpecial(HttpServletRequest request) {
        logger.debug(this.getClass() + "updatePartyFeeSpecial begin");
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", getString(request, "id"));
        param.put("userId", getString(request, "userId"));
        param.put("amount", getString(request, "amount"));
        param.put("remark", getString(request, "remark"));
        boolean m = partyFeeService.updatePartyFeeSpecial(param);
        Map<String, Object> map = new HashMap<String, Object>();
        logger.debug(this.getClass() + " updatePartyFeeSpecial end");
        map.put("result", m);
        return map;
    }

    /**
     * <B>方法名称：</B>通过Id获取特殊党费<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 王钱俊
     * @since 2015年11月16日
     * @param request
     * @return
     */
    @RequestMapping("getPartyFeeSpecialById")
    @ResponseBody
    public Map<String, Object> getPartyFeeSpecialById(HttpServletRequest request) {
        logger.debug(this.getClass() + "getPartyFeeSpecialById begin");
        String id = getString(request, "id");
        PartyFeeSpecial partyFeeSpecial = partyFeeService.getPartyFeeSpecialById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        logger.debug(this.getClass() + " getPartyFeeSpecialById end");
        map.put("result", partyFeeSpecial);
        return map;
    }

    /**
     * <B>方法名称：</B>查询统计党费列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param request
     * @return
     */
    @RequestMapping("countPartyFeeByYear")
    @ResponseBody
    public Map<String, Object> countPartyFeeByYear(HttpServletRequest request) {
        logger.debug(this.getClass() + "countPartyFeeByYear begin");
        String ccpartyId = getString(request, "ccpartyId");
        Integer year = getInteger(request, "year");
        if (year == null) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        Integer start = getInteger(request, "offset");
        Integer limit = getInteger(request, "limit");
        List<PartyFeeCountView> partyFeeCountViews = partyFeeService.countPartyFeeByYear(start, limit, ccpartyId, year);
        Integer total = partyFeeService.countPartyMemberByCcpartyId(ccpartyId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", partyFeeCountViews);
        map.put("total", total);
        logger.debug(this.getClass() + " countPartyFeeByYear end");
        return map;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织某年度的党费收支情况<BR>
     * <B>概要说明：</B>支部手册（组织电子活动证）使用<BR>
     * 
     * @author 赵子靖
     * @since 2016年7月4日
     * @param request
     * @return
     */
    @RequestMapping("getCcpartyPartyFeeMonthTotalDetailForCard")
    @ResponseBody
    public Map<String, Object> getCcpartyPartyFeeMonthTotalDetailForCard(HttpServletRequest request) {
        logger.debug(this.getClass() + "getCcpartyPartyFeeMonthTotalDetailForCard begin");
        String ccpartyId = getString(request, "ccpartyId");
        Integer year = getInt(request, "year");
        PartyFeeDetailView detailView = partyFeeService.getCcpartyPartyFeeMonthTotalDetailForCard(ccpartyId, year);
        Map<String, Object> map = new HashMap<String, Object>();
        logger.debug(this.getClass() + " getCcpartyPartyFeeMonthTotalDetailForCard end");
        map.put("item", detailView);
        return map;
    }

    /**
     * 
     * <B>方法名称：</B>获取某用户某年都缴纳党费情况<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月6日
     * @param request
     * @return
     */
    @RequestMapping("getPartyFeeByUserForCard")
    @ResponseBody
    public Map<String, Object> getPartyFeeByUserForCard(HttpServletRequest request) {
        logger.debug(this.getClass() + "getPartyFeeByUserForCard begin");
        String userId = getString(request, "userId");
        Integer year = getInt(request, "year");
        PartyFeeDetailView partyFeeDetailView = partyFeeService.getPartyFeeByUserForCard(userId, year);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("item", partyFeeDetailView);
        logger.debug(this.getClass() + " getPartyFeeByUserForCard end");
        return map;
    }

}
