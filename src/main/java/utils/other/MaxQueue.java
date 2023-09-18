package utils.other;

import java.util.ArrayDeque;

public class MaxQueue {
    ArrayDeque<Integer> queue;
    ArrayDeque<Integer> downQueue;

    public MaxQueue() {
        this.queue = new ArrayDeque<>();
        this.downQueue = new ArrayDeque<>();
    }

    public int max_value() {
        return downQueue.isEmpty() ? -1 : downQueue.peekFirst();
    }

    public void push_back(int num) {
        queue.addLast(num);
        while (!downQueue.isEmpty() && downQueue.peekLast() < num) {  //维护降序排列的队列
            downQueue.pollLast();
        }
        downQueue.addLast(num);
    }

    public int pop_front() {
        if (queue.isEmpty()) return -1;
        if (!downQueue.isEmpty() && downQueue.peekFirst().equals(queue.peekFirst())) {
            downQueue.pollFirst();
        }
        return queue.pollFirst();
    }
}
