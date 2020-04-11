package com.hxh.sort;


import java.util.Arrays;

/**
 * @Auther: hxh
 * @Date: 2019/5/13 16:57
 * @Description:
 */
public class HeapSort {
    /**
     * 最大限度利用已排序的序列，减少比较次数
     * 调整堆
     */
    public static void adJustHeap(int[] nums,int parentIndex,int length){
        int temp = nums[parentIndex];
        int childrenIndex = 2 * parentIndex + 1;
        while(childrenIndex < length){
            // 如果有两个子节点，找出子节点中较大的
            if(childrenIndex+1<length && nums[childrenIndex] < nums[childrenIndex+1]){
                childrenIndex++;
            }
            //比较父节点和子节点，如果父节点小于子节点则子节点值覆盖父节点，然后下沉循环判断
            if(temp < nums[childrenIndex]){
                nums[parentIndex] = nums[childrenIndex];
                parentIndex = childrenIndex;
                childrenIndex = 2 * parentIndex + 1;
            }else{
                break;
            }
        }
        // 把父节点的值赋值给最终的父节点
        nums[parentIndex] = temp;

    }

    public  static int compare(int a ,int b){
        return a - b ;
    }

    public static void swap(int[] nums,int a,int b){
        int temp = nums[a];
        nums[a] = nums[b];
        nums[b]=temp;
    }



    public static void print(int[] nums){
        System.out.println(Arrays.toString(nums));
    }

    public static void main(String[] args) {
        int[] nums = {1,2,6,4,5,3,7,8,9,0};
        print(nums);
        int len = nums.length;
        //构造大顶堆,从最后一个非叶子节点开始调整
        for(int i = (len-1)/2;i >=0;i--){
            adJustHeap(nums,i,len);
        }
        print(nums);
        for(int j=len-1;j>0;j--){
            swap(nums,0,j);
            adJustHeap(nums,0,j);
        }
        print(nums);

        int a = 100,b=200;
        a =a^b;
        b=a^b;
        a=a^b;
        System.out.println(a+"---"+b);
    }
}
