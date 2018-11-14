package com.wsgcya.coder.common.util.jsztdes;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;

/**
 * 3DES加密工具类,星级客服的加密代码
 * 
 */
public class DES3Util {
	// 密钥
	private final static String SECRETKEY = "askgakjgidign399ew3593er";
	// 向量
	private final static String IV = "2156ghrb";
	// 加解密统一使用的编码方式
	private final static String encoding = "utf-8";

	/**
	 * 3DES加密
	 * 
	 * @param plainText 普通文本
	 * @return
	 */
	public static String encrypt(String plainText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(SECRETKEY.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		return Base64.encode(encryptData);
	}

	/**
	 * 3DES解密
	 *
	 * @param encryptText 加密文本
	 * @return
	 */
	public static String decrypt(String encryptText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(SECRETKEY.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

		return new String(decryptData, encoding);
	}
	/**
	 * 3DES加密
	 * @param secretKey 秘钥
	 * @param iv 偏移向量
	 * @param plainText	普通文本
	 * @return
	 * @throws Exception
	 */
	public static String encryptString(String secretKey,String iv,String plainText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		return Base64.encode(encryptData);
	}
	/**
	 * URLEncoder编码加密信息
	 * @param secretKey
	 * @param iv
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String encryptStringURLEncoder(String secretKey,String iv,String plainText) throws Exception {
		String base64Str=encryptString(secretKey, iv, plainText);
		return URLEncoder.encode(base64Str);
	}
	/**
	 * 3DES解密
	 * @param secretKey 秘钥
	 * @param iv 偏移向量
	 * @param encryptText 密文
	 * @return
	 * @throws Exception
	 */
	public static String decryptString(String secretKey,String iv,String encryptText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

		return new String(decryptData, encoding);
		
	}
	/**
	 * 3DES解码后解密 
	 * @param secretKey 秘钥
	 * @param iv 偏移向量
	 * @param encryptText 密文
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String decryptStringURLDecoder(String secretKey,String iv,String encryptText) throws Exception {
		String retJsonStr=decryptString(secretKey, iv, URLDecoder.decode(encryptText));
		return retJsonStr;
	}
	
}
