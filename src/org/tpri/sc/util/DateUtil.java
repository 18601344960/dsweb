package org.tpri.sc.util;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import org.apache.log4j.Logger;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B>日期工具类<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 交通运输部规划研究院（易文俊）
 * @since 2015年5月10日
 */
public class DateUtil {
    protected static Logger logger = Logger.getLogger(DateUtil.class);

    public static final SimpleDateFormat sdf = new SimpleDateFormat();

    /** 默认日期格式 */
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd";
    public static final String DIRECTORYPATH_FORMAT = "yyyy" + File.separator + "MM" + File.separator + "dd";
    public static final String YYYYMMDD_FORMAT = "yyyyMMdd";
    public static final String YYYYMMDDHHMMSS_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 包含年月日时分秒的格式 */
    public static DateFormat longDateFormat = new SimpleDateFormat(YYYYMMDDHHMMSS_FORMAT);

    /**
     * 获取分级目录存储的日期路径：yyyy/MM/dd/
     * 
     * @param created
     * @return
     */
    public static String getDirectoryPath(Date created) {
        sdf.applyPattern(DIRECTORYPATH_FORMAT);
        return sdf.format(created);
    }

    /**
     * 字符串转换成日期 如果转换格式为空，则利用默认格式进行转换操作
     * 
     * @param str 字符串
     * @param format 日期格式
     * @return 日期
     * @throws java.text.ParseException
     */
    public static Date str2Date(String str, String format) {
        if (null == str || "".equals(str)) {
            return null;
        }
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "".equals(format)) {
            format = DEFAULT_FORMAT;
        }
        sdf.applyPattern(format);
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            logger.error("error", e);
        }
        return null;
    }

    /**
     * 日期转换为字符串
     * 
     * @param date 日期
     * @param format 日期格式
     * @return 字符串
     */
    public static String date2Str(Date date, String format) {
        if (null == date) {
            return null;
        }
        sdf.applyPattern(format);
        return sdf.format(date);
    }

    /**
     * 日期转换为中文字符串
     */
    public static String date2ChineseStr(Date date) {
        if (null == date || date.equals("")) {
            return null;
        }
        String str = (date.getYear() + 1900) + "年" + (date.getMonth() + 1) + "月" + (date.getDate() + 1) + "日";
        return str;
    }

    /**
     * 日期转换为长日期字符串,包含年月日时分秒
     */
    public static String date2StrLong(Date date) {
        if (null == date) {
            return "";
        }
        return longDateFormat.format(date);
    }

    /**
     * 时间戳转换为字符串
     */
    public static String timestamp2Str(Timestamp time) {
        Date date = null;
        if (null != time) {
            date = new Date(time.getTime());
        }
        return date2Str(date, DEFAULT_FORMAT);
    }

    /**
     * 字符串转换时间戳
     */
    public static Timestamp str2Timestamp(String str, String format) {
        Date date = str2Date(str, format);
        return new Timestamp(date.getTime());
    }

    /**
     * 时间戳转换为字符串
     */
    public static String timestamp2Str(Timestamp time, String format) {
        Date date = null;
        if (null != time) {
            date = new Date(time.getTime());
        }
        return date2Str(date, format);
    }

    /** 字符串转换时间戳 */
    public static Timestamp str2Timestamp(String str) {
        Date date = str2Date(str, DEFAULT_FORMAT);
        return new Timestamp(date.getTime());
    }

    /** 当前日期加减n天后的日期，返回String (yyyy-mm-dd) */
    public static String nDaysAftertoday(int n) {
        sdf.applyPattern(DEFAULT_FORMAT);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DATE, n);
        return sdf.format(rightNow.getTime());
    }

    /**
     * 得到指定或者当前时间后n天的Calendar
     */
    public static String afterNDays(int n) {
        Calendar c = Calendar.getInstance();
        // 偏移量，给定n天的毫秒数
        long offset = n * 24 * 60 * 60 * 1000;
        c.setTimeInMillis(c.getTimeInMillis() + offset);
        return sdf.format(c.getTime());
    }

    /**
     * 获取当前时间戳
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取加后缀的开始日期字符串
     */
    public static String getFromDateStrPostfix(String fromDateStr) {
        return fromDateStr + " 00:00:00";
    }

    /**
     * 获取加后缀的结束日期字符串
     */
    public static String getToDateStrPostfix(String fromDateStr) {
        return fromDateStr + " 23:59:59";
    }

    /**
     * 获取指定日期的前N天
     */
    public static java.sql.Date getBeforeAfterDate(String datestr, int day) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date olddate = null;
        try {
            df.setLenient(false);
            olddate = new java.sql.Date(df.parse(datestr).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误");
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(olddate);

        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        int Day = cal.get(Calendar.DAY_OF_MONTH);

        int NewDay = Day - day;

        cal.set(Calendar.YEAR, Year);
        cal.set(Calendar.MONTH, Month);
        cal.set(Calendar.DAY_OF_MONTH, NewDay);

        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * 日历转成日期
     */
    public static Date CalendarToDate(Calendar c) {
        return c.getTime();
    }

    /**
     * 日期转成日历
     */
    public static Calendar DateToCalendar(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c;
    }

    /**
     * 得到日期中年月组成的字符串
     * 
     * @return yyyyMM格式的年月字符串
     */
    public static String convertDateToYearMonth(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM", new DateFormatSymbols());
        return df.format(d);
    }

    /**
     * 得到日期中年月日组成的字符串
     */
    public static String convertDateToYearMonthDay(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", new DateFormatSymbols());
        return df.format(d);
    }

    /**
     * 得到日期中时间字符串
     */
    public static String convertDateToTime(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("HH24MISS", new DateFormatSymbols());
        return df.format(d);
    }

    /**
     * 获取当前年月的字符串
     * 
     * @return 当前年月，yyyyMM格式
     */
    public static String getCurrentYearMonth() {
        Calendar cal = Calendar.getInstance();
        String currentYear = (new Integer(cal.get(Calendar.YEAR))).toString();
        String currentMonth = null;
        if (cal.get(Calendar.MONTH) < 9)
            currentMonth = "0" + (new Integer(cal.get(Calendar.MONTH) + 1)).toString();
        else
            currentMonth = (new Integer(cal.get(Calendar.MONTH) + 1)).toString();
        return (currentYear + currentMonth);
    }

    /**
     * 获取输入格式的日期字符串，字符串遵循Oracle格式
     * 
     * @param d - 日期
     * @param format - 指定日期格式，格式的写法为Oracle格式
     * @return 按指定的日期格式转换后的日期字符串
     */
    public static String dateToString(Date d, String format) {
        if (d == null)
            return "";
        Hashtable<Integer, String> h = new Hashtable<Integer, String>();
        String javaFormat = new String();
        String s = format.toLowerCase();
        if (s.indexOf("yyyy") != -1)
            h.put(new Integer(s.indexOf("yyyy")), "yyyy");
        else if (s.indexOf("yy") != -1)
            h.put(new Integer(s.indexOf("yy")), "yy");
        if (s.indexOf("mm") != -1)
            h.put(new Integer(s.indexOf("mm")), "MM");
        if (s.indexOf("dd") != -1)
            h.put(new Integer(s.indexOf("dd")), "dd");
        if (s.indexOf("hh24") != -1)
            h.put(new Integer(s.indexOf("hh24")), "HH");
        if (s.indexOf("mi") != -1)
            h.put(new Integer(s.indexOf("mi")), "mm");
        if (s.indexOf("ss") != -1)
            h.put(new Integer(s.indexOf("ss")), "ss");
        int intStart = 0;
        while (s.indexOf("-", intStart) != -1) {
            intStart = s.indexOf("-", intStart);
            h.put(new Integer(intStart), "-");
            intStart++;
        }
        intStart = 0;
        while (s.indexOf("/", intStart) != -1) {
            intStart = s.indexOf("/", intStart);
            h.put(new Integer(intStart), "/");
            intStart++;
        }
        intStart = 0;
        while (s.indexOf(" ", intStart) != -1) {
            intStart = s.indexOf(" ", intStart);
            h.put(new Integer(intStart), " ");
            intStart++;
        }
        intStart = 0;
        while (s.indexOf(":", intStart) != -1) {
            intStart = s.indexOf(":", intStart);
            h.put(new Integer(intStart), ":");
            intStart++;
        }
        if (s.indexOf("年") != -1)
            h.put(new Integer(s.indexOf("年")), "年");
        if (s.indexOf("月") != -1)
            h.put(new Integer(s.indexOf("月")), "月");
        if (s.indexOf("日") != -1)
            h.put(new Integer(s.indexOf("日")), "日");
        if (s.indexOf("时") != -1)
            h.put(new Integer(s.indexOf("时")), "时");
        if (s.indexOf("分") != -1)
            h.put(new Integer(s.indexOf("分")), "分");
        if (s.indexOf("秒") != -1)
            h.put(new Integer(s.indexOf("秒")), "秒");
        int i = 0;
        while (h.size() != 0) {
            Enumeration<Integer> e = h.keys();
            int n = 0;
            while (e.hasMoreElements()) {
                i = ((Integer) e.nextElement()).intValue();
                if (i >= n)
                    n = i;
            }
            String temp = (String) h.get(new Integer(n));
            h.remove(new Integer(n));
            javaFormat = temp + javaFormat;
        }
        SimpleDateFormat df = new SimpleDateFormat(javaFormat, new DateFormatSymbols());
        return df.format(d);
    }

    /**
     * 取得当前日期,格式为:YYYYMMDD
     */
    public static String getNowDate() {
        String nowTime = "";
        SimpleDateFormat sdfLongTime = new SimpleDateFormat("yyyyMMdd");
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new java.util.Date().getTime());
            nowTime = sdfLongTime.format(date);
            return nowTime;
        } catch (Exception e) {
            System.out.println("Error at DateUtils.java getNowDate():" + e.getMessage());
        }
        return nowTime;
    }

    /**
     * 取得当前年份+year后的格式为:YYYYMMDDHHMISS的时间
     */
    @SuppressWarnings("deprecation")
    public static String getNowLongTime(int year) {
        String nowTime = "";
        SimpleDateFormat sdfLongTime = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new java.util.Date().getTime());
            date.setYear(date.getYear() + year);
            nowTime = sdfLongTime.format(date);
            return nowTime;
        } catch (Exception e) {
            System.out.println("Error at DateUtils.java getNowLongTime():" + e.getMessage());
        }
        return nowTime;
    }

    /**
     * 字符串转成指定格式的日历
     */
    public static Calendar stringToCalendar(String dateSting, String format) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Date date = sf.parse(dateSting);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 日历转成指定格式的字符串
     */
    public static String calendarString(Calendar c, String format) {
        Date d = c.getTime();
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(d);
    }

    /**
     * 时间字符串转成当前时间的日期 必须保证传过来的time是HH:mm:ss格式的
     */
    @SuppressWarnings("deprecation")
    public static Date timeToDate(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (time.indexOf(".") > -1)
            time = time.substring(0, time.indexOf("."));
        String[] timeArray = time.split(":");
        if (timeArray.length == 3) {
            Date date = df.parse(df.format(new Date()));
            date.setHours(Integer.valueOf(timeArray[0]));
            date.setMinutes(Integer.valueOf(timeArray[1]));
            date.setSeconds(Integer.valueOf(timeArray[2]));
            return date;
        }
        return null;
    }

    /**
     * 获取指定日期的时间
     */
    @SuppressWarnings("deprecation")
    public static String getTime(Date date) {
        int hour = date.getHours();
        int minutes = date.getMinutes();
        int second = date.getSeconds();
        return hour + ":" + minutes + ":" + second;
    }

    /**
     * 获取当前日期
     */
    public static Date getNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new DateFormatSymbols());
        Date date = new Date();
        try {
            date = df.parse(df.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取指定日期那天的开始时间
     */
    @SuppressWarnings("deprecation")
    public static Date getStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期那天的结束时间
     */
    @SuppressWarnings("deprecation")
    public static Date getEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDate(), 23, 59, 59);
        return calendar.getTime();
    }

    /**
     * 根据月份获取月份的总天数
     */
    public static int getDayofMonth(int month) {
        if (month < 0 || month > 12) {
            return -1;
        }
        switch (month) {
        case 1:
        case 3:
        case 5:
        case 7:
        case 8:
        case 10:
        case 12:
            return 31;
        case 2:
            return 28;
        case 4:
        case 6:
        case 9:
        case 11:
            return 30;
        default:
            return -1;
        }
    }

    /**
     * 获取某一季度的总天数
     */
    public static int getDayofQuarter(int quarter) {
        if (quarter < 0 || quarter > 4) {
            return -1;
        }
        switch (quarter) {
        case 1:
            return 31 + 28 + 31;
        case 2:
            return 30 + 31 + 30;
        case 3:
            return 31 + 31 + 30;
        case 4:
            return 31 + 30 + 31;
        default:
            return -1;
        }
    }

    /**
     * 获取某月的1号在一年中的第几天
     */
    public static int getMonthDayOfYear(int month) {
        int total = 0;
        for (int i = 1; i < month; i++) {
            total += getDayofMonth(i);
        }
        return total + 1;
    }

    /**
     * 获取某季度的1第一天在一年中的第几天
     */
    public static int getQuarterDayOfYear(int quarter) {
        int total = 0;
        for (int i = 1; i < quarter; i++) {
            total += getDayofQuarter(i);
        }
        return total + 1;
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static void main(String[] args) {
        System.out.println(getBeforeAfterDate("2013-03-30", 2));
        ;
    }
}
