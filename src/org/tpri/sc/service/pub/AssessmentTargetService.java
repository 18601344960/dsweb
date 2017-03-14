package org.tpri.sc.service.pub;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.entity.obt.PartyMember;
import org.tpri.sc.entity.org.CCParty;
import org.tpri.sc.entity.pub.Assessment;
import org.tpri.sc.entity.pub.AssessmentTarget;
import org.tpri.sc.entity.pub.AssessmentUser;
import org.tpri.sc.manager.obt.PartyMemberManager;
import org.tpri.sc.manager.org.CCPartyManager;
import org.tpri.sc.manager.pub.AssessmentManager;
import org.tpri.sc.manager.pub.AssessmentTargetManager;
import org.tpri.sc.manager.pub.AssessmentTopicManager;
import org.tpri.sc.manager.pub.AssessmentTopicOptionManager;
import org.tpri.sc.manager.pub.AssessmentUserManager;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.util.StringUtil;
import org.tpri.sc.util.UUIDUtil;
import org.tpri.sc.view.ZTreeView;

/**
 * 
 * <B>系统名称：党建系统</B><BR>
 * <B>模块名称：问卷测评</B><BR>
 * <B>中文类名：问卷测评通知服务类</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年9月17日
 */
@Service("AssessmentTargetService")
public class AssessmentTargetService {

    public Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private AssessmentTargetManager assessmentTargetManager;
    @Autowired
    private CCPartyManager ccpartyManager;
    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private AssessmentTopicManager assessmentTopicManager;
    @Autowired
    private AssessmentTopicOptionManager assessmentTopicOptionManager;
    @Autowired
    private CCpartyService ccpartyService;
    @Autowired
    private AssessmentUserManager assessmentUserManager;
    @Autowired
    private PartyMemberManager partyMemberManager;

    /**
     * 
     * <B>方法名称：</B>获取答卷的测评对象列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param offset
     * @param limit
     * @param assessmentId
     * @return
     */
    public List<AssessmentTarget> getAssessmentTargetsByAssessment(Integer offset, Integer limit, String assessmentId) {
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessment(offset, limit, assessmentId);
        if (targets != null && targets.size() > 0) {
            for (AssessmentTarget target : targets) {
                target.setCcparty(ccpartyManager.getCCPartyFromMc(target.getCcpartyId()));
            }
        }
        return targets;
    }

    /**
     * 
     * <B>方法名称：</B>获取答卷的测评对象数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param assessmentId
     * @return
     */
    public Integer getAssessmentTargetsTotalByAssessment(String assessmentId) {
        return assessmentTargetManager.getAssessmentTargetsTotalByAssessment(assessmentId);
    }

    /**
     * 
     * <B>方法名称：</B>获取测评对象<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param assessmentId
     * @param userId
     * @return
     */
    public AssessmentTarget getAssessmentTargetByAssessmentAndTarget(String assessmentId, String ccpartyId) {
        return assessmentTargetManager.getAssessmentTargetByAssessmentAndTarget(assessmentId, ccpartyId);
    }

    /**
     * 
     * <B>方法名称：</B>删除<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param id
     * @return
     */
    public Map<String, Object> deleteAssessmentTarget(String id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        AssessmentTarget target = assessmentTargetManager.getAssessmentTargetById(id);
        if (target == null) {
            ret.put("success", false);
            ret.put("msg", "撤销失败，通知对象不存在。");
            return ret;
        }
        Assessment assessment = assessmentManager.getAssessmentById(target.getAssessmentId());
        if (assessment == null) {
            ret.put("success", false);
            ret.put("msg", "撤销失败，答卷不存在。");
            return ret;
        }
        CCParty ccparty = ccpartyManager.getCCPartyFromMc(assessment.getCcpartyId());
        CCParty targetCcparty = ccpartyManager.getCCPartyFromMc(target.getCcpartyId());
        //同系统
        deleteAssessmentTargetByCcpartyRecursion(target.getCcpartyId(), assessment.getId());
        ret.put("success", true);
        ret.put("msg", "撤销成功。");
        return ret;
    }

    /**
     * 
     * <B>方法名称：</B>递归删除参与单位<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年9月24日
     * @param ccparty
     * @param assessmentId
     */
    public void deleteAssessmentTargetByCcpartyRecursion(String ccpartyId, String assessmentId) {
        if (StringUtil.isEmpty(ccpartyId)) {
            return;
        }
        AssessmentTarget target = assessmentTargetManager.getAssessmentTargetByAssessmentAndTarget(assessmentId, ccpartyId);
        if (target != null) {
            assessmentTargetManager.delete(target.getId(), ObjectType.PUB_ASSESSMENT_TARGET);
        }
        List<CCParty> suns = ccpartyManager.getCCPartyListByParentId(ccpartyId, null);
        if (suns != null && suns.size() > 0) {
            for (CCParty sun : suns) {
                deleteAssessmentTargetByCcpartyRecursion(sun.getId(), assessmentId);
            }
        }
    }

    /**
     * 
     * <B>方法名称：</B>递归增加参与单位<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年8月10日
     * @param ccparty
     * @param assessmentId
     */
    public void SaveAssessmentTargetByCcpartyRecursion(CCParty ccparty, String assessmentId) {
        if (ccparty == null) {
            return;
        }
        AssessmentTarget target = new AssessmentTarget();
        target.setId(UUIDUtil.id());
        target.setCcpartyId(ccparty.getId());
        target.setAssessmentId(assessmentId);
        assessmentTargetManager.saveOrUpdate(target);
        List<CCParty> suns = ccpartyManager.getCCPartyListByParentId(ccparty.getId(), CCParty.STATUS_0);
        if (suns != null && suns.size() > 0) {
            for (CCParty sun : suns) {
                SaveAssessmentTargetByCcpartyRecursion(sun, assessmentId);
            }
        }
    }

    /**
     * 
     * <B>方法名称：</B>获取答卷的测评对象列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param offset
     * @param limit
     * @param assessmentId
     * @return
     */
    public List<AssessmentTarget> getAssessmentTargetsByAssessment(String assessmentId) {
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment == null) {
            return null;
        }
        List<CCParty> suns = ccpartyManager.getCCPartyListByParentId(assessment.getCcpartyId(), null);
        List<Object> sunCcpartyIds = new ArrayList<Object>();
        if (suns != null && suns.size() > 0) {
            for (CCParty ccparty : suns) {
                sunCcpartyIds.add(ccparty.getId());
            }
        }
        List<AssessmentTarget> allTargets = assessmentTargetManager.getAssessmentTargetsByAssessment(null, null, assessmentId);
        Map<String, AssessmentTarget> allTargetMaps = new HashMap<String, AssessmentTarget>();
        if (allTargets != null && allTargets.size() > 0) {
            for (AssessmentTarget assessmentTarget : allTargets) {
                allTargetMaps.put(assessmentTarget.getCcpartyId(), assessmentTarget);
            }
        }
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessmentAndCcpartys(assessmentId, sunCcpartyIds);
        if (targets != null && targets.size() > 0) {
            for (AssessmentTarget target : targets) {
                target.setCcparty(ccpartyManager.getCCPartyFromMc(target.getCcpartyId()));
                //计算完成比例
                List<Object> ccpartyIds = new ArrayList<Object>();
                List<ZTreeView> sunCcpartys = ccpartyService.getTreeCCPartyAndLowerLevel(target.getCcpartyId());
                if (sunCcpartys != null && sunCcpartys.size() > 0) {
                    for (ZTreeView ccparty : sunCcpartys) {
                        if (allTargetMaps.get(ccparty.getId()) != null) {
                            ccpartyIds.add(ccparty.getId());
                        }
                    }
                }
                //提交人数
                List<AssessmentUser> assessmentUsers = assessmentUserManager.getAssessmentUsersByAssessmentAndCcpartys(assessmentId, ccpartyIds);
                if (assessmentUsers != null && assessmentUsers.size() > 0) {
                    target.setSubmitNum(assessmentUsers.size());
                }
                //总人数
                List<PartyMember> members = partyMemberManager.getPartyMembersByCcpartys(ccpartyIds);
                if (members != null && members.size() > 0) {
                    target.setMemberNum(members.size());
                }
                //计算比率
                if (target.getMemberNum() != 0) {
                    double ratio = ((double) target.getSubmitNum() / target.getMemberNum()) * 100;
                    DecimalFormat df = new DecimalFormat("######0.00");
                    target.setRatio(Double.parseDouble(df.format(ratio)));
                }
            }
        }
        return targets;
    }

    /**
     * 
     * <B>方法名称：</B>获取参与答卷组织树<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月10日
     * @param assessmentId
     * @return
     */
    public List<ZTreeView> getAssessmentTargetTrees(String assessmentId) {
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment == null) {
            return null;
        }
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessment(null, null, assessmentId);
        if (targets != null && targets.size() > 0) {
            for (AssessmentTarget target : targets) {
                CCParty ccparty = ccpartyManager.getCCPartyFromMc(target.getCcpartyId());
                if (ccparty != null) {
                    ZTreeView tree = new ZTreeView();
                    tree.setId(ccparty.getId());
                    tree.setName(ccparty.getName());
                    tree.setAttr1(ccparty.getFullName());
                    tree.setpId(ccparty.getParentId());
                    if (assessment.getCcpartyId().equals(ccparty.getId())) {
                        tree.setOpen(true);
                    } else {
                        tree.setOpen(false);
                    }
                    ccpartyService.setCcpartyIcon(tree, ccparty);
                    tree.setType(ccparty.getType());
                    trees.add(tree);
                }
            }
        }
        return trees;
    }

    /**
     * 
     * <B>方法名称：</B>获取某组织的完成情况<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月11日
     * @param assessmentId
     * @param ccpartyId
     * @return
     */
    public List<AssessmentTarget> getCcpartyAssessmentTargetsByAssessment(String assessmentId, String ccpartyId) {
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        CCParty ccparty = ccpartyManager.getCCPartyFromMc(ccpartyId);
        if (assessment == null || ccparty == null) {
            return null;
        }
        List<CCParty> suns = ccpartyManager.getCCPartyListByParentId(ccpartyId, null);
        List<Object> sunCcpartyIds = new ArrayList<Object>();
        if (suns != null && suns.size() > 0) {
            for (CCParty sunCcparty : suns) {
                sunCcpartyIds.add(sunCcparty.getId());
            }
        }
        if (CCParty.TYPE_4.equals(ccparty.getType())) {
            sunCcpartyIds.add(ccparty.getId());
        }
        List<AssessmentTarget> allTargets = assessmentTargetManager.getAssessmentTargetsByAssessment(null, null, assessmentId);
        Map<String, AssessmentTarget> allTargetMaps = new HashMap<String, AssessmentTarget>();
        if (allTargets != null && allTargets.size() > 0) {
            for (AssessmentTarget assessmentTarget : allTargets) {
                allTargetMaps.put(assessmentTarget.getCcpartyId(), assessmentTarget);
            }
        }

        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessmentAndCcpartys(assessmentId, sunCcpartyIds);
        if (targets != null && targets.size() > 0) {
            for (AssessmentTarget target : targets) {
                target.setCcparty(ccpartyManager.getCCPartyFromMc(target.getCcpartyId()));
                //计算完成比例
                List<Object> ccpartyIds = new ArrayList<Object>();
                List<ZTreeView> sunCcpartys = ccpartyService.getTreeCCPartyAndLowerLevel(target.getCcpartyId());
                if (sunCcpartys != null && sunCcpartys.size() > 0) {
                    for (ZTreeView sunCcparty : sunCcpartys) {
                        if (allTargetMaps.get(sunCcparty.getId()) != null) {
                            ccpartyIds.add(sunCcparty.getId());
                        }
                    }
                }
                //提交人数
                List<AssessmentUser> assessmentUsers = assessmentUserManager.getAssessmentUsersByAssessmentAndCcpartys(assessmentId, ccpartyIds);
                if (assessmentUsers != null && assessmentUsers.size() > 0) {
                    target.setSubmitNum(assessmentUsers.size());
                }
                //总人数
                List<PartyMember> members = partyMemberManager.getPartyMembersByCcpartys(ccpartyIds);
                if (members != null && members.size() > 0) {
                    target.setMemberNum(members.size());
                }
                //计算比率
                if (target.getMemberNum() != 0) {
                    double ratio = ((double) target.getSubmitNum() / target.getMemberNum()) * 100;
                    DecimalFormat df = new DecimalFormat("######0.00");
                    target.setRatio(Double.parseDouble(df.format(ratio)));
                }
            }
        }
        return targets;
    }
    
    public List<ZTreeView> getCcpartyAssessmentTargetTrees(String assessmentId,String ccpartyId){
        List<ZTreeView> trees = new ArrayList<ZTreeView>();
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if(assessment==null){
            return null;
        }
        
        Map<String, String> ccpartyIdMap = new HashMap<String, String>();
        List<ZTreeView> sunCcpartys = ccpartyService.getTreeCCPartyAndLowerLevel(ccpartyId);
        if(sunCcpartys!=null && sunCcpartys.size()>0){
            for (ZTreeView zTreeView : sunCcpartys) {
                ccpartyIdMap.put(zTreeView.getId(), zTreeView.getId());
            }
        }
        
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessment(null, null, assessmentId);
        if(targets!=null && targets.size()>0){
            for (AssessmentTarget target : targets) {
                CCParty ccparty = ccpartyManager.getCCPartyFromMc(target.getCcpartyId());
                if(ccparty!=null && ccpartyIdMap.get(target.getCcpartyId())!=null){
                    ZTreeView tree = new ZTreeView();
                    tree.setId(ccparty.getId());
                    tree.setName(ccparty.getName());
                    tree.setAttr1(ccparty.getFullName());
                    tree.setpId(ccparty.getParentId());
                    if(ccpartyId.equals(ccparty.getId())){
                        tree.setOpen(true);
                    }else{
                        tree.setOpen(false);
                    }
                    ccpartyService.setCcpartyIcon(tree, ccparty);
                    tree.setType(ccparty.getType());
                    trees.add(tree);
                }
            }
        }
        return trees;
    }
}