/**
 * @description 权限管理
 * @author zhaozijing
 * @since 2015-06-12
 */
var ManagerPrivilege = function() {
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

			$(document).on("click", "#add-privilege-process-definition", function() {
						// 新增权限弹出框
						var data = new Object();
						data.title = "新增权限";
						data.status = '0';
						data.system = '0';
						$("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/privilege/dialogs/privilege-dialogs.html', data));
						$("#privilege-process-definition-dialog").modal({});
					});
			$(document).on("click", "#update-privilege-process-definition", function() {
						// 编辑权限弹出框
						var selectedRows = $("#privilege-process-definition-table").bootstrapTable('getSelections');
						if (selectedRows.length != 1) {
							Notify.notice("请选择一条记录编辑");
							return;
						}
						var data = new Object();
						data = selectedRows[0];
						data.title = "编辑权限";
						$("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/privilege-dialogs.html', data));
						$("#privilege-process-definition-dialog").modal({});

					});
			$(document).on("click", "#delete-privilege-process-definition", function() {
				// 删除权限
				var selectedRows = $("#privilege-process-definition-table").bootstrapTable('getSelections');
				if (selectedRows.length < 1) {
					Notify.notice("请选中需要删除的权限");
					return;
				}
				var deletePrivilegeIds = [];
				for (var index in selectedRows) {
					deletePrivilegeIds.push(selectedRows[index]._id);
				}
				Ajax.call({
							url : "uam/deletePrivilege",
							p : {
								deletePrivilegeIds : JSON.stringify(deletePrivilegeIds)
							},
							f : function(data) {
								if (data && data.success == true) {
									Notify.success("删除成功");
									$("#privilege-process-definition-dialog").modal('toggle');
									t.render();
								} else {
									Notify.error("删除失败");
								}
							}
						});
					// $("#dialogs").html(nunjucks.render(Global.appName +
					// '/tpl/manager/dialogs/privilege-dialogs.html',selectedRows[0]));
				});
		},
		render : function() {
			Ajax.call({
				url : "uam/getPrivilegesTableTree",
				p : {
				},
				f : function(data) {
					if (data && data.rows) {
						$(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/privilege/privilege-list.html', data));
						$("#privilege-process-definition-table").bootstrapTable();
						$("#privilege-process-definition-table").treetable({
							expandable : true,
						});
						$('#privilege-process-definition-table').treetable('expandAll'); // 展开全部
					} else {
						Notify.error("获取权限列表失败");
					}
				}
			});
		},
		addOrUpdatePrivilegeSubmit : function() {
			// 新增or保存按钮事件
			var id = $("#privilege_id").val(); // 权限ID
			var name = CheckInputUtils.splitSpaceForStrAround($("#privilege_name").val()); // 权限名称
			var desc = CheckInputUtils.splitSpaceForStrAround($("#privilege_desc").val()); // 权限描述
			if (name == null || name == "") {
				Notify.notice("请输入权限名称");
				return;
			}
			Ajax.call({
						url : 'uam/addOrEditPrivilege',
						p : {
							id : id,
							name : name,
							desc : desc
						},
						f : function(data) {
							if (data.success) {
								Notify.success("保存成功");
								$("#privilege-process-definition-dialog").modal('toggle');
								t.render();
							} else {
								Notify.error("保存失败");
							}
						}
					});
		}

	}
	return t;
}();