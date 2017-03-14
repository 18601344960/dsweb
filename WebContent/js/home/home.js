/**
 * @description 首页
 * @author yiwenjun
 * @since 2015-03-31
 */
var Home = function() {
    var t = {
        init : function() {
            t.initView();
            t.initEvent();
        },
        initView : function() {
            // ajax加载首页滚动通知
//            $("#notice-container").html(nunjucks.render(Global.appName + '/tpl/home/announcement.html'));
//            $("#walkthrough-content").html(nunjucks.render(Global.appName + '/tpl/home/guide.html'));
//            $("#work-navigation").html(nunjucks.render(Global.appName + '/tpl/home/work-navigation.html', Global));
            Ajax.call({
                url : "com/getFirstAnnouncement",
                p : {
                    ccpartyId : Global.ccpartyId
                },
                f : function(data) {
                    if (data) {
                        $("#notice-list").html(nunjucks.render(Global.appName + '/tpl/home/announcement.html', data));
                    } else {
                        Notify.error("获取公告失败");
                    }
                }
            });
            t.initGuide();
        },
        initAnnouncement : function() {
            var notices = $("#notice-list");
            if (notices.size() == 0) {
                return false;
            }
            var noticeRows = notices.find("ul");
            t.scroll(noticeRows);
        },
        initEvent : function() {
            $(".app-open").hide();
            $(".app-1").hover(function() {
                $(".app-open1").css("display", "block");
            }, function() {
                $(".app-open1").css("display", "none");
            })
            $(".app-2").hover(function() {
                $(".app-open2").css("display", "block");
            }, function() {
                $(".app-open2").css("display", "none");
            })
            $(".app-3").hover(function() {
                $(".app-open3").css("display", "block");
            }, function() {
                $(".app-open3").css("display", "none");
            })
            $(".app-4").hover(function() {
                $(".app-open4").css("display", "block");
            }, function() {
                $(".app-open4").css("display", "none");
            })
            $("#class-navigagtion-container button").button().click(function(event) {
                event.preventDefault();
            });
            $(document).on("click", "#show-guide-btn a", function() {
                t.showGuide();
            });

        },
        initGuide : function() {
            // 设置参数
            $('body').pagewalkthrough({
                name : 'introduction',
                steps : [ {
                    popup : { // 定义弹出提示引导层
                        content : '#walkthrough-1',
                        width : '500px',
                        type : 'modal'
                    }
                }, {
                    wrapper : '.article-add',// 当前引导对应的元素位置
                    popup : {
                        content : '#walkthrough-2',// 关联的内容元素
                        type : 'tooltip',// 弹出方式（tooltip和modal以及nohighlight）
                        position : 'top'// 弹出层位置（top,left,
                    // right or bottom）
                    }
                }, {
                    wrapper : '.work-share',
                    popup : {
                        content : '#walkthrough-3',
                        type : 'tooltip',
                        position : 'top'
                    }
                }, {
                    wrapper : '.work-brand',
                    popup : {
                        content : '#walkthrough-5',
                        type : 'tooltip',
                        position : 'top'
                    }
                }, {
                    wrapper : '.information',
                    popup : {
                        content : '#walkthrough-4',
                        type : 'tooltip',
                        position : 'top'
                    }
                }, {
                    wrapper : '.user-center',
                    popup : {
                        content : '#walkthrough-6',
                        type : 'tooltip',
                        position : 'bottom'
                    }
                }, {
                    wrapper : '.work-center',
                    popup : {
                        content : '#walkthrough-7',
                        type : 'tooltip',
                        position : 'bottom'
                    }
                }
                // 自己重新写的

                ]
            });
            var isShow = Global.getCookie("_walkthrough-introduction");
            if (isShow != "0") {
                t.showGuide();
            }
        },
        showGuide : function() {
            $('body').pagewalkthrough('show');
        },
        scroll : function(uls) {
            var li_h = uls.eq(0).find("li").outerHeight();
            this.copy = function() {
                uls.each(function() {
                    var lis = $(this).find("li");
                    $(this).append(lis.clone());
                    $(this).attr("li_s", lis.size() * 2);
                    $(this).attr("cur_li", lis.size() * 2 - 1);
                });
            }
            this.copy();
            // 移动到最下边的li
            this.toLast = function() {
                uls.each(function() {
                    $(this).scrollTop(10000);
                });
            }
            this.toLast();

            var timer = null;// 滚动的超时时间
            var i = 0;
            // 开始滚动ul数组
            function start(i) {
                var cur_ul = uls.eq(i);
                if (cur_ul) {
                    cur_ul.animate({
                        scrollTop : cur_ul.scrollTop() - li_h
                    }, function() {
                        var cur_li = parseInt(cur_ul.attr("cur_li"));
                        var li_s = parseInt(cur_ul.attr("li_s"));
                        cur_li--;
                        if ((li_s / 2 - 1) == cur_li) {
                            cur_ul.attr("cur_li", li_s - 1);
                            $(this).scrollTop(10000);
                        } else {
                            cur_ul.attr("cur_li", cur_li);
                        }
                        start(++i);
                    });
                }
                ;
            }
            var timer;
            $(uls).hover(function() {
                clearInterval(timer);
            }, function() {
                timer = setInterval(function() {
                    start(i);
                }, 3000);
            }).trigger("mouseleave");
        },
        closeDiv : function() {
            $("#indexLeftAdv").hide();
        },
        openAddConferece : function() {
            var iHeight = window.screen.availHeight;
            var iWidth = window.screen.availWidth;
            var windowObject=Global.openWindow('article-add', 'newWindow', iWidth, iHeight);
        }
    }
    return t;
}();