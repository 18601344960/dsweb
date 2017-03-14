/**
 * @description 用户工具类
 * @author 赵子靖
 * @since 2016-03-17
 */
var UserUtils = function() {
    var t = {
        // 登录账号是否存在
        checkLoginNo : function(loginNo, inputId) {
            if (CheckInputUtils.isEmpty(loginNo)) {
                Notify.notice("请输入登录账号。");
                $("#" + inputId).focus();
                return;
            } else {
                Ajax.call({
                    url : 'uam/checkUserLoginNo',
                    p : {
                        loginNo : loginNo
                    },
                    f : function(data) {
                        if (!data.success) {
                            Notify.notice(data.msg);
                            $("#" + inputId).focus();
                            return;
                        }
                    }
                });
            }
        },
        checkIdNumber : function(idNumber, inputId) {
            if (CheckInputUtils.isEmpty(idNumber)) {
                Notify.notice("请输入身份证号码。");
                $("#" + inputId).focus();
                return;
            } else {
                Ajax.call({
                    url : 'uam/checkUserIdNumber',
                    p : {
                        idNumber : idNumber
                    },
                    f : function(data) {
                        if (!data.success) {
                            Notify.notice(data.msg);
                            $("#" + inputId).focus();
                            return;
                        }
                    }
                });
            }
        },
        // 发展阶段中新增验证身份证Id，如果身份证存在则获取原用户信息并且将相关input框赋值
        developmentCheckIdNumber : function(idNumber, inputId, dialogId) {
            if (CheckInputUtils.isEmpty(idNumber)) {
                Notify.notice("请输入身份证号码。");
                $("#" + inputId).focus();
                return;
            } else {
                Ajax
                        .call({
                            url : 'uam/developmentCheckUserIdNumber',
                            p : {
                                idNumber : idNumber
                            },
                            f : function(data) {
                                if (!data.success) {
                                    Notify.info("提醒：" + data.msg);
                                    $("#" + dialogId + " #userId").val(data.item.id);// 用户ID
                                    $("#" + dialogId + " #loginNo").val(data.item.loginNo);// 登录账号
                                    $("#" + dialogId + " #loginNo").attr('readonly', 'readonly');
                                    $("#" + dialogId + " #name").val(data.item.name);// 姓名
                                    $("#" + dialogId + " input[type=radio][name='gender'][value=" + data.item.gender + "]").attr("checked", 'checked');// 性别
                                    $("#" + dialogId + " #birthDay").val(Global.getDate(data.item.name));// 出生日期
                                    $("#" + dialogId + " #headImage")
                                            .html(
                                                    '<img id="headImgId" src="uam/getHeadImg?userId='
                                                            + data.item.id
                                                            + '&random='
                                                            + new Date().getTime()
                                                            + '" onError="Global.onIconError(this)" style="cursor: pointer;border: 1px solid #DBDBDB;width:130px;height:180px;" title="点击更换" onclick="DevelopmentHandle.replaceHeadImg()">');
                                    $("#" + dialogId + " #userNationName").val(Global.getCodeName('A0121.' + data.item.nation)); // 民族
                                    $("#" + dialogId + " #userNationId").val(data.item.nation);
                                    $("#" + dialogId + " #userBirthPlaceName").val(Global.getCodeName('A0114.' + data.item.birthPlace));// 籍贯
                                    $("#" + dialogId + " #userBirthPlaceId").val(data.item.birthPlace);
                                    $("#" + dialogId + " #mobile").val(data.item.mobile);// 联系电话
                                    $("#" + dialogId + " #address").val(data.item.address);// 居住地
                                    $("#" + dialogId + " #educationName").val(Global.getCodeName('A0405.' + data.item.education));// 学历
                                    $("#" + dialogId + " #educationId").val(data.item.education);
                                    $("#" + dialogId + " #degreeName").val(Global.getCodeName('A0440.' + data.item.degree));// 学位
                                    $("#" + dialogId + " #degreeId").val(data.item.degree);
                                }
                            }
                        });
            }
        },
        // 调整还更换头像页面
        replaceUserHeadImgPage : function(userId) {
            var data = {
                userId : userId
            }
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/common/replace-user-head-img.html', data));
            $("#replace-user-head-img").modal({});
        },
        // 保存用户头像
        saveUserHeadImg : function() {
            var headImg = $("#replace-user-head-img #headImg").val();
            var userId = $("#replace-user-head-img #userId").val();
            // 对头像进行验证
            var suffix = headImg.substring(headImg.indexOf("."), headImg.length);
            if (!CheckInputUtils.checkIsImage(suffix)) {
                Notify.notice("请选择正确的图片，格式后缀名应以'.jpg', '.jpeg', '.png','.bmp'结尾，大小在500KB之内！");
                return;
            }
            // 使用ajaxfileupload异步上传文件
            $.ajaxFileUpload({
                url : 'uam/replaceHeadImg', // 用于文件上传的服务器端请求地址
                data : {
                    userId : userId
                }, // 此参数非常严谨，写错一个引号都不行
                secureuri : false, // 是否需要安全协议，一般设置为false
                fileElementId : 'headImg', // 文件上传域的ID
                dataType : 'json', // 返回值类型 一般设置为json
                success : function(data, status) // 服务器成功响应处理函数
                {
                    Notify.info(data.msg);
                    // 将image href连接更换
                    $("#headImgId").attr("src", "uam/getHeadImg?userId=" + userId + "&timestam=" + new Date().getTime());
                    $("#replace-user-head-img").modal('toggle');
                    $("#head_" + userId).attr("src", "uam/getHeadImg?userId=" + userId + "&timestam=" + new Date().getTime());
                },
                error : function(data, status, e) // 服务器响应失败处理函数
                {
                    Notify.error(data.msg);
                }
            })// end ajaxfileupload
        },
        // 删除用户头像
        deleteUserHeadImg : function(userId) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除头像？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'uam/deleteUserHeadImg',
                            p : {
                                userId : userId
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.notice(data.msg);
                                    $("#headImgId").attr("src", "uam/getHeadImg?userId=" + userId + "&timestam=" + new Date().getTime());
                                    $("#head_" + userId).attr("src", "uam/getHeadImg?userId=" + userId + "&timestam=" + new Date().getTime());
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });
                    }
                }
            });

        }
    }
    return t;
}();