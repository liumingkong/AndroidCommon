package com.black.common.net;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;

import com.black.common.utils.Utils;

import java.util.Locale;

/**
 * APN工具类
 */
public class APNUtil {
    private static final String TAG = "APNUtil";
    public static final int MPROXYTYPE_CMWAP = 1;
    public static final int MPROXYTYPE_WIFI = 2;
    /**
     * cmnet
     */
    public static final int MPROXYTYPE_CMNET = 4;
    /**
     * uninet服务器列表
     */
    public static final int MPROXYTYPE_UNINET = 8;
    public static final int MPROXYTYPE_UNIWAP = 16;
    public static final int MPROXYTYPE_NET = 32;
    public static final int MPROXYTYPE_WAP = 64;
    public static final int MPROXYTYPE_DEFAULT = 128;
    public static final int MPROXYTYPE_CTNET = 256;
    public static final int MPROXYTYPE_CTWAP = 512;
    public static final int MPROXYTYPE_3GWAP = 1024;
    public static final int MPROXYTYPE_3GNET = 2048;
    private static Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");

    // apn属性类型
    public static final String APN_PROP_APN = "apn";
    // apn属性代理
    public static final String APN_PROP_PROXY = "proxy";
    // apn属性端口
    public static final String APN_PROP_PORT = "port";

    public static final byte APNTYPE_NONE = 0;//未知类型
    public static final byte APNTYPE_CMNET = 1;//cmnet
    public static final byte APNTYPE_CMWAP = 2;//cmwap
    public static final byte APNTYPE_WIFI = 3;//WiFi
    public static final byte APNTYPE_UNINET = 4;//uninet
    public static final byte APNTYPE_UNIWAP = 5;//uniwap
    public static final byte APNTYPE_NET = 6;//net类接入点
    public static final byte APNTYPE_WAP = 7;//wap类接入点
    public static final byte APNTYPE_CTNET = 8; //ctnet
    public static final byte APNTYPE_CTWAP = 9; //ctwap
    public static final byte APNTYPE_3GWAP = 10; //3gwap
    public static final byte APNTYPE_3GNET = 11; //3gnet

    public static String getAnpNameNet(Context context) {
        String netInfo = "mobile";
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null)
                return netInfo;

            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null)
                return netInfo;
            String typeName = info.getTypeName();
            if (typeName.toUpperCase(Locale.getDefault()).equals("WIFI")) { // wifi网络
                return "wifi";
            } else {
                String extraInfo = info.getExtraInfo().toLowerCase(Locale.getDefault());
                if (extraInfo.startsWith("cmwap")) { // cmwap
                    return "cmwap";
                } else if (extraInfo.startsWith("cmnet")
                        || extraInfo.startsWith("epc.tmobile.com")) { // cmnet
                    return "cmnet";
                } else if (extraInfo.startsWith("uniwap")) {
                    return "uniwap";
                } else if (extraInfo.startsWith("uninet")) {
                    return "uninet";
                } else if (extraInfo.startsWith("wap")) {
                    return "wap";
                } else if (extraInfo.startsWith("net")) {
                    return "net";
                } else if (extraInfo.startsWith("ctwap")) {
                    return "ctwap";
                } else if (extraInfo.startsWith("ctnet")) {
                    return "ctnet";
                } else if (extraInfo.startsWith("3gwap")) {
                    return "3gwap";
                } else if (extraInfo.startsWith("3gnet")) {
                    return "3gnet";
                } else if (extraInfo.startsWith("#777")) { // cdma
                    String proxy = getApnProxy(context);
                    if (proxy != null && proxy.length() > 0) {
                        return "cdma_ctwap";
                    } else {
                        return "cdma_ctnet";
                    }
                }
                if (!Utils.isEmptyString(extraInfo)) {
                    return extraInfo;
                }
            }
        } catch (Throwable e) {
        }
        return netInfo;
    }

    public static byte getApnType(Context context) {
        int netType = getMProxyType(context);

        if (netType == MPROXYTYPE_WIFI) {
            return APNTYPE_WIFI;
        } else if (netType == MPROXYTYPE_CMWAP) {
            return APNTYPE_CMWAP;
        } else if (netType == MPROXYTYPE_CMNET) {
            return APNTYPE_CMNET;
        } else if (netType == MPROXYTYPE_UNIWAP) {
            return APNTYPE_UNIWAP;
        } else if (netType == MPROXYTYPE_UNINET) {
            return APNTYPE_UNINET;
        } else if (netType == MPROXYTYPE_WAP) {
            return APNTYPE_WAP;
        } else if (netType == MPROXYTYPE_NET) {
            return APNTYPE_NET;
        } else if (netType == MPROXYTYPE_CTWAP) {
            return APNTYPE_CTWAP;
        } else if (netType == MPROXYTYPE_CTNET) {
            return APNTYPE_CTNET;
        } else if (netType == MPROXYTYPE_3GWAP) {
            return APNTYPE_3GWAP;
        } else if (netType == MPROXYTYPE_3GNET) {
            return APNTYPE_3GNET;
        }
        return APNTYPE_NONE;
    }

    /**
     * 获取系统APN
     *
     * @param context
     * @return
     */
    public static String getApn(Context context) {
        String strResult = "Mobile";
        try {
            Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null,
                    null, null, null);
            c.moveToFirst();
            if (c.isAfterLast()) {
                c.close();
                return null;
            }

            strResult = c.getString(c.getColumnIndex(APN_PROP_APN));
            c.close();
        } catch (Throwable throwable) {
        }
        return strResult;
    }
    public static String getApnProxy(Context context) {
        Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null,
                null, null, null);
        c.moveToFirst();
        if (c.isAfterLast()) {
            c.close();
            return null;
        }
        String strResult = c.getString(c.getColumnIndex(APN_PROP_PROXY));
        c.close();
        return strResult;
    }

    public static int getMProxyType(Context act) {
        try {
            ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null)
                return MPROXYTYPE_DEFAULT;

            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null)
                return MPROXYTYPE_DEFAULT;
            String typeName = info.getTypeName();
            if (typeName.toUpperCase(Locale.getDefault()).equals("WIFI")) { // wifi网络
                return MPROXYTYPE_WIFI;
            } else {
                String extraInfo = info.getExtraInfo().toLowerCase(Locale.getDefault());
                if (extraInfo.startsWith("cmwap")) { // cmwap
                    return MPROXYTYPE_CMWAP;
                } else if (extraInfo.startsWith("cmnet")
                        || extraInfo.startsWith("epc.tmobile.com")) { // cmnet
                    return MPROXYTYPE_CMNET;
                } else if (extraInfo.startsWith("uniwap")) {
                    return MPROXYTYPE_UNIWAP;
                } else if (extraInfo.startsWith("uninet")) {
                    return MPROXYTYPE_UNINET;
                } else if (extraInfo.startsWith("wap")) {
                    return MPROXYTYPE_WAP;
                } else if (extraInfo.startsWith("net")) {
                    return MPROXYTYPE_NET;
                } else if (extraInfo.startsWith("ctwap")) {
                    return MPROXYTYPE_CTWAP;
                } else if (extraInfo.startsWith("ctnet")) {
                    return MPROXYTYPE_CTNET;
                } else if (extraInfo.startsWith("3gwap")) {
                    return MPROXYTYPE_3GWAP;
                } else if (extraInfo.startsWith("3gnet")) {
                    return MPROXYTYPE_3GNET;
                } else if (extraInfo.startsWith("#777")) { // cdma
                    String proxy = getApnProxy(act);
                    if (proxy != null && proxy.length() > 0) {
                        return MPROXYTYPE_CTWAP;
                    } else {
                        return MPROXYTYPE_CTNET;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MPROXYTYPE_DEFAULT;
    }

    public static boolean isNetworkAvailable(Context act) {
        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isAvailable())
            return true;
        return false;
    }

    public static State getNetworkState(Context act) {
        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return null;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null)
            return info.getState();
        return null;
    }

    public static boolean isConnected(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected() || mobile.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
