package com.hxh.code.test.spi;


import java.util.List;

/**
 * @Auther: hu.xiaohe
 * @Date: 2019/7/11 10:15
 * @Description:
 */
public class DBSearch implements Search {

    @Override
    public List<String> search(String keyword) {
        System.out.println("now use DB search,keyword="+keyword);
        return null;
    }
}
