/**
 * @description 党组织电子活动证
 * @author 赵子靖
 * @since 2016-05-16
 */
var CcpartyCard = function() {
    var t = {
        paramter : new Object({
            ccpartyId : Global.ccpartyId,
            cardConfig : [ 1, 2, 3],
            beginTime : '',
            endTime : '',
            label : '',
            step : '',
            format : '',
            brandType : -1,
            isRecommend : -1,
            cardConfig1 : true,
            cardConfig2 : false,
            cardConfig3 : false,
            year : ''
        }),
        page : new Object(),
        init : function(paramter) {
            if (paramter) {
                t.paramter.ccpartyId = paramter.ccpartyId;
                t.paramter.cardConfig = paramter.cardConfig;
                t.paramter.beginTime = paramter.beginTime;
                t.paramter.endTime = paramter.endTime;
                t.paramter.label = paramter.label;
                t.paramter.step = paramter.step;
                t.paramter.format = paramter.format;
                t.paramter.brandType = paramter.brandType;
                t.paramter.isRecommend = paramter.isRecommend;
            }
            t.year = new Date().getFullYear();
            t.paramter.cardConfig1 = true;
            t.paramter.cardConfig2 = false;
            t.paramter.cardConfig3 = false;
            if (t.paramter.cardConfig) {
                for (var i = 0; i < t.paramter.cardConfig.length; i++) {
                    if (1 == t.paramter.cardConfig[i]) {
                        t.paramter.cardConfig1 = true;
                    } else if (2 == t.paramter.cardConfig[i]) {
                        t.paramter.cardConfig2 = true;
                    } else if (3 == t.paramter.cardConfig[i]) {
                        t.paramter.cardConfig3 = true;
                    }
                }
            }
            t.initView();
            t.initEvent();
        },
        initView : function() {
        },
        initEvent : function() {
        },
        render : function(ccpartyId) {
            t.paramter.ccpartyId = ccpartyId;
            Ajax.call({
                url : 'card/getCcpartyCardPages',
                p : {
                    ccpartyId : t.paramter.ccpartyId,
                    paramter:"{'data':" + JSON.stringify(t.paramter, "data") + "}"
                },
                f : function(data) {
                    if (data && data.page) {
                        t.page = data.page;
                        data.page.ccparty = data.ccparty;
                        data.page.Global = Global;
                        $("#ccparty_card").html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/ccparty-card-index.html', data.page));
                    } else {
                        Notify.error("获取党组织电子活动证页码异常");
                    }
                }
            });
        },
        // 弹框加载
        loadCcpartyCard : function() {
            Global.initLoader("#load_card");
            Ajax.call({
                url : 'card/getCcpartyCardPages',
                p : {
                    ccpartyId : t.paramter.ccpartyId,
                    paramter:"{'data':" + JSON.stringify(t.paramter, "data") + "}"
                },
                f : function(data) {
                    if (data && data.page) {
                        t.page = data.page;
                        $("#load_card").html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/dialogs/ccparty-card-page.html'));
                        $("#ccparty_card").html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/ccparty-card-index.html', t.page));
                    } else {
                        Notify.error("获取党组织电子活动证页码异常");
                    }
                }
            });
        },
        // 缓存加载
        loadInitContent : function(element, page) {
            if (page == 2) {
                // 加载目录
                t.paramter.page=t.page;
                element.html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/card-index.html', t.paramter));
            } else if (page == 3) {
                // 加载党组织基本信息
                t.loadCcpartyInfo(element);
            }
        },
        // 翻页时如果该页面内容不为空时加载
        loadPageContent : function(element, page) {
            if (page == (t.page.partyFeeBeginPage * 1) && t.paramter.cardConfig2) {
                // 党费收缴
                t.loadPartyFee(element, page);
            }else if (page == (t.page.branchConferenceBeginPage * 1) && t.paramter.cardConfig3) {
                // 支部工作描述
                t.loadBranchConferenceDescription(element, page);
            } else if (page >= t.page.branchConferenceBeginPage && page <= t.page.branchConferenceEndPage && t.paramter.cardConfig3) {
                // 支部工作详情
                t.loadBranchConferenceInfo(element, page);
            } 
        },
        // 加载党组织基本信息
        loadCcpartyInfo : function(element) {
            Ajax.call({
                url : 'org/getCcpartyInfoForCard',
                p : {
                    ccpartyId : t.paramter.ccpartyId
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        // 对领导班子成员进行数据整理
                        var leaderHtml = '';
                        if (data.item.titleViews != null) {
                            for (var i = 0; i < data.item.titleViews.length; i++) {
                                var titleView = data.item.titleViews[i];
                                leaderHtml += '<li><b>' + titleView.title.name + '：</b>';
                                if (titleView.electionMembers) {
                                    for (var iteart = 0; iteart < titleView.electionMembers.length; iteart++) {
                                        var member = titleView.electionMembers[iteart];
                                        if (iteart == 0) {
                                            leaderHtml += member.userName;
                                        } else {
                                            leaderHtml += '、' + member.userName;
                                        }
                                    }
                                }
                                leaderHtml += '</li>';
                            }
                        } else {
                            leaderHtml = '暂无领导班子数据。';
                        }
                        data.item.leaderHtml = leaderHtml;
                        element.html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/card-ccparty-info.html', data.item));
                    } else {
                        Notify.error("党组织基本信息获取失败。");
                    }
                }
            });
        },
        // 支部工作描述
        loadBranchConferenceDescription : function(element, page) {
            $(".p" + page).html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/card-branch-conference-description.html'));
            Ajax.call({
                url : 'obt/getConferenceDescriptionForCard',
                p : {
                    ccpartyId : t.paramter.ccpartyId,
                    paramter:"{'data':" + JSON.stringify(t.paramter, "data") + "}"
                },
                f : function(data) {
                    if (data && data.item) {
                        $(".p" + page).html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/card-branch-conference-description.html', data));
                    } else {
                        Notify.error("加载支部工作信息异常");
                    }
                }
            });
        },
        // 支部工作列表
        loadBranchConferenceInfo : function(element, page) {
            Ajax.call({
                url : 'obt/getConferenceListByCcparty',
                p : {
                    ccpartyId : t.paramter.ccpartyId,
                    offset : (page - t.page.branchConferenceBeginPage-1) * 2,
                    limit : 2,
                    paramter:"{'data':" + JSON.stringify(t.paramter, "data") + "}"
                },
                f : function(data) {
                    if (data && data.items) {
                        $.extend(data, Global);
                        $(".p" + page).html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/card-branch-conference-info.html', data));
                        $("[limit]").limit();
                    } else {
                        Notify.error("加载支部工作信息异常");
                    }
                }
            });
        },
        // 党费收缴描述
        loadPartyFee : function(element, page) {
            Global.initLoader(".p" + page);
            Ajax.call({
                url : 'obt/getCcpartyPartyFeeMonthTotalDetailForCard',
                p : {
                    ccpartyId : t.paramter.ccpartyId,
                    year : t.year
                },
                f : function(data) {
                    if (data && data.item) {
                        data.getYearForSelectOption = Global.getYearForSelectOption;
                        data.page=page;
                        $(".p" + page).html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/card-party-fee.html', data));
                        $("#fee-year-select").val(t.year);
                    } else {
                        Notify.error("加载党费收缴异常");
                    }
                }
            });
        },
        //党费按年份搜索
        partyFeeYearSelected:function(year,page){
            t.year = year;
            t.loadPartyFee(null,page)
        }
    }
    return t;
}();