/**
 * @description 党小组成员
 * @author yiwenjun
 * @since 2016-08-02
 */
var PartyGroupMember = function() {
    var t = {
        mainContainer : '',
        currentCcpartyId : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initEvent();
        },
        initEvent : function() {
        },
        render : function(currentCcpartyId) {
            t.currentCcpartyId = currentCcpartyId;
            Ajax.call({
                url : "org/getCcpartyGroupList",
                p : {
                    ccpartyId : currentCcpartyId
                },
                f : function(data) {
                    if (data && data.rows && data.rows.length > 0) {
                        data.Global = Global;
                        $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-group-member.html', data));
                        for ( var index in data.rows) {
                            var row = data.rows[index];
                            t.initPartyGroupMemberTable(row.id);
                        }
                    }
                }
            });
        },
        initPartyGroupMemberTable : function(groupId) {
            $("#" + groupId).bootstrapTable('destroy');
            $("#" + groupId).bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        groupId : groupId
                    })
                    return params;
                }
            });
        },
        // 添加小组成员对话框
        addPartyGroupMemberDialog : function(groupId) {
            var data = {
                groupId : groupId
            }
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-group-member-choose.html', data));
            $("#choose-party-group-member-dialog").modal({});
            $("#choose-party-group-member-table").bootstrapTable('destroy');
            $("#choose-party-group-member-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : t.currentCcpartyId
                    })
                    return params;
                }
            });
        },
        // 添加小组成员
        addPartyGroupMember : function(groupId) {
            var selectedRows = $("#choose-party-group-member-table").bootstrapTable('getSelections');
            if (selectedRows.length <= 0) {
                Notify.notice("请至少选择一名党员");
                return;
            }
            var userIds = [];
            for (var i = 0; i < selectedRows.length; i++) {
                userIds.push(selectedRows[i].id);
            }
            bootbox.confirm({
                size : 'small',
                message : "确认添加小组成员？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/addPartyGroupMember",
                            p : {
                                groupId : groupId,
                                userIds : JSON.stringify(userIds)
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("添加成功");
                                    $("#choose-party-group-member-dialog").modal("toggle");
                                    t.initPartyGroupMemberTable(groupId);
                                    $("#" + groupId + "-count").html(data.memberCount);
                                } else {
                                    Notify.error("添加失败，稍后再试");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 移除小组成员
        removePartyGroupMember : function(memberId, groupId) {
            bootbox.confirm({
                size : 'small',
                message : "确认移除党小组成员？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/removePartyGroupMember",
                            p : {
                                memberId : memberId
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("移除成功");
                                    t.initPartyGroupMemberTable(groupId);
                                    $("#" + groupId + "-count").html(data.memberCount);
                                } else {
                                    Notify.error("移除失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 设置组长
        setGroupLeader : function(groupId) {
            var selectedRows = $("#" + groupId).bootstrapTable('getSelections');
            if (selectedRows.length <= 0) {
                Notify.notice("请选择一名成员！");
                return;
            }
            if (selectedRows[0].type == 2) {
                Notify.info("该成员已经是组长身份");
                return;
            }
            var memberId = selectedRows[0].id;
            bootbox.confirm({
                size : 'small',
                message : "确认设置为本小组组长？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/setGroupLeader",
                            p : {
                                memberId : memberId
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("设置成功");
                                    t.initPartyGroupMemberTable(groupId);
                                } else {
                                    Notify.error("设置失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 设置副组长
        setGroupDeputyLeader : function(groupId) {
            var selectedRows = $("#" + groupId).bootstrapTable('getSelections');
            if (selectedRows.length <= 0) {
                Notify.notice("请选择一名成员！");
                return;
            }
            if (selectedRows[0].type == 1) {
                Notify.info("该成员已经是副组长身份");
                return;
            }
            var memberId = selectedRows[0].id;
            bootbox.confirm({
                size : 'small',
                message : "确认设置为本小组副组长？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/setGroupDeputyLeader",
                            p : {
                                memberId : memberId
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("设置成功");
                                    t.initPartyGroupMemberTable(groupId);
                                } else {
                                    Notify.error("设置失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 设置成员
        setGroupMember : function(groupId) {
            var selectedRows = $("#" + groupId).bootstrapTable('getSelections');
            if (selectedRows.length <= 0) {
                Notify.notice("请选择一名成员！");
                return;
            }
            if (selectedRows[0].type == 0) {
                Notify.info("已经是成员身份");
                return;
            }
            var memberId = selectedRows[0].id;
            bootbox.confirm({
                size : 'small',
                message : "确认设置为本小组成员？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/setGroupMember",
                            p : {
                                memberId : memberId
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("设置成功");
                                    t.initPartyGroupMemberTable(groupId);
                                } else {
                                    Notify.error("设置失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        iconFormatter : function(value, row) {
            var html = "";
            if (value) {
                html += '<div><img src="uam/getHeadImg?userId=' + value.id + '" gender="' + value.gender + '" onError="Global.onIconError(this)" width=40 height=50><div>';
            }
            return html;
        },
        memberOperator : function(value, row) {
            var html = "";
            html += '<a href="javascript:PartyGroupMember.removePartyGroupMember(\'' + row.id + '\',\'' + row.groupId + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>移除</a>';
            return html;
        },
        userNameFormatter : function(value, row) {
            if (value) {
                var name = value.name;
                if (row.type == 1) {
                    name += "（副组长）"
                } else if (row.type == 2) {
                    name += "（组长）"
                }
                return name;
            }
        },
        userTypeFormatter : function(value, row) {
            if (value) {
                return Global.getCodeName(value.type);
            }
        }
    }
    return t;
}();