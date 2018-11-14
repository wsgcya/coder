package com.wsgcya.coder.common.enumpojo;

/**
 * 公众号授权类型
 *
 * @author ganchao
 * @date 2017-08-09 11:35
 */
public enum WechatAccessType {

    FULL_ACCESS(1, "全权授权"),
    THIRD_ACCESS(2, "第三方授权"),
    EXTERNAL_ACCESS(3,"外部获取") ;

    private Integer code;

    private String message;

    WechatAccessType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static WechatAccessType findByCode(Integer code) {
        for (WechatAccessType value : WechatAccessType.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
