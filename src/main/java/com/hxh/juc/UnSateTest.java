package com.hxh.juc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Auther: hxh
 * @Date: 2019/11/28 14:28
 * @Description:
 */
public class UnSateTest {
   private static  Unsafe unsafe = null;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}