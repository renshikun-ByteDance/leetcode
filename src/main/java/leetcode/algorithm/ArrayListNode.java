package leetcode.algorithm;


import utils.pojo.ListNode;

/**
 * 链表相关的题目
 */
public class ArrayListNode {


    /**
     * 21. 合并两个有序链表
     */
//    public ListNodeQuestion mergeTwoLists(ListNodeQuestion l1, ListNodeQuestion l2) {
////        if (l1 == null) {
////            return l2;
////        }
////        if (l2 == null) {
////            return l1;
////        }
////        if (l1.val <= l2.val)
//
//
//            if (l1 == null) {
//                return l2;
//            }
//            else if (l2 == null) {
//                return l1;
//            }
//            else if (l1.val < l2.val) {
//                l1.next = mergeTwoLists(l1.next, l2);
//                return l1;
//            }
//            else {
//                l2.next = mergeTwoLists(l1, l2.next);
//                return l2;
//            }
//    }



    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode prehead = new ListNode(-1);

        ListNode prev = prehead;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }

        // 合并后 l1 和 l2 最多只有一个还未被合并完，我们直接将链表末尾指向未合并完的链表即可
        prev.next = l1 == null ? l2 : l1;

        return prehead.next;
    }




}
