/**
 * @description 系统参数
 * @author yiwenjun
 * @since 2015-06-07
 */
var ManagerEnvironment = function() {
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
        render : function($mainContainer) {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/environment-list.html'));
            $("#environment-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {})
                    return params;
                }
            });
        },
        newEnvironment : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/environment-add-bill.html'));
            $("#environment-add-dialog").modal({});
        },
        addEnvironment : function() {
            var id = $("#environment-add-dialog #id").val();
            var value = $("#environment-add-dialog #value").val();
            var type = $("#environment-add-dialog #type").val();
            var description = $("#environment-add-dialog #description").val();
            Ajax.call({
                url : 'sys/getEnvironmentById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item && data.item.id) {
                        Notify.info("已经存在该ID的变量");
                    } else {
                        bootbox.confirm({
                            size : 'small',
                            message : "确认新增环境变量？",
                            callback : function(result) {
                                if (result) {
                                    if (id == null || id == "") {
                                        Notify.info("ID不能为空");
                                        return;
                                    }
                                    Ajax.call({
                                        url : 'sys/addEnvironment',
                                        p : {
                                            id : id,
                                            value : value,
                                            type : type,
                                            description : description
                                        },
                                        f : function(data) {
                                            if (data.success) {
                                                Notify.success("保存成功");
                                                $("#environment-add-dialog").modal('toggle');
                                                $("#environment-table").bootstrapTable("refresh");
                                            } else {
                                                Notify.error("保存失败");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        },
        editEnvironment : function(id) {
            Ajax.call({
                url : 'sys/getEnvironmentById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/environment-edit-bill.html', data.item));
                        $("#environment-edit-dialog").modal({});
                        $("#environment-edit-dialog #type").val(data.item.type)
                    }
                }
            });
        },
        updateEnvironment : function() {
            var id = $("#environment-edit-dialog #id").val();
            var value = $("#environment-edit-dialog #value").val();
            var type = $("#environment-edit-dialog #type").val();
            var description = $("#environment-edit-dialog #description").val();

            bootbox.confirm({
                size : 'small',
                message : "确认修改环境变量？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'sys/updateEnvironment',
                            p : {
                                id : id,
                                value : value,
                                type : type,
                                description : description
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("保存成功");
                                    $("#environment-edit-dialog").modal('toggle');
                                    $("#environment-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        deleteEnvironment : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除环境变量？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "sys/deleteEnvironment",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("删除成功");
                                    $("#environment-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("删除失败");
                                }
                            }
                        });
                    }
                }
            });

        },
        typeFormatter : function(value, row) {
            return Global.getEnumName('sys_environment.type.' + value);
        },
        operator : function(value, row) {
            var html = "";
            html += '<a href="javascript:ManagerEnvironment.editEnvironment(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
            html += '<a href="javascript:ManagerEnvironment.deleteEnvironment(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a>'
            return html;
        }
    }
    return t;
}();