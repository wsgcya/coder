package com.wsgcya.coder.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 电小二加解密
 * 
 * @ClassName AESUtil
 * @Description AES加密工具类
 * @author ZhuZihao
 * @Date 2017-6-2 下午4:22:01
 * @version 1.0.0
 */
public class AESUtil {

    /**
     * 加密
     * 
     * @param content
     *            需要加密的内容
     * @param password
     *            加密密码
     * @return
     */
    public static String encrypt(String content, String password) {
	byte[] encryptResult = encryptToBytes(content, password);
	return parseByte2HexStr(encryptResult);
    }

    /**
     * 加密
     * 
     * @param content
     *            需要加密的内容
     * @param password
     *            加密密码
     * @return 对加密结果进行base64
     */
    public static String encryptToFlow(String sSrc, String sKey) throws Exception {
	if (sKey == null) {
	    System.out.print("Key为空null");
	    return null;
	}
	// 判断Key是否为16位
	if (sKey.length() != 16) {
	    System.out.print("Key长度不是16位");
	    return null;
	}
	byte[] raw = sKey.getBytes("utf-8");
	SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

	return new String(Base64.encodeBase64(encrypted), "utf-8");// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /**
     * 加密
     * 
     * @param content
     *            需要加密的内容
     * @param password
     *            加密密码
     * @return
     */
    private static byte[] encryptToBytes(String content, String password) {
	try {
	    KeyGenerator kgen = KeyGenerator.getInstance("AES");
	    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	    random.setSeed(password.getBytes());
	    kgen.init(128, random);
	    SecretKey secretKey = kgen.generateKey();
	    byte[] enCodeFormat = secretKey.getEncoded();
	    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
	    Cipher cipher = Cipher.getInstance("AES");// 创建密码器
	    byte[] byteContent = content.getBytes("utf-8");
	    cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
	    byte[] result = cipher.doFinal(byteContent);
	    return result; // 加密
	}catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}catch (NoSuchPaddingException e) {
	    e.printStackTrace();
	}catch (InvalidKeyException e) {
	    e.printStackTrace();
	}catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}catch (IllegalBlockSizeException e) {
	    e.printStackTrace();
	}catch (BadPaddingException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 解密
     * 
     * @param encryptResultStr
     *            待解密内容
     * @param password
     *            解密密钥
     * @return
     */
    public static String decrypt(String encryptResultStr, String password) {
	byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);
	byte[] decryptResult = decryptToBytes(decryptFrom, password);
	try {
	    return new String(decryptResult, "UTF-8");
	}catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 解密
     * 
     * @param content
     *            待解密内容
     * @param password
     *            解密密钥
     * @return
     */
    private static byte[] decryptToBytes(byte[] content, String password) {
	try {
	    KeyGenerator kgen = KeyGenerator.getInstance("AES");
	    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	    random.setSeed(password.getBytes());
	    kgen.init(128, random);
	    SecretKey secretKey = kgen.generateKey();
	    byte[] enCodeFormat = secretKey.getEncoded();
	    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
	    Cipher cipher = Cipher.getInstance("AES");// 创建密码器
	    cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
	    byte[] result = cipher.doFinal(content);
	    return result; // 加密
	}catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}catch (NoSuchPaddingException e) {
	    e.printStackTrace();
	}catch (InvalidKeyException e) {
	    e.printStackTrace();
	}catch (IllegalBlockSizeException e) {
	    e.printStackTrace();
	}catch (BadPaddingException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 将二进制转换成16进制
     * 
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < buf.length; i++) {
	    String hex = Integer.toHexString(buf[i] & 0xFF);
	    if (hex.length() == 1) {
		hex = '0' + hex;
	    }
	    sb.append(hex.toUpperCase());
	}
	return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     * 
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
	if (hexStr.length() < 1)
	    return null;
	byte[] result = new byte[hexStr.length() / 2];
	for (int i = 0; i < hexStr.length() / 2; i++) {
	    int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
	    int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
	    result[i] = (byte) (high * 16 + low);
	}
	return result;
    }

    /**
     * 
     * @Title aesEncrypt
     * @Class AESUtil
     * @return String
     * @param content
     *            加密内容
     * @param key
     * @return
     * @throws Exception
     * @Description AES/ECB模式加密
     * @author wangzhe@hzchuangbo.com
     * @Date 2017年10月11日
     */
    public static String aesEncrypt(String content, String key) throws Exception {
	if (content == null || key == null) {
	    return null;
	}
	Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
	byte[] bytes = cipher.doFinal(content.getBytes("utf-8"));
	return new Base64().encodeToString(bytes);
    }

    /**
     * 
     * @Title aesDecrypt
     * @Class AESUtil
     * @return String
     * @param content
     *            解密内容
     * @param key
     * @return
     * @throws Exception
     * @Description AES/ECB模式解密
     * @author wangzhe@hzchuangbo.com
     * @Date 2017年10月11日
     */
    public static String aesDecrypt(String content, String key) throws Exception {
	if (content == null || key == null) {
	    return null;
	}
	Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
	byte[] bytes = new Base64().decode(content);
	bytes = cipher.doFinal(bytes);
	return new String(bytes, "utf-8");
    }

}
