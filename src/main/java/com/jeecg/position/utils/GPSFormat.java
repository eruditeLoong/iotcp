package com.jeecg.position.utils;

/**
 * @name: GPSFormat
 * @description:
 * @author: Erudite Loong
 * @version: 1.0
 * @time: 2019/9/26 0026 18:38
 * <p>
 * WGS-84：是国际标准，GPS坐标（Google Earth使用、或者GPS模块）
 * GCJ-02：中国坐标偏移标准，Google Map、高德、腾讯使用
 * BD-09：百度坐标偏移标准，Baidu Map使用
 * Mercator：墨卡托投影
 */
public class GPSFormat {

    public static final double PI = 3.14159265358979324D;
    public static final double X_PI = 3.14159265358979324D * 3000.0 / 180.0;

    /**
     * WGS84坐标系：即地球坐标系，国际上通用的坐标系。
     * 设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
     */
    //WGS-84 to GCJ-02
    public static GpsPoint gcj_encrypt(double wgsLng, double wgsLat) {
        if (outOfChina(wgsLng, wgsLat))
            return new GpsPoint(wgsLng, wgsLat);
        GpsPoint d = delta(wgsLng, wgsLat);
        return new GpsPoint(d.getLng() + wgsLng, d.getLat() + wgsLat);
    }

    //GCJ-02 to WGS-84
    public static GpsPoint gcj_decrypt(double gcjLng, double gcjLat) {
        if (outOfChina(gcjLng, gcjLat))
            return new GpsPoint(gcjLng, gcjLat);
        GpsPoint d = delta(gcjLng, gcjLat);
        return new GpsPoint(gcjLng - d.getLng(), gcjLat - d.getLat());
    }

    /**
     * GCJ02坐标系：即火星坐标系，
     * 是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
     */
    //GCJ02坐标转换BD09坐标
    public static GpsPoint GCJ02_BD09(double gcjLng, double gcjLat) {
        double x = gcjLat;
        double y = gcjLng;
        double z = Math.sqrt(x * x + y * y) + 0.00002D * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003D * Math.cos(x * X_PI);
        double BDLat = z * Math.sin(theta) + 0.006D;
        double BDLon = z * Math.cos(theta) + 0.0065D;
        return new GpsPoint(BDLon, BDLat);
    }

    //BD09坐标转换GCJ02坐标
    public static GpsPoint BD09_GCJ02(double bdLng, double bdLat) {
        double x = bdLng - 0.0065d, y = bdLat - 0.006d;
        double z = Math.sqrt(x * x + y * y) - 0.00002d * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003d * Math.cos(x * X_PI);
        double gcjLon = z * Math.cos(theta);
        double gcjLat = z * Math.sin(theta);
        return new GpsPoint(gcjLon, gcjLat);
    }

    /**
     * 墨卡托投影
     * 墨卡托(Mercator)投影，又名“等角正轴圆柱投影”，
     * 荷兰地图学家墨卡托（Mercator）在1569年拟定，
     * 假设地球被围在一个中空的圆柱 里，其赤道与圆柱相接触，
     * 然后再假想地球中心有一盏灯，把球面上的图形投影到圆柱体上，
     * 再把圆柱体展开，这就是一幅标准纬线为零度（即赤道）的“墨卡托投 影”绘制出的世界地图。
     */
    //WGS-84 to Web mercator
    //mercatorLat -> y mercatorLon -> x
    public static double[] WGS84_Mercator(double wgsLat, double wgsLng) {
        double x = wgsLng * 20037508.34D / 180.0;
        double y = Math.log(Math.tan((90.0 + wgsLat) * PI / 360.0)) / (PI / 180.);
        y = y * 20037508.34D / 180.0;
        double[] d = {x, y};
        return d;

        /*
        if ((Math.abs(wgsLng) > 180 || Math.abs(wgsLat) > 90))
             return null;
        double x = 6378137.0 * wgsLng * 0.017453292519943295D;
        double a = wgsLat * 0.017453292519943295D;
        double y = 3189068.5 * Math.log((1.0 + Math.sin(a)) / (1.0 - Math.sin(a)));
        return new Point(x, y);
        */
    }

    // Web mercator to WGS-84
    // mercatorLat -> y mercatorLon -> x
    public static GpsPoint Mercator_WGS84(double mercatorLng, double mercatorLat) {
        double x = mercatorLng / 20037508.34d * 180.;
        double y = mercatorLat / 20037508.34d * 180.;
        y = 180 / PI * (2 * Math.atan(Math.exp(y * PI / 180.0)) - PI / 2);
        return new GpsPoint(x, y);

        /*
        if (Math.abs(mercatorLng) < 180 && Math.abs(mercatorLat) < 90)
             return null;
        if ((Math.abs(mercatorLng) > 20037508.3427892D) || (Math.abs(mercatorLat) > 20037508.3427892d))
            return null;
        double a = mercatorLng / 6378137.0D * 57.295779513082323D;
        double x = a - (Math.floor(((a + 180.0) / 360.0)) * 360.0);
        double y = (1.5707963267948966D - (2.0 * Math.atan(Math.exp((-1.0 * mercatorLat) / 6378137.0d)))) * 57.295779513082323d;
        return new Point(x, y);
        */
    }

    //求两坐标距离
    public static double distance(double lngA, double latA, double lngB, double latB) {
        int earthR = 6371000;
        double x = Math.cos(latA * PI / 180.) * Math.cos(latB * PI / 180.) * Math.cos((lngA - lngB) * PI / 180);
        double y = Math.sin(latA * PI / 180.) * Math.sin(latB * PI / 180.);
        double s = x + y;
        if (s > 1) s = 1;
        if (s < -1) s = -1;
        double alpha = Math.acos(s);
        double distance = alpha * earthR;
        return distance;
    }

    //重载，根据两个点求距离
    public static double distance(GpsPoint p1, GpsPoint p2) {
        return distance(p1.getLng(), p1.getLat(), p2.getLng(), p2.getLat());
    }

    private static boolean outOfChina(double lng, double lat) {
        if (lng < 72.004 || lng > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    private static GpsPoint delta(double lng, double lat) {
        // Krasovsky 1940
        //
        // a = 6378245.0, 1/f = 298.3
        // b = a * (1 - f)
        // ee = (a^2 - b^2) / a^2;
        double a = 6378245.0; //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        double ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLon = transformLon(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
        return new GpsPoint(dLon, dLat);
    }
}

class GpsPoint {
    private double lat;
    private double lng;

    public GpsPoint() {
    }

    public GpsPoint(double lng, double lat) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Point [lat=" + lat + ", lng=" + lng + "]";
    }
}
