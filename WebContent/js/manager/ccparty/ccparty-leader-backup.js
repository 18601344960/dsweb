/**
 * @description 委员候补设置
 * @author zhaozijing
 * @since 2015-08-14
 */
var CcpartyLeaderBackup = function() {
	var t = {
		mainContainer : '',
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initView();
			t.initEvent();
		},
		initView : function() {
		},
		initEvent : function() {
			//新增
			$(document).on("click", "#add-ccparty-leader-backup", function() {
				t.addCcpartyLeaderBackup();
			});
		},
		render : function() {
			$("#leaders-backup-table").bootstrapTable({
				queryParams : function(params) {
					$.extend(params, {
						ccpartyId:CcpartyNavigation.ccpartyId,
						type:'1'
					})
					return params;
				}
			});
		},
		//新增
		addCcpartyLeaderBackup:function(){
			var data = new Object();
			data.ccpartyName=CcpartyNavigation.ccpartyName;
			data.dialog="leader-dialog";
			data.type="1";
			$("#dialogs").html(nunjucks.render(Global.appName + '/tpl/organization/dialogs/ccparty-dialog.html', data));
			//职位加载
			CcpartyLeader.loadTitleCode();
			$("#leader-dialog").modal({});
		}
	}
	return t;
}();