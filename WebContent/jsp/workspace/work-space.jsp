<%@page import="org.tpri.sc.util.RequestUtils"%>
<!-- 
	@description 工作平台
	@author yiwenjun
	@since 2016-06-29
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
<link rel="stylesheet" type="text/css" href="css/work-space.css"/>
<link rel="stylesheet" type="text/css" href="css/party-member.css" />
<link rel="stylesheet" type="text/css" href="css/statistics.css">
<link rel="stylesheet" type="text/css" href="css/party-fee.css">
<link rel="stylesheet" type="text/css" href="css/ccparty-card.css">
<link rel="stylesheet" type="text/css" href="css/partymember-electronic-card.css">
<link rel="stylesheet" type="text/css" href="css/assessment-topic.css">

<script type="text/javascript" src="/dsfc/thirdparty/echarts-2.2.7/echarts-all.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/ajaxFileUpload/ajaxfileupload.js"></script>
<script type="text/javascript" src="js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="js/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="js/workspace/work-space.js"></script>
<script type="text/javascript" src="js/workspace/ccparty-article.js"></script>
<script type="text/javascript" src="js/workspace/announcement.js"></script>
<script type="text/javascript" src="js/workspace/work-brand.js"></script>
<script type="text/javascript" src="js/workspace/work-requirement.js"></script>
<script type="text/javascript" src="js/workspace/work-rule.js"></script>
<script type="text/javascript" src="js/workspace/partymember/party-member.js"></script>
<script type="text/javascript" src="js/workspace/partymember/party-member-baseInfo.js"></script>
<script type="text/javascript" src="js/workspace/partymember/party-member-info.js"></script>
<script type="text/javascript" src="js/workspace/partymember/party-member-card.js"></script>
<script type="text/javascript" src="js/workspace/partymember/ccparty-leader.js"></script>
<script type="text/javascript" src="js/workspace/partymember/party-group-member.js"></script>
<script type="text/javascript" src="js/workspace/partymember/party-member-export.js"></script>
<script type="text/javascript" src="js/myspace/my-electronic-card.js"></script>
<script type="text/javascript" src="js/workspace/election/election.js"></script>
<script type="text/javascript" src="js/workspace/election/election-monitor.js"></script>
<script type="text/javascript" src="js/workspace/statistics.js"></script>
<script type="text/javascript" src="js/workspace/report-export.js"></script>
<script type="text/javascript" src="js/workspace/report-import.js"></script>
<script type="text/javascript" src="js/workspace/report-online.js"></script>
<script type="text/javascript" src="js/workspace/party-fee/party-fee.js"></script>
<script type="text/javascript" src="js/common/checkInputUtils.js"></script>
<script type="text/javascript" src="js/myspace/partymember-electronic-card.js"></script>
<script type="text/javascript" src="js/workspace/work-card/work-card.js"></script>
<script type="text/javascript" src="js/workspace/my-ccparty-card.js"></script>
<script type="text/javascript" src="js/workspace/ccparty-card.js"></script>
<script type="text/javascript" src="js/workspace/development/development-procedure.js"></script>
<script type="text/javascript" src="js/workspace/development/development-handle.js"></script>
<script type="text/javascript" src="js/workspace/development/development-step-handle.js"></script>
<script type="text/javascript" src="js/workspace/development/development-procedure-common-content.js"></script>
<script type="text/javascript" src="js/workspace/assessment/assessment.js"></script>
<script type="text/javascript" src="js/workspace/assessment/assessment-topic.js"></script>
<script type="text/javascript" src="js/workspace/assessment/assessment-target.js"></script>
<script type="text/javascript" src="js/workspace/assessment/assessment-user.js"></script>
<script type="text/javascript" src="js/workspace/assessment/assessment-result-statistical.js"></script>
<script type="text/javascript" src="js/workspace/conference-category.js"></script>
<script type="text/javascript" src="js/workspace/partymembers-card/partymembers-card.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
	    WorkSpace.init('<%=RequestUtils.getParameter(request,"navigation")%>');
	});
</script>

</head>
<body style="overflow: auto;">
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="screen-width border-style content-bg" >
			<div id="main-container">
				<div id="main-container-detail" class="row" style="padding: 2px 0;">
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