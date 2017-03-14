/**
 * @description 文章预览
 * @author yiwenjun
 * @since 2015-03-31
 */
var ArticlePreview = function() {
    var t = {
        articleId : "",
        init : function(articleId) {
            t.articleId = articleId;
            t.initView();
        },
        initView : function() {
            Ajax.call({
                url : "obt/getConferenceById",
                p : {
                    id : t.articleId
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        $("#main-container").html(nunjucks.render(Global.appName + '/tpl/conference/conference-preview.html', data.item));
                    } else {
                        Notify.error("获取文章失败");
                    }
                }
            });

        }
    }
    return t;
}();