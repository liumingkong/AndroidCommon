package com.black.common.utils;

import android.os.Environment;
import android.os.StatFs;

/**
 * Sdcard 工具类
 */
public class SDCardUtil {

    public static final int MB = 1024 * 1024;
    public static final int MINIMUM_SDCARD_SPACE = 5; // 5MB

    private static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    public static boolean isSDCardReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean isSDCardWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && freeSpaceOnSd() >= MINIMUM_SDCARD_SPACE;
    }
}
