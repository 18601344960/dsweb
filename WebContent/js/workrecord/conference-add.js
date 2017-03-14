/**
 * @description 文章录入
 * @author yiwenjun
 * @since 2015-05-02
 */
var ConferenceAdd = function() {
    var t = {
        ccpartyId : Global.ccpartyId,
        init : function() {
            Global.source = t.source;
            t.initView();
            t.initEvent();
        },
        initView : function() {
            $("#content").html(nunjucks.render(Global.appName + '/tpl/conference/conference-add.html', Global));
            var currentDate = new Date();
            $("#occurTime").val(currentDate.getFullYear() + "-" + (currentDate.getMonth() + 1) + "-" + currentDate.getDate());
            Global.initLoader('step-list');
            Ajax.call({
                url : "obt/getConferenceStepList",
                p : {
                    timeStamp : new Date().getTime()
                },
                async : false,
                f : function(data) {
                    if (data && data.rows) {
                        $("#conference-form #step-list").append(nunjucks.render(Global.appName + '/tpl/conference/category-container.html', data));
                    }
                }
            });
            Global.initLoader('format-list');
            Ajax.call({
                url : "obt/getConferenceFormatList",
                p : {
                    timeStamp : new Date().getTime()
                },
                async : false,
                f : function(data) {
                    if (data && data.rows) {
                        $("#conference-form #format-list").append(nunjucks.render(Global.appName + '/tpl/conference/category-container.html', data));
                    }
                }
            });
            Global.initLoader('label-list');
            Ajax.call({
                url : "obt/getConferenceLabelList",
                p : {
                    ccpartyId : t.ccpartyId
                },
                async : false,
                f : function(data) {
                    if (data && data.rows) {
                        $("#conference-form #label-list").append(nunjucks.render(Global.appName + '/tpl/conference/category-container.html', data));
                    }
                }
            });
            ComEditor.initEditor();
            ComFile.initUploadify();
        },
        initEvent : function() {
            $(document).on("click", ".method-label", function() {
                if ($(this).hasClass("method-label-selected")) {
                    $(this).removeClass("method-label-selected");
                } else {
                    $(this).addClass("method-label-selected");
                }
            });
            $(document).on("click", "#close-btn", function() {
                bootbox.confirm({
                    size : 'small',
                    message : "确认放弃保存？",
                    callback : function(result) {
                        if (result) {
                            window.location.href = "home";
                        }
                    }
                });
            });

            $(document).on("click", "#party-branch", function() {
                $("input[name='aticle-source']").each(function() {
                    $(this).checked = false;
                });
                $(this).checked = true;
                $("#conference-form #source-name").val(Global.ccpartyName);
                $("#brand-container").show();
            });
            $(document).on("click", "#party-member", function() {
                $("input[name='aticle-source']").each(function() {
                    $(this).checked = false;
                });
                $(this).checked = true;
                $("#conference-form #source-name").val(Global.userName);
                $("#brand-container").hide();
            });
        },
        // 验证后保存
        checkAddConference : function(operatorType) {
            var name = $("#conference-form #name").val();
            var occurTime = $("#conference-form #occurTime").val();
            var address = $("#conference-form #address").val();
            var attendance = $("#conference-form #attendance").val();
            var secretLevel = $('input[name="secretLevel"]:checked').val();
            var stepIds = [];
            var formatIds = [];
            var labelIds = [];
            $("#step-list").find(".method-label").each(function() {
                if ($(this).hasClass("method-label-selected")) {
                    stepIds.push($(this).attr("id"));
                }
            });
            $("#format-list").find(".method-label").each(function() {
                if ($(this).hasClass("method-label-selected")) {
                    formatIds.push($(this).attr("id"));
                }
            });
            $("#label-list").find(".method-label").each(function() {
                if ($(this).hasClass("method-label-selected")) {
                    labelIds.push($(this).attr("id"));
                }
            });
            var orgnizerUserIds = [];
            var orgnizerUserNames = [];
            $("#choose-orgnizer-member-list").find(".member-name").each(function() {
                var userType = $(this).attr("userType");
                if (userType == 0) {
                    var userId = $(this).attr("userId");
                    orgnizerUserIds.push(userId);
                } else {
                    var userName = $(this).attr("userName");
                    orgnizerUserNames.push(userName);
                }
            });

            var participantUserIds = [];
            var participantUserNames = [];
            $("#choose-participant-member-list").find(".member-name").each(function() {
                var userType = $(this).attr("userType");
                if (userType == 0) {
                    var userId = $(this).attr("userId");
                    participantUserIds.push(userId);
                } else {
                    var userName = $(this).attr("userName");
                    participantUserNames.push(userName);
                }
            });

            var sourceType = $("input[name='aticle-source']:checked").val();
            var sourceId;
            var sourceName;
            if (sourceType == 0) {
                sourceId = Global.ccpartyId;
                sourceName = Global.ccpartyName;
            } else if (sourceType == 1) {
                sourceId = Global.userId;
                sourceName = Global.userName;
            }
            var content = ComEditor.getContent();
            var files = ComFile.getUploadFiles();
            if (stepIds.length < 1) {
                Notify.notice("请选择工作步骤标签！");
                return;
            }
            if (!sourceId || sourceId.trim() == "") {
                Notify.notice("请选择工作来源！");
                return;
            }
            if (!name || name.trim() == "") {
                Notify.notice("工作主题不能为空！");
                $("#conference-form #name").focus();
                return;
            }
            if (!occurTime || occurTime.trim() == "") {
                Notify.notice("请选择工作时间");
                $("#conference-form #occurTime").focus();
                return;
            }
            if ('preview' == operatorType) {
                t.addConference(operatorType);
            } else {
                bootbox.confirm({
                    size : 'small',
                    message : "确认保存？",
                    callback : function(result) {
                        if (result) {
                            t.addConference(operatorType);
                        }
                    }
                });
            }
        },
        // 保存
        addConference : function(operatorType) {
            var name = $("#conference-form #name").val();
            var occurTime = $("#conference-form #occurTime").val();
            var address = $("#conference-form #address").val();
            var attendance = $("#conference-form #attendance").val();
            var secretLevel = $('input[name="secretLevel"]:checked').val();
            var stepIds = [];
            var formatIds = [];
            var labelIds = [];
            $("#step-list").find(".method-label").each(function() {
                if ($(this).hasClass("method-label-selected")) {
                    stepIds.push($(this).attr("id"));
                }
            });
            $("#format-list").find(".method-label").each(function() {
                if ($(this).hasClass("method-label-selected")) {
                    formatIds.push($(this).attr("id"));
                }
            });
            $("#label-list").find(".method-label").each(function() {
                if ($(this).hasClass("method-label-selected")) {
                    labelIds.push($(this).attr("id"));
                }
            });
            var orgnizerUserIds = [];
            var orgnizerUserNames = [];
            $("#choose-orgnizer-member-list").find(".member-name").each(function() {
                var userType = $(this).attr("userType");
                if (userType == 0) {
                    var userId = $(this).attr("userId");
                    orgnizerUserIds.push(userId);
                } else {
                    var userName = $(this).attr("userName");
                    orgnizerUserNames.push(userName);
                }
            });

            var participantUserIds = [];
            var participantUserNames = [];
            $("#choose-participant-member-list").find(".member-name").each(function() {
                var userType = $(this).attr("userType");
                if (userType == 0) {
                    var userId = $(this).attr("userId");
                    participantUserIds.push(userId);
                } else {
                    var userName = $(this).attr("userName");
                    participantUserNames.push(userName);
                }
            });

            var sourceType = $("input[name='aticle-source']:checked").val();
            var sourceId;
            var sourceName;
            if (sourceType == 0) {
                sourceId = Global.ccpartyId;
                sourceName = Global.ccpartyName;
            } else if (sourceType == 1) {
                sourceId = Global.userId;
                sourceName = Global.userName;
            }
            var content = ComEditor.getContent();
            var files = ComFile.getUploadFiles();
            if (stepIds.length < 1) {
                Notify.notice("请选择工作步骤标签！");
                return;
            }
            if (!sourceId || sourceId.trim() == "") {
                Notify.notice("请选择工作来源！");
                return;
            }
            if (!name || name.trim() == "") {
                Notify.notice("工作主题不能为空！");
                $("#conference-form #name").focus();
                return;
            }
            if (!occurTime || occurTime.trim() == "") {
                Notify.notice("请选择工作时间");
                $("#conference-form #occurTime").focus();
                return;
            }
            var status = 0;// 默认不发布
            if ('publish' == operatorType) {
                status = 1;
            }
            Ajax.call({
                url : "obt/addConference",
                p : {
                    name : name,
                    occurTime : occurTime,
                    address : address,
                    attendance : attendance,
                    content : content,
                    stepIds : JSON.stringify(stepIds),
                    formatIds : JSON.stringify(formatIds),
                    labelIds : JSON.stringify(labelIds),
                    sourceType : sourceType,
                    sourceId : sourceId,
                    sourceName : sourceName,
                    files : JSON.stringify(files),
                    ccpartyId : Global.ccpartyId,
                    orgnizerUserIds : JSON.stringify(orgnizerUserIds),
                    orgnizerUserNames : JSON.stringify(orgnizerUserNames),
                    participantUserIds : JSON.stringify(participantUserIds),
                    participantUserNames : JSON.stringify(participantUserNames),
                    secretLevel : secretLevel,
                    status : status
                },
                f : function(data) {
                    if (data && data.success) {
                        if ('close' == operatorType) {
                            // 关闭窗口
                            Notify.success(data.msg);
                            window.close();
                        } else if ('new' == operatorType) {
                            // 新建窗口
                            Notify.success(data.msg);
                            t.newAddConference();
                        } else if ('publish' == operatorType) {
                            // 关闭窗口
                            Notify.success(data.msg);
                            window.close();
                        } else if ('preview' == operatorType) {
                            // 预览
                            window.location.href = "article-preview?articleId=" + data.conferenceId;
                        }
                    } else {
                        if (data.msg) {
                            Notify.error(data.msg);
                        } else {
                            Notify.error("保存失败，请稍后再试。");
                        }
                    }
                }
            });
        },
        // 新建窗口
        newAddConference : function() {
            $("#content").html(nunjucks.render(Global.appName + '/tpl/conference/conference-add.html', Global));
            var currentDate = new Date();
            $("#occurTime").val(currentDate.getFullYear() + "-" + (currentDate.getMonth() + 1) + "-" + currentDate.getDate());
            Global.initLoader('step-list');
            Ajax.call({
                url : "obt/getConferenceStepList",
                p : {
                    timeStamp : new Date().getTime()
                },
                async : false,
                f : function(data) {
                    if (data && data.rows) {
                        $("#conference-form #step-list").append(nunjucks.render(Global.appName + '/tpl/conference/category-container.html', data));
                    }
                }
            });
            Global.initLoader('format-list');
            Ajax.call({
                url : "obt/getConferenceFormatList",
                p : {
                    timeStamp : new Date().getTime()
                },
                async : false,
                f : function(data) {
                    if (data && data.rows) {
                        $("#conference-form #format-list").append(nunjucks.render(Global.appName + '/tpl/conference/category-container.html', data));
                    }
                }
            });
            Global.initLoader('label-list');
            Ajax.call({
                url : "obt/getConferenceLabelList",
                p : {
                    ccpartyId : t.ccpartyId
                },
                async : false,
                f : function(data) {
                    if (data && data.rows) {
                        $("#conference-form #label-list").append(nunjucks.render(Global.appName + '/tpl/conference/category-container.html', data));
                    }
                }
            });
            ComEditor.destroy();
            ComEditor.initEditor();
            ComFile.initUploadify();
        },
    }
    return t;
}();