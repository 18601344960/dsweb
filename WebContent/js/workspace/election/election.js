/**
 * @description 换届选举
 * @author yiwenjun
 * @since 2015-07-01
 */
var Election = function() {
    var t = {
        mainContainer : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initEvent();
        },
        initEvent : function() {
            $(document).on("click", "#searchCcpartyId", function() {
                t.initCcpartyTree();
                $("#searchCcpartyMenuContent").slideDown("fast");
                $("body").bind("mousedown", t.onCcpartyBodyDown);
            });
        },
        render : function() {
            t.initEelectionDetail();
        },
        initEelectionDetail : function(electionId, rowNum) {
            Ajax.call({
                url : "obt/getCurrentElection",
                p : {
                    ccpartyId : Global.ccpartyId,
                    id : electionId
                },
                f : function(data) {
                    if (data) {
                        $.extend(data, Global);
                        if (rowNum) {
                            data.rowNum = rowNum;
                        }
                        $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-container.html', data));
                        // 领导班子成员列表
                        $("#election-member-table").bootstrapTable('destroy');
                        $("#election-member-table").bootstrapTable({
                            queryParams : function(params) {
                                $.extend(params, {
                                    ccpartyId : Global.ccpartyId,
                                    electionId : data.item.id
                                })
                                return params;
                            }
                        });
                    } else {
                        Notify.error("获取失败");
                        $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-container.html'));
                    }
                }
            });
        },
        // 新增换届选举对话框
        newElectionDialog : function() {
            Ajax.call({
                url : "obt/getElectionMaxSequence",
                p : {
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-add-bill.html', data));
                        $("#election-dialog").modal({});
                    } else {
                        Notify.error("获取最大届次失败");
                    }
                }
            });
        },
        // 新增换届选举
        addElection : function() {
            bootbox.confirm({
                size : 'small',
                message : "确认新增换届选举",
                callback : function(result) {
                    if (result) {
                        var selectMode = $("#election-dialog #selectMode").val();
                        var sequence = $("#election-dialog #sequence").val();
                        var ageLimit = $("#election-dialog #ageLimit").val();
                        var startTime = $("#election-dialog #startTime").val();
                        var endTime = $("#election-dialog #endTime").val();
                        var endTime = $("#election-dialog #endTime").val();
                        var participants = $("#election-dialog #participants").val();
                        var attendance = $("#election-dialog #attendance").val();
                        if (!sequence || sequence == '') {
                            Notify.notice("届次不能为空");
                            return;
                        }
                        Ajax.call({
                            url : "obt/addElection",
                            p : {
                                ccpartyId : Global.ccpartyId,
                                selectMode : selectMode,
                                sequence : sequence,
                                ageLimit : ageLimit,
                                startTime : startTime,
                                endTime : endTime,
                                participants : participants,
                                attendance : attendance
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("保存成功");
                                    $("#election-dialog").modal("toggle");
                                    t.initEelectionDetail();
                                } else {
                                    Notify.error(data.description);
                                }
                            }
                        });
                    }
                }
            });
        },
        // 更新换届选举对话框
        editElectionDialog : function(id) {
            if (!id || id == "") {
                Notify.notice("暂无换届选举信息，请先新增一届选举信息！");
                return;
            }
            Ajax.call({
                url : "obt/getElectionById",
                p : {
                    id : id
                },
                f : function(data) {
                    if (data && data.item) {
                        $.extend(data.item, Global);
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-edit-bill.html', data.item));
                        $("#election-dialog").modal({});
                        $("#election-dialog #selectMode").val(data.item.selectMode);
                    } else {
                        Notify.error("获取失败");
                    }
                }
            });

        },
        // 更新换届选举
        updateElection : function() {
            var rowNum = $("#rowNum").val();
            bootbox.confirm({
                size : 'small',
                message : "确认保存换届选举？",
                callback : function(result) {
                    if (result) {
                        var id = $("#election-dialog #id").val();
                        var selectMode = $("#election-dialog #selectMode").val();
                        var sequence = $("#election-dialog #sequence").val();
                        var ageLimit = $("#election-dialog #ageLimit").val();
                        var startTime = $("#election-dialog #startTime").val();
                        var endTime = $("#election-dialog #endTime").val();
                        var participants = $("#election-dialog #participants").val();
                        var attendance = $("#election-dialog #attendance").val();
                        if (!sequence || sequence == '') {
                            Notify.notice("届次不能为空");
                            return;
                        }
                        Ajax.call({
                            url : "obt/updateElection",
                            p : {
                                id : id,
                                ccpartyId : Global.ccpartyId,
                                selectMode : selectMode,
                                sequence : sequence,
                                ageLimit : ageLimit,
                                startTime : startTime,
                                endTime : endTime,
                                participants : participants,
                                attendance : attendance
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("保存成功");
                                    $("#election-dialog").modal("toggle");
                                    t.initEelectionDetail(id, rowNum);
                                } else {
                                    Notify.error("保存失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 删除换届选举
        deleteElection : function(id) {
            if (!id || id == "") {
                Notify.notice("暂无换届选举信息，请先新增一届选举信息！");
                return;
            }
            bootbox.confirm({
                size : 'small',
                message : "确认删除该换届选举？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/deleteElection",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("删除成功");
                                    t.initEelectionDetail();
                                } else {
                                    Notify.error("删除失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 上一届换届选举
        lastElection : function(id, sequence, rowNum) {
            Ajax.call({
                url : "obt/getLastElection",
                p : {
                    ccpartyId : Global.ccpartyId,
                    sequence : sequence
                },
                f : function(data) {
                    if (data) {
                        if (data.item) {
                            t.initEelectionDetail(data.item.id, (Number(rowNum) - 1));
                        } else {
                            Notify.info("没有上一届");
                        }
                    } else {
                        Notify.error("获取失败");
                    }
                }
            });
        },
        // 下一届换届选举
        nextElection : function(id, sequence, rowNum) {
            Ajax.call({
                url : "obt/getNextElection",
                p : {
                    ccpartyId : Global.ccpartyId,
                    sequence : sequence
                },
                f : function(data) {
                    if (data) {
                        if (data.item) {
                            t.initEelectionDetail(data.item.id, (Number(rowNum) + 1));
                        } else {
                            Notify.info("没有下一届");
                        }
                    } else {
                        Notify.error("获取失败");
                    }
                }
            });
        },
        viewElectionProcedure : function(id) {
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-procedure-bill.html'));
            $("#election-dialog").modal({});
        },
        // 打开添加待选成员对话框
        newElectionMember : function(electionId) {
            if (!electionId || electionId == "") {
                Notify.notice("请先新增一届选举信息！然后再添加领导班子成员");
                return;
            }
            var data = {};
            data.electionId = electionId;
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-member-add.html', data));
            $("#add-election-member-dialog").modal({});
            t.loadElectionMember();
        },
        // 加载待选成员
        loadElectionMember : function() {
            var searchCcpartyId = $("#add-election-member-dialog #searchCcpartyId").attr("ids");
            var search = $("#add-election-member-dialog #search").val();
            $("#all-party-member-table").bootstrapTable("destroy");
            $("#all-party-member-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        searchCcpartyId : searchCcpartyId,
                        search : search
                    })
                    return params;
                }
            });
        },
        // 添加领导班子成员
        addElectionMember : function(electionId) {
            var flag = $("#flag").val();
            var userId = '';
            var name = '';
            var gender = '';
            var birthDay = '';
            if (flag == 'in') {
                // 系统内
                var selectedRows = $("#all-party-member-table").bootstrapTable('getSelections');
                if (selectedRows.length < 1) {
                    Notify.notice("请选择需要添加的领导班子成员");
                    return;
                }
                userId = selectedRows[0].id;
            } else if (flag == 'out') {
                // 系统外
                name = $("#userName").val();
                gender = $("input[name='sex']:checked").val(); // 性别
                birthDay = $("#birthDay").val();
                if (CheckInputUtils.isEmpty(name)) {
                    Notify.notice("请输入姓名");
                    $("#userName").focus();
                    return;
                }
            }
            bootbox.confirm({
                size : 'small',
                message : "确认添加领导班子成员？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/addElectionMember",
                            p : {
                                electionId : electionId,
                                userId : userId,
                                userName : name,
                                gender : gender,
                                birthDay : birthDay,
                                ccpartyId : Global.ccpartyId
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("添加成功");
                                    $("#add-election-member-dialog").modal("toggle");
                                    $("#election-member-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error("添加失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 打开修改领导班子成员对话框
        editElectionMember : function(memberId) {
            Ajax.call({
                url : "obt/getElectionMemberById",
                p : {
                    memberId : memberId
                },
                f : function(data) {
                    if (data) {
                        $.extend(data, Global)
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/election/election-member-edit.html', data));
                        $("#edit-election-member-dialog").modal({});
                        t.initPartyTitleTree("#partyTitle-tree", 'sys/getCodeTreeByParentId', "A070101"); // 党内职务
                    } else {
                        Notify.error("获取成员信息失败");
                    }
                }
            });
        },
        // 修改领导班子成员信息
        updateElectionMember : function(memberId) {
            bootbox.confirm({
                size : 'small',
                message : "确认修改领导班子成员？",
                callback : function(result) {
                    if (result) {
                        var memberId = $("#edit-election-member-dialog #memberId").val();
                        var partyTitleId = $("#edit-election-member-dialog #partyTitleId").val();
                        var startTime = $("#edit-election-member-dialog #startTime").val();
                        var endTime = $("#edit-election-member-dialog #endTime").val();
                        var remark = $("#edit-election-member-dialog #remark").val();
                        var sequence = $("#edit-election-member-dialog #sequence").val();
                        Ajax.call({
                            url : "obt/updateElectionMember",
                            p : {
                                memberId : memberId,
                                partyTitleId : partyTitleId,
                                startTime : startTime,
                                endTime : endTime,
                                remark : remark,
                                ccpartyId : Global.ccpartyId,
                                sequence : sequence
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("修改成功");
                                    $("#edit-election-member-dialog").modal('toggle');
                                    $("#election-member-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error("修改失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 移除候选人
        removeElectionMember : function(memberId) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除领导班子成员？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/removeElectionMember",
                            p : {
                                memberId : memberId
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("移除成功");
                                    $("#election-member-table").bootstrapTable('refresh');
                                } else {
                                    Notify.error("移除失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        // 组织树初始化
        initCcpartyId : function() {
            t.initCcpartyTree();
            var ccpartyIdObj = $("#searchCcpartyId");
            ccpartyIdObj.val('');
            ccpartyIdObj.attr("ids", '');
            t.loadElectionMember();
        },
        // 组织初始化树
        initCcpartyTree : function() {
            ComTree.initTree({
                divContainer : "#search-ccparty-tree",
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                chkStyle : "checkbox",
                enable : true,
                async : false,
                chkboxType : {
                    "Y" : "",
                    "N" : ""
                },
                onCheck : t.ccpartyCallBack
            });
        },
        // 树点击回调函数
        ccpartyCallBack : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("search-ccparty-tree");
            var nodes = zTree.getCheckedNodes(true);
            var names = "";
            var ids = "";
            for (var i = 0, l = nodes.length; i < l; i++) {
                names += nodes[i].name + ",";
                ids += nodes[i].id + ",";
            }
            if (names.length > 0) {
                names = names.substring(0, names.length - 1);
                ids = ids.substring(0, ids.length - 1);
            }
            var ccpartyIdObj = $("#searchCcpartyId");
            ccpartyIdObj.val(names);
            ccpartyIdObj.attr("ids", ids);
            t.loadElectionMember();
            $("body").bind("mousedown", t.onCcpartyBodyDown);
        },
        // 鼠标失去焦点
        onCcpartyBodyDown : function(event) {
            if (!(event.target.id == "searchCcpartyMenuBtn" || event.target.id == "searchCcpartyMenuContent" || $(event.target).parents("#searchCcpartyMenuContent").length > 0)) {
                $("#searchCcpartyMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        // 计算届满日期
        calculateEndTime : function() {
            var ageLimit = $("#ageLimit").val();
            var startTime = $("#startTime").val();
            if (!CheckInputUtils.isEmpty(startTime) && !CheckInputUtils.isEmpty(ageLimit)) {
                var args = startTime.split("-");
                var endTime = (args[0] * 1) + (ageLimit * 1);
                endTime += '-' + args[1] + "-" + args[2];
                $("#endTime").val(endTime);
            }
        },
        // 切换操作
        switchOperator : function(id) {
            if (id == 'in') {
                // 系统内
                $("#flag").val("in");
                $("#systemIn").hide();
                $("#systemOut").show();
                $("#systemInButton").show();
                $("#systemInTable").show();
                $("#systemOutTable").hide();
            } else if (id == 'out') {
                // 系统外
                $("#flag").val("out");
                $("#systemOut").hide();
                $("#systemIn").show();
                $("#systemInButton").hide();
                $("#systemInTable").hide();
                $("#systemOutTable").show();
            }
        },
        iconFormatter : function(value, row) {
            var html = "";
            if (value) {
                html += '<div><img src="uam/getHeadImg?userId=' + value.id + '" gender="' + value.gender + '" onError="Global.onIconError(this)" width=60 height=70><div>';
            }
            return html;
        },
        userFormatter : function(value, row) {
            if (value) {
                return value.name;
            }
            return "";
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
        },
        userGenderFormatter : function(value, row) {
            if (value) {
                return Global.getCodeName('A0107.' + value.gender);
            }
        },
        partyTitleFormatter : function(value, row) {
            return Global.getCodeName('A070101.' + value);
        },
        memberTitlesFormatter : function(value, row) {
            var memberTitleNames = "";
            for ( var index in value) {
                var memberTitle = value[index];
                memberTitleNames = memberTitleNames + Global.getCodeName('A070101.' + memberTitle.partyTitleId) + "、";
            }
            if (memberTitleNames.length > 0) {
                memberTitleNames = memberTitleNames.substr(0, memberTitleNames.length - 1);
            }
            return memberTitleNames;
        },
        typeFormatter : function(value, row) {
            if (value != null) {
                return Global.getEnumName('obt_party_member.type.' + value.type);
            }
        },
        selectModeFormatter : function(value, row) {
            return Global.getEnumName('obt_election.selectMode.' + value);
        },
        dateFormatter : function(value, row) {
            if (value) {
                return Global.getDate(value);
            }
            return "";
        },
        memberOperator : function(value, row) {
            var html = "";
            html += '<a href="javascript:Election.editElectionMember(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-pencil"></i>编辑</a>';
            html += '<a href="javascript:Election.removeElectionMember(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a>';
            return html;
        },
        addElectionMemberTitle : function(partyTitleId) {
            var memberId = $("#memberId").val();
            Ajax.call({
                url : "obt/addElectionMemberTitle",
                p : {
                    memberId : memberId,
                    partyTitleId : partyTitleId
                },
                f : function(data) {
                    if (data && data.success == true) {
                        Notify.success("添加成功");
                        t.refreshElectionMemberTitle();
                    } else {
                        Notify.error("添加失败");
                    }
                }
            });
        },
        // 刷新党内职务
        refreshElectionMemberTitle : function() {
            var memberId = $("#memberId").val();
            Ajax.call({
                url : 'obt/getElectionMemberTitleList',
                p : {
                    memberId : memberId,
                },
                f : function(data) {
                    if (data && data.rows) {
                        var memberTitles = "";
                        for ( var index in data.rows) {
                            var memberTitle = data.rows[index];
                            memberTitles = memberTitles + '<div class="member-name" ><div>' + Global.getCodeName('A070101.' + memberTitle.partyTitleId) + '<i class="fa fa-times" title="移除" onclick="Election.deleteElectionMemberTitle(\'' + memberTitle.id + '\')"></i></div></div>';
                        }
                        $("#choose-member-list").html(memberTitles);
                        $("#election-member-table").bootstrapTable('refresh');
                    } else {
                        Notify.error("刷新党内职务列表失败");
                    }
                }
            });
        },
        deleteElectionMemberTitle : function(memberTitleId) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除党内职务？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/deleteElectionMemberTitle",
                            p : {
                                id : memberTitleId
                            },
                            f : function(data) {
                                if (data && data.success == true) {
                                    Notify.success("删除成功");
                                    t.refreshElectionMemberTitle();
                                } else {
                                    Notify.error("删除失败");
                                }
                            }
                        });
                    }
                }
            });
        },
        initPartyTitleTree : function(divId, url, parentId) {
            var setting = {
                check : {
                    enable : false,
                    chkStyle : '',
                    chkboxType : {
                        "Y" : "ps",
                        "N" : "s"
                    },
                },
                view : {
                    dblClickExpand : false,
                    showIcon : false
                },
                async : {
                    enable : true,
                    url : url,
                    autoParam : [ "id=parentId" ]
                },
                data : {
                    simpleData : {
                        enable : true
                    }
                },
                callback : {
                    onClick : t.onNodeClick, // 点击事件
                    onCheck : null, // check事件
                    onRightClick : null, // 右键事件
                    onAsyncSuccess : null,
                    onNodeCreated : null,
                    onMouseUp : null
                }
            };
            Ajax.call({
                url : url,
                p : {
                    parentId : parentId
                },
                async : true,
                f : function(data) {
                    if (data) {
                        if (data.items) {
                            t.treeObj = $.fn.zTree.init($(divId), setting, data.items);
                        } else {
                            t.treeObj = $.fn.zTree.init($(divId), setting, data);
                        }
                    } else {
                        Notify.error("获取数据失败");
                    }
                }
            });
        },
        // 回调点击
        onNodeClick : function(event, treeId, treeNode) {
            // t.addElectionMemberTitle(treeNode.id);
            t.addElectionMemberTitle(treeNode.attr1);
        },
        // 响应选择zTree事件
        selectTree : function(param) {
            $("#" + param + "Container").slideDown("fast");
            $("body").bind("mousedown", param, t.onBodyDown);
            $("#allLower").attr("disabled", "disabled");
            var treeObj = $.fn.zTree.getZTreeObj(param + "-tree");
            var showNodeId = $("#" + param + "Id").val();
            var node = treeObj.getNodeByParam("id", showNodeId, null);
            treeObj.checkNode(node, true, false);
        },
        // 树鼠标失去焦点
        onBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == event.data + "Container" || $(event.target).parents("#" + event.data + "Container").length > 0)) {
                $("#" + event.data + "Container").fadeOut("fast");
                $("body").unbind("mousedown", event.data, t.onBodyDown);
                $("#allLower").attr("disabled", false);
            }
        }
    }
    return t;
}();