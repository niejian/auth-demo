package cn.com.authDemo.test.user;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: nj
 * @date: 2018/12/2:下午4:26
 */
public class CountdownLatchTest implements Runnable {
    final AtomicInteger number = new AtomicInteger();
    volatile boolean bol = false;



    @Override
    public void run() {
        System.out.println(number.getAndIncrement());
        synchronized (this) {
            try {
                if (!bol) {
                    System.out.println(bol);
                    bol = true;
                    Thread.sleep(10000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("并发数量为" + number.intValue());
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors. newCachedThreadPool();
        CountdownLatchTest test = new CountdownLatchTest();
        for (int i=0; i<10; i++) {
            pool.execute(test);
        }


    }
}
