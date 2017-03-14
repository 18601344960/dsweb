/**
 * @description 统计分析
 * @author 赵子靖
 * @since 2015-04-07
 */
var Statistics = function() {
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
            $(document).on("show.bs.tab", '#conference-tab a[data-toggle="tab"]', function(e) {
                var tabId = $(e.target).attr('href');
                if (tabId == "#step") {
                    t.renderStep();
                } else if (tabId == "#format") {
                    t.renderFormat();
                } else if (tabId == "#label") {
                    t.renderLabel();
                } else if (tabId == "#ccparty-contrast") {
                    t.renderCcpartyContrast();
                }
            });
            // 选择统计组织
            $(document).on("click", "#queryCcpartyId", function() {
                t.initCcpartySubjectTree();
                $("#menuContent").slideDown("fast");
                $("body").bind("mousedown", t.onCcpartyBodyDown);
            });
        },
        render : function() {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/statistics/conference-statistics.html'));
            t.renderStep();
        },
        // 党组织树 平级和下级
        initCcpartySubjectTree : function() {
            ComTree.initTree({
                divContainer : "#ccparty-tree",
                url : 'org/getTreeCCPartyOneselfAndEqualLevelAndSon',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                onClick : t.ccpartyClickCallBack
            });
        },
        ccpartyClickCallBack : function(e, treeId, treeNode) {
            $("#queryCcpartyId").attr("ids", treeNode.id);
            $("#queryCcpartyId").val(treeNode.name);
            $("#menuContent").fadeOut("fast");
            $("body").unbind("mousedown", t.onBodyDown);
            t.searchActivity();
        },
        // 鼠标失去焦点
        onCcpartyBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
                $("#menuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        // 工作步骤统计加载
        renderStep : function() {
            var beginDate = $("#step-table-buttons #beginDate").val();
            var endDate = $("#step-table-buttons #endDate").val();
            $("#step-table").bootstrapTable("destroy");
            $("#step-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        beginDate : beginDate,
                        endDate : endDate
                    })
                    return params;
                }
            });
        },
        // 工作形式统计加载
        renderFormat : function() {
            var beginDate = $("#format-table-buttons #beginDate").val();
            var endDate = $("#format-table-buttons #endDate").val();
            $("#format-table").bootstrapTable("destroy");
            $("#format-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        beginDate : beginDate,
                        endDate : endDate
                    })
                    return params;
                }
            });
        },
        // 工作内容统计加载
        renderLabel : function() {
            var beginDate = $("#label-table-buttons #beginDate").val();
            var endDate = $("#label-table-buttons #endDate").val();
            $("#label-table").bootstrapTable("destroy");
            $("#label-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        beginDate : beginDate,
                        endDate : endDate
                    })
                    return params;
                }
            });
        },
        // 组织工作统计加载
        renderCcpartyContrast : function() {
            var beginDate = $("#contrast-table-buttons #beginDate").val();
            var endDate = $("#contrast-table-buttons #endDate").val();
            $("#ccparty-contrast-table").bootstrapTable("destroy");
            $("#ccparty-contrast-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        beginDate : beginDate,
                        endDate : endDate
                    })
                    return params;
                }
            });
        },
        // 查询事件
        searchActivity : function() {
            Global.initLoader("#statistics-table-container");
            var ccpartyId = $("#queryCcpartyId").attr("ids"); // 所选组织
            var beginTime = $("#beginTime").val();
            var endTime = $("#endTime").val();
            Ajax.call({
                url : "obt/getConferencesViewList",
                p : {
                    ccpartyId : ccpartyId,
                    beginTime : beginTime,
                    endTime : endTime
                },
                f : function(data) {
                    if (data && data.items.length > 0) {
                        $("#statistics-table-container").html(nunjucks.render(Global.appName + '/tpl/workspace/statistics/statistics-table.html', data));
                        $("#statistics-chart-container").html('<div id="main" style="height:400px;clear:both"></div>');
                        t.renderChart(data);
                    } else {
                        $("#statistics-table-container").html("暂无数据");
                        $("#statistics-chart-container").html("");
                    }
                }
            });
        },
        // 统计分析导出
        exportStatistics : function() {
            var ccpartyId = $("#queryCcpartyId").attr('ids'); // 所选组织
            var beginTime = $("#beginTime").val();
            var endTime = $("#endTime").val();
            window.location.href = "/" + Global.appName + "/obt/exportStatistics?beginTime=" + beginTime + "&endTime=" + endTime + "&ccpartyId=" + ccpartyId;
        },
        renderChart : function(data) {
            var items = data.items;
            var categories = [];
            var acticleNum = [];
            var viewNum = [];
            var replayNum = [];
            var filesNum = [];
            for ( var index in items) {
                categories[index] = items[index].categoryName;
                acticleNum[index] = items[index].num;
                viewNum[index] = items[index].hits;
                replayNum[index] = items[index].reply;
                filesNum[index] = items[index].files;
            }
            var myChart = echarts.init(document.getElementById('main'));
            option = {
                title : {
                    text : '图表数据统计',
                    subtext : ''
                },
                tooltip : {
                    trigger : 'axis'
                },
                legend : {
                    data : [ '文章数', '浏览数', '回复数', '附件数' ]
                },
                toolbox : {
                    show : true,
                    feature : {
                        mark : {
                            show : true
                        },
                        dataView : {
                            show : true,
                            readOnly : false
                        },
                        magicType : {
                            show : true,
                            type : [ 'line', 'bar' ]
                        },
                        restore : {
                            show : true
                        },
                        saveAsImage : {
                            show : true
                        }
                    }
                },
                calculable : true,
                xAxis : [ {
                    type : 'category',
                    axisLabel : {
                        rotate : 60
                    },
                    data : categories
                } ],
                yAxis : [ {
                    type : 'value'
                } ],
                series : [ {
                    name : '文章数',
                    type : 'bar',
                    data : acticleNum
                }, {
                    name : '浏览数',
                    type : 'bar',
                    data : viewNum
                }, {
                    name : '回复数',
                    type : 'bar',
                    data : replayNum
                }, {
                    name : '附件数',
                    type : 'bar',
                    data : filesNum
                } ]
            };
            myChart.setOption(option);
        }
    }
    return t;
}();
