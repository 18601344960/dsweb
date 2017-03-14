/**
 * @description 登录
 * @author yiwenjun
 * @since 2015-04-15
 */
var Login = function() {
    var t = {
        init : function() {
            t.intView();
            t.initEvent();
            $("#footer-current-ccparty-name").hide();
        },
        intView : function() {
            if (Global.loginKeyValues && Global.loginKeyValues.trim != "") {
                var keyValues = Global.loginKeyValues.split(";");
                if (keyValues && keyValues != "" & keyValues.length > 0) {
                    for (i = 0; i < keyValues.length; i++) {
                        var keyValue = keyValues[i];
                        if (keyValue && keyValue != "") {
                            var l = keyValue.split(":")[0];
                            var p = keyValue.split(":")[1];
                            if (i == 0) {
                                $("#userId").val(l);
                                $("#userId").css("color", "#000");
                                if (p && p != "") {
                                    $("#password").val(p);
                                    $("#remember-password").prop('checked', true)
                                    $("#password").css("color", "#000");
                                    $("input[name=password-prompt]").hide();
                                    $("input[name=password]").show();
                                }
                            }
                            $("#user-login-no-list").append('<li id="' + l + '-choose" userId="' + l + '">' + l + '<span class="glyphicon glyphicon-remove-circle" onclick="javascript:Login.deleteLoginNo(this)"></span></li>');
                            $("#" + l + "-choose").data("p", p);
                        }
                    }
                } else {
                    $("#user-login-no-list").hide();
                }
            }
            var today = new Date();
            var currentYear = today.getFullYear();
            $("#footer-copyright-year").html(currentYear);
        },
        initEvent : function() {
            $(document).on("click", "#login-btn", function() {
                t.submitLogin();
            });
            $(document).on("click", "#kaptchaImage", function() {
                $(this).hide().attr('src', 'kaptcha/getKaptchaImage?random=' + Math.floor(Math.random() * 100)).fadeIn();
            });
            $(document).on("click", "#user-login-no-list li", function() {
                var l = $(this).attr("userId");
                $("#userId").val(l);
                var p = $("#" + l + "-choose").data("p");
                if (p && p != "") {
                    $("#password").val(p);
                    $("#remember-password").prop('checked', true)
                    $("#password").css("color", "#000");
                    $("input[name=password-prompt]").hide();
                    $("input[name=password]").show();
                } else {
                    $("#password").val("");
                    $("#remember-password").prop('checked', false)
                    $("#password").css("color", "#999");
                    $("input[name=password-prompt]").show();
                    $("input[name=password]").hide();
                }
            });

        },
        submitLogin : function() {
            var userId = $("#userId").val();
            var password = $("#password").val();
            var code = $("#kaptcha").val();
            if ((!userId && userId.trim() == "") || userId.trim() == "登录账号") {
                t.setTips("请输入登录账号");
                return;
            }
            if (!password && password == "") {
                t.setTips("请输入密码");
                return;
            }
            if (!code && code == "") {
                t.setTips("请输入验证码");
                return;
            } else {
                if (code.length == 4) {
                    if (code && code.length == 4) {
                        Ajax.call({
                            url : "kaptcha/validateKaptchaCode",
                            p : {
                                code : code
                            },
                            f : function(data) {
                                if (data.success == true) {
                                    t.validatePassword(userId, password);
                                } else {
                                    t.changeCode();
                                    if (data.msg) {
                                        t.setTips(data.msg);
                                    } else {
                                        t.setTips("验证码错误");
                                    }
                                }
                            }
                        });
                    }
                } else {
                    t.setTips("请输入正确验证码");
                    return;
                }
            }

        },
        validatePassword : function(userId, password) {
            Ajax.call({
                url : "uam/loginValidate",
                p : {
                    userId : userId,
                    password : password,
                    rememberPassword : $("#remember-password").is(':checked') == true ? true : false
                },
                f : function(data) {
                    if (data.success == true) {
                        window.location.href = "home";
                    } else {
                        if (data.msg) {
                            t.setTips(data.msg);
                        } else {
                            t.setTips("用户名或密码不正确");
                            t.changeCode();
                        }
                    }
                }
            });
        },
        changeCode : function() {
            $('#kaptchaImage').hide().attr('src', 'kaptcha/getKaptchaImage?random=' + Math.floor(Math.random() * 100)).fadeIn();
        },
        //监控回车事件
        bindEnter : function(obj) {
            if (obj.keyCode == 13) {
                Login.submitLogin();
                obj.returnValue = false;
            }
        },
        validateCode : function(code) {
            if (code && code.length == 4) {
                Ajax.call({
                    url : "kaptcha/validateKaptchaCode",
                    p : {
                        code : code
                    },
                    f : function(data) {
                        if (data.success == true) {

                        } else {
                            if (data.msg) {
                                t.setTips(data.msg);
                            } else {
                                t.setTips("验证码错误");
                            }
                        }
                    }
                });
            }
        },
        setTips : function(val) {
            $("#login-result-tip").html(val);
        },
        deleteLoginNo : function(deleteBtn) {
            var userId = $(deleteBtn).parent().attr("userId");
            Ajax.call({
                url : "uam/deleteLogonNoHistory",
                p : {
                    userId : userId
                },
                f : function(data) {
                    if (data && data.success == true) {
                        $(deleteBtn).parent().remove();
                    } else {
                        Notice.error("删除历史失败");
                    }
                }
            });
        }
    }
    return t;
}();

$(function() {
    var usernameDefStr = '登录账号';
    $("#userId").focus(function() {
        $("#user-login-no-list").parent().removeClass("open");
        Login.setTips("");
        $(this).css("color", "#000");
        if ($(this).val() == usernameDefStr) {
            $(this).val("");
        }
    });
    $("#userId").blur(function() {
        if ($(this).val() == "") {
            $(this).val(usernameDefStr);
            if (usernameDefStr != '登录账号') {
                $("#userId").css("color", "#000");
            } else {
                $(this).css("color", "#999");
            }
        }
    });
    if (usernameDefStr != '登录账号') {
        $("#userId").css("color", "#000");
    }
    $("input[name=password-prompt]").focus(function() {
        Login.setTips("");
        $("input[name=password-prompt]").hide();
        $("input[name=password]").show().focus();
    });
    $("input[name=password]").blur(function() {
        if ($("input[name=password]").val() == "") {
            $("input[name=password]").hide();
            $("input[name=password-prompt]").show();
        }
    });
    $("input[name=password]").focus(function() {
        $(this).css("color", "#000");
    });

    var kaptchaDefStr = "验证码";
    $("#kaptcha").val(kaptchaDefStr);
    $("#kaptcha").css("color", "#999");
    $("#kaptcha").focus(function() {
        Login.setTips("");
        $(this).css("color", "#000");
        if ($(this).val() == kaptchaDefStr) {
            $(this).val("");
        }
    });
    $("#kaptcha").blur(function() {
        if ($(this).val() == "") {
            $(this).val(kaptchaDefStr);
            if (kaptchaDefStr != '验证码') {
                $("#kaptcha").css("color", "#000");
            } else {
                $(this).css("color", "#999");
            }
        }
    });
});