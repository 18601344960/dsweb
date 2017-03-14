/**
 * @description 导入上报数据
 * @author yiwenjun
 * @since 2015-09-02
 */
var ReportImport = function() {
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
        },
        render : function($mainContainer) {
            $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/report/report-import-container.html'));
        },
        importReport : function() {
            var file = $("#file").val();
            if(!file||file.trim()==""){
                Notify.notice("请先选中需要上传的数据包");
                return;
            }
            var postfix = file.substring(file.lastIndexOf(".")+1,file.length);
            if(postfix!="zip"){
                Notify.notice("请确认上传的数据包为zip格式的数据包");
                return;
            }
            bootbox.confirm({
                size : 'small',
                message : "确认导入上报数据？",
                callback : function(result) {
                    if (result) {
                        $.ajaxFileUpload({
                            url : 'zbsc/reportImport',
                            data : {
                                ccpartyId : Global.ccpartyId
                            },
                            secureuri : false,
                            fileElementId : 'file',
                            dataType : 'json',
                            success : function(data, status) {
                                $("#file").val("");
                                $("#icon-path-text").val($("#icon-path-text").val()+"——导入成功");
                                Notify.info(data.msg);
                            },
                            error : function(data, status, e) {
                                $("#file").val("");
                                $("#icon-path-text").val($("#icon-path-text").val()+"——导入失败");
                                Notify.error(data.msg);
                            }
                        })
                    }
                }
            });
        }

    }
    return t;
}();