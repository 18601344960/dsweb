/**
 * @description 我的信息
 * @author 赵子靖
 * @since 2016-06-26
 */
var MyInfo = function() {
    var t = {
        mainContainer : '',
        userId : '',
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
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/myspace/my-info/my-info-index.html'));
            t.loadHeadInfo();
            if (Global.userType == 0 || Global.userType == 1) {
                $("#my-info-content").show();
                t.loadUserBaseInfo();
                t.loadPartymemberInfo();
            } else {
                $("#my-info-content").hide();
            }
        },
        loadHeadInfo : function() {
            t.loadWait('head-info');
            Ajax.call({
                url : "uam/getUserInfosForMyInfo",
                p : {
                    userId : Global.userId
                },
                f : function(data) {
                    $.extend(data, Global);
                    data.random = new Date().getTime();
                    var date = new Date();
                    var nowDate = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                    if (data.item.partyMember != null) {
                        data.item.partyAge = JSUtils.dateDiff(data.item.partyMember.joinTime, nowDate);
                    }
                    $("#head-info").html(nunjucks.render(Global.appName + '/tpl/myspace/my-info/show/show-head-info.html', data));
                }
            });
        },
        // 用户头部信息编辑页面跳转
        editUserHeadPage : function(userId) {
            t.loadWait('head-info');
            Ajax.call({
                url : "uam/getUserInfosForMyInfo",
                p : {
                    userId : userId
                },
                f : function(data) {
                    $.extend(data, Global);
                    data.random = new Date().getTime();
                    $("#head-info").html(nunjucks.render(Global.appName + '/tpl/myspace/my-info/edit/edit-head-info.html', data));
                }
            });
        },
        // 修改头部信息
        updateUserHeadInfo : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认保存？",
                callback : function(result) {
                    if (result) {
                        var userId = $("#edit-head-info #userId").val();
                        var name = $("#edit-head-info #name").val();
                        var namePhoneticize = $("#edit-head-info #namePhoneticize").val();
                        if (CheckInputUtils.isEmpty(name)) {
                            Notify.info("请输入姓名。");
                            $("#edit-head-info #name").focus();
                            return;
                        }
                        Ajax.call({
                            url : 'uam/updateUserLittleInfoForMyInfo',
                            p : {
                                userId : userId,
                                name : name,
                                namePhoneticize : namePhoneticize
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success(data.msg);
                                    t.loadHeadInfo();
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });
                    }
                }
            });
        },
        // 加载用户基本信息
        loadUserBaseInfo : function() {
            t.loadWait('user-base-info');
            Ajax.call({
                url : "uam/getUserInfosForMyInfo",
                p : {
                    userId : Global.userId
                },
                f : function(data) {
                    $.extend(data, Global);
                    $("#user-base-info").html(nunjucks.render(Global.appName + '/tpl/myspace/my-info/show/show-user-base-info.html', data));
                }
            });
        },
        // 用户基本信息编辑加载页面
        editUserBaseInfoPage : function(userId) {
            t.loadWait('user-base-info');
            Ajax.call({
                url : "uam/getUserInfosForMyInfo",
                p : {
                    userId : userId
                },
                f : function(data) {
                    $.extend(data, Global);
                    $("#user-base-info").html(nunjucks.render(Global.appName + '/tpl/myspace/my-info/edit/edit-user-base-info.html', data));
                    $("input[type=radio][name='sex'][value=" + data.item.gender + "]").attr("checked", 'checked');
                    SelectTree.initSelectTree("#nation-tree", 'sys/getCodeTreeByParentId', "A0121"); // 民族
                    $("#nationId").val(data.item.nation);
                    $("#nationName").val(Global.getCodeName("A0121." + data.item.nation));
                    SelectTree.initSelectTree("#birthPlace-tree", 'sys/getCodeTreeByParentId', "A0114"); // 籍贯
                    $("#birthPlaceId").val(data.item.birthPlace);
                    $("#birthPlaceName").val(Global.getCodeName("A0114." + data.item.birthPlace));
                    SelectTree.initSelectTree("#education-tree", 'sys/getCodeTreeByParentId', "A0405"); // 学历
                    $("#educationId").val(data.item.education);
                    $("#educationName").val(Global.getCodeName("A0405." + data.item.education));
                    SelectTree.initSelectTree("#degree-tree", 'sys/getCodeTreeByParentId', "A0440"); // 学位
                    $("#degreeName").val(Global.getCodeName("A0440." + data.item.degree));
                    $("#degreeId").val(data.item.degree);
                }
            });
        },
        // 保存用户基本信息
        updateUserBaseInfo : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认保存？",
                callback : function(result) {
                    if (result) {
                        var userId = $("#user-base-info #userId").val();
                        var gender = $("input[name='sex']:checked").val(); // 性别
                        var birthday = $("#user-base-info #birthday").val();
                        var nation = $("#user-base-info #nationId").val(); // 民族
                        var birthPlace = $("#user-base-info #birthPlaceId").val(); // 籍贯
                        var education = $("#user-base-info #educationId").val(); // 学历
                        var degree = $("#user-base-info #degreeId").val(); // 学位
                        var mobile = $("#user-base-info #mobile").val();//手机
                        var officePhone = CheckInputUtils.splitSpaceForStrAround($("#user-base-info #officePhone").val());// 办公电话
                        var email = CheckInputUtils.splitSpaceForStrAround($("#user-base-info #email").val()); // 电邮
                        var address = $("#user-base-info #address").val(); // 家庭地址
                        Ajax.call({
                            url : 'uam/updateUserBaseInfoForMyInfo',
                            p : {
                                userId : userId,
                                gender : gender,
                                birthday : birthday,
                                nation : nation,
                                birthPlace : birthPlace,
                                education : education,
                                degree : degree,
                                mobile : mobile,
                                officePhone : officePhone,
                                email : email,
                                address : address
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success(data.msg);
                                    t.loadUserBaseInfo();
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });
                    }
                }
            });
        },
        // 加载党员信息
        loadPartymemberInfo : function() {
            t.loadWait('partymember-info');
            Ajax.call({
                url : "uam/getUserInfosForMyInfo",
                p : {
                    userId : Global.userId
                },
                f : function(data) {
                    $.extend(data, Global);
                    //非党员
                    if (data.item.partyMember != null) {
                        $("#partymember-info").html(nunjucks.render(Global.appName + '/tpl/myspace/my-info/show/show-partymember-info.html', data));
                    } else {
                        $("#partymember-info").remove();
                        $('#partymember-line').remove()
                    }
                }
            });
        },
        // 党员信息编辑加载页面
        editPartymemberInfoPage : function(userId) {
            t.loadWait('partymember-info');
            Ajax.call({
                url : "uam/getUserInfosForMyInfo",
                p : {
                    userId : userId
                },
                f : function(data) {
                    $.extend(data, Global);
                    $("#partymember-info").html(nunjucks.render(Global.appName + '/tpl/myspace/my-info/edit/edit-partymember-info.html', data));
                    $("#partymember-info #joinType").val(data.item.partyMember.joinType);
                    $("#partymember-info #joinActivity").val(data.item.partyMember.joinActivity);
                }
            });
        },
        //保存党员信息
        updatePartymemberInfo : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认保存？",
                callback : function(result) {
                    if (result) {
                        var userId = $("#partymember-info #userId").val();
                        var joinCurrentTime = $("#partymember-info #joinCurrentTime").val();
                        var joinType = $("#partymember-info #joinType").val();
                        var activistTime = $("#partymember-info #activistTime").val();
                        var applyTime = $("#partymember-info #applyTime").val();
                        var targetTime = $("#partymember-info #targetTime").val();
                        var trainer = $("#partymember-info #trainer").val();
                        var passTime = $("#partymember-info #passTime").val();
                        var auditTime = $("#partymember-info #auditTime").val();
                        var joinTime = $("#partymember-info #joinTime").val();
                        var fee = $("#partymember-info #fee").val();
                        var introducer = $("#partymember-info #introducer").val();
                        var joinActivity = $("#partymember-info #joinActivity").val();
                        Ajax.call({
                            url : 'org/updatePartymemberInfoForMyInfo',
                            p : {
                                userId : userId,
                                joinCurrentTime : joinCurrentTime,
                                joinType : joinType,
                                activistTime : activistTime,
                                applyTime : applyTime,
                                targetTime : targetTime,
                                trainer : trainer,
                                passTime : passTime,
                                auditTime : auditTime,
                                joinTime : joinTime,
                                fee : fee,
                                introducer : introducer,
                                joinActivity : joinActivity
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success(data.msg);
                                    t.loadPartymemberInfo();
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });

                    }
                }
            });
        },
        // 加载等待
        loadWait : function(divId) {
            var loadId = "loader_" + divId;
            Global.initLoader("#" + divId);
        }
    };
    return t;
}();