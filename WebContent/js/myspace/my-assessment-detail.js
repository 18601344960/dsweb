/**
 * @description 问卷测评详情
 * @author 赵子靖
 * @since 2015-09-17
 */
var MyAssessmentDetail = function() {
	var t = {
	    assessmentId:"",
	    userId:"",
		init : function(assessmentId,userId) {
			t.assessmentId=assessmentId;
			t.userId=userId;
		    t.initView();
		},
		initView : function() {
		    Ajax.call({
                url : "pub/getMyAssessmentTopicByAssessmentAndUser",
                p : {
                    assessmentId : t.assessmentId,
                    userId : t.userId
                },
                f : function(data) {
                    if (data) {
                        if(data.isAnswer){
                            // 开始答题
                            $.extend(data.item, Global);
                            $("#detail-container").html(nunjucks.render(Global.appName + '/tpl/myspace/my-assessment/begin-assessment.html', data.item));
                        }else{
                            // 查看答卷
                            t.viewMyAssessmentQuestion(t.assessmentId);
                        }
                    } else {
                        Notify.error("获取答题答卷详情失败,请稍后再试。");
                    }
                }
            });
		},
		//窗口关闭
		windowClose:function(){
		    window.close();
		},
		submitAssessmentQuestion : function() {
            var assessmentId = $("#question_survey").attr("assessmentId"); // 问卷ID
            // 取出考试结果
            var index = 1; // 索引
            var answers = [];
            var isSubmit = true;
            $('#question_survey div').each(function(i) {
                if (!isSubmit) {
                    return;
                }
                if (i >= 2) {
                    var topicDivId = $(this).attr("id");
                    var topicId = $("#" + topicDivId).attr("topicId"); // 试题ID
                    var topicType = $("#" + topicDivId).attr("questionType"); // 类型
                    var isMust = $("#" + topicDivId).attr("isMust"); // 是否必答
                    var answerArray = {};
                    answerArray.topicId = topicId;
                    if ("1" == topicType) {
                        // 单选题
                        radioVal = $("input:radio[name='topic_" + index + "_radio']:checked").val();
                        if ((radioVal == null || radioVal == "" || radioVal == "undefined") && isMust == "true") {
                            // 必做题并且没有选择答案
                            Notify.notice("您存在第" + index + "试题未完成，请检查!");
                            isSubmit = false;
                            return;
                        }
                        answerArray.answer = radioVal;
                        answers.push(answerArray);
                    } else if ("2" == topicType) {
                        var checkNums = 0;
                        // 多选题
                        $("input[name=topic_" + index + "_checkbox]:checked").each(function() {
                            if (this.checked) {
                                answerArray = {};
                                answerArray.topicId = topicId;
                                answerArray.answer = $(this).val();
                                answers.push(answerArray);
                                checkNums += 1;
                            }
                        });
                        if (checkNums == 0 && isMust) {
                            // 必做题并且没有选择答案
                            Notify.notice("您存在第" + index + "试题未完成，请检查!");
                            isSubmit = false;
                            return;
                        }
                    } else if ("3" == topicType) {
                        // 简答题
                        answerArray.answer = $("#" + topicId + "_answer").val();
                        if ((answerArray.answer == null || answerArray.answer == "" || answerArray.answer == "undefined") && isMust == "true") {
                            // 必做题并且没有选择答案
                            Notify.notice("您存在第" + index + "试题未完成，请检查!");
                            isSubmit = false;
                            return;
                        }
                        answers.push(answerArray);
                    }
                    index++;
                }
            })
            // 发送ajax保存测试结果
            if (isSubmit) {
                bootbox.confirm({
                    size : 'small',
                    message : "确认提交答卷？",
                    callback : function(result) {
                        if (result) {
                            $("#question_survey #submit-button").attr('disabled', "true");    
                            Ajax.call({
                                url : "pub/saveAssessmentResult",
                                p : {
                                    assessmentId : assessmentId,
                                    userId : Global.userId,
                                    answers : "{'data':" + JSON.stringify(answers, "data") + "}"
                                },
                                f : function(data) {
                                    if (data.success) {
                                        // 跳转列表页面
                                        Notify.success(data.msg + "3秒后关闭窗口。");
                                        // 提示三秒后关闭窗口
                                        var second = 3;
                                        timer = setInterval(function() {
                                            second -= 1;
                                            if (second == 0) {
                                                clearInterval(timer);
                                                window.close();
                                            }
                                        }, 1000);// 1000为1秒钟
                                    } else {
                                        $("#question_survey #submit-button").removeAttr('disabled');
                                        if (data.msg) {
                                            Notify.error(data.msg);
                                        } else {
                                            Notify.error("答卷提交失败，请稍后再试。");
                                        }
                                    }
                                }
                            });
                        }
                    }
                });

            }
        },
        // 查看答卷结果
        viewMyAssessmentQuestion : function(assessmentId) {
            Ajax.call({
                url : "pub/viewAssessmentQuestion",
                p : {
                    assessmentId : assessmentId,
                    userId : Global.userId
                },
                f : function(data) {
                    if (data && data.item) {
                        if (data.item.isExpiry == 1) {
                            data.item.titleTip = "&nbsp;<font style='color:red;font-size:20px;'>(已过期)</font>";
                        }
                        $.extend(data.item, Global);
                        // 测评
                        $("#detail-container").html(nunjucks.render(Global.appName + '/tpl/myspace/my-assessment/view-my-assessment.html', data.item));
                        if (data.item.topics) {
                            for (var i = 0; i < data.item.topics.length; i++) {
                                // 遍历试题
                                var topic = data.item.topics[i];
                                switch (topic.type) {
                                case 1:
                                    // 单选
                                    if (topic.myAssessresult) {
                                        $("input[type=radio][name='topic_" + topic.id + "_radio'][value=" + topic.myAssessresult[0].result + "]").attr("checked", 'checked');
                                    }
                                    break;
                                case 2:
                                    // 多选
                                    if (topic.myAssessresult) {
                                        for (var temp = 0; temp < topic.myAssessresult.length; temp++) {
                                            var myResult = topic.myAssessresult[temp];
                                            if (myResult.isOtherOption == 1) {
                                                // 其他选项
                                                $("#topic_" + topic.id + "_1_checkbox").prop("checked", "checked");
                                                $("#" + topic.id + "_other").val(myResult.result);
                                            } else {
                                                $("#topic_" + topic.id + "_" + myResult.result + "_checkbox").prop("checked", "checked");
                                            }
                                        }
                                    }
                                    break;
                                default:
                                    break;
                                }
                            }
                        }
                    } else {
                        Notify.error("获取答卷失败,请稍后再试。");
                    }
                }
            });
        }
	}
	return t;
}();