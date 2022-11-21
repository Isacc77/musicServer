package com.example.musicserver.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        String password = "123456";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String newPassword = bCryptPasswordEncoder.encode(password);
        System.out.println("After encrypt: "+newPassword);
        boolean same_password_result = bCryptPasswordEncoder.matches(password,newPassword);
        System.out.println("Compare with right result: "+same_password_result);
        boolean other_password_result = bCryptPasswordEncoder.matches("987654",newPassword);
        System.out.println("Compare with false result: " + other_password_result);
    }
}
