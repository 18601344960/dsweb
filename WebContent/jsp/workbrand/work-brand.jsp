<!-- 
	@description 工作品牌页面 
	@author yiwenjun
	@since 2015-07-20
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>支部工作手册</title>
<%@include file="../common/base.jsp"%>
<link rel="stylesheet" type="text/css" href="css/workshare.css">

<script type="text/javascript" src="js/workbrand/work-brand.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        WorkBrand.init('#workshare-right');
    });
</script>

</head>
<body>
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="w1200 border-style content-bg">
			<div class="location">
				您的位置：<a href="home"><i class="fa fa-home"></i>首页</a> >工作品牌
			</div>
			<div id="main-container">
				<div id="main-container-detail" class="row">
					<!-- 左侧搜索 -->
					<div id="workshare-left" class="workshare-search"></div>
					<!-- 右侧内容 -->
					<div id="workshare-right" class="workshare-article"></div>
				</div>
			</div>
		</div>

		<%@include file="../common/footer.jsp"%>
	</div>
</body>
</html>