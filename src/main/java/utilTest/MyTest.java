package utilTest;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.jmx.remote.internal.ArrayQueue;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.junit.Before;
import org.junit.Test;
import sun.security.util.BitArray;

import javax.script.ScriptEngineManager;
import java.util.*;

/**
 * @Auther: hu.xiaohe
 * @Date: 2018/11/27 10:45
 * @Description:
 */
public class MyTest {
    @Test
    public void test1(){
        List<String> dnList = Lists.newArrayList();
       for(int i=1;i<33;i++){
           dnList.add("device-"+i);
       }
       List<String> typeList = Lists.newArrayList("gatewayVersion","deviceType");

        int maxSize = dnList.size() * typeList.size() * 2;
        //  单次查询最大长度 64
        int maxLen = 2 << 5;
        int size = maxSize > maxLen ? maxLen : maxSize;
        boolean isExceedLimit = maxSize > maxLen ? true : false;

        String[] hashRangKeyValues = new String[size];
        Map<Integer, String[]> keyMap = Maps.newHashMap();
        int len = 0;
        int key = 0;
        // 未超过最大限制
        if (!isExceedLimit) {
            for (int i = 0; i < dnList.size(); i++) {
                for (int j = 0; j < typeList.size(); j++) {
                    hashRangKeyValues[len] = dnList.get(i);
                    hashRangKeyValues[len + 1] = typeList.get(j);
                    len = len + 2;
                }
            }
            keyMap.put(key, hashRangKeyValues);
        } else {
            // 超过最大限制，分批查询
            for (int i = 0; i < dnList.size(); i++) {
                for (int j = 0; j < typeList.size(); j++) {
                    hashRangKeyValues[len] = dnList.get(i);
                    hashRangKeyValues[len + 1] = typeList.get(j);
                    len = len + 2;
                    key++;
                    if (len == maxLen) {
                        keyMap.put(key, hashRangKeyValues);
                        hashRangKeyValues = new String[maxSize - (key * 2) > maxLen ? maxLen : maxSize - (key * 2)];
                        len = 0;
                    }
                }
            }
            keyMap.put(key, hashRangKeyValues);
        }


    }

    @Test
    public void test2(){
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion("cn-nothr-1")
                .withCredentials(new DefaultAWSCredentialsProviderChain()).build();
        DynamoDB docClient = new DynamoDB(dynamoDB);
        Table table = docClient.getTable("life-mytest");
        List<AttributeDefinition> attributeDefinitions = table.getDescription().getAttributeDefinitions();
        dynamoDB.createTable(new CreateTableRequest().withTableName("life-mytest")
                    .withAttributeDefinitions(attributeDefinitions)
                .withBillingMode(BillingMode.PAY_PER_REQUEST)
        );
    }

    @Test
    public void test3(){
        int a = 5;
        int b = 3;
        a=a^b;
        b=a^b;
        a=b^a;
        System.out.println(a+"---"+b);
    }

    // 3个空瓶子换一瓶啤酒，total个空瓶子供换多少个
    public int count(int total){
        int sum = 0;
        while (total>0){
            int divNum = total /3;
            int leftNum = total % 3;
            if(divNum>0){
                sum+=divNum;
                total=divNum+leftNum;
            }else {
                if(leftNum==2){
                    //找老板借一瓶然后凑成3瓶兑换还给老板
                    sum+=1;
                }
                break;
            }
        }
        return sum;
    }

    /**
     * 获取某个数二进制某位的值
     * @param num
     * @param index
     * @return
     */
    public int valueAtBit(int num,int index){
        return (num >> (index-1)) & 1;
    }

    public int setValueAtBit(int num,int index){
        return (1 << index) ^ num;
    }
    //测试 Boolean数组占用空间大小
    @Test
    public void test4(){
       int[] a = {1,2};
        int[] b = {4,5,6};
        System.arraycopy(a,0,b,0,2);
        System.out.println(Arrays.toString(b));
    }

}
