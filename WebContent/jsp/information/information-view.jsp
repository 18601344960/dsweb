<!-- 
	@description 首页工作必备详情页面 
	@author yiwenjun
	@since 2015-03-28
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>支部工作手册</title>
<%@include file="../common/base.jsp"%>
<link href="css/info.css" rel="stylesheet" type="text/css">
<link href="css/detail.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/information/information-view.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
	    InformationView.init('<%=request.getParameter("id")%>');
	});
</script>
</head>
<body>
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="w1200 border-style content-bg">
			<div class="location">当前位置：<a href="home">首页</a> > <a href="information">工作必备</a> > 内容</div>
			<!-- 内容详情 -->
			<div id="main-container"></div>
		</div>
		<%@include file="../common/footer.jsp"%>
	</div>
</body>
</html>