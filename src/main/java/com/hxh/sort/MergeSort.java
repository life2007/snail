package com.hxh.sort;

import java.util.Arrays;

/**
 * @Auther: hxh
 * @Date: 2019/9/18 13:35
 * @Description:
 */
public class MergeSort {
    /**
     * 归并排序
     * 将待排序数据分为 已排序区间和未排序区间两部分，每次从未排序区间选择最小的数据插入已排序区间末尾
     */


    public static void main(String[] args) {
        int[] nums = {9,8,7,6,5,9};
        mergeSort(nums,0,nums.length-1 );
        System.out.println(Arrays.toString(nums));
    }

    public static void mergeSort(int[] nums,int start,int end){
        if(start>= end){
            return;
        }

        int middle = (start + end) / 2;
        mergeSort(nums, start, middle );
        mergeSort(nums, middle + 1, end);
        // 合并左右两部分数据
        merge(nums, start, middle,middle + 1, end);
    }

    public static void merge(int[] nums,int leftStart,int leftEnd,int rightStart,int rightEnd){
        int[] temp = new int[rightEnd-leftStart+1];
        int i=0;
        int left = leftStart;
        int right = rightStart;
        while(left<=leftEnd &&right<=rightEnd ){
            if(nums[left] < nums[right]){
                temp[i++] = nums[left++];
            }else{
                temp[i++] = nums[right++];
            }
        }
        while(left<=leftEnd){
            temp[i++] = nums[left++];
        }
        while(right<=rightEnd){
            temp[i++] = nums[right++];
        }
//        i=0;
//        for(int k=leftStart;k<=rightEnd; k++){
//            nums[k] = temp [i++];
//        }
        System.arraycopy(temp,0,nums,leftStart,temp.length );
    }

}
