package org.tpri.sc.service.obt;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.sc.entity.obt.PartyFee;
import org.tpri.sc.entity.obt.PartyFeeReport;
import org.tpri.sc.entity.obt.PartyFeeSpecial;
import org.tpri.sc.entity.obt.PartyFeeUse;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.PartyFeeManager;
import org.tpri.sc.manager.obt.PartyMemberManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.sys.EnvironmentManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.obt.PartyFeeCountView;
import org.tpri.sc.view.obt.PartyFeeDetailView;
import org.tpri.sc.view.obt.PartyFeeView;
import org.tpri.sc.view.obt.PartyFeesDetailView;

/**
 * @description 党费管理服务类
 * @author 王钱俊
 * @since 2015-10-19
 */
@Service("PartyFeeService")
public class PartyFeeService {

    public static final int MONTH = 0;

    @Autowired
    private PartyFeeManager partyFeeManager;

    @Autowired
    private PartyMemberManager partyMemberManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    CCPartyManager ccpartyManager;

    @Resource(name = "EnvironmentManager")
    EnvironmentManager environmentManager;

    @Resource(name = "CCpartyService")
    private CCpartyService cCpartyService;

    public Logger logger = Logger.getLogger(PartyFeeService.class);

    // 根据ccpartyId获取党费列表
    public List<PartyFeeView> getPartyFeesListByCcparty(Integer start, Integer limit, String ccpartyId, Integer year, Integer month) {
        List<PartyMember> list = partyFeeManager.getPartyMemberByCcpartyIdAndOrderBySequence(start, limit, ccpartyId);
        List<User> userMcList = userManager.getPartyWorkersByCcparty(ccpartyId);
        List<UserMc> receiverList = new ArrayList<UserMc>();
        if (userMcList != null && userMcList.size() > 0) {
            for (User user : userMcList) {
                UserMc parentUser = userManager.getUserFromMc(user.getParentUserId());
                if (parentUser == null) {
                    continue;
                }
                receiverList.add(parentUser);
            }
        }
        List<PartyFeeView> partyFeeViews = new ArrayList<>();
        for (PartyMember partyMember : list) {
            PartyFeeView partyFeeView = new PartyFeeView();
            String userId = partyMember.getId();
            PartyFee partyFee = partyFeeManager.getPartyFeesByUserIdAndYear(userId, year, month);
            if (partyFee == null) {
                partyFee = new PartyFee();
                partyFee.setId(UUIDUtil.id());
                partyFee.setUserId(partyMember.getId());
                if (year != null) {
                    partyFee.setYear(year);
                }
                if (month != null) {
                    partyFee.setMonth(month);
                }
                partyFee.setBaseSalary(0);
                partyFee.setRate(0);
                partyFee.setPayAble(0);
                partyFee.setPayIn(0);
            }
            partyFeeView.setPartyFee(partyFee);
            partyFeeView.setId(partyFee.getId());
            partyFeeView.setReceiverList(receiverList);
            UserMc user = userManager.getUserFromMc(userId);
            if (user == null) {
                continue;
            }
            partyFeeView.setName(user.getName());
            partyFeeView.setCcpartyId(partyMember.getCcpartyId());
            partyFeeViews.add(partyFeeView);
        }
        return partyFeeViews;
    }

    /**
     * <B>方法名称：</B>根据ccpartyId统计党费列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月23日
     * @param ccpartyId
     * @return
     */
    public int countPartyMemberByCcpartyId(String ccpartyId) {
        return partyFeeManager.countPartyMemberByCcpartyId(ccpartyId);
    }

    /**
     * <B>方法名称：</B>获取所有党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 杨兴
     * @since 2016年8月23日
     * @param ccpartyId
     * @return
     */
    public List<User> getUsersByCcparty(String ccpartyId) {
        return userManager.getUserByCcparty(ccpartyId);
    }

    /**
     * <B>方法名称：</B>通过userID获取党费记录<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param userId
     * @param year
     * @param month
     * @return
     */
    public PartyFee getPartyFeesByUserIdAndYear(String userId, int year, int month) {
        PartyFee partyFee = partyFeeManager.getPartyFeesByUserIdAndYear(userId, year, month);
        return partyFee;
    }

    /**
     * <B>方法名称：</B>保存月实际缴纳党费<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param userId
     * @param payActually
     * @param year
     * @param month
     * @return
     */
    public boolean savePayIn(String userId, double payActually, int year, int month) {
        PartyFee partyFee = partyFeeManager.getPartyFeesByUserIdAndYear(userId, year, month);
        if (partyFee == null) {
            partyFee = new PartyFee();
            partyFee.setId(UUIDUtil.id());
            partyFee.setUserId(userId);
            partyFee.setYear(year);
            partyFee.setMonth(month);
            partyFee.setBaseSalary(0);
            partyFee.setRate(0);
            partyFee.setPayAble(0);
            partyFee.setPayIn(0);
        }
        partyFee.setPayIn(payActually);
        return partyFeeManager.saveOrUpdate(partyFee);

    }

    /**
     * <B>方法名称：</B>保存签收人<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param userId
     * @param year
     * @param month
     * @param receiver
     * @return
     */
    public boolean saveReceiver(String userId, int year, int month, String receiver) {
        PartyFee partyFee = partyFeeManager.getPartyFeesByUserIdAndYear(userId, year, month);
        if (partyFee == null) {
            partyFee = new PartyFee();
            partyFee.setId(UUIDUtil.id());
            partyFee.setUserId(userId);
            partyFee.setYear(year);
            partyFee.setMonth(month);
            partyFee.setBaseSalary(0);
            partyFee.setRate(0);
            partyFee.setPayAble(0);
            partyFee.setPayIn(0);
        }
        partyFee.setReceiver(receiver);
        return partyFeeManager.saveOrUpdate(partyFee);
    }

    /**
     * <B>方法名称：</B>保存签收日期<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param userId
     * @param year
     * @param month
     * @param receiveDate
     * @return
     */
    public boolean saveReceiveDate(String userId, int year, int month, Date receiveDate) {
        PartyFee partyFee = partyFeeManager.getPartyFeesByUserIdAndYear(userId, year, month);
        if (partyFee == null) {
            partyFee = new PartyFee();
            partyFee.setId(UUIDUtil.id());
            partyFee.setUserId(userId);
            partyFee.setYear(year);
            partyFee.setMonth(month);
            partyFee.setBaseSalary(0);
            partyFee.setRate(0);
            partyFee.setPayAble(0);
            partyFee.setPayIn(0);
        }
        partyFee.setReceiveDate(receiveDate);
        return partyFeeManager.saveOrUpdate(partyFee);
    }

    /**
     * <B>方法名称：</B>保存备注<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年9月27日
     * @param userId
     * @param year
     * @param month
     * @param receiverId
     * @return
     */
    public boolean savePartyFeeRemark(String userId, int year, int month, String remark) {
        PartyFee partyFee = partyFeeManager.getPartyFeesByUserIdAndYear(userId, year, month);
        if (partyFee == null) {
            partyFee = new PartyFee();
            partyFee.setId(UUIDUtil.id());
            partyFee.setUserId(userId);
            partyFee.setYear(year);
            partyFee.setMonth(month);
            partyFee.setBaseSalary(0);
            partyFee.setRate(0);
            partyFee.setPayAble(0);
            partyFee.setPayIn(0);
        }
        partyFee.setRemark(remark);
        return partyFeeManager.saveOrUpdate(partyFee);
    }

    //根据ID获取特殊党费记录
    public PartyFeeSpecial getPartyFeeSpecialById(String id) {
        PartyFeeSpecial partyFeeSpecial = partyFeeManager.getPartyFeeSpecialById(id);
        UserMc user = userManager.getUserFromMc(partyFeeSpecial.getUserId());
        partyFeeSpecial.setUserMc(user);
        return partyFeeSpecial;
    }

    //缴纳特殊党费
    public boolean addPartyFeeSpecial(Map<String, String> param) {
        String userId = param.get("userId");
        String ccpartyId = param.get("ccpartyId");
        int year = Integer.parseInt(param.get("year"));
        double amount = Double.parseDouble(param.get("amount"));
        String remark = param.get("remark");
        String createUserId = param.get("createUserId");
        PartyFeeSpecial partyFeeSpecial = new PartyFeeSpecial();
        partyFeeSpecial.setId(UUIDUtil.id());
        partyFeeSpecial.setCcpartyId(ccpartyId);
        partyFeeSpecial.setUserId(userId);
        partyFeeSpecial.setYear(year);
        partyFeeSpecial.setAmount(amount);
        partyFeeSpecial.setRemark(remark);
        partyFeeSpecial.setCreateUserId(createUserId);
        partyFeeSpecial.setCreateTime(new Date());
        return partyFeeManager.add(partyFeeSpecial);
    }

    //更新特殊党费
    public boolean updatePartyFeeSpecial(Map<String, String> param) {
        PartyFeeSpecial partyFeeSpecial = partyFeeManager.getPartyFeeSpecialById(param.get("id"));
        partyFeeSpecial.setUserId(param.get("userId"));
        partyFeeSpecial.setAmount(Double.parseDouble(param.get("amount")));
        partyFeeSpecial.setRemark(param.get("remark"));
        return partyFeeManager.updatePartySpecial(partyFeeSpecial);
    }

    //删除特殊党费
    public boolean deletePartyFeeSpecial(String id) {
        return partyFeeManager.deletePartySpecial(id);
    }

    //根据年份和党组织Id获取特殊党费记录
    public List getPartyFeeSpecialByYear(int year, String ccpartyId, Integer start, Integer limit) {
        List<PartyFeeSpecial> list = new ArrayList<PartyFeeSpecial>();
        List<PartyMember> partyMembers = partyFeeManager.getPartyMemberByCcpartyIdAndOrderBySequence(start, limit, ccpartyId);
        for (PartyMember partyMember : partyMembers) {
            String userId = partyMember.getId();
            UserMc user = userManager.getUserFromMc(userId);
            List partyFeeSpecials = partyFeeManager.getEveryUserPartyFeeSpecialByYear(year, ccpartyId, userId);
            PartyFeeSpecial partyFeeSpecial = new PartyFeeSpecial();
            Iterator itertor = partyFeeSpecials.iterator();
            if (partyFeeSpecials != null && partyFeeSpecials.size() > 0) {
                while (itertor.hasNext()) {
                    Object[] object = (Object[]) itertor.next();
                    partyFeeSpecial.setUserMc(user);
                    partyFeeSpecial.setUserId(userId);
                    partyFeeSpecial.setCcpartyId((String) object[0]);
                    partyFeeSpecial.setYear((int) object[2]);
                    partyFeeSpecial.setAmount((double) object[3]);
                    list.add(partyFeeSpecial);
                }
            } else {
                partyFeeSpecial.setCcpartyId(ccpartyId);
                partyFeeSpecial.setUserId(userId);
                partyFeeSpecial.setUserMc(user);
                partyFeeSpecial.setYear(year);
                partyFeeSpecial.setAmount(0);
                list.add(partyFeeSpecial);
            }
        }
        return list;
    }

    public List<PartyFeeSpecial> getPartyFeeSpecialDetailByYear(int year, String ccpartyId, String userId, Integer start, Integer limit) {
        List<PartyFeeSpecial> partyFeeSpecials = partyFeeManager.getPartyFeeSpecialDetailByYear(year, ccpartyId, userId, start, limit);
        if (partyFeeSpecials != null && partyFeeSpecials.size() > 0) {
            for (PartyFeeSpecial partyFeeSpecial : partyFeeSpecials) {
                UserMc user = userManager.getUserFromMc(partyFeeSpecial.getUserId());
                partyFeeSpecial.setUserMc(user);
            }
        }
        return partyFeeSpecials;
    }

    //根据年份和党组织Id和userID获取特殊党费条数
    public int getTotalPartyFeeSpecialDetailByYear(int year, String ccpartyId, String userId) {
        return partyFeeManager.getTotalPartyFeeSpecialDetailByYear(year, ccpartyId, userId);
    }

    /**
     * <B>方法名称：</B>查询统计党费列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年8月5日
     * @param start
     * @param limit
     * @param ccpartyId
     * @param year
     * @return
     */
    public List<PartyFeeCountView> countPartyFeeByYear(Integer start, Integer limit, String ccpartyId, int year) {
        List<PartyMember> partyMembers = partyFeeManager.getPartyMemberByCcpartyIdAndOrderBySequence(start, limit, ccpartyId);
        List<PartyFeeCountView> partyFeeCountViews = new ArrayList<PartyFeeCountView>();
        double totalJanuary = 0, totalFebruary = 0, totalMarch = 0, totalApril = 0, totalMay = 0, totalJune = 0, totalJuly = 0, totalAugust = 0, totalSeptember = 0, totalOctorber = 0, totalNovember = 0, totalDecember = 0, totalSpecialFee = 0;
        for (PartyMember partyMember : partyMembers) {
            String userId = partyMember.getId();
            List<PartyFee> partyFeeList = partyFeeManager.countPartyFeeByYear(userId, year);
            List partyFeeSpecialList = partyFeeManager.getEveryUserPartyFeeSpecialByYear(year, ccpartyId, userId);
            Iterator itertor = partyFeeSpecialList.iterator();
            PartyFeeCountView partyFeeCountView = new PartyFeeCountView();
            if (partyFeeList != null && partyFeeList.size() > 0) {
                for (PartyFee partyFee : partyFeeList) {
                    if (partyFee.getMonth() == 1) {
                        partyFeeCountView.setJanuary(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalJanuary += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 2) {
                        partyFeeCountView.setFebruary(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalFebruary += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 3) {
                        partyFeeCountView.setMarch(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalMarch += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 4) {
                        partyFeeCountView.setApril(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalApril += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 5) {
                        partyFeeCountView.setMay(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalMay += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 6) {
                        partyFeeCountView.setJune(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalJune += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 7) {
                        partyFeeCountView.setJuly(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalJuly += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 8) {
                        partyFeeCountView.setAugust(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalAugust += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 9) {
                        partyFeeCountView.setSeptember(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalSeptember += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 10) {
                        partyFeeCountView.setOctorber(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalOctorber += partyFee.getPayIn();
                        }
                    } else if (partyFee.getMonth() == 11) {
                        partyFeeCountView.setNovember(partyFee.getPayIn());
                        if (partyFee.getPayIn() != -1) {
                            totalNovember += partyFee.getPayIn();
                        }
                    } else {
                        partyFeeCountView.setDecember(partyFee.getPayIn());
                        totalDecember += partyFee.getPayIn();
                    }
                }
            }
            if (partyFeeSpecialList != null && partyFeeSpecialList.size() > 0) {
                while (itertor.hasNext()) {
                    Object[] object = (Object[]) itertor.next();
                    partyFeeCountView.setSpecialFee((double) object[3]);
                    totalSpecialFee += (double) object[3];
                }
            }
            partyFeeCountView.setId(UUIDUtil.id());
            partyFeeCountView.setUserId(partyMember.getId());
            partyFeeCountView.setCcpartyId(ccpartyId);
            partyFeeCountView.setYear(year);
            UserMc user = userManager.getUserFromMc(userId);
            if (user == null) {
                continue;
            }
            partyFeeCountView.setName(user.getName());
            partyFeeCountViews.add(partyFeeCountView);
        }
        PartyFeeCountView totalPartyFeeCountView = new PartyFeeCountView();
        totalPartyFeeCountView.setId(UUIDUtil.id());
        totalPartyFeeCountView.setCcpartyId(ccpartyId);
        totalPartyFeeCountView.setYear(year);
        totalPartyFeeCountView.setName("合计");
        totalPartyFeeCountView.setJanuary(totalJanuary);
        totalPartyFeeCountView.setFebruary(totalFebruary);
        totalPartyFeeCountView.setMarch(totalMarch);
        totalPartyFeeCountView.setApril(totalApril);
        totalPartyFeeCountView.setMay(totalMay);
        totalPartyFeeCountView.setJune(totalJune);
        totalPartyFeeCountView.setJuly(totalJuly);
        totalPartyFeeCountView.setAugust(totalAugust);
        totalPartyFeeCountView.setSeptember(totalSeptember);
        totalPartyFeeCountView.setOctorber(totalOctorber);
        totalPartyFeeCountView.setNovember(totalNovember);
        totalPartyFeeCountView.setDecember(totalDecember);
        totalPartyFeeCountView.setSpecialFee(totalSpecialFee);
        partyFeeCountViews.add(totalPartyFeeCountView);
        return partyFeeCountViews;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织某年度的党费收支情况<BR>
     * <B>概要说明：</B>支部手册（组织电子活动证）使用<BR>
     * 
     * @author 赵子靖
     * @since 2016年7月4日
     * @param ccpartyId
     * @param year
     * @return
     */
    public PartyFeeDetailView getCcpartyPartyFeeMonthTotalDetailForCard(String ccpartyId, Integer year) {
        if (year == null) {
            year = Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        }
        PartyFeeDetailView detailView = new PartyFeeDetailView(); //党费收缴明细视图
        CCParty ccparty = ccpartyManager.getCCPartyById(ccpartyId);
        if (ccparty == null) {
            return null;
        }
        detailView.setCcparty(ccparty);
        detailView.setYear(year);
        List<PartyFeesDetailView> detailViews = new ArrayList<PartyFeesDetailView>(); //党费收缴明细列表
        for (int i = 1; i <= 12; i++) {
            PartyFeesDetailView details = new PartyFeesDetailView();
            details.setMonth(i);
            detailViews.add(details);
        }
        //保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");
        //党费缴纳统计
        List<PartyFee> partyFees = partyFeeManager.getPartyFeesByCcparty(ccpartyId, year);
        if (partyFees != null && partyFees.size() > 0) {
            for (PartyFee fee : partyFees) {
                detailViews.get(fee.getMonth() - 1).setShoulePartyFee(detailViews.get(fee.getMonth() - 1).getShoulePartyFee() + fee.getPayAble());//应缴纳
                if (fee.getPayIn() != -1) {
                    detailViews.get(fee.getMonth() - 1).setActuallyPartyFee(detailViews.get(fee.getMonth() - 1).getActuallyPartyFee() + fee.getPayIn());//实际缴纳
                }
            }
            //大额党费
            List<Object> specialObjs = partyFeeManager.getPartyFeeSpecialForMonth(ccpartyId, year);
            if (specialObjs != null && specialObjs.size() > 0) {
                for (int i = 0; i < specialObjs.size(); i++) {
                    Object[] objs = (Object[]) specialObjs.get(i);
                    String month = String.valueOf(objs[1]);
                    switch (Integer.parseInt(month)) {
                    case 1:
                        detailViews.get(0).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 2:
                        detailViews.get(1).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 3:
                        detailViews.get(2).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 4:
                        detailViews.get(3).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 5:
                        detailViews.get(4).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 6:
                        detailViews.get(5).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 7:
                        detailViews.get(6).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 8:
                        detailViews.get(7).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 9:
                        detailViews.get(8).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 10:
                        detailViews.get(9).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 11:
                        detailViews.get(10).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    case 12:
                        detailViews.get(11).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                        break;
                    default:
                        break;
                    }
                }
            }
            detailView.setShoulePartyFeeTotal(detailView.getShoulePartyFeeTotal() + detailViews.get(0).getShoulePartyFee() + detailViews.get(1).getShoulePartyFee()
                    + detailViews.get(2).getShoulePartyFee() + detailViews.get(3).getShoulePartyFee() + detailViews.get(4).getShoulePartyFee() + detailViews.get(5).getShoulePartyFee()
                    + detailViews.get(6).getShoulePartyFee() + detailViews.get(7).getShoulePartyFee() + detailViews.get(8).getShoulePartyFee() + detailViews.get(9).getShoulePartyFee()
                    + detailViews.get(10).getShoulePartyFee() + detailViews.get(11).getShoulePartyFee());
            detailView.setActuallyPartyFeeTotal(detailView.getActuallyPartyFeeTotal() + detailViews.get(0).getActuallyPartyFee() + detailViews.get(1).getActuallyPartyFee()
                    + detailViews.get(2).getActuallyPartyFee() + detailViews.get(3).getActuallyPartyFee() + detailViews.get(4).getActuallyPartyFee() + detailViews.get(5).getActuallyPartyFee()
                    + detailViews.get(6).getActuallyPartyFee() + detailViews.get(7).getActuallyPartyFee() + detailViews.get(8).getActuallyPartyFee() + detailViews.get(9).getActuallyPartyFee()
                    + detailViews.get(10).getActuallyPartyFee() + detailViews.get(11).getActuallyPartyFee());
            detailView.setBigPartyFeeTotal(detailView.getBigPartyFeeTotal() + detailViews.get(0).getBigPartyFee() + detailViews.get(1).getBigPartyFee() + detailViews.get(2).getBigPartyFee()
                    + detailViews.get(3).getBigPartyFee() + detailViews.get(4).getBigPartyFee() + detailViews.get(5).getBigPartyFee() + detailViews.get(6).getBigPartyFee()
                    + detailViews.get(7).getBigPartyFee() + detailViews.get(8).getBigPartyFee() + detailViews.get(9).getBigPartyFee() + detailViews.get(10).getBigPartyFee()
                    + detailViews.get(11).getBigPartyFee());
        }
        detailView.setFeesDeatilViews(detailViews);
        return detailView;
    }

    /**
     * 
     * <B>方法名称：</B>获取某党员某年份缴纳记录<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年7月6日
     * @param userId
     * @param year
     * @return
     */
    public PartyFeeDetailView getPartyFeeByUserForCard(String userId, Integer year) {
        if (year == null) {
            year = Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        }
        PartyFeeDetailView detailView = new PartyFeeDetailView(); //党费收缴明细视图
        detailView.setYear(year);
        List<PartyFeesDetailView> detailViews = new ArrayList<PartyFeesDetailView>(); //党费收缴明细列表
        List<PartyFee> partyFees = partyFeeManager.getPartyFeesByUser(userId, year); //记录集合
        for (int i = 1; i <= 12; i++) {
            PartyFeesDetailView details = new PartyFeesDetailView();
            details.setMonth(i);
            detailViews.add(details);
        }
        if (partyFees != null && partyFees.size() > 0) {
            for (int i = 0; i < partyFees.size(); i++) {
                PartyFee fee = partyFees.get(i);
                detailViews.get(fee.getMonth() - 1).setShoulePartyFee(fee.getPayAble());
                detailViews.get(fee.getMonth() - 1).setActuallyPartyFee(fee.getPayIn());
            }
        }
        //大额党费
        List<Object> specialObjs = partyFeeManager.getPartyFeeSpecialByUserForMonth(userId, year);
        if (specialObjs != null && specialObjs.size() > 0) {
            for (int i = 0; i < specialObjs.size(); i++) {
                Object[] objs = (Object[]) specialObjs.get(i);
                String month = String.valueOf(objs[1]);
                switch (Integer.parseInt(month)) {
                case 1:
                    detailViews.get(0).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 2:
                    detailViews.get(1).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 3:
                    detailViews.get(2).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 4:
                    detailViews.get(3).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 5:
                    detailViews.get(4).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 6:
                    detailViews.get(5).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 7:
                    detailViews.get(6).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 8:
                    detailViews.get(7).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 9:
                    detailViews.get(8).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 10:
                    detailViews.get(9).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 11:
                    detailViews.get(10).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                case 12:
                    detailViews.get(11).setBigPartyFee(Double.parseDouble(String.valueOf(objs[2])));
                    break;
                default:
                    break;
                }
            }
        }
        //应缴纳总额
        detailView.setShoulePartyFeeTotal(detailView.getShoulePartyFeeTotal() + detailViews.get(0).getShoulePartyFee() + detailViews.get(1).getShoulePartyFee()
                + detailViews.get(2).getShoulePartyFee() + detailViews.get(3).getShoulePartyFee() + detailViews.get(4).getShoulePartyFee() + detailViews.get(5).getShoulePartyFee()
                + detailViews.get(6).getShoulePartyFee() + detailViews.get(7).getShoulePartyFee() + detailViews.get(8).getShoulePartyFee() + detailViews.get(9).getShoulePartyFee()
                + detailViews.get(10).getShoulePartyFee() + detailViews.get(11).getShoulePartyFee());
        //实缴总额
        detailView.setActuallyPartyFeeTotal(detailView.getActuallyPartyFeeTotal() + detailViews.get(0).getActuallyPartyFee() + detailViews.get(1).getActuallyPartyFee()
                + detailViews.get(2).getActuallyPartyFee() + detailViews.get(3).getActuallyPartyFee() + detailViews.get(4).getActuallyPartyFee() + detailViews.get(5).getActuallyPartyFee()
                + detailViews.get(6).getActuallyPartyFee() + detailViews.get(7).getActuallyPartyFee() + detailViews.get(8).getActuallyPartyFee() + detailViews.get(9).getActuallyPartyFee()
                + detailViews.get(10).getActuallyPartyFee() + detailViews.get(11).getActuallyPartyFee());
        detailView.setBigPartyFeeTotal(detailView.getBigPartyFeeTotal() + detailViews.get(0).getBigPartyFee() + detailViews.get(1).getBigPartyFee() + detailViews.get(2).getBigPartyFee()
                + detailViews.get(3).getBigPartyFee() + detailViews.get(4).getBigPartyFee() + detailViews.get(5).getBigPartyFee() + detailViews.get(6).getBigPartyFee()
                + detailViews.get(7).getBigPartyFee() + detailViews.get(8).getBigPartyFee() + detailViews.get(9).getBigPartyFee() + detailViews.get(10).getBigPartyFee()
                + detailViews.get(11).getBigPartyFee());
        detailView.setFeesDeatilViews(detailViews);
        return detailView;
    }

    /**
     * 
     * <B>方法名称：</B>党员手册党费缴纳<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月1日
     * @param userId
     * @param year
     * @param ret
     */
    public void setPartyFeeForMemberCard(String userId, Integer year, ModelAndView ret) {
        PartyFeeDetailView detailView = this.getPartyFeeByUserForCard(userId, year);
        List<Map<String, Object>> moneyList = new ArrayList<Map<String, Object>>();
        for (PartyFeesDetailView view : detailView.getFeesDeatilViews()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("MONTH", view.getMonth() + "月");
            map.put("SJDF", view.getActuallyPartyFee());
            map.put("DEDF", view.getBigPartyFee());
            moneyList.add(map);
        }
        ret.addObject("mainData", moneyList);
    }
}
