/**
 * @description 问卷测评结果详情
 * @author 赵子靖
 * @since 2015-09-17
 */
var AssessmentResultDetail = function() {
    var t = {
        init : function(id, userId) {
            t.initView(id, userId);
            AssessmentVote.init();
        },
        initView : function(id, userId) {
            Ajax.call({
                url : "pub/viewAssessmentQuestion",
                p : {
                    assessmentId : id,
                    userId : userId
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        data.item.target = data.target;
                        // 测评
                        $("#detail-container").html(nunjucks.render(Global.appName + '/tpl/workpractice/assessment/view-user-assessment.html', data.item));
                    } else {
                        Notify.error("获取答题答卷详情失败");
                    }
                }
            });
        }
    }
    return t;
}();