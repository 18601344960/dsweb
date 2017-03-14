<!-- 
	@description 添加工作记录预览页面
	@author yiwenjun
	@since 2015-03-28
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>支部工作手册</title>
<%@include file="../common/base.jsp"%>
<link href="css/workshare.css" rel="stylesheet" type="text/css">
<link href="css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="css/method.css">

<script type="text/javascript" src="js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="js/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="js/workrecord/conference-edit.js"></script>
<script type="text/javascript" src="js/workrecord/conference-preview.js"></script>
<script type="text/javascript" src="js/workrecord/conference-view.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
	    ArticlePreview.init('<%=request.getParameter("articleId")%>');
    });
</script>
</head>
<body>
	<div id="wrapper">
		<div id="content" class="w1200 border-style content-bg">
			<div id="main-container"></div>
		</div>
	</div>
</body>
</html>