package utils.other;

import utils.pojo.ListNode;

public class MyLinkedList {
    int size;
    ListNode head;

    public MyLinkedList() {
        this.size = 0;
        this.head = new ListNode(0);
    }

    public int get(int index) {
        if (index < 0 || index >= size) {  //索引无效
            return -1;
        }
        ListNode curr = head;
        for (int i = 0; i <= index; i++) {
            curr = curr.next;
        }
        return curr.val;
    }

    public void addAtHead(int val) {
        addAtIndex(0, val);
    }

    public void addAtTail(int val) {
        addAtIndex(size, val);
    }

    public void addAtIndex(int index, int val) {
        if (index > size) {
            return;   //无效索引，无法插入
        }
        index = Math.max(0, index);
        ListNode addNode = new ListNode(val);
        ListNode currNode = head;
        for (int i = 0; i < index; i++) {
            currNode = currNode.next;
        }
        addNode.next = currNode.next;
        currNode.next = addNode;
        size++;
    }

    public void deleteAtIndex(int index) {
        if (index < 0 || index >= size) {
            return;  //无效索引，无法删除
        }
        ListNode currNode = head;
        for (int i = 0; i < index; i++) {
            currNode = currNode.next;   // currNode 最终停留在待删除节点的前一位
        }
        currNode.next = currNode.next.next;
        size--;
    }
}
