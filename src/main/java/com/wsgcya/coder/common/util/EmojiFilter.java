package com.wsgcya.coder.common.util;


public class EmojiFilter {
    
    /**
     * 
     * @Title filter
     * @Class EmojiFilter
     * @return String
     * @param str
     * @return
     * @Description 过滤特殊字符
     * @author qinshijiang@telincn.com
     * @Date 2016年10月27日
     */
    public static String filter(String str){
        if(str == null || str.length() == 0){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<str.length()-1;i++){
            int ch = str.charAt(i);
            int min = Integer.parseInt("E001", 16);
            int max = Integer.parseInt("E537", 16);
            if(ch >= min && ch <= max){
                sb.append("");
            }else{
                sb.append((char)ch);
            }
        }
        return sb.toString();
    }
    
    /*@Test
    public void test(){
	String test = StringUtils.urlDecode("test%E2%AC%86%EF%B8%8F中文");
	System.out.println(test);
    }*/
}
