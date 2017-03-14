/**
 * @description 树结构公用
 * @author yiwenjun
 * @since 2015-08-09
 */
var ComTree = function() {
    var t = {
        treeObj : '',
        onNodeCreated : function(event, treeId, treeNode) {
            // alert(treeNode.tId + ", " + treeNode.name);

        },
        // 初始化树
        initTree : function(param) {
            if (!param) {
                return;
            }
            var paramDefault = {
                dblClickExpand : false,
                simpleData : true,
                onClick : null,
                onCheck : null,
                onRightClick : null,
                onMouseUp : null,
                url : null,
                p : {},
                async : true, // Ajax.call加载方式 false同步 true异步
                divContainer : '',
                enable : false,
                showIcon : true,
                chkStyle : '',
                lowerAsyncEnable : false,// 下级数据默认同步
                chkboxType : {
                    "Y" : "ps",
                    "N" : "s"
                },
                selectedFirstNode : false
            };
            var config = $.extend(paramDefault, param);
            var setting = {
                check : {
                    enable : config.enable,
                    chkStyle : config.chkStyle,
                    chkboxType : config.chkboxType
                },
                view : {
                    dblClickExpand : config.dblClickExpand,
                    showIcon : config.showIcon
                },
                async : {
                    enable : config.lowerAsyncEnable,
                    url : config.url,
                    autoParam : [ "id=parentId" ]
                },
                data : {
                    simpleData : {
                        enable : config.simpleData
                    }
                },
                callback : {
                    onClick : config.onClick, // 点击事件
                    onCheck : config.onCheck, // check事件
                    onRightClick : config.onRightClick, // 右键事件
                    onAsyncSuccess : config.onAsyncSuccess,
                    onNodeCreated : t.onNodeCreated,
                    onMouseUp : t.onMouseUp,
                    beforeClick : config.beforeClick
                }
            };
            Ajax.call({
                url : config.url,
                p : config.p,
                async : config.async, // false同步 true异步
                f : function(data) {
                    if (data) {
                        if (config.lowerAsyncEnable) {
                            t.treeObj = $.fn.zTree.init($(config.divContainer), setting, data);
                        } else {
                            t.treeObj = $.fn.zTree.init($(config.divContainer), setting, data.items);
                        }
                        // 只展开第一层
                        if (t.treeObj.getNodes().length > 0) {
                            var treenode = t.treeObj.getNodeByParam("id", t.treeObj.getNodes()[0].id, null);
                            t.treeObj.expandNode(treenode, true, false);
                            if (config.selectedFirstNode) {
                                t.treeObj.selectNode(treenode);
                                t.treeObj.selectNode(treenode, false);
                            }
                        } else {
                            $(config.divContainer).html("暂无数据");
                        }
                    } else {
                        Notify.error("获取数据失败");
                    }
                }
            });
        },
        // 获取选中的check树值(树类型为checkbox获取值得方法)
        returnCheckedValue : function() {
            var nodes = t.treeObj.getCheckedNodes(true);
            var value = {
                names : [],
                ids : []
            }
            for (var i = 0, l = nodes.length; i < l; i++) {
                value.ids.push(nodes[i].id);
                value.names.push(nodes[i].name);
            }
            return value;
        }
    }
    return t;
}();
