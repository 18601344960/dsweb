<!-- 
	@description 下载浏览器 页面
	@author 易文俊
	@since 2016-03-04
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width">
<title>党建工作管理系统-下载浏览器</title>
<link href="css/global.css" rel="stylesheet" type="text/css">
<link href="css/common.css" rel="stylesheet" type="text/css">

<style type="text/css">
.wrap {
	background: rgba(0, 0, 0, 0) url("/dsfc/images/tpri/download-bg.png") no-repeat scroll center 0px/cover;
	bottom: 0;
	left: 0;
	min-height: 600px;
	position: absolute;
	right: 0;
	top: 0;
	z-index: 0;
}
.logo-top {
    background-image: url("images/login_logo.png");
    background-position: center center;
    background-repeat: no-repeat;
    height: 100px;
    margin: 50px auto 0;
    width: 960px;
}
.register-box {
    background: rgba(255, 255, 255, 0.85) none repeat scroll 0 0;
    border-radius: 10px;
    box-shadow: 10px 10px 5px 5px;
    height: 200px;
    margin: 20px auto;
    width: 560px;
}
.reg-form {
    height: 200px;
    margin: 10px auto;
    width: 500px;
}
.reg-form .use-tip {
	color: #b40404;	
    font-size: 18px;
    margin: 30px 0 0;
}
.reg-form .use-tip span {
}
.reg-form table td {
    padding: 25px;
}
#footer {
    bottom: 10px;
    height: 60px;
    left: 0;
    position: absolute;
    right: 0;
}
</style>

</head>
<body>
	<div class="wrap">
		<div class="logo-top"></div>
		<div class="register-box">
			<div class="reg-form">
				<div class="use-tip">请使用火狐浏览器访问系统，如您尚未安装火狐浏览器浏览器，下点击下面的链接下载后安装：</div>
				<div>
					<table style="margin: auto;">
						<tbody>
							<tr>
								<td><a target="_blank" href="/dsfc/tpri/browser/Firefox_46.0.1.5966_setup.exe"><img title="火狐 Firefox" src="images/icons/firefox.png">火狐</a></td>
							</tr>
						</tbody>
					</table>
					
				</div>
				<div class="use-tip">建议在屏幕分辨率1360*768及以上环境下使用</div>
			</div>
		</div>
	</div>
</body>
</html>
