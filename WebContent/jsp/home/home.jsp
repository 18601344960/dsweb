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
<title>驾校一考通</title>
<%@include file="../common/base.jsp"%>
<link href="/dsfc/thirdparty/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
<link href="/dsfc/css/style-metronic.css" rel="stylesheet" type="text/css" />
<link href="/dsfc/css/style.css" rel="stylesheet" type="text/css" />
<link href="/dsfc/css/style-responsive.css" rel="stylesheet" type="text/css" />
<link href="/dsfc/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="/dsfc/css/custom.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/slides-playing/css/slides.css">
<link rel="stylesheet" type="text/css" href="css/application-management.css">
<link href="/dsfc/thirdparty/pagewalkthrough/jquery.pagewalkthrough.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/home.css">

<script src="/dsfc/thirdparty/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<script src="/dsfc/thirdparty/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="/dsfc/thirdparty/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="/dsfc/thirdparty/jquery.blockui.min.js" type="text/javascript"></script>
<script src="/dsfc/thirdparty/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/dsfc/thirdparty/slides-playing/js/jquery.slides.min.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/jquery.SuperSlide.js"></script>

<script type="text/javascript" src="/dsfc/thirdparty/pagewalkthrough/jquery.pagewalkthrough.js"></script>
<script type="text/javascript" src="js/home/home.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        Home.init();
    });
</script>

</head>
<body class="application-bg">
	<%@include file="../common/header.jsp"%>
	<div class="clearfix"></div>
	<div id="show-guide-btn">
		<a>帮助引导</a>
	</div>
	<div class="container">
		<!-- 通知通告 -->
		<div class="row list-group " style="margin: 40px 0px;" id="notice-list">
		</div>
		<div class="row list-group gzf" style="margin: 40px 0px;">
			<a href="question-select" href="javascript:;" class="col-md-3 col-sm-3 col-xs-6 list-group-item article-add">
				<div class="gzf-icon">
					<i class="fa fa-th fa-5x"></i>
				</div>
				<div class="gzf-title">题库选择</div>
			</a> 
			<a href="work-share" class="col-md-3 col-sm-3 col-xs-6 list-group-item work-share">
				<div class="gzf-icon">
					<i class="fa fa-list-ol fa-5x"></i>
				</div>
				<div class="gzf-title">章节练习</div>
			</a> 
			<a href="work-brand" class="col-md-3 col-sm-3 col-xs-6 list-group-item work-brand">
				<div class="gzf-icon">
					<i class="fa fa-legal fa-5x"></i>
				</div>
				<div class="gzf-title">强化训练</div>
			</a> <a href="information" class="col-md-3 col-sm-3 col-xs-6 list-group-item information">
				<div class="gzf-icon">
					<i class="fa fa-tags fa-5x"></i>
				</div>
				<div class="gzf-title">专项练习</div>
			</a>
		</div>
		<div class="row list-group gzf" style="margin: 40px 0px;">
			<a href="question-select" href="javascript:;" class="col-md-3 col-sm-3 col-xs-6 list-group-item article-add">
				<div class="gzf-icon">
					<i class="fa fa-exclamation-triangle fa-5x"></i>
				</div>
				<div class="gzf-title">错题练习</div>
			</a> 
			<a href="work-share" class="col-md-3 col-sm-3 col-xs-6 list-group-item work-share">
				<div class="gzf-icon">
					<i class="fa fa-file-o fa-5x"></i>
				</div>
				<div class="gzf-title">模拟考试</div>
			</a> 
			<a href="work-brand" class="col-md-3 col-sm-3 col-xs-6 list-group-item work-brand">
				<div class="gzf-icon">
					<i class="fa fa-video-camera fa-5x"></i>
				</div>
				<div class="gzf-title">语音教学</div>
			</a> <a href="information" class="col-md-3 col-sm-3 col-xs-6 list-group-item information">
				<div class="gzf-icon">
					<i class="fa fa-bar-chart-o fa-5x"></i>
				</div>
				<div class="gzf-title">统计分析</div>
			</a>
		</div>
	</div>
	<%@include file="../common/footer.jsp"%>
</body>
</html>