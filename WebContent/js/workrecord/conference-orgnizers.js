/**
 * @description 组织生活组织者
 * @author 易文俊
 * @since 2016-07-12
 */
var ConferenceOrgnizer = function() {
    var t = {
        mainContainer : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initEvent();
        },
        initEvent : function() {
        },
        // 选择添加组织者对话框
        addOrgnizerDialog : function(isAdd) {
            var data = {
                isAdd : isAdd
            }
            var conferenceId = $("#conference-form #conferenceId").val();
            $("#conference-form").data("conferenceId", conferenceId);
            $("#choose-orgnizer-list").html(nunjucks.render(Global.appName + '/tpl/conference/choose-orgnizer-list.html', data));
            $("#choose-orgnizer-table").bootstrapTable('destroy');
            $("#choose-orgnizer-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId
                    })
                    return params;
                }
            });
        },
        // 选择确认
        addOrgnizer : function(isAdd) {
            if (isAdd) {
                var orgnizerType = $('input[name="orgnizerType"]:checked').val();
                if (orgnizerType == 0) {
                    var selectedUserRows = $("#choose-orgnizer-table").bootstrapTable('getSelections');
                    if (selectedUserRows.length <= 0) {
                        Notify.notice("请至少选择一位人员");
                        return;
                    }
                    var orgnizers = "";
                    for (var i = 0; i < selectedUserRows.length; i++) {
                        orgnizers += '<div class="member-name" userId="' + selectedUserRows[i].id + '" userType="0"><div>' + selectedUserRows[i].name + '<i class="fa fa-times" title="移除" onclick="ConferenceOrgnizer.removeAddOrgnizer(this)"></i></div></div>';
                    }
                    $("#choose-orgnizer-member-list").append(orgnizers);
                    $("#choose-orgnizer-list").html("");
                } else {
                    var orgnizer = $("#out-orgnizer").val().trim();
                    if (orgnizer.length <= 0) {
                        Notify.notice("请填写组织外组织者");
                        return;
                    }
                    orgnizer = orgnizer.replace(/,/g, ";");
                    orgnizer = orgnizer.replace(/，/g, ";");
                    orgnizer = orgnizer.replace(/、/g, ";");
                    orgnizer = orgnizer.replace(/；/g, ";");
                    orgnizer = orgnizer.split(";");
                    var orgnizers = "";
                    for (var i = 0; i < orgnizer.length; i++) {
                        orgnizers += '<div class="member-name" userName="' + orgnizer[i] + '" userType="1"><div>' + orgnizer[i] + '<i class="fa fa-times" title="移除" onclick="ConferenceOrgnizer.removeAddOrgnizer(this)"></i></div></div>';
                    }
                    $("#choose-orgnizer-member-list").append(orgnizers);
                    $("#choose-orgnizer-list").html("");
                }
            } else {
                var orgnizerType = $('input[name="orgnizerType"]:checked').val();
                if (orgnizerType == 0) {
                    var selectedUserRows = $("#choose-orgnizer-table").bootstrapTable('getSelections');
                    if (selectedUserRows.length <= 0) {
                        Notify.notice("请至少选择一位人员");
                        return;
                    }
                    var userIds = [];
                    for (var i = 0; i < selectedUserRows.length; i++) {
                        userIds.push(selectedUserRows[i].id);
                    }
                    if (!isAdd) {
                        var conferenceId = $("#conference-form").data("conferenceId");
                        Ajax.call({
                            url : 'obt/addConferenceInOrgnizer',
                            p : {
                                conferenceId : conferenceId,
                                userIds : JSON.stringify(userIds)
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    t.refreshOrgnizer();
                                    $("#choose-orgnizer-list").html("");
                                } else {
                                    Notify.error("增加组织者失败，请稍后再试。");
                                }
                            }
                        });
                    }
                } else {
                    var orgnizer = $("#out-orgnizer").val().trim();
                    if (orgnizer.length <= 0) {
                        Notify.notice("请填写组织外组织者");
                        return;
                    }
                    orgnizer = orgnizer.replace(/,/g, ";");
                    orgnizer = orgnizer.replace(/，/g, ";");
                    orgnizer = orgnizer.replace(/、/g, ";");
                    orgnizer = orgnizer.replace(/；/g, ";");
                    orgnizer = orgnizer.split(";");
                    var userNames = [];
                    for (var i = 0; i < orgnizer.length; i++) {
                        userNames.push(orgnizer[i]);
                    }
                    if (!isAdd) {
                        var conferenceId = $("#conference-form").data("conferenceId");
                        Ajax.call({
                            url : 'obt/addConferenceOutOrgnizer',
                            p : {
                                conferenceId : conferenceId,
                                userNames : JSON.stringify(userNames)
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    t.refreshOrgnizer();
                                    $("#choose-orgnizer-list").html("");
                                } else {
                                    Notify.error("增加组织者失败，请稍后再试。");
                                }
                            }
                        });
                    }
                }
            }
        },
        hideOrgnizer : function() {
            $("#choose-orgnizer-list").html("");
        },
        // 刷新组织者
        refreshOrgnizer : function() {
            var conferenceId = $("#conference-form #conferenceId").val();
            Ajax.call({
                url : 'obt/getConferenceOrgnizer',
                p : {
                    conferenceId : conferenceId,
                },
                f : function(data) {
                    if (data && data.rows) {
                        var orgnizers = "";
                        for ( var index in data.rows) {
                            var orgnizer = data.rows[index];
                            orgnizers = orgnizers + '<div class="member-name" ><div>' + orgnizer.userName + '<i class="fa fa-times" title="移除" onclick="ConferenceOrgnizer.removeOrgnizer(\'' + orgnizer.id + '\')"></i></div></div>';
                        }
                        $("#choose-orgnizer-member-list").html(orgnizers);
                    } else {
                        Notify.error("刷新组织者列表失败");
                    }
                }
            });
        },
        removeAddOrgnizer: function($this) {
            $($this).parent().parent().remove();
        },
        // 移除组织者
        removeOrgnizer : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认移除？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'obt/deleteConferenceOrgnizer',
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    t.refreshOrgnizer();
                                } else {
                                    Notify.error("移除失败，请稍后再试。");
                                }
                            }
                        });
                    }
                }
            });
        },
        orgnizerTypeClick : function(orgnizerType) {
            if (orgnizerType == 0) {
                $("#in-orgnizer-container").show();
                $("#out-orgnizer-container").hide();
            } else {
                $("#in-orgnizer-container").hide();
                $("#out-orgnizer-container").show();
            }
        },
        ccpartyFormatter : function(value, row) {
            if (value != null) {
                return value.ccparty.name;
            }
        },
        genderFormatter : function(value, row) {
            if (value) {
                return Global.getCodeName('A0107.' + value);
            }
        }
    }
    return t;
}();