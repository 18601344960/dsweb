/**
 * @description 党员手册
 * @author 赵子靖
 * @since 2016-09-01
 */
var PartyMembersCard = function() {
    var t = {
        mainContainer : '',
        ccpartyId : Global.ccpartyId,
        ccpartyName : Global.ccpartyName,
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
        },
        initView : function() {
        },
        initEvent : function() {
            $(document).on("click", "#ccpartySelectBtn", function() {
                $("#ccpartyMenuContent").slideDown("fast");
                $("body").bind("mousedown", t.onCcpartyBodyDown);
            });
        },
        render : function() {
            var data = {
                ccpartyId : t.ccpartyId,
                ccpartyName : t.ccpartyName
            }
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/partymembers-card/partymembers-card.html',data));
            t.initCcpartyTree(); // 加载组织树
            t.getPartymembers();
        },
        // 根据组织获取党员列表
        getPartymembers : function(ccpartyId) {
            $("#partymember-right").html('<p style="text-align: center;">暂无信息</p>');
            Global.initLoader('#chats');
            Ajax.call({
                url : 'uam/getUsersByCcparty',
                p : {
                    ccpartyId : t.ccpartyId,
                    search : $("#partymember-center #name-search").val()
                },
                f : function(data) {
                    if (data && data.items) {
                        data.random = Math.random();
                        Global.initLoader("#chats");
                        $("#chats").html(nunjucks.render(Global.appName + '/tpl/workspace/partymembers-card/load-partymembers.html', data));
                        $("#" + PartyMember.userId).attr("class", "partymembers_click");
                    } else {
                        Notify.error("加载人员列表失败");
                    }
                }
            });
        },
        // 组织初始化树
        initCcpartyTree : function() {
            ComTree.initTree({
                divContainer : "#ccparty-tree",
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                async : false,
                onClick : t.ccpartyClick
            });
        },
        // 树点击回调函数
        ccpartyClick : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("ccparty-tree");
            var nodes = zTree.getCheckedNodes(true);
            var ccpartyIdObj = $("#ccpartyId");
            ccpartyIdObj.val(treeNode.name);
            ccpartyIdObj.attr("ids", treeNode.id);
            t.ccpartyId = treeNode.id;
            t.ccpartyName = treeNode.name;
            $("#ccpartyMenuContent").fadeOut("fast");
            $("body").unbind("mousedown", t.onBodyDown);
            t.getPartymembers();
        },
        // 鼠标失去焦点
        onCcpartyBodyDown : function(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "ccpartyMenuContent" || $(event.target).parents("#ccpartyMenuContent").length > 0)) {
                $("#ccpartyMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", t.onBodyDown);
            }
        },
        partymemberClick : function($this, userId) {
            $("#partymember-manager-tab").find("li").removeClass();
            $("#partymember-manager-tab").find("li:first").addClass('active');
            $(".chats li").removeClass("partymembers_click");
            $(".chats li").addClass("partymembers");
            $this.className = "partymembers_click";
            MyElectronicCard.init("#partymember-right");
            MyElectronicCard.paramter.userId=userId;
            MyElectronicCard.render();
        },
    }
    return t;
}();