package utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeAndDate {
    public static void main(String[] args) {

        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("当前时间为: " + ft.format(date));
        System.out.println("当前时间为: " + dft.format(date));


        System.out.println(System.currentTimeMillis());
        String format = ft.format(new Date(System.currentTimeMillis()));

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Timestamp timestamp01 = new Timestamp(1655781360 * 1000L);
        Timestamp timestamp02 = new Timestamp(1655781380 * 1000L);
        Timestamp timestamp03 = new Timestamp(1655781400 * 1000L);
        Timestamp timestamp04 = new Timestamp(1655781362 * 1000L);
        Timestamp timestamp05 = new Timestamp(1655781423 * 1000L);
        System.out.println(timestamp);
        System.out.println(timestamp01);
        System.out.println(timestamp02);
        System.out.println(timestamp03);
        System.out.println(timestamp04);
        System.out.println(timestamp05);

    }
}
