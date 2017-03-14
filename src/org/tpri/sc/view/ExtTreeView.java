package org.tpri.sc.view;

import java.util.List;

/**
 * @description ExtTree树节点视图对象
 * @author 易文俊
 * @since 2015-05-08
 */
public class ExtTreeView {
	private String nodeId;
    private String text;
    private boolean leaf;
    private String icon;
    private String cls;
    private boolean expanded;
    private boolean checked;
    private List<ExtTreeView> children;
    
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public List<ExtTreeView> getChildren() {
		return children;
	}
	public void setChildren(List<ExtTreeView> children) {
		this.children = children;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}

}
