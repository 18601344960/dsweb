/**
 * @description 品牌管理
 * @author yiwenjun
 * @since 2015-09-01
 */
var WorkBrand = function() {
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
            $(document).on("click", "#new-brand-btn", function() {
                t.newWorkBrand();
            });
            $(document).on("click", "#add-brand-btn", function() {
                t.addWorkBrand();
            });

            $(document).on("click", "#update-brand-btn", function() {
                t.updateWorkBrand();
            });
        },
        render : function($mainContainer) {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/workbrand/work-brand-list.html'));
            $("#brand-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId
                    })
                    return params;
                }
            });
        },
        newWorkBrand : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/workbrand/brand-add-bill.html'));
            $("#brand-add-dialog").modal({});
            $('#brand-add-dialog').on('hidden.bs.modal', function(e) {
                ComEditor.destroy();
                ComFile.destroy();
            })
            ComEditor.initEditor();
            ComFile.initUploadify();
        },
        addWorkBrand : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认新增工作品牌？",
                callback : function(result) {
                    if (result) {
                        var name = $("#brand-add-dialog #name").val();
                        var content = ComEditor.getContent();
                        var files = ComFile.getUploadFiles();
                        if (!name || name == '') {
                            Notify.notice("标题不能为空");
                            return;
                        }
                        Ajax.call({
                            url : 'zbsc/addWorkBrand',
                            p : {
                                name : name,
                                ccpartyId : Global.ccpartyId,
                                content : content,
                                files : JSON.stringify(files)
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("保存成功");
                                    $("#brand-add-dialog").modal('toggle');
                                    $("#brand-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        editWorkBrand : function(id) {
            Ajax.call({
                url : 'zbsc/getWorkBrandById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/workbrand/brand-edit-bill.html', data.item));
                        $("#brand-edit-dialog").modal({});
                        $('#brand-edit-dialog').on('hidden.bs.modal', function(e) {
                            ComEditor.destroy();
                            ComFile.destroy();
                        })
                        ComEditor.initEditor();
                        ComFile.initUploadify();
                    }
                }
            });
        },
        updateWorkBrand : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认修改工作品牌？",
                callback : function(result) {
                    if (result) {
                        var id = $("#brand-edit-dialog #id").val();
                        var name = $("#brand-edit-dialog #name").val();
                        var content = ComEditor.getContent();
                        var files = ComFile.getUploadFiles();
                        if (!name || name == '') {
                            Notify.notice("标题不能为空");
                            return;
                        }
                        Ajax.call({
                            url : 'zbsc/updateWorkBrand',
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
                                    $("#brand-edit-dialog").modal('toggle');
                                    $("#brand-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        deleteWorkBrand : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除工作品牌？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "zbsc/deleteWorkBrand",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("删除成功");
                                    $("#brand-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("删除失败");
                                }
                            }
                        });
                    }
                }
            });

        },
        createUserFormatter : function(value, row) {
            if (!value) {
                return "";
            }
            return value.name;
        },
        operator : function(value, row) {
            var html = "";
            html += '<a href="javascript:WorkBrand.editWorkBrand(\'' + row.id + '\')" class="btn btn-default btn-xs operate-btn"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
            html += '<a href="javascript:WorkBrand.deleteWorkBrand(\'' + row.id + '\')" class="btn btn-default btn-xs operate-btn"><i class="glyphicon glyphicon-trash"></i>删除</a>';
            html += '<a href="work-brand-view?id=' + row.id + '" target="_blank" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-eye-open"></i>查看</a>'
            return html;
        }
    }
    return t;
}();