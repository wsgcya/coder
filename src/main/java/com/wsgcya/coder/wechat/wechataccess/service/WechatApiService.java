package com.wsgcya.coder.wechat.wechataccess.service;

import com.wsgcya.coder.common.pojo.WechatOauthInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface WechatApiService {

    /**
     *
     * @Title checkSignature
     * @Class WechatApiService
     * @return Boolean
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     * @Description 验证签名的合法性
     * @author qinshijiang@telincn.com
     * @Date 2016年7月30日
     */
    Boolean checkSignature(String signature, String timestamp, String nonce);

    /**
     *
     * @Title goWechatOauth
     * @Class WechatApiService
     * @return WechatOauth
     * @param code
     * @param scope
     * @param state
     * @param request
     * @param seesion
     * @return
     * @Description 微信oauth验证
     * @author qinshijiang@telincn.com
     * @Date 2016年7月30日
     */
    WechatOauthInfo goWechatOauth(String code, String scope, String state, HttpServletRequest request, HttpSession seesion);

    /**
     *
     * @Title goWechatOauthOut
     * @Class WechatApiService
     * @return WechatOauth
     * @param code
     * @param scope
     * @param state
     * @param request
     * @param seesion
     * @return
     * @Description 对外微信oauth验证
     * @author qinshijiang@telincn.com
     * @Date 2017年1月16日
     */
    WechatOauthInfo goWechatOauthOut(String code, String scope, String state, HttpServletRequest request, HttpSession seesion);

   // Result miniLogin(String jsCode);

  //  Result miniLogin(String appId, String jsCode);

   // Result miniLogin(String appid, String secret, String jsCode);

    /**
     *
     * @Title getJsapiSignature
     * @Class WechatApiService
     * @return String
     * @param url
     * @return
     * @Description 获取JSAPI
     * @author yujialin@hzchuangbo.com
     * @Date 2016年10月12日
     */
    String getJsapiSignature(String url);

    /**
     *
     * @Title getCardSignature
     * @Class WechatApiService
     * @return String
     * @param cards
     * @param openid
     * @return
     * @Description 获取卡券签名
     * @author qinshijiang@telincn.com
     * @Date 2017年3月15日
     */
   // String getCardSignature(String[] cards,String openid);
}
