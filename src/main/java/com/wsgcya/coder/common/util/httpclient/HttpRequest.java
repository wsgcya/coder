package com.wsgcya.coder.common.util.httpclient;

import org.apache.commons.httpclient.NameValuePair;

import java.util.Map;

/* *
 *类名：HttpRequest
 *功能：Http请求对象的封装
 *详细：封装Http请求
 *日期：2017-2-15 16:43:16
 *作者：zjq
 *说明：
 */

public class HttpRequest {

    /**
     * HTTP GET method
     */
    public static final String METHOD_GET = "GET";

    /**
     * HTTP POST method
     */
    public static final String METHOD_POST = "POST";

    /** HTTP POST contentType */
    /**
     * text/xml
     */
    public static final String CONTENT_TYPE = "application/json";

    public static final String HEAD_CONTENT_TYPE = "application/x-www-form-urlencoded; text/html;";

    /**
     * 待请求的url
     */
    private String url = null;

    /**
     * 默认的请求方式
     */
    private String method = METHOD_POST;

    private String body = null;

    private int timeout = 0;

    private int connectionTimeout = 0;

    /**
     * Post方式请求时组装好的参数值对
     */
    private NameValuePair[] parameters = null;

    /**
     * Get方式请求时对应的参数
     */
    private String queryString = null;

    /**
     * 默认的请求编码方式
     */
    private String charset = "UTF-8";

    /**
     * 请求发起方的ip地址
     */
    private String clientIp;

    /**
     * 请求发起的content-type 默认application/json
     */
    private String contentType = CONTENT_TYPE;

    private String headContentType = HEAD_CONTENT_TYPE;

    /**
     * 请求返回的方式
     */
    private HttpResultType resultType = HttpResultType.BYTES;

    public HttpRequest(HttpResultType resultType) {
        super();
        this.resultType = resultType;
    }

    /**
     * @return Returns the clientIp.
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * @param clientIp The clientIp to set.
     */
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public NameValuePair[] getParameters() {
        return parameters;
    }

    /*NameValuePair pair1 = new NameValuePair("login_code", "73D0006");
    NameValuePair pair2 = new NameValuePair("password", "MjU1ODAyYWI=");
    NameValuePair pair3 = new NameValuePair("type", "random_code");
    NameValuePair[] parameters = new NameValuePair[]{pair1, pair2,pair3};*/
    public void setParameters(NameValuePair[] parameters) {
        this.parameters = parameters;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @return Returns the charset.
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset The charset to set.
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    public HttpResultType getResultType() {
        return resultType;
    }

    public void setResultType(HttpResultType resultType) {
        this.resultType = resultType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeadContentType() {
        return headContentType;
    }

    public void setHeadContentType(String headContentType) {
        this.headContentType = headContentType;
    }

    public static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }
        return nameValuePair;
    }
}