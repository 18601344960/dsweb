<!-- 
	@description 工作必备页面 
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
<%@include file="../common/base.jsp" %>
<link rel="stylesheet" type="text/css" href="css/info.css">
<style type="text/css">
.fixed-table-container .fixed-table-body table{
	border-bottom:none;
}
.fixed-table-container tbody td{
	border-left:none;
}
table a{
	color:#333;
	font-size:18px;
}
table a:HOVER{
	color:#f00;
	font-size:18px;
}
.fixed-table-container thead th{
	border-left:none;
}
.table > thead > tr > th, 
.table > tbody > tr > th, 
.table > tfoot > tr > th, 
.table > thead > tr > td, 
.table > tbody > tr > td, 
.table > tfoot > tr > td{
 	border-top:none;
 }
</style>

<script type="text/javascript" src="js/information/information.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
	    Information.init();
	});
</script>

</head>
<body>
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="w1200 border-style content-bg" style="height:auto;">
			<div class="location">当前位置：<a href="home">首页</a> > <a href="info">工作必备</a></div>
			<div id="main-container"  class="row" style="min-Height:400px;margin:20px;padding-bottom:30px;"></div>
		</div>
		<%@include file="../common/footer.jsp"%>
	</div>
</body>
</html>