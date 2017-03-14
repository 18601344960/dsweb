/**
 * @description 品牌标签
 * @author yiwenjun
 * @since 2015-09-02
 */
var ConferenceCategory = function() {
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
            $(document).on("click", "#new-category-btn", function() {
                t.newCategory();
            });
            $(document).on("click", "#add-category-btn", function() {
                t.addCategory();
            });

            $(document).on("click", "#update-category-btn", function() {
                t.updateCategory();
            });
        },
        render : function($mainContainer) {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/category/category-list.html'));
            $("#category-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                    })
                    return params;
                }
            });
        },
        newCategory : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/category/category-add-bill.html'));
            $("#category-add-dialog").modal({});
        },
        addCategory : function() {
            var description = $("#description").val();
            bootbox.confirm({
                size : 'small',
                message : "确认新增栏目？",
                callback : function(result) {
                    if (result) {
                        var name = $("#category-add-dialog #name").val();
                        if (!name || name == '') {
                            Notify.notice("栏目名称不能为空");
                            return;
                        }
                        Ajax.call({
                            url : 'obt/addConferenceLabel',
                            p : {
                                parentId : 'C',
                                ccpartyId : Global.ccpartyId,
                                name : name,
                                description : description
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("保存成功");
                                    $("#category-add-dialog").modal('toggle');
                                    $("#category-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        editCategory : function(id) {
            Ajax.call({
                url : 'obt/getConferenceCategoryById',
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/category/category-edit-bill.html', data.item));
                        $("#category-edit-dialog").modal({});
                    }
                }
            });
        },
        updateCategory : function() {
            var description = $("#description").val();
            var orderNo = $("#orderNo").val();
            bootbox.confirm({
                size : 'small',
                message : "确认修改栏目？",
                callback : function(result) {
                    if (result) {
                        var id = $("#category-edit-dialog #id").val();
                        var name = $("#category-edit-dialog #name").val();
                        if (!name || name == '') {
                            Notify.notice("名称不能为空");
                            return;
                        }
                        Ajax.call({
                            url : 'obt/updateConferenceLabel',
                            p : {
                                id : id,
                                name : name,
                                description : description,
                                orderNo : orderNo
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success("保存成功");
                                    $("#category-edit-dialog").modal('toggle');
                                    $("#category-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        deleteCategory : function(id) {
            Ajax.call({
                url : "obt/getConferenceLabelByCategoryId",
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.success) {
                        t.deleteConferenceCategory(id);
                    } else {
                        t.deleteConferenceCategoryAndArtical(id);
                    }
                }
            });
        },
        deleteConferenceCategory : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除栏目？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/deleteConferenceCategory",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("删除成功");
                                    $("#category-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("删除失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        deleteConferenceCategoryAndArtical : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "存在关联，删除后相关文章也会被删除，确认删除栏目？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/deleteConferenceCategoryAndArtical",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("删除成功");
                                    $("#category-table").bootstrapTable("refresh");
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
            return Global.getEnumName('obt_conference_category.type.' + value);
        },
        operator : function(value, row) {
            var html = "";
            if (row.readOnly == 0) {
                html += '<a href="javascript:ConferenceCategory.editCategory(\'' + row.id + '\')" class="btn btn-default btn-xs operate-btn"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
                html += '<a href="javascript:ConferenceCategory.deleteCategory(\'' + row.id + '\')" class="btn btn-default btn-xs operate-btn"><i class="glyphicon glyphicon-trash"></i>删除</a>'
            }
            return html;
        }
    }
    return t;
}();