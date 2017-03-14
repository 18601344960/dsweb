/**
 * @description 导航
 * @author yiwenjun
 * @since 2015-03-28
 */
var Navigation = function() {
	var t = {
		initNavigation:function(initTab){
			var data={
				initTab:initTab
			}
			$("#common-navigation").html(nunjucks.render(Global.appName+'/tpl/common/navigation.html',data));
			t.navScroll($("#navwraper"), $("#nav"), $("#navmenu"), "dd", "dt", 250, "curr");
			t.linkScroll($("a.linkscroll"), 100, 5);
			t.srchEff($("#srchText"), 176, 140, 250);
		},
		navScroll : function(navwrap, Dom, Menu, list, curr, speed, defClass) {
			var $list = Dom.find(list), listLen = $list.length, $menuList = Menu.find("dl"), menuLen = $menuList.length;
			i = 0, arrListInfo = [], bool = true, currIdx = 0;
			for (i = 0; i < listLen; i++) {
				var othis = $list.eq(i), sPath = othis.find("a").attr("href"), sText = othis.text(), nPosX = othis.position().left, z;
				arrListInfo.push([sText, nPosX, sPath]);
				if (othis.hasClass(defClass) && bool) {
					Dom.append("<dt style=\"display:none;left:" + nPosX + "px;\"><a href=\"" + sPath + "\"><span>" + sText + "</span><em></em></a></dt>").find(curr).fadeIn(200);
					bool = false;
					currIdx = i;
				};
				for (z = 0; z < menuLen; z++) {
					var omenu = $menuList.eq(z);
					if (Number(omenu.attr("name")) == i) {
						omenu.css("left", nPosX).find("dd:last a").css("background", "none");
					};
				};
			};
			setTimeout(function() {
						$list.bind("mouseover", function() {
									var index = $(this).index();
									fnAnimate(Dom, arrListInfo, index, $menuList, true);
									return false;
								});
						navwrap.bind("mouseleave", function() {
									$menuList.fadeOut(speed);
									fnAnimate(Dom, arrListInfo, currIdx, $menuList, false);
									return false;
								});
					}, speed);
			function fnMenuShow(d, y) {
				if (y != -1) {
					// 个性化，只用了第三个菜单
					if (y == 3) {
						d.eq(y).fadeIn(speed).siblings().fadeOut(speed);
					}

				};
				return false;
			};
			function fnAnimate(d, a, x, m, b) {
				d.find(curr).stop().animate({
							"left" : a[x][1]
						}, speed, function() {
							$(this).find("a").attr("href", a[x][2]).find("span").text(a[x][0]);
							// .fadeIn(100);
							if (b) {
								m.fadeOut(speed);
								fnMenuShow(m, x);
							};
						}).find("span").hide();
				return false;
			};
			return false;
		},
		// 二级导航链接效果
		linkScroll : function(Dom, speed, x) {
			Dom.hover(function() {
						a($(this), x, speed);
					}, function() {
						a($(this), 0, speed);
					});
			function a($this, v, s, a) {
				$this.stop().animate({
							"left" : v
						}, s);
			};
		},
		srchEff:function(Dom, maxWidth, minWdith, speed) {
				var value = Dom.val();
				Dom.focusin(function(e) {
							var _self = $(e.target);
							_self.animate({
										"width" : maxWidth
									}, speed).val("");
							return false;
						}).focusout(function(e) {
							var _self = $(e.target);
							_self.animate({
										"width" : minWdith
									}, speed);
							if (_self.val() === "") {
								_self.val(value);
							};
							return false;
						});
				return false;
			}
	}
	return t;
}();