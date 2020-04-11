package com.hxh.code.test.disrupter;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;

/**
 * @Auther: hxh
 * @Date: 2019/10/25 10:00
 * @Description:
 */
public class LogEvenetProducerWithTranslator {
    private final RingBuffer<LogEvent> ringBuffer;
    public LogEvenetProducerWithTranslator(RingBuffer<LogEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public  void produceData(Long value){
        ringBuffer.publishEvent((logEvent,sequene) ->{
            logEvent.setValue(value);
        });
    }

}