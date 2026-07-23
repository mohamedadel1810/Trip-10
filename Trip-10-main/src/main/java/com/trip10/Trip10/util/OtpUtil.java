package com.trip10.Trip10.util;

public class OtpUtil {

    public static String generateCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
}
