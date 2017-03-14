<!-- 
	@description 机关党支部工作法工作步骤
	@author yiwenjun
	@since 2016-06-17
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>驾校一考通</title>
<%@include file="../common/base.jsp"%>
<link rel="stylesheet" type="text/css" href="/scweb/css/home.css" />

<script type="text/javascript" src="js/home/work-step.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        WorkStep.init();
    });
</script>
</head>
<body>
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="w1200 border-style content-bg">
			<div class="location">
				您的位置：<a href="home"><i class="fa fa-home"></i>首页</a> > 机关支部工作法步骤
			</div>
			<div id="main-container" style="margin: 0 70px; padding: 0 30px 30px 30px; height: 600px;"></div>
			<div id="Split line" style="width: auto; height: auto; margin-top: 30px;"></div>
		</div>
		<%@include file="../common/footer.jsp"%>
	</div>

</body>
</html>