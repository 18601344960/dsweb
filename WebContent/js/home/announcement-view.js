/**
 * @description 查看通知内容
 * @author yiwenjun
 * @since 2015-03-31
 */
var AnnouncementView = function() {
    var t = {
        init : function(articleId) {
            t.initView(articleId);
        },
        initView : function(articleId) {
            Ajax.call({
                url : "com/getAnnouncementById",
                p : {
                    id : articleId,
                    isView : true
                },
                f : function(data) {
                    if (data && data.item) {
                        $("#announcement-container").html(nunjucks.render(Global.appName + '/tpl/information/information-view.html', data.item));
                        $("#location").append(" > "+data.item.name);
                    } else {
                        alert("获取通知通告失败");
                    }
                }
            });
        }
    }
    return t;
}();