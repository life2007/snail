package com.hxh.code.test.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * SPI的作用，通过配置来动态指定一个接口的实现类
 *
 * @Auther: hu.xiaohe
 * @Date: 2019/7/11 10:17
 * @Description:
 */
public class SPITest {
    public static void main(String[] args) {
        ServiceLoader<Search> serviceLoader = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = serviceLoader.iterator();
       while(iterator.hasNext()){
           Search search = iterator.next();
           search.search("test");
       }
    }
}
