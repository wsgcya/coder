package com.wsgcya.coder.common.util.httpclient;

import com.wsgcya.coder.common.util.StringUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

/* *
 *类名：HttpProtocolHandler
 *功能：HttpClient方式访问
 *详细：获取远程HTTP数据
 *日期：2017-2-15 16:45:55
 *作者：zjq
 *说明：
 */

@SuppressWarnings("deprecation")
public class HttpProtocolHandler {
    private static Logger logger = LoggerFactory.getLogger(HttpProtocolHandler.class);

    private static String DEFAULT_CHARSET = "GBK";

    /**
     * 连接超时时间，由bean factory设置，缺省为8秒钟
     */
    private static int defaultConnectionTimeout = 8000;

    /**
     * 回应超时时间, 由bean factory设置，缺省为30秒钟
     */
    private static int defaultSoTimeout = 30000;

    /**
     * 闲置连接超时时间, 由bean factory设置，缺省为60秒钟
     */
    private int defaultIdleConnTimeout = 60000;

    private int defaultMaxConnPerHost = 30;

    private int defaultMaxTotalConn = 80;

    /**
     * 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒
     */
    private static final long defaultHttpConnectionManagerTimeout = 3 * 1000;

    /**
     * HTTP连接管理器，该连接管理器必须是线程安全的.
     */
    private HttpConnectionManager connectionManager;

    private static HttpProtocolHandler httpProtocolHandler = new HttpProtocolHandler();

    private static boolean hasInit = false;

    // 需证书的HTTP请求器
    private static CloseableHttpClient httpClient = null;

    @Value("${wxmch.certUrl}")
    private static String defaultCertUrl;

    /**
     * 工厂方法
     *
     * @return
     */
    public static HttpProtocolHandler getInstance() {
        return httpProtocolHandler;
    }

    /**
     * 私有的构造方法
     */
    private HttpProtocolHandler() {
        // 创建一个线程安全的HTTP连接池
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(
                defaultMaxConnPerHost);
        connectionManager.getParams().setMaxTotalConnections(
                defaultMaxTotalConn);

        IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
        ict.addConnectionManager(connectionManager);
        ict.setConnectionTimeout(defaultIdleConnTimeout);

        ict.start();
    }

    /**
     * 执行Http请求
     *
     * @param request         请求数据
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath     文件路径
     * @return
     * @throws HttpException , IOException
     */
    public HttpResponse execute(HttpRequest request, String strParaFileName,
                                String strFilePath) throws HttpException, IOException {
        HttpClient httpclient = new HttpClient(connectionManager);

        // 设置连接超时
        int connectionTimeout = defaultConnectionTimeout;
        if (request.getConnectionTimeout() > 0) {
            connectionTimeout = request.getConnectionTimeout();
        }
        httpclient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(connectionTimeout);

        // 设置回应超时
        int soTimeout = defaultSoTimeout;
        if (request.getTimeout() > 0) {
            soTimeout = request.getTimeout();
        }
        httpclient.getHttpConnectionManager().getParams()
                .setSoTimeout(soTimeout);

        // 设置等待ConnectionManager释放connection的时间
        httpclient.getParams().setConnectionManagerTimeout(
                defaultHttpConnectionManagerTimeout);

        String charset = request.getCharset();
        charset = charset == null ? DEFAULT_CHARSET : charset;
        HttpMethod method = null;

        // get模式且不带上传文件
        if (request.getMethod().equalsIgnoreCase(HttpRequest.METHOD_GET)) {
            method = new GetMethod(request.getUrl());
            method.getParams().setCredentialCharset(charset);
            if (null != request.getQueryString()) {
                method.setQueryString(request.getQueryString());
            }
        } else if (strParaFileName.equals("") && strFilePath.equals("")) {
            // post模式且不带上传文件 参数为数组list
            method = new PostMethod(request.getUrl());
            if (null != request.getParameters()) {
                ((PostMethod) method).addParameters(request.getParameters());
                method.addRequestHeader("Content-Type", request.getHeadContentType() + " charset=" + charset);
            }
            String body = request.getBody();
            if (!StringUtils.isEmpty(body)) {
                method.addRequestHeader("Content-Type", request.getContentType() + ";charset=UTF-8");
                ((PostMethod) method).setRequestEntity(new StringRequestEntity(
                        body, request.getContentType(), charset));
            }
        } else {
            // post模式且带上传文件
            method = new PostMethod(request.getUrl());
            List<Part> parts = new ArrayList<Part>();
            for (int i = 0; i < request.getParameters().length; i++) {
                parts.add(new StringPart(request.getParameters()[i].getName(),
                        request.getParameters()[i].getValue(), charset));
            }
            // 增加文件参数，strParaFileName是参数名，使用本地文件
            parts.add(new FilePart(strParaFileName, new FilePartSource(
                    new File(strFilePath))));

            // 设置请求体
            ((PostMethod) method).setRequestEntity(new MultipartRequestEntity(
                    parts.toArray(new Part[0]), new HttpMethodParams()));
        }

        // 设置Http Header中的User-Agent属性
        method.addRequestHeader("User-Agent", "Mozilla/4.0");
        HttpResponse response = new HttpResponse();
        try {
            httpclient.executeMethod(method);
            if (request.getResultType().equals(HttpResultType.STRING)) {
                response.setStringResult(method.getResponseBodyAsString());
            } else if (request.getResultType().equals(HttpResultType.BYTES)) {
                response.setByteResult(method.getResponseBody());
            }
            response.setResponseHeaders(method.getResponseHeaders());
        } catch (UnknownHostException ex) {
            logger.error("http UnknownHost", ex);
            return null;
        } catch (IOException ex) {
            logger.error("http IOException", ex);
            return null;
        } catch (Exception ex) {
            logger.error("http Exception", ex);
            return null;
        } finally {
            method.releaseConnection();
            httpclient.getHttpConnectionManager().closeIdleConnections(0);
        }
        return response;
    }

    /**
     * 执行ssl posthttp
     *
     * @param certUrl 证书绝对路径
     * @param url     请求地址
     * @param xml     请求xml
     * @param mch_id  商户账号
     * @return
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     */
    public static String getSSLPostMethod(String certUrl, String url,
                                          String xml, String mch_id) throws UnrecoverableKeyException,
            KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, IOException {
        if (StringUtils.isBlank(certUrl)) {
            certUrl = defaultCertUrl;
        }
        if (!hasInit || !certUrl.equals(defaultCertUrl)) {
            initSSL(certUrl, mch_id);
        }

        logger.info("certUrl=====>" + certUrl);
        logger.info("url=====>" + url);
        logger.info("xml=====>" + xml);
        logger.info("mch_id=====>" + mch_id);

        String result = "";
        RequestConfig rc = RequestConfig.custom()
                .setSocketTimeout(defaultSoTimeout)
                .setConnectTimeout(defaultConnectionTimeout).build();
        HttpPost httpPost = new HttpPost(url);
        System.out.println("请求参数:" + xml);
        logger.info("请求参数:" + xml);
        StringEntity postEntity = new StringEntity(xml, "UTF-8");
        logger.info("postEntity=====>" + postEntity);
        httpPost.setEntity(postEntity);
        httpPost.setConfig(rc);
        org.apache.http.HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            logger.info("response=====>" + response);
        } catch (Exception e) {
            logger.error("e",e);
        }
        HttpEntity entity = response.getEntity();
        result = EntityUtils.toString(entity, "UTF-8");
        return result;
    }

    public static String postFile(String url, byte[] bytes1, String fileName, String fileLength) {
        String result = null;
        BufferedReader reader = null;
        try {
            URL urlObj = new URL(url);
            // 连接
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            /**
             * 设置关键值
             */
            con.setConnectTimeout(defaultConnectionTimeout);
            con.setReadTimeout(defaultSoTimeout);
            con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false); // post方式不能使用缓存
            // 设置请求头信息
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            // 设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            con.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary="
                            + BOUNDARY);
            // 请求正文信息
            // 第一部分：
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + fileLength + "\";filename=\""
                    + fileName + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            OutputStream out = new DataOutputStream(con.getOutputStream());
            // 输出表头
            out.write(head);
            // 文件正文部分
            // 把文件已流文件的方式 推入到url中
            InputStream in = new ByteArrayInputStream(bytes1);
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            // 结尾部分
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            out.write(foot);
            out.flush();
            out.close();
            StringBuffer buffer = new StringBuffer();
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (Exception e) {
            logger.error("post上传file异常", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static void initSSL(String certUrl, String mch_id)
            throws UnrecoverableKeyException, KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(certUrl));// 加载本地的证书进行https加密传输
        try {
            keyStore.load(instream, mch_id.toCharArray());// 设置证书密码
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, mch_id.toCharArray())// 商户号
                    .build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
                    .build();
            hasInit = true;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }
    }

    /**
     * 将NameValuePairs数组转变为字符串
     *
     * @param nameValues
     * @return
     */
    protected String toString(NameValuePair[] nameValues) {
        if (nameValues == null || nameValues.length == 0) {
            return "null";
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < nameValues.length; i++) {
            NameValuePair nameValue = nameValues[i];

            if (i == 0) {
                buffer.append(nameValue.getName() + "=" + nameValue.getValue());
            } else {
                buffer.append("&" + nameValue.getName() + "="
                        + nameValue.getValue());
            }
        }
        return buffer.toString();
    }
}