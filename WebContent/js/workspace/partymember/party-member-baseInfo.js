/**
 * @description 党员管理-基础信息维护
 * @author zhaozijing
 * @since 2015-07-14
 */
var PartyMemberBaseInfo = function() {
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
            $(document).on("click", "#organizationId", function() {
                t.initOrganizationSubjectTree();
                var ccpartyIdObj = $("#organizationId");
                var ccpartyIdOffset = $("#organizationId").offset();
                $("#organizationMenuContent").css({}).slideDown("fast");
                $("body").bind("mousedown", t.onBodyOrgDown);
            });
        },
        onBodyOrgDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "organizationMenuContent" || $(event.target).parents("#organizationMenuContent").length > 0)) {
                $("#organizationMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyOrgDown);
            }
        },
        // 行政组织树
        initOrganizationSubjectTree : function() {
            var setting = {
                check : {
                    enable : true,
                    chkStyle : "radio",
                    radioType : "all"
                },
                view : {
                    dblClickExpand : false
                },
                data : {
                    simpleData : {
                        enable : true
                    }
                },
                callback : {
                    onClick : t.organizationTreeClick,
                    onCheck : t.organizationTreeCheck
                }
            };
            Ajax.call({
                url : 'org/getCurrentOrganizationAndSunsToTreeView',
                p : {
                    organizationId : Global.organizationId
                },
                f : function(data) {
                    if (data && data.items) {
                        $.fn.zTree.init($("#organization-tree"), setting, data.items);
                    } else {
                        Notify.error("获取组织树失败");
                    }
                }
            });
        },
        organizationTreeClick : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("organization-tree");
            zTree.checkNode(treeNode, !treeNode.checked, null, true);
            return false;
        },
        organizationTreeCheck : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("organization-tree");
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
            var ccpartyIdObj = $("#organizationId");
            ccpartyIdObj.val(names);
            ccpartyIdObj.attr("ids", ids);
        },
        // 根据会员ID加载会员基本信息和党员信息
        loadUserInfo : function(userId, pId) {
            Global.initLoader("#right-user-info");
            if (userId == null || userId == "undefined" || userId == "") {
                PartyMember.userId = '';
                PartyMember.userName = '';
                $("#right-user-info").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-member-baseinfo.html'));
                // 去掉群众和团委
                $("#user_type option[value='A476203']").remove();
                $("#user_type option[value='A476213']").remove();
                SelectTree.initSelectTree("#userNation-tree", 'sys/getCodeTreeByParentId', "A0121"); // 民族
                SelectTree.initSelectTree("#userBirthPlace-tree", 'sys/getCodeTreeByParentId', "A0114"); // 籍贯
                SelectTree.initSelectTree("#userEducation-tree", 'sys/getCodeTreeByParentId', "A0405"); // 学历
                SelectTree.initSelectTree("#degree-tree", 'sys/getCodeTreeByParentId', "A0440"); // 学位

                var obj = {
                        pId : pId
                }
                $("#right-partymember-info").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-member-partyinfo.html', obj));
                $("#partymember-table #ccpartyId").attr("ids",PartyMember.ccpartyId);
                $("#partymember-table #ccpartyId").val(PartyMember.ccpartyName);
            } else {
                Ajax.call({
                    url : 'uam/loadUserInfoByUserId',
                    p : {
                        userId : userId
                    },
                    f : function(data) {
                        if (data && data.item) {
                            data.item.globalUserType = Global.userType;
                            PartyMember.userId = data.item.id;
                            data.item.success = data.success;
                            data.item.ids = data.ids;
                            data.item.names = data.names;
                            data.item.birthDay = Global.getDate(data.item.birthDay);
                            data.item.random = Math.random();
                            data.item.showIdNumber = '';
                            if (!CheckInputUtils.isEmpty(data.item.idNumber)) {
                                for (var i = 0; i < data.item.idNumber.length - 8; i++) {
                                    data.item.showIdNumber += '*';
                                }
                                data.item.showIdNumber += data.item.idNumber.substr(data.item.idNumber.length - 8);
                            }
                            data.item.Global = Global;
                            $("#right-user-info").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-member-baseinfo.html', data.item));
                            $("input[type=radio][name='sex'][value=" + data.item.gender + "]").attr("checked", 'checked');
                            $("input[type=radio][name='user_status'][value=" + data.item.status + "]").attr("checked", 'checked');
                            if (data.department != null) {
                                $("#departmentId").val(data.department.id);
                            }
                            $("#user_type").val(data.item.type);
                            // 去掉群众和团委
                            $("#user_type option[value='A476203']").remove();
                            $("#user_type option[value='A476213']").remove();

                            $("#userNationId").val(data.item.nation);
                            $("#userNationName").val(Global.getCodeName("A0121." + data.item.nation));
                            $("#userBirthPlaceId").val(data.item.birthPlace);
                            $("#userBirthPlaceName").val(Global.getCodeName("A0114." + data.item.birthPlace, "name1"));
                            $("#userEducationId").val(data.item.education);
                            $("#userEducationName").val(Global.getCodeName("A0405." + data.item.education));
                            $("#degreeName").val(Global.getCodeName("A0440." + data.item.degree));
                            $("#degreeId").val(data.item.degree);
                            
                            PartyMemberInfo.loadPartyMemberInfo(userId, pId);
                        } else {
                            Notify.error("加载用户基本信息失败");
                        }
                    }
                });
            }
        },
        // 对用户的信息校验
        checkUserInput : function(user_paramter) {
            var user_id = $("#id").val(); // 用户ID
            var user_loginNo = $("#loginNo").val();// 登录账号
            var user_name = CheckInputUtils.splitSpaceForStrAround($("#user_name").val()); // 姓名
            var user_idNumber = CheckInputUtils.splitSpaceForStrAround($("#user_idNumber").val()); // 身份证
            var user_gender = $("input[name='sex']:checked").val(); // 性别
            var user_nation = $("#userNationId").val(); // 民族
            var user_birthPlace = $("#userBirthPlaceId").val(); // 籍贯
            var user_occupation = CheckInputUtils.splitSpaceForStrAround($("#user_occupation").val()); // 职业
            var user_education = $("#userEducationId").val(); // 教育程度
            var user_jobTitle = CheckInputUtils.splitSpaceForStrAround($("#user_jobTitle").val()); // 行政职务
            var user_email = CheckInputUtils.splitSpaceForStrAround($("#user_email").val()); // 电邮
            var user_officePhone = CheckInputUtils.splitSpaceForStrAround($("#user_officePhone").val());// 办公电话
            var user_mobile = CheckInputUtils.splitSpaceForStrAround($("#user_mobile").val()); // 手机
            var user_status = $("input[name='user_status']:checked").val(); // 状态
            var user_type = $("#user_type").val();
            var user_departmentId = $("#departmentId").val(); // 部门
            var user_birthday = $("#user_birthday").val(); // 出生年月
            var user_address = $("#user_address").val(); // 家庭地址
            var degree = $("#degreeId").val(); // 学位
            var sequence = $("#sequence").val();//显示顺序
            if (CheckInputUtils.isEmpty(user_loginNo)) {
                Notify.notice("请输入登录账号。");
                $("#loginNo").focus();
                return;
            } else if (CheckInputUtils.isEmpty(user_name)) {
                Notify.notice("请输入姓名");
                $("#user_name").focus();
                return;
            } else if (CheckInputUtils.isEmpty(user_type)) {
                Notify.notice("请选择政治面貌。");
                $("#user_type").focus();
                return;
            }
            user_paramter.id = user_id;
            user_paramter.loginNo = user_loginNo;
            user_paramter.name = user_name;
            user_paramter.idNumber = user_idNumber;
            user_paramter.gender = user_gender;
            user_paramter.nation = user_nation;
            user_paramter.birthPlace = user_birthPlace;
            user_paramter.occupation = user_occupation;
            user_paramter.education = user_education;
            user_paramter.jobTitle = user_jobTitle;
            user_paramter.email = user_email;
            user_paramter.officePhone = user_officePhone;
            user_paramter.mobile = user_mobile;
            user_paramter.status = user_status;
            user_paramter.departmentId = user_departmentId;
            user_paramter.birthday = user_birthday;
            user_paramter.address = user_address;
            user_paramter.type = user_type;
            user_paramter.degree = degree;
            user_paramter.sequence = sequence;
            return user_paramter;
        },
        // 更换上传头像
        replaceHeadImg : function() {
            if (PartyMember.userId == null || PartyMember.userId == '') {
                Notify.info("请在左侧选中用户！");
                return;
            }
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/partymember-replace-head-dialog.html'));
            $("#replace-head-dialog").modal({});
        },
        // 保存头像
        saveHeadImg : function() {
            var headImg = $("#headImg").val();
            // 对头像进行验证
            var suffix = headImg.substring(headImg.indexOf("."), headImg.length);
            if (!CheckInputUtils.checkIsImage(suffix)) {
                Notify.notice("请选择正确的图片，格式后缀名应以'.gif', '.jpg', '.jpeg', '.png'结尾，大小在500KB之内！");
                return;
            }
            // 使用ajaxfileupload异步上传文件
            $.ajaxFileUpload({
                url : 'uam/replaceHeadImg', // 用于文件上传的服务器端请求地址
                data : {
                    userId : PartyMember.userId
                }, // 此参数非常严谨，写错一个引号都不行
                secureuri : false, // 是否需要安全协议，一般设置为false
                fileElementId : 'headImg', // 文件上传域的ID
                dataType : 'json', // 返回值类型 一般设置为json
                success : function(data, status) // 服务器成功响应处理函数
                {
                    Notify.info(data.msg);
                    // 将image href连接更换
                    $("#headImgId").attr("src", "uam/getHeadImg?userId=" + PartyMember.userId + "&timestam=" + new Date().getTime());
                    $("#replace-head-dialog").modal('toggle');
                    $("#head_" + PartyMember.userId).attr("src", "uam/getHeadImg?userId=" + PartyMember.userId + "&timestam=" + new Date().getTime());
                },
                error : function(data, status, e) // 服务器响应失败处理函数
                {
                    Notify.error(data.msg);
                }
            })// end ajaxfileupload

        },
        // 密码重置
        resetPasswordDialog : function(id) {
            var data = {
                id : id
            }
            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/user-reset-password-dialog.html', data));
            $("#reset-dialog").modal({});
        },
        // 密码重置提交事件
        resetUserPasswordSubmit : function() {
            var id = $("#id").val();
            var password = $("#password").val();
            var confirmPassword = $("#confirmPassword").val();
            if (CheckInputUtils.isEmpty(password)) {
                Notify.notice("请输入重置密码");
                $("#password").focus();
                return;
            } else if (CheckInputUtils.isEmpty(confirmPassword)) {
                Notify.notice("请再次输入重置密码");
                $("#confirmPassword").focus();
                return;
            } else if (password != confirmPassword) {
                Notify.notice("两次输入的密码不一致");
                $("#confirmPassword").focus();
                return;
            }
            Ajax.call({
                url : 'uam/resetUserPasswordByManager',
                p : {
                    id : id,
                    newPassword : password
                },
                f : function(data) {
                    if (data.success) {
                        Notify.success(data.msg);
                        $("#reset-dialog").modal('toggle');
                        $("#user-process-definition-table").bootstrapTable('refresh');
                    } else {
                        Notify.error(data.msg);
                    }
                }
            });
        },
        //验证登录账号用于修改用户使用
        checkLoginNoForUpdate:function(loginNo,inputId){
            var beforeLoginNo = $("#beforeLoginNo").val();
            if(beforeLoginNo!=loginNo){
                UserUtils.checkLoginNo(loginNo,inputId);
            }
        }
    }
    return t;
}();