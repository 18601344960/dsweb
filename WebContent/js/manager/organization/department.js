/**
 * @description 部门
 * @author zhaozijing
 * @since 2015-07-30
 */
var Department = function() {
	var t = {
		mainContainer : '',
		organizationIdIsExist : true,
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initView();
			t.initEvent();
		},
		initView : function() {

		},
		initEvent : function() {
			// 新增事件
			$(document).on("click", "#add-department-button", function() {
				t.addDepartment();
			});
		},
		// 新增
		addDepartment : function() {
			var config = {
				flag : 'new'
			};
			config.organizationName = "test";
			config.organizationName = AdministrationNavigation.administrationName;
			$("#dialogs").html(
					nunjucks.render(Global.appName
							+ '/tpl/manager/dialogs/department-dialog.html',
							config));
			$("#administration-department-dialog").modal({});
		},
		// 修改
		editDepartment : function() {
			var selectedRows = $("#department-table").bootstrapTable(
					'getSelections');
			if (selectedRows.length != 1) {
				Notify.info("请先选择编辑的信息");
				return;
			}
			selectedRows[0].flag = 'edit';
			selectedRows[0].organizationName = AdministrationNavigation.administrationName;
			$("#dialogs").html(
					nunjucks.render(Global.appName
							+ '/tpl/manager/dialogs/department-dialog.html',
							selectedRows[0]));
			$("#isCcpartyDepartment").val(selectedRows[0].isCcpartyDepartment);
			$("#administration-department-dialog").modal({});
		},
		render : function() {
			$("#department-table")
					.bootstrapTable(
							{
								queryParams : function(params) {
									$
											.extend(
													params,
													{
														organizationId : AdministrationNavigation.administrationId,
													})
									return params;
								}
							});
		},
		// 删除部门
		deleteDepartment : function(id) {
			bootbox.confirm({
				size : 'small',
				message : "确认删除？",
				callback : function(result) {
					if (result) {
						Ajax.call({
							url : "org/deleteDepartment",
							p : {
								id : id
							},
							f : function(data) {
								if (data && data.success) {
									Notify.success(data.msg);
									$("#department-table").bootstrapTable(
											'refresh');
								} else {
									Notify.error(data.msg);
								}
							}
						});
					}
				}
			});

		},
		// 保存或者修改
		saveOrUpdateDepartment : function() {
			var organizationId = AdministrationNavigation.administrationId;
			var id = $("#departmentId").val();
			var name = CheckInputUtils.splitSpaceForStrAround($(
					"#departmentName").val());
			var location = $("#departmentLocation").val();
			var description = $("#departmentDescription").val();
			var isCcpartyDepartment = $("#isCcpartyDepartment").val();
			if (CheckInputUtils.isEmpty(name)) {
				Notify.notice("请输入部门名称");
				$("#departmentName").focus();
				return;
			}
			Ajax.call({
				url : "org/saveOrUpdateDepartment",
				p : {
					id : id,
					organizationId : organizationId,
					name : name,
					location : location,
					description : description,
					isCcpartyDepartment : isCcpartyDepartment
				},
				f : function(data) {
					if (data.success) {
						Notify.success(data.msg);
						$("#administration-department-dialog").modal('toggle');
						$("#department-table").bootstrapTable('refresh');
					} else {
						Notify.error(data.msg);
					}
				}
			});

		},
		operateDepartment : function(value, row) {
			var oper = '<a class="btn btn-default btn-xs" href="javascript:Department.editDepartment()"><i class="fa fa-edit"></i>编辑</a>&nbsp;&nbsp;';
			oper += '<a class="btn btn-default btn-xs" href="javascript:Department.deleteDepartment(\''
					+ value
					+ '\')"><i class="fa fa-trash-o"></i>删除</a>&nbsp;&nbsp;';
			return oper;
		},
		icdFormatter : function(value, row) {
			return Global.getEnumName("org_department.isccpartydepartment."
					+ value);
		}
	}
	return t;
}();