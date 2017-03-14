/**
 * @description 系统用户管理
 * @author 易文俊
 * @since 2016-01-11
 */
var ManagerSystemUser = function() {
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
            // 党组织树监听事件
            $(document).on("click", "#system-user-dialog #ccpartyId", function() {
                t.initCcpartySubjectTree();
                $("#system-user-dialog #ccpartyMenuContent").slideDown("fast");
                $("body").bind("mousedown", t.onCcpartyBodyDown);
            });
        },
        // 加载系统用户
        render : function() {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/system-user-list.html'));
            $("#system-user-table").bootstrapTable('destroy');
            $("#system-user-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                    	ccpartyId:Global.ccpartyId
                    })
                    return params;
                }
            });
        },
        // 新增系统用户
        newSystemUserDialog : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/system-user-add-bill.html'));
            $("#system-user-dialog").modal({});
        },
        // 修改系统用户对话框
        editSystemUserDialog : function(id) {
            Ajax.call({
                url : 'uam/getSystemUserById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data.item) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/system-user-edit-bill.html', data.item));
                        $("#system-user-dialog #gender").val(data.item.gender);
                        if (data.item.ccparty != null) {
                            $("#system-user-dialog #ccpartyId").val(data.item.ccparty.name);
                            $("#system-user-dialog #ccpartyId").attr("ids", data.item.ccparty.id);
                        }
                        $("#system-user-dialog").modal({});
                    } else {
                        Notify.error("获取编辑信息失败，请稍后再试。");
                    }
                }
            });
        },
        // 新增
        addSystemUser : function() {
            var loginNo = $("#system-user-dialog #loginNo").val();
            var name = $("#system-user-dialog #name").val();
            var gender = $("#system-user-dialog #gender").val();
            var ccpartyId = $("#system-user-dialog #ccpartyId").attr("ids");
            if (CheckInputUtils.isEmpty(loginNo)) {
                Notify.notice("请输入登录账号。");
                $("#system-user-dialog #loginNo").focus();
                return;
            } else if (CheckInputUtils.isEmpty(name)) {
                Notify.notice("请输入姓名");
                $("#system-user-dialog #name").focus();
                return;
            }
            Ajax.call({
                url : 'uam/addSystemUser',
                p : {
                    loginNo : loginNo,
                    name : name,
                    gender : gender,
                    ccpartyId : ccpartyId
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success("保存成功");
                        $("#system-user-dialog").modal('toggle');
                        $("#system-user-table").bootstrapTable('refresh');
                    } else {
                        Notify.error("保存失败");
                    }
                }
            });
        },
        updateSystemUser : function() {
            var id = $("#system-user-dialog #id").val();
            var name = $("#system-user-dialog #name").val();
            var gender = $("#system-user-dialog #gender").val();
            var ccpartyId = $("#system-user-dialog #ccpartyId").attr("ids");
            if (CheckInputUtils.isEmpty(name)) {
                Notify.notice("请输入姓名");
                $("#system-user-dialog #name").focus();
                return;
            }
            Ajax.call({
                url : 'uam/updateSystemUser',
                p : {
                    id : id,
                    name : name,
                    gender : gender,
                    ccpartyId : ccpartyId
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success("保存成功");
                        $("#system-user-dialog").modal('toggle');
                        $("#system-user-table").bootstrapTable('refresh');
                    } else {
                        Notify.error("保存失败");
                    }
                }
            });
        },
        // 密码重置
        resetPasswordDialog : function(id, name) {
            var data = {
                id : id,
                name : name
            };
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/reset-system-user-passwork-dialog.html', data));
            $("#reset-dialog").modal({});
        },
        // 重置
        resetPasswordSubmit : function() {
            var id = $("#reset-dialog #id").val();
            var password = $("#reset-dialog #password").val();
            var confirmPassword = $("#reset-dialog #confirmPassword").val();
            if (CheckInputUtils.isEmpty(password)) {
                Notify.notice("请输入重置密码");
                $("#reset-dialog #password").focus();
                return;
            } else if (CheckInputUtils.isEmpty(confirmPassword)) {
                Notify.notice("请再次输入重置密码");
                $("#reset-dialog #confirmPassword").focus();
                return;
            } else if (password != confirmPassword) {
                Notify.notice("两次输入的密码不一致");
                $("#reset-dialog #confirmPassword").focus();
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
                        $("#system-user-table").bootstrapTable('refresh');
                    } else {
                        Notify.error(data.msg);
                    }
                }
            });
        },
        // 重置系统用户角色
        resetRole : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认重置系统用户角色？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'uam/resetSystemUserRole',
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success(data.msg);
                                    $("#system-user-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });
                    }
                }
            });
        },
        // 删除系统用户
        deleteSystemUser : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除系统用户？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'uam/deleteUser',
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success(data.msg);
                                    $("#system-user-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });
                    }
                }
            });

        },
        // 党组织树
        initCcpartySubjectTree : function() {
            ComTree.initTree({
                divContainer : "#system-user-dialog #ccparty-tree",
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
            $("#system-user-dialog #ccpartyId").val(treeNode.name);
            $("#system-user-dialog #ccpartyId").attr("ids", treeNode.id);
        },
        // 鼠标失去焦点
        onCcpartyBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "ccpartyMenuContent" || $(event.target).parents("#system-user-dialog #ccpartyMenuContent").length > 0)) {
                $("#system-user-dialog #ccpartyMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        // 验证登录账号是否占用
        checkLoginNo : function(loginNo) {
            if (CheckInputUtils.isEmpty(loginNo)) {
                Notify.notice("请输入登录账号。");
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
        // 清除组织
        clearOrganization : function(inputId) {
            $("#" + inputId).val('');
            $("#" + inputId).attr("ids", '');
        },
        // 党组织
        ccpartyFormatter : function(value, row) {
            if (value != null) {
                return value.name + "(" + value.id + ")";
            } else {
                return "-";
            }
        },
        operator : function(value, row) {
            var html = "";
            html += '<a href="javascript:ManagerSystemUser.editSystemUserDialog(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
            html += '<a href="javascript:ManagerSystemUser.deleteSystemUser(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a>'
            html += '<a href="javascript:ManagerSystemUser.resetPasswordDialog(\'' + row.id + '\',\'' + row.name
                    + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-repeat"></i>重置密码</a>'
            if (row.userType != 3) {
                html += '<a href="javascript:ManagerSystemUser.resetRole(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-user"></i>重置角色</a>'
            }
            return html;
        }
    }
    return t;
}();