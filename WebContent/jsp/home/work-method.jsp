<!-- 
	@description 机关党支部工作法介绍页面 
	@author yiwenjun
	@since 2015-03-28
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>驾校一考通</title>
<%@include file="../common/base.jsp"%>
<style type="text/css">
.method-title {
	font-size: 35px;
	padding: 10px;
	font-family: '宋体';
	color: #333;
	text-align: center;
}

.method-subtitle {
	font-size: 18px;
	font-family: '宋体';
	color: #333;
	text-align: center;
}

#quotation {
	margin: 30px;
	padding: 0 10px;
}

#quotation li {
	color: #fff;
	line-height: 28px;
	padding-bottom: 30px;
}

#quotation li p {
	color: #000;
	font-family: "宋体";
	font-size: 16px;
	line-height: 28px;
	text-indent: 2em;
}

#quotation li .step {
	color: #000;
	font-family: "宋体";
	font-size: 20px;
	font-weight: bold;
	line-height: 36px;
	text-indent: 2em;
}
</style>
<script type="text/javascript" src="js/home/work-method.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        WorkMethod.init();
    });
</script>

</head>
<body>
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="w1200 border-style content-bg">
			<div class="location">
				您的位置：<a href="home"><i class="fa fa-home"></i>首页</a> > 机关支部工作法
			</div>
			<div id="main-container" style="margin: 0 20px; padding: 0 30px 30px 30px;">
				<div class="row">
					<div id="work-method-container" class="col-md-12 method-wrap"></div>
				</div>
			</div>
		</div>
		<%@include file="../common/footer.jsp"%>
	</div>
</body>
</html>