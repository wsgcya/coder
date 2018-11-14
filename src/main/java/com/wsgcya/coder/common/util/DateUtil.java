package com.wsgcya.coder.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 *
 * @author changwen(changwen @ telincn.com)
 */
public class DateUtil {
    //默认使用系统当前时区
    private static final ZoneId ZONE = ZoneId.systemDefault();

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";
    private static final String MONTH_PATTERN = "MM.dd";
    private static final String DEFAULT_DELIM = "-";
    public static String yyyyMMddHHmmssSpt = "yyyy-MM-dd HH:mm:ss";
    public static String yyyyMMddHHmmssSSpt = "yyyy-MM-dd HH:mm:ss.S";
    public static String yyyyMMddHHmmSpt = "yyyy-MM-dd HH:mm";
    public static String yyyyMMddSpt = "yyyy-MM-dd";
    public static String yyyyMMSpt = "yyyy-MM";
    public static String HHmmssSpt = "HH:mm:ss";
    public static String HHmmSpt = "HH:mm";


    public static long getUnixTimestamp(){
        return getUnixTimestamp(new Date());
    }

    public static long getUnixTimestamp(String dataTime, String format){
        return getUnixTimestamp(parseDateTime(dataTime, format));
    }

    public static long getUnixTimestamp(Date date){
        return date.getTime() / 1000;
    }

    /**
     * 字符串->日期
     *
     * @param dataTime
     * @param pattern
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date parseDateTime(String dataTime, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(dataTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(dataTime);
        }
    }

    public static String parseDateTime(Date dataTime, String pattern) {
        return new SimpleDateFormat(pattern).format(dataTime);
    }

    public static String parseDateTime(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static String parseDateTime(long dataTime, String pattern) {
        return new SimpleDateFormat(pattern).format(dataTime);
    }

    public static String parseDateTime(LocalDate localDate, String pattern) {
        return parseDateTime(localDateToDate(localDate), pattern);
    }

    public static String parseTimeStr2Str(String time, String pattern) {
        return parseDateTime(parseDateTime(time, pattern), pattern);
    }

    public static String getCurrentDateTime(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    /**
     * 将Date转换成LocalDateTime
     *
     * @param d date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime;
    }

    /**
     * 将Date转换成LocalDate
     *
     * @param d date
     * @return
     */
    public static LocalDate dateToLocalDate(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalDate();
    }

    /**
     * 将Date转换成LocalTime
     *
     * @param d date
     * @return
     */
    public static LocalTime dateToLocalTime(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalTime();
    }

    /**
     * 将LocalDate转换成Date
     *
     * @param localDate
     * @return date
     */
    public static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        Instant instant = localDate.atStartOfDay().atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 将LocalDateTime转换成Date
     *
     * @param localDateTime
     * @return date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 验证日期是否合法（yyyy-MM-dd）
     *
     * @param sDate
     * @return
     */
    public static boolean isValidDate(String sDate) {
        if ("null".equalsIgnoreCase(sDate) || "undefined".equalsIgnoreCase(sDate) || "000000".equalsIgnoreCase(sDate)) {
            return true;
        }
        if (!"null".equalsIgnoreCase(sDate) && !"undefined".equalsIgnoreCase(sDate) && !"000000".equalsIgnoreCase(sDate)) {
            String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
            // String datePattern1 = fmt;
            String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                    + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                    + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                    + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                    + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                    + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
            if ((sDate != null)) {
                Pattern pattern = Pattern.compile(datePattern1);
                Matcher match = pattern.matcher(sDate);
                if (match.matches()) {
                    pattern = Pattern.compile(datePattern2);
                    match = pattern.matcher(sDate);
                    return match.matches();
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Get the first day of month
     *
     * @param pattern e.g. MM.dd yyyy-MM-dd
     * @return
     */
    public static String getFirstDayOfMonth(String pattern) {
        if (pattern == null)
            pattern = MONTH_PATTERN;
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE, 1);// set the first day of month
        return new SimpleDateFormat(pattern).format(date.getTime());
    }

    /**
     * Get the first day of month
     *
     * @return
     */
    public static Date getFirstDayOfMonth() {

        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE, 1);// set the first day of month
        return date.getTime();
    }

    /**
     * Get the last day of month
     *
     * @param pattern e.g. MM.dd yyyy-MM-dd
     * @return
     */
    public static String getLastDayOfMonth(String pattern) {
        if (pattern == null)
            pattern = MONTH_PATTERN;
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, 1);
        date.set(Calendar.DAY_OF_MONTH, 0);// set the first day of month
        return new SimpleDateFormat(pattern).format(date.getTime());
    }

    /**
     * Get the last day of month
     *
     * @return
     */
    public static Date getLastDayOfMonth() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, 1);
        date.set(Calendar.DAY_OF_MONTH, 0);// set the first day of month
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 999);
        return date.getTime();
    }

    /**
     * Get the current day of month
     *
     * @param pattern e.g. MM.dd yyyy-MM-dd
     * @return
     */
    public static String getCurrentDayOfMonth(String pattern) {
        if (pattern == null)
            pattern = MONTH_PATTERN;
        Calendar date = Calendar.getInstance();
        return new SimpleDateFormat(pattern).format(date.getTime());
    }

    /**
     * Get the begin date and end date for query request
     *
     * @param pattern
     * @param delim
     * @return
     */
    public static String getBeginEndDate(String pattern, String delim) {
        if (delim == null)
            delim = DEFAULT_DELIM;
        return getFirstDayOfMonth(pattern) + delim + getCurrentDayOfMonth(pattern);
    }

    /**
     * Get the begin date and end date for query request
     *
     * @return
     */
    public static String getDefaultBeginEndDate() {
        return getFirstDayOfMonth(MONTH_PATTERN) + DEFAULT_DELIM + getCurrentDayOfMonth(MONTH_PATTERN);
    }

    /**
     * Get the begin date and end date for query request
     *
     * @return
     */
    public static String getBeforeDayBeginEndDate() {
        return getFirstDayOfMonth(MONTH_PATTERN) + DEFAULT_DELIM + getBeforeDayOfMonth(MONTH_PATTERN);
    }

    private static String getBeforeDayOfMonth(String pattern) {
        if (pattern == null)
            pattern = MONTH_PATTERN;
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, -1);
        return new SimpleDateFormat(pattern).format(date.getTime());
    }

    /**
     * @return
     */
    public static String getDefaultQueryDate() {
        Calendar date = Calendar.getInstance();
        return new SimpleDateFormat(DEFAULT_PATTERN).format(date.getTime());
    }

    /**
     * 获取上个月第一天的日期
     *
     * @return
     */
    public static String getFirstDayOfLastMonth(String pattern) {
        if (pattern == null)
            pattern = DEFAULT_PATTERN;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, -1);
        return new SimpleDateFormat(pattern).format(cal.getTime());
    }

    /**
     * 获取上个月最后一天的日期
     *
     * @return
     */
    public static String getLastDayOfLastMonth(String pattern) {
        if (pattern == null)
            pattern = DEFAULT_PATTERN;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        int MaxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), MaxDay);
        return new SimpleDateFormat(pattern).format(cal.getTime());
    }

    public static String getBeginEndDateOfLastMonth(String delim) {
        if (delim == null)
            delim = DEFAULT_DELIM;
        return getFirstDayOfLastMonth(MONTH_PATTERN) + delim + getLastDayOfLastMonth(MONTH_PATTERN);
    }

    /**
     * 获取套餐余量的开始与结束时间 时间格式:2012-08-17至2012-08-17
     *
     * @return
     */
    public static String getPackageFlowDayMonth() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        String result = "";
        switch (month) {
            case 0:
                if (day == 1) {
                    year = year - 1;
                    cal.set(year, 11, 1);
                    result += (year) + "-12-1至" + (year) + "-12-" + cal.getActualMaximum(Calendar.DATE);
                } else {
                    result += year + "-" + (month + 1) + "-1至" + year + "-" + (month + 1) + "-" + (day - 1);
                }
                break;
            default:
                if (day == 1) {
                    cal.set(year, month - 1, 1);
                    result += year + "-" + month + "-1" + "至" + year + "-" + month + "-" + cal.getActualMaximum(Calendar.DATE);
                } else {
                    result += year + "-" + (month + 1) + "-1" + "至" + year + "-" + (month + 1) + "-" + (day - 1);
                }
                break;
        }

        return result;
    }

    /**
     * 日期 累加天数
     *
     * @param date
     * @param days
     */
    public static Date addDateNumber(String date, int days) {
        return addDateNumber(date, yyyyMMddHHmmssSpt, days);
    }

    public static Date addDateNumber(String date, String format, int days) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date result = null;
        try {
            // String转Date
            result = addDateNumber(simpleDateFormat.parse(date), days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 日期累加天数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDateNumber(Date date, int days) {
        Date result = null;
        Calendar calendar = Calendar.getInstance();
        // Date转Calendar
        calendar.setTime(date);
        // 增加天数
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * @param date
     * @param months
     * @return
     * @Title addMonthNumber
     * @Class DateUtil
     * @Description 日期累加月数
     * @author qinshijiang@telincn.com
     * @Date 2017年10月31日
     */
    public static Date addMonthNumber(Date date, int months) {
        Date result = null;
        Calendar calendar = Calendar.getInstance();
        // Date转Calendar
        calendar.setTime(date);
        // 增加月数
        calendar.add(Calendar.MONTH, months);
        result = calendar.getTime();
        return result;
    }

    /**
     * 获取2个日期之间的时间差
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int betweenDay(Date startTime, Date endTime) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(startTime);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endTime);

        long l = c2.getTimeInMillis() - c1.getTimeInMillis();
        int days = new Long(l / (1000 * 60 * 60 * 24)).intValue();
        return days;
    }

    /**
     * 获取2个时间的毫秒差
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static long betweenMsec(Date startTime, Date endTime) {
        return endTime.getTime() - startTime.getTime();
    }

    /**
     * 获取日期的起始时间 即 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getBeginOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getBeginOfDay() {
        return getBeginOfDay(new Date());
    }

    /**
     * 获取日期的起始时间 即 23:59:59
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date getEndOfDay() {
        return getEndOfDay(new Date());
    }

    /**
     * @param time
     * @return
     * @Title chargeDate
     * @Class DateUtil
     * @Description yyyyMMdd格式转换成时间
     * @author wangzhe@hzchuangbo.com
     * @Date 2017年4月20日
     */
    public static Date chargeDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param date
     * @return
     * @Title chargeTime
     * @Class DateUtil
     * @Description 时间转成 yyyy-MM-dd字符串
     * @author wangzhe@hzchuangbo.com
     * @Date 2017年4月20日
     */
    public static String chargeTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        return sdf.format(date);
    }

    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取本月剩余天数
     *
     * @return
     */
    public static int getRemainMonthDay() {
        Calendar c = Calendar.getInstance();
        int totalDays = getCurrentMonthDay();
        int alreadyDay = c.get(Calendar.DAY_OF_MONTH);
        return totalDays - alreadyDay + 1;
    }

    /**
     * 判断日期是否是周末
     * @param bDate
     * @return
     * @throws ParseException
     */
    public static String isWeekend(String bDate){
        DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
        Date bdate = null;
        try {
            bdate = format1.parse(bDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(bdate);
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return "OK";
        } else{
            return "NO";
        }

    }

}
