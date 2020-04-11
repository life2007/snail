package com.hxh.code.test.spi;


import java.util.List;

/**
 * @Auther: hu.xiaohe
 * @Date: 2019/7/11 10:14
 * @Description:
 */
public class FileSearch implements Search {

    @Override
    public List<String> search(String keyword) {
        System.out.println("now use file system search,keyword="+keyword);
        return null;
    }
}
