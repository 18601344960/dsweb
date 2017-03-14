package org.tpri.sc.service.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tpri.sc.entity.pub.Assessment;
import org.tpri.sc.entity.pub.AssessmentTarget;
import org.tpri.sc.entity.pub.AssessmentUser;
import org.tpri.sc.manager.pub.AssessmentManager;
import org.tpri.sc.manager.pub.AssessmentTargetManager;
import org.tpri.sc.manager.pub.AssessmentUserManager;
import org.tpri.sc.service.org.CCpartyService;
import org.tpri.sc.util.DateUtil;
import org.tpri.sc.view.ZTreeView;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>答卷测评用户服务类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年6月30日
 */
@Service("AssessmentUserService")
public class AssessmentUserService {
    @Autowired
    private AssessmentUserManager assessmentUserManager;
    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private AssessmentTargetManager assessmentTargetManager;
    @Autowired
    private CCpartyService ccpartyService;

    /**
     * 
     * <B>方法名称：</B>获取答卷的测评用户列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param offset
     * @param limit
     * @param assessmentId
     * @return
     */
    public List<AssessmentUser> getAssessmentUsersByAssessment(Integer offset, Integer limit, String assessmentId) {
        return assessmentUserManager.getAssessmentUsersByAssessment(offset, limit, assessmentId);
    }

    /**
     * 
     * <B>方法名称：</B>获取答卷的测评用户数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param assessmentId
     * @return
     */
    public Integer getAssessmentUsersTotalByAssessment(String assessmentId) {
        return assessmentUserManager.getAssessmentUsersTotalByAssessment(assessmentId);
    }

    /**
     * 
     * <B>方法名称：</B>获取测评用户<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年6月30日
     * @param assessmentId
     * @param userId
     * @return
     */
    public AssessmentUser getAssessmentUserByAssessmentAndUser(String assessmentId, String userId) {
        return assessmentUserManager.getAssessmentUserByAssessmentAndUser(assessmentId, userId);
    }

    /**
     * 
     * <B>方法名称：</B>获取人员完成情况列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月10日
     * @param offset
     * @param limit
     * @param search
     * @param assessmentId
     * @param ccpartyIdsStr
     * @return
     */
    public List<AssessmentUser> getAllAssessmentUsersByAssessment(Integer offset, Integer limit, String search, Integer status, String assessmentId, String ccpartyIdsStr) {
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment == null) {
            return null;
        }
        List<AssessmentUser> assessmentUsers = new ArrayList<AssessmentUser>();
        //计算需要查询哪些组织的人员
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (!StringUtils.isEmpty(ccpartyIdsStr)) {
            String[] ccpartyIdArgs = ccpartyIdsStr.split(",");
            if (ccpartyIdArgs != null && ccpartyIdArgs.length > 0) {
                for (int i = 0; i < ccpartyIdArgs.length; i++) {
                    ccpartyIds.add(ccpartyIdArgs[i]);
                }
            }
        }
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessmentAndCcpartys(assessmentId, ccpartyIds);
        if (targets != null && targets.size() > 0) {
            ccpartyIds = new ArrayList<Object>();
            for (AssessmentTarget assessmentTarget : targets) {
                if (!assessment.getCcpartyId().equals(assessmentTarget.getCcpartyId())) {
                    if (!assessment.getCcpartyId().equals(assessmentTarget.getCcpartyId())) {
                        ccpartyIds.add(assessmentTarget.getCcpartyId());
                    }
                }
            }
        }
        //查询人员完成情况
        List<Object> result = new ArrayList<Object>();
        //全体党员
        ccpartyIds.add(assessment.getCcpartyId());
        result = assessmentUserManager.getAllAssessmentUsersByAssessment(offset, limit, search, status, assessmentId, ccpartyIds);
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Object[] objs = (Object[]) result.get(i);
                AssessmentUser assessmentUser = new AssessmentUser();
                assessmentUser.setUserId(String.valueOf(objs[0]));
                assessmentUser.setUserName(String.valueOf(objs[1]));
                assessmentUser.setCcpartyId(String.valueOf(objs[2]));
                assessmentUser.setCcpartyName(String.valueOf(objs[3]));
                assessmentUser.setId(objs[5] != null ? String.valueOf(objs[5]) : "");
                assessmentUser.setSubmitTime(objs[6] != null ? DateUtil.str2Timestamp(String.valueOf(objs[6]), DateUtil.YYYYMMDDHHMMSS_FORMAT) : null);
                assessmentUsers.add(assessmentUser);
            }
        }
        return assessmentUsers;
    }

    /**
     * 
     * <B>方法名称：</B>根据ID获取答卷人员数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年9月12日
     * @param assessmentId
     */
    public Integer getAllAssessmentUserTotalByAssessment(String search, Integer status, String assessmentId, String ccpartyIdsStr) {
        int total = 0;
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment == null) {
            return total;
        }
        //计算需要查询哪些组织的人员
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (!StringUtils.isEmpty(ccpartyIdsStr)) {
            String[] ccpartyIdArgs = ccpartyIdsStr.split(",");
            if (ccpartyIdArgs != null && ccpartyIdArgs.length > 0) {
                for (int i = 0; i < ccpartyIdArgs.length; i++) {
                    ccpartyIds.add(ccpartyIdArgs[i]);
                }
            }
        }
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessmentAndCcpartys(assessmentId, ccpartyIds);
        if (targets != null && targets.size() > 0) {
            ccpartyIds = new ArrayList<Object>();
            for (AssessmentTarget assessmentTarget : targets) {
                if (!assessment.getCcpartyId().equals(assessmentTarget.getCcpartyId())) {
                    ccpartyIds.add(assessmentTarget.getCcpartyId());
                }
            }
        }
        //查询人员完成情况
        //全体党员
        ccpartyIds.add(assessment.getCcpartyId());
        total = assessmentUserManager.getAllAssessmentUserTotalByAssessment(search, status, assessmentId, ccpartyIds);
        return total;
    }

    /**
     * 
     * <B>方法名称：</B>获取当前组织下的人员完成情况<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月11日
     * @param offset
     * @param limit
     * @param search
     * @param status
     * @param currentCcpartyId
     * @param assessmentId
     * @param ccpartyIdsStr
     * @return
     */
    public List<AssessmentUser> getCcpartyAllAssessmentUsersByAssessment(Integer offset, Integer limit, String search, Integer status, String currentCcpartyId, String assessmentId,
            String ccpartyIdsStr) {
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment == null) {
            return null;
        }
        List<AssessmentUser> assessmentUsers = new ArrayList<AssessmentUser>();
        //计算需要查询哪些组织的人员
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (!StringUtils.isEmpty(ccpartyIdsStr)) {
            //组织搜索
            String[] ccpartyIdArgs = ccpartyIdsStr.split(",");
            if (ccpartyIdArgs != null && ccpartyIdArgs.length > 0) {
                for (int i = 0; i < ccpartyIdArgs.length; i++) {
                    ccpartyIds.add(ccpartyIdArgs[i]);
                }
            }
        } else {
            //当前组织搜索
            List<ZTreeView> sunCcpartys = ccpartyService.getTreeCCPartyAndLowerLevel(currentCcpartyId);
            if (sunCcpartys != null && sunCcpartys.size() > 0) {
                for (ZTreeView zTreeView : sunCcpartys) {
                    ccpartyIds.add(zTreeView.getId());
                }
            }
        }
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessmentAndCcpartys(assessmentId, ccpartyIds);
        if (targets != null && targets.size() > 0) {
            ccpartyIds = new ArrayList<Object>();
            for (AssessmentTarget assessmentTarget : targets) {
                if (!assessment.getCcpartyId().equals(assessmentTarget.getCcpartyId())) {
                    if (!assessment.getCcpartyId().equals(assessmentTarget.getCcpartyId())) {
                        ccpartyIds.add(assessmentTarget.getCcpartyId());
                    }
                }
            }
        }
        //查询人员完成情况
        List<Object> result = new ArrayList<Object>();
        //全体党员
        ccpartyIds.add(assessment.getCcpartyId());
        result = assessmentUserManager.getAllAssessmentUsersByAssessment(offset, limit, search, status, assessmentId, ccpartyIds);
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Object[] objs = (Object[]) result.get(i);
                AssessmentUser assessmentUser = new AssessmentUser();
                assessmentUser.setUserId(String.valueOf(objs[0]));
                assessmentUser.setUserName(String.valueOf(objs[1]));
                assessmentUser.setCcpartyId(String.valueOf(objs[2]));
                assessmentUser.setCcpartyName(String.valueOf(objs[3]));
                assessmentUser.setId(objs[5] != null ? String.valueOf(objs[5]) : "");
                assessmentUser.setSubmitTime(objs[6] != null ? DateUtil.str2Timestamp(String.valueOf(objs[6]), DateUtil.YYYYMMDDHHMMSS_FORMAT) : null);
                assessmentUsers.add(assessmentUser);
            }
        }
        return assessmentUsers;
    }

    /**
     * 
     * <B>方法名称：</B>获取当前组织下的人员完成情况数<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author zhaozijing
     * @since 2016年10月11日
     * @param search
     * @param status
     * @param currentCcpartyId
     * @param assessmentId
     * @param ccpartyIdsStr
     * @return
     */
    public Integer getCcpartyAllAssessmentUserTotalByAssessment(String search, Integer status, String currentCcpartyId, String assessmentId, String ccpartyIdsStr) {
        int total = 0;
        Assessment assessment = assessmentManager.getAssessmentById(assessmentId);
        if (assessment == null) {
            return total;
        }
        //计算需要查询哪些组织的人员
        List<Object> ccpartyIds = new ArrayList<Object>();
        if (!StringUtils.isEmpty(ccpartyIdsStr)) {
            String[] ccpartyIdArgs = ccpartyIdsStr.split(",");
            if (ccpartyIdArgs != null && ccpartyIdArgs.length > 0) {
                for (int i = 0; i < ccpartyIdArgs.length; i++) {
                    ccpartyIds.add(ccpartyIdArgs[i]);
                }
            }
        } else {
            //当前组织搜索
            List<ZTreeView> sunCcpartys = ccpartyService.getTreeCCPartyAndLowerLevel(currentCcpartyId);
            if (sunCcpartys != null && sunCcpartys.size() > 0) {
                for (ZTreeView zTreeView : sunCcpartys) {
                    ccpartyIds.add(zTreeView.getId());
                }
            }
        }
        List<AssessmentTarget> targets = assessmentTargetManager.getAssessmentTargetsByAssessmentAndCcpartys(assessmentId, ccpartyIds);
        if (targets != null && targets.size() > 0) {
            ccpartyIds = new ArrayList<Object>();
            for (AssessmentTarget assessmentTarget : targets) {
                if (!assessment.getCcpartyId().equals(assessmentTarget.getCcpartyId())) {
                    ccpartyIds.add(assessmentTarget.getCcpartyId());
                }
            }
        }
        //查询人员完成情况
        //全体党员
        ccpartyIds.add(assessment.getCcpartyId());
        total = assessmentUserManager.getAllAssessmentUserTotalByAssessment(search, status, assessmentId, ccpartyIds);
        return total;
    }
}