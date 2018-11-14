package com.wsgcya.coder.wechat.wechataccess.service;

import java.util.Map;

public interface WechatEventService {
    /**
     *
     * @Title wechatEvent
     * @Class WechatApiService
     * @return String
     * @param body
     * @return
     * @Description 处理微信推送的消息
     * @author qinshijiang@telincn.com
     * @Date 2016年7月30日
     */
    String wechatEvent(Map<String,Object> body);
}
