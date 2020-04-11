package com.hxh.sort;

import java.util.Arrays;

/**
 * @Auther: hxh
 * @Date: 2019/9/18 13:35
 * @Description:
 */
public class BubbleSort {
    /**
     * 冒泡排序
     * 每一轮 将 该轮中最大的元素放到最后
     */

    /**
     *交换数组中的两个元素值
     */
    public static void swap(int[] nums,int i,int j){
       nums[i] = nums[i] ^ nums[j];
       nums[j] = nums[i] ^ nums[j];
       nums[i]= nums[i] ^ nums[j];
    }

    public static void main(String[] args) {
      int[] nums = {9,8,7,6,5,9};
        bubbleSort(nums);
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 每轮将最大的数据放到最后
     * @param nums
     */
    public static void bubbleSort(int[] nums){
        if(nums == null || nums.length <=1){
            return;
        }
        //外层循环次数
        for(int i=0;i<nums.length; i++){
            // 每轮循环比较相近的两个数，将该轮最大数像冒泡一样升到后面
            for(int j=1;j<nums.length - i; j++){
                if(nums[j-1]>nums[j]){
                    swap(nums, j, j-1);
                }
            }
        }
    }

}
