package com.hxh.sort;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * 假设数据已排序
 *心得：
 *完美基于已有的丑--算法无止境，解决问题后才有机会优化解决方案。
 * @Auther: hxh
 * @Date: 2019/5/13 16:57
 * @Description:
 */
public class BinarySerach {
    /**
     * 无重复数据升序
     * @param nums
     * @param target
     * @return
     */
    public static int binarySearch(int[] nums,int target){
        int rs = -1;
        int low = 0;
        int hight = nums.length -1;
        while(low <=hight){
            int mid = low +((hight-low)>>1);
            if(nums[mid]>target){
                hight = mid - 1;
            }else if(nums[mid]<target){
                low = mid+1;
            }else{
                rs = mid;
                break;
            }
        }
        return rs;
    }

    /**
     * 有重复元素，找到第一个相等数据
     * @param nums
     * @param target
     * @return
     */
    public static int binarySearch1(int[] nums, int target){
       int rs = -1;
        int low = 0;
        int hight = nums.length -1;
        while(low <=hight){
            int mid = low +((hight-low)>>1);
            if(nums[mid]>target){
                hight = mid - 1;
            }else if(nums[mid]<target){
                low = mid+1;
            }else{
                // 目标，判断是不是第一个相等元素
               if(mid==0 || nums[mid-1]!=target){
                   rs = mid;
                   break;
               }
               hight=mid-1;

            }
        }
        return rs;

    }

    /**
     * 有重复元素，找到第一个相等数据，精简版
     * @param nums
     * @param target
     * @return
     */
    public static int binarySearch2(int[] nums, int target){
        int rs = -1;
        int low = 0;
        int len = nums.length-1;
        int hight = len;
        while(low <=hight){
            int mid = low +((hight-low)>>1);
            //1、mid 是第一个相等的元素，则之后的循环均小于target，最终会等于第一次相等的前一个
            if(nums[mid]>=target){
                hight = mid - 1;
            }else if(nums[mid]<target){
                low = mid+1;
            }
        }
        if(low < len && nums[low]==target ){
            rs = low;
        }
        return rs;

    }

    /**
     * 有重复元素，找到最后相等数据
     * @param nums
     * @param target
     * @return
     */
    public static int binarySearch4(int[] nums, int target){
        int rs = -1;
        int low = 0;
        int hight = nums.length -1;
        int len = nums.length;
        while(low <=hight){
            int mid = low +((hight-low)>>1);
            if(nums[mid]>target){
                hight = mid - 1;
            }else if(nums[mid]<target){
                low = mid+1;
            }else{
                // 目标，判断是不是最后一个相等元素
                if(mid==len-1 || nums[mid+1]!=target){
                    rs = mid;
                    break;
                }
                low=mid+1;

            }
        }
        return rs;

    }

    /**
     * 有重复元素，找到最后一个小于等于指定值的数据
     * @param nums
     * @param target
     * @return
     */
    public static int binarySearch5(int[] nums, int target){
        int rs = -1;
        int low = 0;
        int hight = nums.length -1;
        int len = nums.length;
        while(low <=hight){
            int mid = low +((hight-low)>>1);
            if(nums[mid]>target){
               hight = mid-1;
            }else {
                if(mid==len-1 || nums[mid+1]>target){
                    rs = mid;
                    break;
                }else{
                    low= mid+1;
                }
            }
        }
        return rs;

    }

    /**
     * 有重复元素，找到第一个大于等于指定值的数据
     * @param nums
     * @param target
     * @return
     */
    public static int binarySearch6(int[] nums, int target){
        int rs = -1;
        int low = 0;
        int hight = nums.length -1;
        int len = nums.length;
        while(low <=hight){
            int mid = low +((hight-low)>>1);
            if(nums[mid]<target){
                low = mid+1;
            }else {
                if(mid==0 || nums[mid-1]<target){
                    rs = mid;
                    break;
                }else{
                    hight= mid-1;
                }
            }
        }
        return rs;

    }

    /**
     * 查找最后一个小于等于指定值的元素
     * @param nums
     * @param ip
     * @return
     */
    public static long  getIpAddress(long[] nums,long ip){
        int rs = -1;
        int low = 0;
        int hight=nums.length-1;
        while(low <=hight){
            int mid = low+((hight-low)>>1);
           if(nums[mid]>ip){
               hight = mid-1;
           }else{
               if(mid==nums.length-1 || nums[mid+1]>ip ){
                   rs = mid;
                   break;
               }else{
                   hight=mid-1;
               }
           }
        }
        return rs;
    }

    public static void print(int[] nums){
        System.out.println(Arrays.toString(nums));
    }

    public static void main(String[] args) {
//        int[] nums = {0,1,3,5,8,8,8,9,11,12,15};
//        print(nums);
//        int rs = binarySearch6(nums, 15);
//        System.out.println(rs+"---"+(rs>-1?nums[rs]:-1));
        long[] ips = {202102133000L,202102133255L,202102135000L,202102136255L,202102156034L,202102157255L};
        long ipAddress = getIpAddress(ips, 202102135001L);
        System.out.println(ipAddress);

        new LinkedHashMap<>();
    }

    /**
     * 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
     *
     * ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。
     *
     * 搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1 。

     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/search-in-rotated-sorted-array
     *
     */
    @Test
    public void test1(){
        int[] data = {4,5,6,7,0,1,2};
        //int[] data = {5,1,2,3,4};
        int target = 1;
        int index = search1(data, target);
        System.out.println("--index="+index);
        if(index!=-1){
            System.out.println(data[index]+"--res="+(data[index]==target));
        }
     }

    /**
     * logN想到需要二分查找,二分查找精髓在于利用数据已有的有序性，每次通过和中间值的比较后能
     * 跳过一半的无用数据，从而达到logN的时间复杂度;
     * 旋转数组的特点是数据分成两段有序的自数组，且后子数组数据均小于前子数组,
     * 要想使用常规的二分查找，需把旋转有序数组 "还原成" 正常的数组，
     * 数值 1  2  3  4  5  6  7  8  9
     * 下标 0  1  2  3  4  5  6  7  8
     * 旋转后
     * 数值 9  1  2  3  4  5  6  7  8
     *下标  0  1  2  3  4  5  6  7  8
     * 问题的关键是 找到最小的那个数的下标，也就是整体的偏移量
     * 最小数的下标需满足
     *       min< pre 且 min<next
     *
     * 计算出偏移量后，即可找到 旋转数组与 还原的 "正常数组"的元素映射，之后就是使用正常的二分查找即可
     *
     * @param nums
     * @param target
     * @return
     */
    public static int search1(int[] nums, int target) {
       int len = nums.length, left =0,right=len-1,rs=-1,offset=0;
       offset = getOffset(nums);
        System.out.println(offset);
        while(left <=right){
            int midle = left + ((right-left)>>1);
            int midleOffset = (midle+offset)%len;
            if(nums[midleOffset] > target){
                right = midle-1;
            }else if(nums[midleOffset] < target){
                left = midle +1;
            }else{
                rs = midleOffset;
                break;
            }
        }
        return rs;
    }

    /**
     * 获取旋转数组的偏移量
     * @param nums
     * @return
     */
    public static int getOffset(int[] nums){
        int offset = 0,len = nums.length;
        int left =0,right=len-1;
        if(len==1){
            return offset;
        }
        while(left <=right){
            int midle = left + ((right-left)>>1);
            if(nums[midle]>nums[0]){
                left=midle+1;
            }else if(nums[midle]<nums[0]){
                // 此时需注意，不能将right移动到最小值的左边,否则将无法找到最小值
                if(nums[midle-1]>=nums[0]){
                    offset=midle;
                    break;
                }else if(nums[midle-1]<nums[0]){
                    right=midle-1;
                }

            }else{
                if( nums[0]>nums[1]){
                    offset=1;
                }
                break;
            }
        }
        return offset;
    }

}
