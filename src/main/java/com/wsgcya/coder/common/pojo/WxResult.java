package com.wsgcya.coder.common.pojo;

/**
 * 微信返回结果
 *
 * @author yewangwang@telincn.com
 * @date 2017-07-31 14:45
 */
public class WxResult {
    private static final String SUCCESS_CODE = "0";

    private String errcode;
    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public boolean isSuccess() {
        return errcode == null || errcode.isEmpty() || errcode.equals(SUCCESS_CODE);
    }
}
