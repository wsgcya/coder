package com.wsgcya.coder.common.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WechatUserBean implements Serializable{
    private String account;
    private String password;
    private String openId;
    private String name;
    private String idCard;
    private String telephone;
    private Integer sex;
    private String city;
    private String nickName;
    private String avatar;
    private String email;
    private Integer status;

}
