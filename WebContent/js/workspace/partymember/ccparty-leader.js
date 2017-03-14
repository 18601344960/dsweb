/**
 * @description 党组织领导班子
 * @author 赵子靖
 * @since 2016-01-13
 */
var CcpartyLeader = function() {
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
            $(document).on("show.bs.tab", '#ccparty-member-container a[data-toggle="tab"]', function(e) {
                var tabId = $(e.target).attr('href');
                if (tabId == "#ccparty-info-tab") {
                } else if (tabId == "#search-ccparty-member-tab") {
                    CcpartyLeader.renderSearchCcpartyMember();
                }
            });
        },
        render : function() {
            Global.initLoader(t.mainContainer);
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/ccparty-leader.html'));
            Global.initLoader("#load-ccparty-infos");
            Global.initLoader("#load-ccparty-leaders");
            // 加载统计信息党组织、党员数
            Ajax.call({
                url : 'org/statisticsCcpartyInfosForPartymemberManager',
                p : {
                    ccpartyId : PartyMember.ccpartyId
                },
                f : function(data) {
                    if (data && data.item) {
                        $("#load-ccparty-infos").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/load-ccparty-infos.html', data.item));
                    } else {
                        Notify.error("加载人员列表失败");
                    }
                }
            });
            // 加载党组织领导班子成员
            Ajax.call({
                url : 'obt/getLastElectionMemberListByCcparty',
                p : {
                    ccpartyId : PartyMember.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        if (data.rows != null) {
                            $.extend(data, Global);
                            $("#load-ccparty-leaders").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/load-ccparty-leaders.html', data));
                        } else {
                            $("#load-ccparty-leaders").html("暂无换届选举信息。");
                        }
                    } else {
                        $("#load-ccparty-leaders").html("加载失败，可能是暂无换届信息。");
                    }
                }
            });
        },
        // 领导信息预览
        showPartyMemberInfoDialog : function(userId) {
            if (userId != null && userId != '') {
                Ajax.call({
                    url : "uam/loadUserInfoByUserId",
                    p : {
                        userId : userId
                    },
                    f : function(data) {
                        if (data && data.item) {
                            $.extend(data, Global);
                            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/view-party-member-dialog.html', data));
                            $("#view-party-member-dialog").modal({});
                        } else {
                            Notify.error("获取信息失败。");
                        }
                    }
                });
            } else {
                Notify.info("系统外人员，暂无简介。");
            }
        },
        //换届选举信息
        showElectionInfo : function(divId) {
            Global.initLoader("#" + divId);
            // 加载党组织领导班子成员
            Ajax.call({
                url : 'obt/getLastElectionMemberListByCcparty',
                p : {
                    ccpartyId : PartyMember.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        if (data.rows != null) {
                            $.extend(data, Global);
                            $("#" + divId).html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/load-ccparty-leaders.html', data));
                        } else {
                            $("#" + divId).html("暂无换届选举信息。");
                        }
                    } else {
                        $("#" + divId).html("加载失败，可能是暂无换届信息。");
                    }
                }
            });
        },
        // 移入
        mouseOve : function($this) {
            $this.className = "party-leader-info-user-out";
        },
        // 移出
        mouseOut : function($this) {
            $this.className = "party-leader-info-user";
        },
        renderSearchCcpartyMember : function() {
            $("#search-ccparty-member-tab").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/search-ccparty-member.html'));
            t.searchCcpartyMember();
        },
        searchCcpartyMember : function() {
            var search = $("#search-name").val();
            $("#search-ccparty-member-table").bootstrapTable("destroy");
            $("#search-ccparty-member-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : PartyMember.ccpartyId,
                        search : search
                    })
                    return params;
                }
            });
        },
        ccpartyFormatter : function(value, row) {
            if (value != null) {
                return value.ccparty.name;
            }
        },
        genderFormatter : function(value, row) {
            if (value) {
                return Global.getCodeName('A0107.' + value);
            }
        },
        nationFormatter : function(value, row) {
            if (value) {
                return Global.getCodeName('A0121.' + value);
            }
        },
        birthPlaceFormatter : function(value, row) {
            if (value) {
                return Global.getCodeName('A0114.' + value);
            }
        },
        birthDayFormatter : function(value, row) {
            if (value) {
                return Global.getDate(value);
            }
        }

    }
    return t;
}();