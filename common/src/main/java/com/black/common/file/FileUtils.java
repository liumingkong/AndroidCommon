package com.black.common.file;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by liumingkong on 15/5/25.
 */
public class FileUtils {

    // 从Assets读取文件内容
    public static String readFromAssets(Context context, String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader(context
                    .getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isFileExisted(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    // 创建目录
    public static File createFolder(String folder) {
        File file = new File(folder);

        if (!file.exists()) {
            String[] subfolder = folder.split(File.separator);
            StringBuilder sb = new StringBuilder();

            for (String subfolder0 : subfolder) {
                sb.append(subfolder0).append(File.separator);
                file = new File(sb.toString());

                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }
        return file;
    }
}
