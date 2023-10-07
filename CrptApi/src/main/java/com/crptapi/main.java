package com.crptapi;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class main {
    private static Lock lock = new ReentrantLock(false);

    public static void accessMethod() {
        int count = 0;

        lock.lock();
        try {
            for (int i = 0; i < 20; i++) {
                count += 1;
                System.out.println(count);
            }
        } finally {
            lock.unlock();
        }
    }

    public void accessMethodSync() {
        int count = 0;

        synchronized (this) {
            for (int i = 0; i < 20; i++) {
                count += 1;
                System.out.println(count);
            }
        }

    }

    public static void callAccessMethodSync() {
        accessMethod();
    }

    public static void main(String[] args) {
        Caller caller1 = new Caller(1);
        caller1.run();
        Caller caller2 = new Caller(2);
        caller2.run();
        Caller caller3 = new Caller(3);
        caller3.run();
        Caller caller4 = new Caller(4);
        caller4.run();


    }
}
