package com.wsgcya.coder.wechat.wechataccess.dao;

import org.apache.ibatis.annotations.Param;
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
    String getTokenByAppid(@Param("appid") String appid);

    /**
    * @author: ganchao
    * @description: 更新token
    * @date: 2018/11/14 18:24
    * @param: [appid, token]
    * @return: void
    */
    void updateTokenByAppid(@Param("appid") String appid, @Param("token") String token);
}
