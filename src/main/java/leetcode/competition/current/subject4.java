package leetcode.competition.current;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * 2022年中国民生银行信用卡中心"第五届 1024编程大赛"
 */
public class subject4 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String information = scanner.nextLine();
        String[] mainInfo = information.split("\\s+");
        int nums = Integer.parseInt(mainInfo[0]);
        int designNums = Integer.parseInt(mainInfo[1]);
        int devNums = Integer.parseInt(mainInfo[2]);
        int testNums = Integer.parseInt(mainInfo[3]);
        //未开始的任务，各个阶段的任务
        PriorityQueue<int[]> queue1 = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);  //小根堆，有序存储
        PriorityQueue<int[]> queue2 = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);  //小根堆，有序存储
        PriorityQueue<int[]> queue3 = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);  //小根堆，有序存储

        //正在处理的任务（复杂）
        ArrayDeque<int[]> workingQueue1 = new ArrayDeque<>();
        ArrayDeque<int[]> workingQueue2 = new ArrayDeque<>();
        ArrayDeque<int[]> workingQueue3 = new ArrayDeque<>();

        //初始化最初的缓存队列
        int count = 1;
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String[] job = nextLine.split("\\s+");
            int sort = Integer.parseInt(job[0]);
            int day1 = Integer.parseInt(job[1]);
            int day2 = Integer.parseInt(job[2]);
            int day3 = Integer.parseInt(job[3]);
            queue1.add(new int[]{sort, day1, day2, day3});
            //任务计数
            count++;
            if (count > nums) {
                break;
            }
        }

        int currentDay = 0;  //以天为进度，顺序执行
        while (!queue1.isEmpty() || !queue2.isEmpty() || !queue3.isEmpty()
                || !workingQueue1.isEmpty() || !workingQueue2.isEmpty() || !workingQueue3.isEmpty()) {

            //------------------------------------------------------------------------------------------------
            // 缓存队列 queue 进入处理队列 workingQueue，正在等待阶段 queue 的工作，每日进行判断，是否能够进入当前阶段，以及对人员的影响
            //------------------------------------------------------------------------------------------------

            //1、等待开始进入"设计阶段"的任务，判断能否进入设计阶段
            while (designNums > 0 && !queue1.isEmpty()) {  //有剩余设计人员，同时有待设计的任务
                int[] poll = queue1.poll();
                workingQueue1.add(poll);      //将其添加到开发阶段的队列中
                designNums--;                 //占用一个人进行处理
            }

            //2、等待开始进入"开发阶段"的任务，判断能否进入开发阶段
            while (devNums > 0 && !queue2.isEmpty()) {
                int[] poll = queue2.poll();
                workingQueue2.add(poll);
                devNums--;
            }

            //3、等待开始的"测试阶段"的任务，判断能否进入测试阶段
            while (testNums > 0 && !queue3.isEmpty()) {
                int[] poll = queue3.poll();
                workingQueue3.add(poll);
                testNums--;
            }

            //------------------------------------------------------------------------------------------------
            // 正在处理阶段 workingQueue 的工作，每日进行处理，影响剩余时长，判断是否进入下一个阶段，以及对人员的影响
            //------------------------------------------------------------------------------------------------

            //1、处于设计阶段的任务
            int size1 = workingQueue1.size();
            while (!workingQueue1.isEmpty()) {
                int[] poll = workingQueue1.pollFirst();  //从队列开头取
                size1--;
                poll[1]--;
                if (poll[1] == 0) {   //设计阶段完成
                    designNums++;                         //释放一位设计人员
                    queue2.add(poll);                     //将任务缓存至进入等待开发阶段的队列中
                } else {
                    workingQueue1.addLast(poll);          //放回原队列尾部
                }
                //判断首轮是否处理完毕
                if (size1 == 0) {
                    break;
                }
            }

            //2、处于开发阶段的任务
            int size2 = workingQueue2.size();
            while (!workingQueue2.isEmpty()) {
                int[] poll = workingQueue2.pollFirst();  //从队列开头取
                size2--;
                poll[2]--;
                if (poll[2] == 0) {     //开发阶段完成
                    devNums++;                         //释放一位开发人员
                    queue3.add(poll);                  //将任务缓存至进入等待测试阶段的队列中
                } else {
                    workingQueue2.addLast(poll);       //放回原队列尾部
                }
                //判断首轮是否处理完毕
                if (size2 == 0) {
                    break;
                }
            }

            //2、处于测试阶段的任务
            int size3 = workingQueue3.size();
            while (!workingQueue3.isEmpty()) {
                int[] poll = workingQueue3.pollFirst();  //从队列开头取
                size3--;
                poll[3]--;
                if (poll[3] == 0) {     //测试阶段完成
                    testNums++;                         //释放一位测试人员
                                                        //此项目结束，无需进入任何队列
                } else {
                    workingQueue3.addLast(poll);       //放回原队列尾部
                }
                //判断首轮是否处理完毕
                if (size3 == 0) {
                    break;
                }
            }
            currentDay++;
        }
        System.out.println(currentDay);
    }
}
