<%@page import="org.tpri.sc.controller.PageSupport"%>
<!-- 
	@description 登录页 
	@author yiwenjun
	@since 2015-04-23
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>驾校一考通</title>
<link rel="stylesheet" type="text/css" href="/dsfc/thirdparty/bootstrap-3.3.5/css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="/dsfc/css/style-metronic.css"/>
<link rel="stylesheet" type="text/css" href="css/zbsc.css"/>
<link rel="stylesheet" type="text/css" href="css/common.css"/>
<link rel="stylesheet" type="text/css" href="css/login.css" />

<script type="text/javascript" src="/dsfc/thirdparty/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/dsfc/thirdparty/nunjucks/nunjucks.min.js"></script>
<script type="text/javascript" src="/dsfc/tpri/util/ajax.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/common/login.js"></script>
<script type="text/javascript">
	/* var browser=Global.myBrowser();
	if (browser != "Firefox" && browser != "Chrome") {
	    window.location.href = "download-browser";
	} */
	$.extend(Global,<%=PageSupport.buildAttributes(request,response)%>);
</script>
<script type="text/javascript">
    $(document).ready(function() {
        Login.init();
    });
</script>
</head>
<body onkeydown="Login.bindEnter(event)">
	<div class="wrap">
		<div class="logo-container">
			<div class="logo-to" style="float: left;font-size: 50px;margin:14px;color: #555">驾驶员一考通</div>
		</div>
		<div class="login-wrap">
			<div class="banner-show">
				<div class="cell">
					<div class="con"></div>
				</div>
			</div>
			<div class="register-box">
				<div class="reg-slogan">用户登录</div>
				<div class="reg-form" id="js-form-mobile">
					<br>
					<div class="cell">
						<div class="dropdown">
							<i class="glyphicon glyphicon-user cell-icon"></i> 
							<input type="text" name="mobile" id="userId" class="text" maxlength="20" value="登录账号" style="color: #999"/>
							<i class="glyphicon glyphicon-chevron-down"  style="float:right;padding:13px;" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></i> 
						  	<ul id="user-login-no-list" class="dropdown-menu" role="menu" aria-labelledby="dLabel" style="margin-left:40px;width:240px;">
						  	</ul>
						</div>
					</div>
					<div class="cell">
						<i class="glyphicon glyphicon-lock cell-icon"></i> <input type="text" name="password-prompt" id="password-prompt" class="text" value="密码" style="color: #999;width: 138px; " /> <input type="password"
							name="password" id="password" class="text" maxlength="20" style="display: none; width: 138px;" /><div style="float: right;width: 100px;padding:10px;"><input type="checkbox" id="remember-password" style="position:static;width: 20px;margin-top:0;"/><label for="remember-password" style="position:static;font-size:14px;color:#333;padding:0;line-height:20px;cursor: default;">记住密码</label></div>
					</div>
					<div class="cell">
						<i class="glyphicon glyphicon-credit-card cell-icon"></i> <input type="text" name="kaptcha" id="kaptcha" class="text" maxlength="4" value="验证码" style="color: #999; width: 138px;"
							onkeyup="Login.validateCode(this.value)" />
						<div style="padding: 0 0 0 10px; float: right;">
							<img src="kaptcha/getKaptchaImage" id="kaptchaImage" />
						</div>
					</div>
					<div>
						<div id="login-result-tip" style="color: red;"></div>
					</div>
					<div class="bottom">
						<a id="login-btn" href="javascript:;" class="button btn-green"> 立即登录</a>
					</div>
					<div class="use-tip">
						<span>建议在屏幕分辨率1360*768及以上环境下使用</span>
						<table style="margin: 0 auto;">
							<tbody>
								<tr>
									<td style="font: 14px '微软雅黑', arial; color: #B40404;">点击下载以下浏览器访问：</td>
									<td><a target="_blank" href="/dsfc/tpri/browser/Firefox_46.0.1.5966_setup.exe"><img title="火狐 Firefox" src="images/icons/firefox.png">火狐</a></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="common/footer.jsp"%>
</body>
</html>
