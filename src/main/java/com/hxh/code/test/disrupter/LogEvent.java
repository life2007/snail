package com.hxh.code.test.disrupter;
import lombok.*;

import	java.io.Serializable;

/**
 * @Auther: hxh
 * @Date: 2019/10/24 16:05
 * @Description: 事件（event）就是Disruptor进行交换的数据类型
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEvent implements Serializable {
    private Long value;
}