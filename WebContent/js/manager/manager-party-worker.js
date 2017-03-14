/**
 * @description 党务人员管理
 * @author 易文俊
 * @since 2016-01-08
 */
var ManagerPartyWorker = function() {
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
            $(document).on("click", "#party-worker #selectCcpartyId", function() {
                $("#party-worker #ccpartyMenuContent").slideDown("fast");
                $("body").bind("mousedown", t.onCcpartyBodyDown);
            });
            $(document).on("click", "#select-users-dialog #selectOrganizationId", function() {
                $("#select-users-dialog #menuContent").slideDown("fast");
                $("body").bind("mousedown", t.onOrganizationBodyDown);
            });
            $(document).on("click", "#select-users-dialog #partyworkCcparty", function() {
                $("#partyworkCcpartyMenuContent").css({}).slideDown("fast");
                $("body").bind("mousedown", t.onBodyPartyWorkCcpartyDown);
            });
        },
        // 加载党务人员
        render : function() {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/party-worker-list.html'));
            ManagerPartyWorker.initCcpartyTree();
            t.loadPartyWorkers();
        },
        //加载党务人员列表
        loadPartyWorkers:function(){
            var search = $("#search").val();
            var ccpartyIds = $("#selectCcpartyId").attr("ids");
            $("#user-work-table").bootstrapTable('destroy');
            $("#user-work-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        searchCcpartyIds:ccpartyIds,
                        search:search
                    })
                    return params;
                }
            });
        },
        //组织初始化树
        initCcpartyTree : function() {
            ComTree.initTree({
                divContainer : "#party-worker #ccparty-tree",
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                chkStyle : "checkbox",
                enable : true,
                async : false,
                chkboxType : {
                    "Y" : "",
                    "N" : ""
                },
                onCheck : t.ccpartyCallBack
            });
        },
        // 树点击回调函数
        ccpartyCallBack : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("ccparty-tree");
            var nodes = zTree.getCheckedNodes(true);
            var names = "";
            var ids = "";
            for (var i = 0, l = nodes.length; i < l; i++) {
                names += nodes[i].name + ",";
                ids += nodes[i].id + ",";
            }
            if (names.length > 0) {
                names = names.substring(0, names.length - 1);
                ids = ids.substring(0, ids.length - 1);
            }
            var ccpartyIdObj = $("#selectCcpartyId");
            ccpartyIdObj.val(names);
            ccpartyIdObj.attr("ids", ids);
            t.loadPartyWorkers();
        },
        // 鼠标失去焦点
        onCcpartyBodyDown : function(event) {
            if (!(event.target.id == "ccpartyMenuBtn" || event.target.id == "ccpartyMenuContent" || $(event.target).parents("#ccpartyMenuContent").length > 0)) {
                $("#party-worker #ccpartyMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        // 组织树初始化
        initCcpartyId : function() {
            t.initCcpartyTree();
            var ccpartyIdObj = $("#selectCcpartyId");
            ccpartyIdObj.val('');
            ccpartyIdObj.attr("ids", '');
            t.loadPartyWorkers();
        },
        // 党务工作归属组织树
        // 党组织树
        initPartyWorkCcpartySubjectTree : function() {
            var setting = {
                check : {
                    enable : true,
                    chkStyle : "radio",
                    radioType : "all"
                },
                view : {
                    dblClickExpand : false
                },
                data : {
                    simpleData : {
                        enable : true
                    }
                },
                callback : {
                    onClick : t.treeClick,
                    onCheck : t.treeCheck
                }
            };
            Ajax.call({
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                async:false,
                f : function(data) {
                    if (data && data.items) {
                        var treeObj = $.fn.zTree.init($("#party-work-ccparty-tree"), setting, data.items);
                    } else {
                        Notify.error("获取组织树失败");
                    }
                }
            });
        },
        treeClick : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("party-work-ccparty-tree");
            zTree.checkNode(treeNode, !treeNode.checked, null, true);
            return false;
        },
        treeCheck : function(e, treeId, treeNode) {
            var ccpartyIdObj = $("#select-users-dialog #partyworkCcparty");
            ccpartyIdObj.val(treeNode.name);
            ccpartyIdObj.attr("ids", treeNode.id);
        },
        //组织树失去焦点
        onBodyPartyWorkCcpartyDown:function(event) {
            if (!(event.target.id == "partyworkCcpartyMenuContent" || $(event.target).parents("#partyworkCcpartyMenuContent").length > 0)) {
                $("#select-users-dialog #partyworkCcpartyMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        // 选择用户
        selectUsersDialog : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/party-worker/select-users-dialog.html'));
            $("#select-users-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId
                    })
                    return params;
                },
                onCheck : function(row) {
                    $("#partyWorkNameDiv #userName").text(row.name);
                    $("#partyWorkNameDiv").slideDown();
                }
            });
            t.initOrganizationTree();
            $("#select-users-dialog").modal({});
        },
        // 用户搜索加载
        searchSelectUsers : function() {
            $("#partyWorkNameDiv").slideUp();
            var organizationIds = $("#select-users-dialog #selectOrganizationId").attr("ids");
            var search = $("#select-users-dialog #search").val();
            $("#select-users-table").bootstrapTable('destroy');
            $("#select-users-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        ccpartyIds : organizationIds,
                        search : search
                    })
                    return params;
                },
                onCheck : function(row) {
                    $("#partyWorkNameDiv #userName").text(row.name);
                    $("#partyWorkNameDiv").slideDown();
                }
            });
        },
        // 行政单位初始化树
        initOrganizationTree : function() {
            ComTree.initTree({
                divContainer : "#organization-tree",
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                chkStyle : "checkbox",
                enable : true,
                async : false,
                chkboxType : {
                    "Y" : "",
                    "N" : ""
                },
                onCheck : t.organizationCallBack
            });
        },
        // 树点击回调函数
        organizationCallBack : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("organization-tree");
            var nodes = zTree.getCheckedNodes(true);
            var names = "";
            var ids = "";
            for (var i = 0, l = nodes.length; i < l; i++) {
                names += nodes[i].name + ",";
                ids += nodes[i].id + ",";
            }
            if (names.length > 0) {
                names = names.substring(0, names.length - 1);
                ids = ids.substring(0, ids.length - 1);
            }
            var organizationIdObj = $("#selectOrganizationId");
            organizationIdObj.val(names);
            organizationIdObj.attr("ids", ids);
            t.searchSelectUsers();
        },
        // 鼠标失去焦点
        onOrganizationBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
                $("#menuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        // 组织树初始化
        initOrganizationId : function() {
            t.initOrganizationTree();
            var organizationIdObj = $("#selectOrganizationId");
            organizationIdObj.val('');
            organizationIdObj.attr("ids", '');
            t.searchSelectUsers();
        },
        editPartyWorkerDialog : function(id) {
            Ajax.call({
                url : 'uam/getPartyWorkerById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data.item) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/party-worker/party-worker-edit-bill.html', data.item));
                        $("#party-worker-dialog").modal({});
                    } else {
                        Notify.error("获取编辑信息失败，请稍后再试。");
                    }
                }
            });
        },
        addPartyWorker : function() {
            var selectedUsers = $("#select-users-table").bootstrapTable('getSelections');
            if (selectedUsers.length <= 0) {
                Notify.notice("请先选择需要授予党务工作者的用户。");
                return;
            }
            var partyWorkerName = $("#select-users-dialog #partyWorkerName").val();
            if (CheckInputUtils.isEmpty(partyWorkerName)) {
                Notify.notice("请输入角色名称");
                $("#select-users-dialog #partyWorkerName").focus();
                return;
            }
            var ccpartyFlag = $("input[name='ccparty']:checked").val();
            var sysUserId = Global.userId;
            if(ccpartyFlag==1){
                var ccpartyId = $("#partyworkCcparty").attr("ids");
            	if(CheckInputUtils.isEmpty(ccpartyId)){
            		Notify.notice("请选择需要归属的党组织。");
            		return;
            	}
            	sysUserId = ccpartyId;  //个组织的系统用户ID即是组织ID左右直接赋值
            }
            Ajax.call({
                url : 'uam/addPartyWorker',
                p : {
                    userId : selectedUsers[0].id,
                    partyWorkerName : partyWorkerName,
                    sysUserId : sysUserId
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success("保存成功");
                        $("#select-users-dialog").modal('toggle');
                        $("#user-work-table").bootstrapTable('refresh');
                    } else {
                        if(data.msg){
                            Notify.error(data.msg);
                        }else{
                            Notify.error("保存失败");
                        }
                    }
                }
            });
        },
        updatePartyWorker : function() {
            var id = $("#party-worker-dialog #id").val();
            var name = $("#party-worker-dialog #name").val();
            if (CheckInputUtils.isEmpty(name)) {
                Notify.notice("请输入显示名称");
                $("#party-worker-dialog #name").focus();
                return;
            }
            // 传递参数
            var paramters = {
                id : id,
                name : name
            };
            Ajax.call({
                url : 'uam/updatePartyWorker',
                p : {
                    paramters : "{'data':" + JSON.stringify(paramters, "data") + "}"
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success("保存成功");
                        $("#party-worker-dialog").modal('toggle');
                        $("#user-work-table").bootstrapTable('refresh');
                    } else {
                        Notify.error("保存失败");
                    }
                }
            });
        },
        // 删除
        deletePartyWorker : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'uam/deleteUser',
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("已成功删除。");
                                    $("#user-work-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error("删除失败，请稍后再试。");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 角色选择
        changeRole : function(roleId, userId) {
            var isChecked = $("#" + roleId).is(':checked') == true ? 1 : 0;
            Ajax.call({
                url : 'uam/updatePartyWorkerRole',
                p : {
                    userId : userId,
                    roleId : roleId,
                    isChecked : isChecked
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success(data.msg);
                    } else {
                        Notify.error("权限保存失败。");
                    }
                }
            });
        },
        //radio选中
        ccpartyRadioChanger:function(value){
            if(value==0){
                $("#select-users-dialog #partyworkCcparty").hide();
            }else if(value==1){
            	//渲染组织树
            	t.initPartyWorkCcpartySubjectTree();
                $("#select-users-dialog #partyworkCcparty").show();
            }
        },
        typeName : function(value, row) {
            return Global.getEnumName('com.type.' + value);
        },
        statusFormatter : function(value, row) {
            return Global.getEnumName('com.status.' + value);
        },
        // 用户性别
        genderFormatter : function(value, row) {
            return Global.getCodeName('A0107.' + value);
        },
        // 用户政治面貌
        userTypeFormatter : function(value, row) {
            return Global.getCodeName(value);
        },
        // 党组织
        commonPartyNameFormatter : function(value, row) {
            if (value != null) {
                return value.name;
            }
        },
        // 关联用户
        parentUserFormatter : function(value, row) {
            if (value != null) {
                return value.name;
            }
        },
        detailFormatter : function(index, row) {
            var html = "";
            html += "<label>角色分配：</label></br>";
            html += "<div style='margin-left:40px;'>";
            Ajax.call({
                url : 'uam/getUserRoles',
                p : {
                    id : row.id
                },
                async : false, // false同步 true异步
                f : function(data) {
                    if (data.rows) {
                        for (var i = 0; i < data.rows.length; i++) {
                            if (data.rows[i].isHave) {
                                html += "<input type='checkbox' id='" + data.rows[i].role.id + "' checked='checked' onchange='ManagerPartyWorker.changeRole(\"" + data.rows[i].role.id + "\",\""
                                        + row.id + "\")'/><label for='" + data.rows[i].role.id + "' style='margin:0 30px 0 5px;'>" + data.rows[i].role.name + "</label>";
                            } else {
                                html += "<input type='checkbox' id='" + data.rows[i].role.id + "' onchange='ManagerPartyWorker.changeRole(\"" + data.rows[i].role.id + "\",\"" + row.id
                                        + "\")'/><label for='" + data.rows[i].role.id + "' style='margin:0 30px 0 5px;'>" + data.rows[i].role.name + "</label>";
                            }
                        }
                    }
                }
            });
            html += "</div>";
            return html;
        },
        operator : function(value, row) {
            var html = "";
            html += '<a href="javascript:ManagerPartyWorker.editPartyWorkerDialog(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
            html += '<a href="javascript:ManagerPartyWorker.deletePartyWorker(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a>'
            return html;
        }
    }
    return t;
}();