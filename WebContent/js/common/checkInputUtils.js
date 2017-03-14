/**
 * @description 表达验证通用工具类
 * @author zhaozijing
 * @since 2015-06-10
 */
var CheckInputUtils = function() {
	var t = {
		// 验证公民身份证是否正确
		checkIsPeopleNumber : function(peopleNumber) {
			// 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
			var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
			if (reg.test(peopleNumber)) {
				return true;
			} else {
				Notify.notice("请输入正确的居民身份证号码");
				return false;
			}
		},
		// 去除字符串两边空格
		splitSpaceForStrAround : function(str) {
			if(str==null || str==''){
				return '';
			}else{
				return str.replace(/(^\s*)|(\s*$)/g, "");
			}
		},
		// 判断是否为空
		isEmpty : function(str) {
			if (str != null && str != undefined && str != "") {
				return false;
			} else {
				return true;
			}
		},
		//判断是否为正整数
		isInteger:function(str){
			var reg = /(^[0-9]*[1-9][0-9]*$)/;
			if (reg.test(str)) {
				return true;
			} else {
				Notify.notice("请输入正整数");
				return false;
			}
		},
		moneyCheck:function (value){
	        var isNum = /^\d+(\.\d+)?$/;
	        if(!isNum.test(value)){
	        	Notify.notice("请输入正确的金额");
	            return false;
	        }else{
	            return true;
	        }
	    },
		/**
		 * 将数字转为大写英文字母
		 * 
		 * @param number
		 */
		numberToLetter : function(number) {
			var letter = "";
			switch (number) {
				case 1 :
					letter = "A";
					break;
				case 2 :
					letter = "B";
					break;
				case 3 :
					letter = "C";
					break;
				case 4 :
					letter = "D";
					break;
				case 5 :
					letter = "E"
					break;
				case 6 :
					letter = "F"
					break;
				case 7 :
					letter = "G"
					break;
				case 8 :
					letter = "H"
					break;
				case 9 :
					letter = "I"
					break;
				case 10 :
					letter = "J"
					break;
				case 11 :
					letter = "K"
					break;
				case 12 :
					letter = "L"
					break;
				case 13 :
					letter = "M"
					break;
				case 14 :
					letter = "N"
					break;
				case 15 :
					letter = "O"
					break;
				case 16 :
					letter = "P"
					break;
				case 17 :
					letter = "Q"
					break;
				case 18 :
					letter = "R"
					break;
				case 19 :
					letter = "S"
					break;
				case 20 :
					letter = "T"
					break;
				case 21 :
					letter = "U"
					break;
				case 22 :
					letter = "V"
					break;
				case 23 :
					letter = "W"
					break;
				case 24 :
					letter = "X"
					break;
				case 25 :
					letter = "Y"
					break;
				case 26 :
					letter = "Z"
					break;
				default :
					letter = ".";
					break;
			}
			return letter;
		},
		//验证文件后缀名是否图片类型
		checkIsImage:function(suffix){
			if(t.isEmpty(suffix)){
				return false;
			}
			var ext = ['.gif', '.jpg', '.jpeg', '.png'];
			suffix = suffix.toLowerCase();
			var flag = false;
			for(var i = 0; i < ext.length; i++)
			{
				if (suffix==(ext[i]))
				{
					flag = true;
					break;
				}
			}	
			return flag;
		}
	}
	return t;
}();