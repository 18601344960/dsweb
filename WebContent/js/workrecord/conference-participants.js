/**
 * @description 组织生活参与人员
 * @author 易文俊
 * @since 2016-06-24
 */
var ConferenceParticipants = function() {
    var t = {
        mainContainer : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initEvent();
        },
        initEvent : function() {
        },
        // 选择添加参会人员对话框
        addParticipantsDialog : function(isAdd) {
            var data = {
                isAdd : isAdd
            }
            var conferenceId = $("#conference-form #conferenceId").val();
            $("#conference-form").data("conferenceId", conferenceId);
            $("#choose-participants-list").html(nunjucks.render(Global.appName + '/tpl/conference/choose-participants-list.html', data));
            $("#choose-participants-table").bootstrapTable('destroy');
            $("#choose-participants-table").bootstrapTable({
                pagination : (Global.ccpartyType == Global.ccpartyBranch) ? false : true,
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId
                    })
                    return params;
                }
            });
        },
        // 选择确认
        addParticipants : function(isAdd) {
            if (isAdd) {
                var participantsType = $('input[name="participantsType"]:checked').val();
                var attendance = parseInt($('#attendance').val());
                if (participantsType == 0) {
                    var selectedUserRows = $("#choose-participants-table").bootstrapTable('getSelections');
                    if (selectedUserRows.length <= 0) {
                        Notify.notice("请至少选择一位人员");
                        return;
                    }
                    var participantsHtml = "";
                    for (var i = 0; i < selectedUserRows.length; i++) {
                        var userId = selectedUserRows[i].id;
                        var existUser = false;
                        $("#choose-participant-member-list .member-name").each(function() {
                            if (userId == $(this).attr("userId")) {
                                existUser = true;
                            }
                        })
                        if (!existUser) {
                            participantsHtml += '<div class="member-name" userId="' + selectedUserRows[i].id + '" userType="0"><div>' + selectedUserRows[i].name + '<i class="fa fa-times" title="移除" onclick="ConferenceParticipants.removeAddParticipants(this)"></i></div></div>';
                            attendance += 1;
                        }
                    }
                    $("#choose-participant-member-list").append(participantsHtml);
                    $("#choose-participants-list").html("");
                } else {
                    var participants = $("#out-participants").val().trim();
                    if (participants.length <= 0) {
                        Notify.notice("请填写组织外参与人员");
                        return;
                    }
                    participants = participants.replace(/,/g, ";");
                    participants = participants.replace(/，/g, ";");
                    participants = participants.replace(/、/g, ";");
                    participants = participants.replace(/；/g, ";");
                    participants = participants.split(";");
                    var participantsHtml = "";
                    for (var i = 0; i < participants.length; i++) {
                        participantsHtml += '<div class="member-name" userName="' + participants[i] + '" userType="1"><div>' + participants[i] + '<i class="fa fa-times" title="移除" onclick="ConferenceParticipants.removeAddParticipants(this)"></i></div></div>';
                        attendance += 1;
                    }
                    $("#choose-participant-member-list").append(participantsHtml);
                    $("#choose-participants-list").html("");
                }
                $('#attendance').val(attendance);
            } else {
                var attendance = parseInt($('#attendance').val());
                var participantsType = $('input[name="participantsType"]:checked').val();
                if (participantsType == 0) {
                    var selectedUserRows = $("#choose-participants-table").bootstrapTable('getSelections');
                    if (selectedUserRows.length <= 0) {
                        Notify.notice("请至少选择一位人员");
                        return;
                    }
                    var userIds = [];
                    for (var i = 0; i < selectedUserRows.length; i++) {
                        var userId = selectedUserRows[i].id;
                        userIds.push(userId);
                        var existUser = false;
                        $("#choose-participant-member-list .member-name").each(function() {
                            if (userId == $(this).attr("userId")) {
                                existUser = true;
                            }
                        })
                        if (!existUser) {
                            attendance += 1;
                        }
                    }
                    var conferenceId = $("#conference-form").data("conferenceId");
                    Ajax.call({
                        url : 'obt/addConferenceInParticipants',
                        p : {
                            conferenceId : conferenceId,
                            userIds : JSON.stringify(userIds)
                        },
                        f : function(data) {
                            if (data && data.success) {
                                Notify.success(data.msg);
                                t.refreshParticipants();
                                $("#choose-participants-list").html("");
                            } else {
                                Notify.error("增加参会人员失败，请稍后再试。");
                            }
                        }
                    });
                } else {
                    var participants = $("#out-participants").val().trim();
                    if (participants.length <= 0) {
                        Notify.notice("请填写组织外参与人员");
                        return;
                    }
                    participants = participants.replace(/,/g, ";");
                    participants = participants.replace(/，/g, ";");
                    participants = participants.replace(/、/g, ";");
                    participants = participants.replace(/；/g, ";");
                    participants = participants.split(";");
                    var userNames = [];
                    for (var i = 0; i < participants.length; i++) {
                        userNames.push(participants[i]);
                        attendance += 1;
                    }
                    var conferenceId = $("#conference-form").data("conferenceId");
                    Ajax.call({
                        url : 'obt/addConferenceOutParticipants',
                        p : {
                            conferenceId : conferenceId,
                            userNames : JSON.stringify(userNames)
                        },
                        f : function(data) {
                            if (data && data.success) {
                                Notify.success(data.msg);
                                t.refreshParticipants();
                                $("#choose-participants-list").html("");
                            } else {
                                Notify.error("增加参会人员失败，请稍后再试。");
                            }
                        }
                    });
                }
                $('#attendance').val(attendance);
            }
        },
        hideParticipants : function() {
            $("#choose-participants-list").html("");
        },
        // 刷新参与人员
        refreshParticipants : function() {
            var conferenceId = $("#conference-form #conferenceId").val();
            Ajax.call({
                url : 'obt/getConferenceParticipants',
                p : {
                    conferenceId : conferenceId,
                },
                f : function(data) {
                    if (data && data.rows) {
                        var participants = "";
                        for ( var index in data.rows) {
                            var participant = data.rows[index];
                            participants = participants + '<div class="member-name" userId="' + participant.userId + '"><div>' + participant.userName + '<i class="fa fa-times" title="移除" onclick="ConferenceParticipants.removeParticipant(\'' + participant.id + '\')"></i></div></div>';
                        }
                        $("#choose-participant-member-list").html(participants);
                    } else {
                        Notify.error("刷新参与人员列表失败");
                    }
                }
            });
        },
        removeAddParticipants : function($this) {
            $($this).parent().parent().remove();
            var attendance = parseInt($('#attendance').val());
            if (attendance > 0) {
                $('#attendance').val(attendance - 1);
            }
        },
        // 移除参与人员
        removeParticipant : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认移除？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'obt/deleteConferenceParticipants',
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    t.refreshParticipants();
                                    var attendance = parseInt($('#attendance').val());
                                    if (attendance > 0) {
                                        $('#attendance').val(attendance - 1);
                                    }
                                } else {
                                    Notify.error("移除失败，请稍后再试。");
                                }
                            }
                        });
                    }
                }
            });
        },
        participantsTypeClick : function(participantsType) {
            if (participantsType == 0) {
                $("#in-participants-container").show();
                $("#out-participants-container").hide();
            } else {
                $("#in-participants-container").hide();
                $("#out-participants-container").show();
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