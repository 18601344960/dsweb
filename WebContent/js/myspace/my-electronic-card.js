/**
 * @description 我的党员手册
 * @author 赵子靖
 * @since 2016-07-06
 */
var MyElectronicCard = function() {
    var t = {
        mainContainer : '',
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
            isRecommend : -1
        }),
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
        },
        initView : function() {
        },
        initParamter : function() {
            t.paramter.cardConfig = [ 1, 2, 3];
            t.paramter.beginTime = '';
            t.paramter.endTime = '';
            t.paramter.label = '';
            t.paramter.step = '';
            t.paramter.format = '';
            t.paramter.brandType = -1;
            t.paramter.isRecommend = -1;
        },
        initEvent : function() {
        },
        render : function() {
            var config = {
                getYearForSelectOption : Global.getYearForSelectOption
            };
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/myspace/my-electronic-card-navicat.html', config));
            t.loadSearch();
            t.loadMyElectronicCard();
        },
        // 搜索条件加载
        loadSearch : function() {
            Ajax.call({
                url : "obt/getCcpartyConferenceCategorys",
                p : {
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        $("#card-search").html(nunjucks.render(Global.appName + '/tpl/myspace/electronic-card/card-search.html', data));
                    }
                }
            });
        },
        //电子活动证加载
        loadMyElectronicCard : function() {
            var year = $("#year").val();
            Global.initLoader("#my-electronic-card");
            Ajax.call({
                url : "uam/loadUserInfoByUserId",
                p : {
                    userId : Global.userId
                },
                f : function(data) {
                    t.paramter.userName = data.item.name;
                    PartymemberElectronicCard.init(t.paramter);
                    $("#my-electronic-card").html(nunjucks.render(Global.appName + '/tpl/myspace/my-electronic-card.html'));
                    PartymemberElectronicCard.render(t.paramter.userId);
                }
            });
        },
        // 搜索条件显示
        showSearchCard : function() {
            $("#hidden-card-search").slideUp();
            $("#show-card-search").slideDown();
        },
        // 隐藏搜索调节剂
        hiddenSearchCard : function() {
            $("#show-card-search").slideUp();
            $("#hidden-card-search").slideDown();
        },
        // 日期搜索
        searchDate : function() {
            t.paramter.beginTime = $("#beginTime").val();
            t.paramter.endTime = $("#endTime").val();
        },
        // 工作法步骤点击
        clickStep : function(id) {
            // 判断class
            if($("#" + id).attr("class")=='search-step-selected'){
                $("#" + id).attr("class", "search-step");
            }else{
                $("#" + id).attr("class", "search-step-selected");
            }
            t.paramter.step = "";
            $("#search-step-div a").each(function() {
                if("search-step-selected"==$(this).attr("class")){
                    t.paramter.step += $(this).attr("id")+",";
                }
            });
            if(t.paramter.step){
                t.paramter.step = t.paramter.step.substring(0, t.paramter.step.length-1);
            }
        },
        // 形式点击
        clickFormat : function(id) {
            if($("#" + id).attr("class")=='search-format-selected'){
                $("#" + id).attr("class", "search-format");
            }else{
                $("#" + id).attr("class", "search-format-selected");
            }
            t.paramter.format = "";
            $("#search-format-div a").each(function() {
                if("search-format-selected"==$(this).attr("class")){
                    t.paramter.format += $(this).attr("id")+",";
                }
            });
            if(t.paramter.format){
                t.paramter.format = t.paramter.format.substring(0, t.paramter.format.length-1);
            }
        },
        // 内容点击
        clickLabel : function(id) {
            if($("#" + id).attr("class")=='search-label-selected'){
                $("#" + id).attr("class", "search-label");
            }else{
                $("#" + id).attr("class", "search-label-selected");
            }
            t.paramter.label = "";
            $("#search-label-div a").each(function() {
                if("search-label-selected"==$(this).attr("class")){
                    t.paramter.label += $(this).attr("id")+",";
                }
            });
            if(t.paramter.label){
                t.paramter.label = t.paramter.label.substring(0, t.paramter.label.length-1);
            }
        },
        // 工作品牌搜索
        searchBrandType : function(brandType) {
            t.paramter.brandType = brandType;
        },
        // 是否推荐
        searchisRecommend : function(isRecommend) {
            t.paramter.isRecommend = isRecommend;
        },
        // 搜索
        search : function() {
            t.paramter.cardConfig = [];
            $('input[name="card-config"]:checked').each(function() {
                t.paramter.cardConfig.push($(this).val());
            });
            t.loadMyElectronicCard();
        },
        downloadCard : function() {
            var html = "reports/mgr/memberCardReport?userId="+Global.userId+"&paramter="+"{'data':" + JSON.stringify(t.paramter, "data") + "}"+"&format=pdf&date=" + new Date();
            window.open(html);
        }
    };
    return t;
}();