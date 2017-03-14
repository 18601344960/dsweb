/**
 * @description 工作手册
 * @author 赵子靖
 * @since 2016-09-03
 */
var WorkCard = function() {
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
        },
        render : function() {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/work-card/work-card.html'));
            t.initCcpartyTree(); // 加载组织树
            t.loadWorkCard();
        },
        // 组织初始化树
        initCcpartyTree : function() {
            ComTree.initTree({
                divContainer : "#ccparty-tree",
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                selectedFirstNode:true,
                async : false,
                onClick : t.ccpartyClick
            });
        },
        // 树点击回调函数
        ccpartyClick : function(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("ccparty-tree");
            var nodes = zTree.getCheckedNodes(true);
            t.ccpartyId = treeNode.id;
            t.ccpartyName = treeNode.name;
            t.loadWorkCard();
        },
        loadWorkCard:function(){
            MyCcpartyCard.init('#work-card');
            MyCcpartyCard.paramter.ccpartyId=t.ccpartyId;
            MyCcpartyCard.render();
        }
        
    }
    return t;
}();