/**
 * @description 本组织的文章列表
 * @author 赵子靖
 * @since 2015-09-22
 */
var CcpartyArticle = function() {
    var t = {
        mainContainer : '',
        windowTimer:'',
        paramter : new Object({
            beginTime : '',
            endTime : '',
            label : '',
            step : '',
            format : '',
            name : '',
            sourceType : -1,
            brandType : -1,
            isRecommend : -1
        }),
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
        },
        initParamter : function() {
            t.paramter.beginTime = '';
            t.paramter.endTime = '';
            t.paramter.label = '';
            t.paramter.step = '';
            t.paramter.format = '';
            t.paramter.name = '';
            t.paramter.sourceType = -1;
            t.paramter.brandType = -1;
            t.paramter.isRecommend = -1;
        },
        initView : function() {
        },
        initEvent : function() {
        },
        render : function(param) {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-conference/ccparty-article.html'));
            t.initParamter();
            t.loadSearch();
            t.renderList();
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
                        $("#search").html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-conference/search.html', data));
                    }
                }
            });
        },
        // 渲染本组织的文章列表和分页
        renderList : function(param) {
            Global.initLoader("#article-list");
            var offset = 0;
            var limit = 10;
            var beginTime;
            var endTime;
            var search;
            if (param) {
                offset = param.offset;
                limit = param.limit;
                beginTime = param.beginTime;
                endTime = param.endTime;
                search = param.search;
            }
            Ajax.call({
                url : "obt/getCcpartyConferenceList",
                p : {
                    offset : offset,
                    limit : limit,
                    ccpartyId : Global.ccpartyId,
                    paramter : "{'data':" + JSON.stringify(t.paramter, "data") + "}"
                },
                f : function(data) {
                    if (data && data.rows) {
                        $.extend(data, Global);
                        $("#article-list").html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-conference/ccparty-article-list.html', data));
                        var options = {
                            offset : offset,
                            limit : limit,
                            total : data.total,
                            callback : {
                                onGotoPage : t.renderList
                            }
                        };
                        $(t.mainContainer).data("pagination", options);
                        $("#tpri-pagination").TpriPagination(options);
                    } else {
                        Notify.error("获取本组织的文章失败，请稍后再试！");
                    }
                }
            });
        },
        // 删除文章
        deleteArticle : function(articleId, tabName) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除？",
                callback : function(result) {
                    if (result) {
                        var ids = [];
                        ids.push(articleId);
                        Ajax.call({
                            url : "obt/deleteConference",
                            p : {
                                ids : JSON.stringify(ids)
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("删除成功");
                                    var param = $(t.mainContainer).data("pagination");
                                    t.renderList(param);
                                } else {
                                    Notify.error("删除失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 取消推荐
        cancelRecommend : function(articleId) {
            Ajax.call({
                url : "obt/cancelRecommendConference",
                p : {
                    articleId : articleId
                },
                f : function(data) {
                    if (data && data.success == true) {
                        Notify.success("取消推荐成功");
                        var param = $(t.mainContainer).data("pagination");
                        t.renderList(param);
                    } else {
                        Notify.error("取消推荐失败");
                    }
                }
            });
        },
        // 文章推荐
        recommend : function(articleId) {
            Ajax.call({
                url : "obt/recommendConference",
                p : {
                    articleId : articleId
                },
                f : function(data) {
                    if (data && data.success == true) {
                        Notify.success("推荐成功");
                        var param = $(t.mainContainer).data("pagination");
                        t.renderList(param);
                    } else {
                        Notify.error("推荐失败");
                    }
                }
            });
        },
        // 评论管理
        commentManager : function(articleId) {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-conference/article-comment-manager-dialog.html'));
            $("#article-comment-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        articleId : articleId
                    })
                    return params;
                }
            });
            $("#comment-manager-dialog").modal({});
        },
        // 评论删除
        deleteComment : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除评论？",
                callback : function(result) {
                    if (result) {
                        var ids = [];
                        ids.push(id);
                        Ajax.call({
                            url : "obt/deleteConferenceComment",
                            p : {
                                ids : JSON.stringify(ids)
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    $("#article-comment-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error("删除失败，请稍后再试。");
                                }
                                var param = $(t.mainContainer).data("pagination");
                                t.renderList(param);
                            }
                        });
                    }
                }
            });

        },
        //置顶状态改变
        updateIsTop:function(id,isTop){
            Ajax.call({
                url : "obt/updateConferenceIsTop",
                p : {
                    id : id,
                    isTop : isTop
                },
                f : function(data) {
                    if (data && data.success) {
                        Notify.success(data.msg);
                        var param = $(t.mainContainer).data("pagination");
                        t.renderList(param);
                    } else {
                        if(data.msg){
                            Notify.erro(data.msg);
                        }else{
                            Notify.error("操作失败，请稍后再试。");
                        }
                    }
                }
            });
        },
        commentUserName : function(value, row) {
            return value.name;
        },
        commentOperatorFormatter : function(value, row) {
            var operator = "";
            operator += '<a href="javascript:CcpartyArticle.deleteComment(\'' + value + '\')" class="btn btn-default btn-xs" ><i class="fa fa-trash-o"></i>删除</a>';
            return operator;
        },
        publishConference : function(id) {
            var ids = '';
            if(CheckInputUtils.isEmpty(id)){
                //批量
                ids = t.getConferenceCheckboxVal();
                if(CheckInputUtils.isEmpty(ids)){
                    Notify.notice("请先勾选需要操作的数据。");
                    return;
                }
            }else{
                ids = id;
            }
            bootbox.confirm({
                size : 'small',
                message : "确认发布？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/publishConference",
                            p : {
                                ids : ids
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    $("#checkAllConferenceCheckbox").prop('checked',false);
                                    var param = $(t.mainContainer).data("pagination");
                                    t.renderList(param);
                                } else {
                                    if(data.msg){
                                        Notify.error(data.msg);
                                    }else{
                                        Notify.success("发布失败，请稍后再试。");
                                    }
                                }
                            }
                        });
                    }
                }
            });
        },
        unpublishConference : function(id) {
            var ids = '';
            if(CheckInputUtils.isEmpty(id)){
                //批量
                ids = t.getConferenceCheckboxVal();
                if(CheckInputUtils.isEmpty(ids)){
                    Notify.notice("请先勾选需要操作的数据。");
                    return;
                }
            }else{
                ids = id;
            }
            bootbox.confirm({
                size : 'small',
                message : "确认取消发布？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/unpublishConference",
                            p : {
                                ids : ids
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    $("#checkAllConferenceCheckbox").prop('checked',false);
                                    var param = $(t.mainContainer).data("pagination");
                                    t.renderList(param);
                                } else {
                                    if(data.msg){
                                        Notify.error(data.msg);
                                    }else{
                                        Notify.error("取消发布失败，请稍后再试。");
                                    }
                                }
                            }
                        });
                    }
                }
            });
        },
        openAddConferece : function() {
            var iHeight = window.screen.availHeight;
            var iWidth = window.screen.availWidth;
            var windowObject=Global.openWindow('article-add', 'newWindow', iWidth, iHeight);
            if(!t.windowTimer.closed){
                clearInterval(t.windowTimer);
            }
            //启动定时器来监视子窗口是否关闭
            t.windowTimer = setInterval(function() {
                if (windowObject.closed == true) { 
                    var param = $(t.mainContainer).data("pagination");
                    t.renderList(param);
                    clearInterval(t.windowTimer);
                }
            }, 1000);// 1000为1秒钟
        },
        openEditConferece : function(id) {
            var iHeight = window.screen.availHeight;
            var iWidth = window.screen.availWidth;
            var windowObject=Global.openWindow('article-edit?conferenceId=' + id, 'newWindow', iWidth, iHeight);
            //启动定时器来监视子窗口是否关闭
            timer = setInterval(function() {
                if (windowObject.closed == true) { 
                    var param = $(t.mainContainer).data("pagination");
                    t.renderList(param);
                    clearInterval(timer);
                }
            }, 1000);// 1000为1秒钟
        },
        downloadConference : function(id) {
            var html = "reports/obt/conferenceDetail?id=" + id + "&format=pdf&date=" + new Date();
            window.open(html);
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
            t.renderList();
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
            t.renderList();
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
            t.renderList();
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
            t.renderList();
        },
        //标题搜索
        searchName : function(name) {
            t.paramter.name = name;
            t.renderList();
        },
        //来源搜索
        searchSourceType : function(sourceType) {
            t.paramter.sourceType = sourceType;
            t.renderList();
        },
        // 工作品牌搜索
        searchBrandType : function(brandType) {
            t.paramter.brandType = brandType;
            t.renderList();
        },
        // 是否推荐
        searchisRecommend : function(isRecommend) {
            t.paramter.isRecommend = isRecommend;
            t.renderList();
        },
        // 搜索
        search : function() {
            t.paramter.beginTime = $("#beginTime").val();
            t.paramter.endTime = $("#endTime").val();
            t.renderList();
        },
        //多选框全选和取消全选
        changeAllConferenceCheckbox:function($this){
            if($($this).prop('checked')){
                //全选
                $(".article-list-table tr").each(function(){
                    $(this).find("td:first input").prop('checked',true);
                    $(this).attr("class","article-list-table-hover");
                });
            }else{
                //取消全选
                $(".article-list-table tr").each(function(){
                    $(this).find("td:first input").prop('checked',false);
                    $(this).attr("class","");
                });
            }
        },
        //多选框
        changeConferenceCheckbox:function($this){
            if($($this).prop('checked')){
                $($this).parent().parent().attr("class","article-list-table-hover");
            }else{
                $($this).parent().parent().attr("class","");
            }
        },
        //获取复选框选中值
        getConferenceCheckboxVal:function(){
            var ids = '';
            $(".article-list-table tr").each(function(){
                var checkbox = $(this).find("td:first input");
                if(checkbox.prop("checked")){
                    ids+=checkbox.val()+',';
                }
            });
            return ids;
        }

    }
    return t;
}();