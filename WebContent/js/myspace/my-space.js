/**
 * @description 个人中心
 * @author 赵子靖
 * @since 2015-06-03
 */
var MySpace = function() {
    var t = {
        ccpartyId : Global.ccpartyId,
        mainContainer : "#right-container .main-detail",
        init : function(navigation) {
            t.initView();
            t.initEvent();
            MyInfo.init(t.mainContainer);
            MyPasswordUpdate.init(t.mainContainer);
            MyComment.init(t.mainContainer);
            MyPraise.init(t.mainContainer);
            MyArticle.init(t.mainContainer);
            MyElectronicCard.init(t.mainContainer);
            MyAssessment.init(t.mainContainer);
            MyCcpartyCard.init(t.mainContainer);
            $("#" + navigation).trigger("click");
        },
        initView : function() {
            $("#left-container").html(nunjucks.render(Global.appName + '/tpl/myspace/navigation.html', Global));
            //我的答卷数目 红圈显示
            Ajax.call({
                url : "pub/getMyAssessmentNum",
                p : {
                    userId : Global.userId,
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        t.showMessageNum($("#my-assessment-total"), data.total);
                    }
                }
            });
        },
        initEvent : function() {
            $(document).on("click", "#my-info", function() {
                // 我的个人信息
                t.changeSelected($(this));
                MyInfo.render();
            });
            $(document).on("click", "#nav-ccparty-card", function() {
                // 支部手册
                t.changeSelected($(this));
                MyCcpartyCard.render();
            });
            $(document).on("click", "#my-member-card", function() {
                // 党员手册
                t.changeSelected($(this));
                MyElectronicCard.initParamter();
                MyElectronicCard.render();
            });
            $(document).on("click", "#edit-password", function() {
                // 修改密码
                t.changeSelected($(this));
                MyPasswordUpdate.render();
            });
            $(document).on("click", "#my-article", function() {
                // 我的文章
                t.changeSelected($(this));
                MyArticle.render();
            });
            $(document).on("click", "#my-comment", function() {
                // 我的评论
                t.changeSelected($(this));
                MyComment.render();
            });
            $(document).on("click", "#my-prasie", function() {
                // 我的点赞
                t.changeSelected($(this));
                MyPraise.render();
            });
            $(document).on("click", "#my-assessment", function() {
                // 答题答卷
                t.changeSelected($(this));
                MyAssessment.render();
            });
            
        },
        // 导航事件
        changeSelected : function($this) {
            var title = $this.find("a").text();
            $("#right-container .main-title").html(title);

            $(".panel-collapse").removeClass("in");
            $this.parent().parent().parent().addClass("in");

            $(".panel-collapse ul li").removeClass("menu-selected");
            if ($this.hasClass("menu-item")) {
                $this.addClass("menu-selected");
            }

            $(".panel-heading").removeClass("header-menu-selected");
            if ($this.hasClass("panel-heading")) {
                $this.addClass("header-menu-selected");
            }

            Global.initLoader(t.mainContainer);
        },
        render : function() {
            $(t.mainContainer).html("正在建设中...");
        },
        dateRender : function(dateTime) {
            var date = dateTime.slice(0, 10);
            return date
        },
        showMessageNum : function($div, count) {
            $div.html(count);
            if (count > 0) {
                $div.show();
            } else {
                $div.hide();
            }
        }
    }
    return t;
}();