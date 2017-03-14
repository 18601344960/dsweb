/**
 * @description 我的文章列表
 * @author 赵子靖
 * @since 2015-09-21
 */
var MyArticle = function() {
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
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/myspace/my-conference/my-article.html'));
            t.renderList();
        },
        // 渲染我的文章列表和分页
        renderList : function(param) {
            Global.initLoader("#article-list");
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
                url : "obt/getMyConferenceList",
                p : {
                    offset : offset,
                    limit : limit,
                    beginTime : beginTime,
                    endTime : endTime,
                    searchKey : searchKey
                },
                f : function(data) {
                    if (data && data.rows) {
                        $.extend(data, Global);
                        $("#article-list").html(nunjucks.render(Global.appName + '/tpl/myspace/my-conference/my-article-list.html', data));
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
                        Notify.error("获取我的文章失败，请稍后再试！");
                    }
                }
            });
        },
        // 删除文章
        deleteArticle : function(articleId) {
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
        // 评论管理
        commentManager : function(articleId) {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/myspace/my-conference/article-comment-manager-dialog.html'));
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
        // 回车事件
        bindEnter : function(obj) {
            if (obj.keyCode == 13) {
                t.searchSubmit();
            }
        },
        publishConference : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认发布？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/publishConference",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("发布成功");
                                } else {
                                    Notify.success("发布失败");
                                }
                                var param = $(t.mainContainer).data("pagination");
                                t.renderList(param);
                            }
                        });
                    }
                }
            });
        },
        unpublishConference : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认取消发布？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/unpublishConference",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("取消发布成功");
                                } else {
                                    Notify.success("取消发布失败");
                                }
                                var param = $(t.mainContainer).data("pagination");
                                t.renderList(param);
                            }
                        });
                    }
                }
            });
        },
        commentUserName : function(value, row) {
            return value.name;
        },
        commentOperatorFormatter : function(value, row) {
            var operator = "";
            operator += '<a href="javascript:MyArticle.deleteComment(\'' + value + '\')" class="btn btn-default btn-xs" ><i class="fa fa-trash-o"></i>删除</a>';
            return operator;
        },
        openAddConferece : function() {
            var iHeight = window.screen.availHeight;
            var iWidth = window.screen.availWidth;
            var windowObject = Global.openWindow('article-add', 'newWindow', iWidth, iHeight);
            //启动定时器来监视子窗口是否关闭
            timer = setInterval(function() {
                if (windowObject.closed == true) {
                    var param = $(t.mainContainer).data("pagination");
                    t.renderList(param);
                    clearInterval(timer);
                }
            }, 1000);// 1000为1秒钟
        },
        openEditConferece : function(id) {
            var iHeight = window.screen.availHeight;
            var iWidth = window.screen.availWidth;
            var windowObject = Global.openWindow('article-edit?conferenceId=' + id, 'newWindow', iWidth, iHeight);
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
        }
    }
    return t;
}();