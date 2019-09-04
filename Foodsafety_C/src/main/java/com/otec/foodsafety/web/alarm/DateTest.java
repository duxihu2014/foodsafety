package com.otec.foodsafety.web.alarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类描述:
 * Created by duxihu on 2019/8/14 0014.
 */
public class DateTest {


    public static String getSpecifiedDayBefore(String specifiedDay){
    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-1);

        String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }
    /**
     * 获得指定日期的后一天
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay){
        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day+1);

        String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }


    public static String getDateString(int i){
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-i);

        String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        //System.out.println(dayAfter);
        return dayAfter;
    }
    public static void main(String[] args) {
        int j = 7;
        String date[] = new String[j];
        for (int i = 0; i <7 ; i++) {
            String datetime = DateTest.getDateString(i);
            date[i]=datetime;
            System.out.println(date[i]);
        }

        System.out.println(date.toString());
        //date = new String["01","02","03","04","05","06","07","08","09","10","11","12"];
        //date = new String{"01","02","03","04","05","06","07","08","09","10","11","12"};
//         date[] = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
//
//
//        String[] date=new String[]{"a","b"};
//       String[] str={"a","b"};


    }
}
