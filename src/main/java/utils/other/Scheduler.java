package utils.other;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Scheduler {

    static HashMap<String, String> esMap = new HashMap<>();

    public static void main(String[] args) {
        //周期性线程，用于周期性执行"微批"刷写 ES和 HBase的操作

        AtomicLong AtomicIncrementID = new AtomicLong(0);
        init();
        System.out.println("初始化映射关系为： " + esMap.toString());

        long initialDelay;
        long period;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String key = String.valueOf(AtomicIncrementID.getAndIncrement());
                esMap.put(key, "a" + key);
                System.out.println("当前: " + dateformat.format(System.currentTimeMillis()) + " 映射关系为 " + esMap.toString());
            }
        };

        //基于线程池设计的定时任务类。每个调度任务都会分配到线程池中的某一个线程去执行，任务就是并发调度执行的，任务之间互不影响。
        ScheduledExecutorService scheduledExecutor =
                Executors.newScheduledThreadPool(1);  //创建一个定长线程池，支持定时及周期性任务执行、延迟执行

        //FIFO 此次终止后，间隔指定时长，再触发下次开始
        ScheduledFuture scheduledFuture =
                scheduledExecutor.scheduleWithFixedDelay(
                        runnable,
                        5,
                        5,
                        TimeUnit.SECONDS);


    }


    public static void init() {
        esMap.put("name", "b0");
        esMap.put("phone", "b1");
        esMap.put("adress", "b2");
    }
}
