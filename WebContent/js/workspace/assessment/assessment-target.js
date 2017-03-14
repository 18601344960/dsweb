/**
 * @description 答卷测评组织
 * @author 赵子靖
 * @since 2016-06-30
 */
var AssessmentTarget = function() {
    var t = {
        mainContainer : '',
        assessmentId : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
        },
        initView : function(assessmentId) {
            t.assessmentId = assessmentId;
        },
        initEvent : function() {
            $(document).on("show.bs.tab", '#assessment-info-tab a[data-toggle="tab"]', function(e) {
                var tabId = $(e.target).attr('href');
                if (tabId == "#ccparty-info") {
                    AssessmentTarget.loadAssessmentTargets();
                } else if (tabId == "#user-info") {
                    AssessmentUser.loadAssessmentUsers();
                }
            });
            
            $(document).on("show.bs.tab", '#ccparty-assessment-info-tab a[data-toggle="tab"]', function(e) {
                var tabId = $(e.target).attr('href');
                if (tabId == "#ccparty-info") {
                    AssessmentTarget.loadCcpartyAssessmentTargets();
                } else if (tabId == "#user-info") {
                    AssessmentUser.loadCcpartyAssessmentUsers();
                }
            });
        },
        loadAssessmentTargets : function() {
            $("#assessment-target-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        assessmentId : t.assessmentId
                    })
                    return params;
                }
            });
        },
        loadCcpartyAssessmentTargets : function() {
            $("#assessment-target-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        assessmentId : t.assessmentId,
                        ccpartyId : Global.ccpartyId
                    })
                    return params;
                }
            });
        },
        // 测评组织
        loadAssessmentTargetDialog : function(assessmentId) {
            var data = new Object();
            var selectRows = $("#assessment-list-table").bootstrapTable('getSelections');
            if (selectRows[0].status == 0) {
                data.flag = 'manager';
            } else if (selectRows[0].status == 1) {
                data.flag = 'view';
            }
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workpractice/dialogs/assessment/assessment-target.html', data));
            $("#assessment-target-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        assessmentId : selectRows[0].id
                    })
                    return params;
                }
            });
            $("#assessment-target-table").bootstrapTable('refresh');
            $("#assessment-target-dialog").modal({});

        },
        // 新增通知对象
        loadCcpartysDialog : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workpractice/dialogs/assessment/ccparty-dialog.html'));
            t.loadCcpartys();
            $("#ccparty-dialog").modal({});
        },
        // 组织加载
        loadCcpartys : function() {
            var selectRows = $("#assessment-list-table").bootstrapTable('getSelections');
            $("#ccparty-table").bootstrapTable('destroy');
            $("#ccparty-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        assessmentId : selectRows[0].id,
                        ccpartyId : Global.ccpartyId
                    })
                    return params;
                }
            });
            $("#ccparty-table").bootstrapTable('refresh');
        },
        // 选择确认
        affirmCcparty : function() {
            var selectedCcpartyRows = $("#ccparty-table").bootstrapTable('getSelections');
            if (selectedCcpartyRows.length <= 0) {
                Notify.notice("请至少选择一条记录");
                return;
            }
            var ccpartyIds = [];
            for (var i = 0; i < selectedCcpartyRows.length; i++) {
                ccpartyIds.push(selectedCcpartyRows[i].id);
            }
            var assessmentSelectRows = $("#assessment-list-table").bootstrapTable('getSelections');
            Ajax.call({
                url : 'pub/setAssessmentTarget',
                p : {
                    ccpartyIds : JSON.stringify(ccpartyIds),
                    assessmentId : assessmentSelectRows[0].id
                },
                f : function(data) {
                    if (data && data.success) {
                        Notify.success(data.msg);
                        t.loadAssessmentTargetDialog(assessmentSelectRows[0].id);
                    } else {
                        Notify.error("设置失败");
                    }
                }
            });
        },
        // 删除通知对象
        deleteAssessmentTarget : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认撤回？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'pub/deleteAssessmentTarget',
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    $("#assessment-target-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error("撤回失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 选择人员窗口关闭
        closeCcpartyDialog : function() {
            $("#ccparty-dialog").modal('toggle');
            t.loadAssessmentTargetDialog();
        },
        // 单个组织下发
        sendSingleAssessmentTarget : function(targetId) {
            bootbox.confirm({
                size : 'small',
                message : "确认下发到该组织？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'pub/sendSingleAssessmentTarget',
                            p : {
                                targetId : targetId
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    $("#assessment-target-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });
                    }
                }
            });

        },
        ccpartyNameFormatter:function(value,row){
            if(value!=null){
                return value.name;
            }
        },
        ccpartyPartyTypeFormatter : function(value, row) {
            return Global.getEnumName('org_ccparty.partyType.' + value);
        },
        partyMemberTypeFormatter : function(value, row) {
            return Global.getEnumName('obt_party_member.type.' + value.type);
        },
        sendStatusFormatter : function(value, row) {
            return Global.getEnumName('pub_assess_target.sendStatus.' + value);
        },
        statusFormatter : function(value, row) {
            return Global.getEnumName('pub_assess_target.status.' + value);
        },
        dataFormater : function(value, row) {
            return Global.getDate(value);
        },
        numFormatter:function(value,row){
            return value+"/"+row.memberNum;
        },
        ratoFormatter : function(value, row) {
            if(value==0){
                return "<font style='color:red'>"+value+"%</font>";
            }else if(value==100){
                return "<font style='color:green'>"+value+"%</font>";
            }else{
                return value+"%";
            }
        },
        resultOperator : function(value, row) {
            var html = "";
            if (row.status != 0) {
                html += '<a href="assessment-result-statistical?id=' + value + '" target="_blank" class="btn btn-default btn-xs"><i class="fa fa-bar-chart-o"></i>结果统计</a>';
            }
            return html;
        },
        // 通知对象操作渲染
        assessmentTargetOperator : function(value, row) {
            var html = "";
            if(row.status == 1){
                html += '<a href="javascript:AssessmentTarget.sendSingleAssessmentTarget(\'' + value + '\')" class="btn btn-default btn-xs"><i class="fa  fa-share-square-o"></i>下发</a>';
            }else{
                html += '<a href="javascript:AssessmentTarget.deleteAssessmentTarget(\'' + value + '\')" class="btn btn-default btn-xs"><i class="fa fa-mail-reply"></i>撤回</a>';
            }
            return html;
        }
    }
    return t;
}();