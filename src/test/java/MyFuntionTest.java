import com.hxh.List.MyListUtil;
import com.hxh.List.Node;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @Auther: hxh
 * @Date: 2020/4/7 10:46
 * @Description:
 */
public class MyFuntionTest {
    Node head ;
    @Before
    public void init(){
        head = new Node(1,null);
        Node node2 = new Node(2,null);
        Node node3 = new Node(3,null);
        Node node4 = new Node(4,null);
        Node node5 = new Node(5,null);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
    }

    @Test
    public void testReverseList(){
        printList(head);
        Node node = MyListUtil.reverseList(null);
        printList(node);
    }

    @Test
    public void testReverseBetweenList(){
        printList(head);
        Node node = MyListUtil.reverseBetween(head,2,4);
        printList(node);
    }

    @Test
    public void testLettuceCluster(){
        RedisURI uri = RedisURI.builder().withHost("10.100.102.18").withPort(6379).build();
        RedisClusterClient redisClusterClient = RedisClusterClient.create(uri);
        StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect();
        RedisAdvancedClusterCommands<String, String> commands = connection.sync();
        commands.setex("name",10, "throwable");
        String value = commands.get("name");
        System.out.println(value);
    }

    @Test
    public void testLettuce(){
        RedisURI redisUri = RedisURI.builder()                    // <1> 创建单机连接的连接信息
                .withHost("localhost")
                .withPort(6379)
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        RedisClient redisClient = RedisClient.create(redisUri);   // <2> 创建客户端
        StatefulRedisConnection<String, String> connection = redisClient.connect();     // <3> 创建线程安全的连接
        RedisCommands<String, String> redisCommands = connection.sync();                // <4> 创建同步命令
        SetArgs setArgs = SetArgs.Builder.nx().ex(5);
        String result = redisCommands.set("name", "throwable", setArgs);
        result = redisCommands.get("name");
        System.out.println(result);
        // ... 其他操作
        connection.close();   // <5> 关闭连接
        redisClient.shutdown();  // <6> 关闭客户端
    }

    public void printList(Node head){
        System.out.println("----------------------------------------------------");
        Node current = head;
        while (current !=null){
            System.out.print(current.value+"--->");
            current = current.next;
        }
        System.out.print("null");
        System.out.println("");
        System.out.println("----------------------------------------------------");
    }

}