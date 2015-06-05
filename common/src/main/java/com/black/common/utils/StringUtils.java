package com.black.common.utils;

import java.util.regex.Pattern;

/**
 * Created by liumingkong on 15/6/4.
 */
public class StringUtils {

    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    // 判断是不是一个合法的电子邮件地址
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }
}
