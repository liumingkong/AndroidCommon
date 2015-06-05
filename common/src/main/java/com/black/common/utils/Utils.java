package com.black.common.utils;

import java.util.Collection;

/**
 * Created by liumingkong on 14-3-19.
 */
public class Utils {

    public static boolean isEmptyCollection(Collection collection) {
        if (isNull(collection) || isZero(collection.size())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNull(Object obj){
        if (obj == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyString(String str) {
        if (isNull(str)) {
            return true;
        } else {
            if (isZero(str.trim().length())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isZero(int num){
        if (num == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isZeroLong(long num){
        if (num == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isZeroDouble(double num){
        if (num == 0.0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyByte(byte[] content){
        if (isNull(content) || isZero(content.length)) {
            return true;
        } else {
            return false;
        }
    }
}
