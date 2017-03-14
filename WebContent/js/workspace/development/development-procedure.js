/**
 * @description 发展党员程序
 * @author yiwenjun
 * @since 2015-11-16
 */
var DevelopmentProcedure = function() {
    var t = {
        mainContainer : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initEvent();
            DevelopmentHandle.init(t.mainContainer);
            DevelopmentStepHandle.init(t.mainContainer);
        },
        initEvent : function() {
            $(document).on("click", ".development-phase-parent", function() {
                $(".development-phase").find(".development-phase-parent,.development-phase-child").each(function() {
                    $(this).removeClass("development-phase-selected");
                })
                var title = $(this).attr("title");
                $("#current-development-phase").html(title);
                $(this).addClass("development-phase-selected");
                var type = $(this).attr("type");
                t.initTable(type, null);
            })
            $(document).on("click", ".development-phase-child", function() {
                $(".development-phase").find(".development-phase-parent,.development-phase-child").each(function() {
                    $(this).removeClass("development-phase-selected");
                })
                var title = $(this).attr("title");
                $("#current-development-phase").html(title);
                $(this).addClass("development-phase-selected");
                var developtmentId = $(this).attr("developtmentId");
                t.initTable(null, developtmentId);
            })
            $(document).on("change", ".development-phase-set-child input", function() {
                var checked = $(this).prop("checked");
                var developtmentId = $(this).parent().attr("developtmentId");
                if (checked == true) {
                    $(this).parent().removeClass("development-phase-set-child-disable");
                    t.updateDevelopmentProcedure(developtmentId, 0)
                } else {
                    $(this).parent().addClass("development-phase-set-child-disable");
                    t.updateDevelopmentProcedure(developtmentId, 1)
                }
            })
        },
        render : function() {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/development/development-procedure-container.html', Global));
            t.initDevelopmentProcedure();
        },
        initDevelopmentProcedure : function() {
            Ajax.call({
                url : "obt/getDevelopmentProcedureList",
                p : {
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        $("#development-procedure-phase-container").html(nunjucks.render(Global.appName + '/tpl/workspace/development/development-procedure.html'));
                        $(".development-phase-child").each(function() {
                            var developtmentId = $(this).attr("developtmentId");
                            for ( var devId in data.config) {
                            }
                            if (data.config[developtmentId] == 1) {
                                $(this).addClass("development-phase-child-disable");
                            }
                        });

                        $("#development-phase-fisrt-child").addClass("development-phase-selected");
                        t.initTable(null, '0001');
                    } else {
                        Notify.error("获取配置失败！");
                    }
                }
            });
        },
        initTable : function(type, developmentId) {
            $("#development-party-member-table").bootstrapTable('destroy');
            if (type != null) {
                $("#development-party-member-table").bootstrapTable({
                    queryParams : function(params) {
                        $.extend(params, {
                            type : type,
                            ccpartyId : Global.ccpartyId
                        })
                        return params;
                    },
                    // 双击编辑
                    onDblClickRow : function(row) {
                        DevelopmentProcedure.dbClickDevelopmentEdit(row);
                    }
                });
            } else {
                $("#development-party-member-table").bootstrapTable({
                    queryParams : function(params) {
                        $.extend(params, {
                            developmentId : developmentId,
                            ccpartyId : Global.ccpartyId
                        })
                        return params;
                    },
                    // 双击编辑
                    onDblClickRow : function(row) {
                        DevelopmentProcedure.dbClickDevelopmentEdit(row);
                    }
                });
            }

        },
        dbClickDevelopmentEdit : function(row) {
            // 获取用户、党员信息进行编辑
            Ajax.call({
                url : "uam/loadUserInfoByUserId",
                p : {
                    userId : row.user.id
                },
                f : function(data) {
                    if (data && data.item) {
                        if (row.type == 0) {
                            // 申请人
                            DevelopmentHandle.editProposer(data);
                        } else if (row.type == 1) {
                            // 积极分子
                            DevelopmentHandle.editActivist(data);
                        } else if (row.type == 2) {
                            // 发展对象
                            DevelopmentHandle.editDevelopmentObject(data);
                        } else if (row.type == 3) {
                            // 预备党员
                            DevelopmentHandle.editProbationaryPartyMember(data);
                        } else if (row.type == 4) {
                            // 正式党员
                            DevelopmentHandle.editPartyMember(data);
                        }
                    } else {
                        Notify.error("获取信息失败，请稍后再试。");
                    }
                }
            });
        },
        setDevelopmentProcedure : function() {
            Ajax.call({
                url : "obt/getDevelopmentProcedureList",
                p : {
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/development/development-procedure-set-container.html', data));
                        $("#develop-party-member-dialog").modal({});
                        $(".development-phase-set-child").each(function() {
                            var developtmentId = $(this).attr("developtmentId");
                            for ( var devId in data.config) {
                                if (devId == developtmentId && data.config[developtmentId] == 0) {
                                    $(this).find("input").each(function() {
                                        $(this).attr("checked", true);
                                    });
                                }
                            }
                            if (data.config[developtmentId] == 1) {
                                $(this).addClass("development-phase-set-child-disable");
                            }
                        });
                    } else {
                        Notify.error("获取配置失败！");
                    }
                }
            });
        },
        updateDevelopmentProcedure : function(developtmentId, status) {
            Ajax.call({
                url : "obt/updateDevelopmentProcedureConfig",
                p : {
                    ccpartyId : Global.ccpartyId,
                    developtmentId : developtmentId,
                    status : status
                },
                f : function(data) {
                    if (data) {
                        Notify.success("更新配置成功！");
                        t.initDevelopmentProcedure();
                    } else {
                        Notify.error("更新配置失败！");
                    }
                }
            });
        },
        // 下一步资料填写对话框
        nextStepDevelopmentProcedureDialog : function(partymemberId, processId) {
            // 发送请求获取该阶段下的填写数据
            Ajax.call({
                url : "org/getPartymemberDevelopmentProcedureInfo",
                p : {
                    partymemberId : partymemberId,
                    processId : processId
                },
                f : function(data) {
                    if (data && data.row) {
                        $.extend(data.row, Global);
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/development/proposer-' + data.row.process.id + '-dialogs.html', data.row));
                        $("#proposer-" + data.row.process.id + "-dialog").modal({});
                        if (data.row.process.id == '0103') {
                            $("#proposer-0103-dialog #groupOpinion04").val(data.row.developmentInfo.groupOpinion04);
                        } else if (data.row.process.id == '0104') {
                            $("#proposer-0104-dialog #branchOpinion05").val(data.row.developmentInfo.branchOpinion05);
                        } else if (data.row.process.id == '0202') {
                            $("#proposer-0202-dialog #politicalaAudit07").val(data.row.developmentInfo.politicalaAudit07);
                        } else if (data.row.process.id == '0203') {
                            $("#proposer-0203-dialog #cultivateResult08").val(data.row.developmentInfo.cultivateResult08);
                        } else if (data.row.process.id == '0204') {
                            $("#proposer-0204-dialog #auditResult09").val(data.row.developmentInfo.auditResult09);
                        } else if (data.row.process.id == '0206') {
                            $("#proposer-0206-dialog #branchDiscussResult11").val(data.row.developmentInfo.branchDiscussResult11);
                            $("#proposer-0206-dialog #joinCcpartyType11").val(data.row.developmentInfo.joinCcpartyType11);
                        } else if (data.row.process.id == '0208') {
                            $("#proposer-0208-dialog #auditResult13").val(data.row.developmentInfo.auditResult13);
                            $("#proposer-0208-dialog #isSpecialPartymember13").val(data.row.developmentInfo.isSpecialPartymember13);
                            $("#proposer-0208-dialog #isSpecialMaxPrivilege13").val(data.row.developmentInfo.isSpecialMaxPrivilege13);
                        } else if (data.row.process.id == '0302') {
                            $("#proposer-0302-dialog #groupOpinion15").val(data.row.developmentInfo.groupOpinion15);
                        } else if (data.row.process.id == '0304') {
                            $("#proposer-0304-dialog #branchOpinion17").val(data.row.developmentInfo.branchOpinion17);
                        } else if (data.row.process.id == '0305') {
                            $("#proposer-0305-dialog #branchMeetingResult18").val(data.row.developmentInfo.branchMeetingResult18);
                            $("#proposer-0305-dialog #officialInfo18").val(data.row.developmentInfo.officialInfo18);
                        } else if (data.row.process.id == '0306') {
                            $("#proposer-0306-dialog #auditResult19").val(data.row.developmentInfo.auditResult19);
                        }
                    } else {
                        Notify.error("获取数据失败，请稍后再试。");
                    }
                }
            });
        },
        viewDevelopmentProcedure : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/development/develop-party-member-procedure-bill.html'));
            $("#develop-party-member-dialog").modal({});
        },
        typeFormatter : function(value, row) {
            return Global.getEnumName('obt_party_member.type.' + value);
        },
        nameFormatter : function(value, row) {
            if (value) {
                return value.name;
            }
            return "";
        },
        genderFormatter : function(value, row) {
            if (value) {
                return Global.getCodeName('A0107.' + value.gender);
            }
            return "";
        },
        idNumberFormatter : function(value, row) {
            if (value) {
                return value.idNumber;
            }
            return "";
        },
        ccpartyFormatter : function(value, row) {
            if (value) {
                return value.name;
            }
            return '';
        },
        processFormatter : function(value, row) {
            if (value != null) {
                return value.phaseName;
            } else {
                return Global.getEnumName('obt_party_member.type.' + row.type);
            }
        },
        operatorFormatter : function(value, row) {
            var operator = "";
            if (row.type!=4 && row.process != null) {
                operator += '<a class="btn btn-default btn-xs" href="javascript:DevelopmentProcedure.nextStepDevelopmentProcedureDialog(\'' + value + '\',\'' + row.process.id + '\')"><i class="fa fa-level-down"></i>下一步</a>';
            }
            operator += '<a class="btn btn-default btn-xs" target="_blank" href="development-procedure-timer-shaft?id=' + value + '"><i class="fa fa-book"></i>发展历程</a>';
            return operator;
        }
    }
    return t;
}();