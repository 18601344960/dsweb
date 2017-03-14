/**
 * @description 委员设置
 * @author zhaozijing
 * @since 2015-08-14
 */
var CcpartyLeader = function() {
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
			// 新增
			$(document).on("click", "#add-ccparty-leader", function() {
				t.addCcpartyLeader();
			});
			// 编辑
			$(document).on("click", "#edit-ccparty-leader", function() {
				t.editCcpartyLeader();
			});
			// 确定事件
			$(document).on("click", "#leader-partymember-submit", function() {
				t.leaderPartyMemberSubmit();
			});
			// 保存
			$(document).on("click", "#ccparty-leader-submit", function() {
				t.saveOrUpdateLeader();
			});
		},
		render : function() {
			$("#leaders-table").bootstrapTable({
				queryParams : function(params) {
					$.extend(params, {
						ccpartyId : CcpartyNavigation.ccpartyId,
						type : '0'
					})
					return params;
				}
			});
		},
		partymemberNameFormatter : function(value, row) {
			return value.user.name;
		},
		codeFormatter : function(value, row) {
			return value.name;
		},
		nameFormatter : function(value, row) {
			return value.name;
		},
		genderFormatter : function(value, row) {
			return Global
					.getEnumName("obt_party_member.gender." + value.gender);
		},
		operateLeader : function(value, row) {
			var oper = '<a class="btn btn-default btn-xs" href="javascript:CcpartyLeader.editCcpartyLeader()"><i class="fa fa-edit"></i>编辑</a>&nbsp;&nbsp;';
			if (row.status == 0) {
				oper += '<a class="btn btn-default btn-xs" href="javascript:CcpartyLeader.statusOperator(&quot;'
						+ row.id
						+ '&quot;,&quot;1&quot;)"><i class="fa fa-dot-circle-o"></i>任职到期</a>&nbsp;&nbsp;';
			} else if (row.status == 1) {
				oper += '<a class="btn btn-default btn-xs" href="javascript:CcpartyLeader.statusOperator(&quot;'
						+ row.id
						+ '&quot;,&quot;0&quot;)"><i class="fa fa-check-circle-o"></i>重新任职</a>&nbsp;&nbsp;';
			}
			return oper;
		},
		statusFormatter : function(value, row) {
			return Global.getEnumName("org_ccparty_leader.status." + value);
		},
		// 新增
		addCcpartyLeader : function() {
			var data = new Object();
			data.ccpartyName = CcpartyNavigation.ccpartyName;
			data.dialog = "leader-dialog";
			data.type = "0";
			$("#dialogs").html(
					nunjucks.render(Global.appName
							+ '/tpl/organization/dialogs/ccparty-dialog.html',
							data));
			// 职位加载
			t.loadTitleCode();
			$("#leader-dialog").modal({});
		},
		// 编辑
		editCcpartyLeader : function() {
			var selectedRows = null;
			if (CcpartyManager.type == 0) {
				selectedRows = $("#leaders-table").bootstrapTable(
						'getSelections');
			} else if (CcpartyManager.type == 1) {
				selectedRows = $("#leaders-backup-table").bootstrapTable(
						'getSelections');
			}
			if (selectedRows.length != 1) {
				Notify.notice("请选择一条记录进行编辑");
				return;
			}
			var data = selectedRows[0];
			data.ccpartyName = CcpartyNavigation.ccpartyName;
			data.dialog = "leader-dialog";
			$("#dialogs").html(
					nunjucks.render(Global.appName
							+ '/tpl/organization/dialogs/ccparty-dialog.html',
							data));
			// 职位加载
			t.loadTitleCode(selectedRows[0].code.id);
			$("#leader-dialog").modal({});
		},
		// 加载职位
		loadTitleCode : function(selectVal) {
			$("#title option").remove(); // 清空option选项
			Ajax.call({
				url : "sys/getCodeListByParentId",
				p : {
					parentId : 'A070101' // 党内职务
				},
				f : function(data) {
					if (data && data.rows) {
						for (var i = 0; i < data.rows.length; i++) {
							$("#title").append(
									"<option value='" + data.rows[i].id + "'>"
											+ data.rows[i].name + "</option>");
						}
						if (selectVal != null) {
							$("#title").val(selectVal);
						}
					} else {
						$("#title").append(
								"<option value='0'>暂无可以使用的职务</option>");
					}
				}
			});
		},
		// 加载可以设置党委员的党员列表
		loadLeaderPartymembers : function() {
			$("#leader-dialog").modal('toggle');
			$("#leader-partymember-list-table").bootstrapTable({
				queryParams : function(params) {
					$.extend(params, {
						ccpartyId : CcpartyNavigation.ccpartyId
					})
					return params;
				}
			});
			$("#leader-partymember-dialog").modal({});
		},
		// 关闭党委设置弹框
		leaderPartymemberDialogClose : function() {
			$("#leader-partymember-dialog").modal('toggle');
			$("#leader-dialog").modal({});
		},
		// 选择确定
		leaderPartyMemberSubmit : function() {
			var selectedRows = $("#leader-partymember-list-table")
					.bootstrapTable('getSelections');
			if (selectedRows.length != 1) {
				Notify.notice("请选择一位党员进行设置");
				return;
			}
			$("#partymember").attr("ids", selectedRows[0].id);
			$("#partymember").val(selectedRows[0].user.name);
			t.leaderPartymemberDialogClose();
		},
		// 状态更改
		statusOperator : function(id, status) {
			bootbox.confirm({
				size : 'small',
				message : "确认执行？",
				callback : function(result) {
					if (result) {
						Ajax.call({
							url : "org/leaderStatusOperator",
							p : {
								id : id,
								status : status
							},
							f : function(data) {
								if (data && data.success) {
									Notify.success(data.msg);
									if (CcpartyManager.type == 0) {
										$("#leaders-table").bootstrapTable(
												'refresh');
									} else if (CcpartyManager.type == 1) {
										$("#leaders-backup-table")
												.bootstrapTable('refresh');
									}
								} else {
									Notify.error(data.msg);
								}
							}
						});
					}
				}
			});
		},
		// 保存或修改党委
		saveOrUpdateLeader : function() {
			var id = $("#id").val();
			var partymember = $("#partymember").attr("ids");
			var title = $("#title").val();
			var description = $("#description").val();
			var type = $("#type").val();
			if (CheckInputUtils.isEmpty(partymember)) {
				Notify.notice("请选择党员");
				$("#partymember").focus();
				return;
			}
			Ajax.call({
				url : "org/saveOrUpdateLeader",
				p : {
					id : id,
					partymember : partymember,
					title : title,
					description : description,
					ccpartyId : CcpartyNavigation.ccpartyId,
					type : type
				},
				f : function(data) {
					if (data && data.success) {
						Notify.success(data.msg);
						$("#leader-dialog").modal('toggle');
						if (CcpartyManager.type == 0) {
							$("#leaders-table").bootstrapTable('refresh');
						} else if (CcpartyManager.type == 1) {
							$("#leaders-backup-table")
									.bootstrapTable('refresh');
						}
					} else {
						Notify.error(data.msg);
					}
				}
			});
		}
	}
	return t;
}();