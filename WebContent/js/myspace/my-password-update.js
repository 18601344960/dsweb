/**
 * @description 我的密码修改
 * @author 赵子靖
 * @since 2015-09-21
 */
var MyPasswordUpdate = function() {
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
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/myspace/updatePassword.html'));
        },
        // 密码修改确认
        updateSubmit : function() {
            var oldPwd = $("#oldPwd").val(); // 原始密码
            var newPwd = $("#newPwd").val(); // 新密码
            var affirmPwd = $("#affirmPwd").val(); // 确认密码
            if (oldPwd == null || oldPwd == "") {
                Notify.notice("请输入原始密码");
                $("#oldPwd").focus();
                return;
            } else if (newPwd == null || newPwd == "") {
                Notify.notice("请输入新密码");
                $("#newPwd").focus();
                return;
            } else if (affirmPwd == null || affirmPwd == "") {
                Notify.notice("请输入确认密码");
                $("#affirmPwd").focus();
                return;
            }
            if (newPwd != affirmPwd) {
                Notify.notice("新密码和确认密码不一致，请检查");
                $("#affirmPwd").focus();
                return;
            }
            // 发送ajax修改密码
            Ajax.call({
                url : "uam/resetUserPassword",
                p : {
                    id : Global.userId,
                    oldPassword : oldPwd,
                    newPassword : newPwd,
                },
                f : function(data) {
                    if (data && data.success == true) {
                        Notify.success(data.msg);
                        $("#oldPwd").val("");
                        $("#newPwd").val("");
                        $("#affirmPwd").val("");
                    } else {
                        Notify.error(data.msg);
                    }
                }
            });
        }
    }
    return t;
}();