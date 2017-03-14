package org.tpri.sc.manager.obt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.tpri.sc.core.ManagerBase;
import org.tpri.sc.core.ObjectRegister;
import org.tpri.sc.core.ObjectType;
import org.tpri.sc.dao.condition.Condition;
import org.tpri.sc.dao.condition.DaoPara;
import org.tpri.sc.dao.condition.Order;
import org.tpri.sc.entity.obt.ElectionMemberTitle;
import org.tpri.sc.util.DateUtil;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举领导班子成员党内职务管理类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月1日
 */

@Repository("ElectionMemberTitleManager")
public class ElectionMemberTitleManager extends ManagerBase {

    static {
        ObjectRegister.registerClass(ObjectType.OBT_ELECTION_MEMBER_TITLE, ElectionMemberTitle.class);
    }

    public ElectionMemberTitle getMemberTitleById(String id) {
        Object obj = this.loadOne(ObjectType.OBT_ELECTION_MEMBER_TITLE, new String[] { "id" }, new Object[] { id });
        if (obj == null) {
            return null;
        }
        ElectionMemberTitle electionMemberTitle = (ElectionMemberTitle) obj;
        return electionMemberTitle;
    }

    /**
     * <B>方法名称：</B>获取某班子成员的党内职务<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param memberId
     * @return
     */
    public List<ElectionMemberTitle> getMemberTitlesByMemberId(String memberId) {
        DaoPara daoPara = new DaoPara();
        daoPara.setClazz(ElectionMemberTitle.class);
        daoPara.addCondition(Condition.EQUAL("memberId", memberId));
        daoPara.addOrder(Order.asc("sequence"));
        List list = dao.loadList(daoPara);
        return list;
    }

    /**
     * <B>方法名称：</B>删除某领导班子成员下的职务<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param memberId
     * @return
     */
    public boolean deleteElectionMemberByElectionId(String memberId) {
        String hql = "delete from ElectionMemberTitle where memberId=：memberId ";
        Object[] params = new Object[] { memberId };
        dao.delete(hql, params);
        return true;
    }

    /**
     * <B>方法名称：</B>根据ID删除领导班子成员党内职务<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月2日
     * @param id
     * @return
     */
    public boolean deleteElectionMemberTitle(String id) {
        return super.delete(id, ObjectType.OBT_ELECTION_MEMBER_TITLE);
    }
    
    /**
     * 
     * <B>方法名称：</B>获取某用户的有效任职情况<BR>
     * <B>概要说明：</B><BR>
     * 返回的结果格式： 组织ID 组织名称 职务ID 职务名称
     * 
     * @author 赵子靖
     * @since 2016年7月4日
     * @param userId
     * @return
     */
    public List<Object> getUserElectionTitles(String userId,String ccpartyId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT OC.ID CCPARTYID,OC.NAME CCPARTYNAME,SC.ID TITLEID,SC.NAME TITLE \n");
        sql.append(" FROM OBT_ELECTION_MEMBER AS OEM \n");
        sql.append(" LEFT JOIN OBT_ELECTION_MEMBER_TITLE AS OEMT ON OEMT.MEMBER_ID=OEM.ID\n");
        sql.append(" LEFT JOIN OBT_ELECTION AS OE ON OE.ID=OEM.ELECTION_ID\n");
        sql.append(" LEFT JOIN ORG_CCPARTY AS OC ON OE.CCPARTY_ID=OC.ID\n");
        sql.append(" LEFT JOIN SYS_CODE AS SC ON CONCAT('A070101.',OEMT.PARTY_TITLE_ID)=SC.ID\n");
        sql.append(" WHERE OEM.USER_ID=:userId\n");
        sql.append(" ##有效的换届选举\n");
        sql.append(" AND OE.ID IN(\n");
        sql.append(" SELECT E.ID FROM(\n");
        sql.append(" SELECT OE.ID,OE.CCPARTY_ID FROM OBT_ELECTION AS OE \n");
        sql.append(" ORDER BY OE.SEQUENCE DESC\n");
        sql.append(" )AS E\n");
        sql.append(" GROUP BY E.CCPARTY_ID \n");
        sql.append(" )\n");
        sql.append(" AND DATE_FORMAT(OE.END_DATE,'%Y-%M-%D')>=:nowDate\n");
        sql.append(" AND DATE_FORMAT(OEM.END_DATE,'%Y-%M-%D')>=:nowDate\n");
        if(!StringUtils.isEmpty(ccpartyId)){
            sql.append(" AND OC.ID=:ccpartyId \n");
        }
        sql.append(" ORDER BY OC.SEQUENCE,OEMT.SEQUENCE\n");
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_FORMAT);
        Session session = dao.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString()).setString("userId", userId).setString("nowDate", sdf.format(new Date()));
        if(!StringUtils.isEmpty(ccpartyId)){
            query.setString("ccpartyId", ccpartyId);
        }
        return query.list();
    }

}
