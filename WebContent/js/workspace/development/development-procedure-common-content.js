/**
 * @description 党员发展阶段公共内容JS
 * @author 赵子靖
 * @since 2015-12-18
 */
var DevelopmentProcedureCommonContent = function() {
	var t = {
		mainContainer : '',
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initEvent();
		},
		initEvent : function() {
		},
		// 参加组织生活情况
		renderActivityCommonContent : function(partymemberId) {
			$("#maintainDialogs")
					.html(nunjucks
							.render(Global.appName
									+ '/tpl/workspace/dialogs/development/activity-common-content-list-dialog.html'));
			$("#activity-common-content-list-table").bootstrapTable({
				queryParams : function(params) {
					$.extend(params, {
								partymemberId : partymemberId,
								type : 1
							})
					return params;
				},
				onClickRow : function(row) {
					$("#activity-common-content-list-dialog #id").val(row.id);
					$("#activity-common-content-list-dialog #contentDate")
							.val(Global.getDate(row.contentDate));
					$("#activity-common-content-list-dialog #name")
							.val(row.name);
				}
			});
			$("#activity-common-content-list-dialog").modal({});
		},
		// 向党组织汇报情况
		renderReportCommonContent : function(partymemberId) {
			$("#maintainDialogs")
					.html(nunjucks
							.render(Global.appName
									+ '/tpl/workspace/dialogs/development/report-common-content-list-dialog.html'));
			$("#report-common-content-list-table").bootstrapTable({
				queryParams : function(params) {
					$.extend(params, {
								partymemberId : partymemberId,
								type : 2
							})
					return params;
				},
				onClickRow : function(row) {
					$("#report-common-content-list-dialog #id").val(row.id);
					$("#report-common-content-list-dialog #contentDate")
							.val(Global.getDate(row.contentDate));
					$("#report-common-content-list-dialog #name").val(row.name);
					$("#report-common-content-list-dialog #content")
							.val(row.content);
				}
			});
			$("#report-common-content-list-dialog").modal({});
		},
		// 向党组织汇报情况
		renderInspectCommonContent : function(partymemberId) {
			$("#maintainDialogs")
					.html(nunjucks
							.render(Global.appName
									+ '/tpl/workspace/dialogs/development/inspect-common-content-list-dialog.html'));
			$("#inspect-common-content-list-table").bootstrapTable({
				queryParams : function(params) {
					$.extend(params, {
								partymemberId : partymemberId,
								type : 3
							})
					return params;
				},
				onClickRow : function(row) {
					$("#inspect-common-content-list-dialog #id").val(row.id);
					$("#inspect-common-content-list-dialog #contentDate")
							.val(Global.getDate(row.contentDate));
					$("#inspect-common-content-list-dialog #name")
							.val(row.name);
					$("#inspect-common-content-list-dialog #content")
							.val(row.content);
				}
			});
			$("#inspect-common-content-list-dialog").modal({});
		},
		// 保存或修改
		saveOrCommonContent : function(dialogDivId) {
			var name = $("#" + dialogDivId + " #name").val();
			var paramters = {
				id : $("#" + dialogDivId + " #id").val(),
				type : $("#" + dialogDivId + " #type").val(),
				contentDate : $("#" + dialogDivId + " #contentDate").val(),
				name : name,
				partymemberId : $("#partymemberId").val(),
				procedureId : $("#procedureId").val(),
				content : $("#" + dialogDivId + " #content").val()
			}
			if (CheckInputUtils.isEmpty(name)) {
				Notify.notice("有必填项为空。");
				$("#" + dialogDivId + " #name").focus();
				return;
			}
			Ajax.call({
						url : 'obt/saveOrUpdateDevelopmentProcedureCommonContent',
						p : {
							paramters : "{'data':"
									+ JSON.stringify(paramters, "data") + "}"
						},
						f : function(data) {
							if (data.success) {
								Notify.success(data.msg);
								if (paramters.type == 1) {
									$("#activity-common-content-list-table")
											.bootstrapTable('refresh');
								} else if (paramters.type == 2) {
									$("#report-common-content-list-table")
											.bootstrapTable('refresh');
								} else if (paramters.type == 3) {
									$("#inspect-common-content-list-table")
											.bootstrapTable('refresh');
								}
								$("#" + dialogDivId + " #id").val("");
								$("#" + dialogDivId + " #contentDate").val("");
								$("#" + dialogDivId + " #name").val("");
								$("#" + dialogDivId + " #content").val("");
							} else {
								Notify.error(data.msg);
							}
						}
					});
		},
		// 删除
		deleteCommonContent : function(id, type) {
			bootbox.confirm({
				size : 'small',
				message : "确认删除？",
				callback : function(result) {
					if (result) {
						Ajax.call({
							url : 'obt/deleteDevelopmentProcedureCommonContent',
							p : {
								id : id
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									if (type == 1) {
										$("#activity-common-content-list-table")
												.bootstrapTable('refresh');
									} else if (type == 2) {
										$("#report-common-content-list-table")
												.bootstrapTable('refresh');
									} else if (type == 3) {
										$("#inspect-common-content-list-table")
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
		dataFormatter : function(value, row) {
			return Global.getDate(value);
		},
		// 删除
		deleteOperator : function(value, row) {
			var operator = "";
			operator += '<a href="javascript:DevelopmentProcedureCommonContent.deleteCommonContent(\''
					+ value
					+ '\',\''
					+ row.type
					+ '\')" class="btn btn-default btn-xs operate-btn"><i class="glyphicon glyphicon-trash"></i>删除</a>';
			return operator;
		}

	}
	return t;
}();