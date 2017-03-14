<!-- 
	@description 应用首页 
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
<%@include file="common/base.jsp" %>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/slides-playing/css/slides.css">
<link rel="stylesheet" type="text/css" href="css/report.css">

<script type="text/javascript" src="/dsfc/thirdparty/slides-playing/js/jquery.slides.min.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/jquery.SuperSlide.js"></script>
<script type="text/javascript" src="js/workshare/work-share.js"></script>
<script type="text/javascript">
	function exportReport(id, title, format) {
	    var html ="reports/mgr/"+id+"?year=2016&format="+format;
	    html = html + "&date=" + new Date();
	    window.open(html);
	}
</script>
</head>
<body class="application-bg">
	<%@include file="common/header.jsp"%>
    <div class="clearfix"></div>
    <div id="content" class="w1200 border-style content-bg">
		<div id="main-container">
			<div id="main-container-detail" class="row">
				<!-- 左侧搜索 -->
				<div id="report-left" class="report-search">
					<table>
					    <tr>
						    <td><label>党支部工作手册</label></td>
						    <td><input type="button" id="pdf"  class="pdf" onclick="exportReport('dzbgzsc', '党支部工作手册', 'pdf')"  style="height: 25px;width:25px;" /></td>
						</tr>
						<tr>
						    <td><label>中国共产党党员手册</label></td>
						    <td><input type="button" id="pdf"  class="pdf" onclick="exportReport('dysc', '中国共产党党员手册', 'pdf')"  style="height: 25px;width:25px;" /></td>
						</tr>
					</table>
					
					
				</div>
				<!-- 右侧内容 -->
				<div id="report-right"  class="report-article">
				</div>
			</div>
		</div>
	</div>
</body>
</html>