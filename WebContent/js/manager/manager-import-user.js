/**
 * @description 用户导入
 * @author 赵子靖
 * @since 2016-08-22
 */
var ManagerImportUser = function() {
    var t = {
        mainContainer : '',
        ccpartyId : '',
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
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/manager/excel-import-user.html'));
            ComFile.initUploadify();
            t.initCcpartySubjectTree();
        },
        // 行政组织树
        initCcpartySubjectTree : function() {
            ComTree.initTree({
                divContainer : "#ccparty-tree",
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                onClick : t.ccpartyClickCallBack,
                selectedFirstNode : false
            });
        },
        ccpartyClickCallBack : function(e, treeId, treeNode) {
            if('Z000104'!=treeNode.type){
                Notify.notice("导入组织必须选择为党支部。");
                t.ccpartyId = '';
            }else{
                t.ccpartyId = treeNode.id;
            }
        },
        // excel文件导入
        beginExcelImportPartymemberInfo : function() {
            var files = ComFile.getUploadFiles();
            if (!t.ccpartyId || t.ccpartyId.trim() == "") {
                Notify.notice("请在左边选择待导入党员所在党组织。");
                return;
            }
            for (var i = 0; i < files.length; i++) {
                var suffix = files[i].fileName.substring(files[i].fileName.lastIndexOf("\.") + 1);
                if (suffix != 'xls' && suffix != 'xlsx') {
                    Notify.notice("请选择正确的Excel文件上传。");
                    return;
                }
            }
            if (CheckInputUtils.isEmpty(files)) {
                Notify.notice("请先选择需要导入的党员的数据文件。");
                return;
            }
            bootbox.confirm({
                size : 'small',
                message : "确认开始导入用户数据？",
                callback : function(result) {
                    if (result) {
                        // 导入对话框遮罩
                        $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/manager/dialogs/import-wait-dialog.html'));
                        $("#import-wait-dialog").modal({});
                        Global.initLoader();
                        Ajax.call({
                            url : "obt/beginExcelImportPartymemberInfo",
                            p : {
                                files : JSON.stringify(files),
                                ccpartyId : t.ccpartyId
                            },
                            timeout : 1800000, // 超时时间设置，单位毫秒 超时时间：半小时
                            f : function(data) {
                                $("#import-wait-dialog").modal('toggle');
                                if (data && data.success) {
                                    Notify.success(data.msg);
                                    t.render();
                                } else {
                                    Notify.error(data.msg);
                                }
                            }
                        });
                    }
                }
            });
        }
    }
    return t;
}();