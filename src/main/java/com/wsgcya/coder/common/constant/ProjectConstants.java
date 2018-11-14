package com.wsgcya.coder.common.constant;

/**
 * 项目常量
 *
 * @author ganchao
 * @date 2018-01-11 15:09
 */
public class ProjectConstants {
    public static final String ACCOUNT_OPENID = "openid";

    /**
     * 默认初始容器大小
     */
    public static final int INITIAL_CAPACITY = 4;
    /**
     * 跳转标识
     */
    public static final String JUMP_TOKEN = "jumptoken";
    /**
     * 来源标记
     */
    public static final String FROM = "from";

    /**
     * 公众号列表缓存
     */
    public static final String WECHAT_LIST = "CACHE:WECHAT_LIST";

    //################ 用户追踪相关字段 start
    private static final String CB_UTM_PREFIX = "cbutm_";
    /**
     * 用户访问 - 三级域名标识
     */
    public static final String UTM_DOMAIN = CB_UTM_PREFIX + "domain";
    /**
     * 用户渠道
     */
    public static final String UTM_CHANNEL = CB_UTM_PREFIX + "channel";

    //################ 用户追踪相关字段 end
}
