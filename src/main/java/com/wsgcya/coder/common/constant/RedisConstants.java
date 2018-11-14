package com.wsgcya.coder.common.constant;

/**
 * redis 常量
 *
 * @author ganchao
 * @date 2018-01-11 15:09
 */
public class RedisConstants {
    /**
     * rediskey:微信菜单访问统计
     */
    public static final String WECHAT_MENUVIEWSTATIS = "WECHAT:MENU_VIEW_STATIS";
    /**
     * rediskey:微信token存储
     */
    public static final String WECHAT_TOKEN_PREFIX = "WECHAT_TOKEN:";
    /**
     * rediskey:小程序token存储
     */
    public static final String MINI_TOKEN_PREFIX = "MINIPROGRAM_TOKEN:";

    /**
     * 按分钟短信发送数量记录REDIS KEY 前缀
     */
    public static final String SMS_MIN_PREFIX = "SMS_MIN_SEND_KEY_";
    /**
     * 按小时短信发送数量记录REDIS KEY 前缀
     */
    public static final String SMS_HOUR_PREFIX = "SMS_HOUR_SEND_KEY_";
    /**
     * 按天短信发送数量记录REDIS KEY 前缀
     */
    public static final String SMS_DAY_PREFIX = "SMS_DAY_SEND_KEY_";
    /**
     * 统计验证码错误次数的REDIS KEY 前缀
     */
    public static final String SMS_DAY_FAIL_PREFIX="SMS_DAY_FAIL_KEY_";
    /**
     * 同步锁前缀
     */
    public static final String LOCK_PREFIX = "LOCK:";
    /**
     * 第三方平台存放票据(微信每10分钟推送过来一次）
     */
    public static final String COMPONENT_VERIFY_TICKET = "COMPONENT_VERIFY_TICKET";
    /**
     * 第三方平台 存放token
     */
    public static final String COMPONENT_TOKEN = "COMPONENT_TOKEN";
    /**
     * 第三方平台 预授权码信息
     */
    public static final String COMPONENT_PREAUTHCODE = "COMPONENT_PREAUTHCODE:";
    /**
     * 授权给第三方平台的公众号token信息
     */
    public static final String COMPONENT_APP_TOKEN_PREFIX = "API_AUTH_TOKEN:";
    /**
     * 授权给第三方平台的公众号refresh_token信息
     */
    public static final String COMPONENT_APP_REFRESH_TOKEN = "AUTH_REFRESH_TOKEN";
    /**
     * 授权给第三方平台的公众号的授权码
     */
    public static final String COMPONENT_APP_AUTHORIZATION_CODE_HASH = "AUTHORIZATION_CODE_HASH";
    /**
     * 企业号token前缀
     */
    public static final String CROP_APP_TOKEN_PREFIX = "CROP_TOKEN:";
    /**
     * 菜单点击统计（包含CLICK,VIEW）
     */
    public static final String MENU_CLICK_PREFIX = "WECHAT:MENU_CLICK_STAT_";
    /**
     * 用户手机资产 - 用户姓名缓存
     */
    public static final String ZC_PHONE_ACCOUNT_CACHE = "ZC_PHONE_ACCOUNT_CACHE:";
    /**
     * 用户固话资产 - 用户姓名缓存
     */
    public static final String ZC_LANDlINE_ACCOUNT_CACHE = "ZC_LANDlINE_ACCOUNT_CACHE:";
    /**
     * 用户宽带资产 - 用户姓名缓存
     */
    public static final String ZC_BROADBAND_ACCOUNT_CACHE = "ZC_BROADBAND_ACCOUNT_CACHE:";

    /**
     *  缓存key 前缀
     */
    public static final String CACHE_PREFIX="CACHE:";

    public static final String TRADE_NUM_KEY = "TRADE_NUM";

    /**
     * 循环流水缓存
     */
    public static final String ROUND_NUM_KEY = "ROUND_NUM:";

    /**小程序生成rd*/
    public static final String MINI_RD_KEY = ROUND_NUM_KEY + "RD";

}
