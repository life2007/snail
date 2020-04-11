package com.hxh.code.test.disrupter;

import com.lmax.disruptor.EventHandler;

/**
 * @Auther: hxh
 * @Date: 2019/10/25 10:12
 * @Description:
 */
public class LogEventHander implements EventHandler<LogEvent> {
    @Override
    public void onEvent(LogEvent logEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("consumer:"+Thread.currentThread().getName()+"event:value="+logEvent.getValue()+",sequence="+sequence+",endOfBatch="+endOfBatch);
    }
}