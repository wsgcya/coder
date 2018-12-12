package com.wsgcya.coder.timer;

import com.wsgcya.coder.wechat.wechataccess.service.TokenManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by ganchao
 * @date: 2018/11/14 13:17
 * @description:  使用spring自带定时器
 */
@Component
@Configurable
@EnableScheduling
public class TimeScheduledTasks {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenManagerService tokenManagerService;
    @Value("${wechat.appid}")
    private String appid;
    @Value("${wechat.appsecret}")
    private String appsecret;


    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void reportCurrentByCron(){
        tokenManagerService.refreshToken(appid,appsecret);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String ss =  LocalDateTime.now().format(formatter);
        logger.info(ss+">>>>>更新token完毕!");
    }
    
    public static void main(String[] args){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String ss =  LocalDateTime.now().format(formatter);
        //String ss =  LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(ss);
    }  
}
