<!-- 
	@description 管理中心页面
	@author yiwenjun
	@since 2015-09-01
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>支部工作手册</title>
<%@include file="../common/base.jsp"%>
<link href="/dsfc/thirdparty/jquery-treetable-master/stylesheets/jquery.treetable.css" rel="stylesheet" type="text/css"/>
<link href="/dsfc/thirdparty/jquery-treetable-master/stylesheets/jquery.treetable.theme.default.css" rel="stylesheet" type="text/css"/>
<link href="css/organization.css" rel="stylesheet" type="text/css" />
<link href="css/party-member.css" rel="stylesheet" type="text/css" />
<link href="css/manager.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/dsfc/thirdparty/jquery-treetable-master/javascripts/src/jquery.treetable.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/ajaxFileUpload/ajaxfileupload.js"></script>
<script type="text/javascript" src="js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="js/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="js/manager/manager-home.js"></script>
<script type="text/javascript" src="js/manager/manager-environment.js"></script>
<script type="text/javascript" src="js/manager/ccparty/ccparty-manager.js"></script>
<script type="text/javascript" src="js/manager/ccparty/ccparty-leader.js"></script>
<script type="text/javascript" src="js/manager/ccparty/ccparty-leader-backup.js"></script>
<script type="text/javascript" src="js/manager/ccparty/ccparty-navigation.js"></script>
<script type="text/javascript" src="js/manager/ccparty/ccparty-group.js"></script>
<script type="text/javascript" src="js/manager/organization/administration-manager.js"></script>
<script type="text/javascript" src="js/manager/organization/department.js"></script>
<script type="text/javascript" src="js/manager/organization/administration-navigation.js"></script>
<script type="text/javascript" src="js/manager/user/manager-user-navigation.js"></script>
<script type="text/javascript" src="js/manager/user/manager-user.js"></script>
<script type="text/javascript" src="js/manager/role/manager-role.js"></script>
<script type="text/javascript" src="js/manager/privilege/manager-privilege.js"></script>
<script type="text/javascript" src="js/manager/manager-system-user.js"></script>
<script type="text/javascript" src="js/manager/manager-party-worker.js"></script>
<script type="text/javascript" src="js/manager/manager-import-user.js"></script>
<script type="text/javascript" src="js/workspace/ccparty-card.js"></script>
<script type="text/javascript" src="js/workspace/my-ccparty-card.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		ManagerHome.init('<%=request.getParameter("navigation")%>');
	});
</script>
</head>
<body>
	<div id="wrapper">
		<%@include file="../common/header.jsp"%>
		<div id="content" class="screen-width border-style content-bg">
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
	<div id="dialogs"></div>
</body>
</html>
