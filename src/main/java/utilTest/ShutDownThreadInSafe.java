package main.java.utilTest;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: hu.xiaohe
 * @Date: 2018/10/29 10:37
 * @Description:
 */
public class ShutDownThreadInSafe {
    public static void main(String[] args) throws InterruptedException {
        Runner one = new Runner();
        Thread countThread = new Thread(one, "CountThread");
        countThread.start();
        TimeUnit.SECONDS.sleep(2);
        countThread.interrupt();
        Runner two = new Runner();
         countThread = new Thread(two, "CountThread");
         countThread.start();
         TimeUnit.SECONDS.sleep(2);
         two.cancel();
    }

    public static class Runner implements  Runnable{
    private long count;
    private volatile  boolean on = true;
        @Override
        public void run() {
            while(on && !Thread.currentThread().isInterrupted()){
                    count++;
            }
            System.out.println("count = "+count);
        }

        public void cancel(){
            on = false;
        }
    }

}
