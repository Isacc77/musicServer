package com.example.musicserver.tools;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    //define salt
    private static final String salt = "1b2i3t4e";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }
    /**
     * Encryption for the first time: the analog front-end encrypts itself, and then transmits it to the back-end
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass) {
        String str = "" + salt.charAt(1) + salt.charAt(3) + inputPass
                + salt.charAt(5) + salt.charAt(6);
        return md5(str);
    }

    /**
     * 第2次MD5加密
     * @param formPass The password encrypted by the front end is passed to the back end for the second encryption
     * @param salt     Salt value in user database
     * @return
     */
    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5)
                + salt.charAt(4);
        return md5(str);
    }

    /**
     * combined above two functions
     * @param inputPass
     * @param saltDB
     * @return
     */

    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println("First encrypt:" + inputPassToFormPass("123456"));
        System.out.println("Second encrypt:" + formPassToDBPass(inputPassToFormPass("123456"),
                "1b2i3t4e"));
        System.out.println("1:" + inputPassToDbPass("123456", "1b2i3t4e"));
    }

}
