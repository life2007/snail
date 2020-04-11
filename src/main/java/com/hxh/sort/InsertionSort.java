package com.hxh.sort;

import java.util.Arrays;

/**
 * @Auther: hxh
 * @Date: 2019/9/18 13:35
 * @Description:
 */
public class InsertionSort {
    /**
     * 插入排序
     * 将待排序数据分为 已排序和未排序两部分，循环未排序数据插入已排序部分
     */


    public static void main(String[] args) {
        int[] nums = {9,8,7,6,5,9};

        insertionSort(nums);
        System.out.println(Arrays.toString(nums));
    }

    public static void insertionSort(int[] nums){
        if(nums == null || nums.length <=0){
            return;
        }
        int len = nums.length;
        for(int i = 1; i < len; i++){
            int j = i;
            int value = nums[j];
            for(;j>0;j--){
                if(nums[j-1]>value){
                    nums[j] = nums[j-1];
                }else{
                    break;
                }
            }
            nums[j]=value;
        }
    }



}
