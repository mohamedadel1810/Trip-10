package com.trip10.Trip10.util;

public class PhoneUtil {

    /**
     * Normalizes an Egyptian phone number to the canonical +20XXXXXXXXXX form,
     * so a number stored as "01012345678" and one looked up as "+201012345678"
     * (or vice versa) still match.
     */
    public static String normalize(String phone) {
        if (phone == null) return null;
        phone = phone.trim();
        if (phone.startsWith("+20")) return phone;
        if (phone.startsWith("0")) return "+20" + phone.substring(1);
        return phone;
    }
}
