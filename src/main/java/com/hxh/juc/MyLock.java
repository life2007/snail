package com.hxh.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Auther: hxh
 * @Date: 2019/11/5 15:20
 * @Description: 独占锁
 */
public class MyLock implements Lock {

    private  static class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int acquacquire) {
            assert acquacquire==1;
            if(compareAndSetState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int releases) {
            assert releases==1;
            if(getState()==0){
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState()==1;
        }
    }

    private final Sync sync= new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        MyLock lock = new MyLock();
        for(int i=0;i<10;i++){
          pool.execute(()->{
                try{
                    System.out.println(Thread.currentThread().getName() + " 尝试获取锁锁....");
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + " 获取锁....");
                }finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + " 释放锁....");
                }

            });
        }
    }
}