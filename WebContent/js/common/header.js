/**
 * @description 公共头部
 * @author yiwenjun
 * @since 2015-03-28
 */
var Header = function() {
    var t = {
        init : function(isHome) {
            t.initView(isHome);
            t.initEvent();
        },
        initView : function(isHome) {
            $("#common-header").html(nunjucks.render(Global.appName + '/tpl/common/header.html', Global));
            $("#show-current-ccparty-name").html(Global.ccpartyName);
            var userName = Global.userName;
            if (Global.userTitle && Global.userTitle != "") {
                userName = userName ;
            }
            $("#show-current-user-name").html(userName);
            if (Global.userId && Global.userId != "") {
                Ajax.call({
                    url : "uam/getRoleUsers",
                    p : {
                        userId : Global.userId
                    },
                    f : function(data) {
                        if (data) {
                            if (data.rows.length > 1) {
                                $("#role-users").html(nunjucks.render(Global.appName + '/tpl/common/role-user.html', data));
                            } else {
                                $("#header-role-user").hide();
                            }
                        } else {
                            Notify.error("获取用户角色列表失败");
                        }
                    }
                });
            }
            var today = new Date();
            var currentYear = today.getFullYear();
            $("#footer-copyright-year").html(currentYear);
        },
        logout : function() {
            Ajax.call({
                url : "uam/logout",
                p : {},
                f : function(data) {
                    if (data.success == true) {
                        window.location.href = "login";
                    } else {
                        alert("登出失败");
                    }
                }
            });
        },
        initEvent : function() {
            $(document).on("click", ".header-role-user .change-role", function() {
                var roleUserId = $(this).attr("roleUserId");
                var url = location.href;
                if (url.indexOf("?") > 0) {
                    if (url.indexOf("#") > 0) {
                        url = url.substring(0, url.indexOf("#"));
                    }
                    if (url.indexOf("roleUserId") > 0) {
                        url = Global.changeURLPara(url, "roleUserId", roleUserId);

                    } else {
                        url = url + "&roleUserId=" + roleUserId;
                    }
                } else {
                    url = url + "?roleUserId=" + roleUserId;
                }
                window.location.href = url;

            })
        }
    }
    return t;
}();
$(Header.init);