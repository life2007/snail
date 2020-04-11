package com.hxh.sort;

import com.google.common.collect.Maps;
import com.sun.org.apache.xml.internal.utils.Trie;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

/**
 * @Auther: hxh
 * @Date: 2019/5/29 09:22
 * @Description:
 */
public class Recursion {
  public static  Map<Integer,Long> map = new HashMap<Integer,Long>();
    public static long fn(int n){
        long rs = 0;
        if(n>0){
            Long value = map.get(Integer.valueOf(n));
            if(value!=null){
                return value;
            }
            if(n==1){
                rs = 1;
            }else if(n==2){
                rs = 2;
            }else {
                rs = fn(n-1)+fn(n-2);
            }
        }else {
            throw new IllegalArgumentException("n must gre 0");
        }
        map.put(Integer.valueOf(n),Long.valueOf(rs));
        return rs;
    }
    // 调⽤⽅式：
// int[]a = a={1, 2, 3, 4}; printPermutations(a, 4, 4);
// k表示要处理的⼦数组的数据个数
    public static void printPermutations(int[] data, int n, int k) {
        if (k == 1) {
            for (int i = 0; i < n; ++i) {
                System.out.print(data[i] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < k; ++i) {
            int tmp = data[i];
            data[i] = data[k-1];
            data[k-1] = tmp;
           printPermutations(data, n, k - 1);
            tmp = data[i];
            data[i] = data[k-1];
            data[k-1] = tmp;
        }
    }

    public static  int countPermutations(int n){
        int rs = -1;
        if(n==1){
            rs = 1;
        }else {
            rs = n * countPermutations(n-1);
        }
        return rs;
    }

    // a, b分别是主串和模式串；n, m分别是主串和模式串的⻓度。
    public static int kmp(char[] a, int n, char[] b, int m) {
        int[] next = getNexts(b, m);
        int j = 0;
        for (int i = 0; i < n; ++i) {
            while (j > 0 && a[i] != b[j]) { // ⼀直找到a[i]和b[j]
                j = next[j - 1] + 1;
            }
            if (a[i] == b[j]) {
                ++j;
            }
            if (j == m) { // 找到匹配模式串的了
                return i - m + 1;
            }
        }
        return -1;
    }
    private static int[] getNexts(char[] b, int m) {
        int[] next = new int[m];
        next[0] = -1;
        int k = -1;
        for (int i = 1; i < m; ++i) {
            while (k != -1 && b[k + 1] != b[i]) {
                k = next[k];
            }
            if (b[k + 1] == b[i]) {
                ++k;
            }
            next[i] = k;
        }
        return next;
    }


    public static void main(String[] args) {
        String mainStr = "ababaeabac";
        String targetStr = "ababacd";

        char[] str =mainStr .toCharArray();
        char[] target = targetStr.toCharArray();
        int kmp = kmp(str, str.length, target, target.length);
        System.out.println("main str:"+mainStr);
        System.out.println("target str : "+targetStr);
        System.out.println("the index of targetStr in mainStr is :"+kmp);
       if(kmp !=-1){
           System.out.println(mainStr.substring(kmp,kmp+target.length));
       }

        Trie trie;
    }

    @Test
    public void test(){
      for(int i=1;i< 31;i++){
          System.out.println("第 "+i +" 阶共计有 "+ fn(i)+" 种上台阶方法");
      }
    }

}
