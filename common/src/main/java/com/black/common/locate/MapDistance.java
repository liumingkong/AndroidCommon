package com.black.common.locate;

/**
 * Created by liumingkong on 1/16/14.
 */
public class MapDistance {

    private final static double EARTH_RADIUS = 6378137d;
    private final static double RADIUS_CONSTANT = 180d;
    private final static double PRECISION_CONSTANT = 10000d;

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double radLat1 = deg2rad(lat1);
        double radLat2 = deg2rad(lat2);
        double a = radLat1 - radLat2;
        double b = deg2rad(lon1) - deg2rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * PRECISION_CONSTANT) / PRECISION_CONSTANT;
        return s;

    }

    //将角度转换为弧度
    public static double deg2rad(double degree) {
        return degree / RADIUS_CONSTANT * Math.PI;
    }

    //将弧度转换为角度
    public static double rad2deg(double radian) {
        return radian * 180 / Math.PI;
    }

}