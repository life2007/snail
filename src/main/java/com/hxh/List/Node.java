package com.hxh.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Auther: hxh
 * @Date: 2020/4/7 10:40
 * @Description:
 */
@Data
@AllArgsConstructor
@Builder
public class Node<T> {
    public T value;
    public Node<T> next;
}