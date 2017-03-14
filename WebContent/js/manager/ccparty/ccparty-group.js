/**
 * @description 党小组设置
 * @author 易文俊
 * @since 2016-08-01
 */
var CcpartyGroup = function() {
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
        render : function() {
            $("#ccparty-group-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : CcpartyNavigation.ccpartyId,
                    })
                    return params;
                }
            });
        },
        // 打开新增党小组对话框
        addCcpartyGroupDialog : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/ccparty/ccparty-group/ccparty-group-add.html'));
            $("#ccparty-group-dialog").modal({});
        },
        // 新增党小组
        addCcpartyGroup : function() {
            var ccpartyId = CcpartyNavigation.ccpartyId;
            var name = CheckInputUtils.splitSpaceForStrAround($("#ccparty-group-dialog #name").val());
            var description = $(" #ccparty-group-dialog #description").val();
            var sequence = $("#ccparty-group-dialog #sequence").val();
            if (CheckInputUtils.isEmpty(name)) {
                Notify.notice("请输入小组名称");
                $("#name").focus();
                return;
            }
            Ajax.call({
                url : "org/addCcpartyGroup",
                p : {
                    ccpartyId : ccpartyId,
                    name : name,
                    description : description,
                    sequence : sequence
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success(data.msg);
                        $("#ccparty-group-dialog").modal('toggle');
                        $("#ccparty-group-table").bootstrapTable('refresh');
                    } else {
                        Notify.error(data.msg);
                    }
                }
            });
        },
        // 打开修改党小组对话框
        updateCcpartyGroupDialog : function(id) {
            Ajax.call({
                url : "org/getCcpartyGroupById",
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/ccparty/ccparty-group/ccparty-group-edit.html',data.item));
                        $("#ccparty-group-dialog").modal({});
                    }
                }
            });
        },
        // 编辑党小组
        updateCcpartyGroup : function() {
            var name = CheckInputUtils.splitSpaceForStrAround($("#ccparty-group-dialog #name").val());
            var id = $(" #ccparty-group-dialog #id").val();
            var description = $(" #ccparty-group-dialog #description").val();
            var sequence = $("#ccparty-group-dialog #sequence").val();
            if (CheckInputUtils.isEmpty(name)) {
                Notify.notice("请输入小组名称");
                $("#name").focus();
                return;
            }
            Ajax.call({
                url : "org/updateCcpartyGroup",
                p : {
                    id : id,
                    name : name,
                    description : description,
                    sequence : sequence
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success(data.msg);
                        $("#ccparty-group-dialog").modal('toggle');
                        $("#ccparty-group-table").bootstrapTable('refresh');
                    } else {
                        Notify.error(data.msg);
                    }
                }
            });

        },
        // 删除党小组
        deleteCcpartyGroup : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "org/deleteCcpartyGroup",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success(data.msg);
                                    $("#ccparty-group-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });
                    }
                }
            });
        },
        operater : function(value, row) {
            var html = '<a class="btn btn-default btn-xs" href="javascript:CcpartyGroup.updateCcpartyGroupDialog(\'' + value + '\')"><i class="fa fa-edit"></i>编辑</a>';
            html += '<a class="btn btn-default btn-xs" href="javascript:CcpartyGroup.deleteCcpartyGroup(\'' + value + '\')"><i class="fa fa-trash-o"></i>删除</a>';
            return html;
        }
    }
    return t;
}();