/**
 * @description 管理中心
 * @author yiwenjun
 * @since 2015-09-01
 */
var ManagerHome = function() {
    var t = {
        mainContainer : "#right-container .main-detail",
        init : function(navigation) {
            t.initView();
            t.initEvent();
            ManagerEnvironment.init(t.mainContainer);
            ManagerPrivilege.init(t.mainContainer);
            CcpartyNavigation.init(t.mainContainer);
            AdministrationNavigation.init(t.mainContainer);
            ManagerUserNavigation.init(t.mainContainer);
            ManagerRole.init(t.mainContainer);
            ManagerPrivilege.init(t.mainContainer);
            ManagerSystemUser.init(t.mainContainer);
            ManagerPartyWorker.init(t.mainContainer);
            ManagerImportUser.init(t.mainContainer);
            $("#" + navigation).trigger("click");
        },
        initView : function() {
            $("#left-container").html(nunjucks.render(Global.appName + '/tpl/manager/navigation.html', Global));
        },
        initEvent : function() {
            $(document).on("click", "#nav-ccparty", function() {
                // 党组织管理
                t.changeSelected($(this));
                CcpartyNavigation.render();
            });
            $(document).on("click", "#nav-organization", function() {
                // 行政组织管理
                t.changeSelected($(this));
                AdministrationNavigation.render();
            });
            $(document).on("click", "#nav-party-worker", function() {
                // 党务人员
                t.changeSelected($(this));
                ManagerPartyWorker.render();
            });
            $(document).on("click", "#nav-user", function() {
                // 用户
                t.changeSelected($(this));
                ManagerUserNavigation.render();
            });
            $(document).on("click", "#nav-role", function() {
                // 角色
                t.changeSelected($(this));
                ManagerRole.render();
            });
            $(document).on("click", "#nav-privilege", function() {
                // 权限
                t.changeSelected($(this));
                ManagerPrivilege.render();
            });
            $(document).on("click", "#nav-system-user", function() {
                // 系统用户
                t.changeSelected($(this));
                ManagerSystemUser.render();
            });
            $(document).on("click", "#nav-system-environment", function() {
                // 系统参数
                t.changeSelected($(this));
                ManagerEnvironment.render();
            });
            $(document).on("click", "#nav-import-user", function() {
                // 用户导入
                t.changeSelected($(this));
                ManagerImportUser.render();
            });
        },
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
        renderDefault : function() {
            $(t.mainContainer).html("正在建设中...");
        }
    }
    return t;
}();