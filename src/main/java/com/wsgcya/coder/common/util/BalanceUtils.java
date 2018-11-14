package com.wsgcya.coder.common.util;


import java.math.BigDecimal;

/**
 * 结算工具类 返回值只保留2为小数 即精确的分
 *
 * @Version 1.0
 * Created by yewangwang on 2017年5月26日11:40:13
 */
public class BalanceUtils {

    public static int FENG = 0;
    public static int JIAO = 1;
    public static int YUAN = 2;
    private static int scale = 10;

    /**
     * 单位转换，如果小数点后面没有有效数字，取整
     *
     * @param fromUnit
     * @param toUnit
     * @param num
     * @param round
     * @return
     */
    public static String convert(int fromUnit, int toUnit, String num, boolean round) {
        String str = convert(fromUnit, toUnit, num);
        if (round) {
            BigDecimal money = new BigDecimal(str);
            double d = money.doubleValue();
            if (d % 1 == 0) {
                return String.valueOf(money.intValue());
            } else {
                return str;
            }
        }
        return str;
    }

    /**
     * 转换单位，保留2为小数
     * @param fromUnit
     * @param toUnit
     * @param num
     * @return
     */
    public static String convert(int fromUnit, int toUnit, String num) {
        BigDecimal nb = new BigDecimal(num);
        if ((FENG == fromUnit && JIAO == toUnit)
                || (JIAO == fromUnit && YUAN == toUnit)) {
            //分 -> 角 ，角->元
            nb = new BigDecimal(divide(num, scale + ""));
        } else if ((JIAO == fromUnit && FENG == toUnit)
                || (YUAN == fromUnit && JIAO == toUnit)) {
            //角 -> 分 ，元->角
            nb = new BigDecimal(multiplication(num, scale + ""));

        } else if (FENG == fromUnit && YUAN == toUnit) {
            // 分 -> 元
            nb = new BigDecimal(divide(num, (scale * scale) + ""));
        } else if (YUAN == fromUnit && FENG == toUnit) {
            // 元 -> 分
            nb = new BigDecimal(multiplication(num, (scale * scale) + ""));
        } else {
           // throw new UnSupportConvertException();
        }
        return nb.toString();
    }

    /**
     * 加法
     */
    public static String addition(String addend1, String addend2) {
        BigDecimal a1 = new BigDecimal(addend1);
        BigDecimal a2 = new BigDecimal(addend2);
        BigDecimal sum = a1.add(a2);
        return sum.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
    }

    /**
     * 减法
     *
     * @param minuend    被减数
     * @param subtrahend 减数
     */
    public static String subtraction(String minuend, String subtrahend) {
        BigDecimal min = new BigDecimal(minuend);
        BigDecimal sub = new BigDecimal(subtrahend);
        BigDecimal remainder = min.subtract(sub);
        return remainder.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
    }

    /**
     * 乘法
     */
    public static String multiplication(String multiplicand, String multiplier) {
        BigDecimal m1 = new BigDecimal(String.valueOf(multiplicand));
        BigDecimal m2 = new BigDecimal(String.valueOf(multiplier));
        BigDecimal sum = m1.multiply(m2);
        return sum.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
    }

    /**
     * 除法
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 商
     */

    public static String divide(String dividend, String divisor) {
        BigDecimal d1 = new BigDecimal(dividend);
        BigDecimal d2 = new BigDecimal(divisor);
        BigDecimal shang = d1.divide(d2, 2, BigDecimal.ROUND_HALF_EVEN);
        return shang.toString();
    }
}
