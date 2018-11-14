package com.wsgcya.coder.common.util.jsztdes;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * 江苏掌厅加解密wapjs.189.cn
 * @Description 
 * @author qinshijiang@telincn.com
 * @Date 2016年9月27日
 */
public class JsDesUtil {
    private static final String UTF8 = "UTF-8";
    private static final String GBK = "GBK";
    private static String strDefaultKey = "a41s56df4a";
    private String charset;
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    private static final int FROM_MOBILE = 2;
    private static final int FROM_SERVER = 0;
    private static final int TO_MOBILE = 3;
    private static final int TO_SERVER = 1;

    public static String byteArr2HexStr(byte bytes[]) throws Exception {
	int iLen = bytes.length;
	StringBuffer sb = new StringBuffer(iLen * 2);
	for (int i = 0; i < iLen; i++) {
	    int intTmp;
	    for (intTmp = bytes[i]; intTmp < 0; intTmp += 256) {
	    }
	    if (intTmp < 16) {
		sb.append("0");
	    }
	    sb.append(Integer.toString(intTmp, 16));
	}

	return sb.toString();
    }

    public byte[] hexStr2ByteArr(String text) throws Exception {
	byte arrB[] = text.getBytes(charset);
	int iLen = arrB.length;
	byte arrOut[] = new byte[iLen / 2];
	for (int i = 0; i < iLen; i += 2) {
	    String strTmp = new String(arrB, i, 2);
	    arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
	}

	return arrOut;
    }

    public JsDesUtil() throws Exception {
	this(strDefaultKey);
    }

    public JsDesUtil(String key) throws Exception {
	charset = UTF8;
	encryptCipher = null;
	decryptCipher = null;
	Key keyTmp = getKey(key.getBytes(charset));
	encryptCipher = Cipher.getInstance("DES");
	encryptCipher.init(1, keyTmp);
	decryptCipher = Cipher.getInstance("DES");
	decryptCipher.init(2, keyTmp);
    }

    public byte[] encrypt(byte bytes[]) throws Exception {
	return encryptCipher.doFinal(bytes);
    }

    public byte[] decrypt(byte bytes[]) throws Exception {
	return decryptCipher.doFinal(bytes);
    }

    /**
     * 
     * @Title encrypt
     * @Class JsDesUtil
     * @return String
     * @param text
     * @param direction
     * @return
     * @throws Exception
     * @Description 加密方法 FROM_SERVER:0,TO_SERVER:1,FROM_MOBILE:2,TO_MOBILE:3
     * @author qinshijiang@telincn.com
     * @Date 2016年9月27日
     */
    public static String encrypt(String text, int direction) throws Exception {
	if (direction == FROM_MOBILE || direction == FROM_SERVER) {
	    throw new LawlessDataDirectionException("direction can't be FROM_MOBILE or FROM_SERVER when encrypt!!");
	}
	if (text == null) {
	    text = "";
	}
	JsDesUtil des = new JsDesUtil();
	if (direction == TO_MOBILE) {
	    des.setCharset(UTF8);
	}else if (direction == TO_SERVER) {
	    des.setCharset(GBK);
	}
	return byteArr2HexStr(des.encrypt(text.getBytes(des.getCharset())));
    }

    /**
     *
     * @Title decrypt
     * @Class JsDesUtil
     * @return String
     * @param text
     * @param direction
     * @return
     * @throws Exception
     * @Description 解密方法 FROM_SERVER:0,TO_SERVER:1,FROM_MOBILE:2,TO_MOBILE:3
     * @author qinshijiang@telincn.com
     * @Date 2016年9月27日
     */
    public static String decrypt(String text, int direction) throws Exception {
	if (direction == TO_MOBILE || direction == TO_SERVER) {
	    throw new LawlessDataDirectionException("direction can't be TO_MOBILE or TO_SERVER when decrypt!!");
	}
	JsDesUtil des = new JsDesUtil();
	if (direction == FROM_MOBILE) {
	    des.setCharset(GBK);
	}else if (direction == FROM_SERVER) {
	    des.setCharset(UTF8);
	}
	byte bytes[] = (byte[]) null;
	try {
	    bytes = des.decrypt(des.hexStr2ByteArr(text));
	}catch (Exception e) {
	    e.printStackTrace();
	}
	if (bytes != null) {
	    return new String(bytes);
	}else {
	    return text;
	}
    }

    public Key getKey(byte bytes[]) throws Exception {
	byte arrB[] = new byte[8];
	for (int i = 0; i < bytes.length && i < arrB.length; i++) {
	    arrB[i] = bytes[i];
	}

	Key key = new SecretKeySpec(arrB, "DES");
	return key;
    }

    /**
     *
     * @Title decryptWithBase64
     * @Class JsDesUtil
     * @return String
     * @param text
     * @param direction
     * @return
     * @throws Exception
     * @Description 解密方法base64 FROM_SERVER:0,TO_SERVER:1,FROM_MOBILE:2,TO_MOBILE:3
     * @author qinshijiang@telincn.com
     * @Date 2016年9月27日
     */
    public static String decryptWithBase64(String text, int direction) throws Exception {
	if (direction == TO_MOBILE || direction == TO_SERVER) {
	    throw new LawlessDataDirectionException("direction can't be TO_MOBILE or TO_SERVER when decrypt!!");
	}
	JsDesUtil des = new JsDesUtil();
	if (direction == FROM_MOBILE) {
	    des.setCharset(GBK);
	}else if (direction == FROM_SERVER) {
	    des.setCharset(UTF8);
	}
	byte bytes[] = (byte[]) null;
	try {
	    bytes = des.decrypt(Base64.decode(text));
	}catch (Exception e) {
	    e.printStackTrace();
	}
	if (bytes != null) {
	    return new String(bytes);
	}else {
	    return text;
	}
    }

    /**
     * 
     * @Title encryptWith64
     * @Class JsDesUtil
     * @return String
     * @param text
     * @param direction
     * @return
     * @throws Exception
     * @Description 加密方法base64 FROM_SERVER:0,TO_SERVER:1,FROM_MOBILE:2,TO_MOBILE:3
     * @author qinshijiang@telincn.com
     * @Date 2016年9月27日
     */
    public static String encryptWith64(String text, int direction) throws Exception {
	if (direction == FROM_MOBILE || direction == FROM_SERVER) {
	    throw new LawlessDataDirectionException("direction can't be FROM_MOBILE or FROM_SERVER when encrypt!!");
	}
	if (text == null) {
	    text = "";
	}
	JsDesUtil des = new JsDesUtil();
	if (direction == TO_MOBILE) {
	    des.setCharset(UTF8);
	}else if (direction == TO_SERVER) {
	    des.setCharset(GBK);
	}
	return Base64.encode(des.encrypt(text.getBytes(des.getCharset())));
    }

    public void setCharset(String charset) {
	this.charset = charset;
    }

    public String getCharset() {
	return charset;
    }

}
