package utils;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class TimeAndDate {
    public static void main(String[] args) throws ParseException {

//        String dateTime = "2023-01-03 23:59:59.9";

//        System.out.println(LocalDateTime.now());
//        String timeStr1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
//        String timeStr2 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
//        System.out.println("当前时间为:" + timeStr1);
//        System.out.println("当前时间为:" + timeStr2);
//
//        Date date = new Date();
//        System.out.println(date);
//
//        String dateTime = "2023-01-03 23:59:59.9";
//
//        Timestamp timestamp = new Timestamp(1672790399 * 1000L);
//        timestamp.setNanos(900000000);
//        System.out.println(timestamp);
//
//
////
////        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        SimpleDateFormat ft01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//        SimpleDateFormat ft02 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
//        SimpleDateFormat ft03 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//////        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
////        System.out.println("当前时间为: " + ft.format(dateTime));     //截取
//        System.out.println("当前时间为: " + ft01.format(timestamp));   //正常
//        System.out.println("当前时间为: " + ft02.format(timestamp));   //补零
//        System.out.println("当前时间为: " + ft03.format(timestamp));   //正常

//        System.out.println("当前时间为: " + dft.format(dateTime));


//
//        System.out.println(System.currentTimeMillis());
//        String format = ft.format(new Date(System.currentTimeMillis()));
//        Timestamp timestamp01 = new Timestamp(1655781360 * 1000L);
////
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        Timestamp timestamp01 = new Timestamp(1655781360 * 1000L);
//        Timestamp timestamp02 = new Timestamp(1655781380 * 1000L);
//        Timestamp timestamp03 = new Timestamp(1655781400 * 1000L);
//        Timestamp timestamp04 = new Timestamp(1655781362 * 1000L);
//        Timestamp timestamp05 = new Timestamp(1655781423 * 1000L);
//        Timestamp timestamp06 = new Timestamp(1654827262 * 1000L);
//        Timestamp testTimestramp = new Timestamp(1658459736 * 1000L);
////        Timestamp timestamp06 = new Timestamp(1656472085675 * 1000L);
//        Timestamp timestamp07 = new Timestamp(Long.parseLong("1592446898000"));
//        Timestamp timestamp1 = new Timestamp(Long.parseLong("1656832597000"));
//        System.out.println(timestamp1);
//        timestamp1.setNanos(2 * 100000000);
//        System.out.println(timestamp1);
//        Timestamp timestamp08 = new Timestamp(Long.parseLong("1656832597374"));
//        Timestamp timestamp09 = new Timestamp(Long.parseLong("1656832597000"));
//        Timestamp timestamp10 = new Timestamp(Long.parseLong("1657004696000"));
//        System.out.println(new Timestamp(1659928483 * 1000L));
//        System.out.println("基本类型：double 二进制位数：" + Double.SIZE);
//        System.out.println("包装类：java.lang.Double");
//        System.out.println("最小值：Double.MIN_VALUE=" + Double.MIN_VALUE);
//        System.out.println("最大值：Double.MAX_VALUE=" + Double.MAX_VALUE);


        String timeStamp_1 = "1676419200100";      // 模拟   2023-02-15 00:00:00.1
        String timeStamp_2 = "1676419200010";      // 模拟   2023-02-15 00:00:00.01
        String timeStamp_3 = "1676419200001";      // 模拟   2023-02-15 00:00:00.001

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  // S,SS,SSS 输出结果不同

        System.out.println("原始时间： 2023-02-15 00:00:00.1 , 转换后时间： " + sdf.format(new Date(Long.parseLong(timeStamp_1))));
        System.out.println("原始时间： 2023-02-15 00:00:00.01 , 转换后时间： " + sdf.format(new Date(Long.parseLong(timeStamp_2))));
        System.out.println("原始时间： 2023-02-15 00:00:00.001 , 转换后时间： " + sdf.format(new Date(Long.parseLong(timeStamp_3))));


        Timestamp timestamp = new Timestamp(1676419200 * 1000L);
        timestamp.setNanos(100000000);
        System.out.println(timestamp);
        System.out.println(timestamp.toString());
        System.out.println(sdf.format(timestamp));


//        Timestamp timestamp_1 = new Timestamp(1676419200 * 1000L);
//        timestamp.setNanos(10000000);
//        System.out.println(timestamp_1);
//        System.out.println(sdf.format(timestamp_1));


//        SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");  // S,SS,SSS 输出结果不同
//        String dateTime = "2023-05-15 08:00:00.1";
//        System.out.println(sdf01.parse(sdf01));


//        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2005-06-09");
//        String now = new SimpleDateFormat("yyyy年MM月dd日").format(date);
//
//        System.out.println(now);


        System.out.println("..............DATETIME_2........补充0.........");
        Date date02 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2023-05-15 08:00:00.6");
        System.out.println(date02.getTime());
        long time = date02.getTime();
        String now02 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(time);
        System.out.println(now02);


        System.out.println("\n\n");
        TimeZone LOCAL_TZ = TimeZone.getDefault();
        long currTime0 = 86399000;     //23:59:59
        long currTime1 = 86399900;     //23:59:59.9
        long currTime2 = 86399100;     //23:59:59.10
        long currTime3 = 86399100;     //23:59:59.100
        long currTime456 = 86399999;     //23:59:59.999
        System.out.println(new Time(currTime0 - LOCAL_TZ.getOffset(currTime0)));
        System.out.println(millsToTime(currTime0));
        System.out.println(millsToTime(currTime1));
        System.out.println(millsToTime(currTime2));
        System.out.println(millsToTime(currTime3));
        System.out.println(millsToTime(currTime456));

//        DataTimeUtil.secToTime(currTime)


        System.out.println("\n\n");
        System.out.println("....................................................");
        String datetime10 = "2023-02-22 00:00:00";
        String datetime11 = "2023-02-22 00:00:00.9";
        String datetime12 = "2023-02-22 00:00:00.68";
        String datetime13 = "2023-02-22 00:00:00.168";
        String datetime14 = "2023-02-22 00:00:00.7861";
        String datetime15 = "2023-02-22 00:00:00.58961";

        System.out.println(DateTimeFormat(datetime10));
        System.out.println(DateTimeFormat(datetime11));
        System.out.println(DateTimeFormat(datetime12));
        System.out.println(DateTimeFormat(datetime13));
        System.out.println(DateTimeFormat(datetime14));
        System.out.println(DateTimeFormat(datetime15));


        String str = "9.9999999999999999999999";
//        String str = "9.90";
        System.out.println(Double.parseDouble(str));
        System.out.println(new BigDecimal(str));

        String datetime06 = "2023-02-22 00:00:00.0001";
        String datetime07 = "2023-02-22 00:00:00";
        System.out.println(datetime06.length());
        System.out.println(datetime07.length());
        System.out.println(datetime06.substring(0, 23));
        String[] split = datetime07.split(".");
//        System.out.println(split[1].length());


        double nums01 = 2.4;
        double nums02 = 0.2;
        System.out.println(nums01 + nums02);

        double number1 = 1;
        double number2 = 20.2;
        double number3 = 300.03;
        double result = number1 + number2 + number3;
        System.out.println("使用double运算结果: " + result);


    }


    /**
     * 此方法将 8 位的时间戳转换 Time 类型的，注意 8 位的时间戳最大表示 3 位毫秒位，不包含微秒的信息
     */
    private static String millsToTime(Long time) {   //返回值定长，三位毫秒位，不足右侧补 0
        String timeStr = "";
        long hour = 0L;
        long minute = 0L;
        long second = 0L;
        long millisecond = 0L;
        if (time <= 0L) {
            return "00:00:00.000";
        } else {
            second = time / 1000L;
            minute = second / 60L;
            millisecond = time % 1000L;
            if (second < 60L) {
                timeStr = "00:00:" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else if (minute < 60L) {
                second = second % 60L;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else {
                hour = minute / 60L;
                minute = minute % 60L;
                second = second - hour * 3600L - minute * 60L;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second) + "." + unitFormat2(millisecond);
            }
            return timeStr;
        }
    }

    /**
     * 时分秒的格式转换
     */
    private static String unitFormat(long i) {
        String retStr = "";
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 毫秒的格式转换
     */
    private static String unitFormat2(long i) {
        String retStr = "";
        if (i >= 0 && i < 10)
            retStr = "00" + i;
        else if (i >= 10 && i < 100) {
            retStr = "0" + i;
        } else
            retStr = "" + i;
        return retStr;
    }


    private static String DateTimeFormat(String datetime) {
        //1、特殊情况，没有毫秒位的情况
        if (!datetime.contains(".")) {
            return datetime + ".000";
        }

        //2、正常情况，均有毫秒位的情况
        String mills = datetime.split("\\.")[1];

        //不足三位毫秒位，右侧补零
        if (mills.length() == 1) {
            return datetime + "00";
        }
        if (mills.length() == 2) {
            return datetime + "0";
        }
        //恰好三位毫秒位
        if (mills.length() == 3) {
            return datetime;
        }
        //超过三位毫秒位，截取
        if (mills.length() > 3) {
            return datetime.substring(0, 23);
        }

        return datetime;  //默认值，未撞上上述逻辑
    }


}
