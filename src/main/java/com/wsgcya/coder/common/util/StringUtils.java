package com.wsgcya.coder.common.util;

import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang.StringUtils {
    private final static Logger logger = LoggerFactory.getLogger(StringUtils.class);
    static final String X_FORWARDED_PROTO = "x-forwarded-proto";
    static final String X_REAL_IP = "x-real-ip";
    static final String X_FORWARDED_FOR = "x-forwarded-for";
    static final String UNKNOWN = "unknown";
    static final String UTF_8 = "UTF-8";
    static final String HTTP_URLHEADER_SPLITCHAR = "://";
    static final String HTTP_PATHQUERY_SPLITCHAR = "?";
    static final String HTTP_QUERY_SPLITCHAR = "&";
    static final String HTTP_QYUERY_PARAM_SPLITCHAR = "=";
    static final String MUILT_IP_SPLITCHAR = ",";
    static final String DECIMAL_FORMAT_CHAR = "#";
    static final String CODE_QUERY_SPLITCHAR = "code=";
    static final String STATE_QUERY_SPLITCHAR = "state=";
    static final String APPID_QUERY_SPLITCHAR = "appid=";
    static final String WXAUTH_URL_FORMAT = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
    static final String QYHAUTH_URL_FORMAT = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&agentid=%s&state=%s#wechat_redirect";
    static final String COMPONENTAUTH_URL_FORMAT = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s&component_appid=%s#wechat_redirect";
    static final String WXAUTH_DEFAULT_SCOPE = "snsapi_base";
    static final String WXAUTH_DEFAULT_STATE = "123";
    static final String NUMBER_REG_EXPRESS = "^\\d{11}";
    static final String NUMBER_NOTZERO_REG_EXPRESS = "[1-9]";
    static final String RANDOM_STRING_SOURCE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String RANDOM_CHAR_SOURCE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String PHONE_HIDDEN_CHAR = "****";
    static final String URLENCODE_ERROR_LOG = "url encode error";
    static final String URLDECODE_ERROR_LOG = "url decode error";

    /**
     * @param request
     * @return
     * @Title getCurrentPath
     * @Class StringUtils
     * @Description 获取当前完整路径
     * @author qinshijiang@telincn.com
     * @Date 2016年7月30日
     */
    public static String getCurrentPath(HttpServletRequest request, String domain) {
        String url = getRequestScheme(request) + HTTP_URLHEADER_SPLITCHAR + domain + request.getContextPath() + request.getServletPath();
        if (request.getQueryString() != null) {
            String queryString = request.getQueryString();
            if (queryString.indexOf(CODE_QUERY_SPLITCHAR) != -1) {
                String[] params = queryString.split(HTTP_QUERY_SPLITCHAR);
                queryString = "";
                for (String param : params) {
                    if (!param.startsWith(CODE_QUERY_SPLITCHAR) && param.indexOf(STATE_QUERY_SPLITCHAR) == -1
                            && param.indexOf(APPID_QUERY_SPLITCHAR) == -1) {
                        queryString += param + HTTP_QUERY_SPLITCHAR;
                    }
                }
                if (StringUtils.isNotBlank(queryString)) {
                    queryString = queryString.substring(0, queryString.length() - 1);
                }
            }
            if (StringUtils.isNotBlank(queryString)) {
                url += HTTP_PATHQUERY_SPLITCHAR + queryString;
            }
        }
        return url;
    }

    /**
     * @param request
     * @param domain
     * @param appIdPrefix
     * @return
     */
    public static String getCurrentPath(HttpServletRequest request, String domain, String appId, Boolean appIdPrefix) {
        String url;
        if (appIdPrefix) {
            url = getRequestScheme(request) + HTTP_URLHEADER_SPLITCHAR + appId + "." + domain + request.getContextPath() + request.getServletPath();
        } else {
            url = getRequestScheme(request) + HTTP_URLHEADER_SPLITCHAR + domain + request.getContextPath() + request.getServletPath();
        }

        if (request.getQueryString() != null) {
            String queryString = request.getQueryString();
            if (queryString.indexOf(CODE_QUERY_SPLITCHAR) != -1) {
                String[] params = queryString.split(HTTP_QUERY_SPLITCHAR);
                queryString = "";
                for (String param : params) {
                    if (!param.startsWith(CODE_QUERY_SPLITCHAR) && param.indexOf(STATE_QUERY_SPLITCHAR) == -1
                            && param.indexOf(APPID_QUERY_SPLITCHAR) == -1) {
                        queryString += param + HTTP_QUERY_SPLITCHAR;
                    }
                }
                if (StringUtils.isNotBlank(queryString)) {
                    queryString = queryString.substring(0, queryString.length() - 1);
                }
            }
            if (StringUtils.isNotBlank(queryString)) {
                url += HTTP_PATHQUERY_SPLITCHAR + queryString;
            }
        }
        return url;
    }

    /**
     * @param request
     * @return
     * @Title getRequestScheme
     * @Class StringUtils
     * @Description 获取http scheme 如果存在x-forwarded-proto则说明协议从这里获取,否则从scheme获取
     * @author qinshijiang@telincn.com
     * @Date 2017年11月6日
     */
    public static String getRequestScheme(HttpServletRequest request) {
        String scheme = request.getScheme();
        if (isNotBlank(request.getHeader(X_FORWARDED_PROTO))) {
            scheme = request.getHeader(X_FORWARDED_PROTO);
        }
        return scheme;
    }

    /**
     * @param url
     * @param param
     * @param paramValue
     * @return
     * @Title urlAddQueryString
     * @Class StringUtils
     * @Description url拼接get参数
     * @author qinshijiang@telincn.com
     * @Date 2017年11月6日
     */
    public static String urlAddQueryString(String url, String param, String paramValue) {
        if (url.indexOf(HTTP_PATHQUERY_SPLITCHAR) != -1) {
            url += HTTP_QUERY_SPLITCHAR + param + HTTP_QYUERY_PARAM_SPLITCHAR + paramValue;
        } else {
            url += HTTP_PATHQUERY_SPLITCHAR + param + HTTP_QYUERY_PARAM_SPLITCHAR + paramValue;
        }
        return url;
    }

    /**
     * @param request
     * @return
     * @Title getCurrentPath
     * @Class StringUtils
     * @Description 获取当前连接路径
     * @author qinshijiang@telincn.com
     * @Date 2016年9月24日
     */
    public static String getCurrentPath(HttpServletRequest request) {
        String url = request.getContextPath() + request.getServletPath();
        if (request.getQueryString() != null) {
            String queryString = request.getQueryString();
            if (queryString.indexOf(CODE_QUERY_SPLITCHAR) != -1) {
                String[] params = queryString.split(HTTP_QUERY_SPLITCHAR);
                queryString = "";
                for (String param : params) {
                    if (param.indexOf(CODE_QUERY_SPLITCHAR) == -1 && param.indexOf(STATE_QUERY_SPLITCHAR) == -1) {
                        queryString += param + HTTP_QUERY_SPLITCHAR;
                    }
                }
                if (StringUtils.isNotBlank(queryString)) {
                    queryString = queryString.substring(0, queryString.length() - 1);
                }
            }
            if (StringUtils.isNotBlank(queryString)) {
                url += HTTP_PATHQUERY_SPLITCHAR + queryString;
            }
        }
        return url;
    }

    /**
     * @param date
     * @param pattern
     * @return
     * @Title dateToString
     * @Class StringUtils
     * @Description 日期格式化
     * @author qinshijiang@telincn.com
     * @Date 2016年7月30日
     */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * @param appId
     * @param url
     * @param scope
     * @param state
     * @return
     * @Title getWeixinUrl
     * @Class StringUtils
     * @Description 获取微信oauth链接url
     * @author qinshijiang@telincn.com
     * @Date 2016年9月24日
     */
    public static String getWeixinUrl(String appId, String url, String scope, String state) {
        try {
            byte[] urlbyte = URLCodec.encodeUrl(null, url.getBytes(UTF_8));
            url = new String(urlbyte);
        } catch (Exception e) {
            logger.error(URLENCODE_ERROR_LOG, e);
        }
        return String.format(WXAUTH_URL_FORMAT, appId, url, scope, state);
    }

    /**
     * @param appId
     * @param url
     * @param scope
     * @param state
     * @return
     * @Title getQyhUrl
     * @Class StringUtils
     * @Description 获取企业号Oauth连接URL
     * @author qinshijiang@telincn.com
     * @Date 2017年6月26日
     */
    public static String getQyhUrl(String appId, String url, String scope, String state, String agentid) {
        try {
            byte[] urlbyte = URLCodec.encodeUrl(null, url.getBytes(UTF_8));
            url = new String(urlbyte);
        } catch (Exception e) {
            logger.error(URLENCODE_ERROR_LOG, e);
        }
        return String.format(QYHAUTH_URL_FORMAT, appId, url, scope, agentid, state);
    }

    /**
     * @param componentAppid
     * @param appId
     * @param url
     * @param scope
     * @param state
     * @return
     * @Title getPlatformUrl
     * @Class StringUtils
     * @Description 获取第三方平台oauth连接URL
     * @author qinshijiang@telincn.com
     * @Date 2017年9月27日
     */
    public static String getPlatformUrl(String componentAppid, String appId, String url, String scope, String state) {
        try {
            byte[] urlbyte = URLCodec.encodeUrl(null, url.getBytes(UTF_8));
            url = new String(urlbyte);
        } catch (Exception e) {
            logger.error(URLENCODE_ERROR_LOG, e);
        }
        return String.format(COMPONENTAUTH_URL_FORMAT, appId, url, scope, state, componentAppid);
    }

    /**
     * @param param
     * @return
     * @Title UrlEncode
     * @Class StringUtils
     * @Description url encode Utf-8
     * @author qinshijiang@telincn.com
     * @Date 2016年10月20日
     */
    public static String UrlEncode(String param) {
        try {
            byte[] urlbyte = URLCodec.encodeUrl(null, param.getBytes(UTF_8));
            param = new String(urlbyte);
        } catch (Exception e) {
            logger.error(URLENCODE_ERROR_LOG, e);
        }
        return param;
    }

    /**
     * @param appId
     * @param request
     * @param scope
     * @param state
     * @param domain
     * @return
     * @Title getWeixinUrl
     * @Class StringUtils
     * @Description 获取微信oauth链接url
     * @author qinshijiang@telincn.com
     * @Date 2016年9月24日
     */
    public static String getWeixinUrl(String appId, HttpServletRequest request, String scope, String state, String domain) {
        if (isBlank(scope)) {
            scope = WXAUTH_DEFAULT_SCOPE;
        }
        if (isBlank(state)) {
            state = WXAUTH_DEFAULT_STATE;
        }
        return getWeixinUrl(appId, getCurrentPath(request, domain), scope, state);
    }

    /**
     * @param appId
     * @param request
     * @param scope
     * @param state
     * @param domain
     * @return
     * @Title getQyhUrl
     * @Class StringUtils
     * @Description 获取企业号oauth Url
     * @author qinshijiang@telincn.com
     * @Date 2017年6月26日
     */
    public static String getQyhUrl(String appId, String agentId, HttpServletRequest request, String scope, String state, String domain) {
        if (isBlank(scope)) {
            scope = WXAUTH_DEFAULT_SCOPE;
        }
        if (isBlank(state)) {
            state = WXAUTH_DEFAULT_STATE;
        }
        return getQyhUrl(appId, getCurrentPath(request, domain), scope, state, agentId);
    }

    /**
     * @param componentAppid
     * @param appId
     * @param request
     * @param scope
     * @param state
     * @param domain
     * @return
     * @Title getPlatformUrl
     * @Class StringUtils
     * @Description 获取第三方平台oauth url
     * @author qinshijiang@telincn.com
     * @Date 2017年9月27日
     */
    public static String getPlatformUrl(String componentAppid, String appId, HttpServletRequest request, String scope, String state, String domain) {
        if (isBlank(scope)) {
            scope = WXAUTH_DEFAULT_SCOPE;
        }
        if (isBlank(state)) {
            state = WXAUTH_DEFAULT_STATE;
        }
        return getPlatformUrl(componentAppid, appId, getCurrentPath(request, domain), scope, state);
    }

    /**
     * @param componentAppid
     * @param appId
     * @param request
     * @param scope
     * @param state
     * @param domain
     * @param appIdPrefix
     * @return
     */
    public static String getPlatformUrl(String componentAppid, String appId, HttpServletRequest request, String scope, String state, String domain, Boolean appIdPrefix) {
        if (isBlank(scope)) {
            scope = WXAUTH_DEFAULT_SCOPE;
        }
        if (isBlank(state)) {
            state = WXAUTH_DEFAULT_STATE;
        }
        return getPlatformUrl(componentAppid, appId, getCurrentPath(request, domain, appId, appIdPrefix), scope, state);
    }

    /**
     * createRandomString:(根据提供的sourceCharGroup,从中生成随机字符串). <br/>
     * <p>
     * 此方法产生的随机数可能出现重复的情况，推荐使用@link createRandomString()
     *
     * @param sourceCharGroup
     * @return
     */
    public static String createRandomString(String sourceCharGroup, int length) {
        String nonStr = "";
        String source = sourceCharGroup;
        for (int i = 0; i < length; i++) {
            int begin = (int) (Math.random() * (source.length() - 1));
            nonStr += source.substring(begin, begin + 1);
        }
        return nonStr;
    }

    public static String createRandomString(int length) {
        String nonStr = "";
        String source = RANDOM_STRING_SOURCE;
        for (int i = 0; i < length; i++) {
            int begin = (int) (Math.random() * (source.length() - 1));
            nonStr += source.substring(begin, begin + 1);
        }
        return nonStr;
    }

    /**
     * 生成微信JS-SDK的nonce_str字段，32位随机数
     *
     * @return
     */
    public static String createRandomString() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
        return uuid;
    }

    public static String createCodeId() {
        String codeId = String.valueOf(System.currentTimeMillis());
        return codeId.substring(3, codeId.length());
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    public static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    public static String byteToHexStr(byte mByte) {
        char[] Digit = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    /**
     * @Title：doubleToString
     * @Class：StringUtils
     * @return：String
     * @params:@param d
     * @params:@return
     * @Description:将科学技术法的DOUBLE类型整数转换成为10进制格式
     * @author : qinshijiang@telincn.com @Date：2013-5-13
     */
    public static String doubleToString(Double d) {
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT_CHAR);// 最多保留几位小数，就用几个#，最少位就用0来确定
        return df.format(d);
    }

    /**
     * 生成微信JS-SDK的nonce_str字段，32位随机数
     *
     * @return
     */
    public static String createNonStr() {
        String nonStr = "";
        String source = RANDOM_STRING_SOURCE;
        for (int i = 0; i < 16; i++) {
            int begin = (int) (Math.random() * (source.length() - 1));
            nonStr += source.substring(begin, begin + 1);
        }
        return nonStr;
    }

    /**
     * @param url
     * @return
     * @Title urlEncode
     * @Class StringUtils
     * @Description 基于UTF-8的urlencode
     * @author qinshijiang@telincn.com
     * @Date 2016年9月29日
     */
    public static String urlEncode(String url) {
        if (isNotBlank(url)) {
            try {
                byte[] urlbyte = URLCodec.encodeUrl(null, url.getBytes(UTF_8));
                url = new String(urlbyte);
            } catch (Exception e) {
                logger.error(URLENCODE_ERROR_LOG, e);
            }
        }
        return url;
    }

    /**
     * @param param
     * @return
     * @Title urlDecode
     * @Class StringUtils
     * @Description 基于UTF-8的urldecode
     * @author qinshijiang@telincn.com
     * @Date 2016年10月27日
     */
    public static String urlDecode(String param) {
        if (isNotBlank(param)) {
            try {
                byte[] parambyte = URLCodec.decodeUrl(param.getBytes(UTF_8));
                param = new String(parambyte);
            } catch (Exception e) {
                logger.error(URLDECODE_ERROR_LOG, e);
            }
        }
        return param;
    }

    /**
     * @param str
     * @return
     * @Title isNumeric
     * @Class StringUtils
     * @Description 判断是否1—9的整数
     * @author yujialin@hzchuangbo.com
     * @Date 2016年10月19日
     */
    public static boolean isNumeric(String str) {
        return matchStr(str, NUMBER_NOTZERO_REG_EXPRESS);
    }

    public static boolean isNumber(String str){
        return matchStr(str, "\\d*");
    }

    public static boolean matchStr(String str, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * @param request
     * @return
     * @throws Exception
     * @Title getIpAddr
     * @Class StringUtils
     * @Description 获取用户IP
     * @author qinshijiang@telincn.com
     * @Date 2016年11月9日
     */
    public static String getIpAddr(HttpServletRequest request) {

        //如果多级代理，x-real-ip是代理机器的ip
        if (logger.isDebugEnabled()) {
            logger.info("x-forwarded-for = {},x-real-ip = {},remote-addr = {}",
                    request.getHeader("x-forwarded-for"),
                    request.getHeader("x-real-ip"),
                    request.getRemoteAddr());
        }

        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                if (logger.isDebugEnabled()) {
                    logger.info("x-forwarded-for return id = [{}]", ip.substring(0, index));
                }
                return ip.substring(0, index);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.info("x-forwarded-for return id = [{}]", ip);
                }
                return ip;
            }
        }
        ip = request.getHeader("x-real-ip");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            if (logger.isDebugEnabled()) {
                logger.info("x-real-ip return id = [{}]", ip);
            }
            return ip;
        }

        ip = request.getRemoteAddr();
        if (logger.isDebugEnabled()) {
            logger.info("remoteAddr = [{}]", ip);
            logger.info("remoteAddr return id = [{}]", ip);
        }
        return ip;
    }

    /**
     * @param phone
     * @return
     * @Title getYcPhone
     * @Class StringUtils
     * @Description 将手机号中间四位隐藏
     * @author yujialin@hzchuangbo.com
     * @Date 2016年11月15日
     */
    public static String getYcPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return null;
        }
        String str = phone.substring(0, 3);
        String str2 = phone.substring(7);
        return str + PHONE_HIDDEN_CHAR + str2;
    }

    /**
     * 姓名脱敏处理
     *
     * @param name
     * @return
     */
    public static String getYcName(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        char[] nameChars = name.toCharArray();
        if (name.length() == 2) {
            nameChars[1] = '*';
        } else {
            int num = nameChars.length - 2;
            for (int i = 1; i <= num; i++) {
                nameChars[i] = '*';
            }
        }
        return new String(nameChars);

    }

    /**
     * @param phone
     * @return
     * @Title checkPhone
     * @Class StringUtils
     * @Description 判断字符串是否为11位数字
     * @author yujialin@hzchuangbo.com
     * @Date 2017年8月28日
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile(NUMBER_REG_EXPRESS);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * @param str
     * @return
     * @Title replaceAllBlank
     * @Class StringUtils
     * @Description 清空字符串内的换行符, 空格\n 回车(\u000a) \t 水平制表符(\u0009) \s 空格(\u0008) \r 换行(\u000d)
     * @author qinshijiang@telincn.com
     * @Date 2017年11月10日
     */
    public static String replaceAllBlank(String str) {
        String s = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            s = m.replaceAll("");
        }
        return s;
    }

    /**
     * 判断Map是否为空
     *
     * @param map
     * @return
     */
    public static <K, V> boolean isEmptyMap(Map<K, V> map) {
        return (map == null || map.size() < 1);
    }

    /**
     * 判断数组是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmptyArray(Object[] obj) {
        return (obj == null || obj.length < 1);
    }

    /**
     * 是否是中国身份证
     * @param idNumber
     * @return
     */
    public static boolean isIDNumber(String idNumber) {
        if (idNumber == null || "".equals(idNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾


        boolean matches = idNumber.matches(regularExpression);

        //判断第18位校验值
        if (matches) {

            if (idNumber.length() == 18) {
                try {
                    char[] charArray = idNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        logger.debug("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }

                } catch (Exception e) {
                    logger.error("异常:" + idNumber, e);
                    return false;
                }
            }

        }
        return matches;
    }

    /**
     * 当前时间点离一天结束剩余秒数
     * @param timeStr yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static Integer getRemainSecondsOneDay(String timeStr) {
        Integer seconds= 0;
        try {
            Calendar c=Calendar.getInstance();
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr));
            seconds=(int)((c.getTimeInMillis()-System.currentTimeMillis())/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return seconds;
    }

    /**
     * 在数字前补0
     * @param str
     * @param len 若数字位数小于该参数，则补全0至len为止
     * @return
     */
    public static String addZeroForNum(String str, int len){
        return String.format("%0" + len + "d", Long.parseLong(str));
    }

    /**
     * 截取数字字符串末尾，若数字长度不够，则用0补全
     * @param str
     * @param len
     * @return
     */
    public static String subAndAddZeroForNum(String str, int len){
        if (StringUtils.isBlank(str)){
            return addZeroForNum("0", len);
        }
        if (str.length() < len){
            return addZeroForNum(str, len);
        }
        return str.substring(str.length() - len);
    }
}
