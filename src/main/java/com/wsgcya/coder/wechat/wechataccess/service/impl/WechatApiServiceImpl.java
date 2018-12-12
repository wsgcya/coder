package com.wsgcya.coder.wechat.wechataccess.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wsgcya.coder.common.config.WechatConfig;
import com.wsgcya.coder.common.pojo.WechatOauthInfo;
import com.wsgcya.coder.common.util.HttpUtil;
import com.wsgcya.coder.common.util.StringUtils;
import com.wsgcya.coder.common.util.WeixinUtil;
import com.wsgcya.coder.wechat.wechataccess.service.TokenManagerService;
import com.wsgcya.coder.wechat.wechataccess.service.WechatApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class WechatApiServiceImpl implements WechatApiService {

    private static Logger logger = LoggerFactory.getLogger(WechatApiServiceImpl.class);

    @Value("${wechat.appid}")
    private String appid;
    @Value("${wechat.token}")
    private String token;
    @Value("${wechat.appsecret}")
    private String appsecret;
    @Autowired
    private WechatConfig wechatConfig;
   // @Autowired
   // private MiniAppConfigs miniAppConfigs;
    @Value("${server.domain}")
    private String doamin;
    @Autowired
    private TokenManagerService tokenManagerService;
   // @Autowired
   // private BaseRedisService baseRedisService;

    @Override
    public Boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[]{
                token, timestamp, nonce
        };
        // 将 token、timestamp、nonce 三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 sha1 加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = StringUtils.byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        content = null;
        // 将 sha1 加密后的字符串可与 signature 对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    @Override
    public WechatOauthInfo goWechatOauth(String code, String scope, String state, HttpServletRequest request, HttpSession session) {
        WechatOauthInfo oauth = new WechatOauthInfo();
        oauth.setRedirectUrl(StringUtils.getWeixinUrl(wechatConfig.getAppId(), request, "snsapi_base", state, doamin));
        String openid = (String) session.getAttribute("openid");
        String needuserinfo = (String) session.getAttribute("needuserinfo");
        if (StringUtils.isNotBlank(openid) && StringUtils.isBlank(needuserinfo)) {
            oauth.setOpenid(openid);
            oauth.setStatus(2);
            if ("snsapi_userinfo".equals(scope)) {
                String pinickname = (String) session.getAttribute("pinickname");
                if (StringUtils.isBlank(pinickname)) {
                    oauth.setStatus(0);
                    session.setAttribute("needuserinfo", "0");
                    oauth.setRedirectUrl(StringUtils.getWeixinUrl(wechatConfig.getAppId(), request, scope, state, doamin));
                }
            }
            return oauth;
        }
        if (StringUtils.isBlank(code)) {
            oauth.setStatus(0);
        } else {
            getOauthInfo(oauth, code, session, scope, state, request);// 获取用户信息
            setRetryTime(oauth, session);// 设置三次重试
        }
        return oauth;
    }


  /*  *//**
     * 使用封装的小程序参数进行登录
     * @param jsCode
     * @return
     *//*
    @Override
    public Result miniLogin(String jsCode){
        return miniLogin(miniAppConfigs.getMini().get(0).getAppid(), miniAppConfigs.getMini().get(0).getSecret(), jsCode);
    }

    *//**
     * 使用封装的小程序参数进行登录
     * @param jsCode
     * @return
     *//*
    @Override
    public Result miniLogin(String appId, String jsCode){
        MiniAppConfig config = miniAppConfigs.getMini().stream().filter(u -> appId.equals(u.getAppid())).findFirst().get();
        return miniLogin(appId, config.getSecret(), jsCode);
    }

    *//**
     * 小程序登录
     * @param appid
     * @param secret
     * @param jsCode
     * @return
     *//*
    @Override
    public Result miniLogin(String appid, String secret, String jsCode){
        String loginFormat = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
        try {
            String loginUrl = String.format(loginFormat, appid, secret, jsCode);
            String res = HttpUtil.httpSend(loginUrl, "", false);
            logger.info("{}|mini login res:{}", jsCode, res);
            if (StringUtils.isNotBlank(res)){
                MiniLoginInfo miniLoginInfo = JSON.parseObject(res, MiniLoginInfo.class);
                //登录成功时errcode不存在
                if (miniLoginInfo.getErrcode() == null){
                    //获取6位循环流水号
                    String roundNum = StringUtils.subAndAddZeroForNum(String.valueOf(baseRedisService.incr(RedisConstants.MINI_RD_KEY)), 6);
                    String rd = DateUtil.parseDateTime("yyyyMMddHHmmssSSS") + roundNum;
                    miniLoginInfo.setRd(rd);
                    //缓存rd 2小时
                    baseRedisService.set(rd, JSON.toJSONString(miniLoginInfo), 7200, TimeUnit.SECONDS);
                    return ResultGenerator.genSuccessResult(miniLoginInfo);
                } else {
                    return ResultGenerator.genFailResult("-1", "fail", miniLoginInfo);
                }
            }
        } catch (Exception e) {
            logger.error("小程序登录异常|appid: {}|secret: {}|jsCode: {}", appid, secret, jsCode, e);
        }
        return ResultGenerator.genFailResult(ResultCode.FAIL);
    }*/

    /**
     * @param oauth
     * @param code
     * @param session
     * @param scope
     * @return void
     * @Title getOauthInfo
     * @Class WechatApiServiceImpl
     * @Description 调用微信获取信息
     * @author ganchao
     * @Date 2016年9月19日
     */
    private void getOauthInfo(WechatOauthInfo oauth, String code, HttpSession session, String scope, String state, HttpServletRequest request) {
        String oauthTem = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        String oauthUrl = String.format(oauthTem, wechatConfig.getAppId(), wechatConfig.getAppSecret(), code);
        String result = HttpUtil.httpReq(oauthUrl, "", "");
        if (StringUtils.isNotBlank(result)) {
            JSONObject resJon = JSONObject.parseObject(result);
            if (StringUtils.isNotBlank(resJon.getString("openid"))) {
                oauth.setOpenid(resJon.getString("openid"));
                oauth.setStatus(2);
                session.setAttribute("openid", resJon.getString("openid"));
                session.removeAttribute("accesstime");
                String needuserinfo = (String) session.getAttribute("needuserinfo");
                if ("snsapi_userinfo".equals(scope)) {
                    if (StringUtils.isNotBlank(needuserinfo)) {
                        setUserImageInfo(oauth, resJon.getString("access_token"), resJon.getString("openid"), session, state, request);
                    } else {
                        oauth.setStatus(0);
                    }
                }
            }
        }
    }

    /**
     * @param oauth
     * @param session
     * @return void
     * @Title setRetryTime
     * @Class WechatApiServiceImpl
     * @Description 设置三次重试
     * @author ganchao
     * @Date 2016年9月19日
     */
    private void setRetryTime(WechatOauthInfo oauth, HttpSession session) {
        if (StringUtils.isBlank(oauth.getOpenid())) {
            Object accessTime = session.getAttribute("accesstime");
            if (accessTime != null) {
                int atimes = (int) accessTime;
                if (atimes >= 3) {
                    oauth.setStatus(1);
                } else {
                    oauth.setStatus(0);
                    session.setAttribute("accesstime", atimes + 1);
                }
            } else {
                oauth.setStatus(0);
                session.setAttribute("accesstime", 1);
            }
        }
    }

    /**
     *
     * @param oauth
     * @param accessToken
     * @param openid
     * @param session
     * @param state
     * @param request
     * @return void
     * @Title setUserImageInfo
     * @Class WechatApiServiceImpl
     * @Description 通过oauth获取用户的头像和昵称
     * @author ganchao
     * @Date 2016年7月30日
     */
    private void setUserImageInfo(WechatOauthInfo oauth, String accessToken, String openid, HttpSession session, String state,
                                  HttpServletRequest request) {
        String snsUserTemp = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
        String result = HttpUtil.httpReq(String.format(snsUserTemp, accessToken, openid), "", "");
        if (StringUtils.isNotBlank(result)) {
            JSONObject resJson = JSONObject.parseObject(result);
            if (StringUtils.isNotBlank(resJson.getString("nickname")) || StringUtils.isNotBlank(resJson.getString("headimgurl"))) {
                session.removeAttribute("needuserinfo");
                session.setAttribute("pinickname", resJson.getString("nickname"));
                session.setAttribute("piheadurl", resJson.getString("headimgurl"));
                session.setAttribute("unionid",resJson.getString("unionid"));
            }
            if (StringUtils.isNotBlank(resJson.getString("errcode"))) {
                oauth.setStatus(0);
                oauth.setRedirectUrl(StringUtils.getWeixinUrl(wechatConfig.getAppId(), request, "snsapi_userinfo", state, doamin));
            }
        }
    }

    @Override
    public String getJsapiSignature(String url) {
     //   String jsApiTicket = tokenManagerService.getJsApiTicket();
        String res = "";
     /*   if (StringUtils.isNotBlank(jsApiTicket)) {
            res = WeixinUtil.signatureJsapiInfo(jsApiTicket, url, wechatConfig.getAppId());
        }*/
        return res;
    }

    @Override
    public WechatOauthInfo goWechatOauthOut(String code, String scope, String state, HttpServletRequest request, HttpSession seesion) {
        WechatOauthInfo oauth = new WechatOauthInfo();
        oauth.setRedirectUrl(StringUtils.getWeixinUrl(wechatConfig.getAppId(), request, "snsapi_base", state, doamin));
        String openid = (String) seesion.getAttribute("openid");
        String needuserinfo = (String) seesion.getAttribute("needuserinfo");
        if (StringUtils.isNotBlank(openid) && StringUtils.isBlank(needuserinfo)) {
            oauth.setOpenid(openid);
            oauth.setStatus(2);
            if ("snsapi_userinfo".equals(scope)) {
                String pinickname = (String) seesion.getAttribute("pinickname");
                if (StringUtils.isBlank(pinickname)) {
                    String wechatToken = tokenManagerService.getToken(appid,appsecret);
                    String personInfo = WeixinUtil.getWxUserInfo(wechatToken, openid);
                    JSONObject json = JSON.parseObject(personInfo);
                    String subFlag = json.getString("subscribe");
                    if (StringUtils.isNotBlank(subFlag) && "1".equals(subFlag)) {
                        seesion.setAttribute("pinickname", json.getString("nickname"));
                        seesion.setAttribute("piheadurl", json.getString("headimgurl"));
                    } else {
                        oauth.setStatus(0);
                        seesion.setAttribute("needuserinfo", "0");
                        oauth.setRedirectUrl(StringUtils.getWeixinUrl(wechatConfig.getAppId(), request, scope, state, doamin));
                    }
                }
            }
            return oauth;
        }
        if (StringUtils.isBlank(code)) {
            oauth.setStatus(0);
        } else {
            getOauthInfo(oauth, code, seesion, scope, state, request);// 获取用户信息
            setRetryTime(oauth, seesion);// 设置三次重试
        }
        return oauth;
    }

    /*@Override
    public String getCardSignature(String[] cards, String openid) {
        JSONArray jsonArr = new JSONArray();
        if (cards != null && cards.length > 0) {
            String cardTicket= tokenManagerService.getCardTicket();
            for (String card : cards) {
                jsonArr.add(WeixinUtil.signatureCardInfo(card, cardTicket, StringUtils.createCodeId(), openid));
            }
        }
        return jsonArr.toString();
    }*/


}
