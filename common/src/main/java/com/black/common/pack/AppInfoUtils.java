package com.black.common.pack;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.black.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumingkong on 15/6/6.
 */
public class AppInfoUtils {

    // 获取系统中所有APP的信息列表
    public static List<AppInfo> getDeviceAppInfo(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();
        try {
            PackageManager pm = context.getPackageManager();
            // 查询条件
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            // 可选的查询条件
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (!Utils.isEmptyCollection(resolveInfos)) {
                for (ResolveInfo resolveInfo : resolveInfos) {
                    AppInfo appInfo = new AppInfo();
                    appInfo.appName = resolveInfo.loadLabel(pm).toString();// 获取应用名称
                    appInfo.packageName = resolveInfo.activityInfo.packageName;// 包名
                    appInfos.add(appInfo);
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return appInfos;
    }
}
