/**
 * @description 工作平台
 * @author 易文俊
 * @since 2016-06-29
 */
var WorkSpace = function() {
    var t = {
        ccpartyId : Global.ccpartyId,
        mainContainer : "#right-container .main-detail",
        init : function(navigation) {
            t.initView();
            t.initEvent();
            CcpartyArticle.init(t.mainContainer);
            Announcement.init(t.mainContainer);
            WorkBrand.init(t.mainContainer);
            WorkRule.init(t.mainContainer);
            WorkRequirement.init(t.mainContainer);
            PartyMember.init(t.mainContainer);
            PartyFee.init(t.mainContainer);
            Election.init(t.mainContainer);
            ElectionMonitor.init(t.mainContainer);
            Statistics.init(t.mainContainer);
            ReportExport.init(t.mainContainer);
            ReportImport.init(t.mainContainer);
            ReportOnline.init(t.mainContainer);
            DevelopmentProcedure.init(t.mainContainer);
            Assessment.init(t.mainContainer);
            ConferenceCategory.init(t.mainContainer);
            WorkCard.init(t.mainContainer);
            PartyMembersCard.init(t.mainContainer);
            $("#" + navigation).trigger("click");
        },
        initView : function() {
            $("#left-container").html(nunjucks.render(Global.appName + '/tpl/workspace/navigation.html', Global));
        },
        initEvent : function() {
            $(document).on("click", "#nav-ccparty-article", function() {
                // 工作记录
                t.changeSelected($(this));
                CcpartyArticle.render();
            });
            $(document).on("click", "#nav-announcement", function() {
                // 通知通告
                t.changeSelected($(this));
                Announcement.render();
            });
            $(document).on("click", "#nav-work-rule", function() {
                // 工作制度
                t.changeSelected($(this));
                WorkRule.render();
            });
            $(document).on("click", "#nav-work-requirement", function() {
                // 工作要求
                t.changeSelected($(this));
                WorkRequirement.render();
            });
            $(document).on("click", "#nav-party-member", function() {
                // 党员管理
                t.changeSelected($(this));
                PartyMember.render();
            });
            $(document).on("click", "#nav-development", function() {
                // 发展党员
                t.changeSelected($(this));
                DevelopmentProcedure.render();
            });
            $(document).on("click", "#nav-party-fee", function() {
                // 党费收缴
                t.changeSelected($(this));
                PartyFee.render();
            });
            $(document).on("click", "#nav-election", function() {
                // 换届选举
                t.changeSelected($(this));
                Election.render();
            });
            $(document).on("click", "#nav-election-monitor", function() {
                // 换届情况
                t.changeSelected($(this));
                ElectionMonitor.render();
            });
            $(document).on("click", "#nav-party-card", function() {
                // 工作手册
                t.changeSelected($(this));
                WorkCard.render();
            });
            $(document).on("click", "#nav-partymember-card", function() {
                // 党员手册
                t.changeSelected($(this));
                PartyMembersCard.render();
            });
            $(document).on("click", "#nav-statistics", function() {
                // 统计分析
                t.changeSelected($(this));
                Statistics.render();
            });
            $(document).on("click", "#nav-assessment", function() {
                // 问卷调查
                t.changeSelected($(this));
                Assessment.render();
            });
            $(document).on("click", "#nav-brand-label", function() {
                // 品牌标签
                t.changeSelected($(this));
                ConferenceCategory.render();
            });
            $(document).on("click", "#nav-report-export", function() {
                // 导出上报数据
                t.changeSelected($(this));
                ReportExport.render();
            });
            $(document).on("click", "#nav-report-import", function() {
                // 导入上报数据
                t.changeSelected($(this));
                ReportImport.render();
            });
            $(document).on("click", "#nav-report-online", function() {
                // 在线上报数据
                t.changeSelected($(this));
                t.render();
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
        }
    }
    return t;
}();