<!-- 
	@description 文章编辑页面
	@author yiwenjun
	@since 2015-05-12
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>支部工作手册</title>
<script type="text/javascript">
	//window.UEDITOR_HOME_URL="/";
</script>
<%@include file="../common/base.jsp"%>
<link rel="stylesheet" type="text/css" href="css/method.css">

<script type="text/javascript" src="js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="js/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="js/workrecord/conference-add.js"></script>
<script type="text/javascript" src="js/workrecord/conference-edit.js"></script>
<script type="text/javascript" src="js/workrecord/conference-participants.js"></script>
<script type="text/javascript" src="js/workrecord/conference-orgnizers.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
	    ConferenceEdit.init('<%=request.getParameter("conferenceId")%>');
	});
</script>
</head>
<body>
	<div id="wrapper">
		<div id="content" class="border-style content-bg" style="padding:20px 50px;">
		</div>
		<div id="menuContent" class="menuContent" style="display: none; position: absolute;z-index: 10000;">
			<ul id="ccparty-tree" class="ztree" style="margin-top: 0; width: 300px;height: 300px;"></ul>
		</div>
	</div>
</body>
</html>