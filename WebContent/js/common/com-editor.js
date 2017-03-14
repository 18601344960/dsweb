/**
 * @description 编辑器操作公用
 * @author yiwenjun
 * @since 2015-08-09
 */
var ComEditor = function() {
	var t = {
		editorId : 'editor-container',
		//初始化编辑器
		initEditor : function() {
			var ue = UE.getEditor(t.editorId, {
						toolbars : [['fullscreen', 'undo', 'redo', '|', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'removeformat', 'formatmatch', 'autotypeset', 'pasteplain', 'selectall', 'cleardoc', '|', 'rowspacingtop', 'rowspacingbottom', 'lineheight', 'indent', '|', 'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', 'horizontal', '|', 'preview']]
					});
		},
		//获得编辑器内容
		getContent: function(){
			return UE.getEditor(ComEditor.editorId).getContent();
		},
		//得到编辑器的纯文本内容，但会保留段落格式 
		getPlainTxt: function(){
			return UE.getEditor(ComEditor.editorId).getPlainTxt();
		},
		//获取编辑器中的纯文本内容,没有段落格式 
		getContentTxt: function(){
			return UE.getEditor(ComEditor.editorId).getContentTxt();
		},
		//销毁编辑器
		destroy:function(){
			UE.getEditor(ComEditor.editorId).destroy();
		}
	}
	return t;
}();
