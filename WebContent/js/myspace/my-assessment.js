/**
 * @description 我的答卷
 * @author 赵子靖
 * @since 2016-08-11
 */
var MyAssessment = function() {
    var t = {
        mainContainer : '',
        windowTimer:'',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
        },
        initView : function() {
        },
        initEvent : function() {
        },
        render : function() {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/myspace/my-assessment/my-assessment-index.html'));
            t.renderMyAssessmentList();
        },
        // 渲染列表和分页
        renderMyAssessmentList : function(param) {
            Global.initLoader("#assessment-list");
            var offset = 0;
            var limit = 10;
            var search = $("#search").val();
            var joinType = $("#joinType").val();
            if (param) {
                offset = param.offset;
                limit = param.limit;
            }
            Ajax.call({
                url : "pub/getMyAssessments",
                p : {
                    ccpartyId : Global.ccpartyId,
                    userId : Global.userId,
                    offset : offset,
                    limit : limit,
                    search : search,
                    joinType : joinType
                },
                f : function(data) {
                    if (data && data.rows) {
                        $.extend(data, Global);
                        $("#assessment-list").html(nunjucks.render(Global.appName + '/tpl/myspace/my-assessment/my-assessment-list.html', data));
                        var options = {
                            offset : offset,
                            limit : limit,
                            total : data.total,
                            search : search,
                            joinType : joinType,
                            callback : {
                                onGotoPage : t.renderMyAssessmentList
                            }
                        };
                        $(t.mainContainer).data("pagination", options);
                        $("#tpri-pagination").TpriPagination(options);
                    } else {
                        if(data.msg){
                            Notify.error(data.msg);
                        }else{
                            Notify.error("获取答卷列表失败，请稍后再试！");
                        }
                    }
                }
            });
        },
        // 模糊搜索
        search : function() {
            var param = $(t.mainContainer).data("pagination");
            param.search = $("#search").val();
            param.joinType = $("#joinType").val();
            param.offset = 0;
            t.renderMyAssessmentList(param);
        },
        //答卷详情查看或者开始答题
        openMyAssessmentTopic : function(assessmentId) {
            var iHeight = window.screen.availHeight-10;
            var iWidth = window.screen.availWidth-10;
            var url = "my-assessment-detail?assessmentId=" + assessmentId + "&userId="+Global.userId+"&date=" + new Date();
            var windowObject = Global.openWindow(url, 'newWindow', iWidth, iHeight);
            //启动定时器来监视子窗口是否关闭
            if(!t.windowTimer.closed){
                clearInterval(t.windowTimer);
            }
            t.windowTimer = setInterval(function() {
                if (windowObject.closed == true) { 
                    t.renderMyAssessmentList();
                    clearInterval(t.windowTimer);
                }
            }, 1000);// 1000为1秒钟
        }
    }
    return t;
}();