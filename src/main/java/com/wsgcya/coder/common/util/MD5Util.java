package com.wsgcya.coder.common.util;




import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Locale;

public class MD5Util {  
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",  
            "8", "9", "a", "b", "c", "d", "e", "f"};  
  
    /** 
     * 转换字节数组为16进制字串 
     * @param b 字节数组 
     * @return 16进制字串 
     */  
    public static String byteArrayToHexString(byte[] b) {  
        StringBuilder resultSb = new StringBuilder();  
        for (byte aB : b) {  
            resultSb.append(byteToHexString(aB));  
        }  
        return resultSb.toString();  
    }  
  
    /** 
     * 转换byte到16进制 
     * @param b 要转换的byte 
     * @return 16进制格式 
     */  
    private static String byteToHexString(byte b) {  
        int n = b;  
        if (n < 0) {  
            n = 256 + n;  
        }  
        int d1 = n / 16;  
        int d2 = n % 16;  
        return hexDigits[d1] + hexDigits[d2];  
    }  
  
    /** 
     * MD5编码 
     * @param origin 原始字符串 
     * @return 经过MD5加密之后的结果 
     */  
    public static String MD5Encode(String origin) {  
        String resultString = null;  
        try {  
            resultString = origin;  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(resultString.getBytes("UTF-8"));  
            resultString = byteArrayToHexString(md.digest());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return resultString;  
    }

    public static String HMACSHA256(String data, String key) throws Exception {
        return HMACSHA256(data, key.getBytes("UTF-8"));
    }

    public static String HMACSHA256(String data, byte[] key) throws Exception {
        return HMACSHA256(data.getBytes("UTF-8"), key);
    }

    public static String HMACSHA256(byte[] data, byte[] key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data);
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    public static byte[] decodeHexUpper(String str) throws Exception {
        return Hex.decode(str.toLowerCase(Locale.US));
    }

}