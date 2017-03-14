/**
 * @description 问卷测评结果统计
 * @author 赵子靖
 * @since 2015-09-17
 */
var AssessmentResultStatistical = function() {
    var t = {
        init : function(id, targetId) {
            t.initView(id, targetId);
        },
        initView : function(id,targetId) {
            Ajax.call({
                url : "pub/getAssessmentResultStatistical",
                p : {
                    assessmentId : id,
                    targetId : targetId
                },
                f : function(data) {
                    if (data && data.item) {
                        t.assembleData(data);
                    } else {
                        Notify.error("获取问卷测统计结果失败");
                    }
                }
            });
        },
        initEvent : function() {
        },
        //装配数据
        assembleData:function(data){
            var divContent = $("#detail-container");
            // 测评统计
            divContent.html('<div class="detail-title">' + data.item.assessment.name + '&nbsp;&nbsp;(结果统计)</div>');
            for (var i = 0; i < data.item.topics.length; i++) {
                divContent.append('<div class="statistical_title">' + data.item.topics[i].topicSeq + '、' + data.item.topics[i].topicName + '</div>');
                if (data.item.topics[i].topicType == 1 || data.item.topics[i].topicType == 2) {
                    // 单选、多选
                    divContent.append('<div id="main' + data.item.topics[i].topicId + '" class="echart_main"></div>');
                    t.renderChart(data.item.topics[i].answers, 'main' + data.item.topics[i].topicId);
                    var html = '<div class="answer-option">';
                    for(var j=0;j<data.item.topics[i].answers.length;j++){
                        html += '<b>'+data.item.topics[i].answers[j].answerSeq+"</b>、"+data.item.topics[i].answers[j].answerContent+"</br>";
                    }
                    html += '</div>';
                    divContent.append(html);
                } else if (data.item.topics[i].topicType == 3) {
                    // 简单
                    for (var j = 0; j < data.item.topics[i].answers.length; j++) {
                        // '+data.item.topics[i].answers[j].userName+'
                        // 答案给出者
                        divContent.append('<div class="reply"><span class="user_name">***</span>&nbsp;给出的答案');
                        divContent.append('<p class="reply_content">' + data.item.topics[i].answers[j].answerContent + '</p>');
                    }
                    divContent.append('</div>');
                }
            }
        },
        // 测评统计
        renderChart : function(items, div) {
            var answerName = [];
            var answerNum = [];
            for ( var index in items) {
                answerName[index] = items[index].answerSeq;
                answerNum[index] = items[index].nums;
            }
            var templateChart = echarts.init(document.getElementById(div));
            option = {
                title : {
                    text : ''
                },
                tooltip : {
                    trigger : 'axis'
                },
                legend : {
                    data : [ '选择人数' ]
                },
                calculable : true,
                xAxis : [ {
                    type : 'category',
                    data : answerName
                } ],
                yAxis : [ {
                    type : 'value'
                } ],
                series : [ {
                    name : '选择人数',
                    type : 'bar',
                    data : answerNum,
                    itemStyle : {
                        normal : {
                            color : 'tomato',
                            barBorderColor : 'tomato',
                            barBorderWidth : 6,
                            barBorderRadius : 0,
                            label : {
                                show : true,
                                position : 'top'
                            }
                        }
                    }
                } ]
            };
            templateChart.setOption(option);
        },
        // 答卷结果详情
        assessmentResultInfos : function(assessmentId) {
            Ajax.call({
                url : 'pub/getAssessmentResultInfos',
                p : {
                    id : assessmentId
                },
                f : function(data) {
                    if (data.item) {
                        data.Global = Global;
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/assessment-result-info-dialog.html', data));
                        $("#assessment-result-info-dialog").modal({});
                        AssessmentTarget.initView(data.item.id);
                        AssessmentTarget.loadAssessmentTargets();
                        AssessmentUser.initView(data.item.id);
                    }
                }
            });
        },
        // 本组织答卷结果详情
        ccpartyAssessmentResultInfos : function(assessmentId) {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/dialogs/assessment/ccparty-assessment-result-dialog.html'));
            $("#ccparty-assessment-result-dialog").modal({});
            AssessmentTarget.initView(assessmentId);
            AssessmentTarget.loadCcpartyAssessmentTargets();
            AssessmentUser.initView(assessmentId,'currentCcparty');
        },
    }
    return t;
}();