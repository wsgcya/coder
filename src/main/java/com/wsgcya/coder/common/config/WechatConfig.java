package com.wsgcya.coder.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ganchao
 * @date 2018-01-22 14:08
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "weixin")
public class WechatConfig {
    private String appId = "";
    private String appSecret;
    private String mchId;
    private String mchKey;
    private String token;
    private String createip;
    private Integer accessType = 1;
    /**
     *  是否授权刷新token true为允许
     */
    private Boolean refreshToken = false;
    private String externalTokenUrl ="";

}
