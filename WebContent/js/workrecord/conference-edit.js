/**
 * @description 组织生活编辑
 * @author yiwenjun
 * @since 2015-05-02
 */
var ConferenceEdit = function() {
    var t = {
        conferenceId : "",
        init : function(conferenceId) {
            t.conferenceId = conferenceId;
            t.initView();
        },
        initView : function() {
            Ajax.call({
                url : "obt/getConferenceById",
                p : {
                    id : t.conferenceId
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        $("#content").html(nunjucks.render(Global.appName + '/tpl/conference/conference-edit.html', data.item));
                        if (data.item.sourceType == 0) {
                            $("#party-branch").attr("checked", "checked");
                        } else {
                            $("#party-member").attr("checked", "checked");
                        }
                        $("input[name=secretLevel][value='" + data.item.secretLevel + "']").attr("checked", true);
                        $("input[name=isBrand][value='" + data.item.isBrand + "']").attr("checked", true);
                        ComEditor.initEditor();
                        ComFile.initUploadify();
                        t.initEvent();
                        t.renderCategories();
                        $("#files-container").bootstrapTable({
                            queryParams : function(params) {
                                $.extend(params, {
                                    tableIndex : 0,
                                    objectId : t.conferenceId
                                })
                                return params;
                            }
                        });
                    }
                }
            });
        },
        renderCategories : function() {
            //工作步骤
            Ajax.call({
                url : "obt/getConferenceStepList",
                p : {},
                f : function(data) {
                    if (data && data.rows) {
                        $("#step-list").append(nunjucks.render(Global.appName + '/tpl/conference/category-container.html', data));
                        t.renderConferenceStepsByConferenceId(t.conferenceId);
                    }
                }
            });
            //生活形式
            Ajax.call({
                url : "obt/getConferenceFormatList",
                p : {
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data && data.rows) {
                        $("#format-list").append(nunjucks.render(Global.appName + '/tpl/conference/category-container.html', data));
                        t.renderConferenceFormatsByConferenceId(t.conferenceId);
                    }
                }
            });
            // 生活内容
            Ajax.call({
                url : "obt/getConferenceLabelList",
                p : {
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data && data.rows) {
                        $("#label-list").append(nunjucks.render(Global.appName + '/tpl/conference/category-container.html', data));
                        t.renderConferenceLabelsByConferenceId(t.conferenceId);
                    }
                }
            });
        },
        // 渲染工作记录的工作步骤
        renderConferenceStepsByConferenceId : function(conferenceId) {
            Ajax.call({
                url : "obt/getConferenceStepsByConferenceId",
                p : {
                    conferenceId : conferenceId
                },
                f : function(data) {
                    if (data && data.rows) {
                        for ( var index in data.rows) {
                            $("#" + data.rows[index].categoryId).addClass("method-label-selected");
                        }
                    }
                }
            });
        },
        // 渲染工作记录的生活形式
        renderConferenceFormatsByConferenceId : function(conferenceId) {
            Ajax.call({
                url : "obt/getConferenceFormatsByConferenceId",
                p : {
                    conferenceId : conferenceId
                },
                f : function(data) {
                    if (data && data.rows) {
                        for ( var index in data.rows) {
                            $("#" + data.rows[index].categoryId).addClass("method-label-selected");
                        }
                    }
                }
            });
        },
        // 渲染工作记录的生活内容
        renderConferenceLabelsByConferenceId : function(conferenceId) {
            Ajax.call({
                url : "obt/getConferenceLabelsByConferenceId",
                p : {
                    conferenceId : conferenceId
                },
                f : function(data) {
                    if (data && data.rows) {
                        for ( var index in data.rows) {
                            $("#" + data.rows[index].categoryId).addClass("method-label-selected");
                        }
                    }
                }
            });
        },

        initEvent : function() {
            $(document).on("click", ".method-label", function() {
                if ($(this).hasClass("method-label-selected")) {
                    $(this).removeClass("method-label-selected");
                } else {
                    $(this).addClass("method-label-selected");
                }
            });

            $(document).on("click", "#party-branch", function() {
                $("input[name='aticle-source']").each(function() {
                    $(this).checked = false;
                });
                $(this).checked = true;
                $("#source-name").val(Global.ccpartyName);
                $("#brand-container").show();
            });
            $(document).on("click", "#party-member", function() {
                $("input[name='aticle-source']").each(function() {
                    $(this).checked = false;
                });
                $(this).checked = true;
                $("#source-name").val(Global.userName);
                $("#brand-container").hide();
            });
        },
        //验证
        checkSaveUpdateConference:function(operatorType){
            var id = $("#conference-form #id").val();
            var name = $("#conference-form #name").val();
            var occurTime = $("#conference-form #occurTime").val();
            var address = $("#conference-form #address").val();
            var attendance = $("#conference-form #attendance").val();
            var secretLevel = $('input[name="secretLevel"]:checked').val();
            var status = $("#conference-form #status").val();
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
                Notify.notice("请选择工作步骤！");
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
            if('preview'==operatorType){
                t.updateConference(operatorType);
            }else{
                bootbox.confirm({
                    size : 'small',
                    message : "确认保存？",
                    callback : function(result) {
                        if (result) {
                            t.updateConference(operatorType);
                        }
                    }
                });
            }
        },
        // 修改
        updateConference : function(operatorType) {
            var id = $("#conference-form #conferenceId").val();
            var name = $("#conference-form #name").val();
            var occurTime = $("#conference-form #occurTime").val();
            var address = $("#conference-form #address").val();
            var attendance = $("#conference-form #attendance").val();
            var secretLevel = $('input[name="secretLevel"]:checked').val();
            var status = $("#conference-form #status").val();
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
            if('publish'==operatorType){
                status = 1;
            }
            Ajax.call({
                url : "obt/editConference",
                p : {
                    id : id,
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
                    status : status,
                    secretLevel : secretLevel
                },
                f : function(data) {
                    if (data && data.success) {
                        if('close'==operatorType){
                            //关闭窗口
                            Notify.success(data.msg);
                            window.close();
                        }else if('new'==operatorType){
                            //新建窗口
                            Notify.success(data.msg);
                            ConferenceAdd.newAddConference();
                        }else if('publish'==operatorType){
                            //关闭窗口
                            Notify.success(data.msg);
                            window.close();
                        }else if('preview'==operatorType){
                            //预览
                            window.location.href = "article-preview?articleId="+id;
                        }
                    }else{
                        if(data.msg){
                            Notify.error(data.msg);
                        }else{
                            Notify.error("保存失败，请稍后再试。");
                        }
                    }
                }
            });
        },
        updateAndPublishConference : function() {
            t.updateConference(1);
        }
    }
    return t;
}();