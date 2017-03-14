/**
 * @description 管理工作要求
 * @author yiwenjun
 * @since 2015-09-02
 */
var WorkRequirement = function() {
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
            $(document).on("click", "#new-work-requirement-btn", function() {
                t.newWorkRequirement();
            });
            $(document).on("click", "#add-work-requirement-btn", function() {
                t.addWorkRequirement();
            });

            $(document).on("click", "#update-work-requirement-btn", function() {
                t.updateWorkRequirement();
            });
        },
        render : function($mainContainer) {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/workrequirement/work-requirement-list.html'));
            $("#work-requirement-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        category : 2
                    })
                    return params;
                }
            });
        },
        newWorkRequirement : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/workrequirement/work-requirement-add-bill.html', Global));
            $("#work-requirement-add-dialog").modal({});
            $('#work-requirement-add-dialog').on('hidden.bs.modal', function(e) {
                ComEditor.destroy();
                ComFile.destroy();
            })
            ComEditor.initEditor();
            ComFile.initUploadify();
        },
        addWorkRequirement : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认新增工作要求？",
                callback : function(result) {
                    if (result) {
                        var name = $("#work-requirement-add-dialog #name").val();
                        var type = $("#work-requirement-add-dialog #type").val();
                        var content = ComEditor.getContent();
                        var files = ComFile.getUploadFiles();
                        if (!name || name == '') {
                            Notify.notice("标题不能为空");
                            return;
                        }
                        Ajax.call({
                            url : 'com/addInformation',
                            p : {
                                category : 2,
                                name : name,
                                type : type ? 0 : 1,
                                ccpartyId : Global.ccpartyId,
                                content : content,
                                files : JSON.stringify(files)
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("保存成功");
                                    $("#work-requirement-add-dialog").modal('toggle');
                                    $("#work-requirement-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        editWorkRequirement : function(id) {
            Ajax.call({
                url : 'com/getInformationById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/workrequirement/work-requirement-edit-bill.html', data.item));
                        $("#work-requirement-edit-dialog").modal({});
                        $("#work-requirement-edit-dialog #type").val(data.item.type);
                        $('#work-requirement-edit-dialog').on('hidden.bs.modal', function(e) {
                            ComEditor.destroy();
                            ComFile.destroy();
                        })
                        ComEditor.initEditor();
                        ComFile.initUploadify();
                    }
                }
            });
        },
        updateWorkRequirement : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认修改工作要求？",
                callback : function(result) {
                    if (result) {
                        var id = $("#work-requirement-edit-dialog #id").val();
                        var name = $("#work-requirement-edit-dialog #name").val();
                        var type = $("#work-requirement-edit-dialog #type").val();
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
                                    $("#work-requirement-edit-dialog").modal('toggle');
                                    $("#work-requirement-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        deleteWorkRequirement : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除工作要求？",
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
                                    $("#work-requirement-table").bootstrapTable("refresh");
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
            return Global.getEnumName('com_information.type.' + value);
        },
        nameFormatter : function(value, row) {
            var html = '<a href="information-view?id=' + row.id + '" target="_blank" class="bootstrap-title">' + value + '</a>'
            return html;
        },
        createUserFormatter : function(value, row) {
            if (!value) {
                return "";
            }
            return value.name;
        },
        publishOperate : function(value, row) {
            var html = '';
            if (value == 1) {
                html += '<a href="javascript:WorkRequirement.unpublishInformation(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="fa fa-undo"></i>取消发布</a>';
            } else {
                html += '<a href="javascript:WorkRequirement.publishInformation(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-cloud"></i>发布</a>';
            }
            return html;
        },
        //发布工作要求
        publishInformation : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认发布工作要求？",
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
                                    $("#work-requirement-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("发布失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        //取消发布工作要求
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
                                    $("#work-requirement-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("取消发布失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        operator : function(value, row) {
            var html = '';
            html += '<a href="javascript:WorkRequirement.editWorkRequirement(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
            html += '<a href="javascript:WorkRequirement.deleteWorkRequirement(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a>'
            return html;
        }
    }
    return t;
}();