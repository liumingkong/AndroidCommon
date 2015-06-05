package com.black.common.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by weijia on 14-2-28.
 */
public class MD5Util {
    private static final char[] HEX
            = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
    }

    /**
     * 计算文件的MD5
     *
     * @param fileName 文件的绝对路径
     * @return
     * @throws IOException
     */
    public static String md5(String fileName) {
        File f = new File(fileName);
        return md5(f);
    }

    /**
     * 计算文件的MD5，重载方法
     *
     * @param file 文件对象
     * @return
     * @throws IOException
     */
    public static String md5(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                                                 file.length());
            messageDigest.update(byteBuffer);
            return byte2hex(messageDigest.digest());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                Log.e("Common","error when close inputstream,exception={}", e);
            }
        }
    }

    /**
     * 计算文件的MD5，重载方法
     *
     * @param bytes 文件对象
     * @return
     * @throws IOException
     */
    public static String md5(byte[] bytes) {
        if (Utils.isEmptyByte(bytes)) return null;
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        messageDigest.update(bb);
        return byte2hex(messageDigest.digest());
    }

    public static String md5String(String instr) {
        String s = null;
        // 用来将字节转换成 16 进制表示的字�?
        try {
            messageDigest.update(instr.getBytes());
            byte tmp[] = messageDigest.digest(); // MD5 的计算结果是�?�� 128 位的长整数，
            // 用字节表示就�?16 个字�?
            char str[] = new char[16 * 2]; // 每个字节�?16 进制表示的话，使用两个字符，
            // �?��表示�?16 进制�?�� 32 个字�?
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第�?��字节�?��，对 MD5 的每�?��字节
                // 转换�?16 进制字符的转�?
                byte byte0 = tmp[i]; // 取第 i 个字�?
                str[k++] = HEX[byte0 >>> 4 & 0xf]; // 取字节中�?4 位的数字转换,
                // >>>
                // 为�?辑右移，将符号位�?��右移
                str[k++] = HEX[byte0 & 0xf]; // 取字节中�?4 位的数字转换
            }
            s = new String(str).toUpperCase(); // 换后的结果转换为字符�?

        } catch (Exception e) {

        }
        return s;
    }

    private static String byte2hex(byte bytes[]) {
        return byte2hex(bytes, 0, bytes.length);
    }

    private static String byte2hex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEX[(bt & 0xf0) >> 4];
        char c1 = HEX[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

}
