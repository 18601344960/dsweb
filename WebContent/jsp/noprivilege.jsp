<!-- 
	@description 无权限访问提示页面
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
<%@include file="common/base.jsp"%>
</head>
<body>
	<div id="wrapper">
		<%@include file="common/header.jsp"%>
		<div id="content" class="w1200 border-style content-bg">
			<div id="main-container">
				<div class="content-detail">
					<div class="noprivilege-tip">对不起，你没有权限访问该页面</div>
					<div class="noprivilege-btns"><a href="home" class="btn btn-primary btn-lg noprivilege-btn">返回主页</a><a href="login" class="btn btn-default btn-lg noprivilege-btn">其他用户登录</a></div>
				</div>
			</div>
		</div>
		<%@include file="common/footer.jsp"%>
	</div>
</body>
</html>