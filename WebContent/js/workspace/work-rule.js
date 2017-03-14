/**
 * @description 管理工作制度
 * @author yiwenjun
 * @since 2015-09-02
 */
var WorkRule = function() {
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
            $(document).on("click", "#new-work-rule-btn", function() {
                t.newWorkRule();
            });
            $(document).on("click", "#add-work-rule-btn", function() {
                t.addWorkRule();
            });

            $(document).on("click", "#update-work-rule-btn", function() {
                t.updateWorkRule();
            });
        },
        render : function($mainContainer) {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/workrule/work-rule-list.html'));
            $("#work-rule-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        category : 1
                    })
                    return params;
                }
            });
        },
        newWorkRule : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/workrule/work-rule-add-bill.html', Global));
            $("#work-rule-add-dialog").modal({});
            $('#work-rule-add-dialog').on('hidden.bs.modal', function(e) {
                ComEditor.destroy();
                ComFile.destroy();
            })
            ComEditor.initEditor();
            ComFile.initUploadify();
        },
        addWorkRule : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认新增工作制度？",
                callback : function(result) {
                    if (result) {
                        var name = $("#work-rule-add-dialog #name").val();
                        var type = $("#work-rule-add-dialog #type").val();
                        var content = ComEditor.getContent();
                        var files = ComFile.getUploadFiles();
                        if (!name || name == '') {
                            Notify.notice("标题不能为空");
                            return;
                        }
                        Ajax.call({
                            url : 'com/addInformation',
                            p : {
                                category : 1,
                                type : type ? 0 : 1,
                                name : name,
                                ccpartyId : Global.ccpartyId,
                                content : content,
                                files : JSON.stringify(files)
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("保存成功");
                                    $("#work-rule-add-dialog").modal('toggle');
                                    $("#work-rule-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        editWorkRule : function(id) {
            Ajax.call({
                url : 'com/getInformationById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/workrule/work-rule-edit-bill.html', data.item));
                        $("#work-rule-edit-dialog").modal({});
                        $("#work-rule-edit-dialog #type").val(data.item.type);
                        $('#work-rule-edit-dialog').on('hidden.bs.modal', function(e) {
                            ComEditor.destroy();
                            ComFile.destroy();
                        })
                        ComEditor.initEditor();
                        ComFile.initUploadify();
                    }
                }
            });
        },
        updateWorkRule : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认修改工作制度？",
                callback : function(result) {
                    if (result) {
                        var id = $("#work-rule-edit-dialog #id").val();
                        var name = $("#work-rule-edit-dialog #name").val();
                        var type = $("#work-rule-edit-dialog #type").val();
                        var content = ComEditor.getContent();
                        var files = ComFile.getUploadFiles();
                        if (!name || name == '') {
                            Notify.notice("标题不能为空");
                            return;
                        }
                        Ajax.call({
                            url : 'com/updateInformation',
                            p : {
                                id : id,
                                name : name,
                                type : type ? 0 : 1,
                                ccpartyId : Global.ccpartyId,
                                content : content,
                                files : JSON.stringify(files)
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("保存成功");
                                    $("#work-rule-edit-dialog").modal('toggle');
                                    $("#work-rule-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        deleteWorkRule : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除工作制度？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "com/deleteInformation",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("删除成功");
                                    $("#work-rule-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("删除失败");
                                }
                            }
                        });
                    }
                }
            });

        },
        //发布工作制度
        publishInformation : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认发布工作制度？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "com/publishInformation",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("发布成功");
                                    $("#work-rule-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("发布失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        //取消发布工作制度
        unpublishInformation : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认取消发布？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "com/unpublishInformation",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("取消发布成功");
                                    $("#work-rule-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("取消发布失败");
                                }
                            }
                        });
                    }
                }
            });
        },

        typeFormatter : function(value, row) {
            return Global.getEnumName('com_information.type.' + value);
        },
        createUserFormatter : function(value, row) {
            if (!value) {
                return "";
            }
            return value.name;
        },
        nameFormatter : function(value, row) {
            var html = '<a href="information-view?id=' + row.id + '" target="_blank" class="bootstrap-title">' + value + '</a>'
            return html;
        },
        publishOperate : function(value, row) {
            var html = '';
            if (value == 1) {
                html += '<a href="javascript:WorkRule.unpublishInformation(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="fa fa-undo"></i>取消发布</a>';
            } else {
                html += '<a href="javascript:WorkRule.publishInformation(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-cloud"></i>发布</a>';
            }
            return html;
        },
        operator : function(value, row) {
            var html = '';
            html += '<a href="javascript:WorkRule.editWorkRule(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
            html += '<a href="javascript:WorkRule.deleteWorkRule(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a>'
            return html;
        }
    }
    return t;
}();