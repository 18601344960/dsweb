/**
 * @description 我的文章评论
 * @author 赵子靖
 * @since 2015-09-21
 */
var MyComment = function() {
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
			$(t.mainContainer).html(
					nunjucks.render(Global.appName
							+ '/tpl/myspace/my-comment.html'));
			t.renderList();
		},
		// 渲染我的评论列表和分页
		renderList : function(param) {
		    Global.initLoader("#comment-list");
			var offset = 0;
			var limit = 10;
			var beginTime ;
			var endTime ;
			var searchKey ;
			if (param) {
				offset = param.offset;
				limit = param.limit;
				beginTime = param.beginTime;
				endTime = param.endTime;
				searchKey = param.searchKey;
			}
			Ajax.call({
				url : "obt/getMyCommentList",
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
						$("#comment-list").html(
								nunjucks.render(Global.appName
										+ '/tpl/myspace/my-comment-list.html',
										data));
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
		// 删除我的评论
		deleteComment : function(commentId) {
			bootbox.confirm({
				size : 'small',
				message : "确认删除？",
				callback : function(result) {
					if (result) {
						var ids = [];
						ids.push(commentId);
						Ajax.call({
							url : "obt/deleteConferenceComment",
							p : {
								ids : JSON.stringify(ids)
							},
							f : function(data) {
								if (data && data.success == true) {
									Notify.success("删除成功");
									var param = $(t.mainContainer).data(
											"pagination");
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
		bindEnter:function(obj){
			if(obj.keyCode == 13){  
				t.searchSubmit();  
			}
		}
	}
	return t;
}();