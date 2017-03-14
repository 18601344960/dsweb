/**
 * @description 工作品牌
 * @author yiwenjun
 * @since 2015-07-20
 */
var WorkBrand = function() {
    var t = {
        mainContainer : '',
        name : '',
        beginTime : '',
        endTime : '',
        label : '',
        step : '',
        format : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
        },
        initSearch : function() {
            t.name = '';
            t.beginTime = '';
            t.endTime = '';
            t.label = '';
            t.step = '';
        },
        initView : function() {
            t.render();
        },
        initEvent : function() {
        },
        // 加载
        render : function() {
            t.loadSearch();
            t.loadActivitys();
        },
        // 加载左侧搜索
        loadSearch : function() {
            Global.initLoader("#workshare-left");
            Ajax.call({
                url : "obt/getCcpartyConferenceCategorys",
                p : {
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        $("#workshare-left").html(nunjucks.render(Global.appName + '/tpl/workbrand/search.html', data));
                    }
                }
            });
        },
        // 加载品牌列表
        loadActivitys : function(param) {
            Global.initLoader(t.mainContainer);
            var offset = 0;
            var limit = 10;
            if (param) {
                offset = param.offset;
                limit = param.limit;
            }
            Ajax.call({
                url : "obt/getWorkBrandsOfCcparty",
                p : {
                    offset : offset,
                    limit : limit,
                    ccpartyId : Global.ccpartyId,
                    name : t.name,
                    beginTime : t.beginTime,
                    endTime : t.endTime,
                    label : t.label,
                    format : t.format,
                    step : t.step
                },
                f : function(data) {
                    if (data && data.rows) {
                        $.extend(data, Global);
                        $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workshare/conference-list.html', data));
                        $("[limit]").limit();
                        var options = {
                            offset : offset,
                            limit : limit,
                            total : data.total,
                            callback : {
                                onGotoPage : t.loadActivitys
                            }
                        };
                        $(t.mainContainer).data("pagination", options);
                        $("#tpri-pagination").TpriPagination(options);
                    } else {
                        Notify.error("获取文章失败，请稍后再试！");
                    }
                }
            });
        },
        // 标题搜索
        searchName : function(name) {
            t.name = name;
            t.loadActivitys();
        },
        // 日期搜索
        searchDate : function() {
            t.beginTime = $("#beginTime").val();
            t.endTime = $("#endTime").val();
            t.loadActivitys();
        },
        // 工作法步骤点击
        clickStep : function(id) {
            //判断class
            if($("#" + id).attr("class")=='search-step-selected'){
                $("#" + id).attr("class", "search-step");
            }else{
                $("#" + id).attr("class", "search-step-selected");
            }
            t.step = "";
            $(".search-step-div a").each(function() {
                if("search-step-selected"==$(this).attr("class")){
                    t.step += $(this).attr("id")+",";
                }
            });
            if(t.step){
                t.step = t.step.substring(0, t.step.length-1);
            }
            t.loadActivitys();
        },
        // 形式点击
        clickFormat : function(id) {
            if($("#" + id).attr("class")=='search-format-selected'){
                $("#" + id).attr("class", "search-format");
            }else{
                $("#" + id).attr("class", "search-format-selected");
            }
            t.format = "";
            $(".search-format-div a").each(function() {
                if("search-format-selected"==$(this).attr("class")){
                    t.format += $(this).attr("id")+",";
                }
            });
            if(t.format){
                t.format = t.format.substring(0, t.format.length-1);
            }
            t.loadActivitys();
        },
        // 内容点击
        clickLabel : function(id) {
            if($("#" + id).attr("class")=='search-label-selected'){
                $("#" + id).attr("class", "search-label");
            }else{
                $("#" + id).attr("class", "search-label-selected");
            }
            t.label = "";
            $(".search-label-div a").each(function() {
                if("search-label-selected"==$(this).attr("class")){
                    t.label += $(this).attr("id")+",";
                }
            });
            if(t.label){
                t.label = t.label.substring(0, t.label.length-1);
            }
            t.loadActivitys();
        },
        // 搜索
        search : function() {
            t.loadActivitys();
        },
        // 重置
        resetSearch : function() {
            t.initSearch();
            t.loadSearch();
            t.loadActivitys();
        }
    }
    return t;
}();