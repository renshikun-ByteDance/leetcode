package utils.other;

import java.util.ArrayDeque;

public class StockSpanner01 {

    private ArrayDeque<int[]> queue;
    private int currDay;

    public StockSpanner01() {
        this.queue = new ArrayDeque<>();
        queue.addLast(new int[]{0, Integer.MAX_VALUE});
        currDay = 0;
    }

    public int next(int price) {
        currDay++;
        while (!queue.isEmpty() && queue.peekLast()[1] <= price) {
            queue.pollLast();
        }
        int ans = currDay - queue.peekLast()[0];
        queue.addLast(new int[]{currDay, price});
        return ans;
    }


}
