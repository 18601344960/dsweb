/**
 * @description 党员电子活动证
 * @author 赵子靖
 * @since 2015-08-19
 */
var PartymemberElectronicCard = function() {
    var t = {
        paramter : new Object({
            userId : Global.userId,
            userName : '',
            cardConfig : [ 1, 2, 3],
            beginTime : '',
            endTime : '',
            label : '',
            step : '',
            format : '',
            brandType : -1,
            isRecommend : -1,
            cardConfig1:true,cardConfig2:false,cardConfig3:false,
            year:''
        }),
        page : new Object(),
        init : function(paramter) {
            if (paramter) {
                t.paramter.userId = paramter.userId;
                t.paramter.userName = paramter.userName;
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
            t.paramter.cardConfig1=true;t.paramter.cardConfig2=false;t.paramter.cardConfig3=false;
            if(t.paramter.cardConfig){
                for(var i=0;i<t.paramter.cardConfig.length;i++){
                    if(1==t.paramter.cardConfig[i]){
                        t.paramter.cardConfig1=true;
                    }else if(2==t.paramter.cardConfig[i]){
                        t.paramter.cardConfig2=true;
                    }else if(3==t.paramter.cardConfig[i]){
                        t.paramter.cardConfig3=true;
                    }
                }
            }
            t.initView();
            t.initEvent();
            t.render(t.paramter.userId);
        },
        initView : function() {
        },
        initEvent : function() {
        },
        render : function(userId) {
            t.paramter.userId = userId;
            Ajax.call({
                url : 'card/getCardPages',
                p : {
                    userId : t.paramter.userId,
                    paramter:"{'data':" + JSON.stringify(t.paramter, "data") + "}"
                },
                f : function(data) {
                    if (data && data.page) {
                        t.page = data.page;
                        $("#partymember_card").html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/partymember-electronic-card.html', data.page));
                    } else {
                        Notify.error("获取党员电子活动证页码异常");
                    }
                }
            });
        },
        // 加载数据
        loadCardDataDialog : function() {
            Global.initLoader("#load_card");
            Ajax.call({
                url : 'card/getCardPages',
                p : {
                    userId : t.paramter.userId,
                    paramter:"{'data':" + JSON.stringify(t.paramter, "data") + "}"
                },
                f : function(data) {
                    if (data && data.page) {
                        t.page = data.page;
                        $("#load_card").html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/dialogs/partymember-card-page.html'));
                        $("#partymember_card").html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/partymember-electronic-card.html', data.page));
                    } else {
                        Notify.error("获取党员电子活动证页码异常");
                    }
                }
            });
        },
        // 缓存加载
        loadInitContent : function(element, page) {
            if (page == 2) {
                // 加载目录
                t.paramter.page=t.page;
                element.html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/card-index.html', t.paramter));
            } else if (page == 3) {
                // 加载党员基本信息
                t.loadPartymemberInfo(element);
            }  else if (page == t.page.partyFeeBeginPage * 1 && t.paramter.cardConfig2) {
                // 党费收缴
                t.loadPartyFee(element, page);
            }
        },
        // 翻页时如果该页面内容不为空时加载
        loadPageContent : function(element, page) {
            if (page == (t.page.activityBeginPage * 1 ) && t.paramter.cardConfig3) {
                // 组织生活描述
                t.loadPartymemberActivityDescription(element, page);
            } else if (page >= t.page.activityBeginPage && page <= t.page.activityEndPage && t.paramter.cardConfig3) {
                // 组织生活详情
                t.loadPartymemberActivityInfo(element, page);
            }
        },
        // 加载基本信息
        loadPartymemberInfo : function(element) {
            Ajax.call({
                url : 'uam/loadUserInfoByUserId',
                p : {
                    userId : t.paramter.userId
                },
                f : function(data) {
                    if (data && data.item) {
                        data.item.organizationNames = data.organizationNames;
                        data.item.ccpartyName = data.names;
                        $.extend(data, Global);
                        element.html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/card-partymember-info.html', data));
                    } else {
                        Notify.error("加载党员信息失败");
                    }
                }
            });
        },
        // 宣誓
        loadJoinParty : function(element) {
            var data = new Object();
            data.partymemberName = t.paramter.userName;
            element.html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/card-join-party-info.html', data));
        },
        loadPartymemberActivityDescription : function(element, page) {
            $(".p" + page).html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/card-org-activity-description.html'));
            Ajax.call({
                url : 'obt/getStatisticsDetailBranchConference',
                p : {
                    userId : t.paramter.userId,
                    paramter:"{'data':" + JSON.stringify(t.paramter, "data") + "}"
                },
                f : function(data) {
                    if (data && data.item) {
                        data.partymemberName = t.paramter.userName;
                        data.msg = data.item;
                        $(".p" + page).html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/card-org-activity-description.html', data));
                    } else {
                        Notify.error("加载组织生活活动信息异常");
                    }
                }
            });
        },
        loadPartymemberActivityInfo : function(element, page) {
            Ajax.call({
                url : 'obt/getBranchConferenceListByUser',
                p : {
                    userId : t.paramter.userId,
                    start : (page - t.page.activityBeginPage-1) * 2,
                    limit : 2,
                    paramter:"{'data':" + JSON.stringify(t.paramter, "data") + "}"
                },
                f : function(data) {
                    if (data && data.items) {
                        $.extend(data, Global);
                        $(".p" + page).html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/card-org-activity-info.html', data));
                        $("[limit]").limit();
                    } else {
                        Notify.error("加载组织生活活动信息异常");
                    }
                }
            });
        },
        // 党费收缴描述
        loadPartyFee : function(element, page) {
            Global.initLoader(".p" + page);
            Ajax.call({
                url : 'obt/getPartyFeeByUserForCard',
                p : {
                    userId : t.paramter.userId,
                    year : t.year
                },
                f : function(data) {
                    if (data && data.item) {
                        data.getYearForSelectOption = Global.getYearForSelectOption;
                        data.page=page;
                        $(".p" + page).html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/card-party-fee.html', data));
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