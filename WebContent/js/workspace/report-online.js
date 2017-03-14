/**
 * @description 在线上报数据
 * @author yiwenjun
 * @since 2015-09-02
 */
var ReportOnline = function() {
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
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/report/report-online-container.html'));
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
        onlineReport : function() {
            var beginTime = $("#online-report-container #beginTime").val();
            var endTime = $("#online-report-container #endTime").val();
            var categoryIds = $("#online-report-container #selectedCategories").attr("ids");
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
                    message : "未选择标签，确认上报选择时间段内的所有文章？",
                    callback : function(result) {
                        if (result) {
                            Notify.success('建设中，近期期待...');
                        }
                    }
                });
            } else {
                bootbox.confirm({
                    size : 'small',
                    message : "确认在线上报数据？",
                    callback : function(result) {
                        if (result) {
                            Notify.success('建设中，近期期待...');
                        }
                    }
                });
            }
        }
    }
    return t;
}();