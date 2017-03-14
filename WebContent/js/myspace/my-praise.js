/**
 * @description 我的点赞
 * @author 赵子靖
 * @since 2016-06-17
 */
var MyPraise = function() {
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
        render : function(param) {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/myspace/my-praise.html'));
            t.renderList();
        },
        // 渲染我的评论列表和分页
        renderList : function(param) {
            Global.initLoader("#praise-list");
            var offset = 0;
            var limit = 10;
            var beginTime;
            var endTime;
            var searchKey;
            if (param) {
                offset = param.offset;
                limit = param.limit;
                beginTime = param.beginTime;
                endTime = param.endTime;
                searchKey = param.searchKey;
            }
            Ajax.call({
                url : "obt/getMyConferencePraiseList",
                p : {
                    offset : offset,
                    limit : limit,
                    beginTime : beginTime,
                    endTime : endTime,
                    searchKey : searchKey,
                    userId : Global.userId
                },
                f : function(data) {
                    if (data && data.rows) {
                        $.extend(data, Global);
                        $("#praise-list").html(nunjucks.render(Global.appName + '/tpl/myspace/my-praise-list.html', data));
                        var options = {
                            offset : offset,
                            limit : limit,
                            total : data.total,
                            beginTime : beginTime,
                            endTime : endTime,
                            searchKey : searchKey,
                            callback : {
                                onGotoPage : t.renderList
                            }
                        };
                        $(t.mainContainer).data("pagination", options);
                        $("#tpri-pagination").TpriPagination(options);
                    } else {
                        Notify.error("获取我的文章评论失败，请稍后再试！");
                    }
                }
            });
        },
        // 删除我的点赞
        deletePraise : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除？",
                callback : function(result) {
                    if (result) {
                        var ids = [];
                        ids.push(id);
                        Ajax.call({
                            url : "obt/deleteConferencePraise",
                            p : {
                                ids : JSON.stringify(ids)
                            },
                            f : function(data) {
                                if (data && data.success) {
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
        // 模糊搜索
        searchSubmit : function() {
            var param = $(t.mainContainer).data("pagination");
            param.beginTime = $("#beginTime").val();
            param.endTime = $("#endTime").val();
            param.searchKey = $("#searchKey").val();
            param.offset = 0;
            t.renderList(param);
        },
        searchClearSubmit : function() {
            $("#beginTime").val("");
            $("#endTime").val("");
            $("#searchKey").val("");
            t.renderList();
        },
        bindEnter : function(obj) {
            if (obj.keyCode == 13) {
                t.searchSubmit();
            }
        }
    }
    return t;
}();