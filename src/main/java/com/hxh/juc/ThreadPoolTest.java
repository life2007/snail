package com.hxh.juc;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: hu.xiaohe
 * @Date: 2019/7/1 13:49
 * @Description:
 */
public class ThreadPoolTest {
    @Test
    public void test1() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            Thread thread = Thread.currentThread();
            System.out.println("Thread["+thread.getName()+"] begine : "+new Date());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread["+thread.getName()+"]    end : "+new Date());
        },1,1, TimeUnit.SECONDS);
        scheduledExecutorService.awaitTermination(1,TimeUnit.MINUTES);

    }

    @Test
    public void test2() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        scheduledExecutorService.scheduleWithFixedDelay(()->{
            Thread thread = Thread.currentThread();
            System.out.println("Thread["+thread.getName()+"] begine : "+new Date());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread["+thread.getName()+"]    end : "+new Date());
        },1,1, TimeUnit.SECONDS);
        scheduledExecutorService.awaitTermination(1,TimeUnit.MINUTES);

    }
}
