<!-- 
	@description 题库选择页面 
	@author 赵子靖
	@since 2017-03-11
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>驾驶员一考通</title>
<%@include file="../common/base.jsp"%>
<link rel="stylesheet" type="text/css" href="css/question.css">

<script type="text/javascript" src="js/question/question-select/question-select.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        QuestionSelect.init('#main-container-detail');
    });
</script>

</head>
<body>
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="w1200 border-style content-bg">
			<div class="location">
				您的位置：<a href="home" title="首页"><i class="fa fa-home"></i>首页</a>&nbsp;>&nbsp;题库选择
			</div>
			<div id="main-container">
				<div id="main-container-detail" class="row">
				</div>
			</div>
		</div>

		<%@include file="../common/footer.jsp"%>
	</div>
</body>
</html>