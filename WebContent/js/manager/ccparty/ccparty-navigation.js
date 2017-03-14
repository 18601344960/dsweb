/**
 * @description 党组织导航
 * @author zhaozijing
 * @since 2015-08-13
 */
var CcpartyNavigation = function() {
	var t = {
		mainContainer : '',
		ccpartyId:Global.ccpartyId,
		ccpartyName:'',
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initView();
			t.initEvent();
			CcpartyManager.init("#ccparty-right");
		},
		initView : function() {
		},
		initEvent : function() {
			
		},
		render : function() {
			$(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/ccparty/ccparty-navigation.html'));
	    	t.initCcpartySubjectTree();	//加载左侧党组织树
			var zTree = $.fn.zTree.getZTreeObj("ccparty-navigation-tree"), 
            nodes = zTree.getSelectedNodes();
			if (nodes.length != 0) {
                t.ccpartyId = nodes[0].id;
                t.ccpartyName = nodes[0].name;
                if (nodes[0].type == 'Z000104') {
                    //组织树隐藏
                    $("#ccparty-left").hide();
                    $("#ccparty-right").css('width','100%');
                    $("#delete-ccparty-btn").hide();
                }
                CcpartyManager.render();
            }
		},
		//组织树
		initCcpartySubjectTree : function() {
			ComTree.initTree({
				divContainer : "#ccparty-navigation-tree",
				url : 'org/getTreeCCPartyAndLowerLevel',
				async: false,     //false同步 true异步
				selectedFirstNode : true,
				p : {
					ccpartyId : Global.ccpartyId
				},
				onClick : t.ccpartyClickCallBack,
				onRightClick:t.onRightClickCallBack
			});
		},
		ccpartyClickCallBack:function(e, treeId, treeNode) {
			t.ccpartyId=treeNode.id;
			t.ccpartyName=treeNode.name;
			$("#ccparty-right").html(nunjucks.render(Global.appName + '/tpl/manager/ccparty/ccparty-manager.html'));
			CcpartyManager.render();
		}
		
	}
	return t;
}();