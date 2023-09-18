package utils.other;


import java.util.HashMap;

public class TopVotedCandidate {
    //动态记录候选人person和votes的状态
    HashMap<Integer, Integer> voteCounts;
    //记录每次投票后，当前的票王是谁，即<time,person>
    HashMap<Integer, Integer> timeTop;
    //选举时间
    int[] times;

    public TopVotedCandidate(int[] persons, int[] times) {
        //每次选举后，每个person的得票votes状态
        voteCounts = new HashMap<>();
        //每个时刻的票王
        timeTop = new HashMap<>();
        int topVotes = 0;
        for (int i = 0; i < persons.length; i++) {
            //times[i]时刻为persion[i]投一票
            voteCounts.put(persons[i], voteCounts.getOrDefault(persons[i], 0) + 1);
            Integer tempVotes = voteCounts.get(persons[i]);
            //投票后，如果persion[i]的累积票数大于等于当前的最大票数，则记录times[i]时刻的票王为persion[i]
            if (tempVotes >= topVotes) {
                timeTop.put(times[i], persons[i]);
                topVotes = tempVotes;
            } else
                //否则，times[i]时刻的票王仍未上一个时刻tiems[i-1]的票王
                timeTop.put(times[i], timeTop.get(times[i - 1]));    //不会有数组越界的情况，因为第一个时刻不会走此
        }
        this.times = times;
        System.out.println("voteCounts:" + voteCounts);
        System.out.println("timeTop:" + timeTop);
    }

    //基于二分搜索，找到小于等于查询时刻t的最大投票时间v
    //即找到满足 times[v] <= t 的最大的 l
    //二分方式一：通过变量记录搜索值
    public int q(int t) {
        int left = 0;
        int right = times.length - 1;
        int k = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);   //向左倾斜
            if (times[mid] <= t) {
                k = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        System.out.println("k: " + k);
        System.out.println("left: " + left);
        return timeTop.get(times[k]);
    }

    //二分方式二：通过left或right记录搜索值，但需要进行复杂的判断
    public int q01(int t) {
        int left = 0;
        int right = times.length - 1;
        int k = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);   //向左倾斜
            if (times[mid] < t) {                    //本题中，k和等号同时使用
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
//        System.out.println("k: " + k);
//        System.out.println("left: " + left);
        int mm = -1;
        if (left == times.length)
            mm = times[left - 1];
        else if (times[left] == t)
            mm = times[left];
        else if (times[left] > t)
            mm = times[left - 1];
        return timeTop.get(mm);
    }

    //二分方式三：通过left和right记录搜索值，最终状态left==right
    public int q02(int t) {
        int left = 0;
        int right = times.length - 1;
        int k = 0;
        while (left < right) {       //一定不能写等号，因为下面写法的最终状态 left==right，如果此处有等号，会进入死循环   ****待深入理解
            int mid = left + ((right - left + 1) >> 1);   //向右倾斜
            if (times[mid] <= t) {                        //本题中，k和等号应该同时使用，因此计算mid的时候，应该+1使其向右倾斜，防止在区间剩余两个元素时进入死循环
                //在搜索区间变为[m,m+1]时，按照常规的(right + left)/2方式计算mid时;此时left=mid进入死循环
                //因此，在if中的等号和left/right=mid 同时使用时，计算mid的方式应该发生改变 +1
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return timeTop.get(times[left]);
    }


}