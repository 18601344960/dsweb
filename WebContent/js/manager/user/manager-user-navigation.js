/**
 * @description 用户管理-导航
 * @author zhaozijing
 * @since 2015-08-12
 */
var ManagerUserNavigation = function() {
	var t = {
			mainContainer:'',
			organizationId:Global.organizationId,
			organizationName:'',
			departmentId:'',
			init:function(mainContainer){
				t.mainContainer = mainContainer;
				t.initView();
				t.initEvent();
				ManagerUser.init("#user-right");
			},
			initView:function(){
			},
			initEvent:function(){
		    },
		    onBodyDown : function(event) {
			},
		    render : function() {
		    	$(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/user/user-manager.html'));
		    	t.initOrganizationSubjectTree();	//加载组织树
		    	ManagerUser.render();
			},
			//行政组织树
			initOrganizationSubjectTree : function() {	
				ComTree.initTree({
					divContainer : "#organization-tree",
					url : 'org/getCurrentOrganizationAndSunsToTreeView',
					p : {
						organizationId : Global.organizationId
					},
					onClick : t.organizationClickCallBack
				});
			},
			organizationClickCallBack:function(e, treeId, treeNode) {
				t.organizationId=treeNode.id;
				t.organizationName=treeNode.name;
				ManagerUser.render();
			}
	}
	return t;
}();