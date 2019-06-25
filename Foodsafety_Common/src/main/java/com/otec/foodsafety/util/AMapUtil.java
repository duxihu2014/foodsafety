package com.otec.foodsafety.util;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class AMapUtil {

     static class GeoCoordinate {
        private double latitude;
        private double longitude;

        public double getLatitude()
        {
            return this.latitude;
        }

        public double getLongitude()
        {
            return this.longitude;
        }

        public GeoCoordinate(double latitude, double longitude)
        {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public String toString()
        {
            return String.format("%f,%f", longitude, latitude);
        }
    }

    public GeoCoordinate GetCenterPointFromListOfCoordinates(
            List<GeoCoordinate> geoCoordinateList) {
        int total = geoCoordinateList.size();
        double X = 0, Y = 0, Z = 0;
        for(GeoCoordinate g : geoCoordinateList)
        {
            double lat, lon, x, y, z;
            lat = g.getLatitude() * Math.PI / 180;
            lon = g.getLongitude() * Math.PI / 180;
            x = Math.cos(lat) * Math.cos(lon);
            y = Math.cos(lat) * Math.sin(lon);
            z = Math.sin(lat);
            X += x;
            Y += y;
            Z += z;
        }
        X = X / total;
        Y = Y / total;
        Z = Z / total;
        double Lon = Math.atan2(Y, X);
        double Hyp = Math.sqrt(X * X + Y * Y);
        double Lat = Math.atan2(Z, Hyp);

        GeoCoordinate center = new GeoCoordinate(Lat * 180 / Math.PI, Lon * 180 / Math.PI);
        return center;
    }

    public static double formatDouble3(double d, int digits) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        // 保留digits位小数
        numberFormat.setMaximumFractionDigits(digits);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        numberFormat.setRoundingMode(RoundingMode.UP);
        return Double.valueOf(numberFormat.format(d));
    }

    public static String getCenter(String[] points){
        AMapUtil aMapUtil = new AMapUtil();
        List list = new ArrayList();

         for (String p:points){
             String[] coordinate=p.split(",");
             list.add(new AMapUtil.GeoCoordinate(Double.parseDouble(coordinate[1]), Double.parseDouble(coordinate[0])));
         }
        GeoCoordinate center = aMapUtil.GetCenterPointFromListOfCoordinates(list);
        return center.toString();
     }

    public static String getCenter(List<String[]> points){
        AMapUtil aMapUtil = new AMapUtil();
        List list = new ArrayList();

        for (String[] coordinate : points){
            list.add(new AMapUtil.GeoCoordinate(Double.parseDouble(coordinate[1]), Double.parseDouble(coordinate[0])));
        }
        GeoCoordinate center = aMapUtil.GetCenterPointFromListOfCoordinates(list);
        return center.toString();
    }


    public static void main(String[] args) {
        AMapUtil aMapUtil = new AMapUtil();
        List list = new ArrayList();
        list.add(new AMapUtil.GeoCoordinate(30.938862d, 121.047653d));
        list.add(new AMapUtil.GeoCoordinate(30.933863d, 121.047241d));
        list.add(new AMapUtil.GeoCoordinate(30.933954d, 121.051567d));
        list.add(new AMapUtil.GeoCoordinate(30.939051d, 121.051399d));

        GeoCoordinate center = aMapUtil.GetCenterPointFromListOfCoordinates(list);
        out.println(center.toString());
    }
}
