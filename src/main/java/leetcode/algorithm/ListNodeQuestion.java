package leetcode.algorithm;

import utils.pojo.ListNode;

import java.util.ArrayList;

/**
 * 链表问题
 * https://www.cnblogs.com/easyidea/p/13371863.html
 */
public class ListNodeQuestion {

    /**
     * 83. 删除排序链表中的重复元素
     */
    public ListNode deleteDuplicates(ListNode head) {   //head = [1,1,2] 输出：[1,2]
        ListNode currentNode = head;  //创建一个节点作为，"引用"初始化
        while (currentNode != null && currentNode.next != null) {   //链表结尾
            if (currentNode.val == currentNode.next.val) {
                currentNode.next = currentNode.next.next;           //此时引用作为了一个桥梁，忽略下一个节点，直接在当前节点和下下个节点间搭建桥梁
            } else {
                currentNode = currentNode.next;                     //引用重新初始化，直接移动至下一个节点，不做桥接
            }
        }
        return head;
    }


    /**
     * 82. 删除排序链表中的重复元素 II
     */
    public ListNode deleteDuplicatesII(ListNode head) {  //head = [1,2,3,3,4,4,5] 输出：[1,2,5]
        if (head == null) {
            return head;
        }
        //创建一个临时的起始节点
        ListNode tempHead = new ListNode(0, head);
        ListNode currentNode = tempHead;
        while (currentNode.next != null && currentNode.next.next != null) {
            if (currentNode.next.val == currentNode.next.next.val) {
                int nums = currentNode.next.val;
                //循环中 currentNode 位置不变，会根据"元素是否存在重复"来更新其指向的下一个节点
                while (currentNode.next != null && currentNode.next.val == nums) {   //类似一个滑动窗口
                    currentNode.next = currentNode.next.next;
                }
            } else {
                //更新 currentNode的位置
                currentNode = currentNode.next;
            }
        }
        return tempHead.next;
    }


    /**
     * 160. 相交链表
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode A = headA;
        ListNode B = headB;
        while (A != B) {
            A = A != null ? A.next : headB;
            B = B != null ? B.next : headA;
        }
        return A;
    }

    /**
     * 2. 两数相加
     */
    public ListNode addTwoNumbers(ListNode listNode1, ListNode listNode2) {
        ListNode ans = new ListNode(0);
        //------------------------------------------------
        // 使用指针指向目标链表的开头，从而实现“穿针引线”
        //------------------------------------------------
        ListNode currentNode = ans;
        int add = 0;
        while (listNode1 != null || listNode2 != null) {
            int x = listNode1 == null ? 0 : listNode1.val;
            int y = listNode2 == null ? 0 : listNode2.val;
            int sum = x + y + add;

            add = sum / 10;
            sum %= 10;
            currentNode.next = new ListNode(sum);
            currentNode = currentNode.next;

            if (listNode1 != null) listNode1 = listNode1.next;
            if (listNode2 != null) listNode2 = listNode2.next;

        }
        if (add == 1) {
            currentNode.next = new ListNode(1);
        }

        return ans.next;
    }

    /**
     * 142. 环形链表 II
     */
    public ListNode detectCycle(ListNode head) {
        //--------------------------------------------------------------------
        // 从 head 到环的入口的长度为 a，环内的长度为 b，所以任意一点走够 a + nb 步，其一定位于 环的入口
        // 本题关键在于构建两次相交
        //    1、首次相交 slow 走了 s 步，fast 走了 2s 步，同时 fast 肯定比 slow 多走了 nb 步，一圈 b 步
        //       所以可以认为首次相交时 slow 走了 s 步或者 nb 步，因此只需要 slow 再走 a 步即可达到 环的入口，那怎么才能让 slow 再走 n步呢
        //    2、构建第二次相交，在首次相遇后，让 fast 重新回到 head 处，并且每次移动一步，与此同时 slow 也是从首次相遇的位置和 fast 每次一步同步伐的移动
        //       二者再次的时候，fast此轮走了 a 步，slow 走了 a + nb 步，且二者一定相遇在环的入口处
        //--------------------------------------------------------------------
        ListNode fast = head;
        ListNode slow = head;
        while (true) {
            if (fast == null || fast.next == null) return null;
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) break;
        }
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }


    /**
     * 24. 两两交换链表中的节点
     */
    public ListNode swapPairs(ListNode head) {
        //---------------------------------------
        // 虚拟头指针
        //---------------------------------------
        ListNode currHead = new ListNode(0, head);
        ListNode currNode = currHead;
        while (currNode.next != null && currNode.next.next != null) {
            //-------------------------------------------------------
            // 两个节点进行位置交换，基于两个指针获取待交换位置的两个节点
            //-------------------------------------------------------
            ListNode left = currNode.next;
            ListNode right = currNode.next.next;

            //-----------------------------------------
            // 三个指针，穿针引线，实现两个节点位置的交换
            //-----------------------------------------
            currNode.next = right;
            left.next = right.next;
            right.next = left;

            currNode = left;  //虚拟指针，重新跳到待交换位置的两个节点的前一个节点位置
        }
        return currHead.next;
    }

    /**
     * 143. 重排链表
     */
    public void reorderList(ListNode head) {
        //-----------------------------------------------------------------------------
        // 由于链表不支持下标访问，所以无法随机访问链表中任意位置的元素
        // 因此可以利用线性表来存储该链表，然后利用线性表下标访问的特点，直接顺序访问指定元素
        //-----------------------------------------------------------------------------
        if (head == null) {
            return;
        }
        ArrayList<ListNode> arr = new ArrayList<>();
        ListNode currNode = head;
        while (currNode != null) {
            arr.add(currNode);
            currNode = currNode.next;
        }
        int left = 0;
        int right = arr.size() - 1;
        while (left < right) {
            arr.get(left).next = arr.get(right);
            left++;
            if (left == right) {
                break;
            }
            arr.get(right).next = arr.get(left); //注意 left 在上面已经移动了一位
            right--;
        }
        arr.get(left).next = null; //断尾
    }

    /**
     * 25. K 个一组翻转链表
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(-1, head);
        ListNode prev = dummy;
        while (true) {
            ListNode last = prev;
            //将 last 向后移动 K 位置
            for (int i = 0; i < k; i++) {
                last = last.next;
                if (last == null) {  //如果不够 K 位，直接返回结果，剩余节点不进行翻转
                    return dummy.next;
                }
                //------------------------------
                // 注意，last 在后续翻转并无作用
                //------------------------------
            }

            //----------------------------------------------------------
            // 每个区间翻转的核心逻辑：
            //    prev "自身"一直指向整个区间开始转移时的前一个节点，作为固定位点用于串联后续节点
            //    curr "自身"一直指向整个区间开始转移时的首个节点，其 "next" 在每轮翻转过程中，一直指向下一位的下一位
            //         curr 的下一位通过 prev 指向当前区间的首个元素，同时 prev 通过其 "next" 指向 curr 的 下一位，从而使其成为整个区间的首个元素
            // 区间翻转完成后，要将 prev 重置为 下一次待翻转区间的前一位元素
            //----------------------------------------------------------
            //翻转当前区间内的 K 个节点
            ListNode curr = prev.next;
            ListNode next;
            for (int i = 0; i < k - 1; i++) {
                next = curr.next;
                curr.next = next.next;
                next.next = prev.next;
                prev.next = next;
            }
            prev = curr;
        }
    }


}
