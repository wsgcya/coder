package com.wsgcya.coder.common.util;

import java.math.BigDecimal;

/**
 * 流量单位
 *
 * @author yewangwang@telincn.com
 * @date 2017年4月11日
 */
public class FlowUtil {
    public static String KB = "KB";
    public static String MB = "MB";
    public static String GB = "GB";

    private static Double scale = 1024D;

    /**
     * 流量单位换算
     *
     * @param fromUnit
     * @param toUnit
     * @param flowNum
     * @return
     * @author yewangwang@telincn.com
     * @date 2017年4月11日
     */
    public static Double changeUnit(String fromUnit, String toUnit, Double flowNum) {
        if (flowNum < 0D) {
            return flowNum;
        }
        Double result = null;
        if (KB.equals(fromUnit) && MB.equals(toUnit) || MB.equals(fromUnit) && GB.equals(toUnit)) {
            result = flowNum / scale;
        }else if (KB.equals(fromUnit) && GB.equals(toUnit)) {
            result = flowNum / scale / scale;
        }else if (MB.equals(fromUnit) && KB.equals(toUnit) || GB.equals(fromUnit) && MB.equals(toUnit)) {
            result = flowNum * scale;
        }else if (GB.equals(fromUnit) && KB.equals(toUnit)) {
            result = flowNum * scale * scale;
        }
        return result;
    }

    /**
     * 流量单位换算
     *
     * @param fromUnit
     * @param toUnit
     * @param flowNum
     * @param pointNum
     *            保留的小数点位数,截尾
     * @return
     * @author yewangwang@telincn.com
     * @date 2017年4月11日
     */
    public static Double changeUnit(String fromUnit, String toUnit, Double flowNum, int pointNum) {
        if (flowNum < 0D) {
            return flowNum;
        }
        Double result = FlowUtil.changeUnit(fromUnit, toUnit, flowNum);
        BigDecimal decimal = new BigDecimal(result);
        result = decimal.setScale(pointNum, BigDecimal.ROUND_DOWN).doubleValue();
        return result;
    }

    /**
     * 
     * @Title changeUnitCx
     * @Class FlowUtil
     * @return Double
     * @param flowNum
     * @return
     * @Description 流量计算从kb转为MB（同步查询页面的计算）
     * @author yujialin@hzchuangbo.com
     * @Date 2017年9月28日
     */
    public static Double changeUnitCx(Double flowNum) {
        Double calNum = 0D;
        if (flowNum >= 1048000) {
            calNum = flowNum / 1024 / 1024;
            BigDecimal decimal = new BigDecimal(calNum);
            if(calNum >= 100 && calNum < 1000){
                calNum = decimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            }else if(calNum >= 1000){
                calNum = Math.rint(calNum);
            }else{
                calNum = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            calNum = calNum * 1024;
        }else{
            calNum = flowNum / 1024;
            calNum = Math.rint(calNum);
        }
        return calNum;
    }
    
    /**
     * 
     * @Title changeUnitCxShow
     * @Class FlowUtil
     * @return Double
     * @param fromUnit
     * @param toUnit
     * @param flowNum
     * @param pointNum
     * @return
     * @Description 小程序首页显示的流量计算
     * @author yujialin@hzchuangbo.com
     * @Date 2017年9月28日
     */
    public static Double changeUnitCxShow(String fromUnit, String toUnit, Double flowNum, int pointNum){
        if(flowNum < 0D) {
            return 0D;
        }
        Double result = FlowUtil.changeUnit(fromUnit, toUnit, flowNum);
        BigDecimal decimal = new BigDecimal(result);
        result = decimal.setScale(pointNum, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

}
