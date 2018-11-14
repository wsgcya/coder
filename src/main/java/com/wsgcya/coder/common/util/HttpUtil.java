package com.wsgcya.coder.common.util;

import com.alibaba.fastjson.JSONObject;
import com.sun.corba.se.impl.util.Version;
import com.wsgcya.coder.common.util.wechat.HttpClientFactory;
import com.wsgcya.coder.common.util.wechat.JsonResponseHandler;
import com.wsgcya.coder.common.util.wechat.LocalResponseHandler;
import com.wsgcya.coder.common.util.wechat.XmlResponseHandler;
import org.apache.http.*;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

public class HttpUtil {
    protected final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static ContentType TYPE_PLAIN = ContentType.create("text/plain", Consts.UTF_8);
    private static ResultErrorHandler resultErrorHandler;
    private static int timeout = 8000; // 超时
    private static int retryExecutionCount = 2; // 重试次数
    private static Map<String, CloseableHttpClient> httpClient_mchKeyStore = new HashMap<String, CloseableHttpClient>();
    protected static final Header userAgentHeader = new BasicHeader(HttpHeaders.USER_AGENT, "weixin-popular sdk java v" + Version.VERSION);

    protected static CloseableHttpClient httpClient = HttpClientFactory.createHttpClient(100, 10, timeout, retryExecutionCount);

    /**
     * @param useUrl
     * @param accessToken
     * @param postData
     * @param timeArgs    请求超时时间,读取超时时间设置
     * @return
     * @Title httpReq
     * @Class HttpUtil
     * @Description http 请求
     * @author qinshijiang@telincn.com
     * @Date 2016年7月30日
     */
    public static String httpReq(String useUrl, String accessToken, String postData, int... timeArgs) {
        String result = "", unknown = "";
        StringBuilder sb = new StringBuilder();
        String openUrl = useUrl;
        if (StringUtils.isNotBlank(accessToken)) {
            openUrl = useUrl + accessToken;
        }
        HttpURLConnection httpUrlConnection = null;
        long begin = System.currentTimeMillis();
        try {
            URL url = new URL(openUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            int connecTimeout = 3000;// 连接默认超时3秒
            int readTimeout = 5000;// 读取默认超时5秒
            if (timeArgs != null) {
                int argLength = timeArgs.length;
                if (argLength > 0) {
                    connecTimeout = timeArgs[0];
                }
                if (timeArgs.length > 1) {
                    readTimeout = timeArgs[1];
                }
            }
            httpUrlConnection.setConnectTimeout(connecTimeout);
            httpUrlConnection.setReadTimeout(readTimeout);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.connect();
            if (StringUtils.isNotBlank(postData)) {
                DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
                out.write(postData.getBytes("utf-8"));
                out.flush();
                out.close();
            }
            // 获取动态链接响应的状态码
            int HttpResult = httpUrlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStreamReader br = new InputStreamReader(httpUrlConnection.getInputStream());
                int data = 0;
                while ((data = br.read()) != -1) {
                    sb.append((char) data);
                }
                br.close();
                result = sb.toString();
            } else {
                unknown = httpUrlConnection.getResponseMessage();
            }
        } catch (Exception e) {
            logger.error("request useUrl:" + openUrl + ",postData:" + postData + ",error ", e);
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        if (!"".equals(unknown)) {
            logger.error("response error" + unknown);
        }
        long end = System.currentTimeMillis();
        logger.info("url:" + openUrl + ",req:" + postData + ",res:" + sb.toString() + ",unknown:" + unknown + ",time:" + (end - begin));
        return result;
    }

    /**
     * @param useUrl
     * @param map
     * @param timeArgs
     * @return
     * @Title httpReq
     * @Class HttpUtil
     * @Description 优化http请求传参，支持map，与sentPostParams可等价替换
     * @author yujialin@hzchuangbo.com
     * @Date 2017年1月19日
     */
    public static String httpReq(String useUrl, Map<String, String> map, int... timeArgs) {
        String postdata = "";
        if (map != null) {
            for (Entry<String, String> entry : map.entrySet()) {
                postdata += "&" + entry.getKey() + "=" + entry.getValue();
            }
            postdata = postdata.substring(1);
        }
        return httpReq(useUrl, null, postdata, timeArgs);
    }

    /**
     * @param useUrl
     * @param accessToken
     * @param postData
     * @param proxyisavalid
     * @param proxyIp
     * @param proxyPort
     * @param timeArgs
     * @return
     * @Title httpReqWithProxy
     * @Class HttpUtil
     * @Description http请求可以使用代理
     * @author qinshijiang@telincn.com
     * @Date 2017年1月11日
     */
    public static String httpReqWithProxy(String useUrl, String accessToken, String postData, boolean proxyisavalid, String proxyIp,
                                          Integer proxyPort, int... timeArgs) {
        String result = "", unknown = "";
        StringBuilder sb = new StringBuilder();
        String openUrl = useUrl;
        if (StringUtils.isNotBlank(accessToken)) {
            openUrl = useUrl + accessToken;
        }
        HttpURLConnection httpUrlConnection = null;
        long begin = System.currentTimeMillis();
        try {
            URL url = new URL(openUrl);
            if (proxyisavalid) {
                httpUrlConnection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort)));
            } else {
                httpUrlConnection = (HttpURLConnection) url.openConnection();
            }
            int connecTimeout = 3000;// 连接默认超时3秒
            int readTimeout = 5000;// 读取默认超时5秒
            if (timeArgs != null) {
                int argLength = timeArgs.length;
                if (argLength > 0) {
                    connecTimeout = timeArgs[0];
                }
                if (timeArgs.length > 1) {
                    readTimeout = timeArgs[1];
                }
            }
            httpUrlConnection.setConnectTimeout(connecTimeout);
            httpUrlConnection.setReadTimeout(readTimeout);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.connect();
            if (StringUtils.isNotBlank(postData)) {
                DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
                out.write(postData.getBytes("utf-8"));
                out.flush();
                out.close();
            }
            // 获取动态链接响应的状态码
            int HttpResult = httpUrlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStreamReader br = new InputStreamReader(httpUrlConnection.getInputStream());
                int data = 0;
                while ((data = br.read()) != -1) {
                    sb.append((char) data);
                }
                br.close();
                result = sb.toString();
            } else {
                unknown = httpUrlConnection.getResponseMessage();
            }
        } catch (Exception e) {
            logger.error("request useUrl:" + openUrl + ",postData:" + postData + ",error ", e);
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        if (!"".equals(unknown)) {
            logger.error("request={}, response error:{}", openUrl, unknown);
        }
        long end = System.currentTimeMillis();
        logger.info("url:" + openUrl + ",req:" + postData + ",res:" + sb.toString() + ",unknown:" + unknown + ",time:" + (end - begin));
        return result;
    }

    /**
     * @param useUrl
     * @param postData
     * @param proxyisavalid
     * @param proxyIp
     * @param proxyPort
     * @return
     * @Title httpReqWithProxy
     * @Class HttpUtil
     * @Description http请求可以使用代理
     * @author qinshijiang@telincn.com
     * @Date 2017年1月11日
     */
    public static String httpReqWithProxy(String useUrl, String postData, boolean proxyisavalid, String proxyIp, Integer proxyPort) {
        return httpReqWithProxy(useUrl, null, postData, proxyisavalid, proxyIp, proxyPort, 1000, 5000);
    }

    /**
     * @param useUrl
     * @param proxyisavalid
     * @param proxyIp
     * @param proxyPort
     * @return
     * @Title httpReqWithProxy
     * @Class HttpUtil
     * @Description http请求可以使用代理
     * @author qinshijiang@telincn.com
     * @Date 2017年1月11日
     */
    public static String httpReqWithProxy(String useUrl, boolean proxyisavalid, String proxyIp, Integer proxyPort) {
        return httpReqWithProxy(useUrl, null, null, proxyisavalid, proxyIp, proxyPort, 1000, 5000);
    }

    public static String httpSend(String strUrl, String postData, Integer... timeArgs) {
        String result = "", unknown = "";
        int HttpResult = 0;
        StringBuilder sb = new StringBuilder();
        HttpURLConnection httpUrlConnection = null;
        try {
            URL url = new URL(strUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            int connecTimeout = 60000;// 连接默认超时60秒
            int readTimeout = 60000;// 读取默认超时60秒
            if (timeArgs != null) {
                int argLength = timeArgs.length;
                if (argLength > 0) {
                    connecTimeout = timeArgs[0];
                }
                if (timeArgs.length > 1) {
                    readTimeout = timeArgs[1];
                }
            }
            httpUrlConnection.setConnectTimeout(connecTimeout);
            httpUrlConnection.setReadTimeout(readTimeout);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            // 如果对端socket 无法正常关闭
            httpUrlConnection.setRequestProperty("Connection", "close");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.connect();
            DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
            out.write(postData == null ? null : postData.getBytes());
            out.flush();
            out.close();
            // 获取动态链接响应的状态码
            HttpResult = httpUrlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStreamReader br = new InputStreamReader(httpUrlConnection.getInputStream());
                int data = 0;
                while ((data = br.read()) != -1) {
                    sb.append((char) data);
                }
                br.close();
                result = sb.toString();
            } else {
                unknown = httpUrlConnection.getResponseMessage();
            }
        } catch (Exception e) {
            logger.error("request error", e);
        }
        if (!"".equals(unknown)) {
            logger.error("response error " + unknown + HttpResult + strUrl);
        }
        // logger.info("请求结果:" + sb.toString() + ";unknown:" + unknown);
        if ("".equals(sb.toString())) {
            result = "";
        }
        if (httpUrlConnection != null) {
            httpUrlConnection.disconnect();
        }
        return result;
    }

    /**
     * http请求
     *
     * @param strUrl
     * @param postData
     * @param soapFlag 是否设置SOAPAction true设置 false不设置
     * @param timeArgs 可变参数 第一个数为连接超时时长 第二个为读取超时时长
     * @return
     * @Title httpSend
     * @Description
     */
    public static String httpSend(String strUrl, String postData, boolean soapFlag, Integer... timeArgs) {
        String result = "", unknown = "";
        int HttpResult = 0;
        StringBuilder sb = new StringBuilder();
        HttpURLConnection httpUrlConnection = null;
        try {
            URL url = new URL(strUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            int connecTimeout = 60000;// 连接默认超时60秒
            int readTimeout = 60000;// 读取默认超时60秒
            if (timeArgs != null) {
                int argLength = timeArgs.length;
                if (argLength > 0) {
                    connecTimeout = timeArgs[0];
                }
                if (timeArgs.length > 1) {
                    readTimeout = timeArgs[1];
                }
            }
            httpUrlConnection.setConnectTimeout(connecTimeout);
            httpUrlConnection.setReadTimeout(readTimeout);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("Content-Type", "text/xml;charset=\"utf-8\"");
            httpUrlConnection.setRequestProperty("Accept", "text/xml");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Pragma", "no-cache");
            if (soapFlag) {
                httpUrlConnection.setRequestProperty("SOAPAction", "");
            }
            httpUrlConnection.connect();
            DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
            out.write(postData == null ? null : postData.getBytes());
            out.flush();
            out.close();
            // 获取动态链接响应的状态码
            HttpResult = httpUrlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStreamReader br = new InputStreamReader(httpUrlConnection.getInputStream());
                int data = 0;
                while ((data = br.read()) != -1) {
                    sb.append((char) data);
                }
                br.close();
                result = sb.toString();
            } else {
                unknown = httpUrlConnection.getResponseMessage();
            }
            httpUrlConnection.disconnect();
        } catch (Exception e) {
            logger.error("request error", e);
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        if (!"".equals(unknown)) {
            logger.error("response error " + unknown + HttpResult + strUrl);
        }
        if ("".equals(sb.toString())) {
            result = "";
        }
        return result;
    }

    public static String httpSendJson(String strUrl, String postData, boolean soapFlag, Integer... timeArgs) {
        String result = "", unknown = "";
        int HttpResult = 0;
        StringBuilder sb = new StringBuilder();
        HttpURLConnection httpUrlConnection = null;
        try {
            URL url = new URL(strUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            int connecTimeout = 60000;// 连接默认超时60秒
            int readTimeout = 60000;// 读取默认超时60秒
            if (timeArgs != null) {
                int argLength = timeArgs.length;
                if (argLength > 0) {
                    connecTimeout = timeArgs[0];
                }
                if (timeArgs.length > 1) {
                    readTimeout = timeArgs[1];
                }
            }
            httpUrlConnection.setConnectTimeout(connecTimeout);
            httpUrlConnection.setReadTimeout(readTimeout);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("Content-Type", "application/json;charset=\"utf-8\"");
            httpUrlConnection.setRequestProperty("Accept", "application/json");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Pragma", "no-cache");
            if (soapFlag) {
                httpUrlConnection.setRequestProperty("SOAPAction", "");
            }
            httpUrlConnection.connect();
            DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
            out.write(postData == null ? null : postData.getBytes());
            out.flush();
            out.close();
            // 获取动态链接响应的状态码
            HttpResult = httpUrlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStreamReader br = new InputStreamReader(httpUrlConnection.getInputStream());
                int data = 0;
                while ((data = br.read()) != -1) {
                    sb.append((char) data);
                }
                br.close();
                result = sb.toString();
            } else {
                unknown = httpUrlConnection.getResponseMessage();
            }
            httpUrlConnection.disconnect();
        } catch (Exception e) {
            logger.error("request error", e);
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        if (!"".equals(unknown)) {
            logger.error("response error " + unknown + HttpResult + strUrl);
        }
        if ("".equals(sb.toString())) {
            result = "";
        }
        return result;
    }

    public static String httpSendJson(String strUrl, String postData, Integer... timeArgs) {
        String result = "", unknown = "";
        int HttpResult = 0;
        StringBuilder sb = new StringBuilder();
        HttpURLConnection httpUrlConnection = null;
        try {
            URL url = new URL(strUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            int connecTimeout = 60000;// 连接默认超时60秒
            int readTimeout = 60000;// 读取默认超时60秒
            if (timeArgs != null) {
                int argLength = timeArgs.length;
                if (argLength > 0) {
                    connecTimeout = timeArgs[0];
                }
                if (timeArgs.length > 1) {
                    readTimeout = timeArgs[1];
                }
            }
            httpUrlConnection.setConnectTimeout(connecTimeout);
            httpUrlConnection.setReadTimeout(readTimeout);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("Content-Type", "application/json;charset=\"utf-8\"");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Pragma", "no-cache");
            httpUrlConnection.connect();
            DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
            out.write(postData == null ? null : postData.getBytes());
            out.flush();
            out.close();
            // 获取动态链接响应的状态码
            HttpResult = httpUrlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStreamReader br = new InputStreamReader(httpUrlConnection.getInputStream());
                int data = 0;
                while ((data = br.read()) != -1) {
                    sb.append((char) data);
                }
                br.close();
                result = sb.toString();
            } else {
                unknown = httpUrlConnection.getResponseMessage();
            }
            httpUrlConnection.disconnect();
        } catch (Exception e) {
            logger.error("request error", e);
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        if (!"".equals(unknown)) {
            logger.error("response error " + unknown + HttpResult + strUrl);
        }
        if ("".equals(sb.toString())) {
            result = "";
        }
        return result;
    }

    /**
     * @param strUrl
     * @param timeArgs
     * @return String
     * @Title: httpGet
     * @Class HttpUtil
     * @Description: 部分接口会将请求结果放在重定向后的地址参数中进行回调， 该方法直接获取重定向后的地址截取参数并封装
     * @author caizhiyong@hzchuangbo.com
     * @Date 2017年12月19 周二
     */
    public static String getRedirectParams(String strUrl, Integer... timeArgs) {
        String result = "", unknown = "ok";
        int HttpResult = 0;
        HttpURLConnection httpUrlConnection = null;
        JSONObject redirectRes = new JSONObject();
        try {
            URL url = new URL(strUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            int connecTimeout = 10000;// 连接默认超时10秒
            int readTimeout = 40000;// 读取默认超时40秒
            if (timeArgs != null) {
                int argLength = timeArgs.length;
                if (argLength > 0) {
                    connecTimeout = timeArgs[0];
                }
                if (timeArgs.length > 1) {
                    readTimeout = timeArgs[1];
                }
            }
            httpUrlConnection.setConnectTimeout(connecTimeout);
            httpUrlConnection.setReadTimeout(readTimeout);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Pragma", "no-cache");
            // // 如果对端socket 无法正常关闭
            httpUrlConnection.setRequestProperty("Connection", "close");
            httpUrlConnection.connect();
            // 获取动态链接响应的状态码
            HttpResult = httpUrlConnection.getResponseCode();
            // 直接获取重定向后的地址带着的参数，并组合成json格式
            String file = httpUrlConnection.getURL().getFile();
            String params = file.substring(file.indexOf("?") + 1, file.length());
            for (String parameter : params.split("&")) {
                String key = parameter.substring(0, parameter.indexOf("="));
                String value = parameter.substring(parameter.indexOf("=") + 1);
                redirectRes.put(key, value);
            }
            unknown = httpUrlConnection.getResponseMessage();
            httpUrlConnection.disconnect();
        } catch (Exception e) {
            logger.error("req redirect error|" + redirectRes, e);
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        if (!"ok".equals(unknown)) {
            logger.error("response error |{}|{}|{}", unknown, HttpResult, strUrl);
        }
        logger.info("请求结果:{}|unknown:{}", redirectRes, unknown);
        if (redirectRes.isEmpty()) {
            result = "";
        } else {
            result = redirectRes.toJSONString();
        }
        return result;
    }

    public static String soapSend(String postData, String strUrl) {
        String result = "", unknown = "";
        StringBuilder sb = new StringBuilder();
        HttpURLConnection httpUrlConnection = null;
        try {
            URL url = new URL(strUrl);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(60000);
            httpUrlConnection.setReadTimeout(100000);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpUrlConnection.setRequestProperty("Accept", "text/xml");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Pragma", "no-cache");
            httpUrlConnection.setRequestProperty("Content-Length", postData.length() + "");
            httpUrlConnection.setRequestProperty("SOAPAction", "http://tempuri.org/giveflow");
            httpUrlConnection.connect();
            DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
            out.write(postData == null ? "".getBytes() : postData.getBytes("utf-8"));
            out.flush();
            out.close();
            // 获取动态链接响应的状态码
            int HttpResult = httpUrlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStreamReader br = new InputStreamReader(httpUrlConnection.getInputStream());
                int data = 0;
                while ((data = br.read()) != -1) {
                    sb.append((char) data);
                }
                br.close();
                result = sb.toString();
            } else {
                unknown = httpUrlConnection.getResponseMessage();
            }
        } catch (Exception e) {
            logger.error("request error", e);
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        if (!"".equals(unknown)) {
            logger.error("response error" + unknown);
        }
        if ("".equals(sb.toString())) {
            result = "";
        }
        return result;
    }

    /**
     * HTTP访问，使用POST方法提交参数
     *
     * @param url_
     * @param postParams
     * @return
     * @throws Exception
     * @Title sentPostParams
     * @Class HttpUtil
     * @Description
     * @author zhoushifeng@telincn.com
     * @Date 2016年8月28日
     */
    public static String sentPostParams(String url_, Map<String, String> postParams) throws Exception {
        return httpReq(url_, postParams, 3000, 20000);
        /**
         * StringBuffer sb = new StringBuffer();
         *
         * // 设置超时时间为15秒 RequestConfig requestConfig =
         * RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000)
         * .build();
         *
         * CloseableHttpClient httpclient = HttpClients.custom().build();
         * HttpPost httpPost = null; CloseableHttpResponse response = null;
         * String result = ""; try { httpPost = new HttpPost(url_);
         * httpPost.setConfig(requestConfig); MultipartEntityBuilder builder =
         * MultipartEntityBuilder.create();
         * builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); if (postParams
         * != null) { for (Entry<String, String> e : postParams.entrySet()) {
         * builder.addTextBody(e.getKey(), e.getValue(), TYPE_PLAIN); }
         * httpPost.setEntity(builder.build()); } response =
         * httpclient.execute(httpPost); HttpEntity entity =
         * response.getEntity(); if (entity != null) { BufferedReader
         * bufferedReader = new BufferedReader(new
         * InputStreamReader(entity.getContent())); String text; while((text =
         * bufferedReader.readLine()) != null) { sb.append(text); } }
         * EntityUtils.consume(entity); result = sb.toString(); }catch
         * (Exception e) { logger.error("sentPostParams error ", e); }finally {
         * try { if (response != null) { response.close(); } if (httpPost !=
         * null) { httpPost.releaseConnection(); } httpclient.close(); }catch
         * (Exception e) { logger.error("sentPostParams close error ", e); } }
         * return result;
         **/
    }

    /**
     * @param actionUrl
     * @param params
     * @return
     * @Title ztpost
     * @Class HttpUtil
     * @Description 掌厅获取token用post
     * @author qinshijiang@telincn.com
     * @Date 2016年9月29日
     */
    public static String ztpost(String actionUrl, Map<String, String> params) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpPost httpPost = new HttpPost(actionUrl);
        httpPost.setConfig(requestConfig);
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (Entry<String, String> entry : params.entrySet()) {// 构建表单字段内容
            String key = entry.getKey();
            String value = entry.getValue();
            list.add(new BasicNameValuePair(key, value));
        }
        HttpResponse httpResponse;
        String responseString = "";
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            httpResponse = httpclient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                responseString = EntityUtils.toString(httpResponse.getEntity());
            } else if (httpResponse.getStatusLine().getStatusCode() == 404) {
                logger.warn("actionUrl:{} not found 404!" + actionUrl);
            }
        } catch (Exception e) {
            logger.error("zt post error", e);
        } finally {
            try {
                httpPost.releaseConnection();
                httpclient.close();
            } catch (IOException e) {
                logger.error("zt close post error", e);
            }
        }
        return responseString;
    }

    @Test
    public void test() {
        Map<String, String> postParams = new HashMap<String, String>();
        postParams.put("telphone", "18915977691");
        postParams.put("areaCode", "025");
        postParams.put("billMonth", "201609");
        try {
            // String res =
            // sentPostParams("http://192.168.1.35:8844/handler/?method=getMonthBill",
            // postParams);

            String res = httpReqWithProxy("http://192.168.1.35:8844/handler/?method=getMonthBill", null,
                    "telphone=18915977691&areaCode=025&billMonth=201609", false, null, null, 1000, 5000);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 日志记录
     *
     * @param request request
     * @return log request id
     */
    private static String loggerRequest(HttpUriRequest request) {
        String id = UUID.randomUUID().toString();
        if (logger.isInfoEnabled() || logger.isDebugEnabled()) {
            if (request instanceof HttpEntityEnclosingRequestBase) {
                HttpEntityEnclosingRequestBase request_base = (HttpEntityEnclosingRequestBase) request;
                HttpEntity entity = request_base.getEntity();
                String content = null;
                // MULTIPART_FORM_DATA 请求类型判断,文件类型不记录
                if (!entity.getContentType().toString().contains(ContentType.MULTIPART_FORM_DATA.getMimeType())) {
                    try {
                        content = EntityUtils.toString(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                }
                logger.info("URI[{}] {} {} ContentLength:{} Content:{}", id, request.getURI().toString(), entity.getContentType(), entity
                        .getContentLength(), content == null ? "multipart_form_data" : content);
            } else {
                logger.info("URI[{}] {}", id, request.getURI().toString());
            }
        }
        return id;
    }

    /**
     * 使用GET方式提交请求,适用于流量包受理
     *
     * @param url
     * @return
     */
    public static String send(String url) {

        StringBuilder sb = new StringBuilder();
        try {
            URL urlObj = new URL(url);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlObj.openConnection();
            httpUrlConnection.setConnectTimeout(100000);// 设置连接主机超时（单位：毫秒）
            httpUrlConnection.setReadTimeout(100000);// 设置从主机读取数据超时（单位：毫秒）
            httpUrlConnection.setRequestMethod("GET");
            // 如果对端socket 无法正常关闭
            httpUrlConnection.setRequestProperty("Connection", "close");
            httpUrlConnection.connect();
            int HttpResult = httpUrlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStreamReader br = new InputStreamReader(httpUrlConnection.getInputStream(), "utf-8");
                int data = 0;
                while ((data = br.read()) != -1) {
                    sb.append((char) data);
                }
            } else {
                logger.info("URL|" + HttpResult + "|" + httpUrlConnection.getResponseMessage());
            }
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        } catch (Exception e) {
            logger.error("request error| GET |", e);
        }
        return sb.toString();
    }

    /**
     * 将链接请求到的图片通过流输出到页面，解决跨域问题
     * @param imgUrl
     */
    public static void showImg(HttpServletResponse response, String imgUrl) throws Exception{
        OutputStream outputStream = response.getOutputStream();
        String url = imgUrl;
        URL urlObj = new URL(imgUrl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) urlObj.openConnection();
        httpUrlConnection.setConnectTimeout(100000);// 设置连接主机超时（单位：毫秒）
        httpUrlConnection.setReadTimeout(100000);// 设置从主机读取数据超时（单位：毫秒）
        httpUrlConnection.setRequestMethod("GET");
        // 如果对端socket 无法正常关闭
        httpUrlConnection.setRequestProperty("Connection", "close");
        httpUrlConnection.connect();
        int HttpResult = httpUrlConnection.getResponseCode();
        if (HttpResult != HttpURLConnection.HTTP_OK) {
            logger.info("URL: {}|status: {}|msg: {}", imgUrl, HttpResult, httpUrlConnection.getResponseMessage());
        }
        InputStream inputStream = httpUrlConnection.getInputStream();
        int data = -1;
        while ((data = inputStream.read()) != -1) {
            outputStream.write(data);
        }
        if (httpUrlConnection != null) {
            //disconnect实现了close流操作
            httpUrlConnection.disconnect();
        }
        response.setHeader("Cache-Control", "max-age=2592000");
        response.setHeader("Expires", "Thu, 19 Nov 2081 08:52:00 GMT");
        response.setHeader("Pragma", "public");
        response.setHeader("content-type", "image/jpg");
    }

    /**
     * 数据返回自动JSON对象解析
     *
     * @param request request
     * @param clazz   clazz
     * @return result
     */
    public static <T> T executeJsonResult(HttpUriRequest request, Class<T> clazz) {
        return execute(request, JsonResponseHandler.createResponseHandler(clazz));
    }

    public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) {
        String uriId = loggerRequest(request);
        if (responseHandler instanceof LocalResponseHandler) {
            LocalResponseHandler lrh = (LocalResponseHandler) responseHandler;
            lrh.setUriId(uriId);
        }
        try {
            T t = httpClient.execute(request, responseHandler, HttpClientContext.create());
            if (resultErrorHandler != null) {
                resultErrorHandler.doHandle(uriId, request, t);
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    public static CloseableHttpResponse execute(HttpUriRequest request) {
        loggerRequest(request);
        try {
            return httpClient.execute(request, HttpClientContext.create());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 数据返回自动XML对象解析
     *
     * @param request request
     * @param clazz   clazz
     * @return result
     */
    public static <T> T executeXmlResult(HttpUriRequest request, Class<T> clazz) {
        return execute(request, XmlResponseHandler.createResponseHandler(clazz));
    }

    /**
     * 数据返回自动XML对象解析
     *
     * @param request   request
     * @param clazz     clazz
     * @param sign_type 数据返回验证签名类型
     * @param key       数据返回验证签名key
     * @return result
     * @since 2.8.5
     */
    public static <T> T executeXmlResult(HttpUriRequest request, Class<T> clazz, String sign_type, String key) {
        return execute(request, XmlResponseHandler.createResponseHandler(clazz, sign_type, key));
    }

    /**
     * MCH keyStore 请求 数据返回自动XML对象解析
     *
     * @param mch_id  mch_id
     * @param request request
     * @param clazz   clazz
     * @return result
     */
    public static <T> T keyStoreExecuteXmlResult(String mch_id, HttpUriRequest request, Class<T> clazz) {
        return keyStoreExecuteXmlResult(mch_id, request, clazz, null, null);
    }

    /**
     * @param mch_id    mch_id
     * @param request   request
     * @param clazz     clazz
     * @param sign_type 数据返回验证签名类型
     * @param key       数据返回验证签名key
     * @return result
     * @since 2.8.5
     */
    public static <T> T keyStoreExecuteXmlResult(String mch_id, HttpUriRequest request, Class<T> clazz, String sign_type, String key) {
        return keyStoreExecute(mch_id, request, XmlResponseHandler.createResponseHandler(clazz, sign_type, key));
    }

    public static <T> T keyStoreExecute(String mch_id, HttpUriRequest request, ResponseHandler<T> responseHandler) {
        String uriId = loggerRequest(request);
        userAgent(request);
        if (responseHandler instanceof LocalResponseHandler) {
            LocalResponseHandler lrh = (LocalResponseHandler) responseHandler;
            lrh.setUriId(uriId);
        }
        try {
            T t = httpClient_mchKeyStore.get(mch_id).execute(request, responseHandler, HttpClientContext.create());
            if (resultErrorHandler != null) {
                resultErrorHandler.doHandle(uriId, request, t);
            }
            return t;
        } catch (Exception e) {
            logger.error("execute error", e);
        }
        return null;
    }

    private static void userAgent(HttpUriRequest httpUriRequest) {
        httpUriRequest.addHeader(userAgentHeader);
    }
}
