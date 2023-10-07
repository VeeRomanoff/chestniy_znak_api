package com.crptapi;

public class Caller implements Runnable{
    private final int threadNumber;

    public Caller(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        System.out.println("Inside of thread " + threadNumber);
        main.accessMethod();
    }
}
