package com.softserveinc.webapp.utils;

public class PhoneNumberGenerator {

    public static long generate() {
        long leftLimit = 1000000000L;
        long rightLimit = 9999999999L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
