/**
 * @description 导出上报数据
 * @author yiwenjun
 * @since 2015-09-02
 */
var ReportExport = function() {
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
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/report/report-export-container.html'));
            t.initCategoryTree();
        },
        initCategoryTree : function() {
            var setting = {
                check : {
                    enable : true,
                    chkStyle : "checkbox",
                    radioType : "all",
                    chkboxType : {
                        "Y" : "s",
                        "N" : "s"
                    }
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
                    onCheck : t.treeCheck
                }
            };
            Ajax.call({
                url : 'obt/getConferenceLabelZTree',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data && data.rows) {
                        $.fn.zTree.init($("#category-tree"), setting, data.rows);
                    } else {
                        Notify.error("获栏目失败");

                    }
                }
            });
        },
        // 选中节点事件
        treeCheck : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("category-tree");
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
            var selectedCategories = $("#selectedCategories");
            selectedCategories.val(names);
            selectedCategories.attr("ids", ids);
        },
        exportReport : function() {
            var beginTime = $("#export-report-container #beginTime").val();
            var endTime = $("#export-report-container #endTime").val();
            var categoryIds = $("#selectedCategories").attr("ids");
            if (!beginTime || beginTime.trim() == "") {
                Notify.notice('开始时间不能为空');
                return;
            }
            if (!endTime || endTime.trim() == "") {
                Notify.notice('结束时间不能为空');
                return;
            }
            if (!categoryIds || categoryIds.trim() == "") {
                bootbox.confirm({
                    size : 'small',
                    message : "未选择标签，确认导出选择时间段内的所有文章？",
                    callback : function(result) {
                        if (result) {
                            Ajax.call({
                                url : "zbsc/reportExport",
                                p : {
                                    ccpartyId : Global.ccpartyId,
                                    categoryIds : categoryIds,
                                    beginTime : beginTime,
                                    endTime : endTime
                                },
                                f : function(data) {
                                    if (data.success == true) {
                                        window.location.href = data.zipPath;
                                    } else {
                                        Notify.error('导出失败');
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                bootbox.confirm({
                    size : 'small',
                    message : "确认导出上报数据？",
                    callback : function(result) {
                        if (result) {
                            Ajax.call({
                                url : "zbsc/reportExport",
                                p : {
                                    ccpartyId : Global.ccpartyId,
                                    categoryIds : categoryIds,
                                    beginTime : beginTime,
                                    endTime : endTime
                                },
                                f : function(data) {
                                    if (data.success == true) {
                                        window.location.href = data.zipPath;
                                    } else {
                                        Notify.error('导出失败');
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }
    return t;
}();