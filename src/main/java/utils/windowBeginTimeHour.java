package utils;

import java.sql.Timestamp;

public class windowBeginTimeHour {
    public static void main(String[] args) {

        long baseTime = 1661799064;

        //+ 8:00，基准为 1970-01-01 08:00:00
        long currentTime1 = baseTime * 1000L;                         //2022-08-30 02:51:04.0
        long currentTime2 = baseTime * 1000L + 8 * 60 * 60 * 1000L;   //2022-08-30 10:51:04.0
        long currentTime3 = baseTime * 1000L + 20 * 60 * 60 * 1000L;  //2022-08-30 22:51:04.0

        System.out.println("currentTime1: " + currentTime1 + " => " + new Timestamp(currentTime1));
        System.out.println("currentTime2: " + currentTime2 + " => " + new Timestamp(currentTime2));
        System.out.println("currentTime3: " + currentTime3 + " => " + new Timestamp(currentTime3));

        long windowSize = 24 * 60 * 60 * 1000L;  //24小时

        System.out.println("currentTime1 滚动窗口开始时间: " + new Timestamp(getWindowStartWithOffset(currentTime1, 0, windowSize)));  //2022-08-29 08:00:00.0
        System.out.println("currentTime2 滚动窗口开始时间: " + new Timestamp(getWindowStartWithOffset(currentTime2, 0, windowSize)));  //2022-08-30 08:00:00.0
        System.out.println("currentTime3 滚动窗口开始时间: " + new Timestamp(getWindowStartWithOffset(currentTime3, 0, windowSize)));  //2022-08-30 08:00:00.0
        //---------------------------------------
        // 由于 currentTime 时间戳含时区，其基于 1970-01-01 08:00:00（UTC +8）
        // 在下面计算窗口开始时间的函数中 % windowSize，其实获取的是 相较于 8:00:00，即获取的是给定 currentTime 上一个 8:00:00 的窗口
        //      因此，最终保留的开始时间一定是 1970-01-01 08:00:00 + n 个 windowSize（单元），也就是窗口的开始时间一定某天的 8 点，而非 0点
        // 所以，会导致 currentTime1 和 currentTime2 分别属于不同的窗口，和预期不同
        //---------------------------------------

        long offset = -8 * 60 * 60 * 1000L;
        System.out.println("currentTime1 滚动窗口开始时间(offset): " + new Timestamp(getWindowStartWithOffset(currentTime1, offset, windowSize)));  //2022-08-30 00:00:00.0
        System.out.println("currentTime2 滚动窗口开始时间(offset): " + new Timestamp(getWindowStartWithOffset(currentTime2, offset, windowSize)));  //2022-08-30 00:00:00.0
        System.out.println("currentTime3 滚动窗口开始时间(offset): " + new Timestamp(getWindowStartWithOffset(currentTime3, offset, windowSize)));  //2022-08-30 00:00:00.0

    }


    private static long getWindowStartWithOffset(long timestamp, long offset, long windowSize) {
        long xxx = (timestamp - offset + windowSize) % windowSize;
        long yyy = (timestamp - offset) % windowSize;
//        return timestamp - (timestamp - offset ) % windowSize;
        return timestamp - (timestamp - offset + windowSize) % windowSize;
    }


}
