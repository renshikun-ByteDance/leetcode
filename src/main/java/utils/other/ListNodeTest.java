package utils.other;

import utils.pojo.ListNode;

import java.util.ArrayList;

/**
 * 链表的创建、插入、替换、删除操作
 * https://www.cnblogs.com/easyidea/p/13371863.html
 */
public class ListNodeTest {
    public static void main(String[] args) {
        ListNode beginNode = new ListNode(0);
        ListNode currentNode = beginNode;     //临时节点，穿针引线的作用
        //1、创建链表
        for (int i = 1; i <= 10; i++) {
            ListNode nextNode = new ListNode(i);
            currentNode.next = nextNode;   //串联
            currentNode = nextNode;        //重新赋值，指向新节点
        }
        //2、遍历链表
        currentNode = beginNode;           //重新赋值，指向新节点
        int times = 0;
        ArrayList<Integer> ans = new ArrayList<>();
        printNodes(currentNode, ans, times);
        System.out.println("....................................");

        //3、插入节点
//        currentNode = beginNode;  //此处本质无需重新赋值，因为 printNodes 函数中的赋值，在此无效，区别上面的 ans/times 在函数内变更是全局的差异
        while (currentNode.next != null) {
            if (currentNode.val == 6) {
                ListNode insertNode = new ListNode(66);
                ListNode nextNext = currentNode.next;
                currentNode.next = insertNode;
                insertNode.next = nextNext;
            }
            currentNode = currentNode.next;
        }
        //2、遍历链表
        currentNode = beginNode;    //需要重新赋值，因为上面对 currentNode 做了变更
        printNodes(currentNode, ans, times);
        System.out.println("....................................");

        //3、替换节点
        while (currentNode.next != null) {
            if (currentNode.val == 3) {   //替换 3 后面的节点
                ListNode replaceNode = new ListNode(99);
                replaceNode.next = currentNode.next.next;
                currentNode.next = replaceNode;
            }
            currentNode = currentNode.next;
        }
        //2、遍历链表
        currentNode = beginNode;    //需要重新赋值，因为上面对 currentNode 做了变更
        printNodes(currentNode, ans, times);
    }

    private static void printNodes(ListNode currentNode, ArrayList<Integer> ans, int times) {
        while (currentNode.next != null) {
            System.out.println("当前节点信息为: " + currentNode.val);
            currentNode = currentNode.next;    //重新赋值，指向新节点
            //---------------------------------------------
            // 以下两个参数仅用于对比 不同变量和操作，对其全局的影响性，此处仅有 ans 全局有效，times/currentNode仅局部有效
            //---------------------------------------------
            times++;
            ans.add(times);
        }
    }
}
