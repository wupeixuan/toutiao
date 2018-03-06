package com.wpx;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程
 */



public class MultiThread {
    public static void testThread(){
        for (int i = 0; i < 10; i++) {
            //new MyThread(i).start();
        }
        for (int i = 0; i < 10; i++) {
            final int tid = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        for (int i = 0; i < 10 ;++i){
                            Thread.sleep(1000);
                            System.out.println(String.format("T2 %d:%d",tid,i));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static Object obj  = new Object();

    static class Producer implements Runnable{

        private BlockingQueue<String> q;
        public Producer(BlockingQueue<String> q){
            this.q = q;
        }
        @Override
        public void run() {
            try{
                for (int i = 0; i < 100 ;++i){
                    Thread.sleep(10);
                    q.put(String.valueOf(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    static class Consumer implements Runnable{
        private BlockingQueue<String> q;
        public Consumer(BlockingQueue<String> q){
            this.q = q;
        }
        @Override
        public void run() {
            while(true){
                try {
                    System.out.println(Thread.currentThread().getName()+":"+q.take());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void sleep(int millis){
        try{
            //Thread.sleep(new Random().nextInt(millis));
            Thread.sleep(millis);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void testWithAtomic(){
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for (int j = 0; j < 10; j++) {
                        System.out.println(atomicInteger.incrementAndGet());
                    }
                }
            }).start();
        }
    }
    public static void testWithoutAtomic(){
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for (int j = 0; j < 10; j++) {
                        System.out.println(counter++);
                    }
                }
            }).start();
        }
    }

    public static void testAtomic(){
        testWithAtomic();
        //testWithoutAtomic();
    }
    public static void testBlockingQueue(){

        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q),"Consumer1").start();
        new Thread(new Consumer(q),"Consumer2").start();
    }

    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userid;

    public static void testThreadLocal(){
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserIds.set(finalI);
                    sleep(1000);
                    System.out.println("ThreadLocal:" + threadLocalUserIds.get());
                }
            }).start();
        }
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userid = finalI;
                    sleep(1000);
                    System.out.println("NonThreadLocal:" + userid);
                }
            }).start();
        }
    }

    public static void testExecutor(){
        //ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    sleep(1000);
                    System.out.println("Execute1 " + i);
                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    sleep(1000);
                    System.out.println("Execute2 " + i);
                }
            }
        });
//        service.submit(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 10; i++) {
//                    sleep(1000);
//                    System.out.println("Execute3 " + i);
//                }
//            }
//        });

        service.shutdown();
        while(!service.isTerminated()){
            sleep(1000);
            System.out.println("wait for termination");
        }
    }

    public static void testFutrue() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(1000);
                //return 1;
                throw new IllegalArgumentException("yichang");
            }
        });
        service.shutdown();

        try{
            System.out.println(future.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] argv){
        testThread();
        //testBlockingQueue();
        //testAtomic();
        //testThreadLocal();
        //testExecutor();
//        testFutrue();
    }
}
