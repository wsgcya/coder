package com.wsgcya.coder.common.pojo;

import lombok.Data;

@Data
public class WechatToken {
    private String accessToken;
    private String jsapiTicket;
    private String cardTicket;
}
