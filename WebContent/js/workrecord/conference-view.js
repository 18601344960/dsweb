/**
 * @description 文章浏览
 * @author yiwenjun
 * @since 2015-03-31
 */
var ConferenceView = function() {
    var t = {
        articleId : '',
        viewSource : '',
        init : function(articleId,viewSource) {
            t.articleId = articleId;
            t.viewSource = viewSource;
            t.initView();
            t.initEvent();
        },
        initView : function() {
            Ajax.call({
                url : "obt/getConferenceById",
                p : {
                    id : t.articleId,
                    isView : true
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        $("#main-container").html(nunjucks.render(Global.appName + '/tpl/conference/conference-view.html', data.item));
                        $("#location-title").text(data.item.name);
                        t.getUpDownConference();
                    } else {
                        Notify.error("获取文章失败");
                    }
                }
            });
            t.loadComment();

        },
        //获取上一篇、下一篇
        getUpDownConference:function(){
            Global.initLoader('#beforeConference');
            Global.initLoader('#afterConference');
            Ajax.call({
                url : "obt/getUpDownConference",
                p : {
                    id : t.articleId,
                    viewSource : t.viewSource,
                    ccpartyId : Global.ccpartyId,
                    userId : Global.userId
                },
                f : function(data) {
                    if (data) {
                        if(data.beforeConference){
                            $('#beforeConference').html('<a href="article-view?articleId='+data.beforeConference.id+'&viewSource='+t.viewSource+'" title="'+data.beforeConference.name+'">'+data.beforeConference.name+'</a>');
                        }else{
                            $('#beforeDiv').remove();
                        }
                        if(data.afterConference){
                            $('#afterConference').html('<a href="article-view?articleId='+data.afterConference.id+'&viewSource='+t.viewSource+'" title="'+data.afterConference.name+'">'+data.afterConference.name+'</a>');
                        }else{
                            $('#afterDiv').remove();
                        }
                    } else {
                        Notify.error("获取上一篇、下一篇文章失败");
                        $('#beforeConference').html("<font style='color:red'>文章获取失败。</font>");
                        $('#afterConference').html("<font style='color:red'>文章获取失败。</font>");
                    }
                }
            });
        },
        initEvent : function() {
            $(document).on("click", "#submit-btn", function() {
                t.addComment();
            })
        },
        addComment : function() {
            var content = $("#comment-content").val();
            Ajax.call({
                url : "obt/addConferenceComment",
                p : {
                    articleId : t.articleId,
                    content : content
                },
                f : function(data) {
                    if (data && data.success == true) {
                        t.loadComment();
                        $("#comment-content").val("");
                        Notify.success("添加评论成功");
                    } else {
                        Notify.error("添加评论失败");
                    }
                }
            });
        },
        loadComment : function() {
            Ajax.call({
                url : "obt/getConferenceCommentList",
                p : {
                    articleId : t.articleId
                },
                f : function(data) {
                    if (data && data.items && data.items.length > 0) {
                        $("#comment-list").html(nunjucks.render(Global.appName + '/tpl/workshare/comment.html', data));
                    } else {
                        $("#comment-list").html("<div class='item'>暂无评论</div>");
                    }
                }
            });
        },
        printArticle : function() {
            window.print();
        },
        praiseArticle : function() {
            Ajax.call({
                url : "obt/addConferencePraise",
                p : {
                    articleId : t.articleId,
                    type : 0
                },
                f : function(data) {
                    if (data && data.success == true) {
                        if (data && data.repeat == true) {
                            Notify.notice("您已点赞");
                        } else {
                            Notify.success("点赞成功");
                            var praiseCount = $("#praise-count").text();
                            $("#praise-count").text(parseInt(praiseCount) + 1);
                        }
                    } else {
                        Notify.error("点赞失败");
                    }
                }
            });
        },
        downloadConference : function(id) {
            var html = "reports/obt/conferenceDetail?id=" + id + "&format=pdf&date=" + new Date();
            window.open(html);
        }
    }
    return t;
}();