/**
 * @description 问卷测评详情
 * @author 赵子靖
 * @since 2015-09-17
 */
var AssessmentDetail = function() {
	var t = {
		init : function(id) {
			t.initView(id);
		},
		initView : function(id) {
		    Ajax.call({
                url : "pub/getAssessmentTopicByAssessment",
                p : {
                    assessmentId : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        $("#detail-container").html(nunjucks.render(Global.appName + '/tpl/workspace/assessment/view-assessment-question-info.html', data.item));
                    } else {
                        Notify.error("获取答题答卷详情失败,请稍后再试。");
                    }
                }
            });
		}
	}
	return t;
}();