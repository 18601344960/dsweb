/**
 * @description 用户管理
 * @author zhaozijing
 * @since 2015-06-09
 */
var ManagerUser = function() {
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
            // 行政组织树监听事件
            $(document).on("click", "#user-dialog #userOrganizationId", function() {
                $("#userOrganizationMenuContent").slideDown("fast");
                $("body").bind("mousedown", t.onOrganizationBodyDown);
            });
            // 党组织树监听事件
            $(document).on("click", "#user-dialog #ccpartyId", function() {
                t.initCcpartySubjectTree();
                $("#ccpartyMenuContent").slideDown("fast");
                $("body").bind("mousedown", t.onCcpartyBodyDown);
            });
            // 团委树监听事件
            $(document).on("click", "#user-dialog #youthId", function() {
                t.initYouthSubjectTree();
                $("#youthMenuContent").slideDown("fast");
                $("body").bind("mousedown", t.onYouthBodyDown);
            });
            // 新增用户
            $(document).on("click", "#add-user-process-definition", function() {
                t.addUserDialog();
            });
        },
        // 加载用户
        render : function() {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/user/user-list.html'));
            $("#user-process-definition-table").bootstrapTable('destroy');
            $("#user-process-definition-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        organizationId : ManagerUserNavigation.organizationId
                    })
                    return params;
                }
            });
        },
        searchUser : function() {
            var search = $("#search").val();
            var departmentId = $("#searchDepartmentId").val();
            $("#user-process-definition-table").bootstrapTable('destroy');
            $("#user-process-definition-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        organizationId : ManagerUserNavigation.organizationId,
                        departmentId : departmentId,
                        search : search
                    })
                    return params;
                }
            });
        },
        // 行政组织树
        initOrganizationSubjectTree : function() {
            ComTree.initTree({
                divContainer : "#user-organization-tree",
                url : 'org/getCurrentOrganizationAndSunsToTreeView',
                p : {
                    organizationId : Global.organizationId
                },
                onClick : t.organizationClickCallBack
            });
        },
        // 点击回调函数
        organizationClickCallBack : function(e, treeId, treeNode) {
            // step1、赋值
            $("#userOrganizationId").val(treeNode.name);
            $("#userOrganizationId").attr("ids", treeNode.id);
        },
        // 鼠标失去焦点
        onOrganizationBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "userOrganizationMenuContent" || $(event.target).parents("#userOrganizationMenuContent").length > 0)) {
                $("#userOrganizationMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        // 党组织树
        initCcpartySubjectTree : function() {
            ComTree.initTree({
                divContainer : "#ccparty-tree",
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                onClick : t.ccpartyClickCallBack
            });
        },
        // 点击回调函数
        ccpartyClickCallBack : function(e, treeId, treeNode) {
            // step1、赋值
            $("#ccpartyId").val(treeNode.name);
            $("#ccpartyId").attr("ids", treeNode.id);
        },
        // 鼠标失去焦点
        onCcpartyBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "ccpartyMenuContent" || $(event.target).parents("#ccpartyMenuContent").length > 0)) {
                $("#ccpartyMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        // 团委织树
        initYouthSubjectTree : function() {
            ComTree.initTree({
                divContainer : "#youth-tree",
                url : 'mlw/getYouthChildrensTree',
                p : {
                    youthId : Global.youthId
                },
                onClick : t.youthClickCallBack
            });
        },
        // 点击回调函数
        youthClickCallBack : function(e, treeId, treeNode) {
            // step1、赋值
            $("#youthId").val(treeNode.name);
            $("#youthId").attr("ids", treeNode.id);
        },
        // 鼠标失去焦点
        onYouthBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "youthMenuContent" || $(event.target).parents("#youthMenuContent").length > 0)) {
                $("#youthMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        typeRadioChange : function(value) {
            if ('A476201' == value || 'A476202' == value) {
                $("#youthTr").hide();
                $("#youthId").val("");
                $("#youthId").attr("ids", "");
                // 所属党组织显示
                $("#orgTr").show();
            } else if ('A476203' == value) {
                $("#orgTr").hide();
                $("#ccpartyId").val("");
                $("#ccpartyId").attr("ids", "");
                // 显示团委
                $("#youthTr").show();
            } else {
                // 隐藏党组织、团委
                $("#orgTr").hide();
                $("#ccpartyId").val("");
                $("#ccpartyId").attr("ids", "");
                $("#youthTr").hide();
                $("#youthId").val("");
                $("#youthId").attr("ids", "");
            }
        },
        // 新增用户对话框
        addUserDialog : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/user-add-dialog.html'));
            t.initOrganizationSubjectTree();
            JSUtils.getSysCodeByParent('A4762', 'user_type', '');
            $("#user-dialog").modal({});
        },
        // 编辑用户
        editUserDialog : function(id) {
            Ajax.call({
                url : "uam/loadUserInfoByUserId",
                p : {
                    userId : id
                },
                f : function(data) {
                    if (data && data.item) {
                        data.item.showIdNumber = '';
                        if (!CheckInputUtils.isEmpty(data.item.idNumber)) {
                            for (var i = 0; i < data.item.idNumber.length - 8; i++) {
                                data.item.showIdNumber += '*';
                            }
                            data.item.showIdNumber += data.item.idNumber.substr(data.item.idNumber.length - 8);
                        }
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/user-edit-dialog.html', data.item));
                        JSUtils.getSysCodeByParent('A4762', 'user_type', data.item.type);
                        $("#user_gender").val(data.item.gender);
                        $("#user_status_" + data.item.status).attr("checked", "checked");
                        $("#userOrganizationId").attr("ids", data.organizationIds);
                        $("#userOrganizationId").val(data.organizationNames);
                        if (data.department != null) {
                            $("#user_department").val(data.department.id);
                        }
                        $("#user_type").val(data.item.type);
                        if (data.item.type == 'A476201' || data.item.type == 'A476202') {
                            // 党员
                            $("#orgTr").show();
                            $("#ccpartyId").attr("ids", data.ids);
                            $("#ccpartyId").val(data.names);
                        } else if (data.item.type == 'A476203') {
                            // 团委
                            $("#youthTr").show();
                            $("#youthId").attr("ids", data.ids);
                            $("#youthId").val(data.names);
                        }
                        t.initOrganizationSubjectTree();
                        if (data.department != null) {
                            $("#user-dialog #user_department").val(data.department.id);
                        }
                        $("#user-dialog").modal({});
                    } else {
                        Notify.error("获取用户信息异常");
                    }
                }
            });
        },
        // 密码重置
        resetPasswordDialog : function(id, name) {
            var data = {
                id : id,
                name : name
            }
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/user-reset-password-dialog.html', data));
            $("#reset-dialog").modal({});
        },
        // 密码重置提交事件
        resetUserPasswordSubmit : function() {
            var id = $("#id").val();
            var password = $("#password").val();
            var confirmPassword = $("#confirmPassword").val();
            if (CheckInputUtils.isEmpty(password)) {
                Notify.notice("请输入重置密码");
                $("#password").focus();
                return;
            } else if (CheckInputUtils.isEmpty(confirmPassword)) {
                Notify.notice("请再次输入重置密码");
                $("#confirmPassword").focus();
                return;
            } else if (password != confirmPassword) {
                Notify.notice("两次输入的密码不一致");
                $("#confirmPassword").focus();
                return;
            }
            Ajax.call({
                url : 'uam/resetUserPasswordByManager',
                p : {
                    id : id,
                    newPassword : password
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success(data.msg);
                        $("#reset-dialog").modal('toggle');
                        $("#user-process-definition-table").bootstrapTable('refresh');
                    } else {
                        Notify.error(data.msg);
                    }
                }
            });
        },
        // 删除用户
        deleteUser : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除该用户，删除后将不可恢复？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "uam/deleteUser",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    $("#user-process-definition-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });
                    }
                }
            });
        },
        // 用户角色设置
        userRoleSet : function() {
            var selectedRows = $("#user-process-definition-table").bootstrapTable('getSelections');
            if (selectedRows.length != 1) {
                Notify.notice("请选择一条记录进行角色分配");
                return;
            }
            var data = new Object();
            data.dialog = "user-role-dialog";
            data.name = selectedRows[0].name;
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/user-dialogs.html', data));
            t.initRoleSubjectTree();
            $("#user-role-dialog").modal({});
            var treeObj = $.fn.zTree.getZTreeObj("role-tree");
            var roles = selectedRows[0].roles;
            for (var i = 0; i < roles.length; i++) {
                // 将权限树反选
                var node = treeObj.getNodeByParam("id", roles[i].id, null);
                treeObj.checkNode(node, true, false);
            }
        },
        // 新增用户
        addUser : function() {
            var loginNo = $("#loginNo").val();// 登录账号
            var name = $("#user_name").val(); // 姓名
            var idNumber = $("#user_idNumber").val(); // 身份证号
            var gender = $("#user_gender").val(); // 政治面貌
            var type = $("#user_type").val(); // 政治面貌
            var ccpartyId = $("#ccpartyId").attr("ids"); // 所属党组织
            var youthId = $("#youthId").attr("ids"); // 所属团委
            var organizationId = $("#userOrganizationId").attr("ids"); // 行政单位
            var departmentId = $("#user_department").val(); // 部门
            var officePhone = $("#officePhone").val();
            var mobile = $("#mobile").val();
            if (CheckInputUtils.isEmpty(loginNo)) {
                Notify.notice("请输入登录账号。");
                $("#loginNo").focus();
                return;
            } else if (CheckInputUtils.isEmpty(name)) {
                Notify.notice("请输入用户姓名");
                $("#user_name").focus();
                return;
            } else if (CheckInputUtils.isEmpty(organizationId)) {
                Notify.notice("请选择行政单位");
                $("#userOrganizationId").focus();
                return;
            } else if (("A476201" == type || 'A476202' == type) && (ccpartyId == null || ccpartyId == "")) {
                Notify.notice("请选择所属党组织");
                $("#ccpartyId").focus();
                return;
            } else if (("A476203" == type) && (youthId == null || youthId == "")) {
                Notify.notice("请选择所属团委");
                $("#youthId").focus();
                return;
            }
            // 传递参数
            var paramters = {
                loginNo : loginNo,
                name : name,
                idNumber : idNumber,
                type : type,
                ccpartyId : ccpartyId,
                youthId : youthId,
                organizationId : organizationId,
                departmentId : departmentId,
                gender : gender,
                officePhone : officePhone,
                mobile : mobile
            };

            Ajax.call({
                url : 'uam/addUserForManager',
                p : {
                    paramters : "{'data':" + JSON.stringify(paramters, "data") + "}"
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success(data.msg);
                        $("#user-dialog").modal('toggle');
                        $("#user-process-definition-table").bootstrapTable('refresh');
                    } else {
                        Notify.error("保存失败,请稍后再试。");
                    }
                }
            });

        },
        // 保存按钮事件
        updateUser : function() {
            var id = $("#user_id").val(); // ID主键
            var name = $("#user_name").val(); // 姓名
            var idNumber = $("#user_idNumber").val(); // 身份证号
            var gender = $("#user_gender").val(); // 政治面貌
            var type = $("#user_type").val(); // 政治面貌
            var ccpartyId = $("#ccpartyId").attr("ids"); // 所属党组织
            var youthId = $("#youthId").attr("ids"); // 所属团委
            var organizationId = $("#userOrganizationId").attr("ids"); // 行政单位
            var departmentId = $("#user_department").val(); // 部门
            var officePhone = $("#officePhone").val();
            var mobile = $("#mobile").val();
            if (CheckInputUtils.isEmpty(name)) {
                Notify.notice("请输入用户姓名");
                $("#user_name").focus();
                return;
            } else if (CheckInputUtils.isEmpty(organizationId)) {
                Notify.notice("请选择行政单位");
                $("#userOrganizationId").focus();
                return;
            } else if (("A476201" == type || 'A476202' == type) && (ccpartyId == null || ccpartyId == "")) {
                Notify.notice("请选择所属党组织");
                $("#ccpartyId").focus();
                return;
            } else if (("A476203" == type) && (youthId == null || youthId == "")) {
                Notify.notice("请选择所属团委");
                $("#youthId").focus();
                return;
            }
            // 传递参数
            var paramters = {
                id : id,
                name : name,
                idNumber : idNumber,
                type : type,
                ccpartyId : ccpartyId,
                youthId : youthId,
                organizationId : organizationId,
                departmentId : departmentId,
                gender : gender,
                officePhone : officePhone,
                mobile : mobile
            };

            Ajax.call({
                url : 'uam/updateUserForManager',
                p : {
                    paramters : "{'data':" + JSON.stringify(paramters, "data") + "}"
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success("保存成功");
                        $("#user-dialog").modal('toggle');
                        $("#user-process-definition-table").bootstrapTable('refresh');
                    } else {
                        Notify.error("保存失败");
                    }
                }
            });

        },
        // 验证登录账号是否占用
        checkLoginNo : function(loginNo) {
            if (CheckInputUtils.isEmpty(loginNo)) {
                Notify.notice("请输入登录账号。");
                $("#loginNo").focus();
                return;
            } else if (CheckInputUtils.checkIsMobilePhone(loginNo)) {
                Notify.notice("请不要使用手机号作为登录账号。");
                $("#loginNo").focus();
                return;
            } else {
                Ajax.call({
                    url : 'uam/checkUserLoginNo',
                    p : {
                        loginNo : loginNo
                    },
                    f : function(data) {
                        if (data.success == false) {
                            Notify.notice(data.msg);
                            $("#loginNo").focus();
                            return;
                        }
                    }
                });
            }
        },
        departmentName : function(value, row) {
            if (value != null) {
                return value.name;
            } else {
                return "-";
            }
        },
        codeName : function(value, row) {
            return Global.getCodeName(value);
        },
        genderFormatter : function(value, row) {
            return Global.getCodeName('A0107.' + value);
        },
        statusFormatter : function(value, row) {
            return Global.getEnumName('uam_user.status.' + value);
        },
        idNumberFormatter : function(value, row) {
            var showIdNumber = '';
            if (!CheckInputUtils.isEmpty(value)) {
                for (var i = 0; i < value.length - 8; i++) {
                    showIdNumber += '*';
                }
                showIdNumber += value.substr(value.length - 8);
            }
            return showIdNumber;
        },
        operator : function(value, row) {
            var html = "";
            html += '<a href="javascript:ManagerUser.editUserDialog(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
            html += '<a href="javascript:ManagerUser.deleteUser(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a>'
            html += '<a href="javascript:ManagerUser.resetPasswordDialog(\'' + row.id + '\',\'' + row.name + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-repeat"></i>重置密码</a>'
            return html;
        }
    }
    return t;
}();