/**
 * @description 换届选举监控
 * @author yiwenjun
 * @since 2016-07-05
 */
var ElectionMonitor = function() {
    var t = {
        mainContainer : '',
        summaryDiv : '',
        detailDiv : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            if (Global.ccpartyType == Global.ccpartyBranch) {
                t.detailDiv = t.mainContainer;
            } else {
                t.detailDiv = "#election-info-tab";
                t.summaryDiv = '#main-detail-right';
            }
            t.initView();
            t.initEvent();
        },
        initView : function() {
        },
        initEvent : function() {
            $(document).on("show.bs.tab", '#election-container a[data-toggle="tab"]', function(e) {
                var tabId = $(e.target).attr('href');
                if (tabId == "#low-election-info-tab") {
                } else if (tabId == "#election-info-tab") {
                    t.initEelectionDetail();
                }
            });
        },
        render : function() {
            $(t.mainContainer).data("ccpartyId", Global.ccpartyId);
            if (Global.ccpartyType == Global.ccpartyBranch) {
                t.initEelectionDetail();
            } else {
                $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-monitor-container.html'));
                t.initCcpartyTree();
                t.initEelectionSummary(Global.ccpartyId);
            }
        },
        // 组织树获取
        initCcpartyTree : function(data) {
            ComTree.initTree($.extend({
                divContainer : "#ccparty-tree",
                url : 'org/getTreeCCPartyAndLowerLevel',
                async : false, // false同步 true异步
                p : {
                    ccpartyId : Global.ccpartyId
                },
                onClick : t.ccpartyClick
            }));
        },
        // 组织树点击事件
        ccpartyClick : function(e, treeId, treeNode) {
            var ccpartyId = treeNode.id;
            $("#child-ccparty-name").html(treeNode.name);
            $(t.mainContainer).data("ccpartyId", ccpartyId);
            if (treeNode.type == Global.ccpartyGroup || treeNode.type == Global.ccpartyCommitte || treeNode.type == Global.ccpartyGeneral) {
                t.detailDiv = "#election-info-tab";
                t.initEelectionSummary(ccpartyId);
            } else {
                t.detailDiv = t.summaryDiv;
                t.initEelectionDetail();
            }
        },
       /* // 点击根组织事件
        clickRoot : function(ccpartyId) {
            t.initEelectionSummary(ccpartyId);
        },
        // 点击下级组织事件
        clickChild : function(ccpartyId) {
            $("#main-detail-right").data("ccpartyId", ccpartyId);
            t.initEelectionDetail();
        },*/
        initEelectionSummary : function(ccpartyId) {
            Global.initLoader(t.summaryDiv);
            $(t.summaryDiv).html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-monitor-summaryl-container.html'));
            $("#election-summary-table").bootstrapTable('destroy');
            $("#election-summary-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : ccpartyId
                    })
                    return params;
                }
            });
        },
        initEelectionDetail : function(electionId, rowNum) {
            Global.initLoader(t.detailDiv);
            var ccpartyId = $(t.mainContainer).data("ccpartyId");
            Ajax.call({
                url : "obt/getCurrentElection",
                p : {
                    ccpartyId : ccpartyId,
                    id : electionId
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data, Global);
                        if (rowNum) {
                            data.rowNum = rowNum;
                        }
                        $(t.detailDiv).html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-monitor-detail-container.html', data));
                        // 领导班子成员列表
                        $("#election-member-table").bootstrapTable('destroy');
                        $("#election-member-table").bootstrapTable({
                            queryParams : function(params) {
                                $.extend(params, {
                                    ccpartyId : Global.ccpartyId,
                                    electionId : data.item.id
                                })
                                return params;
                            }
                        });
                    } else {
                        $(t.detailDiv).html('<div id="election-detail">该组织暂无换届选举信息</div>');
                    }
                }
            });
        },
        // 上一届换届选举
        lastElection : function(id, sequence, rowNum) {
            var ccpartyId = $(t.mainContainer).data("ccpartyId");
            Ajax.call({
                url : "obt/getLastElection",
                p : {
                    ccpartyId : ccpartyId,
                    sequence : sequence
                },
                f : function(data) {
                    if (data) {
                        if (data.item) {
                            t.initEelectionDetail(data.item.id, (Number(rowNum) - 1));
                        } else {
                            Notify.info("没有上一届");
                        }
                    } else {
                        Notify.error("获取失败");
                    }
                }
            });
        },
        // 下一届换届选举
        nextElection : function(id, sequence, rowNum) {
            var ccpartyId = $(t.mainContainer).data("ccpartyId");
            Ajax.call({
                url : "obt/getNextElection",
                p : {
                    ccpartyId : ccpartyId,
                    sequence : sequence
                },
                f : function(data) {
                    if (data) {
                        if (data.item) {
                            t.initEelectionDetail(data.item.id, (Number(rowNum) + 1));
                        } else {
                            Notify.info("没有下一届");
                        }
                    } else {
                        Notify.error("获取失败");
                    }
                }
            });
        },
        ccpartyFormatter : function(value, row) {
            if (value != null) {
                return value.name;
            }
        },
        sequenceFormatter : function(value, row) {
            if (row.id) {
                return value;
            }
        },
        selectModeFormatter : function(value, row) {
            if (row.id) {
                return Global.getEnumName('obt_election.selectMode.' + value);
            }
        },
        participantsFormatter : function(value, row) {
            if (row.id) {
                return value;
            }
        },
        attendanceFormatter : function(value, row) {
            if (row.id) {
                return value;
            }
        },
        ageLimitFormatter : function(value, row) {
            if (row.id) {
                return value;
            }
        },
        dateFormatter : function(value, row) {
            if (row.id) {
                if (value) {
                    return Global.getDate(value);
                }
            }
        },
        remarkFormatter : function(value, row) {
            var status = row.status;
            var html = "";
            if (status == -1) {
                html = '<div style="color:#666;font-weight:bold;">' + value + '</div>';
            } else if (status == 0) {
                html = '<div style="color:#67F52C;font-weight:bold;">' + value + '</div>';
            } else if (status == 1) {
                html = '<div style="color:red;font-weight:bold;">' + value + '</div>';
            } else if (status == 2) {
                html = '<div style="color:#D16A3B;font-weight:bold;">' + value + '</div>';
            } else if (status == 3) {
                html = '<div style="color:#D16A3B;font-weight:bold;">' + value + '</div>';
            }
            return html;

        }
    }
    return t;
}();