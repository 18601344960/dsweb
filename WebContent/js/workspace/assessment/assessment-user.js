/**
 * @description 答卷测评用户
 * @author 赵子靖
 * @since 2016-06-30
 */
var AssessmentUser = function() {
    var t = {
        assessmentId : '',
        ccpartyIds : '',
        search : '',
        status : '',
        mainContainer : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
        },
        initView : function(assessmentId,initType) {
            t.assessmentId = assessmentId;
            if('currentCcparty'==initType){
                t.initCurrentCcpartySubjectTree();
            }else{
                t.initCcpartySubjectTree();
            }
            t.ccpartyIds='';
            t.search='';
            t.status='';
            t.initEvent();
        },
        initEvent : function() {
            // 搜索党组织树监听事件
            $(document).on("click", "#userCcpartyId,#choose-ccparty-btn", function() {
                $("#menuContent").slideDown("fast");
                $("body").bind("mousedown", t.onCcpartyBodyDown);
            });
            
            //当前组织树搜索监听事件
            $(document).on("click", "#searchCcpartyId,#choose-ccparty-btn", function() {
                $("#menuContent").slideDown("fast");
                $("body").bind("mousedown", t.onCcpartyBodyDown);
            });
            
        },
        loadAssessmentUsers:function(){
            $("#assessment-user-table").bootstrapTable('destroy');
            $("#assessment-user-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        assessmentId : t.assessmentId,
                        ccpartyIds : t.ccpartyIds,
                        search : t.search,
                        status : t.status
                    })
                    return params;
                }
            });
        },
        loadCcpartyAssessmentUsers:function(){
            $("#assessment-user-table").bootstrapTable('destroy');
            $("#assessment-user-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        currentCcpartyId:Global.ccpartyId,
                        assessmentId : t.assessmentId,
                        ccpartyIds : t.ccpartyIds,
                        search : t.search,
                        status : t.status
                    })
                    return params;
                }
            });
        },
        //检索
        searchFunction:function(){
            t.ccpartyIds = $("#userCcpartyId").attr("ids");
            t.search = $("#search").val();
            t.status = $("#status").val();
            t.loadAssessmentUsers();
        },
        //本组织检索
        searchCcpartyFunction:function(){
            t.ccpartyIds = $("#searchCcpartyId").attr("ids");
            t.search = $("#search").val();
            t.status = $("#status").val();
            t.loadCcpartyAssessmentUsers();
        },
        // 党组织初始化树
        initCcpartySubjectTree : function() {
            ComTree.initTree({
                divContainer : "#ccparty-tree",
                url : 'pub/getAssessmentTargetTrees',
                p : {
                    assessmentId : t.assessmentId
                },
                chkStyle : "checkbox",
                enable : true,
                async : false,
                chkboxType : {
                    "Y" : "ps", 
                    "N" : "s"
                },
                onCheck : t.ccpartyCallBack
            });
        },
        // 树点击回调函数
        ccpartyCallBack : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("ccparty-tree");
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
            var ccpartyIdObj = $("#userCcpartyId");
            ccpartyIdObj.val(names);
            ccpartyIdObj.attr("ids", ids);
            $("body").bind("mousedown", t.onCcpartyBodyDown);
            t.searchFunction();
        },
        // 当前组织党组织初始化树
        initCurrentCcpartySubjectTree : function() {
            ComTree.initTree({
                divContainer : "#ccparty-tree",
                url : 'pub/getCcpartyAssessmentTargetTrees',
                p : {
                    assessmentId : t.assessmentId,
                    ccpartyId : Global.ccpartyId
                },
                chkStyle : "checkbox",
                enable : true,
                async : false,
                chkboxType : {
                    "Y" : "ps", 
                    "N" : "s"
                },
                onCheck : t.currentCcpartyCallBack
            });
        },
        // 树点击回调函数
        currentCcpartyCallBack : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("ccparty-tree");
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
            $("body").bind("mousedown", t.onCcpartyBodyDown);
            t.searchCcpartyFunction();
        },
        // 鼠标失去焦点
        onCcpartyBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
                $("#menuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        assessmentUserStatus:function(value,row){
            if(value){
               return "<font style='color:green'>已完成</font>"; 
            }else{
                return "<font style='color:red'>未完成</font>";
            }
        },
    }
    return t;
}();