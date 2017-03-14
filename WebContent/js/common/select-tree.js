/**
 * @description 下拉选择树
 * @author yiwenjun
 * @since 2015-11-28
 */
var SelectTree = function() {
    var t = {
        // 初始化下拉选择树
        // divId 选择树容器
        // url 请求url
        // divId 父id参数
        // setIdAttr1 是否将tree节点的Id设为attr1，是：true,否：false,默认是；
        // showNameAttr2 是否显示值为attr2，是：true,否：false,默认否；
        setIdAttr1 : true,
        showNameAttr2 : false,
        initSelectTree : function(divId, url, parentId, setIdAttr1, showNameAttr2) {
            t.setIdAttr1 = true;
            t.showNameAttr2 = false;
            if (setIdAttr1 != null) {
                t.setIdAttr1 = setIdAttr1;
            }
            if (showNameAttr2) {
                t.showNameAttr2 = showNameAttr2;
            }
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
            var regx = /\w+/;
            var showNode = regx.exec(treeId);
            var showNodeId = $("#" + showNode[0] + "Id");
            var showNodeName = $("#" + showNode[0] + "Name");

            if (t.setIdAttr1) {
                showNodeId.val(treeNode.attr1);
            } else {
                showNodeId.val(treeNode.id);
            }
            if (t.showNameAttr2) {
                showNodeName.val(treeNode.attr2);
            } else {
                showNodeName.val(treeNode.name);
            }

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
        },
    }
    return t;
}();
