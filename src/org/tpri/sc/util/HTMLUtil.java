/**
 * Copyright 2016 TPRI. All Rights Reserved.
 */
package org.tpri.sc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>html工具类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2016年7月27日
 */
public class HTMLUtil {
    /**
     * <B>方法名称：</B>删除html标签<BR>
     * <B>概要说明：</B><BR>
     * 
     * @author 易文俊
     * @since 2016年7月27日
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr,boolean brToNewLine) {
        if (htmlStr == null || htmlStr.trim().equals("")) {
            return htmlStr;
        }
        if(brToNewLine){
            String regEx_br = "<br/>"; //定义HTML标签的换行正则表达式 
            Pattern p_html = Pattern.compile(regEx_br, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll("\n"); //替换换行 
        }
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式 

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签 

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签 

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签 
        
        String regEx_space = "&nbsp;"; //定义HTML标签的空格正则表达式 
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); //替换空格


        return htmlStr.trim(); //返回文本字符串 
    }
}
