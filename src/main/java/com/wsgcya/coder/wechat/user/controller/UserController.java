package com.wsgcya.coder.wechat.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wechat/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/ssss")
    public String toiii(){
        logger.info("2020210sdasfasfdxcxcxc");
        stringRedisTemplate.opsForValue().set("sssss","sssss");
        return "/user/kkk";
    }


}
