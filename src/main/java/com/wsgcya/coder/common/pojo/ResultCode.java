package com.wsgcya.coder.common.pojo;

public enum ResultCode {
    //系统
    SUCCESS(                    "0",        "SUCCESS"),
    FAIL(                       "400",      "失败"),
    AUTH_FAIL(                  "401",      "认证失败"),
    ACCESS_DENIED(              "403",      "权限不足,拒绝访问"),
    SYSTEM_ERROR(               "500",      "小主，系统正在开小差，请稍后再试。"),

    //短信方面的
    GET_SMS_TO_OFTEN(           "40001",    "短信获取过于频繁"),
    VCODE_FAIL_TO_MUCH(         "40002",    "验证码错误次数过多"),
    VCODE_NOT_EXITS(            "40003",    "验证码不存在"),
    VCODE_HAS_EXPIRED(          "40004",    "验证码已过期"),
    ERROR_VCODE(                "40005",    "验证码错误"),

    //用户
    EMPTY_USER(                 "45000",    "未查询到用户"),
    UN_BIND_USER(               "45001",    "未绑定用户"),
    UN_SUBSCRIPTION_USER(       "45002",    "未关注用户"),
    UN_STAR_USER(               "45003",    "非星级用户"),
    IS_ZHENGQI_USER(            "45004",    "政企用户"),
    INVALID_PHONE_LENGTH(       "45005",    "无效的号码长度"),
    INVALID_PHONE(              "45006",    "无效的号码"),
    HAS_BIND_USER(              "45007",    "未绑定用户"),


    //业务方面
    INVALID_PARAM(              "50001",    "参数错误"),
    INVALID_DATE(               "50002",    "无效的时间"),
    EXCEPTION_ACTIVITY_STATUS(  "50003",    "活动状态异常"),
    EMPTY_OPENID(               "50004",    "openid为空"),
    UNBIND_FAIL(                "50005",    "解綁失败"),
    INVALID_NETWORK_OPERATOR_USER("50006",  "非当前运营商用户"),
    INVALID_BIND_PTYPE(         "50007",    "无效的绑定运营商"),
    EMPTY_PHONE_ACCOUNT(        "50008",    "该号码是空号"),
    UNIFY_SIGN_CHECK_FAIL(      "50009",    "统一支付签名校验失败"),
    NOT_HAS_RECORD(             "50010",    "未查询到记录"),
    INVALID_NJTELCOM_USER(      "50011",    "非南京电信用户"),
    INVALID_TELCOM_USER(        "50012",    "非中国电信用户"),
    INVALID_ORDER_STATUS(       "50013",    "订单状态异常"),
    INVALID_HBTELCOM_USER(      "50014",    "非湖北电信用户"),
    FEE_FORMAT_ERROR(           "50015",    "金额格式异常"),

    //活动
    ACTIVITY_NOT_START(         "60000",    "活动未开始"),
    ACTIVITY_END(               "60001",    "活动已结束"),
    ALREADY_RECEIVED(           "60002",    "已领取奖品"),
    MULTIPLE_BINDING(           "60003",    "多次绑定,非首次绑定"),
    NOT_FIRST_GET(              "60004",    "非首次领取奖品"),
    GET_PRIZE_PROGRESS(         "60005",    "领奖中，请稍后..."),
    ONE_DAY_PACKAGE_USER(       "60006",    "一元包日用户"),
    LOCK_NUMBER_FAIL(           "60007",    "锁定号卡失败"),
    NULL_PRIZE_POLL(            "60008",    "奖品领完啦"),
    NULL_CHANGE(                "60009",    "没有领奖机会"),
    PRIZE_NON_EXIST(            "60010",    "奖品不存在")






    ;
    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResultCode findByCode(String code){
        for(ResultCode errorCode : ResultCode.values()){
            if(errorCode.getCode().equals(code)){
                return errorCode;
            }
        }
        return null;
    }

    public static ResultCode findByName(String name){
        for(ResultCode errorCode : ResultCode.values()){
            if(errorCode.name().equals(name)){
                return errorCode;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
