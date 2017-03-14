<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- 
	@description 工作品牌详情页面
	@author yiwenjun
	@since 2015-07-20
-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>支部工作手册</title>
<%@include file="../common/base.jsp"%>
<link href="css/detail.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="js/workbrand/work-brand-view.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		WorkBrandView.init('<%=request.getParameter("id")%>');
	});
</script>
</head>
<body>
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="w1200 border-style content-bg">
			<div id="location" class="location">
				当前位置：<a href="home">首页</a> > <a href="work-brand">工作品牌</a>
			</div>
			<div id="main-container">
				<div id="work-brand-container" style="padding:30px;margin:10px 50px"></div>
			</div>
		</div>
		<%@include file="../common/footer.jsp"%>
	</div>
</body>
</html>