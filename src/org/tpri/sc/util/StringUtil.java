package org.tpri.sc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String字符串处理工具类
 * 
 * @author zhaozijing
 *
 */
public class StringUtil {

    /**
     * 统计某个字符在字符串中出现的次数
     * 
     * @param str 字符串
     * @param findChar 查找的目标字符
     * @return 出现次数
     */
    public static int counter(String str, char findChar) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == findChar) {
                count++;
            }
        }
        return count;
    }

    public static boolean isBlank(String str) {
        if (str == null || str.trim().length() < 1) {
            return true;
        }
        return false;
    }

    /**
     * 创建一个32位的随机数 英文字母和数字
     * 
     * @param length 生成的长度
     * @return 返回结果
     */
    public static String randomForNumbers(int length) {//传入的字符串的长度
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            int r = (int) (Math.random() * 3);
            int rn1 = (int) (48 + Math.random() * 10);
            int rn2 = (int) (65 + Math.random() * 26);
            int rn3 = (int) (97 + Math.random() * 26);

            switch (r) {
            case 0:
                builder.append((char) rn1);
                break;
            case 1:
                builder.append((char) rn2);
                break;
            case 2:
                builder.append((char) rn3);
                break;
            }
        }
        return builder.toString();
    }

    /**
     * 将数字转为字母 例如1对应A 2对应B
     * 
     * @param number 需要转换的索引
     * @return 字母
     */
    public static String numberToLetter(int number) {
        String letter = ".";
        switch (number) {
        case 1:
            letter = "A";
            break;
        case 2:
            letter = "B";
            break;
        case 3:
            letter = "C";
            break;
        case 4:
            letter = "D";
            break;
        case 5:
            letter = "E";
            break;
        case 6:
            letter = "F";
            break;
        case 7:
            letter = "G";
            break;
        case 8:
            letter = "H";
            break;
        case 9:
            letter = "I";
            break;
        case 10:
            letter = "J";
            break;
        case 11:
            letter = "K";
            break;
        case 12:
            letter = "L";
            break;
        case 13:
            letter = "M";
            break;
        case 14:
            letter = "N";
            break;
        case 15:
            letter = "O";
            break;
        case 16:
            letter = "P";
            break;
        case 17:
            letter = "Q";
            break;
        case 18:
            letter = "R";
            break;
        case 19:
            letter = "S";
            break;
        case 20:
            letter = "T";
            break;
        case 21:
            letter = "U";
            break;
        case 22:
            letter = "V";
            break;
        case 23:
            letter = "W";
            break;
        case 24:
            letter = "X";
            break;
        case 25:
            letter = "Y";
            break;
        case 26:
            letter = "Z";
            break;
        default:
            break;
        }
        return letter;
    }

    /**
     * 判断字符串是否为空
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str) && !"null".equals(str) && !"NULL".equals(str)) {
            return false;
        } else {
            return true;
        }
    }

    public static String returnStr(String str) {
        if (isEmpty(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 
     * <B>方法名称：</B>判断字符是否为数字<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 赵子靖
     * @since 2016年2月26日
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 
     * <B>方法名称：</B>身份证号最后一位更改<BR>
     * <B>概要说明：</B><BR>
     * 最后一位如果不为数字 同一用X代替
     * 
     * @author 赵子靖
     * @since 2016年2月26日
     * @param idNumber
     * @return
     */
    public static String updateIdNumberLastCharX(String idNumber) {
        if (idNumber != null && !idNumber.equals("")) {
            idNumber = idNumber.trim();
            if (!isNumeric(idNumber)) {
                idNumber = idNumber.substring(0, idNumber.length() - 1) + "X";
            }
            return idNumber;
        }
        return null;
    }

    public static String stringNullToBlank(String str) {
        return str == null ? "" : str;
    }
}
