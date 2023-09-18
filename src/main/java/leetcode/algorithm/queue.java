package leetcode.algorithm;

import utils.pojo.ListNode;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class queue {

    /**
     * 20. 有效的括号
     */
    public boolean isValid(String str) {
        Stack<Character> left = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '(' || xx == '[' || xx == '{') left.push(xx);
            else {
                if (left.isEmpty()) return false;
                Character yy = left.pop();
                if ((xx == ')' && yy != '(') || (xx == ']' && yy != '[') || (xx == '}' && yy != '{'))
                    return false;
            }
        }
        return left.isEmpty();
    }


    public boolean isValid01(String s) {   //基于栈 stack LIFO的特性
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
     * 71. 简化路径
     */
    public String simplifyPath(String path) {   //基于栈/双端队列
        String[] dirNames = path.split("/");
        ArrayDeque<String> arrayQueue = new ArrayDeque<>();
        for (int i = 0; i < dirNames.length; i++) {
            if (dirNames[i].equals(".") || dirNames[i].equals("")) {  //当前目录，忽略
                continue;
            }
            if (dirNames[i].equals("..")) {  //回退上一级目录
                if (!arrayQueue.isEmpty()) {
                    arrayQueue.pollLast();
                }
            } else {
                arrayQueue.addLast(dirNames[i]);                      //有效的目录层级
            }
        }
        if (arrayQueue.isEmpty()) return "/";
        StringBuilder ans = new StringBuilder();
        while (!arrayQueue.isEmpty()) {
            ans.append("/").append(arrayQueue.pollFirst());
        }
        return ans.toString();
    }


    /**
     * 844. 比较含退格的字符串
     */
    public boolean backspaceCompare(String xx, String yy) {
        ArrayDeque<Character> word1 = new ArrayDeque<>();
        ArrayDeque<Character> word2 = new ArrayDeque<>();
        for (char ch : xx.toCharArray()) {
            if (ch != '#') word1.addLast(ch);
            else if (!word1.isEmpty()) word1.pollLast();
        }
        for (char ch : yy.toCharArray()) {
            if (ch != '#') word2.addLast(ch);
            else if (!word2.isEmpty()) word2.pollLast();
        }
        StringBuilder ans1 = new StringBuilder();
        StringBuilder ans2 = new StringBuilder();
        while (!word1.isEmpty()) {
            ans1.append(word1.pollFirst());
        }
        while (!word2.isEmpty()) {
            ans2.append(word2.pollFirst());
        }
        return ans1.toString().equals(ans2.toString());
    }

    /**
     * 394. 字符串解码
     */
    public String decodeString(String word) {   //类似括号问题
        ArrayDeque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ']') {
                StringBuilder path = new StringBuilder();
                while (stack.peekLast() != '[') {
                    path.append(stack.pollLast());
                }
                stack.pollLast();  //移除 '['
                StringBuilder digit = new StringBuilder();
                while (!stack.isEmpty() && Character.isDigit(stack.peekLast())) {
                    digit.append(stack.pollLast());
                }
                int freq = Integer.valueOf(digit.reverse().toString());  //频次
                String unit = path.reverse().toString();
                while (freq > 0) {
                    for (int m = 0; m < unit.length(); m++) {
                        stack.addLast(unit.charAt(m));
                    }
                    freq--;
                }
            } else {
                stack.addLast(word.charAt(i));
            }
        }
        StringBuilder ans = new StringBuilder();
        while (!stack.isEmpty()) {
            ans.append(stack.pollFirst());
        }
        return ans.toString();
    }

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

    public int longestValidParentheses001(String str) {    //错误写法，案例："()(()"，因此仍应按照上面的写法走
        int maxWindow = 0;
        int currentWindow = 0;
        Stack<Integer> left = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '(') {         //1、左括号
                left.push(i);
            } else {                 //2、右括号
                if (!left.isEmpty()) {
                    left.pop();  //将队列中堆顶的左括号剔出队列，与当前的右括号匹配
                    currentWindow += 2;
                    maxWindow = Math.max(maxWindow, currentWindow);
                } else {
                    currentWindow = 0;  //重新开始计数
                }
            }
        }
        return maxWindow;
    }

    // 将问题转换为最长连续 1的区间长度
    public int longestValidParentheses01(String str) {
        Stack<Integer> left = new Stack<>();
        int[] flags = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '(') {            //1、左括号，将其坐标添加至栈中，以供抵消
                left.push(i);
            } else {                    //2、右括号，尝试在抵消栈中的左括号
                if (!left.isEmpty()) {           //2.1 栈中存在未匹配的左括号
                    flags[i] = 1;                        //这种情况下，左右匹配括号一定是连续的，此处的连续不是 (),而是 (.....)
                    flags[left.pop()] = 1;
                }
                //当前右括号没有与之匹配的左括号，则忽略，因为这部分不能都成有效括号
            }
        }
        //求最长连续 1的长度
        int maxWindow = 0;
        int currentWindow = 0;
        for (int flag : flags) {
            currentWindow++;
            if (flag == 0) {
                currentWindow = 0;
            }
            maxWindow = Math.max(maxWindow, currentWindow);
        }
        return maxWindow;
    }


    public int longestValidParentheses000(String str) {
        int ans = 0;
        Stack<Integer> left = new Stack<>();
        left.push(-1);   //维护所有 '('的坐标，外加一个最右侧未匹配的 ')'的坐标
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '(') {    //1.左括号
                left.push(i);
            } else {            //2.右括号
                Integer pop = left.pop();//取出堆顶元素
                if (left.isEmpty()) {         //2.2取出的元素是堆底元素 ')'
                    left.push(i);                     //取而代之
                } else {                       //2.3取出的元素是 '('，与当前的')'匹配
                    ans = Math.max(ans, i - left.peek());  //计算最大长度：由于堆底仍有元素，直接减即可，而无需 + 1
                    //--------------------
                    // 此时的堆底元素为：
                    //     1、'(': 连续添加的，可直接剪掉
                    //     2、')': 记录未匹配的最右侧')'，相当于挡板，可直接剪掉
                    //--------------------
                }
            }
        }
        return ans;
    }


    /**
     * 1046. 最后一块石头的重量
     */
    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);  //降序
        for (int stone : stones) {
            sortedQueue.add(stone);
        }
        while (sortedQueue.size() > 1) {
            Integer max = sortedQueue.poll();
            Integer next = sortedQueue.poll();
            if (max > next)
                sortedQueue.add(max - next);
        }
        return sortedQueue.size() == 0 ? 0 : sortedQueue.peek();
    }


    /**
     * 682. 棒球比赛
     */
    public int calPoints(String[] ops) {
        int total = 0;
        ArrayDeque<Integer> dequeQueue = new ArrayDeque<>();
        for (String op : ops) {
            if (op.length() > 1) {  //一定是多位的整数
                dequeQueue.addLast(Integer.valueOf(op));
            } else if (op.length() == 1) {
                char xx = op.charAt(0);
                if (Character.isDigit(xx)) {
                    dequeQueue.addLast(xx - '0');
                } else if (xx == 'D') {
                    Integer last = dequeQueue.pollLast();
                    dequeQueue.addLast(last);        //添加上次原始得分
                    dequeQueue.addLast(last * 2); //添加本次翻倍得分
                } else if (xx == 'C') {
                    dequeQueue.pollLast(); //取消上次得分
                } else if (xx == '+') {
                    Integer prev = dequeQueue.pollLast();
                    Integer prevPrev = dequeQueue.pollLast();
                    dequeQueue.addLast(prevPrev);   //顺序很重要
                    dequeQueue.addLast(prev);
                    dequeQueue.addLast(prev + prevPrev);
                }
            }
        }
        while (!dequeQueue.isEmpty()) {
            total += dequeQueue.pollLast();
        }
        return total;
    }

    /**
     * 678. 有效的括号字符串
     */
    public boolean checkValidString(String str) {  //贪心
        ArrayDeque<Integer> left = new ArrayDeque<>();
        ArrayDeque<Integer> star = new ArrayDeque<>();
        //------------------------------------------------------------------------------------------------
        // 校验右括号的合理性：
        //     用 "左括号"和 "星号"（贪心，将星号转换为左括号） 来一起共同抵消 右括号
        // 可以在 "(***))"的基础上，想想这一个过程，从左至右的贪心，是将星号转换为"("，使得能顺利遍历到最右侧，注意转换前后其实括号的配对关系发生了变化
        //------------------------------------------------------------------------------------------------
        //逐位添加和比较
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '(') {
                left.addLast(i);          //1.左括号入队列
            } else if (xx == '*') {
                star.addLast(i);          //2.星号入队列
            } else {                      //3.右括号抵消左括号和星号（贪心）
                if (!left.isEmpty()) left.pollLast();        //3.1 优先抵消 左括号
                else if (!star.isEmpty()) star.pollLast();   //3.2 其次抵消 星号（将星号视为左括号）
                else return false;                           //3.3 当前右括号，在其左侧无剩余的左括号或星号与之匹配，则直接失败
            }
        }
        //------------------------------------------------------------------------------------------------
        // 校验左括号的合理性
        //    此时右括号一定已经抵消完了，所以只能用"星号"（贪心，将星号转换为右括号）来抵消 左括号
        //    注意，这时需要校验星号和待抵消的左括号的位置关系
        //------------------------------------------------------------------------------------------------
        while (!left.isEmpty()) {
            if (star.isEmpty()) return false;           //有待抵消的左括号，但无冗余的星号
            if (left.pollLast() > star.pollLast()) {    //判断位置的异常
                return false;
            }
        }
        return true;
    }

    public boolean checkValidString10(String str) {
        Stack<Integer> left = new Stack<>();  //LIFO
        Stack<Integer> star = new Stack<>();  //LIFO
        //------------------------------------------------------------------------------------------------
        // 先从左至右校验合理性（贪心）
        // 可以在 "(***))"的基础上，想想这一个过程，从左至右的贪心，是将星号转换为"("，使得能顺利遍历到最右侧，注意转换前后其实括号的配对关系发生了变化
        //------------------------------------------------------------------------------------------------
        //逐位添加和比较
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '(') left.push(i);          //1.左括号入队列
            else if (xx == '*') star.push(i);     //2.星号入队列
            else {                                //3.右括号抵消左括号和星号
                if (!left.isEmpty()) left.pop();         //3.1 优先抵消 左括号
                else if (!star.isEmpty()) star.pop();    //3.2 其次抵消 星号（将星号视为左括号）
                else return false;                       //3.3 当前右括号，在其左侧无剩余的左括号或星号与之匹配，则直接失败
            }
        }
        //运行到这里说明，右括号全部被匹配
        if (left.isEmpty()) return true;  //此时如果待匹配的左侧括号为空，则无论 星号队列是否为空，均为true，因为可以将星号视为空
        //运行到这里说明，左括号仍有剩余，待匹配
        if (star.isEmpty()) return false; //此时为了满足条件，只能将星号视为右括号与之匹配，如果星号队列为空，则直接 false

        //贪心
        while (!left.isEmpty() && !star.isEmpty()) {
            if (left.pop() > star.pop()) {   //贪心：在最后一个左括号的右侧，一定有星号，否则就无法匹配 即 (* 的关系
                return false;
            }
        }
        return left.isEmpty();
    }

    public boolean checkValidString01(String str) {   //基于正反遍历，将括号问题转换为分值判定问题
        int n = str.length();
        int dir1 = 0;
        int dir2 = 0;
        for (int i = 0; i < n; i++) {
            //正向
            char xx = str.charAt(i);
            dir1 += (xx != ')') ? 1 : -1;     //星号视为左括号（贪心）
            //反向
            char yy = str.charAt(n - 1 - i);
            dir2 += (yy != '(') ? 1 : -1;     //星号视为右括号（贪心）
            //每个位置均验证合理性
            if (dir1 < 0 || dir2 < 0) return false;
        }
        return true;
    }


    public boolean checkValidString02(String str) {  //贪心
        int min = 0;   //待匹配的左括号的最小个数
        int max = 0;   //待匹配的左括号的最大个数
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '(') {
                min++;
                max++;
            } else if (xx == ')') {
                min--;
                max--;
            } else { //贪心
                max++;    //将星号视为左括号，则待匹配的左括号的最大值增加
                min--;    //将星号视为右括号，则待匹配的左括号的最小值减少
                //将星号转换空字符，见下逻辑
            }
            //------------------------------------------------
            // 两层寓意：
            //     1、正常情况下，如 "))(("
            //     2、贪心情况下，如 "*))"
            //           贪心的情况下，即将当前以左位置中的星号全部转换为左括号，此时仍有待匹配的左括号不够，即右括号太多了
            //------------------------------------------------
            if (max < 0) return false;

            //------------------------------------------------
            // 运行到这里则说明 max >= 0，而此时如果 min < 0，则一定是贪心将星号转换为右括号导致的左括号不够
            // 此时可进一步贪心，将星号转换为空字符串，即满足条件
            //------------------------------------------------
            if (min < 0) min = 0;
        }
        //-----------------------------
        // 贪心，只关心是否存在极值情况，使得左括号全部匹配完毕，不能有未匹配的左括号
        //-----------------------------
        return min == 0;  //此时可能存在 max > 0 如 "**)"，等于 0 为了避免 "**("
    }


    /**
     * 22. 括号生成
     */
    public List<String> generateParenthesis(int n) {   // DFS 而非队列，仅用于总结
        ArrayList<String> ans = new ArrayList<>();
        String[] element = {"(", ")"};
        generateParenthesisDfs(n, element, ans, 0, new StringBuilder());
        return ans;
    }

    private void generateParenthesisDfs(int n, String[] element, ArrayList<String> ans, int sum, StringBuilder path) {
        //迭代终止条件
        if (path.length() == 2 * n && sum == 0) {
            ans.add(new String(path));
            return;
        }

        //剪枝：无法构成有效括号
        if (path.length() > 2 * n) {
            return;
        }

        for (String parenthesis : element) {  //横向枚举遍历
            sum = parenthesis.equals("(") ? sum + 1 : sum - 1;
            //剪枝一：当前的组合无法构成有效括号
            if (sum < 0) return;

            //1、添加元素
            path.append(parenthesis);
            //2、纵向递归搜索
            generateParenthesisDfs(n, element, ans, sum, path);
            //3、移除元素
            path.deleteCharAt(path.length() - 1);
            sum = parenthesis.equals("(") ? sum - 1 : sum + 1;
        }
    }


    /**
     * 856. 括号的分数
     */
    public int scoreOfParentheses(String str) {
        return scoreOfParenthesesDfs(str, 0, str.length() - 1);
    }

    private int scoreOfParenthesesDfs(String str, int left, int right) {
        int ans = 0;        //当前层的分数，即以当前右边界结尾的括号对的分数
        int balance = 0;    //当前层的动态平衡指标，用于衡量是否对当前层的括号再进行匹配

        for (int i = left; i <= right; i++) {   //left代表的为区间的左边界，一定是 '('，i在尝试搜索区间右边界，范围是小于 right
            char xx = str.charAt(i);
            balance += xx == '(' ? 1 : -1;

            //-------------------------------------------------------------------
            // 虽然遍历是 i 向右在搜索，但每次满足条件的是区间，其特点是 从两端到中间
            //-------------------------------------------------------------------

            if (balance == 0) {        //一定是遇到右边界了
                if (i == left + 1)          //1.最小单元的括号对 ()
                    ans++;                                                      //积分 1
                else                        //2.多个括号对组合
                    ans += 2 * scoreOfParenthesesDfs(str, left + 1, i - 1);    //递归回溯结果积分

                //在内部跳动，如 (()()()) 时移动的方式，如果不存在这种情况，在此计算业务影响，因为left时层级别的，非全局的
                left = i + 1;   //关键
            }
        }
        return ans;
    }


    /**
     * 921. 使括号有效的最少添加
     */
    public int minAddToMakeValid(String str) {
        int ans = 0;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            if (!stack.isEmpty() && stack.peek() == '(' && str.charAt(i) == ')') {  //可以抵消
                stack.pop();  //移除对应的括号
                continue;
            }
            stack.add(str.charAt(i));
        }
        return stack.size();
    }

    public int minAddToMakeValid01(String str) {
        int ans = 0;
        int score = 0;
        //-----------------------------------------------------
        // 将「有效括号问题」转化为「分值有效性」的数学判定
        //     score 动态记录处理过程中的得分，将 '(' 记为 +1，将 ')' 记为 -1
        //     一个有效的括号应当在整个过程中不出现负数，因此一旦 score 出现负数，我们需要马上增加 ( 来确保合法性；当整个 s 处理完后，还需要添加 socre 等同的 ) 来确保合法性
        //-----------------------------------------------------
        for (int i = 0; i < str.length(); i++) {
            score += str.charAt(i) == '(' ? 1 : -1;
            if (score < 0) {  //缺少左括号
                score = 0;
                ans += 1;
            }
        }
        return ans + score;  //缺少右括号
    }


    public int minAddToMakeValid02(String str) {
        ArrayDeque<Character> stack = new ArrayDeque<>();  //记录未匹配的左括号
        int ans = 0;  //记录需要添加的左括号的数量
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == ')') {
                if (stack.isEmpty()) {
                    ans++;
                } else {
                    stack.pollLast();
                }
            } else {
                stack.addLast('(');
            }
        }
        return ans + stack.size();
    }

    public int minAddToMakeValid03(String str) {
        int current = 0;  //左括号充裕，记录当前未匹配的左括号
        int need = 0;     //左括号不足，记录需要添加的左括号的数量
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == ')') {
                if (current == 0) {  //无冗余的左括号可用于抵消
                    need++;
                } else {             //有冗余的左括号可用于抵消
                    current--;
                }
            } else {
                current++;
            }
        }
        return current + need;  //最终current表示需要同等数量的右括号来抵消
    }

    public int minAddToMakeValid04(String str) {  //错误写法，案例 "()))(("
        int left = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') left++;
        }
        int right = str.length() - left;
        return Math.abs(right - left);
    }


    /**
     * 1541. 平衡括号字符串的最少插入次数
     */
    public int minInsertions(String str) {
        int left = 0;
        int right = 0;
        Stack<Character> stack = new Stack<>();  //只记录左括号
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '(') {
                stack.add(xx);
            } else {          //当前为右侧括号
                //1. 专注于判断后一位是否为右侧括号
                if (i < str.length() - 1) {
                    if (str.charAt(i + 1) == ')') {  //1.1 自身存在两个连续的右括号，抵消一个左括号
                        i++;          //跳过下一位
                    } else {                         //1.2 自身不存在两个连续的右括号，添加一个，进入下一个配对循环
                        right++;      //缺少一个右侧括号
                    }
                } else {
                    right++;          //缺少一个右侧括号
                }

                //2.专注于判断前一位是否存在左括号
                if (!stack.isEmpty()) {
                    stack.pop();  //配对
                } else {
                    left++;       //添加一个左括号
                }
            }
        }
        return left + right + 2 * stack.size();
    }


    public int minInsertions00(String str) {
        int leftNums = 0;
        int leftNeed = 0;
        int rightNeed = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                leftNums++;
            } else {
                //1.专注于左侧括号
                if (leftNums > 0) {
                    leftNums--;
                } else {
                    leftNeed++;
                }

                //2.专注于右侧括号
                if (i < str.length() - 1) {
                    if (str.charAt(i + 1) == ')') {
                        i++;          //下一位是右括号，则用下一位与之配对，故跳过下一位
                    } else {
                        rightNeed++;  //下一位不是右括号，则插入一个右括号
                    }
                } else {
                    rightNeed++;
                }
            }
        }
        return leftNeed + rightNeed + 2 * leftNums;
    }


    public int minInsertions01(String s) {   //没太理解
        int res = 0, need = 0;//res 记录人为添加的左右括号维护括号平衡；need记录需要的右括号
        for (char c : s.toCharArray()) {
            if (c == '(') {
                need += 2;
                //因为右括号数量必须是偶数，所以当遇到左括号时判断need，如果是奇数则添加一个右括号以平衡，添加之后，need--
                if (need % 2 == 1) {
                    res++;
                    need--;
                }
            } else {//碰到右括号
                //右括号需求-1
                need--;
                //如果需求剪到负，说明需要添加一个左括号保持满足规则，又因为一个左括号匹配两个右，于是need=1
                if (need == -1) {
                    need = 1;//右括号需求由 -1--->+1
                    res++;//添加一个左以满足规则
                }
            }
        }
        return res + need;
    }


    /**
     * 1106. 解析布尔表达式
     */
    public boolean parseBoolExpr(String expression) {  //栈，本质也是括号问题
        ArrayDeque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == ',') {
                continue;
            }
            if (expression.charAt(i) != ')') {
                stack.addLast(expression.charAt(i));
            }
            if (expression.charAt(i) == ')') {
                int numsT = 0;
                int numsF = 0;
                while (!stack.isEmpty() && !stack.peekLast().equals('(')) {
                    Character ch = stack.pollLast();
                    numsT += ch.equals('t') ? 1 : 0;
                    numsF += ch.equals('f') ? 1 : 0;
                }
                stack.pollLast();  //先移除左括号
                Character ch = stack.pollLast();  //当前的符号
                if (ch.equals('&')) {       //内部至少两个元素
                    if (numsF == 0) {   //全为真，则为真
                        stack.addLast('t');
                    } else {
                        stack.addLast('f');
                    }
                    continue;
                }
                if (ch.equals('|')) {       //内部至少两个元素
                    if (numsT > 0) {  //存在真，则为真
                        stack.addLast('t');
                    } else {
                        stack.addLast('f');
                    }
                    continue;
                }
                if (ch.equals('!')) {       //内部仅有一个元素
                    if (numsT > 0) {  //当前真，则为假
                        stack.addLast('f');
                    } else {
                        stack.addLast('t');
                    }
                }
            }
        }
        return stack.pollLast().equals('t');
    }


    /**
     * 215. 数组中的第K个最大元素
     */
    public int findKthLargest(int[] nums, int k) {  //维护一个大小为 K 的堆
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o1 - o2);  //小根堆
        for (int num : nums) {
            sortedQueue.add(num);
            if (sortedQueue.size() > k) {
                sortedQueue.poll();
            }
        }
        return sortedQueue.peek();
    }

    public int findKthLargest01(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }


    /**
     * 剑指 Offer 40. 最小的k个数
     */
    public int[] getLeastNumbers(int[] arr, int k) {
        int[] ans = new int[k];
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);  //降序排序
        for (int num : arr) {
            sortedQueue.add(num);
            if (sortedQueue.size() > k) sortedQueue.poll();
        }
        int currentIndex = 0;
        while (!sortedQueue.isEmpty()) {
            ans[currentIndex] = sortedQueue.poll();
            currentIndex++;
        }
        return ans;
    }

    //-------------------------------------------------------------------------------------
    // 以下三个题均是通过队列来保证在不改变字符的相对位置的前提下，获取字典序最小的字符串
    //-------------------------------------------------------------------------------------


    /**
     * 2434. 使用机器人打印字典序最小的字符串
     */
    public String robotWithString(String str) {
        int n = str.length();
        int[] min = new int[n + 1];
        min[n] = 'z';
        for (int i = n - 1; i > 0; i--) {  //从当前位置 i 至最后，最小的字符
            min[i] = Math.min(str.charAt(i), min[i + 1]);
        }

        //可以将各个阶段的挡板信息打印出来，更加直观
        StringBuilder minBuilder = new StringBuilder();
        for (int i = 0; i <= str.length(); i++) {
            minBuilder.append((char) min[i]);
        }
        System.out.println(minBuilder.toString());

        StringBuilder ans = new StringBuilder();
        ArrayDeque<Character> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            queue.addLast(str.charAt(i));
            while (!queue.isEmpty() && queue.peekLast() <= min[i + 1]) {   //想想：写完最后一个元素后，会向前判断堆中的元素，均在这里完成，相当于一个挡板
                ans.append(queue.pollLast());
            }
        }
        return ans.toString();
    }

    public String robotWithString10(String str) {   //错误写法
        int n = str.length();
        StringBuilder ans = new StringBuilder();
        ArrayDeque<Character> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            //--------------------------------------------------------
            // 错误原因：仅仅考虑相邻元素的大小，没有考虑全局各个元素的大小，示例："bdda"
            //--------------------------------------------------------
            while (!queue.isEmpty() && queue.peekLast() < str.charAt(i)) {
                ans.append(queue.pollLast());
            }
            queue.addLast(str.charAt(i));
        }
        while (!queue.isEmpty()) {
            ans.append(queue.pollLast());
        }
        return ans.toString();
    }

    public String robotWithString00(String str) {
        StringBuilder ans = new StringBuilder();
        int[] min = new int[str.length() + 1];  //min[i] 记录从 i 至最后，最小的字符
        for (int i = str.length() - 1; i > 0; i--) {  // index = 0 没有使用
            min[i] = Math.min(str.charAt(i), i == str.length() - 1 ? 'z' : min[i + 1]);
        }
        Stack<Character> stackQueue = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            stackQueue.add(str.charAt(i));
            //由于 min[str.length()] = 0; 所以最后一位及堆中剩余元素均不在这里写 ans
            while (!stackQueue.isEmpty() && stackQueue.peek() <= min[i + 1]) {  //想想：写完最后一个元素后，会向前判断堆中的元素，均在这里完成，相当于一个挡板
                ans.append(stackQueue.pop());
            }
        }
        while (!stackQueue.isEmpty()) {
            ans.append(stackQueue.pop());
        }
        return ans.toString();
    }

    //-----------------------------------------------------------------------
    // 上下三者的差异：
    //    min[]的长度、min[str.length()] 赋值为 0 / 'z'，进而导致相关逻辑的差异
    //-----------------------------------------------------------------------

    public String robotWithString000(String str) {
        StringBuilder ans = new StringBuilder();
        int[] min = new int[str.length()];  //min[i] 记录从 i 至最后，最小的字符
        for (int i = str.length() - 1; i > 0; i--) {  // index = 0 没有使用
            min[i] = Math.min(str.charAt(i), i == str.length() - 1 ? 'z' : min[i + 1]);
        }
        Stack<Character> stackQueue = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            stackQueue.add(str.charAt(i));
            //由于 min[str.length()] = 0; 所以最后一位及堆中剩余元素均不在这里写 ans
            while (i != str.length() - 1 && !stackQueue.isEmpty() && stackQueue.peek() <= min[i + 1]) {  //想想：写完最后一个元素后，会向前判断堆中的元素，均在这里完成，相当于一个挡板
                ans.append(stackQueue.pop());
            }
        }
        while (!stackQueue.isEmpty()) {
            ans.append(stackQueue.pop());
        }
        return ans.toString();
    }

    public String robotWithString01(String str) {  //等效于上个方法
        StringBuilder ans = new StringBuilder();
        int[] min = new int[str.length()];  //min[i] 记录从 i + 1 至最后，最小的字符
        for (int i = str.length() - 1; i > 0; i--) {
            min[i - 1] = Math.min(str.charAt(i), i == str.length() - 1 ? 'z' : min[i]);
        }
        Stack<Character> stackQueue = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            stackQueue.add(str.charAt(i));
            //--------------------------------------------------------------------------
            // 注意：最后一个元素，是在这里写入 ans 还是在下面的堆写入 ans，其实效果是一样的
            //      min[str.length() - 1] 无论赋值与否，都可以：
            //           1、不赋值，采用默认值 0，则在包括最后一个元素在内的所有堆中元素，均基于堆来写
            //           2、赋值，赋值为 'z'，则在包括最后一个元素在内的所有堆中元素，则在这里写
            //-----------------------------------------------------------
            while (!stackQueue.isEmpty() && stackQueue.peek() <= min[i]) {   //关键：判断当前位置，如果找到第一个后，会判断左侧位置
                ans.append(stackQueue.pop());
            }
        }
        while (!stackQueue.isEmpty()) {
            ans.append(stackQueue.pop());
        }
        return ans.toString();
    }


    //---------------------------------------------------------------------------
    // 单调栈相关题目
    //---------------------------------------------------------------------------


    /**
     * 456. 132 模式
     */
    public boolean find132pattern(int[] nums) {  //132 => ijk
        int n = nums.length;
        ArrayDeque<Integer> arrayQueue = new ArrayDeque<>();   //维护中间元素 j
        int k = Integer.MIN_VALUE; //维护第三个元素 k
        for (int i = n - 1; i >= 0; i--) { //倒叙遍历第一个元素 i
            if (nums[i] < k) return true;    //满足 1m2
            while (!arrayQueue.isEmpty() && arrayQueue.peekLast() < nums[i]) {
                //----------------------------------------------------------------------
                // 队列中存储多个元素，每次从右侧加入队列，从右侧出队列，从左到右依次降序存储
                // 由于 K 是从队列右侧取处的元素，因此一定有 K 位于 j 右侧且是小于 j 的最大值（贪心）
                //----------------------------------------------------------------------
                k = arrayQueue.pollLast();   //K从队列中取值，一定小于队列中的值，且倒叙遍历，故满足 32
            }
            arrayQueue.add(nums[i]);
        }
        return false;
    }

    public boolean find132pattern01(int[] nums) {  //132 => ijk
        int n = nums.length;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        int k = Integer.MIN_VALUE;
        for (int i = n - 1; i >= 0; i--) {
            while (!queue.isEmpty() && nums[i] > queue.peekLast()) {
                k = queue.pollLast();
            }
            if (i > 0 && nums[i - 1] < k) return true;  //并不是仅判断 13 相邻的情况，而是提前判断
            queue.addLast(nums[i]);
        }
        return false;
    }

    /**
     * 739. 每日温度
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] ans = new int[n];
        ArrayDeque<Integer> arrayQueue = new ArrayDeque<>();  //记录搜索坐标
        for (int i = 0; i < n; i++) {
            //-----------------------------------------------------------------------------------------------------------
            // 队列一直在动态的记录着没有找到后续高温的日期索引，如果遍历到某天温度，会"反向"将当前队列中低于此温度的天取出来，赋值
            // 队列中的元素一定是降序的排列，不存在升序，因为如果有升序，则升序的元素一定将前一个元素给取出队列了
            //-----------------------------------------------------------------------------------------------------------
            while (!arrayQueue.isEmpty() && temperatures[arrayQueue.peekLast()] < temperatures[i]) {
                Integer index = arrayQueue.pollLast();
                ans[index] = i - index;
            }
            arrayQueue.addLast(i);  //前面是空或是更高温的天气
        }
        return ans;
    }

    public int[] dailyTemperatures01(int[] temperatures) {  //错误解法，理解错误，题目不是让判断这天后是否有更高温，而是判断第几天有更高温度
        int n = temperatures.length;
        int[] nums = new int[n];  //记录当前位置之后最大值
        nums[n - 1] = temperatures[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            nums[i] = Math.max(nums[i + 1], temperatures[i + 1]);
        }
        int[] ans = new int[n];
        for (int i = 0; i < n - 1; i++) {
            if (temperatures[i] < nums[i]) ans[i] = 1;
        }
        return ans;
    }


    /**
     * 1475. 商品折扣后的最终价格
     */
    public int[] finalPrices(int[] prices) {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();  //记录索引
        for (int i = 0; i < prices.length; i++) {
            while (!arrayDeque.isEmpty() && prices[arrayDeque.peekLast()] >= prices[i]) {  //决定了队列中元素值升序排序
                prices[arrayDeque.pollLast()] -= prices[i];
            }
            arrayDeque.addLast(i);
        }
        return prices;
    }

    public int[] finalPrices01(int[] prices) {  //暴力解法
        for (int i = 0; i < prices.length; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                if (prices[i] >= prices[j]) {
                    prices[i] -= prices[j];
                    break;
                }
            }
        }
        return prices;
    }


    /**
     * 402. 移掉 K 位数字
     */
    public String removeKdigits(String num, int k) {
        ArrayDeque<Character> arrayDeque = new ArrayDeque<>();   //双端队列
        for (int i = 0; i < num.length(); i++) {
            while (!arrayDeque.isEmpty() && arrayDeque.peekLast() > num.charAt(i) && k > 0) {  //队列内升序排序
                arrayDeque.pollLast();
                k--;
            }
            arrayDeque.addLast(num.charAt(i));
        }
        //1、头，移除前导零
        while (!arrayDeque.isEmpty() && arrayDeque.peekFirst() == '0') {
            arrayDeque.pollFirst();
        }
        //2、尾，消耗 K
        while (!arrayDeque.isEmpty() && k > 0) {  //因为队列内升序排序，因此从后往前移除元素
            arrayDeque.pollLast();
            k--;
        }
        if (arrayDeque.isEmpty()) return "0";

        StringBuilder ans = new StringBuilder();
        while (!arrayDeque.isEmpty()) {
            ans.append(arrayDeque.pollFirst());
        }
        return ans.toString();
    }


    /**
     * 316. 去除重复字母
     */
    public String removeDuplicateLetters(String str) {
        int n = str.length();
        //---------------------------------------
        // 可以将此过程理解为，针对一行高低不同的木头，对于每个木头：
        //    1、尝试将其前面高于它的木头剔除掉，能否剔除掉还取决于此木头后是否还有高度相同的木头
        //    2、如果当前位置前面已经有这个木头了，说明前面的排列情况不能将这个木头剔除，同事为了保证同一个木头仅出现一次，所以忽略当前这个木头
        //---------------------------------------
        ArrayDeque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            //重复元素，如果靠前的元素被选择，靠后的元素将被剔除
            if (stack.contains(str.charAt(i))) {
                //-----------------------------------------------------------------------------
                // 队列中已存在当前元素，存在即合理，当前元素未被在其后进入队列的元素剔除，说明合理
                //-----------------------------------------------------------------------------
                continue;
            }
            //重复元素，如果此考前的元素后有比此元素小的元素，考前的元素将被剔除
            while (!stack.isEmpty() && stack.peekLast() > str.charAt(i) && str.indexOf(stack.peekLast(), i) != -1) {
                stack.pollLast();
            }
            stack.addLast(str.charAt(i));
        }
        StringBuilder ans = new StringBuilder();
        while (!stack.isEmpty()) {
            ans.append(stack.pollFirst());
        }
        return ans.toString();
    }

    public String removeDuplicateLetters01(String str) {
        int n = str.length();
        int[] buckets = new int[26];
        for (int i = 0; i < n; i++) {    //记录当前位置后字符的个数
            buckets[str.charAt(i) - 'a']++;
        }
        ArrayDeque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            buckets[str.charAt(i) - 'a']--; //更新后续当前字符剩余的个数
            if (stack.contains(str.charAt(i))) {
                continue;
            }
            while (!stack.isEmpty() && stack.peekLast() > str.charAt(i) && buckets[stack.peekLast() - 'a'] > 0) {
                stack.pollLast();
            }
            stack.addLast(str.charAt(i));
        }
        StringBuilder ans = new StringBuilder();
        while (!stack.isEmpty()) {
            ans.append(stack.pollFirst());
        }
        return ans.toString();
    }

    public String removeDuplicateLetters02(String str) {
        int n = str.length();
        Stack<Character> stack = new Stack<>();
        int[] visited = new int[26];  //记录当前队列中是否存在对应的字符
        int[] buckets = new int[26];  //记录待遍历的字符串中，此字符剩余的个数
        for (int i = 0; i < n; i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        for (int i = 0; i < n; i++) {
            int ch = str.charAt(i) - 'a';
            //1、更新当前位置后此字符的个数
            buckets[ch]--;
            //2、去重
            if (visited[ch] == 1) {  //当前字符已经使用
                continue;
            }
            //3、当前字符可添加到队列中，找到当前字符合理的插入位置
            while (!stack.isEmpty() && stack.peek() > str.charAt(i)) { //判断当前字符与堆顶元素的大小，进而判断是否更新堆中已有元素
                //1、如果之后不存在当前字符了，则此字符不可剔除
                if (buckets[stack.peek() - 'a'] == 0) {
                    break;
                }
                //2、之后仍存在当前字符，将此字符从队列中剔除
                char deleteCh = stack.pop();
                visited[deleteCh - 'a'] = 0;  //记录队列中不存在此字符，用于将后续此字符添加到队列中
            }
            //4、在堆中插入当前字符
            stack.add(str.charAt(i));
            visited[ch] = 1;
        }
        StringBuilder ans = new StringBuilder();
        while (!stack.isEmpty()) {
            ans.append(stack.pop());
        }
        return ans.reverse().toString();
    }


    /**
     * 1081. 不同字符的最小子序列
     */
    public String smallestSubsequence(String str) {
        int n = str.length();
        //记录各个字符出现的次数
        int[] buckets = new int[26];
        for (int i = 0; i < n; i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        Stack<Character> stack = new Stack<>();
        //记录堆中已经存在的字符
        int[] used = new int[26];
        //依次将字符串中的字符尝试放入队列
        for (int i = 0; i < n; i++) {
            char currentCh = str.charAt(i);
            //当前字符在前面已经被使用，为避免重复不再使用，直接跳过此字符
            if (used[currentCh - 'a'] == 1) {
                buckets[currentCh - 'a']--;  //减少后续此字符的个数
                continue;
            }
            //当前字符未被使用，可尝试放入队列，通过移除堆顶元素，来将当前元素放入队列的合理位置，从而保证字符不重复，相对顺序不改变，同时字符序列最小
            while (!stack.isEmpty() && stack.peek() > currentCh && buckets[stack.peek() - 'a'] > 0) {
                used[stack.peek() - 'a'] = 0;  //置为未使用，使用后续的字符
                stack.pop();//移除堆顶元素
            }
            //加入队列
            stack.add(currentCh);
            used[currentCh - 'a'] = 1;
            buckets[currentCh - 'a']--;
        }
        StringBuilder ans = new StringBuilder();
        while (!stack.isEmpty()) {
            ans.append(stack.pop());
        }
        return ans.reverse().toString();
    }


    /**
     * 239. 滑动窗口最大值
     */
    public int[] maxSlidingWindow(int[] nums, int k) {  //单调栈 + 滑动窗口
        int n = nums.length;
        int[] ans = new int[n - k + 1];
        //单调队列
        ArrayDeque<Integer> queue = new ArrayDeque<>();  //降序排序
        for (int i = 0; i < n; i++) {
            //1、维护队列的单调性
            while (!queue.isEmpty() && nums[queue.peekLast()] < nums[i]) {  //队列中单调递减
                queue.pollLast();
            }
            queue.addLast(i);
            //2、剔除窗口外的元素
            if (!queue.isEmpty() && queue.peekFirst() < i - k + 1) {
                queue.pollFirst();
            }
            //3、滑动窗口形成后，记录各个窗口中最大元素
            if (i - k + 1 >= 0 && !queue.isEmpty()) {
                ans[i - k + 1] = nums[queue.peekFirst()];
            }
        }
        return ans;
    }

    public int[] maxSlidingWindow01(int[] nums, int k) {  //超时
        ArrayList<Integer> ans = new ArrayList<>();
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);  //降序排序，小根堆/大顶堆
        int currentIndex = 0;
        while (currentIndex < nums.length) {
            //添加元素
            sortedQueue.add(nums[currentIndex]);
            //移除元素
            if (currentIndex >= k) {  //维持固定的窗口大小
                sortedQueue.remove(nums[currentIndex - k]);
            }
            if (sortedQueue.size() == k) {
                ans.add(sortedQueue.peek());
            }
            currentIndex++;
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }


    /**
     * 42. 接雨水
     */
    public int trap(int[] height) {
        int sum = 0;
        Stack<Integer> stack = new Stack<>();  //记录索引
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {  //保证桶内降序排序
                //------------------------------------------------------------------------
                // 当前 height[i] 作为桶的右挡板，然后需要在栈中依次找到 桶底索引和桶左挡板索引
                //------------------------------------------------------------------------

                //1、首先，在栈中找到桶底的索引
                Integer bottomIndex = stack.pop();
                //2、然后，在栈找到桶左挡板的索引
                if (stack.isEmpty()) {  //无更多元素，无法构成一个完整的桶，直接跳出
                    break;
                }
                Integer leftIndex = stack.peek();   //桶左挡板索引，不可取出，便于后续以当前的桶右边界，以当前左挡板为桶底，继续寻找新桶的左边界

                //计算当前桶内积水量
                int distance = i - leftIndex - 1;
                int high = Math.min(height[leftIndex], height[i]) - height[bottomIndex];  //水位高度取决于桶两侧的最短板
                sum += distance * high;
            }
            stack.add(i);
        }
        return sum;
    }


    public int trap00(int[] height) {   //逐列累加
        int n = height.length;
        int[] max1 = new int[n];
        int[] max2 = new int[n];
        //1、各个位点及其左侧的最大高度
        max1[0] = height[0];
        for (int i = 1; i < n; i++) {
            max1[i] = Math.max(max1[i - 1], height[i]);
        }
        //2、各个位点及其右侧的最大高度
        max2[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            max2[i] = Math.max(max2[i + 1], height[i]);
        }
        int ans = 0;
        //累加各个位点可以接到雨水的量
        for (int i = 0; i < n; i++) {
            ans += Math.min(max1[i], max2[i]) - height[i];
        }
        return ans;
    }


    public int trap01(int[] height) {   //逐行累加，但会超时
        int ans = 0;
        int maxHeight = Arrays.stream(height).max().getAsInt();
        for (int h = 1; h <= maxHeight; h++) {   //每层高度
            int start = 0;
            int rowSum = 0;
            for (int i = 0; i < height.length; i++) {
                if (start == 1 && height[i] < h) {
                    rowSum++;
                }
                //--------------------------------------------------------------------------------
                // 水位线为 h 时，则高于此水位线的台阶相当于栅栏，从而存储雨水
                // 每个栅栏是前一个区间的后挡板，也是下一个区间的前挡板，所以既要累加前一个区间的雨水量，也要从重新开始计算下一个区间的雨水量
                //--------------------------------------------------------------------------------
                if (height[i] >= h) {
                    ans += rowSum;  //作为区间的后挡板，累加前一个区间的积水量
                    start = 1;      //作为区间的前挡板，可以从下一个位置开始积累雨水
                    rowSum = 0;     //重置当前积累的雨水量
                }
            }
        }
        return ans;
    }


    /**
     * 84. 柱状图中最大的矩形
     */
    public int largestRectangleArea(int[] heights) {
        int maxArea = 0;
        int n = heights.length;
        int[] nums = new int[n + 2];
        System.arraycopy(heights, 0, nums, 1, n);  //两端为默认值 0，强制弹出所有索引
        Stack<Integer> stack = new Stack<>();   //效率低，建议使用双端队列
        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                //当前矩形的高度
                int height = nums[stack.pop()];
                //当前矩形的左边界
                Integer leftIndex = stack.peek();  //右边界为 i，矩形不包含两边
                //当前矩形的宽度
                int width = i - leftIndex - 1;

                //更新最大矩形高度
                maxArea = Math.max(maxArea, height * width);
            }
            stack.add(i);
        }
        return maxArea;
    }


    /**
     * 85. 最大矩形
     */
    public int maximalRectangle(char[][] matrix) {   //上题的变形
        int maxRectangle = 0;
        int m = matrix.length;
        if (m == 0) return 0;
        int n = matrix[0].length;
        int[][] grid = new int[m][n + 2];  //新增首尾两列
        //最后一行初始化
        for (int i = 0; i < n; i++) {
            if (matrix[m - 1][i] == '1') grid[m - 1][i + 1] = 1;
        }
        //其余行初始化
        for (int i = m - 2; i >= 0; i--) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '0') grid[i][j + 1] = 0;
                else grid[i][j + 1] = grid[i + 1][j + 1] + 1;    //依赖于上一行
            }
        }

        ArrayDeque<Integer> stack = new ArrayDeque<>();  //记录索引
        for (int[] height : grid) {   //逐行计算
            for (int i = 0; i < height.length; i++) {
                while (!stack.isEmpty() && height[stack.peekLast()] > height[i]) {
                    int high = height[stack.pollLast()];  //矩形高度
                    Integer leftIndex = stack.peekLast(); //左边界
                    int width = i - leftIndex - 1;        //矩形宽度

                    maxRectangle = Math.max(maxRectangle, high * width);
                }
                stack.addLast(i);
            }
            stack.clear();  //移除队列中所有的元素
        }
        return maxRectangle;
    }


    /**
     * 1696. 跳跃游戏 VI
     */
    public int maxResult(int[] nums, int k) {  //正向，剪枝
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MIN_VALUE);
        dp[0] = nums[0];
        //关键在于从前向后施加影响
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n && j <= i + k; j++) {  //当前位置 i 对后续位置 j 的影响
                dp[j] = Math.max(dp[j], dp[i] + nums[j]);
                //剪枝
                if (dp[j] >= dp[i]) {
                    //--------------------------------------------------------------------------------------------------
                    // 当 dp[j] > dp[i]时，dp[j+1] 到 dp[i+k] 必然已经不会受 dp[i]的影响了，因为 dp[j]比dp[i]是更优的跳跃选择
                    //--------------------------------------------------------------------------------------------------
                    break;
                }
            }
        }
        return dp[n - 1];
    }

    public int maxResult00(int[] nums, int k) {  //反向，超时
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MIN_VALUE);
        dp[0] = nums[0];
        for (int i = 1; i < n; i++) {
            for (int j = Math.max(0, i - k); j < i; j++) {  //无法剪枝
                dp[i] = Math.max(dp[i], dp[j] + nums[i]);
            }
        }
        return dp[n - 1];
    }

    //--------------------------------------------------------
    // 上下两种写法的差异：
    //    上面是对当前元素对后 K 个元素的影响，并及时剪枝跳出
    //    下面是对当前元素对前 K 个元素的影响（超时）
    //    下面是对当前元素对前 K 个元素的影响，基于单调队列进行时间优化
    //--------------------------------------------------------

    public int maxResult01(int[] nums, int k) {  //超时
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MIN_VALUE);
        dp[0] = nums[0];
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= k; j++) {   //超时的原因，K 范围太大，重复遍历同一个元素多次
                if (i - j >= 0) {
                    dp[i] = Math.max(dp[i], dp[i - j] + nums[i]);
                }
            }
        }
        return dp[n - 1];
    }

    public int maxResult02(int[] nums, int k) {   //基于单调队列的方式，进行优化
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MIN_VALUE);
        ArrayDeque<Integer> queue = new ArrayDeque<>();      //记录索引，降序
        dp[0] = nums[0];
        queue.addLast(0);
        //关键在于从前向后施加影响
        for (int i = 1; i < n; i++) {
            //1、维护窗口大小
            while (!queue.isEmpty() && i - queue.peekFirst() > k) {
                queue.pollFirst();
            }
            //2、贪心计算当前位置最大值
            dp[i] = dp[queue.peekFirst()] + nums[i];  //满足影响范围
            //3、维护队列的单调性
            while (!queue.isEmpty() && dp[queue.peekLast()] <= dp[i]) {   //降序排序
                //贪心，移除队列尾部小于当前元素的元素，因为移除的这部分元素不会比当前元素更有利于作为后续元素的前置状态
                queue.pollLast();
            }
            queue.addLast(i);
        }
        return dp[n - 1];
    }

    public int maxResult001(int[] nums, int k) {  //优先队列
        int n = nums.length;
        int[] ans = new int[n];
        //优先队列，存储各个位点的最大累加值，记录信息：位点最大累加值、位点索引
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);  //按照值的大小降序排序
        sortedQueue.add(new int[]{nums[0], 0});
        ans[0] = nums[0];
        for (int i = 1; i < n; i++) {
            //1、剔除窗口外的数据
            while (!sortedQueue.isEmpty() && sortedQueue.peek()[1] < i - k) {
                sortedQueue.poll();
            }
            //2、计算当前位点的最大值
            int curr = sortedQueue.peek()[0] + nums[i];
            ans[i] = curr;
            sortedQueue.add(new int[]{curr, i});
        }
        return ans[n - 1];
    }


    public int maxResult002(int[] nums, int k) {  //有序集合 + 滑动窗口
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(dp[0], 1);
        for (int i = 1; i < n; i++) {
//            dp[i] = Math.max(treeMap.lastKey(), 0) + nums[i];
            //不能写为上面的，因为本题不是找子序列，而是必须从前面的继承过来，如果不继承的化，可以从 0 开始
            dp[i] = treeMap.lastKey() + nums[i];
            if (i >= k) {
                treeMap.put(dp[i - k], treeMap.getOrDefault(dp[i - k], 0) - 1);
                if (treeMap.get(dp[i - k]) == 0) {
                    treeMap.remove(dp[i - k]);
                }
            }
            treeMap.put(dp[i], treeMap.getOrDefault(dp[i], 0) + 1);
        }
        return dp[n - 1];
    }


    /**
     * 1425. 带限制的子序列和
     */
    public int constrainedSubsetSum(int[] nums, int k) {  //单调栈 + 动态规划，相当于跳跃的步长不超过 K
        int n = nums.length;
        int[] dp = new int[n];   //dp[i]以 nums[i] 作为子序列最后一个元素，表示此子序列的最大值
        dp[0] = nums[0];
        int ans = nums[0];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(0);
        for (int i = 1; i < n; i++) {
            //维护窗口大小
            while (!queue.isEmpty() && i - queue.peekFirst() > k) {
                queue.pollFirst();
            }
            dp[i] = Math.max(dp[queue.peekFirst()], 0) + nums[i];  //所有前置状态为负数，则直接从当前元素开始转移（前置状态为 0）
            //维护各个位置子序列的最值
            ans = Math.max(ans, dp[i]);
            //--------------------------------------------------------
            // 维护单调性
            //     贪心，移除队列尾部小于当前元素的元素，因为移除的这部分元素不会比当前元素更有利于作为后续元素的前置状态
            //--------------------------------------------------------
            while (!queue.isEmpty() && dp[queue.peekLast()] <= dp[i]) {
                queue.pollLast();
            }
            //将当前元素添加到队列中
            queue.addLast(i);
        }
        return ans;
    }


    public int constrainedSubsetSum00(int[] nums, int k) {  //优先队列 + 动态规划
        int n = nums.length;
        int[] dp = new int[n];  //dp[i]指的是以 nums[i] 作为最后一位元素的子序列的最大和
        dp[0] = nums[0];
        //优先队列
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);
        sortedQueue.add(new int[]{nums[0], 0});
        for (int i = 1; i < n; i++) {
            //1、剔除越界元素
            while (!sortedQueue.isEmpty() && i - sortedQueue.peek()[1] > k) {
                sortedQueue.poll();
            }
            dp[i] = Math.max(sortedQueue.isEmpty() ? 0 : sortedQueue.peek()[0], 0) + nums[i];
            sortedQueue.add(new int[]{dp[i], i});
        }
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }


    public int constrainedSubsetSum001(int[] nums, int k) {  //动态规划 + 提前剪枝
        int n = nums.length;
        int[] dp = new int[n];  //dp[i]指的是以 nums[i] 作为最后一位元素的子序列的最大和
        Arrays.fill(dp, Integer.MIN_VALUE);
        dp[0] = Math.max(nums[0], 0);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n && j <= i + k; j++) {
                dp[j] = Math.max(dp[j], Math.max(dp[i], 0) + nums[j]);
                if (dp[j] >= dp[i]) {
                    break;
                }
            }
        }
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }


    public int constrainedSubsetSum03(int[] nums, int k) {  //有序集合 + 滑动窗口
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();  //value为次数
        treeMap.put(dp[0], 1);
        for (int i = 1; i < n; i++) {
            dp[i] = Math.max(treeMap.lastKey(), 0) + nums[i];
            if (i >= k) {
                treeMap.put(dp[i - k], treeMap.getOrDefault(dp[i - k], 0) - 1);
                if (treeMap.getOrDefault(dp[i - k], 0) <= 0) {
                    treeMap.remove(dp[i - k]);
                }
            }
            treeMap.put(dp[i], treeMap.getOrDefault(dp[i], 0) + 1);
        }
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }


    //-----------------------------------------------------------
    // 注意：上下虽然都是有序集合，但 TreeMap 的 value 一个是频次，一个索引（存在同一个 key 覆盖的情况，不推荐）
    //-----------------------------------------------------------

    public int constrainedSubsetSum01(int[] nums, int k) {  //有序集合 + 滑动窗口
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        TreeMap<Integer, Integer> sorted = new TreeMap<>();
        sorted.put(dp[0], 0);
        int ans = nums[0];
        for (int i = 1; i < n; i++) {
            dp[i] = Math.max(sorted.lastKey(), 0) + nums[i];
            ans = Math.max(ans, dp[i]);
            sorted.put(dp[i], i);
            //维护滑动窗口
            if (i >= k) {
                Integer left = sorted.get(dp[i - k]);
                if (i - left == k) {      //不能去掉，因为存在重复数据
                    sorted.remove(dp[i - k]);
                }
            }
        }
        return ans;
    }


    /**
     * 496. 下一个更大元素 I
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int[] ans = new int[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums2.length; i++) {
            while (!queue.isEmpty() && queue.peekLast() < nums2[i]) {
                map.put(queue.pollLast(), nums2[i]);
            }
            queue.addLast(nums2[i]);
        }
        for (int i = 0; i < n; i++) {
            ans[i] = map.getOrDefault(nums1[i], -1);
        }
        return ans;
    }


    /**
     * 503. 下一个更大元素 II
     */
    public int[] nextGreaterElements(int[] nums) {  //单调栈 + 循环数组
        int n = nums.length;
        int[] ans = new int[n];
        Arrays.fill(ans, -1);

        ArrayDeque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < 2 * n - 1; i++) {  //循环数组
            while (!stack.isEmpty() && nums[stack.peekLast()] < nums[i % n]) {
                ans[stack.pollLast()] = nums[i % n];
            }
            stack.addLast(i % n);
        }
        return ans;
    }


    /**
     * 1438. 绝对差不超过限制的最长连续子数组
     */
    public int longestSubarray(int[] nums, int limit) {  //滑动窗口 + 有序集合
        int ans = 0;
        int left = 0;
        int right = 0;
        TreeMap<Integer, Integer> sortedMap = new TreeMap<>();
        while (right < nums.length) {
            sortedMap.put(nums[right], sortedMap.getOrDefault(nums[right], 0) + 1);
            //维护绝对值差，其实就是一个满足最值条件的窗口
            while (!sortedMap.isEmpty() && sortedMap.lastKey() - sortedMap.firstKey() > limit) {
                sortedMap.put(nums[left], sortedMap.get(nums[left]) - 1);
                if (sortedMap.get(nums[left]) == 0) {
                    sortedMap.remove(nums[left]);
                }
                left++;
            }
            ans = Math.max(ans, right - left + 1);
            right++;
        }
        return ans;
    }

    public int longestSubarray01(int[] nums, int limit) {  //滑动窗口 + 单调队列
        int ans = 0;
        int n = nums.length;
        int left = 0;
        int right = 0;
        ArrayDeque<Integer> queueMax = new ArrayDeque<>();   //记录较大值，从前向后降序排序记录较大值
        ArrayDeque<Integer> queueMin = new ArrayDeque<>();   //记录较小值，从前向后升序排序记录较小值
        //----------------------------------------------------------------
        // 滑动窗口体现在 窗口 [left , right] 满足最大值和最小值的差小于等于 limit
        //----------------------------------------------------------------
        while (right < n) {
            //1、维护较大值的队列
            while (!queueMax.isEmpty() && queueMax.peekLast() < nums[right]) {   //允许重复元素存在
                queueMax.pollLast();
            }
            //2、维护较小值的队列
            while (!queueMin.isEmpty() && queueMin.peekLast() > nums[right]) {   //允许重复元素存在
                queueMin.pollLast();
            }
            queueMax.addLast(nums[right]);
            queueMin.addLast(nums[right]);
            while (!queueMax.isEmpty() && !queueMin.isEmpty() && queueMax.peekFirst() - queueMin.peekFirst() > limit) {
                if (queueMax.peekFirst() == nums[left]) {
                    queueMax.pollFirst();
                }
                if (queueMin.peekFirst() == nums[left]) {
                    queueMin.pollFirst();
                }
                left++;
            }
            ans = Math.max(ans, right - left + 1);
            right++;
        }
        return ans;
    }


    public int longestSubarray02(int[] nums, int limit) {  //滑动窗口 + 有序集合
        int n = nums.length;
        int ans = 0;
        int left = 0;
        int right = 0;
        PriorityQueue<Integer> queueMax = new PriorityQueue<>(Comparator.reverseOrder());  //按数值降序排序
        PriorityQueue<Integer> queueMin = new PriorityQueue<>(Comparator.naturalOrder());  //按数值升序排序
        while (left <= right && right < n) {
            queueMax.add(nums[right]);  //可以包含重复元素
            queueMin.add(nums[right]);  //可以包含重复元素

            if (!queueMax.isEmpty() && !queueMin.isEmpty()) {
                if (queueMax.peek() - queueMin.peek() > limit) {
                    queueMax.remove(nums[left]);  //关键，删除优先队列中任意元素
                    queueMin.remove(nums[left]);
                    left++;
                }
            }
            ans = Math.max(ans, right - left + 1);
            right++;
        }
        return ans;
    }

    /**
     * 862. 和至少为 K 的最短子数组
     */
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length;
        long[] prefixSum = new long[n + 1];  //prefixSum[i]表示从 [0,i) 的和
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        int res = n + 1;
        //--------------------------------------------------
        // 本题维护单调栈的关键：
        //     一定是升序，因为元素入队列前会从队尾剔除比当前元素大的值，因为剔除的这部分元素不会比当前值得到更优结果（更短的距离）
        //     由于队列升序，因此队首为最小元素，当前元素为最大元素，故贪心的从队首元素开始，将其作为子数组的左端点，计算是否满足 K 的条件，不满足直接跳出，满足则计算并剔除队首元素
        //--------------------------------------------------
        ArrayDeque<Integer> queue = new ArrayDeque<>();  //递增的序列
        for (int i = 0; i <= n; i++) {  //位置 0 ，相当于数据铺地（队列与前缀和），左侧挡板
            long currentPrefixSum = prefixSum[i];
            //1、剔除队尾元素，保证队列内的元素 + 当前元素，整体是单调递增的
            while (!queue.isEmpty() && prefixSum[queue.peekLast()] >= currentPrefixSum) {
                //---------------------------------------------------------------------
                // 剔除的原因：
                // 如果以 当前队尾中的元素（较大） 或 当前元素（较小），作为满足条件的子数组的左端点，则一定是以当前较小元素得到的距离更小
                // 所以需要从队尾中剔除比当前元素大的元素，进而保证 队列元素 + 当前元素是递增的
                //---------------------------------------------------------------------
                queue.pollLast();
            }
            //2、剔除队首元素，贪心的搜索最短距离
            while (!queue.isEmpty() && currentPrefixSum - prefixSum[queue.peekFirst()] >= k) {
                //----------------------------------------------------------------------------
                // 如果成立，则以 queue.peekFirst() 作为左端点的子数组，以 i 作为右端点一定是距离最短的
                // 因为如果在 i 之后有索引 j（j > i）可以作为子数组的右端点，但其子数组的长度 j - queue.peekFirst() 一定比 i - queue.peekFirst() 大，所以可以剔除队首元素
                //----------------------------------------------------------------------------
                res = Math.min(res, i - 1 - queue.pollFirst() + 1);
            }
            queue.addLast(i);
        }
        return res == n + 1 ? -1 : res;
    }

    public int shortestSubarray01(int[] nums, int k) {  //暴力，超时
        int n = nums.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        int ans = n + 10;
        for (int i = 0; i < n + 1; i++) {
            for (int j = i + 1; j < n + 1; j++) {
                if (prefix[j] - prefix[i] >= k) {
                    ans = Math.min(ans, j - i);
                    break;
                }
            }
        }
        return ans == n + 10 ? -1 : ans;
    }


    /**
     * 209. 长度最小的子数组
     */
    public int minSubArrayLen(int target, int[] nums) {   //单调栈
        int n = nums.length;
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        int ans = n + 10;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n + 1; i++) {
            while (!queue.isEmpty() && prefix[i] - prefix[queue.peekFirst()] >= target) {
                ans = Math.min(ans, i - queue.pollFirst());
            }
            queue.addLast(i);
        }
        return ans == n + 10 ? 0 : ans;
    }

    public int minSubArrayLen01(int target, int[] nums) {  //二分
        int n = nums.length;
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        int ans = n + 10;
        for (int i = 0; i < n + 1; i++) {
            int left = i + 1;
            int right = n;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (prefix[mid] - prefix[i] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            if (left == n + 1) continue;
            ans = Math.min(ans, left - i);
        }
        return ans == n + 10 ? 0 : ans;
    }

    public int minSubArrayLen02(int target, int[] nums) {   //滑动窗口
        if (Arrays.stream(nums).sum() < target) return 0;
        int left = 0;
        int right = 0;
        int sum = 0;
        int minWindow = Integer.MAX_VALUE;
        while (right < nums.length) {
            sum += nums[right]; //滑动窗口右侧，一直向前移动，探索未知区域
            while (left <= right && sum >= target) {  //滑动窗口左侧边界，基于条件移动
                minWindow = Math.min(minWindow, right - left + 1);
                sum -= nums[left];
                left++;
            }
            right++;
        }
        return minWindow;
    }

    /**
     * 407. 接雨水 II
     */
    public int trapRainWater(int[][] height) {
        //------------------------------------------------
        // 一个环状的外层挡板，沿着其最低点向内搜索，即不断沿着挡板最低点向内收缩，在收缩过程中判断是否可以积水
        // 注意：在搜索过程中，外层挡板一直是一圈完整的环
        // 关键：挡板向内移动，挡板高度的计算，其应该是 Math.max(当前位置高度，相邻挡板的高度)，其实新挡板的高度只会增加不会减少，因为相邻外层挡板的高度限制了新挡板高度的最小值
        //------------------------------------------------
        int ans = 0;
        int m = height.length;
        int n = height[0].length;
        int[][] visited = new int[m][n];
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //小顶堆，按照各个位点的挡板高度升序排序
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上下左右
        //初始化
        for (int i = 0; i < m; i++) {  //将最外围的围墙添加到堆中
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    sortedQueue.add(new int[]{i, j, height[i][j]});  //最外层挡板的高度为台阶高度，故最外层台阶仅仅是挡板，不会是凹槽来存水
                    visited[i][j] = 1;
                }
            }
        }
        //向内搜索
        while (!sortedQueue.isEmpty()) {  //外围围墙向内收拢
            int[] lowestPoint = sortedQueue.poll();   //外层挡板的最低点，每次均从墙的最低位置开始搜索
            for (int[] dir : directions) {
                int nextRow = lowestPoint[0] + dir[0];
                int nextCol = lowestPoint[1] + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (visited[nextRow][nextCol] == 0) {
                        if (height[nextRow][nextCol] < lowestPoint[2]) {     //最低点相邻的位置，比"挡板高度"更低，可积雨水
                            ans += lowestPoint[2] - height[nextRow][nextCol];
                        }
                        //-----------------------------------------------------------------------------------------------------------------------
                        // 无论是否能接到雨水，更新挡板位置、"挡板向内收缩"
                        // 因此向内收缩的挡板的坐标为 [nextRow][nextCol]，但其高度不一定是 height[nextRow][nextCol]，新挡板的高度应该是前一个挡板的高度和此位置高度的最大值
                        //------------------------------------------------------------------------------------------------------------------------
                        sortedQueue.add(new int[]{nextRow, nextCol, Math.max(height[nextRow][nextCol], lowestPoint[2])});   //挡板高度，而非这个位置的高度
                        visited[nextRow][nextCol] = 1;
                    }
                }
            }
        }
        return ans;
    }

    public int trapRainWater00(int[][] height) {   //相较于上，明确了在什么条件下更新蓄水量，以及挡板高度应该更新为那个高度
        int ans = 0;
        int m = height.length;
        int n = height[0].length;
        int[][] visited = new int[m][n];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //按照挡板高度升序排序
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    sortedQueue.add(new int[]{i, j, height[i][j]});   //最外层挡板的高度为台阶高度，故最外层台阶仅仅是挡板，不会是凹槽来存水
                    visited[i][j] = 1;
                }
            }
        }
        while (!sortedQueue.isEmpty()) {
            int[] lowestPoint = sortedQueue.poll();   //外层挡板中的最低点
            for (int[] dir : directions) {
                int nextRow = lowestPoint[0] + dir[0];
                int nextCol = lowestPoint[1] + dir[1];
                if (nextRow >= 0 && nextRow < m - 1 && nextCol >= 0 && nextCol < n - 1) {
                    if (visited[nextRow][nextCol] == 0) {
                        if (height[nextRow][nextCol] < lowestPoint[2]) {   //可以蓄水
                            ans += lowestPoint[2] - height[nextRow][nextCol];
                            sortedQueue.add(new int[]{nextRow, nextCol, lowestPoint[2]});  //向内收缩，挡板高度为外层挡板的高度
                        } else {
                            sortedQueue.add(new int[]{nextRow, nextCol, height[nextRow][nextCol]});  //向内收缩，挡板高度为当前点的高度
                        }
                        visited[nextRow][nextCol] = 1;
                    }
                }
            }
        }
        return ans;
    }

    //----------------------------------------------------------------------
    // 上面的写法：广度优先搜索 + 优先队列
    //     最外圈挡板，沿着最低挡板点向内收缩挡板，每个位点仅会进入一次
    // 下面的写法：广度优先搜索 + 双端队列
    //     最外圈挡板，沿着一圈挡板顺序搜索（向内搜索），每个位点会进入多次
    //----------------------------------------------------------------------


    public int trapRainWater01(int[][] height) { //广度优先搜索
        //--------------------------------------------------------------------
        // 接水后的高度为：
        //    water[i][j] = max(height[i][j],min(water[i−1][j],water[i+1][j],water[i][j−1],water[i][j+1]))
        // 实际接水容量为：
        //    temp = water[i][j] - height[i][j]
        //--------------------------------------------------------------------
        int ans = 0;
        int m = height.length;
        int n = height[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int maxHeight = 0;
        //获取最大高度
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                maxHeight = Math.max(maxHeight, height[i][j]);
            }
        }
        //各个位点的初始水位线
        int[][] waterMark = new int[m][n];  //可以将其理解为一圈一圈的挡板，后面会初始化最外层挡板的高度为原始高度
        for (int i = 0; i < m; i++) {
            Arrays.fill(waterMark[i], maxHeight);
        }
        //记录更新水位线的位点
        ArrayDeque<int[]> arrayQueue = new ArrayDeque<>();  //记录在队列中的位点，其挡板高度（水位高度）已经固定
        //用边界来初始化需要更新水位线的位点
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {   //初始化最外层位点的挡板高度
                    if (height[i][j] < waterMark[i][j]) {
                        waterMark[i][j] = height[i][j];  //最外层位点的挡板高度就是原本台阶高度，因此在最外层一定不可蓄水
                        arrayQueue.addLast(new int[]{i, j});
                    }
                }
            }
        }
        //已经更新水位线的位点，对四周位点的水位线施加影响
        while (!arrayQueue.isEmpty()) {            //广度优先搜索
            int[] currentPoint = arrayQueue.pollFirst();   //从头或从尾取，不重要，重要的是将里面的取完
            int row = currentPoint[0];             //每个位点会先后被相邻的四个位点更新
            int col = currentPoint[1];
            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (waterMark[nextRow][nextCol] > waterMark[row][col]) {    //当前节点会影响相邻节点的水位线
                        //-----------------------------------------------------
                        // 当前位点的水位线高度取决于相邻位点的水位线高度以及当前位点的高度，两个高度决定了能否积累雨水，如果可以积累雨水，决定了积累的量
                        //    1、waterMark[row][col] < height[nextRow][nextCol] 无法构成积水槽，水位线高度定为原始台阶高度
                        //    2、waterMark[row][col] > height[nextRow][nextCol] 可以构成积水槽，水位线高度定为当前节点的水位线高度
                        // 当前点 [nextRow][nextCol] 比相邻点 [row][col] 高，则更新当前点的水位线
                        //    最终等价于 water[i][j] = max(height[i][j],min(water[i−1][j],water[i+1][j],water[i][j−1],water[i][j+1]))
                        //-----------------------------------------------------
                        waterMark[nextRow][nextCol] = Math.max(height[nextRow][nextCol], waterMark[row][col]);  //思考：height[nextRow][nextCol] 决定了 waterMark[nextRow][nextCol] 的下限
                        arrayQueue.addLast(new int[]{nextRow, nextCol});
                        //-----------------------------------------------------------------
                        // 一个位点接雨水后的高度，不仅仅取决于相邻的四个位点，而可能间接的取决于相邻位点的相邻位点，所以本题中，只要更新了当前位点的高度值，就要添加到队列中，从而对其相邻的位点进行影响
                        // 每次添加到队列中的位点，其节水后的高度并非最终确认值，需要多轮迭代，直至将队列中元素处理完毕，才是最终状态
                        // 我们不断重复的进行调整，直到所有的方块的接水高度不再有调整时即为满足要求
                        //-----------------------------------------------------------------
                    }
                }
            }
        }
        //累加各个位点积累的雨水
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ans += waterMark[i][j] - height[i][j];
            }
        }
        return ans;
    }

    /**
     * 1823. 找出游戏的获胜者
     */
    public int findTheWinner(int n, int k) {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) {
            queue.addLast(i);
        }
        while (queue.size() > 1) {
            for (int i = 1; i < k && queue.size() > 1; i++) {
                queue.addLast(queue.pollFirst());
            }
            queue.pollFirst();  //移除第 K 个人，所以上面应该计数 K - 1
        }
        return queue.peekLast();
    }

    public int findTheWinner01(int n, int k) {  //模拟，循环数组
        int[] ans = new int[n];
        Arrays.fill(ans, 1);
        int currIndex = -1;
        int nums = n;
        while (nums > 1) {
            int times = 0;
            while (times < k) {
                currIndex++;
                currIndex %= n;
                while (ans[currIndex] == 0) {
                    currIndex++;
                    currIndex %= n;
                }
                times++;
            }
            ans[currIndex] = 0;
            nums--;
        }
        for (int i = 0; i < n; i++) {
            if (ans[i] == 1) return i + 1;    // + 1 的原因是，索引号和孩子的编号差 1
        }
        return -1;
    }


    /**
     * 56. 合并区间
     */
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];   //首先按照区间起点升序排序
            return o2[1] - o1[1];                       //其次按照区间终点降序排序
        });
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int[] tuple : intervals) {
            if (queue.isEmpty() || queue.peekLast()[1] < tuple[0]) {
                queue.addLast(tuple);
            } else if (queue.peekLast()[1] < tuple[1]) {
                queue.addLast(new int[]{queue.pollLast()[0], tuple[1]});
            }
        }
        ArrayList<int[]> ans = new ArrayList<>();
        while (!queue.isEmpty()) {
            ans.add(queue.pollFirst());
        }
        return ans.toArray(new int[ans.size()][]);
    }


    public int[][] merge01(int[][] intervals) {
        ArrayList<int[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];  //优先按照起点升序排序
            return o2[1] - o1[1];                      //其次按照终点降序排序，升序降序无所谓
        });
        int currEndTime = intervals[0][1];
        int[] edge = {intervals[0][0], intervals[0][1]};
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= currEndTime) {  //重叠
                edge[0] = Math.min(edge[0], intervals[i][0]);  //多余
                edge[1] = Math.max(edge[1], intervals[i][1]);
            } else {
                ans.add(edge.clone());
                edge[0] = intervals[i][0];
                edge[1] = intervals[i][1];
            }
            currEndTime = Math.max(currEndTime, intervals[i][1]);  //一定取最大值
        }
        ans.add(edge);
        return ans.toArray(new int[ans.size()][]);
    }


    /**
     * 56. 合并区间
     */
    public int[][] merge02(int[][] intervals) {
        ArrayList<int[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];   //按照起点升序排序
            else return o1[1] - o2[1];                  //按照终点升序排序
        });
        int[] edges = intervals[0];
        if (intervals.length == 1) return new int[][]{edges};
        int currEndTime = intervals[0][1];
        int currIndex = 1;
        while (currIndex < intervals.length) {
            while (currIndex < intervals.length && intervals[currIndex][0] <= currEndTime) {
                edges[1] = Math.max(edges[1], intervals[currIndex][1]);
                currEndTime = edges[1];
                currIndex++;
            }
            ans.add(edges.clone());
            if (currIndex < intervals.length) {
                edges = intervals[currIndex];
                currEndTime = edges[1];
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }

    /**
     * 649. Dota2 参议院
     */
    public String predictPartyVictory(String str) {
        int n = str.length();
        ArrayDeque<Character> queue = new ArrayDeque<>();
        int numsR = 0;
        int numsD = 0;
        for (int i = 0; i < n; i++) {
            queue.addLast(str.charAt(i));
            if (str.charAt(i) == 'R') numsR++;
            else numsD++;
        }
        if (numsR == 0) return "Dire";
        if (numsD == 0) return "Radiant";
        int removeR = 0;
        int removeD = 0;
        while (numsD > 0 && numsR > 0 && !queue.isEmpty()) {
            Character curr = queue.pollFirst();
            if (curr == 'R') {
                if (removeR > 0) {        //1、不能选举，无主动权
                    removeR--;                  //抵消前面的禁止票
                    numsR--;                    //此议员后续均不可参与选举
                    if (numsR == 0) return "Dire";
                } else {                  //2、可以选举，有主动权
                    queue.addLast(curr);        //具有选举权，后续仍可选举
                    removeD++;                  //禁止敌对阵营的一位议员的选举权
                }
            }
            if (curr == 'D') {
                if (removeD > 0) {
                    removeD--;
                    numsD--;
                    if (numsD == 0) return "Radiant";
                } else {
                    queue.addLast(curr);
                    removeR++;
                }
            }
        }
        return "";
    }

    //-------------------------------------------------------
    // 上下思路略有不同
    //-------------------------------------------------------

    public String predictPartyVictory01(String senate) {
        ArrayDeque<Character> queue = new ArrayDeque<>();   //记录仍可投票的人员
        int numsR = 0;
        int numsD = 0;
        for (char ch : senate.toCharArray()) {
            queue.addLast(ch);
            numsD += ch == 'D' ? 1 : 0;
            numsR += ch == 'R' ? 1 : 0;
        }
        if (numsD == 0) return "Radiant";
        if (numsR == 0) return "Dire";
        int needR = 0;   //失效 R 的个数
        int needD = 0;   //失效 D 的个数
        while (numsD > 0 && numsR > 0 && !queue.isEmpty()) {
            Character current = queue.pollFirst();
            if (current == 'R') {
                if (needR > 0) needR--;   //当前元素被前方元素置为失效，不可参与投票，故不会再加入队列
                else if (needR == 0) {    //未失效，具有投票的权力
                    numsD--;
                    if (numsD == 0) return "Radiant";
                    needD++;
                    queue.addLast(current);
                }
            }
            if (current == 'D') {
                if (needD > 0) needD--;
                else if (needD == 0) {
                    numsR--;
                    if (numsR == 0) return "Dire";
                    needR++;
                    queue.addLast(current);
                }
            }
        }
        return "";
    }


    /**
     * 1124. 表现良好的最长时间段
     */
    public int longestWPI(int[] hours) {
        int maxWindow = 0;
        int n = hours.length;
        for (int i = 0; i < n; i++) {
            hours[i] = hours[i] > 8 ? 1 : -1;
        }
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + hours[i];
        }

        //-----------------------------------------------------------------------------------
        // 将问题转换为 寻找满足 prefix[j] > prefix[i] 的最大区间（其中 j > i）
        // 其实就是"最长上坡"问题，这个上坡并非在区间内连续上坡，二者右端点 高于 左端点的 整体上坡问题
        //-----------------------------------------------------------------------------------

        //记录左边界
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i <= n; i++) {    //关键左侧堆顶（最大值）为 0
            if (queue.isEmpty() || prefix[queue.peekLast()] > prefix[i]) {  //单调栈，降序排序、记录索引
                queue.addLast(i);
            }
        }
        //-------------------------------------
        // 对比两种写法的差异：
        //     上面固定了堆顶的元素值，下面的呈现波浪形的更新降序排序（一旦遇到一个较大值，甚至会将堆中元素全部清掉）
        //-------------------------------------
//        for (int i = 0; i <= n; i++) {
//            while (!queue.isEmpty() && prefix[queue.peekLast()] < prefix[i]) {  //单调栈，降序排序、记录索引
//                queue.pollLast();
//            }
//            queue.addLast(i);
//        }

        //枚举右边界
        for (int i = n; i >= 0; i--) {  //反向
            if (queue.isEmpty()) break;
            while (!queue.isEmpty() && queue.peekLast() >= i) {
                queue.pollLast();
            }
            while (!queue.isEmpty() && prefix[queue.peekLast()] < prefix[i]) {
                maxWindow = Math.max(maxWindow, i - queue.pollLast());
            }
        }
        return maxWindow;
    }


    public int longestWPI01(int[] hours) {
        int ans = 0;
        int n = hours.length;
        for (int i = 0; i < n; i++) {
            hours[i] = hours[i] > 8 ? 1 : -1;
        }
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + hours[i];
        }

        //1、左边界
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i <= n; i++) {
            if (queue.isEmpty() || prefix[queue.peekLast()] > prefix[i]) {  //单调栈，降序排序、记录索引
                queue.addLast(i);
            }
        }
        //2、右边界
        for (int i = n; i >= 0; i--) {
            //1、合理性
            while (!queue.isEmpty() && queue.peekLast() > i) {
                queue.pollLast();
            }

            //2、计算区间区间长度
            while (!queue.isEmpty() && prefix[queue.peekLast()] < prefix[i]) {
                ans = Math.max(ans, i - queue.pollLast());
            }
        }
        return ans;
    }

    /**
     * 962. 最大宽度坡
     */
    public int maxWidthRamp(int[] nums) {
        int ans = 0;
        int n = nums.length;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (queue.isEmpty() || nums[queue.peekLast()] > nums[i]) {
                queue.addLast(i);
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            while (!queue.isEmpty() && queue.peekLast() >= i) {
                queue.pollLast();
            }
            while (!queue.isEmpty() && nums[queue.peekLast()] <= nums[i]) {
                ans = Math.max(ans, i - queue.pollLast());
            }
        }
        return ans;
    }


    /**
     * 406. 根据身高重建队列
     */
    public int[][] reconstructQueue(int[][] people) {  //贪心思想
        //首先，按数组中第一个元素降序排序，后续向内部插入时，前面的元素均是高度大于此元素
        //其次，按数组中第二个元素升序排序，保证正确性
        Arrays.sort(people, (o1, o2) -> {
            if (o1[0] != o2[0]) return o2[0] - o1[0];  //首先，按照身高降序排序
            return o1[1] - o2[1];                      //其次，按照前方人数升序排序
        });
        LinkedList<int[]> queue = new LinkedList<>();
//        ArrayList<int[]> queue = new ArrayList<>();  //均可
        for (int i = 0; i < people.length; i++) {
            if (queue.size() > people[i][1]) {   //向内插入
                queue.add(people[i][1], people[i]);
            } else {
                queue.addLast(people[i]);        //向后插入
            }
//            queue.add(people[i][1], people[i]);  //与上面逻辑等价
        }
        return queue.toArray(new int[queue.size()][]);
    }


    /**
     * 1053. 交换一次的先前排列
     */
    public int[] prevPermOpt1(int[] arr) {   //基于单调队列
        //---------------------------------------------------------------
        // 贪心：从后往前找到小于 arr[i] 的最大的数 arr[j]，如果存在多个数满足条件，选择最靠左的进行替换
        //---------------------------------------------------------------
        int n = arr.length;
        ArrayDeque<Integer> queue = new ArrayDeque<>();  //队列元素单调递减（沿着遍历方向看，其实也是队尾向队首）
        for (int i = n - 1; i >= 0; i--) {
            //1、元素相同，剔除此元素较大的下标
            while (!queue.isEmpty() && arr[queue.peekLast()] == arr[i]) {
                queue.pollLast();
            }
            //2、维护队列的单调性
            while (!queue.isEmpty() && arr[i] > arr[queue.peekLast()]) {
                Integer index = queue.pollLast();
                if (queue.isEmpty() || arr[i] <= arr[queue.peekLast()]) {  //关键：从小于 arr[i] 的最大值
                    int temp = arr[i];
                    arr[i] = arr[index];
                    arr[index] = temp;
                    return arr;
                }
            }
            queue.addLast(i);
        }
        return arr;
    }

    public int[] prevPermOpt101(int[] arr) {   //基于贪心思想
        //---------------------------------------------------------------
        // 贪心：从后往前找到小于 arr[i] 的最大的数 arr[j]，如果存在多个数满足条件，选择最靠左的进行替换
        //---------------------------------------------------------------
        int n = arr.length;
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {  //从后往前看，非递增的数组，找到首个拐点
                int index = n - 1;
                while (arr[i] <= arr[index] || arr[index] == arr[index - 1]) {
                    index--;
                }
                int temp = arr[i];
                arr[i] = arr[index];
                arr[index] = temp;
                return arr;
            }
        }
        return arr;
    }

    public int[] prevPermOpt102(int[] arr) {    //错误写法，没办法过滤掉相同值的情况，案例 int[] nums1 = {3, 1, 1, 3};
        int n = arr.length;
        int min = arr[n - 1];
        int[] max = new int[n];  //记录当前位置后的最大值
        max[n - 1] = arr[n - 1];
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(arr[n - 1], n - 1);
        for (int i = n - 2; i >= 0; i--) {
            max[i] = Math.max(max[i + 1], arr[i + 1]);
            map.put(arr[i], i);
        }
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > max[i]) {
                Integer index = map.get(max[i]);
                arr[index] = arr[i];
                arr[i] = max[i];
                return arr;
            }
        }
        return arr;
    }

    /**
     * 1019. 链表中的下一个更大节点
     */
    public int[] nextLargerNodes(ListNode head) {
        ArrayList<Integer> arr = new ArrayList<>();
        ListNode curr = head;
        while (curr != null) {
            arr.add(curr.val);
            curr = curr.next;
        }
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        int[] ans = new int[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            while (!queue.isEmpty() && arr.get(queue.peekLast()) < arr.get(i)) {
                ans[queue.pollLast()] = arr.get(i);
            }
            queue.addLast(i);
        }
        return ans;
    }


}
