/**
 * @description 全局公共js
 * @author yiwenjun
 * @since 2015-03-31
 */
var Global = function() {
    var t = {
        appName : 'dsweb',
        ccpartyGroup : 'Z000101', //党组代码
        ccpartyCommitte : 'Z000102', //党委代码
        ccpartyGeneral : 'Z000103', //总支代码
        ccpartyBranch : 'Z000104', //支部代码
        commonPartyMemberId : 'ROLE_1004', // 普通党员权限标示
        init : function() {
            //t.initToTop();
            t.initEvent();
        },
        initEvent : function(c) {
            t.initZtreeHover();
            t.initTodoClick();
        },
        initToTop : function() {
            $(window).manhuatoTop({
                // 设置滚动高度时显示
                showHeight : 100,
                // 返回顶部的速度以毫秒为单位
                speed : 500

            });
        },
        // 初始化ztree的悬停事件
        initZtreeHover : function() {
            $(document).on("mouseover mouseout", ".ztree li a", function(event) {
                if (event.type == "mouseover") {
                    $(this).find("span").each(function() {
                        $(this).addClass("span-hover");
                    });
                } else if (event.type == "mouseout") {
                    $(this).find("span").each(function() {
                        $(this).removeClass("span-hover");
                    });
                }
            });
        },
        // 初始化点击样式为module-todo的元素的时间
        initTodoClick : function() {
            $(document).on("click", ".module-todo", function() {
                t.todo();
            });
        },
        todo : function() {
            Notify.info("正在建设中...");
        },
        cloneAttributes : function(attributes) {
            $.extend(Global, attributes);
        },
        /** 判断用户是否具有某个权限 */
        hasPrivilege : function(privilegeId) {
            for ( var index in Global.allPrivilegeIds) {
                if (Global.allPrivilegeIds[index] == privilegeId) {
                    return true;
                }
            }
            return false;
        },
        /** 字符串转json对象的方法 */
        string2json : function(string) {
            if (string == undefined || string == null) {
                return null;
            }
            return $.parseJSON(string);
        },
        /** json对象转字符串的方法 */
        json2string : function(obj) {
            return JSON.stringify(obj);
        },
        /**
         * 图片加载失败时的处理: PS：errorCount是计数器，避免进入死循环判断
         */
        onImgError : function(img) {
            // 记录原有路径
            var originalSrc = img.getAttribute('originalSrc');
            var src = img.getAttribute('src');
            if (!originalSrc) {
                img.setAttribute('originalSrc', src);
                originalSrc = src;
            }
            var noImgUrl = '/scfc/images/tpri/404.jpg';
            var errorUrl = img.getAttribute('errorUrl');
            var errorCount = parseInt(img.getAttribute('errorCount') || '0');
            if (errorCount >= 1) {
                img.src = noImgUrl;
                return;
            }
            // 立即修改errorCount，img的src变了之后，如果还是加载失败，会立即调用onImgError，因此需要提前设置errorCount
            img.setAttribute('errorCount', errorCount + 1);

            if (errorCount == 0 && errorUrl) {
                img.src = errorUrl;
            } else {
                img.src = noImgUrl;
            }
        },
        /**
         * 找不到党员照片时，跳转到此
         */
        onIconError : function(img) {
            // 记录原有路径
            var originalSrc = img.getAttribute('originalSrc');
            var gender = img.getAttribute('gender');
            var src = img.getAttribute('src');
            if (!originalSrc) {
                img.setAttribute('originalSrc', src);
                originalSrc = src;
            }
            var noImgUrl = '/scfc/images/tpri/male.png';
            if (gender == "2") {
                noImgUrl = '/scfc/images/tpri/female.png';
            }
            var errorUrl = img.getAttribute('errorUrl');
            var errorCount = parseInt(img.getAttribute('errorCount') || '0');
            if (errorCount >= 1) {
                img.src = noImgUrl;
                return;
            }
            // 立即修改errorCount，img的src变了之后，如果还是加载失败，会立即调用onImgError，因此需要提前设置errorCount
            img.setAttribute('errorCount', errorCount + 1);

            if (errorCount == 0 && errorUrl) {
                img.src = errorUrl;
            } else {
                img.src = noImgUrl;
            }
        },
        // 判断浏览器
        myBrowser : function() {
            var userAgent = navigator.userAgent; // 取得浏览器的userAgent字符串
            var isOpera = userAgent.indexOf("Opera") > -1;
            // 判断是否Opera浏览器
            if (isOpera) {
                return "Opera";
            }
            // 判断是否Firefox浏览器
            if (userAgent.indexOf("Firefox") > -1) {
                return "Firefox";
            }
            // 判断是否Chrome浏览器
            if (userAgent.indexOf("Chrome") > -1) {
                return "Chrome";
            }
            // 判断是否Safari浏览器
            if (userAgent.indexOf("Safari") > -1) {
                return "Safari";
            }
            // 判断是否IE浏览器
            if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
                return "IE";
            }
        },
        // 获取枚举值
        getEnumName : function(enumId) {
            if (!Global.enums) {
                return "";
            }
            var enumName = Global.enums[enumId];
            if (!enumName) {
                return "";
            }
            return enumName;
        },
        // 获取代码名称
        getCodeName : function(codeId) {
            if (!Global.codes) {
                return "";
            }
            var codeName = Global.codes[codeId];
            if (!codeName) {
                return "";
            }
            return codeName;
        },
        // 获取日期时间串中的日期
        getDate : function(dateTime) {
            if (!dateTime) {
                return "";
            }
            return dateTime.substr(0, 10);
        },
        // 星期的中文字符
        getDay : function(day) {
            switch (day) {
            case 0:
                return "日";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            default:
                return "-";
            }
        },
        // 获取cookie
        getCookie : function(name) {
            var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
            if (arr != null)
                return decodeURIComponent(arr[2]);
            return null;
        },
        // 设置cookie
        setCookie : function(name, value, options) {
            var expires = '', path = '', domain = '', secure = '';
            if (options) {
                if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
                    var exp;
                    if (typeof options.expires == 'number') {
                        exp = new Date();
                        exp.setTime(exp.getTime() + options.expires * 24 * 60 * 60 * 1000);
                    } else {
                        exp = options.expires;
                    }
                    expires = ';expires=' + exp.toUTCString();
                }
                path = options.path ? '; path=' + options.path : '';
                domain = options.domain ? ';domain=' + options.domain : '';
                secure = options.secure ? ';secure' : '';
            }
            document.cookie = [ name, '=', encodeURIComponent(value), expires, path, domain, secure ].join('');
        },
        initLoader : function(divId) {
            //divId 加载的divId
            if (divId == '' || divId == null) {
                divId = '#loader';
            }
            $(divId).html('<div style="width:100px;margin:0 auto;"><i class="fa fa-spinner fa-spin fa-5x fa-fw" style="color:#de2810;"></i></div>');
        },
        // 设置url参数值，ref参数名,value新的参数值
        changeURLPara : function(url, ref, value) {
            var str = "";
            if (url.indexOf('?') != -1) {
                str = url.substr(url.indexOf('?') + 1);
            } else {
                return url + "?" + ref + "=" + value;
            }
            var returnurl = "";
            var setparam = "";
            var arr;
            var modify = "0";

            if (str.indexOf('&') != -1) {
                arr = str.split('&');
                for (i in arr) {
                    if (arr[i].split('=')[0] == ref) {
                        setparam = value;
                        modify = "1";
                    } else {
                        setparam = arr[i].split('=')[1];
                    }
                    returnurl = returnurl + arr[i].split('=')[0] + "=" + setparam + "&";
                }
                returnurl = returnurl.substr(0, returnurl.length - 1);
                if (modify == "0") {
                    if (returnurl == str) {
                        returnurl = returnurl + "&" + ref + "=" + value;
                    }
                }
            } else {
                if (str.indexOf('=') != -1) {
                    arr = str.split('=');
                    if (arr[0] == ref) {
                        setparam = value;
                        modify = "1";
                    } else {
                        setparam = arr[1];
                    }
                    returnurl = arr[0] + "=" + setparam;
                    if (modify == "0")
                        if (returnurl == str)
                            returnurl = returnurl + "&" + ref + "=" + value;
                } else {
                    returnurl = ref + "=" + value;
                }
            }
            return url.substr(0, url.indexOf('?')) + "?" + returnurl;
        },
        /**
         * @description 浏览器打开弹出窗
         * @author yiwenjun
         */
        openWindow : function(url, name, iWidth, iHeight) {
            var iTop = (window.screen.height - 30 - iHeight) / 2; //获得窗口的垂直位置;
            var iLeft = (window.screen.width - 10 - iWidth) / 2; //获得窗口的水平位置;
            var windowObject=window.open(url, name, 'height=' + iHeight + ',innerHeight=' + iHeight + ',width=' + iWidth + ',innerWidth=' + iWidth + ',top=' + iTop + ',left=' + iLeft + ',toolbar=no,menubar=no,scrollbars=yes,resizeable=no,location=no,status=no');
            return windowObject;
        },
        /**
         * @description 获取年份的select选项
         * @author yiwenjun
         * @since 2015-09-23
         * 
         * @param type:
         *            1当前年往后num年；2当前年份往前num年； 3当前年份前后num年，默认1
         * @param num:年数，默认10
         * @param order:0顺序；1倒序，默认0
         * @param isFirstBlack：0显示否为空；1显示第一个；默认0
         * @return select下的选项html
         */
        getYearForSelectOption : function(type, num, order, isFirstBlack) {

            var today = new Date();
            var currentYear = today.getFullYear();
            if (!num || num < 1) {
                num = 10;
            }
            if (!isFirstBlack) {
                isFirstBlack = 0;
            }
            var options = "";
            if (isFirstBlack == 0) {
                options += '<option value=""></option>';
            }
            if (type == 2) {
                if (order == 1) {
                    for (var year = currentYear; year > currentYear - num; year--) {
                        options += '<option value=' + year + '>' + year + '</option>';
                    }
                } else {
                    for (var year = currentYear - num + 1; year <= currentYear; year++) {
                        options += '<option value=' + year + '>' + year + '</option>';
                    }
                }
            } else if (type == 3) {
                if (order == 1) {
                    for (var year = currentYear + num; year > currentYear - num; year--) {
                        options += '<option value=' + year + '>' + year + '</option>';
                    }
                } else {
                    for (var year = currentYear - num + 1; year < currentYear + num; year++) {
                        options += '<option value=' + year + '>' + year + '</option>';
                    }
                }
            } else {
                if (order == 1) {
                    for (var year = currentYear + num - 1; year >= currentYear; year--) {
                        options += '<option value=' + year + '>' + year + '</option>';
                    }
                } else {
                    for (var year = currentYear; year < currentYear + num; year++) {
                        options += '<option value=' + year + '>' + year + '</option>';
                    }
                }
            }
            return options;
        }
    }
    return t;
}();
$(function() {
    Global.init();
    String.prototype.trim = function() {
        return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
    }
});