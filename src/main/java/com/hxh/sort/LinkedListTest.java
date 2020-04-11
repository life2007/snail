package com.hxh.sort;



import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @Auther: hxh
 * @Date: 2019/5/16 09:30
 * @Description:
 */
public class LinkedListTest {

   public static boolean hasCircle(Node node){
        Node fast = node;
        Node slow = node;
        boolean hasCircle = false;
        while(fast !=null && fast.next.next !=null){
           fast = fast.next.next;
           slow = slow.next;
           if(fast.value==slow.value){
               hasCircle = true;
               break;
           }
        }
        return hasCircle;
   }

   /**
    *合并两个有序列表
    */
   public static Node mergeTwoSortedList(Node node1,Node node2){
       if(node1==null || node2==null){
           throw new IllegalArgumentException("invalid param");
       }
       Node first =node1;
       Node second = node2;
       Node joinNode = new Node(-1,null);
       Node rs = joinNode;
       while(first!=null && second != null){
            if(first.getValue() <= second.getValue()){
                joinNode.next=first;
                first = first.next;
            }else{
                joinNode.next=second;
                second = second.next;
            }
            joinNode = joinNode.next ;
       }
       joinNode.next = first!=null?first :second;
        rs = rs.next;
       return rs;
   }


   public static Node reverseNode(Node node){
       Node head = null;
        Node current = node;
        Node next = current.next;
        if(current == null || next == null){
            return node;
        }
       Node oldNext2 = next.next;
       current.next = head;
        head = next;
        next.next = current;
       while(oldNext2 !=null){
           current =  oldNext2;
           next = current.next;
           oldNext2 = next.next;
           current.next = head;
           head = next;
           if(next ==null){
               break;
           }
           next.next = current;

       }
       return head;
   }

    /**
     * 1-->2--->3--->4--->5
     * p1  p2   p3
     * @param node
     * @return
     */
   public static Node reverseNode2(Node node){
        if(node ==null || node.next==null){
            return node;
        }
        Node p1 =node;
        Node p2 = node.next;
        Node p3 = null;
        while(p2 !=null){
            p3 = p2.next;
            p2.next = p1;
            p1 = p2;
            p2 = p3;
        }
        node.next = null;
        return p1;

   }


   public static void print(Node node){
       Node head = node;
       while(head !=null){
               System.out.print(head.value + "-->");

           head = head.next;
       }
       System.out.println();

   }

    public static class Node{
        private int value;
        private Node next;
        public Node(int value,Node node){
            this.next = node;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public static void main(String[] args) {
       Node node1 = new Node(1,null);
       Node node2 = new Node(2,null);
        Node node3 = new Node(3,null);
        Node node4 = new Node(4,null);
        node1.setNext(node2);
        node2.setNext(node3);
        node3.setNext(node4);
       // node4.setNext(node1);
        //System.out.println(hasCircle(node1));
        print(node1);
//        Node reverseNode = reverseNode2(node1);
//        print(reverseNode);
        Node node5= new Node(3,null);
        Node node6= new Node(4,null);
        Node node7= new Node(5,null);
        node5.next = node6;
        node6.next=node7;
        Node node = mergeTwoSortedList(node1, node5);
        print(node);
        ArrayList list;
    }

}
