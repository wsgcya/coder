package com.wsgcya.coder.wechat.wechataccess.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface WechatMapper {
    /**
    * @author: ganchao
    * @description: 获取token
    * @date: 2018/11/14 17:59
    * @param: [appid]
    * @return: java.lang.String
    */
    String getTokenByAppid(String appid);

}
