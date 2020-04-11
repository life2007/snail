package main.java.utilTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 1、确定访问模式
 * 2、定义资源数
 * 3、组合自定义同步器
 * @Auther: hu.xiaohe
 * @Date: 2018/11/5 16:30
 * @Description:
 */
public class TwinsLock implements Lock {
    private final Sync syc = new Sync(2);
    private final static class Sync extends AbstractQueuedSynchronizer{
        Sync(int count){
            if(count <=0){
                throw new IllegalArgumentException("count must large than zero");
            }
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int reductCount) {
            for(;;){
                int current = getState();
                int newCount = current - reductCount;
                if(newCount < 0 || compareAndSetState(current,newCount)){
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int returnCount) {
            for(;;){
                int current = getState();
                int newCount = current +  returnCount;
                if(compareAndSetState(current,newCount)){
                    return true;
                }
            }
        }
    }

    @Override
    public void lock() {
        syc.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        syc.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        final Lock lock = new TwinsLock();
        class worker extends  Thread{
            public void run(){
                while(true){
                    lock.lock();
                    try{
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(Thread.currentThread().getName());
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

        for(int i=0;i<10;i++){
            worker worker = new worker();
            worker.setDaemon(true);
            worker.start();
        }
        for(int i=0;i<10;i++){
            TimeUnit.SECONDS.sleep(1);
            System.out.println();
        }
    }
}
