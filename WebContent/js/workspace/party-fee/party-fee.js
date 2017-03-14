/**
 * @description 党费缴纳
 * @author 王钱俊
 * @since 2015-12-10
 */
var PartyFee = function() {
    var ccparty, username;
    var t = {
        mainContainer : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initEvent();
        },
        initEvent : function() {
            $(document).on("show.bs.tab", '#todo-audit-tabs a[data-toggle="tab"]', function(e) {
                var tabId = $(e.target).attr('href');
                if (tabId == "#todo-list") {
                    t.getPartyFeeList();
                } else if (tabId == "#history-list") {
                    t.getSpecialFeeList();
                } else {
                    t.countPartyFeeByYear();
                }
            })
        },
        render : function() {
            var data = {};
            var ccpartyId = Global.ccpartyId;
            data.ccpartyId = Global.ccpartyId;
            $.extend(data, Global);
            $("#main-tools").html(nunjucks.render(Global.appName + '/tpl/workspace/party-fee/party-fee-tools.html'));
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/party-fee/fee-list.html', data));
            var myDate = new Date();
            var year = myDate.getFullYear();
            var month = myDate.getMonth() + 1;
            $("#party-fee-month-select").val(month);
            t.getPartyFeeList();
        },
        getPartyFeeList : function() {
            var year = $("#party-fee-year-select").val();
            var month = $("#party-fee-month-select").val();
            $("#fee-table").bootstrapTable('destroy');
            $("#fee-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        year : year,
                        month : month
                    })
                    return params;
                },

            });
        },
        getSpecialFeeList : function() {
            var year = $("#special-fee-year-select").val();
            $("#special-fee-table").bootstrapTable('destroy');
            $("#special-fee-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        year : year
                    })
                    return params;
                }
            });

        },
        countPartyFeeByYear : function() {
            var year = $("#count-fee-year-select").val();
            $("#count-fee-table").bootstrapTable('destroy');
            $("#count-fee-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : Global.ccpartyId,
                        year : year
                    })
                    return params;
                },
                onLoadSuccess : function(data) {
                    $('#count-fee-table tr:last').css({
                        "background-color" : "#FFF0F5",
                        "font-family" : "SimHei",
                        "font-size" : "12pt"
                    });
                }
            });
        },
        printParyPDF : function() {
            Ajax.call({
                url : 'obt/getUsersByCcparty',
                p : {
                    ccpartyId : Global.ccpartyId,
                },
                f : function(data) {
                    if (data && data.items) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/party-fee/dialogs/party-fee-print-empty.html', data));
                        $("#print-empty-dialog").modal({});
                        var myDate = new Date();
                        var year = myDate.getFullYear();
                        var month = myDate.getMonth() + 1;
                        var time;
                        if (month < 10) {
                            time = year + "-0" + month;
                        } else {
                            time = year + "-" + month;
                        }
                        $("#print-time").val(time);
                    } else {
                        Notify.error("加载人员列表失败");
                    }
                }
            });
        },
        printEmptyPDF : function() {
            var time = $("#print-time").val();
            var year = time.substring(0, 4);
            var month = time.substring(5, 8);
            var ccpartyId = Global.ccpartyId;
            var userIdList = "";
            $("input[name='checkbox']:checked").each(function() {
                userIdList += this.value + ",";
            });
            if (userIdList != "") {
                userIdList = userIdList.substring(0, userIdList.length - 1);
            }
            window.open("reports/obt/PartyFee?format=pdf&year=" + year + "&ccpartyId=" + ccpartyId + "&userIdList=" + userIdList + "&month=" + month);
        },
        printParyPDFWithData : function() {
            var year = $("#count-fee-year-select").val();
            var ccpartyId = Global.ccpartyId;
            window.open("reports/obt/PartyFeeData?format=pdf&year=" + year + "&ccpartyId=" + ccpartyId);
        },
        printLargeParyPDF : function() {
            var ccpartyId = Global.ccpartyId;
            window.open("reports/obt/LargePartyFee?format=pdf&ccpartyId=" + ccpartyId);
        },

        blurEvent : function(userId) {
            var reg = /^\d+((\.?$)|(\.\d$)|(\.\d\d$))|^免?$/;
            var payIn = $.trim($("#" + userId + "PayIn").val());
            if (payIn == "" || payIn.length < 1) {
                $("#" + userId + "PayIn").val("0.00")
            } else if (reg.test(payIn) == false) {
                var year = $("#party-fee-year-select").val();
                var month = $("#party-fee-month-select").val();
                Ajax.call({
                    url : "obt/getUsersByUserId",
                    p : {
                        userId : userId,
                        year : year,
                        month : month
                    },
                    f : function(data) {
                        if (data && data.items) {
                            payIn = data.items.payIn;
                            if (payIn == -1) {
                                payIn = '免';
                            } else {
                                payIn = data.items.payIn.toFixed(2);
                            }
                        } else {
                            payIn = '0.00';
                        }
                    }
                });
                $("#" + userId + "PayIn").val(payIn);
            } else if (payIn.substring(payIn.length - 1) == '.') {
                payIn = payIn.substring(0, payIn.indexOf('.'));
                payIn = parseFloat(payIn).toFixed(2);
                $("#" + userId + "PayIn").val(payIn);
            } else if (payIn != '免') {
                payIn = parseFloat(payIn).toFixed(2);
                $("#" + userId + "PayIn").val(payIn);
            }
        },
        savePayIn : function(userId, index) {
            var year = $("#party-fee-year-select").val();
            var month = $("#party-fee-month-select").val();
            var beforeChangePayIn;
            var payAble;
            index = parseInt(index) + 1;
            var payIn = $.trim($("#" + userId + "PayIn").val());
            var reg = /^\d+((\.?$)|(\.\d$)|(\.\d\d$))|^免?$/;
            if (payIn == "" || payIn.length < 1) {
                Ajax.call({
                    url : "obt/savePayIn",
                    p : {
                        userId : userId,
                        payActually : 0,
                        year : year,
                        month : month
                    },
                    f : function(data) {
                        if (data.success) {

                        } else {
                            Notify.error("设置免缴失败");
                        }
                    }
                });
            } else if (reg.test(payIn) == false) {
                Ajax.call({
                    url : "obt/getUsersByUserId",
                    p : {
                        userId : userId,
                        year : year,
                        month : month
                    },
                    f : function(data) {
                        if (data && data.items) {
                            beforeChangePayIn = data.items.payIn;
                            if (beforeChangePayIn == -1) {
                                beforeChangePayIn = '免';
                            } else {
                                beforeChangePayIn = data.items.payIn.toFixed(2);
                            }
                        } else {
                            beforeChangePayIn = '0.00';
                        }
                    }
                })
                alert("只能输入两位小数或免");
                $("#" + userId + "PayIn").val(beforeChangePayIn);
                return;
            } else if (payIn == "免") {
                Ajax.call({
                    url : "obt/savePayIn",
                    p : {
                        userId : userId,
                        payActually : -1,
                        year : year,
                        month : month
                    },
                    f : function(data) {
                        if (data.success) {
                        } else {
                            Notify.error("设置免缴失败");
                        }
                    }
                })
            } else {
                Ajax.call({
                    url : "obt/savePayIn",
                    p : {
                        userId : userId,
                        payActually : PartyFee.getTwoDecimals(payIn),
                        year : year,
                        month : month
                    },
                    f : function(data) {
                        if (data.success) {

                        } else {
                            Notify.error("缴纳党费失败");
                        }
                    }
                })
            }
        },
        deleteSpecial : function(id) {
            bootbox.confirm({
                size : 'small',
                message : "确认删除？",
                callback : function(result) {
                    if (result) {
                        Ajax.call({
                            url : "obt/deletePartyFeeSpecial",
                            p : {
                                id : id
                            },
                            f : function(data) {
                                if (data) {
                                    Notify.success("删除成功");

                                    $("#special-fee-table").bootstrapTable("refresh");
                                } else {
                                    Notify.error("删除失败");
                                }
                            }
                        })
                    }
                }
            })
        },
        SpecialFeeAddDailog : function(id, name) {
            var userMc = {
                id : id,
                name : name
            };
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/party-fee/dialogs/add-special-fee.html', userMc));
            $("#targets").val(userMc.name);
            $("#targets").attr("ids", userMc.id);
            t.initCcpartyUserTree();
            $("#add-special-fee-dialog").modal({});
        },
        initCcpartyUserTree : function() {
            ComTree.initTree({
                divContainer : "#use-tree",
                url : 'org/loadTreePartyMemberByCcpartyId',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                enable : false,
                async : false, // false同步 true异步
                onClick : t.zTreeOnClick
            });
        },
        zTreeOnClick : function(event, treeId, treeNode) {
            var names = treeNode.name;
            var ids = treeNode.id;
            var targets = $("#targets");
            targets.val(names);
            targets.attr("ids", ids);
        },
        // 组织树鼠标失去焦点
        onFeeUseBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "userTreeContent" || $(event.target).parents("#userTreeContent").length > 0)) {
                $("#userTreeContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
                $("#allLower").attr("disabled", false);
            }
        },
        saveSpecialFee : function() {
            var userId = $("#targets").attr("ids");
            var amount = $("#special-amount").val();
            var remark = $("#special-remark").val();
            var year = $("#special-fee-year-select").val();
            var judgeAmount = CheckInputUtils.splitSpaceForStrAround(amount);
            if (judgeAmount == '') {
                judgeAmount = 0;
            } else {
                if (!CheckInputUtils.moneyCheck(judgeAmount)) {
                    return;
                } else {
                    judgeAmount = PartyFee.getTwoDecimals(judgeAmount);
                }
            }
            Ajax.call({
                url : "obt/addPartyFeeSpecial",
                p : {
                    year : year,
                    userId : userId,
                    amount : judgeAmount,
                    remark : remark,
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        Notify.success("添加成功");
                        $("#add-special-fee-dialog").modal('toggle');
                        $("#special-fee-import-table").bootstrapTable("refresh");
                        $("#special-fee-table").bootstrapTable("refresh");
                    } else {
                        Notify.error("添加失败");
                    }
                }
            })
        },
        updateSpecial : function(id) {
            Ajax.call({
                url : "obt/getPartyFeeSpecialById",
                p : {
                    id : id
                },
                f : function(data) {
                    if (data) {
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/party-fee/dialogs/edit-special-fee.html', data.result));
                        $("#targets").val(data.result.userMc.name);
                        $("#targets").attr("ids", data.result.userMc.id)
                        $("#special-remark").val(data.result.remark);
                        t.initCcpartyUserTree();
                        $("#edit-special-fee-dialog").modal({});
                    } else {
                        Notify.error("删除失败");
                    }
                }
            })
        },
        saveEditSpecialFee : function() {
            var id = $("#special-id").val();
            var userId = $("#targets").attr("ids");
            var amount = $("#special-amount").val();
            var judgeAmount = CheckInputUtils.splitSpaceForStrAround(amount);
            if (judgeAmount == '') {
                judgeAmount = 0;
            } else {
                if (!CheckInputUtils.moneyCheck(judgeAmount)) {
                    return;
                } else {
                    judgeAmount = PartyFee.getTwoDecimals(judgeAmount);
                }
            }
            var remark = $("#special-remark").val();
            Ajax.call({
                url : "obt/updatePartyFeeSpecial",
                p : {
                    id : id,
                    userId : userId,
                    amount : judgeAmount,
                    remark : remark
                },
                f : function(data) {
                    if (data) {
                        Notify.success("编辑成功");
                        $("#edit-special-fee-dialog").modal('toggle');
                        $("#special-fee-import-table").bootstrapTable("refresh");
                        $("#special-fee-table").bootstrapTable("refresh");
                    } else {
                        Notify.error("添加失败");
                    }
                }
            })
        },
        saveReceiver : function(userId) {
            var year = $("#party-fee-year-select").val();
            var month = $("#party-fee-month-select").val();
            var receiver = $("#" + userId + "Receiver").val();
            Ajax.call({
                url : "obt/saveReceiver",
                p : {
                    userId : userId,
                    year : year,
                    month : month,
                    receiver : receiver
                },
                f : function(data) {
                    if (data) {
                    } else {
                        Notify.error("添加失败");
                    }
                }
            })
        },
        saveReceivDate : function(userId, receiveDate) {
            var year = $("#party-fee-year-select").val();
            var month = $("#party-fee-month-select").val();
            Ajax.call({
                url : "obt/saveReceivDate",
                p : {
                    userId : userId,
                    year : year,
                    month : month,
                    receiveDate : receiveDate
                },
                f : function(data) {
                    if (data) {
                    } else {
                        Notify.error("添加失败");
                    }
                }
            })
        },
        savePartyFeeRemark : function(userId, index) {
            var year = $("#party-fee-year-select").val();
            var month = $("#party-fee-month-select").val();
            var remark = $.trim($("#" + userId + "Remark").val());
            Ajax.call({
                url : "obt/savePartyFeeRemark",
                p : {
                    userId : userId,
                    remark : remark,
                    year : year,
                    month : month
                },
                f : function(data) {
                    if (data.success) {
                    } else {
                        Notify.error("备注失败");
                    }
                }
            })
        },
        selectUser : function(selectOrNot) {
            if (selectOrNot == "0") {
                $("[name=checkbox]:checkbox").prop("checked", true);
            } else {
                $("[name=checkbox]:checkbox").prop("checked", false);
            }
        },
        getTwoDecimals : function(value) {
            var TwoDecimals;
            if (value.indexOf(".") != -1) {
                var decimalsPoints = value.substring(value.indexOf(".") + 1).length;
                if (decimalsPoints >= 3) {// 小数点位数大于三
                    value = value.substring(0, value.indexOf(".") + 3);
                    TwoDecimals = parseFloat(value);
                    TwoDecimals = TwoDecimals + 0.01;
                    return TwoDecimals;
                } else {
                    return parseFloat(value).toFixed(2);
                }
            } else {
                return parseFloat(value).toFixed(2);
            }
        },

        payInFormatter : function(value, row, index) {
            var userId = value.userId;
            var payIn = value.payIn;
            if (payIn == "-1") {
                payIn = "免"
            } else {
                payIn = payIn.toFixed(2);
            }
            return '<input type="text" style="width: 100px;margin:0 auto" class="form-control" value="' + payIn + '" id = "' + value.userId + 'PayIn" onkeyup  = "PartyFee.savePayIn(\'' + value.userId + '\',\'' + index + '\')" onblur = "PartyFee.blurEvent(\'' + userId + '\')"/>';
        },

        yearFormatter : function(value, row) {
            if (!value) {
                return "";
            } else {
                return '<span id="year">' + value.year + '</span>';
            }
        },
        monthFormatter : function(value, row) {
            if (!value) {
                return "";
            } else {
                return '<span id="month">' + value.month + '</span>';
            }
        },
        receiverFormatter : function(value, row) {
            if (!value) {
                return "";
            } else {
                var html = '<select id="' + value.userId + 'Receiver" style = "width: 100px;margin:0 auto" class="form-control" onchange="PartyFee.saveReceiver(\'' + value.userId + '\')  "><option value = ""></option>';
                var length = row.receiverList.length;
                for (var i = 0; i < length; i++) {
                    if (value.receiver == row.receiverList[i].name) {
                        html += '<option value = "' + row.receiverList[i].name + '" selected="selected">' + row.receiverList[i].name + '</option>';
                    } else {
                        html += '<option value = "' + row.receiverList[i].name + '" >' + row.receiverList[i].name + '</option>';
                    }
                }
                html += '</select>';
                return html;
            }
        },
        receiverDateFormatter : function(value, row) {
            var dateFmt = "dateFmt:'yyyy-MM-dd'";
            if (value.receiveDate) {
                var date = value.receiveDate.substring(0, 10);
                return '<input type="text" style = "width: 100px;margin:0 auto" class="form-control"  value="' + date + '" id="' + value.userId + 'ReceiverTime" onClick="WdatePicker({' + dateFmt + ',onpicking:function(dp) {PartyFee.saveReceivDate(\'' + value.userId
                        + '\',dp.cal.getNewDateStr())}})" />';
            } else {
                return '<input type="text" style="width: 100px;margin:0 auto" class="form-control" value="" id="' + value.userId + 'ReceiverTime" onClick="WdatePicker({' + dateFmt + ',onpicking:function(dp) {PartyFee.saveReceivDate(\'' + value.userId + '\',dp.cal.getNewDateStr())}})" />';
            }

        },
        remarkFormatter : function(value, row, index) {
            var userId = value.userId;
            var remark = value.remark;
            if (!remark) {
                remark = "";
            }
            return '<input type="text" style="width: 200px;margin:0 auto" class="form-control" value="' + remark + '" id = "' + userId + 'Remark" onblur="PartyFee.savePartyFeeRemark(\'' + userId + '\',\'' + index + '\')"/>';
        },
        partyFeeSpecialFormatter : function(value, row) {
            if (!value) {
                return '<span id="total">0.00</span>';
            } else if (value == "0") {
                return '<span id="partyFeeSpecial">0.00</span>';
            } else {

                return '<span id="partyFeeSpecial">' + value.toFixed(2) + '</span>';
            }
        },
        totalFee : function(value, row) {
            if (!value) {
                return '<span id="total">0.00</span>';
            } else if (value == "0") {
                return '<span id="total">0.00</span>';
            } else {

                return '<span id="total">' + value.toFixed(2) + '</span>';
            }
        },
        userName : function(value, row) {
            if (!value) {
                return "";
            } else {
                return value.name;
            }
        },
        amount : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == "0") {
                return '<span>0.00</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        january : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        february : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        march : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        april : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        may : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        june : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        july : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        august : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        september : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        octorber : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        november : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        december : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else if (value == -1) {
                return '<span>免</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        specialFee : function(value, row) {
            if (!value) {
                return '<span>0.00</span>';
            } else {
                return '<span>' + value.toFixed(2) + '</span>';
            }
        },
        detailFormatter : function(index, row) {
            var html = "<div name='row_detail' style ='background-color:white;' ><label>明细：</label></br><div style='width:70%; margin:auto;'>";
            html += "<table class='party-special-detail-table' border = '1'  >";
            html += "<tr>";
            html += "<td align = 'center'  width = '20%'>姓名</td>";
            html += "<td align = 'center'  width = '20%'>金额</td>";
            html += "<td align = 'center'  width = '30%'>缴纳时间</td>";
            html += "<td align = 'center'  width = '30%' >操作</td>";
            html += "</tr>";
            Ajax.call({
                url : 'obt/getPartyFeeSpecialDetailByYear',
                p : {
                    year : row.year,
                    ccpartyId : row.ccpartyId,
                    userId : row.userId
                },
                async : false, // false同步 true异步
                f : function(data) {
                    if (data.rows) {
                        for (var i = 0; i < data.rows.length; i++) {
                            html += "<td align = 'center'>" + data.rows[i].userMc.name + "</td>";
                            html += "<td  align = 'center'>" + data.rows[i].amount.toFixed(2) + "</td>";
                            html += "<td  align = 'center'>" + data.rows[i].createTime + "</td>";
                            html += '<td align="center"> <a href="javascript:PartyFee.updateSpecial(\'' + data.rows[i].id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-edit"></i>编辑</a>';
                            html += '<a href="javascript:PartyFee.deleteSpecial(\'' + data.rows[i].id + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-trash"></i>删除</a></td>';
                            html += "</tr>";
                        }
                    }
                }
            });
            html += "</table></div></div>";
            return html;
        },
        specialOperate : function(value, row) {
            var SpecialFeeAddDailog = '<a href="javascript:PartyFee.SpecialFeeAddDailog(\'' + row.userMc.id + '\',\'' + row.userMc.name + '\')" class="btn btn-default btn-xs"><i class="glyphicon glyphicon-plus"></i>增加</a>';
            return SpecialFeeAddDailog;
        }
    }
    return t;

}();
