package leetcode.algorithm;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class queue {

    /**
     * 1047. 删除字符串中的所有相邻重复项
     */
    public String removeDuplicates(String s) {//基于LIFO栈
        Stack<Character> stack = new Stack<>();    //基于先进后出 LIFO队列，即从队首拿的元素为后入组的，同时这个也不方便用FIFO的数据类型
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (!stack.isEmpty() && array[i] == stack.peek()) {
                stack.pop();
            } else {
                stack.add(array[i]);
            }
        }
        StringBuilder ans = new StringBuilder();
        while (!stack.isEmpty())          //从队首开始
            ans.append(stack.pop());
        return ans.reverse().toString();  //反转

//        for (Character c : stack) {   //从队尾开始处理，相当于 FIFO
//            ans.append(c);
//        }
//        return ans.toString();
    }

    public String removeDuplicates01(String s) { //基于双向队列实现
        ArrayDeque<Character> dequeQueue = new ArrayDeque<>();   //两端操作
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (!dequeQueue.isEmpty() && dequeQueue.getLast() == array[i])
                dequeQueue.removeLast();
            else
                dequeQueue.add(array[i]);  //add是向队尾添加元素
        }
        StringBuilder ans = new StringBuilder();
        while (!dequeQueue.isEmpty()) {
            ans.append(dequeQueue.poll());
        }
        return ans.toString();
    }

    public String removeDuplicates02(String s) {  //基于StringBuild实现
        StringBuilder ans = new StringBuilder();
        char[] array = s.toCharArray();
        int top = -1;
        for (int i = 0; i < array.length; i++) {
            if (ans.length() > 0 && ans.charAt(top) == array[i]) {  //注意：队列头的位置为 0，即最先进入队列的元素位置为 0
                ans.deleteCharAt(top);
                top--;
            } else {
                ans.append(array[i]);
                top++;
            }
        }
        return ans.toString();   //从头向尾读，故无需 反转
    }


    /**
     * 20. 有效的括号
     */
    public boolean isValid(String s) {   //基于栈 stack LIFO的特性
        HashMap<Character, Character> hTable = new HashMap<>();
        hTable.put(')', '(');
        hTable.put(']', '[');
        hTable.put('}', '{');
        Stack<Character> stack = new Stack<>();
        char[] array = s.toCharArray();
        for (char c : array) {
            if (!stack.isEmpty() && hTable.containsKey(c) && stack.peek() == hTable.get(c)) {
                stack.pop();  //取出无放回
            } else {
                stack.add(c);
            }
        }
        return stack.isEmpty();
    }


    /**
     * 1190. 反转每对括号间的子串
     */
    public String reverseParentheses(String s) {
        StringBuilder ans = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == ')') {
                StringBuilder builder = new StringBuilder();
                while (stack.peek() != '(') {     //借助 栈 的 LIFO和 StringBuilder的 FIFO，来实现反转
                    builder.append(stack.pop());  //从栈中取出无放回
                }
                stack.pop();  //剔除 '('
                for (Character c : builder.toString().toCharArray())  //借助 栈 的 LIFO和 StringBuilder的 FIFO，来实现反转
                    stack.push(c);
            } else {  //将字符和'('均记录至stack中
                stack.push(array[i]);
            }
        }
//        while (!stack.isEmpty()) {
//            ans.append(stack.pop());
//        }
//        return ans.reverse().toString();

        for (Character c : stack) {     //巧妙使用集合：将 Stack非先进先出 LIFO，转换为 FIFO
            ans.append(c);
        }
        return ans.toString();
    }

    public String reverseParentheses01(String s) {
        StringBuilder ans = new StringBuilder();
        ArrayDeque<Character> dequeQueue = new ArrayDeque<>();
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == ')') {
                StringBuilder builder = new StringBuilder();
                while (dequeQueue.getLast() != '(') {
                    builder.append(dequeQueue.pollLast());  //逐个从队尾取出后进队列的元素，下面则顺序写入，其实就已经反转了
                }
                dequeQueue.pollLast();  //剔除'('
                for (Character ca : builder.toString().toCharArray()) {  //无需反转，FIFO
                    dequeQueue.add(ca);
                }
            } else {
                dequeQueue.add(array[i]);  //队尾添加元素
            }
        }
        while (!dequeQueue.isEmpty()) {
            ans.append(dequeQueue.pollFirst());  //从队首取元素
        }
        return ans.toString();
    }


    public String reverseParentheses02(String s) {
        StringBuilder ans = new StringBuilder();
        ArrayDeque<Character> dequeQueue = new ArrayDeque<>();
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == ')') {
                ConcurrentLinkedQueue<Character> FIFO = new ConcurrentLinkedQueue<>();
                while (dequeQueue.getLast() != '(') {
                    FIFO.add(dequeQueue.pollLast());  //逐个从队尾取出后进队列的元素，下面则顺序写入，其实就已经反转了
                }
                dequeQueue.pollLast();  //剔除'('
                while (!FIFO.isEmpty()) {
                    dequeQueue.add(FIFO.poll());
                }
            } else {
                dequeQueue.add(array[i]);  //队尾添加元素
            }
        }
        while (!dequeQueue.isEmpty()) {
            ans.append(dequeQueue.pollFirst());  //从队首取元素
        }
        return ans.toString();
    }


    /**
     * 32. 最长有效括号
     */
    public int longestValidParentheses(String s) {
        int res = 0;
        Stack<Integer> stack = new Stack<>();
        //题目关键：栈底维护的元素为当前已经遍历过的元素中，最后一个没有被匹配的右括号的下标
        stack.push(-1);
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {
                stack.add(i);
            } else {  // 遇到 ')'
                stack.pop();   //取出堆顶元素
                //注意：堆底一直维护的是一个"最后一个没有被匹配的右括号的下标"
                if (stack.isEmpty()) {  //1、如果取出堆顶元素后，堆为空，则说明 当前取到的是"最后一个没有被匹配的右括号的下标"，而非可以配对的 '左括号'，因此，此时需要更新"最后一个没有被匹配的有括号的下标"
                    stack.push(i);         //更新"最后一个没有被匹配的右括号的下标"，堆中有且仅有这一种情况使得'右括号'被添加到堆中，堆中其余元素均为左括号
                } else {                //2、否则直接计算，且不会向队列中添加右括号
                    res = Math.max(res, i - stack.peek());
                }
            }
        }
        return res;
    }

    // 将问题转换为最长连续 1的区间长度
    public int longestValidParentheses02(String s) {
        Stack<Integer> stack = new Stack<>();
        char[] array = s.toCharArray();
        int[] flags = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {  //仅将左括号的坐标添加至栈中，以供抵消
                stack.push(i);
            } else {   //遇到右括号
                // 仅尝试找到匹配的左侧括号，如有 将其标识置为 1
                if (!stack.isEmpty() && array[stack.peek()] == '(') {  //栈中只要有元素，就一定是与其##匹配##的左括号
                    flags[i] = 1;
                    flags[stack.pop()] = 1;
                }
                // 无对应的左侧括号，不做任何操作，即标识默认为 0
            }
        }
        int max = 0;
        int Times = 0;
        //求最长连续 1的长度
        for (int i = 0; i < flags.length; i++) {
            if (flags[i] == 1) {
                Times++;
                max = Math.max(max, Times);
            } else {
                Times = 0;  //重置归零
            }
        }
        return max;
    }


}
