package com.hxh.code.test.disrupter;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: hxh
 * @Date: 2019/10/25 10:16
 * @Description:
 */
public class LogEventMain {
    public static void main(String[] args) throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newCachedThreadPool();
        LogEventFactory factory = new LogEventFactory();
        int buffersize =1024 * 1024;
        Disruptor<LogEvent> disruptor = new Disruptor<LogEvent>(factory, buffersize,executorService, ProducerType.MULTI,new BlockingWaitStrategy());
        disruptor.handleEventsWith(new LogEventHander());
        disruptor.start();
        LogEvenetProducerWithTranslator producer = new LogEvenetProducerWithTranslator(disruptor.getRingBuffer());
        for (long i=0;i<100000;i++){
            producer.produceData(i);
        }
        disruptor.shutdown();
        executorService.shutdown();
        System.out.println("总共耗时："+(System.currentTimeMillis()-beginTime) );
    }
}