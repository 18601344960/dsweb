<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>支部工作手册</title>
<%@include file="../common/base.jsp"%>
<link href="css/workshare.css" rel="stylesheet" type="text/css">
<link href="css/detail.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="js/workrecord/conference-view.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
	    ConferenceView.init('<%=request.getParameter("articleId")%>','<%=request.getParameter("viewSource")%>');
	});
</script>
</head>
<body>
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="w1200 border-style content-bg">
			<div id="main-container"></div>
			<div id="comment-container">
	    		<div class="items">
					<div class="comment-tips">评论：</div>
					<div id="comment-list"></div>
		    	</div>
		    	<div class="comment-text-container row">
				    <textarea id="comment-content"></textarea>
			    </div>
			    <div class="comment-btn-container row">
				    <input id="submit-btn" type="button" class="btn btn-primary btn-lg" value="发表评论" />
			    </div>
		    </div>
		</div>
		<%@include file="../common/footer.jsp"%>
	</div>
</body>
</html>