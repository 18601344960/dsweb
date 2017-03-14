/**
 * @description 行政组织导航
 * @author zhaozijing
 * @since 2015-08-17
 */
var AdministrationNavigation = function() {
	var t = {
		mainContainer : '',
		administrationId: Global.organizationId,
		administrationName:'',
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initView();
			t.initEvent();
			AdministrationManager.init("#administration-right");
		},
		initView : function() {
		},
		initEvent : function() {
			
		},
		render : function() {
			$(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/organization/administration-navigation.html'));
	    	t.initAdministrationSubjectTree();	//加载左侧党组织树
	    	$("#administration-right").html(nunjucks.render(Global.appName + '/tpl/manager/organization/administration-manager.html'));
			AdministrationManager.render();
		},
		//行政组织树
		initAdministrationSubjectTree : function() {
			ComTree.initTree({
				divContainer : "#administration-navigation-tree",
				url : 'org/getCurrentOrganizationAndSunsToTreeView',
				async: false,     //false同步 true异步
				p : {
					organizationId : Global.organizationId
				},
				onClick : t.administrationClickCallBack
			});
		},
		administrationClickCallBack:function(e, treeId, treeNode) {
			t.administrationId=treeNode.id;
			t.administrationName=treeNode.name;
			$("#administration-right").html(nunjucks.render(Global.appName + '/tpl/manager/organization/administration-manager.html'));
			AdministrationManager.render();
		}
	}
	return t;
}();