package com.imooc.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private  static final String salt="1a2b3c4d";

//两次md5防止数据库泄露
    public static  String inputPassToFormPass(String inputPass){
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass +salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    public static  String formPassToDbPass(String formPass,String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass +salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String input,String saltDB){
        String formPass = inputPassToFormPass(input);//明文密码转换为form密码
        String dbPass = formPassToDbPass(formPass,saltDB);
        return dbPass;
    }
    public static void main (String[] args){
        System.out.println(inputPassToDbPass("womenyiqi1314","1a2b3c4d"));
    }
}
