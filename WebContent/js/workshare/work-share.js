/**
 * @description 工作共享
 * @author 易文俊
 * @since 2015-04-20
 */
var WorkShare = function() {
    var t = {
        mainContainer : '',
        currentCcpartyId : Global.ccpartyId,
        ccpartyId : '',
        sourceType : -1,
        brandType : -1,
        isRecommend : -1,
        name : '',
        beginTime : '',
        endTime : '',
        label : '',
        format : '',
        step : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
        },
        initSearch : function() {
            t.ccpartyId = '';
            t.name = '';
            t.sourceType = -1;
            t.brandType = -1;
            t.beginTime = '';
            t.endTime = '';
            t.label = '';
            t.step = '';
            t.format = '';
        },
        initView : function() {
            t.render();
        },
        initEvent : function() {
            // 党组织树监听事件
            $(document).on("click", "#ccpartyId,#choose-ccparty-btn", function() {
                t.initCcpartySubjectTree();
                var ccpartyIdObj = $("#ccpartyId");
                var ccpartyIdOffset = $("#ccpartyId").offset();
                $("#menuContent").css({
                    left : ccpartyIdOffset.left + "px",
                    top : ccpartyIdOffset.top + ccpartyIdObj.outerHeight() + "px"
                }).slideDown("fast");
                $("body").bind("mousedown", t.onCcpartyBodyDown);
            });
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
                        $("#workshare-left").html(nunjucks.render(Global.appName + '/tpl/workshare/search.html', data));
                    }
                }
            });
        },
        // 文章列表加载
        loadActivitys : function(param) {
            Global.initLoader(t.mainContainer);
            var offset = 0;
            var limit = 10;
            if (param) {
                offset = param.offset;
                limit = param.limit;
            }
            Ajax.call({
                url : "obt/getConferenceListForShare",
                p : {
                    offset : offset,
                    limit : limit,
                    currentCcpartyId : t.currentCcpartyId,
                    ccpartyId : t.ccpartyId,
                    sourceType : t.sourceType,
                    brandType : t.brandType,
                    isRecommend : t.isRecommend,
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
        // 党组织初始化树
        initCcpartySubjectTree : function() {
            ComTree.initTree({
                divContainer : "#ccparty-tree",
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                onClick : t.ccpartyClickCallBack
            });
        },
        // 树点击回调函数
        ccpartyClickCallBack : function(e, treeId, treeNode) {
            $("#ccpartyId").attr("ids", treeNode.id);
            $("#ccpartyId").val(treeNode.name);
            $("#menuContent").fadeOut("fast");
            $("body").unbind("mousedown", t.onBodyDown);
            t.ccpartyId = treeNode.id;
            t.loadActivitys();

        },
        // 鼠标失去焦点
        onCcpartyBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
                $("#menuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        // 初始化组织树
        initCcpartyId : function() {
            $("#ccpartyId").attr("ids", "");
            $("#ccpartyId").val("");
            t.ccpartyId = '';
            $("#menuContent").fadeOut("fast");
            $("body").unbind("mousedown", t.onBodyDown);
            t.loadActivitys();
        },
        // 来源搜索
        searchSourceType : function(sourceType) {
            t.sourceType = sourceType;
            if (t.sourceType == 1) {
                $("#search-party-condition").hide();
            } else {
                $("#search-party-condition").show();
            }
            t.loadActivitys();
        },
        // 工作品牌搜索
        searchBrandType : function(brandType) {
            t.brandType = brandType;
            t.loadActivitys();
        },
        //是否推荐
        searchisRecommend:function(isRecommend){
            t.isRecommend = isRecommend;
            t.loadActivitys();
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
