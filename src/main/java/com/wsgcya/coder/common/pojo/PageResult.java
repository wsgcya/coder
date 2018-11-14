package com.wsgcya.coder.common.pojo;

import com.alibaba.fastjson.JSON;

/**
 * 统一API响应结果封装
 */
public class PageResult extends Result {

    private Integer total;
    private Integer pageIndex;


    public Integer getTotal() {
        return total;
    }

    public PageResult setTotal(Integer total) {
        this.total = total;
        return this;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public PageResult setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
