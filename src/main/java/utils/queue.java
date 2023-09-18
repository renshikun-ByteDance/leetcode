package utils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class queue {

    public static void main(String[] args) {


//        ArrayQueue<> arrayQueue = new ArrayQueue<>();
        ArrayDeque<Object> objects = new ArrayDeque<>();

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        arrayList.add("c");
        arrayList.add("b");
        arrayList.add("c");

        arrayList.get(2);

        System.out.println(arrayList.indexOf("c"));
        System.out.println(arrayList.lastIndexOf("c"));


        LinkedList<String> link = new LinkedList<>();
        link.add("a");
        link.add("c");
        link.add("d");
        link.add("b");
        link.add("d");
        link.addFirst("f");
        link.addLast("l");

        System.out.println("最后一个 d 的位置: " + link.lastIndexOf("d"));

        // LinkedList是基于双向循环链表实现的双向队列
        // ArrayDeque是基于数组实现的双向队列，ArrayDeque 可以当作 FIFO（队列）来使用，也可以当作LIFO（栈）来使用

        ArrayDeque<String> dequeQueue = new ArrayDeque<>();   //队首为最先进入的元素，队尾为后进入的元素
        dequeQueue.add("a");
        dequeQueue.add("b");
        dequeQueue.add("c");   //向队尾添加元素，类似FIFO，队首在前，队尾在后，add为向队尾添加元素，addFirst为向队首添加元素
        dequeQueue.addFirst("z");
        System.out.println("Deque first : " + dequeQueue.peek());    //获取队首元素，只查询
        System.out.println("Deque last : " + dequeQueue.getLast());  //获取队尾元素，只查询
        System.out.println(Arrays.toString(dequeQueue.toArray()));


        // 优先队列：可自定义排序的顺序，分为"大根堆"和"小根堆"，但只能从 队首取元素
        PriorityQueue<String> sortedQueue = new PriorityQueue<>();  //默认 小根堆

        // 1、FIFO 先进先出的队列，只能从队首取元素
        ConcurrentLinkedQueue<String> fifoQueue = new ConcurrentLinkedQueue<>();

        fifoQueue.add("a");
        fifoQueue.add("b");
        fifoQueue.add("c");
        fifoQueue.add("d");

        String poll = fifoQueue.poll();
        System.out.println("队首元素: " + poll);

        // 2、LIFO 后进先出的队列，只能从队首取元素a
        Stack<String> lifoQueue = new Stack<>();
        lifoQueue.push("a");
        lifoQueue.push("b");
        lifoQueue.push("c");
        System.out.println("lifoQueue 队首元素为: "+lifoQueue.peek()); //不取出，直接读取队首元素
    }


}
