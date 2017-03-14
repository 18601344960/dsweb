<%@page import="org.tpri.sc.controller.PageSupport"%>
<!-- 
	@description 使用jquery设计页面需要的静态文件
	@author 易文俊
	@since 2015-04-03
-->
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/font-awesome/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/bootstrap-3.3.5/css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/pnotify/jquery.pnotify.default.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/pnotify/jquery.pnotify.default.icons.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/uniform/css/uniform.default.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/ztree/css/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/bootstrap-table/bootstrap-table.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/manhuatoTop/manhuatoTop-tpri.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/uploadifive/uploadifive.css" />
<link rel="stylesheet" type="text/css" href="/dsfc/tpri/pagination/tpri-pagination.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/css/style-metronic.css"/>
<link rel="stylesheet" type="text/css" href="css/zbsc.css"/>
<link rel="stylesheet" type="text/css" href="css/common.css"/>
<link rel="stylesheet" type="text/css" href="css/index.css">

<script type="text/javascript" src="/dsfc/thirdparty/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/bootstrap-table/bootstrap-table.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/nunjucks/nunjucks.min.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/json2.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/ztree/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/manhuatoTop/manhuatoTop.1.0.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/shCircleLoader/js/jquery.shCircleLoader.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/bootbox/bootbox.min.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/pnotify/jquery.pnotify.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/dsfc/tpri/pagination/tpri-pagination.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/uploadifive/jquery.uploadifive.min.js"></script>
<script type="text/javascript" src="/dsfc/tpri/util/ajax.js"></script>
<script type="text/javascript" src="/dsfc/tpri/limit/jquery.limit.js"></script>
<script type="text/javascript" src="/dsfc/tpri/pnotify/Notify.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/common/checkInputUtils.js"></script>
<script type="text/javascript" src="js/common/com-file.js"></script>
<script type="text/javascript" src="js/common/com-editor.js"></script>
<script type="text/javascript" src="js/common/com-tree.js"></script>
<script type="text/javascript" src="js/common/utils.js"></script>
<script type="text/javascript" src="js/common/user-utils.js"></script>
<script type="text/javascript" src="js/common/select-tree.js"></script>

<script type="text/javascript" src="js/common/constants.js"></script>

<script type="text/javascript">
	/* var browser=Global.myBrowser();
	if (browser != "Firefox" && browser != "Chrome") {
	    window.location.href = "download-browser";
	} */
	$.extend(Global,<%=PageSupport.buildAttributes(request,response)%>);
	$(document).ready(function(){
		bootbox.setLocale("zh_CN");
	}); 
</script>
