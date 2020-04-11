package com.hxh.code.test.disrupter;

import com.lmax.disruptor.EventFactory;

/**
 * @Auther: hxh
 * @Date: 2019/10/24 16:16
 * @Description:事件工厂，生产指定类型的事件，Disruper 通过EventFactory在RingBuffer中
 * 中预创建Event实例，一个Event实例实际上被用作一个 “数据槽”，事件产生前，需先从RingBuffer获取
 * 一个Event实例，然后往Event实例中填充数据，之后再发布到RingBuffer 中，之后由Consumer获取该
 * Event实例并从中读取数据
 */
public class LogEventFactory implements EventFactory<LogEvent> {
    @Override
    public LogEvent newInstance() {
        return LogEvent.builder().build();
    }
}