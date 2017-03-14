/**
 * @description 答题答卷试题设置
 * @author 赵子靖
 * @since 2016-06-29
 */
var AssessmentTopic = function() {
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
        // 设置题目
        settTopic : function(assessmentId) {
            Ajax.call({
                url : "pub/getAssessmentTopicByAssessment",
                p : {
                    assessmentId : assessmentId
                },
                f : function(data) {
                    if (data && data.item) {
                        $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/assessment/sett-assessment-topic.html', data.item));
                    } else {
                        Notify.error("获取答题答卷失败。");
                    }
                }
            });
        },
        // 创建单选题
        createRadioQuestion : function() {
            // 获取最大题号
            Ajax.call({
                url : "pub/getMaxSeqAssessmentTopicByAssessment",
                p : {
                    assessmentId : $("#question_survey").attr("assessmentId")
                },
                f : function(data) {
                    if (data) {
                        // 单选题新增
                        data.dialogTitle = '新增单选题';
                        data.flag = 'add';
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/radio-question-dialog.html', data));
                        $("#radio-question-dialog").modal({});
                    } else {
                        Notify.error("新增失败，请稍后再试。");
                    }
                }
            });
        },
        // 创建多选题
        createCheckQuestion : function() {
            // 获取试题最大序号
            Ajax.call({
                url : "pub/getMaxSeqAssessmentTopicByAssessment",
                p : {
                    assessmentId : $("#question_survey").attr("assessmentId")
                },
                f : function(data) {
                    if (data) {
                        // 多选题新增
                        data.dialogTitle = '新增多选题';
                        data.flag = 'add';
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/checkbox-question-dialog.html', data));
                        $("#checkbox-question-dialog").modal({});
                    } else {
                        Notify.error("新增失败，请稍后再试。");
                    }
                }
            });
        },
        // 创建简答题
        createAnswerQuestion : function() {
            // 获取试题最大序号
            Ajax.call({
                url : "pub/getMaxSeqAssessmentTopicByAssessment",
                p : {
                    assessmentId : $("#question_survey").attr("assessmentId")
                },
                f : function(data) {
                    if (data) {
                        // 简答题新增
                        data.dialogTitle = '新增简答题';
                        data.flag = 'add';
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/subject-question-dialog.html', data));
                        $("#subject-question-dialog").modal({});
                    } else {
                        Notify.error("新增失败，请稍后再试。");
                    }
                }
            });
        },
        /**
         * 删除单选题的选项
         * 
         * @param radioQuestionTrId
         */
        deleteRadioQuestionOption : function(radioQuestionTrId) {
            $("#" + radioQuestionTrId).remove();
        },
        /**
         * 添加单选题选项
         */
        addRadioQuestionOption : function() {
            var index = $("#radio_question_table tr").length + 5; // 保证序号不重复
            // Notify.info(index);
            var option = "<tr id='radio_question_" + index + "'>";
            option += "<td align='right'>选项：<input type='radio' name='radio_question_radio' value='radio_question_value_" + index + "'></td>";
            option += "<td><input type='text' class='form-control' id='radio_question_value_" + index + "'  /></td>";
            option += "<td>";
            option += "<li class='fa fa-times-circle' style='cursor: pointer;color: #FF0000' title='删除'  onclick='AssessmentTopic.deleteRadioQuestionOption(&quot;radio_question_" + index
                    + "&quot;)'></li>";
            option += "&nbsp;<li class='fa fa-plus-circle' style='cursor: pointer;color: #1C86EE;' title='增加' onclick='AssessmentTopic.addRadioQuestionOption()'></li>";
            option += "</td>";
            option += "</tr>";
            $("#radio_question_table").append(option);
        },
        /**
         * 单选题的保存事件
         */
        addOrUpdateRadioQuestion : function() {
            var isMust = true;
            if (!document.getElementById("radio_isMust").checked) {
                isMust = false;
            }
            var question_title = $("#radio_question_title").val();
            if (CheckInputUtils.isEmpty(question_title)) {
                Notify.notice("请输入题目");
                $("#radio_question_title").focus();
                return;
            }
            // 遍历table 的tr拿到选项
            var trList = $("#radio_question_table tr");
            var options = new Array([ trList.length - 1 ]);
            var id = $("#radio-question-dialog #id").val();
            var seq = $("#radio-question-dialog #seq").val();
            for (var i = 2; i < trList.length; i++) {
                var tdArr = trList.eq(i).find("td");
                var optionValue = tdArr.eq(1).find("input").val();// 选项
                if (CheckInputUtils.isEmpty(optionValue)) {
                    Notify.notice("不能存在为空的选择，请检查");
                    return;
                }
                options[i - 2] = {
                    title : optionValue
                };
            }
            var questions = []; // 试题集合
            var question = {
                id : id,
                topicTitle : question_title,
                isMust : isMust,
                topicType : 1,
                options : options,
                seq : seq
            };
            questions.push(question);
            t.saveAssessmentTopic(questions, 'radio-question-dialog');
        },
        /**
         * 编辑试题
         * 
         * @param editDivId
         *            编辑的divID
         * @param topicType
         *            试题类型 1单选、2多选、3简单
         */
        editTopic : function(topicId) {
            Ajax.call({
                url : "pub/getAssessmentTopicById",
                p : {
                    topicId : topicId,
                },
                f : function(data) {
                    if (data && data.row) {
                        if (data.row.type == 1) {
                            // 单选编辑
                            data.row.dialogTitle = '编辑单选题';
                            data.row.flag = 'edit';
                            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/radio-question-dialog.html', data.row));
                            if (data.row.isMust == 0) {
                                $("#radio-question-dialog #radio_isMust").attr("checked", true);
                            } else {
                                $("#radio-question-dialog #radio_isMust").attr("checked", false);
                            }
                            $("#radio-question-dialog").modal({});
                        } else if (data.row.type == 2) {
                            // 多选编辑
                            data.row.dialogTitle = '编辑多选题';
                            data.row.flag = 'edit';
                            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/checkbox-question-dialog.html', data.row));
                            if (data.row.isMust == 0) {
                                $("#checkbox-question-dialog #checkbox_isMust").attr("checked", true);
                            } else {
                                $("#checkbox-question-dialog #checkbox_isMust").attr("checked", false);
                            }
                            $("#checkbox-question-dialog").modal({});
                        } else if (data.row.type == 3) {
                            // 简答
                            data.row.dialogTitle = '编辑简答题';
                            data.row.flag = 'edit';
                            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/subject-question-dialog.html', data.row));
                            if (data.row.isMust == 0) {
                                $("#subject-question-dialog #subject_isMust").attr("checked", true);
                            } else {
                                $("#subject-question-dialog #subject_isMust").attr("checked", false);
                            }
                            $("#subject-question-dialog").modal({});
                        }
                    } else {
                        Notify.error("删除失败，请稍后再试。");
                    }
                }
            });
        },
        /**
         * 添加多选题选项
         */
        addCheckboxQuestionOption : function() {
            var index = $("#checkbox_question_table tr").length + 5; // 保证序号不重复
            var option = "<tr id='checkbox_question_" + index + "'>";
            option += "<td align='right'>选项：<input type='checkbox' id='checkbox_question_checkbox_" + index + "' name='checkbox_question_checkbox' value='checkbox_question_value_" + index + "'></td>";
            option += "<td><input type='text' class='form-control' id='checkbox_question_value_" + index + "'  /></td>";
            option += "<td>";
            option += "<li class='fa fa-times-circle' style='cursor: pointer;color: #FF0000' title='删除'  onclick='AssessmentTopic.deleteRadioQuestionOption(&quot;checkbox_question_" + index
                    + "&quot;)'></li>";
            option += "&nbsp;<li class='fa fa-plus-circle' style='cursor: pointer;color: #1C86EE;' title='增加' onclick='AssessmentTopic.addCheckboxQuestionOption()'></li>";
            option += "</td>";
            option += "</tr>";
            $("#checkbox_question_table").append(option);
        },
        /**
         * 多选题的保存事件
         */
        addOrUpdateCheckboxQuestion : function() {
            var isMust = true;
            if (!document.getElementById("checkbox_isMust").checked) {
                isMust = false;
            }
            var question_title = $("#checkbox_question_title").val();
            if (CheckInputUtils.isEmpty(question_title)) {
                Notify.notice("请输入题目");
                $("#checkbox_question_title").focus();
                return;
            }
            // 遍历table 的tr拿到选项
            var trList = $("#checkbox_question_table tr");
            var options = new Array([ trList.length - 1 ]);
            var id = $("#checkbox-question-dialog #id").val();
            var seq = $("#checkbox-question-dialog #seq").val();
            for (var i = 2; i < trList.length; i++) {
                var tdArr = trList.eq(i).find("td");
                var optionValue = tdArr.eq(1).find("input").val();// 选项
                if (CheckInputUtils.isEmpty(optionValue)) {
                    Notify.notice("不能存在为空的选择，请检查");
                    return;
                }
                options[i - 2] = {
                    title : optionValue
                };
            }
            var questions = []; // 试题集合
            var question = {
                id : id,
                topicTitle : question_title,
                isMust : isMust,
                topicType : 2,
                options : options,
                seq : seq
            };
            questions.push(question);
            t.saveAssessmentTopic(questions, 'checkbox-question-dialog');
        },
        /**
         * 简答题的保存事件
         */
        addOrUpdateSubjectQuestion : function() {
            var isMust = true;
            if (!document.getElementById("subject_isMust").checked) {
                isMust = false;
            }
            var question_title = $("#subject_question_title").val();
            if (CheckInputUtils.isEmpty(question_title)) {
                Notify.notice("请输入题目");
                $("#subject_question_title").focus();
                return;
            }
            var id = $("#subject-question-dialog #id").val();
            var seq = $("#subject-question-dialog #seq").val();
            var questions = []; // 试题集合
            var question = {
                id : id,
                topicTitle : question_title,
                isMust : isMust,
                topicType : 3,
                options : null,
                seq : seq
            };
            questions.push(question);
            t.saveAssessmentTopic(questions, 'subject-question-dialog');
        },
        // 公共保存试题方法
        saveAssessmentTopic : function(questions, dialogId) {
            var assessmentId = $("#question_survey").attr("assessmentId"); // 试卷ID
            Ajax.call({
                url : "pub/saveAssessmentTopic",
                p : {
                    assessmentId : assessmentId,
                    questions : "{'data':" + JSON.stringify(questions, "data") + "}",
                },
                f : function(data) {
                    if (data && data.success) {
                        Notify.success(data.msg);
                        t.settTopic(assessmentId);
                        $("#" + dialogId).modal('toggle');
                    } else {
                        Notify.error("试题保存失败，请稍后再试。");
                    }
                }
            });
        },
        // 删除试题
        deleteTopic : function(topicId) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "pub/deleteAssessmentTopic",
                            p : {
                                topicId : topicId,
                            },
                            f : function(data) {
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    t.settTopic($("#question_survey").attr("assessmentId"));
                                } else {
                                    Notify.error("删除失败，请稍后再试。");
                                }
                            }
                        });
                    }
                }
            });

        },
    }
    return t;
}();