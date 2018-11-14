package com.wsgcya.coder.common.pojo;

public class WechatOauthInfo {

    private String openid;
    private String accessToken;
    private String scope;
    private String redirectUrl;
    private int status;// 当前状态:0无code跳转,获取不到openid跳转;1:获取不到openid,三次跳转后放弃;2:成功获取openid
    private String nickname;
    private String headImgUrl;

    public String getOpenid() {
	return openid;
    }

    public void setOpenid(String openid) {
	this.openid = openid;
    }

    public String getAccessToken() {
	return accessToken;
    }

    public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
    }

    public String getScope() {
	return scope;
    }

    public void setScope(String scope) {
	this.scope = scope;
    }

    public String getRedirectUrl() {
	return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
	this.redirectUrl = redirectUrl;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
    }

    public String getHeadImgUrl() {
	return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
	this.headImgUrl = headImgUrl;
    }

}
