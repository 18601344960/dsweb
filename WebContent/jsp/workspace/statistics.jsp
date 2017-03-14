<!-- 
	@description 考核统计页面 
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
<link rel="stylesheet" type="text/css" href="css/statistics.css">

<script src="/dsfc/thirdparty/echarts-2.2.7/echarts-all.js"></script>
<script type="text/javascript" src="js/statistics/statistics.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		Statistics.init();
	});
</script>

</head>
<body>
	<div id="wrapper">
		<div id="content" class="w1200 border-style content-bg" style="width: 100%">
			<div id="main-container" class="center" style="padding:20px 30px 40px 30px;width: 100%">
				<div id="detail-container" style="margin: 0px auto;">
					<!-- 查询条件 周期 -->
					<div style="width: 100%;height: 50px;">
					<!-- 查询条件 组织 -->
						<div id="ccparty-container" style="float:left;width: 36%;"></div>
						<div id="period-select-container" class="row form-horizontal" style="float: left;width: 60%;"></div>
					</div>
					<!-- 统计表格 -->
					<div id="statistics-table-container">数据加载中，请稍后。。。</div>
					<!-- 统计图表 -->
					<div class="colselect">
						<!-- 
						<btn href="javascript:void(0);" checked="false" colId="2">树立目标</btn>
						<btn href="javascript:void(0);" checked="false" colId="3">查找问题</btn>
						<btn href="javascript:void(0);" checked="false" colId="4">明确责任</btn>
						<btn href="javascript:void(0);" checked="false" colId="5">解决问题</btn>
						<btn href="javascript:void(0);" checked="false" colId="6">总结评议</btn>
						 -->
					</div>
					<div id="statistics-chart-container" style="margin-top: 50px;">数据加载中，请稍后。。。</div>
				</div>
			</div>
			<div id="menuContent" class="menuContent" style="display: none; position: absolute;">
				<ul id="ccparty-tree" class="ztree" style="margin-top: 0; width: 300px;height: 300px;"></ul>
			</div>
		</div>
	</div>
</body>
</html>