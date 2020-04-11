package com.hxh.code.test;

import org.junit.Test;
import sun.misc.Unsafe;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Auther: hu.xiaohe
 * @Date: 2019/7/10 18:23
 * @Description:
 */
public class CodeTest {

    /**
     * 输入一个整数，输出该数二进制表示中1的个数。其中负数用补码表示
     */
    @Test
    public void test1(){
        AtomicStampedReference<Integer> asr = new AtomicStampedReference(0,1);
    }
}
