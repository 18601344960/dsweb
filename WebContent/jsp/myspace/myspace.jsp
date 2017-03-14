<%@page import="org.tpri.sc.util.RequestUtils"%>
<!-- 
	@description 个人中心
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
<link rel="stylesheet" type="text/css" href="/dsfc/tpri/pagination/tpri-pagination.css">
<link rel="stylesheet" type="text/css" href="css/myspace.css">
<link rel="stylesheet" type="text/css" href="css/myInfo.css">
<link rel="stylesheet" type="text/css" href="css/partymember-electronic-card.css" />

<script type="text/javascript" src="/dsfc/thirdparty/ajaxFileUpload/ajaxfileupload.js"></script>
<script type="text/javascript" src="js/myspace/my-space.js"></script>
<script type="text/javascript" src="js/myspace/my-info.js"></script>
<script type="text/javascript" src="js/myspace/my-password-update.js"></script>
<script type="text/javascript" src="js/myspace/my-comment.js"></script>
<script type="text/javascript" src="js/myspace/my-article.js"></script>
<script type="text/javascript" src="js/myspace/my-praise.js"></script>
<script type="text/javascript" src="js/myspace/my-electronic-card.js"></script>
<script type="text/javascript" src="js/myspace/partymember-electronic-card.js"></script>
<script type="text/javascript" src="js/myspace/my-assessment.js"></script>
<script type="text/javascript" src="js/workspace/my-ccparty-card.js"></script>
<script type="text/javascript" src="js/workspace/ccparty-card.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		MySpace.init('<%=RequestUtils.getParameter(request,"navigation")%>');
	});
</script>

</head>
<body  style="overflow-y:scroll">
	<div id="wrapper" >
		<%@include file="../common/header.jsp"%>
		<div id="content" class="screen-width border-style content-bg">
			<div id="main-container">
				<div id="main-container-detail" class="row" style="padding: 2px 0;" >
					<div id="left-container" class="mod-setting"></div>
					<div id="right-container">
						<div class="main-title"></div>
						<div class="main-detail"></div>
					</div>
				</div>
			</div>
		</div>
		<%@include file="../common/footer.jsp"%>
	</div>
</body>
</html>