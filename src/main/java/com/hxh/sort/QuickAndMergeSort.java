package com.hxh.sort;


import java.util.Arrays;
import java.util.Collections;

/**
 * @Auther: hxh
 * @Date: 2019/5/13 16:57
 * @Description:
 */
public class QuickAndMergeSort {
    /**
     * 快速排序
     *分治算法，通过一趟排序将要排序的数据分割成独立的两部分：分割点左边都是比它小的数，右边都是比它大的数。
     * 然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列
     * 1、填坑法
     * 2、比较法
     * @param nums
     * @return
     */
    public static void quickSort(int[] nums,int start,int end){
       if(start < end){
           int partition = partition1(nums, start, end);
           quickSort(nums,start,partition);
           quickSort(nums,partition+1,end);
       }
        return ;
    }


    /**
     * 填坑法
     * 将基准数据所在位置作为第一个坑，首先从右向左找到比基准数据小的位置，然后将该位置数据填入坑中，该位置变成新的坑；
     * 然后从左向右找到比基准数据大的位置，将该数据填入坑中，反复执行此过程；
     * 结束条件:左指针大于右指针
     * @param nums
     * @param start
     * @param end
     * @return
     */
    public static int partition1(int[] nums,int start,int end){
        int base = getBase(nums,start, end);
        while(start < end){
            while(start < end && nums[end]>=base){
                end--;
            }
            nums[start]=nums[end];
            while(start <end && nums[start]<=base){
               start++;
            }
            nums[end]=nums[start];

        }
        nums[end] = base;
        return start;
    }

    /**
     * 比较交换法
     * 选择一个基准数据，start、 end、 两个指针，左指针找到比基准数据大的位置，右指针找打比基准数据小的数据，然后交换
     * 结束条件:start end 两个指针相等
     * @param nums
     * @param start
     * @param end
     * @return
     */
    public static int partition2(int[] nums,int start,int end){
        int base = nums[start];
        while(start != end){
            while(start != end && nums[end]>=base){
                end--;
            }
            nums[start]=nums[end];
            while(start !=end && nums[start]<=base){
                start++;
            }
            swap(nums,start,end);

        }
        nums[start] = base;
        return start;
    }


    /**
     * 获取基准数据，获取方式：
     * 从开头、中间、结尾三个数中选取最小的
     * @param nums
     * @param start
     * @param end
     * @return
     */
    public static int getBase(int[] nums,int start,int end){
        int middle = start + (end - start) / 2;
        if(nums[start] > nums[end]){
            swap(nums,start, end);
        }
        if(nums[middle] > nums[end]){
            swap(nums,middle, end);
        }
        if(nums[middle] > nums[start]){
            swap(nums,middle, end);
        }
        return nums[start];
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

            int[] nums = {0,5,9,4,3,1,2};
            print(nums);
        int i = searchNum(nums, 1);
        System.out.println(i);
        quickSort(nums,0,nums.length-1);
        print(nums);
    }

    /**
     * O(n)时间复杂度内求无序数组(无重复)中的第K大元素。比如，4， 2， 5， 12， 3这样一组数据，第3大元素就是4。
     * @param nums
     * @param target
     * @return
     */
    public static int searchNum(int[] nums,int target){
        if(target <0 || target > nums.length-1){
                throw new IllegalArgumentException("invalid param");
        }
        int len = nums.length;
        int partition = parttion4(nums, 0, len - 1);
       while(len-partition !=target){
           if(len - partition > target){
               partition = parttion4(nums, partition+1, len-1);
           }else{
               partition = parttion4(nums, 0, partition);
           }
       }
       return nums[partition];

    }

    public static int parttion4(int[] nums,int start,int end){
        int base = nums[start];
        while(start < end){
            while(start<end && nums[end]>=base){
                end--;
            }
            nums[start] = nums[end];
            while(start < end && nums[start]<=base){
                start++;
            }
            nums[end] = nums[start];
        }
        nums[start] = base;
        return start;
    }
}
