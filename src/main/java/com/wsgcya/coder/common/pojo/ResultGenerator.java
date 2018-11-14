package com.wsgcya.coder.common.pojo;


/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genFailResult(String code, String message) {
        return new Result()
                .setCode(code)
                .setMsg(message);
    }

    public static Result genFailResult(ResultCode resultCode) {
        return new Result()
                .setCode(resultCode)
                .setMsg(resultCode.getMsg());
    }

    public static Result genFailResult(ResultCode resultCode, String message) {
        return new Result()
                .setCode(resultCode)
                .setMsg(message);
    }

    public static Result genFailResult(String code, String message, Object data) {
        return new Result()
                .setCode(code)
                .setMsg(message)
                .setData(data);
    }

    public static Result genSuccessPageResult() {
        return new PageResult()
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result genSuccessPageResult(Object data, Integer total) {
        return new PageResult()
                .setTotal(total)
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);


    }

    public static Result genSuccessPageResult(Object data, Integer total, Integer pageIndex) {
        return new PageResult()
                .setTotal(total)
                .setPageIndex(pageIndex)
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);


    }
}
