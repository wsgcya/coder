package com.wsgcya.coder.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Package: com.telin.framework.util 微信调用接口公用类 File: WeixinUtil.java
 */
public class WeixinUtil {

    public enum SignType {
	MD5, HMACSHA256
    }

    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";
    public static final String FIELD_SIGN = "sign";

    /**
     * 卡券Code解码接口
     *
     * @param encrypt_code
     * @param accessToken
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String decryptCode(String encrypt_code, String accessToken) throws IOException {
	String url = "https://api.weixin.qq.com/card/code/decrypt?access_token=" + accessToken;
	CloseableHttpClient httpClient = HttpClients.createDefault();
	HttpPost post = new HttpPost(url);
	StringEntity postingString = new StringEntity("{\"encrypt_code\":\"" + encrypt_code + "\"}", "utf-8");// json传递
	post.setEntity(postingString);
	post.setHeader("Content-type", "application/json");
	HttpResponse response = httpClient.execute(post);
	String resultMsg = EntityUtils.toString(response.getEntity(), "utf-8");
	httpClient.close();
	return resultMsg;
    }

    /**
     * cardConsume:(核销Code接口). <br/>
     *
     * @param code
     * @param card_id
     * @param accessToken
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String cardConsume(String code, String card_id, String accessToken) throws IOException {
	String url = "https://api.weixin.qq.com/card/code/consume?access_token=" + accessToken;
	CloseableHttpClient httpClient = HttpClients.createDefault();
	HttpPost post = new HttpPost(url);
	String postBody = "{" + "\"code\": \"" + code + "\" ,\"card_id\": \"" + card_id + "\"}";
	StringEntity postingString = new StringEntity(postBody, "utf-8");// json传递
	post.setEntity(postingString);
	post.setHeader("Content-type", "application/json");
	HttpResponse response = httpClient.execute(post);
	String resultMsg = EntityUtils.toString(response.getEntity(), "utf-8");
	httpClient.close();
	return resultMsg;
    }

    /**
     * getCardDetail:(查看卡券详情). <br/>
     *
     * @param card_id
     * @param accessToken
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @author bairongfeng@quantongfu.com
     */
    public static String getCardDetail(String card_id, String accessToken) throws ClientProtocolException, IOException {
	String url = "https://api.weixin.qq.com/card/get?access_token=%s";
	url = String.format(url, accessToken);

	CloseableHttpClient httpClient = HttpClients.createDefault();
	HttpPost post = new HttpPost(url);
	JSONObject jobj = new JSONObject();
	jobj.put("card_id", card_id);
	String postBody = jobj.toJSONString();
	StringEntity postingString = new StringEntity(postBody, "utf-8");// json传递
	post.setEntity(postingString);
	post.setHeader("Content-type", "application/json");
	HttpResponse response = httpClient.execute(post);
	String resultMsg = EntityUtils.toString(response.getEntity(), "utf-8");
	httpClient.close();
	return resultMsg;
    }

    /**
     * getUserInfo:(获取微信用户的具体信息). <br/>
     *
     * @param openid
     * @param accessToken
     * @return
     * @throws ParseException
     * @throws IOException
     * @author xiaqin@quantongfu.com
     * @date 2015-08-14
     */
    public static String getUserInfo(String openid, String accessToken) throws ParseException, IOException {
	String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s";
	url = String.format(url, accessToken, openid);
	CloseableHttpClient httpClient = HttpClients.createDefault();
	HttpGet get = new HttpGet(url);
	get.setHeader("Content-type", "application/json");
	HttpResponse response = httpClient.execute(get);
	String resultMsg = EntityUtils.toString(response.getEntity(), "utf-8");
	httpClient.close();
	return resultMsg;
    }

    /**
     * 生成签名
     *
     * @param data
     *            待签名数据
     * @param key
     *            API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, Object> data, String key) throws Exception {
	Map<String, String> treeMap = new HashMap<>();
	for (Entry<String, Object> tree : data.entrySet()) {
	    if (tree.getValue() != null) {
		treeMap.put(tree.getKey(), tree.getValue().toString());
	    }
	}
	return generateSignature(treeMap, key, SignType.MD5);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data
     *            待签名数据
     * @param key
     *            API密钥
     * @param signType
     *            签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, SignType signType) throws Exception {
	Set<String> keySet = data.keySet();
	String[] keyArray = keySet.toArray(new String[keySet.size()]);
	Arrays.sort(keyArray);
	StringBuilder sb = new StringBuilder();
	for (String k : keyArray) {
	    if (k.equals(FIELD_SIGN)) {
		continue;
	    }
	    if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
		sb.append(k).append("=").append(data.get(k).trim()).append("&");
	}
	sb.append("key=").append(key);
	if (SignType.MD5.equals(signType)) {
	    return MD5Util.MD5Encode(sb.toString()).toUpperCase();
	}else if (SignType.HMACSHA256.equals(signType)) {
	    return MD5Util.HMACSHA256(sb.toString(), key);
	}else {
	    throw new Exception(String.format("Invalid sign_type: %s", signType));
	}
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
     *
     * @param data
     *            Map类型数据
     * @param key
     *            API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, Object> data, String key) throws Exception {
	Map<String, String> treeMap = new HashMap<>();
	for (Entry<String, Object> tree : data.entrySet()) {
	    treeMap.put(tree.getKey(), tree.getValue().toString());
	}
	return isSignatureValid(treeMap, key, SignType.MD5);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data
     *            Map类型数据
     * @param key
     *            API密钥
     * @param signType
     *            签名方式
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key, SignType signType) throws Exception {
	if (!data.containsKey(FIELD_SIGN)) {
	    return false;
	}
	String sign = data.get(FIELD_SIGN);
	return generateSignature(data, key, signType).equals(sign);
    }

    /**
     * 微信支付接口
     *
     * @param xml
     * @return
     * @throws IOException
     */
    public static String payOrder(String url, String xml) throws IOException {
	CloseableHttpClient httpClient = HttpClients.createDefault();
	HttpPost post = new HttpPost(url);
	StringEntity entity = new StringEntity(xml.toString(), "UTF-8");
	post.setEntity(entity);
	post.setHeader("Content-Type", "text/xml;charset=UTF-8");
	HttpResponse response = httpClient.execute(post);
	String resultMsg = EntityUtils.toString(response.getEntity(), "utf-8");
	httpClient.close();
	return resultMsg;
    }

    /**
     *
     * @Title signatureJsapiInfo
     * @Class QueryJsSignatureService
     * @return String
     * @param jsapiToken
     * @param url
     * @return
     * @Description 生成jsapi签名
     * @author qinshijiang@telincn.com
     * @Date 2016年9月26日
     */
    public static String signatureJsapiInfo(String jsapiToken, String url, String appid) {
	String jsapiResult = "{appId:'%s',timestamp:'%s' ,nonceStr: '%s',signature: '%s',jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage']}";
	// String jsapiResult = "{debug: true,appId:'%s',timestamp:'%s'
	// ,nonceStr: '%s',signature: '%s',jsApiList:
	// ['onMenuShareTimeline','onMenuShareAppMessage']}";
	String nonceStr = StringUtils.createNonStr();
	String strSignFormat = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";
	Long timestamp = System.currentTimeMillis();
	String signature = EncryptionUtil.encode("SHA1", String.format(strSignFormat, jsapiToken, nonceStr, timestamp, url));
	return String.format(jsapiResult, appid, timestamp, nonceStr, signature);
    }

    /**
     *
     * @Title buildTextMsg
     * @Class WechatEventServiceImpl
     * @return String
     * @param openid
     * @param wxghid
     * @param content
     * @return
     * @Description组装微信回复文本消息
     * @author qinshijiang@telincn.com
     * @Date 2016年9月22日
     */
    public static String buildTextMsg(String openid, String wxghid, String content) {
	String xmlFormat = "<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content><FuncFlag>0</FuncFlag></xml>";
	return String.format(xmlFormat, openid, wxghid, System.currentTimeMillis() / 1000, content);
    }

	/**
	 *
	 * @Title buildTextMsg
	 * @Class WechatEventServiceImpl
	 * @return String
	 * @param openid
	 * @param wxghid
	 * @param mediaId
	 * @return
	 * @Description组装微信回复图片消息
	 * @author qinshijiang@telincn.com
	 * @Date 2016年9月22日
	 */
	public static String buildImageMsg(String openid, String wxghid, String mediaId) {
		String xmlFormat = "<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[image]]></MsgType><Image><MediaId><![CDATA[%s]]></MediaId></Image></xml>";
		return String.format(xmlFormat, openid, wxghid, System.currentTimeMillis() / 1000, mediaId);
	}

    /**
     *
     * @Title buildNewsMsg
     * @Class WechatEventServiceImpl
     * @return String
     * @param openid
     * @param wxghid
     * @param newsList
     * @return
     * @Description组装微信回复图文消息
     * @author qinshijiang@telincn.com
     * @Date 2016年9月22日
     */
    public static String buildNewsMsg(String openid, String wxghid, List<JSONObject> newsList) {
	String xmlFormat = "<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[news]]></MsgType><ArticleCount>%s</ArticleCount><Articles>%s</Articles></xml>";
	String itemFormat = "<item><Title><![CDATA[%s]]></Title> <Description><![CDATA[%s]]></Description><PicUrl><![CDATA[%s]]></PicUrl><Url><![CDATA[%s]]></Url></item>";
	String itemRes = "";
	for (JSONObject json : newsList) {
	    itemRes += String.format(itemFormat, json.get("title"), json.get("description"), json.get("picurl"), json.get("url"));
	}
	return String.format(xmlFormat, openid, wxghid, System.currentTimeMillis() / 1000, newsList.size(), itemRes);
    }

    /**
     *
     * @Title buildNewsMsg
     * @Class WechatEventServiceImpl
     * @return String
     * @param openid
     * @param wxghid
     * @param content
     * @return
     * @Description 组装微信回复图文消息,用于小i机器人
     * @author qinshijiang@telincn.com
     * @Date 2016年9月30日
     */
    public static String buildNewsMsg(String openid, String wxghid, String articleCount, String content) {
	String xmlFormat = "<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[news]]></MsgType><ArticleCount>%s</ArticleCount>%s</xml>";
	return String.format(xmlFormat, openid, wxghid, System.currentTimeMillis() / 1000, articleCount, content);
    }

    /**
     *
     * @Title buildTraslateCustom
     * @Class WechatEventServiceImpl
     * @return String
     * @param openid
     * @param wxghid
     * @return
     * @Description 组装转至微信客服的消息
     * @author qinshijiang@telincn.com
     * @Date 2016年10月8日
     */
    public static String buildTraslateCustom(String openid, String wxghid) {
	String xmlFormat = " <xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[transfer_customer_service]]></MsgType></xml>";
	return String.format(xmlFormat, openid, wxghid, System.currentTimeMillis() / 1000);
    }

    /**
     *
     * @Title addUserIntoTag
     * @Class WeixinUtil
     * @return String
     * @param accessToken
     * @param openid
     * @param tagid
     * @return
     * @Description 将用户添加到到分组中
     * @author qinshijiang@telincn.com
     * @Date 2016年12月20日
     */
    public static String addUserIntoTag(String accessToken, String openid, int tagid) {
	String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=";
	String postJson = "{\"openid_list\" : [\"%s\"],\"tagid\" : %s}";
	return HttpUtil.httpReq(url, accessToken, String.format(postJson, openid, tagid));
    }

    /**
     *
     * @Title addUserIntoTag
     * @Class WeixinUtil
     * @return String
     * @param accessToken
     * @param openid
     * @param tagid
     * @return
     * @Description 将用户从分组中去除
     * @author qinshijiang@telincn.com
     * @Date 2016年12月20日
     */
    public static String rmUserOutTag(String accessToken, String openid, int tagid) {
	String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=";
	String postJson = "{\"openid_list\" : [\"%s\"],\"tagid\" : %s}";
	return HttpUtil.httpReq(url, accessToken, String.format(postJson, openid, tagid));
    }

    /**
     *
     * @Title getUserTags
     * @Class WeixinUtil
     * @return String
     * @param accessToken
     * @param openid
     * @return
     * @Description 获取用户标签列表
     * @author qinshijiang@telincn.com
     * @Date 2016年12月20日
     */
    public static String getUserTags(String accessToken, String openid) {
	String url = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=";
	String postJson = "{\"openid\" : \"%s\"}";
	return HttpUtil.httpReq(url, accessToken, String.format(postJson, openid));
    }

    /**
     *
     * @Title getWxUserInfo
     * @Class WeixinUtil
     * @return String
     * @param accessToken
     * @param openid
     * @return
     * @Description 获取微信用户信息
     * @author qinshijiang@telincn.com
     * @Date 2017年1月16日
     */
    public static String getWxUserInfo(String accessToken, String openid) {
	String useUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN ";
	return HttpUtil.httpReq(String.format(useUrl, accessToken, openid), null, null);
    }

    /**
     *
     * @Title getTempQrcode
     * @Class WeixinUtil
     * @return String
     * @param accessToken
     * @param expSeconds
     * @param sceneId
     * @return
     * @throws IOException
     * @Description 获取临时二维码(字符串形式)
     * @author yujialin@hzchuangbo.com
     * @Date 2017年3月14日
     */
    public static String getTempQrcodeStr(String accessToken, int expSeconds, String sceneId) {
	String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
	String postJson = "{\"expire_seconds\":" + expSeconds + ",\"action_name\":\"QR_STR_SCENE\",\"action_info\":{\"scene\":{\"scene_str\":\""
		+ sceneId + "\"}}}";
	return HttpUtil.httpReq(url, null, postJson);
    }

    /**
     *
     * @Title getTempQrcode
     * @Class WeixinUtil
     * @return String
     * @param accessToken
     * @param expSeconds
     * @param sceneId
     * @return
     * @Description
     * @author yujialin@hzchuangbo.com
     * @Date 2017年12月23日
     */
    public static String getTempQrcode(String accessToken, int expSeconds, String sceneId) {
	String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
	String postJson = "{\"expire_seconds\":" + expSeconds + ",\"action_name\":\"QR_SCENE\",\"action_info\":{\"scene\":{\"scene_id\":" + sceneId
		+ "}}}";
	return HttpUtil.httpReq(url, null, postJson);
    }

    /**
     *
     * @Title signatureCardInfo
     * @Class WeixinUtil
     * @return String
     * @param cardId
     * @param cardTiket
     * @param codeId
     * @param openid
     * @return
     * @Description 生成卡券签名json
     * @author qinshijiang@telincn.com
     * @Date 2017年3月15日
     */
    public static JSONObject signatureCardInfo(String cardId, String cardTiket, String codeId, String openid) {
	JSONObject cardjson = new JSONObject();
	cardjson.put("cardId", cardId);
	String nonceStr = StringUtils.createNonStr();
	Long timestamp = System.currentTimeMillis() / 1000;
	String[] cardValues = new String[] {
		cardId, cardTiket, timestamp.toString(), nonceStr, codeId, openid
	};
	Arrays.sort(cardValues);
	String signature = EncryptionUtil.encode("SHA1", StringUtils.join(cardValues));
	JSONObject extjson = new JSONObject();
	extjson.put("code", codeId);
	extjson.put("openid", openid);
	extjson.put("timestamp", timestamp);
	extjson.put("nonce_str", nonceStr);
	extjson.put("signature", signature);
	cardjson.put("cardExt", extjson.toJSONString());
	return cardjson;
    }

    /**
     *
     * @Title getLongQrcode
     * @Class WeixinUtil
     * @return String
     * @param accessToken
     * @param fdyPhone
     * @return
     * @Description 获取永久二维码
     * @author wangzhe@hzchuangbo.com
     * @Date 2017年3月22日
     */
    public static String getLongQrcode(String accessToken, String fdyPhone) {
	String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
	String postJson = "{\"action_name\":\"QR_LIMIT_SCENE\",\"action_info\":{\"scene\":{\"scene_id\":" + fdyPhone + "}}}";
	return HttpUtil.httpReq(url, null, postJson);
    }

    public static String buildTempsessionMsg(String openid, String ghid, String sessionFrom) {
	String json = "{\"ToUserName\": \"%s\",\"FromUserName\": \"%s\",\"CreateTime\":\"%s\",\"MsgType\": \"event\",\"Event\": \"user_enter_tempsession\",\"SessionFrom\": \"%s\"}";
	return String.format(json, ghid, openid, System.currentTimeMillis(), sessionFrom);
    }

}
