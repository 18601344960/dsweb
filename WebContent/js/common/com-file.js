/**
 * @description 文件操作公用
 * @author yiwenjun
 * @since 2015-07-10
 */
var ComFile = function() {
    var t = {
        uploadId : 'file-upload',
        uploadQueueId : 'file-upload-queue',
        // 初始化文件上传
        initUploadify : function() {
            $("#" + t.uploadId).uploadifive({
                buttonText : '选择文件',
                'uploadScript' : 'file/uploadFile',
                auto : true,
                removeCompleted : false,
                buttonClass : 'file-upload-btn',
                fileObjName : 'file',
                queueID : 'file-upload-queue',
                height : 50,
                width : 100,
                formData : {},
                onFallback : function() {
                    alert("该浏览器无法使用上传组件!");
                },
                onUpload : function(file) {
                },
                onUploadComplete : function(file, data) {
                    t.uploadSuccess(file, data);
                }
            });
        },
        // 销毁文件上传
        destroy : function() {
            $("#" + t.uploadId).uploadifive('destroy');
        },
        uploadSuccess : function(file, filePath) {
            var id = file.queueItem[0].id;
            if (!filePath || filePath.trim() == "") {
                var fileName = $("#" + id).find(".fileName").html();
                $("#" + id).find(".fileName").html('<span style="color:red;">' + fileName + '</span>')
                $("#" + id).find(".data").html('<span style="color:red;">-上传失败</span>');
                Notify.error("上传文件失败，可能无法访问文件服务器");
            } else {
                filePath = filePath.replace(/^\"|\"$/g, '');
                filePath = filePath.replace(/\\\\/g, '\\');
                $("#" + id).attr("filePath", filePath);
                $("#" + id).attr("fileName", file.name);
                $("#" + id).attr("fileSize", file.size);
            }
        },
        // 获取正在上传的文件队列
        getUploadFiles : function() {
            var files = [];
            $("#" + t.uploadQueueId).find(".uploadifive-queue-item").each(function() {
                var file = {};
                var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
                if (isChrome) {
                    file.filePath = $(this).attr("filePath").replace(/(^\"*)|(\"*$)/g, "");
                } else {
                    file.filePath = $(this).attr("filePath");
                }
                file.fileName = $(this).attr("fileName");
                file.fileSize = $(this).attr("fileSize");
                files.push(file);
            });
            return files;
        },

        // 删除文件记录
        deleteFile : function(fileId) {
            Ajax.call({
                url : "com/deleteFile",
                p : {
                    fileId : fileId
                },
                f : function(data) {
                    if (data && data.success == true) {
                        Notify.success("删除文件成功");
                        $("#" + fileId).remove();
                        $("#files-container").bootstrapTable('refresh');
                    }
                }
            });
        },
        // 上移文件顺序
        upFile : function(fileId) {
            Ajax.call({
                url : "com/upFile",
                p : {
                    fileId : fileId
                },
                f : function(data) {
                    if (data && data.success == true) {
                        Notify.success("上移文件成功");
                        $("#files-container").bootstrapTable('refresh');
                    }
                }
            });
        },
        // 下移文件顺序
        downFile : function(fileId) {
            Ajax.call({
                url : "com/downFile",
                p : {
                    fileId : fileId
                },
                f : function(data) {
                    if (data && data.success == true) {
                        Notify.success("下移文件成功");
                        $("#files-container").bootstrapTable('refresh');
                    }
                }
            });
        },
        nameFormatter : function(value, row) {
            return '<a href="file/getFileData?filePath=' + row.filePath + '">' + row.name + '</a>';
        },
        operatorFormatter : function(value, row) {
            var html = "";
            if (row.orderNo > 0) {
                html += '<a href="javascript:ComFile.upFile(\'' + row.id + '\')"style="margin: 10px;" title="上移"><i class="glyphicon glyphicon-arrow-up"></i></a>';
            }
            html += '<a href="javascript:ComFile.downFile(\'' + row.id + '\')"style="margin: 10px;" title="下移"><i class="glyphicon glyphicon-arrow-down"></i></a>';
            html += '<a href="javascript:ComFile.deleteFile(\'' + row.id + '\')"style="margin: 10px;" title="删除"><i class="fa fa-times"></i></a>';
            return html;
        }
    }
    return t;
}();
