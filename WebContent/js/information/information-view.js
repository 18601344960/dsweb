/**
 * @description 工作必备详情
 * @author yiwenjun
 * @since 2015-05-05
 */
var InformationView = function() {
    var t = {
        init : function(infoId) {
            t.initView(infoId);
            t.initEvent();
        },
        initView : function(infoId) {
            Ajax.call({
                url : "com/getInformationById",
                p : {
                    id : infoId,
                    isView : true
                },
                f : function(data) {
                    if (data && data.item) {
                        $("#main-container").html(nunjucks.render(Global.appName + '/tpl/information/information-view.html', data.item));
                    } else {
                        Notify.error("获取失败");
                    }
                }
            });
        },
        
        initEvent : function() {
        }
    }
    return t;
}();