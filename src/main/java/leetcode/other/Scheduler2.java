package leetcode.other;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Scheduler2 {


    public static void open() {

        //基于线程池设计的定时任务类。每个调度任务都会分配到线程池中的某一个线程去执行，任务就是并发调度执行的，任务之间互不影响。
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);  //创建一个定长线程池，支持定时及周期性任务执行、延迟执行

        //FIFO 此次终止后，间隔指定时长，再触发下次开始
        ScheduledFuture scheduledFuture =
                scheduledExecutor.scheduleWithFixedDelay(
                        () -> {
                            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            System.out.println("当前: " + dateformat.format(System.currentTimeMillis()));
                        },
                        0,    //首次加载延迟 0 S
                        5,         //周期性加载的时间间隔
                        TimeUnit.SECONDS);

    }

}
