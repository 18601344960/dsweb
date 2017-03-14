package org.tpri.sc.view.org;

import java.util.List;

import org.tpri.sc.entity.obt.ElectionMember;
import org.tpri.sc.entity.sys.Code;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>换届选举职务视图<BR>
 * <B>概要说明：</B><BR>
 * @author 交通运输部规划研究院（赵子靖）
 * @since 2016年7月3日
 */
public class CCPartyTitleView {
    private Code title;
    private List<ElectionMember> electionMembers;

    public Code getTitle() {
        return title;
    }

    public void setTitle(Code title) {
        this.title = title;
    }

    public List<ElectionMember> getElectionMembers() {
        return electionMembers;
    }

    public void setElectionMembers(List<ElectionMember> electionMembers) {
        this.electionMembers = electionMembers;
    }

}
