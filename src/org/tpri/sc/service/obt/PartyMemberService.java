package org.tpri.sc.service.obt;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.obt.DevelopmentProcedure;
import org.tpri.sc.entity.obt.DevelopmentProcedureCommonContent;
import org.tpri.sc.entity.obt.DevelopmentProcedureEnum;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.obt.PartyMemberDevelopmentInfo;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.sys.Code;
import org.tpri.sc.entity.uam.Role;
import org.tpri.sc.entity.uam.User;
import org.tpri.sc.entity.uam.UserMc;
import org.tpri.sc.manager.obt.DevelopmentProcedureCommonContentManager;
import org.tpri.sc.manager.obt.DevelopmentProcedureManager;
import org.tpri.sc.manager.obt.PartyMemberDevelopmentInfoManager;
import org.tpri.sc.manager.obt.PartyMemberManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.org.OrganizationManager;
import org.tpri.sc.manager.sys.CodeManager;
import org.tpri.sc.manager.uam.RoleManager;
import org.tpri.sc.manager.uam.UserManager;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.service.uam.UserService;
import org.tpri.sc.util.BaseConfig;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.util.FileUtil;
import org.tpri.sc.util.MD5Util;
import org.tpri.sc.util.PinYinUtil;
import org.tpri.sc.util.TpriStringUtils;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.ZTreeView;
import org.tpri.sc.view.obt.PartymemberDevelopmentProcedureView;

/**
 * @description 党员服务类
 * @author 易文俊
 * @since 2015-04-24
 */

@Service("PartyMemberService")
public class PartyMemberService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private PartyMemberManager partyMemberManager;
    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;
    @Autowired
    private CCpartyService ccpartyService;
    @Autowired
    private DevelopmentProcedureManager developmentProcedureManager;
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private PartyMemberDevelopmentInfoManager partyMemberDevelopmentInfoManager;
    @Autowired
    private CodeManager codeManager;
    @Autowired
    private ElectionMemberTitleService electionMemberTitleService;
    @Autowired
    private DevelopmentProcedureCommonContentManager developmentProcedureCommonContentManager;
    @Autowired
    private RoleManager roleManager;

    /**
     * <B>方法名称：</B>获取所有党员（预备党员和正式党员），如果ccpartyId有值则获取某组织的党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月12日
     * @return
     */
    public List<PartyMember> getPartyMemberList(String ccpartyId, Integer offset, Integer limit) {
        List<PartyMember> partyMemberlist = partyMemberManager.getFormalPartyMemberList(ccpartyId, offset, limit);
        List<PartyMember> partyMemberlists = new ArrayList<PartyMember>();
        for (PartyMember partyMember : partyMemberlist) {
            CCParty cCPartyMc = ccpartyManager.getCCPartyFromMc(partyMember.getCcpartyId());
            partyMember.ccpartyId = cCPartyMc.getId();
            partyMember.setCcparty(cCPartyMc);
            partyMember.setUser(userManager.getUserFromMc(partyMember.getId()));
            partyMemberlists.add(partyMember);
        }
        return partyMemberlists;
    }

    /**
     * <B>方法名称：</B>获取所有党员数目（预备党员和正式党员），如果ccpartyId有值则获取某组织的党员数目<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年1月12日
     * @param ccpartyId
     * @return
     */

    public Integer getPartyMemberTotal(String ccpartyId) {
        return partyMemberManager.getFormalPartyMemberTotal(ccpartyId);
    }

    /**
     * 
     * @Description: 获取党员信息
     * @param id
     * @return
     */
    public PartyMember getPartyMember(String id) {
        PartyMember PartyMember = partyMemberManager.getPartyMember(id);
        return PartyMember;
    }

    /**
     * 
     * @Description: 获取党员信息
     * @param start
     * @param limit
     * @return
     */
    public List loadPartyMember(Integer start, Integer limit) {
        List<PartyMember> partyMemberlist = partyMemberManager.getPartyMemberList(start, limit);
        return partyMemberlist;
    }

    /**
     * 
     * <B>方法名称：</B>获取党组织下的党员数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年9月24日
     * @param ccpartyId
     * @return
     */
    public Integer getPartymemberTotalByCcparty(String ccpartyId) {
        return partyMemberManager.getPartyMemberTotalByCcpartyId(ccpartyId);
    }

    /**
     * 
     * @Description: 根据党组织获取党员信息
     * @param start
     * @param limit
     * @param ccpartyId
     * @return
     */
    public List<PartyMember> loadPartyMemberByCcpartyId(Integer start, Integer limit, String ccpartyId) {
        List<PartyMember> partyMemberlist = partyMemberManager.getPartyMemberByCcpartyId(start, limit, ccpartyId);
        List<PartyMember> partyMemberlists = new ArrayList<PartyMember>();
        for (PartyMember partyMember : partyMemberlist) {
            CCParty cCPartyMc = ccpartyManager.getCCPartyFromMc(partyMember.getCcpartyId());
            partyMember.ccpartyId = cCPartyMc.getId();
            partyMember.setCcparty(cCPartyMc);
            partyMember.setUser(userManager.getUserFromMc(partyMember.getId()));
            partyMemberlists.add(partyMember);
        }
        return partyMemberlists;
    }

    public List<ZTreeView> loadTreePartyMemberByCcpartyId(Integer start, Integer limit, String ccpartyId) {
        List<PartyMember> partyMemberlist = partyMemberManager.getPartyMemberByCcpartyId(start, limit, ccpartyId);
        List<ZTreeView> treeList = new ArrayList<ZTreeView>();
        for (PartyMember partyMember : partyMemberlist) {
            ZTreeView tree = new ZTreeView();
            CCParty cCPartyMc = ccpartyManager.getCCPartyFromMc(partyMember.getCcpartyId());
            partyMember.ccpartyId = cCPartyMc.getId();
            tree.setId(partyMember.getId());
            tree.setName(userManager.getUserFromMc(partyMember.getId()).getName());
            tree.setIcon(ZTreeView.CCPARTY_USER_TREE_ICON);
            treeList.add(tree);
        }
        return treeList;
    }

    /**
     * 
     * <B>方法名称：</B>转出党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月23日
     * @param loginUser
     * @param id
     * @return
     */
    public Map<String, Object> fakeDeletePartymember(UserMc loginUser, String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        PartyMember member = partyMemberManager.getPartyMember(id);
        if (member == null) {
            ret.put("success", false);
            ret.put("msg", "删除失败。");
            return ret;
        }
        member.setStatus(PartyMember.STATUS_1);
        member.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        member.setUpdateUserId(loginUser.getId());
        partyMemberManager.saveOrUpdate(member);
        User user = userManager.getUser(member.getId());
        if (user != null) {
            user.setStatus(User.STATUS_1);
            userManager.updateUser(user);
        }
        ret.put("success", true);
        ret.put("msg", "已成功删除。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>恢复党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月23日
     * @param loginUser
     * @param id
     * @return
     */
    public Map<String, Object> recoverPartyMember(UserMc loginUser, String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        PartyMember member = partyMemberManager.getPartyMember(id);
        if (member == null) {
            ret.put("success", false);
            ret.put("msg", "恢复失败。");
            return ret;
        }
        member.setStatus(PartyMember.STATUS_0);
        member.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        member.setUpdateUserId(loginUser.getId());
        partyMemberManager.saveOrUpdate(member);
        User user = userManager.getUser(member.getId());
        if (user != null) {
            user.setStatus(User.STATUS_0);
            userManager.updateUser(user);
        }
        ret.put("success", true);
        ret.put("msg", "已成功恢复。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>删除党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月23日
     * @param loginUser
     * @param id
     * @return
     */
    public Map<String, Object> realDeletePartymember(UserMc loginUser, String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        PartyMember member = partyMemberManager.getPartyMember(id);
        if (member == null) {
            ret.put("success", false);
            ret.put("msg", "删除失败，党员信息不存在。");
            return ret;
        }
        //删除党员
        partyMemberManager.delete(member.getId(), ObjectType.OBT_PARTY_MEMBER);
        //删除用户
        User user = userManager.getUser(id);
        if (user != null) {
            userManager.delete(user.getId(), ObjectType.UAM_USER);
        }
        ret.put("success", true);
        ret.put("msg", "已成功删除。");
        return ret;
    }

    public boolean editPartyMember(UserMc loadUser, String id, String name, String partyNo, int gender, String idNumber, String nation, String occupation, String education, String jobTitle,
            String birthPlace, String officePhone, String mobile, String email, String address, String ccpartyId, String joinTime, String type, String introducer, String picture, String status,
            String description, String joinType, String joinCurrentTime, String joinActivity, String fee, String isDelegate, String isDestitution) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PartyMember p = partyMemberManager.getPartyMember(id);
        p.setName(name);
        p.setPartyNo(partyNo);

        p.setCcpartyId(ccpartyId);
        if (joinTime != null && !"".equals(joinTime)) {
            Date date = sdf.parse(joinTime);
            p.setJoinTime(date);
        }
        p.setType(Integer.valueOf(type));
        p.setIntroducer(introducer);

        p.setStatus(Integer.valueOf(status));
        p.setDescription(description);

        p.setUpdateUserId(loadUser.getId());
        p.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        p.setJoinType(Integer.parseInt(joinType));
        if (joinCurrentTime != null && !"".equals(joinCurrentTime)) {
            Date date = sdf.parse(joinCurrentTime);
            p.setJoinCurrentTime(date);
        }

        p.setJoinActivity(Integer.parseInt(joinActivity));
        p.setFee(Double.parseDouble(fee));
        p.setIsDelegate(Integer.parseInt(isDelegate));
        p.setIsDestitution(Integer.parseInt(isDestitution));
        partyMemberManager.saveOrUpdate(p);

        User user = userManager.getUserByIdNumber(p.getId());
        user.setGender(gender);
        user.setNation(nation);
        user.setOccupation(occupation);
        user.setEducation(education);
        user.setJobTitle(jobTitle);
        user.setBirthPlace(birthPlace);
        user.setOfficePhone(officePhone);
        user.setMobile(mobile);
        user.setEmail(email);
        user.setAddress(address);
        user.setStatus(Integer.parseInt(status));
        userManager.saveOrUpdate(user);
        return true;

    }

    /**
     * 
     * @Description: 党员保存 用于党建项目使用
     * @param loadUser
     * @param objs
     * @return
     * @throws ParseException
     */
    public boolean saveOrUpdatePartyMemberForDjweb(UserMc loginUser, JSONObject objs) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PartyMember p = partyMemberManager.getPartyMember(objs.getString("id"));
        if (p == null) {
            p = new PartyMember();
            p.setId(objs.getString("id"));
            p.setCreateTime(new Timestamp(System.currentTimeMillis()));
            p.setCreateUserId(loginUser.getId());

        }
        p.setPartyNo(TpriStringUtils.returnStr(objs.getString("partyNo")));
        String ccpartyId = objs.getString("ccpartyId");
        if (ccpartyId != null && !"".equals(ccpartyId)) {
            CCParty ccparty = ccpartyManager.getCCPartyFromMc(p.getCcpartyId());
            p.setCcparty(ccparty);
        }
        p.setType(objs.getInt("type"));
        p.setIsDelegate(new Integer(objs.getString("isDelegate")).intValue());
        p.setIsManager(new Integer(objs.getString("isManager")).intValue());
        p.setIsDestitution(new Integer(objs.getString("isDestitution")).intValue());
        String joinTime = TpriStringUtils.returnStr(objs.getString("joinTime"));
        p.setJoinTime(StringUtils.isEmpty(joinTime) ? null : sdf.parse(joinTime));
        String joinCurrentTime = TpriStringUtils.returnStr(objs.getString("joinCurrentTime"));
        p.setJoinCurrentTime(StringUtils.isEmpty(joinCurrentTime) ? null : sdf.parse(joinCurrentTime));
        p.setJoinType(new Integer(objs.getString("joinType")).intValue());
        p.setIntroducer(TpriStringUtils.returnStr(objs.getString("introducer")));
        String passTime = TpriStringUtils.returnStr(objs.getString("passTime"));
        p.setPassTime(StringUtils.isEmpty(passTime) ? null : sdf.parse(passTime));
        String auditTime = TpriStringUtils.returnStr(objs.getString("auditTime"));
        p.setAuditTime(StringUtils.isEmpty(auditTime) ? null : sdf.parse(auditTime));
        if (!StringUtils.isEmpty(objs.getString("fee"))) {
            p.setFee(Double.parseDouble(objs.getString("fee")));
        } else {
            p.setFee(0);
        }
        p.setJoinActivity(new Integer(objs.getString("joinActivity")).intValue());

        String applyTime = TpriStringUtils.returnStr(objs.getString("applyTime"));
        p.setApplyTime(StringUtils.isEmpty(applyTime) ? null : sdf.parse(applyTime));
        String activistTime = TpriStringUtils.returnStr(objs.getString("activistTime"));
        p.setActivistTime(StringUtils.isEmpty(activistTime) ? null : sdf.parse(applyTime));
        String targetTime = TpriStringUtils.returnStr(objs.getString("targetTime"));
        p.setTargetTime(StringUtils.isEmpty(targetTime) ? null : sdf.parse(applyTime));
        p.setTrainer(TpriStringUtils.returnStr(objs.getString("trainer")));
        p.setCcpartyId(ccpartyId);
        partyMemberManager.saveOrUpdate(p);
        return true;

    }

    public List<PartyMember> loadPartyMemberByPartyId(String ccpartyId) {
        List<PartyMember> partyMembers = new ArrayList<PartyMember>();
        getPartyMember(partyMembers, ccpartyId);
        return partyMembers;
    }

    /**
     * 
     * @Description: 根据身份证号获取党员
     * @param idNumber
     * @return
     */
    public List<User> loadPartyMemberByIdNumber(String idNumber) {
        return partyMemberManager.loadPartyMemberByIdNumber(idNumber);
    }

    private void getPartyMember(List<PartyMember> partyMembers, String ccpartyId) {
        partyMembers.addAll(partyMemberManager.loadPartyMemberByPartyId(ccpartyId));
        List<CCParty> ccpartyList = ccpartyManager.getCCPartyListByParentId(ccpartyId, CCParty.STATUS_0);
        if (ccpartyList != null && ccpartyList.size() > 0) {
            for (CCParty ccparty : ccpartyList) {
                getPartyMember(partyMembers, ccparty.getId());
            }
        }
    }

    /**
     * 
     * @Description: 根据用户ID获取党员信息
     * @param userId
     * @return
     */
    public PartyMember getPartyMemberByUserId(String userId) {
        PartyMember partyMember = partyMemberManager.getPartyMemberByUserId(userId);
        partyMember.setUser(userManager.getUserFromMc(userId));
        partyMember.setCcparty(ccpartyManager.getCCPartyFromMc(partyMember.getCcpartyId()));
        return partyMember;
    }

    /**
     * 
     * @Description: 对用户和党员信息统一保存处理
     * @param loginUser
     * @param paramters 参数集合
     * @return
     * @throws ParseException
     */
    public Map<String, Object> saveOrUpdatePartymemberAndUser(UserMc loginUser, String paramters, String ccpartyId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject jodata = JSONObject.fromObject(paramters);
        JSONArray ja = JSONArray.fromObject(jodata.get("data"));
        JSONObject userParamters = ja.getJSONObject(0); //用户参数集合
        JSONObject partymemberParamters = ja.getJSONObject(1); //党员参数集合
        if (partymemberParamters.getString("ccpartyId") == null) {
            partymemberParamters.put("ccpartyId", ccpartyId);
        }
        String id = "";
        if (!StringUtils.isEmpty(userParamters.getString("id"))) {
            id = userParamters.getString("id");
        } else {
            //组合ID
            id = UUIDUtil.id();
        }
        //step1、党员信息处理
        PartyMember partyMember = this.savePartyMemberInfos(loginUser, partymemberParamters, userParamters,id);
        //step2、用户信息处理
        User user = userService.saveUserInfos(loginUser, userParamters, partyMember,id);

        ret.put("success", true);
        ret.put("msg", "信息保存成功。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>保存党员信息并返回党员对象<BR>
     * <B>概要说明：</B>党员管理模块 保存党员<BR>
     * 
     * @author 赵子靖
     * @since 2015年12月7日
     * @param loginUser
     * @param objs
     * @param userParamters
     * @return
     * @throws ParseException
     */
    public PartyMember savePartyMemberInfos(UserMc loginUser, JSONObject objs, JSONObject userParamters, String id) {
        PartyMember partyMember = partyMemberManager.getPartyMember(id);
        if (partyMember == null) {
            partyMember = new PartyMember();
            partyMember.setId(id);
            partyMember.setCreateTime(new Timestamp(System.currentTimeMillis()));
            partyMember.setCreateUserId(loginUser.getId());
        } else {
            partyMember.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            partyMember.setUpdateUserId(loginUser.getId());
        }
        partyMember.setCcpartyId(objs.getString("ccpartyId"));
        if (User.TYPE_01.equals(userParamters.getString("type"))) {
            partyMember.setType(PartyMember.TYPE_4); //正式党员
        } else if (User.TYPE_02.equals(userParamters.getString("type"))) {
            partyMember.setType(PartyMember.TYPE_3); //预备党员
        }
        partyMember.setJoinTime(StringUtils.isEmpty(objs.getString("joinTime")) ? null : DateUtil.str2Date(objs.getString("joinTime"), DateUtil.DEFAULT_FORMAT));
        partyMember.setJoinCurrentTime(StringUtils.isEmpty(objs.getString("joinCurrentTime")) ? null : DateUtil.str2Date(objs.getString("joinCurrentTime"), DateUtil.DEFAULT_FORMAT));
        partyMember.setJoinType(objs.getInt("joinType"));
        partyMember.setPassTime(StringUtils.isEmpty("passTime") ? null : DateUtil.str2Date(objs.getString("passTime"), DateUtil.DEFAULT_FORMAT));
        partyMember.setAuditTime(StringUtils.isEmpty("auditTime") ? null : DateUtil.str2Date(objs.getString("auditTime"), DateUtil.DEFAULT_FORMAT));
        //partyMember.setFee(StringUtils.isEmpty(objs.getString("fee")) ? 0 : Double.parseDouble(objs.getString("fee")));
        //partyMember.setJoinActivity(objs.getInt("joinActivity"));
        partyMember.setApplyTime(StringUtils.isEmpty(objs.getString("applyTime")) ? null : DateUtil.str2Date(objs.getString("applyTime"), DateUtil.DEFAULT_FORMAT));
        partyMember.setActivistTime(StringUtils.isEmpty(objs.getString("activistTime")) ? null : DateUtil.str2Date(objs.getString("activistTime"), DateUtil.DEFAULT_FORMAT));
        partyMember.setTargetTime(StringUtils.isEmpty(objs.getString("targetTime")) ? null : DateUtil.str2Date(objs.getString("targetTime"), DateUtil.DEFAULT_FORMAT));
        partyMember.setTrainer(objs.getString("trainer"));
        partyMember.setIntroducer(objs.getString("introducer"));
        partyMemberManager.saveOrUpdate(partyMember);
        return partyMember;

    }

    /**
     * 
     * <B>方法名称：</B>获取党组织下的党员总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年10月13日
     * @param ccpartyId
     * @return
     */
    public int getPartymemberNums(String ccpartyId) {
        //获取所有子节点
        List<ZTreeView> trees = ccpartyService.getTreeCCPartyAndLowerLevel(ccpartyId);
        List<String> statisticsCcpartyIds = new ArrayList<String>();
        if (trees != null && trees.size() > 0) {
            for (ZTreeView tree : trees) {
                statisticsCcpartyIds.add(tree.getId());
            }
        }
        return partyMemberManager.getPartymemberNums(ccpartyId, statisticsCcpartyIds);
    }

    /**
     * 
     * <B>方法名称：</B>保存党员信息<BR>
     * <B>概要说明：</B>个人资料<BR>
     * 
     * @author 赵子靖
     * @since 2016年5月30日
     * @param objs
     * @return
     */
    public Map<String, Object> updatePartymemberInfoForMyInfo(JSONObject objs) {
        Map<String, Object> ret = new HashMap<String, Object>();
        User user = userManager.getUser(objs.getString("userId"));
        if (user == null || user.getPartyMember() == null) {
            ret.put("success", false);
            ret.put("msg", "保存失败。");
            return ret;
        }
        user.getPartyMember().setJoinCurrentTime(StringUtils.isEmpty(objs.getString("joinCurrentTime")) ? null : DateUtil.str2Date(objs.getString("joinCurrentTime"), DateUtil.DEFAULT_FORMAT));
        user.getPartyMember().setJoinType(objs.getInt("joinType"));
        user.getPartyMember().setActivistTime(StringUtils.isEmpty(objs.getString("activistTime")) ? null : DateUtil.str2Date(objs.getString("activistTime"), DateUtil.DEFAULT_FORMAT));
        user.getPartyMember().setApplyTime(StringUtils.isEmpty(objs.getString("applyTime")) ? null : DateUtil.str2Date(objs.getString("applyTime"), DateUtil.DEFAULT_FORMAT));
        user.getPartyMember().setTargetTime(StringUtils.isEmpty(objs.getString("targetTime")) ? null : DateUtil.str2Date(objs.getString("targetTime"), DateUtil.DEFAULT_FORMAT));
        user.getPartyMember().setTrainer(objs.getString("trainer"));
        user.getPartyMember().setPassTime(StringUtils.isEmpty(objs.getString("passTime")) ? null : DateUtil.str2Date(objs.getString("passTime"), DateUtil.DEFAULT_FORMAT));
        user.getPartyMember().setAuditTime(StringUtils.isEmpty(objs.getString("auditTime")) ? null : DateUtil.str2Date(objs.getString("auditTime"), DateUtil.DEFAULT_FORMAT));
        user.getPartyMember().setJoinTime(StringUtils.isEmpty(objs.getString("joinTime")) ? null : DateUtil.str2Date(objs.getString("joinTime"), DateUtil.DEFAULT_FORMAT));
        user.getPartyMember().setFee(StringUtils.isEmpty(objs.getString("fee")) ? 0 : Double.parseDouble(objs.getString("fee")));
        user.getPartyMember().setIntroducer(objs.getString("introducer"));
        user.getPartyMember().setJoinActivity(objs.getInt("joinActivity"));
        partyMemberManager.update(user.getPartyMember());
        ret.put("success", true);
        ret.put("msg", "已成功保存。");
        return ret;
    }

    /**
     * <B>方法名称：</B>获取某组织及其下属组织的所有成员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月13日
     * @param search
     * @param offset
     * @param limit
     * @param ccpartyId
     * @param searchCcpartyId
     * @return
     */
    public List<User> getAllPartyMembersByCcpartyId(String search, Integer offset, Integer limit, String ccpartyId, String searchCcpartyId) {
        //所有某党组织的所有下级组织（自己、儿子和孙子组织）
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (StringUtils.isEmpty(searchCcpartyId)) {
            List<ZTreeView> trees = ccpartyService.getTreeCCPartyAndLowerLevel(ccpartyId);
            if (trees != null && trees.size() > 0) {
                for (ZTreeView tree : trees) {
                    ccpartyIds.add(tree.getId());
                }
            }
        } else {
            String[] ids = searchCcpartyId.split(",");
            for (int i = 0; i < ids.length; i++) {
                ccpartyIds.add(ids[i]);
            }
        }
        List<User> users = partyMemberManager.getAllPartyMembersByCcpartyId(search, offset, limit, ccpartyIds);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (user.getPartyMember() != null) {
                    user.getPartyMember().setCcparty(ccpartyManager.getCCPartyFromMc(user.getPartyMember().getCcpartyId()));
                }
            }
        }
        return users;
    }

    /**
     * <B>方法名称：</B>获取某组织及其下属组织的所有成员总数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年6月13日
     * @param search
     * @param ccpartyId
     * @param searchCcpartyId
     * @return
     */
    public Integer getAllPartyMembersByCcpartyIdTotal(String search, String ccpartyId, String searchCcpartyId) {
        //所有某党组织的所有下级组织（自己、儿子和孙子组织）
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (StringUtils.isEmpty(searchCcpartyId)) {
            List<ZTreeView> trees = ccpartyService.getTreeCCPartyAndLowerLevel(ccpartyId);
            if (trees != null && trees.size() > 0) {
                for (ZTreeView tree : trees) {
                    ccpartyIds.add(tree.getId());
                }
            }
        } else {
            String[] ids = searchCcpartyId.split(",");
            for (int i = 0; i < ids.length; i++) {
                ccpartyIds.add(ids[i]);
            }
        }
        return partyMemberManager.getAllPartyMembersByCcpartyIdTotal(search, ccpartyIds);
    }

    /**
     * <B>方法名称：</B>加载某个阶段的发展党员<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月30日
     * @param ccpartyId
     * @param type
     * @param developmentId
     * @param start
     * @param limit
     * @return
     */
    public List<PartyMember> getPartyMemberListByType(String ccpartyId, Integer type, String developmentId, Integer start, Integer limit) {
        List<PartyMember> partyMemberlist = partyMemberManager.getPartyMemberListByType(ccpartyId, type, developmentId, start, limit);
        for (PartyMember partyMember : partyMemberlist) {
            CCParty cCPartyMc = ccpartyManager.getCCPartyFromMc(partyMember.getCcpartyId());
            partyMember.setCcparty(cCPartyMc);
            partyMember.setUser(userManager.getUserFromMc(partyMember.getId()));
            partyMember.setProcess(developmentProcedureManager.getDevelopmentProcedureById(partyMember.getDevelopmentId()));
        }
        return partyMemberlist;
    }

    /**
     * <B>方法名称：</B>加载某个阶段的发展党员数量<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2015年11月30日
     * @param ccpartyId
     * @param type
     * @param developmentId
     * @return
     */
    public Integer getPartyMemberTotalByType(String ccpartyId, Integer type, String developmentId) {
        return partyMemberManager.getPartyMemberTotalByType(ccpartyId, type, developmentId);
    }

    /**
     * 
     * <B>方法名称：</B>处理从党员发展计划保存党员和用户<BR>
     * <B>概要说明：</B>发展党员模块 保存申请人、积极分子、发展对象、预备党员、正式党员统一入口<BR>
     * 
     * @author 赵子靖
     * @since 2015年12月4日
     * @param loginUser
     * @param paramters
     * @return
     */
    public Map<String, Object> saveOrUpdatePartymemberForDevelopmentPlan(UserMc loginUser, String paramters) {
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject jodata = JSONObject.fromObject(paramters);
        JSONArray ja = JSONArray.fromObject(jodata.get("data"));
        JSONObject objs = ja.getJSONObject(0);

        boolean isAddUserOperator = false; //是否新增用户操作 
        boolean isAddPartymemberOperator = false;//是否新增党员操作

        User user = userManager.getUser(objs.getString("id"));
        if (user == null) {
            isAddUserOperator = true;
            user = new User();
            user.setId(UUIDUtil.id());
            user.setLoginNo(objs.getString("loginNo"));
            user.setSystemNo(BaseConfig.SYSTEM_NO);
            //设置密码
            user.setPassword(MD5Util.md5(User.PASSWORD_DEFAULT));
            user.setCreateTime(new Timestamp(System.currentTimeMillis()));
            user.setCreateUserId(loginUser.getId());
            //增加党员权限
            user.setRoles(new HashSet<Role>());
            user.getRoles().add(roleManager.getRoleById(Role.ROLE_PARTYMEMBER_DEFAULT));
        }
        PartyMember partyMember = partyMemberManager.getPartyMember(user.getId());
        if (partyMember == null) {
            isAddPartymemberOperator = true;
            partyMember = new PartyMember();
            partyMember.setId(user.getId());
            partyMember.setCreateTime(new Timestamp(System.currentTimeMillis()));
            partyMember.setCreateUserId(loginUser.getId());
            partyMember.setCcpartyId(objs.getString("ccparty"));
            partyMemberManager.add(partyMember);
        }
        //公共处理程序
        user.setName(objs.getString("name"));
        user.setNamePhoneticize(PinYinUtil.getEname(user.getName()));
        user.setNameFirstCharacter(PinYinUtil.getFirstEname(user.getName()));
        user.setGender(objs.getInt("gender"));
        user.setBirthDay(StringUtils.isEmpty(objs.getString("birthDay")) ? null : DateUtil.str2Date(objs.getString("birthDay"), DateUtil.DEFAULT_FORMAT));
        user.setNation(objs.getString("userNationId"));
        user.setBirthPlace(objs.getString("userBirthPlaceId"));
        user.setMobile(objs.getString("mobile"));
        user.setIdNumber(objs.getString("idNumber"));
        user.setAddress(objs.getString("address"));
        user.setEducation(objs.getString("educationId"));
        user.setDegree(objs.getString("degreeId"));
        partyMember.setType(objs.getInt("type"));

        //查找出每个阶段的第一个可用阶段
        List<Object> procedures = developmentProcedureManager.getDevelopmentProceduresByPhaseCode(objs.getString("phaseCode"), loginUser.getCcpartyId());
        if (procedures != null && procedures.size() > 0) {
            partyMember.setDevelopmentId(String.valueOf(procedures.get(0))); //不可跳过的第一个发展阶段
        }

        //特殊处理
        if (PartyMember.TYPE_0 == objs.getInt("type")) {
            //申请人
            user.setType(User.TYPE_13);
            partyMember.setApplyTime(StringUtils.isEmpty(objs.getString("applyTime")) ? null : DateUtil.str2Date(objs.getString("applyTime"), DateUtil.DEFAULT_FORMAT));

        } else if (PartyMember.TYPE_1 == objs.getInt("type")) {
            //积极分子
            user.setType(User.TYPE_13);
            partyMember.setApplyTime(StringUtils.isEmpty(objs.getString("applyTime")) ? null : DateUtil.str2Date(objs.getString("applyTime"), DateUtil.DEFAULT_FORMAT));
            partyMember.setActivistTime(StringUtils.isEmpty(objs.getString("activistTime")) ? null : DateUtil.str2Date(objs.getString("activistTime"), DateUtil.DEFAULT_FORMAT)); //列为积极分子日期
            partyMember.setTrainer(objs.getString("trainer"));//培养联系人
        } else if (PartyMember.TYPE_2 == objs.getInt("type")) {
            //发展对象
            user.setType(User.TYPE_13);
            partyMember.setApplyTime(StringUtils.isEmpty(objs.getString("applyTime")) ? null : DateUtil.str2Date(objs.getString("applyTime"), DateUtil.DEFAULT_FORMAT));
            partyMember.setActivistTime(StringUtils.isEmpty(objs.getString("activistTime")) ? null : DateUtil.str2Date(objs.getString("activistTime"), DateUtil.DEFAULT_FORMAT)); //列为积极分子日期
            partyMember.setTrainer(objs.getString("trainer"));//培养联系人
            partyMember.setTargetTime(StringUtils.isEmpty("targetTime") ? null : DateUtil.str2Date(objs.getString("targetTime"), DateUtil.DEFAULT_FORMAT));//列为发展对象日期
        } else if (PartyMember.TYPE_3 == objs.getInt("type")) {
            //预备党员
            user.setType(User.TYPE_02);
            partyMember.setIsDelegate(objs.getInt("isDelegate"));
            partyMember.setJoinActivity(objs.getInt("joinActivity"));
            partyMember.setIntroducer(objs.getString("introducer"));
            partyMember.setPassTime(StringUtils.isEmpty(objs.getString("passTime")) ? null : DateUtil.str2Date(objs.getString("passTime"), DateUtil.DEFAULT_FORMAT));
            partyMember.setAuditTime(StringUtils.isEmpty(objs.getString("auditTime")) ? null : DateUtil.str2Date(objs.getString("auditTime"), DateUtil.DEFAULT_FORMAT));
            partyMember.setFee(StringUtils.isEmpty(objs.getString("fee")) ? 0 : Double.parseDouble(objs.getString("fee")));
            partyMember.setJoinCurrentTime(StringUtils.isEmpty(objs.getString("joinCurrentTime")) ? null : DateUtil.str2Date(objs.getString("joinCurrentTime"), DateUtil.DEFAULT_FORMAT));
            partyMember.setJoinType(objs.getInt("joinType"));

        } else if (PartyMember.TYPE_4 == objs.getInt("type")) {
            //正式党员
            user.setType(User.TYPE_01);
            partyMember.setIsDelegate(objs.getInt("isDelegate"));
            partyMember.setJoinActivity(objs.getInt("joinActivity"));
            partyMember.setFee(StringUtils.isEmpty(objs.getString("fee")) ? 0 : Double.parseDouble(objs.getString("fee")));
            partyMember.setJoinCurrentTime(StringUtils.isEmpty(objs.getString("joinCurrentTime")) ? null : DateUtil.str2Date(objs.getString("joinCurrentTime"), DateUtil.DEFAULT_FORMAT));
            partyMember.setJoinType(objs.getInt("joinType"));
        }
        user.setPartyMember(partyMember);
        partyMemberManager.saveOrUpdate(partyMember);
        if (isAddPartymemberOperator) {
            userManager.addUser(user);
        } else {
            userManager.updateUser(user);
        }
        ret.put("success", true);
        ret.put("msg", "已成功保存。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取某发展阶段下的党员信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param partymemberId
     * @param processId
     * @return
     */
    public Map<String, Object> getPartymemberDevelopmentProcedureInfo(String partymemberId, String processId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        PartyMember member = partyMemberManager.getPartyMember(partymemberId);
        member.setUser(userManager.getUserFromMc(member.getId()));
        member.setProcess(developmentProcedureManager.getDevelopmentProcedureById(processId));
        member.setDevelopmentInfo(partyMemberDevelopmentInfoManager.getPartymemberDevelopmentInfoByPartymember(partymemberId));
        ret.put("row", member);
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>党员发展详细流程保存<BR>
     * <B>概要说明：</B>每个阶段的详细步骤保存入口<BR>
     * 
     * @author 赵子靖
     * @since 2015年12月18日
     * @param loginUser
     * @param paramters
     * @return
     */
    public Map<String, Object> saveOrUpdatePartymemberDevelopmentStep(UserMc loginUser, String paramters) {
        Map<String, Object> ret = new HashMap<String, Object>();
        JSONObject jodata = JSONObject.fromObject(paramters);
        JSONArray ja = JSONArray.fromObject(jodata.get("data"));
        JSONObject objs = ja.getJSONObject(0);
        PartyMember partymember = partyMemberManager.getPartyMember(objs.getString("id"));
        User user = userManager.getUser(objs.getString("id"));
        boolean saveNextProcess = true;
        if (partymember == null || user == null) {
            ret.put("success", false);
            ret.put("msg", "保存失败，党员不存在。");
            return ret;
        }
        //发展信息
        PartyMemberDevelopmentInfo developmentInfo = partyMemberDevelopmentInfoManager.getPartymemberDevelopmentInfoByPartymember(objs.getString("id"));
        if (developmentInfo == null) {
            developmentInfo = new PartyMemberDevelopmentInfo();
            developmentInfo.setId(UUIDUtil.id());
            developmentInfo.setPartymemberId(partymember.getId());
        }
        if (DevelopmentProcedureEnum.PROCEDURE_0001.getType().equals(objs.getString("processId"))) {
            partymember.setActivistTime(StringUtils.isEmpty(objs.getString("activistTime")) ? null : DateUtil.str2Date(objs.getString("activistTime"), DateUtil.DEFAULT_FORMAT));
            partymember.setTrainer(objs.getString("trainer"));
        } else if (DevelopmentProcedureEnum.PROCEDURE_0101.getType().equals(objs.getString("processId"))) {
            developmentInfo.setOrgEducationInfo02(objs.getString("orgEducationInfo02"));
            developmentInfo.setWorkInfo02(objs.getString("workInfo02"));
        } else if (DevelopmentProcedureEnum.PROCEDURE_0102.getType().equals(objs.getString("processId"))) {
            developmentInfo.setMassesOpinion03(objs.getString("massesOpinion03"));
            developmentInfo.setYouthOpinion03(objs.getString("youthOpinion03"));
        } else if (DevelopmentProcedureEnum.PROCEDURE_0103.getType().equals(objs.getString("processId"))) {
            developmentInfo.setGroupOpinion04(objs.getInt("groupOpinion04"));
            developmentInfo.setDiscussDate04(StringUtils.isEmpty(objs.getString("discussDate04")) ? null : DateUtil.str2Date(objs.getString("discussDate04"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setGroupLeader04(objs.getString("groupLeader04"));
            if (objs.getInt("groupOpinion04") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0104.getType().equals(objs.getString("processId"))) {
            developmentInfo.setBranchOpinion05(objs.getInt("branchOpinion05"));
            developmentInfo.setDiscussDate05(StringUtils.isEmpty(objs.getString("discussDate05")) ? null : DateUtil.str2Date(objs.getString("discussDate05"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setSecretary05(objs.getString("secretary05"));
            if (objs.getInt("branchOpinion05") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0201.getType().equals(objs.getString("processId"))) {
            partymember.setIntroducer(objs.getString("introducer"));
        } else if (DevelopmentProcedureEnum.PROCEDURE_0202.getType().equals(objs.getString("processId"))) {
            developmentInfo.setPoliticalaAudit07(objs.getInt("politicalaAudit07"));
            if (objs.getInt("politicalaAudit07") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0203.getType().equals(objs.getString("processId"))) {
            developmentInfo.setCultivateDate08(StringUtils.isEmpty(objs.getString("cultivateDate08")) ? null : DateUtil.str2Date(objs.getString("cultivateDate08"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setCultivateResult08(objs.getInt("cultivateResult08"));
            if (objs.getInt("cultivateResult08") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0204.getType().equals(objs.getString("processId"))) {
            developmentInfo.setSuperiorAuditOrg09(objs.getString("superiorAuditOrg09"));
            developmentInfo.setAuditDate09(StringUtils.isEmpty(objs.getString("auditDate09")) ? null : DateUtil.str2Date(objs.getString("auditDate09"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setAuditResult09(objs.getInt("auditResult09"));
            if (objs.getInt("auditResult09") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0205.getType().equals(objs.getString("processId"))) {
            developmentInfo.setNoticeInfo10(objs.getString("noticeInfo10"));
        } else if (DevelopmentProcedureEnum.PROCEDURE_0206.getType().equals(objs.getString("processId"))) {
            developmentInfo.setBranchDiscussDate11(StringUtils.isEmpty(objs.getString("branchDiscussDate11")) ? null
                    : DateUtil.str2Date(objs.getString("branchDiscussDate11"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setBranchDiscussResult11(objs.getInt("branchDiscussResult11"));
            developmentInfo.setJoinCcpartyType11(objs.getInt("joinCcpartyType11"));
            if (objs.getInt("branchDiscussResult11") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0207.getType().equals(objs.getString("processId"))) {
            developmentInfo.setTalkDate12(StringUtils.isEmpty(objs.getString("talkDate12")) ? null : DateUtil.str2Date(objs.getString("talkDate12"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setTalker12(objs.getString("talker12"));
        } else if (DevelopmentProcedureEnum.PROCEDURE_0208.getType().equals(objs.getString("processId"))) {
            developmentInfo.setAuditDate13(StringUtils.isEmpty(objs.getString("auditDate13")) ? null : DateUtil.str2Date(objs.getString("auditDate13"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setAuditResult13(objs.getInt("auditResult13"));
            developmentInfo.setIsSpecialPartymember13(objs.getInt("isSpecialPartymember13"));
            developmentInfo.setIsSpecialMaxPrivilege13(objs.getInt("isSpecialMaxPrivilege13"));
            developmentInfo.setSuperiorAuditOrg13(objs.getString("superiorAuditOrg13"));
            if (objs.getInt("auditResult13") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0301.getType().equals(objs.getString("processId"))) {
            user.setType(User.TYPE_02);
            developmentInfo.setWorkInfo14(objs.getString("workInfo14"));
        } else if (DevelopmentProcedureEnum.PROCEDURE_0302.getType().equals(objs.getString("processId"))) {
            user.setType(User.TYPE_02);
            developmentInfo.setDiscussDate15(StringUtils.isEmpty(objs.getString("discussDate15")) ? null : DateUtil.str2Date(objs.getString("discussDate15"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setGroupOpinion15(objs.getInt("groupOpinion15"));
            developmentInfo.setGroupLeader15(objs.getString("groupLeader15"));
            if (objs.getInt("groupOpinion15") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0303.getType().equals(objs.getString("processId"))) {
            user.setType(User.TYPE_02);
            developmentInfo.setMassesOpinion16(objs.getString("massesOpinion16"));
        } else if (DevelopmentProcedureEnum.PROCEDURE_0304.getType().equals(objs.getString("processId"))) {
            user.setType(User.TYPE_02);
            developmentInfo.setDiscussDate17(StringUtils.isEmpty(objs.getString("discussDate17")) ? null : DateUtil.str2Date(objs.getString("discussDate17"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setBranchOpinion17(objs.getInt("branchOpinion17"));
            developmentInfo.setSecretary17(objs.getString("secretary17"));
            if (objs.getInt("branchOpinion17") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0305.getType().equals(objs.getString("processId"))) {
            user.setType(User.TYPE_02);
            developmentInfo.setOfficialDate18(StringUtils.isEmpty(objs.getString("officialDate18")) ? null : DateUtil.str2Date(objs.getString("officialDate18"), DateUtil.DEFAULT_FORMAT));
            developmentInfo.setBranchMeetingResult18(objs.getInt("branchMeetingResult18"));
            developmentInfo.setOfficialInfo18(objs.getInt("officialInfo18"));
            if (objs.getInt("branchMeetingResult18") != 0) {
                saveNextProcess = false;
            }
        } else if (DevelopmentProcedureEnum.PROCEDURE_0306.getType().equals(objs.getString("processId"))) {
            user.setType(User.TYPE_01);
            developmentInfo.setSuperiorAuditOrg19(objs.getString("superiorAuditOrg19"));
            developmentInfo.setAuditResult19(objs.getInt("auditResult19"));
            if (objs.getInt("auditResult19") != 0) {
                saveNextProcess = false;
            }
        }

        if (partymember.getDevelopmentId().equals(objs.getString("processId")) && saveNextProcess) {
            Object object = developmentProcedureManager.getNextDevelopmentProcedures(partymember.getDevelopmentId(), loginUser.getCcpartyId());
            if (object == null) {
                ret.put("success", false);
                ret.put("msg", "保存失败，没有可用的下一阶段。");
                return ret;
            }
            DevelopmentProcedure procedure = developmentProcedureManager.getDevelopmentProcedureById(object.toString());
            partymember.setDevelopmentId(procedure.getId());//设置下一阶段
            partymember.setType(procedure.getPhaseCode());
            ret.put("nextProcessId", object.toString());
        } else {
            ret.put("nextProcessId", objs.getString("processId"));
        }

        partyMemberDevelopmentInfoManager.saveOrUpdate(developmentInfo);
        partyMemberManager.saveOrUpdate(partymember);
        ret.put("success", true);
        ret.put("msg", "已成功保存。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取党员名册<BR>
     * <B>概要说明：</B>党员手册<BR>
     * 
     * @author 赵子靖
     * @since 2016年7月28日
     * @param ccpartyId
     * @param ret
     */
    public void getPartyMembersByCcpartyForPartyCard(String ccpartyId, ModelAndView ret) {
        List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT);
        List<User> members = userManager.getUserByCcparty(ccpartyId);
        if (members != null && members.size() > 0) {
            int count = 1;
            for (User user : members) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("SEQ", count++);
                map.put("NAME", user.getName());
                map.put("SEX", user.getGender() == 1 ? "男" : "女");
                Code nation = codeManager.getCode("A0121." + user.getNation());
                map.put("NATION", nation != null ? nation.getName() : "");
                Code degree = codeManager.getCode("A0440." + user.getDegree());
                map.put("DEGREE", degree != null ? degree.getName() : "");
                map.put("BIRTHDAY", user.getBirthDay() != null ? sdf.format(user.getBirthDay()) : "");
                if (user.getPartyMember() != null) {
                    map.put("PARTY_TIME", user.getPartyMember().getJoinTime() != null ? sdf.format(user.getPartyMember().getJoinTime()) : "");
                } else {
                    map.put("PARTY_TIME", "");
                }
                map.put("WORK_TIME", "");
                //获取党内职务
                map.put("DUTY", electionMemberTitleService.getCcpartyElectionTitle(user.getId(), ccpartyId));
                map.put("DESC_INFO", "");
                subList.add(map);
            }
        } else {
            for (int i = 1; i <= 13; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("SEQ", i);
                map.put("NAME", "");
                map.put("SEX", "");
                map.put("NATION", "");
                map.put("DEGREE", "");
                map.put("BIRTHDAY", "");
                map.put("PARTY_TIME", "");
                map.put("WORK_TIME", "");
                map.put("DUTY", "");
                map.put("DESC_INFO", "");
                subList.add(map);
            }
        }
        ret.addObject("sub5Data", subList);
    }

    /**
     * 
     * <B>方法名称：</B>党员手册信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月1日
     * @param userId
     * @param ret
     */
    public void setUserByUserIdForMemberCard(String userId, ModelAndView ret) {
        User user = userManager.getUser(userId);
        if (user == null || user.getPartyMember() == null) {
            ret.addObject("ZBMC", "");
            ret.addObject("DYMC", "");
            return;
        }
        CCParty ccparty = ccpartyManager.getCCPartyById(user.getPartyMember().getCcpartyId());
        if (ccparty == null) {
            ret.addObject("ZBMC", "");
            ret.addObject("DYMC", "");
            return;
        }
        ret.addObject("ZBMC", ccparty.getName());
        ret.addObject("DYMC", user.getName());
    }

    public void setBaseInfoForMemberCard(String userId, ModelAndView ret, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT);
        User user = userManager.getUser(userId);
        if (user == null) {
            ret.addObject("SEX", "");
            ret.addObject("NATIONAL", "");
            ret.addObject("NATIVE", "");
            ret.addObject("BIRTHDAY", new Date());
            ret.addObject("PARTY_DATE", new Date());
            ret.addObject("UNIT", "");
            ret.addObject("XZ_DUTY", "");
            ret.addObject("DZBMC", "");
            ret.addObject("PARTY_DUTY", "");
            ret.addObject("DYLX", "");
            ret.addObject("ORG_DATE", new Date());
            return;
        }
        CCParty ccparty = ccpartyManager.getCCPartyById(user.getPartyMember().getCcpartyId());
        if (ccparty == null) {
            return;
        }
        ret.addObject("SEX", user.getGender() == 1 ? "男" : "女");
        Code nation = codeManager.getCode("A0121." + user.getNation());
        ret.addObject("NATIONAL", nation != null ? nation.getName() : "");
        Code birthPlace = codeManager.getCode("A0114." + user.getBirthPlace());
        ret.addObject("NATIVE", birthPlace != null ? birthPlace.getName1() : "");
        ret.addObject("BIRTHDAY", user.getBirthDay() != null ? sdf.format(user.getBirthDay()) : "");
        ret.addObject("PARTY_DATE", user.getPartyMember().getJoinTime() != null ? sdf.format(user.getPartyMember().getJoinTime()) : "");
        ret.addObject("UNIT", "");
        ret.addObject("XZ_DUTY", "");
        ret.addObject("DZBMC", ccparty.getName());
        ret.addObject("PARTY_DUTY", electionMemberTitleService.getCcpartyElectionTitle(userId, ccparty.getId()));
        String typeTitle = "";
        switch (user.getPartyMember().getType()) {
        case PartyMember.TYPE_0:
            typeTitle = "申请人";
            break;
        case PartyMember.TYPE_1:
            typeTitle = "积极分子";
            break;
        case PartyMember.TYPE_2:
            typeTitle = "发展对象";
            break;
        case PartyMember.TYPE_3:
            typeTitle = "预备党员";
            break;
        case PartyMember.TYPE_4:
            typeTitle = "正式党员";
            break;
        default:
            break;
        }
        ret.addObject("DYLX", typeTitle);
        ret.addObject("ORG_DATE", user.getPartyMember().getJoinCurrentTime() != null ? sdf.format(user.getPartyMember().getJoinCurrentTime()) : "");
        //头像显示处理
        InputStream in = null;
        try {
            if (user.getIcon() != null && user.getIcon().length != 0) {
                in = new ByteArrayInputStream(user.getIcon());
            } else {
                ///scfc/images/tpri/male.png
                if (user.getGender() == 2) {
                    //女
                    in = new FileInputStream(FileUtil.getContextPath(request) + "/images/cards/female.png");
                } else {
                    //男
                    in = new FileInputStream(FileUtil.getContextPath(request) + "/images/cards/male.png");
                }
            }
            ret.addObject("imageParameter", in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("下载党员手册用户头像文件未找到。" + e.getMessage());
        }
    }

    /**
     * 
     * <B>方法名称：</B>获取某党员的发展流程信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年8月2日
     * @return
     */
    public Map<String, Object> getPartymemberDevelopmentProcedureByUser(String userId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        PartymemberDevelopmentProcedureView view = new PartymemberDevelopmentProcedureView();
        User user = userManager.getUser(userId);
        if (user == null || user.getPartyMember() == null) {
            ret.put("item", view);
            return ret;
        }
        view.setUserName(user.getName());
        CCParty ccparty = ccpartyManager.getCCPartyFromMc(user.getPartyMember().getCcpartyId());
        if (ccparty == null) {
            ret.put("item", view);
            return ret;
        }
        view.setCcpartyName(ccparty.getName());
        view.setPartyMember(user.getPartyMember());
        view.setInfo(partyMemberDevelopmentInfoManager.getPartymemberDevelopmentInfoByPartymember(userId));
        view.setActivityContents(developmentProcedureCommonContentManager.getProceduresCommonContentByType(userId, DevelopmentProcedureCommonContent.TYPE_1));
        view.setReportContents(developmentProcedureCommonContentManager.getProceduresCommonContentByType(userId, DevelopmentProcedureCommonContent.TYPE_2));
        view.setInspectContents(developmentProcedureCommonContentManager.getProceduresCommonContentByType(userId, DevelopmentProcedureCommonContent.TYPE_3));
        ret.put("item", view);
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>获取已转出或退档的党员集合<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月23日
     * @param offset
     * @param limit
     * @param search
     * @param ccpartyId
     * @return
     */
    public List<User> getExportPartyMembers(Integer offset, Integer limit, String search, String ccpartyId) {
        List<User> users = partyMemberManager.getExportPartyMembers(offset, limit, search, ccpartyId);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                user.getPartyMember().setUser(userManager.getUserFromMc(user.getPartyMember().getUpdateUserId()));
            }
        }
        return users;
    }

    /**
     * 
     * <B>方法名称：</B>获取已转出或者退档的党员数目<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月23日
     * @param search
     * @param ccpartyId
     * @return
     */
    public Integer getExportPartyMembersTotal(String search, String ccpartyId) {
        return partyMemberManager.getExportPartyMembersTotal(search, ccpartyId);
    }
}
