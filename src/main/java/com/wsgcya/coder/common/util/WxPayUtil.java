package com.wsgcya.coder.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class WxPayUtil {
    /**
     * Description: 生成请求流水
     *
     * @param random
     * @return
     */
    public static String operateRequestSeq(String random) {
        if (StringUtils.isEmpty(random)) {
            random = randomFor6();
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = sdf.format(date);
        return "WT" + now + random;
    }

    /**
     * Description: 生成订单号
     *
     * @param random
     * @return
     */
    public static String operateOrderId(String random) {
        if (StringUtils.isBlank(random)) {
            random = randomFor6();
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = sdf.format(date);
        return "WTO" + now + random;
    }



    /**
     * Description: 生成6位不重复的随机数
     *
     * @return
     * @author snc
     * @taskId
     */
    public static String randomFor6() {
        int[] array = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        };
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int random = 0;
        for (int i = 0; i < 6; i++) {
            random = random * 10 + array[i];
        }
        return String.valueOf(random);
    }
}
