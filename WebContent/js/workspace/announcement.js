/**
 * @description 通知通告
 * @author yiwenjun
 * @since 2015-09-02
 */
var Announcement = function() {
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
            $(document).on("click", "#new-announcement-btn", function() {
                t.newAnnouncement();
            });
            $(document).on("click", "#add-announcement-btn", function() {
                t.addAnnouncement();
            });

            $(document).on("click", "#update-announcement-btn", function() {
                t.updateAnnouncement();
            });
        },
        render : function($mainContainer) {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/announcement/announcement-list.html'));
            $("#announcement-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        category : 0
                    })
                    return params;
                }
            });
        },
        newAnnouncement : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/announcement/announcement-add-bill.html'));
            $("#announcement-dialog").modal({});
            $('#announcement-dialog').on('hidden.bs.modal', function(e) {
                ComEditor.destroy();
                ComFile.destroy();
            })
            ComEditor.initEditor();
            ComFile.initUploadify();
        },
        addAnnouncement : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认新增通知？",
                callback : function(result) {
                    if (result) {
                        var name = $("#announcement-dialog #name").val();
                        var content = ComEditor.getContent();
                        var files = ComFile.getUploadFiles();
                        if (!name || name == '') {
                            Notify.notice("标题不能为空");
                            return;
                        }
                        Ajax.call({
                            url : 'com/addAnnouncement',
                            p : {
                                name : name,
                                ccpartyId : Global.ccpartyId,
                                content : content,
                                files : JSON.stringify(files)
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("保存成功");
                                    $("#announcement-dialog").modal('toggle');
                                    $("#announcement-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        editAnnouncement : function(id) {
            Ajax.call({
                url : 'com/getAnnouncementById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/announcement/announcement-edit-bill.html', data.item));
                        $("#announcement-dialog").modal({});
                        $('#announcement-dialog').on('hidden.bs.modal', function(e) {
                            ComEditor.destroy();
                            ComFile.destroy();
                        })
                        ComEditor.initEditor();
                        ComFile.initUploadify();
                    }
                }
            });
        },
        updateAnnouncement : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认修改通知？",
                callback : function(result) {
                    if (result) {
                        var id = $("#announcement-dialog #id").val();
                        var name = $("#announcement-dialog #name").val();
                        var content = ComEditor.getContent();
                        var files = ComFile.getUploadFiles();
                        if (!name || name == '') {
                            Notify.notice("标题不能为空");
                            return;
                        }
                        Ajax.call({
                            url : 'com/updateAnnouncement',
                            p : {
                                id : id,
                                name : name,
                                ccpartyId : Global.ccpartyId,
                                content : content,
                                files : JSON.stringify(files)
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("保存成功");
                                    $("#announcement-dialog").modal('toggle');
                                    $("#announcement-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        deleteAnnouncement : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除通知？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "com/deleteAnnouncement",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("删除成功");
                                    $("#announcement-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("删除失败");
                                }
                            }
                        });
                    }
                }
            });

        },
        // 发布通知
        publishAnnouncement : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认发布通知？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "com/publishAnnouncement",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("发布成功");
                                    $("#announcement-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("发布失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 取消发布通知
        unpublishAnnouncement : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认发布通知？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "com/unpublishAnnouncement",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("取消成功");
                                    $("#announcement-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("取消发布失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        nameFormatter : function(value, row) {
            var html = '<a href="announcement-view?id=' + row.id + '" target="_blank" class="bootstrap-title">'+value+'</a>'
            return html;
        },
        statusFormatter : function(value, row) {
            return Global.getEnumName('com_announcement.status.' + value);
        },
        createUserFormatter : function(value, row) {
            if (!value) {
                return "";
            }
            return value.name;
        },
        publishStatus : function(value, row) {
            var html = '';
            if (value == 1) {
                html += '<a href="javascript:Announcement.unpublishAnnouncement(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="fa fa-undo"></i>取消发布</a>';
            } else {
                html += '<a href="javascript:Announcement.publishAnnouncement(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-cloud"></i>发布</a>';
            }
            return html;
        },
        operator : function(value, row) {
            var html = '';
            if (row.status != 1) {
                html += '<a href="javascript:Announcement.editAnnouncement(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
                html += '<a href="javascript:Announcement.deleteAnnouncement(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a>';
            }
            return html;
        }
    }
    return t;
}();