/**
 * @description 支部工作法
 * @author yiwenjun
 * @since 2015-04-26
 */
var WorkMethod = function() {
	var t = {
		ccpartyId : Global.ccpartyId,
		init : function() {
			t.initView();
		},
		initView : function() {
			$("#work-method-container").html(nunjucks.render(Global.appName + '/tpl/home/work-method.html'));
		}
	}
	return t;
}();