/**
 * @description 支部工作法步骤
 * @author yiwenjun
 * @since 2016-06-30
 */
var WorkStep = function() {
    var t = {
        init : function() {
            t.initView();
            t.initEvent();
        },
        initView : function() {
            $("#main-container").html(nunjucks.render(Global.appName + '/tpl/home/work-step.html'));
        },
        initEvent : function() {
            $(document).on("click", "#goal", function() {
                window.location.href = 'article-add?step=S001';
            });
            $(document).on("click", "#search-problem", function() {
                window.location.href = 'article-add?step=S002';
            });
            $(document).on("click", "#plan", function() {
                window.location.href = 'article-add?step=S003';
            });
            $(document).on("click", "#solving", function() {
                window.location.href = 'article-add?step=S004';
            });
            $(document).on("click", "#summary-comments", function() {
                window.location.href = 'article-add?step=S005';
            });
        }
    }
    return t;
}();