package utils.review;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
//import java.util.Date;

public class TimeAndDays {

    /**
     * Used for time conversions from SQL types.
     */
    private static final TimeZone LOCAL_TZ = TimeZone.getDefault();


    public static void main(String[] args) throws InterruptedException, ParseException {
        //------------------------
        // java.time.LocalDate
        //    https://blog.csdn.net/qq_31635851/article/details/117880835
        //------------------------
//        LocalDate localDate = LocalDate.now();
//        LocalDate localDate2 = LocalDate.parse("2019-07-09");
//        displayObjectClass(localDate);
//        System.out.println(localDate);

        //------------------------
        // java.time.LocalTime
        //    https://www.runoob.com/java/java8-datetime-api.html
        //------------------------
//        LocalTime now = LocalTime.now();
//        displayObjectClass(now);
//        System.out.println(now);
//
//        // 获取当前时间日期
//        ZonedDateTime date1 = ZonedDateTime.parse("2015-12-03T10:15:30+05:30[Asia/Shanghai]");
//        System.out.println("date1: " + date1);
//
//        ZoneId id = ZoneId.of("Europe/Paris");
//        System.out.println("ZoneId: " + id);
//
//        ZoneId currentZone = ZoneId.systemDefault();
//        System.out.println("当期时区: " + currentZone);
//
//        long timestramp = System.currentTimeMillis();
//        System.out.println(timestramp);
//        Date date = new Date(timestramp);
//        System.out.println("Data: " + date);
//        System.out.println("getTime: " + date.getTime());
//
//
//        final long converted = toEpochMillis(date);
//        System.out.println(converted);
//        System.out.println((int) (converted / 86400000L));
//        System.out.println("year: " + (1970 + 19153 / 365) + " month: " + ((19153 % 365) / 12));
//
//        System.out.println(".............");
//
//        HashMap<String, Object> abc = new HashMap<>();
//        abc.put("a", 1);
//        abc.put("b", 2);
//        abc.put("c", null);
//        abc.put("d", "");
//        abc.put("e", true);
//
//
//        HashMap<String, Object> efg = new HashMap<>();
//        efg.put("cust_name", String.class);
//
//
//        System.out.println(abc.toString());
//        System.out.println(abc.get("c"));
////        System.out.println(abc.get("c").toString().length());
//        System.out.println(abc.get("d"));
//
//        System.out.println(DigestUtils.md5Hex("UnionMapFunction"));
//
//        Boolean eeeeee = (Boolean) abc.get("e");
//        displayObjectClass(eeeeee);
//        System.out.println(eeeeee);
//
//
//        System.out.println(new Timestamp(1661323062 * 1000L));  //source: ts_ms
//        System.out.println(new Timestamp(0));
//
//        TimeZone LOCAL_TZ = TimeZone.getDefault();
//        System.out.println(LOCAL_TZ);
//        int offset = LOCAL_TZ.getOffset(1661351862 * 1000L);
//
//        System.out.println(new Timestamp(1661351862 * 1000L));  //CREATE_TIME
//        System.out.println(new Timestamp(1661351862 * 1000L - offset));  //CREATE_TIME

//        LocalDate localDate2 = LocalDate.parse("2019-07-09");
//        LocalDate localDate3 = LocalDate.parse("2020-06-01 09:30");
//        LocalTime parse = LocalTime.parse("2020-06-01 09:30");
//        System.out.println(localDate2);
//        System.out.println(localDate3);
//        System.out.println(parse);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = "2020-06-01 09:30";
        String str1 = "2020-06-01 07:30";
        java.util.Date parse1 = format.parse(str);
        java.util.Date parse2 = format.parse(str1);
        System.out.println(parse1);
        System.out.println(parse2);
        System.out.println(parse1.after(parse2));




    }


    private static void displayObjectClass(Object object) {
        if (object instanceof Boolean)
            System.out.println("boolean ......");
        else if (object instanceof String)
            System.out.println("对象是 java.util.String 类的实例");
        else if (object instanceof ArrayList)
            System.out.println("对象是 java.util.ArrayList 类的实例");
        else if (object instanceof Date) {
            System.out.println("对象是 " + object.getClass() + " 类的实例");
        } else if (object instanceof LocalDate) {
            System.out.println("对象是 " + object.getClass() + " 类的实例");
        } else if (object instanceof Time) {
            System.out.println("对象是 " + object.getClass() + " 类的实例");
        } else if (object instanceof LocalTime) {
            System.out.println("对象是 " + object.getClass() + " 类的实例");
        } else
            System.out.println("对象是 " + object.getClass() + " 类的实例");
    }

    private static long toEpochMillis(Date date) {
        final long time = date.getTime();
        return time + (long) LOCAL_TZ.getOffset(time);
    }


}
