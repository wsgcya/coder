package com.wsgcya.coder.common.util;


import com.wsgcya.coder.common.pojo.Result;
import com.wsgcya.coder.common.pojo.ResultCode;
import com.wsgcya.coder.common.pojo.ResultGenerator;

import java.lang.reflect.Field;

/**
 * @Auther: caizhiyong@hzchuangbo.com
 * @ClassName: ObjectUtil
 * @Description: TODO(对象数据处理工具类)
 * @Date: 2018/09/20 00:23
 */
public class ObjectUtil {

    /**
     * 判断对象内成员变量的值是否为empty
     * @param object
     * @return
     * @throws Exception
     */
    public static Result isNotEmpty(Object object) throws Exception{
        if (object == null){
            return ResultGenerator.genFailResult(ResultCode.FAIL, "object is null");
        }
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldObj = field.get(object);
            if(fieldObj == null || "".equals(fieldObj)){
                String emptyResult = "[" + field.getName() + "] is empty";
                return ResultGenerator.genFailResult(ResultCode.FAIL, emptyResult);
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 判断对象内指定部分成员变量的值是否为empty
     * @param object
     * @param fieldNames
     * @return
     * @throws Exception
     */
    public static Result isNotEmpty(Object object, String ... fieldNames) throws Exception{
        if (object == null){
            return ResultGenerator.genFailResult(ResultCode.FAIL, "object is null");
        }
        Class clz = object.getClass();
        for (String fieldName : fieldNames) {
            Field field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldObj = field.get(object);
            if(fieldObj == null || "".equals(fieldObj)){
                String emptyResult = "[" + field.getName() + "] is empty";
                return ResultGenerator.genFailResult(ResultCode.FAIL, emptyResult);
            }
        }
        return ResultGenerator.genSuccessResult();
    }
}
