package org.tpri.sc.manager.obt;

import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.entity.obt.PartyMemberDevelopmentInfo;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>党员发展详情管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2015年12月21日
 */
@Repository("PartyMemberDevelopmentInfoManager")
public class PartyMemberDevelopmentInfoManager extends ManagerBase {
    static {
        ObjectRegister.registerClass(ObjectType.OBT_PARTYMEMBER_DEVELOPMENT_INFO, PartyMemberDevelopmentInfo.class);
    }

    /**
     * 
     * <B>方法名称：</B>获取详情<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月21日
     * @param id
     * @return
     */
    public PartyMemberDevelopmentInfo getPartyMemberDevelopmentInfoById(String id) {
        return (PartyMemberDevelopmentInfo) this.loadOne(ObjectType.OBT_PARTYMEMBER_DEVELOPMENT_INFO, new String[] { "id" }, new Object[] { id });
    }

    /**
     * 
     * <B>方法名称：</B>获取某党员的发展信息<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2015年12月21日
     * @param partymemberId
     * @return
     */
    public PartyMemberDevelopmentInfo getPartymemberDevelopmentInfoByPartymember(String partymemberId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(PartyMemberDevelopmentInfo.class);
        daoPara.addCondition(Condition.EQUAL("partymemberId", partymemberId));
        return (PartyMemberDevelopmentInfo) dao.loadOne(daoPara);
    }
}
