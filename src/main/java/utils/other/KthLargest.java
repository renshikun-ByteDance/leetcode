package utils.other;

import java.util.PriorityQueue;

public class KthLargest {

    private int k;
    private PriorityQueue<Integer> sortedQueue;

    //---------------------------------------------------
    // 维护一个长度固定 K的小根队列（从小到大）
    //    即第 K 大的元素就是 堆顶（队列长度不够 K 时，返回的是 堆顶元素）
    //---------------------------------------------------

    public KthLargest(int k, int[] nums) {
        this.k = k;
        sortedQueue = new PriorityQueue<>((o1, o2) -> (o1 - o2));
        for (int num : nums) {
            add(num);
        }
    }

    public int add(int val) {
        sortedQueue.add(val);
        if (sortedQueue.size() > k) {
            sortedQueue.poll();
        }
        return sortedQueue.peek();  //不能取出
    }



}
