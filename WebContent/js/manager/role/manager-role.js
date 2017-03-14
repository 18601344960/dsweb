/**
 * @description 角色管理
 * @author zhaozijing
 * @since 2015-06-12
 * @update 2015-08-13
 */
var ManagerRole = function() {
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
        },
        // 加载角色列表
        render : function() {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/role/role-list.html', Global));
            $("#role-table").bootstrapTable('destroy');
            $("#role-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {})
                    return params;
                }
            });
        },
        statusFormatter : function(value, row) {
            return Global.getEnumName('uam_role.status.' + value);
        },
        // 新增角色
        addRole : function() {
            var data = new Object();
            data.dialogType = "add";
            data.dialog = "role-dialog";
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/role-dialogs.html', data));
            $("#role-dialog").modal({});
        },
        // 编辑角色
        editRole : function(id) {
            Ajax.call({
                url : 'uam/getRoleById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {

                        data.item.dialogType = "edit";
                        data.item.dialog = "role-dialog";
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/role-dialogs.html', data.item));
                        $("#role_status_" + data.item.status).attr("checked", "checked");
                        $("#role-dialog").modal({});

                    }
                }
            });
        },
        // 权限分配
        rolePrivilege : function(id) {
            Ajax.call({
                url : 'uam/getRoleById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        data.item.dialog = "role-privilege-dialog";
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/role-dialogs.html', data.item));
                        t.initPrivilegeSubjectTree(data.item.id);
                        $("#role-privilege-dialog").modal({});
                        var treeObj = $.fn.zTree.getZTreeObj("privilege-tree");
                        var privileges = data.item.privileges;
                        for (var i = 0; i < privileges.length; i++) {
                            // 将权限树反选
                            var node = treeObj.getNodeByParam("id", privileges[i].id, null);
                            treeObj.checkNode(node, true, false);
                        }
                    }
                }
            });
        },
        // 权限树
        initPrivilegeSubjectTree : function(roleId) {
            ComTree.initTree({
                divContainer : "#privilege-tree",
                url : 'uam/getAllPrivilegesTree',
                p : {
                    roleId : roleId
                },
                async : false,
                enable : true,
                chkStyle : "checkbox",
                chkboxType : {
                    "Y" : "",
                    "N" : ""
                }
            });
        },
        // 新增or保存按钮事件
        addOrUpdateRoleSubmit : function() {
            var id = $("#role_id").val(); // 角色ID
            var name = CheckInputUtils.splitSpaceForStrAround($("#role_name").val()); // 角色名称
            var desc = CheckInputUtils.splitSpaceForStrAround($("#role_desc").val()); // 角色描述
            var status = $("input[name='role_status']:checked").val(); // 状态
            if (name == null || name == "") {
                Notify.notice("请输入角色名称");
                return;
            }
            Ajax.call({
                url : 'uam/addOrEditRole',
                p : {
                    id : id,
                    name : name,
                    desc : desc,
                    status : status,
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success("保存成功");
                        $("#role-dialog").modal('toggle');
                        $("#role-table").bootstrapTable('refresh');
                    } else {
                        Notify.error("保存失败");
                    }
                }
            });
        },
        // 权限保存
        saveRolePrivilegeSubmit : function() {
            var selectedRows = $("#role-table").bootstrapTable('getSelections');
            var privileges = "";
            var roleId = selectedRows[0].id;
            var treeObj = $.fn.zTree.getZTreeObj("privilege-tree");
            var nodes = treeObj.getCheckedNodes(true);
            for (var i = 0; i < nodes.length; i++) {
                privileges += nodes[i].id + ",";
            }
            if(CheckInputUtils.isEmpty(privileges)){
                Notify.notice("请至少选择一条权限。");
                return;
            }
            Ajax.call({
                url : 'uam/saveRolePrivileges',
                p : {
                    roleId : roleId,
                    privileges : privileges
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success(data.msg);
                        $("#role-privilege-dialog").modal('toggle');
                        $("#role-table").bootstrapTable('refresh');
                    } else {
                        Notify.error(data.msg);
                    }
                }
            });
        },
        operator : function(value, row) {
            var html = '';
            html += '<a href="javascript:ManagerRole.editRole(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
            html += '<a href="javascript:ManagerRole.rolePrivilege(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-user"></i>分配权限</a>';
            return html;
        }
    }
    return t;
}();