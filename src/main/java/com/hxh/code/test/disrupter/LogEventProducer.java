package com.hxh.code.test.disrupter;

import com.lmax.disruptor.RingBuffer;

/**
 * @Auther: hxh
 * @Date: 2019/10/24 16:23
 * @Description:
 */
public class LogEventProducer {
    private final RingBuffer<LogEvent> ringBuffer ;
    public LogEventProducer(RingBuffer<LogEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }
    public void produceData(Long value){
        long sequence = ringBuffer.next();
        try{
            LogEvent logEvent = ringBuffer.get(sequence);
            logEvent.setValue(value);
        }finally {
            ringBuffer.publish(sequence);
        }
    }
}