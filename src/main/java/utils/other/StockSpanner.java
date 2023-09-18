package utils.other;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

/**
 * 901. 股票价格跨度
 */
public class StockSpanner {

    private ArrayDeque<Integer> queue;
    private ArrayList<Integer> nums;
    private int currDay;


    public StockSpanner() {
        this.queue = new ArrayDeque<>();
        this.nums = new ArrayList<>();
        this.currDay = 0;
    }

    public int next(int price) {
        currDay++;
        while (!queue.isEmpty() && nums.get(queue.peekLast() - 1) <= price) {
            queue.pollLast();
        }
        int left = queue.isEmpty() ? 0 : queue.peekLast();
        nums.add(price);
        queue.addLast(currDay);
        return currDay - left;
    }

}
