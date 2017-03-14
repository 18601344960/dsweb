/**
 * @description JS工具类
 * @author 赵子靖
 * @since 2015-07-17
 */
var JSUtils = function() {
    var t = {
        // 获取当前之前年份列表
        getYears : function(nums) {
            var myDate = new Date();
            var nowYear = myDate.getFullYear();
            var yearArray = new Array(nums);
            yearArray[0] = nowYear;
            for (var i = 1; i < nums; i++) {
                yearArray[i] = yearArray[i - 1] * 1 - 1; // 之前的一年
            }
            return yearArray;
        },
        // 日期格式化输出
        dateFormatToYYYYMMDD : function(date) {
            if (date == null || date == '') {
                return '';
            } else {
                if (date.length >= 10) {
                    return date.substring(0, 10);
                }
            }
        },
        // 获取当前日期格式化输出 YYYY-MM-DD
        getNowDateToYYYYMMDD : function() {
            var today = new Date();
            var year = today.getYear();
            var month = today.getMonth();
            var date = today.getDate();
            month = month + 1;
            if (year < 1900)
                year += 1900; // Firefox下的兼容处理
            if (month <= 9)
                month = "0" + month;
            if (date <= 9)
                date = "0" + date;
            return nowDateFormat = year + "-" + month + "-" + date;
        },
        // 根据parentId获取sys_code国标代码
        // parentId:国标码父ID，selectDiv：选项div，selectedOption选中option的值
        getSysCodeByParent : function(parentId, selectDiv, selectedOption) {
            $("#" + selectDiv + " option").remove(); // 清空option选项
            Ajax.call({
                url : "sys/getCodeListByParentId",
                p : {
                    parentId : parentId
                },
                async : false, // false同步 true异步
                f : function(data) {
                    $("#" + selectDiv).append("<option value=''></option>");
                    for (var i = 0; i < data.rows.length; i++) {
                        if (selectedOption == data.rows[i].id) {
                            $("#" + selectDiv).append("<option selected='selected' value='" + data.rows[i].id + "'>" + data.rows[i].name + "</option>");
                        } else {
                            $("#" + selectDiv).append("<option value='" + data.rows[i].id + "'>" + data.rows[i].name + "</option>");
                        }
                    }
                    if (data.rows.length == 0) {
                        $("#" + selectDiv).append("<option value=''>暂无可选内容</option>");
                    }
                }
            });
        },
        // bootstrap表格列的鼠标指针
        BootstrapGridCellStyle : function(value, row, index) {
            if (value && value != "" && value != 0) {
                return '<a href="javascript:;" style="color:blue;text-decoration:underline;">' + value + '</a>'
            } else {
                return value;
            }
        },
        dateDiff:function(date1, date2){  
		    if(CheckInputUtils.isEmpty(date1) || CheckInputUtils.isEmpty(date2)){
		        return 0;
		    }
	        var type1 = typeof date1, type2 = typeof date2;       
	        if(type1 == 'string')       
	            date1 = t.stringToTime(date1);       
	        else if(date1.getTime)       
	            date1 = date1.getTime();       
	        if(type2 == 'string')       
	            date2 = t.stringToTime(date2);       
	        else if(date2.getTime)       
	            date2 = date2.getTime();   
	        return Math.round((date2 - date1) / 1000 / 60 / 60 / 24 /360);//除1000是毫秒，不加是秒   
	    } ,
	    stringToTime:function(string){       
	        var f = string.split(' ', 2);       
	        var d = (f[0] ? f[0] : '').split('-', 3);       
	        var t = (f[1] ? f[1] : '').split(':', 3);       
	        return (new Date(       
	        parseInt(d[0], 10) || null,       
	        (parseInt(d[1], 10) || 1)-1,       
	        parseInt(d[2], 10) || null,       
	        parseInt(t[0], 10) || null,      
	        parseInt(t[1], 10) || null,       
	        parseInt(t[2], 10) || null)).getTime();   
	    }
    }
    return t;
}();