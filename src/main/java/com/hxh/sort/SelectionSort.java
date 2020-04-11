package com.hxh.sort;

import java.util.Arrays;

/**
 * @Auther: hxh
 * @Date: 2019/9/18 13:35
 * @Description:
 */
public class SelectionSort {
    /**
     * 选择排序
     * 将待排序数据分为 已排序区间和未排序区间两部分，每次从未排序区间选择最小的数据插入已排序区间末尾
     */

    /**
     *交换数组中的两个元素值
     */
    public static void swap(int[] nums,int i,int j){
        if(i==j){
            return;
        }
       nums[i] = nums[i] ^ nums[j];
       nums[j] = nums[i] ^ nums[j];
       nums[i]= nums[i] ^ nums[j];
    }

    public static void main(String[] args) {
      int[] nums = {9,8,7,6,5,9};
        selectionSort(nums);
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 填坑法，每轮选择一个最小元素依次与坑中元素交换
     * @param nums
     */
    public static void selectionSort(int[] nums){
        if(nums == null || nums.length <=0){
            return;
        }
        //循环所有坑
        for(int i=0;i<nums.length-1; i++){
            //选出该最小的元素与坑中的元素交换
            int min =i;
            for(int j = i+1; j < nums.length; j++){
                if(nums[j]<nums[min]){
                    min = j;
                }
            }
            swap(nums,i,min);
        }
    }



}
