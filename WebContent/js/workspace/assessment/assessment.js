/**
 * @description 问卷测评管理
 * @author 赵子靖
 * @since 2015-06-16
 */
var Assessment = function() {
    var t = {
        mainContainer : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
            AssessmentTopic.init(t.mainContainer);
            AssessmentTarget.init(t.mainContainer);
            AssessmentUser.init(t.mainContainer);
        },
        initView : function() {
        },
        initEvent : function() {
            $(document).on("show.bs.tab", '#assessment-tab a[data-toggle="tab"]', function(e) {
                var tabId = $(e.target).attr('href');
                if (tabId == "#ccparty-assessment") {
                    t.renderCcpartyAssessment();
                } else if (tabId == "#leader-ccparty-assessment") {
                    t.renderLeaderAssessment();
                }
            });
        },
        render : function() {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/assessment/assessment-list.html'));
            t.renderCcpartyAssessment();
        },
        // 加载答题答卷列表
        renderCcpartyAssessment : function() {
            $("#assessment-list-table").bootstrapTable('destroy');
            $("#assessment-list-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId
                    })
                    return params;
                }
            });
        },
        // 加载上级的答卷
        renderLeaderAssessment : function() {
            $("#leader-assessment-table").bootstrapTable('destroy');
            $("#leader-assessment-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId
                    })
                    return params;
                }
            });
        },
        // 组织树获取
        initCcpartySubjectTree : function() {
            Global.initLoader("#ccparty-trees");
            ComTree.initTree({
                divContainer : "#ccparty-trees",
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                chkStyle : "checkbox",
                async : false,
                enable : true,
                chkboxType : {
                    "Y" : "ps",
                    "N" : "ps"
                },
                beforeClick : t.beforeCcpartyTreeClick
            });
        },
        // 选中
        beforeCcpartyTreeClick : function(treeId, treeNode) {
            ComTree.treeObj.checkNode(treeNode, !treeNode.checked, null, true);
        },
        // 回选参与单位
        selectedCcpartyTree : function(data) {
            if (data && data.targets) {
                for (var i = 0; i < data.targets.length; i++) {
                    var treeNode = ComTree.treeObj.getNodeByParam("id", data.targets[i].ccpartyId, null);
                    if (treeNode) {
                        ComTree.treeObj.checkNode(treeNode, !treeNode.checked, null, true);
                    }
                }
            }
        },
        // 新增
        addAssessment : function() {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/add-assessment-dialog.html'));
            $("#add-assessment-dialog").modal({});
            t.initCcpartySubjectTree();
        },
        // 修改弹框
        editAssessmentDialog : function(id) {
            Ajax.call({
                url : "pub/getAssessmentById",
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/update-assessment-dialog.html', data.item));
                        $("input[type=radio][name='isExpiry'][value=" + data.item.isExpiry + "]").attr("checked", 'checked');
                        if (data.item.isExpiry == 1) {
                            $("#endDateDiv,#endDateLabel").show();
                        }
                        $("#update-assessment-dialog").modal({});
                        t.initCcpartySubjectTree();
                        // 回选参与单位
                        t.selectedCcpartyTree(data.item);
                    } else {
                        Notify.error("获取问卷测评失败！");
                    }
                }
            });
        },
        // 保存
        saveAssessment : function() {
            var name = $("#add-assessment-dialog #name").val(); // 名称
            var description = $("#add-assessment-dialog #description").val(); // 描述
            var isExpiry = $("input[name='isExpiry']:checked").val();
            var endDate = $("#add-assessment-dialog #endDate").val(); // 截止日期
            var ccpartyValue = ComTree.returnCheckedValue();
            var ccpartyIds = ""; // 参与组织ID
            if (ccpartyValue) {
                for (var i = 0; i < ccpartyValue.ids.length; i++) {
                    if (ccpartyIds) {
                        ccpartyIds += "," + ccpartyValue.ids[i];
                    } else {
                        ccpartyIds = ccpartyValue.ids[i];
                    }
                }
            }
            if (CheckInputUtils.isEmpty(name)) {
                Notify.notice("请填写答卷名称。");
                $("#add-assessment-dialog #name").focus();
                return;
            }
            if (isExpiry == 1 && CheckInputUtils.isEmpty(endDate)) {
                Notify.notice("请选择截止日期。");
                $("#add-assessment-dialog #endDate").focus();
                return;
            }
            if (CheckInputUtils.isEmpty(ccpartyIds)) {
                Notify.notice("请选择参与单位。");
                return;
            }
            bootbox.confirm({
                size : 'small',
                message : "确认保存？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "pub/saveAssessment",
                            p : {
                                name : name,
                                isExpiry : isExpiry,
                                endDate : endDate,
                                ccpartyId : Global.ccpartyId,
                                description : description,
                                ccpartyIds : ccpartyIds
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    $("#add-assessment-dialog").modal('toggle');
                                    $("#assessment-list-table").bootstrapTable("refresh");
                                } else {
                                    if (data.msg) {
                                        Notify.error(data.msg);
                                    } else {
                                        Notify.error("保存失败,请稍后再试。");
                                    }
                                }
                            }
                        });
                    }
                }
            });
        },
        // 修改
        updateAssessment : function() {
            var id = $("#update-assessment-dialog #id").val();
            var name = $("#update-assessment-dialog #name").val();
            var isExpiry = $("input[name='isExpiry']:checked").val();
            var description = $("#update-assessment-dialog #description").val();
            var endDate = $("#update-assessment-dialog #endDate").val();
            var ccpartyValue = ComTree.returnCheckedValue();
            var ccpartyIds = ""; // 参与组织ID
            if (ccpartyValue) {
                for (var i = 0; i < ccpartyValue.ids.length; i++) {
                    if (ccpartyIds) {
                        ccpartyIds += "," + ccpartyValue.ids[i];
                    } else {
                        ccpartyIds = ccpartyValue.ids[i];
                    }
                }
            }
            if (CheckInputUtils.isEmpty(name)) {
                Notify.notice("请填写答卷名称。");
                $("#update-assessment-dialog #name").focus();
                return;
            }
            if (isExpiry == 1 && CheckInputUtils.isEmpty(endDate)) {
                Notify.notice("请选择截止日期。");
                $("#update-assessment-dialog #endDate").focus();
                return;
            }
            if (CheckInputUtils.isEmpty(ccpartyIds)) {
                Notify.notice("请选择参与单位。");
                return;
            }
            bootbox.confirm({
                size : 'small',
                message : "确认保存？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "pub/updateAssessment",
                            p : {
                                id : id,
                                name : name,
                                isExpiry : isExpiry,
                                endDate : endDate,
                                description : description,
                                ccpartyIds : ccpartyIds
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    $("#update-assessment-dialog").modal('toggle');
                                    $("#assessment-list-table").bootstrapTable("refresh");
                                } else {
                                    if (data.msg) {
                                        Notify.error(data.msg);
                                    } else {
                                        Notify.error("保存失败,请稍后再试。");
                                    }
                                }
                            }
                        });
                    }
                }
            });
        },
        // 删除答卷
        deleteAssessment : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "pub/deleteAssessment",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    $("#update-assessment-dialog").modal('toggle');
                                    $("#assessment-list-table").bootstrapTable("refresh");
                                } else {
                                    if (data.msg) {
                                        Notify.error(data.msg);
                                    } else {
                                        Notify.error("抱歉，答卷删除失败，请稍后再试。");
                                    }
                                }
                            }
                        });
                    }
                }
            });
        },
        // 编辑测试试题的题目
        editTestQuestion : function(assessmentId) {
            Ajax.call({
                url : "pub/getAssessmentQuestionInfo",
                p : {
                    assessmentId : assessmentId
                },
                f : function(data) {
                    if (data && data.item) {
                        $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/assessment/assessment-edit-evaluation.html', data.item));
                    } else {
                        Notify.error("获取问卷测评试题失败");
                    }
                }
            });
        },
        // 试卷发布
        publishAssessment : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认开启答卷？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'pub/publishAssessment',
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success(data.msg);
                                    $("#assessment-list-table").bootstrapTable("refresh");
                                } else {
                                    if (data.msg) {
                                        Notify.error(data.msg);
                                    } else {
                                        Notify.success("问卷开启失败，请稍后再试！");
                                    }
                                }
                            }
                        });
                    }
                }
            });
        },
        // 结束测评
        overAssessment : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认结束答卷？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'pub/overAssessment',
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success(data.msg);
                                    $("#assessment-list-table").bootstrapTable("refresh");
                                } else {
                                    Notify.success("结束失败，请稍后再试！");
                                }
                            }
                        });
                    }
                }
            });
        },
        assessmentInfo : function(value, row) {
            var html = "";
            html += '<a href="javascript:AssessmentResultStatistical.ccpartyAssessmentResultInfos(\'' + value + '\')" class="btn btn-default btn-xs"><i class="fa fa-file-text-o"></i>完成情况</a>';
            return html;
        },
        // 数据渲染
        statusFormatter : function(value, row) {
            var html = "";
            if (value == 0) {
                html = "<font style='color:red;'>" + Global.getEnumName('pub_assessment.status.' + value) + "</font>";
            } else if (value == 1) {
                html = "<font style='color:blue;'>" + Global.getEnumName('pub_assessment.status.' + value) + "</font>";
            } else if (value == 2) {
                html = "<font style='color:gray;'>" + Global.getEnumName('pub_assessment.status.' + value) + "</font>";
            }
            return html;
        },
        // 状态更改
        updateAssessmentStatus : function(id, status) {
            bootbox.confirm({
                size : 'small',
                message : "确认操作？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : 'pub/updateAssessmentStatus',
                            p : {
                                id : id,
                                status : status
                            },
                            f : function(data) {
                                if (data.success) {
                                    Notify.success(data.msg);
                                    $("#assessment-list-table").bootstrapTable("refresh");
                                } else {
                                    if(data.msg){
                                        Notify.error(data.msg);
                                    }else{
                                        Notify.success("操作失败，请稍后再试！");
                                    }
                                }
                            }
                        });
                    }
                }
            });

        },
        endDateFormatter : function(value, row) {
            if (value) {
                return Global.getDate(value);
            }
        },
        ccpartyFormatter : function(value, row) {
            if (value) {
                return value.name;
            }
        },
        // 是否有截止日期选择
        isExpiryChange : function(value) {
            if (value == 0) {
                // 隐藏并清空
                $("#endDateLabel,#endDateDiv").slideUp();
            } else if (value == 1) {
                // 显示
                $("#endDateLabel,#endDateDiv").slideDown();
            }
        },
        // 标题渲染
        nameFormater : function(value, row) {
            var html = "";
            html += '<a href="assessment-detail?id=' + row.id + '" target="_blank" class="bootstrap-title">' + value + '</a>';
            return html;
        },
        userGenderFormatter : function(value, row) {
            return Global.getCodeName('A0107.' + value);
        },
        userTypeFormatter : function(value, row) {
            return Global.getCodeName(value);
        },
        notifyStatusFormatter : function(value, row) {
            return Global.getEnumName('pub_assess_target.status.' + value);
        },
        // 答卷类型
        typeFormater : function(value, row) {
            return Global.getEnumName('pub_assessment.type.' + value);
        },
        // 操作渲染
        assessmentOperator : function(value, row) {
            var html = "";
            if (row.status != 2) {
                html += '<a href="javascript:Assessment.editAssessmentDialog(\'' + value + '\')" class="btn btn-default btn-xs"><i class="fa fa-edit"></i>编辑</a>';
            }
            if (row.status == 0) {
                // 未开启
                html += '<a href="javascript:Assessment.deleteAssessment(\'' + value + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a>';
                html += '<a href="javascript:Assessment.publishAssessment(\'' + value + '\')" class="btn btn-default btn-xs"><i class="fa fa-play-circle-o"></i>开启</a>';
            } else if (row.status == 1) {
                // 已开启
                html += '<a href="javascript:Assessment.overAssessment(\'' + value + '\')" class="btn btn-default btn-xs"><i class="fa fa-ban"></i>关闭</a>';
            } else if (row.status == 2) {
                // 发布，只是状态更改
                html += '<a href="javascript:Assessment.updateAssessmentStatus(\'' + value + '\',\'1\')" class="btn btn-default btn-xs"><i class="fa fa-ban"></i>发布</a>';
            }
            return html;
        },
        resultOperator : function(value, row) {
            var html = "";
            if (row.status == 0) {
                html += '<a href="javascript:AssessmentTopic.settTopic(\'' + value + '\')" class="btn btn-default btn-xs"><i class="fa fa-gear"></i>答卷设置</a>';
            } else {
                html += '<a href="javascript:AssessmentResultStatistical.assessmentResultInfos(\'' + value + '\')" class="btn btn-default btn-xs"><i class="fa fa-file-text-o"></i>答卷情况</a>';
                html += '<a href="assessment-result-statistical?id=' + value + '" target="_blank" class="btn btn-default btn-xs"><i class="fa fa-bar-chart-o"></i>结果统计</a>';
            }
            return html;
        },
        // 测评对象操作渲染
        targetOperator : function(value, row) {
            return html;
        }
    }
    return t;
}();