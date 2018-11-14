package com.wsgcya.coder.wechat.wechataccess.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wsgcya.coder.common.constant.RedisConstants;
import com.wsgcya.coder.common.constant.UrlConstants;
import com.wsgcya.coder.common.util.HttpClientNew;
import com.wsgcya.coder.common.util.StringUtils;
import com.wsgcya.coder.wechat.wechataccess.dao.WechatMapper;
import com.wsgcya.coder.wechat.wechataccess.service.TokenManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 公众号token管理器 实现
 *
 * @author ganchao
 * @date 2018-01-22 13:47
 */
@Service
public class TokenManagerServiceImpl implements TokenManagerService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private WechatMapper wechatMapper;

    /**
     * 调用刷新token时，分布式锁的持有时间
     */
    // private static final long TOKEN_REFRESH_SECONDS = 60L;

    @Override
    public String getToken(String appid, String secret) {
        String token = stringRedisTemplate.opsForValue().get("RedisConstants.WECHAT_TOKEN_PREFIX+\"_\"+appid");
        if (StringUtils.isNotBlank(token)){
            return token;
        }else {
            token = refreshToken(appid, secret);
            if ("over".equals(token) || "error".equals(token) || "lock".equals(token)){
                return wechatMapper.getTokenByAppid(appid);
            }else {
                return token;
            }
        }

    }

    @Override
    public String refreshToken(String appid, String secret) {
        int hour = LocalDateTime.now().getHour();
        if (!stringRedisTemplate.opsForValue().setIfAbsent("gettoken_"+hour,"token")){
            return "over";
        }
        String token = "error";
        String key = RedisConstants.LOCK_PREFIX+appid+"refreshToken";
        if (!stringRedisTemplate.opsForValue().setIfAbsent(key,"token")){
            return "lock";
        }
        try {
            String tokenUrl = String.format(UrlConstants.WECHAT_TOKEN_URL, appid,secret);
            String tokenResult = HttpClientNew.sendGet(tokenUrl);
            logger.info("获取头ken返回值为:"+tokenResult);
            token = JSONObject.parseObject(tokenResult).getString("access_token");
            stringRedisTemplate.opsForValue().set(RedisConstants.WECHAT_TOKEN_PREFIX+"_"+appid,token);
            stringRedisTemplate.opsForValue().set("gettoken_"+hour,"token");
            stringRedisTemplate.expire("gettoken_"+hour,3600L, TimeUnit.SECONDS);
            wechatMapper.updateTokenByAppid(appid,token);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("刷新token出错",e);
        }finally {
            stringRedisTemplate.delete(key);
        }
        return token;
    }


/*
    @Override
    public String getDefaultMiniToken(){
        MiniAppConfig config = miniAppConfigs.getMini().get(0);
        return getMiniToken(config.getAppid(), config.getSecret(), WechatAccessType.findByCode(config.getAccessType()),
                config.getRefreshToken());
    }

    @Override
    public Map<String, String> getMiniToken(){
        return miniAppConfigs.getMini().stream().collect(Collectors.toMap(
                MiniAppConfig::getAppid,
                MiniAppConfig -> getMiniToken(MiniAppConfig.getAppid(), MiniAppConfig.getSecret(), WechatAccessType.findByCode(MiniAppConfig.getAccessType()), MiniAppConfig.getRefreshToken()),
                (key1, key2) -> key2,
                HashMap::new
        ));
    }

    @Override
    public String getMiniToken(String appId, String secret, WechatAccessType type, Boolean refresh){
        WechatToken wechatToken = new WechatToken();
        if (WechatAccessType.FULL_ACCESS.equals(type)) {
            //全权处理
            wechatToken = getMiniAccessToken(appId, secret, refresh);
        } else if (WechatAccessType.THIRD_ACCESS.equals(type)) {
            //TODO(授权第三方)
        } else if (WechatAccessType.EXTERNAL_ACCESS.equals(type)){
            wechatToken = getExternalMiniToken(appId);
        }
        return wechatToken.getAccessToken() == null ? "" : wechatToken.getAccessToken();
    }

    private WechatToken getMiniAccessToken(String appId, String secret, Boolean refresh){
        WechatToken tokenObj = redisService.getKeyJsonResult(RedisConstants.MINI_TOKEN_PREFIX + appId, WechatToken.class);
        Boolean canRefreshToken = refresh && (tokenObj == null || StringUtils.isBlank(tokenObj.getAccessToken()));
        if (canRefreshToken) {
            tokenObj = refreshMiniAccessToken(appId, secret);
        }
        return tokenObj == null ? new WechatToken() : tokenObj;
    }

    *//**
     * 通过其他方式获取 小程序token
     * @param appId
     * @return
     *//*
    private WechatToken getExternalMiniToken(String appId) {
        return externalMiniHandler.doTokenHandler(appId);
    }

    *//**
     * 通过其他方式获取 token
     *
     * @param appId
     * @return
     *//*
    private WechatToken getExternalToken(String appId) {
        return externalTokenHandler.doHandler(appId);
    }

    @Override
    public String getComponentToken() {
        String accessToken = redisTemplate.opsForValue().get(RedisConstants.COMPONENT_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            String ticket = redisTemplate.opsForValue().get(RedisConstants.COMPONENT_VERIFY_TICKET);
            ComponentAccessToken token = ComponentAPI.api_component_token(platformConfig.getCompent_appid(), platformConfig.getCompent_secret(),
                    ticket);
            logger.info("getComponentToken appid:{},secret:{},ticket:{},token:{}",platformConfig.getCompent_appid(), platformConfig.getCompent_secret(),
                    ticket,token.getComponent_access_token());
            if (token.isSuccess()) {
                accessToken = token.getComponent_access_token();
                redisTemplate.opsForValue().set(RedisConstants.COMPONENT_TOKEN, accessToken, 7000L, TimeUnit.SECONDS);
            } else {
                logger.error("获取第三方平台{}的token失败 :{}", platformConfig.getCompent_appid(), JSON.toJSONString(token));
            }
        }
        return accessToken;
    }

    private WechatToken getAccessToken(String appId, String secret, Boolean refresh) {
        WechatToken tokenObj = redisService.getKeyJsonResult(RedisConstants.WECHAT_TOKEN_PREFIX + appId, WechatToken.class);
        // 计算是否可以刷新token
        Boolean canRefreshToken = refresh && (tokenObj == null || StringUtils.isBlank(tokenObj.getAccessToken()));
        if (canRefreshToken) {
            tokenObj = refreshAccessToken(appId, secret);
        }
        return tokenObj == null ? new WechatToken() : tokenObj;
    }

    @Override
    public String getJsApiTicket() {
        return getJsApiTicket(weixinConfig.getAppId(), weixinConfig.getAppSecret(), WechatAccessType.findByCode(weixinConfig.getAccessType()),
                weixinConfig.getRefreshToken());
    }

    @Override
    public String getJsApiTicket(String appId, String secret, WechatAccessType type, Boolean refresh) {
        WechatToken wechatToken;
        if (WechatAccessType.THIRD_ACCESS.equals(type)) {
            // 授权给第三方平台
            wechatToken = getApiAuthToken(appId, refresh);
        } else if (WechatAccessType.FULL_ACCESS.equals(type)) {
            wechatToken = getAccessToken(appId, secret, refresh);
        } else {
            wechatToken = getExternalJsTicket(appId);
        }
        return wechatToken.getJsapiTicket() == null ? "" : wechatToken.getJsapiTicket();
    }

    *//**
     * 通过外部链接获取 jsTicket
     *
     * @param appId
     * @return
     *//*
    private WechatToken getExternalJsTicket(String appId) {
        return externalJsTicketHandler.doHandler(appId);
    }

    @Override
    public String getCardTicket() {
        return getCardTicket(weixinConfig.getAppId(), weixinConfig.getAppSecret(), WechatAccessType.findByCode(weixinConfig.getAccessType()),
                weixinConfig.getRefreshToken());
    }

    @Override
    public String getCardTicket(String appId, String secret, WechatAccessType type, Boolean refresh) {
        WechatToken wechatToken;
        if (WechatAccessType.THIRD_ACCESS.equals(type)) {
            // 授权给第三方平台
            wechatToken = getApiAuthToken(appId, refresh);
        } else if (WechatAccessType.FULL_ACCESS.equals(type)) {
            wechatToken = getAccessToken(appId, secret, refresh);
        } else {
            wechatToken = getExternalCardTicket(appId);
        }
        return wechatToken.getCardTicket() == null ? "" : wechatToken.getCardTicket();
    }

    *//**
     * 通过外部链接获取 cardTicket
     *
     * @param appId
     * @return
     *//*
    private WechatToken getExternalCardTicket(String appId) {
        return externalCardTicketHandler.doHandler(appId);
    }

    *//**
     * 获取第三方授权公众号的token
     *
     * @param appId
     * @param refresh 是否授权刷新token
     * @return
     *//*
    private WechatToken getApiAuthToken(String appId, Boolean refresh) {
        WechatToken tokenObj = redisService.getKeyJsonResult(RedisConstants.COMPONENT_APP_TOKEN_PREFIX + appId, WechatToken.class);
        // 计算是否可以刷新
        Boolean canRefresh = refresh && (tokenObj == null || StringUtils.isBlank(tokenObj.getAccessToken()));
        if (canRefresh) {
            tokenObj = refreshApiAuthToken(appId);
        }
        return tokenObj == null ? new WechatToken() : tokenObj;
    }

    *//**
     * 刷新 全权授权 公众号token
     *
     * @param appId
     * @return
     *//*
    private WechatToken refreshAccessToken(String appId, String secret) {
        String key = RedisConstants.LOCK_PREFIX + "refreshAccessToken";
        // 增加刷新的同步锁
        if (!redisService.setNX(key, TOKEN_REFRESH_SECONDS)) {
            logger.info("未获取到同步锁{}，返回", key);
            return null;
        }
        // 拉取旧的token
        WechatToken oldToken = redisService.getKeyJsonResult(RedisConstants.WECHAT_TOKEN_PREFIX + appId, WechatToken.class);
        WechatToken wechatToken = oldToken == null ? new WechatToken() : oldToken;
        TdAccessToken tokenLog = new TdAccessToken();
        tokenLog.setAtIntime(new Date());
        try {
            // 刷新token
            Token tokenResult = TokenAPI.token(appId, secret);
            logger.info("公众号{}，刷新token的结果{}", appId, JSON.toJSONString(tokenResult));
            if (tokenResult.isSuccess()) {
                wechatToken.setAccessToken(tokenResult.getAccess_token());
                tokenLog.setAtAccessToken(tokenResult.getAccess_token());
                tokenLog.setAtTokexpiresIn(tokenResult.getExpires_in());
            }

            // 刷新jsApiTicket
            Ticket apiJsTicket = TicketAPI.ticketGetticket(wechatToken.getAccessToken(), "jsapi");
            logger.info("公众号{}刷新jsApiTicket的结果{}", appId, JSON.toJSONString(apiJsTicket));
            if (apiJsTicket.isSuccess()) {
                wechatToken.setJsapiTicket(apiJsTicket.getTicket());
                tokenLog.setAtJsapiTicket(apiJsTicket.getTicket());
                tokenLog.setAtJsexpiresIn(apiJsTicket.getExpires_in());
            }

            // 刷新cardTicket
            Ticket cardTicket = TicketAPI.ticketGetticket(wechatToken.getAccessToken(), "wx_card");
            logger.info("公众号{}刷新cardTicket的结果{}", appId, JSON.toJSONString(cardTicket));
            if (cardTicket.isSuccess()) {
                wechatToken.setCardTicket(cardTicket.getTicket());
                tokenLog.setAtCardTicket(cardTicket.getTicket());
            }
            //记录token
            tokenLog.setAtIntime(new Date());
            accessTokenRepository.save(tokenLog);

            // 将结果更新到缓存中
            redisTemplate.opsForValue().set(RedisConstants.WECHAT_TOKEN_PREFIX + appId, JSON.toJSONString(wechatToken), 7000L, TimeUnit.SECONDS);
        } finally {
            unlock(key);
        }

        return wechatToken;
    }

    *//**
     * 刷新小程序token
     *//*
    private WechatToken refreshMiniAccessToken(String appId, String secret) {
        String key = RedisConstants.LOCK_PREFIX + "refreshMiniAccessToken:" + appId;
        // 增加刷新的同步锁
        if (!redisService.setNX(key, TOKEN_REFRESH_SECONDS)) {
            logger.info("未获取到同步锁{}，返回", key);
            return null;
        }
        // 拉取旧的token
        WechatToken oldToken = redisService.getKeyJsonResult(RedisConstants.MINI_TOKEN_PREFIX + appId, WechatToken.class);
        WechatToken wechatToken = oldToken == null ? new WechatToken() : oldToken;
        TdAccessTokenMini tokenMini = new TdAccessTokenMini();
        tokenMini.setAtIntime(new Date());
        try {
            // 刷新token
            Token tokenResult = TokenAPI.token(appId, secret);
            logger.info("小程序【{}】刷新token的结果: {}", appId, JSON.toJSONString(tokenResult));
            if (tokenResult.isSuccess()) {
                wechatToken.setAccessToken(tokenResult.getAccess_token());
                tokenMini.setAtAccessToken(tokenResult.getAccess_token());
                tokenMini.setAtTokexpiresIn(tokenResult.getExpires_in());
            }

            // 刷新jsApiTicket
            Ticket apiJsTicket = TicketAPI.ticketGetticket(wechatToken.getAccessToken(), "jsapi");
            logger.info("小程序【{}】刷新jsApiTicket的结果: {}", apiJsTicket, JSON.toJSONString(apiJsTicket));
            if (apiJsTicket.isSuccess()) {
                wechatToken.setJsapiTicket(apiJsTicket.getTicket());
                tokenMini.setAtJsapiTicket(apiJsTicket.getTicket());
                tokenMini.setAtJsexpiresIn(apiJsTicket.getExpires_in());
            }

            // 刷新cardTicket
            Ticket cardTicket = TicketAPI.ticketGetticket(wechatToken.getAccessToken(), "wx_card");
            logger.info("小程序【{}】刷新cardTicket的结果: {}", cardTicket, JSON.toJSONString(apiJsTicket));
            if (cardTicket.isSuccess()) {
                wechatToken.setCardTicket(cardTicket.getTicket());
                tokenMini.setAtCardTicket(cardTicket.getTicket());
            }
            //记录token
            tokenMini.setAtAppid(appId);
            tokenMini.setAtIntime(new Date());
            accessTokenMiniRepository.save(tokenMini);

            // 将结果更新到缓存中
            redisTemplate.opsForValue().set(RedisConstants.MINI_TOKEN_PREFIX + appId, JSON.toJSONString(wechatToken), 4000L, TimeUnit.SECONDS);
        } finally {
            unlock(key);
        }
        return wechatToken;
    }

    *//**
     * 刷新第三方授权公众号的token 包括accessToken, jsApiTicket, cardTicket
     *
     * @param appId
     * @return
     *//*
    private WechatToken refreshApiAuthToken(String appId) {
        String key = RedisConstants.LOCK_PREFIX + "refreshApiAuthToken";
        // 增加刷新的同步锁
        if (!redisService.setNX(key, TOKEN_REFRESH_SECONDS)) {
            logger.info("未获取到同步锁{}，返回", key);
            return null;
        }
        // 拉取旧的token
        WechatToken oldToken = redisService.getKeyJsonResult(RedisConstants.COMPONENT_APP_TOKEN_PREFIX + appId, WechatToken.class);
        WechatToken wechatToken = oldToken == null ? new WechatToken() : oldToken;
        TdAccessToken tokenLog = new TdAccessToken();
        tokenLog.setAtIntime(new Date());
        try {
            // 刷新token

            // 查询是否有refreshToken
            String refreshToken = (String) redisTemplate.opsForHash().get(RedisConstants.COMPONENT_APP_REFRESH_TOKEN, appId);
            if (StringUtils.isBlank(refreshToken)) {
                // 没有refreshToken，可能是第一次授权，通过授权码获取token
                String authCode = (String) redisTemplate.opsForHash().get(RedisConstants.COMPONENT_APP_AUTHORIZATION_CODE_HASH, appId);
                ApiQueryAuthResult apiQueryAuthResult = ComponentAPI.api_query_auth(getComponentToken(), platformConfig.getCompent_appid(), authCode);
                logger.info("公众号{}通过授权码换取token的结果为{}", appId, JSON.toJSONString(apiQueryAuthResult));
                if (apiQueryAuthResult.isSuccess()) {
                    wechatToken.setAccessToken(apiQueryAuthResult.getAuthorization_info().getAuthorizer_access_token());
                    // 保存refresh token
                    redisTemplate.opsForHash().put(RedisConstants.COMPONENT_APP_REFRESH_TOKEN, appId,
                            apiQueryAuthResult.getAuthorization_info().getAuthorizer_refresh_token());

                    tokenLog.setAtApiAuthToken(apiQueryAuthResult.getAuthorization_info().getAuthorizer_access_token());
                    tokenLog.setAtApiAuthRestoken(apiQueryAuthResult.getAuthorization_info().getAuthorizer_refresh_token());
                    tokenLog.setAtApiTokexpiresIn(apiQueryAuthResult.getAuthorization_info().getExpires_in());
                }

            } else {
                // 有refreshToken，通过refreshToken获取token
                AuthorizerAccessToken authorizerAccessToken = ComponentAPI.api_authorizer_token(getComponentToken(),
                        platformConfig.getCompent_appid(), appId, refreshToken);
                logger.info("公众号{}，刷新token的结果{}", appId, JSON.toJSONString(authorizerAccessToken));
                if (authorizerAccessToken.isSuccess()) {
                    wechatToken.setAccessToken(authorizerAccessToken.getAuthorizer_access_token());
                    // 保存刷新token
                    logger.info("公众号refresh_token : {} ==> {}", refreshToken, authorizerAccessToken.getAuthorizer_refresh_token());
                    redisTemplate.opsForHash().put(RedisConstants.COMPONENT_APP_REFRESH_TOKEN, appId,
                            authorizerAccessToken.getAuthorizer_refresh_token());

                    tokenLog.setAtApiAuthToken(authorizerAccessToken.getAuthorizer_access_token());
                    tokenLog.setAtApiAuthRestoken(authorizerAccessToken.getAuthorizer_refresh_token());
                    tokenLog.setAtApiTokexpiresIn(authorizerAccessToken.getExpires_in());
                }
            }

            // 刷新jsApiTicket
            Ticket apiJsTicket = TicketAPI.ticketGetticket(wechatToken.getAccessToken(), "jsapi");
            logger.info("公众号{}刷新jsApiTicket的结果{}", apiJsTicket, JSON.toJSONString(apiJsTicket));
            if (apiJsTicket.isSuccess()) {
                wechatToken.setJsapiTicket(apiJsTicket.getTicket());

                tokenLog.setAtJsapiTicket(apiJsTicket.getTicket());
                tokenLog.setAtJsexpiresIn(apiJsTicket.getExpires_in());
            }

            // 刷新cardTicket
            Ticket cardTicket = TicketAPI.ticketGetticket(wechatToken.getAccessToken(), "wx_card");
            logger.info("公众号{}刷新cardTicket的结果{}", apiJsTicket, JSON.toJSONString(apiJsTicket));
            if (cardTicket.isSuccess()) {
                wechatToken.setCardTicket(cardTicket.getTicket());

                tokenLog.setAtCardTicket(cardTicket.getTicket());
            }

            // 将结果更新到缓存中
            redisTemplate.opsForValue().set(RedisConstants.COMPONENT_APP_TOKEN_PREFIX + appId, JSON.toJSONString(wechatToken), 7000L,
                    TimeUnit.SECONDS);

            accessTokenRepository.save(tokenLog);
        } finally {
            // 释放锁
            unlock(key);
        }

        return wechatToken;
    }

    @Override
    public String getCropToken(String agentId, String sercet) {
        WechatToken tokenObj = redisService.getKeyJsonResult(RedisConstants.CROP_APP_TOKEN_PREFIX + agentId, WechatToken.class);
        // 计算是否可以刷新
        Boolean canRefresh = (tokenObj == null || StringUtils.isBlank(tokenObj.getAccessToken()));
        if (canRefresh) {
            tokenObj = refreshCropToken(agentId, sercet);
        }
        return tokenObj == null ? null : tokenObj.getAccessToken();
    }

    *//**
     * @param agentId
     * @param secret
     * @return
     * @Title refreshCropToken
     * @Class TokenManagerServiceImpl
     * @Description 刷新企业号Token
     * @author qinshijiang@telincn.com
     * @Date 2018年1月30日
     *//*
    private WechatToken refreshCropToken(String agentId, String secret) {
        String key = RedisConstants.LOCK_PREFIX + "refreshCropToken";
        // 增加刷新的同步锁
        if (!redisService.setNX(key, TOKEN_REFRESH_SECONDS)) {
            logger.info("未获取到同步锁{}，返回", key);
            return null;
        }
        WechatToken wechatToken = new WechatToken();
        try {
            // 刷新token
            Token tokenResult = TokenAPI.cropToken(qyhConfig.getCorpId(), secret);
            logger.info("企业号{}，应用ID{},刷新token的结果{}", qyhConfig.getCorpId(), agentId, JSON.toJSONString(tokenResult));
            if (tokenResult.isSuccess()) {
                wechatToken.setAccessToken(tokenResult.getAccess_token());
            }
            // 将结果更新到缓存中
            redisTemplate.opsForValue().set(RedisConstants.CROP_APP_TOKEN_PREFIX + agentId, JSON.toJSONString(wechatToken), 7000L, TimeUnit.SECONDS);
        } finally {
            // 释放锁
            unlock(key);
        }

        return wechatToken;
    }

    *//**
     * 解锁
     *
     * @param key
     *//*
    private void unlock(String key) {
        redisService.delete(key);
    }*/

}
