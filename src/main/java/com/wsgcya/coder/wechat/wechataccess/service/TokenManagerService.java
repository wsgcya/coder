package com.wsgcya.coder.wechat.wechataccess.service;



/**
 * 公众号token管理器
 *
 * @author ganchao
 * @date 2018-01-22 13:47
 */
public interface TokenManagerService {

    String getToken(String appid,String secret);

    String refreshToken(String appid,String secret);
   /* *//**
     * 获取微信token 根据全局配置，返回微信token或者返回第三方token
     *
     * @return
     *//*
    String getToken();

    *//**
     * 获取第三方接入的公众号的 token ，指定是否在过期时刷新
     * @param appId
     * @param refresh
     * @return
     *//*
    String getAuthToken(String appId, Boolean refresh);

    *//**
     * 根据appid获取token 根据全局配置，返回微信token或者返回第三方token
     *
     * @param appId
     * @param secret
     *            第三方授权 可以为空
     * @param type
     *            授权模式
     * @param refresh
     *            是否授权更新
     * @return
     *//*
    String getToken(String appId, String secret, WechatAccessType type, Boolean refresh);*/

    /*String getDefaultMiniToken();

    Map<String, String> getMiniToken();

    String getMiniToken(String appId, String secret, WechatAccessType type, Boolean refresh);

    *//**
     * 获取第三方平台token
     *
     * @return
     *//*
    String getComponentToken();

    *//**
     * 获取微信jsapiticket
     *
     * @return
     *//*
    String getJsApiTicket();

    *//**
     * 获取微信jsapiticket
     *
     * @param appId
     * @param secret
     *            第三方授权 可以为空
     * @param type
     * @param refresh
     *            是否授权更新
     * @return
     *//*
    String getJsApiTicket(String appId, String secret, WechatAccessType type, Boolean refresh);

    *//**
     * 获取微信cardTicket
     *
     * @return
     *//*
    String getCardTicket();

    *//**
     * 获取微信cardTicket
     *
     * @param appId
     * @param secret
     *            第三方授权 可以为空
     * @param type
     * @param refresh
     *            是否授权更新
     * @return
     *//*
    String getCardTicket(String appId, String secret, WechatAccessType type, Boolean refresh);

    *//**
     *
     * @Title getCropToken
     * @Class TokenManagerService
     * @param agentId
     * @return String
     * @return
     * @Description 获取企业微信token
     * @author ganchao
     * @Date 2018年1月30日
     *//*
    String getCropToken(String agentId, String secret);*/

}
