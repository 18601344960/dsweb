/**
 * @description 工作必备
 * @author yiwenjun
 * @since 2015-04-03
 */

var Information = function() {
	var t = {
		mainContainer : '',
		init : function() {
			t.mainContainer = "#main-container";
			t.initView();
			t.initEvent();
		},
		initView : function() {
			$("#main-container").html( nunjucks.render(Global.appName + '/tpl/information/information-container.html'));
			$("#left-table").bootstrapTable({
				queryParams : function(params) {
					$.extend(params, {
						ccpartyId : Global.ccpartyId,
						category : 1,
					})
					return params;
				},
				onClickRow : function(row) {
				},
				onLoadSuccess : function(row) {
					$("[limit]").limit();
				}
			});
			$(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/information/information-container.html'));
			$(t.mainContainer).data("category",1);
			$(t.mainContainer).data("search","");
			t.renderList();
		},

		initEvent : function() {
			$(document).on("show.bs.tab", '#work-tabs a[data-toggle="tab"]',
					function(e) {
						var tabId = $(e.target).attr('href');
						if (tabId == "#work-rule-tab") {
							$(t.mainContainer).data("category",1);
							$(t.mainContainer).data("search","");
							t.renderList();
						} else if (tabId == "#work-reqiurement-tab") {
							$(t.mainContainer).data("category",2);
							$(t.mainContainer).data("search","");
							t.renderList();
						}
					});
			$("#right-table").bootstrapTable({
				queryParams : function(params) {
					$.extend(params, {
						ccpartyId : Global.ccpartyId,
						category : 2,
					})
					return params;
				},
				onClickRow : function(row) {
				},
				onLoadSuccess : function(row) {
					$("[limit]").limit();
				}
			});
			$("#left-table thead").remove();
			$("#right-table thead").remove();

		},

		renderList : function(param) {
		    Global.initLoader("#tab-content");
			var offset = 0;
            var limit = 20;
            if (param) {
                offset = param.offset;
                limit = param.limit;
            }
			Ajax.call({
				url : "com/getShowInformationList",
				p : { 
					ccpartyId : Global.ccpartyId,
					category : $(t.mainContainer).data("category"),
					offset : offset,
					limit : limit,
					status :1,
					search : $(t.mainContainer).data("search")
				},
				f : function(data) {
					if (data && data.rows) {
						$.extend(data, Global);
						$("#tab-content").html(nunjucks .render( Global.appName + '/tpl/information/information-list.html',data));
						$("[limit]").limit();
						options = {
							limit : limit,
							total : data.total,
							offset : offset,
							callback : {
								onGotoPage : t.renderList
							}
						};
						$("#main-container").data("pagination", options);
						$("#tpri-pagination").TpriPagination(options);
					} else {
						Notify.error("获取内容失败，请稍后再试！");
					}
				}
			});
		},
		nameFormatter : function(value, row) {
			if (row.type == 0) {
				return '<span limit="23" style="cursor: pointer;color:red;" title="'
						+ row.name
						+ '" onclick="Information.locationHref(\''
						+ row.id + '\')">' + value + '</span>';
			} else {
				return '<span limit="23" style="cursor: pointer;" title="'
						+ row.name + '" onclick="Information.locationHref(\''
						+ row.id + '\')">' + value + '</span>';
			}
		},
		// 模糊搜索
		searchSubmit : function() {
			$(t.mainContainer).data("category",$("#category").val());
			$(t.mainContainer).data("search",$("#searchKey").val());
			t.renderList();
		},
		dateFormatter : function(value, row) {
			return Global.getDate(value);
		},

		//跳转文章详情
		onclick:function(id){
			window.open("information-view?id="+ id );	
		}
		
	};
	return t;
}();
 