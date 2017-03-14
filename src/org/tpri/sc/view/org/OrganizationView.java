
package org.tpri.sc.view.org;

import org.tpri.sc.entity.org.Organization;

/**
 * @description 行政单位界面视图
 * @author 常华荣
 * @since 2015-04-24
 */
public class OrganizationView extends Organization {
	
	private static final long serialVersionUID = 1L;
	protected String typeName;
	protected String orgName;
	protected String StatusName;
	protected boolean leaf;
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getStatusName() {
		return StatusName;
	}
	public void setStatusName(String statusName) {
		StatusName = statusName;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
}
