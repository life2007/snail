package com.hxh.List;

import com.google.common.base.Preconditions;

/**
 * 单链接反转
 * @Auther: hxh
 * @Date: 2020/4/7 10:42
 * @Description:
 */
public class MyListUtil {
    public static Node reverseList(Node head){
        Node current = head;
        Node pre = null;
        while(current != null){
            Node next = current.next;
            current.next = pre;
            pre = current;
            current = next;
        }
        return pre;
    }

    public static Node reverseBetween(Node head, int m, int n) {
        if(head == null || m==n){
            return head;
        }
        int index=1;
        Node current = head,mNode=null,mPre=null,nNode=null,nNext=null,pre=null;
        while(current!=null){
            if(index <  m){
                pre = current;
                current=current.next;
            }else if(index==m) {
                mNode=current;
                mPre = pre;
                pre = current;
                current=current.next;
            }else if(index <=n){
                if(index==n){
                    nNode =current;
                    nNext = current.next;
                    current.next=pre;
                    if(mPre!=null){
                        mPre.next=nNode;
                    }else{
                        head = nNode;
                    }

                    mNode.next = nNext;

                    break;
                }
                Node next= current.next;

                current.next= pre;
                pre = current;
                current=next;

            }

            index++;


        }

        return head;
    }
}